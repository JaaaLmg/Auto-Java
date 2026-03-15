package com.autojava.builder;

import com.autojava.bean.Constants;
import com.autojava.bean.FieldInfo;
import com.autojava.bean.TableInfo;
import com.autojava.utils.JsonUtils;
import com.autojava.utils.PropertiesUtils;
import com.autojava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取数据库表信息的工具类
 */
public class BuildTable {
    private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);
    private static Connection conn = null;
    private static String SQL_SHOW_TABLE_STATUS = "show table status";
    private static String SQL_SHOW_TABLE_FIELDS = "show full fields from %s";
    private static String SQL_SHOW_TABLE_INDEX = "show index from %s";

    static {
        String driverName = PropertiesUtils.getString("db.driver.name");
        String url = PropertiesUtils.getString("db.url");
        String user = PropertiesUtils.getString("db.username");
        String password = PropertiesUtils.getString("db.password");

        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            logger.error("获取数据库连接失败",e);
        }

    }

    /**
     * 获取数据库表信息
     */
    public static List<TableInfo> getTables(){
        PreparedStatement ps = null;
        ResultSet tableResult = null;
        List<TableInfo> tableInfoList = new ArrayList<>();
        try {
            ps = conn.prepareStatement(SQL_SHOW_TABLE_STATUS);
            tableResult = ps.executeQuery();
            while (tableResult.next()){
                String tableName = tableResult.getString("name");
                String comment = tableResult.getString("comment");
                //logger.info("tableName：{} , comment:{}",tableName,comment);

                String beanName = tableName;
                if(Constants.IGNORE_TABLE_PRIFIX){
                    beanName = beanName.substring(beanName.indexOf("_")+1);
                }
                beanName = processField(beanName, true);
                //logger.info("beanName:{}",beanName);

                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setBeanName(beanName);
                tableInfo.setComment(comment);
                tableInfo.setBeanParamName(beanName + Constants.SUFFIX_BEAN_QUERY);
                //logger.info("表名:{} , 注释:{} , JavaBean:{} , JavaParamBean:{}", tableInfo.getTableName(), tableInfo.getComment(), tableInfo.getBeanName(), tableInfo.getBeanParamName());

                readFieldInfo(tableInfo);

                getKeyIndexInfo(tableInfo);

                logger.info("tableInfo:{}", JsonUtils.convertObj2Json(tableInfo));
                tableInfoList.add(tableInfo);
            }
        }catch (Exception e){
            logger.error("获取表信息失败",e);
        }finally {
            if(tableResult != null){
                try {
                    tableResult.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return tableInfoList;
    }

    /**
     * 获取数据库表字段信息
     */
    public static void readFieldInfo(TableInfo tableInfo){
        PreparedStatement ps = null;
        ResultSet fieldResult = null;
        List<FieldInfo> fieldInfoList = new ArrayList<>();
        tableInfo.setFieldList(fieldInfoList);
        try {
            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_FIELDS, tableInfo.getTableName()));
            fieldResult = ps.executeQuery();
            while (fieldResult.next()){
                String field = fieldResult.getString("field");
                String type = fieldResult.getString("type");
                String extra = fieldResult.getString("extra");
                String comment = fieldResult.getString("comment");
                if(type.indexOf('(')>0){
                    type = type.substring(0,type.indexOf('('));
                }
                String propertyName = processField(field, false);

                FieldInfo fieldInfo = new FieldInfo();
                fieldInfoList.add(fieldInfo);

                fieldInfo.setFieldName(field);
                fieldInfo.setComment(comment);
                fieldInfo.setSqlType(type);
                fieldInfo.setAutoIncrement(extra.equalsIgnoreCase("auto_increment"));
                fieldInfo.setPropertyName(propertyName);
                fieldInfo.setJavaType(processJavaType(type));

                if(ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type)){
                    tableInfo.setHaveDateTime(true);
                }
                if(ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)){
                    tableInfo.setHaveDate(true);
                }
                if(ArrayUtils.contains(Constants.SQL_DECIMAL_TYPES, type)){
                    tableInfo.setHaveBigDecimal(true);
                }
                tableInfo.setFieldList(fieldInfoList);
            }
        }catch (Exception e){
            logger.error("获取表信息失败",e);
        }finally {
            if(fieldResult != null){
                try {
                    fieldResult.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 获取数据库表索引信息
     */
    public static void getKeyIndexInfo(TableInfo tableInfo){
        PreparedStatement ps = null;
        ResultSet fieldResult = null;
        try {
            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_INDEX, tableInfo.getTableName()));
            fieldResult = ps.executeQuery();

            Map<String, FieldInfo> tempMap = new HashMap();
            for(FieldInfo fieldInfo : tableInfo.getFieldList()){
                tempMap.put(fieldInfo.getFieldName(), fieldInfo);
            }

            while (fieldResult.next()){
                String keyName = fieldResult.getString("key_name");
                Integer nonUnique = fieldResult.getInt("non_unique");
                String columnName = fieldResult.getString("column_name");
                if(nonUnique == 1) continue;
                List<FieldInfo> keyFieldList = tableInfo.getKeyIndexMap().get(keyName);
                if(null == keyFieldList){
                    keyFieldList = new ArrayList<>();
                    tableInfo.getKeyIndexMap().put(keyName, keyFieldList);
                }
                keyFieldList.add(tempMap.get(columnName));
            }
        }catch (Exception e){
            logger.error("读取索引失败",e);
        }finally {
            if(fieldResult != null){
                try {
                    fieldResult.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 将数据库表字段名转为java属性名
     */
    private static String processField(String fieldName, Boolean upperCaseFirstLetter){
        StringBuffer sb = new StringBuffer();
        String[] subWords = fieldName.split("_");
        sb.append(upperCaseFirstLetter ? StringUtils.upperCaseFieldLetter(subWords[0]) : subWords[0]);
        for(int i=1; i< subWords.length; i++){
            sb.append(StringUtils.upperCaseFieldLetter(subWords[i]));
        }
        return sb.toString();
    }

    /**
     * 将数据库字段类型转为java类型
     */
    private static String processJavaType(String type){
        if(ArrayUtils.contains(Constants.SQL_INTEGER_TYPES, type)){
            return "Integer";
        }else if(ArrayUtils.contains(Constants.SQL_LONG_TYPES, type)){
            return "Long";
        }else if(ArrayUtils.contains(Constants.SQL_STRING_TYPES, type)){
            return "String";
        }else if(ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type) || ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)){
            return "Date";
        }else if(ArrayUtils.contains(Constants.SQL_DECIMAL_TYPES, type)){
            return "BigDecimal";
        }else{
            throw new RuntimeException("无法识别的类型："+type);
        }
    }
}
