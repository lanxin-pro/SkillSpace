package cn.iocoder.educate.framework.file.core.client.db;

import cn.iocoder.educate.framework.file.core.client.AbstractFileClient;

/**
 * 基于 DB 存储的文件客户端的配置类
 *
 * @Author: j-sentinel
 * @Date: 2023/7/13 15:24
 */
public class DBFileClient extends AbstractFileClient<DBFileClientConfig> {

    public DBFileClient(Long id, DBFileClientConfig config) {
        super(id, config);
    }

    @Override
    protected void doInit() {

    }

    @Override
    public String upload(byte[] content, String path, String mineType) {
        return null;
    }

    @Override
    public Long getId() {
        return null;
    }
}
