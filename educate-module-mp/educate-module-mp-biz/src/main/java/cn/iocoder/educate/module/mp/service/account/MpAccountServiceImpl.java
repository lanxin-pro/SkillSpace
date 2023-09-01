package cn.iocoder.educate.module.mp.service.account;

import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.account.vo.MpAccountPageReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.educate.module.mp.dal.mysql.account.MpAccountMapper;
import cn.iocoder.educate.module.mp.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.mp.framework.mp.core.MpServiceFactory;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
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

    @Resource
    private MpServiceFactory mpServiceFactory;

    @Override
    @PostConstruct
    public void initLocalCache() {
        List<MpAccountDO> mpAccountDOS = mpAccountMapper.selectList(new LambdaQueryWrapper<>());
        log.info("[initLocalCacheIfUpdate][缓存公众号账号，数量为:{}]", mpAccountDOS.size());

        // 第二步：构建缓存。创建或更新支付 Client
        mpServiceFactory.init(mpAccountDOS);

        // 这里需要初始化工厂
        accountCache = mpAccountDOS.stream()
                .collect(Collectors.toMap(MpAccountDO::getAppId, Function.identity(),(v1,v2) -> v1));
    }

    @Override
    public PageResult<MpAccountDO> getAccountPage(MpAccountPageReqVO mpAccountPageReqVO) {
        return mpAccountMapper.selectPage(mpAccountPageReqVO);
    }

    @Override
    public void generateAccountQrCode(Long id) {
        // 校验存在
        MpAccountDO account = validateAccountExists(id);
        // 生成二维码
        WxMpService mpService = mpServiceFactory.getRequiredMpService(account.getAppId());
        String qrCodeUrl;
        try {
            WxMpQrCodeTicket qrCodeTicket = mpService.getQrcodeService().qrCodeCreateLastTicket("default");
            qrCodeUrl = mpService.getQrcodeService().qrCodePictureUrl(qrCodeTicket.getTicket());
        } catch (WxErrorException e) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ACCOUNT_GENERATE_QR_CODE_FAIL,
                    e.getError().getErrorMsg());
        }

        // 保存二维码
        mpAccountMapper.updateById(new MpAccountDO().setId(id).setQrCodeUrl(qrCodeUrl));
    }

    private MpAccountDO validateAccountExists(Long id) {
        MpAccountDO account = mpAccountMapper.selectById(id);
        if (account == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ACCOUNT_NOT_EXISTS);
        }
        return account;
    }

}
