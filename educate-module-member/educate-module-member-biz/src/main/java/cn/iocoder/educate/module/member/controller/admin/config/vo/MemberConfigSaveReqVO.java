package cn.iocoder.educate.module.member.controller.admin.config.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Author: j-sentinel
 * @Date: 2023/11/21 14:36
 */
@Schema(description = "管理后台 - 会员配置保存 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberConfigSaveReqVO extends MemberConfigBaseVO {
}
