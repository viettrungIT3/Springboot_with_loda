
## [SB3] Spring Bean Life Cycle + @PostConstruct và @PreDestroy

### Lý thuyết: 
- [@PostConstruct](https://loda.me/articles/sb3-spring-bean-life-cycle-postconstruct-va-predestroy#28ed34818d4f41ee9d7b0a96ac9b1257) được đánh dấu trên một method duy nhất bên trong `Bean`. `IoC Container` hoặc `ApplicationContext` sẽ gọi hàm này **_sau khi_** một `Bean` _được tạo ra và quản lý_.
- [@PreDestroy](https://loda.me/articles/sb3-spring-bean-life-cycle-postconstruct-va-predestroy#e90b66ec79ef4f298a3258d8ba1c4c69) được đánh dấu trên một method duy nhất bên trong `Bean`. `IoC Container` hoặc `ApplicationContext` sẽ gọi hàm này _**trước khi**_ một `Bean` _bị xóa hoặc không được quản lý nữa_.
- [Bean Life Cycle](https://loda.me/articles/sb3-spring-bean-life-cycle-postconstruct-va-predestroy#bd8e85abfd064e6796de5f44d4d4c32d): Spring Boot từ thời điểm chạy lần đầu tới khi _shutdown_ thì các `Bean` nó quản lý sẽ có một vòng đời được biểu diễn:
  1. Khi `IoC Container` (`ApplicationContext`) tìm thấy một Bean cần quản lý, nó sẽ khởi tạo bằng `Constructor`
  2. inject dependencies vào `Bean` bằng Setter, và thực hiện các quá trình cài đặt khác vào `Bean` như `setBeanName`, `setBeanClassLoader`, v.v..
  3. Hàm đánh dấu `@PostConstruct` được gọi
  4. Tiền xử lý sau khi `@PostConstruct` được gọi.
  5. `Bean` sẵn sàng để hoạt động
  6. Nếu `IoC Container` không quản lý bean nữa hoặc bị shutdown nó sẽ gọi hàm `@PreDestroy` trong `Bean`
  7. Xóa `Bean`.

### Ý nghĩa.
`@PostConstruct` và `@PreDestroy` là 2 Annotation cực kỳ ý nghĩa, nếu bạn nắm được vòng đời của một `Bean`, bạn có thể tận dụng nó để làm các nhiệm vụ riêng như setting, thêm giá trị mặc định trong thuộc tính sau khi tạo, xóa dữ liệu trước khi xóa, v.v.. Rất nhiều chức năng khác tùy theo nhu cầu.

#### **_Nguồn_**: https://loda.me/articles/sb3-spring-bean-life-cycle-postconstruct-va-predestroy