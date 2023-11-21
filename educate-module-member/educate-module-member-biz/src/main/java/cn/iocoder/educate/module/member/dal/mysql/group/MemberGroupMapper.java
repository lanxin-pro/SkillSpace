package cn.iocoder.educate.module.member.dal.mysql.group;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageParam;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.member.controller.admin.group.vo.MemberGroupPageReqVO;
import cn.iocoder.educate.module.member.dal.dataobject.group.MemberGroupDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;
import java.util.List;

/**
 * 用户分组 Mapper
 *
 * @Author: j-sentinel
 * @Date: 2023/11/21 23:42
 */
@Mapper
public interface MemberGroupMapper extends BaseMapper<MemberGroupDO> {

    default MemberGroupDO selelctByName(String name){
        LambdaQueryWrapper<MemberGroupDO> memberGroupDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        memberGroupDOLambdaQueryWrapper.eq(MemberGroupDO::getName,name);
        return this.selectOne(memberGroupDOLambdaQueryWrapper);
    }

    default List<MemberGroupDO> selectListByStatus(Integer status){
        LambdaQueryWrapper<MemberGroupDO> memberGroupDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        memberGroupDOLambdaQueryWrapper.eq(MemberGroupDO::getStatus,status);
        return this.selectList(memberGroupDOLambdaQueryWrapper);
    }

    default PageResult<MemberGroupDO> selectPage(MemberGroupPageReqVO memberGroupPageReqVO){
        LambdaQueryWrapper<MemberGroupDO> memberTagDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 特殊：不分页，直接查询全部
        if (PageParam.PAGE_SIZE_NONE.equals(memberGroupPageReqVO.getPageNo())) {
            List<MemberGroupDO> memberTagList = this.selectList(memberTagDOLambdaQueryWrapper);
            return new PageResult<>(memberTagList, (long) memberTagList.size());
        }
        memberTagDOLambdaQueryWrapper.like(StringUtils.hasText(memberGroupPageReqVO.getName()),
                        MemberGroupDO::getName, memberGroupPageReqVO.getName())
                .eq(ObjectUtil.isNotEmpty(memberGroupPageReqVO.getStatus()),
                        MemberGroupDO::getStatus,memberGroupPageReqVO.getStatus())
                .between(null != ArrayUtils.get(memberGroupPageReqVO.getCreateTime(),0) &&
                                ArrayUtils.get(memberGroupPageReqVO.getCreateTime(),1) != null,
                        MemberGroupDO::getCreateTime,
                        ArrayUtils.get(memberGroupPageReqVO.getCreateTime(),0),
                        ArrayUtils.get(memberGroupPageReqVO.getCreateTime(),1))
                .orderByDesc(MemberGroupDO::getId);
        IPage<MemberGroupDO> page = new Page<>(memberGroupPageReqVO.getPageNo(), memberGroupPageReqVO.getPageSize());
        IPage<MemberGroupDO> memberGroupDOIPage = this.selectPage(page, memberTagDOLambdaQueryWrapper);
        return new PageResult<>(memberGroupDOIPage.getRecords(), memberGroupDOIPage.getTotal());
    }

}
