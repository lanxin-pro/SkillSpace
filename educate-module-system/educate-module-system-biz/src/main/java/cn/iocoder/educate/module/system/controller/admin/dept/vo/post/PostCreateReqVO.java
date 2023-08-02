package cn.iocoder.educate.module.system.controller.admin.dept.vo.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/2 10:54
 */
@Schema(description = "管理后台 - 岗位创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PostCreateReqVO extends PostBaseVO {
}
