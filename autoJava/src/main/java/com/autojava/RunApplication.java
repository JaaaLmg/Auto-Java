package com.autojava;

import com.autojava.bean.TableInfo;
import com.autojava.builder.BuildTable;

import java.util.List;

public class RunApplication {
    public static void main(String[] args) {
        List<TableInfo> tableInfoList = BuildTable.getTables();
    }
}
