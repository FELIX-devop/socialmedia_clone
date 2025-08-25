#!/bin/bash

echo "🚀 Starting Instagram Clone Development Environment"
echo "=================================================="

# Check if MongoDB is running
echo "📊 Checking MongoDB..."
if ! pgrep -x "mongod" > /dev/null; then
    echo "❌ MongoDB is not running. Please start MongoDB first:"
    echo "   brew services start mongodb-community"
    echo "   or run: mongod"
    exit 1
else
    echo "✅ MongoDB is running"
fi

# Start Python Image Validator
echo "🐍 Starting Python Image Validator (Port 5001)..."
cd python-validator
python3 -m pip install -r requirements.txt > /dev/null 2>&1
python3 app.py &
PYTHON_PID=$!
cd ..
echo "✅ Python validator started (PID: $PYTHON_PID)"

# Wait a moment for Python to start
sleep 3

# Start Spring Boot Backend
echo "☕ Starting Spring Boot Backend (Port 8080)..."
cd backend
./mvnw spring-boot:run > /dev/null 2>&1 &
SPRING_PID=$!
cd ..
echo "✅ Spring Boot backend started (PID: $SPRING_PID)"

# Wait for Spring Boot to start
echo "⏳ Waiting for backend to start..."
sleep 15

# Start React Frontend
echo "⚛️  Starting React Frontend (Port 5173)..."
cd frontend
npm install > /dev/null 2>&1
npm run dev > /dev/null 2>&1 &
REACT_PID=$!
cd ..
echo "✅ React frontend started (PID: $REACT_PID)"

echo ""
echo "🎉 All services are starting up!"
echo "=================================================="
echo "📱 Frontend: http://localhost:5173"
echo "🔧 Backend:  http://localhost:8080"
echo "🐍 Python:   http://localhost:5001"
echo ""
echo "👤 Demo Account:"
echo "   Username: demo"
echo "   Password: demo123"
echo ""
echo "🛑 To stop all services, run: pkill -f 'python3 app.py' && pkill -f 'spring-boot:run' && pkill -f 'vite'"
echo ""
echo "📋 Service PIDs:"
echo "   Python: $PYTHON_PID"
echo "   Spring: $SPRING_PID"
echo "   React:  $REACT_PID"
