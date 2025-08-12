# Báo Cáo Chi Tiết - Hệ Thống Phụ Bản Đặc Biệt

## Tổng Quan
Hệ thống phụ bản đặc biệt bao gồm các hoạt động dungeon, ải gia tộc, cấm thuật và các map tùy chỉnh với cơ chế hoạt động riêng biệt. Các phụ bản này được quản lý thông qua `DataCache.idMapCustom` và có logic xử lý đặc thù.

## Danh Sách Phụ Bản Chính

### 1. Cấm Thuật Izanami (Map 89 - Vòng Lặp Ảo Tưởng)

#### 1.1 Thông Tin Cơ Bản
- **Yêu cầu:** Cấp 40+, có thể tham gia tổ đội
- **Thời gian:** 5 phút mỗi vòng lặp
- **Hồi sinh:** Tối đa 3 lần trong phụ bản
- **NPC:** Onoki (các làng)

#### 1.2 Cơ Chế Hoạt Động
```java
// Kiểm tra số lần tham gia
if (client.mChar.infoChar.soLanCamThuat <= 0 && client.player.role < 2) {
    client.mChar.client.session.serivce.NhacNhoMessage(
        "Số lần tham gia cấm thuật hôm nay đã hết quay lại vào ngày mai, hoặc hãy sử dụng Lệnh bài Izanami"
    );
    break;
}
client.mChar.infoChar.soLanCamThuat -= 1;

// Tạo zone tùy chỉnh
if (client.mChar.infoChar.groupId == -1) {
    // Solo: tạo zone riêng
    short id = Map.maps[89].createZoneCustom(
        client.mChar.infoChar.groupId, -1, 300000, 
        System.currentTimeMillis(), false, false, 
        client.mChar.level(), 500, 10000, false
    );
    Map.maps[89].addCharInMapCustom(client, id);
} else {
    // Tổ đội: chỉ trưởng nhóm mở
    GroupTemplate group = Group.gI().getGroup(client.mChar.infoChar.groupId);
    Char key = group.getKey();
    if (key == null || key.id != client.mChar.id) {
        client.session.serivce.NhacNhoMessage("Chỉ trưởng nhóm mới có thể mở cấm thuật");
        break;
    }
    
    short id = Map.maps[89].createZoneCustom(
        client.mChar.infoChar.groupId, -1, 300000, 
        System.currentTimeMillis(), false, false, 
        client.mChar.level(), 500, 1, false
    );
    group.idZoneCamThuat = id;
    // Thêm tất cả thành viên tổ đội vào zone
}
```

#### 1.3 Logic Boss & Vòng Lặp
```java
if (zone.map.mapID == 89) {
    ArrayList<Mob> aliveMobs = getAliveMobs(zone.vecMob);
    if (aliveMobs.isEmpty()) {
        if (!zone.infoMap.isBossCamThuat) {
            // Tạo boss Kabuto (ID 238)
            Mob mob2 = new Mob();
            mob2.id = 238;
            mob2.hpGoc = mob.hpFull * 100;
            mob2.exp = mob.exp * 100;
            mob2.expGoc = mob.expGoc * 100;
            mob2.level = mob.level;
            mob2.paintMiniMap = false;
            mob2.isBoss = true;
            mob2.timeRemove = 300000 + System.currentTimeMillis();
            mob2.setXY((short) 1050, (short) 137);
            mob2.reSpawn();
            zone.vecMob.add(mob2);
            addMobToZone(mob2);
            zone.showMessWhiteZone("Kabuto đã xuất hiện.");
            zone.infoMap.isBossCamThuat = true;
        } else {
            // Xử lý vòng lặp tiếp theo
            if (zone.infoMap.vongLap < 15) {
                zone.infoMap.vongLap += 1;
                zone.infoMap.time = zone.infoMap.timeClose = 11000;
                zone.infoMap.timeStartHoatDong = System.currentTimeMillis();
                zone.infoMap.isNextMapCamThuat = true;
                updateTimeHoatDongZone(zone.infoMap.timeStartHoatDong, zone.infoMap.time, false);
            } else {
                // Hoàn thành 15 vòng lặp
                zone.infoMap.time = zone.infoMap.timeClose = 11000;
                zone.infoMap.timeStartHoatDong = System.currentTimeMillis();
                zone.infoMap.isNextMapCamThuat = false;
                updateTimeHoatDongZone(zone.infoMap.timeStartHoatDong, zone.infoMap.time, false);
            }
        }
    }
}
```

#### 1.4 Phần Thưởng Đặc Biệt
```java
if (client.mChar.zone.map.mapID == 89) { // cấm thuật, vòng lặp ảo tưởng
    List<Integer> listItem = Arrays.asList(354, 562, 564, 566); // đá chế tạo
    item = new Item(UTPKoolVN.getRandomList(listItem));
    itemMap = ItemMap.createItemMap(item, mob.cx, mob.cy);
    itemMap.idEntity = DataCache.getIDItemMap();
    itemMap.idChar = client.mChar.id;
    createItemMap(itemMap, mob);
}
```

### 2. Ải Gia Tộc (Map 46 & 47)

#### 2.1 Thông Tin Cơ Bản
- **Yêu cầu:** Phải có gia tộc, chỉ tộc trưởng/tộc phó mở cửa
- **Thời gian:** 120 giây (2 phút)
- **Giới hạn:** 1 lần/ngày cho mỗi gia tộc
- **NPC:** Onoki

#### 2.2 Cơ Chế Mở Cửa
```java
if (giaToc.info.soLanMoAi < 1) {
    client.session.serivce.ShowMessGold("Số lần mở ải đã hết, hãy quay lại vào ngày mai");
    break;
}
if (giaToc.MapAi.size() > 0) {
    client.session.serivce.ShowMessGold("Ải đã được mở hãy chọn vào ải");
    break;
}

Family_Member getMem = Family.gI().getMe(client.mChar, giaToc);
if (getMem != null && getMem.role >= 4) { // Tộc trưởng hoặc tộc phó
    giaToc.MapAi.clear();
    short id = Map.maps[46].createZoneCustom(-1, giaToc.id, 120000, System.currentTimeMillis(), false, false, client.mChar.level(), 500, 1, false);
    short id2 = Map.maps[47].createZoneCustom(-1, giaToc.id, 120000, System.currentTimeMillis(), false, false, client.mChar.level(), 500, 1, false);
    client.mChar.chatFamily("Đã mở cửa Ải Gia Tộc");
    Map.maps[46].addCharInMapCustom(client, id);
    giaToc.MapAi.put(46, id);
    giaToc.MapAi.put(47, id2);
    giaToc.info.soLanMoAi -= 1;
}
```

#### 2.3 Tạo Quái Vật Theo Hệ (Map 46)
```java
if (map.mapID == 46) {
    int hpMob = 18000;
    int l = 60;
    
    // Hệ Thổ (14 con)
    for (int i = 0; i < 14; i++) {
        Mob mob = new Mob();
        mob.id = 123;
        mob.level = level;
        mob.he = 2; // Thổ
        mob.cx = (short) (460 + l);
        mob.cy = 363;
        mob.timeRemove = 7200000 + System.currentTimeMillis();
        mob.hpGoc = mob.hp = mob.hpFull = level * hpMob;
        mob.expGoc = mob.hpGoc / 8;
        mob.paintMiniMap = false;
        mob.isHoiSinhMob = false;
        mob.createNewEffectList();
        mob.idEntity = DataCache.getIDMob();
        mob.reSpawnMobHoatDong(1, false);
        vecMob.add(mob);
        addMobToZone(mob);
        l += 60;
    }
    
    // Tương tự cho các hệ khác: Thủy (124), Hỏa (125), Phong (126)
    // Mỗi hệ 14 con, vị trí khác nhau
}
```

#### 2.4 Tạo Quái Vật Đặc Biệt (Map 47)
```java
if (map.mapID == 47) {
    int hpMob = 18000;
    int l = 60;
    
    // Hồi sức (19 con)
    for (int i = 0; i < 19; i++) {
        Mob mob = new Mob();
        mob.id = 127;
        mob.level = level;
        mob.cx = (short) (200 + l);
        mob.cy = 302;
        mob.timeRemove = 7200000 + System.currentTimeMillis();
        mob.hpGoc = mob.hp = mob.hpFull = level * hpMob;
        mob.expGoc = mob.hpGoc / 8;
        mob.paintMiniMap = false;
        mob.isHoiSinhMob = false;
        mob.createNewEffectList();
        mob.idEntity = DataCache.getIDMob();
        mob.reSpawnMobHoatDong(1, true);
        mob.setHoiHp(); // Khả năng hồi HP
        vecMob.add(mob);
        l += 60;
    }
    
    // Phản đòn (19 con)
    for (int i = 0; i < 19; i++) {
        Mob mob = new Mob();
        mob.id = 128;
        mob.level = level;
        mob.cx = (short) (200 + l);
        mob.cy = 600;
        mob.timeRemove = 7200000 + System.currentTimeMillis();
        mob.hpGoc = mob.hp = mob.hpFull = level * hpMob;
        mob.expGoc = mob.hpGoc / 8;
        mob.paintMiniMap = false;
        mob.isHoiSinhMob = false;
        mob.createNewEffectList();
        mob.idEntity = DataCache.getIDMob();
        mob.reSpawnMobHoatDong(1, true);
        mob.setPhanDon(); // Khả năng phản đòn
        vecMob.add(mob);
        l += 60;
    }
}
```

#### 2.5 Boss & Phần Thưởng
```java
if (zone.map.mapID == 46) {
    ArrayList<Mob> aliveMobs = getAliveMobs(zone.vecMob);
    if (aliveMobs.isEmpty()) {
        for (Char c : zone.vecChar) {
            c.client.session.serivce.ShowMessWhite("Bạn nhận được 1 điểm chuyên cần 2 điểm cống hiến gia tộc");
            c.infoChar.chuyenCan += 1;
            c.infoChar.chuyenCanTuan += 1;
            FamilyTemplate giaToc = Family.gI().getGiaToc(FamilyId);
            if (giaToc != null) {
                Family_Member getMem = Family.gI().getMe(c, giaToc);
                if (getMem != null) {
                    getMem.congHien += 2;
                    getMem.congHienTuan += 2;
                    giaToc.info.congHienTuan += 2;
                    giaToc.info.PlusExp(20); // Gia tộc nhận EXP
                }
            }
        }
    }
}
```

### 3. Sơn Cáp (Map 84 - Khu Luyện Tập)

#### 3.1 Thông Tin Cơ Bản
- **Loại:** Map luyện tập thường
- **Quái vật:** ID 121, level 66, HP 231,000
- **Vị trí:** Các điểm cố định trên map

#### 3.2 Cấu Trúc Quái Vật
```json
{
  "cx": 242, "cy": 212, "exp": 13398, "hp": 231000,
  "id": 121, "level": 66, "levelBoss": 0,
  "paintMiniMap": false, "status": 2
}
```

### 4. Địa Cung (Maps 6, 7, 18, 19)

#### 4.1 Thông Tin Cơ Bản
- **Yêu cầu:** Cấp 15+
- **Cấp độ:** Sơ cấp, Trung cấp, Cao cấp, Thượng cấp
- **NPC:** Raikage (các làng)
- **Có thể:** Tổ đội tham gia

#### 4.2 Menu NPC
```java
case 59:
    addAction(list, "Nhận chìa khóa", client -> client.session.serivce.nullopen());
    addAction(list, "Địa cung (Sơ cấp)", client -> client.session.serivce.nullopen());
    addAction(list, "Địa cung (Trung cấp)", client -> client.session.serivce.nullopen());
    addAction(list, "Địa cung (Cao cấp)", client -> client.session.serivce.nullopen());
    addAction(list, "Địa cung (Thượng cấp)", client -> client.session.serivce.nullopen());
    break;
```

#### 4.3 Map Template
```java
MapTemplate{id=6, name=Địa cung (sơ cấp), typeBlockMap=0, type=4}
MapTemplate{id=7, name=Địa cung (trung cấp), typeBlockMap=0, type=4}
MapTemplate{id=18, name=Địa cung (cao cấp), typeBlockMap=0, type=4}
MapTemplate{id=19, name=Địa cung (thượng cấp), typeBlockMap=0, type=4}
```

## Cơ Chế Chung

### 1. Zone Tùy Chỉnh
```java
public synchronized short createZoneCustom(int GroupId, int FamilyId, int timeClose, 
    long timeStartHoatDong, boolean isHoatDongTime, boolean isTimeHoatDong, 
    int levelMob, int xExp, int xHp, boolean isHoiSinhMob) {
    
    short id = DataCache.getIDZoneCustom();
    Zone zone = new Zone(this, id);
    zone.infoMap.time = timeClose;
    
    if (zone.map.mapID == 46 || zone.map.mapID == 47) {
        zone.infoMap.timeClose = 7200000; // 2 giờ cho ải gia tộc
    } else {
        zone.infoMap.timeClose = timeClose;
    }
    
    zone.FamilyId = FamilyId;
    zone.GroupId = GroupId;
    zone.type = 1;
    zone.map.levelMap = levelMob;
    zone.infoMap.timeStartHoatDong = timeStartHoatDong;
    zone.infoMap.isHoatDongTime = isHoatDongTime;
    zone.infoMap.isTimeHoatDong = isTimeHoatDong;
    zone.createNpc();
    
    if (zone.map.mapID == 46 || zone.map.mapID == 47) {
        // zone.setMobFamilyGate(levelMob); // Tạm thời comment
    } else {
        zone.setMobCusTom(levelMob, xExp, xHp, isHoiSinhMob);
    }
    
    zone.createThread();
    listZoneCusTom.add(zone);
    return id;
}
```

### 2. Kiểm Tra Điều Kiện Chuyển Map
```java
if (waypoint_next.mapNext == 46 || waypoint_next.mapNext == 47) {
    if (!client.mChar.zone.infoMap.isSpamwMobAi) {
        client.session.serivce.ShowMessGold("Chưa thể qua map");
        client.mChar.backXY();
        client.session.serivce.setXYChar();
        return;
    }
    
    // Kiểm tra quái vật
    for (int i = 0; i < client.mChar.zone.vecMob.size(); i++) {
        Mob mob = client.mChar.zone.vecMob.get(i);
        if (!mob.isDie) {
            client.session.serivce.ShowMessGold("Cần hạ hết quái và boss mới có thể chuyển map");
            client.mChar.backXY();
            client.session.serivce.setXYChar();
            return;
        }
    }
    
    // Kiểm tra gia tộc
    FamilyTemplate giaToc = Family.gI().getGiaToc(client.mChar);
    if (giaToc != null) {
        int idMapNext = waypoint_next.mapNext;
        short idZone = giaToc.MapAi.getOrDefault(idMapNext, (short) 0);
        b = Map.maps[waypoint_next.mapNext].addCharInMapCustom(client, idZone);
    } else {
        client.session.serivce.ShowMessGold("Bạn chưa có gia tộc [3]");
    }
}
```

## Phần Thưởng & Tích Lũy

### 1. Điểm Chuyên Cần & Cống Hiến
- **Chuyên cần:** +1 điểm mỗi lần hoàn thành ải
- **Cống hiến gia tộc:** +2 điểm mỗi lần hoàn thành
- **EXP gia tộc:** +20 điểm mỗi lần hoàn thành

### 2. Vật Phẩm Đặc Biệt
- **Cấm thuật:** Đá chế tạo (ID: 354, 562, 564, 566)
- **Ải gia tộc:** Phần thưởng theo cấp độ và thời gian

## Bảo Mật & Kiểm Soát

### 1. Giới Hạn Tham Gia
- Số lần tham gia mỗi ngày
- Kiểm tra quyền hạn (tộc trưởng/tộc phó)
- Xác thực thành viên gia tộc

### 2. Chống Lạm Dụng
- Kiểm tra vị trí người chơi
- Xác thực quái vật đã bị tiêu diệt
- Giới hạn thời gian hoạt động

## Tối Ưu Hóa & Hiệu Suất

### 1. Quản Lý Zone
- Tự động dọn dẹp zone hết hạn
- Tái sử dụng ID zone
- Kiểm soát số lượng zone đồng thời

### 2. Đồng Bộ Hóa
- Cập nhật thời gian thực
- Thông báo zone cho tất cả thành viên
- Xử lý đồng thời nhiều người chơi

## Kết Luận

Hệ thống phụ bản đặc biệt cung cấp trải nghiệm chơi game đa dạng với các thử thách khác nhau. Mỗi phụ bản có cơ chế hoạt động riêng biệt, phần thưởng hấp dẫn và yêu cầu hợp tác nhóm. Hệ thống được thiết kế để đảm bảo cân bằng game và ngăn chặn lạm dụng.
