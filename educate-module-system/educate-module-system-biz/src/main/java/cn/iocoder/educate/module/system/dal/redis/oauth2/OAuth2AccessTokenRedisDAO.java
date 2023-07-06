package cn.iocoder.educate.module.system.dal.redis.oauth2;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONUtil;
import cn.iocoder.educate.framework.common.util.json.JsonUtils;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 *
 * {@link OAuth2AccessTokenDO} 的 RedisDAO
 *
 * @Author: j-sentinel
 * @Date: 2023/5/16 11:36
 */
@Repository
public class OAuth2AccessTokenRedisDAO {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void set(OAuth2AccessTokenDO accessTokenDO) {
        String redisKey = accessTokenDO.getAccessToken();
        // 清理多余字段，避免缓存
        accessTokenDO.setUpdater(null).setUpdateTime(null).setCreateTime(null).setCreator(null).setDeleted(null);
        long time = LocalDateTimeUtil.between(LocalDateTime.now(), accessTokenDO.getExpiresTime(), ChronoUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(redisKey, JsonUtils.toJsonString(accessTokenDO), time, TimeUnit.SECONDS);
    }

    public OAuth2AccessTokenDO get(String accessToken) {
        return JsonUtils.parseObject(stringRedisTemplate.opsForValue().get(accessToken),OAuth2AccessTokenDO.class);
    }

    public void delete(String accessToken) {
        String redisKey = formatKey(accessToken);
        stringRedisTemplate.delete(redisKey);
    }

    private static String formatKey(String accessToken) {
        return String.format("oauth2_access_token:%s", accessToken);
    }
}
