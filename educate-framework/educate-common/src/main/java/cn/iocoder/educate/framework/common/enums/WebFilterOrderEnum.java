package cn.iocoder.educate.framework.common.enums;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/8 17:04
 *
 * Web 过滤器顺序的枚举类，保证过滤器按照符合我们的预期
 *
 *  考虑到每个 starter 都需要用到该工具类，所以放到 common 模块下的 enums 包下
 */
public interface WebFilterOrderEnum {

    int CORS_FILTER = Integer.MIN_VALUE;
}
