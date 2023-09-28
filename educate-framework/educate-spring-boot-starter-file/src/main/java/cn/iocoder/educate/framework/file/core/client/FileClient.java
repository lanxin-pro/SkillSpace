package cn.iocoder.educate.framework.file.core.client;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/13 12:23
 */
public interface FileClient {

    /**
     * 上传文件
     *
     * @param content 文件流
     * @param path 相对路径
     * @param mineType 完整路径，即 HTTP 访问地址
     * @return 上传文件时，抛出 Exception 异常
     */
    String upload(byte[] content, String path, String mineType);

    /**
     * 获得客户端编号
     *
     * @return 客户端编号
     */
    Long getId();

    /**
     * 获得文件的内容
     *
     * @param path 相对路径
     * @return 文件的内容
     */
    byte[] getContent(String path);

    /**
     * 删除文件
     *
     * @param path 相对路径
     * @throws Exception 删除文件时，抛出 Exception 异常
     */
    void delete(String path);

}
