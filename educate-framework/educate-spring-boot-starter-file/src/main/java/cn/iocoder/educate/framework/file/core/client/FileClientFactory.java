package cn.iocoder.educate.framework.file.core.client;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/13 12:22
 */
public interface FileClientFactory {

    /**
     * 获得文件客户端
     *
     * @param configId 配置编号
     * @return 文件客户端
     */
    FileClient getFileClient(Long configId);

    /**
     * 创建文件客户端
     *
     * @param configId 配置编号
     * @param storage 存储器的枚举 {@link cn.iocoder.educate.framework.file.core.enums.FileStorageEnum}
     * @param config 文件配置
     * @param <Config>
     */
    <Config extends FileClientConfig> void createOrUpdateFileClient(Long configId, Integer storage, Config config);

}
