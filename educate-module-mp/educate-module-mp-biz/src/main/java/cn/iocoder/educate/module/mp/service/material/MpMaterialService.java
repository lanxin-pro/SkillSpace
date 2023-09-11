package cn.iocoder.educate.module.mp.service.material;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.material.vo.MpMaterialPageReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.material.MpMaterialDO;

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

}
