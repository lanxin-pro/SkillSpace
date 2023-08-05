package cn.iocoder.educate.module.infra.service.file;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.json.JsonUtils;
import cn.iocoder.educate.framework.common.util.validation.ValidationUtils;
import cn.iocoder.educate.framework.file.core.client.FileClient;
import cn.iocoder.educate.framework.file.core.client.FileClientConfig;
import cn.iocoder.educate.framework.file.core.client.FileClientFactory;
import cn.iocoder.educate.framework.file.core.enums.FileStorageEnum;
import cn.iocoder.educate.module.infra.controller.admin.file.vo.config.FileConfigCreateReqVO;
import cn.iocoder.educate.module.infra.controller.admin.file.vo.config.FileConfigPageReqVO;
import cn.iocoder.educate.module.infra.controller.admin.file.vo.config.FileConfigUpdateReqVO;
import cn.iocoder.educate.module.infra.covert.file.FileConfigConvert;
import cn.iocoder.educate.module.infra.dal.dataobject.file.FileConfigDO;
import cn.iocoder.educate.module.infra.dal.mysql.file.FileConfigMapper;
import cn.iocoder.educate.module.infra.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.infra.mq.producer.file.FileConfigProducer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.Validator;
import java.util.List;
import java.util.Map;

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

    @Resource
    private Validator validator;

    @Resource
    private FileConfigProducer fileConfigProducer;

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

    @Override
    public PageResult<FileConfigDO> getFileConfigPage(FileConfigPageReqVO fileConfigPageReqVO) {
        return fileConfigMapper.selectPage(fileConfigPageReqVO);
    }

    @Override
    public Long createFileConfig(FileConfigCreateReqVO createReqVO) {
        // 插入
        FileConfigDO fileConfig = FileConfigConvert.INSTANCE.convert(createReqVO);
        FileClientConfig fileClientConfig = parseClientConfig(createReqVO.getStorage(), createReqVO.getConfig());
        fileConfig.setConfig(fileClientConfig)
                // 默认非 master
                .setMaster(false);
        fileConfigMapper.insert(fileConfig);
        // 发送刷新配置的消息
        fileConfigProducer.sendFileConfigRefreshMessage();
        // 返回
        return fileConfig.getId();
    }

    @Override
    public void updateFileConfig(FileConfigUpdateReqVO fileConfigUpdateReqVO) {
        // 校验存在
        FileConfigDO config = validateFileConfigExists(fileConfigUpdateReqVO.getId());
        // 更新
        FileConfigDO fileConfigDO = FileConfigConvert.INSTANCE.convert(fileConfigUpdateReqVO);
        FileClientConfig fileClientConfig = parseClientConfig(config.getStorage(), fileConfigUpdateReqVO.getConfig());
        fileConfigDO.setConfig(fileClientConfig);
        fileConfigMapper.updateById(fileConfigDO);
        // 发送刷新配置的消息
        fileConfigProducer.sendFileConfigRefreshMessage();
    }

    private FileConfigDO validateFileConfigExists(Long id) {
        FileConfigDO config = fileConfigMapper.selectById(id);
        if (config == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.FILE_CONFIG_NOT_EXISTS);
        }
        return config;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFileConfigMaster(Long id) {
        // 校验存在
        validateFileConfigExists(id);
        // 更新其它为非 master
        fileConfigMapper.updateBatch(new FileConfigDO().setMaster(false));
        // 更新
        fileConfigMapper.updateById(new FileConfigDO().setId(id).setMaster(true));
        // 发送刷新配置的消息
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
                fileConfigProducer.sendFileConfigRefreshMessage();
            }

        });
    }

    @Override
    public void deleteFileConfig(Long id) {
        // 校验存在
        FileConfigDO config = validateFileConfigExists(id);
        if (Boolean.TRUE.equals(config.getMaster())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.FILE_CONFIG_DELETE_FAIL_MASTER);
        }
        // 删除
        fileConfigMapper.deleteById(id);
        // 发送刷新配置的消息
        fileConfigProducer.sendFileConfigRefreshMessage();
    }

    @Override
    public FileConfigDO getFileConfig(Long id) {
        return fileConfigMapper.selectById(id);
    }

    @Override
    public String testFileConfig(Long id) {
        // 校验存在
        validateFileConfigExists(id);
        // 上传文件
        byte[] content = ResourceUtil.readBytes("file/lantest.jpg");
        return fileClientFactory.getFileClient(id)
                .upload(content, IdUtil.fastSimpleUUID() + ".jpg", "image/jpeg");
    }

    private FileClientConfig parseClientConfig(Integer storage, Map<String, Object> config) {
        FileStorageEnum fileStorageEnum = FileStorageEnum.getByStorage(storage);
        if(ObjectUtil.isEmpty(fileStorageEnum)){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.FILE_CONFIG_STORAGE_NOT_EXISTS);
        }
        // 获取配置类
        Class<? extends FileClientConfig> configClass = FileStorageEnum.getByStorage(storage)
                .getConfigClass();
        // hutu工具类toBean方法可以将 属性,class 转换为类
        FileClientConfig clientConfig = JsonUtils.parseObject2(JsonUtils.toJsonString(config), configClass);
        // 参数校验
        ValidationUtils.validate(validator, clientConfig);
        // 设置参数
        return clientConfig;
    }

}
