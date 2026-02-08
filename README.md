# Classes and Relationships Comparator

[![GitHub Repository](https://img.shields.io/badge/GitHub-Repository-blue?logo=github)](https://github.com/ThakurVipulUP72/classes-and-relationships-comparator)
[![Java](https://img.shields.io/badge/Java-21-orange?logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.0-brightgreen?logo=spring)](https://spring.io/projects/spring-boot)

A comprehensive tool for comparing manually created class diagrams with LLM-generated class diagrams from user stories.

## 🔗 Quick Links

- **Repository**: [https://github.com/ThakurVipulUP72/classes-and-relationships-comparator](https://github.com/ThakurVipulUP72/classes-and-relationships-comparator)
- **Frontend** (when running): [http://localhost:3000](http://localhost:3000)
- **Backend API** (when running): [http://localhost:8080](http://localhost:8080)
- **H2 Console** (when running): [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

## Features

### Core Comparison
- **Multi-format Support**: JSON, CSV, Excel (XLSX), and PlantUML
- **Class Comparison**: Compare classes between manual and LLM-generated diagrams
- **Relationship Comparison**: Compare relationships (associations, inheritance, etc.)
- **Detailed Metrics**: Precision, Recall, and F1 Score for each story and overall

### Visualization & Reporting
- **Interactive UML Diagrams**: Generate color-coded class diagrams with vis.js
- **PDF Reports**: Comprehensive comparison reports with metrics and tables
- **PlantUML Export**: Export diagrams to PlantUML format
- **Comparison Timeline**: Track metrics over time
- **Heatmap Visualization**: View similarity patterns across user stories

### Advanced AI Integration
- **Semantic Similarity Matching**: Uses Levenshtein, Jaccard, and Cosine similarity
- **Auto-Suggestion Engine**: Suggests potential matches for unmatched items
- **Confidence Scores**: Calculates confidence for each match
- **LLM Provider Support**: Ready for OpenAI, Anthropic Claude, Google Gemini integration

### Data Management
- **Database Persistence**: H2 (development) and PostgreSQL (production) support
- **Comparison History**: Store and retrieve historical comparisons
- **Baseline Comparison**: Set baselines and track improvements
- **API Key Management**: Secure storage with BCrypt encryption

### Quality Metrics Dashboard
- **Trend Analysis**: View metric trends over time (daily/weekly/monthly)
- **Per-Story Metrics**: Detailed metrics for each user story
- **Anomaly Detection**: Flag unusual comparison results
- **Baseline Comparison**: Compare current results against baseline

## Technology Stack

### Backend
- Java 21
- Spring Boot 3.5.0
- Spring Data JPA
- H2 Database (development)
- PostgreSQL (production)
- Apache POI (Excel)
- Apache Commons CSV
- iText7 (PDF generation)
- PlantUML
- Apache OpenNLP (NLP operations)
- BCrypt (encryption)

### Frontend
- HTML5, CSS3, JavaScript
- Chart.js (metrics visualization)
- vis.js (network diagrams)

## Getting Started

> **📖 [READ THIS FIRST: How to Run the Application Locally](HOW_TO_RUN.md)**
> 
> The application runs on YOUR local computer. Please follow the detailed guide in [HOW_TO_RUN.md](HOW_TO_RUN.md) for step-by-step instructions.

### Prerequisites
- Java 21 or higher
- Maven 3.9+ (or use included Maven wrapper)
- Python 3 or Node.js (for frontend server)

### Quick Start (Local Development)

**Windows:**
1. Double-click `start-backend.bat` (wait for "Started ClassCompareApp")
2. Double-click `start-frontend.bat` (in a new terminal)
3. Open http://localhost:3000 in your browser

**Linux/Mac:**
1. Run `./start-backend.sh` (wait for "Started ClassCompareApp")
2. Run `./start-frontend.sh` (in a new terminal)
3. Open http://localhost:3000 in your browser

**Manual Start:**
```bash
# Terminal 1 - Backend
cd class-comparer/backend
mvn spring-boot:run

# Terminal 2 - Frontend
cd class-comparer/frontend
python3 -m http.server 3000
```

> ⚠️ **Important**: Both backend AND frontend must be running. See [HOW_TO_RUN.md](HOW_TO_RUN.md) for detailed troubleshooting.

### Access Links

Once the application is running, access it at:

| Service | URL | Description |
|---------|-----|-------------|
| **Frontend** | http://localhost:3000 | Main UI for file upload and comparison |
| **Backend API** | http://localhost:8080 | REST API base URL |
| **H2 Console** | http://localhost:8080/h2-console | Database management interface |

**H2 Database Connection Details:**
- JDBC URL: `jdbc:h2:mem:classcomparer`
- Username: `sa`
- Password: (leave empty)

**Key API Endpoints:**
- `POST /api/upload` - Upload and compare files
- `GET /api/metrics/history` - Get comparison history
- `GET /api/keys` - List API keys
- `POST /api/visualize/pdf` - Generate PDF report
- `POST /api/baseline` - Set comparison baseline

### Configuration

Edit `class-comparer/backend/src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080

# Database (H2 for development)
spring.datasource.url=jdbc:h2:mem:classcomparer
spring.jpa.hibernate.ddl-auto=update

# File Upload
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB

# LLM Configuration (optional)
llm.enabled=true
llm.provider=openai
llm.api.key=${LLM_API_KEY}
llm.model=gpt-4
```

## API Endpoints

### File Upload & Comparison
- `POST /api/upload` - Upload JSON files for comparison
- `POST /api/import/csv` - Import CSV files
- `POST /api/import/plantuml` - Import PlantUML files

### Export & Visualization
- `GET /api/export/excel` - Export results to Excel
- `POST /api/export/modified` - Export modified results
- `POST /api/visualize/pdf` - Generate PDF report
- `POST /api/visualize/plantuml` - Export to PlantUML
- `POST /api/visualize/diagram` - Get diagram data for vis.js

### Metrics & History
- `GET /api/metrics/history` - Get comparison history
- `GET /api/metrics/trend` - Get metric trends
- `GET /api/metrics/heatmap` - Get heatmap data

### Suggestions & AI
- `POST /api/suggestions` - Get match suggestions
- `POST /api/suggestions/story/{index}` - Get story-specific suggestions

### Baseline Management
- `POST /api/baseline` - Set comparison as baseline
- `GET /api/baseline` - Get current baseline
- `GET /api/baseline/compare/{id}` - Compare against baseline
- `DELETE /api/baseline` - Delete baseline

### API Key Management
- `POST /api/keys` - Add API key
- `GET /api/keys` - List API keys (masked)
- `DELETE /api/keys/{id}` - Delete API key

## Usage Examples

### JSON Format
```json
{
  "Dataset_Name": {
    "stories": [
      {
        "story_id": "1",
        "classes": ["User", "Account", "Order"],
        "relationships": [
          {"source": "User", "relation": "has", "target": "Account"},
          {"source": "User", "relation": "places", "target": "Order"}
        ]
      }
    ]
  }
}
```

### CSV Format (Classes)
```csv
Story,Class
Story_1,User
Story_1,Account
Story_1,Order
```

### CSV Format (Relationships)
```csv
Story,Source,Relation,Target
Story_1,User,has,Account
Story_1,User,places,Order
```

### PlantUML Format
```plantuml
@startuml
class User
class Account
class Order
User --> Account : has
User --> Order : places
@enduml
```

## Development

### Project Structure
```
class-comparer/
├── backend/
│   ├── src/main/java/com/example/classcompare/
│   │   ├── controller/      # REST controllers
│   │   ├── service/         # Business logic
│   │   ├── entity/          # JPA entities
│   │   ├── repository/      # Data repositories
│   │   ├── model/           # DTOs and models
│   │   └── util/            # Utilities
│   └── pom.xml
└── frontend/
    └── index.html
```

### Building for Production

1. Update `application.properties` for PostgreSQL:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/classcomparer
spring.datasource.username=your_username
spring.datasource.password=your_password
```

2. Build JAR:
```bash
mvn clean package
java -jar target/classcompare-1.0.0.jar
```

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is open source. See LICENSE for details.

## Acknowledgments

- Spring Boot team for the excellent framework
- Apache POI for Excel support
- iText for PDF generation
- PlantUML for diagram support