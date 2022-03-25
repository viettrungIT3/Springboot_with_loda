# [SB14] Restful API + @RestController + @PathVariable + @RequestBody
Chi tiết tại:
[Loda.me - 「Spring Boot #14」 Restful API + @RestController + @PathVariable + @RequestBody](https://loda.me/articles/sb14-restful-api-restcontroller-pathvariable-requestbody)

# Content without images
### Giới thiệu

[Trong bài trước][link-spring-boot-13], chúng ta đã tìm hiểu cách làm Web với **Spring boot** và **Thymeleaf**. Mặc dù rất tốt rồi, nhưng đó chưa hẳn là điểm mạnh của **Spring Boot**.

Hiện tại xu hướng hiện nay là sẽ để các frontend framework take care nhiều việc hơn, còn phía server chỉ nên cung cấp API cho frontend framework là đủ.

**Spring boot** thì lại quá mạnh cho việc tạo Restful API :3

### @RestController

Khác với `@Controller` là sẽ trả về một template.

`@RestController` trả về dữ liệu dưới dạng JSON.

```java
@RestController
@RequestMapping("/api/v1")
public class RestApiController{

    @GetMapping("/todo")
    public List<Todo> getTodoList() {
        return todoList;
    }
}
```

Các đối tượng trả về dưới dạng Object sẽ được **Spring Boot** chuyển thành JSON.

Các đối tượng trả về rất đa dạng, bạn có thể trả về `List`, `Map`, v.v.. **Spring Boot** sẽ convert hết chúng thành JSON, mặc định sẽ dùng Jackson converter để làm điều đó.

Nếu bạn muốn API tùy biến được kiểu dữ liệu trả về, bạn có thể trả về đối tượng `ResponseEntity` của **Spring** cung cấp. Đây là đối tượng cha của mọi response và sẽ wrapper các object trả về. Cái này bạn xem tiếp phần dưới sẽ rõ.

### @RequestBody

Vì bây giờ chúng ta xây dựng API, nên các thông tin từ phía **Client** gửi lên **Server** sẽ nằm trong `Body`, và cũng dưới dạng `JSON` luôn.

```java
@RestController
@RequestMapping("/api/v1")
public class RestApiController {

    List<Todo> todoList = new CopyOnWriteArrayList<>();

    @PostMapping("/todo")
    public ResponseEntity addTodo(@RequestBody Todo todo) {
        todoList.add(todo);
        // Trả về response với STATUS CODE = 200 (OK)
        // Body sẽ chứa thông tin về đối tượng todo vừa được tạo.
        return ResponseEntity.ok().body(todo);
    }

}
```

Tất nhiên là **Spring Boot** sẽ làm giúp chúng ta các phần nặng nhọc, nó chuyển chuỗi JSON trong request thành một Object Java. bạn chỉ cần cho nó biết cần chuyển JSON thành Object nào bằng Annotation `@RequestBody`

### @PathVariable

`RESTful API` là một tiêu chuẩn dùng trong việc thết kế các thiết kế API cho các ứng dụng web để quản lý các resource.

Và với cách thống nhất này, thì sẽ có một phần thông tin quan trọng sẽ nằm ngay trong chính URL của api.

Ví dụ:

1. URL tạo To-do: [.../api/v1/todo](http://localhost:8080/api/v1/todo). Tương ứng với HTTP method là POST
2. URL lấy thông tin To-do số 12: [.../api/v1/todo/2](http://localhost:8080/api/v1/todo/2). Tương ứng với HTTP method là GET
3. URL sửa thông tin To-do số 12: [.../api/v1/todo/2](http://localhost:8080/api/v1/todo/2). Tương ứng với HTTP method là PUT
4. URL xoá To-do số 12: [.../api/v1/todo/2](http://localhost:8080/api/v1/todo/2). Tương ứng với HTTP method là DELETE

Ngoài thông tin trong `Body` của request, thì cái chúng ta cần chính là cái con số 12 nằm trong URL. Phải lấy được con số đó thì mới biết được đối tượng To-do cần thao tác là gì.

`@PathVariable` tham chiến.

```java
@RestController
@RequestMapping("/api/v1")
public class RestApiController {

    /*
    phần path URL bạn muốn lấy thông tin sẽ để trong ngoặc kép {}
     */
    @GetMapping("/todo/{todoId}")
    public Todo getTodo(@PathVariable(name = "todoId") Integer todoId){
        // @PathVariable lấy ra thông tin trong URL
        // dựa vào tên của thuộc tính đã định nghĩa trong ngoặc kép /todo/{todoId}
        return todoList.get(todoId);
    }
}
```

### Demo

Chúng ta sẽ demo một server Rest API đơn giản với **Spring Boot**. Các API sẽ phục vụ việc thao tác với đối tượng `To-do`.

[Giống bài #13][link-spring-boot-13] nhưng lần này là API chứ không phải website.

#### Cài đặt

_pom.xml_

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <packaging>pom</packaging>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.5.RELEASE</version>
        <relativePath /> <!-- lookup parent from repository -->
    </parent>
    <groupId>me.loda.spring</groupId>
    <artifactId>spring-boot-learning</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>spring-boot-learning</name>
    <description>Everything about Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>

        <!--spring mvc, rest-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
        </plugins>
    </build>

</project>
```

Cấu trúc thư mục:

![spring-boot-rest-api](https://super-static-assets.s3.amazonaws.com/8a72ee8e-d4aa-4a06-985f-e92802c5bc44/images/a6bdf9e7-0b37-46ee-9154-230906458c16.jpg?w=1500&f=webp)

#### Tạo model

Sử dụng [Lombok][] cho tiện nha các bạn.

_Todo.java_

```java
import lombok.Data;

@Data
public class Todo {
    private String title;
    private String detail;
}
```

#### Tạo RestController

Phần này rất dễ, thay `@Controller` thành `RestController` thôi :)) đùa đấy, bạn xem code ở dưới nhẻ.

Vì tôi chỉ muốn Demo cách tạo API cho các bạn, nên chúng ta tạm bỏ qua Database nhé ;)

Bạn để ý 2 đoạn `@RequestMapping` và `@PostConstruct` nhé!

```java
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lưu ý, @RequestMapping ở class, sẽ tác động tới
 * tất cả các RequestMapping ở bên trong nó.
 * 
 * Mọi Request ở trong method sẽ được gắn thêm prefix /api/v1
 */
@RestController
@RequestMapping("/api/v1")
public class RestApiController {

    private List<Todo> todoList = new CopyOnWriteArrayList<>();


    // bạn còn nhớ @PostConstruct dùng để làm gì chứ?
    // nếu không nhớ, hãy coi lại bài viết Spring Boot #3 nhé
    @PostConstruct
    public void init(){
        // Thêm null vào List để bỏ qua vị trí số 0;
        todoList.add(null);
    }

    @GetMapping("/todo")
    public List<Todo> getTodoList() {
        return todoList;
    }

    /*
    phần path URL bạn muốn lấy thông tin sẽ để trong ngoặc kép {}
     */
    @GetMapping("/todo/{todoId}")
    public Todo getTodo(@PathVariable(name = "todoId") Integer todoId){
        // @PathVariable lấy ra thông tin trong URL
        // dựa vào tên của thuộc tính đã định nghĩa trong ngoặc kép /todo/{todoId}
        return todoList.get(todoId);
    }


    /*
    @RequestBody nói với Spring Boot rằng hãy chuyển Json trong request body
    thành đối tượng Todo
     */
    @PutMapping("/todo/{todoId}")
    public Todo editTodo(@PathVariable(name = "todoId") Integer todoId,
                         @RequestBody Todo todo){
        todoList.set(todoId, todo);
        // Trả về đối tượng sau khi đã edit
        return todo;    }

    @DeleteMapping("/todo/{todoId}")
    public ResponseEntity deleteTodo(@PathVariable(name = "todoId") Integer todoId){
        todoList.remove(todoId.intValue());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/todo")
    public ResponseEntity addTodo(@RequestBody Todo todo) {
        todoList.add(todo);
        // Trả về response với STATUS CODE = 200 (OK)
        // Body sẽ chứa thông tin về đối tượng todo vừa được tạo.
        return ResponseEntity.ok().body(todo);
    }
}
```

#### Chạy thử

_App.java_

```java
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

Server sẽ on trên port 8080.

Bây giờ chỉ cần Test thôi!

#### Tạo ra một đối tượng To-do

```
POST http://localhost:8080/api/v1/todo
```

![spring-boot-rest-api](https://super-static-assets.s3.amazonaws.com/8a72ee8e-d4aa-4a06-985f-e92802c5bc44/images/cdc53d58-606a-45bb-a805-96afe90196f0.jpg?w=1500&f=webp)


#### Xem danh sách To-do

```
GET http://localhost:8080/api/v1/todo
```

![spring-boot-rest-api](https://super-static-assets.s3.amazonaws.com/8a72ee8e-d4aa-4a06-985f-e92802c5bc44/images/020b1df0-e295-4be5-b4cf-d184fef3e6c4.jpg?w=1500&f=webp)


#### Sửa To-do

```
PUT http://localhost:8080/api/v1/todo/1
```
![spring-boot-rest-api](https://super-static-assets.s3.amazonaws.com/8a72ee8e-d4aa-4a06-985f-e92802c5bc44/images/d950f08f-93af-4fff-8d50-71dfdda2b7a0.jpg?w=1500&f=webp)

#### Lấy thông tin To-do

```
GET http://localhost:8080/api/v1/todo/1
```

![spring-boot-rest-api](https://super-static-assets.s3.amazonaws.com/8a72ee8e-d4aa-4a06-985f-e92802c5bc44/images/4eb7cc67-51bf-49ab-8e2a-78e590bc6f6b.jpg?w=1500&f=webp)

#### Xóa To-do

```
DELETE http://localhost:8080/api/v1/todo/1
```

![spring-boot-rest-api](https://super-static-assets.s3.amazonaws.com/8a72ee8e-d4aa-4a06-985f-e92802c5bc44/images/31649562-34f9-4cdd-b8d2-0923bfac9672.jpg?w=1500&f=webp)


### Kết

Như mọi khi, [toàn bộ code tham khảo tại Github][link-github]
<a class="btn btn-icon btn-github mr-1" target="_blank" href="https://github.com/loda-kun/spring-boot-learning">
<i class="fab fa-github"></i>
</a>

[link-lombok]: https://loda.me/general-huong-dan-su-dung-lombok-giup-code-java-nhanh-hon-69-loda1552789752787/
[link-spring-boot-13]: https://loda.me/articles/sb13-chi-tit-spring-boot-thymeleaf-mysql-i18n-web-demo
[link-github]: https://github.com/loda-kun/spring-boot-learning