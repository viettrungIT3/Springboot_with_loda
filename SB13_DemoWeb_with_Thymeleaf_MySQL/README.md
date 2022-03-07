# [SB13] Demo Web Todo với Thymeleaf + MySQL + i18n

Chi tiết tại: [Loda.me - 「Spring Boot #13 Special」 Chi tiết Spring Boot + Thymeleaf + MySQL + i18n + Web Demo](https://loda.me/articles/sb13-chi-tit-spring-boot-thymeleaf-mysql-i18n-web-demo)

### Giới thiệu

Hôm nay, chúng ta sẽ vận dụng toàn bộ kiến thức đã học để tạo ra website quản lý công việc bằng Spring Boot + Thymeleaf + MySQL.

### Cài đặt

Chúng ta sẽ các dependencies sau:

1. spring-boot-starter-web
2. lombok
3. spring-boot-starter-thymeleaf
4. spring-boot-starter-data-jpa
5. mysql-connector-java

Cấu trúc thư mục:

![spring-thymleaf-mysql](https://super-static-assets.s3.amazonaws.com/8a72ee8e-d4aa-4a06-985f-e92802c5bc44/images/4e8709ee-6d3f-4120-a35f-19b0ab8f7e20.jpg?w=1500&f=webp)
![sspring-thymleaf-mysq](https://super-static-assets.s3.amazonaws.com/8a72ee8e-d4aa-4a06-985f-e92802c5bc44/images/7ffd392e-c512-4d14-9f4e-d50b8f30cb84.jpg?w=1500&f=webp)

### Tạo Database

_script.sql_

```sql
CREATE SCHEMA IF NOT EXISTS `todo_db` DEFAULT CHARACTER SET utf8mb4 ;

CREATE TABLE IF NOT EXISTS `todo_db`.`todo` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NULL DEFAULT NULL,
  `detail` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;
```

Thêm 1 record vào DB

```sql
INSERT INTO `todo_db`.`todo` (`title`, `detail`) VALUES ('Làm bài tập', 'Hoàn thiện bài viết Spring Boot #13');
```

Xem thử kết quả:

![sspring-thymleaf-mysq](https://super-static-assets.s3.amazonaws.com/8a72ee8e-d4aa-4a06-985f-e92802c5bc44/images/e45682df-0720-4b51-94dc-3c63fa4dc79e.jpg?w=1500&f=webp)

### Cấu hình ứng dụng

Cấu hình là phần cực kì quan trọng rồi, chúng ta phải cung cấp cho **Spring Boot** các thông tin về Database và Thymeleaf.

Ngoài ra, tùy chỉnh một số thông tin để giúp chúng ta lập trình đơn giản hơn.

_application.properties_

```java
#Chạy ứng dụng trên port 8085
server.port=8085

# Bỏ tính năng cache của thymeleaf để lập trình cho nhanh
spring.thymeleaf.cache=false

# Các message tĩnh sẽ được lưu tại thư mục i18n
spring.messages.basename=i18n/messages


# Bỏ properties này đi khi deploy
# Nó có tác dụng cố định ngôn ngữ hiện tại chỉ là Tiếng Việt
spring.web.locale-resolver=fixed

spring.mvc.view.prefix=/WEB-INT/views/
spring.mvc.view.suffix=.jsp

# Mặc định ngôn ngữ là tiếng việt
spring.web.locale=vi_VN
# Đổi thành tiếng anh bằng cách bỏ comment ở dứoi
#spring.web.locale=en_US

spring.datasource.url=jdbc:mysql://localhost:3306/todo_db?useSSL=false
spring.datasource.username=root
spring.datasource.password=abc@123


## Hibernate Properties
# The SQL dialect makes Hibernate generate better SQL for the chosen database
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5InnoDBDialect
# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto = update
```

### Tạo Model

Tạo model `Todo` liên kết tới bảng `todo` trong Database.

Phần này chúng ta sử dụng [Lombok][link-lombok] và [Hibernate][link-hibernate]
_Todo.java_

```java
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String detail;
}

```

Ngoài ra, chúng ta tạo thêm một đối tượng là `TodoValidator`, có trách nhiệm kiểm tra xem một object `Todo` là hợp lệ hay không.

```java
import org.thymeleaf.util.StringUtils;

/*
Đối tượng này dùng để kiểm tra xem một Object Todo có hợp lệ không
 */
public class TodoValidator {

    /**
     * Kiểm tra một object Todo có hợp lệ không
     * @param todo
     * @return
     */
    public boolean isValid(Todo todo) {
        return Optional.ofNullable(todo)
                       .filter(t -> !StringUtils.isEmpty(t.getTitle())) // Kiểm tra title khác rỗng
                       .filter(t -> !StringUtils.isEmpty(t.getDetail())) // Kiểm tra detail khác rỗng
                       .isPresent(); // Trả về true nếu hợp lệ, ngược lại false
    }
}


```

Vậy là xong phần chuẩn bị `Model`.


### TodoConfig

Trong ứng dụng của mình, tôi muốn tự tạo ra Bean `TodoValidator`.

Đây là lúc sử dụng `@Configuration` và `@Bean` đã học tại bài [Spring Boot #6][link-spring-boot-6]

_config/TodoConfig.java_

```java
@Configuration
public class TodoConfig {
    /**
     * Tạo ra Bean TodoValidator để sử dụng sau này
     * @return
     */
    @Bean
    public TodoValidator validator() {
        return new TodoValidator();
    }
}

```

### Tầng Repository

Tầng Repository, chịu trách nhiệm giao tiếp với Database. Chúng ta sử dụng **Spring JPA**.

_repository/TodoRepository.java_

```java
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
```

### Tầng Service

Tầng Service, chị trách nhiệm thực hiện các xử lý logic, business, hỗ trợ cho tầng Controller.

_service/TodoService.java_

```java
@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoValidator validator;

    /**
     * Lấy ra danh sách List<Todo>
     *
     * @param limit - Giới hạn số lượng lấy ra
     *
     * @return Trả về danh sách List<Todo> dựa theo limit, nếu limit == null thì trả findAll()
     */
    public List<Todo> findAll(Integer limit) {
        return Optional.ofNullable(limit)
                       .map(value -> todoRepository.findAll(PageRequest.of(0, value)).getContent())
                       .orElseGet(() -> todoRepository.findAll());
    }

    /**
     * Thêm một Todo mới vào danh sách công việc cần làm
     *
     * @return Trả về đối tượng Todo sau khi lưu vào DB, trả về null nếu không thành công
     */
    public Todo add(Todo todo) {
        if (validator.isValid(todo)) {
            return todoRepository.save(todo);
        }
        return null;
    }
}

```

### Tầng Controller

Tầng Controller, nơi đón nhận các request từ phía người dùng, và chuyển tiếp xử lý xuống tầng Service.

_controller/TodoController.java_

```java
@Controller
public class TodoController {

    @Autowired
    private TodoService todoService;

    /*
    @RequestParam dùng để đánh dấu một biến là request param trong request gửi lên server.
    Nó sẽ gán dữ liệu của param-name tương ứng vào biến
     */
    @GetMapping("/listTodo")
    public String index(Model model, @RequestParam(value = "limit", required = false) Integer limit) {
        // Trả về đối tượng todoList.
        model.addAttribute("todoList", todoService.findAll(limit));
        // Trả về template "listTodo.html"
        return "listTodo";
    }

    @GetMapping("/addTodo")
    public String addTodo(Model model) {
        model.addAttribute("todo", new Todo());
        return "addTodo";
    }

    /*
    @ModelAttribute đánh dấu đối tượng Todo được gửi lên bởi Form Request
     */
    @PostMapping("/addTodo")
    public String addTodo(@ModelAttribute Todo todo) {
        return Optional.ofNullable(todoService.add(todo))
                       .map(t -> "success") // Trả về success nếu save thành công
                       .orElse("failed"); // Trả về failed nếu không thành công

    }
}

```

### Templates

Tầng Controller đã trả về templates, nhiệm vụ tiếp theo là sử dụng Template Engine để xử lý các templates này và trả về webpage cho người dùng.

Template Engine chúng ta sử dụng sẽ là **Thymeleaf**, đã học tại các bài Spring Boot [#8][link-spring-boot-8], [#9][link-spring-boot-9], [#10][link-spring-boot-10].

_index.html_

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8" />
  <title>Loda To-do</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

  <!--css-->
  <link th:href="@{/css/bootstrap.css}" rel="stylesheet" />
  <link th:href="@{/css/main.css}" rel="stylesheet" />

  <!--js-->
  <script th:src="@{/js/bootstrap.js}"></script>
</head>
<body>
<h1 th:text="#{loda.message.hello}"></h1>

<a th:href="@{/listTodo}" th:text="#{loda.value.viewListTodo}" class="btn btn-primary"></a>
<a th:href="@{/addTodo}" th:text="#{loda.value.addTodo}" class="btn btn-success"></a>
</body>

</html>
```

_listTodo.html_

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8" />
  <title>Loda To-do</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

  <!--css-->
  <link th:href="@{/css/bootstrap.css}" rel="stylesheet" />
  <link th:href="@{/css/main.css}" rel="stylesheet" />

  <!--js-->
  <script th:src="@{/js/bootstrap.js}"></script>
</head>
<body>
<h1 th:text="#{loda.value.listTodo} + ':'"></h1>


<ul>
  <!--Duyệt qua toàn bộ phần tử trong biến "todoList"-->
  <li th:each="todo : ${todoList}">
    <!--Với mỗi phần tử, lấy ra title và detail-->
    <span th:text="*{todo.getTitle()}"></span> : <span th:text="*{todo.getDetail()}"></span>
  </li>
</ul>

<a th:href="@{/addTodo}" th:text="#{loda.value.addTodo}" class="btn btn-success"></a>
</body>

</html>
```

_addTodo.html_

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8" />
  <title>Loda To-do</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

  <!--css-->
  <link th:href="@{/css/bootstrap.css}" rel="stylesheet" />
  <link th:href="@{/css/main.css}" rel="stylesheet" />

  <!--js-->
  <script th:src="@{/js/bootstrap.js}"></script>

</head>
<body>
<h1>To-do</h1>

<form th:action="@{/addTodo}" th:object="${todo}" method="post">
  <p>title: <input type="text" th:field="*{title}" /></p>
  <p>detail: <input type="text" th:field="*{detail}" /></p>
  <p><button type="submit" class="btn btn-success">Add</button></p>
</form>
</body>

</html>
```

_success.html_

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8" />
  <title>Loda To-do</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

  <link th:href="@{/css/bootstrap.css}" rel="stylesheet" />
  <link th:href="@{/css/main.css}" rel="stylesheet" />

</head>
<body>
<h1>To-do</h1>
<h1 style="color: green" th:text="#{loda.message.success}"></h1>

<a th:href="@{/listTodo}" th:text="#{loda.value.viewListTodo}" class="btn btn-primary"></a>

</body>

</html>
```

_failed.html_

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8" />
  <title>Loda To-do</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

  <link th:href="@{/css/bootstrap.css}" rel="stylesheet" />
  <link th:href="@{/css/main.css}" rel="stylesheet" />

</head>
<body>
<h1>To-do</h1>
<h1 style="color: red" th:text="#{loda.message.failed}"></h1>

<a th:href="@{/listTodo}" th:text="#{loda.value.viewListTodo}" class="btn btn-primary"></a>

</body>

</html>
```

### i18n

Trong các template, tôi có sử dụng các message tĩnh, những message này hỗ trợ đa ngôn ngữ.

Chúng ta định nghĩa các message này tại thư mục `i18n`.

_i18n/messages_vi.properties_

```java
loda.message.hello=Welcome to TodoApp
loda.message.success=Thêm Todo thành công!
loda.message.failed=Thêm Todo không thành công!

loda.value.addTodo=Thêm công việc
loda.value.viewListTodo=Xem danh sách công việc
loda.value.listTodo=Danh sách công việc
```

_i18n/messages_en.properties_

```java
loda.message.hello=Welcome to TodoApp
loda.message.success=Add To-do Successfully!
loda.message.failed=Add To-do Failed!

loda.value.addTodo=Add To-do
loda.value.viewListTodo=View To-do list
loda.value.listTodo=To-do list
```

### Chạy thử ứng dụng

Chạy ứng dụng:

_App.java_

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

Truy cập địa chỉ: `http://localhost:8085/`

![sspring-thymleaf-mysq](https://super-static-assets.s3.amazonaws.com/8a72ee8e-d4aa-4a06-985f-e92802c5bc44/images/3455f3e8-42ae-4596-b61b-c66a886b762a.jpg?w=1500&f=webp)

Vì chúng ta cấu hình `Locale` là `vi`, nên ngôn ngữ đều hiện Tiếng Việt, rất tuyệt :3

Bấm vào **Xem danh sách công việc** để tới `/listTodo`

![sspring-thymleaf-mysq](https://super-static-assets.s3.amazonaws.com/8a72ee8e-d4aa-4a06-985f-e92802c5bc44/images/2a40224b-a980-4521-981d-f7cb0b6a5f9f.jpg?w=1500&f=webp)

Vì chúng ta đã insert 1 bản ghi vào Database từ trước, nên ở đây nó hiện ra 1 việc cần làm.

Bấm vào **Thêm công việc** để tới `/addTodo`

![sspring-thymleaf-mysq](https://super-static-assets.s3.amazonaws.com/8a72ee8e-d4aa-4a06-985f-e92802c5bc44/images/1a6837ca-4032-47a3-9656-28431ac4eb03.jpg?w=1500&f=webp)

Bấm **Add** để lưu thông tin vào Database.

![sspring-thymleaf-mysq](https://super-static-assets.s3.amazonaws.com/8a72ee8e-d4aa-4a06-985f-e92802c5bc44/images/4f86983f-5c95-4500-916b-65a2a5b225f3.jpg?w=1500&f=webp)

Vậy là giờ chúng ta có 2 công việc :3

![sspring-thymleaf-mysq](https://super-static-assets.s3.amazonaws.com/8a72ee8e-d4aa-4a06-985f-e92802c5bc44/images/04f8dd2f-c50e-4345-af2f-629c6b752193.jpg?w=1500&f=webp)

Bây giờ giả sử dụng ta gửi lên request tạo ra một Todo không hợp lệ.

`TodoValidator` sẽ trả về null -> thêm thất bại

![sspring-thymleaf-mysq](https://super-static-assets.s3.amazonaws.com/8a72ee8e-d4aa-4a06-985f-e92802c5bc44/images/916ffdd6-7df4-43f9-91f8-29043e948fee.jpg?w=1500&f=webp)

### Kết

Vậy là chúng ta đã đi được nửa Series **Spring Boot**.

Trong các phần tới, chúng ta sẽ tìm hiểu về cách làm RestAPI với **Spring Boot**, đây mới là phần mạnh mẽ của nhất.


Như mọi khi, [toàn bộ code tham khảo tại Github][link-github]
<a class="btn btn-icon btn-github mr-1" target="_blank" href="https://github.com/loda-kun/spring-boot-learning">
<i class="fab fa-github"></i>
</a>







[link-lombok]: https://loda.me/general-huong-dan-su-dung-lombok-giup-code-java-nhanh-hon-69/
[link-hibernate]: https://loda.me/hibernate-la-gi
[link-github]: https://github.com/loda-kun/spring-boot-learning