package cn.iocoder.educate.module.mp.framework.mp.core;

import cn.iocoder.educate.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.educate.module.mp.service.handler.message.MessageAutoReplyHandler;
import cn.iocoder.educate.module.mp.service.handler.message.MessageReceiveHandler;
import com.binarywang.spring.starter.wxjava.mp.properties.WxMpProperties;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import me.chanjar.weixin.mp.constant.WxMpEventConstants;

import java.util.List;
import java.util.Map;

/**
 * 默认的 {@link MpServiceFactory} 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/8/31 15:44
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultMpServiceFactory implements MpServiceFactory {

    /**
     * 微信 appId 与 WxMpService 的映射
     */
    private volatile Map<String, WxMpService> appId2MpServices;

    /**
     * 公众号账号 id 与 WxMpService 的映射
     */
    private volatile Map<Long, WxMpService> id2MpServices;

    /**
     * 微信 appId 与 WxMpMessageRouter 的映射
     */
    private volatile Map<String, WxMpMessageRouter> mpMessageRouters;

    private final RedisTemplateWxRedisOps redisTemplateWxRedisOps;

    private final WxMpProperties mpProperties;

    // ========== 各种 Handler ==========

    private final MessageAutoReplyHandler messageAutoReplyHandler;
    private final MessageReceiveHandler messageReceiveHandler;

    @Override
    public void init(List<MpAccountDO> list) {
        // 构建
        Map<String, WxMpService> appId2MpServices = Maps.newHashMap();
        Map<Long, WxMpService> id2MpServices = Maps.newHashMap();
        Map<String, WxMpMessageRouter> mpMessageRouters = Maps.newHashMap();
        // 处理 list
        list.forEach(account -> {
            // 构建 WxMpService 对象
            WxMpService mpService = buildMpService(account);
            appId2MpServices.put(account.getAppId(), mpService);
            id2MpServices.put(account.getId(), mpService);
            // 构建 WxMpMessageRouter 对象
            WxMpMessageRouter mpMessageRouter = buildMpMessageRouter(mpService);
            mpMessageRouters.put(account.getAppId(), mpMessageRouter);
        });

        // 设置到缓存
        this.appId2MpServices = appId2MpServices;
        this.id2MpServices = id2MpServices;
        this.mpMessageRouters = mpMessageRouters;
    }

    @Override
    public WxMpService getMpService(Long id) {
        return id2MpServices.get(id);
    }

    @Override
    public WxMpService getMpService(String appId) {
        return appId2MpServices.get(appId);
    }

    @Override
    public WxMpMessageRouter getMpMessageRouter(String appId) {
        return mpMessageRouters.get(appId);
    }

    private WxMpService buildMpService(MpAccountDO account) {
        // 第一步，创建 WxMpRedisConfigImpl 对象
        WxMpRedisConfigImpl configStorage = new WxMpRedisConfigImpl(
                redisTemplateWxRedisOps, mpProperties.getConfigStorage().getKeyPrefix());
        configStorage.setAppId(account.getAppId());
        configStorage.setSecret(account.getAppSecret());
        configStorage.setToken(account.getToken());
        configStorage.setAesKey(account.getAesKey());

        // 第二步，创建 WxMpService 对象
        WxMpService service = new WxMpServiceImpl();
        service.setWxMpConfigStorage(configStorage);
        return service;
    }

    private WxMpMessageRouter buildMpMessageRouter(WxMpService mpService) {
        WxMpMessageRouter router = new WxMpMessageRouter(mpService);

        // 记录所有事件的日志（异步执行）
        router.rule().async(true).handler(messageReceiveHandler).next();
        // 默认
        router.rule().async(false).handler(messageAutoReplyHandler).end();

        return router;
    }

}
