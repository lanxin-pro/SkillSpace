package cn.iocoder.educate.module.infra.api.file;

import cn.iocoder.educate.module.infra.service.file.FileService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 文件 API 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/7/13 11:32
 */
@Service
@Validated
public class FileApiImpl implements FileApi {

    @Resource
    private FileService fileService;

    @Override
    public String createFile(String name, String path, byte[] content) {
        return fileService.createFile(name,path,content);
    }

}
