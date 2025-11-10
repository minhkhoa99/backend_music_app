-- Tạo bảng file nhạc (music_file)
CREATE TABLE music_file (
    id BIGSERIAL PRIMARY KEY,
    file_code VARCHAR(100) UNIQUE NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    thumbnail_path VARCHAR(500),
    file_type VARCHAR(50),
    genre_id BIGINT REFERENCES music_genre(id) ON DELETE SET NULL,
    download_link VARCHAR(500),
    description TEXT,
    artist VARCHAR(255),
    album VARCHAR(255),
    duration INTEGER, -- thời lượng (giây)
    file_size BIGINT, -- kích thước file (bytes)
    release_year INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo indexes để tăng tốc độ tìm kiếm
CREATE INDEX idx_music_file_genre ON music_file(genre_id);
CREATE INDEX idx_music_file_artist ON music_file(artist);
CREATE INDEX idx_music_file_release_year ON music_file(release_year);
CREATE INDEX idx_music_file_code ON music_file(file_code);
CREATE INDEX idx_music_file_name ON music_file(file_name);

-- Thêm comment cho bảng
COMMENT ON TABLE music_file IS 'Bảng quản lý file nhạc';
COMMENT ON COLUMN music_file.file_code IS 'Mã file nhạc (unique)';
COMMENT ON COLUMN music_file.file_name IS 'Tên file nhạc';
COMMENT ON COLUMN music_file.file_path IS 'Đường dẫn lưu trữ file';
COMMENT ON COLUMN music_file.thumbnail_path IS 'Đường dẫn hình ảnh thumbnail';
COMMENT ON COLUMN music_file.file_type IS 'Loại file (mp3, wav, flac, etc.)';
COMMENT ON COLUMN music_file.duration IS 'Thời lượng bài hát (giây)';
COMMENT ON COLUMN music_file.file_size IS 'Kích thước file (bytes)';
COMMENT ON COLUMN music_file.release_year IS 'Năm phát hành';

-- Function để tự động cập nhật updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger cho music_genre
CREATE TRIGGER update_music_genre_updated_at
    BEFORE UPDATE ON music_genre
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Trigger cho music_file
CREATE TRIGGER update_music_file_updated_at
    BEFORE UPDATE ON music_file
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

