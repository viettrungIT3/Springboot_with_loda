# [SB4] @Component vs @Service vs @Repository

### Controller-Service - Repository chia project thành 3 lớp:
![](https://super-static-assets.s3.amazonaws.com/8a72ee8e-d4aa-4a06-985f-e92802c5bc44/images/f5ff73d5-5bd6-4d03-a0e1-da9418ec9a70.png?w=1500&f=webp)

- **Consumer Layer** hay **Controller**: là tầng giao tiếp với bên ngoài và handler các request từ bên ngoài tới hệ thống.

- **Service Layer**: Thực hiện các nghiệp vụ và xử lý logic

- **Repository Layer**: Chịu trách nhiệm giao tiếp với các DB, thiết bị lưu trữ, xử lý query và trả về các kiểu dữ liệu mà tầng Service yêu cầu.

### @Controller vs @Service vs @Repository
Để phục vụ cho kiến trúc ở trên, **Spring Boot** tạo ra 3 Annotation là `@Controller` vs `@Service` vs `@Repository` để chúng ta có thể đánh dấu các tầng với nhau.

- `@Service` Đánh dấu một Class là tầng Service, phục vụ các logic nghiệp vụ.
- `@Repository` Đánh dấu một Class Là tầng Repository, phục vụ truy xuất dữ liệu.

### Giải thích:
Về bản chất `@Service` và `@Repository` cũng chính là `@Component`. Nhưng đặt tên khác nhau để giúp chúng ta phân biệt các tầng với nhau.

Trong các bài đầu tiên chúng ta đã biết `@Component` đánh dấu cho Spring Boot biết Class đó là `Bean`. Và hiển nhiên `@Service` và `@Repository` cũng vậy. Vì thế ở ví dụ trên chúng ta có thể lấy `GirlService` từ `ApplicationContext`.

Về bản chất thì bạn có thể sử dụng thay thế 3 Annotation `@Component`, `@Service` và `@Repository` cho nhau mà không ảnh hưởng gì tới code của bạn cả. Nó vẫn sẽ hoạt động.

Tuy nhiên từ góc độ thiết kế thì chúng ta cần phân rõ 3 Annotation này cho các Class đảm nhiệm đúng nhiệm vụ của nó.

- `@Service` gắn cho các `Bean` đảm nhiệm xử lý logic
- `@Repository` gắn cho các `Bean` đảm nhiệm giao tiếp với DB
- `@Component` gắn cho các `Bean` khác.\


### Chi tiết tại: https://loda.me/articles/sb4-component-vs-service-vs-repository