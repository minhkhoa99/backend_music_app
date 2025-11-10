# HƯỚNG DẪN SETUP DATABASE

## 1. Cài đặt PostgreSQL

### Windows

1. Download PostgreSQL từ: https://www.postgresql.org/download/windows/
2. Chạy installer và làm theo hướng dẫn
3. Ghi nhớ password của user `postgres`
4. Mặc định PostgreSQL chạy trên port 5432

### Kiểm tra PostgreSQL đã cài đặt

```bash
psql --version
```

---

## 2. Tạo Database và User

### Sử dụng pgAdmin hoặc psql

#### Option 1: Sử dụng psql (Command Line)

```bash
# Đăng nhập vào PostgreSQL
psql -U postgres

# Trong psql console, chạy các lệnh sau:
```

```sql
-- Tạo database
CREATE DATABASE music_management;

-- Tạo user
CREATE USER music_admin WITH PASSWORD 'changeme';

-- Gán quyền
GRANT ALL PRIVILEGES ON DATABASE music_management TO music_admin;

-- Kết nối vào database
\c music_management

-- Gán quyền trên schema
GRANT ALL ON SCHEMA public TO music_admin;

-- Kiểm tra
\l  -- Liệt kê databases
\du -- Liệt kê users
```

#### Option 2: Sử dụng pgAdmin

1. Mở pgAdmin
2. Right-click "Databases" → "Create" → "Database"
3. Nhập tên: `music_management`
4. Click "Save"
5. Right-click "Login/Group Roles" → "Create" → "Login/Group Role"
6. Nhập username: `music_admin`
7. Tab "Definition" → nhập password: `changeme`
8. Tab "Privileges" → check "Can login"
9. Click "Save"

---

## 3. Cấu hình Connection String

### Development Environment

File: `src/main/resources/application-dev.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/music_management
spring.datasource.username=music_admin
spring.datasource.password=changeme
```

### Production Environment

File: `src/main/resources/application-prod.properties`

```properties
spring.datasource.url=jdbc:postgresql://your-server:5432/music_management
spring.datasource.username=music_admin
spring.datasource.password=${DB_PASSWORD}  # Sử dụng environment variable
```

---

## 4. Migration với Flyway

Khi chạy ứng dụng lần đầu, Flyway sẽ tự động:

1. Tạo bảng `flyway_schema_history` để theo dõi migrations
2. Chạy các file migration trong `src/main/resources/db/migration/`
3. Tạo các bảng: `music_genre`, `music_file`
4. Thêm dữ liệu mẫu vào bảng `music_genre`

### Kiểm tra Migration Status

```sql
SELECT * FROM flyway_schema_history;
```

---

## 5. Tạo thủ công các bảng (nếu cần)

### Bảng music_genre

```sql
CREATE TABLE music_genre (
    id SERIAL PRIMARY KEY,
    genre_code VARCHAR(50) UNIQUE NOT NULL,
    genre_name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Bảng music_file

```sql
CREATE TABLE music_file (
    id SERIAL PRIMARY KEY,
    file_code VARCHAR(100) UNIQUE NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    thumbnail_path VARCHAR(500),
    file_type VARCHAR(50),
    genre_id INTEGER REFERENCES music_genre(id) ON DELETE SET NULL,
    download_link VARCHAR(500),
    description TEXT,
    artist VARCHAR(255),
    album VARCHAR(255),
    duration INTEGER,
    file_size BIGINT,
    release_year INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo indexes
CREATE INDEX idx_music_file_genre ON music_file(genre_id);
CREATE INDEX idx_music_file_artist ON music_file(artist);
CREATE INDEX idx_music_file_release_year ON music_file(release_year);
```

---

## 6. Thêm dữ liệu mẫu

### Thể loại nhạc

```sql
INSERT INTO music_genre (genre_code, genre_name, description) VALUES
('ROCK', 'Rock', 'Nhạc Rock'),
('POP', 'Pop', 'Nhạc Pop'),
('JAZZ', 'Jazz', 'Nhạc Jazz'),
('CLASSICAL', 'Classical', 'Nhạc Cổ điển'),
('VPOP', 'Vietnamese Pop', 'Nhạc Pop Việt Nam');
```

### File nhạc mẫu

```sql
INSERT INTO music_file (
    file_code, file_name, file_path, file_type, genre_id, 
    artist, album, release_year, duration, file_size
) VALUES
('SONG001', 'Bài hát mùa hè', '/uploads/song001.mp3', 'mp3', 1, 
 'Nguyễn Văn A', 'Album Mùa Hè', 1980, 240, 5242880),
('SONG002', 'Nơi tình yêu bắt đầu', '/uploads/song002.mp3', 'mp3', 2, 
 'Trần Thị B', 'Album Tình Yêu', 1975, 300, 6291456),
('SONG003', 'Jazz về đêm', '/uploads/song003.mp3', 'mp3', 3, 
 'John Smith', 'Midnight Jazz', 1960, 420, 8388608);
```

---

## 7. Queries hữu ích

### Kiểm tra số lượng records

```sql
SELECT 'music_genre' as table_name, COUNT(*) as count FROM music_genre
UNION ALL
SELECT 'music_file', COUNT(*) FROM music_file;
```

### Xem file nhạc cũ hơn 40 năm

```sql
SELECT 
    file_name, 
    artist, 
    release_year,
    EXTRACT(YEAR FROM CURRENT_DATE) - release_year as age
FROM music_file
WHERE release_year <= EXTRACT(YEAR FROM CURRENT_DATE) - 40
ORDER BY release_year ASC;
```

### Thống kê theo thể loại

```sql
SELECT 
    mg.genre_name,
    COUNT(mf.id) as file_count,
    SUM(mf.file_size) as total_size
FROM music_genre mg
LEFT JOIN music_file mf ON mg.id = mf.genre_id
GROUP BY mg.genre_name
ORDER BY file_count DESC;
```

### Thống kê theo năm

```sql
SELECT 
    release_year,
    COUNT(*) as file_count
FROM music_file
WHERE release_year IS NOT NULL
GROUP BY release_year
ORDER BY release_year DESC;
```

---

## 8. Backup và Restore

### Backup Database

```bash
pg_dump -U music_admin -d music_management -F c -f music_management_backup.dump
```

### Restore Database

```bash
pg_restore -U music_admin -d music_management -c music_management_backup.dump
```

---

## 9. Troubleshooting

### Lỗi: "password authentication failed"

- Kiểm tra lại username và password
- Kiểm tra file `pg_hba.conf` (phương thức xác thực)

### Lỗi: "could not connect to server"

- Kiểm tra PostgreSQL service đang chạy
- Kiểm tra port 5432 không bị chặn bởi firewall

### Kiểm tra PostgreSQL đang chạy (Windows)

```bash
# Trong Services (services.msc), tìm "postgresql"
# Hoặc
pg_ctl status -D "C:\Program Files\PostgreSQL\15\data"
```

### Reset Password (nếu quên)

1. Edit file `pg_hba.conf`
2. Thay `md5` thành `trust` cho local connections
3. Restart PostgreSQL service
4. Đổi password:
   ```sql
   ALTER USER music_admin WITH PASSWORD 'new_password';
   ```
5. Đổi lại `trust` thành `md5` trong `pg_hba.conf`
6. Restart PostgreSQL service

