package cn.iocoder.educate.module.infra.dal.mysql.file;

import cn.iocoder.educate.framework.file.core.client.db.DBFileContentFrameworkDAO;
import cn.iocoder.educate.module.infra.dal.dataobject.file.FileContentDO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/14 10:51
 */
@Repository
@Component
public class FileContentDAOImpl implements DBFileContentFrameworkDAO {

    @Resource
    private FileContentMapper fileContentMapper;

    @Override
    public void insert(Long configId, String path, byte[] content) {
        FileContentDO fileContentDO = new FileContentDO().setConfigId(configId)
                .setPath(path).setContent(content);
        fileContentMapper.insert(fileContentDO);
    }
}
