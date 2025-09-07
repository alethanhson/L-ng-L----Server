# Báo Cáo Chi Tiết - Hệ Thống Mini Games

## Tổng Quan
Hệ thống Mini Games là tập hợp các trò chơi nhỏ trong game, bao gồm Bầu Cua, Kho Báu, và các hoạt động may mắn khác. Hệ thống này tạo ra sự đa dạng và hấp dẫn cho người chơi.

## Kiến Trúc Hệ Thống

### 1. Game Bầu Cua (`com.langla.real.baucua.BauCua`)
**Vai trò:** Quản lý trò chơi Bầu Cua với hệ thống cược và thưởng

#### 1.1 Cấu Trúc Dữ Liệu
```java
public class BauCua {
    protected static BauCua Instance;
    
    public int ketQua = 0;                      // Kết quả (1: TÔM, 2: CUA)
    public String ketQuaText = "Chưa có";       // Text kết quả
    public long tongCuoc1 = 0;                  // Tổng cược TÔM
    public long tongCuoc2 = 0;                  // Tổng cược CUA
    public long time = System.currentTimeMillis() + 65000;  // Thời gian đếm ngược
    public boolean isSetNumber = true;           // Đã set số chưa
    public ArrayList<BauCuaTpl> listChar;       // Danh sách người chơi
}
```

#### 1.2 Khởi Động Game
```java
public void Start() {
    new Thread(this::update, "Bầu Cua").start();
}
```

#### 1.3 Vòng Lặp Game
```java
public void update() {
    while (!Maintenance.isRunning) {
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
            Thread.sleep(1000);
        } catch (Exception ex) {
            Utlis.logError(BauCua.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }
    UTPKoolVN.Print("Bau Cua Close Success..!!");
}
```

### 2. Hệ Thống Xử Lý Kết Quả

#### 2.1 Xử Lý Kết Quả
```java
public void handle() {
    try {
        // Random kết quả 1-2
        ketQua = Utlis.nextInt(1, 2);
        ArrayList<BauCuaTpl> listWin = new ArrayList<BauCuaTpl>();
        
        // Tìm người thắng
        for (BauCuaTpl bauCuaTpl : listChar) {
            if (bauCuaTpl.cuoc == ketQua) {
                listWin.add(bauCuaTpl);
            }
        }
        
        // Xác định text kết quả
        switch (ketQua) {
            case 1:
                ketQuaText = "TÔM";
                break;
            case 2:
                ketQuaText = "CUA";
                break;
        }
        
        // Xử lý thưởng
        if (listWin.size() > 0) {
            handleGift(listWin);
        } else {
            PlayerManager.getInstance().chatWord("Kết quả Bầu Cua lần này là: " + ketQuaText + " rất tiếc đã không có ai may mắn");
        }
        
        // Reset game
        tongCuoc1 = 0;
        tongCuoc2 = 0;
        listChar.clear();
    } catch (Exception ex) {
        Utlis.logError(BauCua.class, ex, "Da say ra loi:\n" + ex.getMessage());
    }
}
```

#### 2.2 Xử Lý Thưởng
```java
public void handleGift(ArrayList<BauCuaTpl> listWin) {
    try {
        int amount = 0;
        int rand = Utlis.nextInt(1, 3);
        
        for (BauCuaTpl bauCuaTpl : listWin) {
            Char charWin = PlayerManager.getInstance().getChar(bauCuaTpl.idChar);
            Thu thu = new Thu();
            thu.chuDe = "Thưởng Mini Game Bầu Cua";
            thu.nguoiGui = "Hệ thống";
            thu.noiDung = "Phần thưởng tham gia mini game Bầu Cua";
            
            // Random loại thưởng
            if (rand == 1) {
                // Thưởng vàng (1.5x - 2x)
                double xvang = 1.5 + Math.random() * 0.5;
                int a = (int) (bauCuaTpl.vangCuoc * xvang);
                thu.vang = a;
                amount += a;
            } else if (rand == 2) {
                // Thưởng vỏ sò (0.05x - 0.1x)
                double xso = 0.05 + Math.random() * 0.05;
                Item voso = new Item(176);
                int a = (int) (bauCuaTpl.vangCuoc * xso);
                voso.amount = a;
                thu.item = voso;
                amount += a;
            } else if (rand == 3) {
                // Thưởng chakra vĩ thú (10x - 15x)
                int xchar = Utlis.nextInt(10, 15);
                Item item = new Item(763);
                int a = bauCuaTpl.vangCuoc * xchar;
                item.amount = a;
                thu.item = item;
                amount += a;
            }
            
            // Gửi thưởng
            if (charWin == null) {
                // Người chơi offline
                thu.id = 999;
                CharDB.guiThuOffline(bauCuaTpl.idChar, thu);
            } else {
                // Người chơi online
                thu.id = charWin.baseIdThu++;
                charWin.listThu.add(thu);
                charWin.client.session.serivce.updateThu();
            }
        }
        
        // Thông báo kết quả
        if (rand == 1) {
            PlayerManager.getInstance().chatWord("Chúc mừng " + listWin.size() + " người đã nhận được " + Utlis.numberFormat(amount) + " vàng từ Mini Game Bầu Cua");
        } else if (rand == 2) {
            PlayerManager.getInstance().chatWord("Chúc mừng " + listWin.size() + " người đã nhận được " + Utlis.numberFormat(amount) + " Vỏ Sò từ Mini Game Bầu Cua");
        } else if (rand == 3) {
            PlayerManager.getInstance().chatWord("Chúc mừng " + listWin.size() + " người đã nhận được " + Utlis.numberFormat(amount) + " Chakra Vĩ Thú từ Mini Game Bầu Cua");
        }
    } catch (Exception ex) {
        Utlis.logError(BauCua.class, ex, "Da say ra loi:\n" + ex.getMessage());
    }
}
```

### 3. Game Kho Báu (`com.langla.real.khobau.KhoBau`)
**Vai trò:** Quản lý trò chơi Kho Báu với hệ thống quay thưởng

#### 3.1 Cấu Trúc Dữ Liệu
```java
public class KhoBau {
    protected static KhoBau Instance;
    
    // ID các loại vật phẩm thưởng
    public List<Integer> idHoaLuc = Arrays.asList(0, 9, 18, 4, 17);      // Hỏa lực
    public List<Integer> idBac = Arrays.asList(2, 5, 19, 10, 1);          // Bạc
    public List<Integer> idVangKhoa = Arrays.asList(8, 13, 16, 21, 12);   // Vàng khóa
    public List<Integer> idDa = Arrays.asList(7, 11, 20, 15, 6);          // Đá
    public List<Integer> idRuong = Arrays.asList(3, 14);                   // Rương
    public List<Integer> idHiem = Arrays.asList(3, 14, 15, 6, 21, 12, 10, 1, 4, 17); // Hiếm
}
```

#### 3.2 Khởi Động Kho Báu
```java
public void Star(Char chars) {
    chars.quayKhoBau = new KhoBauTpl();
    chars.client.session.serivce.SendKhoBau();
}
```

#### 3.3 Hệ Thống Quay Thưởng
```java
public void Quay(Char chars, byte muccuoc) {
    try {
        // Xác định số vỏ sò cần
        int minevoso = 2;
        if (muccuoc == 1) {
            minevoso = 10;
        } else if (muccuoc == 2) {
            minevoso = 50;
        }
        
        // Kiểm tra vỏ sò
        int vosobag = chars.getAmountAllById(176);
        if (vosobag < minevoso) {
            chars.client.session.serivce.ShowMessGold("Không có đủ vỏ sò");
            return;
        }
        
        // Trừ vỏ sò
        chars.removeAmountAllItemBagById(176, minevoso, "Quay vỏ sò");
        
        int sao = 0;
        int item = 0;
        
        // Xử lý lần đầu quay
        if (chars.quayKhoBau.solanquay == 0) {
            if (muccuoc == 1) {
                if (Utlis.nextInt(100) < 2) {  // 2% cơ hội
                    sao = Utlis.nextInt(2);
                }
            } else if (muccuoc == 2) {
                if (Utlis.nextInt(100) < 5) {  // 5% cơ hội
                    sao = Utlis.nextInt(2);
                }
            } else {
                sao = 0;
            }
        } else {
            // Xử lý các lần quay tiếp theo
            int tile = Utlis.nextInt(1, 100);
            int phanTramCong = 0;
            
            if (muccuoc == 1) {
                phanTramCong = 1;  // Tăng 1%
            } else if (muccuoc == 2) {
                phanTramCong = 3;  // Tăng 3%
            }
            
            // Phân bố tỉ lệ sao
            if (tile <= 1 + phanTramCong) {        // 1%
                sao = 6;
            } else if (tile <= 2 + phanTramCong) { // 1%
                sao = 5;
            } else if (tile <= 4 + phanTramCong) { // 2%
                sao = 4;
            } else if (tile <= 7 + phanTramCong) { // 3%
                sao = 3;
            } else if (tile <= 11 + phanTramCong) { // 4%
                sao = 2;
            } else if (tile <= 16 + phanTramCong) { // 5%
                sao = 1;
            } else {                                // 83%
                sao = -2;
            }
        }
        
        // Kiểm tra mức cược
        if (chars.quayKhoBau.solanquay > 0 && chars.quayKhoBau.cuoc != muccuoc) {
            chars.client.session.serivce.ShowMessGold("Đã sảy ra lỗi. vui lòng thoát tab kho báu và thử lại");
            return;
        }
        
        // Cập nhật thông tin quay
        chars.quayKhoBau.solanquay++;
        chars.quayKhoBau.cuoc = muccuoc;
        
        // Xử lý kết quả sao
        if (sao > 0) {
            // Thưởng theo sao
            handleRewardByStar(chars, sao);
        } else if (sao == 0) {
            // Thưởng vật phẩm thường
            handleNormalReward(chars);
        } else {
            // Thưởng đặc biệt
            handleSpecialReward(chars);
        }
        
    } catch (Exception ex) {
        Utlis.logError(KhoBau.class, ex, "Da say ra loi:\n" + ex.getMessage());
    }
}
```

### 4. Hệ Thống Template

#### 4.1 BauCuaTpl
```java
public class BauCuaTpl {
    public int idChar;           // ID nhân vật
    public String nameChar;      // Tên nhân vật
    public int cuoc;             // Lựa chọn cược (1: TÔM, 2: CUA)
    public long vangCuoc;        // Số vàng cược
    public long timeCuoc;        // Thời gian cược
}
```

#### 4.2 KhoBauTpl
```java
public class KhoBauTpl {
    public int solanquay;        // Số lần quay
    public byte cuoc;            // Mức cược
    public long timeStart;       // Thời gian bắt đầu
    public boolean isFinish;     // Đã hoàn thành chưa
}
```

### 5. Hệ Thống Quản Lý Người Chơi

#### 5.1 Kiểm Tra Người Chơi
```java
public BauCuaTpl checkPlayer(int id) {
    for (BauCuaTpl bauCuaTpl : listChar) {
        if (bauCuaTpl.idChar == id) {
            return bauCuaTpl;
        }
    }
    return null;
}
```

#### 5.2 Thêm Người Chơi
```java
public void addPlayer(BauCuaTpl player) {
    if (!listChar.contains(player)) {
        listChar.add(player);
        // Cập nhật tổng cược
        if (player.cuoc == 1) {
            tongCuoc1 += player.vangCuoc;
        } else if (player.cuoc == 2) {
            tongCuoc2 += player.vangCuoc;
        }
    }
}
```

### 6. Hệ Thống Thưởng

#### 6.1 Phân Loại Thưởng
- **Thưởng Vàng:** Tỷ lệ 1.5x - 2x số tiền cược
- **Thưởng Vỏ Sò:** Tỷ lệ 0.05x - 0.1x số tiền cược
- **Thưởng Chakra Vĩ Thú:** Tỷ lệ 10x - 15x số tiền cược

#### 6.2 Hệ Thống Sao (Kho Báu)
- **6 Sao:** 1% cơ hội (thưởng cao nhất)
- **5 Sao:** 1% cơ hội
- **4 Sao:** 2% cơ hội
- **3 Sao:** 3% cơ hội
- **2 Sao:** 4% cơ hội
- **1 Sao:** 5% cơ hội
- **0 Sao:** 83% cơ hội (thưởng thường)
- **-2 Sao:** Thưởng đặc biệt

### 7. Hệ Thống Mức Cược

#### 7.1 Bầu Cua
- **TÔM:** Cược vào kết quả TÔM
- **CUA:** Cược vào kết quả CUA

#### 7.2 Kho Báu
- **Mức 1:** 10 vỏ sò (tỷ lệ thấp)
- **Mức 2:** 50 vỏ sò (tỷ lệ cao)

### 8. Hệ Thống Thời Gian

#### 8.1 Bầu Cua
- **Thời gian đếm ngược:** 65 giây
- **Thời gian ẩn số:** 5 giây cuối
- **Chu kỳ game:** Liên tục

#### 8.2 Kho Báu
- **Không giới hạn thời gian**
- **Có thể quay nhiều lần**
- **Reset theo session**

### 9. Hệ Thống Bảo Mật

#### 9.1 Validation
- Kiểm tra số vỏ sò đủ
- Kiểm tra mức cược hợp lệ
- Kiểm tra người chơi tồn tại

#### 9.2 Anti-Cheat
- Kiểm tra thời gian cược
- Validate dữ liệu đầu vào
- Log tất cả hoạt động

### 10. Hệ Thống Database

#### 10.1 Lưu Trữ Dữ Liệu
- Lưu thông tin cược
- Lưu lịch sử thưởng
- Lưu trạng thái người chơi

#### 10.2 Gửi Thưởng Offline
```java
if (charWin == null) {
    thu.id = 999;
    CharDB.guiThuOffline(bauCuaTpl.idChar, thu);
}
```

### 11. Hệ Thống Thông Báo

#### 11.1 Chat Server
```java
PlayerManager.getInstance().chatWord("Kết quả Bầu Cua lần này là: " + ketQuaText + " rất tiếc đã không có ai may mắn");
```

#### 11.2 Thông Báo Cá Nhân
```java
chars.client.session.serivce.ShowMessGold("Không có đủ vỏ sò");
```

### 12. Hiệu Suất & Tối Ưu

#### 12.1 Thread Management
- Sử dụng thread riêng cho mỗi game
- Sleep hợp lý để tiết kiệm CPU
- Xử lý bất đồng bộ cho các tác vụ nặng

#### 12.2 Memory Management
- Sử dụng ArrayList cho danh sách động
- Tự động dọn dẹp dữ liệu cũ
- Cache thông tin người chơi

### 13. Monitoring & Debugging

#### 13.1 Performance Metrics
- Số lượng người chơi tham gia
- Tỷ lệ thắng/thua
- Thời gian xử lý mỗi vòng

#### 13.2 Error Handling
```java
try {
    // Xử lý logic
} catch (Exception ex) {
    Utlis.logError(BauCua.class, ex, "Da say ra loi:\n" + ex.getMessage());
}
```

### 14. Tính Năng Đặc Biệt

#### 14.1 Hệ Thống May Mắn
- Random tỷ lệ thưởng
- Tăng tỷ lệ theo mức cược
- Bonus cho người chơi mới

#### 14.2 Hệ Thống Cải Thiện
- Tăng tỷ lệ theo số lần quay
- Reset may mắn mỗi session
- Cân bằng game economy

### 15. Kết Luận

Hệ thống Mini Games được thiết kế với:
- **Đa dạng loại game** với cơ chế khác nhau
- **Hệ thống thưởng cân bằng** và hấp dẫn
- **Bảo mật cao** với validation đầy đủ
- **Performance tối ưu** với thread management
- **Dễ dàng mở rộng** thêm game mới

Hệ thống tạo ra sự đa dạng và hấp dẫn cho người chơi, đồng thời duy trì sự cân bằng trong game economy.
