package me.loda.spring.configurationandbean;

public class MongoDbConnector  extends DatabaseConnector {

    @Override
    public void connect() {
        System.out.println("Đã kết nối tới Mongodb: " + getUrl());
    }
}
