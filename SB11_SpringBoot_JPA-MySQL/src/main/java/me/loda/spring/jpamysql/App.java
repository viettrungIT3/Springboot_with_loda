package me.loda.spring.jpamysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);

        UserRepository userRepository = context.getBean(UserRepository.class);

        // Lấy ra toàn bộ user trong db
        userRepository.findAll().forEach(System.out::println);

        // Lưu user xuống database
        User user = userRepository.save(new User());
        // Khi lưu xong, nó trả về User đã lưu kèm theo Id.
        System.out.println("User vừa lưu có ID: " + user.getId());
        Long UserID = user.getId();
        // Cập nhật user
        user.setAgi(100);
        // Update user
        userRepository.save(user);

        // Query lấy ra user vừa xong để kiểm tra xem.
        User user2 = userRepository.findById(UserID).get();
        System.out.println("User: " + user);
        System.out.println("User2: " + user2);

        // Xóa User khỏi DB
        userRepository.delete(user);

        User user3 = userRepository.findById(UserID).orElse(null);
        System.out.println("User3: " + user3);
    }
}
