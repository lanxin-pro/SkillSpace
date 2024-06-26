package cn.iocoder.educate.framework.sms.core.client.dto;

import cn.iocoder.educate.framework.sms.core.enums.SmsTemplateAuditStatusEnum;
import lombok.Data;

/**
 * 短信模板 Response DTO
 *
 * @Author: j-sentinel
 * @Date: 2023/8/11 16:37
 */
@Data
public class SmsTemplateRespDTO {

    /**
     * 模板编号
     */
    private String id;

    /**
     * 短信内容
     */
    private String content;

    /**
     * 审核状态
     *
     * 枚举 {@link SmsTemplateAuditStatusEnum}
     */
    private Integer auditStatus;

    /**
     * 审核未通过的理由
     */
    private String auditReason;

}
