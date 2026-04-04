package com.autojava;

import com.autojava.bean.TableInfo;
import com.autojava.bean.Constants;
import com.autojava.builder.*;
import com.autojava.utils.FileCheckUtils;

import java.util.List;

public class RunApplication {

    public static void main(String[] args) {
        List<TableInfo> tableInfoList = BuildTable.getTables();

        FileCheckUtils.checkExistingFilesBeforeGenerate();

        BuildTemplates.execute();

        for(TableInfo tableInfo : tableInfoList){
            BuildPo.execute(tableInfo);
            BuildQuery.execute(tableInfo);
            BuildMapper.execute(tableInfo);
            BuildMapperXml.execute(tableInfo);
            BuildService.execute(tableInfo);
            BuildServiceImpl.execute(tableInfo);
            BuildController.execute(tableInfo);
        }

        System.out.println("生成完毕！ 代码路径: " + Constants.PATH_BASE);
    }
}
