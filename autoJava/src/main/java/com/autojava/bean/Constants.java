package com.autojava.bean;

import com.autojava.utils.PropertiesUtils;

/**
 * 配置信息
 */
public class Constants {
    // 项目相关
    public static String PROJECT_AUTHOR;

    // 命名相关
    public static Boolean IGNORE_TABLE_PRIFIX;
    public static String SUFFIX_BEAN_QUERY;
    public static String SUFFIX_BEAN_QUERY_FUZZY;
    public static String SUFFIX_BEAN_QUERY_TIME_START;
    public static String SUFFIX_BEAN_QUERY_TIME_END;

    // 属性相关
    public static String IGNORE_BEAN_TOJSON_FIELD;
    public static String IGNORE_BEAN_TOJSON_ANNOTATION;
    public static String IGNORE_BEAN_TOJSON_IMPORT;

    // getter 和 setter 方法
    public static Boolean IGNORE_GETTER_SETTER;
    public static Boolean IGNORE_TO_STRING;

    // 日期序列化、反序列化
    public static String BEAN_DATE_FORMAT_ANNOTATION;
    public static String BEAN_DATE_FORMAT_IMPORT;
    public static String BEAN_DATE_UNFORMAT_ANNOTATION;
    public static String BEAN_DATE_UNFORMAT_IMPORT;

    // 路径相关
    public static String PATH_BASE;
    public static String PATH_JAVA = "java";
    public static String PATH_JAVA_BASE;
    public static String PATH_PO;
    public static String PATH_QUERY;
    public static String PATH_UTILS;
    public static String PATH_ENUMS;

    // 包名相关
    public static String PATH_RESOURCE = "resources";
    public static String PACKAGE_BASE;
    public static String PACKAGE_PO;
    public static String PACKAGE_QUERY;
    public static String PACKAGE_UTILS;
    public static String PACKAGE_ENUMS;

    static {
        PROJECT_AUTHOR = PropertiesUtils.getString("project.author");

        IGNORE_TABLE_PRIFIX = Boolean.valueOf(PropertiesUtils.getString("ignore.table.prefix"));
        SUFFIX_BEAN_QUERY = PropertiesUtils.getString("suffix.bean.query");
        SUFFIX_BEAN_QUERY_FUZZY = PropertiesUtils.getString("suffix.bean.query.fuzzy");
        SUFFIX_BEAN_QUERY_TIME_START = PropertiesUtils.getString("suffix.bean.query.time.start");
        SUFFIX_BEAN_QUERY_TIME_END = PropertiesUtils.getString("suffix.bean.query.time.end");

        IGNORE_GETTER_SETTER = Boolean.valueOf(PropertiesUtils.getString("ignore.bean.getterSetter"));
        IGNORE_TO_STRING = Boolean.valueOf(PropertiesUtils.getString("ignore.bean.toString"));

        // 需要忽略的属性
        IGNORE_BEAN_TOJSON_FIELD = PropertiesUtils.getString("ignore.bean.tojson.field");
        IGNORE_BEAN_TOJSON_ANNOTATION = PropertiesUtils.getString("ignore.bean.tojson.annotation");
        IGNORE_BEAN_TOJSON_IMPORT = PropertiesUtils.getString("ignore.bean.tojson.import");

        // 日期的序列化、反序列化
        BEAN_DATE_FORMAT_ANNOTATION = PropertiesUtils.getString("bean.date.format.annotation");
        BEAN_DATE_FORMAT_IMPORT = PropertiesUtils.getString("bean.date.format.import");
        BEAN_DATE_UNFORMAT_ANNOTATION = PropertiesUtils.getString("bean.date.unformat.annotation");
        BEAN_DATE_UNFORMAT_IMPORT = PropertiesUtils.getString("bean.date.unformat.import");

        // 基础路径
        PACKAGE_BASE = PropertiesUtils.getString("package.base");
        PACKAGE_UTILS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.utils");
        PACKAGE_ENUMS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.enums");
        PATH_BASE = PropertiesUtils.getString("path.base");
        PATH_JAVA_BASE = PATH_BASE + PATH_JAVA;

        // PO相关路径
        PACKAGE_PO = PACKAGE_BASE + "." + PropertiesUtils.getString("package.po");
        PATH_PO = PATH_JAVA_BASE + "/" + PACKAGE_PO.replace(".", "/");

        // Query相关路径
        PACKAGE_QUERY = PACKAGE_BASE + "." + PropertiesUtils.getString("package.query");
        PATH_QUERY = PATH_JAVA_BASE + "/" + PACKAGE_QUERY.replace(".", "/");

        PATH_UTILS = PATH_JAVA_BASE + "/" + PACKAGE_UTILS.replace(".", "/");
        PATH_ENUMS = PATH_JAVA_BASE + "/" + PACKAGE_ENUMS.replace(".", "/");
    }

    public static final String[] SQL_DATE_TIME_TYPES = new String[]{"datetime", "timestamp"};
    public static final String[] SQL_DATE_TYPES = new String[]{"date"};
    public static final String[] SQL_DECIMAL_TYPES = new String[]{"decimal", "double", "float", "numeric"};
    public static final String[] SQL_STRING_TYPES = new String[]{"char", "varchar", "text", "mediumtext", "longtext"};
    public static final String[] SQL_INTEGER_TYPES = new String[]{"int", "tinyint", "smallint", "mediumint"};
    public static final String[] SQL_LONG_TYPES = new String[]{"bigint"};

    public static void main(String[] args) {
        System.out.println(PATH_JAVA_BASE);
        System.out.println(PACKAGE_BASE);
        System.out.println(PATH_PO);
    }
}
