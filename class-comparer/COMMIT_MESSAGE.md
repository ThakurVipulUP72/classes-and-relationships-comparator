# Commit Message for Class Comparer Application

## Option 1: Detailed Commit Message

```
feat: Build comprehensive class comparison tool with interactive UI

Implement full-stack application for comparing manual and LLM-generated class
diagrams with advanced features including search, filtering, metrics calculation,
and Excel export capabilities.

### Backend Features:
- Spring Boot REST API with file upload endpoints (JSON/XLSX support)
- Comprehensive comparison service with precision, recall, and F1 score metrics
- Advanced similarity detection using Levenshtein distance algorithm
- Excel export service with Apache POI for generating detailed reports
- Support for both original and modified comparison exports
- CORS configuration for frontend integration

### Frontend Features:
- Modern, responsive UI with gradient design and smooth animations
- Dual file upload (Manual and LLM classes)
- Real-time comparison results display
- Interactive search functionality for filtering results
- User story-based filtering with substring matching
- Individual item deletion with visual feedback
- Merge functionality for combining manual and LLM items
- Global statistics dashboard (Precision, Recall, F1 Score)
- Checkbox-based selection for batch operations
- Download buttons for original and modified Excel reports

### Technical Stack:
- Backend: Spring Boot 3.5.0, Java 21, Apache POI 5.2.5
- Frontend: Vanilla HTML/CSS/JavaScript
- Build Tool: Maven
- API Architecture: RESTful with JSON data exchange

### Key Capabilities:
- Matches classes and relationships between manual and LLM outputs
- Identifies items present only in manual or only in LLM
- Calculates character-based similarity scores
- Supports user story filtering and advanced search
- Allows UI-based modifications with modified Excel export
- Provides comprehensive metrics and statistics

Resolves: Class comparison and analysis requirements
```

---

## Option 2: Concise Commit Message

```
feat: Implement Class Comparer application with comparison and export features

Full-stack Java/Spring Boot application for comparing manual vs LLM-generated
class diagrams. Features include:

- Spring Boot REST API with JSON/XLSX file upload
- Comparison engine with precision/recall/F1 metrics
- Similarity detection using Levenshtein distance
- Interactive web UI with search, filter, and edit capabilities
- Excel export with Apache POI (original and modified reports)
- Support for merging, deleting, and managing comparison results

Tech: Spring Boot 3.5.0, Java 21, Apache POI, Vanilla JS
```

---

## Option 3: Conventional Commit Format

```
feat(class-comparer)!: add comprehensive class comparison and analysis tool

BREAKING CHANGE: Initial implementation

Features:
* Backend comparison service with advanced metrics
* Excel export with Apache POI
* Interactive web UI with real-time updates
* Search and filter functionality
* User story-based analysis
* Modified report export capability

Backend:
- Spring Boot 3.5.0 REST API
- JSON/XLSX file parsing
- Similarity algorithms (Levenshtein distance)
- Precision, Recall, F1 score calculation
- Stateful session management

Frontend:
- Modern responsive design
- File upload with validation
- Interactive comparison results
- Delete and merge operations
- Global statistics display
- Excel download functionality

Tech Stack: Spring Boot, Java 21, Apache POI 5.2.5, Maven
```

---

## How to Use:

Once Git is installed, use one of these commands:

### For Option 1 (Detailed):
```bash
git add .
git commit -F COMMIT_MESSAGE.md --edit
# (Edit to keep only the content between the first ``` markers)
```

### For Option 2 or 3 (Copy and paste):
```bash
git add .
git commit -m "feat: Implement Class Comparer application with comparison and export features

Full-stack Java/Spring Boot application for comparing manual vs LLM-generated
class diagrams. Features include:

- Spring Boot REST API with JSON/XLSX file upload
- Comparison engine with precision/recall/F1 metrics
- Similarity detection using Levenshtein distance
- Interactive web UI with search, filter, and edit capabilities
- Excel export with Apache POI (original and modified reports)
- Support for merging, deleting, and managing comparison results

Tech: Spring Boot 3.5.0, Java 21, Apache POI, Vanilla JS"
```

### Or use the simple one-liner:
```bash
git add .
git commit -m "feat: Build comprehensive class comparison tool with interactive UI and metrics"
```

---

## 📝 Git Signoff Commands:

The `--signoff` (or `-s`) flag adds a "Signed-off-by" line with your name and email, certifying that you have the right to submit the code.

### First, configure your Git identity:
```bash
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

### Commit with Signoff - Option 1 (Detailed):
```bash
git add .
git commit --signoff -m "feat: Build comprehensive class comparison tool with interactive UI

Implement full-stack application for comparing manual and LLM-generated class
diagrams with advanced features including search, filtering, metrics calculation,
and Excel export capabilities.

Backend Features:
- Spring Boot REST API with file upload endpoints (JSON/XLSX support)
- Comprehensive comparison service with precision, recall, and F1 score metrics
- Advanced similarity detection using Levenshtein distance algorithm
- Excel export service with Apache POI for generating detailed reports

Frontend Features:
- Modern, responsive UI with gradient design and smooth animations
- Dual file upload (Manual and LLM classes)
- Real-time comparison results display
- Interactive search functionality for filtering results
- User story-based filtering with substring matching
- Individual item deletion with visual feedback
- Merge functionality for combining manual and LLM items
- Global statistics dashboard (Precision, Recall, F1 Score)
- Download buttons for original and modified Excel reports

Tech Stack: Spring Boot 3.5.0, Java 21, Apache POI 5.2.5, Maven"
```

### Commit with Signoff - Option 2 (Concise) ⭐ RECOMMENDED:
```bash
git add .
git commit -s -m "feat: Implement Class Comparer application with comparison and export features

Full-stack Java/Spring Boot application for comparing manual vs LLM-generated
class diagrams. Features include:

- Spring Boot REST API with JSON/XLSX file upload
- Comparison engine with precision/recall/F1 metrics
- Similarity detection using Levenshtein distance
- Interactive web UI with search, filter, and edit capabilities
- Excel export with Apache POI (original and modified reports)
- Support for merging, deleting, and managing comparison results

Tech: Spring Boot 3.5.0, Java 21, Apache POI, Vanilla JS"
```

### Commit with Signoff - Option 3 (Short):
```bash
git add .
git commit -s -m "feat: Build comprehensive class comparison tool with interactive UI and metrics"
```

### Quick Command (Signoff with editor):
```bash
git add .
git commit --signoff
# This opens your default editor to write the commit message
```

**Note:** The signoff will add this line to your commit:
```
Signed-off-by: Your Name <your.email@example.com>
```

---

## After Committing:

### If you already have a GitHub repository:
```bash
git remote add origin https://github.com/YOUR_USERNAME/class-comparer.git
git branch -M main
git push -u origin main
```

### If you need to create a repository first:
1. Go to https://github.com/new
2. Create a new repository named "class-comparer"
3. Then run:
```bash
git remote add origin https://github.com/YOUR_USERNAME/class-comparer.git
git branch -M main
git push -u origin main
```
