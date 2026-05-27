# Learnings - Student Management with SQLite

## Baseline Learnings

### Hardest Bug
SQLite database was not found because the
connection URL was wrong.

### How I Fixed It
Used `jdbc:sqlite:students.db` which creates
the database file in the project root folder
automatically if it doesn't exist.

### What I Learned
- SQLite database setup with JDBC
- DriverManager.getConnection() for DB connection
- CREATE TABLE IF NOT EXISTS for safe table creation
- PreparedStatement to prevent SQL injection
- ResultSet to read query results
- AUTOINCREMENT for auto ID generation
- Data persistence across app restarts
- CRUD operations with SQL
- INSERT, SELECT, UPDATE, DELETE statements
- LIKE operator for search queries

## Tier 1 - Grade Statistics Learnings

### What I Learned
- AVG() SQL function for average grade
- MAX() SQL function for highest grade
- MIN() SQL function for lowest grade
- GROUP BY concept for course filtering
- JLabel for displaying live statistics
- Updating stats after every operation
- Course filter with LIKE operator
- getCourseAverage() for per course stats

## Tier 2 - CSV Export/Import Learnings

### What I Learned
- FileWriter to write CSV files
- BufferedReader to read CSV files
- String.split(",") to parse CSV rows
- Skipping header with reader.readLine()
- JFileChooser for file save/open dialog
- Data backup and restore concept
- Importing only new records (duplicate skip)
- parts[1] to parts[4] for correct column mapping