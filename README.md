# Expense Tracker (Java Swing + MySQL)

A desktop Expense Tracker built with Java Swing and MySQL. It lets you manage categories and expenses with CRUD operations and a simple launcher UI.

## Features
- **Launcher UI**: `ExpenseTrackerGUI` opens entry points for Categories and Expenses.
- **Categories (CRUD)**: Create, list, update, delete categories via `CategoryGUI` backed by `CategoryDAO` and `category` table.
- **Expenses (CRUD)**: Add, list (sorted by date desc), update, delete expenses via `ExpenseGUI` backed by `ExpenseDAO` and `expense` table.
- **MySQL persistence**: JDBC via `DatabaseConnection` using MySQL Connector/J.


## Tech Stack
- **Language**: Java 11
- **UI**: Swing
- **DB**: MySQL 8.x
- **Build**: Maven

## Prerequisites
- Java 11+ (JDK)
- Maven 3.8+
- MySQL Server 8.x

## Project Structure
```
src/main/java/com/tracker/
  Main.java                      # App entry point (launches ExpenseTrackerGUI)
  gui/
    ExpenseTrackerGUI.java       # Launcher window with buttons
    CategoryGUI.java             # Category management UI
    ExpenseGUI.java              # Expense management UI
  dao/
    CategoryDAO.java             # CRUD for category
    ExpenseDAO.java              # CRUD for expense
  model/
    Category.java                # Category entity (id, type)
    Expense.java                 # Expense entity (id, categoryId, description, amount, date)
  util/
    DatabaseConnection.java      # JDBC connection (URL, USERNAME, PASSWORD)
```

## Database Setup
Update credentials in `src/main/java/com/tracker/util/DatabaseConnection.java` if needed:

```java
public class DatabaseConnection {
    public static final String URL = "jdbc:mysql://localhost:3306/expensetracker";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "<your-password>";
}
```

Create database and tables:

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS expensetracker;
USE expensetracker;

-- Category table
CREATE TABLE IF NOT EXISTS category (
  id INT AUTO_INCREMENT PRIMARY KEY,
  type VARCHAR(255) NOT NULL
);

-- Expense table
CREATE TABLE IF NOT EXISTS expense (
  id INT AUTO_INCREMENT PRIMARY KEY,
  category_id INT NOT NULL,
  description VARCHAR(1000) NOT NULL,
  amount INT NOT NULL,
  date DATETIME NOT NULL,
  CONSTRAINT fk_expense_category FOREIGN KEY (category_id)
    REFERENCES category(id) ON DELETE RESTRICT ON UPDATE CASCADE
);
```

## Build & Run
From the project root (`EXPENSE TRACKER/`):

### Using Maven Exec
```bash
mvn clean package
mvn exec:java -Dexec.mainClass=com.tracker.Main
```

### Using the shaded JAR
```bash
mvn clean package
java -jar target/expensetracker-application-1.0.0.jar
```

## Configuration Notes
- **MySQL connection** is defined in `DatabaseConnection.java`.
- Ensure MySQL is running and accessible at `localhost:3306` or adjust the `URL`.
- If you see timezone warnings, append `?serverTimezone=UTC` to the JDBC URL.

## Troubleshooting
- **JDBC Driver not found**: Maven should fetch `mysql-connector-j` automatically. Run `mvn -U clean package`.
- **Connection refused**: Verify MySQL is running, credentials are correct, and the DB `expensetracker` exists.
- **GUI not appearing**: Check logs and ensure `Main` is launching `ExpenseTrackerGUI` without exceptions.

## License
This project is provided as-is for learning purposes. Add a license if you plan to distribute.>
