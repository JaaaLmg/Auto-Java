package com.autojava.builder;

import com.autojava.bean.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BuildUtils {
    private static Logger logger = LoggerFactory.getLogger(BuildUtils.class);

    public static void execute() {
        List<String> headerInfoList = new ArrayList<>();

        // 生成date枚举
        headerInfoList.add("package " + Constants.PACKAGE_ENUMS + ";\n");
        build(headerInfoList,"DateTimePatternEnum", Constants.PATH_ENUMS);

        headerInfoList.clear();

        headerInfoList.add("package " + Constants.PACKAGE_UTILS + ";\n");
        build(headerInfoList,"DateUtils", Constants.PATH_UTILS);
    }

    public static void build(List<String> headerInfoList, String fileName, String outputPath) {
        File folder = new File(outputPath);
        if(!folder.exists()) folder.mkdirs();
        File javaFile = new File(outputPath, fileName + ".java");

        String templatePath = BuildUtils.class.getClassLoader().getResource("template/" + fileName + ".txt").getPath();

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
