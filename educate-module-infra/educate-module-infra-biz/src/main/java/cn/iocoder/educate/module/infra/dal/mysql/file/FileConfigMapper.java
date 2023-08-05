package cn.iocoder.educate.module.infra.dal.mysql.file;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.infra.controller.admin.file.vo.config.FileConfigPageReqVO;
import cn.iocoder.educate.module.infra.dal.dataobject.file.FileConfigDO;
import cn.iocoder.educate.module.infra.dal.dataobject.file.FileDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/13 13:26
 */
@Mapper
public interface FileConfigMapper extends BaseMapper<FileConfigDO> {

    default void updateBatch(FileConfigDO fileConfigDO) {
        update(fileConfigDO, new LambdaQueryWrapper<>());
    }

    default PageResult<FileConfigDO> selectPage(FileConfigPageReqVO fileConfigPageReqVO) {
        LambdaQueryWrapper<FileConfigDO> fileConfigDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        fileConfigDOLambdaQueryWrapper
                .like(StringUtils.hasText(fileConfigPageReqVO.getName()),FileConfigDO::getName,fileConfigPageReqVO.getName())
                .eq(ObjectUtil.isNotEmpty(fileConfigPageReqVO.getStorage()),FileConfigDO::getStorage,fileConfigPageReqVO.getStorage())
                .between(null != ArrayUtils.get(fileConfigPageReqVO.getCreateTime(),0) &&
                                ArrayUtils.get(fileConfigPageReqVO.getCreateTime(),1) != null,
                        FileConfigDO::getCreateTime,
                        ArrayUtils.get(fileConfigPageReqVO.getCreateTime(),0),
                        ArrayUtils.get(fileConfigPageReqVO.getCreateTime(),1))
                .orderByDesc(FileConfigDO::getId);
        Page<FileConfigDO> page = new Page<>(fileConfigPageReqVO.getPageNo(), fileConfigPageReqVO.getPageSize());
        Page<FileConfigDO> fileConfigDOPage = this.selectPage(page, fileConfigDOLambdaQueryWrapper);
        return new PageResult<>(fileConfigDOPage.getRecords(),fileConfigDOPage.getTotal());
    }

}
