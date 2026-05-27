# ShadowFox Student Management System

Student Management System built with Java and SQLite
as part of ShadowFox Java Internship - Part 3 Task 2
(All Tiers Completed)

## Features

### Baseline
- Add students with Name, Email, Course, Grade
- View all students in table
- Update student details
- Delete student with confirmation
- Search students by name
- Data persists using SQLite database
- Double-click row to edit

### Tier 1 - Grade Statistics
- Average grade (all students)
- Highest grade
- Lowest grade
- Stats panel always visible
- Filter students by course
- Course average grade report
- Statistics popup button

### Tier 2 - CSV Export/Import
- Export all students to CSV file
- Import students from CSV file
- JFileChooser for file dialogs
- Data backup and restore

## How to Run
1. Open in IntelliJ IDEA
2. Run `StudentGUI.java`
3. Database `students.db` created automatically

## How to Export
1. Add some students
2. Click 📤 Export CSV button
3. Choose save location
4. Click Save

## How to Import
1. Click 📥 Import CSV button
2. Select your CSV file
3. Students loaded automatically

## Technologies Used
- Java 25
- SQLite (sqlite-jdbc 3.43.0.0)
- Swing GUI
- JTable with DefaultTableModel
- PreparedStatement (SQL injection prevention)
- JDBC for database connection
- FileWriter for CSV export
- BufferedReader for CSV import
- JFileChooser for file dialogs
- Maven

## Database Schema
```sql
CREATE TABLE students (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    course TEXT NOT NULL,
    grade REAL NOT NULL
)
```

## Project Structure
src/
├── Student.java           → Data model
├── DatabaseManager.java   → SQLite + CSV operations
├── StudentGUI.java        → Swing GUI
└── Main.java              → Entry point
## Author
Sai Kumar - ShadowFox Java Internship 2026