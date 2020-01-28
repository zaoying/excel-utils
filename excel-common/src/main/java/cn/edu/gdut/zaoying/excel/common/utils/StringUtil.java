package cn.edu.gdut.zaoying.excel.common.utils;

import javax.validation.constraints.NotBlank;

public class StringUtil {

    public static String firstLetter2LowerCase(@NotBlank String src){
        char[] chars = src.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return String.valueOf(chars);
    }

    public static String firstLetter2UpperCase(@NotBlank String src){
        char[] chars = src.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return String.valueOf(chars);
    }
}
