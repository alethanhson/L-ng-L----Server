# Báo Cáo Chi Tiết - Hệ Thống EXP & Level

## Tổng Quan
Hệ thống EXP & Level quản lý việc tích lũy kinh nghiệm, lên cấp, chia sẻ EXP tổ đội và các nguồn EXP khác (boss, hoạt động, nhiệm vụ). Cap level mặc định tối đa là 100.

## Thành Phần Chính

### 1. Cấu Trúc EXP Nhân Vật (`Char.level()`, `Char.addExp()`)
```java
public int level() {
    long expRemain = this.infoChar.exp;
    int level;
    for (level = 0; level < DataCenter.gI().exps.length && expRemain >= DataCenter.gI().exps[level]; ++level) {
        expRemain -= DataCenter.gI().exps[level];
    }
    return level;
}

public void addExp(long exp) {
    if (infoChar.isKhoaCap) return;
    exp *= 5; // hệ số EXP toàn cục
    int before = this.level();
    this.infoChar.exp += exp;
    int after = this.level();
    if (after >= 100) {
        after = 100;
        this.infoChar.exp = DataCenter.gI().GetExpFormLevel(100);
    }
    infoChar.level = after;
    if (before != after) {
        upLevel(before, after);
    }
    msgAddExp();
}
```
- **Công thức level:** Duyệt dãy `DataCenter.exps[]` để trừ dần EXP và suy ra cấp hiện tại.
- **Giới hạn:** Level tối đa 100; khi đạt, EXP được set về tổng EXP của level 100.
- **Hệ số EXP:** Nhân 5 lần khi cộng EXP (có thể là cấu hình open beta/test).
- **Sự kiện lên cấp:** Gọi `upLevel()` và đồng bộ client `msgAddExp()`.

### 2. Cộng Hưởng EXP & Buff
```java
public int getPlusExp() {
    return this.infoChar.exp_plus + infoChar.exp_rank; // buff EXP và rank EXP
}
```
- EXP nhận từ mob được tăng theo `exp_plus` và `exp_rank` nếu có hiệu ứng/buff.

### 3. EXP Từ Mob/Boss (PvE)
```java
long expp = mob.exp;
if (client.mChar.getPlusExp() > 0) expp += expp * client.mChar.getPlusExp() / 100;
if (client.mChar.infoChar.groupId != -1) {
    long expTuSkillBuff = 0;
    for (Char aChar : getVecChar()) {
        if (aChar != null && aChar.id != client.mChar.id && aChar.infoChar.groupId == client.mChar.infoChar.groupId) {
            aChar.client.mChar.addExp((long) (mob.exp * 0.30));     // chia EXP cho tổ đội: 30%
            if (Math.abs(aChar.cx - client.mChar.cx) < 300 && Math.abs(aChar.cy - client.mChar.cy) < 300)
                expTuSkillBuff += aChar.client.mChar.getChiSoFormSkill(104); // buff quanh phạm vi
        }
    }
    if (expTuSkillBuff > 0) expp += expp * expTuSkillBuff / 100;
}
client.mChar.addExp(expp);
```
- **EXP mob:** `mob.exp` (ngẫu nhiên theo HP/hệ/level boss, xem `Mob.reSpawn()`).
- **Buff EXP:** Cộng thêm theo % `exp_plus`/`exp_rank` và kỹ năng tổ đội (ID 104) nếu trong phạm vi 300px.
- **Chia EXP tổ đội:** Thành viên cùng `groupId` nhận 30% EXP mob.

### 4. EXP Mốc Đặc Biệt Của Mob (`Mob.reSpawn()`)
```java
exp = expGoc;
levelBoss = 0;
if (isBoss) levelBoss = 7;
else {
    int num = Utlis.nextInt(10000);
    if (num < 1) { hp *= 100; exp *= 100; levelBoss = 2; }
    else if (num < 5) { hp *= 10; exp *= 10; levelBoss = 1; }
}
if (exp > 2100000000) exp = 2100000000;
```
- Mob có thể ngẫu nhiên nâng cấp thành dạng mạnh hơn (levelBoss 1/2) kèm EXP cao hơn.
- Boss chuẩn đặt `levelBoss = 7`.

### 5. Nguồn EXP Khác
- **Nhiệm vụ (Task):** EXP thưởng từ nhiệm vụ (xem `task/TaskHandler`, không trích đầy đủ tại đây).
- **Hoạt động/Hoạt động đặc biệt:** Có thể cộng EXP qua các event/map custom.
- **EXP Phân Thân:** Khi người chơi ở trạng thái phân thân, giết mob cộng `expPhanThan` và tự tăng level phân thân theo mốc.
```java
if (client.mChar.infoChar.isPhanThan) {
    int exp_plus = (mob.levelBoss == 1 ? 5 : mob.levelBoss == 2 ? 10 : mob.isBoss ? 100 : 1);
    client.mChar.infoChar.expPhanThan += exp_plus;
    int expMax = 5000000 * (client.mChar.infoChar.levelPhanThan + 1);
    if (client.mChar.infoChar.expPhanThan >= expMax) client.mChar.infoChar.levelPhanThan++;
}
```

### 6. EXP Gia Tộc (Family)
```java
public int level = 1;   public int exp = 0;
public int getMaxExp(){ return level * 10000; }
public void PlusExp(int exp){ this.exp += exp; if (exp >= getMaxExp()) { level++; maxMember += 5; } }
```
- Gia tộc có hệ thống EXP/Level riêng, tăng level gia tộc tăng `maxMember`.

### 7. Dữ Liệu Cấu Hình EXP (`DataCenter.exps[]`)
- Mảng EXP theo từng cấp (cộng dồn). Hàm `GetExpFormLevel(level)` trả về tổng EXP tương ứng.
- Được dùng để giới hạn và tính toán khi lên cấp/capping level 100.

## Lưu Ý Cân Bằng
- Điều chỉnh hệ số `exp *= 5` theo môi trường (test/live).
- Kiểm soát chia EXP tổ đội để tránh lạm dụng.
- Giới hạn EXP mob tối đa để tránh tràn số (`2_100_000_000`).
- Kiểm tra và reset buff EXP theo thời gian/hoạt động.
