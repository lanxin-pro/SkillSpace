package cn.iocoder.educate.framework.file.core.enums;

import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.educate.framework.file.core.client.FileClient;
import cn.iocoder.educate.framework.file.core.client.FileClientConfig;
import cn.iocoder.educate.framework.file.core.client.db.DBFileClient;
import cn.iocoder.educate.framework.file.core.client.db.DBFileClientConfig;
import cn.iocoder.educate.framework.file.core.client.s3.S3FileClient;
import cn.iocoder.educate.framework.file.core.client.s3.S3FileClientConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件存储器枚举
 *
 * @Author: j-sentinel
 * @Date: 2023/7/13 15:20
 */
@AllArgsConstructor
@Getter
public enum FileStorageEnum {
    DB(1, DBFileClientConfig.class, DBFileClient.class),

    S3(20, S3FileClientConfig.class, S3FileClient.class),
    ;

    /**
     * 存储器
     */
    private final Integer storage;

    /**
     * 配置类
     */
    private final Class<? extends FileClientConfig> configClass;

    /**
     * 客户端类
     */
    private final Class<? extends FileClient> clientClass;

    public static FileStorageEnum getByStorage(Integer storage) {
        return ArrayUtil.firstMatch(o -> o.getStorage().equals(storage), values());
    }
}
