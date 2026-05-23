# Learnings - Student Management with SQLite

## Hardest Bug
SQLite database was not found because the
connection URL was wrong.

## How I Fixed It
Used `jdbc:sqlite:students.db` which creates
the database file in the project root folder
automatically if it doesn't exist.

## What I Learned
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