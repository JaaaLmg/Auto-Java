package com.autojava.builder;

import com.autojava.bean.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 构建由模板生成的类
 */
public class BuildTemplates {
    private static Logger logger = LoggerFactory.getLogger(BuildTemplates.class);

    public static void execute() {
        List<String> headerInfoList = new ArrayList<>();

        // 生成date枚举
        headerInfoList.add("package " + Constants.PACKAGE_ENUMS + ";\n");
        build(headerInfoList,"DateTimePatternEnum", Constants.PATH_ENUMS);
        headerInfoList.clear();

        // 生成date工具类
        headerInfoList.add("package " + Constants.PACKAGE_UTILS + ";\n");
        build(headerInfoList,"DateUtils", Constants.PATH_UTILS);
        headerInfoList.clear();

        // 生成baseMapper
        headerInfoList.add("package " + Constants.PACKAGE_MAPPERS + ";\n");
        build(headerInfoList,"BaseMapper", Constants.PATH_MAPPERS);
        headerInfoList.clear();

        // 生成PageSize枚举
        headerInfoList.add("package " + Constants.PACKAGE_ENUMS + ";\n");
        build(headerInfoList,"PageSize", Constants.PATH_ENUMS);
        headerInfoList.clear();

        // 生成SimplePage类
        headerInfoList.add("package " + Constants.PACKAGE_QUERY + ";\n");
        headerInfoList.add("import " + Constants.PACKAGE_ENUMS + ".PageSize;\n");
        build(headerInfoList,"SimplePage", Constants.PATH_QUERY);
        headerInfoList.clear();

        // 生成query基类
        headerInfoList.add("package " + Constants.PACKAGE_QUERY + ";\n");
        build(headerInfoList,"BaseQuery", Constants.PATH_QUERY);
        headerInfoList.clear();
    }

    public static void build(List<String> headerInfoList, String fileName, String outputPath) {
        File folder = new File(outputPath);
        if(!folder.exists()) folder.mkdirs();
        File javaFile = new File(outputPath, fileName + ".java");

        String templatePath = BuildTemplates.class.getClassLoader().getResource("template/" + fileName + ".txt").getPath();

        try (OutputStream out = new FileOutputStream(javaFile);
             OutputStreamWriter outw = new OutputStreamWriter(out, "utf-8");
             BufferedWriter bw = new BufferedWriter(outw);
             InputStream in = new FileInputStream(templatePath);
             InputStreamReader inr = new InputStreamReader(in, "utf-8");
             BufferedReader br = new BufferedReader(inr)) {

            for(String headerInfo : headerInfoList){
                bw.write(headerInfo);
                bw.newLine();
            }

            String lineInfo = null;
            while((lineInfo = br.readLine()) != null) {
                bw.write(lineInfo);
                bw.newLine();
            }

        } catch (Exception e) {
            logger.error("生成基础工具类: {} 失败:", fileName, e);
        }
    }
}
