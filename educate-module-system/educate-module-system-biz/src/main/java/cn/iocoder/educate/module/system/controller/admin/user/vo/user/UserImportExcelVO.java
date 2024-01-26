package cn.iocoder.educate.module.system.controller.admin.user.vo.user;

import cn.iocoder.educate.framework.excel.core.annotations.DictFormat;
import cn.iocoder.educate.framework.excel.core.covert.DictConvert;
import cn.iocoder.educate.module.system.enums.DictTypeConstants;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户 Excel 导入 VO
 *
 * @author j-sentinel
 * @date 2024/1/26 12:02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true) // 设置 chain = false，避免用户导入有问题 即为@Data默认形式
public class UserImportExcelVO {

    @ExcelProperty("登录名称")
    private String username;

    @ExcelProperty("用户名称")
    private String nickname;

    @ExcelProperty("部门编号")
    private Long deptId;

    @ExcelProperty("用户邮箱")
    private String email;

    @ExcelProperty("手机号码")
    private String mobile;

    @ExcelProperty(value = "用户性别", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.USER_SEX)
    private Integer sex;

    @ExcelProperty(value = "账号状态", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.COMMON_STATUS)
    private Integer status;

}
