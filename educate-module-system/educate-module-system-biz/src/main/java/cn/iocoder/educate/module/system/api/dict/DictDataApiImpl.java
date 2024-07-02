package cn.iocoder.educate.module.system.api.dict;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.educate.module.system.api.dict.dto.DictDataRespDTO;
import cn.iocoder.educate.module.system.dal.dataobject.dict.DictDataDO;
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

    @Override
    public DictDataRespDTO getDictData(String type, String value) {
        DictDataDO dictDataDO = dictDataService.getDictData(type, value);
        return BeanUtil.toBean(dictDataDO, DictDataRespDTO.class);
    }
}
