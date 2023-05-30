package cn.iocoder.educate.module.system.service.sms;

import cn.iocoder.educate.framework.sms.core.client.SmsClientFactory;
import cn.iocoder.educate.framework.sms.core.properties.SmsChannelProperties;
import cn.iocoder.educate.module.system.convert.sms.SmsChannelConvert;
import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsChannelDO;
import cn.iocoder.educate.module.system.dal.mysql.sms.SmsChannelMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/28 19:12
 */
@Slf4j
@Service
public class SmsChannelServiceImpl implements SmsChannelService{

    @Resource
    private SmsChannelMapper smsChannelMapper;

    @Resource
    private SmsClientFactory smsClientFactory;

    @Getter // 为了方便测试，这里提供 getter 方法
    private volatile Map<String, SmsChannelDO> smsChannelCache;

    @Override
    @PostConstruct
    public void initLocalCache() {
        List<SmsChannelDO> channelDOS = smsChannelMapper.selectList(new QueryWrapper<>());
        log.info("[initLocalCache][缓存短信渠道，数量为:{}]", channelDOS.size());

        List<SmsChannelProperties> smsChannelProperties = SmsChannelConvert.INSTANCE.convertListToProperties(channelDOS);
        smsChannelProperties.forEach(smsChannelProperties1 -> {
            smsClientFactory.createOrUpdateSmsClient(smsChannelProperties1);
        });
    }

    @Override
    public SmsChannelDO getSmsChannel(Long channelId) {
        return smsChannelMapper.selectById(channelId);
    }
}
