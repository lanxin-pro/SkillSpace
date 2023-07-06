package cn.iocoder.educate.module.system.service.logger;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.api.logger.dto.OperateLogCreateReqDTO;
import cn.iocoder.educate.module.system.controller.admin.logger.vo.OperateLogPageReqVO;
import cn.iocoder.educate.module.system.convert.logger.OperateLogConvert;
import cn.iocoder.educate.module.system.dal.dataobject.logger.OperateLogDO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.educate.module.system.dal.mysql.logger.OperateLogMapper;
import cn.iocoder.educate.module.system.service.user.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/7 15:08
 */
@Service
@Validated
@Slf4j
public class OperateLogServiceImpl implements OperateLogService{

    @Resource
    private OperateLogMapper operateLogMapper;

    @Resource
    private AdminUserService adminUserService;

    @Override
    public void createOperateLog(OperateLogCreateReqDTO createReqDTO) {
        OperateLogDO convertDo = OperateLogConvert.INSTANCE.convert(createReqDTO);
        // 限制字符串长度，如果超过指定长度，截取指定长度并在末尾加"..."
        convertDo.setJavaMethodArgs(StrUtil.maxLength(convertDo.getJavaMethodArgs(),
                OperateLogDO.JAVA_METHOD_ARGS_MAX_LENGTH));
        convertDo.setResultData(StrUtil.maxLength(convertDo.getResultData(),
                OperateLogDO.RESULT_MAX_LENGTH));
        operateLogMapper.insert(convertDo);
    }

    @Override
    public PageResult<OperateLogDO> getOperateLogPage(OperateLogPageReqVO reqVO) {
        // 处理基于用户昵称的查询
        Collection<Long> userIds = null;
        if(StrUtil.isNotEmpty(reqVO.getUserNickname())){
            List<AdminUserDO> adminUserDOList = adminUserService.getUserListByNickname(reqVO.getUserNickname());
            // Set集合 有序id不可重复
            userIds = adminUserDOList
                    .stream()
                    .map(AdminUserDO::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            if(CollUtil.isEmpty(userIds)){
                return PageResult.empty();
            }
        }
        // 查询分页信息
        return operateLogMapper.selectPage(reqVO,userIds);
    }
}
