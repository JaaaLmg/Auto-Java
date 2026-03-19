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
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * service接口生成器
 */
public class BuildService {
    private static final Logger logger = LoggerFactory.getLogger(BuildService.class);

    public static void execute(TableInfo tableInfo){
        File folder = new File(Constants.PATH_SERVICE);
        if(!folder.exists()) folder.mkdirs();

        // 创建文件
        File file = new File(folder, tableInfo.getBeanName()+ Constants.SUFFIX_SERVICE + ".java");
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
            bw.write("package " + Constants.PACKAGE_SERVICE + ";\n\n");
            bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";\n");
            bw.write("import " + Constants.PACKAGE_QUERY + "." + tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY + ";\n");
            bw.write("import " + Constants.PACKAGE_VO + ".PaginationResultVO;\n");
            bw.write("import java.util.List;\n\n");

            // 生成类注释
            BuildComment.createClassComment(bw, tableInfo.getComment()+"Service接口");
            bw.write("public interface "+ tableInfo.getBeanName() + Constants.SUFFIX_SERVICE + " {\n");

            // 创建方法
            BuildComment.createMethodComment(bw, "根据查询条件查询列表");
            bw.write("\tList<" + tableInfo.getBeanName() + "> queryList(" + tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY + " query);\n\n");
            BuildComment.createMethodComment(bw, "根据查询条件查询数量");
            bw.write("\tInteger queryCount(" + tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY + " query);\n\n");
            BuildComment.createMethodComment(bw, "分页查询");
            bw.write("\tPaginationResultVO<" + tableInfo.getBeanName() + "> queryPage(" + tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY + " query);\n\n");
            BuildComment.createMethodComment(bw, "新增");
            bw.write("\tInteger add(" + tableInfo.getBeanName() + " bean);\n\n");
            BuildComment.createMethodComment(bw, "批量新增");
            bw.write("\tInteger addBatch(List<" + tableInfo.getBeanName() + "> listBean);\n\n");
            BuildComment.createMethodComment(bw, "批量新增或修改");
            bw.write("\tInteger addOrUpdateBatch(List<" + tableInfo.getBeanName() + "> listBean);\n\n");

            // 主键相关方法
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            for(Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyFieldList = entry.getValue();

                // 生成方法参数
                StringJoiner keyMethodParamJoiner = new StringJoiner(", ");
                for(FieldInfo fieldInfo : keyFieldList){
                    keyMethodParamJoiner.add(fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName());
                }

                // 生成方法名和注释
                StringJoiner keyMethodNameJoiner = new StringJoiner("And");
                StringJoiner CommentNameJoiner = new StringJoiner("和", "根据", "");
                for(FieldInfo fieldInfo : keyFieldList){
                    keyMethodNameJoiner.add(StringUtils.upperCaseFieldLetter(fieldInfo.getPropertyName()));
                    CommentNameJoiner.add(StringUtils.upperCaseFieldLetter(fieldInfo.getPropertyName()));
                }

                BuildComment.createFieldComment(bw, CommentNameJoiner.toString()+"查询");   // 生成方法注释
                bw.write("\t"+ tableInfo.getBeanName() +" get"+ tableInfo.getBeanName() + "By" + keyMethodNameJoiner.toString() + "(" + keyMethodParamJoiner.toString() + ");\n\n");    // 生成方法
                bw.newLine();

                BuildComment.createFieldComment(bw, CommentNameJoiner.toString()+"更新");   // 生成方法注释
                bw.write("\tInteger update" + tableInfo.getBeanName() + "By" + keyMethodNameJoiner.toString() + "(" + tableInfo.getBeanName() + " bean, " + keyMethodParamJoiner.toString() + ");\n\n");    // 生成方法
                bw.newLine();

                BuildComment.createFieldComment(bw, CommentNameJoiner.toString()+"删除");   // 生成方法注释
                bw.write("\tInteger delete" + tableInfo.getBeanName() +"By" + keyMethodNameJoiner.toString() + "(" + keyMethodParamJoiner.toString() + ");\n\n");    // 生成方法
                bw.newLine();
            }

            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建service失败", e);
        }
    }
}
