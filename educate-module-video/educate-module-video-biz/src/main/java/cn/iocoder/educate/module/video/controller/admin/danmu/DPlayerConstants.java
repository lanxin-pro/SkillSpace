package cn.iocoder.educate.module.video.controller.admin.danmu;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/19 19:17
 */
public class DPlayerConstants {

    // 成功
    public static final int DPLAYER_SUCCESS_CODE = 0;

    public static final String DPLAYER_BARRAGE_ID = "0";
    public static final String DPLAYER_BARRAGE_AUTHOR = "DPlayer";
    public static final Double DPLAYER_BARRAGE_TIME = 1.236;
    public static final String DPLAYER_BARRAGE_TEXT = "你说我的弹幕到底在哪里？？？？";
    public static final Integer DPLAYER_BARRAGE_COLOR = 15024726;
    public static final Integer DPLAYER_BARRAGE_TYPE = 1;

    public static final List barrage_init(List data){
        data = new ArrayList();
        data.add(DPLAYER_BARRAGE_TIME);
        data.add(DPLAYER_BARRAGE_TYPE);
        data.add(DPLAYER_BARRAGE_COLOR);
        data.add(DPLAYER_BARRAGE_AUTHOR);
        data.add(DPLAYER_BARRAGE_TEXT);
        return data;
    }

}
