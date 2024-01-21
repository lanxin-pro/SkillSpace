package cn.iocoder.educate.module.member.dal.mysql.user;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.module.member.dal.dataobject.user.MemberUserDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.List;

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

}
