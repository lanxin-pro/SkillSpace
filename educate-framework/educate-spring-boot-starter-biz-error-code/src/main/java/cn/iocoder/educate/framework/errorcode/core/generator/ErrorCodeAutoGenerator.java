package cn.iocoder.educate.framework.errorcode.core.generator;

/**
 * 错误码的自动生成器
 *
 * @Author: j-sentinel
 * @Date: 2023/8/20 11:14
 */
public interface ErrorCodeAutoGenerator {

    /**
     * 将配置类到错误码写入数据库
     */
    void execute();

}
