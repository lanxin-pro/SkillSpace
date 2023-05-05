package cn.iocoder.educate.framework.common.util.monitor;

import org.apache.skywalking.apm.toolkit.trace.TraceContext;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/5 8:28
 * 链路追踪工具类
 * 考虑到每个 starter 都需要用到该工具类，所以放到 common 模块下的 util 包下
 */
public class TracerUtils {

    /**
     * 获得链路追踪编号，直接返回 SkyWalking 的 TraceId。
     * 如果不存在的话为空字符串！！！
     *
     * @return 链路追踪编号
     */
    public static String getTraceId() {
        return TraceContext.traceId();
    }
}
