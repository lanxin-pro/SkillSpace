package cn.iocoder.educate.module.member.dal.mysql.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.member.controller.admin.user.vo.MemberUserPageReqVO;
import cn.iocoder.educate.module.member.dal.dataobject.user.MemberUserDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 会员 User Mapper
 *
 * @Author: j-sentinel
 * @Date: 2023/12/6 21:18
 */
@Mapper
public interface MemberUserMapper extends BaseMapper<MemberUserDO> {

    default MemberUserDO selectByMobile(String mobile){
        LambdaQueryWrapper<MemberUserDO> memberUserDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        memberUserDOLambdaQueryWrapper.eq(MemberUserDO::getMobile, mobile);
        return this.selectOne(memberUserDOLambdaQueryWrapper);
    }

    default List<MemberUserDO> selectListByNickname(String nickname){
        LambdaQueryWrapper<MemberUserDO> memberUserDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        memberUserDOLambdaQueryWrapper.like(StringUtils.hasText(nickname),
                MemberUserDO::getNickname, nickname);
        return this.selectList(memberUserDOLambdaQueryWrapper);
    }

    default PageResult<MemberUserDO> selectPage(MemberUserPageReqVO reqVO) {
        // SELECT FIND_IN_SET('2', '1,2,3,4,5'); -- 返回 2，因为 '2' 在列表中的第二个位置

        // 处理 tagIds 过滤条件
        String tagIdSql = "";
        if (CollUtil.isNotEmpty(reqVO.getTagIds())) {
            tagIdSql = reqVO.getTagIds().stream()
                    .map(tagId -> "FIND_IN_SET(" + tagId + ", tag_ids)")
                    .collect(Collectors.joining(" OR "));
        }

        LambdaQueryWrapper<MemberUserDO> memberUserDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        memberUserDOLambdaQueryWrapper.like(StringUtils.hasText(reqVO.getMobile()),
                MemberUserDO::getMobile, reqVO.getMobile())
                .between(reqVO.getLoginDate() != null,
                        MemberUserDO::getLoginDate, ArrayUtil.get(reqVO.getLoginDate(), 0),
                        ArrayUtil.get(reqVO.getLoginDate(), 1))
                .like(StringUtils.hasText(reqVO.getNickname()),
                        MemberUserDO::getNickname, reqVO.getNickname())
                .between(reqVO.getCreateTime() != null,
                        MemberUserDO::getCreateTime, ArrayUtil.get(reqVO.getCreateTime(), 0),
                        ArrayUtil.get(reqVO.getCreateTime(), 1))
                .eq(ObjectUtil.isNotEmpty(reqVO.getLevelId()),
                        MemberUserDO::getLevelId, reqVO.getLevelId())
                .eq(ObjectUtil.isNotEmpty(reqVO.getGroupId()),
                        MemberUserDO::getGroupId, reqVO.getGroupId())
                .apply(StrUtil.isNotEmpty(tagIdSql), tagIdSql)
                .orderByDesc(MemberUserDO::getId);
        Page page = new Page<MemberUserDO>(reqVO.getPageNo(), reqVO.getPageSize());
        Page memberUserPage = this.selectPage(page, memberUserDOLambdaQueryWrapper);
        return new PageResult<>(memberUserPage.getRecords(), memberUserPage.getTotal());
    }

}
