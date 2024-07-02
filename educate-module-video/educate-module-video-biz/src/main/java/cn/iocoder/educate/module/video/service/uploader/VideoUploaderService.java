package cn.iocoder.educate.module.video.service.uploader;

import cn.iocoder.educate.module.video.controller.admin.file.vo.VideoFileChunkRespVO;
import cn.iocoder.educate.module.video.controller.admin.file.vo.VideoFileChunkVO;
import cn.iocoder.educate.module.video.controller.admin.file.vo.VideoFileMergeRespVO;

import java.io.IOException;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/25 19:41
 */
public interface VideoUploaderService {

    /**
     * 检查文件是否存在，如果存在则跳过该文件的上传，如果不存在，返回需要上传的分片集合
     *
     * @param chunkDTO 分片的VO对象
     * @return
     */
    VideoFileChunkRespVO checkChunkExist(VideoFileChunkVO chunkDTO);

    /**
     * 上传文件分片
     *
     * @param chunkDTO 分片的VO对象
     */
    void uploadChunk(VideoFileChunkVO chunkDTO) throws IOException;

    /**
     * 合并文件分片
     * @param identifier 文件md5的值
     * @param fileName 文件名字
     * @param totalChunks 拥有多少个分片
     *
     * @return String 返回上传访问路径
     * @throws Exception
     */
    VideoFileMergeRespVO mergeChunk(String identifier, String fileName, Integer totalChunks)throws Exception;

}
