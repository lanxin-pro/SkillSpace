package cn.iocoder.educate.framework.pay.core.client.impl;

import cn.iocoder.educate.framework.pay.core.client.PayClient;
import cn.iocoder.educate.framework.pay.core.client.PayClientConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付客户端的抽象类，提供模板方法，减少子类的冗余代码
 *
 * @author j-sentinel
 * @date 2024/2/18 14:45
 */
@Slf4j
public abstract class AbstractPayClient<Config extends PayClientConfig> implements PayClient {

    /**
     * 渠道编号
     */
    private final Long channelId;

    /**
     * 渠道编码
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final String channelCode;

    /**
     * 支付配置
     */
    protected Config config;

    public AbstractPayClient(Long channelId, String channelCode, Config config) {
        this.channelId = channelId;
        this.channelCode = channelCode;
        this.config = config;
    }

    /**
     * 自定义初始化
     */
    protected abstract void doInit();

    /**
     * 初始化
     */
    public final void init() {
        doInit();
        log.debug("[init][客户端({}) 初始化完成]", getId());
    }

    /**
     * 刷新配置
     *
     * @param config 配置
     */
    public final void refresh(Config config) {
        // 判断是否更新
        if (config.equals(this.config)) {
            return;
        }
        log.info("[refresh][客户端({})发生变化，重新初始化]", getId());
        this.config = config;
        // 初始化
        this.init();
    }

    @Override
    public Long getId() {
        return channelId;
    }

}
