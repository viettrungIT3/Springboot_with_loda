package me.loda.spring.configurationandbean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class App {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(App.class, args);
//        // Lấy ra bean SimpleBean trong Context
//        SimpleBean simpleBean = context.getBean(SimpleBean.class);
//        // In ra màn hình
//        System.out.println("Simple Bean Example: " + simpleBean.toString());

        DatabaseConnector mysql = (DatabaseConnector) context.getBean("mysqlConnector");
        mysql.connect();

        DatabaseConnector mongodb = (DatabaseConnector) context.getBean("mogodbConnector");
        mongodb.connect();

        DatabaseConnector postgresql = (DatabaseConnector) context.getBean("postgresqlConnector");
        postgresql.connect();
    }
}
