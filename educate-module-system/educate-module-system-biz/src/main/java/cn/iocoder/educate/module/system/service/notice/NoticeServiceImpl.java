package cn.iocoder.educate.module.system.service.notice;

import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.notice.vo.NoticeCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.notice.vo.NoticePageReqVO;
import cn.iocoder.educate.module.system.controller.admin.notice.vo.NoticeUpdateReqVO;
import cn.iocoder.educate.module.system.convert.notice.NoticeConvert;
import cn.iocoder.educate.module.system.dal.dataobject.notice.NoticeDO;
import cn.iocoder.educate.module.system.dal.mysql.notice.NoticeMapper;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 通知公告 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/8/10 18:08
 */
@Service
public class NoticeServiceImpl implements NoticeService {

    @Resource
    private NoticeMapper noticeMapper;

    @Override
    public Long createNotice(NoticeCreateReqVO noticeCreateReqVO) {
        NoticeDO notice = NoticeConvert.INSTANCE.convert(noticeCreateReqVO);
        noticeMapper.insert(notice);
        return notice.getId();
    }

    @Override
    public void updateNotice(NoticeUpdateReqVO noticeUpdateReqVO) {
        // 校验是否存在
        validateNoticeExists(noticeUpdateReqVO.getId());
        // 更新通知公告
        NoticeDO updateObj = NoticeConvert.INSTANCE.convert(noticeUpdateReqVO);
        noticeMapper.updateById(updateObj);
    }

    @Override
    public void deleteNotice(Long id) {
        // 校验是否存在
        validateNoticeExists(id);
        // 删除通知公告
        noticeMapper.deleteById(id);
    }

    @Override
    public PageResult<NoticeDO> getNoticePage(NoticePageReqVO noticePageReqVO) {
        return noticeMapper.selectPage(noticePageReqVO);
    }

    @Override
    public NoticeDO getNotice(Long id) {
        return noticeMapper.selectById(id);
    }

    public void validateNoticeExists(Long id) {
        if (id == null) {
            return;
        }
        NoticeDO notice = noticeMapper.selectById(id);
        if (notice == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.NOTICE_NOT_FOUND);
        }
    }

}
