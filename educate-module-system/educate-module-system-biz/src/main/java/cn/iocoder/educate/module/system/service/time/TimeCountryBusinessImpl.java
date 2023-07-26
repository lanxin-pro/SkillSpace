package cn.iocoder.educate.module.system.service.time;

import cn.iocoder.educate.module.system.controller.admin.time.vo.TimeCountryTimeZoneVO;
import cn.iocoder.educate.module.system.dal.dataobject.time.TimeCountryDO;
import cn.iocoder.educate.module.system.dal.mysql.time.TimeCountryMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/26 16:31
 */
@Slf4j
@Service
public class TimeCountryBusinessImpl implements TimeCountryBusiness {

    @Resource
    private TimeCountryMapper timeCountryMapper;

    @Override
    public List<TimeCountryTimeZoneVO> getCountryWithTimeZoneList() {
        List<TimeCountryDO> list = getAllCountry();
        return list.stream().map(countryDO -> {
            TimeCountryTimeZoneVO timeCountryTimeZoneVO = new TimeCountryTimeZoneVO();
            timeCountryTimeZoneVO.setId(countryDO.getId());
            timeCountryTimeZoneVO.setCountry(countryDO.getZhName());
            timeCountryTimeZoneVO.setTimeZone(countryDO.getTimeZone());
            return timeCountryTimeZoneVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<TimeCountryDO> getAllCountry() {
        QueryWrapper<TimeCountryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().
                eq(TimeCountryDO::getIsFrozen, false).
                orderByDesc(TimeCountryDO::getSort);
        return timeCountryMapper.selectList(queryWrapper);
    }

}
