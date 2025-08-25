# Instagram Clone - React + Spring Boot + MongoDB + Python

A minimal Instagram clone built with modern web technologies.

## Tech Stack
- **Frontend**: React + Vite + React Router (Port 5173)
- **Backend**: Spring Boot 3, Java 17, Spring Web, Spring Data MongoDB, Spring Security (Port 8080)
- **Database**: MongoDB (local) - database: `insta_clone`
- **Python Microservice**: Flask with Pillow for image validation (Port 5001)

## Quick Start

### Prerequisites
- Java 17+
- Node.js 16+
- Python 3.8+
- MongoDB (local installation)

### 1. MongoDB Setup
```bash
# Start MongoDB (macOS with Homebrew)
brew services start mongodb-community

# Or start manually
mongod

# Create database (will be created automatically when first post is created)
```

### 2. Python Image Validator
```bash
cd python-validator
pip install flask pillow
python app.py
```
**Runs on**: http://localhost:5001

### 3. Spring Boot Backend
```bash
cd backend
mvn spring-boot:run
```
**Runs on**: http://localhost:8080

**Features**:
- Creates `/uploads` folder automatically
- Seeds demo user (username: `demo`, password: `demo123`) on first run
- Serves static files from `/uploads/**` endpoint

### 4. React Frontend
```bash
cd frontend
npm install
npm run dev
```
**Runs on**: http://localhost:5173

**Default User**:
- Username: `demo`
- Password: `demo123`

## API Endpoints

### Authentication
- `POST /api/auth/login` - Login with username/password

### Posts
- `GET /api/posts` - Get all posts (sorted by creation date)
- `POST /api/posts` - Create new post (multipart with image + caption)

### Static Files
- `GET /uploads/**` - Serve uploaded images

## Project Structure
```
├── frontend/           # React + Vite application
├── backend/            # Spring Boot application
├── python-validator/   # Flask image validation service
└── README.md          # This file
```

## Features
- ✅ User authentication (demo user seeded)
- ✅ Image upload with validation
- ✅ Post creation and feed display
- ✅ Responsive Instagram-style UI
- ✅ Image validation via Python microservice
- ✅ Static file serving
- ✅ MongoDB persistence

## Troubleshooting

### Common Issues
1. **Port conflicts**: Ensure ports 5001, 8080, and 5173 are available
2. **MongoDB connection**: Verify MongoDB is running locally
3. **Image uploads**: Check that `/uploads` folder exists in backend directory
4. **CORS issues**: Backend is configured to allow frontend origin

### Logs
- **Python**: Check console for image validation messages
- **Spring Boot**: Check console for application logs
- **React**: Check browser console for frontend errors

## Development Notes
- Images are stored locally in `backend/uploads/` directory
- JWT tokens are used for authentication (stored in localStorage)
- All image uploads go through Python validation before storage
- Demo data is automatically seeded on first backend startup
