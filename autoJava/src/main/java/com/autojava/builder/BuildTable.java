package com.autojava.builder;

import com.autojava.utils.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

// 获取数据库表信息
public class BuildTable {
    private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);
    private static Connection conn = null;
    private static String SQL_SHOW_TABLE_STATUS = "show table status";

    static {
        String driverName = PropertiesUtils.getString("db.driver.name");
        String url = PropertiesUtils.getString("db.url");
        String user = PropertiesUtils.getString("db.username");
        String password = PropertiesUtils.getString("db.password");

        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            logger.error("获取数据库连接失败",e);
        }

    }

    public static void getTables(){
        PreparedStatement ps = null;
        ResultSet tableResult = null;
        try {
            ps = conn.prepareStatement(SQL_SHOW_TABLE_STATUS);
            tableResult = ps.executeQuery();
            while (tableResult.next()){
                String tableName = tableResult.getString("name");
                String comment = tableResult.getString("comment");
                logger.info("tableName：{} , comment:{}",tableName,comment);
            }
        }catch (Exception e){
            logger.error("获取表信息失败",e);
        }finally {
            if(tableResult != null){
                try {
                    tableResult.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
