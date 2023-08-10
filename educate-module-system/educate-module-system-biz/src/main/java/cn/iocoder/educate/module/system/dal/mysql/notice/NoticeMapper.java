package cn.iocoder.educate.module.system.dal.mysql.notice;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.notice.vo.NoticePageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.notice.NoticeDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/10 18:09
 */
@Mapper
public interface NoticeMapper extends BaseMapper<NoticeDO> {

    default PageResult<NoticeDO> selectPage(NoticePageReqVO noticePageReqVO) {
        LambdaQueryWrapper<NoticeDO> noticeDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        noticeDOLambdaQueryWrapper
                .like(StringUtils.hasText(noticePageReqVO.getTitle()),NoticeDO::getTitle,noticePageReqVO.getTitle())
                .eq(ObjectUtil.isNotEmpty(noticePageReqVO.getStatus()),NoticeDO::getStatus,noticePageReqVO.getStatus())
                .orderByDesc(NoticeDO::getId);
        Page<NoticeDO> page = new Page<>(noticePageReqVO.getPageNo(), noticePageReqVO.getPageSize());
        Page<NoticeDO> noticeDOPage = this.selectPage(page, noticeDOLambdaQueryWrapper);
        return new PageResult<>(noticeDOPage.getRecords(),noticeDOPage.getTotal());
    }

}
