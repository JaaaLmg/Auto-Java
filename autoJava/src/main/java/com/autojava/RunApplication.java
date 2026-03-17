package com.autojava;

import com.autojava.bean.TableInfo;
import com.autojava.builder.*;

import java.util.List;

public class RunApplication {
    public static void main(String[] args) {
        List<TableInfo> tableInfoList = BuildTable.getTables();

        BuildTemplates.execute();

        for(TableInfo tableInfo : tableInfoList){
            BuildPo.execute(tableInfo);
            BuildQuery.execute(tableInfo);
            BuildMapper.execute(tableInfo);
            BuildMapperXml.execute(tableInfo);
        }
    }
}
