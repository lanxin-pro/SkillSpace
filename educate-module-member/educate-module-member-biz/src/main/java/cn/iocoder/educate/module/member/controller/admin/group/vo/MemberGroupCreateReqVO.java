package cn.iocoder.educate.module.member.controller.admin.group.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Author: j-sentinel
 * @Date: 2023/11/21 23:31
 */
@Schema(description = "管理后台 - 用户分组创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberGroupCreateReqVO extends MemberGroupBaseVO {
}
