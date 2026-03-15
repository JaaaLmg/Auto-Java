package com.autojava.builder;

import com.autojava.bean.Constants;
import com.autojava.bean.FieldInfo;
import com.autojava.bean.TableInfo;
import com.autojava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Query的生成器
 */
public class BuildQuery {
    private static final Logger logger = LoggerFactory.getLogger(BuildQuery.class);

    public static void execute(TableInfo tableInfo){
        File folder = new File(Constants.PATH_QUERY);
        if(!folder.exists()) folder.mkdirs();

        // 创建文件
        File file = new File(folder, tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY + ".java");
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
            bw.write("package " + Constants.PACKAGE_QUERY + ";\n\n");
            if(tableInfo.getHaveBigDecimal()) bw.write("import java.math.BigDecimal;\n");
            if(tableInfo.getHaveDate() || tableInfo.getHaveDateTime()){
                bw.write("import java.util.Date;\n");
            }
            bw.newLine();

            // 生成类注释
            BuildComment.createClassComment(bw, tableInfo.getComment()+"查询对象");

            bw.write("public class "+ tableInfo.getBeanName() + "Query {\n");

            // 创建字段
            List<FieldInfo> extendFieldList = new ArrayList<>(tableInfo.getFieldList());
            for(FieldInfo fieldInfo : tableInfo.getFieldList()){
                // 生成字段注释
                if(fieldInfo.getComment() != null) BuildComment.createFieldComment(bw, fieldInfo.getComment());

                // 生成原始字段
                bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";\n");
                // 生成fuzzy字段(String类型)
                if(ArrayUtils.contains(Constants.SQL_STRING_TYPES, fieldInfo.getSqlType())) {
                    bw.write("\tprivate String " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_FUZZY + ";\n");

                    FieldInfo fuzzyField = new FieldInfo();
                    fuzzyField.setJavaType("String");
                    fuzzyField.setPropertyName(fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_FUZZY);
                    extendFieldList.add(fuzzyField);
                }
                // 生成时间起止字段(String类型)
                if(ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType()) ||
                   ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())) {
                    bw.write("\tprivate String " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_START + ";\n");
                    bw.write("\tprivate String " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_END + ";\n");

                    FieldInfo startField = new FieldInfo();
                    startField.setJavaType("String");
                    startField.setPropertyName(fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_START);
                    extendFieldList.add(startField);
                    FieldInfo endField = new FieldInfo();
                    endField.setJavaType("String");
                    endField.setPropertyName(fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_END);
                    extendFieldList.add(endField);
                }

                bw.newLine();
            }

            // 创建getter和setter方法
            if(!Constants.IGNORE_GETTER_SETTER){
                for(FieldInfo fieldInfo : extendFieldList){
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

            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建query失败", e);
        }
    }
}
