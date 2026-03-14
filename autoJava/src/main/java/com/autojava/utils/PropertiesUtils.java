package com.autojava.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

// 读取properties文件的工具类
public class PropertiesUtils {
    private static Properties props = new Properties();
    private static Map<String, String> PROPER_MAP = new ConcurrentHashMap<>();

    static {
        InputStream is = null;
        try {
            is = PropertiesUtils.class.getClassLoader().getResourceAsStream("application.properties");  // 获取application.properties文件
            props.load(new InputStreamReader(is, StandardCharsets.UTF_8));

            // 先加载所有原始值
            Iterator<Object> iterator = props.keySet().iterator();
            while(iterator.hasNext()){
                String key = (String) iterator.next();
                PROPER_MAP.put(key, props.getProperty(key));
            }

            // 解析变量引用
            for (String key : PROPER_MAP.keySet()) {
                PROPER_MAP.put(key, resolveValue(PROPER_MAP.get(key)));
            }
        } catch (Exception e) {

        } finally {
            if(is != null){
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解析变量引用
     */
    private static String resolveValue(String value) {
        if (value == null) {
            return null;
        }

        // 解析 ${...} 变量
        int startIndex = value.indexOf("${");
        while (startIndex != -1) {
            int endIndex = value.indexOf("}", startIndex);
            if (endIndex == -1) {
                break;
            }

            String varName = value.substring(startIndex + 2, endIndex);
            String varValue = PROPER_MAP.get(varName);

            if (varValue != null) {
                value = value.substring(0, startIndex) + varValue + value.substring(endIndex + 1);
                startIndex = value.indexOf("${", startIndex + varValue.length());
            } else {
                startIndex = value.indexOf("${", endIndex);
            }
        }

        return value;
    }

    public static String getString(String key){
        return PROPER_MAP.get(key);
    }

    public static void main(String[] args) {
        System.out.println(getString("db.url"));
    }
}
