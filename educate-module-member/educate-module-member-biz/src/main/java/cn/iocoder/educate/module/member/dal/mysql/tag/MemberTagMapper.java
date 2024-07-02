package cn.iocoder.educate.module.member.dal.mysql.tag;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.educate.framework.common.pojo.PageParam;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.member.controller.admin.tag.vo.MemberTagPageReqVO;
import cn.iocoder.educate.module.member.dal.dataobject.tag.MemberTagDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/11/21 17:58
 */
@Mapper
public interface MemberTagMapper extends BaseMapper<MemberTagDO> {

    default MemberTagDO selelctByName(String name){
        LambdaQueryWrapper<MemberTagDO> memberTagDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        memberTagDOLambdaQueryWrapper.eq(MemberTagDO::getName,name);
        return this.selectOne(memberTagDOLambdaQueryWrapper);
    }

    default PageResult<MemberTagDO> selectPage(MemberTagPageReqVO memberTagPageReqVO){
        LambdaQueryWrapper<MemberTagDO> memberTagDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 特殊：不分页，直接查询全部
        if (PageParam.PAGE_SIZE_NONE.equals(memberTagPageReqVO.getPageNo())) {
            List<MemberTagDO> memberTagList = this.selectList(memberTagDOLambdaQueryWrapper);
            return new PageResult<>(memberTagList, (long) memberTagList.size());
        }
        memberTagDOLambdaQueryWrapper.like(StringUtils.hasText(memberTagPageReqVO.getName()),
                MemberTagDO::getName, memberTagPageReqVO.getName())
                .between(null != ArrayUtils.get(memberTagPageReqVO.getCreateTime(),0) &&
                                ArrayUtils.get(memberTagPageReqVO.getCreateTime(),1) != null,
                        MemberTagDO::getCreateTime,
                        ArrayUtils.get(memberTagPageReqVO.getCreateTime(),0),
                        ArrayUtils.get(memberTagPageReqVO.getCreateTime(),1))
                .orderByDesc(MemberTagDO::getId);
        IPage<MemberTagDO> page = new Page<>(memberTagPageReqVO.getPageNo(), memberTagPageReqVO.getPageSize());
        IPage<MemberTagDO> memberTagDOIPage = this.selectPage(page, memberTagDOLambdaQueryWrapper);
        return new PageResult<>(memberTagDOIPage.getRecords(), memberTagDOIPage.getTotal());
    }

}
