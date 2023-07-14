package cn.iocoder.educate.framework.file.core.client.s3;

import cn.iocoder.educate.framework.file.core.client.AbstractFileClient;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/13 15:46
 */
public class S3FileClient extends AbstractFileClient<S3FileClientConfig> {

    public S3FileClient(Long id, S3FileClientConfig config) {
        super(id, config);
    }

    @Override
    protected void doInit() {

    }

    @Override
    public String upload(byte[] content, String path, String mineType) {
        return null;
    }

}
