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
    @PermitAll
    @OperateLog(enable = false)
    public CommonResult<VideoFileChunkRespVO> getVideoPage(VideoFileChunkVO videoFileChunkVO) {
        return success(videoUploaderService.checkChunkExist(videoFileChunkVO));
    }

    /**
     * 上传文件分片
     */
    @PostMapping("/chunkUpload")
    @PermitAll
    @OperateLog(enable = false)
    public CommonResult<String> uploadChunk() {
        return success("success");
    }

    /**
     * 请求合并文件分片
     */
    @PostMapping("/merge")
    @PermitAll
    @OperateLog(enable = false)
    public CommonResult<String> mergeChunks() {
        return success("success");
    }

}
