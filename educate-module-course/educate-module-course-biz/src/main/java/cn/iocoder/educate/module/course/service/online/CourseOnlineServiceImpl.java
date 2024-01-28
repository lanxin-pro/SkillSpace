package cn.iocoder.educate.module.course.service.online;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.course.controller.admin.online.vo.CourseOnlinePageReqVO;
import cn.iocoder.educate.module.course.dal.dataobject.online.CourseOnlineDO;
import cn.iocoder.educate.module.course.dal.mysql.online.CourseOnlineMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * @author j-sentinel
 * @date 2024/1/28 11:46
 */
@Slf4j
@Service
public class CourseOnlineServiceImpl implements CourseOnlineService {

    @Resource
    private CourseOnlineMapper courseMapper;

    @Override
    public PageResult<CourseOnlineDO> getCourseOnlinePage(CourseOnlinePageReqVO dictDataPageReqVO) {
        return courseMapper.selectPage(dictDataPageReqVO);
    }

}
