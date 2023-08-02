package cn.iocoder.educate.module.infra.dal.mysql.file;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.infra.controller.admin.file.vo.file.FilePageReqVO;
import cn.iocoder.educate.module.infra.dal.dataobject.codegen.CodegenTableDO;
import cn.iocoder.educate.module.infra.dal.dataobject.file.FileDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/13 11:35
 */
@Mapper
public interface FileMapper extends BaseMapper<FileDO> {

    default PageResult<FileDO> selectPage(FilePageReqVO filePageReqVO) {
        LambdaQueryWrapper<FileDO> fileDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        fileDOLambdaQueryWrapper
                .like(StringUtils.hasText(filePageReqVO.getPath()),FileDO::getPath,filePageReqVO.getPath())
                .like(StringUtils.hasText(filePageReqVO.getType()),FileDO::getType,filePageReqVO.getType())
                .between(null != ArrayUtils.get(filePageReqVO.getCreateTime(),0) &&
                                ArrayUtils.get(filePageReqVO.getCreateTime(),1) != null,
                        FileDO::getCreateTime,
                        ArrayUtils.get(filePageReqVO.getCreateTime(),0),
                        ArrayUtils.get(filePageReqVO.getCreateTime(),1))
                .orderByDesc(FileDO::getId);
        Page<FileDO> page = new Page<>(filePageReqVO.getPageNo(), filePageReqVO.getPageSize());
        Page<FileDO> fileDOPage = this.selectPage(page, fileDOLambdaQueryWrapper);
        return new PageResult<>(fileDOPage.getRecords(),fileDOPage.getTotal());
    }

}
