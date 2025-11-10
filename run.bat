@echo off
REM Script để setup và chạy ứng dụng trên Windows

echo ================================
echo Music Management System Setup
echo ================================
echo.

REM Kiểm tra Java
echo [1/5] Checking Java installation...
java -version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install JDK 17 or higher
    pause
    exit /b 1
)
echo Java OK
echo.

REM Kiểm tra Maven
echo [2/5] Checking Maven installation...
mvn -version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven 3.6 or higher
    pause
    exit /b 1
)
echo Maven OK
echo.

REM Kiểm tra PostgreSQL
echo [3/5] Checking PostgreSQL installation...
psql --version
if %ERRORLEVEL% NEQ 0 (
    echo WARNING: PostgreSQL is not installed or not in PATH
    echo Please make sure PostgreSQL is running
    echo.
)
echo.

REM Build project
echo [4/5] Building project...
call mvn clean install -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Build failed
    pause
    exit /b 1
)
echo Build successful
echo.

REM Run application
echo [5/5] Starting application...
echo Application will be available at: http://localhost:3005
echo Swagger UI: http://localhost:3005/swagger-ui.html
echo.
echo Press Ctrl+C to stop the application
echo.

call mvn spring-boot:run

pause

