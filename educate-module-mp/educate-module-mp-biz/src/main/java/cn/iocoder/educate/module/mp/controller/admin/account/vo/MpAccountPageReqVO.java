package cn.iocoder.educate.module.mp.controller.admin.account.vo;

import cn.iocoder.educate.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/31 11:44
 */
@Schema(description = "管理后台 - 公众号账号分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MpAccountPageReqVO extends PageParam {

    @Schema(name = "公众号名称", description = "模糊匹配")
    private String name;

    @Schema(name = "公众号账号", description = "模糊匹配")
    private String account;

    @Schema(name = "公众号 appid", description = "模糊匹配")
    private String appId;

}