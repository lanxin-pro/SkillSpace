package cn.iocoder.educate.module.infra.service.file;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.util.io.FileUtils;
import cn.iocoder.educate.framework.file.core.client.FileClient;
import cn.iocoder.educate.framework.file.core.utils.FileTypeUtils;
import cn.iocoder.educate.module.infra.dal.dataobject.file.FileDO;
import cn.iocoder.educate.module.infra.dal.mysql.file.FileMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * 文件 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/7/13 11:34
 */
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private FileMapper fileMapper;

    @Resource
    private FileConfigService fileConfigService;

    @Override
    public String createFile(String name, String path, byte[] content) {
        // 计算默认的 path 名
        String mineType = FileTypeUtils.getMineType(content, name);
        if(StrUtil.isEmpty(path)){
            path = FileUtils.generatePath(content, name);
        }
        // 如果 name 为空，则使用 path 填充
        if(StrUtil.isEmpty(name)){
            name = path;
        }
        // 上传到文件存储器 实则是获取我init的客户端
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, mineType);

        // 保存到数据库
        FileDO file = new FileDO();
        file.setConfigId(client.getId());
        file.setName(name);
        file.setPath(path);
        file.setUrl(url);
        file.setType(mineType);
        file.setSize(content.length);
        fileMapper.insert(file);
        return url;
    }

}
