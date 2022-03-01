## [SB2] @Autowired - @Primary - @Qualifier
- [@Autowired](https://loda.me/articles/sb2-autowired-primary-qualifier#010cd97b5ca74dc7a90235af2d80175c) đánh dấu cho Spring biết rằng sẽ tự động inject bean tương ứng vào vị trí được đánh dấu.
- Trong thực tế, sẽ có trường hợp chúng ta sử dụng @Autowired khi Spring Boot có chứa 2 Bean cùng loại trong Context.
Lúc này thì Spring sẽ bối rối và không biết sử dụng Bean nào để inject vào đối tượng.
Có 2 cách giải quyết: 
  - Cách 1: [@Primary](https://loda.me/articles/sb2-autowired-primary-qualifier#6c89cca093d448368aa38d3a4bfcf92b) là annotation đánh dấu trên một Bean, giúp nó luôn được ưu tiên lựa chọn trong trường hợp có nhiều Bean cùng loại trong Context.
  - Cách 2: [@Qualifier](https://loda.me/articles/sb2-autowired-primary-qualifier#e0d1b8b20bc54846bef7f7d1dba246ce) xác định tên của một Bean mà bạn muốn chỉ định inject.

#### `Kết luận`: @Primary và @Qualifier là một trong những tính năng bạn nên biết trong Spring để có thể xử lý vấn đề nhiều Bean cùng loại trong một Project.

Nguồn: https://loda.me/articles/sb2-autowired-primary-qualifier