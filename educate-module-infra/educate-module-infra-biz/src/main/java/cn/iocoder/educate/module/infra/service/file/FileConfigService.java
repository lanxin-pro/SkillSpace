package cn.iocoder.educate.module.infra.service.file;

import cn.iocoder.educate.framework.file.core.client.FileClient;

/**
 * 文件配置 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/7/13 12:59
 */
public interface FileConfigService {

    /**
     * 初始化文件客户端
     */
    void initLocalCache();

    /**
     *  获得 Master 文件客户端
     *
     * @return 文件客户端
     */
    FileClient getMasterFileClient();
}
