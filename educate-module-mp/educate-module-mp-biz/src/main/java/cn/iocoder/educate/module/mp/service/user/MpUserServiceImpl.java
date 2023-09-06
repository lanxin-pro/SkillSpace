package cn.iocoder.educate.module.mp.service.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.user.vo.MpUserPageReqVO;
import cn.iocoder.educate.module.mp.controller.admin.user.vo.MpUserUpdateReqVO;
import cn.iocoder.educate.module.mp.convert.user.MpUserConvert;
import cn.iocoder.educate.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.educate.module.mp.dal.dataobject.user.MpUserDO;
import cn.iocoder.educate.module.mp.dal.mysql.user.MpUserMapper;
import cn.iocoder.educate.module.mp.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.mp.framework.mp.core.MpServiceFactory;
import cn.iocoder.educate.module.mp.service.account.MpAccountService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 微信公众号粉丝 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/9/3 19:29
 */
@Service
@Validated
@Slf4j
public class MpUserServiceImpl implements MpUserService {

    @Resource
    private MpAccountService mpAccountService;

    @Resource
    private MpUserMapper mpUserMapper;

    @Resource
    private MpServiceFactory mpServiceFactory;

    @Override
    public PageResult<MpUserDO> getUserPage(MpUserPageReqVO mpUserPageReqVO) {
        return mpUserMapper.selectPage(mpUserPageReqVO);
    }

    @Override
    public MpUserDO getUser(Long id) {
        return mpUserMapper.selectById(id);
    }

    @Override
    public MpUserDO getUser(String appId, String openId) {
        return mpUserMapper.selectByAppIdAndOpenid(appId, openId);
    }

    @Override
    public void updateUser(MpUserUpdateReqVO updateReqVO) {
        // 校验存在
        MpUserDO user = validateUserExists(updateReqVO.getId());

        // 第一步，更新标签到公众号
        updateUserTag(user.getAppId(), user.getOpenid(), updateReqVO.getTagIds());

        // 第二步，更新基本信息到数据库
        MpUserDO mpUserDO = MpUserConvert.INSTANCE.convert(updateReqVO);
        mpUserDO.setId(user.getId());
        mpUserMapper.updateById(mpUserDO);
    }

    @Override
    public void syncUser(Long accountId) {
        // 获取公众号账号信息
        MpAccountDO account = mpAccountService.getRequiredAccount(accountId);

        // for 循环，避免递归出意外问题，导致死循环
        String nextOpenid = null;
        //
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            log.info("[syncUser][第({}) 次加载公众号粉丝列表，nextOpenid({})]", i, nextOpenid);
            try {
                nextOpenid = syncUser0(account, nextOpenid);
            } catch (WxErrorException e) {
                log.error("[syncUser][第({}) 次同步粉丝异常]", i, e);
                break;
            }
            // 如果 nextOpenid 为空，表示已经同步完毕
            if (StrUtil.isEmpty(nextOpenid)) {
                break;
            }
        }

    }

    private String syncUser0(MpAccountDO account, String nextOpenid) throws WxErrorException {
        // 第一步，从公众号流式加载粉丝
        WxMpService mpService = mpServiceFactory.getRequiredMpService(account.getId());
        WxMpUserList wxUserList = mpService.getUserService().userList(nextOpenid);
        // 关注者列表
        if (CollUtil.isEmpty(wxUserList.getOpenids())) {
            return null;
        }

        // 第二步，分批加载粉丝信息
        // 对集合按照指定长度分段，每一个段为单独的集合，返回这个集合的列表
        List<List<String>> openidsList = CollUtil.split(wxUserList.getOpenids(), 100);
        // 【1-100】【101-200】【201-254】
        for (List<String> openids : openidsList) {
            log.info("[syncUser][批量加载粉丝信息，一共拥有的批次openids({})]", openids);
            // 根据用户的openid获取用户的信息
            List<WxMpUser> wxUsers = mpService.getUserService().userInfoList(openids);
            // 添加
            batchSaveUser(account, wxUsers);
        }

        // 返回下一次的 nextOpenId
        return wxUserList.getNextOpenid();
    }

    /**
     * 添加
     *
     * @param account 公众号账号
     * @param wxUsers 公众号用户的信息
     */
    private void batchSaveUser(MpAccountDO account, List<WxMpUser> wxUsers) {
        if (CollUtil.isEmpty(wxUsers)) {
            return;
        }
        List<String> openIds = wxUsers.stream()
                .map(WxMpUser::getOpenId)
                .collect(Collectors.toList());
        // TODO j-sentinel 这里可以使用hutool工具类来优化
        // 1. 获得数据库已保存的粉丝列表
        List<MpUserDO> dbUsers = mpUserMapper
                .selectListByAppIdAndOpenid(account.getAppId(), openIds);
        Map<String, MpUserDO> openId2Users = dbUsers.stream()
                .collect(Collectors.toMap(MpUserDO::getOpenid, Function.identity(), (v1, v2) -> v1));

        // 2.1 根据情况，插入或更新
        // 将account和微信用户转换为数据库的DO
        List<MpUserDO> users = MpUserConvert.INSTANCE.convertList(account, wxUsers);
        List<MpUserDO> newUsers = new ArrayList<>();
        for (MpUserDO user : users) {
            // 能够从DB数据库钟查询到的
            MpUserDO dbUser = openId2Users.get(user.getOpenid());
            // 新增：稍后批量插入(数据库没有就add)
            if (dbUser == null) {
                newUsers.add(user);
            } else {
                // 更新：直接执行更新
                user.setId(dbUser.getId());
                mpUserMapper.updateById(user);
            }
        }
        // 2.2 批量插入
        if (CollUtil.isNotEmpty(newUsers)) {
            mpUserMapper.insertBatch(newUsers);
        }
    }

    private void updateUserTag(String appId, String openid, List<Long> tagIds) {
        WxMpService mpService = mpServiceFactory.getRequiredMpService(appId);
        try {
            // 第一步，先取消原来的标签
            List<Long> oldTagIds = mpService.getUserTagService().userTagList(openid);
            for (Long tagId : oldTagIds) {
                mpService.getUserTagService().batchUntagging(tagId, new String[]{openid});
            }
            // 第二步，再设置新的标签
            if (CollUtil.isEmpty(tagIds)) {
                return;
            }
            for (Long tagId: tagIds) {
                mpService.getUserTagService().batchTagging(tagId, new String[]{openid});
            }
        } catch (WxErrorException e) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_UPDATE_TAG_FAIL,
                    e.getError().getErrorMsg());
        }
    }

    private MpUserDO validateUserExists(Long id) {
        MpUserDO user = mpUserMapper.selectById(id);
        if (user == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_NOT_EXISTS);
        }
        return user;
    }

}
