package com.autojava.bean;

import com.autojava.utils.PropertiesUtils;

/**
 * 配置信息
 */
public class Constants {
    public static Boolean IGNORE_TABLE_PRIFIX;
    public static String SUFFIX_BEAN_PARAM;

    static {
        IGNORE_TABLE_PRIFIX = Boolean.valueOf(PropertiesUtils.getString("ignore.table.prefix"));
        SUFFIX_BEAN_PARAM = PropertiesUtils.getString("suffix.bean.param");
    }

    public static final String[] SQL_DATE_TIME_TYPES = new String[]{"datetime", "timestamp"};
    public static final String[] SQL_DATE_TYPES = new String[]{"date"};
    public static final String[] SQL_DECIMAL_TYPES = new String[]{"decimal", "double", "float", "numeric"};
    public static final String[] SQL_STRING_TYPES = new String[]{"char", "varchar", "text", "mediumtext", "longtext"};
    public static final String[] SQL_INTEGER_TYPES = new String[]{"int", "tinyint", "smallint", "mediumint"};
    public static final String[] SQL_LONG_TYPES = new String[]{"bigint"};
}
