package cn.iocoder.educate.module.pay.service.channel;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.util.json.JsonUtils;
import cn.iocoder.educate.framework.pay.core.client.PayClientConfig;
import cn.iocoder.educate.framework.pay.core.enums.channel.PayChannelEnum;
import cn.iocoder.educate.module.pay.controller.admin.channel.vo.PayChannelCreateReqVO;
import cn.iocoder.educate.module.pay.controller.admin.channel.vo.PayChannelUpdateReqVO;
import cn.iocoder.educate.module.pay.convert.channle.PayChannelConvert;
import cn.iocoder.educate.module.pay.dal.dataobject.channel.PayChannelDO;
import cn.iocoder.educate.module.pay.dal.mysql.channel.PayChannelMapper;
import cn.iocoder.educate.framework.common.util.cache.CacheUtils;
import cn.iocoder.educate.framework.pay.core.client.PayClient;
import cn.iocoder.educate.framework.pay.core.client.PayClientFactory;
import cn.iocoder.yudao.module.pay.enums.ErrorCodeConstants;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.Set;

import static cn.iocoder.yudao.module.pay.enums.ErrorCodeConstants.CHANNEL_NOT_FOUND;

/**
 * 支付渠道 Service 实现类
 *
 * @author j-sentinel
 * @date 2024/2/18 15:04
 */
@Slf4j
@Service
public class PayChannelServiceImpl implements PayChannelService {

    @Resource
    private PayChannelMapper payChannelMapper;

    @Resource
    private PayClientFactory payClientFactory;

    @Getter
    private final LoadingCache<Long, PayClient> clientCache = CacheUtils.buildAsyncReloadingCache(Duration.ofSeconds(10L),
            new CacheLoader<Long, PayClient>() {

                @Override
                public PayClient load(Long id) {
                    // 查询，然后尝试清空
                    PayChannelDO channel = payChannelMapper.selectById(id);
                    if (channel != null) {
                        payClientFactory.createOrUpdatePayClient(channel.getId(), channel.getCode(), channel.getConfig());
                    }
                    return payClientFactory.getPayClient(id);
                }

            });


    @Override
    public List<PayChannelDO> getChannelListByAppIds(Set<Long> appIds) {
        return payChannelMapper.selectListByAppIds(appIds);
    }

    @Override
    public Long createChannel(PayChannelCreateReqVO createReqVO) {
        // 断言是否有重复的
        PayChannelDO dbChannel = getChannelByAppIdAndCode(createReqVO.getAppId(), createReqVO.getCode());
        if (dbChannel != null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CHANNEL_EXIST_SAME_CHANNEL_ERROR);
        }
        // 新增渠道
        PayChannelDO channel = PayChannelConvert.INSTANCE.convert(createReqVO)
                // 解析前端的json，转换为PayClientConfig类型的对象 PayClientConfig -> 有很多子类呐 多态
                .setConfig(parseConfig(createReqVO.getCode(), createReqVO.getConfig()));
        payChannelMapper.insert(channel);
        return channel.getId();
    }

    @Override
    public void updateChannel(PayChannelUpdateReqVO updateReqVO) {
        // 校验存在
        PayChannelDO dbChannel = validateChannelExists(updateReqVO.getId());

        // 更新
        PayChannelDO channel = PayChannelConvert.INSTANCE.convert(updateReqVO)
                .setConfig(parseConfig(dbChannel.getCode(), updateReqVO.getConfig()));
        payChannelMapper.updateById(channel);

        // 清空缓存
        clearCache(channel.getId());
    }

    @Override
    public void deleteChannel(Long id) {
        // 校验存在
        validateChannelExists(id);

        // 删除
        payChannelMapper.deleteById(id);

        // 清空缓存
        clearCache(id);
    }

    @Override
    public PayChannelDO getChannel(Long id) {
        return payChannelMapper.selectById(id);
    }

    @Override
    public PayChannelDO getChannelByAppIdAndCode(Long appId, String code) {
        return payChannelMapper.selectByAppIdAndCode(appId, code);
    }

    @Override
    public List<PayChannelDO> getEnableChannelList(Long appId) {
        return payChannelMapper.selectListByAppId(appId, CommonStatusEnum.ENABLE.getStatus());
    }

    /**
     * 删除缓存
     *
     * @param id 渠道编号
     */
    private void clearCache(Long id) {
        clientCache.invalidate(id);
    }

    private PayChannelDO validateChannelExists(Long id) {
        PayChannelDO channel = payChannelMapper.selectById(id);
        if (channel == null) {
            throw ServiceExceptionUtil.exception(CHANNEL_NOT_FOUND);
        }
        return channel;
    }

    /**
     * 解析并校验配置
     *
     * @param code      渠道编码
     * @param configStr 配置
     * @return 支付配置
     */
    private PayClientConfig parseConfig(String code, String configStr) {
        // 解析配置 -> PayClientConfig payClass = new AlipayPayClientConfig();
        Class<? extends PayClientConfig> payClass = PayChannelEnum.getByCode(code).getConfigClass();
        if (ObjectUtil.isNull(payClass)) {
            throw ServiceExceptionUtil.exception(CHANNEL_NOT_FOUND);
        }
        PayClientConfig config = JsonUtils.parseObject2(configStr, payClass);
        Assert.notNull(config);
        return config;
    }

}
