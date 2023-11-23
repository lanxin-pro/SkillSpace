package cn.iocoder.educate.module.member.service.level;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.member.controller.admin.level.vo.level.MemberLevelCreateReqVO;
import cn.iocoder.educate.module.member.controller.admin.level.vo.level.MemberLevelPageReqVO;
import cn.iocoder.educate.module.member.controller.admin.level.vo.level.MemberLevelUpdateReqVO;
import cn.iocoder.educate.module.member.convert.level.MemberLevelConvert;
import cn.iocoder.educate.module.member.dal.dataobject.level.MemberLevelDO;
import cn.iocoder.educate.module.member.dal.mysql.level.MemberLevelMapper;
import cn.iocoder.educate.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.member.enums.ErrorCodeConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.LEVEL_EXPERIENCE_MAX;

/**
 * 会员等级 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/11/22 21:39
 */
@Slf4j
@Service
@Validated
public class MemberLevelServiceImpl implements MemberLevelService {

    @Resource
    private MemberLevelMapper memberLevelMapper;

    @Resource
    private MemberUserService memberUserService;

    @Override
    public Long createLevel(MemberLevelCreateReqVO createReqVO) {
        // 校验配置是否有效
        validateConfigValid(null, createReqVO.getName(), createReqVO.getLevel(), createReqVO.getExperience());

        // 插入
        MemberLevelDO levelDO = MemberLevelConvert.INSTANCE.convert(createReqVO);
        memberLevelMapper.insert(levelDO);
        // 返回
        return levelDO.getId();
    }

    @Override
    public void updateLevel(MemberLevelUpdateReqVO memberLevelUpdateReqVO) {
    }

    @Override
    public void deleteLevel(Long id) {
        // 校验存在
        validateLevelExists(id);
        // 校验分组下是否有用户
        validateLevelHasUser(id);
        // 删除
        memberLevelMapper.deleteById(id);
    }

    @Override
    public MemberLevelDO getLevel(Long id) {
        return memberLevelMapper.selectById(id);
    }

    @Override
    public List<MemberLevelDO> getLevelListByStatus(Integer status) {
        return memberLevelMapper.selectListByStatus(status);
    }

    @Override
    public PageResult<MemberLevelDO> getLevelPage(MemberLevelPageReqVO pageReqVO) {
        return memberLevelMapper.selectPage(pageReqVO);
    }

    private void validateLevelHasUser(Long id) {
        Long count = memberUserService.getUserCountByLevelId(id);
        if (count > 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.LEVEL_HAS_USER);
        }
    }

    private MemberLevelDO validateLevelExists(Long id) {
        MemberLevelDO levelDO = memberLevelMapper.selectById(id);
        if (levelDO == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.LEVEL_NOT_EXISTS);
        }
        return levelDO;
    }


    private void validateConfigValid(Long id, String name, Integer level, Integer experience) {
        List<MemberLevelDO> list = memberLevelMapper.selectList(new LambdaQueryWrapper<>());
        // 校验名称唯一
        validateNameUnique(list, id, name);
        // 校验等级唯一
        validateLevelUnique(list, id, level);
        // 校验升级所需经验是否有效: 大于前一个等级，小于下一个级别
        validateExperienceOutRange(list, id, level, experience);
    }

    private void validateExperienceOutRange(List<MemberLevelDO> list, Long id, Integer level, Integer experience) {
        list.stream().filter(levelDO -> {
            return ObjUtil.equals(levelDO.getId(), id);
        }).findFirst().ifPresent(levelDO -> {
            if (levelDO.getLevel() < level) {
                // 经验大于前一个等级
                if (experience <= levelDO.getExperience()) {
                    throw ServiceExceptionUtil.exception(ErrorCodeConstants.LEVEL_EXPERIENCE_MIN,
                            levelDO.getName(), levelDO.getExperience());
                }
            } else if (levelDO.getLevel() > level) {
                //小于下一个级别
                if (experience >= levelDO.getExperience()) {
                    throw ServiceExceptionUtil.exception(ErrorCodeConstants.LEVEL_EXPERIENCE_MAX,
                            levelDO.getName(), levelDO.getExperience());
                }
            }
        });
    }

    private void validateLevelUnique(List<MemberLevelDO> list, Long id, Integer level) {
        list.stream().filter(levelDO -> {
            // 为false的时候才会保留下来
            return ObjUtil.notEqual(levelDO.getLevel(), level);
        }).findFirst().ifPresent(levelDO -> {
            if (id == null || !id.equals(levelDO.getId())) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.LEVEL_VALUE_EXISTS,
                        levelDO.getLevel(), levelDO.getName());
            }
        });
    }

    void validateNameUnique(List<MemberLevelDO> list, Long id, String name) {
        list.stream().filter(level -> {
            // 为false的时候才会保留下来
            return ObjUtil.notEqual(level.getName(),name);
        }).findFirst().ifPresent(level -> {
            if (id == null || !id.equals(level.getId())) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.LEVEL_NAME_EXISTS, level.getName());
            }
        });
    }

}
