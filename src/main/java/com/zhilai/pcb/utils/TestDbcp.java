package com.zhilai.pcb.utils;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDbcp extends BasicDataSource {
    @Override
    public synchronized  void  close() throws SQLException{
        System.out.println("......输出数据源Driver的url："+DriverManager.getDriver(url));
        DriverManager.deregisterDriver(DriverManager.getDriver(url));
        super.close();
    }
}
