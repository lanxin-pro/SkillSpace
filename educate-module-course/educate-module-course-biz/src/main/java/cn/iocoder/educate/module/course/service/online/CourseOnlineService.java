package cn.iocoder.educate.module.course.service.online;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.course.controller.admin.online.vo.CourseOnlinePageReqVO;
import cn.iocoder.educate.module.course.controller.admin.online.vo.CourseOnlineCreateReqVO;
import cn.iocoder.educate.module.course.controller.admin.online.vo.CourseOnlineUpdateReqVO;
import cn.iocoder.educate.module.course.controller.admin.online.vo.CourseOnlineUpdateStatusReqVO;
import cn.iocoder.educate.module.course.dal.dataobject.online.CourseOnlineDO;

import java.util.Collection;

/**
 * 课程接口
 *
 * @author j-sentinel
 * @date 2024/1/28 11:46
 */
public interface CourseOnlineService {

    /**
     * 获得课程的分页列表
     * @param dictDataPageReqVO 分页请求
     * @return 课程分页列表
     */
    PageResult<CourseOnlineDO> getCourseOnlinePage(CourseOnlinePageReqVO dictDataPageReqVO);

    /**
     * 新增课程的章
     *
     * @param reqVO 新增的数据
     * @return id
     */
    Long createCourse(CourseOnlineCreateReqVO reqVO);

    /**
     * 根据id获取课程信息
     *
     * @param id 课程id
     * @return 课程信息
     */
    CourseOnlineDO getOnlineInfo(Long id);

    /**
     * 更新课程的章
     *
     * @param updateReqVO 更新的内容
     */
    void updateOnlineInfo(CourseOnlineUpdateReqVO updateReqVO);

    /**
     * 删除课程的章
     *
     * @param id 删除的id
     */
    void deleteOnlineInfo(Long id);

    /**
     * 批量删除课程的章
     *
     * @param ids 批量删除的ids
     */
    void deleteBatchOnlineInfo(Collection<Long> ids);

    /**
     * 更新课程的状态
     *
     * @param updateReqVO 更新的内容
     */
    void updateStatusOnlineInfo(CourseOnlineUpdateStatusReqVO updateReqVO);
}
