package cn.iocoder.educate.module.video.api.enums.live;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 来源类型 1：后台上传 2：沙巴同步
 *
 * @Author: j-sentinel
 * @Date: 2023/10/5 20:18
 */
@Getter
@AllArgsConstructor
public enum OriginTypeEnums {

    ADMIN(1,"后台上传"),
    SABA(2,"沙巴同步")
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
