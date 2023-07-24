package cn.iocoder.educate.module.system.api.user.dto;

import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import lombok.Data;

import java.util.Set;

/**
 * Admin 用户 Response DTO
 *
 * @Author: j-sentinel
 * @Date: 2023/7/24 17:29
 */
@Data
public class AdminUserRespDTO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 帐号状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 岗位编号数组
     */
    private Set<Long> postIds;

    /**
     * 手机号码
     */
    private String mobile;

}
