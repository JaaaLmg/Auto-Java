package com.autojava;

import com.autojava.bean.TableInfo;
import com.autojava.builder.BuildPo;
import com.autojava.builder.BuildQuery;
import com.autojava.builder.BuildTable;
import com.autojava.builder.BuildUtils;

import java.util.List;

public class RunApplication {
    public static void main(String[] args) {
        List<TableInfo> tableInfoList = BuildTable.getTables();

        BuildUtils.execute();

        for(TableInfo tableInfo : tableInfoList){
            BuildPo.execute(tableInfo);
            BuildQuery.execute(tableInfo);
        }
    }
}
