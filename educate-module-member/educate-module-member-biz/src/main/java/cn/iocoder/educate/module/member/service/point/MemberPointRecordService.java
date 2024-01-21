package cn.iocoder.educate.module.member.service.point;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.member.controller.admin.point.vo.recrod.MemberPointRecordPageReqVO;
import cn.iocoder.educate.module.member.dal.dataobject.point.MemberPointRecordDO;

/**
 * 用户积分记录 Service 接口
 *
 * @author j-sentinel
 * @date 2024/1/19 19:50
 */
public interface MemberPointRecordService {

    /**
     * 【管理员】获得积分记录分页
     *
     * @param memberPointRecordPageReqVO 分页查询
     * @return 签到记录分页
     */
    PageResult<MemberPointRecordDO> getPointRecordPage(MemberPointRecordPageReqVO memberPointRecordPageReqVO);

}
