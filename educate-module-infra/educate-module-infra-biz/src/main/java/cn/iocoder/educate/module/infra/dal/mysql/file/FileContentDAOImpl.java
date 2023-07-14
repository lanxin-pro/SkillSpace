package cn.iocoder.educate.module.infra.dal.mysql.file;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.educate.framework.file.core.client.db.DBFileContentFrameworkDAO;
import cn.iocoder.educate.module.infra.dal.dataobject.file.FileContentDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

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

    @Override
    public byte[] selectContent(Long configId, String path) {
        LambdaQueryWrapper<FileContentDO> fileContentDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        fileContentDOLambdaQueryWrapper
                .eq(FileContentDO::getConfigId,configId)
                .eq(FileContentDO::getPath,path)
                .select(FileContentDO::getContent)
                .orderByDesc(FileContentDO::getId);
        List<FileContentDO> fileContentDOS = fileContentMapper.selectList(fileContentDOLambdaQueryWrapper);
        return Optional
                // 获取fileContentDOS列表的第一个元素
                .ofNullable( CollUtil.getFirst(fileContentDOS) )
                    .map(FileContentDO::getContent)
                // ofNullable如果为null，就返回orElse的值
                .orElse(null);
    }
}
