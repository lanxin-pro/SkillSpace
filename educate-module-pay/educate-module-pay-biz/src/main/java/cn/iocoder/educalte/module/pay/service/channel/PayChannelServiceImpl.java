package cn.iocoder.educalte.module.pay.service.channel;

import cn.iocoder.educalte.module.pay.dal.dataobject.channel.PayChannelDO;
import cn.iocoder.educalte.module.pay.dal.mysql.channel.PayChannelMapper;
import cn.iocoder.educate.framework.common.util.cache.CacheUtils;
import cn.iocoder.educate.framework.pay.core.client.PayClient;
import cn.iocoder.educate.framework.pay.core.client.PayClientFactory;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;

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


}
