package cn.iocoder.educate.module.member.controller.admin.point.vo.recrod;

import cn.iocoder.educate.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author j-sentinel
 * @date 2024/1/19 19:45
 */
@Schema(description = "管理后台 - 用户积分记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberPointRecordPageReqVO extends PageParam {

    @Schema(description = "用户昵称", example = "张三")
    private String nickname;

    @Schema(description = "用户编号", example = "123")
    private Long userId;

    /**
     * { @link 参考api中的 MemberPointBizTypeEnum }
     */
    @Schema(description = "业务类型", example = "1")
    private Integer bizType;

    @Schema(description = "积分标题", example = "积分标题")
    private String title;

}
