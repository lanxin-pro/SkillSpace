package cn.iocoder.educate.module.video.api.enums.live;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 直播类型 1直播 2伪直播 3无人直播
 *
 * @Author: j-sentinel
 * @Date: 2023/10/5 20:18
 */
@Getter
@AllArgsConstructor
public enum StudioTypeEnums {

    ANCHOR_STUDIO(1,"直播"),
    FAKE_LIVE(2,"伪直播"),
    STUDIO(3,"无人直播"),
    ;

    /**
     *  类型
     */
    private Integer type;

    /**
     * 类型描述信息
     */
    private String desc;

}
