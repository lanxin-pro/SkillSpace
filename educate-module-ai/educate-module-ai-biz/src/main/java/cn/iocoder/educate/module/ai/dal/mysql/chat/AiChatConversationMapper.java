package cn.iocoder.educate.module.ai.dal.mysql.chat;

import cn.iocoder.educate.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.educate.module.ai.dal.dataobject.chat.AiChatConversationDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author j-sentinel
 * @date 2024/7/7 15:47
 */
@Mapper
public interface AiChatConversationMapper extends BaseMapper<AiChatConversationDO> {

    default List<AiChatConversationDO> selectListByUserId(Long loginUserId){
        LambdaQueryWrapper<AiChatConversationDO> aiChatConversationDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        aiChatConversationDOLambdaQueryWrapper.eq(AiChatConversationDO::getUserId, loginUserId);
        return this.selectList(aiChatConversationDOLambdaQueryWrapper);
    }
}
