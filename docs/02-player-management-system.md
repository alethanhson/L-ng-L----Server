# Báo Cáo Chi Tiết - Hệ Thống Player Management

## Tổng Quan
Hệ thống Player Management là trung tâm quản lý toàn bộ người chơi, nhân vật, session và các tương tác trong game. Hệ thống này đảm bảo việc quản lý hiệu quả hàng nghìn người chơi đồng thời.

## Kiến Trúc Hệ Thống

### 1. PlayerManager Class (`com.langla.real.player.PlayerManager`)
**Vai trò:** Singleton class quản lý toàn bộ người chơi và session

#### 1.1 Cấu Trúc Dữ Liệu
```java
public class PlayerManager {
    protected static PlayerManager instance;
    
    // Quản lý session kết nối
    private final ArrayList<Session> conns = new ArrayList<Session>();
    
    // Quản lý player theo ID
    private final Map<Integer, Player> players_id = new HashMap<Integer, Player>();
    
    // Quản lý character theo ID và tên
    private final Map<Integer, Char> char_id = new HashMap<Integer, Char>();
    private final Map<String, Char> char_name = new HashMap<String, Char>();
}
```

#### 1.2 Singleton Pattern
```java
public static PlayerManager getInstance() {
    if (instance == null)
        instance = new PlayerManager();
    return instance;
}
```

### 2. Character System (`com.langla.real.player.Char`)

#### 2.1 Cấu Trúc Nhân Vật
```java
public class Char extends Entity {
    @JsonIgnore
    public Client client;                    // Client kết nối
    @JsonIgnore
    public InfoTuongKhac TuongKhac;         // Thông tin tướng khác
    @JsonIgnore
    public Info info;                        // Thông tin cơ bản
    @JsonIgnore
    public Trade traDe;                     // Hệ thống giao dịch
    
    public int id;                          // ID nhân vật
    public InfoChar infoChar;               // Thông tin chi tiết
    public Skill[] arraySkill;              // Danh sách kỹ năng
    public Skill skillFight;                // Kỹ năng chiến đấu
    public ArrayList<Effect> listEffect;    // Danh sách hiệu ứng
}
```

#### 2.2 Hệ Thống Skill
```java
// Skill cho từng hệ (5 hệ chính)
public Skill[] skills_0 = new Skill[]{
    DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.KIEM_THUAT_CO_BAN, 1)
};

public Skill[] skills_1 = new Skill[]{
    DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.KIEM_THUAT_CO_BAN, 1),
    DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.TAM_TRUNG_OAN_THU_THUAT, 0),
    DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.KIEM_THUAT_TAM_PHAP, 0),
    DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.DICH_CHUYEN_CHI_THUAT, 0),
    DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.LOI_KIEM, 0),
    DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.TRIEU_HOI_CHIM_CHI_THUAT, 0)
};
```

#### 2.3 Hệ Thống Inventory
```java
public Item[] arrItemBag = new Item[27];        // Túi đồ (27 slot)
public Item[] arrItemBox = new Item[36];        // Hộp đồ (36 slot)
public Item[] arrItemBody = new Item[17];       // Trang bị trên người (17 slot)
public Item[] arrItemBody2 = new Item[17];      // Trang bị phụ (17 slot)
public Item[] arrItemExtend = new Item[3];      // Trang bị mở rộng (3 slot)
```

#### 2.4 Hệ Thống Xã Hội
```java
public ArrayList<SkillClan> listSkillViThu;    // Kỹ năng vị thủ
public ArrayList<DanhHieu> listDanhHieu;       // Danh hiệu
public ArrayList<Thu> listThu;                 // Thư từ
public ArrayList<Friend> listFriend;           // Danh sách bạn bè
public ArrayList<Enemy> listEnemy;             // Danh sách kẻ thù
```

### 3. Thông Tin Nhân Vật (InfoChar)

#### 3.1 Thuộc Tính Cơ Bản
```java
public class InfoChar {
    public String username;           // Tên đăng nhập
    public String name;               // Tên nhân vật
    public byte gioiTinh;            // Giới tính
    public byte idNVChar;            // ID nhân vật
    public byte idhe;                // ID hệ
    public byte idClass;             // ID lớp
    public byte lvPk;                // Level PK
    public int taiPhu;               // Tài phú
    public short speedMove;          // Tốc độ di chuyển
    public byte sachChienDau;        // Sách chiến đấu
}
```

#### 3.2 Thuộc Tính Chiến Đấu
```java
public int hp;                       // HP hiện tại
public int hpFull;                   // HP tối đa
public int mp;                       // MP hiện tại
public int mpFull;                   // MP tối đa
public long exp;                     // Kinh nghiệm
public int bac;                      // Bạc
public int bacKhoa;                  // Bạc khóa
public int vang;                     // Vàng
public int vangKhoa;                 // Vàng khóa
```

#### 3.3 Thuộc Tính Nâng Cao
```java
public short idTask;                 // ID nhiệm vụ
public byte idStep;                  // Bước nhiệm vụ
public short requireTask;            // Yêu cầu nhiệm vụ
public int hoatLuc;                  // Hoạt lực
public int pointNAP;                 // Điểm NAP
public byte rank;                    // Cấp bậc
public byte selectCaiTrang;         // Cài trang được chọn
public int timeChatColor;            // Thời gian chat màu
```

### 4. Hệ Thống Session Management

#### 4.1 Quản Lý Kết Nối
```java
public void put(Session conn) {
    if (!conns.contains(conn)) {
        conns.add(conn);
    }
}

public void remove(Session conn) {
    if (conn.client.player != null) {
        remove(conn.client.player);
        Player.Update(conn.client.player);
    }
    if (conn.client.mChar != null) {
        remove(conn.client.mChar);
        conn.client.mChar.clean();
        CharDB.Update(conn.client.mChar);
    }
    conns.remove(conn);
}
```

#### 4.2 Kiểm Tra Session
```java
public boolean isCheckSession(Session s) {
    return conns.contains(s);
}
```

### 5. Hệ Thống Giao Tiếp

#### 5.1 Chat Toàn Server
```java
public void chatWord(String text) {
    try {
        Message msg = new Message((byte) 22);
        msg.writeByte(1);
        msg.writeUTF("Hệ thống");
        msg.writeUTF(text);
        sendMessageAllChar(msg);
    } catch (Exception ex) {
        Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
    }
}
```

#### 5.2 Gửi Tin Nhắn Cho Tất Cả
```java
public void sendMessageAllChar(Message m) {
    synchronized (conns) {
        for (int i = conns.size()-1; i >= 0; i--)
            if (conns.get(i).client.player != null && conns.get(i).client.mChar != null)
                conns.get(i).sendMessage(m);
    }
}
```

### 6. Hệ Thống Serialization

#### 6.1 Ghi Dữ Liệu Nhân Vật
```java
public void writeMe(Writer writer) throws IOException {
    setUpInfo(false);
    writer.writeUTF(infoChar.username);
    writer.writeInt(id);
    writer.writeUTF(infoChar.name);
    writer.writeByte(infoChar.gioiTinh);
    writer.writeByte(infoChar.idNVChar);
    writer.writeByte(infoChar.idhe);
    writer.writeByte(infoChar.idClass);
    // ... các thông tin khác
}
```

#### 6.2 Ghi Dữ Liệu Hiển Thị
```java
public void writeView(Writer writer) throws IOException {
    writer.writeUTF(infoChar.name);
    writer.writeLong(infoChar.exp);
    writer.writeByte(infoChar.idNVChar);
    writer.writeByte(infoChar.idhe);
    writer.writeByte(infoChar.idClass);
    writer.writeByte(infoChar.gioiTinh);
    writer.writeByte(infoChar.sachChienDau);
    // ... các thông tin hiển thị
}
```

### 7. Hệ Thống Quản Lý Player

#### 7.1 Thêm/Xóa Player
```java
public void put(Player p) {
    if (!players_id.containsKey(p.id)) 
        players_id.put(p.id, p);
}

public void put(Char n) {
    if (!char_id.containsKey(n.id)) 
        char_id.put(n.id, n);
    if (!char_name.containsKey(n.infoChar.name)) 
        char_name.put(n.infoChar.name, n);
}
```

#### 7.2 Tìm Kiếm Player
```java
public synchronized Player getPlayerLogin(int id) {
    return players_id.get(id);
}

public Char getChar(int id) {
    return char_id.get(id);
}

public Char getChar(String name) {
    return char_name.get(name);
}
```

### 8. Hệ Thống Thống Kê

#### 8.1 Số Lượng
```java
public int char_size() {
    return char_id.size();
}

public int conn_size() {
    return conns.size();
}

public int player_size() {
    return players_id.size();
}
```

### 9. Hệ Thống Cài Trang

#### 9.1 Chọn Cài Trang
```java
public void selectCaiTrang(byte selectCaiTrang) {
    infoChar.selectCaiTrang = selectCaiTrang;
    client.session.serivce.updateSelectCaiTrang((byte) infoChar.selectCaiTrang);
}
```

#### 9.2 Cập Nhật Trang Bị
```java
public void updateItemBody(Item item) {
    try {
        Writer writer = new Writer();
        item.write(writer);
        client.session.sendMessage(new Message((byte) -21, writer));
    } catch (Exception ex) {
        Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
    }
}
```

### 10. Hệ Thống Phân Hệ

#### 10.1 Xác Định Hệ
```java
public byte getLopFormSelectChar(byte i) {
    switch (i) {
        case 0:
        case 5:
            return 1;  // Hệ 1
        case 1:
        case 6:
            return 2;  // Hệ 2
        case 2:
        case 7:
            return 3;  // Hệ 3
        case 3:
        case 8:
            return 4;  // Hệ 4
        case 4:
            return 5;  // Hệ 5
    }
    return 0;
}
```

### 11. Hệ Thống Database

#### 11.1 Cập Nhật Dữ Liệu
```java
public void remove(Session conn) {
    if (conn.client.player != null) {
        remove(conn.client.player);
        Player.Update(conn.client.player);        // Cập nhật Player
    }
    if (conn.client.mChar != null) {
        remove(conn.client.mChar);
        conn.client.mChar.clean();
        CharDB.Update(conn.client.mChar);        // Cập nhật Character
    }
    conns.remove(conn);
}
```

### 12. Hệ Thống Bảo Mật

#### 12.1 Synchronization
```java
public synchronized Player getPlayerLogin(int id) {
    return players_id.get(id);
}

public void sendMessageAllChar(Message m) {
    synchronized (conns) {
        // Xử lý gửi tin nhắn
    }
}
```

#### 12.2 Validation
- Kiểm tra session tồn tại trước khi xử lý
- Validate dữ liệu trước khi cập nhật
- Kiểm tra quyền truy cập

### 13. Hiệu Suất & Tối Ưu

#### 13.1 Memory Management
- Sử dụng HashMap cho tìm kiếm nhanh theo ID
- ArrayList cho quản lý session
- Tự động dọn dẹp dữ liệu không sử dụng

#### 13.2 Caching Strategy
- Cache thông tin nhân vật trong memory
- Lazy loading cho dữ liệu không cần thiết
- Batch update cho database

### 14. Monitoring & Debugging

#### 14.1 Performance Metrics
- Số lượng player online
- Số lượng session active
- Thời gian xử lý các operation

#### 14.2 Error Handling
```java
try {
    // Xử lý logic
} catch (Exception ex) {
    Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
}
```

### 15. Kết Luận

Hệ thống Player Management được thiết kế với:
- **Kiến trúc modular** dễ mở rộng
- **Quản lý hiệu quả** hàng nghìn người chơi
- **Bảo mật cao** với synchronization
- **Hiệu suất tối ưu** với caching và indexing
- **Dễ dàng bảo trì** với code structure rõ ràng

Hệ thống có khả năng xử lý số lượng người chơi lớn với độ ổn định cao và khả năng mở rộng tốt.
