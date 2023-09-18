package cn.iocoder.educate.framework.quartz.core.handler;

/**
 * 任务处理器
 *
 * @Author: j-sentinel
 * @Date: 2023/9/18 18:46
 */
public interface JobHandler {

    /**
     * 执行任务
     *
     * @param param 参数
     * @return 结果
     * @throws Exception 异常
     */
    String execute(String param) throws Exception;

}
