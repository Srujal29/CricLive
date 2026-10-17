# Live Cricket Score Management System 🏏
**College Lab Assignment — Full Stack Web Development**

A responsive, real-time **Live Cricket Score Management System** built with **Java Spring Boot**, **MySQL**, **React (TypeScript + Vite)**, and REST APIs connected to **CricAPI / CricketData.org**.

---

## 1. System Architecture & Tech Stack

| Layer | Technology | Details |
| :--- | :--- | :--- |
| **Backend** | Java 21, Spring Boot 3.4.3 | Spring Web, Spring Data JPA, Hibernate, Bean Validation |
| **Frontend** | React 18 / 19, TypeScript, Vite | Axios, Lucide Icons, Modern Sports Dark Theme |
| **Database** | MySQL / MariaDB (XAMPP or Standalone) | `cricket_score_db` (JPA auto-schema generation) |
| **External API** | CricAPI (CricketData.org) | Configurable base URL & API key, rate-limit fallback |
| **Build Tools** | Maven Wrapper (`mvnw`), npm | Independent build & dev environments |

```
cricket-score-management/
├── backend/                  # Spring Boot application
│   ├── src/main/java/com/cricket/
│   │   ├── config/           # CORS, RestTemplate, Scheduling configuration
│   │   ├── controller/       # REST API endpoints (/api/matches/...)
│   │   ├── dto/              # Frontend DTOs & CricAPI response schemas
│   │   ├── entity/           # JPA Entities (MatchEntity, ScoreSnapshot, PlayerStatistic)
│   │   ├── exception/        # Global Exception Handler & custom exceptions
│   │   ├── repository/       # Spring Data JPA repositories
│   │   └── service/          # Business logic, CricAPI sync, and Baseline Seeder
│   ├── src/main/resources/
│   │   └── application.properties # MySQL & CricAPI configs
│   ├── .env.example          # Environment template
│   ├── pom.xml               # Maven dependencies
│   └── mvnw.cmd              # Windows Maven wrapper
├── frontend/                 # React + TypeScript + Vite application
│   ├── src/
│   │   ├── components/       # Navbar, MatchCard, MatchDetailsModal, ApiStatusModal, Skeletons
│   │   ├── services/         # cricketApi.ts (Axios REST service)
│   │   ├── types/            # TypeScript interfaces (Match, ScoreSnapshot, PlayerStatistic)
│   │   ├── App.tsx           # Dashboard tabs, live ticker, auto-refresh countdown
│   │   └── index.css         # Stadium dark theme design system
│   ├── package.json          # npm dependencies
│   └── vite.config.ts        # Vite dev server & backend proxy
└── README.md                 # Setup, API guide, and Viva examination Q&A
```

---

## 2. Database Setup (`cricket_score_db`)

The application uses **MySQL / MariaDB**. Spring Boot JPA will automatically create and update all tables (`matches`, `score_snapshots`, `player_statistics`) upon launch.

### Step 1: Start MySQL
If you are using **XAMPP**:
1. Open **XAMPP Control Panel**.
2. Click **Start** on the **MySQL** module.
*(Or run `C:\xampp\mysql_start.bat`)*

### Step 2: Create the Database
Open terminal/cmd or MySQL CLI and run:
```sql
CREATE DATABASE IF NOT EXISTS cricket_score_db;
```

---

## 3. External Cricket API Setup (CricAPI / CricketData.org)

The system integrates with **CricketData.org** (formerly CricAPI).

### How to get your free API key:
1. Go to **[https://cricketdata.org](https://cricketdata.org)** or **[https://cricapi.com](https://cricapi.com)**.
2. Sign up with your email to create a free account.
3. Access your dashboard and copy your **API Key** (free tier includes **500 requests/day**).
4. Configure the key using **Option A** or **Option B**:

#### Option A: `application.properties`
Open `backend/src/main/resources/application.properties` and paste your key:
```properties
cricket.api.base-url=https://api.cricapi.com/v1
cricket.api.key=YOUR_ACTUAL_CRICAPI_KEY_HERE
cricket.api.refresh-interval-seconds=45
```

#### Option B: Environment Variable
```cmd
set CRICKET_API_KEY=YOUR_ACTUAL_CRICAPI_KEY_HERE
```

> **Offline / Lab Demo Resilience:** If an API key is not configured or if you exceed daily rate limits, the system automatically falls back to the database snapshot and pre-seeded baseline fixtures, guaranteeing zero crashes during presentation!

---

## 4. How to Run the Project (Windows Commands)

### Terminal 1: Run Spring Boot Backend
Navigate to the `backend` folder and start Spring Boot using the included Maven wrapper:
```cmd
cd cricket-score-management\backend
mvnw.cmd spring-boot:run
```
* Backend runs at: **`http://localhost:8080`**
* Test backend health: Open `http://localhost:8080/api/matches/live` in your browser.

### Terminal 2: Run React Frontend
Navigate to the `frontend` folder and launch the Vite development server:
```cmd
cd cricket-score-management\frontend
npm install
npm run dev
```
* Frontend runs at: **`http://localhost:5173`**

---

## 5. REST Endpoints Reference (Postman / Thunder Client)

You can test these endpoints using **Thunder Client** (VS Code extension) or **Postman**:

| HTTP Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/matches/live` | Returns ongoing live matches with scores & overs |
| `GET` | `/api/matches/upcoming` | Returns scheduled fixtures & start dates |
| `GET` | `/api/matches/completed` | Returns finished fixtures and final results |
| `GET` | `/api/matches/{id}` | Fetches match by internal ID or CricAPI UUID |
| `GET` | `/api/matches/{id}/players` | Retrieves batting & bowling stats for the match |
| `GET` | `/api/matches/{id}/summary` | Full summary (innings breakdown + top performers) |
| `GET` | `/api/matches/status` | CricAPI connection status and MySQL record counts |
| `POST` | `/api/matches/refresh` | Syncs live scores from CricAPI without duplicates |
| `POST` | `/api/matches/seed-sample-data` | Seeds or resets baseline demonstration matches |

### Sample Thunder Client / cURL Command:
```cmd
curl.exe http://localhost:8080/api/matches/live
curl.exe http://localhost:8080/api/matches/cric-live-101/summary
curl.exe -X POST http://localhost:8080/api/matches/refresh
```

---

## 6. Features & Highlights

1. **Live Matches Highlight:** Shows pulsating red live indicators, current innings scores (runs/wickets), overs bowled, and live requirement statement (e.g. *"India need 34 runs in 18 balls"*).
2. **Detailed Match Modal:** Click any match card to open an interactive modal showing:
   - Innings Scorecard with Run Rate calculations.
   - Batting statistics (Runs, Balls, 4s, 6s, Strike Rate, Dismissal).
   - Bowling figures (Overs, Maidens, Runs conceded, Wickets, Economy).
   - Match metadata (Venue, Start Time GMT, CricAPI External UUID).
3. **Automated & Manual Refresh:**
   - 45-second live countdown timer in the navbar.
   - Pause / Resume auto-refresh toggle.
   - One-click manual refresh button with spinning visual state.
4. **Data Deduplication:** Refresh operations update existing records by `externalId` without creating duplicate matches or duplicate score snapshots.
5. **CORS Configured:** Secure backend header configuration allows requests from Vite dev server.

---

## 7. Common Troubleshooting & Fixes

### Issue 1: `Unable to open JDBC Connection / Communications link failure`
- **Cause:** MySQL server is stopped.
- **Fix:** Open XAMPP and start MySQL, or verify port 3306 is listening.

### Issue 2: `Port 8080 is already in use`
- **Cause:** Another process or previous Spring Boot instance is running on port 8080.
- **Fix:** Terminate the process or change `server.port=8081` in `application.properties` and update `VITE_API_URL` in frontend.

### Issue 3: `CricAPI rate limit reached (500 hits/day)`
- **Cause:** CricAPI free tier quota exceeded.
- **Fix:** The system automatically falls back to cached records in MySQL. You can also re-seed baseline matches via `POST /api/matches/seed-sample-data`.

---

## 8. College Lab Viva Questions & Answers

**Q1: Explain the layered architecture used in the Spring Boot backend.**  
*Answer:* The project follows standard separation of concerns:
- **Entity layer:** Defines JPA models mapped to MySQL tables (`MatchEntity`, `ScoreSnapshot`, `PlayerStatistic`).
- **Repository layer:** Extends `JpaRepository` for CRUD and custom query operations.
- **Service layer:** Encapsulates business logic, calls external CricAPI, parses JSON into clean entities, and avoids duplicate records.
- **Controller layer:** Exposes RESTful JSON endpoints (`@RestController`, `@GetMapping`, `@PostMapping`).

**Q2: Why is the external CricAPI key stored in the backend instead of frontend?**  
*Answer:* Storing the API key on the backend protects secret credentials from being exposed in public client-side browser bundles, prevents unauthorized quota exhaustion, and allows the backend to cache responses in MySQL to minimize external API costs.

**Q3: How does the system avoid duplicate match records during refresh?**  
*Answer:* Each match is assigned a unique `externalId` (UUID provided by the cricket API). When a refresh occurs, `matchRepository.findByExternalId(apiId)` checks if the record exists. If present, existing attributes and innings snapshots are updated; otherwise, a new entity is inserted.

**Q4: How does the frontend communicate with Spring Boot?**  
*Answer:* The React frontend uses **Axios** to send asynchronous HTTP requests to Spring Boot REST endpoints. The backend has `@CrossOrigin` and `WebMvcConfigurer` configured to allow Cross-Origin Resource Sharing (CORS) from `http://localhost:5173`.

**Q5: What happens if the external cricket API is down or unavailable?**  
*Answer:* The `CricketApiService` and `MatchService` implement graceful error handling with `try-catch` blocks and fallback logic. When external calls fail or timeout, the system returns stored MySQL snapshots so users can continue viewing the latest recorded scores without frontend errors.
