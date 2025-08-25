@echo off
echo 🚀 Starting Instagram Clone Development Environment
echo ==================================================

REM Check if MongoDB is running (Windows)
echo 📊 Checking MongoDB...
netstat -an | findstr ":27017" >nul
if %errorlevel% neq 0 (
    echo ❌ MongoDB is not running. Please start MongoDB first.
    echo    Make sure MongoDB service is running or mongod.exe is started.
    pause
    exit /b 1
) else (
    echo ✅ MongoDB is running
)

REM Start Python Image Validator
echo 🐍 Starting Python Image Validator (Port 5001)...
cd python-validator
python -m pip install -r requirements.txt >nul 2>&1
start "Python Validator" python app.py
cd ..
echo ✅ Python validator started

REM Wait a moment for Python to start
timeout /t 3 /nobreak >nul

REM Start Spring Boot Backend
echo ☕ Starting Spring Boot Backend (Port 8080)...
cd backend
start "Spring Boot" mvn spring-boot:run
cd ..
echo ✅ Spring Boot backend started

REM Wait for Spring Boot to start
echo ⏳ Waiting for backend to start...
timeout /t 15 /nobreak >nul

REM Start React Frontend
echo ⚛️  Starting React Frontend (Port 5173)...
cd frontend
npm install >nul 2>&1
start "React Frontend" npm run dev
cd ..
echo ✅ React frontend started

echo.
echo 🎉 All services are starting up!
echo ==================================================
echo 📱 Frontend: http://localhost:5173
echo 🔧 Backend:  http://localhost:8080
echo 🐍 Python:   http://localhost:5001
echo.
echo 👤 Demo Account:
echo    Username: demo
echo    Password: demo123
echo.
echo 🛑 Close the command windows to stop the services
echo.
pause
