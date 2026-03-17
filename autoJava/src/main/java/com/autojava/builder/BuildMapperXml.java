package com.autojava.builder;

import com.autojava.bean.Constants;
import com.autojava.bean.FieldInfo;
import com.autojava.bean.TableInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

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

}