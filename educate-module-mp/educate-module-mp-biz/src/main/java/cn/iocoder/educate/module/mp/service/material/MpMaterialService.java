package cn.iocoder.educate.module.mp.service.material;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.material.vo.MpMaterialPageReqVO;
import cn.iocoder.educate.module.mp.controller.admin.material.vo.MpMaterialUploadPermanentReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.material.MpMaterialDO;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 公众号素材 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/9/11 14:53
 */
public interface MpMaterialService {

    /**
     * 获得素材分页
     *
     * @param pageReqVO 分页请求
     * @return 素材分页
     */
    PageResult<MpMaterialDO> getMaterialPage(MpMaterialPageReqVO pageReqVO);

    /**
     * 删除素材
     *
     * @param id 编号
     */
    void deleteMaterial(Long id);

    /**
     * 获得素材列表
     *
     * @param mediaIds 素材 mediaId 列表
     * @return 素材列表
     */
    List<MpMaterialDO> getMaterialListByMediaId(Collection<String> mediaIds);

    /**
     * 上传永久素材
     *
     * @param reqVO 请求
     * @return 素材
     * @throws IOException 文件操作发生异常
     */
    MpMaterialDO uploadPermanentMaterial(@Valid MpMaterialUploadPermanentReqVO reqVO) throws IOException;

}
