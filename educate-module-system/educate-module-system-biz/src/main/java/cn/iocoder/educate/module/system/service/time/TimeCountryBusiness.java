package cn.iocoder.educate.module.system.service.time;

import cn.iocoder.educate.module.system.controller.admin.time.vo.TimeCountryTimeZoneVO;
import cn.iocoder.educate.module.system.dal.dataobject.time.TimeCountryDO;

import java.sql.Time;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/26 16:31
 */
public interface TimeCountryBusiness {

    /**
     * 获取所有可用的语言
     * @return List
     */
    List<TimeCountryDO> getAllCountry();

    /**
     * 查询国家和时区
     *
     * @return List
     */
    List<TimeCountryTimeZoneVO> getCountryWithTimeZoneList();

}
