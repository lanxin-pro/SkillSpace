package cn.iocoder.educate.module.mp.framework.mp.core.util;

import cn.iocoder.educate.framework.common.util.validation.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;

import javax.validation.Validator;

/**
 * 公众号工具类
 *
 * @Author: j-sentinel
 * @Date: 2023/9/1 21:09
 */
@Slf4j
public class MpUtils {

    /**
     * 校验消息的格式是否符合要求
     *
     * @param type 类型
     * @param message 消息
     */
    public static void validateMessage(Validator validator, String type, Object message) {
        // 获得对应的校验 group
        Class<?> group;
        switch (type) {
            case WxConsts.XmlMsgType.TEXT:
                group = TextMessageGroup.class;
                break;
            case WxConsts.XmlMsgType.IMAGE:
                group = ImageMessageGroup.class;
                break;
            case WxConsts.XmlMsgType.VOICE:
                group = VoiceMessageGroup.class;
                break;
            case WxConsts.XmlMsgType.VIDEO:
                group = VideoMessageGroup.class;
                break;
            case WxConsts.XmlMsgType.NEWS:
                group = NewsMessageGroup.class;
                break;
            case WxConsts.XmlMsgType.MUSIC:
                group = MusicMessageGroup.class;
                break;
            default:
                log.error("[validateMessage][未知的消息类型({})]", message);
                throw new IllegalArgumentException("不支持的消息类型：" + type);
        }
        // 执行校验
        ValidationUtils.validate(validator, message, group);
    }

    /**
     * Text 类型的消息，参数校验 Group
     */
    public interface TextMessageGroup {}

    /**
     * Image 类型的消息，参数校验 Group
     */
    public interface ImageMessageGroup {}

    /**
     * Voice 类型的消息，参数校验 Group
     */
    public interface VoiceMessageGroup {}

    /**
     * Video 类型的消息，参数校验 Group
     */
    public interface VideoMessageGroup {}

    /**
     * News 类型的消息，参数校验 Group
     */
    public interface NewsMessageGroup {}

    /**
     * Music 类型的消息，参数校验 Group
     */
    public interface MusicMessageGroup {}

    /**
     * Click 类型的按钮，参数校验 Group
     */
    public interface ClickButtonGroup {}

    /**
     * View 类型的按钮，参数校验 Group
     */
    public interface ViewButtonGroup {}

    /**
     * MiniProgram 类型的按钮，参数校验 Group
     */
    public interface MiniProgramButtonGroup {}

    /**
     * SCANCODE_WAITMSG 类型的按钮，参数校验 Group
     */
    public interface ScanCodeWaitMsgButtonGroup {}

    /**
     * VIEW_LIMITED 类型的按钮，参数校验 Group
     */
    public interface ViewLimitedButtonGroup {}

}
