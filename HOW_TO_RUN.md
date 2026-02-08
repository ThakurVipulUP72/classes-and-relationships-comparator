# 🚀 How to Run the Application Locally

This guide will help you run the Class Comparer application on your local computer.

## ⚠️ Important Note

The application runs on **your local computer**, not in the cloud. When you see references to `localhost:3000` or `localhost:8080`, these are URLs that only work when the application is running on YOUR machine.

## 📋 Prerequisites

Before you start, make sure you have these installed:

1. **Java 21** - Download from [Adoptium](https://adoptium.net/) or [Oracle](https://www.oracle.com/java/technologies/downloads/)
2. **Maven** - Download from [Apache Maven](https://maven.apache.org/download.cgi) or use the included `mvnw` wrapper
3. **Python 3** (for frontend) - Download from [Python.org](https://www.python.org/downloads/)
   - Alternative: Node.js with `http-server` package

### Verify Installation

Open a terminal/command prompt and check:

```bash
# Check Java version (should be 21 or higher)
java -version

# Check Maven (optional if using mvnw)
mvn -version

# Check Python
python --version
# or
python3 --version
```

## 🖥️ Starting the Application

### Option 1: Windows Users (Quick Start)

1. **Start Backend** (in one terminal):
   - Double-click `start-backend.bat`
   - OR open Command Prompt and run:
     ```cmd
     start-backend.bat
     ```
   - Wait for the message: "Started ClassCompareApp"

2. **Start Frontend** (in another terminal):
   - Double-click `start-frontend.bat`
   - OR open another Command Prompt and run:
     ```cmd
     start-frontend.bat
     ```

3. **Open your browser** and go to: http://localhost:3000

### Option 2: Linux/Mac Users (Manual Steps)

#### Terminal 1 - Backend:
```bash
cd class-comparer/backend

# Set Java 21 (if needed)
export JAVA_HOME=/path/to/jdk-21
export PATH=$JAVA_HOME/bin:$PATH

# Build and run
mvn clean spring-boot:run

# Or if you don't have Maven installed:
./mvnw clean spring-boot:run
```

Wait for the message: `Started ClassCompareApp in X seconds`

#### Terminal 2 - Frontend:
```bash
cd class-comparer/frontend

# Using Python 3
python3 -m http.server 3000

# OR using Python 2
python -m SimpleHTTPServer 3000

# OR using Node.js http-server
npx http-server -p 3000
```

#### Open Browser:
Go to: http://localhost:3000

### Option 3: One-Line Commands

**Backend:**
```bash
cd class-comparer/backend && mvn spring-boot:run
```

**Frontend:**
```bash
cd class-comparer/frontend && python3 -m http.server 3000
```

## 🌐 Access URLs

Once both servers are running, you can access:

| Service | URL | What it does |
|---------|-----|--------------|
| **Frontend UI** | http://localhost:3000 | Main application interface |
| **Backend API** | http://localhost:8080 | REST API endpoints |
| **H2 Database Console** | http://localhost:8080/h2-console | Database viewer |

### H2 Database Connection:
- **JDBC URL**: `jdbc:h2:mem:classcomparer`
- **Username**: `sa`
- **Password**: (leave empty)

## 🔍 Troubleshooting

### Problem: "Port already in use"

**Solution 1** - Find and stop the process:
```bash
# Windows
netstat -ano | findstr :8080
netstat -ano | findstr :3000
taskkill /PID <process_id> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
lsof -ti:3000 | xargs kill -9
```

**Solution 2** - Use different ports:
```bash
# Backend (use port 8081 instead)
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081

# Frontend (use port 3001 instead)
python3 -m http.server 3001
```

### Problem: "Java version error"

Make sure you're using Java 21:
```bash
java -version
```

If you have multiple Java versions:
```bash
# Windows
set JAVA_HOME=C:\Program Files\Java\jdk-21

# Linux/Mac
export JAVA_HOME=/path/to/jdk-21
export PATH=$JAVA_HOME/bin:$PATH
```

### Problem: "Maven not found"

Use the Maven wrapper instead:
```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

### Problem: "Python not found"

Install Python 3 from [python.org](https://www.python.org/downloads/)

Or use Node.js alternative:
```bash
npx http-server -p 3000
```

### Problem: "Backend starts but frontend shows connection errors"

1. Make sure backend is fully started (look for "Started ClassCompareApp" message)
2. Check backend is responding:
   ```bash
   curl http://localhost:8080/api/keys?userId=test
   ```
3. If you see `[]` (empty array), backend is working!

### Problem: "Application not showing in browser"

1. **Double-check you started both servers** (backend AND frontend)
2. **Make sure you're accessing the correct URL**: http://localhost:3000
3. **Check if servers are running**:
   ```bash
   # Windows
   netstat -ano | findstr :3000
   netstat -ano | findstr :8080
   
   # Linux/Mac
   lsof -i:3000
   lsof -i:8080
   ```
4. **Try accessing backend directly**: http://localhost:8080/api/keys?userId=test
5. **Clear browser cache** or try in incognito/private mode

## 📸 What You Should See

### Backend Terminal:
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 
Started ClassCompareApp in 3.562 seconds
Tomcat started on port 8080
```

### Frontend Terminal:
```
Serving HTTP on 0.0.0.0 port 3000 (http://0.0.0.0:3000/) ...
```

### Browser (http://localhost:3000):
You should see the **Class Comparer Enhanced** interface with:
- Beautiful purple/blue gradient background
- File upload sections for Manual and LLM-generated classes
- "Compare Files" button
- Modern, responsive UI

## 🛑 Stopping the Application

Press `Ctrl+C` in each terminal window to stop the servers.

## 📚 Next Steps

Once the application is running:
1. Upload your JSON files (manual and LLM-generated)
2. Click "Compare Files"
3. View the comparison results
4. Explore the metrics and visualizations

For more details, see the main [README.md](README.md)

## 💡 Tips

- Keep both terminal windows open while using the application
- Backend must be started before frontend for proper API connectivity
- If you make code changes, restart the backend with `Ctrl+C` then `mvn spring-boot:run` again
- Frontend changes (HTML/CSS/JS) don't require restart - just refresh your browser

## 🆘 Still Having Issues?

If you're still having problems:
1. Make sure all prerequisites are installed
2. Check that ports 3000 and 8080 are not being used by other applications
3. Look at the error messages in the terminal - they often tell you what's wrong
4. Try restarting your computer and trying again

---

**Remember**: The application runs on YOUR local computer. The URLs `localhost:3000` and `localhost:8080` only work when the servers are running on your machine!
