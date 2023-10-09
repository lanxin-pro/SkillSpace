package cn.iocoder.educate.module.video.api.enums.live;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 资源类型 1：视频 2：直播
 *
 * @Author: j-sentinel
 * @Date: 2023/10/5 20:18
 */
@Getter
@AllArgsConstructor
public enum SourceTypeEnums {

    VIDEO(1,"视频"),
    STUDIO(2,"直播"),
    LIVE_VIDEO(3,"伪直播资源"),
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
