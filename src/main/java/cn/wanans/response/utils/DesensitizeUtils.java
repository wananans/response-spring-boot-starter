package cn.wanans.response.utils;

import cn.wanans.response.enums.DesensitizeTypeEnum;
import org.springframework.util.StringUtils;

/**
 * @author w
 * @since 2022-09-13
 */
public class DesensitizeUtils {


    /**
     * 功能描述：姓名脱敏
     * 脱敏规则：只显示第一个汉字,比如李某某置换为李**, 李某置换为李*
     *
     * @param name 姓名
     * @return 脱敏数据
     */
    public static String desensitizedName(String name) {

        String result = name;

        if (StringUtils.hasText(name)) {
            char[] chars = name.toCharArray();

            if (chars.length == 2) {
                result = chars[0] + "*";
            } else if (chars.length > 2) {
                for (int i = 1; i < chars.length - 1; i++) {
                    chars[i] = '*';
                }
                result = new String(chars);
            }
        }
        return result;
    }


    /**
     * 功能描述：身份证号脱敏
     * 脱敏规则：保留前六后三, 适用于15位和18位身份证号
     *
     * @param idCard 身份证号
     * @return 脱敏数据
     */
    public static String desensitizedIDcard(String idCard) {

        String result = idCard;

        if (StringUtils.hasText(idCard)) {
            if (idCard.length() == 15) {
                result = idCard.replaceAll("(\\w{6})\\w*(\\w{3})", "$1******$2");
            }
            if (idCard.length() == 18) {
                result = idCard.replaceAll("(\\w{6})\\w*(\\w{3})", "$1*********$2");
            }
        }
        return result;
    }


    /**
     * 功能描述：手机号脱敏
     * 脱敏规则：保留前三后四, 比如15638296218置换为156****6218
     *
     * @param phone 手机号
     * @return 脱敏数据
     */
    public static String desensitizedPhone(String phone) {

        String result = phone;

        if (StringUtils.hasText(phone)) {
            result = phone.replaceAll("(\\w{3})\\w*(\\w{4})", "$1****$2");
        }

        return result;
    }

    /**
     * 功能描述：邮箱脱敏
     * 脱敏规则：保留前一和@后面的, 比如wanans@qq.com置换为w****@qq.com
     *
     * @param email 邮箱
     * @return 脱敏数据
     */
    public static String desensitizedEmail(String email) {

        String result = email;

        if (StringUtils.hasText(email)) {
            result = email.replaceAll("(^\\w)[^@]*(@.*$)", "$1****$2");
        }

        return result;
    }

    /**
     * 功能描述：地址脱敏
     * 脱敏规则：保留区前面的信息，如果不包含区，则保留前6位
     *
     * @param address 地址
     * @return 脱敏数据
     */
    public static String desensitizedAddress(String address) {

        String result = address;

        if (StringUtils.hasText(address)) {
            char[] chars = address.toCharArray();
            if (chars.length > 6) {
                if (address.contains("区")) {
                    int index = address.indexOf("区");
                    for (int i = index + 1; i < chars.length; i++) {
                        chars[i] = '*';
                    }
                } else {
                    for (int i = 6; i < chars.length; i++) {
                        chars[i] = '*';
                    }
                }
            } else {
                int index = chars.length / 5 * 3;
                for (int i = index; i < chars.length; i++) {
                    chars[i] = '*';
                }
            }

            result = new String(chars);
        }
        return result;
    }


    /**
     * 功能描述：密码脱敏
     * 脱敏规则：全部替换为*
     *
     * @param password 密码
     * @return 脱敏数据
     */
    public static String desensitizedPassword(String password) {

        String result = password;

        if (StringUtils.hasText(password)) {
            result = "********";
        }

        return result;
    }

    public static String dataMasking(DesensitizeTypeEnum annotType, String value) {

        String result = null;

        switch (annotType){
            case NAME:{
                result =  desensitizedName(value);
                break;
            }
            case ID_CARD:{
                result =  desensitizedIDcard(value);
                break;
            }
            case PHONE_NUMBER:{
                result =  desensitizedPhone(value);
                break;
            }
            case EMAIL:{
                result =  desensitizedEmail(value);
                break;
            }
            case ADDRESS:{
                result =  desensitizedAddress(value);
                break;
            }
            case PASSWORD:{
                result =  desensitizedPassword(value);
                break;
            }
        }
        return result;
    }
}
