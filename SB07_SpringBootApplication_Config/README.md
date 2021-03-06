# [SB7] Spring Boot Application Config và @Value


## application.properties
Trong **Spring Boot**, các thông tin cấu hình mặc định được lấy từ file `resources/applications.properties`.

Ví dụ, bạn muốn Spring Boot chạy trên port 8081 thay vì 8080:

_applicatoin.properties_

    server.port = 8081

Hoặc bạn muốn log của chương trình chi tiết hơn. Hãy chuyển nó sang dậng Debug bằng cách config như sau:

    logging.level.root=DEBUG

Đây là cách chúng ta có thể can thiệp vào các cấu hình của ứng dụng từ bên ngoài. Cho phép thay đổi linh hoạt tùy môi trường.

## @Value
Trong trường hợp, bạn muốn tự config những giá trị của riêng mình, thì **Spring Boot** hỗ trợ bạn với annotation `@Value`

Ví dụ, tôi muốn cấu hình cho thông tin database của tôi từ bên ngoài ứng dụng

_application.properties_

    loda.mysql.url=jdbc:mysql://host1:33060/loda

`@Value` được sử dụng trên thuộc tính của class, Có nhiệm vụ lấy thông tin từ file properties và gán vào biến.

    public class AppConfig {
        // Lấy giá trị config từ file application.properties
        @Value("${loda.mysql.url}")
        String mysqlUrl;
    }
Thông tin truyền vào annottaion `@Value` chính là tên của cấu hình đặt trong dấu `${name}`

## Kết luận
Bạn sẽ thấy là chương trình đã chạy trên port 8081. Và cấu hình về đường dẫn mysql của tôi tự tạo ra cũng được **Spring Boot** đọc lên và đưa vào giá trị này vào biến.

## Chi tiết tại: https://loda.me/articles/sb7-spring-boot-application-config-va-value