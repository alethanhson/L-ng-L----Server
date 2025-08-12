# Báo Cáo Chi Tiết - Hệ Thống Auto & Tự Động

## Tổng Quan
Hệ thống Auto là tập hợp các tác vụ tự động chạy nền trong game server, bao gồm auto save, auto update, auto maintenance, và các background task khác. Hệ thống này đảm bảo server hoạt động ổn định và dữ liệu được cập nhật liên tục.

## 1. Hệ Thống Auto Save

### 1.1 Auto Save Nhân Vật
```java
// Trong Char.java - Auto save data mỗi phút
if (info.saveData < System.currentTimeMillis()) {
    CharDB.Update(this);  // Lưu dữ liệu nhân vật
    info.saveData = 60000 + System.currentTimeMillis();  // Reset timer 1 phút
    
    // Cập nhật phúc lợi hàng ngày
    phucLoi.thoigianOnlineHomNay += 60000;
    updatePhucLoiHangNgay();
    updateInfoToFamily();
}
```

### 1.2 Auto Save Server
```java
// Trong MyServerSocket.java - Save data khi đóng server
public void SaveData() {
    PlayerManager.getInstance().Clear();  // Lưu tất cả player
    Family.gI().saveData();              // Lưu dữ liệu gia tộc
    
    // Kiểm tra restart flag
    String filePath = "restart.flag";
    File file = new File(filePath);
    if (file.exists()) {
        UTPKoolVN.Print("Reboot Server Success!!");
        System.exit(0);
    }
}
```

## 2. Hệ Thống Auto Maintenance

### 2.1 Bảo Trì Tự Động
```java
// Trong Main.java - Auto maintenance lúc 4:00 sáng
private static void AutoBaoTri() {
    new Thread(() -> {
        while (true) {
            if(UTPKoolVN.getHour() == 4 && UTPKoolVN.getMinute() == 0 && !Maintenance.isRuning){
                try {
                    String flagFilePath = "restart.flag";
                    File restartFlagFile = new File(flagFilePath);
                    
                    // Tạo file restart.flag
                    boolean created = restartFlagFile.createNewFile();
                    if (created) {
                        Maintenance.gI().start(60 * 5);  // Bảo trì 5 phút
                    } else {
                        UTPKoolVN.Print("Reboot Server Failed!!");
                    }
                } catch (IOException ex) {
                    Utlis.logError(PlayerManager.class, ex, "Da say ra loi:\n" + ex.getMessage());
                }
            }
            Thread.sleep(15000);  // Kiểm tra mỗi 15 giây
        }
    }, "Bảo trì tự động").start();
}
```

### 2.2 Quản Lý Bảo Trì
```java
// Trong Maintenance.java - Xử lý bảo trì
public class Maintenance extends Thread {
    public static boolean isRuning = false;
    private int min;
    
    public void start(int min) {
        this.min = min;
        if (!isRuning) {
            this.start();
            isRuning = true;
        }
    }
    
    @Override
    public void run() {
        int count = 0;
        while (this.min > 0) {
            this.min--;
            count++;
            
            if(this.min < 60) {
                // Thông báo theo giây
                WordNhacNho("Hệ thống sẽ bảo trì sau " + min + " giây nữa, vui lòng thoát game để tránh mất vật phẩm");
                UTPKoolVN.Print("Server bao tri sau: " + min + " giay");
            } else if (count >= 60) {
                // Thông báo theo phút
                WordNhacNho("Hệ thống sẽ bảo trì sau " + min/60 + " phút nữa, vui lòng thoát game để tránh mất vật phẩm");
                UTPKoolVN.Print("Server bao tri sau: " + min/60 + " phut");
                count = 0;
            }
            
            Thread.sleep(1000);
        }
        Main.Stop();  // Dừng server
    }
}
```

## 3. Hệ Thống Auto Update

### 3.1 Auto Update Bảng Xếp Hạng
```java
// Trong Main.java - Cập nhật bảng xếp hạng mỗi phút
public static void BangXepHang() {
    new Thread(() -> {
        while (!Maintenance.isRuning) {
            UTPKoolVN.Print("Update Bang Xep Hang");
            try {
                BangXepHang.gI().update();
                Thread.sleep(60000);  // Cập nhật mỗi phút
            } catch (Exception ex) {
                Utlis.logError(Main.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
    }, "Bang Xep Hang").start();
}
```

### 3.2 Auto Update Chợ
```java
// Trong Main.java - Cập nhật chợ mỗi phút
public static void returnCho() {
    new Thread(() -> {
        while (!Maintenance.isRuning) {
            Cho.AutoUpdate();  // Cập nhật chợ
            try {
                Thread.sleep(60000);  // Cập nhật mỗi phút
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }, "Return Chợ").start();
}

// Trong Cho.java - Logic cập nhật chợ
public static void AutoUpdate() {
    try {
        List<ChoTemplate> listItemCho = new ArrayList<>();
        List<ChoTemplate> DataCho = DataCenter.gI().DataCho;
        long currentTimeMillis = System.currentTimeMillis() / 1000L;
        
        // Kiểm tra item hết hạn hoặc đã bán
        for (ChoTemplate cho : DataCho) {
            if (cho.isBuy != 0 || cho.time < currentTimeMillis) {
                listItemCho.add(cho);
            }
        }
        
        // Xử lý các item cần cập nhật
        // ...
        
    } catch (Exception e) {
        Utlis.logError(Cho.class, e, "Lỗi auto update chợ");
    }
}
```

### 3.3 Auto Update Game Bầu Cua
```java
// Trong BauCua.java - Cập nhật game mỗi giây
public void Start() {
    new Thread(this::update, "Bầu Cua").start();
}

public void update() {
    while (!Maintenance.isRuning) {
        try {
            long now = System.currentTimeMillis();
            
            if (time <= now && !isSetNumber) {
                // Hết thời gian, xử lý kết quả
                time = System.currentTimeMillis() + 65000;
                isSetNumber = true;
                handle();
            } else if (time - now <= 5000 && isSetNumber) {
                // Còn 5 giây, ẩn số
                isSetNumber = false;
            }
            
            Thread.sleep(1000);  // Cập nhật mỗi giây
            
        } catch (Exception ex) {
            Utlis.logError(BauCua.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }
    UTPKoolVN.Print("Bau Cua Close Success..!!");
}
```

## 4. Hệ Thống Auto Zone

### 4.1 Auto Update Zone
```java
// Trong Map.java - Cập nhật zone mỗi 100ms
public void createThread() {
    Thread thread = new Thread(() -> {
        while (!Maintenance.isRuning) {
            long l = System.currentTimeMillis();
            try {
                // Cập nhật nhân vật
                for (Char c : vecChar) {
                    if (c.client != null) c.update();
                }
                
                // Cập nhật vật phẩm rơi
                for (ItemMap mItemMap : vecItemMap) {
                    if (mItemMap != null) mItemMap.update(Zone.this);
                }
                
                // Cập nhật quái
                for (Mob mob : vecMob) {
                    if (System.currentTimeMillis() - mob.delayUpdate >= 1000L) {
                        mob.update(Zone.this);
                        mob.delayUpdate = System.currentTimeMillis();
                    }
                    
                    // Auto respawn mob
                    if (mob.isReSpawn && mob.isHoiSinhMob) {
                        if (mob.timeRemove == 0 && !mob.isBoss && 
                            System.currentTimeMillis() - mob.timeDie >= 2500L) {
                            mob.reSpawn();
                            reSpawnMobToAllChar(mob);
                        }
                    }
                    
                    // Auto AI tấn công
                    if (mob.CanAttack()) {
                        for (Char c : vecChar) {
                            if (!c.infoChar.isDie && mob.getRe(c) < 50 + mob.getMobTemplate().speedMove) {
                                if (System.currentTimeMillis() - mob.delayAttack >= 5000) {
                                    Zone.this.mobAttackChar(mob, c.client);
                                    mob.delayAttack = System.currentTimeMillis();
                                }
                            }
                        }
                    }
                }
                
                // Đảm bảo 100ms mỗi frame
                long sleep = (100 - (System.currentTimeMillis() - l));
                if (sleep < 1) sleep = 1;
                Utlis.sleep(sleep);
                
            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi UPDATE:\n" + ex.getMessage());
            }
        }
    });
    thread.start();
}
```

### 4.2 Auto Respawn Mob
```java
// Trong Mob.java - Tự động hồi sinh mob
public void reSpawn() {
    hp = hpFull = hpGoc;
    he = Utlis.nextInt(1, 5);  // Random hệ ngẫu nhiên
    
    // Random mob đặc biệt
    if(Utlis.nextInt(100) < 60 && id == 213 && hpGoc == 99999999) {
        expGoc = Utlis.nextInt(100000000, 1100000000);
    }
    
    exp = expGoc;
    levelBoss = 0;
    
    if(isBoss) {
        levelBoss = 7;
    }
    
    if(!isBoss) {
        int num = Utlis.nextInt(10000);
        if (num < 1) {
            // 0.01% - Mob siêu mạnh
            hp = hpFull = hpGoc * 100;
            exp = expGoc * 100;
            levelBoss = 2;
        } else if (num < 5) {
            // 0.04% - Mob mạnh
            hp = hpFull = hpGoc * 10;
            levelBoss = 1;
            exp = expGoc * 10;
        }
    }
    
    if(exp > 2100000000) exp = 2100000000;
    setHp();
    
    // Set thuộc tính theo hệ
    setElementalProperties();
}
```

## 5. Hệ Thống Auto Effect

### 5.1 Auto Update Effect
```java
// Trong Effect.java - Tự động cập nhật hiệu ứng
public void updateChar(Char aThis) {
    long l = System.currentTimeMillis();
    
    // Kiểm tra hết hạn
    if (l - timeStart >= maintain || maintain < 0) {
        setEff(aThis, this, true);  // Xóa hiệu ứng
        aThis.listEffect.remove(this);
        aThis.msgRemoveEffect(this);
        return;
    }
    
    // Không cập nhật nếu nhân vật chết
    if (aThis.infoChar.hp <= 0) return;
    
    // Xử lý theo type hiệu ứng
    EffectTemplate eff = getEffectTemplate();
    switch (eff.type) {
        case 0: // Hồi HP/MP liên tục
            if (System.currentTimeMillis() - delay >= 500) {
                if (aThis.infoChar.hp < aThis.infoChar.hpFull) {
                    aThis.PlusHp(value);
                    aThis.msgUpdateHp();
                }
                if (aThis.infoChar.mp < aThis.infoChar.mpFull) {
                    aThis.PlusMp(value);
                    aThis.msgUpdateMp();
                }
                delay = System.currentTimeMillis();
            }
            break;
            
        case 2: // Trúng độc
            if (System.currentTimeMillis() - delay >= 350) {
                int hpMine = value * 2;
                hpMine = Utlis.nextInt(hpMine * 90 / 100, hpMine);
                if (charAttack != null && charAttack.client != null) {
                    charAttack.setAttackPlayer(aThis, hpMine, false);
                }
                delay = System.currentTimeMillis();
            }
            break;
    }
}
```

## 6. Hệ Thống Auto Command Line

### 6.1 Active Command Line
```java
// Trong Main.java - Xử lý command line
public static void activeCommandLine() {
    new Thread(() -> {
        Scanner sc = new Scanner(System.in);
        while (true) {
            String line = sc.nextLine();
            if (line.equals("c")) {
                // Mở tool tạo item
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        new CreateCode().setVisible(true);
                    }
                });
            }
            // Có thể thêm các command khác
        }
    }, "Active line").start();
}
```

## 7. Hệ Thống Auto Session

### 7.1 Auto Send Message
```java
// Trong Session.java - Tự động gửi tin nhắn
threadSend = new Thread(() -> {
    while (this.isConnected()) {
        if(this.client == null || this.client.player == null || client.session == null) {
            if (timeConnect < System.currentTimeMillis()) {
                clean();
                return;
            }
        }
        
        try {
            while (vecMessage.size() > 0) {
                if (vecMessage.size() >= 20) {
                    // Batch gửi tin nhắn
                    Writer writer = new Writer();
                    int c = vecMessage.size();
                    while (vecMessage.size() > 0) {
                        Message message = (Message) vecMessage.remove(0);
                        byte cmd = message.cmd;
                        byte[] data = message.getData();
                        // Xử lý gửi tin nhắn
                    }
                }
                // Xử lý tin nhắn đơn lẻ
            }
        } catch (Exception e) {
            // Xử lý lỗi
        }
    }
});
threadSend.start();
```

## 8. Cấu Hình Auto System

### 8.1 Timer Configuration
```java
// Các timer chính trong hệ thống
public static final int AUTO_SAVE_INTERVAL = 60000;        // 1 phút
public static final int AUTO_UPDATE_INTERVAL = 60000;      // 1 phút  
public static final int AUTO_MAINTENANCE_CHECK = 15000;    // 15 giây
public static final int ZONE_UPDATE_INTERVAL = 100;        // 100ms
public static final int MOB_UPDATE_INTERVAL = 1000;        // 1 giây
public static final int EFFECT_UPDATE_INTERVAL = 500;      // 500ms
public static final int BAU_CUA_UPDATE_INTERVAL = 1000;    // 1 giây
```

### 8.2 Thread Management
```java
// Quản lý thread auto
private static final Map<String, Thread> autoThreads = new HashMap<>();

public static void startAutoThread(String name, Runnable task) {
    if (autoThreads.containsKey(name)) {
        autoThreads.get(name).interrupt();
    }
    
    Thread thread = new Thread(task, name);
    thread.setDaemon(true);  // Thread daemon
    thread.start();
    autoThreads.put(name, thread);
}

public static void stopAllAutoThreads() {
    for (Thread thread : autoThreads.values()) {
        thread.interrupt();
    }
    autoThreads.clear();
}
```

## 9. Monitoring & Performance

### 9.1 Performance Metrics
```java
// Theo dõi hiệu suất auto system
public class AutoSystemMonitor {
    private static final Map<String, Long> lastExecutionTime = new HashMap<>();
    private static final Map<String, Long> executionCount = new HashMap<>();
    
    public static void recordExecution(String taskName) {
        long currentTime = System.currentTimeMillis();
        lastExecutionTime.put(taskName, currentTime);
        executionCount.put(taskName, executionCount.getOrDefault(taskName, 0L) + 1);
    }
    
    public static void printStats() {
        UTPKoolVN.Print("=== Auto System Stats ===");
        for (String task : lastExecutionTime.keySet()) {
            long lastExec = lastExecutionTime.get(task);
            long count = executionCount.get(task);
            UTPKoolVN.Print(task + ": " + count + " executions, last: " + 
                (System.currentTimeMillis() - lastExec) + "ms ago");
        }
    }
}
```

### 9.2 Error Handling
```java
// Xử lý lỗi trong auto system
public static void safeExecute(String taskName, Runnable task) {
    try {
        task.run();
        AutoSystemMonitor.recordExecution(taskName);
    } catch (Exception e) {
        Utlis.logError(AutoSystem.class, e, "Lỗi trong task: " + taskName);
        
        // Thử thực hiện lại sau 1 phút
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    task.run();
                } catch (Exception retryEx) {
                    Utlis.logError(AutoSystem.class, retryEx, "Lỗi retry task: " + taskName);
                }
            }
        }, 60000);
    }
}
```

## 10. Gợi Ý Mở Rộng

### 10.1 Auto System Enhancements
- **Configurable Timers:** Cho phép admin điều chỉnh thời gian
- **Priority System:** Ưu tiên các task quan trọng
- **Load Balancing:** Phân tải các auto task
- **Health Checks:** Kiểm tra sức khỏe hệ thống

### 10.2 Advanced Automation
- **Machine Learning:** Tự động điều chỉnh tham số
- **Predictive Maintenance:** Dự đoán bảo trì
- **Auto Scaling:** Tự động mở rộng tài nguyên
- **Smart Scheduling:** Lập lịch thông minh

### 10.3 Monitoring & Alerting
- **Real-time Dashboard:** Hiển thị trạng thái real-time
- **Alert System:** Cảnh báo khi có vấn đề
- **Performance Analytics:** Phân tích hiệu suất
- **Resource Usage Tracking:** Theo dõi sử dụng tài nguyên
