package cn.iocoder.educate.module.video.controller.admin.file;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.educate.module.video.controller.admin.file.vo.VideoFileChunkRespVO;
import cn.iocoder.educate.module.video.controller.admin.file.vo.VideoFileChunkVO;
import cn.iocoder.educate.module.video.service.uploader.VideoUploaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

import java.io.IOException;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/25 19:26
 */
@Tag(name = "管理后台 - 视频标签")
@RestController
@RequestMapping("/video/file")
@Validated
public class VideoUploaderController {

    @Resource
    private VideoUploaderService videoUploaderService;

    @GetMapping("/chunkUpload")
    @Operation(summary = "检查分片是否存在")
    public CommonResult<VideoFileChunkRespVO> getVideoPage(VideoFileChunkVO videoFileChunkVO) {
        return success(videoUploaderService.checkChunkExist(videoFileChunkVO));
    }

    @PostMapping("/chunkUpload")
    @Operation(summary = "上传文件分片")
    public CommonResult<String> uploadChunk(VideoFileChunkVO videoFileChunkVO) {
        try {
            videoUploaderService.uploadChunk(videoFileChunkVO);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success(videoFileChunkVO.getIdentifier());
    }

    @PostMapping("/merge")
    @Operation(summary = "请求合并文件分片")
    public CommonResult<String> mergeChunks(@RequestBody VideoFileChunkVO chunkDTO) throws Exception {
        String url = videoUploaderService.mergeChunk(chunkDTO.getIdentifier(), chunkDTO.getFilename(),
                chunkDTO.getTotalChunks());
        return success(url);
    }

}
