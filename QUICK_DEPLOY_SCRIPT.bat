@echo off
echo ========================================
echo AI Data Dashboard - Quick Deploy Script
echo ========================================
echo.

REM Check if Docker is installed
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Docker is not installed or not in PATH
    echo Please install Docker Desktop from https://docker.com
    pause
    exit /b 1
)

echo Docker found: 
docker --version
echo.

REM Check if we're in the right directory
if not exist "ai-dashboard\package.json" (
    echo ERROR: Please run this script from the demo root directory
    echo Current directory should contain ai-dashboard and demo folders
    pause
    exit /b 1
)

echo Building and starting AI Data Dashboard...
echo.

REM Stop existing containers
echo Stopping existing containers...
docker-compose down

REM Build and start new containers
echo Building and starting containers...
docker-compose up --build -d

echo.
echo ========================================
echo Deployment Complete!
echo ========================================
echo.
echo Frontend: http://localhost
echo Backend: http://localhost:8080
echo.
echo To view logs: docker-compose logs -f
echo To stop: docker-compose down
echo To restart: docker-compose restart
echo.
echo Checking container status...
docker-compose ps

echo.
echo Waiting for services to start...
timeout /t 10 /nobreak >nul

echo.
echo Testing services...
curl -s http://localhost:8080/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo Backend health check: OK
) else (
    echo Backend health check: Starting up...
)

curl -s http://localhost >nul 2>&1
if %errorlevel% equ 0 (
    echo Frontend: OK
) else (
    echo Frontend: Starting up...
)

echo.
echo ========================================
echo Your AI Data Dashboard is deploying!
echo ========================================
echo.
echo It may take a few minutes to fully start.
echo You can check the status with: docker-compose ps
echo.
echo Once ready, open your browser and go to:
echo http://localhost
echo.
echo For production deployment, see DEPLOYMENT_GUIDE.md
echo.
pause
