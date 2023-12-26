package cn.iocoder.educate.module.trade.dal.mysql.cart;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.iocoder.educate.module.trade.dal.dataobject.cart.CartDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: j-sentinel
 * @Date: 2023/12/25 22:36
 */
@Mapper
public interface CartMapper extends BaseMapper<CartDO> {

    default Integer selectSumByUserId(Long userId) {
        QueryWrapper<CartDO> cartDOQueryWrapper = new QueryWrapper<>();
        cartDOQueryWrapper
                .select("SUM(count) AS sumCount")
                .eq("user_id", userId)
                // 只计算选中的
                .eq("selected", true);
        List<Map<String, Object>> result = this.selectMaps(cartDOQueryWrapper);
        // 获得数量
        return CollUtil.getFirst(result) != null ? MapUtil.getInt(result.get(0), "sumCount") : 0;
    }

}
