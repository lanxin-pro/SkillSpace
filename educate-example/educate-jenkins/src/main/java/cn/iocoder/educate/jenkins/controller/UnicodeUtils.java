package cn.iocoder.educate.jenkins.controller;

/**
 * @Author: 董伟豪
 * @Date: 2023/1/5 9:44
 */
public class UnicodeUtils {

    //Unicode转中文方法
    private static String unicodeToCn(String unicode) {
        /** 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格*/
        String[] strs = unicode.split("\\\\u");
        String returnStr = "";
        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
        for (int i = 1; i < strs.length; i++) {
            returnStr += (char) Integer.valueOf(strs[i], 16).intValue();
        }
        return returnStr;
    }

    //中文转Unicode
    private static String cnToUnicode(String cn) {
        char[] chars = cn.toCharArray();
        String returnStr = "";
        for (int i = 0; i < chars.length; i++) {
            returnStr += "\\u" + Integer.toString(chars[i], 16);
        }
        return returnStr;
    }

    public static void main(String[] args) {
        //Unicode码
        String aa = "\\u554a\\u554a\\u554a";
        //转中文
        String cnAa = unicodeToCn(aa);
        System.out.println("Unicode转中文结果： "+cnAa);
        String unicodeAa = cnToUnicode("前一世我为无间修罗榜的无上至尊修罗，最终惨遭27  位坠日高手围杀，我殊死拼搏，以伤换伤杀了20人逃 到昔日至交府邸，却没想到昔日至交好友竟然卑鄙背后 偷袭，毁我至尊金甲，夺我金阔甲药全无，经我重生为天人城陨星，入赘天人城无敌蚀月世家。却惨遭实力未婚妻退婚，这天人城不待也罢！三十年河东，三十年河西，V我50细听我的复仇大计！");
        System.out.println("中文转Unicode结果： "+unicodeAa);
    }
}
