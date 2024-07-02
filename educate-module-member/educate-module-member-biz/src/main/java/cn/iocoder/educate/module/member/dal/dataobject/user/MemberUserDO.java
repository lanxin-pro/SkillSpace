package cn.iocoder.educate.module.member.dal.dataobject.user;

import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.enums.TerminalEnum;
import cn.iocoder.educate.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.educate.framework.mybatis.core.type.LongListTypeHandler;
import cn.iocoder.educate.module.member.dal.dataobject.group.MemberGroupDO;
import cn.iocoder.educate.module.member.dal.dataobject.level.MemberLevelDO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
  * @Author: j-sentinel
  * @Date: 2023/12/6 20:47
  */
@TableName(value = "member_user", autoResultMap = true)
@KeySequence("member_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUserDO extends BaseDO {

    // ========== 账号信息 ==========

    /**
     * 用户ID
     */
    @TableId
    private Long id;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 加密后的密码
     *
     * 因为目前使用 {@link BCryptPasswordEncoder} 加密器，所以无需自己处理 salt 盐
     */
    private String password;

    /**
     * 帐号状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

    /**
     * 注册 IP
     */
    private String registerIp;

    /**
     * 注册终端
     * 枚举 {@link TerminalEnum}
     */
    private Integer registerTerminal;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private LocalDateTime loginDate;

    // ========== 基础信息 ==========

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 真实名字
     */
    private String name;

    /**
     * 性别
     *
     * 枚举 {@link SexEnum}
     */
    private Integer sex;

    /**
     * 出生日期
     */
    private LocalDateTime birthday;

    /**
     * 所在地
     *
     * 关联 {@link Area#getId()} 字段
     */
    private Integer areaId;

    /**
     * 用户备注
     */
    private String mark;

    // ========== 其它信息 ==========

    /**
     * 积分
     */
    private Integer point;
    // TODO 疯狂：增加一个 totalPoint；个人信息接口要返回

    /**
     * 会员标签列表，以逗号分隔
     */
    @TableField(typeHandler = LongListTypeHandler.class)
    private List<Long> tagIds;

    /**
     * 会员级别编号
     *
     * 关联 {@link MemberLevelDO#getId()} 字段
     */
    private Long levelId;

    /**
     * 会员经验
     */
    private Integer experience;

    /**
     * 用户分组编号
     *
     * 关联 {@link MemberGroupDO#getId()} 字段
     */
    private Long groupId;

}
