package cn.iocoder.educate.module.member.service.level;

import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.member.controller.admin.level.vo.level.MemberLevelCreateReqVO;
import cn.iocoder.educate.module.member.controller.admin.level.vo.level.MemberLevelPageReqVO;
import cn.iocoder.educate.module.member.controller.admin.level.vo.level.MemberLevelUpdateReqVO;
import cn.iocoder.educate.module.member.dal.dataobject.level.MemberLevelDO;
import cn.iocoder.educate.module.member.dal.dataobject.user.MemberUserDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 会员等级 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/11/22 21:38
 */
public interface MemberLevelService {

    /**
     * 创建会员等级
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createLevel(@Valid MemberLevelCreateReqVO createReqVO);

    /**
     * 更新会员等级
     *
     * @param updateReqVO 更新信息
     */
    void updateLevel(@Valid MemberLevelUpdateReqVO updateReqVO);

    /**
     * 删除会员等级
     *
     * @param id 编号
     */
    void deleteLevel(Long id);

    /**
     * 获得会员等级
     *
     * @param id 编号
     * @return 会员等级
     */
    MemberLevelDO getLevel(Long id);

    /**
     * 获得开启状态的会员等级列表
     *
     * @return 会员等级列表
     */
    default List<MemberLevelDO> getEnableLevelList() {
        return getLevelListByStatus(CommonStatusEnum.ENABLE.getStatus());
    }

    /**
     * 获得指定状态的会员等级列表
     *
     * @param status 状态
     * @return 会员等级列表
     */
    List<MemberLevelDO> getLevelListByStatus(Integer status);

    /**
     * 获得会员等级分页
     *
     * @param pageReqVO 分页查询
     * @return 会员标签分页
     */
    PageResult<MemberLevelDO> getLevelPage(MemberLevelPageReqVO pageReqVO);

    /**
     * 获得会员等级列表
     *
     * @param ids 编号
     * @return 会员等级列表
     */
    List<MemberLevelDO> getLevelList(Collection<Long> ids);
}
