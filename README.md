# Spring Boot Boilerplate

Ứng dụng mẫu giúp khởi tạo nhanh dự án web với Spring Boot 3, cấu trúc theo mô-đun và hỗ trợ đầy đủ bảo mật, thời gian thực và các tiện ích thường dùng.

## Mục lục
- [Kiến trúc và công nghệ](#kiến-trúc-và-công-nghệ)
- [Chức năng chính](#chức-năng-chính)
- [Yêu cầu](#yêu-cầu)
- [Cài đặt & Chạy](#cài-đặt--chạy)
- [Cấu hình](#cấu-hình)
- [Tài liệu API](#tài-liệu-api)
- [Chạy kiểm thử](#chạy-kiểm-thử)
- [Cấu trúc thư mục](#cấu-trúc-thư-mục)
- [Đóng góp](#đóng-góp)
- [Giấy phép](#giấy-phép)

## Kiến trúc và công nghệ
- **Spring Boot 3** với `spring-boot-starter-parent`.
- **Web MVC** và `spring-boot-starter-data-jpa` cho REST và truy cập CSDL.
- **Spring Security** + **JWT** với danh sách đen token lưu trong **Redis**.
- **WebSocket** xử lý chat/thông báo thời gian thực.
- Hỗ trợ **SQL Server** và **PostgreSQL** qua HikariCP.
- **Flyway** quản lý migration.
- **MapStruct** và **Lombok** giảm boilerplate.
- Log định dạng JSON với `logstash-logback-encoder`.
- **springdoc-openapi** sinh tài liệu API.

## Chức năng chính
- Đăng ký, đăng nhập và quản lý người dùng.
- Quản lý thiết bị, thông báo.
- Trò chuyện real-time qua WebSocket.
- Upload tệp.
- Gói common chứa util, filter, service, exception.

## Yêu cầu
- [Java 21](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/)
- Máy chủ [SQL Server](https://www.microsoft.com/en-us/sql-server) hoặc [PostgreSQL](https://www.postgresql.org/)
- Máy chủ [Redis](https://redis.io/)

## Cài đặt & Chạy
1. Clone repo:
   ```bash
   git clone <repo-url>
   cd springboot-boilerplate
   ```
2. Thiết lập biến môi trường:
   ```bash
   export JWT_SECRET=<jwt-secret>
   export SQLSERVER_PASSWORD=<db-password>
   export REDIS_PASSWORD=<redis-password>
   ```
3. Cấu hình thêm trong `src/main/resources/application.properties` nếu cần.
4. Biên dịch và chạy:
   ```bash
   ./mvnw spring-boot:run
   ```
   Ứng dụng chạy tại `http://localhost:8080`.

Để đóng gói JAR:
```bash
./mvnw clean package
java -jar target/boilerplate.jar
```

## Cấu hình
`application.properties` bao gồm cấu hình server, datasource, Redis, JWT và logging. Các thông số nhạy cảm được lấy từ biến môi trường.

## Tài liệu API
Khi ứng dụng chạy, truy cập:
```
http://localhost:8080/swagger-ui.html
```

## Chạy kiểm thử
```bash
./mvnw test
```

## Cấu trúc thư mục
```
src/
 ├── main
 │   ├── java/com/boilerplate/...  # mã nguồn
 │   └── resources/               # config, template, migration
uploads/                          # tệp tải lên
logs/                             # log output
```

## Đóng góp
Mọi đóng góp đều được hoan nghênh. Vui lòng tạo issue để thảo luận trước khi gửi pull request lớn.

## Giấy phép
Dự án chưa kèm theo giấy phép chính thức. Vui lòng liên hệ nếu muốn sử dụng.

