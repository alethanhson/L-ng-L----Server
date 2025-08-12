# Báo Cáo Chi Tiết - Hệ Thống Nâng Cấp Trang Bị

## Tổng Quan
Hệ thống nâng cấp trang bị cho phép tăng cường các chỉ số của vật phẩm thông qua tiêu hao tài nguyên (bạc khóa, ngọc, tinh thạch, bùa nổ). Cơ chế gồm đổi bùa, nâng cấp bùa và nâng cấp trang bị chính.

## Thành Phần Chính (`com.langla.real.trangbi.UpgradeTrangBi`)

### 1. Đổi Bùa Nổ (`DoiBuaNo`)
```java
public void DoiBuaNo(Char character, byte typeItem, short index) {
    int bac = 1_000_000; int vang = 800; // chi phí cơ bản
    Item itemUpgrade = character.getItemByType(typeItem, index);
    if (itemUpgrade == null || itemUpgrade.getItemTemplate().type != 13) { /* lỗi */ return; }
    if (itemUpgrade.id == 811) { /* đã tối đa */ return; }
    if (itemUpgrade.id == 602) vang = 3000; // siêu bùa
    // kiểm tra option đã max chưa
    for (ItemOption opt : itemUpgrade.getItemOption()) {
        if (opt.a[1] < opt.a[2]) { /* chưa max */ return; }
    }
    Item itemAdd = new Item(602); // bùa nổ mới
    if (itemUpgrade.id == 602) itemAdd.id = 811; // siêu bùa nổ
    itemAdd.he = character.infoChar.idhe; // set hệ
    // Gán option theo hệ (he1..he5) với chuỗi option template
    // ...
    // Trừ tiền, thêm itemAdd vào túi, cập nhật lại thông tin
}
```
- **Mục đích:** Nâng cấp bùa nổ lên phiên bản cao hơn (id 602 → 811) khi đã đạt mốc tối đa.
- **Chi phí:** Bạc, vàng; kiểm tra slot trống.
- **Option theo hệ:** Áp dụng bộ option tương ứng hệ nhân vật.

### 2. Nâng Cấp Bùa Nổ (`NangCapBuaNo`)
```java
public void NangCapBuaNo(Char character, byte typeNC, byte typeItem, short index, Item[] item) {
    Item itemUpgrade = character.getItemByType(typeItem, index);
    int max = itemUpgrade.getChiSoI(typeNC, 2, character.client);
    int min = itemUpgrade.getChiSoI(typeNC, 1, character.client);
    if (typeNC == 0 && min >= max) { /* đã tối đa */ return; }
    // Tính số tinh thạch (id 160) nạp vào
    int tinhThach = 0; for (Item it : item) { /* validate id == 160, cộng số lượng */ }
    if (tinhThach < 10) { /* tối thiểu 10 viên */ }
    int diem = tinhThach / 10;
    int tinhdiem = min + diem;
    if (tinhdiem >= max) { // chạm trần thì bonus đặc biệt với id 811
        if (itemUpgrade.id == 811) {
            itemUpgrade.plusOptionByIndex(2, 1, 25);
            itemUpgrade.plusOptionByIndex(3, 1, 25);
            itemUpgrade.plusOptionByIndex(4, 1, 25);
            itemUpgrade.plusOptionByIndex(5, 1, 1);
            itemUpgrade.plusOptionByIndex(6, 1, 2);
        }
    }
    itemUpgrade.plusOptionByIndex(typeNC, 1, diem);
    // Gửi message (106) cập nhật, trừ tinh thạch, refresh info
}
```
- **Nguyên liệu:** Tinh thạch `id = 160`.
- **Cơ chế:** Mỗi 10 tinh thạch tăng 1 điểm chỉ số; chạm max có thể cộng thêm các option bonus cho siêu bùa.

### 3. Nâng Cấp Trang Bị Chính (`handle`)
```java
// Chi phí dựa theo type và levelNeed/10
if (itemUpgrade.getItemTemplate().type == 2 || type == 7 || type == 8) {
    bacUpgrade = 15_000_000;
    ngocUpgrade = itemUpgrade.getItemTemplate().levelNeed / 10 * 100;
    if (levelNeed/10 == 5) bacUpgrade = 20_000_000;
    else if (levelNeed/10 == 6) { bacUpgrade = 40_000_000; ngocUpgrade = 700; }
} else {
    bacUpgrade = 25_000_000;
    ngocUpgrade = itemUpgrade.getItemTemplate().levelNeed / 10 * 100;
    if (levelNeed/10 == 5) bacUpgrade = 30_000_000;
    else if (levelNeed/10 == 6) { bacUpgrade = 40_000_000; ngocUpgrade = 700; }
}
// Kiểm tra bạc khóa đủ, kiểm tra ngọc đúng loại (itemUpgrade.ngocUpgrade()) và đủ số lượng
```
- **Tài nguyên:** Bạc khóa và Ngọc nâng cấp (loại ngọc phụ thuộc trang bị qua `ngocUpgrade()`).
- **Ràng buộc:** Kiểm tra item trong túi đúng tham chiếu, đủ số lượng, loại ngọc hợp lệ, đủ bạc khóa, slot trống.
- **Giao tiếp client:** Gửi `Message(106)` với chi tiết item và các index được tiêu hao để client refresh UI.

## Option & Ảnh Hưởng Chỉ Số
- Nâng cấp tăng các chỉ số (qua `plusOption/plusOptionByIndex`) theo từng mảng option.
- Bùa/Siêu bùa có các bộ option theo hệ nhân vật, giúp tối ưu build.

## Lưu Ý Cân Bằng
- Chi phí tăng theo `levelNeed` bậc 10 (40/50/60...).
- Giới hạn điểm tăng mỗi lần theo số tinh thạch để kiểm soát tiến độ.
- Ràng buộc loại ngọc theo trang bị để tránh nâng cấp sai.
- Cần log thao tác nâng cấp để phòng chống exploit.
