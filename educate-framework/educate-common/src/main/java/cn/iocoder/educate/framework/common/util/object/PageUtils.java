package cn.iocoder.educate.framework.common.util.object;

import cn.iocoder.educate.framework.common.pojo.PageParam;

/**
 * {@link cn.iocoder.educate.framework.common.pojo.PageParam } 工具类
 *
 * @Author: j-sentinel
 * @Date: 2023/9/12 16:51
 */
public class PageUtils {

    public static int getStart(PageParam pageParam) {
        return (pageParam.getPageNo() - 1) * pageParam.getPageSize();
    }

}
