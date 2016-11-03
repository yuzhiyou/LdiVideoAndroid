package com.zhenaixuanyan.app.videos.Utils;

import java.text.ParseException;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Forrest on 16/5/17.
 * @author forrest
 */
public class ValidateUtil {
    /**
     * 验证是否是手机号码
     *
     * @param mobile  手机号码
     * @return YES/NO
     */
    public static boolean isMobileNumber(String mobile){
        Pattern pattern = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
        Matcher matcher = pattern.matcher(mobile);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 验证码是否有效
     *
     * @param checkCode 验证码
     * @return YES/NO
     */
    public static boolean isCheckCode(String checkCode){
        Pattern pattern = Pattern.compile("\\d{6}$");
        Matcher matcher = pattern.matcher(checkCode);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }
}
