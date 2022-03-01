package me.loda.spring.helloworld;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/*
    - Đánh dấu class bằng @Component
    - Class này sẽ được Spring Boot hiểu là một Bean (hoặc dependency) Và sẽ được Spring Boot quản lý
    - Trong trường hợp bạn muốn mỗi lần sử dụng là một instance hoàn toàn mới. Thì hãy đánh dấu @Component đó bằng @Scope("prototype")
*/

@Component
@Scope("prototype")
public class Bikini implements Outfit{

    @Override
    public void wear() {
        System.out.println("Mặc bikini");
    }
}
