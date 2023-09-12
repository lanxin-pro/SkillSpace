package cn.iocoder.educate.module.mp.service.material;

import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.material.vo.MpMaterialPageReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.material.MpMaterialDO;
import cn.iocoder.educate.module.mp.dal.mysql.material.MpMaterialMapper;
import cn.iocoder.educate.module.mp.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.mp.framework.mp.core.MpServiceFactory;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
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

    /**
     * 延迟加载，解决循环依赖的问题
     */
    @Resource
    @Lazy
    private MpServiceFactory mpServiceFactory;

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

}
