package cn.iocoder.educate.framework.mybatis.core.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.educate.framework.web.core.util.WebFrameworkUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import java.time.LocalDateTime;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/7 10:12
 * 通用参数填充实现类
 * 如果没有显式的对通用参数进行赋值，这里会对通用参数进行填充、赋值
 */
public class DefaultDBFieldHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        if(ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseDO){
            BaseDO baseDO = (BaseDO) metaObject.getOriginalObject();
            LocalDateTime current = LocalDateTime.now();
            // 创建时间为空，则以当前时间为插入时间
            if(ObjectUtil.isNull(baseDO.getCreateTime())){
                baseDO.setCreateTime(current);
            }
            // 更新时间为空，则以当前时间为更新时间
            if(ObjectUtil.isNull(baseDO.getUpdateTime())){
                baseDO.setUpdateTime(current);
            }
            Long loginUserId = WebFrameworkUtils.getLoginUserId();
            // 当前登录用户不为空，创建人为空，则当前登录用户为创建人
            if(ObjectUtil.isNotNull(loginUserId) && ObjectUtil.isNull(baseDO.getCreator())){
                baseDO.setCreator(loginUserId.toString());
            }
            // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
            if(ObjectUtil.isNotNull(loginUserId) && ObjectUtil.isNull(baseDO.getUpdater())){
                baseDO.setUpdater(loginUserId.toString());
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间为空，则以当前时间为更新时间
        Object updateTime = getFieldValByName("updateTime", metaObject);
        if(ObjectUtil.isNull(updateTime)){
            setFieldValByName("updateTime",LocalDateTime.now(),metaObject);
        }
        // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
        Object updater = getFieldValByName("updater", metaObject);
        Long userId = WebFrameworkUtils.getLoginUserId();
        if(ObjectUtil.isNotNull(userId) && ObjectUtil.isNotNull(updater)){
            setFieldValByName("updater",userId.toString(),metaObject);
        }
    }
}
