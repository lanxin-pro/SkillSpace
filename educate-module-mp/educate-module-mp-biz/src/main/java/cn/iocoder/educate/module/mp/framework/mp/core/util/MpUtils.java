package cn.iocoder.educate.module.mp.framework.mp.core.util;

import cn.hutool.core.util.StrUtil;
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

    public static void validateButton(Validator validator, String type, String messageType, Object button) {
        if (StrUtil.isBlank(type)) {
            return;
        }
        // 获得对应的校验 group
        Class<?> group;
        switch (type) {
            case WxConsts.MenuButtonType.CLICK:
                group = ClickButtonGroup.class;
                // 需要额外校验回复的消息格式
                validateMessage(validator, messageType, button);
                break;
            case WxConsts.MenuButtonType.VIEW:
                group = ViewButtonGroup.class;
                break;
            case WxConsts.MenuButtonType.MINIPROGRAM:
                group = MiniProgramButtonGroup.class;
                break;
            case WxConsts.MenuButtonType.SCANCODE_WAITMSG:
                group = ScanCodeWaitMsgButtonGroup.class;
                // 需要额外校验回复的消息格式
                validateMessage(validator, messageType, button);
                break;
            case "article_" + WxConsts.MenuButtonType.VIEW_LIMITED:
                group = ViewLimitedButtonGroup.class;
                break;
            // 不用校验，直接 return 即可
            case WxConsts.MenuButtonType.SCANCODE_PUSH:
            case WxConsts.MenuButtonType.PIC_SYSPHOTO:
            case WxConsts.MenuButtonType.PIC_PHOTO_OR_ALBUM:
            case WxConsts.MenuButtonType.PIC_WEIXIN:
            case WxConsts.MenuButtonType.LOCATION_SELECT:
                return;
            default:
                log.error("[validateButton][未知的按钮({})]", button);
                throw new IllegalArgumentException("不支持的按钮类型：" + type);
        }
        // 执行校验
        ValidationUtils.validate(validator, button, group);
    }

}
