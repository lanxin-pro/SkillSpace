package cn.iocoder.educate.module.system.service.dict;

import cn.iocoder.educate.module.system.dal.mysql.dict.DictTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 字典类型 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/7/19 15:14
 */
@Slf4j
@Service
public class DictTypeServiceImpl implements DictTypeService {

    @Resource
    private DictDataService dictDataService;

    @Resource
    private DictTypeMapper dictTypeMapper;

}
