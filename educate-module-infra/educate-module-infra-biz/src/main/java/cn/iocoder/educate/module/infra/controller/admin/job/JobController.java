package cn.iocoder.educate.module.infra.controller.admin.job;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.quartz.core.util.CronUtils;
import cn.iocoder.educate.module.infra.controller.admin.job.vo.job.JobCreateReqVO;
import cn.iocoder.educate.module.infra.controller.admin.job.vo.job.JobPageReqVO;
import cn.iocoder.educate.module.infra.controller.admin.job.vo.job.JobRespVO;
import cn.iocoder.educate.module.infra.covert.job.JobConvert;
import cn.iocoder.educate.module.infra.dal.dataobject.job.JobDO;
import cn.iocoder.educate.module.infra.service.job.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.quartz.SchedulerException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/17 11:47
 */
@Tag(name = "管理后台 - 定时任务")
@RestController
@RequestMapping("/infra/job")
@Validated
public class JobController {

    @Resource
    private JobService jobService;

    @GetMapping("/page")
    @Operation(summary = "获得定时任务分页")
    @PreAuthorize("@lanxin.hasPermission('infra:job:query')")
    public CommonResult<PageResult<JobRespVO>> getJobPage(@Valid JobPageReqVO pageVO) {
        PageResult<JobDO> pageResult = jobService.getJobPage(pageVO);
        return success(JobConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/get")
    @Operation(summary = "获得定时任务")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@lanxin.hasPermission('infra:job:query')")
    public CommonResult<JobRespVO> getJob(@RequestParam("id") Long id) {
        JobDO job = jobService.getJob(id);
        return success(JobConvert.INSTANCE.convert(job));
    }

    @GetMapping("/get_next_times")
    @Operation(summary = "获得定时任务的下 n 次执行时间")
    @Parameters({
            @Parameter(name = "id", description = "编号", required = true, example = "1024"),
            @Parameter(name = "count", description = "统计的数量", example = "5")
    })
    @PreAuthorize("@lanxin.hasPermission('infra:job:query')")
    public CommonResult<List<LocalDateTime>> getJobNextTimes(@RequestParam("id") Long id,
                                                             @RequestParam(value = "count",
                                                                     required = false,
                                                                     defaultValue = "5") Integer count) {
        JobDO job = jobService.getJob(id);
        if (job == null) {
            return success(Collections.emptyList());
        }
        return success(CronUtils.getNextTimes(job.getCronExpression(), count));
    }

    @PostMapping("/create")
    @Operation(summary = "创建定时任务")
    @PreAuthorize("@lanxin.hasPermission('infra:job:create')")
    public CommonResult<Long> createJob(@Valid @RequestBody JobCreateReqVO createReqVO)
            throws SchedulerException {
        return success(jobService.createJob(createReqVO));
    }

}
