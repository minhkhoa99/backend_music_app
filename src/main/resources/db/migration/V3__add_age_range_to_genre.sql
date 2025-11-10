-- Migration V3: Thêm cột age_range vào bảng music_genre
-- Đáp ứng yêu cầu đề bài: "Thể loại nhạc (mã, tên, LỰA TUỔI NGHE)"

ALTER TABLE music_genre
ADD COLUMN age_range VARCHAR(50);

COMMENT ON COLUMN music_genre.age_range IS 'Lựa tuổi nghe phù hợp (VD: "Mọi lứa tuổi", "13+", "18+", "Trẻ em", "Người lớn")';

-- Cập nhật dữ liệu mẫu
UPDATE music_genre SET age_range = 'Mọi lứa tuổi' WHERE genre_code IN ('ROCK', 'POP', 'JAZZ', 'CLASSICAL');
UPDATE music_genre SET age_range = '13+' WHERE genre_code IN ('HIPHOP', 'METAL');
UPDATE music_genre SET age_range = 'Người lớn' WHERE genre_code IN ('BLUES');
UPDATE music_genre SET age_range = 'Gia đình' WHERE genre_code IN ('COUNTRY', 'FOLK');
UPDATE music_genre SET age_range = '16+' WHERE genre_code IN ('ELECTRONIC');
