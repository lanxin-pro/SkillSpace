package cn.iocoder.educate.module.video.api.enums.live;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 视频清晰度 3-360P, 2-480P, 1-720P, 0-1080P
 *
 * @Author: j-sentinel
 * @Date: 2023/10/5 20:18
 */
@Getter
@AllArgsConstructor
public enum SharpnessEnums {

    P360(3, "360P"),
    P480(2, "480P"),
    P720(1, "720P"),
    P1080(0, "1080P")
    ;

    /**
     * 值
     */
    private final Integer value;

    /**
     * 描述
     */
    private final String desc;

}
