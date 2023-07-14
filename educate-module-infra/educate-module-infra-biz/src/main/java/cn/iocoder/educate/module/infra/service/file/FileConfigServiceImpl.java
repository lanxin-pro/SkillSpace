package cn.iocoder.educate.module.infra.service.file;

import cn.iocoder.educate.framework.file.core.client.FileClient;
import cn.iocoder.educate.framework.file.core.client.FileClientFactory;
import cn.iocoder.educate.module.infra.dal.dataobject.file.FileConfigDO;
import cn.iocoder.educate.module.infra.dal.mysql.file.FileConfigMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 文件配置 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/7/13 12:59
 */
@Service
@Validated
@Slf4j
public class FileConfigServiceImpl implements FileConfigService {

    /**
     * return this cache FileClient Class
     */
    @Getter
    private FileClient masterFileClient;

    @Resource
    private FileClientFactory fileClientFactory;

    @Resource
    private FileConfigMapper fileConfigMapper;

    @Override
    @PostConstruct
    public void initLocalCache() {
        // 第一步：查询数据
        List<FileConfigDO> fileConfigDOS = fileConfigMapper.selectList(new LambdaQueryWrapper<>());
        log.info("[initLocalCache][缓存文件配置，数量为:{}]", fileConfigDOS.size());

        // 第二步：构建缓存：创建或更新文件 Client
        fileConfigDOS.forEach(config -> {
            fileClientFactory.createOrUpdateFileClient(config.getId(), config.getStorage(), config.getConfig());
            // 如果是 master，进行设置
            if (Boolean.TRUE.equals(config.getMaster())) {
                masterFileClient = fileClientFactory.getFileClient(config.getId());
            }
        });
    }

    @Override
    public FileClient getFileClient(Long configId) {
        // 必然是查询的master
        return fileClientFactory.getFileClient(configId);
    }

}
