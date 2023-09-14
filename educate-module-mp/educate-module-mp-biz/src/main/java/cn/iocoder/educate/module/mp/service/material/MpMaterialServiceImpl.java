package cn.iocoder.educate.module.mp.service.material;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.infra.api.file.FileApi;
import cn.iocoder.educate.module.mp.controller.admin.material.vo.MpMaterialPageReqVO;
import cn.iocoder.educate.module.mp.controller.admin.material.vo.MpMaterialUploadPermanentReqVO;
import cn.iocoder.educate.module.mp.convert.material.MpMaterialConvert;
import cn.iocoder.educate.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.educate.module.mp.dal.dataobject.material.MpMaterialDO;
import cn.iocoder.educate.module.mp.dal.mysql.material.MpMaterialMapper;
import cn.iocoder.educate.module.mp.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.mp.framework.mp.core.MpServiceFactory;
import cn.iocoder.educate.module.mp.service.account.MpAccountService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 公众号素材 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/9/11 14:53
 */
@Service
@Validated
@Slf4j
public class MpMaterialServiceImpl implements MpMaterialService {

    @Resource
    private MpMaterialMapper mpMaterialMapper;

    @Resource
    private FileApi fileApi;

    /**
     * 延迟加载，解决循环依赖的问题
     */
    @Resource
    @Lazy
    private MpServiceFactory mpServiceFactory;

    /**
     * 延迟加载，解决循环依赖的问题
     */
    @Resource
    @Lazy
    private MpAccountService mpAccountService;

    @Override
    public PageResult<MpMaterialDO> getMaterialPage(MpMaterialPageReqVO pageReqVO) {
        return mpMaterialMapper.selectPage(pageReqVO);
    }

    @Override
    public void deleteMaterial(Long id) {
        MpMaterialDO material = mpMaterialMapper.selectById(id);
        if (material == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MATERIAL_NOT_EXISTS);
        }

        // 第一步，从公众号删除
        if (material.getPermanent()) {
            WxMpService mpService = mpServiceFactory.getRequiredMpService(material.getAppId());
            try {
                mpService.getMaterialService().materialDelete(material.getMediaId());
            } catch (WxErrorException e) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.MATERIAL_DELETE_FAIL,
                        e.getError().getErrorMsg());
            }
        }

        // 第二步，从数据库中删除
        mpMaterialMapper.deleteById(id);
    }

    @Override
    public List<MpMaterialDO> getMaterialListByMediaId(Collection<String> mediaIds) {
        return mpMaterialMapper.selectListByMediaId(mediaIds);
    }

    @Override
    public MpMaterialDO uploadPermanentMaterial(MpMaterialUploadPermanentReqVO reqVO) throws IOException {
        WxMpService mpService = mpServiceFactory.getRequiredMpService(reqVO.getAccountId());
        // 第一步，上传到公众号 如果没有name的话，那么就给定File的类型作为名字
        String name = StrUtil.blankToDefault(reqVO.getName(), reqVO.getFile().getName());
        File file = null;
        WxMpMaterialUploadResult result;
        String mediaId;
        String url;
        try {
            // 写入到临时文件
            file = FileUtil.newFile(FileUtil.getTmpDirPath() + reqVO.getFile().getOriginalFilename());
            reqVO.getFile().transferTo(file);
            // 上传到公众号
            result = mpService.getMaterialService().materialFileUpload(reqVO.getType(),
                    MpMaterialConvert.INSTANCE.convert(name, file, reqVO.getTitle(), reqVO.getIntroduction()));
            // 上传到文件服务
            mediaId = ObjUtil.defaultIfNull(result.getMediaId(), result.getMediaId());
            url = uploadFile(mediaId, file);
        } catch (WxErrorException e) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MATERIAL_UPLOAD_FAIL, e.getError().getErrorMsg());
        } finally {
            FileUtil.del(file);
        }

        // 第二步，存储到数据库
        MpAccountDO account = mpAccountService.getRequiredAccount(reqVO.getAccountId());
        MpMaterialDO material = MpMaterialConvert.INSTANCE.convert(mediaId, reqVO.getType(), url, account,
                name, reqVO.getTitle(), reqVO.getIntroduction(), result.getUrl()).setPermanent(true);
        mpMaterialMapper.insert(material);
        return material;
    }

    private String uploadFile(String mediaId, File file) {
        String path = mediaId + "." + FileTypeUtil.getType(file);
        return fileApi.createFile(path, FileUtil.readBytes(file));
    }


}
