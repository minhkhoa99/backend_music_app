-- Tạo bảng thể loại nhạc (music_genre)
CREATE TABLE music_genre (
    id BIGSERIAL PRIMARY KEY,
    genre_code VARCHAR(50) UNIQUE NOT NULL,
    genre_name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Thêm comment cho bảng
COMMENT ON TABLE music_genre IS 'Bảng quản lý thể loại nhạc';
COMMENT ON COLUMN music_genre.genre_code IS 'Mã thể loại (unique)';
COMMENT ON COLUMN music_genre.genre_name IS 'Tên thể loại nhạc';

-- Thêm dữ liệu mẫu
INSERT INTO music_genre (genre_code, genre_name, description) VALUES
('ROCK', 'Rock', 'Nhạc Rock'),
('POP', 'Pop', 'Nhạc Pop'),
('JAZZ', 'Jazz', 'Nhạc Jazz'),
('CLASSICAL', 'Classical', 'Nhạc Cổ điển'),
('HIPHOP', 'Hip Hop', 'Nhạc Hip Hop'),
('COUNTRY', 'Country', 'Nhạc Country'),
('BLUES', 'Blues', 'Nhạc Blues'),
('ELECTRONIC', 'Electronic', 'Nhạc Điện tử'),
('FOLK', 'Folk', 'Nhạc Dân gian'),
('METAL', 'Metal', 'Nhạc Metal');

