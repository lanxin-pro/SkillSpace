package cn.iocoder.educate.module.video.api.enums.live;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 礼物类型枚举
 *
 * @Author: j-sentinel
 * @Date: 2023/10/5 20:18
 */
@Getter
@AllArgsConstructor
public enum LiveGiftTypeEnums {

    BARRAGE(8,"弹幕"),
    ON_TIME_CHARGE(6,"按时收费"),
    ON_PLACE_CHARGE(7,"按场收费"),
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
