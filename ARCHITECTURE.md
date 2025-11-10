# 🏗️ KIẾN TRÚC HỆ THỐNG - MUSIC MANAGEMENT

## 📊 TỔNG QUAN KIẾN TRÚC

```
┌─────────────────────────────────────────────────────────────┐
│                        CLIENT LAYER                          │
│  (Browser, Postman, Mobile App, External Systems)           │
└───────────────────────┬─────────────────────────────────────┘
                        │ HTTP/HTTPS
                        ▼
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Genre      │  │  MusicFile   │  │   Report     │      │
│  │  Controller  │  │  Controller  │  │  Controller  │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│         │                  │                  │              │
│         └──────────────────┴──────────────────┘              │
└─────────────────────────┬───────────────────────────────────┘
                          │ DTO
                          ▼
┌─────────────────────────────────────────────────────────────┐
│                      BUSINESS LAYER                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Genre      │  │  MusicFile   │  │   File       │      │
│  │   Service    │  │   Service    │  │   Util       │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│         │                  │                  │              │
│         └──────────────────┴──────────────────┘              │
└─────────────────────────┬───────────────────────────────────┘
                          │ Entity
                          ▼
┌─────────────────────────────────────────────────────────────┐
│                    PERSISTENCE LAYER                         │
│  ┌──────────────┐  ┌──────────────┐                         │
│  │   Genre      │  │  MusicFile   │  Spring Data JPA        │
│  │  Repository  │  │  Repository  │  + Hibernate            │
│  └──────────────┘  └──────────────┘                         │
└─────────────────────────┬───────────────────────────────────┘
                          │ JDBC
                          ▼
┌─────────────────────────────────────────────────────────────┐
│                      DATABASE LAYER                          │
│                    PostgreSQL 15+                            │
│  ┌──────────────┐  ┌──────────────┐                         │
│  │ music_genre  │  │  music_file  │                         │
│  │   (table)    │  │   (table)    │                         │
│  └──────────────┘  └──────────────┘                         │
└─────────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────────┐
│                      FILE STORAGE                            │
│        /uploads/music  |  /uploads/thumbnails               │
└─────────────────────────────────────────────────────────────┘
```

---

## 🎯 LAYERED ARCHITECTURE

### 1. Presentation Layer (Controller)

**Nhiệm vụ:**
- Nhận HTTP requests từ clients
- Validate request parameters
- Gọi Service layer
- Trả về HTTP responses
- Xử lý exception

**Components:**
- `MusicGenreController` - APIs quản lý thể loại
- `MusicFileController` - APIs quản lý file nhạc
- `ReportController` - APIs báo cáo thống kê
- `GlobalExceptionHandler` - Xử lý exception toàn cục

**Endpoints:**
```
/api/genres/*
/api/music-files/*
/api/reports/*
```

---

### 2. Business Layer (Service)

**Nhiệm vụ:**
- Business logic chính
- Validate business rules
- Transaction management
- Tương tác với Repository layer
- Convert giữa Entity và DTO

**Components:**
- `MusicGenreService` & `MusicGenreServiceImpl`
- `MusicFileService` & `MusicFileServiceImpl`
- `FileUtil` - Utility cho file operations

**Business Rules:**
- Validate mã thể loại/file unique
- Kiểm tra file type và size
- Tính toán độ tuổi file nhạc
- Xử lý metadata extraction

---

### 3. Persistence Layer (Repository)

**Nhiệm vụ:**
- Truy vấn database
- CRUD operations
- Custom queries
- Data access abstraction

**Components:**
- `MusicGenreRepository` - Spring Data JPA Repository
- `MusicFileRepository` - Spring Data JPA Repository

**Features:**
- Auto-generated CRUD methods
- Custom query methods
- JPQL queries
- Pagination support

---

### 4. Database Layer

**Schema:**
```
music_genre (Thể loại nhạc)
├── id (PK)
├── genre_code (UNIQUE)
├── genre_name
├── description
├── created_at
└── updated_at

music_file (File nhạc)
├── id (PK)
├── file_code (UNIQUE)
├── file_name
├── file_path
├── thumbnail_path
├── file_type
├── genre_id (FK → music_genre.id)
├── download_link
├── description
├── artist
├── album
├── duration
├── file_size
├── release_year
├── created_at
└── updated_at
```

**Indexes:**
- Primary keys (id)
- Unique constraints (genre_code, file_code)
- Foreign keys (genre_id)
- Search indexes (artist, release_year)

---

## 🔄 DATA FLOW

### Tạo File Nhạc Mới (Create Music File)

```
1. Client
   │ POST /api/music-files
   │ Request Body: MusicFileDTO
   ▼
2. MusicFileController
   │ @PostMapping
   │ Validate @Valid
   ▼
3. MusicFileService
   │ createMusicFile(dto)
   │ - Check duplicate file_code
   │ - Convert DTO → Entity
   │ - Validate genre exists
   ▼
4. MusicFileRepository
   │ save(entity)
   ▼
5. PostgreSQL
   │ INSERT INTO music_file
   ▼
6. Return Flow
   │ Entity → DTO → Response
   │ HTTP 201 Created
   ▼
7. Client receives MusicFileDTO
```

### Upload File Nhạc (Upload Music File)

```
1. Client
   │ POST /api/music-files/upload
   │ multipart/form-data
   ▼
2. MusicFileController
   │ @PostMapping("/upload")
   │ MultipartFile + params
   ▼
3. FileUtil
   │ validateMusicFile()
   │ - Check file type (mp3, wav, etc.)
   │ - Check file size (max 100MB)
   ▼
4. FileUtil
   │ saveMusicFile()
   │ - Generate unique filename
   │ - Save to /uploads/music
   ▼
5. FileUtil (Optional)
   │ extractMetadata()
   │ - Extract artist, album, duration
   ▼
6. MusicFileService
   │ createMusicFile(dto)
   │ Save metadata to database
   ▼
7. Return file path & metadata
```

### Tìm kiếm File Nhạc (Search)

```
1. Client
   │ GET /api/music-files/search?keyword=xyz
   ▼
2. MusicFileController
   │ searchMusicFiles(keyword, pageable)
   ▼
3. MusicFileService
   │ searchMusicFiles(keyword, pageable)
   ▼
4. MusicFileRepository
   │ @Query: SELECT ... WHERE
   │ fileName LIKE %keyword% OR
   │ artist LIKE %keyword%
   ▼
5. PostgreSQL
   │ Full-text search on indexed columns
   ▼
6. Return Page<MusicFile>
   │ Convert to Page<MusicFileDTO>
   ▼
7. Client receives paginated results
```

### Báo cáo File Cũ >40 năm (Report)

```
1. Client
   │ GET /api/reports/old-music?minAge=40
   ▼
2. ReportController
   │ getOldMusicFiles(40)
   ▼
3. MusicFileService
   │ getOldMusicFiles(40)
   │ Calculate: currentYear - minAge
   ▼
4. MusicFileRepository
   │ findOldMusicFiles(maxReleaseYear)
   │ WHERE release_year <= 1984
   ▼
5. PostgreSQL
   │ SELECT * FROM music_file
   │ WHERE release_year <= 1984
   ▼
6. Return List<MusicFile>
   │ Convert to List<MusicFileDTO>
   │ Calculate age for each file
   ▼
7. Client receives list of old files
```

---

## 🔐 SECURITY CONSIDERATIONS

### Current Implementation

- ✅ Input validation (Bean Validation)
- ✅ SQL Injection protection (JPA/Hibernate)
- ✅ File type validation
- ✅ File size limits
- ✅ Exception handling
- ✅ CORS configuration

### Recommended Additions (Future)

- 🔜 Spring Security + JWT authentication
- 🔜 Role-based access control (RBAC)
- 🔜 API rate limiting
- 🔜 Request encryption (HTTPS)
- 🔜 File virus scanning
- 🔜 Audit logging

---

## ⚡ PERFORMANCE OPTIMIZATIONS

### Implemented

- ✅ Database indexing
- ✅ Pagination for large datasets
- ✅ Lazy loading for relationships
- ✅ Connection pooling (HikariCP)
- ✅ Query optimization (JPQL)

### Recommended (Future)

- 🔜 Redis caching (for genres, reports)
- 🔜 CDN for file delivery
- 🔜 Async file processing
- 🔜 Database query caching
- 🔜 Load balancing

---

## 📦 DEPENDENCIES

### Core Framework
- Spring Boot 3.2.0
- Spring Web
- Spring Data JPA
- Hibernate

### Database
- PostgreSQL Driver
- Flyway (Migration)

### Utilities
- Lombok (Reduce boilerplate)
- Apache Commons IO
- Apache Tika (Metadata)
- JAudioTagger (Audio metadata)

### Documentation
- SpringDoc OpenAPI (Swagger)

### Testing
- Spring Boot Test
- JUnit 5
- Mockito
- H2 Database (for tests)

---

## 🌐 DEPLOYMENT ARCHITECTURE

### Option 1: Standalone

```
┌─────────────────────┐
│   Spring Boot App   │ :3005
│   (Embedded Tomcat) │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│    PostgreSQL DB    │ :5432
└─────────────────────┘
```

### Option 2: Docker Compose

```
┌─────────────────────┐
│  Docker Container   │
│  ┌───────────────┐  │
│  │ Spring Boot   │  │ :3005
│  │ Application   │  │
│  └───────┬───────┘  │
│          │          │
│          ▼          │
│  ┌───────────────┐  │
│  │  PostgreSQL   │  │ :5432
│  │   Database    │  │
│  └───────────────┘  │
└─────────────────────┘
   Docker Network
```

### Option 3: Production (Cloud)

```
        Load Balancer
              │
    ┌─────────┴─────────┐
    │                   │
┌───▼───┐         ┌─────▼──┐
│ App 1 │         │ App 2  │  Spring Boot
└───┬───┘         └────┬───┘  Instances
    │                  │
    └────────┬─────────┘
             ▼
    ┌────────────────┐
    │ PostgreSQL RDS │       Managed DB
    └────────────────┘
             │
             ▼
    ┌────────────────┐
    │  File Storage  │       S3/Azure Blob
    └────────────────┘
```

---

## 🎓 DESIGN PATTERNS

### Applied Patterns

1. **Layered Architecture** - Separation of concerns
2. **Repository Pattern** - Data access abstraction
3. **DTO Pattern** - Data transfer objects
4. **Service Pattern** - Business logic encapsulation
5. **Dependency Injection** - Loose coupling
6. **Builder Pattern** - Lombok @Builder
7. **Factory Pattern** - Spring Bean creation
8. **Singleton Pattern** - Spring @Service, @Repository

### SOLID Principles

- ✅ **S**ingle Responsibility - Each class has one job
- ✅ **O**pen/Closed - Open for extension, closed for modification
- ✅ **L**iskov Substitution - Service interfaces
- ✅ **I**nterface Segregation - Focused interfaces
- ✅ **D**ependency Inversion - Depend on abstractions

---

## 📈 SCALABILITY

### Horizontal Scaling
- Stateless application design
- Shared database
- Shared file storage (S3/NFS)
- Load balancer distribution

### Vertical Scaling
- Increase JVM heap size
- Database connection pool tuning
- CPU/Memory upgrade

### Database Scaling
- Read replicas
- Partitioning by year/genre
- Sharding (if needed)

---

**Kiến trúc này được thiết kế để dễ maintain, mở rộng và test!** 🚀

