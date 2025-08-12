# Báo Cáo Chi Tiết - Hệ Thống EXP Vật Phẩm (Item)

## Tổng Quan
Hệ thống EXP vật phẩm cho phép một số trang bị/đồ đặc biệt tăng trưởng theo quá trình sử dụng (đánh boss/mob), thông qua cơ chế tăng option theo mốc (giả lập exp). Điển hình là cơ chế "tu luyện bí kíp" và nâng cấp bùa.

## 1. Tu Luyện Bí Kíp (Trang Bị Slot 11)
```java
// Khi hạ mob có levelBoss 1/2 hoặc boss, nếu đang đeo item slot 11
if (mob.levelBoss == 1 || mob.levelBoss == 2) {
    if (client.mChar.arrItemBody[11] != null) {
        if (client.mChar.arrItemBody[11].getChiSo(1, client, 128) < client.mChar.arrItemBody[11].getChiSo(2, client, 128)) {
            int plus = 1;
            int valueEff = client.mChar.getValueEff(47); // hiệu ứng hỗ trợ tăng nhanh
            if (valueEff == 200) plus = 2; else if (valueEff == 300) plus = 3;
            client.mChar.arrItemBody[11].plusOption(128, 1, plus); // tăng tiến độ (như exp)
            client.mChar.updateItemBody(client.mChar.arrItemBody[11]);
        }
    }
}
```
- **Ý nghĩa:** Option `128` có cặp min/max, mỗi lần hạ boss tăng min lên 1-3 dựa theo hiệu ứng, mô phỏng EXP item.
- **Điều kiện:** Chỉ tăng khi chưa đạt max (`getChiSo(1, ..., 128) < getChiSo(2, ..., 128)`).
- **Hiệu ứng hỗ trợ:** `valueEff` 200/300 làm tăng tốc độ tăng tiến (x2/x3).

## 2. EXP Phân Thân (Liên Quan Trang Bị/Hệ)
- Trạng thái phân thân tăng `expPhanThan` khi hạ mob; đây là thanh tiến độ độc lập tương tự exp item cho một thực thể phụ.

## 3. EXP Gián Tiếp Qua Nâng Cấp Bùa (Bùa Nổ / Siêu Bùa)
- Dùng tinh thạch (id 160) để tăng các chỉ số của bùa theo điểm; mỗi 10 tinh thạch ~ 1 điểm (giống exp).
- Khi đạt trần: bùa đặc biệt (id 811) nhận thêm nhiều option bonus.
- Cách thức này mô phỏng exp lên cấp của bùa thông qua tài nguyên tiêu hao.

## 4. Level Vật Phẩm & Tăng Trưởng Theo Level (Template)
```java
// Khi sinh item theo level template, tự thêm các option theo cấp
public static void setOptionsVuKhi(Item item, int level) {
    item.addItemOption(new ItemOption(2, (50 * level / 10), 50 + (50 * level / 10)));
    item.addItemOption(new ItemOption(3, level * 2, (level * 2) + 10));
    item.addItemOption(new ItemOption(20, level * 2, (level * 2) + 10));
    // ... cộng các option theo mốc 10/20/30/40/50/60
}
```
- Đây là tăng trưởng tĩnh theo cấp yêu cầu của item (`levelNeed`) chứ không phải exp động; nhưng kết hợp với cơ chế tu luyện/nâng cấp tạo thành hệ thống tăng trưởng toàn diện.

## 5. Quy Ước Option Dạng Tiến Độ
- Các option như `128` có 3 tham số: loại (id), giá trị hiện tại (min), giá trị tối đa (max).
- So sánh `getChiSo(1, ..., optId)` và `getChiSo(2, ..., optId)` để xác định còn tăng được không.
- Tăng tiến độ bằng `plusOption(optId, idx, amount)`.

## 6. Cân Bằng & Chống Lạm Dụng
- Chỉ tăng khi tiêu diệt mob đặc thù (boss, levelBoss 1/2), hạn chế farm nhanh.
- Yêu cầu trang bị slot cụ thể (slot 11) để nhận tiến độ, tránh tăng tràn lan.
- Giới hạn trần (max) mỗi option để chặn vượt ngưỡng.
- Có thể kết hợp log sự kiện tăng tiến độ item để theo dõi.

## 7. Gợi Ý UI/UX
- Thanh tiến độ hiển thị min/max của option `128`.
- Tooltip hiển thị phần trăm hoàn thành và nguồn tăng nhanh (hiệu ứng 47).
- Thông báo khi đạt mốc/hoàn tất.
