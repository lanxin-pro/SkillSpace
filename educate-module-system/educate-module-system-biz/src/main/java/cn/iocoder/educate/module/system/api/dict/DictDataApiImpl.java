package cn.iocoder.educate.module.system.api.dict;

import cn.iocoder.educate.module.system.service.dict.DictDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author j-sentinel
 * @date 2024/1/19 18:53
 */
@Service
public class DictDataApiImpl implements DictDataApi {

    @Resource
    private DictDataService dictDataService;

}
