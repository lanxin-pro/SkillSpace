package cn.iocoder.educate.module.member.service.config;

import cn.iocoder.educate.framework.common.util.collection.CollectionUtils;
import cn.iocoder.educate.module.member.controller.admin.config.vo.MemberConfigSaveReqVO;
import cn.iocoder.educate.module.member.convert.config.MemberConfigConvert;
import cn.iocoder.educate.module.member.dal.dataobject.config.MemberConfigDO;
import cn.iocoder.educate.module.member.dal.mysql.config.MemberConfigMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

/**
 * 会员配置 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/11/21 14:32
 */
@Slf4j
@Service
@Validated
public class MemberConfigServiceImpl implements MemberConfigService {

    @Resource
    private MemberConfigMapper memberConfigMapper;

    @Override
    public void saveConfig(MemberConfigSaveReqVO saveReqVO) {
        // 存在，则进行更新
        MemberConfigDO dbConfig = getConfig();
        if (dbConfig != null) {
            memberConfigMapper.updateById(MemberConfigConvert.INSTANCE.convert(saveReqVO)
                    .setId(dbConfig.getId())
            );
            return;
        }
        // 不存在，则进行插入
        MemberConfigDO convert = MemberConfigConvert.INSTANCE.convert(saveReqVO);
        memberConfigMapper.insert(convert);
    }

    @Override
    public MemberConfigDO getConfig() {
        List<MemberConfigDO> list = memberConfigMapper.selectList(new LambdaQueryWrapper<>());
        return CollectionUtils.getFirst(list);
    }

}
