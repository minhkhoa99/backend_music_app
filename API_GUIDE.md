# HƯỚNG DẪN SỬ DỤNG API

## Cài đặt và Chạy ứng dụng

### 1. Sử dụng Maven (Local)

```bash
# Clone project
cd C:\Users\Admin\Desktop\music_management

# Cài đặt dependencies
mvn clean install

# Chạy ứng dụng
mvn spring-boot:run
```

### 2. Sử dụng Docker

```bash
# Build và chạy với Docker Compose
docker-compose up -d

# Xem logs
docker-compose logs -f app

# Dừng services
docker-compose down
```

---

## Truy cập ứng dụng

- **Application URL**: http://localhost:3005
- **Swagger UI**: http://localhost:3005/swagger-ui.html
- **API Docs**: http://localhost:3005/api-docs

---

## Ví dụ sử dụng API

### 1. Quản lý Thể loại Nhạc

#### Tạo thể loại mới

```bash
curl -X POST http://localhost:3005/api/genres \
  -H "Content-Type: application/json" \
  -d '{
    "genreCode": "VPOP",
    "genreName": "Vietnamese Pop",
    "description": "Nhạc Pop Việt Nam"
  }'
```

#### Lấy danh sách thể loại

```bash
curl http://localhost:3005/api/genres
```

#### Lấy thể loại theo ID

```bash
curl http://localhost:3005/api/genres/1
```

#### Cập nhật thể loại

```bash
curl -X PUT http://localhost:3005/api/genres/1 \
  -H "Content-Type: application/json" \
  -d '{
    "genreCode": "VPOP",
    "genreName": "Vietnamese Pop Music",
    "description": "Nhạc Pop Việt Nam hiện đại"
  }'
```

#### Xóa thể loại

```bash
curl -X DELETE http://localhost:3005/api/genres/1
```

---

### 2. Quản lý File Nhạc

#### Tạo file nhạc mới

```bash
curl -X POST http://localhost:3005/api/music-files \
  -H "Content-Type: application/json" \
  -d '{
    "fileCode": "SONG001",
    "fileName": "Bài hát mùa hè",
    "filePath": "/uploads/song001.mp3",
    "fileType": "mp3",
    "genreId": 1,
    "artist": "Nguyễn Văn A",
    "album": "Album Mùa Hè",
    "releaseYear": 1980,
    "duration": 240,
    "fileSize": 5242880,
    "description": "Bài hát về mùa hè"
  }'
```

#### Upload file nhạc

```bash
curl -X POST http://localhost:3005/api/music-files/upload \
  -F "file=@C:\Music\song.mp3" \
  -F "fileCode=SONG002" \
  -F "fileName=Bài hát mới" \
  -F "genreId=1" \
  -F "artist=Trần Văn B" \
  -F "releaseYear=2020"
```

#### Lấy danh sách file nhạc (có phân trang)

```bash
curl "http://localhost:3005/api/music-files?page=0&size=10&sort=createdAt,desc"
```

#### Tìm kiếm file nhạc

```bash
curl "http://localhost:3005/api/music-files/search?keyword=mùa hè"
```

#### Lọc theo thể loại

```bash
curl "http://localhost:3005/api/music-files/filter/genre/1?page=0&size=10"
```

#### Lọc theo nghệ sĩ

```bash
curl "http://localhost:3005/api/music-files/filter/artist?artist=Nguyễn Văn A"
```

#### Lọc theo năm phát hành

```bash
curl "http://localhost:3005/api/music-files/filter/year/1980"
```

---

### 3. Báo cáo & Thống kê

#### Lấy danh sách file nhạc cũ hơn 40 năm

```bash
curl "http://localhost:3005/api/reports/old-music?minAge=40"
```

#### Thống kê theo thể loại

```bash
curl http://localhost:3005/api/reports/by-genre
```

#### Thống kê theo năm

```bash
curl http://localhost:3005/api/reports/by-year
```

#### Báo cáo storage

```bash
curl http://localhost:3005/api/reports/storage
```

---

## Response Examples

### Success Response (200 OK)

```json
{
  "id": 1,
  "fileCode": "SONG001",
  "fileName": "Bài hát mùa hè",
  "filePath": "/uploads/song001.mp3",
  "fileType": "mp3",
  "genreId": 1,
  "genreName": "Vietnamese Pop",
  "artist": "Nguyễn Văn A",
  "album": "Album Mùa Hè",
  "duration": 240,
  "fileSize": 5242880,
  "releaseYear": 1980,
  "age": 44,
  "createdAt": "2025-10-29T10:00:00",
  "updatedAt": "2025-10-29T10:00:00"
}
```

### Error Response (404 Not Found)

```json
{
  "status": 404,
  "message": "Music file not found with id: 999",
  "timestamp": "2025-10-29T10:00:00"
}
```

### Validation Error Response (400 Bad Request)

```json
{
  "status": 400,
  "errors": {
    "fileCode": "Mã file không được để trống",
    "fileName": "Tên file không được để trống"
  },
  "timestamp": "2025-10-29T10:00:00"
}
```

---

## Pagination Parameters

Tất cả các API trả về danh sách đều hỗ trợ phân trang:

- `page`: Số trang (bắt đầu từ 0)
- `size`: Số lượng items trên mỗi trang
- `sort`: Sắp xếp (ví dụ: `createdAt,desc` hoặc `fileName,asc`)

Ví dụ:
```
GET /api/music-files?page=0&size=20&sort=releaseYear,desc
```

---

## Testing

### Chạy Unit Tests

```bash
mvn test
```

### Chạy Integration Tests

```bash
mvn verify
```

---

## Troubleshooting

### Lỗi kết nối Database

Kiểm tra PostgreSQL đang chạy:
```bash
# Windows
pg_ctl status

# Check connection
psql -U music_admin -d music_management
```

### Lỗi Upload File

- Kiểm tra thư mục `uploads/` có quyền write
- Kiểm tra kích thước file không vượt quá 100MB
- Kiểm tra định dạng file được phép (mp3, wav, flac, etc.)

### Xem Logs

```bash
# Trong Docker
docker-compose logs -f app

# Local
# Logs sẽ hiển thị trong console hoặc file logs/
```

