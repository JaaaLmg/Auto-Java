package com.autojava.builder;

import com.autojava.bean.Constants;
import com.autojava.utils.DateUtils;

import java.io.BufferedWriter;
import java.util.Date;

/**
 * @FileName BuildComment
 * @Description 注释生成器
 * @Author LumingJia
 * @Date 2026/3/14
 **/
public class BuildComment {
    public static void createClassComment(BufferedWriter bw, String comment) throws Exception{
        bw.write("/**\n");
        bw.write(" * @Description " + comment + "\n");
        bw.write(" * @Author "+ Constants.PROJECT_AUTHOR + "\n");
        bw.write(" * @Date " + DateUtils.format(new Date(), DateUtils.date_format_2) + "\n");
        bw.write(" */\n");
    }

    public static void createFieldComment(BufferedWriter bw, String comment) throws Exception{
        bw.write("\t/**\n");
        bw.write("\t * " + comment + "\n");
        bw.write("\t */\n");
    }

    public static void createMethodComment(BufferedWriter bw, String comment) throws Exception{
        bw.write("\t/**\n");
        bw.write("\t * " + comment + "\n");
        bw.write("\t */\n");
    }
}
