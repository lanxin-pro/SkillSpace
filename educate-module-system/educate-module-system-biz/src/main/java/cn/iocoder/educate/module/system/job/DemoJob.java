package cn.iocoder.educate.module.system.job;

import cn.iocoder.educate.framework.quartz.core.handler.JobHandler;
import cn.iocoder.educate.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.educate.module.system.dal.mysql.user.AdminUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/18 18:46
 */
@Component
public class DemoJob implements JobHandler {

    @Resource
    private AdminUserMapper adminUserMapper;

    @Override
    public String execute(String param) throws Exception {
        System.out.println("当前租户：" + TenantContextHolder.getTenantId());
        List<AdminUserDO> users = adminUserMapper.selectList(new LambdaQueryWrapper<>());
        return "用户数量：" + users.size();
    }

}
