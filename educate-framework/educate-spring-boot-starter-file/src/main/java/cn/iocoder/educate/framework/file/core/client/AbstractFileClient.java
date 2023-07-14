package cn.iocoder.educate.framework.file.core.client;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * 文件客户端的抽象类，提供模板方法，减少子类的冗余代码
 *
 * @Author: j-sentinel
 * @Date: 2023/7/13 12:26
 */
@Slf4j
public abstract class AbstractFileClient <Config extends FileClientConfig> implements FileClient {

    /**
     * 配置编号
     */
    private final Long id;

    /**
     * 文件配置
     */
    protected Config config;

    public AbstractFileClient(Long id, Config config) {
        this.id = id;
        this.config = config;
    }

    public final void refresh(Config config) {

    }

    /**
     * 初始化
     */
    public final void init() {
        doInit();
        log.info("[init][配置({}) 初始化完成]", config);
    }

    /**
     * 自定义初始化
     */
    protected abstract void doInit();

    @Override
    public Long getId() {
        return id;
    }

    protected String formatFileUrl(String domain, String path) {
        return StrUtil.format("{}/admin-api/infra/file/{}/get/{}", domain, getId(), path);
    }

}
