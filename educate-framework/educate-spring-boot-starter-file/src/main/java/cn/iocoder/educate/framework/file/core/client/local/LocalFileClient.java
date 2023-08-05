package cn.iocoder.educate.framework.file.core.client.local;

import cn.hutool.core.io.FileUtil;
import cn.iocoder.educate.framework.file.core.client.AbstractFileClient;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/4 11:12
 */
public class LocalFileClient extends AbstractFileClient<LocalFileClientConfig> {
    /**
     * 在return (AbstractFileClient<Config>) ReflectUtil.newInstance(storageEnum.getClientClass(), configId, config);的时候
     * 就已经赋值了
     *
     * @param id     file_config的id
     * @param config file的配置类
     */
    public LocalFileClient(Long id, LocalFileClientConfig config) {
        super(id, config);
    }

    @Override
    protected void doInit() {

    }

    @Override
    public String upload(byte[] content, String path, String mineType) {
        // 执行写入
        String filePath = getFilePath(path);
        FileUtil.writeBytes(content, filePath);
        // 拼接返回路径
        return super.formatFileUrl(config.getDomain(), path);
    }

    @Override
    public byte[] getContent(String path) {
        String filePath = getFilePath(path);
        return FileUtil.readBytes(filePath);
    }

    @Override
    public void delete(String path) {
        String filePath = getFilePath(path);
        FileUtil.del(filePath);
    }

    private String getFilePath(String path) {
        return config.getBasePath() + path;
    }


}
