package cn.iocoder.educate.module.video.api.enums.live;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 直播间开关状态
 *
 * @Author: j-sentinel
 * @Date: 2023/10/5 20:17
 */
@Getter
@AllArgsConstructor
public enum LiveSwitchStatusEnums {

    LIVE_OPEN(1,"开启直播间"),
    LIVE_CLOSE(0,"关闭直播间"),
    ;

    /**
     * 开关类型
     */
    private Integer type;

    /**
     * 类型描述信息
     */
    private String desc;

}
