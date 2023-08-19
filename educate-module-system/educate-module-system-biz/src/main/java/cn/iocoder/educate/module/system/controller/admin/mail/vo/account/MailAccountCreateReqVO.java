package cn.iocoder.educate.module.system.controller.admin.mail.vo.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/18 13:33
 */
@Schema(description = "管理后台 - 邮箱账号创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MailAccountCreateReqVO extends MailAccountBaseVO {

}
