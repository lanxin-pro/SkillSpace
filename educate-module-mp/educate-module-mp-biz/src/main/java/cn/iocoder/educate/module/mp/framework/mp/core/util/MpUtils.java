package cn.iocoder.educate.module.mp.framework.mp.core.util;

/**
 * 公众号工具类
 *
 * @Author: j-sentinel
 * @Date: 2023/9/1 21:09
 */
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

}
