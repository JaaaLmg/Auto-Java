package com.autojava.builder;

import com.autojava.bean.Constants;
import com.autojava.bean.FieldInfo;
import com.autojava.bean.TableInfo;
import com.autojava.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class BuildController {
    private static final Logger logger = LoggerFactory.getLogger(com.autojava.builder.BuildController.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_CONTROLLER);
        if (!folder.exists()) folder.mkdirs();

        // 创建文件
        File file = new File(folder, tableInfo.getBeanName() + Constants.SUFFIX_CONTROLLER + ".java");
        try {
            file.createNewFile();
        } catch (IOException e) {
            logger.error("创建文件失败", e);
            return;
        }

        // 写文件
        try (OutputStream out = new FileOutputStream(file);
             OutputStreamWriter ow = new OutputStreamWriter(out, "utf-8");
             BufferedWriter bw = new BufferedWriter(ow)) {

            // 生成导包语句
            bw.write("package " + Constants.PACKAGE_CONTROLLER + ";\n\n");
            bw.write("import " + Constants.PACKAGE_BASE + ".mappers." + tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS + ";\n");
            bw.write("import " + Constants.PACKAGE_BASE + ".service." + tableInfo.getBeanName() + Constants.SUFFIX_SERVICE + ";\n");
            bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";\n");
            bw.write("import " + Constants.PACKAGE_QUERY + "." + tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY + ";\n");
            bw.write("import " + Constants.PACKAGE_QUERY + ".SimplePage;\n");
            bw.write("import " + Constants.PACKAGE_ENUMS + ".PageSize;\n");
            bw.write("import " + Constants.PACKAGE_VO + ".PaginationResultVO;\n");
            bw.write("import " + Constants.PACKAGE_VO + ".ResponseVO;\n");
            bw.write("import org.springframework.stereotype.Service;\n");
            bw.write("import org.springframework.web.bind.annotation.RestController;\n");
            bw.write("import org.springframework.web.bind.annotation.RequestMapping;\n");
            bw.write("import org.springframework.web.bind.annotation.RequestBody;\n");
            bw.write("import javax.annotation.Resource;\n");
            bw.write("import java.util.List;\n\n");

            // 生成类注释
            BuildComment.createClassComment(bw, tableInfo.getComment() + "Controller类");
            bw.write("@RestController\n");
            bw.write("@RequestMapping(\"" + StringUtils.lowerCaseFieldLetter(tableInfo.getBeanName()) + "\")\n");
            bw.write("public class " + tableInfo.getBeanName() + Constants.SUFFIX_CONTROLLER + " extends BaseController {\n");

            bw.write("\t@Resource\n");
            String ServiceName = tableInfo.getBeanName() + Constants.SUFFIX_SERVICE;
            bw.write("\tprivate " + ServiceName +" " + StringUtils.lowerCaseFieldLetter(ServiceName) + ";\n\n");

            // 创建方法
            BuildComment.createMethodComment(bw, "根据查询条件分页查询");
            bw.write("\t@RequestMapping(\"loadDataList\")\n");
            bw.write("\tpublic ResponseVO loadDataList("+ tableInfo.getBeanName() + "Query query) {\n");
            bw.write("\t\t return getSuccessResponseVO(" + StringUtils.lowerCaseFieldLetter(ServiceName) + ".queryPage(query));\n");
            bw.write("\t}\n\n");


            BuildComment.createMethodComment(bw, "新增");
            bw.write("\t@RequestMapping(\"add\")\n");
            bw.write("\tpublic ResponseVO add(" + tableInfo.getBeanName() + " bean) {\n");
            bw.write("\t\t" + StringUtils.lowerCaseFieldLetter(ServiceName) + ".add(bean);\n");
            bw.write("\t\treturn getSuccessResponseVO(null);\n");
            bw.write("\t}\n\n");

            BuildComment.createMethodComment(bw, "批量新增");
            bw.write("\t@RequestMapping(\"addBatch\")\n");
            bw.write("\tpublic ResponseVO addBatch(@RequestBody List<" + tableInfo.getBeanName() + "> listBean) {\n");
            bw.write("\t\t" + StringUtils.lowerCaseFieldLetter(ServiceName) + ".addBatch(listBean);\n");
            bw.write("\t\treturn getSuccessResponseVO(null);\n");
            bw.write("\t}\n\n");

            BuildComment.createMethodComment(bw, "批量新增或修改");
            bw.write("\t@RequestMapping(\"addOrUpdateBatch\")\n");
            bw.write("\tpublic ResponseVO addOrUpdateBatch(@RequestBody List<" + tableInfo.getBeanName() + "> listBean) {\n");
            bw.write("\t\t" + StringUtils.lowerCaseFieldLetter(ServiceName) + ".addOrUpdateBatch(listBean);\n");
            bw.write("\t\treturn getSuccessResponseVO(null);\n");
            bw.write("\t}\n\n");

            // 主键相关方法
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyFieldList = entry.getValue();

                // 生成方法参数
                StringJoiner keyMethodParamJoiner = new StringJoiner(", ");
                for (FieldInfo fieldInfo : keyFieldList) {
                    keyMethodParamJoiner.add(fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName());
                }

                // 生成方法名和注释
                StringJoiner keyMethodNameJoiner = new StringJoiner("And");
                StringJoiner keyMethodParamNameJoiner = new StringJoiner(", ");
                StringJoiner CommentNameJoiner = new StringJoiner("和", "根据", "");
                for (FieldInfo fieldInfo : keyFieldList) {
                    keyMethodNameJoiner.add(StringUtils.upperCaseFieldLetter(fieldInfo.getPropertyName()));
                    keyMethodParamNameJoiner.add(fieldInfo.getPropertyName());
                    CommentNameJoiner.add(StringUtils.upperCaseFieldLetter(fieldInfo.getPropertyName()));
                }

                BuildComment.createFieldComment(bw, CommentNameJoiner.toString() + "查询");   // 生成方法注释
                bw.write("\t@RequestMapping(\"get" + tableInfo.getBeanName() + "By" + keyMethodNameJoiner.toString() +"\")\n");
                bw.write("\tpublic ResponseVO get" + tableInfo.getBeanName() + "By" + keyMethodNameJoiner.toString()+ "(" + keyMethodParamJoiner.toString() + ") {\n");
                bw.write("\t\treturn getSuccessResponseVO(" + StringUtils.lowerCaseFieldLetter(ServiceName) + ".get" + tableInfo.getBeanName() + "By" + keyMethodNameJoiner.toString() + "(" + keyMethodParamNameJoiner.toString() + "));\n");
                bw.write("\t}\n\n");
                bw.newLine();

                BuildComment.createFieldComment(bw, CommentNameJoiner.toString() + "更新");   // 生成方法注释
                bw.write("\t@RequestMapping(\"update" + tableInfo.getBeanName() + "By" + keyMethodNameJoiner.toString() +"\")\n");
                bw.write("\tpublic ResponseVO update" + tableInfo.getBeanName() + "By" + keyMethodNameJoiner.toString()+ "(" + tableInfo.getBeanName() + " bean, " + keyMethodParamJoiner.toString() + ") {\n");
                bw.write("\t\t" + StringUtils.lowerCaseFieldLetter(ServiceName) + ".update" + tableInfo.getBeanName() + "By" + keyMethodNameJoiner.toString() + "(bean, " + keyMethodParamNameJoiner.toString() + ");\n");
                bw.write("\t\treturn getSuccessResponseVO(null);\n");
                bw.write("\t}\n\n");
                bw.newLine();

                BuildComment.createFieldComment(bw, CommentNameJoiner.toString() + "删除");   // 生成方法注释
                bw.write("\t@RequestMapping(\"delete" + tableInfo.getBeanName() + "By" + keyMethodNameJoiner.toString() +"\")\n");
                bw.write("\tpublic ResponseVO delete" + tableInfo.getBeanName() + "By" + keyMethodNameJoiner.toString()+ "(" + keyMethodParamJoiner.toString() + ") {\n");
                bw.write("\t\t" + StringUtils.lowerCaseFieldLetter(ServiceName) + ".delete" + tableInfo.getBeanName() + "By" + keyMethodNameJoiner.toString() + "(" + keyMethodParamNameJoiner.toString() + ");\n");
                bw.write("\t\treturn getSuccessResponseVO(null);\n");
                bw.write("\t}\n\n");
                bw.newLine();
            }

            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建Controller失败", e);
        }
    }
}
