package cn.iocoder.educate.module.course.service.online;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.course.controller.admin.online.vo.CourseOnlinePageReqVO;
import cn.iocoder.educate.module.course.dal.dataobject.online.CourseOnlineDO;

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

}
