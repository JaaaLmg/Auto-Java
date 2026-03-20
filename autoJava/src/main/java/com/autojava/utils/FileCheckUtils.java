package com.autojava.utils;

import com.autojava.bean.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 生成前文件检测工具类
 */
public class FileCheckUtils {

    /**
     * 检测指定目录下是否存在文件（不递归子目录）
     */
    private static boolean hasFiles(String path) {
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            return false;
        }
        File[] files = dir.listFiles(f -> f.isFile());
        return files != null && files.length > 0;
    }

    /**
     * 生成前检测：扫描所有目标目录，收集已存在文件的目录列表，
     * 若有则警告用户并等待确认，输入 Y/y 后继续，否则退出。
     */
    public static void checkExistingFilesBeforeGenerate() {
        // 需要检测的所有 java 源码目录
        String[] javaPaths = {
            Constants.PATH_PO,
            Constants.PATH_QUERY,
            Constants.PATH_MAPPERS,
            Constants.PATH_SERVICE,
            Constants.PATH_SERVICE_IMPL,
            Constants.PATH_VO,
            Constants.PATH_EXCEPTION,
            Constants.PATH_CONTROLLER,
            Constants.PATH_UTILS,
            Constants.PATH_ENUMS
        };
        // 需要检测的 mapper xml 目录
        String[] xmlPaths = {
            Constants.PATH_MAPPERS_XML
        };

        List<String> existingDirs = new ArrayList<>();
        for (String path : javaPaths) {
            if (hasFiles(path)) {
                existingDirs.add(path);
            }
        }
        for (String path : xmlPaths) {
            if (hasFiles(path)) {
                existingDirs.add(path);
            }
        }

        if (existingDirs.isEmpty()) {
            return;
        }

        System.out.println("========================================================");
        System.out.println("  ⚠  警告：以下目录下已存在文件，继续生成将会覆盖原有文件！");
        System.out.println("========================================================");
        for (String dir : existingDirs) {
            System.out.println("  - " + dir);
        }
        System.out.println("--------------------------------------------------------");
        System.out.println("  请确认是否继续生成？输入 Y 继续，输入其他任意键退出：");
        System.out.print("  > ");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        if (!input.equalsIgnoreCase("Y")) {
            System.out.println("  已取消生成，程序退出。");
            System.exit(0);
        }
        System.out.println("  确认继续，开始生成...");
        System.out.println("========================================================");
    }
}
