package cn.iocoder.educate.module.system.dal.dataobject.permission;

import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.mybatis.core.type.JsonLongSetTypeHandler;
import cn.iocoder.educate.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Set;

/**
 * 角色 DO
 *
 * @Author: j-sentinel
 * @Date: 2023/6/23 10:06
 */
@TableName(value = "system_role", autoResultMap = true)
@KeySequence("system_role_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleDO extends TenantBaseDO {

    /**
     * 角色ID
     */
    @TableId
    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色标识
     *
     * 枚举
     */
    private String code;

    /**
     * 角色排序
     */
    private Integer sort;

    /**
     * 角色状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

    /**
     * 角色类型
     *
     * 枚举 {@link RoleTypeEnum}
     */
    private Integer type;

    /**
     * 备注
     */
    private String remark;

    /**
     * 数据范围
     *
     * 枚举 {@link DataScopeEnum}
     */
    private Integer dataScope;

    /**
     * 数据范围(指定部门数组)
     *
     * 适用于 {@link #dataScope} 的值为 {@link DataScopeEnum#DEPT_CUSTOM} 时
     */
    @TableField(typeHandler = JsonLongSetTypeHandler.class)
    private Set<Long> dataScopeDeptIds;

}
