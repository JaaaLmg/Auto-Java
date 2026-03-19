package com.autojava.builder;

import com.autojava.bean.Constants;
import com.autojava.bean.FieldInfo;
import com.autojava.bean.TableInfo;
import com.autojava.utils.DateUtils;
import com.autojava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.StringJoiner;

/**
 * PO的生成器
 */
public class BuildPo {
    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);

    public static void execute(TableInfo tableInfo){
        File folder = new File(Constants.PATH_PO);
        if(!folder.exists()) folder.mkdirs();

        // 创建文件
        File file = new File(folder, tableInfo.getBeanName() + ".java");
        try {
            file.createNewFile();
        } catch (IOException e) {
            logger.error("创建文件失败", e);
            return;
        }

        // 写文件
        try(OutputStream out = new FileOutputStream(file);
            OutputStreamWriter ow = new OutputStreamWriter(out, "utf-8");
            BufferedWriter bw = new BufferedWriter(ow)) {

            // 生成导包语句
            bw.write("package " + Constants.PACKAGE_PO + ";\n\n");
            bw.write("import java.io.Serializable;\n");
            if(tableInfo.getHaveBigDecimal()) bw.write("import java.math.BigDecimal;\n");
            if(tableInfo.getHaveDate() || tableInfo.getHaveDateTime()){
                bw.write("import java.util.Date;\n");
                bw.write("import "+Constants.BEAN_DATE_FORMAT_IMPORT+";\n");
                bw.write("import "+Constants.BEAN_DATE_UNFORMAT_IMPORT+";\n");
                if(!Constants.IGNORE_TO_STRING){
                    bw.write("import "+Constants.PACKAGE_UTILS+".DateUtils;\n");
                    bw.write("import "+Constants.PACKAGE_ENUMS+".DateTimePatternEnum;\n");
                }
            }
            for(FieldInfo fieldInfo : tableInfo.getFieldList()){
                if(ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FIELD.split(","), fieldInfo.getPropertyName())){
                    bw.write("import "+Constants.IGNORE_BEAN_TOJSON_IMPORT+";\n");
                    break;
                }
            }
            bw.newLine();

            // 生成类注释
            BuildComment.createClassComment(bw, tableInfo.getComment());
            bw.write("public class "+ tableInfo.getBeanName() + " implements Serializable {\n");

            // 创建字段
            for(FieldInfo fieldInfo : tableInfo.getFieldList()){
                // 生成字段注释
                if(fieldInfo.getComment() != null) BuildComment.createFieldComment(bw, fieldInfo.getComment());

                // 生成注解
                if(ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())){
                    bw.write("\t" + String.format(Constants.BEAN_DATE_FORMAT_ANNOTATION, DateUtils.date_time_format) + "\n");
                    bw.write("\t" + String.format(Constants.BEAN_DATE_UNFORMAT_ANNOTATION, DateUtils.date_time_format) + "\n");
                }
                if(ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())){
                    bw.write("\t" + String.format(Constants.BEAN_DATE_FORMAT_ANNOTATION, DateUtils.date_format_1) + "\n");
                    bw.write("\t" + String.format(Constants.BEAN_DATE_UNFORMAT_ANNOTATION, DateUtils.date_format_1) + "\n");
                }
                if(ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FIELD.split(","), fieldInfo.getPropertyName())){
                    bw.write("\t" + Constants.IGNORE_BEAN_TOJSON_ANNOTATION + "\n");
                }

                // 生成字段
                bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";\n");
                bw.newLine();
            }

            // 创建getter和setter方法
            if(!Constants.IGNORE_GETTER_SETTER){
                for(FieldInfo fieldInfo : tableInfo.getFieldList()){
                    String methodName = StringUtils.upperCaseFieldLetter(fieldInfo.getPropertyName());

                    bw.write("\tpublic " + fieldInfo.getJavaType() + " get" + methodName + "() {\n");
                    bw.write("\t\treturn " + fieldInfo.getPropertyName() + ";\n");
                    bw.write("\t}\n");

                    bw.newLine();

                    bw.write("\tpublic void set" + methodName + "(" + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ") {\n");
                    bw.write("\t\tthis." + fieldInfo.getPropertyName() + " = " + fieldInfo.getPropertyName() + ";\n");
                    bw.write("\t}\n");
                }
            }

            // 重写toString方法
            if(!Constants.IGNORE_TO_STRING){
                StringJoiner joiner = new StringJoiner(" + \"", "\"", "");
                for(FieldInfo fieldInfo : tableInfo.getFieldList()){
                    String propertyNameFormatted = fieldInfo.getPropertyName();

                    if(ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())){
                        propertyNameFormatted = "DateUtils.format(" + fieldInfo.getPropertyName() + ", DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())";
                    }else if(ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())){
                        propertyNameFormatted = "DateUtils.format(" + fieldInfo.getPropertyName() + ", DateTimePatternEnum.YYYY_MM_DD.getPattern())";
                    }

                    joiner.add(" " + fieldInfo.getComment() + ":\" + (" + fieldInfo.getPropertyName() + " == null ? \"空\" : " + propertyNameFormatted + ")");
                }
                String toString = joiner.toString();

                bw.write("\t@Override\n");
                bw.write("\tpublic String toString() {\n");
                bw.write("\t\treturn \"" + tableInfo.getComment() + " [\" + " + toString + " + \" ]\";\n");
                bw.write("\t}\n");
            }

            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建po失败", e);
        }
    }
}
