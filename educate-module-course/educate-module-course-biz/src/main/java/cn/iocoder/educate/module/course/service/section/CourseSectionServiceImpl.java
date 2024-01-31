package cn.iocoder.educate.module.course.service.section;

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
    public List<CourseSectionDO> findCourseSectionList(String courseId) {
        return courseSectionMapper.findCourseSectionList(courseId);
    }
}
