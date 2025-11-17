-- Migration V4: Thêm cột age_range vào bảng music_file
-- Mục đích: Lưu thông tin lứa tuổi người nghe phù hợp cho từng file nhạc

ALTER TABLE music_file
ADD COLUMN age_range VARCHAR(50);

COMMENT ON COLUMN music_file.age_range IS 'Lứa tuổi người nghe phù hợp (VD: "Trẻ em", "Thanh thiếu niên", "18+", "40+", "Người cao tuổi", "Mọi lứa tuổi")';

-- Cập nhật giá trị mặc định cho các bản ghi hiện có
UPDATE music_file SET age_range = 'Mọi lứa tuổi' WHERE age_range IS NULL;
