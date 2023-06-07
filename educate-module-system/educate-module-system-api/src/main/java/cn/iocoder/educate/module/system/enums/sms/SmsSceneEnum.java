package cn.iocoder.educate.module.system.enums.sms;

import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.educate.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Arrays;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/19 11:18
 */
@Getter
@AllArgsConstructor
public enum SmsSceneEnum implements IntArrayValuable {

    MEMBER_LOGIN(1, "user-sms-login", "会员用户 - 手机号登陆"),
    MEMBER_UPDATE_MOBILE(2, "user-sms-reset-password", "会员用户 - 修改手机"),
    MEMBER_FORGET_PASSWORD(3, "user-sms-update-mobile", "会员用户 - 忘记密码"),

    ADMIN_ALIYUN_MEMBER_LOGIN(21, "admin-aliyun-sms-login", "后台用户 - 手机号登录"),
    ADMIN_DEBUG_MEMBER_LOGIN(22, "admin-debug-ding-sms-login", "后台用户 - 手机号登录");

    /**
     * 验证场景的编号
     */
    private final Integer scene;

    /**
     * 模版编码
     */
    private final String templateCode;

    /**
     * 描述
     */
    private final String description;

    /**
     * 根据scene编号返回我的全部信息
     * @param scene
     * @return
     */
    public static SmsSceneEnum getCodeByScene(Integer scene) {
        return ArrayUtil.firstMatch(sceneEnum -> sceneEnum.getScene().equals(scene),
                values());
    }

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(SmsSceneEnum::getScene).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
