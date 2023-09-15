package cn.iocoder.educate.module.mp.enums.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 公众号消息自动回复的匹配模式
 *
 * @Author: j-sentinel
 * @Date: 2023/9/15 21:19
 */
@Getter
@AllArgsConstructor
public enum MpAutoReplyMatchEnum {

    ALL(1, "完全匹配"),
    LIKE(2, "半匹配"),
    ;

    /**
     * 匹配
     */
    private final Integer match;

    /**
     * 匹配的名字
     */
    private final String name;

}
