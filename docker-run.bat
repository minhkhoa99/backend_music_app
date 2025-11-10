@echo off
REM Script để build Docker image và chạy với Docker Compose

echo ================================
echo Music Management - Docker Setup
echo ================================
echo.

REM Kiểm tra Docker
echo [1/3] Checking Docker installation...
docker --version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Docker is not installed or not running
    echo Please install Docker Desktop for Windows
    pause
    exit /b 1
)
echo Docker OK
echo.

REM Kiểm tra Docker Compose
echo [2/3] Checking Docker Compose installation...
docker-compose --version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Docker Compose is not installed
    pause
    exit /b 1
)
echo Docker Compose OK
echo.

REM Chạy Docker Compose
echo [3/3] Starting services with Docker Compose...
echo This may take a few minutes for the first run...
echo.

docker-compose up -d

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to start services
    pause
    exit /b 1
)

echo.
echo ================================
echo Services started successfully!
echo ================================
echo.
echo Application: http://localhost:3005
echo Swagger UI: http://localhost:3005/swagger-ui.html
echo PostgreSQL: localhost:5432
echo.
echo To view logs: docker-compose logs -f
echo To stop services: docker-compose down
echo.

pause

