package com.autojava.builder;

import com.autojava.bean.Constants;
import com.autojava.bean.FieldInfo;
import com.autojava.bean.TableInfo;
import com.autojava.utils.StringUtils;
import javafx.scene.effect.SepiaTone;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class BuildMapperXml {
    private static final Logger logger = LoggerFactory.getLogger(BuildMapperXml.class);

    public static void execute(TableInfo tableInfo){
        File folder = new File(Constants.PATH_MAPPERS_XML);
        if(!folder.exists()) folder.mkdirs();

        // 创建文件
        File file = new File(folder, tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS + ".xml");
        try {
            file.createNewFile();
        } catch (IOException e) {
            logger.error("创建xml文件失败", e);
            return;
        }

        // 写文件
        try(OutputStream out = new FileOutputStream(file);
            OutputStreamWriter ow = new OutputStreamWriter(out, "utf-8");
            BufferedWriter bw = new BufferedWriter(ow)) {

            generateXmlHead(tableInfo, bw);  // 生成xml头
            generateEntityProject(tableInfo, bw);  // 生成实体映射
            generateCommonQueryColumns(tableInfo, bw);  // 生成通用查询结果列
            generateBaseQueryConditions(tableInfo, bw);  // 生成基础查询条件
            generateExtendQueryConditions(tableInfo, bw);   // 生成扩展查询条件
            generateWholeQueryConditions(tableInfo, bw);    // 生成完整查询条件子句
            generateWholeQueryStatement(tableInfo, bw);     // 生成完整查询语句
            generateInsertStatement(tableInfo, bw);     // 生成插入语句
            generateInsertOrUpdateStatement(tableInfo, bw);     // 生成插入或更新语句
            generateInsertBatchStatement(tableInfo, bw);        // 生成批量插入语句
            generateInsertOrUpdateBatchStatement(tableInfo, bw);    // 生成插入或更新批量语句
            generatePrimaryKeyStatement(tableInfo, bw);     // 生成根据主键查询、删除、修改的语句

            bw.newLine();
            bw.write("</mapper>");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建mapper xml失败", e);
        }
    }


    /**
     * 生成xml头
     */
    private static void generateXmlHead(TableInfo tableInfo, BufferedWriter bw) throws IOException{
        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        bw.write("<!DOCTYPE mapper\n");
        bw.write("\t\tPUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n");
        bw.write("\t\t\"https://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
        bw.write("<mapper namespace=\""+ Constants.PACKAGE_MAPPERS + "." + tableInfo.getBeanName()+ Constants.SUFFIX_MAPPERS +"\">\n");
    }


    /**
     * 生成实体映射
     */
    private static void generateEntityProject(TableInfo tableInfo, BufferedWriter bw) throws IOException{
        bw.write("\t<!--实体映射-->\n");
        bw.write("\t<resultMap id=\"base_result_map\" type=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\">\n");

        // 判断是否由唯一主键
        FieldInfo idFieldInfo = null;
        Map<String, List<FieldInfo>> tableInfoList =  tableInfo.getKeyIndexMap();
        for(Map.Entry<String, List<FieldInfo>> entry : tableInfoList.entrySet()){
            if("PRIMARY".equals(entry.getKey())){
                List<FieldInfo> primaryFieldInfoList = entry.getValue();
                if(primaryFieldInfoList.size() == 1){
                    idFieldInfo = primaryFieldInfoList.get(0);
                    break;
                }
            }
        }
        for(FieldInfo fieldInfo : tableInfo.getFieldList()){
            // 生成注释
            bw.write("\t\t<!--" + fieldInfo.getComment() + "-->\n");
            // 生成字段
            String prefix;
            if(idFieldInfo != null && fieldInfo.getPropertyName().equals(idFieldInfo.getPropertyName())){
                prefix = "\t\t<id";
            } else{
                prefix = "\t\t<result";
            }
            bw.write(prefix + " column=\"" + fieldInfo.getFieldName() +
                    "\" property=\"" + fieldInfo.getPropertyName() + "\"/>\n");
        }

        bw.write("\t</resultMap>\n");
        bw.newLine();
    }


    /**
     * 生成通用查询结果列
     */
    private static void generateCommonQueryColumns(TableInfo tableInfo, BufferedWriter bw) throws IOException{
        bw.write("\t<!--通用查询结果列-->\n");
        bw.write("\t<sql id=\"base_column_list\">\n");

        StringJoiner sj = new StringJoiner(",");
        for(FieldInfo fieldInfo : tableInfo.getFieldList()){
            sj.add(fieldInfo.getFieldName());
        }
        bw.write("\t\t" + sj.toString() + "\n");

        bw.write("\t</sql>\n");
        bw.newLine();
    }


    /**
     * 生成基础查询条件
     */
    private static void generateBaseQueryConditions(TableInfo tableInfo, BufferedWriter bw) throws IOException{
        bw.write("\t<!--基础查询条件-->\n");
        bw.write("\t<sql id=\"base_condition_field\">\n");
        for(FieldInfo fieldInfo : tableInfo.getFieldList()){
            String emptyStrSuffix = "";
            if(ArrayUtils.contains(Constants.SQL_STRING_TYPES, fieldInfo.getSqlType())){
                emptyStrSuffix = " and query." + fieldInfo.getPropertyName() + " != ''";
            }
            bw.write("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null" + emptyStrSuffix + "\">\n");

//            if(ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType()) ||
//               ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())){
//                bw.write("\t\t\t<![CDATA[ and " + fieldInfo.getFieldName() + " = str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d') ]]>\n");
//            }else{
//                bw.write("\t\t\tand " + fieldInfo.getFieldName() + " = #{query." + fieldInfo.getPropertyName() + "}\n");
//            }
            bw.write("\t\t\tand " + fieldInfo.getFieldName() + " = #{query." + fieldInfo.getPropertyName() + "}\n");

            bw.write("\t\t</if>\n");
        }
        bw.write("\t</sql>\n");
        bw.newLine();
    }

    /**
     * 生成扩展查询条件
     */
    private static void generateExtendQueryConditions(TableInfo tableInfo, BufferedWriter bw) throws IOException{
        bw.write("\t<!--扩展查询条件-->\n");
        bw.write("\t<sql id=\"extend_condition_field\">\n");

        for(FieldInfo fieldInfo : tableInfo.getFieldList()){
            // 生成Fuzzy字段
            if(ArrayUtils.contains(Constants.SQL_STRING_TYPES, fieldInfo.getSqlType())){
                String fuzzyFieldName = fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_FUZZY;
                bw.write("\t\t<if test=\"query." + fuzzyFieldName + " != null and query." + fuzzyFieldName + " != ''\">\n");
                bw.write("\t\t\tand " + fieldInfo.getFieldName() + " like concat('%', #{query." + fuzzyFieldName + "}, '%')\n");
                bw.write("\t\t</if>\n");
            }

            // 生成时间起止字段
            if(ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType()) ||
               ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())){
                String timeStartFieldName = fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_START;
                String timeEndFieldName = fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_END;

                bw.write("\t\t<if test=\"query." + timeStartFieldName + " != null and query." + timeStartFieldName + " != ''\">\n");
                bw.write("\t\t\t<![CDATA[ and " + fieldInfo.getFieldName() + " >= str_to_date(#{query." + timeStartFieldName + "}, '%Y-%m-%d') ]]>\n");
                bw.write("\t\t</if>\n");

                bw.write("\t\t<if test=\"query." + timeEndFieldName + " != null and query." + timeEndFieldName + " != ''\">\n");
                bw.write("\t\t\t<![CDATA[ and " + fieldInfo.getFieldName() + " < date_sub(str_to_date(#{query." + timeEndFieldName + "}, '%Y-%m-%d'),interval -1 day) ]]>\n");
                bw.write("\t\t</if>\n");
            }
        }

        bw.write("\t</sql>\n");
        bw.newLine();
    }


    /**
     * 生成合并后的完整查询条件子句
     */
    private static void generateWholeQueryConditions(TableInfo tableInfo, BufferedWriter bw) throws IOException{
        bw.write("\t<!--查询条件的where子句-->\n");
        bw.write("\t<sql id=\"query_condition\">\n");
        bw.write("\t\t<where>\n");
        bw.write("\t\t\t<include refid=\"base_condition_field\"/>\n");
        bw.write("\t\t\t<include refid=\"extend_condition_field\"/>\n");
        bw.write("\t\t</where>\n");
        bw.write("\t</sql>\n");
        bw.newLine();
    }

    /**
     * 生成完整的查询语句
     */
    private static void generateWholeQueryStatement(TableInfo tableInfo, BufferedWriter bw) throws IOException{
        bw.write("\t<!--查询集合的语句-->\n");
        bw.write("\t<select id=\"selectList\" resultMap=\"base_result_map\">\n");
        bw.write("\t\tSELECT\n");
        bw.write("\t\t<include refid=\"base_column_list\"/>\n");
        bw.write("\t\tFROM " + tableInfo.getTableName() + "\n");
        bw.write("\t\t<include refid=\"query_condition\"/>\n");
        bw.write("\t\t<if test=\"query.orderBy != null\">\n");
        bw.write("\t\t\tORDER BY ${query.orderBy}\n");
        bw.write("\t\t</if>\n");
        bw.write("\t\t<if test=\"query.simplePage != null\">\n");
        bw.write("\t\t\tLIMIT #{query.simplePage.start},#{query.simplePage.offset}\n");
        bw.write("\t\t</if>\n");
        bw.write("\t</select>\n");
        bw.newLine();

        bw.write("\t<!--查询数量的语句-->\n");
        bw.write("\t<select id=\"selectCount\" resultType=\"java.lang.Long\">\n");
        bw.write("\t\tSELECT count(1) FROM " + tableInfo.getTableName() + "\n");
        bw.write("\t\t<include refid=\"query_condition\"/>\n");
        bw.write("\t</select>\n");
        bw.newLine();

    }

    /**
     * 生成插入语句
     */
    private static void generateInsertStatement(TableInfo tableInfo, BufferedWriter bw) throws IOException{
        bw.write("\t<!--插入的语句 (匹配有值的字段)-->\n");
        bw.write("\t<insert id=\"insert\" parameterType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\">\n");

        // 返回插入列的自增ID(如果存在)
        FieldInfo autoIncrementFieldInfo = null;
        for(FieldInfo fieldInfo : tableInfo.getFieldList()){
            if(fieldInfo.getAutoIncrement()){
                autoIncrementFieldInfo = fieldInfo;

                bw.write("\t\t<selectKey resultType=\"java.lang.Integer\" keyProperty=\"bean.id\" order=\"AFTER\">\n");
                bw.write("\t\t\tSELECT LAST_INSERT_ID()\n");
                bw.write("\t\t</selectKey>\n");

                break;
            }
        }

        bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + "\n");
        bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
        for(FieldInfo fieldInfo : tableInfo.getFieldList()){
            bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
            bw.write("\t\t\t\t" + fieldInfo.getFieldName() + ",\n");
            bw.write("\t\t\t</if>\n");
        }
        bw.write("\t\t</trim>\n");

        bw.write("\t\tVALUES\n");
        bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
        for(FieldInfo fieldInfo : tableInfo.getFieldList()){
            bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
            bw.write("\t\t\t\t#{bean." + fieldInfo.getPropertyName() + "},\n");
            bw.write("\t\t\t</if>\n");
        }
        bw.write("\t\t</trim>\n");
        bw.write("\t</insert>\n");

        bw.newLine();

    }

    /**
     * 生成插入或更新语句
     */
    private static void generateInsertOrUpdateStatement(TableInfo tableInfo, BufferedWriter bw) throws IOException{
        bw.write("\t<!--插入或更新的语句 (匹配有值的字段)-->\n");
        bw.write("\t<insert id=\"insertOrUpdate\" parameterType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\">\n");
        bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + "\n");
        bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
        for(FieldInfo fieldInfo : tableInfo.getFieldList()){
            bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
            bw.write("\t\t\t\t" + fieldInfo.getFieldName() + ",\n");
            bw.write("\t\t\t</if>\n");
        }
        bw.write("\t\t</trim>\n");

        bw.write("\t\tVALUES\n");
        bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
        for(FieldInfo fieldInfo : tableInfo.getFieldList()){
            bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
            bw.write("\t\t\t\t#{bean." + fieldInfo.getPropertyName() + "},\n");
            bw.write("\t\t\t</if>\n");
        }
        bw.write("\t\t</trim>\n");

        bw.write("\t\tON DUPLICATE KEY UPDATE\n");
        // 唯一索引不允许被修改
        Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
        Set<String> keyIndexNameSet = new HashSet<>();
        for(Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()){
            List<FieldInfo> keyIndexFieldInfoList = entry.getValue();
            for(FieldInfo fieldInfo : keyIndexFieldInfoList){
                keyIndexNameSet.add(fieldInfo.getFieldName());
            }
        }
        bw.write("\t\t<trim prefix=\"\" suffix=\"\" suffixOverrides=\",\">\n");
        for(FieldInfo fieldInfo : tableInfo.getFieldList()){
            if(keyIndexNameSet.contains(fieldInfo.getFieldName())) continue;
            bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
            bw.write("\t\t\t" + fieldInfo.getFieldName() + " = VALUES("+ fieldInfo.getFieldName()+"),\n");
            bw.write("\t\t\t</if>\n");
        }
        bw.write("\t\t</trim>\n");

        bw.write("\t</insert>\n");
        bw.newLine();
    }


    /**
     * 生成批量插入的语句
     */
    private static void generateInsertBatchStatement(TableInfo tableInfo, BufferedWriter bw) throws IOException{
        bw.write("\t<!--批量插入的语句-->\n");
        bw.write("\t<insert id=\"insertBatch\" parameterType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\" useGeneratedKeys=\"true\" keyProperty=\"id\">\n");

        StringJoiner fieldNameJoiner = new StringJoiner(",","(",")");
        for(FieldInfo fieldInfo : tableInfo.getFieldList()){
            if(fieldInfo.getAutoIncrement()){
                continue;
            }
            fieldNameJoiner.add(fieldInfo.getFieldName());
        }
        bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + fieldNameJoiner.toString() + "\n");
        StringJoiner valueNameJoiner = new StringJoiner(",");
        for(FieldInfo fieldInfo : tableInfo.getFieldList()){
            if(fieldInfo.getAutoIncrement()){
                continue;
            }
            valueNameJoiner.add("#{item." + fieldInfo.getPropertyName() + "}");
        }
        bw.write("\t\tVALUES\n");
        bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">\n");
        bw.write("\t\t\t(" + valueNameJoiner.toString() + ")\n");
        bw.write("\t\t</foreach>\n");
        bw.write("\t</insert>\n");
        bw.newLine();
    }


    /**
     * 生成批量插入或更新的语句
     */
    private static void generateInsertOrUpdateBatchStatement(TableInfo tableInfo, BufferedWriter bw) throws IOException{
        bw.write("\t<!--批量插入或更新的语句-->\n");
        bw.write("\t<insert id=\"insertOrUpdateBatch\" parameterType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\" useGeneratedKeys=\"true\" keyProperty=\"id\">\n");
        StringJoiner fieldNameJoiner = new StringJoiner(",","(",")");
        for(FieldInfo fieldInfo : tableInfo.getFieldList()){
            if(fieldInfo.getAutoIncrement()){
                continue;
            }
            fieldNameJoiner.add(fieldInfo.getFieldName());
        }
        bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + fieldNameJoiner.toString() + "\n");
        StringJoiner valueNameJoiner = new StringJoiner(",");
        for(FieldInfo fieldInfo : tableInfo.getFieldList()){
            if(fieldInfo.getAutoIncrement()){
                continue;
            }
            valueNameJoiner.add("#{item." + fieldInfo.getPropertyName() + "}");
        }
        bw.write("\t\tVALUES\n");
        bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">\n");
        bw.write("\t\t\t(" + valueNameJoiner.toString() + ")\n");
        bw.write("\t\t</foreach>\n");

        bw.write("\t\tON DUPLICATE KEY UPDATE\n");
        StringJoiner updateNameJoiner = new StringJoiner(",\n\t\t\t");
        for(FieldInfo fieldInfo : tableInfo.getFieldList()){
            if(fieldInfo.getAutoIncrement()){
                continue;
            }
            updateNameJoiner.add(fieldInfo.getFieldName() + " = VALUES("+ fieldInfo.getFieldName()+")");
        }
        bw.write("\t\t\t" + updateNameJoiner.toString() + "\n");
        bw.write("\t</insert>\n");
        bw.newLine();
    }


    /**
     * 生成根据主键查询、删除、修改的语句
     */
    private static void generatePrimaryKeyStatement(TableInfo tableInfo, BufferedWriter bw) throws IOException{
        Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
        for(Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
            List<FieldInfo> keyFieldList = entry.getValue();

            // 生成方法名和注释
            StringJoiner keyMethodNameJoiner = new StringJoiner("And");
            StringJoiner CommentNameJoiner = new StringJoiner("和", "根据", "");
            for(FieldInfo fieldInfo : keyFieldList){
                keyMethodNameJoiner.add(StringUtils.upperCaseFieldLetter(fieldInfo.getPropertyName()));
                CommentNameJoiner.add(StringUtils.upperCaseFieldLetter(fieldInfo.getPropertyName()));
            }

            // 生成where子句
            StringJoiner whereJoiner = new StringJoiner(" AND ");
            for(FieldInfo fieldInfo : keyFieldList){
                whereJoiner.add(fieldInfo.getFieldName() + " = #{" + fieldInfo.getPropertyName() + "}");
            }


            // 生成查询语句
            bw.write("\t<!--" + CommentNameJoiner.toString() + "查询-->\n");
            bw.write("\t<select id=\"selectBy" + keyMethodNameJoiner.toString() + "\"  resultMap=\"base_result_map\">\n");
            bw.write("\t\tSELECT <include refid=\"base_column_list\"/> FROM " + tableInfo.getTableName() + " WHERE " + whereJoiner.toString()+"\n");
            bw.write("\t</select>\n");
            bw.newLine();

            // 生成删除语句
            bw.write("\t<!--" + CommentNameJoiner.toString() + "删除-->\n");
            bw.write("\t<delete id=\"deleteBy" + keyMethodNameJoiner.toString() + "\" >\n");
            bw.write("\t\tDELETE FROM " + tableInfo.getTableName() + " WHERE " + whereJoiner.toString()+"\n");
            bw.write("\t</delete>\n");
            bw.newLine();

            // 生成修改语句
            bw.write("\t<!--" + CommentNameJoiner.toString() + "修改-->\n");
            bw.write("\t<update id=\"updateBy" + keyMethodNameJoiner.toString() + "\" parameterType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\">\n");
            bw.write("\t\tUPDATE " + tableInfo.getTableName() + "\n");
            bw.write("\t\t<set>\n");
            for(FieldInfo fieldInfo : tableInfo.getFieldList()){
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
                bw.write("\t\t\t\t" + fieldInfo.getFieldName() + " = #{bean." + fieldInfo.getPropertyName() + "},\n");
                bw.write("\t\t\t</if>\n");
            }
            bw.write("\t\t</set>\n");
            bw.write("\t\tWHERE " + whereJoiner.toString()+"\n");
            bw.write("\t</update>\n");
            bw.newLine();
        }
    }

}