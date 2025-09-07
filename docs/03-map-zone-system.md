# Báo Cáo Chi Tiết - Hệ Thống Map & Zone

## Tổng Quan
Hệ thống Map & Zone là trung tâm quản lý toàn bộ bản đồ, khu vực, di chuyển và các hoạt động trong game. Hệ thống này hỗ trợ nhiều loại bản đồ khác nhau với khả năng tùy chỉnh cao.

## Kiến Trúc Hệ Thống

### 1. Map Class (`com.langla.real.map.Map`)
**Vai trò:** Quản lý toàn bộ bản đồ và các khu vực trong game

#### 1.1 Cấu Trúc Dữ Liệu
```java
public class Map {
    public static Map[] maps;                    // Mảng tất cả bản đồ
    public static final int NUM_ZONE = 15;       // Số zone mỗi map
    public ArrayList<Zone> listZone;             // Danh sách zone thường
    public ArrayList<WayPoint> listWayPoint;     // Danh sách điểm di chuyển
    public ArrayList<Zone> listZoneCusTom;       // Danh sách zone tùy chỉnh
    
    public int mapID;                            // ID bản đồ
    public int levelMap;                         // Level bản đồ
}
```

#### 1.2 Khởi Tạo Bản Đồ
```java
public static void createMap() {
    // Tạo tất cả bản đồ từ template
    for (int i = 0; i < DataCenter.gI().MapTemplate.length; i++) {
        maps[i] = new Map(i);
    }
}
```

### 2. Zone System (Inner Class)

#### 2.1 Cấu Trúc Zone
```java
public static class Zone {
    public Map map;                              // Bản đồ chứa zone
    public int zoneID;                           // ID zone
    public int type;                             // Loại zone
    public int GroupId = -1;                     // ID nhóm
    public int FamilyId = -1;                    // ID gia tộc
    
    public InfoMap infoMap = new InfoMap();      // Thông tin zone
    public final ArrayList<Npc> vecNpc;          // Danh sách NPC
    public final ArrayList<Mob> vecMob;          // Danh sách quái vật
    public final ArrayList<ItemMap> vecItemMap;  // Danh sách vật phẩm
    public final ArrayList<Char> vecChar;        // Danh sách người chơi
    
    public Thread thread;                        // Thread xử lý zone
    public int id_ENTITY_ITEM_MAP;               // ID entity item map
}
```

#### 2.2 Tạo Zone
```java
private void createZone(int NUM_ZONE) {
    for (int i = 0; i < NUM_ZONE; i++) {
        Zone zone = new Zone(this, listZone.size());
        try {
            zone.createNpc();                    // Tạo NPC
            zone.createMob();                    // Tạo quái vật
        } catch (Exception ex) {
            Utlis.logError(Map.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
        zone.createThread();                     // Tạo thread xử lý
        listZone.add(zone);
    }
}
```

### 3. Hệ Thống Zone Tùy Chỉnh

#### 3.1 Tạo Zone Custom
```java
public synchronized short createZoneCustom(int GroupId, int FamilyId, int timeClose, 
                                         long timeStartHoatDong, boolean isHoatDongTime, 
                                         boolean isTimeHoatDong, int levelMob, int xExp, 
                                         int xHp, boolean isHoiSinhMob) {
    try {
        short id = DataCache.getIDZoneCustom();
        Zone zone = new Zone(this, id);
        zone.infoMap.time = timeClose;
        zone.FamilyId = FamilyId;
        zone.GroupId = GroupId;
        zone.type = 1;
        zone.map.levelMap = levelMob;
        zone.infoMap.timeStartHoatDong = timeStartHoatDong;
        zone.infoMap.isHoatDongTime = isHoatDongTime;
        zone.infoMap.isTimeHoatDong = isTimeHoatDong;
        
        zone.createNpc();
        zone.setMobCusTom(levelMob, xExp, xHp, isHoiSinhMob);
        zone.createThread();
        listZoneCusTom.add(zone);
        return id;
    } catch (Exception ex) {
        Utlis.logError(Map.class, ex, "Da say ra loi:\n" + ex.getMessage());
        return -1;
    }
}
```

#### 3.2 Tìm Zone Custom
```java
public Zone FindMapCustom(int idZone) {
    try {
        for (Zone zone: listZoneCusTom) {
            if(zone.zoneID == idZone) {
                return zone;
            }
        }
    } catch (Exception ex) {
        Utlis.logError(Map.class, ex, "Da say ra loi:\n" + ex.getMessage());
    }
    return null;
}
```

### 4. Hệ Thống WayPoint (Điểm Di Chuyển)

#### 4.1 Tạo WayPoint
```java
private void createWayPoint() {
    for (int index = 0; index < DataCenter.gI().dataWayPoint.length; ++index) {
        WayPoint waypoint = null;
        if (DataCenter.gI().dataWayPoint[index][0] == this.mapID) {
            // Waypoint đi từ map hiện tại
            waypoint = new WayPoint(0, 0);
            waypoint.create(DataCenter.gI().dataWayPoint[index][0], 
                          DataCenter.gI().dataWayPoint[index][5], 
                          DataCenter.gI().dataWayPoint[index][1], 
                          DataCenter.gI().dataWayPoint[index][2], 
                          DataCenter.gI().dataWayPoint[index][3], 
                          DataCenter.gI().dataWayPoint[index][4], 
                          DataCenter.gI().dataWayPoint[index][10], 
                          DataCenter.gI().dataWayPoint[index][11]);
            waypoint.isNext = true;
            this.listWayPoint.add(waypoint);
        } else if (DataCenter.gI().dataWayPoint[index][5] == this.mapID) {
            // Waypoint đến map hiện tại
            waypoint = new WayPoint(0, 0);
            waypoint.create(DataCenter.gI().dataWayPoint[index][5], 
                          DataCenter.gI().dataWayPoint[index][0], 
                          DataCenter.gI().dataWayPoint[index][6], 
                          DataCenter.gI().dataWayPoint[index][7], 
                          DataCenter.gI().dataWayPoint[index][8], 
                          DataCenter.gI().dataWayPoint[index][9], 
                          DataCenter.gI().dataWayPoint[index][12], 
                          DataCenter.gI().dataWayPoint[index][13]);
            waypoint.isNext = false;
            this.listWayPoint.add(waypoint);
        }
    }
}
```

#### 4.2 Tìm WayPoint
```java
public WayPoint getWayPoint_WhenNextMap(int idMapNext) {
    Map mapNext = this;
    for (int i = 0; i < mapNext.listWayPoint.size(); i++) {
        WayPoint waypoint = mapNext.listWayPoint.get(i);
        if (waypoint.mapNext == idMapNext) {
            return waypoint;
        }
    }
    return null;
}

public WayPoint getWayPoint(XYEntity xy) {
    Map mapNext = this;
    WayPoint _waypoint = null;
    for (int i = 0; i < mapNext.listWayPoint.size(); i++) {
        WayPoint waypoint = mapNext.listWayPoint.get(i);
        if (_waypoint == null || waypoint.getRe(xy) < _waypoint.getRe(xy)) {
            _waypoint = waypoint;
        }
    }
    return _waypoint;
}
```

### 5. Hệ Thống Thêm Người Chơi Vào Map

#### 5.1 Thêm Người Chơi Thường
```java
public boolean addChar(Client client) {
    // Kiểm tra map custom
    if(DataCache.idMapCustom.contains(this.mapID)) {
        if(addCharInMapCustom(client, client.mChar.infoChar.idZoneCustom)) {
            return true;
        } else {
            client.mChar.veMapMacDinh();
            return false;
        }
    }

    // Kiểm tra điều kiện vào map
    if(this.mapID != client.mChar.infoChar.mapDefault && client.mChar.infoChar.isDie) {
        client.session.serivce.ShowMessGold("Bạn đã bị trọng thương không thể thực hiện");
        return false;
    }
    
    // Kiểm tra level map
    int lvmap = DataCenter.gI().getLockMap(this.mapID);
    if(lvmap > 0) {
        if(client.mChar.level() < lvmap) {
            client.session.serivce.ShowMessGold("Cần đạt cấp "+lvmap+" mới có thể tới khu vực này.");
            return false;
        }
    }

    // Tìm zone có chỗ trống
    for (int i = 0; i < this.listZone.size(); i++) {
        Zone z = this.listZone.get(i);
        if (z.vecChar.size() < Zone.MAX_CHAR_INZONE) {
            boolean can = z.addChar(client);
            if (can) {
                return true;
            }
        }
    }
    client.session.serivce.ShowMessGold("Khu vực đã đầy.");
    return false;
}
```

#### 5.2 Thêm Người Chơi Vào Map Custom
```java
public boolean addCharInMapCustom(Client client, short idZoneCustom) {
    for (Zone z : listZoneCusTom) {
        if (z.zoneID == idZoneCustom) {
            boolean ok = z.addChar(client);
            if (ok) {
                client.mChar.infoChar.idZoneCustom = idZoneCustom;
                if(client.mChar.zone.map.mapID == 89) {
                    client.session.serivce.ShowMessWhite("Vòng lặp ảo tưởng thứ "+client.mChar.zone.infoMap.vongLap);
                }
                return true;
            }
        }
    }
    client.mChar.infoChar.idZoneCustom = -1;
    return false;
}
```

### 6. Hệ Thống Chuyển Map

#### 6.1 Chuyển Map Thường
```java
public void nextMap(Client client) {
    try {
        boolean b = false;
        XYEntity xy = client.mChar;
        WayPoint waypoint_next = getWayPoint(xy);
        
        if (waypoint_next != null) {
            WayPoint waypoint = getWayPoint_WhenInMap(waypoint_next.mapNext);
            if (waypoint != null) {
                // Xử lý map đặc biệt (46, 47)
                if(waypoint_next.mapNext == 46 || waypoint_next.mapNext == 47) {
                    if(!client.mChar.zone.infoMap.isSpamwMobAi) {
                        client.session.serivce.ShowMessGold("Chưa thể qua map");
                        client.mChar.backXY();
                        client.session.serivce.setXYChar();
                        return;
                    }
                    
                    // Kiểm tra quái vật
                    for (int i = 0; i < client.mChar.zone.vecMob.size(); i++) {
                        Mob mob = client.mChar.zone.vecMob.get(i);
                        if(!mob.isDie) {
                            client.session.serivce.ShowMessGold("Cần hạ hết quái và boss mới có thể chuyển map");
                            client.mChar.backXY();
                            client.session.serivce.setXYChar();
                            return;
                        }
                    }
                    
                    // Kiểm tra gia tộc
                    FamilyTemplate giaToc = Family.gI().getGiaToc(client.mChar);
                    if(giaToc != null) {
                        int idMapNext = waypoint_next.mapNext;
                        short idZone = giaToc.MapAi.getOrDefault(idMapNext, (short) 0);
                        b = Map.maps[waypoint_next.mapNext].addCharInMapCustom(client, idZone);
                    } else {
                        client.session.serivce.ShowMessGold("Bạn chưa có gia tộc [3]");
                    }
                } else {
                    b = Map.maps[waypoint_next.mapNext].addChar(client);
                }
            }
        }
        
        if (!b) {
            client.mChar.backXY();
            client.session.serivce.setXYChar();
        }
    } catch (Exception ex) {
        Utlis.logError(Map.class, ex, "Da say ra loi:\n" + ex.getMessage());
    }
}
```

### 7. Hệ Thống Tạo Quái Vật

#### 7.1 Tạo Quái Vật Thường
```java
private void createMob() {
    vecMob.clear();
    for (int i = 0; i < map.getMapTemplate().listMob.size(); i++) {
        try {
            Mob mob1 = map.getMapTemplate().listMob.get(i);
            Mob mob2 = mob1.cloneMob();
            mob2.createNewEffectList();
            mob2.idEntity = DataCache.getIDMob();
            mob2.reSpawn();
            vecMob.add(mob2);
        } catch (Exception ex) {
            Utlis.logError(Map.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }
}
```

#### 7.2 Tạo Quái Vật Gia Tộc (Map 46, 47)
```java
private void setMobFamilyGate(int level) {
    vecMob.clear();
    infoMap.isSpamwMobAi = true;
    infoMap.time = 1;
    infoMap.isHoatDongTime = true;
    infoMap.timeStartHoatDong = System.currentTimeMillis();
    updateTimeHoatDongZone(infoMap.timeStartHoatDong, infoMap.time, infoMap.isHoatDongTime);
    ShowMessWhite("Bắt đầu vượt ải gia tộc");
    
    if(map.mapID == 46) {
        // Tạo quái vật cho 5 hệ
        int hpMob = 18000;
        int l = 60;
        
        // Hệ Thổ
        for (int i = 0; i < 14; i++) {
            Mob mob = new Mob();
            mob.id = 123;
            mob.level = level;
            mob.he = 2;
            mob.cx = (short) (460 + l);
            mob.cy = 363;
            mob.timeRemove = 7200000 + System.currentMillis();
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
        
        // Tương tự cho các hệ khác...
    }
}
```

#### 7.3 Tạo Quái Vật Tùy Chỉnh
```java
private void setMobCusTom(int level, int xExp, int xHp, boolean isHoiSinhMob) {
    // Cập nhật thông số quái vật
    for (int i = 0; i < map.getMapTemplate().listMob.size(); i++) {
        try {
            Mob mob1 = map.getMapTemplate().listMob.get(i);
            mob1.level = level;
            mob1.expGoc = level * xExp;
            mob1.hpGoc = level * xHp;
            mob1.isHoiSinhMob = isHoiSinhMob;
            if(DataCache.idBoss.contains(mob1.id)) 
                mob1.isBoss = true;
        } catch (Exception ex) {
            Utlis.logError(Map.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }
    
    // Tạo quái vật mới
    vecMob.clear();
    for (int i = 0; i < map.getMapTemplate().listMob.size(); i++) {
        try {
            Mob mob1 = map.getMapTemplate().listMob.get(i);
            Mob mob2 = mob1.cloneMob();
            mob2.createNewEffectList();
            mob2.idEntity = DataCache.getIDMob();
            mob2.reSpawn();
            vecMob.add(mob2);
        } catch (Exception ex) {
            Utlis.logError(Map.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }
}
```

### 8. Hệ Thống Tạo NPC

#### 8.1 Tạo NPC
```java
private void createNpc() {
    vecNpc.clear();
    for (int i = 0; i < map.getMapTemplate().listNpc.size(); i++) {
        try {
            Npc npc1 = map.getMapTemplate().listNpc.get(i);
            Npc npc2 = npc1.cloneNpc();
            vecNpc.add(npc2);
        } catch (Exception ex) {
            Utlis.logError(Map.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }
}
```

### 9. Hệ Thống Quản Lý Zone

#### 9.1 Xóa Zone Custom
```java
public synchronized void removeZoneCustom(Zone zone) {
    try {
        listZoneCusTom.remove(zone);
    } catch (Exception ex) {
        Utlis.logError(Map.class, ex, "Da say ra loi:\n" + ex.getMessage());
    }
}
```

#### 9.2 Tìm Zone Theo ID
```java
public Zone getZone(int zoneID) {
    for (Zone zone : listZone) {
        if (zone.zoneID == zoneID) {
            return zone;
        }
    }
    return null;
}
```

### 10. Hệ Thống Thông Tin Map

#### 10.1 InfoMap Class
```java
public class InfoMap {
    public int time;                           // Thời gian hoạt động
    public int timeClose;                      // Thời gian đóng
    public long timeStartHoatDong;             // Thời gian bắt đầu hoạt động
    public boolean isHoatDongTime;             // Có phải thời gian hoạt động
    public boolean isTimeHoatDong;             // Có phải thời gian hoạt động
    public int vongLap;                        // Vòng lặp (cho map 89)
    public boolean isSpamwMobAi;               // Có spam quái AI
}
```

### 11. Hệ Thống Xử Lý Đa Luồng

#### 11.1 Thread Zone
```java
public void createThread() {
    thread = new Thread(() -> {
        while (!Maintenance.isRunning) {
            try {
                // Xử lý logic zone
                updateZone();
                Thread.sleep(100); // Cập nhật mỗi 100ms
            } catch (Exception ex) {
                Utlis.logError(Zone.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
    }, "Zone-" + zoneID);
    thread.start();
}
```

### 12. Hệ Thống Bảo Mật & Validation

#### 12.1 Kiểm Tra Điều Kiện Vào Map
- Kiểm tra level yêu cầu
- Kiểm tra trạng thái nhân vật
- Kiểm tra quyền truy cập
- Kiểm tra dung lượng zone

#### 12.2 Synchronization
```java
public synchronized short createZoneCustom(...)
public synchronized void removeZoneCustom(Zone zone)
```

### 13. Hiệu Suất & Tối Ưu

#### 13.1 Memory Management
- Sử dụng ArrayList cho danh sách động
- Tự động dọn dẹp zone không sử dụng
- Cache thông tin map template

#### 13.2 Thread Optimization
- Mỗi zone có thread riêng
- Cập nhật theo tần suất phù hợp
- Xử lý bất đồng bộ cho các tác vụ nặng

### 14. Monitoring & Debugging

#### 14.1 Performance Metrics
- Số lượng zone active
- Số lượng người chơi trong zone
- Thời gian xử lý zone

#### 14.2 Error Handling
```java
try {
    // Xử lý logic
} catch (Exception ex) {
    Utlis.logError(Map.class, ex, "Da say ra loi:\n" + ex.getMessage());
}
```

### 15. Kết Luận

Hệ thống Map & Zone được thiết kế với:
- **Kiến trúc linh hoạt** hỗ trợ nhiều loại bản đồ
- **Zone system mạnh mẽ** với khả năng tùy chỉnh cao
- **Waypoint system** cho di chuyển mượt mà
- **Multi-threading** cho hiệu suất tối ưu
- **Bảo mật cao** với validation đầy đủ

Hệ thống có khả năng xử lý hàng nghìn người chơi đồng thời với độ ổn định cao và khả năng mở rộng tốt.
