package cn.iocoder.educate.module.infra.service.file;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.io.FileUtils;
import cn.iocoder.educate.framework.file.core.client.FileClient;
import cn.iocoder.educate.framework.file.core.utils.FileTypeUtils;
import cn.iocoder.educate.module.infra.controller.admin.file.vo.file.FilePageReqVO;
import cn.iocoder.educate.module.infra.dal.dataobject.file.FileDO;
import cn.iocoder.educate.module.infra.dal.mysql.file.FileMapper;
import cn.iocoder.educate.module.infra.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.File;

/**
 * 文件 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/7/13 11:34
 */
@Service
public class FileServiceImpl implements FileService {

    private static final int INT_CHECK_NAME_EXISTS = 1;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private FileConfigService fileConfigService;

    @Override
    public String createFile(String name, String path, byte[] content) {
        if(StrUtil.isEmpty(path)){
            path = FileUtils.generatePath(content, name);
        }
        // 如果有重复的文件名就给后面加 (1)
        path = validateFileRepetitionExists(path);
        // 计算默认的 path 名
        String mineType = FileTypeUtils.getMineType(content, name);
        // 如果 name 为空，则使用 path 填充
        if(StrUtil.isEmpty(name)){
            name = path;
        }
        // 上传到文件存储器 实则是获取我init的客户端
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = "";
        try {
            url = client.upload(content, path, mineType);
        } catch (Exception exception){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.FILE_OVER_ERROR,exception);
        }

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

    private String validateFileRepetitionExists(String path) {
        int count = INT_CHECK_NAME_EXISTS;
        // 校验是否有重复的path名
        while (fileMapper.selectPathNameCount(path) > 0){
            int dotIndex = path.lastIndexOf(".");
            String nameWithoutExtension = path.substring(0, dotIndex);
            String extension = path.substring(dotIndex);
            path = nameWithoutExtension + "(" + count + ")" + extension;
            count++;
        }
        return path;
    }

    @Override
    public byte[] getFileContent(Long configId, String path) {
        // 获取主客户端
        FileClient client = fileConfigService.getFileClient(configId);
        Assert.notNull(client, "客户端({}) 不能为空", configId);
        // 获取文件内容
        byte[] content = client.getContent(path);
        return content;
    }

    @Override
    public PageResult<FileDO> getFilePage(FilePageReqVO filePageReqVO) {
        return fileMapper.selectPage(filePageReqVO);
    }

    @Override
    public void deleteFile(Long id) {
        // 校验存在
        FileDO file = validateFileExists(id);

        // 从文件存储器中删除
        FileClient client = fileConfigService.getFileClient(file.getConfigId());
        Assert.notNull(client, "客户端({}) 不能为空", file.getConfigId());
        client.delete(file.getPath());

        // 删除记录
        fileMapper.deleteById(id);
    }

    private FileDO validateFileExists(Long id) {
        FileDO fileDO = fileMapper.selectById(id);
        if (fileDO == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.FILE_NOT_EXISTS);
        }
        return fileDO;
    }

}
