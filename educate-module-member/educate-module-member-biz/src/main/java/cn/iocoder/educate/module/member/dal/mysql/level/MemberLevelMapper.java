package cn.iocoder.educate.module.member.dal.mysql.level;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageParam;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.member.controller.admin.level.vo.level.MemberLevelPageReqVO;
import cn.iocoder.educate.module.member.dal.dataobject.level.MemberLevelDO;
import cn.iocoder.educate.module.member.dal.dataobject.tag.MemberTagDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 会员等级 Mapper
 *
 * @Author: j-sentinel
 * @Date: 2023/11/22 21:27
 */
@Mapper
public interface MemberLevelMapper extends BaseMapper<MemberLevelDO> {

    default PageResult<MemberLevelDO> selectPage(MemberLevelPageReqVO memberLevelPageReqVO) {
        LambdaQueryWrapper<MemberLevelDO> memberLevelDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 特殊：不分页，直接查询全部
        if (PageParam.PAGE_SIZE_NONE.equals(memberLevelPageReqVO.getPageNo())) {
            List<MemberLevelDO> memberTagList = this.selectList(memberLevelDOLambdaQueryWrapper);
            return new PageResult<>(memberTagList, (long) memberTagList.size());
        }
        memberLevelDOLambdaQueryWrapper.like(StringUtils.hasText(memberLevelPageReqVO.getName()),
                        MemberLevelDO::getName, memberLevelPageReqVO.getName())
                .eq(ObjectUtil.isNotEmpty(memberLevelPageReqVO.getStatus()),
                        MemberLevelDO::getStatus,memberLevelPageReqVO.getStatus())
                .orderByDesc(MemberLevelDO::getLevel);
        IPage<MemberLevelDO> page = new Page<>(memberLevelPageReqVO.getPageNo(), memberLevelPageReqVO.getPageSize());
        IPage<MemberLevelDO> memberLevelDOIPage = this.selectPage(page, memberLevelDOLambdaQueryWrapper);
        return new PageResult<>(memberLevelDOIPage.getRecords(), memberLevelDOIPage.getTotal());
    }

    default List<MemberLevelDO> selectListByStatus(Integer status) {
        LambdaQueryWrapper<MemberLevelDO> memberLevelDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        memberLevelDOLambdaQueryWrapper.eq(MemberLevelDO::getStatus, status);
        return this.selectList(memberLevelDOLambdaQueryWrapper);
    }

}
