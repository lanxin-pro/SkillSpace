package cn.iocoder.educate.module.member.dal.mysql.point;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.member.controller.admin.point.vo.recrod.MemberPointRecordPageReqVO;
import cn.iocoder.educate.module.member.dal.dataobject.point.MemberPointRecordDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * 用户积分记录 Mapper
 *
 * @author j-sentinel
 * @date 2024/1/19 19:51
 */
@Mapper
public interface MemberPointRecordMapper extends BaseMapper<MemberPointRecordDO> {

    default PageResult<MemberPointRecordDO> selectPage(MemberPointRecordPageReqVO reqVO, Set<Long> userIds) {
        LambdaQueryWrapper<MemberPointRecordDO> memberPointRecordDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        memberPointRecordDOLambdaQueryWrapper.
                in(ObjectUtil.isAllNotEmpty(userIds) && CollectionUtil.isNotEmpty(userIds),
                        MemberPointRecordDO::getUserId, userIds)
                .like(StringUtils.hasText(reqVO.getTitle()),MemberPointRecordDO::getTitle, reqVO.getTitle())
                .eq(ObjectUtil.isNotEmpty(reqVO.getUserId()),MemberPointRecordDO::getUserId , reqVO.getUserId())
                .eq(ObjectUtil.isNotEmpty(reqVO.getBizType()),MemberPointRecordDO::getBizType , reqVO.getBizType())
                .orderByDesc(MemberPointRecordDO::getId);
        Page<MemberPointRecordDO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());
        Page<MemberPointRecordDO> memberPointRecordDOPage = this.selectPage(page, memberPointRecordDOLambdaQueryWrapper);
        return new PageResult<>(memberPointRecordDOPage.getRecords(), memberPointRecordDOPage.getTotal());
    }

}
