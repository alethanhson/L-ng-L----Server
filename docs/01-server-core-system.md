# Báo Cáo Chi Tiết - Hệ Thống Server Core

## Tổng Quan
Hệ thống Server Core là trung tâm điều khiển toàn bộ game server, quản lý kết nối, khởi tạo các module và điều phối hoạt động của server.

## Kiến Trúc Tổng Thể

### 1. Main Class (`com.langla.server.main.Main`)
**Vai trò:** Entry point chính của server, khởi tạo và quản lý toàn bộ hệ thống

#### 1.1 Các Biến Quan Trọng
```java
public static MyServerSocket serverMain;           // Server chính
public static MyServerSocket serverCheckOnline;    // Server kiểm tra online
public static Vector vecClient = new Vector();     // Danh sách client
public static int NUM_CLIENTS = 0;                 // Số lượng client
public static boolean logData = false;             // Bật/tắt log
```

#### 1.2 Cơ Chế Chống DDoS
```java
private static final java.util.Map<String, Integer> ipConnectionCounts = new HashMap<>();
private static final java.util.Map<String, Long> ipLastResetTime = new HashMap<>();
```
- **Rate Limiting:** Giới hạn kết nối theo IP
- **Connection Reset:** Tự động reset số lượng kết nối theo thời gian
- **Configurable:** Có thể bật/tắt qua `PKoolVNDB.isFw`

#### 1.3 Khởi Tạo Server
```java
public static void main(String[] args) {
    printBanner();                    // Hiển thị banner
    Map.createMap();                  // Tạo bản đồ
    openServerSocket();               // Mở socket server
    activeCommandLine();              // Kích hoạt command line
    returnCho();                      // Khởi động hệ thống chợ
    BangXepHang();                   // Khởi động bảng xếp hạng
    BauCua.gI().Start();             // Khởi động game bầu cua
    createAndShowGUI();              // Tạo giao diện quản lý
    AutoBaoTri();                    // Bảo trì tự động
}
```

### 2. Server Socket Management

#### 2.1 Server Chính (Port 2907)
```java
serverMain = new MyServerSocket(PKoolVNDB.PORT_SERVER, new ServerSocketHandler() {
    @Override
    public void socketConnet(Socket socket) {
        // Xử lý kết nối client mới
        Client client = new Client(socket);
        client.id = NUM_CLIENTS++;
        if (client.isConnected()) {
            Main.addClient(client);
            client.session.start();
            PlayerManager.getInstance().put(client.session);
        }
    }
});
```

#### 2.2 Server Kiểm Tra Online (Port 2908)
```java
serverCheckOnline = new MyServerSocket(PKoolVNDB.PORT_CHECK_ONLINE, new ServerSocketHandler() {
    @Override
    public void socketConnet(Socket socket) {
        // Chỉ gửi byte 0 để kiểm tra online
        socket.getOutputStream().write(0);
        socket.getOutputStream().flush();
    }
});
```

### 3. Hệ Thống Bảo Mật

#### 3.1 IP Rate Limiting
```java
private static boolean isRateLimitExceeded(String ip) {
    checkAndResetRateLimit(ip);
    int connections = ipConnectionCounts.getOrDefault(ip, 0);
    if (connections >= PKoolVNDB.MAX_CONNECTIONS_PER_IP) {
        return true;
    }
    ipConnectionCounts.put(ip, connections + 1);
    return false;
}
```

**Cấu hình:**
- `MAX_CONNECTIONS_PER_IP = 50`: Tối đa 50 kết nối/IP
- `RATE_LIMIT_RESET_TIME = 50000ms`: Reset sau 50 giây

#### 3.2 Connection Validation
- Kiểm tra IP trước khi chấp nhận kết nối
- Tự động đóng socket nếu vượt quá giới hạn
- Log chi tiết các kết nối bị từ chối

### 4. Quản Lý Client

#### 4.1 Client Pool Management
```java
public static synchronized void addClient(Client aThis) {
    if (vecClient.contains(aThis)) {
        return;
    }
    vecClient.add(aThis);
}

public static synchronized void removeClient(Client aThis) {
    try {
        vecClient.remove(aThis);
    } catch (Exception ex) {
        Utlis.logError(Player.class, ex, "Da say ra loi:\n" + ex.getMessage());
    }
}
```

#### 4.2 Client Lifecycle
1. **Accept Connection:** Chấp nhận kết nối từ client
2. **Create Client:** Tạo đối tượng Client mới
3. **Start Session:** Khởi động session xử lý
4. **Add to Manager:** Thêm vào PlayerManager
5. **Cleanup:** Dọn dẹp khi client disconnect

### 5. Hệ Thống Bảo Trì

#### 5.1 Bảo Trì Tự Động
```java
private static void AutoBaoTri() {
    new Thread(() -> {
        while (true) {
            if(UTPKoolVN.getHour() == 4 && UTPKoolVN.getMinute() == 0 && !Maintenance.isRuning){
                // Tự động bảo trì lúc 4:00 sáng
                Maintenance.gI().start(60 * 5); // 5 phút
            }
            Thread.sleep(15000); // Kiểm tra mỗi 15 giây
        }
    }, "Bảo trì tự động").start();
}
```

#### 5.2 Bảo Trì Thủ Công
- Giao diện quản lý với các tùy chọn thời gian
- Có thể đặt bảo trì từ 1-30 phút
- Tự động tạo file `restart.flag` khi cần restart

### 6. Giao Diện Quản Lý

#### 6.1 Server Panel
```java
private static void createAndShowGUI() {
    JFrame frame = new JFrame("PKoolVN Server Panel");
    frame.setSize(400, 350);
    // Các component quản lý
}
```

#### 6.2 Các Chức Năng Quản Lý
- **Đặt Bảo Trì:** Chọn thời gian bảo trì
- **Stop Server:** Dừng server an toàn
- **Clear Player:** Xóa danh sách người chơi
- **Bật/Tắt Chống DDoS:** Điều khiển firewall

### 7. Hệ Thống Log & Monitoring

#### 7.1 Log Kết Nối
```java
UTPKoolVN.Print("CLIENT: " + client.id + 
                " IP: " + ip + 
                " >> Start - " + duration + "ms.");
```

#### 7.2 Log Lỗi
```java
Utlis.logError(Player.class, ex, "Da say ra loi:\n" + ex.getMessage());
```

#### 7.3 Performance Monitoring
- Đo thời gian xử lý kết nối
- Theo dõi số lượng client
- Giám sát tài nguyên server

### 8. Xử Lý Đa Luồng

#### 8.1 Thread Management
- **Main Thread:** Xử lý chính
- **Server Threads:** Xử lý kết nối
- **Background Threads:** Các tác vụ nền
- **GUI Thread:** Giao diện người dùng

#### 8.2 Synchronization
```java
public static synchronized void addClient(Client aThis)
public static synchronized void removeClient(Client aThis)
```

### 9. Cấu Hình & Tùy Chỉnh

#### 9.1 Properties Configuration
```properties
pkoolvn.port-server=2907
pkoolvn.port-check-online=2908
pkoolvn.MAX_CONNECTIONS_PER_IP=50
pkoolvn.RATE_LIMIT_RESET_TIME=50000
pkoolvn.FW=false
```

#### 9.2 Environment Variables
- `pkoolvn.debug`: Chế độ debug
- `pkoolvn.host`: IP server
- `pkoolvn.mysql-*`: Cấu hình database

### 10. Xử Lý Lỗi & Recovery

#### 10.1 Exception Handling
```java
try {
    // Xử lý logic
} catch (Exception ex) {
    Utlis.logError(Player.class, ex, "Da say ra loi:\n" + ex.getMessage());
}
```

#### 10.2 Graceful Shutdown
```java
public static void Stop() {
    serverCheckOnline.stop();
    serverMain.stop();
}
```

#### 10.3 Auto Recovery
- Tự động khởi động lại các module
- Xử lý lỗi kết nối database
- Khôi phục trạng thái server

### 11. Hiệu Suất & Tối Ưu

#### 11.1 Memory Management
- Sử dụng Vector cho danh sách client
- HashMap cho IP tracking
- ArrayList cho các collection

#### 11.2 Connection Pooling
- Giới hạn số kết nối đồng thời
- Tự động dọn dẹp kết nối không sử dụng
- Rate limiting để tránh quá tải

#### 11.3 Thread Optimization
- Sử dụng thread pool cho các tác vụ
- Background processing cho các hoạt động không cần real-time
- Synchronization chỉ khi cần thiết

### 12. Monitoring & Debugging

#### 12.1 Real-time Monitoring
- Số lượng client online
- Tình trạng các module
- Performance metrics

#### 12.2 Debug Tools
- Command line interface
- GUI management panel
- Log system chi tiết

#### 12.3 Health Checks
- Kiểm tra kết nối database
- Kiểm tra tài nguyên server
- Kiểm tra trạng thái các module

### 13. Kết Luận

Hệ thống Server Core được thiết kế với:
- **Kiến trúc modular** dễ mở rộng
- **Bảo mật cao** với nhiều lớp bảo vệ
- **Hiệu suất tối ưu** cho số lượng người chơi lớn
- **Quản lý dễ dàng** với giao diện trực quan
- **Tự động hóa** các tác vụ bảo trì và khôi phục

Server có khả năng xử lý hàng nghìn kết nối đồng thời với độ ổn định cao và khả năng phục hồi tốt.
