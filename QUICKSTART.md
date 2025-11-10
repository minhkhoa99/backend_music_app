# ⚡ QUICK START GUIDE

## 🚀 Bắt đầu nhanh trong 5 phút!

### 📋 Yêu cầu

- ✅ JDK 17 hoặc cao hơn
- ✅ PostgreSQL 14+
- ✅ Maven 3.6+ (hoặc Docker)

---

## 🎯 Option 1: Chạy với Docker (Đơn giản nhất)

### Bước 1: Cài Docker Desktop
- Download: https://www.docker.com/products/docker-desktop/

### Bước 2: Chạy ứng dụng
```bash
# Mở Command Prompt tại thư mục dự án
cd C:\Users\Admin\Desktop\music_management

# Chạy Docker
docker-run.bat
```

### Bước 3: Truy cập
- 🌐 Application: http://localhost:3005
- 📚 Swagger UI: http://localhost:3005/swagger-ui.html

**✅ Xong! Dễ dàng phải không?**

---

## 🎯 Option 2: Chạy Local (Cho developers)

### Bước 1: Setup Database

```sql
-- Mở pgAdmin hoặc psql, chạy:
CREATE DATABASE music_management;
CREATE USER music_admin WITH PASSWORD 'changeme';
GRANT ALL PRIVILEGES ON DATABASE music_management TO music_admin;
```

### Bước 2: Cấu hình

Mở file `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/music_management
spring.datasource.username=music_admin
spring.datasource.password=changeme  # Đổi password nếu khác
```

### Bước 3: Chạy ứng dụng

```bash
# Option A: Sử dụng script
run.bat

# Option B: Manual
mvn clean install
mvn spring-boot:run
```
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"; mvn spring-boot:run
```

### Bước 4: Truy cập
- 🌐 Application: http://localhost:3005
- 📚 Swagger UI: http://localhost:3005/swagger-ui.html

**✅ Hoàn tất!**

---

## 🧪 Test nhanh API

### 1️⃣ Tạo thể loại nhạc

Mở Swagger UI: http://localhost:3005/swagger-ui.html

**Hoặc dùng curl:**

```bash
curl -X POST http://localhost:3005/api/genres ^
  -H "Content-Type: application/json" ^
  -d "{\"genreCode\":\"VPOP\",\"genreName\":\"Vietnamese Pop\",\"description\":\"Nhac Pop Viet\"}"
```

**Response:**
```json
{
  "id": 1,
  "genreCode": "VPOP",
  "genreName": "Vietnamese Pop",
  "description": "Nhac Pop Viet",
  "createdAt": "2025-10-29T10:00:00",
  "updatedAt": "2025-10-29T10:00:00"
}
```

### 2️⃣ Xem danh sách thể loại

```bash
curl http://localhost:3005/api/genres
```

### 3️⃣ Tạo file nhạc

```bash
curl -X POST http://localhost:3005/api/music-files ^
  -H "Content-Type: application/json" ^
  -d "{\"fileCode\":\"SONG001\",\"fileName\":\"Bai hat mua he\",\"filePath\":\"/uploads/song001.mp3\",\"fileType\":\"mp3\",\"genreId\":1,\"artist\":\"Nguyen Van A\",\"releaseYear\":1980}"
```

### 4️⃣ Xem file nhạc cũ >40 năm

```bash
curl "http://localhost:3005/api/reports/old-music?minAge=40"
```

**✅ API hoạt động tốt!**

---

## 📚 Tiếp theo là gì?

### Đọc tài liệu chi tiết:

1. **README.md** - Tổng quan dự án
2. **API_GUIDE.md** - Hướng dẫn API đầy đủ
3. **DATABASE_SETUP.md** - Chi tiết về database
4. **ARCHITECTURE.md** - Kiến trúc hệ thống
5. **SUMMARY.md** - Tóm tắt toàn bộ

### Thử các tính năng:

- ✅ CRUD operations
- ✅ Upload file nhạc
- ✅ Tìm kiếm và lọc
- ✅ Báo cáo thống kê

### Phát triển thêm:

- 🔜 Thêm authentication
- 🔜 Tạo frontend
- 🔜 Deploy lên cloud

---

## ❓ Gặp vấn đề?

### Lỗi database connection:
```bash
# Kiểm tra PostgreSQL đang chạy
psql --version
# Kiểm tra port
netstat -an | findstr 5432
```

### Lỗi port 3005 đã được sử dụng:
Sửa `application.properties`:
```properties
server.port=8081  # Đổi sang port khác
```

### Xem logs:
```bash
# Docker
docker-compose logs -f app

# Local: logs hiển thị trong console
```

---

## 🎉 Chúc mừng!

Bạn đã setup thành công **Music Management System**!

🔥 **Next steps:**
1. Khám phá Swagger UI
2. Test các APIs
3. Đọc tài liệu chi tiết
4. Phát triển thêm tính năng

**Happy Coding! 🚀**

---

## 📞 Support

- 📖 Docs: Xem các file .md trong project
- 🌐 Swagger: http://localhost:3005/swagger-ui.html
- 💬 Issues: Tạo issue nếu gặp lỗi

