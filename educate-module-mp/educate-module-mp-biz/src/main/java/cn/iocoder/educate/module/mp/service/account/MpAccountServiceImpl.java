package cn.iocoder.educate.module.mp.service.account;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.account.vo.MpAccountPageReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.educate.module.mp.dal.mysql.account.MpAccountMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 公众号账号 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/8/31 11:41
 */
@Slf4j
@Service
@Validated
public class MpAccountServiceImpl implements MpAccountService {

    /**
     * 账号缓存
     * key：账号编号 {@link MpAccountDO#getAppId()}
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    private volatile Map<String, MpAccountDO> accountCache;

    @Resource
    private MpAccountMapper mpAccountMapper;

    @Override
    @PostConstruct
    public void initLocalCache() {
        List<MpAccountDO> mpAccountDOS = mpAccountMapper.selectList(new LambdaQueryWrapper<>());
        log.info("[initLocalCacheIfUpdate][缓存公众号账号，数量为:{}]", mpAccountDOS.size());

        // 这里需要初始化工厂
        accountCache = mpAccountDOS.stream()
                .collect(Collectors.toMap(MpAccountDO::getAppId, Function.identity(),(v1,v2) -> v1));
    }

    @Override
    public PageResult<MpAccountDO> getAccountPage(MpAccountPageReqVO pageReqVO) {
        return mpAccountMapper.selectPage(pageReqVO);
    }

}
