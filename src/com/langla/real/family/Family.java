package com.langla.real.family;


import com.langla.data.DataCache;
import com.langla.data.DataCenter;
import com.langla.data.SkillClan;
import com.langla.lib.Utlis;
import com.langla.real.item.Item;
import com.langla.real.map.BossRunTime;
import com.langla.real.map.BossTpl;
import com.langla.real.map.Map;
import com.langla.real.map.Mob;
import com.langla.real.other.Effect;
import com.langla.real.other.Thu;
import com.langla.real.player.Char;
import com.langla.real.player.CharDB;
import com.langla.real.player.PlayerManager;
import com.langla.server.lib.Message;
import com.langla.server.main.Maintenance;
import com.langla.utlis.UTPKoolVN;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * @author PKoolVN
 **/
public class Family {
    protected static Family Instance;

    public static Family gI() {
        if (Instance == null)
            Instance = new Family();
        return Instance;
    }

    public Vector<FamilyTemplate> listFamily = new Vector<>();
    public Family(){

    }
//    public void StartFamilyRunTime() {
//        new Thread(() -> {
//            while (!Maintenance.isRunning) {
//                try {
//                    if (UTPKoolVN.getHour() == 0) {
//
//                    }
//                    try {
//                        Thread.sleep(60000);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                } catch (Exception ex) {
//                    Utlis.logError(BossRunTime.class, ex, "Da say ra loi:\n" + ex.getMessage());
//                }
//            }
//        }, "Boss RUN TIME").start();
//    }

    public void updateFamily(Char character) {
        if(character.infoChar.familyId == -1) return;
        FamilyTemplate gT = getGiaToc(character);
        if (gT != null) {

            LocalDate currentDate = LocalDate.now();
            if (gT.info.lastDailyUpdate == null || !gT.info.lastDailyUpdate.isEqual(currentDate)) {
                gT.info.lastDailyUpdate= currentDate;
                //
                gT.info.soLuongThuNapTrongNgay = gT.info.maxMember;
                gT.info.soLanMoAi += 1;
                if(gT.info.soLanMoAi > 5)  gT.info.soLanMoAi = 5;
            }

            LocalDate currentDateDay = LocalDate.now();
            LocalDate startOfWeek = currentDateDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

            if (gT.info.lastWeeklyUpdate == null || startOfWeek.isAfter(gT.info.lastWeeklyUpdate)) { // update hàng tuần
                gT.info.lastWeeklyUpdate = startOfWeek;
                gT.info.congHienTuan = 0;
                for (Family_Member member: gT.listMember){
                    member.congHienTuan = 0;
                }
            }
        }

    }


    public synchronized void TaoGiaToc(Char character, String input){
        try {
            Item itembag;
            if (!Pattern.matches("^[a-zA-Z0-9]{4,15}$", input)) {
                character.client.session.serivce.ShowMessRed("Tên gia tộc phải từ 4-15 kí tự. Viết liền không dấu không có ký tự đặc biệt");
            }  else if (character.level() < 40) {
                character.client.session.serivce.ShowMessRed("Cấp 40 trở lên mới có thể tạo gia tộc.");
            } else if (input.contains("adm")) {
                character.client.session.serivce.ShowMessRed("Tên gia tộc có ký tự không cho phép");
            } else if (FamilyDB.CheckName(input)) {
                character.client.session.serivce.ShowMessRed("Tên gia tộc này đã có người sử dụng");
            } else if (character.infoChar.familyId != -1) {
                character.client.session.serivce.ShowMessRed("Bạn đã có gia tộc không thể thành lập gia tộc");
            } else if ((itembag = character.getItemBagById(301)) == null) {
                character.client.session.serivce.ShowMessRed("Không có gia tộc lệnh");
            } else {
                FamilyTemplate newFamily = new FamilyTemplate();
                newFamily.name = input;
                newFamily.info.timeCreateLog = System.currentTimeMillis();
                newFamily.info.nameKey = character.infoChar.name;
                newFamily.info.idKey = character.id;
                newFamily.info.nganSach = 30000;

                Family_Member newMem = new Family_Member();
                newMem.infoChar = character.infoChar;
                newMem.idCharacter = character.id;
                newMem.role = 5;
                newFamily.listMember.add(newMem);


                newFamily.id = FamilyDB.Insert(newFamily);
                if (newFamily.id == -1) {
                    character.client.session.serivce.ShowMessRed("Đã sảy ra lỗi vui lòng thử lại sau");
                } else {
                    character.client.session.serivce.closeTab();
                    character.client.session.serivce.NhacNhoMessage("Xin chúc mừng bạn đã chính thức trở thành tộc trưởng của gia tộc " + input);
                    character.infoChar.familyId = newFamily.id;
                    character.infoChar.familyName = newFamily.name;
                    character.removeItemBag(itembag, "Tạo gia tộc");
                    listFamily.add(newFamily);
                    character.client.session.serivce.sendInfoGiaTocAllChar(character);
                }
            }
        }  catch (Exception e) {
            Utlis.logError(Family.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
        }
    }
    public synchronized void KhaiMoSkill(Char character, byte idSkill){
        try {
            FamilyTemplate gT = getGiaToc(character);
            if (gT == null) {
                character.client.session.serivce.ShowMessRed("Gia tộc không tồn tại");
            } else {
                Family_Member my = getMe(character, gT);
                if (my != null && my.role == 5) {
                    SkillClan skillClan = DataCenter.gI().getSkillClan(idSkill);

                    if(skillClan == null) return;
                    int phi = skillClan.moneyBuy;

                    if(phi > gT.info.nganSach){
                        character.client.session.serivce.ShowMessRed("Gia tộc không đủ ngân sách");
                        return;
                    }
                    if(skillClan.levelNeed > gT.info.level){
                        character.client.session.serivce.ShowMessRed("Gia tộc chưa đủ level");
                        return;
                    }
                    gT.info.nganSach -= phi;
                    if(!gT.listSkill.contains(skillClan)) gT.listSkill.add(skillClan);
                    String log = "" + character.infoChar.name + " đã khai mở Kỹ năng "+skillClan.name+" gia tộc bị trừ " + Utlis.numberFormat(phi) + " bạc";
                    addLog(log, gT);
                    sendChat(character.infoChar.name, " đã khai mở Kỹ năng "+skillClan.name+" gia tộc bị trừ " + Utlis.numberFormat(phi) + " bạc", gT.id);
                    character.client.session.serivce.sendGiaToc(character);
                    character.setUpInfo(true);
                    FamilyDB.Update(gT);
                } else {
                    character.client.session.serivce.ShowMessRed("Không đủ quyền");
                }
            }
        }  catch (Exception e) {
            Utlis.logError(Family.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
        }
    }
    public void camChat(Char character, String name){
        try {
            FamilyTemplate gT = getGiaToc(character);
            if (gT == null) {
                character.client.session.serivce.ShowMessRed("Gia tộc không tồn tại");
            } else {
                Family_Member member = getMe(name, gT);
                Family_Member my = getMe(character, gT);

                if (member != null && my != null && my.role > member.role) {
                    if(!member.isCamChat){
                        member.isCamChat = true;
                        String log = "" + character.infoChar.name + " đã cấm chat " + name + " - " + getTimeNow() + "";
                        addLog(log, gT);
                        sendChat(character.infoChar.name, " đã cấm chat "+name, gT.id);
                    } else {
                        member.isCamChat = false;
                        String log = "" + character.infoChar.name + " bỏ cấm chat " + name + " - " + getTimeNow() + "";
                        addLog(log, gT);
                        sendChat(character.infoChar.name, " bỏ cấm chat "+name, gT.id);
                    }
                    character.client.session.serivce.sendGiaToc(character);
                    FamilyDB.Update(gT);
                } else {
                    character.client.session.serivce.ShowMessGold("Bạn không đủ quyền thực hiện người này");
                }
            }
        }  catch (Exception e) {
            Utlis.logError(Family.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
        }
    }

    public void setRole(Char character, String name, byte role){
        try {
            FamilyTemplate gT = getGiaToc(character);
            if (gT == null) {
                character.client.session.serivce.ShowMessRed("Gia tộc không tồn tại");
            } else {
                Family_Member member = getMe(name, gT);
                Family_Member my = getMe(character, gT);

                if (member != null && my != null && my.role > member.role) {
                    if(role == 0){
                        member.role = 0;
                        String log = "" + character.infoChar.name + " đã hạ " + name + " xuống thành viên - " + getTimeNow() + "";
                        addLog(log, gT);
                        sendChat(character.infoChar.name, " đã hạ "+name+" xuống thành viên", gT.id);
                    } else if(role == 3){
                        member.role = 3;
                        String log = "" + character.infoChar.name + " đã bổ nhiệm " + name + " làm trưởng lão - " + getTimeNow() + "";
                        addLog(log, gT);
                        sendChat(character.infoChar.name, " đã bổ nhiệm "+name+" làm trưởng lão", gT.id);
                    } else if(role == 4){
                        member.role = 4;
                        String log = "" + character.infoChar.name + " đã bổ nhiệm " + name + " làm tộc phó - " + getTimeNow() + "";
                        addLog(log, gT);
                        sendChat(character.infoChar.name, " đã bổ nhiệm "+name+" làm tộc phó", gT.id);
                    } else if(role == 5){
                        if(member.timeThamGia+172800000L > System.currentTimeMillis()){
                            character.client.session.serivce.NhacNhoMessage("Người chơi phải vào gia tộc trên 2 ngày mới có thể nhường chức.");
                            return;
                        }
                        member.role = 5;
                        my.role = 0;
                        gT.info.nameKey = member.infoChar.name;
                        gT.info.idKey = member.idCharacter;
                        String log = "" + character.infoChar.name + " đã bổ nhiệm " + name + " làm tộc trưởng - " + getTimeNow() + "";
                        addLog(log, gT);
                        sendChat(character.infoChar.name, " đã bổ nhiệm "+name+" làm tộc trưởng", gT.id);
                    }
                    FamilyDB.Update(gT);
                    character.client.session.serivce.sendGiaToc(character);
                    Char get = PlayerManager.getInstance().getChar(name);
                    if(get != null){
                        get.client.session.serivce.sendInfoGiaTocAllChar(get);
                    }
                } else {
                    character.client.session.serivce.ShowMessGold("Bạn không đủ quyền thực hiện người này");
                }
            }
        }  catch (Exception e) {
            Utlis.logError(Family.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
        }
    }
    public synchronized void kickMember(Char character, String name){
        try {
            FamilyTemplate gT = getGiaToc(character);
            if (gT == null) {
                character.client.session.serivce.ShowMessRed("Gia tộc không tồn tại");
            } else {
                Family_Member member = getMe(name, gT);
                Family_Member my = getMe(character, gT);

                if (member != null && my != null && my.role > member.role) {
                    gT.listMember.remove(member);
                    String log = "" + character.infoChar.name + " đã trục xuất " + name + " khỏi ra tộc - " + getTimeNow() + "";
                    addLog(log, gT);
                    sendChat(character.infoChar.name, " đã trục xuất "+name+" khỏi ra tộc", gT.id);
                    character.client.session.serivce.sendGiaToc(character);
                    Char get = PlayerManager.getInstance().getChar(name);
                    if(get != null){
                        get.updateInfoToFamily();
                    }
                    FamilyDB.Update(gT);
                } else {
                    character.client.session.serivce.ShowMessGold("Bạn không đủ quyền thực hiện người này");
                }
            }
        }  catch (Exception e) {
            Utlis.logError(Family.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
        }
    }
    public void gopQuy(Char character, int bac){
        try {
            FamilyTemplate gT = getGiaToc(character);
            if (gT == null) {
                character.client.session.serivce.ShowMessRed("Gia tộc không tồn tại");
            } else {
                Family_Member my = getMe(character, gT);
                if (my != null) {
                    if(bac < 1 || bac > character.infoChar.bac){
                        character.client.session.serivce.ShowMessRed("Bạn không đủ bạc");
                        return;
                    }
                    if(gT.info.nganSach >= 2100000000) return;
                    gT.info.nganSach += bac;
                    my.congHien += bac;
                    my.congHienTuan += bac;
                    gT.info.congHienTuan++;
                    character.mineBac(bac, true, true, "Đóng góp vào gia tộc");
                    String log = "" + character.infoChar.name + " đã đóng góp " + Utlis.numberFormat(bac) + " bạc vào gia tộc - " + getTimeNow() + "";
                    addLog(log, gT);
                    sendChat(character.infoChar.name, " đã đóng góp "+Utlis.numberFormat(bac)+" bạc vào gia tộc", gT.id);
                    character.client.session.serivce.sendGiaToc(character);
                    FamilyDB.Update(gT);
                }
            }
        }  catch (Exception e) {
            Utlis.logError(Family.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
        }
    }
    public synchronized void rutQuy(Char character, int bac){
        try {
            FamilyTemplate gT = getGiaToc(character);
            if (gT == null) {
                character.client.session.serivce.ShowMessRed("Gia tộc không tồn tại");
            } else {
                Family_Member my = getMe(character, gT);
                if (my != null && my.role == 5) {
                    int phi = bac/100;
                    int tongbac = bac+phi;
                    if(bac < 1000){
                        character.client.session.serivce.ShowMessRed("Số bạc tối thiểu là 1000");
                        return;
                    }
                    if(tongbac < 1 || tongbac > gT.info.nganSach){
                        character.client.session.serivce.ShowMessRed("Gia tộc không đủ ngân sách");
                        return;
                    }
                    if(character.infoChar.bac >= 2100000000) return;
                    gT.info.nganSach -= tongbac;
                    character.addBac(bac, true, true, "Rút quỹ ra tộc");
                    String log = "" + character.infoChar.name + " đã rút quỹ ngân sách " + Utlis.numberFormat(bac) + " bạc - " + getTimeNow() + "";
                    addLog(log, gT);
                    sendChat(character.infoChar.name, " đã rút quỹ ngân sách "+Utlis.numberFormat(bac)+" bạc", gT.id);
                    character.client.session.serivce.sendGiaToc(character);
                    FamilyDB.Update(gT);
                } else {
                    character.client.session.serivce.ShowMessRed("Không đủ quyền");
                }
            }
        }  catch (Exception e) {
            Utlis.logError(Family.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
        }
    }

    public synchronized void phatLuong(Char character, String name, int bac){
        try {
            if(character.infoChar.name.equals(name)) return;
            FamilyTemplate gT = getGiaToc(character);
            Char player = PlayerManager.getInstance().getChar(name);
            if (gT == null) {
                character.client.session.serivce.ShowMessRed("Gia tộc không tồn tại");
            } else {
                Family_Member my = getMe(character, gT);
                Family_Member member = getMe(name, gT);
                if (member != null && my != null && my.role == 5) {
                    int phi = bac/100;
                    int tongbac = bac+phi;
                    if(bac < 1000){
                        character.client.session.serivce.ShowMessRed("Số bạc tối thiểu là 1000");
                        return;
                    }
                    if(tongbac < 1 || tongbac > gT.info.nganSach){
                        character.client.session.serivce.ShowMessRed("Gia tộc không đủ ngân sách");
                        return;
                    }
                    Thu thu = new Thu();
                    thu.chuDe = "Tộc trưởng "+character.infoChar.name+" phát lương";
                    thu.nguoiGui = character.infoChar.name;
                    thu.noiDung = "Tộc trưởng "+character.infoChar.name+" phát lương cho bạn";
                    thu.bac = bac;
                    if(player == null) {
                        thu.id = 999;
                        CharDB.guiThuOffline(name,thu);
                    } else {
                        thu.id = player.baseIdThu++;
                        player.listThu.add(thu);
                        player.client.session.serivce.updateThu();
                    }
                    gT.info.nganSach -= tongbac;
                    String log = "" + character.infoChar.name + " đã phát lương cho "+name+" " + Utlis.numberFormat(bac) + " bạc - " + getTimeNow() + "";
                    addLog(log, gT);
                    sendChat(character.infoChar.name, " đã phát lương cho "+name+" "+Utlis.numberFormat(bac)+" bạc", gT.id);
                    character.client.session.serivce.sendGiaToc(character);
                    character.client.session.serivce.ShowMessGold("Đã phát "+Utlis.numberFormat(bac)+" bạc cho "+name);
                    FamilyDB.Update(gT);
                } else {
                    character.client.session.serivce.ShowMessRed("Không đủ quyền");
                }
            }
        }  catch (Exception e) {
            Utlis.logError(Family.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
        }
    }
    public synchronized void roiRaToc(Char character){
        try {
            FamilyTemplate gT = getGiaToc(character);
            if (gT == null) {
                character.client.session.serivce.ShowMessRed("Gia tộc không tồn tại");
            } else {
                Family_Member my = getMe(character, gT);
                if ( my != null) {
                    gT.listMember.remove(my);
                    String log = "" + character.infoChar.name + " đã rời khỏi ra tộc - " + getTimeNow() + "";
                    addLog(log, gT);
                    sendChat(character.infoChar.name, " đã rời khỏi ra tộc ", gT.id);
                    character.updateInfoToFamily();
                    character.client.session.serivce.closeTab();
                    FamilyDB.Update(gT);
                }
            }
        }  catch (Exception e) {
            Utlis.logError(Family.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
        }
    }
    public void XinVaoGiaToc(Char character, String name){
        try {
            FamilyTemplate gT = getGiaToc(name);
            Char player;
            if (character.infoChar.delayGuiGiaToc > System.currentTimeMillis()) {
                character.client.session.serivce.NhacNhoMessage("Bạn đã gửi yêu cầu xin vào gia tộc trước đó. Còn: " +
                        String.format("%.1f", (character.infoChar.delayGuiGiaToc - System.currentTimeMillis()) / 1000.0) + " giây nữa mới được gửi tiếp.");
            } else if (character.infoChar.familyId != -1) {
                character.client.session.serivce.ShowMessRed("Bạn đã có gia tộc rồi");
            } else if (gT == null) {
                character.client.session.serivce.ShowMessRed("Gia tộc không tồn tại");
            } else if (gT.listMember.size() >= gT.info.maxMember) {
                character.client.session.serivce.ShowMessGold("Số lượng thành viên đã đạt tối đa");
            } else if (gT.info.soLuongThuNapTrongNgay < 1) {
                character.client.session.serivce.ShowMessGold("Số lượng thu nạp trong ngày đã hết.");
            } else if ((player = PlayerManager.getInstance().getChar(gT.info.idKey)) == null) {
                character.client.session.serivce.ShowMessRed("Tộc trưởng đã Offline");
            } else {
                player.client.session.serivce.xinVaoGiaToc(character);
                if (character.client.player.role != 2)
                    character.infoChar.delayGuiGiaToc = 60000 + System.currentTimeMillis();
            }
        }  catch (Exception e) {
            Utlis.logError(Family.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
        }
    }
    public void XinVaoGiaTocNameChar(Char character, String name){
        try {
            FamilyTemplate gT;
            Char player = PlayerManager.getInstance().getChar(name);
            if (character.infoChar.delayGuiGiaToc > System.currentTimeMillis()) {
                character.client.session.serivce.NhacNhoMessage("Bạn đã gửi yêu cầu xin vào gia tộc trước đó. \\nCòn: " +
                        String.format("%.1f", (character.infoChar.delayGuiGiaToc - System.currentTimeMillis()) / 1000.0) + " giây nữa mới được gửi tiếp.");
            } else if (player == null) {
                character.client.session.serivce.ShowMessRed("Người chơi đã Offline");
            } else if (character.infoChar.familyId != -1) {
                character.client.session.serivce.ShowMessRed("Bạn đã có gia tộc rồi");
            } else if ((gT = getGiaToc(player.infoChar.familyId)) == null) {
                character.client.session.serivce.ShowMessRed("Gia tộc không tồn tại");
            } else if (gT.listMember.size() >= gT.info.maxMember) {
                character.client.session.serivce.ShowMessGold("Số lượng thành viên đã đạt tối đa");
            } else if (gT.info.soLuongThuNapTrongNgay < 1) {
                character.client.session.serivce.ShowMessGold("Số lượng thu nạp trong ngày đã hết.");
            } else {
                player.client.session.serivce.xinVaoGiaToc(character);
                character.client.session.serivce.NhacNhoMessage("Bạn đã gửi yêu cầu xin vào gia tộc " + gT.name + " thành công. Vui lòng đợi đồng ý.! \\nCòn: " +
                        String.format("%.1f", (character.infoChar.delayGuiGiaToc - System.currentTimeMillis()) / 1000.0) + " giây nữa mới được gửi tiếp.");
                if (character.client.player.role != 2)
                    character.infoChar.delayGuiGiaToc = 60000 + System.currentTimeMillis();
            }
        }  catch (Exception e) {
            Utlis.logError(Family.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
        }
    }
    public void MoiVaoGiaToc(Char character, String name){
        try {
            FamilyTemplate gT = getGiaToc(character);
            Char player = PlayerManager.getInstance().getChar(name);

            if (character.infoChar.delayGuiGiaToc > System.currentTimeMillis()) {
                character.client.session.serivce.NhacNhoMessage("Bạn đã gửi yêu lời mời trước đó. \\nCòn: " +
                        String.format("%.1f", (character.infoChar.delayGuiGiaToc - System.currentTimeMillis()) / 1000.0) + " giây nữa mới được gửi tiếp.");
            } else if (player == null) {
                character.client.session.serivce.ShowMessRed("Người chơi đã Offline");
            } else if (player.infoChar.familyId != -1) {
                character.client.session.serivce.ShowMessRed("Người chơi đã có gia tộc rồi");
            } else if (gT == null) {
                character.client.session.serivce.ShowMessRed("Gia tộc không tồn tại");
            } else if (gT.listMember.size() >= gT.info.maxMember) {
                character.client.session.serivce.ShowMessGold("Số lượng thành viên đã đạt tối đa");
            } else if (gT.info.soLuongThuNapTrongNgay < 1) {
                character.client.session.serivce.ShowMessGold("Số lượng thu nạp trong ngày đã hết.");
            } else {
                Family_Member member = getMe(character, gT);
                if (member != null && member.role >= 3) {
                    player.client.session.serivce.moiVaoGiaToc(character, (byte) member.role);
                    character.client.session.serivce.NhacNhoMessage("Bạn đã gửi lời mời vào gia tộc tới " + name + " thành công.!");
                    if (character.client.player.role != 2)
                        character.infoChar.delayGuiGiaToc = 10000 + System.currentTimeMillis();
                } else {
                    character.client.session.serivce.ShowMessGold("Bạn không đủ thẩm quyền");
                }
            }
        }  catch (Exception e) {
            Utlis.logError(Family.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
        }
    }
    public synchronized void dongYVaoToc(Char character, String name) {
        try {

            Char player = PlayerManager.getInstance().getChar(name);
            FamilyTemplate gT;
            if (player == null) {
                character.client.session.serivce.ShowMessRed("Người chơi đã Offline");
            } else if (player.infoChar.familyId == -1) {
                character.client.session.serivce.ShowMessRed("Người chơi không có gia tộc");
            } else if ((gT = getGiaToc(player)) == null) {
                character.client.session.serivce.ShowMessRed("Gia tộc không tồn tại");
            } else if (gT.listMember.size() >= gT.info.maxMember) {
                character.client.session.serivce.ShowMessGold("Số lượng thành viên đã đạt tối đa");
            } else if (gT.info.soLuongThuNapTrongNgay < 1) {
                character.client.session.serivce.ShowMessGold("Số lượng thu nạp trong ngày đã hết.");
            } else {
                Family_Member member = getMe(player, gT);
                if (member != null && member.role >= 3) {
                    addMember(player, character, gT);
                    player.client.session.serivce.NhacNhoMessage(character.infoChar.name+" đã đồng ý vào gia tộc.!");
                } else {
                    character.client.session.serivce.ShowMessGold(name+" không đủ thẩm quyền");
                }
            }
        } catch (Exception e) {
            Utlis.logError(Family.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
        }
    }
    public synchronized void duyetMember(Char character, String name) {
        try {

            Char player = PlayerManager.getInstance().getChar(name);
            FamilyTemplate gT = getGiaToc(character);
            if (player == null) {
                character.client.session.serivce.ShowMessRed("Người chơi đã Offline");
            } else if (player.infoChar.familyId != -1) {
                character.client.session.serivce.ShowMessRed("Người chơi đã gia tộc rồi");
            } else if (gT == null) {
                character.client.session.serivce.ShowMessRed("Gia tộc không tồn tại");
            } else if (gT.listMember.size() >= gT.info.maxMember) {
                character.client.session.serivce.ShowMessGold("Số lượng thành viên đã đạt tối đa");
            } else if (gT.info.soLuongThuNapTrongNgay < 1) {
                character.client.session.serivce.ShowMessGold("Số lượng thu nạp trong ngày đã hết.");
            } else {
                Family_Member member = getMe(character, gT);
                if (member != null && member.role >= 3) {
                    addMember(character, player, gT);
                    character.client.session.serivce.ShowMessGold("Đã thêm "+name+" vào gia tộc");
                } else {
                    character.client.session.serivce.ShowMessGold("Bạn không đủ thẩm quyền");
                }
            }
        } catch (Exception e) {
                Utlis.logError(Family.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
        }
    }
    public void setThongBao(Char character, String text){
        try {
            FamilyTemplate gT = getGiaToc(character);
            if (character.infoChar.familyId == -1) {
                character.client.session.serivce.ShowMessRed("Bạn chưa có gia tộc");
            } else if (gT == null) {
                character.client.session.serivce.ShowMessRed("Gia tộc không tồn tại");
            } else {
                Family_Member member = getMe(character, gT);
                if (member != null && member.role >= 4) {
                    gT.info.thongBao = text;
                    String log = "" + character.infoChar.name + " thay đổi thông báo thành: " + text + " - " + getTimeNow() + "";
                    addLog(log, gT);
                    character.client.session.serivce.sendGiaToc(character);
                    FamilyDB.Update(gT);
                }
            }
        }  catch (Exception e) {
            Utlis.logError(Family.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
        }
    }
    public FamilyTemplate getGiaToc(String name){
        for (FamilyTemplate get : listFamily) {
            if (get.name.equals(name)) {
                return get;
            }
        }
        return null;
    }
    public FamilyTemplate getGiaToc(Char c){
        for (FamilyTemplate get : listFamily) {
            if (c.infoChar.familyId == get.id) {
                return get;
            }
        }
        return null;
    }
    public FamilyTemplate getGiaToc(int id){
        for (FamilyTemplate get : listFamily) {
            if (get.id == id) {
                return get;
            }
        }
        return null;
    }

    public Family_Member getMe(Char character, FamilyTemplate giaToc){
        for (Family_Member get : giaToc.listMember) {
            if (get.idCharacter == character.id) {
                return get;
            }
        }
        return null;
    }
    public Family_Member getMe(String name, FamilyTemplate giaToc){
        for (Family_Member get : giaToc.listMember) {
            if (get.infoChar.name.equals(name)) {
                return get;
            }
        }
        return null;
    }
    public void addMember(Char character, Char memAdd, FamilyTemplate giaToc) {
        try {

            Family_Member my = getMe(character, giaToc);
            if (my == null) {
                character.client.session.serivce.ShowMessGold("Có lỗi sảy ra");
                return;
            }
            if (giaToc.listMember.size() >= giaToc.info.maxMember) {
                character.client.session.serivce.ShowMessGold("Số lượng thành viên đã đạt tối đa");
            } else if (giaToc.info.soLuongThuNapTrongNgay < 1) {
                character.client.session.serivce.ShowMessGold("Số lượng thu nạp trong ngày đã hết.");
            } else if (my.role < 3) {
                character.client.session.serivce.ShowMessGold("Bạn không đủ quyền");
            } else {
                if (getMe(memAdd, giaToc) == null) {
                    memAdd.infoChar.familyId = giaToc.id;
                    memAdd.infoChar.familyName = giaToc.name;
                    Family_Member newMem = new Family_Member();
                    newMem.infoChar = memAdd.infoChar;
                    newMem.idCharacter = memAdd.id;
                    newMem.role = 0;
                    giaToc.listMember.add(newMem);
                    giaToc.info.soLuongThuNapTrongNgay++;
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
                    String formattedDateTime = currentDateTime.format(formatter);
                    String log = "" + character.infoChar.name + " đã thêm " + memAdd.infoChar.name + " vào gia tộc - " + formattedDateTime + "";
                    addLog(log, giaToc);
                    sendChat(character.infoChar.name, " đã thêm " + memAdd.infoChar.name + " vào gia tộc - " + formattedDateTime + "", giaToc.id);
                    character.client.session.serivce.sendInfoGiaTocAllChar(memAdd);
                    memAdd.client.session.serivce.NhacNhoMessage("Xin chúc mừng bạn đã chính thức trở thành, thành viên của gia tộc: "+giaToc.name);
                    memAdd.setUpInfo(true);
                    FamilyDB.Update(giaToc);
                } else {
                    character.client.session.serivce.ShowMessGold("Thành viên đã được thêm vào trước đó");
                }
            }
        }  catch (Exception e) {
            Utlis.logError(Family.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
        }
    }

    public void saveData(){
        try {
            for (FamilyTemplate get : listFamily) {
                FamilyDB.Update(get);
            }
        } catch (Exception e) {
            Utlis.logError(Family.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
        }
        UTPKoolVN.Print("Save Data Family Sucess!!!");
    }
    public void addLog(String log, FamilyTemplate giaToc){
        FamilyLog newlog = new FamilyLog();
        newlog.log = log+" - " + getTimeNow() + "";
        giaToc.dataLog.add(newlog);
    }

    public void sendMessageAllMember(Message m, int id) {
        FamilyTemplate familyTemplate = getGiaToc(id);
        if(familyTemplate != null){
            for (int i = 0; i < familyTemplate.listMember.size(); i++){
                Char getchar = PlayerManager.getInstance().getChar(familyTemplate.listMember.get(i).idCharacter);
                if(getchar != null) getchar.client.session.sendMessage(m);
            }
        }
    }
    public void sendChat(String name, String text, int id) {
        try {

            Message msg = new Message((byte) 25);
            msg.writeUTF(name);
            msg.writeUTF(text);
            sendMessageAllMember(msg, id);
        } catch (Exception e) {
            Utlis.logError(Family.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
        }
    }
    public String getTimeNow(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        return currentDateTime.format(formatter);
    }

}
