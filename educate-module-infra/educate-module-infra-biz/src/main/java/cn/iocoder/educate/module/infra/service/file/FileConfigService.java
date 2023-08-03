package cn.iocoder.educate.module.infra.service.file;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.file.core.client.FileClient;
import cn.iocoder.educate.module.infra.controller.admin.file.vo.config.FileConfigPageReqVO;
import cn.iocoder.educate.module.infra.dal.dataobject.file.FileConfigDO;

/**
 * 文件配置 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/7/13 12:59
 */
public interface FileConfigService {

    /**
     * 初始化文件客户端
     */
    void initLocalCache();

    /**
     *  获得 Master 文件客户端
     *
     * @return 文件客户端
     */
    FileClient getMasterFileClient();

    /**
     * 获得指定编号的文件客户端
     *
     * @param configId 配置编号
     * @return 文件客户端
     */
    FileClient getFileClient(Long configId);

    /**
     * 获得文件配置分页
     *
     * @param fileConfigPageReqVO 分页查询
     * @return 文件配置分页
     */
    PageResult<FileConfigDO> getFileConfigPage(FileConfigPageReqVO fileConfigPageReqVO);

}
