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

public class BuildMapper {
    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);

    public static void execute(TableInfo tableInfo){
        File folder = new File(Constants.PATH_MAPPERS);
        if(!folder.exists()) folder.mkdirs();

        // 创建文件
        File file = new File(folder, tableInfo.getBeanName()+ Constants.SUFFIX_MAPPERS + ".java");
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
            bw.write("package " + Constants.PACKAGE_MAPPERS + ";\n\n");
            bw.write("import org.apache.ibatis.annotations.Param;\n");

            bw.newLine();

            // 生成类注释
            BuildComment.createClassComment(bw, tableInfo.getComment() + "Mapper");

            bw.write("public interface "+ tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS + "<T, P> extends BaseMapper {\n");

            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            for(Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyFieldList = entry.getValue();

                // 生成方法参数
                StringJoiner keyMethodParamJoiner = new StringJoiner(", ");
                for(FieldInfo fieldInfo : keyFieldList){
                    keyMethodParamJoiner.add("@Param(\"" + fieldInfo.getPropertyName() + "\") " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName());
                }

                // 生成方法名和注释
                StringJoiner keyMethodNameJoiner = new StringJoiner("And");
                StringJoiner CommentNameJoiner = new StringJoiner("和", "根据", "");
                for(FieldInfo fieldInfo : keyFieldList){
                    keyMethodNameJoiner.add(StringUtils.upperCaseFieldLetter(fieldInfo.getPropertyName()));
                    CommentNameJoiner.add(StringUtils.upperCaseFieldLetter(fieldInfo.getPropertyName()));
                }

                BuildComment.createFieldComment(bw, CommentNameJoiner.toString()+"查询");   // 生成方法注释
                bw.write("\tT selectBy" + keyMethodNameJoiner.toString() + "(" + keyMethodParamJoiner.toString() + ");\n");    // 生成方法
                bw.newLine();

                BuildComment.createFieldComment(bw, CommentNameJoiner.toString()+"更新");   // 生成方法注释
                bw.write("\tInteger updateBy" + keyMethodNameJoiner.toString() + "(@Param(\"bean\") T t, " + keyMethodParamJoiner.toString() + ");\n");    // 生成方法
                bw.newLine();

                BuildComment.createFieldComment(bw, CommentNameJoiner.toString()+"删除");   // 生成方法注释
                bw.write("\tInteger deleteBy" + keyMethodNameJoiner.toString() + "(" + keyMethodParamJoiner.toString() + ");\n");    // 生成方法
                bw.newLine();
            }

            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建mapper失败", e);
        }
    }
}
