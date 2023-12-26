package cn.iocoder.educate.module.trade.service.cart;

import cn.iocoder.educate.module.trade.dal.mysql.cart.CartMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 购物车 Service 实现类
 *
 * // TODO j-sentinel：未来优化：购物车的价格计算，支持营销信息；目前不支持的原因，前端界面需要前端 pr 支持下；例如说：会员价格；
 *
 * @Author: j-sentinel
 * @Date: 2023/12/25 22:34
 */
@Service
@Validated
public class CartServiceImpl implements CartService {

    @Resource
    private CartMapper cartMapper;

    @Override
    public Integer getCartCount(Long userId) {
        return cartMapper.selectSumByUserId(userId);
    }

}
