@echo off
REM Script để reset database và chạy lại ứng dụng

echo ================================
echo Reset Database
echo ================================
echo.

echo [1/3] Stopping any running application...
echo Press Ctrl+C if application is running in another window
timeout /t 3 > nul
echo.

echo [2/3] Resetting database...
echo Running SQL script to drop and recreate database...
psql -U postgres -f reset_database.sql

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Failed to reset database
    echo.
    echo Please make sure:
    echo 1. PostgreSQL is running
    echo 2. Password for 'postgres' user is correct
    echo 3. User 'music_admin' exists
    echo.
    echo You can also reset manually:
    echo   psql -U postgres
    echo   DROP DATABASE music_management;
    echo   CREATE DATABASE music_management;
    echo   GRANT ALL PRIVILEGES ON DATABASE music_management TO music_admin;
    echo.
    pause
    exit /b 1
)

echo.
echo Database reset successfully!
echo.

echo [3/3] Starting application...
echo Flyway will run migrations automatically...
echo.

mvn spring-boot:run

pause

