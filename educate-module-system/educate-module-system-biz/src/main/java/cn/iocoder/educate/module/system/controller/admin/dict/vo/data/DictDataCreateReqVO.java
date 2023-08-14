package cn.iocoder.educate.module.system.controller.admin.dict.vo.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/14 14:48
 */
@Schema(description = "管理后台 - 字典数据创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class DictDataCreateReqVO extends DictDataBaseVO {
}
