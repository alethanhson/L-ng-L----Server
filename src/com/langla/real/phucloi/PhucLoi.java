package com.langla.real.phucloi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PKoolVN
 **/
public class PhucLoi {
    public int thoigianOnlineHomNay = 0;

    public byte soNgayOnlineLienTuc = 1;
    public byte soNgayNapLienTuc = 0;
    public int vangNapTichLuy = 0;
    public int vangNapHomNay = 0;
    public int vangNapTuan = 0;
    public int vangTieuHomNay = 0;
    public int vangTieuTuan = 0;

    public int vangNapMoc = 0;

    public int vangNapDon = 0;
    public int diemTichLuyVongQuay = 0;

    public int solanQuay = 0;

    public long timeTheThang = -1;

    public long timeTheVinhVinhVien = -1;
    public boolean isGoiHaoHoa = false;

    public boolean isGoiChiTon = false;

    public LocalDate lastLoginDate;
    public LocalDate lastDailyUpdate; // Ngày cuối cùng cập nhật hàng ngày
    public LocalDate lastWeeklyUpdate; // Ngày cuối cùng cập nhật hàng tuần

    public LocalDate lastNapLienTucUpdate; // Ngày cuối cùng cập nhật nạp liên tục

    public final List<LogPhucLoi> logData = new ArrayList<>();
}
