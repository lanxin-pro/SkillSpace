package cn.iocoder.educate.module.system.service.sms;

import cn.hutool.core.collection.CollectionUtil;
import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsTemplateDO;
import cn.iocoder.educate.module.system.dal.mysql.sms.SmsTemplateMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.ap.shaded.freemarker.template.utility.CollectionUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 短信模板 Service 接口
 *
 * @Author: 董伟豪
 * @Date: 2023/5/27 20:10
 */
@Slf4j
@Service
public class SmsTemplateServiceImpl implements SmsTemplateService{

    @Resource
    private SmsTemplateMapper smsTemplateMapper;

    /**
     * 短信模板缓存
     * key：短信模板编码 {@link SmsTemplateDO#getCode()}
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     *
     */
    @Getter // 为了方便测试，这里提供 getter 方法
    private volatile Map<String,SmsTemplateDO> smsTemplateCache;

    @Override
    public void initLocalCache() {
        List<SmsTemplateDO> smsTemplateList = smsTemplateMapper.selectList(new QueryWrapper<>());
        log.info("[initLocalCache][缓存短信模版，数量为:{}]", smsTemplateList.size());
        smsTemplateCache = smsTemplateList.stream().collect(Collectors.toMap(SmsTemplateDO::getCode,
                Function.identity(),(oldValue, newValue) -> newValue));
    }

    @Override
    public SmsTemplateDO getSmsTemplateByCodeFromCache(String code) {
        return smsTemplateCache.get(code);
    }

}
