package cn.iocoder.educate.module.mp.dal.dataobject.account;

import cn.iocoder.educate.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 公众号账号 DO
 *
 * @Author: j-sentinel
 * @Date: 2023/8/31 11:42
 */
@TableName("mp_account")
@KeySequence("mp_account_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MpAccountDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;

    /**
     * 公众号名称
     */
    private String name;

    /**
     * 公众号账号
     */
    private String account;

    /**
     * 公众号 appid
     */
    private String appId;

    /**
     * 公众号密钥
     */
    private String appSecret;

    /**
     * 公众号token
     */
    private String token;

    /**
     * 消息加解密密钥
     */
    private String aesKey;

    /**
     * 二维码图片 URL
     */
    private String qrCodeUrl;

    /**
     * 备注
     */
    private String remark;

}
