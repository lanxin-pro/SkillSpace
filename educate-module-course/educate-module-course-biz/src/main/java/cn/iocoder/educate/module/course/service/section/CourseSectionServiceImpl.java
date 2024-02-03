package cn.iocoder.educate.module.course.service.section;

import cn.iocoder.educate.module.course.controller.admin.section.vo.CourseSectionRespVO;
import cn.iocoder.educate.module.course.convert.section.CourseSectionConvert;
import cn.iocoder.educate.module.course.dal.dataobject.section.CourseSectionDO;
import cn.iocoder.educate.module.course.dal.mysql.section.CourseSectionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author j-sentinel
 * @date 2024/1/31 19:49
 */
@Slf4j
@Service
public class CourseSectionServiceImpl implements CourseSectionService {

    @Resource
    private CourseSectionMapper courseSectionMapper;

    @Override
    public List<CourseSectionRespVO> findCourseChapterList(String courseId) {
        List<CourseSectionDO> courseChapterList = courseSectionMapper.findCourseChapterList(courseId);
        return CourseSectionConvert.INSTANCE.convert(courseChapterList);

    }

    @Override
    public List<CourseSectionRespVO> findCourseSectionList(Long id) {
        List<CourseSectionDO> courseSectionList = courseSectionMapper.findCourseSectionList(id);
        return CourseSectionConvert.INSTANCE.convert(courseSectionList);
    }
}
