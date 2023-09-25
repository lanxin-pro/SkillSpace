package cn.iocoder.educate.module.video.controller.admin.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/25 19:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理后台 - 附件分片上传返回的对象")
public class VideoFileChunkRespVO {

    /**
     * 是否跳过上传
     */
    private Boolean skipUpload;

    /**
     * 已上传分片的集合
     */
    private Set<Integer> uploaded;

    public VideoFileChunkRespVO(Boolean skipUpload) {
        this.skipUpload = skipUpload;
    }

}
