package cn.iocoder.educate.module.infra.mq.message.file;

import cn.iocoder.educate.framework.mq.core.pugsub.AbstractChannelMessage;
import lombok.Data;

/**
 * 文件配置数据刷新 Message
 *
 * @Author: j-sentinel
 * @Date: 2023/8/4 10:15
 */
@Data
public class FileConfigRefreshMessage extends AbstractChannelMessage {

    @Override
    public String getChannel() {
        return "infra.file-config.refresh";
    }

}
