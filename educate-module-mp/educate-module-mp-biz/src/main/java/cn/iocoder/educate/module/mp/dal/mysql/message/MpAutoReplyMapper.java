package cn.iocoder.educate.module.mp.dal.mysql.message;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.account.vo.MpAccountPageReqVO;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.autoreply.MpMessagePageReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.educate.module.mp.dal.dataobject.message.MpAutoReplyDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/1 20:24
 */
@Mapper
public interface MpAutoReplyMapper extends BaseMapper<MpAutoReplyDO> {

    default PageResult<MpAutoReplyDO> selectPage(MpMessagePageReqVO mpMessagePageReqVO) {
        LambdaQueryWrapper<MpAutoReplyDO> mpAutoReplyDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mpAutoReplyDOLambdaQueryWrapper
                .eq(ObjectUtil.isNotEmpty(mpMessagePageReqVO.getType()),
                        MpAutoReplyDO::getType,mpMessagePageReqVO.getType())
                .eq(MpAutoReplyDO::getAccountId, mpMessagePageReqVO.getAccountId());
        Page<MpAutoReplyDO> page = new Page<>(mpMessagePageReqVO.getPageNo(), mpMessagePageReqVO.getPageSize());
        Page<MpAutoReplyDO> mpAutoReplyDOPage = this.selectPage(page, mpAutoReplyDOLambdaQueryWrapper);
        return new PageResult(mpAutoReplyDOPage.getRecords(),mpAutoReplyDOPage.getTotal());
    }

}
