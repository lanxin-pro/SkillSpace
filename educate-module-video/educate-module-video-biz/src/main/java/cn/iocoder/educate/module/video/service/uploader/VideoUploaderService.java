package cn.iocoder.educate.module.video.service.uploader;

import cn.iocoder.educate.module.video.controller.admin.file.vo.VideoFileChunkRespVO;
import cn.iocoder.educate.module.video.controller.admin.file.vo.VideoFileChunkVO;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/25 19:41
 */
public interface VideoUploaderService {

    /**
     * 检查文件是否存在，如果存在则跳过该文件的上传，如果不存在，返回需要上传的分片集合
     * @param chunkDTO
     * @return
     */
    VideoFileChunkRespVO checkChunkExist(VideoFileChunkVO chunkDTO);

}
