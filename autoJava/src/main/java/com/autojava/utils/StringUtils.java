package com.autojava.utils;

public class StringUtils {
    /**
     * 字段首字母转大写
     */
    public static String upperCaseFieldLetter(String fieldName){
        if(org.apache.commons.lang3.StringUtils.isEmpty(fieldName)){
            return fieldName;
        }
        return fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
    }

    /**
     * 字段首字母转小写
     */
    public static String lowerCaseFieldLetter(String fieldName){
        if(org.apache.commons.lang3.StringUtils.isEmpty(fieldName)){
            return fieldName;
        }
        return fieldName.substring(0,1).toLowerCase() + fieldName.substring(1);
    }
}
