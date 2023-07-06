package cn.iocoder.educate.module.system.convert.post;

import cn.iocoder.educate.module.system.controller.admin.dept.vo.post.PostSimpleRespVO;
import cn.iocoder.educate.module.system.dal.post.PostDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/3 18:31
 */
@Mapper
public interface PostConvert {

    PostConvert INSTANCE = Mappers.getMapper(PostConvert.class);

    List<PostSimpleRespVO> convertList02(List<PostDO> list);

}
