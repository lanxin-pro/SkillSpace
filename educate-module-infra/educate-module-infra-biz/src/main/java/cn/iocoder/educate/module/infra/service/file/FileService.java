package cn.iocoder.educate.module.infra.service.file;

/**
 * 文件 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/7/13 11:33
 */
public interface FileService {

    /**
     * 保存文件，并返回文件的访问路径
     *
     * @param name 文件名称
     * @param path 文件路径
     * @param content 文件内容
     * @return 文件路径
     */
    String createFile(String name, String path, byte[] content);
}
