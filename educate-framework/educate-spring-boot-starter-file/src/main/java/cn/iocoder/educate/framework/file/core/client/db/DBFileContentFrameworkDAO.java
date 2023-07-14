package cn.iocoder.educate.framework.file.core.client.db;

/**
 * 文件内容 Framework DAO 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/7/14 10:45
 */
public interface DBFileContentFrameworkDAO {

    /**
     * 插入文件内容
     *
     * @param configId 配置编号
     * @param path 路径
     * @param content 内容
     */
    void insert(Long configId, String path, byte[] content);
}
