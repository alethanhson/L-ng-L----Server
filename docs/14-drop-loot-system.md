# Báo Cáo Chi Tiết - Hệ Thống Rớt Đồ (Loot/Drop)

## Tổng Quan
Hệ thống rớt đồ quyết định vật phẩm rơi ra khi quái/boss bị tiêu diệt. Cơ chế phân biệt rõ giữa mob thường, boss và các map hoạt động đặc biệt (ví dụ: Cấm Thuật map 89). Vật phẩm rơi ra dưới dạng `ItemMap` kèm metadata (người ưu tiên nhặt, thời điểm tạo, vị trí trên map).

## Dòng Chảy Chính
```java
// Khi mob chết
private void setMobDie(Client client, Zone zone, Mob mob) {
    // ... cộng EXP ...
    if (mob.getMobTemplate().type == 10 || mob.getMobTemplate().type == 8) return;
    dropItem(client, mob);             // RỚT ĐỒ
    handleMapCustom(client, zone, mob);
    handleItemBody(client, mob);
    if (mob.isBoss) { /* cập nhật BossRunTime và remove */ }
}
```

## Tạo Vật Phẩm Rơi (`dropItem`)
```java
public void dropItem(Client client, Mob mob) {
    if (mob.isBoss) {
        // 1) Đá khảm (406..413, 826, 827) - số lượng ngẫu nhiên theo level boss
        int leg = Utlis.nextInt(1, mob.level/2);
        for (int i = 0; i < leg; i++) {
            int x = Utlis.nextInt(50);
            List<Integer> listItem = Arrays.asList(406,407,408,409,410,411,412,413,826,827);
            Item item = new Item(UTPKoolVN.getRandomList(listItem));
            ItemMap im = ItemMap.createItemMap(item, mob.cx + (i % 2 == 0 ? x : -x), mob.cy);
            im.idEntity = DataCache.getIDItemMap(); im.idChar = client.mChar.id;
            createItemMap(im, mob);
        }
        
        // 2) Lệnh bài/điểm Hokage (174,175,179,216,217,218,248,278,302,315)
        leg = Utlis.nextInt(1, mob.level/2);
        for (int i = 0; i < leg; i++) {
            int x = Utlis.nextInt(50);
            List<Integer> listItem = Arrays.asList(174,175,179,216,217,218,248,278,302,315);
            Item item = new Item(UTPKoolVN.getRandomList(listItem));
            ItemMap im = ItemMap.createItemMap(item, mob.cx + (i % 2 == 0 ? x : -x), mob.cy);
            im.idEntity = DataCache.getIDItemMap(); im.idChar = client.mChar.id;
            createItemMap(im, mob);
        }
        
        // 3) Trang bị Hokage theo mốc level (tỷ lệ 30% đang để cao cho open/test)
        if (Utlis.nextInt(100) < 30) {
            List<Integer> listItem = // chọn theo mốc level 20/30/40/50
            // ... sinh item id và set option Hokage (vũ khí/trang bị)
            ItemMap im = ItemMap.createItemMap(item, mob.cx + x, mob.cy);
            im.idEntity = DataCache.getIDItemMap(); im.idChar = client.mChar.id;
            createItemMap(im, mob);
        }
    } else {
        // Mob thường
        if (Math.abs(client.mChar.level() - mob.level) > 10) return; // chênh lệch cấp quá lớn: không rơi
        int tile = Utlis.nextInt(100);
        if (client.mChar.zone.map.mapID == 89) {
            // Cấm Thuật: rơi đá chế tạo 354/562/564/566
            Item item = new Item(UTPKoolVN.getRandomList(Arrays.asList(354, 562, 564, 566)));
            // ... tạo ItemMap và phát xuống zone
        } else {
            if (tile < 10) {
                // 10% rơi đá cấp theo level mob (1/2/3/4)
            } else if (tile < 50) {
                // 40% rơi bạc khóa (id 163), lượng theo level mob
            }
        }
        // item != null => tạo ItemMap, đánh dấu isSystem = true
    }
}
```

## Tạo & Gửi Vật Phẩm Xuống Zone
```java
public void createItemMap(ItemMap itemMap, Mob mob) {
    this.vecItemMap.add(itemMap);
    Writer writer = new Writer();
    writer.writeShort(mob.idEntity); // tham chiếu mob chết
    itemMap.write(writer, -1, -1, this); // ghi idChar, idEntity, XY, dữ liệu item
    sendItemDropFormMob(writer);         // broadcast packet (byte 60)
}
```

## Cấu Trúc `ItemMap`
```java
public class ItemMap extends Entity {
    public int idChar;          // id người được ưu tiên nhặt (-1: ai cũng nhặt)
    public boolean isSystem;    // đánh dấu hệ thống rơi
    public Item item;           // nội dung vật phẩm
    public long timeCreate;     // thời điểm tạo (để TTL/auto clear)
    public static ItemMap createItemMap(Item item, int cx, int cy) { ... }
    public void write(Writer writer, int cx, int cy, Zone zone) { ... }
}
```

## Quy Tắc Theo Ngữ Cảnh
- **Boss:** luôn roll nhiều vật phẩm: đá khảm, lệnh bài, có tỷ lệ trang bị Hokage, có thể cấu hình rơi bạc (đang comment trong mã).
- **Mob thường:** chịu ảnh hưởng chênh lệch cấp (±10), tỷ lệ rơi cơ bản: đá (10%), bạc khóa (40%).
- **Cấm Thuật (map 89):** rơi đá chế tạo đặc thù thay vì bảng tỷ lệ chung.
- **Ải Gia Tộc (46/47):** rơi thưởng qua hoàn thành ải (điểm), quái rơi vật phẩm theo logic map thường.

## Cấu Hình Theo Level/Map
- Phần thưởng Hokage chọn theo mốc `level >= 20/30/40/50` để ra đúng nhóm item id.
- Bạc khóa và đá cấp tăng theo `mob.level`.
- Boss level càng cao thì số lượng cuộn/đá rơi (`leg`) càng nhiều.

## Bảo Mật & Chống Lạm Dụng
- Ưu tiên nhặt (`idChar`) theo người/kẻ kết liễu; có thể mở rộng TTL để chống “cướp loot”.
- Giới hạn chênh lệch cấp để chống farm vượt cấp.
- Đánh dấu `isSystem` cho các rơi hệ thống, dễ kiểm soát và dọn dẹp.

## Gợi Ý Mở Rộng
- Bảng tỷ lệ cấu hình theo JSON (per-map, per-mob, per-event).
- Cơ chế “party loot” chia đều/roll giữa thành viên nhóm.
- TTL tự dọn `ItemMap` sau X giây, phát packet remove.
- Bảng trắng item drop hiếm (announcement khi rơi).
