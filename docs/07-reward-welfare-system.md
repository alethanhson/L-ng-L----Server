# Báo Cáo Chi Tiết - Hệ Thống Reward & Phúc Lợi

## Tổng Quan
Hệ thống Reward & Phúc Lợi là trung tâm quản lý các phần thưởng, phúc lợi hàng ngày, tuần, tháng và các hoạt động đặc biệt trong game. Hệ thống này khuyến khích người chơi tham gia thường xuyên và duy trì sự cân bằng trong game economy.

## Kiến Trúc Hệ Thống

### 1. Phúc Lợi System (`com.langla.real.phucloi.PhucLoi`)
**Vai trò:** Quản lý các phúc lợi và phần thưởng cho người chơi

#### 1.1 Cấu Trúc Dữ Liệu
```java
public class PhucLoi {
    // Thời gian online
    public int thoigianOnlineHomNay = 0;
    public byte soNgayOnlineLienTuc = 1;
    
    // Nạp tiền
    public byte soNgayNapLienTuc = 0;
    public int vangNapTichLuy = 0;
    public int vangNapHomNay = 0;
    public int vangNapTuan = 0;
    
    // Tiêu tiền
    public int vangTieuHomNay = 0;
    public int vangTieuTuan = 0;
    
    // Các mốc nạp tiền
    public int vangNapMoc = 0;
    public int vangNapDon = 0;
    
    // Vòng quay may mắn
    public int diemTichLuyVongQuay = 0;
    public int solanQuay = 0;
    
    // Thẻ thành viên
    public long timeTheThang = -1;
    public long timeTheVinhVien = -1;
    
    // Gói đặc biệt
    public boolean isGoiHaoHoa = false;
    public boolean isGoiChiTon = false;
    
    // Ngày cập nhật
    public LocalDate lastLoginDate;
    public LocalDate lastDailyUpdate;
    public LocalDate lastWeeklyUpdate;
    public LocalDate lastNapLienTucUpdate;
    
    // Log dữ liệu
    public final List<LogPhucLoi> logData = new ArrayList<>();
}
```

### 2. Hệ Thống Phúc Lợi Hàng Ngày

#### 2.1 Phúc Lợi Online
```java
public class DailyWelfare {
    // Phúc lợi theo thời gian online
    public void checkOnlineTime(Char player) {
        int onlineTime = player.getOnlineTime();
        
        if (onlineTime >= 60) {  // 1 giờ
            giveReward(player, "ONLINE_1H", 100);
        }
        if (onlineTime >= 180) { // 3 giờ
            giveReward(player, "ONLINE_3H", 300);
        }
        if (onlineTime >= 360) { // 6 giờ
            giveReward(player, "ONLINE_6H", 600);
        }
    }
    
    // Phúc lợi đăng nhập liên tục
    public void checkConsecutiveLogin(Char player) {
        if (player.phucLoi.soNgayOnlineLienTuc >= 7) {
            giveReward(player, "LOGIN_7DAYS", 1000);
        }
        if (player.phucLoi.soNgayOnlineLienTuc >= 30) {
            giveReward(player, "LOGIN_30DAYS", 5000);
        }
    }
}
```

#### 2.2 Phúc Lợi Nạp Tiền
```java
public class RechargeWelfare {
    // Phúc lợi nạp tiền hàng ngày
    public void checkDailyRecharge(Char player, int amount) {
        player.phucLoi.vangNapHomNay += amount;
        
        if (player.phucLoi.vangNapHomNay >= 1000) {
            giveReward(player, "RECHARGE_1K", 100);
        }
        if (player.phucLoi.vangNapHomNay >= 5000) {
            giveReward(player, "RECHARGE_5K", 500);
        }
        if (player.phucLoi.vangNapHomNay >= 10000) {
            giveReward(player, "RECHARGE_10K", 1000);
        }
    }
    
    // Phúc lợi nạp tiền tuần
    public void checkWeeklyRecharge(Char player, int amount) {
        player.phucLoi.vangNapTuan += amount;
        
        if (player.phucLoi.vangNapTuan >= 50000) {
            giveReward(player, "RECHARGE_WEEK_50K", 5000);
        }
        if (player.phucLoi.vangNapTuan >= 100000) {
            giveReward(player, "RECHARGE_WEEK_100K", 10000);
        }
    }
    
    // Phúc lợi nạp tiền tích lũy
    public void checkAccumulatedRecharge(Char player, int amount) {
        player.phucLoi.vangNapTichLuy += amount;
        
        // Các mốc tích lũy
        int[] milestones = {100000, 500000, 1000000, 5000000, 10000000};
        int[] rewards = {10000, 50000, 100000, 500000, 1000000};
        
        for (int i = 0; i < milestones.length; i++) {
            if (player.phucLoi.vangNapTichLuy >= milestones[i] && 
                player.phucLoi.vangNapMoc < milestones[i]) {
                giveReward(player, "RECHARGE_MILESTONE_" + milestones[i], rewards[i]);
                player.phucLoi.vangNapMoc = milestones[i];
            }
        }
    }
}
```

### 3. Hệ Thống Vòng Quay May Mắn

#### 3.1 Tích Điểm Vòng Quay
```java
public class LuckyWheel {
    // Tích điểm từ các hoạt động
    public void addWheelPoints(Char player, String activity, int points) {
        player.phucLoi.diemTichLuyVongQuay += points;
        
        // Thông báo tích điểm
        player.client.session.serivce.ShowMessWhite(
            "Bạn đã tích được " + points + " điểm vòng quay từ " + activity
        );
    }
    
    // Quay vòng may mắn
    public void spinWheel(Char player) {
        if (player.phucLoi.diemTichLuyVongQuay < 100) {
            player.client.session.serivce.ShowMessGold("Cần ít nhất 100 điểm để quay");
            return;
        }
        
        // Trừ điểm
        player.phucLoi.diemTichLuyVongQuay -= 100;
        player.phucLoi.solanQuay++;
        
        // Random phần thưởng
        int reward = randomReward();
        giveWheelReward(player, reward);
    }
    
    // Random phần thưởng
    private int randomReward() {
        int rand = Utlis.nextInt(100);
        
        if (rand < 1) {        // 1% - Phần thưởng đặc biệt
            return 10000;
        } else if (rand < 5) { // 4% - Phần thưởng cao
            return 5000;
        } else if (rand < 15) { // 10% - Phần thưởng trung bình
            return 1000;
        } else {                // 85% - Phần thưởng thường
            return 100;
        }
    }
}
```

### 4. Hệ Thống Thẻ Thành Viên

#### 4.1 Thẻ Tháng
```java
public class MonthlyCard {
    // Kích hoạt thẻ tháng
    public void activateMonthlyCard(Char player) {
        long currentTime = System.currentTimeMillis();
        long monthInMillis = 30L * 24 * 60 * 60 * 1000; // 30 ngày
        
        player.phucLoi.timeTheThang = currentTime + monthInMillis;
        
        // Phần thưởng kích hoạt
        giveReward(player, "MONTHLY_CARD_ACTIVATE", 1000);
        
        // Thông báo
        player.client.session.serivce.ShowMessWhite(
            "Thẻ tháng đã được kích hoạt! Bạn sẽ nhận được 100 vàng mỗi ngày trong 30 ngày."
        );
    }
    
    // Phần thưởng hàng ngày
    public void giveDailyReward(Char player) {
        if (player.phucLoi.timeTheThang > System.currentTimeMillis()) {
            giveReward(player, "MONTHLY_CARD_DAILY", 100);
            
            player.client.session.serivce.ShowMessWhite(
                "Phần thưởng thẻ tháng hàng ngày: 100 vàng"
            );
        }
    }
}
```

#### 4.2 Thẻ Vĩnh Viễn
```java
public class PermanentCard {
    // Kích hoạt thẻ vĩnh viễn
    public void activatePermanentCard(Char player) {
        player.phucLoi.timeTheVinhVien = System.currentTimeMillis();
        
        // Phần thưởng kích hoạt
        giveReward(player, "PERMANENT_CARD_ACTIVATE", 5000);
        
        // Thông báo
        player.client.session.serivce.ShowMessWhite(
            "Thẻ vĩnh viễn đã được kích hoạt! Bạn sẽ nhận được 200 vàng mỗi ngày vĩnh viễn."
        );
    }
    
    // Phần thưởng hàng ngày
    public void giveDailyReward(Char player) {
        if (player.phucLoi.timeTheVinhVien > 0) {
            giveReward(player, "PERMANENT_CARD_DAILY", 200);
            
            player.client.session.serivce.ShowMessWhite(
                "Phần thưởng thẻ vĩnh viễn hàng ngày: 200 vàng"
            );
        }
    }
}
```

### 5. Hệ Thống Gói Đặc Biệt

#### 5.1 Gói Hào Hoa
```java
public class HaoHoaPackage {
    // Kích hoạt gói hào hoa
    public void activateHaoHoa(Char player) {
        player.phucLoi.isGoiHaoHoa = true;
        
        // Phần thưởng gói hào hoa
        giveReward(player, "HAO_HOA_PACKAGE", 2000);
        
        // Hiệu ứng đặc biệt
        player.addEffect(new Effect("HAO_HOA", 24 * 60 * 60 * 1000)); // 24 giờ
        
        // Thông báo
        player.client.session.serivce.ShowMessWhite(
            "Gói Hào Hoa đã được kích hoạt! Bạn sẽ nhận được nhiều phần thưởng đặc biệt."
        );
    }
    
    // Phần thưởng gói hào hoa
    public void giveHaoHoaReward(Char player) {
        if (player.phucLoi.isGoiHaoHoa) {
            // Tăng tỷ lệ thưởng
            int bonusReward = calculateBonusReward(player);
            giveReward(player, "HAO_HOA_BONUS", bonusReward);
        }
    }
}
```

#### 5.2 Gói Chi Tốn
```java
public class ChiTonPackage {
    // Kích hoạt gói chi tốn
    public void activateChiTon(Char player) {
        player.phucLoi.isGoiChiTon = true;
        
        // Phần thưởng gói chi tốn
        giveReward(player, "CHI_TON_PACKAGE", 3000);
        
        // Hiệu ứng đặc biệt
        player.addEffect(new Effect("CHI_TON", 24 * 60 * 60 * 1000)); // 24 giờ
        
        // Thông báo
        player.client.session.serivce.ShowMessWhite(
            "Gói Chi Tốn đã được kích hoạt! Bạn sẽ nhận được nhiều phần thưởng đặc biệt."
        );
    }
    
    // Phần thưởng gói chi tốn
    public void giveChiTonReward(Char player) {
        if (player.phucLoi.isGoiChiTon) {
            // Tăng tỷ lệ thưởng
            int bonusReward = calculateBonusReward(player);
            giveReward(player, "CHI_TON_BONUS", bonusReward);
        }
    }
}
```

### 6. Hệ Thống Phần Thưởng

#### 6.1 Cấu Trúc Phần Thưởng
```java
public class Reward {
    public String type;           // Loại phần thưởng
    public int amount;            // Số lượng
    public Item item;             // Vật phẩm
    public long expiry;           // Thời gian hết hạn
    public boolean isClaimed;     // Đã nhận chưa
    
    public Reward(String type, int amount) {
        this.type = type;
        this.amount = amount;
        this.isClaimed = false;
    }
}
```

#### 6.2 Hệ Thống Trao Thưởng
```java
public class RewardSystem {
    // Trao phần thưởng
    public void giveReward(Char player, String rewardType, int amount) {
        Reward reward = new Reward(rewardType, amount);
        
        // Thêm vào danh sách phần thưởng
        player.addReward(reward);
        
        // Thông báo
        player.client.session.serivce.ShowMessWhite(
            "Bạn đã nhận được phần thưởng: " + rewardType + " - " + amount
        );
        
        // Log phần thưởng
        logReward(player, reward);
    }
    
    // Trao vật phẩm
    public void giveItem(Char player, int itemId, int amount) {
        Item item = new Item(itemId, false, amount);
        
        // Thêm vào túi đồ
        if (player.addItemToBag(item)) {
            player.client.session.serivce.ShowMessWhite(
                "Bạn đã nhận được: " + item.getItemTemplate().name + " x" + amount
            );
        } else {
            // Túi đồ đầy, gửi qua thư
            sendItemViaMail(player, item);
        }
    }
}
```

### 7. Hệ Thống Log Phúc Lợi

#### 7.1 Cấu Trúc Log
```java
public class LogPhucLoi {
    public long timestamp;        // Thời gian
    public String activity;       // Hoạt động
    public String reward;         // Phần thưởng
    public int amount;            // Số lượng
    public String description;    // Mô tả
    
    public LogPhucLoi(String activity, String reward, int amount, String description) {
        this.timestamp = System.currentTimeMillis();
        this.activity = activity;
        this.reward = reward;
        this.amount = amount;
        this.description = description;
    }
}
```

#### 7.2 Ghi Log
```java
public class WelfareLogger {
    // Ghi log phúc lợi
    public void logWelfare(Char player, String activity, String reward, int amount, String description) {
        LogPhucLoi log = new LogPhucLoi(activity, reward, amount, description);
        player.phucLoi.logData.add(log);
        
        // Lưu vào database
        saveWelfareLog(player.id, log);
    }
    
    // Lưu log vào database
    private void saveWelfareLog(int playerId, LogPhucLoi log) {
        try {
            String sql = "INSERT INTO welfare_log (player_id, timestamp, activity, reward, amount, description) VALUES (?, ?, ?, ?, ?, ?)";
            // Thực hiện insert
        } catch (Exception e) {
            Utlis.logError(WelfareLogger.class, e, "Lỗi lưu log phúc lợi");
        }
    }
}
```

### 8. Hệ Thống Cập Nhật Tự Động

#### 8.1 Cập Nhật Hàng Ngày
```java
public class DailyUpdate {
    // Cập nhật phúc lợi hàng ngày
    public void updateDailyWelfare(Char player) {
        LocalDate today = LocalDate.now();
        
        if (!today.equals(player.phucLoi.lastDailyUpdate)) {
            // Reset phúc lợi hàng ngày
            player.phucLoi.thoigianOnlineHomNay = 0;
            player.phucLoi.vangNapHomNay = 0;
            player.phucLoi.vangTieuHomNay = 0;
            
            // Cập nhật ngày
            player.phucLoi.lastDailyUpdate = today;
            
            // Kiểm tra đăng nhập liên tục
            checkConsecutiveLogin(player);
        }
    }
}
```

#### 8.2 Cập Nhật Hàng Tuần
```java
public class WeeklyUpdate {
    // Cập nhật phúc lợi hàng tuần
    public void updateWeeklyWelfare(Char player) {
        LocalDate today = LocalDate.now();
        LocalDate lastWeek = player.phucLoi.lastWeeklyUpdate;
        
        if (lastWeek == null || today.getDayOfWeek().getValue() < lastWeek.getDayOfWeek().getValue()) {
            // Reset phúc lợi hàng tuần
            player.phucLoi.vangNapTuan = 0;
            player.phucLoi.vangTieuTuan = 0;
            
            // Cập nhật ngày
            player.phucLoi.lastWeeklyUpdate = today;
            
            // Phần thưởng tuần mới
            giveWeeklyReward(player);
        }
    }
}
```

### 9. Hệ Thống Phúc Lợi Đặc Biệt

#### 9.1 Sự Kiện Đặc Biệt
```java
public class SpecialEvent {
    // Phúc lợi sự kiện
    public void giveEventReward(Char player, String eventName) {
        switch (eventName) {
            case "NEW_YEAR":
                giveNewYearReward(player);
                break;
            case "SUMMER":
                giveSummerReward(player);
                break;
            case "WINTER":
                giveWinterReward(player);
                break;
            case "ANNIVERSARY":
                giveAnniversaryReward(player);
                break;
        }
    }
    
    // Phúc lợi năm mới
    private void giveNewYearReward(Char player) {
        giveReward(player, "NEW_YEAR_GIFT", 1000);
        giveItem(player, 1001, 1); // Vật phẩm đặc biệt
    }
}
```

#### 9.2 Phúc Lợi VIP
```java
public class VIPWelfare {
    // Phúc lợi VIP
    public void giveVIPReward(Char player, int vipLevel) {
        int baseReward = vipLevel * 1000;
        
        // Phần thưởng cơ bản
        giveReward(player, "VIP_BASIC", baseReward);
        
        // Phần thưởng đặc biệt theo level
        if (vipLevel >= 5) {
            giveItem(player, 2001, 1); // Vật phẩm VIP
        }
        if (vipLevel >= 10) {
            giveItem(player, 2002, 1); // Vật phẩm VIP cao cấp
        }
    }
}
```

### 10. Hệ Thống Quản Lý Phúc Lợi

#### 10.1 Admin Panel
```java
public class WelfareAdmin {
    // Tạo phúc lợi mới
    public void createWelfare(String name, String description, int amount, long duration) {
        WelfareTemplate template = new WelfareTemplate(name, description, amount, duration);
        DataCenter.gI().addWelfareTemplate(template);
    }
    
    // Chỉnh sửa phúc lợi
    public void editWelfare(int id, String name, String description, int amount, long duration) {
        WelfareTemplate template = DataCenter.gI().getWelfareTemplate(id);
        if (template != null) {
            template.name = name;
            template.description = description;
            template.amount = amount;
            template.duration = duration;
        }
    }
    
    // Xóa phúc lợi
    public void deleteWelfare(int id) {
        DataCenter.gI().removeWelfareTemplate(id);
    }
}
```

### 11. Hệ Thống Bảo Mật

#### 11.1 Validation
- Kiểm tra quyền nhận phúc lợi
- Validate dữ liệu phần thưởng
- Kiểm tra thời gian nhận
- Anti-duplicate reward

#### 11.2 Anti-Cheat
- Kiểm tra thời gian online thực tế
- Validate hoạt động nạp tiền
- Kiểm tra phần thưởng bất thường
- Log tất cả hoạt động

### 12. Hiệu Suất & Tối Ưu

#### 12.1 Performance Optimization
- Cache template phúc lợi
- Batch update cho nhiều người chơi
- Lazy loading cho log data
- Index database cho truy vấn nhanh

#### 12.2 Memory Management
- Giới hạn số lượng log lưu trữ
- Tự động dọn dẹp log cũ
- Pool object cho reward
- Memory leak prevention

### 13. Monitoring & Analytics

#### 13.1 Performance Metrics
- Số lượng phúc lợi được nhận
- Tỷ lệ tham gia các hoạt động
- Phân bố phần thưởng
- Hiệu quả của các gói phúc lợi

#### 13.2 Data Analysis
- Phân tích hành vi người chơi
- Đánh giá hiệu quả phúc lợi
- Tối ưu hóa phần thưởng
- Dự đoán xu hướng

### 14. Tích Hợp & API

#### 14.1 External Integration
- Tích hợp với hệ thống thanh toán
- API cho mobile app
- Webhook cho thông báo
- Third-party analytics

#### 14.2 Database Integration
- MySQL cho dữ liệu chính
- Redis cho cache
- MongoDB cho log
- Elasticsearch cho search

### 15. Kết Luận

Hệ thống Reward & Phúc Lợi được thiết kế với:
- **Đa dạng loại phúc lợi** cho mọi nhu cầu người chơi
- **Hệ thống tích điểm thông minh** khuyến khích tham gia
- **Phần thưởng cân bằng** duy trì game economy
- **Performance tối ưu** với caching và batch processing
- **Bảo mật cao** với validation và anti-cheat

Hệ thống tạo ra động lực cho người chơi tham gia thường xuyên, đồng thời duy trì sự cân bằng và công bằng trong game.
