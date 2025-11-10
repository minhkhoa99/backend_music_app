# 🎵 Music Management System

Hệ thống quản lý file nhạc được xây dựng bằng Java Spring Boot và PostgreSQL.

> 📑 **[Xem MỤC LỤC ĐẦY ĐỦ tại INDEX.md](INDEX.md)** để tìm tài liệu bạn cần!
> 
> ⚡ **Mới bắt đầu?** → Đọc [QUICKSTART.md](QUICKSTART.md) để chạy ứng dụng trong 5 phút!

## 🚀 Tính năng

- ✅ Quản lý thể loại nhạc (CRUD)
- ✅ Quản lý file nhạc (CRUD)
- ✅ Upload/Download file nhạc
- ✅ Tìm kiếm và lọc file nhạc theo nhiều tiêu chí
- ✅ Báo cáo thống kê file nhạc theo độ tuổi
- ✅ Trích xuất metadata từ file nhạc
- ✅ RESTful API với Swagger documentation

## 🛠️ Tech Stack

- **Backend**: Java 17, Spring Boot 3.2
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA / Hibernate
- **Migration**: Flyway
- **API Doc**: Swagger/OpenAPI
- **Build Tool**: Maven

## 📋 Yêu cầu hệ thống

- JDK 17 hoặc cao hơn
- Maven 3.6+
- PostgreSQL 14+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

## 🔧 Cài đặt

### 1. Clone repository

```bash
git clone <repository-url>
cd music_management
```

### 2. Cấu hình Database

Tạo database trong PostgreSQL:

```sql
CREATE DATABASE music_management;
CREATE USER music_admin WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE music_management TO music_admin;
```

### 3. Cấu hình application.properties

Cập nhật file `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/music_management
spring.datasource.username=music_admin
spring.datasource.password=your_password
```

### 4. Build và chạy ứng dụng

```bash
mvn clean install
mvn spring-boot:run
```

Ứng dụng sẽ chạy tại: `http://localhost:3005`

## 📚 API Documentation

Sau khi chạy ứng dụng, truy cập Swagger UI tại:
- http://localhost:3005/swagger-ui.html

## 🗂️ Cấu trúc API

### Music Genre APIs
- `POST /api/genres` - Tạo thể loại mới
- `GET /api/genres` - Lấy danh sách thể loại
- `GET /api/genres/{id}` - Lấy chi tiết thể loại
- `PUT /api/genres/{id}` - Cập nhật thể loại
- `DELETE /api/genres/{id}` - Xóa thể loại

### Music File APIs
- `POST /api/music-files` - Thêm file nhạc mới
- `GET /api/music-files` - Lấy danh sách file nhạc (có phân trang)
- `GET /api/music-files/{id}` - Lấy chi tiết file nhạc
- `PUT /api/music-files/{id}` - Cập nhật file nhạc
- `DELETE /api/music-files/{id}` - Xóa file nhạc
- `POST /api/music-files/upload` - Upload file nhạc
- `GET /api/music-files/download/{id}` - Download file nhạc

### Search & Filter APIs
- `GET /api/music-files/search?keyword={keyword}` - Tìm kiếm
- `GET /api/music-files/filter?genreId={id}` - Lọc theo thể loại

### Report APIs
- `GET /api/reports/old-music?minAge=40` - File nhạc > 40 năm tuổi
- `GET /api/reports/by-genre` - Thống kê theo thể loại

## 🧪 Testing

Chạy tests:

```bash
mvn test
```

## 📦 Build for Production

```bash
mvn clean package
java -jar target/music-management-1.0.0.jar
```

## 🐳 Docker (Optional)

```bash
docker-compose up -d
```

## 📝 License

MIT License

## 👤 Author

Developed with ❤️

