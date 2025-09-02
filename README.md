# Spring Boot Boilerplate

Dự án mẫu giúp khởi tạo ứng dụng web Java với Spring Boot 3. Bao gồm xác thực JWT, giao tiếp WebSocket, tích hợp Redis và thiết kế tính năng theo mô-đun.

## Tính năng
- **Bảo mật JWT** với danh sách đen token lưu trong Redis
- **REST API** cho đăng nhập, quản lý người dùng, thông báo, thiết bị và các tính năng khác
- **WebSocket** phục vụ chat và thông báo thời gian thực
- **SQL Server** lưu trữ dữ liệu thông qua Spring Data JPA
- **OpenAPI/Swagger UI** tài liệu API sử dụng springdoc-openapi

## Yêu cầu
- [Java 21](https://adoptium.net/)
- [Maven](https://maven.apache.org/) 3.9+
- Máy chủ [SQL Server](https://www.microsoft.com/en-us/sql-server)
- Máy chủ [Redis](https://redis.io/)

## Bắt đầu
1. Sao chép kho lưu trữ:
   ```bash
   git clone <repo-url>
   cd springboot-boilerplate
   ```
2. Cấu hình cơ sở dữ liệu, Redis và JWT trong `src/main/resources/application.properties`.
3. Biên dịch và chạy ứng dụng:
   ```bash
   ./mvnw spring-boot:run
   ```
   API sẽ chạy tại `http://localhost:8080`.

## Tài liệu API
Khi ứng dụng chạy, truy cập Swagger UI tại:
```
http://localhost:8080/swagger-ui.html
```

## Chạy kiểm thử
Thực thi toàn bộ kiểm thử đơn vị:
```bash
./mvnw test
```

## Cấu trúc dự án
```
src/
 └── main
     ├── java/com/instar/...   # Mã nguồn Java
     └── resources/            # application.properties, templates
uploads/                      # Tệp được tải lên
```

## Đóng góp
Hoan nghênh các pull request. Với thay đổi lớn, vui lòng mở issue để trao đổi trước.

## Giấy phép
Dự án chưa được cấp phép.

