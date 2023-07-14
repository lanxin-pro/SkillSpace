package cn.iocoder.educate.framework.file.core.client.db;

import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.educate.framework.file.core.client.AbstractFileClient;

/**
 * 基于 DB 存储的文件客户端的配置类
 *
 * @Author: j-sentinel
 * @Date: 2023/7/13 15:24
 */
public class DBFileClient extends AbstractFileClient<DBFileClientConfig> {

    private DBFileContentFrameworkDAO dbFileContentFrameworkDAO;

    /**
     * super就会给我的父Id赋值
     * @param id
     * @param config
     */
    public DBFileClient(Long id, DBFileClientConfig config) {
        super(id, config);
    }

    @Override
    protected void doInit() {

    }

    @Override
    public String upload(byte[] content, String path, String mineType) {
        getDbFileContentFrameworkDAO().insert(getId(),path,content);
        // 拼接返回路径
        return super.formatFileUrl(config.getDomain(), path);
    }

    @Override
    public byte[] getContent(String path) {
        return getDbFileContentFrameworkDAO()
                .selectContent(getId(), path);
    }

    private DBFileContentFrameworkDAO getDbFileContentFrameworkDAO() {
        // 延迟获取，因为 SpringUtil 初始化太慢
        if(dbFileContentFrameworkDAO == null){
            dbFileContentFrameworkDAO = SpringUtil.getBean(DBFileContentFrameworkDAO.class);
        }
        return dbFileContentFrameworkDAO;
    }

}
