package me.loda.spring.applicationsproperties;

public class MySqlConnector extends DatabaseConnector {
    @Override
    public void connect() {
        System.out.println("Đã kết nối tới Mysql: " + getUrl());
    }
}
