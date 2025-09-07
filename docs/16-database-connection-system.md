# Báo Cáo Chi Tiết - Hệ Thống Database & Connection Pool

## Tổng Quan
Hệ thống database sử dụng MySQL với HikariCP connection pool để quản lý kết nối hiệu quả. Hệ thống được thiết kế để xử lý hàng nghìn kết nối đồng thời với khả năng phục hồi cao.

## 1. Cấu Hình Database

### 1.1 Thông Số Kết Nối
```java
public class PKoolVNDB {
    // Database configuration
    public static String DRIVER = "com.mysql.jdbc.Driver";
    public static String URL = "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&useSSL=false";
    public static String DB_HOST = "127.0.0.1";
    public static String DB_PORT = "3306";
    public static String DB_NAME = "langla";
    public static String DB_USER = "langla_user";
    public static String DB_PASSWORD = "88888888";
    public static int MIN_CONN = 1;
    public static int MAX_CONN = 10;
}
```

### 1.2 File Cấu Hình Properties
```properties
# PKoolVN_Config.properties
pkoolvn.driver=com.mysql.jdbc.Driver
pkoolvn.min=1
pkoolvn.max=10
pkoolvn.mysql-user=langla_user
pkoolvn.mysql-password=langla123
pkoolvn.mysql-database=langla
pkoolvn.mysql-port=3306
```

### 1.3 Docker Compose
```yaml
services:
  mysql:
    image: mariadb:10.6
    container_name: langla_mysql
    restart: unless-stopped
    environment:
      MYSQL_ROOT_PASSWORD: root123
      MYSQL_DATABASE: langla
      MYSQL_USER: langla_user
      MYSQL_PASSWORD: langla123
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./SQL/langla (13).sql:/docker-entrypoint-initdb.d/init.sql
```

## 2. HikariCP Connection Pool

### 2.1 Cấu Hình Pool
```java
public class PKoolVN {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;
    
    static {
        config.setDriverClassName(PKoolVNDB.DRIVER);
        config.setJdbcUrl(String.format(PKoolVNDB.URL, 
            PKoolVNDB.DB_HOST, PKoolVNDB.DB_PORT, PKoolVNDB.DB_NAME));
        config.setUsername(PKoolVNDB.DB_USER);
        config.setPassword(PKoolVNDB.DB_PASSWORD);
        config.setMinimumIdle(PKoolVNDB.MIN_CONN);
        config.setMaximumPoolSize(PKoolVNDB.MAX_CONN);
        config.setMaxLifetime(1800000);        // 30 minutes
        config.setIdleTimeout(1500000);        // 25 minutes
        
        // Performance optimizations
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "true");
        
        ds = new HikariDataSource(config);
    }
}
```

### 2.2 Quản Lý Kết Nối
```java
public static Connection getConnection() throws SQLException {
    return ds.getConnection();
}

public static void close() {
    ds.close();
}
```

## 3. Xử Lý Lỗi Database

### 3.1 Connection Timeout
```java
// Lỗi connection timeout
HikariPool-1 - Connection is not available, request timed out after 30000ms.
java.sql.SQLTransientConnectionException: HikariPool-1 - Connection is not available, request timed out after 30000ms.
```

### 3.2 Communications Link Failure
```java
Caused by: com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure
Caused by: java.net.UnknownHostException: vps.langlaplus.com
```

### 3.3 Data Truncation
```java
Data truncation: Data too long for column 'name' at row 1
com.mysql.jdbc.MysqlDataTruncation: Data truncation: Data too long for column 'name' at row 1
```

### 3.4 Parameter Index Out of Bounds
```java
Parameter index out of bounds. 20 is not between valid values of 1 and 18
java.sql.SQLException: Parameter index out of bounds. 20 is not between valid values of 1 and 18
```

### 3.5 JSON Deserialization Error
```java
Cannot construct instance of `com.langla.real.item.Item`, problem: `java.lang.NullPointerException`
com.fasterxml.jackson.databind.exc.ValueInstantiationException: Cannot construct instance of `com.langla.real.item.Item`
```

## 4. Hệ Thống Logging Database

### 4.1 Log Cấu Trúc
```java
public class Utlis {
    private static final String LOG_DIRECTORY = "data/pkoolvn/logs/error/";
    private static final String LOG_TRADE = "data/pkoolvn/logs/trade/";
    private static final String LOG_ADD_CHAR = "data/pkoolvn/logs/char/add/";
    private static final String LOG_ADD_RE = "data/pkoolvn/logs/char/remove/";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger logger = LoggerFactory.getLogger(Utlis.class);
}
```

### 4.2 Ghi Log Lỗi
```java
public static void logError(Class<?> clazz, Exception e, String message) {
    Logger logger = LoggerFactory.getLogger(clazz);
    
    String logFileName = LOG_DIRECTORY + "log-" + dateFormat.format(new Date()) + ".txt";
    try (FileWriter fileWriter = new FileWriter(logFileName, true);
         PrintWriter printWriter = new PrintWriter(fileWriter)) {
        printWriter.println("Timestamp: " + new Date());
        printWriter.println(message);
        
        logger.error(message, e); // Sử dụng logger.error
        
        e.printStackTrace(printWriter);
    } catch (IOException ioException) {
        logger.error("Loi khi ghi log", ioException);
    }
}
```

### 4.3 Log Giao Dịch
```java
public static void logTrade(String message) {
    String logFileName = LOG_TRADE + "log-" + dateFormat.format(new Date()) + ".txt";
    try (FileWriter fileWriter = new FileWriter(logFileName, true);
         PrintWriter printWriter = new PrintWriter(fileWriter)) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm a");
        String dateTime = LocalDateTime.now().format(formatter);
        printWriter.println(""+message+" TIME: " + dateTime + System.lineSeparator());
    } catch (IOException ioException) {
        logger.error("Loi khi ghi log", ioException);
    }
}
```

### 4.4 Log Nhân Vật
```java
public static void logAddChar(String message, int id) {
    String logFileName = LOG_ADD_CHAR + "" + id + ".txt";
    try (FileWriter fileWriter = new FileWriter(logFileName, true);
         PrintWriter printWriter = new PrintWriter(fileWriter)) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm a");
        String dateTime = LocalDateTime.now().format(formatter);
        printWriter.println(""+message+", TIME: " + dateTime + System.lineSeparator());
    } catch (IOException ioException) {
        logger.error("Loi khi ghi log", ioException);
    }
}
```

## 5. Tối Ưu Hóa Database

### 5.1 Connection Pool Tuning
- **Minimum Idle:** 1 connection
- **Maximum Pool Size:** 10 connections
- **Max Lifetime:** 30 minutes
- **Idle Timeout:** 25 minutes

### 5.2 Prepared Statement Caching
```java
config.addDataSourceProperty("cachePrepStmts", "true");
config.addDataSourceProperty("prepStmtCacheSize", "250");
config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
```

### 5.3 Batch Processing
```java
config.addDataSourceProperty("rewriteBatchedStatements", "true");
config.addDataSourceProperty("useLocalSessionState", "true");
```

### 5.4 Metadata Caching
```java
config.addDataSourceProperty("cacheResultSetMetadata", "true");
config.addDataSourceProperty("cacheServerConfiguration", "true");
```

## 6. Bảo Mật Database

### 6.1 User Permissions
```sql
-- Tạo user với quyền hạn chế
CREATE USER 'langla_user'@'%' IDENTIFIED BY 'langla123';
GRANT SELECT, INSERT, UPDATE, DELETE ON langla.* TO 'langla_user'@'%';
FLUSH PRIVILEGES;
```

### 6.2 Connection Security
```java
// Sử dụng SSL (tùy chọn)
config.setJdbcUrl("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&useSSL=false");
```

### 6.3 SQL Injection Prevention
```java
// Sử dụng PreparedStatement
PreparedStatement pstmt = connection.prepareStatement(
    "SELECT * FROM users WHERE username = ? AND password = ?"
);
pstmt.setString(1, username);
pstmt.setString(2, password);
```

## 7. Monitoring & Performance

### 7.1 Connection Pool Metrics
```java
// Theo dõi số lượng connection
HikariDataSource ds = (HikariDataSource) PKoolVN.getDataSource();
int activeConnections = ds.getHikariPoolMXBean().getActiveConnections();
int idleConnections = ds.getHikariPoolMXBean().getIdleConnections();
int totalConnections = ds.getHikariPoolMXBean().getTotalConnections();
```

### 7.2 Performance Monitoring
```java
// Theo dõi thời gian xử lý query
long startTime = System.currentTimeMillis();
// Thực hiện query
long endTime = System.currentTimeMillis();
long duration = endTime - startTime;

if (duration > 1000) { // Log query chậm
    Utlis.logError(DatabaseMonitor.class, null, 
        "Slow query detected: " + duration + "ms");
}
```

### 7.3 Health Check
```java
public static boolean isDatabaseHealthy() {
    try (Connection conn = getConnection()) {
        return conn.isValid(5); // Kiểm tra trong 5 giây
    } catch (SQLException e) {
        Utlis.logError(PKoolVN.class, e, "Database health check failed");
        return false;
    }
}
```

## 8. Backup & Recovery

### 8.1 Database Backup
```bash
# Backup toàn bộ database
mysqldump -u langla_user -p langla > backup_$(date +%Y%m%d_%H%M%S).sql

# Backup với compression
mysqldump -u langla_user -p langla | gzip > backup_$(date +%Y%m%d_%H%M%S).sql.gz
```

### 8.2 Recovery Procedures
```java
public static void recoverFromBackup(String backupFile) {
    try {
        // Dừng server
        Maintenance.isRunning = false;
        
        // Restore database
        String restoreCommand = String.format(
            "mysql -u %s -p%s %s < %s",
            PKoolVNDB.DB_USER, PKoolVNDB.DB_PASSWORD, 
            PKoolVNDB.DB_NAME, backupFile
        );
        
        // Thực hiện restore
        Runtime.getRuntime().exec(restoreCommand);
        
        // Khởi động lại server
        Maintenance.isRunning = true;
        
    } catch (Exception e) {
        Utlis.logError(PKoolVN.class, e, "Recovery failed");
    }
}
```

## 9. Gợi Ý Mở Rộng

### 9.1 Connection Pool Optimization
- Tăng `MAX_CONN` dựa trên tải
- Điều chỉnh timeout values
- Implement connection leak detection

### 9.2 Database Clustering
- Master-Slave replication
- Read replicas cho query
- Failover mechanisms

### 9.3 Advanced Monitoring
- Query performance analysis
- Slow query logging
- Resource usage tracking

### 9.4 Security Enhancements
- Connection encryption
- Audit logging
- Access control lists
