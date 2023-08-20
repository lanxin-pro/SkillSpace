package cn.iocoder.educate.module.system.controller.admin.errorcode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/20 10:34
 */
@Schema(description = "管理后台 - 错误码创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ErrorCodeCreateReqVO extends ErrorCodeBaseVO {

}
