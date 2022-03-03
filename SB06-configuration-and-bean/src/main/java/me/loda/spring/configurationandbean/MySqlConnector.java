package me.loda.spring.configurationandbean;

public class MySqlConnector extends DatabaseConnector {

    @Override
    public void connect() {
        System.out.println("Đã kết nối tới MySQL: " + getUrl());
    }
}
