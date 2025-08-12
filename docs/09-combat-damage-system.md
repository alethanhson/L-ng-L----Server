# Báo Cáo Chi Tiết - Hệ Thống Combat & Damage Calculation

## Tổng Quan
Hệ thống Combat & Damage Calculation là trung tâm xử lý tất cả các tính toán liên quan đến chiến đấu, bao gồm damage, defense, elemental system, critical hits, và các hiệu ứng đặc biệt. Hệ thống này sử dụng các thuật toán phức tạp để tạo ra trải nghiệm chiến đấu cân bằng và hấp dẫn.

## Kiến Trúc Hệ Thống

### 1. InfoTuongKhac System (`com.langla.data.InfoTuongKhac`)
**Vai trò:** Quản lý tất cả các chỉ số chiến đấu của nhân vật

#### 1.1 Cấu Trúc Dữ Liệu
```java
public class InfoTuongKhac {
    // Chỉ số tấn công cơ bản
    public int TanCongCoBan;        // Tấn công cơ bản
    public int TanCong;              // Tấn công tổng
    public int TanCongQuai;          // Tấn công lên quái
    
    // Chỉ số phát huy lực
    public int PhatHuyLucDanhCoban; // Phát huy lực đánh cơ bản
    
    // Chỉ số chính xác và né tránh
    public int ChinhXac;             // Chính xác
    public int BoQuaNeTranh;         // Bỏ qua né tránh
    public int ChiMang;              // Chí mạng
    public int TangTanCongChiMang;   // Tăng tấn công chí mạng
    
    // Chỉ số tương khắc
    public int TangTuongKhac;        // Tăng tương khắc
    public int TangDameTatCa;        // Tăng damage tất cả
    public int TangTanCongLenLoi;    // Tăng tấn công lên Lôi
    public int TangTanCongLenTho;    // Tăng tấn công lên Thổ
    public int TangTanCongLenThuy;   // Tăng tấn công lên Thủy
    public int TangTanCongLenHoa;    // Tăng tấn công lên Hỏa
    public int TangTanCongLenPhong;  // Tăng tấn công lên Phong
    
    // Chỉ số gây hiệu ứng
    public int GaySuyYeu;            // Gây suy yếu
    public int GayTrungDoc;          // Gây trúng độc
    public int GayLamCham;           // Gây làm chậm
    public int GayBong;              // Gây bóng
    public int GayChoang;            // Gây choáng
    
    // Chỉ số kháng tính
    public int BoQuaKhangTinh;       // Bỏ qua kháng tính
    public int KhangLoi;             // Kháng Lôi
    public int KhangTho;             // Kháng Thổ
    public int KhangThuy;            // Kháng Thủy
    public int KhangHoa;             // Kháng Hỏa
    public int KhangPhong;           // Kháng Phong
    public int KhangTatCa;           // Kháng tất cả
    public int GiamSatThuong;        // Giảm sát thương
    
    // Chỉ số phòng thủ
    public int NeTranh;              // Né tránh
    public int PhanDon;              // Phản đòn
    public int GiamTanCongKhiBiChiMang; // Giảm tấn công khi bị chí mạng
    
    // Chỉ số giảm hiệu ứng
    public int GiamSuyYeu;           // Giảm suy yếu
    public int GiamTrungDoc;         // Giảm trúng độc
    public int GiamLamCham;          // Giảm làm chậm
    public int GiamGayBong;          // Giảm gây bóng
    public int GiamGayChoang;        // Giảm gây choáng
    public int GiamTruChiMang;       // Giảm trừ chí mạng
    public int GiamTuongKhac;        // Giảm tương khắc
    
    // Chỉ số đặc biệt
    public int HieuUngNgauNhien;     // Hiệu ứng ngẫu nhiên
    public int satThuongChuyenHp;    // Sát thương chuyển HP
}
```

### 2. Hệ Thống Tính Toán Damage

#### 2.1 Damage Cơ Bản
```java
public int getDame() {
    int dame = TuongKhac.TanCong;           // Damage cơ bản
    dame += getTanCongCoBan();              // Cộng thêm damage cơ bản
    
    // Tính toán phát huy lực
    if (getPhatHuyLucDanhCoBan() > 0) {
        long dameplus = (long) dame * getPhatHuyLucDanhCoBan() / 200;
        dame += dameplus;
    }
    
    return dame;
}

public int getDameMob() {
    return TuongKhac.TanCongQuai;           // Damage lên quái
}
```

#### 2.2 Damage Lên Quái Theo Hệ
```java
public int getDameMob(Mob mob) {
    int dame = getDameMob();                 // Damage cơ bản lên quái
    
    // Tăng damage theo hệ của quái
    switch (mob.he) {
        case 1: // Lôi
            dame += this.getTangTanCongLenLoi();
            break;
        case 2: // Thổ
            dame += this.getTangTanCongLenTho();
            break;
        case 3: // Thủy
            dame += this.getTangTanCongLenThuy();
            break;
        case 4: // Hỏa
            dame += this.getTangTanCongLenHoa();
            break;
        case 5: // Phong
            dame += this.getTangTanCongLenPhong();
            break;
    }
    
    return dame;
}
```

### 3. Hệ Thống Elemental Combat

#### 3.1 Quy Luật Tương Khắc
```java
private static boolean isKhac(Char _myChar, Char _cAnDame) {
    // 1 => Lôi, 2 => Thổ, 3 => Thủy, 4 => Hỏa, 5 => Phong
    switch (_myChar.infoChar.idClass) {
        case 1: // Lôi khắc Thổ
            return _cAnDame.infoChar.idClass == 2;
        case 2: // Thổ khắc Thủy
            return _cAnDame.infoChar.idClass == 3;
        case 3: // Thủy khắc Hỏa
            return _cAnDame.infoChar.idClass == 4;
        case 4: // Hỏa khắc Phong
            return _cAnDame.infoChar.idClass == 5;
        case 5: // Phong khắc Lôi
            return _cAnDame.infoChar.idClass == 1;
        default:
            return false;
    }
}
```

#### 3.2 Quy Luật Kháng Tính
```java
private static boolean isKhang(Char _myChar, Char _cAnDame) {
    // 1 => Lôi, 2 => Thổ, 3 => Thủy, 4 => Hỏa, 5 => Phong
    switch (_myChar.infoChar.idClass) {
        case 1: // Lôi kháng Phong
            return _cAnDame.infoChar.idClass == 5;
        case 2: // Thổ kháng Lôi
            return _cAnDame.infoChar.idClass == 1;
        case 3: // Thủy kháng Thổ
            return _cAnDame.infoChar.idClass == 2;
        case 4: // Hỏa kháng Thủy
            return _cAnDame.infoChar.idClass == 3;
        case 5: // Phong kháng Hỏa
            return _cAnDame.infoChar.idClass == 4;
        default:
            return false;
    }
}
```

### 4. Hệ Thống Tính Toán Damage PvP

#### 4.1 Damage Theo Hệ
```java
private static int getDameTheoHe(Char _myChar, Char _cAnDame, int dame) {
    int dameDown = dame;
    int khang;
    
    switch (_cAnDame.infoChar.idClass) {
        case 1: // Lôi
            // Tăng 10% damage lên Lôi
            dameDown = dameDown + (dameDown * 10 / 100);
            
            // Cộng thêm damage tăng lên Lôi
            dameDown += _myChar.getTangTanCongLenLoi();
            
            // Tính kháng tính
            khang = getKhangTheoHe(_myChar, _cAnDame);
            
            // Kiểm tra tương khắc
            if (isKhac(_myChar, _cAnDame)) {
                dameDown += _myChar.getTangTuongKhac();
                khang += _cAnDame.getGiamTuongKhac();
            }
            
            // Kiểm tra bỏ qua kháng tính
            if (_myChar.getBoQuaKhangTinh() > 0) {
                boolean okBoQua = Utlis.randomBoolean(100, _myChar.getBoQuaKhangTinh());
                if (okBoQua) {
                    khang = 0;
                }
            }
            
            // Trừ kháng tính
            khang += _cAnDame.getGiamSatThuong();
            dameDown -= khang;
            break;
            
        // Tương tự cho các hệ khác...
    }
    
    return dameDown;
}
```

#### 4.2 Tính Toán Kháng Tính
```java
private static int getKhangTheoHe(Char _myChar, Char _cAnDame) {
    int khang = 0;
    
    switch (_myChar.infoChar.idClass) {
        case 1: // Lôi
            khang = _cAnDame.getKhangLoi() + _cAnDame.getKhangTatCa();
            break;
        case 2: // Thổ
            khang = _cAnDame.getKhangTho() + _cAnDame.getKhangTatCa();
            break;
        case 3: // Thủy
            khang = _cAnDame.getKhangThuy() + _cAnDame.getKhangTatCa();
            break;
        case 4: // Hỏa
            khang = _cAnDame.getKhangHoa() + _cAnDame.getKhangTatCa();
            break;
        case 5: // Phong
            khang = _cAnDame.getKhangPhong() + _cAnDame.getKhangTatCa();
            break;
    }
    
    return khang;
}
```

### 5. Hệ Thống Tính Toán Damage PvE

#### 5.1 Attack Mob
```java
public void attackMob(Client client, int idSkill, int idMob) {
    try {
        Skill skill = client.mChar.getSkillWithIdTemplate(idSkill);
        if (skill == null || skill.level == 0 || skill.mpUse > client.mChar.infoChar.mp 
            || skill.levelNeed > client.mChar.level() 
            || System.currentTimeMillis() - skill.time < skill.coolDown 
            || client.mChar.info.isBiChoang) {
            return;
        }
        
        // Sử dụng MP
        skill.time = System.currentTimeMillis();
        client.mChar.MineMp(skill.mpUse);
        client.mChar.msgUpdateMp();
        
        // Tìm mob
        Mob mob = findMobInMap(idMob);
        if (mob == null || mob.hp <= 0) return;
        
        // Kiểm tra khoảng cách
        if (Utlis.getRange(mob.cx, client.mChar.cx) <= skill.rangeNgang + mob.getMobTemplate().speedMove
            && Utlis.getRange(mob.cy, client.mChar.cy) <= skill.rangeDoc + mob.getMobTemplate().speedMove) {
            
            // Tính damage cơ bản
            int dameCoBan = client.mChar.getDame() + client.mChar.getDameMob(mob);
            
            // Cộng thêm damage skill
            int dame = (dameCoBan + skill.getDameMob(client, mob));
            
            // Random damage (90-100%)
            dame = Utlis.nextInt(dame * 90 / 100, dame);
            
            // Kiểm tra chí mạng
            boolean chi_mang = Utlis.randomBoolean(100, client.mChar.getChiMang() / 100);
            if (chi_mang) {
                int num = 80;
                num += client.mChar.getTangTanCongChiMang();
                dame = dame + (dame * num / 100);
            }
            
            // Gây damage
            zone.setDameMob(client, zone, mob, dame, chi_mang);
        }
    } catch (Exception e) {
        Utlis.logError(Map.class, e, "Lỗi attack mob");
    }
}
```

#### 5.2 Mob Attack Player
```java
public void mobAttackChar(Mob mob, Client client) {
    try {
        if (client.mChar.infoChar.isDie) return;
        
        // Tính damage mob theo hệ
        int dame = mob.getDameTheoHe(client.mChar);
        
        // Kiểm tra né tránh
        int neTranh = client.mChar.getNeTranh() / 100;
        boolean ne_Tranh = Utlis.randomBoolean(100, neTranh);
        if (ne_Tranh) {
            sendNeSatThuong(client.mChar.id);
            return;
        }
        
        // Gây damage
        client.mChar.MineHp(dame);
        
        // Kiểm tra phản đòn
        if (client.mChar.getPhanDon() > 0) {
            int damePhan = dame * client.mChar.getPhanDon() / 100;
            client.mChar.setAttackMob(mob, damePhan, false);
        }
        
        // Gửi thông báo
        sendMobAttackChar(mob, client);
        client.mChar.msgUpdateHpMpWhenAttack(false, "");
        
    } catch (Exception e) {
        Utlis.logError(Map.class, e, "Lỗi mob attack char");
    }
}
```

### 6. Hệ Thống Mob Damage

#### 6.1 Damage Theo Hệ Của Mob
```java
public int getDameTheoHe(Char _cAnDam) {
    int num = dame; // Damage cơ bản
    
    // Tăng damage theo hệ của nhân vật
    switch (_cAnDam.infoChar.idClass) {
        case 1: // Lôi
            num += tangDameLenLoi;
            break;
        case 2: // Thổ
            num += tangDameLenTho;
            break;
        case 3: // Thủy
            num += tangDameLenThuy;
            break;
        case 4: // Hỏa
            num += tangDameLenHoa;
            break;
        case 5: // Phong
            num += tangDameLenPhong;
            break;
    }
    
    // Trừ kháng tính theo hệ
    switch (he) {
        case 1: // Lôi
            num -= _cAnDam.getKhangLoi();
            break;
        case 2: // Thổ
            num -= _cAnDam.getKhangTho();
            break;
        case 3: // Thủy
            num -= _cAnDam.getKhangThuy();
            break;
        case 4: // Hỏa
            num -= _cAnDam.getKhangHoa();
            break;
        case 5: // Phong
            num -= _cAnDam.getKhangPhong();
            break;
    }
    
    // Trừ kháng tính chung
    num -= _cAnDam.getGiamSatThuong() + _cAnDam.getKhangTatCa();
    
    // Kiểm tra suy yếu
    if (this.IsSuyYeu) num /= 2;
    
    return num;
}
```

### 7. Hệ Thống Hiệu Ứng Đặc Biệt

#### 7.1 Hiệu Ứng Bóng
```java
public void setAttackMob(Mob mob, int dame, boolean chi_mang) {
    if (infoChar.isDie) return;
    
    if (mob.hp > 0) {
        // Hiệu ứng bóng (tăng damage x2)
        if (mob.IsBong) {
            dame *= 2;
        }
        
        // Sát thương chuyển HP
        if (getSatThuongChuyenHp() > 0) {
            int hpPlus = dame * client.mChar.getSatThuongChuyenHp() / 100;
            PlusHp(hpPlus);
            msgUpdateHp();
        }
        
        // Phản đòn
        if (mob.PhanDon > 0) {
            int phan = dame * mob.PhanDon / 100;
            if (zone.map.mapID == 47 && info.isBiDuoc) {
                // Đặc biệt cho map 47
            } else {
                MineHpPhanDon(phan, false, mob);
            }
        }
        
        // Né tránh
        if (mob.NeTranh > 0) {
            int phan = dame * mob.PhanDon / 100;
            MineHpPhanDon(phan, false, mob);
        }
        
        // Gây damage
        zone.setDameMob(client, zone, mob, dame, chi_mang);
    }
}
```

#### 7.2 Hiệu Ứng PvP
```java
public void setAttackPlayer(Char player, int dame, boolean chi_mang) {
    if (player.infoChar.isDie || infoChar.isDie) return;
    
    // Hiệu ứng bóng
    if (player.info.isBiBong) {
        dame *= 2;
    }
    
    if (player.client.isConnected() && !player.infoChar.isDie) {
        // Gây damage
        player.MineHp(dame);
        player.msgUpdateHpMpWhenAttack(chi_mang, infoChar.name);
        
        // Phản đòn
        if (player.getPhanDon() > 0) {
            int damePhan = dame * player.getPhanDon() / 100;
            MineHp(damePhan);
            msgUpdateHpMpWhenAttack(chi_mang, player.infoChar.name);
        }
    }
}
```

### 8. Hệ Thống Chí Mạng

#### 8.1 Tính Toán Chí Mạng
```java
// Kiểm tra chí mạng
boolean chi_mang = Utlis.randomBoolean(100, client.mChar.getChiMang() / 100);
if (chi_mang) {
    int num = 80; // Tăng cơ bản 80%
    num += client.mChar.getTangTanCongChiMang(); // Cộng thêm % tăng chí mạng
    dame = dame + (dame * num / 100);
}
```

#### 8.2 Chỉ Số Chí Mạng
```java
// Chỉ số chí mạng cơ bản
this.TuongKhac.ChiMang = arrayTiemNang[1] / 3;

// Tăng tấn công chí mạng
this.TuongKhac.TangTanCongChiMang = 0;

// Từ trang bị
this.TuongKhac.ChiMang += this.arrItemBody[i].getChiSo(this.client, 209) / 3;
this.TuongKhac.ChiMang += this.arrItemBody[i].getChiSo(this.client, 5, 15, 28, 63, 144, 166, 362);
this.TuongKhac.ChiMang += this.arrItemBody[i].getChiSo(1, this.client, 203);

// Tăng tấn công chí mạng từ trang bị
this.TuongKhac.TangTanCongChiMang += this.arrItemBody[i].getChiSo(this.client, 41, 309);
```

### 9. Hệ Thống Né Tránh

#### 9.1 Tính Toán Né Tránh
```java
// Kiểm tra né tránh
int neTranh = client.mChar.getNeTranh() / 100;
boolean ne_Tranh = Utlis.randomBoolean(100, neTranh);
if (ne_Tranh) {
    sendNeSatThuong(client.mChar.id);
    return; // Né thành công
}
```

#### 9.2 Chỉ Số Né Tránh
```java
// Né tránh cơ bản
this.TuongKhac.NeTranh = arrayTiemNang[1] / 3;

// Né tránh từ trang bị
this.TuongKhac.NeTranh += this.arrItemBody[i].getChiSo(this.client, 64, 151, 161, 324, 14);
this.TuongKhac.NeTranh += this.arrItemBody[i].getChiSo(this.client, 209) / 3;
this.TuongKhac.NeTranh += this.arrItemBody[i].getChiSo(1, this.client, 204);

// Bỏ qua né tránh
this.TuongKhac.BoQuaNeTranh += this.arrItemBody[i].getChiSo(this.client, 4, 147, 160);
```

### 10. Hệ Thống Phản Đòn

#### 10.1 Tính Toán Phản Đòn
```java
// Phản đòn khi bị tấn công
if (player.getPhanDon() > 0) {
    int damePhan = dame * player.getPhanDon() / 100;
    MineHp(damePhan);
    msgUpdateHpMpWhenAttack(chi_mang, player.infoChar.name);
}

// Phản đòn khi bị mob tấn công
if (client.mChar.getPhanDon() > 0) {
    int damePhan = dame * client.mChar.getPhanDon() / 100;
    client.mChar.setAttackMob(mob, damePhan, false);
}
```

#### 10.2 Chỉ Số Phản Đòn
```java
// Phản đòn từ trang bị
this.TuongKhac.PhanDon += this.arrItemBody[i].getChiSo(this.client, 16);
```

### 11. Hệ Thống Kháng Tính

#### 11.1 Kháng Tính Theo Hệ
```java
// Kháng Lôi
this.TuongKhac.KhangLoi += this.arrItemBody[i].getChiSo(this.client, 7, 35, 82, 108);

// Kháng Thổ
this.TuongKhac.KhangTho += this.arrItemBody[i].getChiSo(this.client, 8, 36, 83, 109);

// Kháng Thủy
this.TuongKhac.KhangThuy += this.arrItemBody[i].getChiSo(this.client, 9, 37, 84, 110);

// Kháng Hỏa
this.TuongKhac.KhangHoa += this.arrItemBody[i].getChiSo(this.client, 10, 38, 85, 111);

// Kháng Phong
this.TuongKhac.KhangPhong += this.arrItemBody[i].getChiSo(this.client, 11, 39, 86, 112);

// Kháng tất cả
this.TuongKhac.KhangTatCa += this.arrItemBody[i].getChiSo(this.client, 121, 152, 258, 40, 81, 12);
this.TuongKhac.KhangTatCa += this.arrItemBody[i].getChiSo(1, this.client, 201);
```

#### 11.2 Giảm Sát Thương
```java
// Giảm sát thương từ trang bị
this.TuongKhac.GiamSatThuong += this.arrItemBody[i].getChiSo(this.client, 13, 173);
this.TuongKhac.GiamSatThuong += this.arrItemBody[i].getChiSo(1, this.client, 206);
```

### 12. Hệ Thống Tăng Tấn Công

#### 12.1 Tăng Tấn Công Theo Hệ
```java
// Tăng tấn công lên Lôi
this.TuongKhac.TangTanCongLenLoi += this.arrItemBody[i].getChiSo(this.client, 21, 113);

// Tăng tấn công lên Thổ
this.TuongKhac.TangTanCongLenTho += this.arrItemBody[i].getChiSo(this.client, 22, 114);

// Tăng tấn công lên Thủy
this.TuongKhac.TangTanCongLenThuy += this.arrItemBody[i].getChiSo(this.client, 23, 115);

// Tăng tấn công lên Hỏa
this.TuongKhac.TangTanCongLenHoa += this.arrItemBody[i].getChiSo(this.client, 24, 116);

// Tăng tấn công lên Phong
this.TuongKhac.TangTanCongLenPhong += this.arrItemBody[i].getChiSo(this.client, 25, 117);
```

#### 12.2 Tăng Tấn Công Chung
```java
// Tăng tấn công cơ bản
this.TuongKhac.TanCongCoBan += this.arrItemBody[i].getChiSo(client, 31);

// Tăng tấn công tổng
this.TuongKhac.TanCong += _dame;

// Tăng tấn công lên quái
this.TuongKhac.TanCongQuai += this.arrItemBody[i].getChiSoDameMob();

// Tăng tấn công tất cả
this.TuongKhac.TangDameTatCa += this.arrItemBody[i].getChiSo(this.client, 53, 54, 55, 56, 57, 138, 139, 140, 141, 142, 307, 310, 372);
```

### 13. Hệ Thống Hiệu Ứng Trạng Thái

#### 13.1 Gây Hiệu Ứng
```java
// Gây suy yếu
this.TuongKhac.GaySuyYeu += this.arrItemBody[i].getChiSo(this.client, 48, 68, 123, 168, 185, 259);

// Gây trúng độc
this.TuongKhac.GayTrungDoc += this.arrItemBody[i].getChiSo(this.client, 49, 69, 124, 169, 186, 260);

// Gây làm chậm
this.TuongKhac.GayLamCham += this.arrItemBody[i].getChiSo(this.client, 50, 70, 125, 170, 187, 261);

// Gây bóng
this.TuongKhac.GayBong += this.arrItemBody[i].getChiSo(this.client, 51, 71, 126, 171, 188, 262);

// Gây choáng
this.TuongKhac.GayChoang += this.arrItemBody[i].getChiSo(this.client, 52, 72, 127, 172, 189, 263);
```

#### 13.2 Giảm Hiệu Ứng
```java
// Giảm suy yếu
this.TuongKhac.GiamSuyYeu += this.arrItemBody[i].getChiSo(this.client, 289, 325, 355);

// Giảm trúng độc
this.TuongKhac.GiamTrungDoc += this.arrItemBody[i].getChiSo(this.client, 290, 326, 356);

// Giảm làm chậm
this.TuongKhac.GiamLamCham += this.arrItemBody[i].getChiSo(this.client, 290, 327, 357);

// Giảm gây bóng
this.TuongKhac.GiamGayBong += this.arrItemBody[i].getChiSo(this.client, 289, 325, 355);

// Giảm gây choáng
this.TuongKhac.GiamGayChoang += this.arrItemBody[i].getChiSo(this.client, 290, 326, 356);
```

### 14. Hệ Thống Random Damage

#### 14.1 Random Damage Cơ Bản
```java
// Random damage (90-100%)
dame = Utlis.nextInt(dame * 90 / 100, dame);
```

#### 14.2 Random Damage Theo HP
```java
public int getRandom(int hpFull) {
    int r = 0;
    if (hpFull < 1000) {
        r = Utlis.nextInt(1, 11);
    } else {
        r = Utlis.nextInt((hpFull / 300), (hpFull / 200));
    }
    if (r >= 800) {
        r = Utlis.nextInt(800, 1000);
    }
    return r;
}

public int getRandomDame(int hpFull) {
    int r = 0;
    r = Utlis.nextInt((hpFull / 150), (hpFull / 100));
    if (r >= 1500) {
        r = Utlis.nextInt(1500, 2000);
    }
    return r;
}
```

### 15. Hệ Thống Bảo Mật Combat

#### 15.1 Validation Damage
```java
// Kiểm tra damage hợp lệ
if (dame < 0) {
    dame = 0;
}

// Kiểm tra damage quá cao
if (dame > MAX_DAMAGE) {
    logSuspiciousDamage(player, dame);
    dame = MAX_DAMAGE;
}
```

#### 15.2 Anti-Cheat
```java
// Kiểm tra tần suất tấn công
long currentTime = System.currentTimeMillis();
if (currentTime - lastAttackTime < MIN_ATTACK_INTERVAL) {
    logSuspiciousAttack(player);
    return;
}

// Kiểm tra khoảng cách
if (getDistance(attacker, target) > MAX_ATTACK_RANGE) {
    logSuspiciousRange(attacker, target);
    return;
}
```

### 16. Hiệu Suất & Tối Ưu

#### 16.1 Performance Optimization
- **Damage Caching:** Cache kết quả tính toán damage
- **Batch Processing:** Xử lý hàng loạt damage
- **Lazy Calculation:** Tính toán khi cần
- **Memory Pooling:** Sử dụng pool cho damage objects

#### 16.2 Memory Management
- **Damage Object Pool:** Pool cho damage objects
- **Effect Cleanup:** Dọn dẹp hiệu ứng hết hạn
- **Memory Leak Prevention:** Ngăn chặn rò rỉ bộ nhớ

### 17. Monitoring & Analytics

#### 17.1 Performance Metrics
- **Damage Distribution:** Phân bố damage
- **Critical Hit Rate:** Tỷ lệ chí mạng
- **Dodge Rate:** Tỷ lệ né tránh
- **Elemental Effectiveness:** Hiệu quả hệ tính

#### 17.2 Combat Analytics
- **Player Performance:** Hiệu suất người chơi
- **Skill Usage:** Sử dụng kỹ năng
- **Damage Patterns:** Mẫu damage
- **Balance Analysis:** Phân tích cân bằng

### 18. Kết Luận

Hệ thống Combat & Damage Calculation được thiết kế với:
- **Thuật toán phức tạp** cho tính toán damage chính xác
- **Hệ thống hệ tính** với tương khắc và kháng tính
- **Hiệu ứng đặc biệt** đa dạng và hấp dẫn
- **Performance tối ưu** với caching và batch processing
- **Bảo mật cao** với anti-cheat system

Hệ thống tạo ra trải nghiệm chiến đấu cân bằng, hấp dẫn và công bằng cho tất cả người chơi, đồng thời duy trì hiệu suất cao cho server.
