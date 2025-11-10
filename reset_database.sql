-- Script để reset database và chạy lại migration

-- Kết nối vào database postgres (default database)
-- Chạy script này bằng cách:
-- psql -U postgres -f reset_database.sql

-- Disconnect all users from music_management database
SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'music_management'
  AND pid <> pg_backend_pid();

-- Drop database nếu tồn tại
DROP DATABASE IF EXISTS music_management;

-- Tạo lại database
CREATE DATABASE music_management;

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE music_management TO music_admin;

-- Connect to new database
\c music_management

-- Grant schema permissions
GRANT ALL ON SCHEMA public TO music_admin;

-- Done
\echo 'Database reset successfully! Now run: mvn spring-boot:run'

