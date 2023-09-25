package cn.iocoder.educate.module.video.controller.admin.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * 附件分片上传
 *
 * @Author: j-sentinel
 * @Date: 2023/9/25 19:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理后台 - 分片的VO对象")
public class VideoFileChunkVO {

    /**
     * 文件md5
     */
    private String identifier;

    /**
     * 分块文件
     */
    MultipartFile file;

    /**
     * 当前分块序号
     */
    private Integer chunkNumber;

    /**
     * 分块大小
     */
    private Long chunkSize;

    /**
     * 当前分块大小
     */
    private Long currentChunkSize;

    /**
     * 文件总大小
     */
    private Long totalSize;

    /**
     * 分块总数
     */
    private Integer totalChunks;

    /**
     * 文件名
     */
    private String filename;

}
