package com.langla.real.player;

import com.PKoolVNDB;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.langla.data.*;
import com.langla.data.center.DataShop;
import com.langla.lib.Utlis;
import com.langla.real.cho.Cho;
import com.langla.real.cho.ChoTemplate;
import com.langla.real.family.Family;
import com.langla.real.family.FamilyTemplate;
import com.langla.real.family.Family_Member;
import com.langla.real.group.Group;
import com.langla.real.group.GroupTemplate;
import com.langla.real.item.GraftCaiTrang;
import com.langla.real.item.Item;
import com.langla.real.item.ItemShop;
import com.langla.real.khobau.KhoBauTpl;
import com.langla.real.lucky.LuckyTpl;
import com.langla.real.map.ItemMap;
import com.langla.real.map.Map;
import com.langla.real.map.Mob;
import com.langla.real.other.*;
import com.langla.real.phucloi.LogPhucLoi;
import com.langla.real.phucloi.PhucLoi;
import com.langla.real.phucloi.PhucLoiTpl;
import com.langla.real.task.TaskHandler;
import com.langla.server.handler.IActionItem;
import com.langla.server.lib.Message;
import com.langla.server.lib.Writer;
import com.langla.utlis.UTPKoolVN;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Char extends Entity {

    @JsonIgnore
    public Client client;
    @JsonIgnore
    public InfoTuongKhac TuongKhac;

    @JsonIgnore
    public Info info;
    @JsonIgnore
    public Trade traDe;

    @JsonIgnore
    public int baseIdThu = 0;

    @JsonIgnore
    public Map.Zone zone;
    @JsonIgnore
    public KhoBauTpl quayKhoBau;
    @JsonIgnore
    public ExecutorService executor; // Khai báo executor

    public int id;
    @JsonIgnore
    public int lastMapID;

    public InfoChar infoChar;

    public Skill[] arraySkill;

    public Skill skillFight;

    public ArrayList<Effect> listEffect;

    public int[] arrayTiemNang;
    public Item[] arrItemBag;
    public Item[] arrItemBox;

    public Item[] arrItemBody;
    public Item[] arrItemBody2;
    public Item[] arrItemExtend;
    public ArrayList<SkillClan> listSkillViThu;
    public ArrayList<DanhHieu> listDanhHieu;
    public ArrayList<Thu> listThu;

    public ArrayList<Friend> listFriend;

    public ArrayList<Enemy> listEnemy;

    public PhucLoi phucLoi;
    public List<Integer> logUseItem;
    public Skill[] skills_0 = new Skill[] {
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.KIEM_THUAT_CO_BAN, 1), };
    public Skill[] skills_1 = new Skill[] {
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.KIEM_THUAT_CO_BAN, 1),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.TAM_TRUNG_OAN_THU_THUAT, 0),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.KIEM_THUAT_TAM_PHAP, 0),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.DICH_CHUYEN_CHI_THUAT, 0),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.LOI_KIEM, 0),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.TRIEU_HOI_CHIM_CHI_THUAT, 0) };
    public Skill[] skills_2 = new Skill[] {
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.KIEM_THUAT_CO_BAN, 1),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.SONG_THANG_LONG, 0),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.TIEU_THUAT_TAM_PHAP, 0),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.AN_CHU_CHI_THUAT, 0),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.ANH_PHONG_XA, 0),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.TRIEU_HOI_DOI_CHI_THUAT, 0), };
    public Skill[] skills_3 = new Skill[] {
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.KIEM_THUAT_CO_BAN, 1),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.THUY_LONG_AN_THUAT, 0),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.GAY_THUAT_TAM_PHAP, 0),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.TANG_SINH_CHI_THUAT, 0),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.AI_BOC_BO_THUAT, 0),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.THUY_LAO_THUAT, 0) };
    public Skill[] skills_4 = new Skill[] {
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.KIEM_THUAT_CO_BAN, 1),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.AM_SAT_THUAT, 0),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.AO_THUAT_TAM_PHAP, 0),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.AN_THAN_CHI_THUAT, 0),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.HAO_HOA_CAU_THUAT, 0),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.TRIEU_HOI_CHIM_YEU_CHI_THUAT, 0) };
    public Skill[] skills_5 = new Skill[] {
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.KIEM_THUAT_CO_BAN, 1),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.THIEN_SAT_THUY_PHI, 0),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.DAO_THUAT_TAM_PHAP, 0),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.ANH_PHAN_THAN_CHI_THUAT, 0),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.PHI_LOI_THAN_THUAT, 0),
            DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.HIEN_NHAN_THUAT, 0) };

    public Char(Client client) {
        this.client = client;
        arrItemBag = new Item[27];

        arrItemBox = new Item[36];

        infoChar = new InfoChar();

        TuongKhac = new InfoTuongKhac();

        info = new Info();

        traDe = new Trade();

        listEffect = new ArrayList<Effect>();

        arrayTiemNang = new int[4];

        arrItemBody = new Item[17];

        arrItemBody2 = new Item[17];

        arrItemExtend = new Item[3];

        listSkillViThu = new ArrayList<SkillClan>();

        listDanhHieu = new ArrayList<DanhHieu>();

        listThu = new ArrayList<Thu>();

        listFriend = new ArrayList<Friend>();

        listEnemy = new ArrayList<Enemy>();

        phucLoi = new PhucLoi();

        quayKhoBau = new KhoBauTpl();

        logUseItem = new ArrayList<>();
    }

    public Char() {
        arrItemBag = new Item[27];

        arrItemBox = new Item[36];

        TuongKhac = new InfoTuongKhac();

        infoChar = new InfoChar();

        info = new Info();

        traDe = new Trade();

        listEffect = new ArrayList<Effect>();

        arrayTiemNang = new int[4];

        arrItemBody = new Item[17];

        arrItemBody2 = new Item[17];

        arrItemExtend = new Item[3];

        listSkillViThu = new ArrayList<SkillClan>();

        listDanhHieu = new ArrayList<DanhHieu>();

        listThu = new ArrayList<Thu>();

        listFriend = new ArrayList<Friend>();

        phucLoi = new PhucLoi();

        quayKhoBau = new KhoBauTpl();

        listEnemy = new ArrayList<Enemy>();

        logUseItem = new ArrayList<>();
    }

    public byte getLopFormSelectChar(byte i) {
        switch (i) {
            case 0:
            case 5:
                return 1;
            case 1:
            case 6:
                return 2;
            case 2:
            case 7:
                return 3;
            case 3:
            case 8:
                return 4;
            case 4:
                return 5;

        }
        return 0;
    }

    public void selectCaiTrang(byte selectCaiTrang) {
        infoChar.selectCaiTrang = selectCaiTrang;
        client.session.serivce.updateSelectCaiTrang((byte) infoChar.selectCaiTrang);

    }

    public void updateItemBody(Item item) {
        try {
            Writer writer = new Writer();
            item.write(writer);
            client.session.sendMessage(new Message((byte) -21, writer));
        } catch (Exception ex) {
            Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void writeMe(Writer writer) throws java.io.IOException {
        setUpInfo(false);
        writer.writeUTF(infoChar.username);
        writer.writeInt(id);
        writer.writeUTF(infoChar.name);
        writer.writeByte(infoChar.gioiTinh);
        writer.writeByte(infoChar.idNVChar);
        writer.writeByte(infoChar.idhe);
        writer.writeByte(infoChar.idClass);
        writer.writeByte(info.typePK);
        writer.writeByte(infoChar.lvPk);
        writer.writeInt(infoChar.taiPhu);
        writer.writeShort(infoChar.speedMove);
        writer.writeByte(infoChar.sachChienDau);
        writer.writeInt(infoChar.hp);
        writer.writeInt(infoChar.hpFull);
        writer.writeInt(infoChar.mp);
        writer.writeInt(infoChar.mpFull);
        writer.writeLong(infoChar.exp);
        writer.writeInt(infoChar.bac);
        writer.writeInt(infoChar.bacKhoa);
        writer.writeInt(infoChar.vang);
        writer.writeInt(infoChar.vangKhoa);
        writer.writeShort(infoChar.idTask);
        writer.writeByte(infoChar.idStep);
        writer.writeShort(infoChar.requireTask);
        writer.writeInt(infoChar.hoatLuc);
        writer.writeInt(infoChar.pointNAP);
        writer.writeShort(arrItemBag.length);

        ArrayList<Item> listItem = new ArrayList<Item>();
        Utlis.getArrayListNotNull(arrItemExtend, listItem);
        writer.writeByte(listItem.size());
        for (int i = 0; i < listItem.size(); i++) {
            Item item = listItem.get(i);
            writer.writeShort(item.id);
            writer.writeBoolean(item.isLock);
            writer.writeByte(item.index);
        }
        writeItemBody(writer, arrItemBody);
        writeItemBody(writer, arrItemBody2);
        writeItemBag(writer, arrItemBag);

        writeEffect(writer);

        writeThu(writer);
        writeFriend(writer);
        writeEnemy(writer);

        writeSkill(writer);

        writeDanhHieu(writer);

        writer.writeByte(infoChar.rank);

        writer.writeByte(infoChar.selectCaiTrang);

        writer.writeInt(infoChar.timeChatColor);

        writer.writeByte(0); // numTaskDoneKTNG
        writer.writeBoolean(false); // doneTaskKTNG
        writeSkillViThu(writer);
    }

    public void write(Writer writer) throws java.io.IOException {
        writer.writeByte(status);
        writer.writeUTF(infoChar.name);
        writer.writeByte(infoChar.idNVChar);
        writer.writeByte(infoChar.gioiTinh);
        writer.writeByte(infoChar.idClass);
        writer.writeByte(info.typePK);
        writer.writeByte(infoChar.lvPk);
        writer.writeShort(infoChar.speedMove);

        writer.writeInt(infoChar.hp);
        writer.writeInt(infoChar.hpFull);
        writer.writeInt(infoChar.mp);
        writer.writeInt(infoChar.mpFull);

        writer.writeLong(infoChar.exp);

        writer.writeShort(cx);
        writer.writeShort(cy);

        writer.writeByte(infoChar.statusGD);

        writeItemBody(writer, arrItemBody);

        writeEffect(writer);

        writeDanhHieu(writer);

        writer.writeByte(infoChar.rank);

        writer.writeByte(infoChar.selectCaiTrang);
        //
        // writer.writeInt(infoChar.timeChatColor);

    }

    public void writeView(Writer writer) throws java.io.IOException {

        writer.writeUTF(infoChar.name);
        writer.writeLong(infoChar.exp);
        writer.writeByte(infoChar.idNVChar);
        writer.writeByte(infoChar.idhe);
        writer.writeByte(infoChar.idClass);
        writer.writeByte(infoChar.gioiTinh);
        writer.writeByte(infoChar.sachChienDau);
        writeItemBody(writer, arrItemBody);
        writeItemBody(writer, arrItemBody2);
        writer.writeShort(arraySkill.length);
        for (int i = 0; i < arraySkill.length; i++) {
            writer.writeShort(arraySkill[i].id);
        }
        writer.writeUTF(infoChar.familyName); // gia tộc
        if (infoChar.familyName.length() > 0) {
            FamilyTemplate giatoc = Family.gI().getGiaToc(this);
            if (giatoc != null) {
                Family_Member member = Family.gI().getMe(this, giatoc);
                if (member != null) {
                    writer.writeUTF(infoChar.familyName);
                    writer.writeByte(member.role);
                }
            }
        }
        writeDanhHieu(writer);
        writer.writeByte(infoChar.selectCaiTrang);
        writeSkillViThu(writer);
        writer.writeByte(infoChar.levelPhanThan);
        writer.writeByte(infoChar.MaxLevelPhanThan);
        writer.writeInt(infoChar.expPhanThan);
        writer.writeByte(0);
        writer.writeByte(0);
    }

    public void writeInfo(Writer writer) throws java.io.IOException {
        writer.writeInt(infoChar.hpFull);
        writer.writeInt(infoChar.hp);
        writer.writeInt(infoChar.mpFull);
        writer.writeInt(infoChar.mp);
        writer.writeByte(infoChar.lvPk);
        writer.writeShort(getPlusExp()); // kinh nghiệm đánh quái
        writer.writeInt(infoChar.taiPhu); // tài phú
        writer.writeShort(infoChar.chuyenCan); // chuyên cần
        writer.writeShort(infoChar.chuyenCanTuan); // chuyên cần tuần
        writer.writeLong(infoChar.cuaCai); // điểm của cải
        writer.writeInt(infoChar.cuaCaiTuan); // của cải tuần
        writer.writeInt(infoChar.tongCuongHoa); // điểm cường hóa
        writer.writeInt(infoChar.cuongHoaTuan); // điểm cường hóa tuần
        writer.writeShort(0); // lôi đài tháng
        writer.writeLong(0); // điểm luyện tập
        writer.writeInt(infoChar.tongVangNap); // điểm nạp nhiều
        writer.writeShort(infoChar.diem_Hokage[0]); // hokage đai trán
        writer.writeShort(infoChar.diem_Hokage[2]); // hokage áo
        writer.writeShort(infoChar.diem_Hokage[4]); // hokage bao tay
        writer.writeShort(infoChar.diem_Hokage[6]); // hokage quần
        writer.writeShort(infoChar.diem_Hokage[8]); // hokage giày
        writer.writeShort(infoChar.diem_Hokage[1]); // hokage vũ khí
        writer.writeShort(infoChar.diem_Hokage[3]); // hokage dây thừng
        writer.writeShort(infoChar.diem_Hokage[5]); // hokage móc sắt
        writer.writeShort(infoChar.diem_Hokage[7]); // hokage ống tiêu
        writer.writeShort(infoChar.diem_Hokage[9]); // hokage túi nhẫn giả
        writer.writeByte(0); // tiềm năng sd
        writer.writeByte(0); // tiềm năng trung
        writer.writeByte(0); // tiềm năng cao
        writer.writeByte(0); // kỹ năng sd
        writer.writeByte(0); // kỹ năng trung
        writer.writeByte(0); // kỹ năng cao
        writer.writeByte(0); // bánh ít bảo
        writer.writeByte(0);
        writer.writeByte(0);
        writer.writeByte(0);
    }

    public void writeItemBody(Writer writer, Item[] arrItem) throws IOException {
        ArrayList<Item> listItem = new ArrayList<Item>();
        Utlis.getArrayListNotNull(arrItem, listItem);
        writer.writeByte(listItem.size());
        for (int i = 0; i < listItem.size(); i++) {
            Item item = listItem.get(i);
            writer.writeShort(item.id);
            writer.writeBoolean(item.isLock);
            writer.writeLong(item.expiry);
            writer.writeByte(item.he);
            writer.writeByte(item.level);
            writer.writeUTF(item.strOptions);
        }
    }

    public static void readItemBody(Message var0, Item[] var1) throws java.io.IOException {
        for (int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2] = null;
        }

        byte var6 = var0.readByte();

        for (int var4 = 0; var4 < var6; ++var4) {
            Item var3;
            (var3 = new Item()).id = var0.readShort();
            var3.isLock = var0.readBoolean();
            var3.expiry = var0.readLong();
            var3.he = var0.readByte();
            var3.level = var0.readByte();
            var3.index = var3.getItemTemplate().type;
            var3.strOptions = var0.reader.readUTF();
            var1[var3.index] = var3;
        }

    }

    public void writeEffect(Writer writer) throws IOException {
        writer.writeByte(listEffect.size());
        for (int i = 0; i < listEffect.size(); i++) {
            Effect eff = listEffect.get(i);
            /*
             * var1.readShort(), var1.readInt(), var1.readLong(), var1.readInt()
             */
            eff.write(writer);
        }
    }

    public void writeDanhHieu(Writer writer) throws IOException {

        writer.writeByte(listDanhHieu.size());

        for (DanhHieu danhHieu : listDanhHieu) {
            writer.writeUTF(danhHieu.name);
            writer.writeInt(danhHieu.hsd);
            if (danhHieu.name.startsWith(" ")) {
                writer.writeInt(danhHieu.detail);
            }
        }
        if (infoChar.selectDanhHieu > listDanhHieu.size())
            infoChar.selectDanhHieu = -1;
        writer.writeByte(infoChar.selectDanhHieu);
    }

    public void writeSkillViThu(Writer writer) throws IOException {

        writer.writeByte(listSkillViThu.size());

        for (SkillClan s : listSkillViThu) {
            writer.writeByte(s.id);
            writer.writeByte(s.levelNeed);

        }
    }

    public void writeItemBag(Writer writer, Item[] arrItem) throws IOException {
        ArrayList<Item> listItem = new ArrayList<Item>();
        Utlis.getArrayListNotNull(arrItem, listItem);
        writer.writeShort(listItem.size());
        for (int i = 0; i < listItem.size(); i++) {
            Item item = listItem.get(i);
            item.write(writer);
        }
    }

    public static void readItemBag(Message var0, Item[] var1) throws java.io.IOException {
        for (int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2] = null;
        }

        short var6 = var0.readShort();

        for (int var4 = 0; var4 < var6; ++var4) {
            Item var3;
            (var3 = new Item()).read(var0);

            try {
                var1[var3.index] = var3;
            } catch (Exception ex) {
                Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

    }

    public void writeFriend(Writer writer) throws IOException {
        writer.writeShort(listFriend.size());
        for (int i = listFriend.size() - 1; i >= 0; i--) {
            Friend friend = listFriend.get(i);
            writer.writeUTF(friend.name);
            writer.writeByte(friend.stt); // 2 mời kết bạn 1, đã là bạn

            if (PlayerManager.getInstance().getChar(friend.name) != null) {
                writer.writeBoolean(true);
            } else {
                writer.writeBoolean(false); // onlie
            }

        }
    }

    public void writeEnemy(Writer writer) throws IOException {
        writer.writeShort(listEnemy.size());
        for (int i = listEnemy.size() - 1; i >= 0; i--) {
            Enemy e = listEnemy.get(i);
            writer.writeUTF(e.name);

            if (PlayerManager.getInstance().getChar(e.name) != null) {
                writer.writeBoolean(true);
            } else {
                writer.writeBoolean(false); // onlie
            }

        }

    }

    public void writeSkill(Writer writer) throws IOException {
        writer.writeShort(skillFight.id);
        writer.writeShort(arraySkill.length);
        for (int i = 0; i < arraySkill.length; i++) {
            writer.writeShort(arraySkill[i].id);
        }
    }

    public void writePhucLoi(Writer msg) throws IOException {
        msg.writeInt(phucLoi.thoigianOnlineHomNay);
        msg.writeByte(phucLoi.soNgayOnlineLienTuc);
        msg.writeInt(phucLoi.vangNapTichLuy);
        msg.writeInt(com.langla.data.center.PhucLoi.getInstance().phucLoiInfo.TongRank);
        msg.writeInt(phucLoi.vangNapHomNay);
        msg.writeInt(phucLoi.vangNapTuan);
        msg.writeInt(phucLoi.vangTieuHomNay);
        msg.writeInt(phucLoi.vangTieuTuan);
        msg.writeLong(phucLoi.timeTheThang);
        msg.writeLong(phucLoi.timeTheVinhVinhVien);
        msg.writeInt(com.langla.data.center.PhucLoi.getInstance().phucLoiInfo.TongSoLanMuaTheThang);
        msg.writeBoolean(phucLoi.isGoiHaoHoa);
        msg.writeBoolean(phucLoi.isGoiChiTon);
        msg.writeInt(com.langla.data.center.PhucLoi.getInstance().phucLoiInfo.TongDauTu);
        msg.writeByte(com.langla.data.center.PhucLoi.getInstance().phucLoiInfo.RankCaoNhat);
        msg.writeBoolean(true);
        msg.writeLong(1794842061074L);
        msg.writeLong(-1);
        msg.writeLong(1794842061074L);
        msg.writeLong(1794842061074L);
        msg.writeLong(1794842061074L);
        msg.writeByte(phucLoi.solanQuay);
        msg.writeByte(phucLoi.soNgayNapLienTuc);
        msg.writeLong(1794842061074L);

        writePhucLoiData(msg);

        msg.writeLong(1794842061074L);
        msg.writeLong(1794842061074L);
        msg.writeLong(1794842061074L);
        msg.writeLong(1794842061074L);
        msg.writeLong(12345);
        msg.writeLong(44444);
        msg.writeLong(123);
        msg.writeLong(31000000000L);
        msg.writeInt(phucLoi.diemTichLuyVongQuay);
        msg.writeLong(31000000000L);
        msg.writeLong(31000000000L);
        msg.writeLong(31000000000L);
        msg.writeLong(31000000000L);
        msg.writeLong(com.langla.data.center.PhucLoi.getInstance().phucLoiInfo.ThoiGianX2Online);
    }

    public void writePhucLoiData(Writer msg) throws IOException {
        msg.writeShort(com.langla.data.center.PhucLoi.getInstance().DataPhucLoi.size());
        for (int i = 0; i < com.langla.data.center.PhucLoi.getInstance().DataPhucLoi.size(); i++) {
            PhucLoiTpl phucLoi = com.langla.data.center.PhucLoi.getInstance().DataPhucLoi.get(i);
            msg.writeShort(phucLoi.idRequest);
            msg.writeUTF(phucLoi.nameDieuKien);
            msg.writeShort(phucLoi.idLoai);
            Item item = new Item(phucLoi.idItem, true, phucLoi.amount);
            item.strOptions = phucLoi.STROP;
            item.expiry = phucLoi.expiry;
            item.write(msg);
            msg.writeBoolean(isCheckPhucLoi(phucLoi));
        }
    }

    public boolean isCheckPhucLoi(PhucLoiTpl pl_tpl) {

        if (isLogPhucLoi(pl_tpl.idRequest, pl_tpl.idLoai))
            return false;
        if (pl_tpl.idLoai == 0) { // online phút
            int min = Utlis.millisecondsToMinutes(phucLoi.thoigianOnlineHomNay);
            return min >= pl_tpl.dieuKien;
        } else if (pl_tpl.idLoai == 1) { // online 7 ngày
            return phucLoi.soNgayOnlineLienTuc >= pl_tpl.dieuKien;
        } else if (pl_tpl.idLoai == 2) { // thăng cấp
            return this.level() >= pl_tpl.dieuKien;
        } else if (pl_tpl.idLoai == 3) { // nạp rank
            return phucLoi.vangNapTichLuy >= pl_tpl.dieuKien;
        } else if (pl_tpl.idLoai == 4) { // rank tất cả
            return com.langla.data.center.PhucLoi.getInstance().phucLoiInfo.TongRank >= pl_tpl.dieuKien;
        } else if (pl_tpl.idLoai == 5) { // nạp ngày
            return phucLoi.vangNapHomNay >= pl_tpl.dieuKien;
        } else if (pl_tpl.idLoai == 6) { // nạp tuần
            return phucLoi.vangNapTuan >= pl_tpl.dieuKien;
        } else if (pl_tpl.idLoai == 7) { //
            // tiêu ngày
            return phucLoi.vangTieuHomNay >= pl_tpl.dieuKien;
        } else if (pl_tpl.idLoai == 8) { // tiêu tuần
            return phucLoi.vangTieuTuan >= pl_tpl.dieuKien;
        } else if (pl_tpl.idLoai == 15 && phucLoi.isGoiHaoHoa) { // gói hào hoa
            return this.level() >= pl_tpl.dieuKien;
        } else if (pl_tpl.idLoai == 16 && phucLoi.isGoiChiTon) { // gói chí tôn
            return this.level() >= pl_tpl.dieuKien;
        } else if (pl_tpl.idLoai == 17) { // quà tất cả đầu tư
            return com.langla.data.center.PhucLoi.getInstance().phucLoiInfo.TongDauTu >= pl_tpl.dieuKien;
        } else if (pl_tpl.idLoai == 18) { // quà tất cả thẻ tháng
            return com.langla.data.center.PhucLoi.getInstance().phucLoiInfo.TongSoLanMuaTheThang >= pl_tpl.dieuKien;
        } else if (pl_tpl.idLoai == 19) { // nạp liên tục
            return phucLoi.soNgayNapLienTuc >= pl_tpl.dieuKien;
        } else if (pl_tpl.idLoai == 20) { // rank chung
            return com.langla.data.center.PhucLoi.getInstance().phucLoiInfo.RankCaoNhat >= pl_tpl.dieuKien;
        } else if (pl_tpl.idLoai == 21) { // nạp 3 mốc
            return phucLoi.vangNapMoc >= pl_tpl.dieuKien;
        } else if (pl_tpl.idLoai == 22) { // nạp đơn
            return phucLoi.vangNapDon >= pl_tpl.dieuKien;
        }
        return false;
    }

    public void updateInfoToFamily() {
        if (infoChar.familyId != -1) {
            FamilyTemplate giaToc = Family.gI().getGiaToc(this);
            if (giaToc != null) {
                Family_Member getMem = Family.gI().getMe(this, giaToc);
                if (getMem != null) {
                    getMem.infoChar = infoChar;
                } else {
                    client.session.serivce.NhacNhoMessage("Bạn đã bị kick khỏi gia tộc " + infoChar.familyName);
                    infoChar.familyId = -1;
                    infoChar.familyName = "";
                    client.session.serivce.sendInfoGiaTocAllChar(this);
                    setUpInfo(true);
                }
            } else {
                client.session.serivce.NhacNhoMessage("Gia tộc " + infoChar.familyName + " đã bị giải tán.");
                infoChar.familyId = -1;
                infoChar.familyName = "";
                client.session.serivce.sendInfoGiaTocAllChar(this);
                setUpInfo(true);
            }
        }
    }

    public void updatePhucLoiHangNgay() {
        LocalDate currentDate = LocalDate.now();
        if (phucLoi.lastDailyUpdate == null || !phucLoi.lastDailyUpdate.isEqual(currentDate)) {
            // Nếu ngày cuối cùng cập nhật không phải hôm nay, cập nhật biến và ghi lại ngày
            synchronized (phucLoi.logData) {
                phucLoi.logData.removeIf(data -> data.idLoai == 0 || data.idLoai == 5 || data.idLoai == 7);
            }
            //
            int vang = 0;
            if (phucLoi.timeTheThang > System.currentTimeMillis()) {
                this.addVangKhoa(30, false, false, "Thẻ tháng hàng ngày");
                vang += 30;
            }
            if (phucLoi.timeTheThang >= 0L) {
                this.addVangKhoa(20, false, false, "Thẻ vĩnh viễn hàng ngày");
                vang += 20;
            }
            if (vang > 0) {
                client.session.serivce
                        .NhacNhoMessage("Bạn nhận được +" + vang + " vàng khóa từ Thẻ phúc lợi hàng ngày");
            }
            phucLoi.vangTieuHomNay = 0;
            phucLoi.vangNapHomNay = 0;
            phucLoi.thoigianOnlineHomNay = 0;
            phucLoi.lastDailyUpdate = currentDate;

            // update thông tin hàng ngày
            if (infoChar.lvPk > 0)
                infoChar.lvPk -= 1;
            infoChar.soLanCamThuat = 1;
            infoChar.sdIzanami = 0;
            infoChar.sdIzanami2 = 0;
            infoChar.isGoiPhanThanFree = false;

            for (int i = 0; i < listEffect.size(); i++) {
                Effect effect = listEffect.get(i);
                if (effect.id == 99) { // xóa phân thân sang ngày hôm sau
                    listEffect.remove(effect);
                    break;
                }
            }
        }

        // So sánh ngày cuối cùng đăng nhập với ngày hiện tại
        currentDate = LocalDate.now();
        if (phucLoi.lastLoginDate != null && phucLoi.lastLoginDate.plusDays(1).isEqual(currentDate)) {
            phucLoi.soNgayOnlineLienTuc += 1;
        } else {
            phucLoi.soNgayOnlineLienTuc = 1;
        }
        phucLoi.lastLoginDate = currentDate;
    }

    public void updatePhucLoiHangTuan() {
        LocalDate currentDate = LocalDate.now();

        // Tìm ngày đầu tiên của tuần mới
        LocalDate startOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        // Kiểm tra nếu đã trôi qua ít nhất một tuần từ lần cập nhật trước đó
        if (phucLoi.lastWeeklyUpdate == null || startOfWeek.isAfter(phucLoi.lastWeeklyUpdate)) {
            synchronized (phucLoi.logData) {
                phucLoi.logData.removeIf(data -> data.idLoai == 6 || data.idLoai == 8);
            }
            // Cập nhật ngày reset cuối cùng
            phucLoi.vangNapTuan = 0;
            phucLoi.vangTieuTuan = 0;
            infoChar.cuaCaiTuan = 0;
            infoChar.cuongHoaTuan = 0;
            infoChar.chuyenCanTuan = 0;
            phucLoi.lastWeeklyUpdate = startOfWeek;
        }
    }

    public boolean isLogPhucLoi(int idRequest, int idloai) {
        int log = 0;
        synchronized (phucLoi.logData) {
            for (int i = 0; i < phucLoi.logData.size(); i++) {
                LogPhucLoi data = phucLoi.logData.get(i);
                if (idRequest == data.idRequest && idloai == data.idLoai) {
                    log++;
                }
            }
        }
        if (log <= 0) {
            return false;
        } else {
            if (com.langla.data.center.PhucLoi.getInstance().phucLoiInfo.ThoiGianX2Online > System.currentTimeMillis()
                    && idloai == 0) {
                if (log >= 2) {
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        }
    }

    public void nhanPhucLoi(short idRequest) {
        if (getCountNullItemBag() == 0) {
            client.session.serivce.ShowMessGold("Túi không đủ chỗ chứa");
            return;
        }
        PhucLoiTpl phucLoiTpl = DataCenter.gI().getPhucLoi_Tpl(idRequest);
        if (phucLoiTpl == null)
            return;
        if (!isCheckPhucLoi(phucLoiTpl))
            return;
        synchronized (phucLoi.logData) {
            LogPhucLoi logNew = new LogPhucLoi();
            logNew.idRequest = phucLoiTpl.idRequest;
            logNew.idLoai = phucLoiTpl.idLoai;
            phucLoi.logData.add(logNew);
        } // thêm vào log
        if (phucLoiTpl.idItem == 163) {
            this.addBacKhoa(phucLoiTpl.amount, true, true, "Nhận phúc lợi");
        } else if (phucLoiTpl.idItem == 192) {
            this.addVangKhoa(phucLoiTpl.amount, true, true, "Nhận phúc lợi");
        } else {
            Item newItem = new Item(phucLoiTpl.idItem, true);
            newItem.amount = phucLoiTpl.amount;
            newItem.strOptions = phucLoiTpl.STROP;
            if (phucLoiTpl.expiry > 0)
                newItem.expiry = System.currentTimeMillis() + phucLoiTpl.expiry;
            this.addItem(newItem, "Nhận phúc lợi");
            this.msgAddItemBag(newItem);
        }
        if (phucLoiTpl.idRequest == 15) {
            synchronized (phucLoi.logData) {
                phucLoi.logData.removeIf(data -> data.idLoai == 1);
                phucLoi.soNgayOnlineLienTuc = 1;
            }
        } else if (phucLoiTpl.idRequest == 103) {
            synchronized (phucLoi.logData) {
                phucLoi.logData.removeIf(data -> data.idLoai == 19);
                phucLoi.soNgayNapLienTuc = 0;
            }
        }
        client.session.serivce.sendPhucLoi(this);
    }

    public void writeThu(Writer writer) throws IOException {
        writer.writeShort(listThu.size());
        for (int i = listThu.size() - 1; i >= 0; i--) {
            Thu thu = listThu.get(i);
            writer.writeShort(thu.id); // id thư
            writer.writeBoolean(thu.dadoc); // false là chưa đọc
            writer.writeUTF(thu.nguoiGui); // người gửi
            writer.writeUTF(thu.chuDe); // chủ đề
            writer.writeUTF(thu.noiDung); // nội dung
            writer.writeInt(thu.bac); // bạc đính kèm
            writer.writeInt(thu.bacKhoa); // bạc khóa đính kèm
            writer.writeInt(thu.vang); // vàng đính kèm
            writer.writeInt(thu.vangKhoa); // vàng khóa đính kèm
            writer.writeLong(thu.exp); // exp đính kèm
            writer.writeInt(thu.time); // thời gian còn lại dạng giây
            if (thu.item == null) {
                writer.writeShort(-1);
            } else {
                thu.item.write(writer);
            }
        }

    }

    public void guiThu(String chude, Char nguoinhan, String noidung, int bacdinhkem, short indexitem) {
        long bacmine = 10;
        if (bacdinhkem > 0) {
            double lephi = bacdinhkem * 0.01;
            bacmine += lephi + bacdinhkem;
        }
        if (infoChar.bac < bacmine) {
            client.session.serivce.ShowMessGold("Không đủ bạc");
        } else if (indexitem >= 0 && getItemBagByIndex(indexitem) == null) {
            client.session.serivce.ShowMessGold("Vật phẩm không tồn tại");
        } else if (indexitem >= 0 && getItemBagByIndex(indexitem).isLock) {
            client.session.serivce.ShowMessGold("Không thể gửi vật phẩm này");
        } else {
            Thu thu = new Thu();
            thu.id = nguoinhan.baseIdThu++;
            thu.chuDe = chude;
            thu.nguoiGui = this.infoChar.name;
            thu.noiDung = noidung;
            if (bacdinhkem > 0) {
                thu.bac = bacdinhkem;
            }
            mineBac(bacmine, false, false, "Gửi thư");
            if (indexitem >= 0) {
                thu.item = getItemBagByIndex(indexitem);
                this.removeItemBagByIndex(indexitem, "Gửi thư");
            }
            nguoinhan.listThu.add(thu);
            nguoinhan.client.session.serivce.updateThu();
            client.session.serivce.msgUpdateGuiThu(this.infoChar.bac, this.infoChar.bacKhoa, indexitem);
        }

    }

    public void nhanItemThu(short id) {
        for (int i = 0; i < listThu.size(); i++) {
            Thu mail = listThu.get(i);
            if (mail != null && mail.id == id) {
                if (mail.bac > 0) {
                    if (addBac(mail.bac, true, true, "Nhận từ thư")) {
                        mail.bac = 0;
                    }
                }
                if (mail.bacKhoa > 0) {
                    if (addBacKhoa(mail.bacKhoa, true, true, "Nhận từ thư")) {
                        mail.bacKhoa = 0;
                    }
                }
                if (mail.vang > 0) {
                    if (addVang(mail.vang, true, true, "Nhận từ thư")) {
                        mail.vang = 0;
                    }
                }
                if (mail.vangKhoa > 0) {
                    if (addVangKhoa(mail.vangKhoa, true, true, "Nhận từ thư")) {
                        mail.vangKhoa = 0;
                    }
                }
                if (mail.exp > 0) {
                    addExp(mail.exp);
                    mail.exp = 0;
                }
                if (mail.item != null) {
                    if (addItem(mail.item, "Nhận từ thư: " + mail.chuDe)) {
                        msgAddItemBag(mail.item);
                        mail.item = null;
                    }
                }
                client.session.serivce.updateThu();
                break;
            }
        }
    }

    public void nhanAllItemThu() {
        for (int i = 0; i < listThu.size(); i++) {
            Thu mail = listThu.get(i);
            if (mail != null) {
                if (mail.bac > 0) {
                    if (addBac(mail.bac, true, true, "Nhận từ thư")) {
                        mail.bac = 0;
                    }
                }
                if (mail.bacKhoa > 0) {
                    if (addBacKhoa(mail.bacKhoa, true, true, "Nhận từ thư")) {
                        mail.bacKhoa = 0;
                    }
                }
                if (mail.vang > 0) {
                    if (addVang(mail.vang, true, true, "Nhận từ thư")) {
                        mail.vang = 0;
                    }
                }
                if (mail.vangKhoa > 0) {
                    if (addVangKhoa(mail.vangKhoa, true, true, "Nhận từ thư")) {
                        mail.vangKhoa = 0;
                    }
                }
                if (mail.exp > 0) {
                    addExp(mail.exp);
                    mail.exp = 0;
                }
                if (mail.item != null) {
                    if (addItem(mail.item, "Nhận từ thư: " + mail.chuDe)) {
                        msgAddItemBag(mail.item);
                        mail.item = null;
                    }
                }
            }
        }
        client.session.serivce.updateThu();
    }

    public void updateDocThu(short id) {

        for (int i = 0; i < listThu.size(); i++) {
            if (listThu.get(i).id == id) {
                listThu.get(i).dadoc = true;
                break;
            }
        }
    }

    public void removeThu(short id) {
        for (int i = 0; i < listThu.size(); i++) {
            if (listThu.get(i).id == id) {
                listThu.remove(i);
                break;
            }
        }
    }

    public Skill getSkillWithIdTemplate(int id) {
        for (int i = 0; i < arraySkill.length; i++) {
            if (arraySkill[i].idTemplate == id) {
                return arraySkill[i];
            }
        }
        return null;
    }

    public void clean() {
        infoChar.cx = cx;
        infoChar.cy = cy;
        if (infoChar.groupId != -1) {
            GroupTemplate group = Group.gI().getGroup(infoChar.groupId);
            if (group != null) {
                group.out(client);
            }
        }
        if (zone != null) {
            if (infoChar.isDie)
                client.mChar.reSpawn();
            zone.removeChar(client);
        }

    }

    public void setAttackMob(Mob mob, int dame, boolean chi_mang) {
        if (infoChar.isDie)
            return;
        if (mob.hp > 0) {
            if (mob.IsBong) { // hieu ung bong
                dame *= 2;
            }
            if (getSatThuongChuyenHp() > 0) {
                int hpPlus = dame * client.mChar.getSatThuongChuyenHp() / 100;
                PlusHp(hpPlus);
                msgUpdateHp();
            }
            if (mob.PhanDon > 0) {
                int phan = dame * mob.PhanDon / 100;
                if (zone.map.mapID == 47 && info.isBiDuoc) {

                } else {
                    MineHpPhanDon(phan, false, mob);
                }
            }
            if (mob.NeTranh > 0) {
                int phan = dame * mob.PhanDon / 100;
                MineHpPhanDon(phan, false, mob);
            }
            zone.setDameMob(client, zone, mob, dame, chi_mang);
        }
    }

    public void setAttackPlayer(Char player, int dame, boolean chi_mang) {
        if (player.infoChar.isDie || infoChar.isDie)
            return;
        if (player.info.isBiBong) { // hieu ung bong
            dame *= 2;
        }
        if (player.client.isConnected() && !player.infoChar.isDie) {
            player.MineHp(dame);
            player.msgUpdateHpMpWhenAttack(chi_mang, infoChar.name);

            if (player.getPhanDon() > 0) { // phản đòn
                int damePhan = dame * player.getPhanDon() / 100;
                MineHp(damePhan);
                msgUpdateHpMpWhenAttack(chi_mang, player.infoChar.name);
            }
        }
    }

    public void MineHpPhanDon(int dame, boolean chi_mang, Mob mob) {
        if (infoChar.isDie)
            return;
        if (info.isBiBong) { // hieu ung bong
            dame *= 2;
        }
        MineHp(dame);
        msgUpdateHpMpWhenAttack(chi_mang, mob.getMobTemplate().name);
    }

    public void resetInfo() {
        this.infoChar.hpFull = (100 + (this.arrayTiemNang[3] * 10));

        this.infoChar.mpFull = (100 + (this.arrayTiemNang[2] * 10));

        this.infoChar.hpFull += arrayTiemNang[1] / 2;

        this.infoChar.mpFull += arrayTiemNang[1] / 3;
        //
        this.infoChar.hpRecv = 0;

        this.infoChar.mpRecv = 0;

        this.infoChar.speedMove = 600;

        this.infoChar.taiPhu = 0;

        this.infoChar.exp_plus = 0;

        if (this.infoChar.idClass == 1 || this.infoChar.idClass == 5) {
            this.TuongKhac.TanCong = arrayTiemNang[0];
        } else {
            this.TuongKhac.TanCong = (arrayTiemNang[2] - 5);
        }
        this.TuongKhac.TanCong += arrayTiemNang[1] / 2;

        this.TuongKhac.TanCongCoBan = 0;
        this.TuongKhac.TanCongQuai = 0;
        this.TuongKhac.PhatHuyLucDanhCoban = 0;
        this.TuongKhac.ChinhXac = 0;
        this.TuongKhac.BoQuaNeTranh = 0;
        this.TuongKhac.ChiMang = arrayTiemNang[1] / 3;
        this.TuongKhac.TangTanCongChiMang = 0;

        this.TuongKhac.TangDameTatCa = 0;
        this.TuongKhac.TangTanCongLenLoi = 0;
        this.TuongKhac.TangTanCongLenTho = 0;
        this.TuongKhac.TangTanCongLenThuy = 0;
        this.TuongKhac.TangTanCongLenHoa = 0;
        this.TuongKhac.TangTanCongLenPhong = 0;

        this.TuongKhac.GaySuyYeu = 0;
        this.TuongKhac.GayTrungDoc = 0;
        this.TuongKhac.GayLamCham = 0;
        this.TuongKhac.GayBong = 0;
        this.TuongKhac.GayChoang = 0;

        this.TuongKhac.BoQuaKhangTinh = 0;
        this.TuongKhac.KhangLoi = 0;
        this.TuongKhac.KhangTho = 0;
        this.TuongKhac.KhangThuy = 0;
        this.TuongKhac.KhangHoa = 0;
        this.TuongKhac.KhangPhong = 0;
        this.TuongKhac.GiamSatThuong = 0;

        this.TuongKhac.NeTranh = arrayTiemNang[1] / 3;
        this.TuongKhac.PhanDon = 0;
        this.TuongKhac.GiamTanCongKhiBiChiMang = 0;

        this.TuongKhac.TangTuongKhac = 0; // tương khắc lên hệ khi pem
        this.TuongKhac.GiamTuongKhac = 0; // giảm tương khắc lên hệ khi pem

        this.TuongKhac.GiamSuyYeu = 0;
        this.TuongKhac.GiamTrungDoc = 0;
        this.TuongKhac.GiamLamCham = 0;
        this.TuongKhac.GiamGayBong = 0;
        this.TuongKhac.GiamGayChoang = 0;
        this.TuongKhac.GiamTruChiMang = 0;
        this.TuongKhac.KhangTatCa = 0;
        this.TuongKhac.HieuUngNgauNhien = 0;
        this.TuongKhac.satThuongChuyenHp = 0;
    }

    public void setUpInfo(boolean isSendClient) {
        try {

            resetInfo();

            int hpMax = 0;

            int mpMax = 0;

            int phanDonPT = 0;

            for (int i = 0; i < this.arrItemBody.length; i++) {
                if (this.arrItemBody[i] != null) {

                    hpMax += this.arrItemBody[i].getChiSo(this.client, 32, 119);
                    mpMax += this.arrItemBody[i].getChiSo(this.client, 120, 33);
                    //
                    this.infoChar.hpFull += this.arrItemBody[i].getChiSo(this.client, 0, 18, 192, 202, 253, 106);
                    this.infoChar.hpFull += this.arrItemBody[i].getChiSo(1, this.client, 202);
                    this.infoChar.mpFull += this.arrItemBody[i].getChiSo(this.client, 1, 19, 107);

                    int chiSo209 = this.arrItemBody[i].getChiSo(this.client, 209);
                    this.infoChar.hpFull += chiSo209 / 2;

                    this.infoChar.mpFull += chiSo209 / 3;

                    this.infoChar.hpRecv += this.arrItemBody[i].getChiSo(this.client, 26, 136, 143, 200, 256);
                    this.infoChar.hpRecv += this.arrItemBody[i].getChiSo(1, this.client, 200);
                    this.infoChar.mpRecv += this.arrItemBody[i].getChiSo(this.client, 27, 137, 257);

                    this.infoChar.speedMove += this.arrItemBody[i].getChiSo(this.client, 17, 91, 118, 150);

                    this.infoChar.taiPhu += this.arrItemBody[i].getChiSo(this.client);

                    this.TuongKhac.TanCongCoBan += this.arrItemBody[i].getChiSo(client, 31);

                    int _dame = this.arrItemBody[i].getChiSoDame();
                    _dame += this.arrItemBody[i].getChiSo(1, client, 199);
                    _dame += chiSo209 / 2;

                    this.TuongKhac.TanCong += _dame;
                    this.TuongKhac.TanCongQuai += this.arrItemBody[i].getChiSoDameMob();

                    this.TuongKhac.ChinhXac += this.arrItemBody[i].getChiSo(this.client, 20);
                    this.TuongKhac.ChinhXac += this.arrItemBody[i].getChiSo(1, this.client, 205);
                    this.TuongKhac.BoQuaNeTranh += this.arrItemBody[i].getChiSo(this.client, 4, 147, 160);
                    this.TuongKhac.ChiMang += this.arrItemBody[i].getChiSo(this.client, 209) / 3;
                    this.TuongKhac.ChiMang += this.arrItemBody[i].getChiSo(this.client, 5, 15, 28, 63, 144, 166, 362);// array);
                    this.TuongKhac.ChiMang += this.arrItemBody[i].getChiSo(1, this.client, 203);
                    this.TuongKhac.TangTanCongChiMang += this.arrItemBody[i].getChiSo(this.client, 41, 309);// 41, 309);
                    this.TuongKhac.TangTanCongLenLoi += this.arrItemBody[i].getChiSo(this.client, 21, 113);// 21, 113);
                    this.TuongKhac.TangTanCongLenTho += this.arrItemBody[i].getChiSo(this.client, 22, 114);// 22, 114);
                    this.TuongKhac.TangTanCongLenThuy += this.arrItemBody[i].getChiSo(this.client, 23, 115);// 23, 115);
                    this.TuongKhac.TangTanCongLenHoa += this.arrItemBody[i].getChiSo(this.client, 24, 116);// 24, 116);
                    this.TuongKhac.TangTanCongLenPhong += this.arrItemBody[i].getChiSo(this.client, 25, 117);// 25,
                                                                                                             // 117);
                    this.TuongKhac.GaySuyYeu += this.arrItemBody[i].getChiSo(this.client, 48, 68, 123, 168, 185, 259);// 48,
                                                                                                                      // 68,
                                                                                                                      // 123,
                                                                                                                      // 168,
                                                                                                                      // 185,
                                                                                                                      // 259);
                    this.TuongKhac.GayTrungDoc += this.arrItemBody[i].getChiSo(this.client, 49, 69, 124, 169, 186, 260);// 49,
                                                                                                                        // 69,
                                                                                                                        // 124,
                                                                                                                        // 169,
                                                                                                                        // 186,
                                                                                                                        // 260);
                    this.TuongKhac.GayLamCham += this.arrItemBody[i].getChiSo(this.client, 50, 70, 125, 170, 187, 261);// 50,
                                                                                                                       // 70,
                                                                                                                       // 125,
                                                                                                                       // 170,
                                                                                                                       // 187,
                                                                                                                       // 261);
                    this.TuongKhac.GayBong += this.arrItemBody[i].getChiSo(this.client, 51, 71, 126, 171, 188, 262);// 51,
                                                                                                                    // 71,
                                                                                                                    // 126,
                                                                                                                    // 171,
                                                                                                                    // 188,
                                                                                                                    // 262);
                    this.TuongKhac.GayChoang += this.arrItemBody[i].getChiSo(this.client, 52, 72, 127, 172, 189, 263);// 52,
                                                                                                                      // 72,
                                                                                                                      // 127,
                                                                                                                      // 172,
                                                                                                                      // 189,
                                                                                                                      // 263);
                    this.TuongKhac.BoQuaKhangTinh += this.arrItemBody[i].getChiSo(this.client, 145, 149, 197, 360);// 145,
                                                                                                                   // 149,
                                                                                                                   // 197,
                                                                                                                   // 360);
                    this.TuongKhac.KhangLoi += this.arrItemBody[i].getChiSo(this.client, 7, 35, 82, 108);// 7, 35, 82,
                                                                                                         // 108);
                    this.TuongKhac.KhangTho += this.arrItemBody[i].getChiSo(this.client, 8, 36, 83, 109);// 8, 36, 83,
                                                                                                         // 109);
                    this.TuongKhac.KhangThuy += this.arrItemBody[i].getChiSo(this.client, 9, 37, 84, 110);// 9, 37, 84,
                                                                                                          // 110);
                    this.TuongKhac.KhangHoa += this.arrItemBody[i].getChiSo(this.client, 10, 38, 85, 111);// 10, 38, 85,
                                                                                                          // 111);
                    this.TuongKhac.KhangPhong += this.arrItemBody[i].getChiSo(this.client, 11, 39, 86, 112);// 11, 39,
                                                                                                            // 86, 112);
                    this.TuongKhac.NeTranh += this.arrItemBody[i].getChiSo(this.client, 64, 151, 161, 324, 14);// 64,
                                                                                                               // 151,
                                                                                                               // 161,
                                                                                                               // 204,
                                                                                                               // 324,
                                                                                                               // 14);
                    this.TuongKhac.NeTranh += this.arrItemBody[i].getChiSo(this.client, 209) / 3;
                    this.TuongKhac.NeTranh += this.arrItemBody[i].getChiSo(1, this.client, 204);//
                    this.TuongKhac.PhanDon += this.arrItemBody[i].getChiSo(this.client, 16);// 16);
                    phanDonPT += this.arrItemBody[i].getChiSo(this.client, 67, 162, 371, 373);
                    this.TuongKhac.GiamTanCongKhiBiChiMang += this.arrItemBody[i].getChiSo(this.client, 174, 42, 43, 44,
                            45, 46);// 174, 42, 43, 44, 45, 46);
                    this.TuongKhac.GiamSatThuong += this.arrItemBody[i].getChiSo(this.client, 13, 173);// 13, 173, 206);
                    this.TuongKhac.GiamSatThuong += this.arrItemBody[i].getChiSo(1, this.client, 206);
                    this.TuongKhac.KhangTatCa += this.arrItemBody[i].getChiSo(this.client, 121, 152, 258, 40, 81, 12);// 121,
                                                                                                                      // 152,
                                                                                                                      // 201,
                                                                                                                      // 258,
                                                                                                                      // 40,
                                                                                                                      // 81,
                                                                                                                      // 12);
                    this.TuongKhac.KhangTatCa += this.arrItemBody[i].getChiSo(1, this.client, 201);
                    this.TuongKhac.TangTuongKhac += this.arrItemBody[i].getChiSo(this.client, 53, 54, 55, 56, 57, 138,
                            139, 140, 141, 142, 307, 310, 372);// 53, 54, 55, 56, 57, 138, 139, 140, 141, 142, 307, 310,
                                                               // 372);
                    this.TuongKhac.GiamTuongKhac += this.arrItemBody[i].getChiSo(this.client, 58, 59, 60, 61, 62, 311,
                            323, 330, 331);// 58, 59, 60, 61, 62, 311, 323, 330, 331, 345);
                    this.TuongKhac.GiamTuongKhac += this.arrItemBody[i].getChiSo(1, this.client, 345);
                    this.TuongKhac.GiamSuyYeu += this.arrItemBody[i].getChiSo(this.client, 289, 325, 355);// 289, 325,
                                                                                                          // 355);
                    this.TuongKhac.GiamTrungDoc += this.arrItemBody[i].getChiSo(this.client, 290, 326, 356);// 290, 326,
                                                                                                            // 356);
                    this.TuongKhac.GiamLamCham += this.arrItemBody[i].getChiSo(this.client, 291, 327, 357);// 291, 327,
                                                                                                           // 357);
                    this.TuongKhac.GiamGayBong += this.arrItemBody[i].getChiSo(this.client, 292, 328, 358);// 292, 328,
                                                                                                           // 358);
                    this.TuongKhac.GiamGayChoang += this.arrItemBody[i].getChiSo(this.client, 293, 329, 359);// 293,
                                                                                                             // 329,
                                                                                                             // 359);
                    this.TuongKhac.GiamTruChiMang += this.arrItemBody[i].getChiSo(this.client, 344, 346, 348);// 344,
                                                                                                              // 346,
                                                                                                              // 348);
                    this.TuongKhac.GiamTruChiMang += this.arrItemBody[i].getChiSo(1, this.client, 344);
                    this.TuongKhac.HieuUngNgauNhien += this.arrItemBody[i].getChiSo(this.client, 349);
                    this.TuongKhac.PhatHuyLucDanhCoban += this.arrItemBody[i].getChiSo(client, 34, 47, 122, 361);
                    this.TuongKhac.satThuongChuyenHp += this.arrItemBody[i].getChiSo(client, 6, 158, 252);
                }
            }

            for (Item item : this.arrItemBody2) {
                if (item != null) {
                    this.infoChar.taiPhu += item.getChiSo(this.client);
                }
            }

            for (Skill skill : this.arraySkill) { // tăng chỉ số skill
                if (skill != null && skill.getSkillTemplate().type >= 5) {
                    this.TuongKhac.TanCong += skill.getChiSo(78, 61);
                    this.infoChar.hpFull += skill.getChiSo(175);
                    this.infoChar.speedMove += skill.getChiSo(91);
                    hpMax += skill.getChiSo(79);
                    mpMax += skill.getChiSo(80);
                    infoChar.exp_plus += skill.getChiSo(66);
                    //
                    TuongKhac.ChinhXac += skill.getChiSo(65);
                    TuongKhac.BoQuaNeTranh += skill.getChiSo(147);
                    TuongKhac.ChiMang += skill.getChiSo(63);
                    TuongKhac.PhanDon += skill.getChiSo(67);
                }
            }
            if (infoChar.familyId != -1) {
                FamilyTemplate giaToc = Family.gI().getGiaToc(this);
                if (giaToc != null) {
                    Family_Member getMem = Family.gI().getMe(this, giaToc);
                    if (getMem != null) {
                        for (SkillClan skill : giaToc.listSkill) {
                            if (skill != null) {
                                this.infoChar.hpFull += skill.getChiSo(0);
                                this.TuongKhac.TanCongQuai += skill.getChiSo(3);
                                this.infoChar.hpRecv += skill.getChiSo(136);
                                this.infoChar.mpRecv += skill.getChiSo(137);
                                this.TuongKhac.GiamSatThuong += skill.getChiSo(173);
                                this.TuongKhac.NeTranh += skill.getChiSo(161);
                                this.TuongKhac.ChinhXac += skill.getChiSo(167);
                                this.TuongKhac.ChiMang += skill.getChiSo(166);
                                this.TuongKhac.KhangTatCa += skill.getChiSo(152);
                                this.TuongKhac.BoQuaKhangTinh += skill.getChiSo(149);
                                this.TuongKhac.TanCong += skill.getChiSo(2);
                                this.TuongKhac.KhangPhong += skill.getChiSo(112);
                                this.TuongKhac.TangTanCongLenPhong += skill.getChiSo(117);
                                this.TuongKhac.GayChoang += skill.getChiSo(127);
                                this.TuongKhac.KhangHoa += skill.getChiSo(111);
                                this.TuongKhac.TangTanCongLenHoa += skill.getChiSo(116);
                                this.TuongKhac.GayBong += skill.getChiSo(126);
                                this.TuongKhac.KhangThuy += skill.getChiSo(110);
                                this.TuongKhac.TangTanCongLenThuy += skill.getChiSo(115);
                                this.TuongKhac.GayLamCham += skill.getChiSo(125);
                                this.TuongKhac.KhangTho += skill.getChiSo(109);
                                this.TuongKhac.TangTanCongLenTho += skill.getChiSo(114);
                                this.TuongKhac.GayTrungDoc += skill.getChiSo(124);
                                this.TuongKhac.KhangLoi += skill.getChiSo(108);
                                this.TuongKhac.TangTanCongLenLoi += skill.getChiSo(113);
                                this.TuongKhac.GaySuyYeu += skill.getChiSo(123);
                            }
                        }
                    }
                }
            }
            if (infoChar.itemSach != null) {
                this.TuongKhac.TanCong += infoChar.itemSach.getChiSo(client, 208);
                this.TuongKhac.TanCongQuai += infoChar.itemSach.getChiSo(client, 207);
            }
            // skill vĩ thú

            for (SkillClan skill : this.listSkillViThu) {
                this.TuongKhac.BoQuaKhangTinh += skill.getChiSo(149);
                this.TuongKhac.ChiMang += skill.getChiSo(166);
                this.TuongKhac.TangTuongKhac += skill.getChiSo(307);
                this.TuongKhac.ChinhXac += skill.getChiSo(167);
                this.TuongKhac.TanCong += skill.getChiSo(2);
                this.TuongKhac.BoQuaKhangTinh += skill.getChiSo(149);
                this.TuongKhac.TangTanCongChiMang += skill.getChiSo(306);
                this.TuongKhac.KhangTatCa += skill.getChiSo(258);
                this.TuongKhac.GiamTuongKhac += skill.getChiSo(331);
                this.TuongKhac.GiamTanCongKhiBiChiMang += skill.getChiSo(174);
            }
            this.infoChar.hpFull = this.infoChar.hpFull + (this.infoChar.hpFull * hpMax / 100);
            this.infoChar.mpFull = this.infoChar.mpFull + (this.infoChar.mpFull * mpMax / 100);
            if (phanDonPT > 0)
                this.TuongKhac.PhanDon += this.TuongKhac.PhanDon * phanDonPT / 100;
            if (this.infoChar.hp > this.infoChar.hpFull)
                this.infoChar.hp = this.infoChar.hpFull;
            if (this.infoChar.mp > this.infoChar.mpFull)
                this.infoChar.mp = this.infoChar.mpFull;
            for (Effect effect : this.listEffect) { // setup lại eff
                Effect.setEff(this, effect, false);
            }
            if (isSendClient) {
                msgUpdateHpFull();
                msgUpdateMpFull();
                msgUpdateStatusChar();
            }
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void update() {
        // check gd
        try {
            if (!this.client.isConnected())
                return;
            if (this.traDe.IsTrade) {
                if (this.zone.findCharInMap(this.traDe.Id_Char_Trade) == null
                        || !this.zone.findCharInMap(this.traDe.Id_Char_Trade).traDe.IsTrade
                        || this.zone.findCharInMap(this.traDe.Id_Char_Trade).traDe.Id_Char_Trade != this.id)
                    this.ClearGiaoDich(true, 1);
            }

            for (int i = listEffect.size() - 1; i >= 0; i--) {
                Effect effect = listEffect.get(i);
                if (effect != null) {
                    effect.updateChar(this);
                }
            }
            if (System.currentTimeMillis() - this.info.delayMS >= 500L) {
                if (this.infoChar.hp > 0 && (infoChar.hpRecv > 0 || infoChar.mpRecv > 0)) {
                    if (infoChar.hp < infoChar.hpFull) {
                        PlusHp(infoChar.hpRecv);
                        msgUpdateHp();
                    }
                    if (infoChar.mp < infoChar.mpFull) {
                        PlusMp(infoChar.mpRecv);
                        msgUpdateMp();
                    }
                }
                this.info.delayMS = System.currentTimeMillis();
            }

            if (infoChar.idClass == 3) {
                double twentyPercentOfHpFull = infoChar.hpFull * 0.2;
                if (infoChar.hp < twentyPercentOfHpFull) {
                    for (Skill skill : arraySkill) {
                        if (skill.getSkillTemplate().id == 17 && infoChar.mp > skill.mpUse
                                && System.currentTimeMillis() - skill.time > skill.coolDown) {
                            if (zone != null) {
                                zone.attackMob(client, skill.getSkillTemplate().id, -1);
                                break;
                            }
                        }
                    }
                }
            }

            /// Auto save Data
            if (info.saveData < System.currentTimeMillis()) {
                CharDB.Update(this);
                // Player.UpdateInfo(this.client.player, this.infoChar.name, this.level());
                info.saveData = 60000 + System.currentTimeMillis();
                phucLoi.thoigianOnlineHomNay += 60000;
                ///
                updatePhucLoiHangNgay();
                updateInfoToFamily();
            }
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }

    }

    public synchronized boolean addItem(Item item, String lydo) {

        if (item.getItemTemplate().isXepChong) {
            for (int i = 0; i < this.arrItemBag.length; i++) {
                if (this.arrItemBag[i] != null && this.arrItemBag[i].id == item.id
                        && item.expiry == this.arrItemBag[i].expiry && item.isLock == this.arrItemBag[i].isLock) {
                    item.setAmount(this.arrItemBag[i].getAmount() + item.getAmount());
                    item.index = (short) i;
                    this.arrItemBag[i] = item;

                    lydo = "ADD ITEM BAG Xếp chồng " + item.getItemTemplate().name + ", SL: " + item.amount + " lý do: "
                            + lydo;
                    Utlis.logAddChar(lydo, this.id);
                    return true;
                }
            }
            for (int i = 0; i < this.arrItemBag.length; i++) {
                if (this.arrItemBag[i] == null) {
                    item.index = (short) i;
                    this.arrItemBag[i] = item;

                    lydo = "ADD ITEM BAG XẾP CHỒNG NEW " + item.getItemTemplate().name + ", SL: " + item.amount
                            + " lý do: " + lydo;
                    Utlis.logAddChar(lydo, this.id);
                    return true;
                }
            }
        } else {
            for (int i = 0; i < this.arrItemBag.length; i++) {
                if (this.arrItemBag[i] == null) {
                    item.index = (short) i;
                    this.arrItemBag[i] = item;

                    lydo = "ADD ITEM BAG NEW " + item.getItemTemplate().name + ", SL: " + item.amount + " lý do: "
                            + lydo;
                    Utlis.logAddChar(lydo, this.id);
                    return true;
                }
            }
        }
        return false;
    }

    public synchronized Item addItem(Item item, boolean newArrBag, String lydo) {
        if (!newArrBag) {
            return null;
        }

        for (int i = 0; i < this.arrItemBag.length; i++) {
            if (this.arrItemBag[i] == null) {
                item.index = (short) i;
                this.arrItemBag[i] = item;

                lydo = "ADD ITEM BAG NEW " + item.getItemTemplate().name + ", SL: " + item.amount + " lý do: " + lydo;
                Utlis.logAddChar(lydo, this.id);
                return this.arrItemBag[i];
            }
        }
        return null;
    }

    public synchronized short addItemBoxToBag(Item item, String lydo) {
        if (item.getItemTemplate().isXepChong) {
            for (int i = 0; i < this.arrItemBag.length; i++) {
                if (this.arrItemBag[i] != null && this.arrItemBag[i].id == item.id
                        && item.expiry == this.arrItemBag[i].expiry && item.isLock == this.arrItemBag[i].isLock) {
                    item.setAmount(this.arrItemBag[i].getAmount() + item.getAmount());
                    item.index = (short) i;
                    this.arrItemBag[i] = item;

                    lydo = "ADD ITEM BOX To BAG XẾP CHỒNG " + item.getItemTemplate().name + ", SL: " + item.amount
                            + " lý do: " + lydo;
                    Utlis.logAddChar(lydo, this.id);
                    return (short) this.arrItemBag[i].index;
                }
            }
            for (int i = 0; i < this.arrItemBag.length; i++) {
                if (this.arrItemBag[i] == null) {
                    item.index = (short) i;
                    this.arrItemBag[i] = item;

                    lydo = "ADD ITEM BOX To BAG XẾP CHỒNG NEW " + item.getItemTemplate().name + ", SL: " + item.amount
                            + " lý do: " + lydo;
                    Utlis.logAddChar(lydo, this.id);
                    return (short) this.arrItemBag[i].index;
                }
            }
        } else {
            for (int i = 0; i < this.arrItemBag.length; i++) {
                if (this.arrItemBag[i] == null) {
                    item.index = (short) i;
                    this.arrItemBag[i] = item;

                    lydo = "ADD ITEM BOX To BAG NEW " + item.getItemTemplate().name + ", SL: " + item.amount
                            + " lý do: " + lydo;
                    Utlis.logAddChar(lydo, this.id);
                    return (short) this.arrItemBag[i].index;
                }
            }
        }
        return -1;
    }

    public synchronized short addItemBagToBox(Item item, String lydo) {
        if (item.getItemTemplate().isXepChong) {
            for (int i = 0; i < this.arrItemBox.length; i++) {
                if (this.arrItemBox[i] != null && this.arrItemBox[i].id == item.id
                        && item.expiry == this.arrItemBox[i].expiry && item.isLock == this.arrItemBox[i].isLock) {
                    item.setAmount(this.arrItemBox[i].getAmount() + item.getAmount());
                    item.index = (short) i;
                    this.arrItemBox[i] = item;

                    lydo = "ADD ITEM BAG to BOX XẾP CHỒNG " + item.getItemTemplate().name + ", SL: " + item.amount
                            + " lý do: " + lydo;
                    Utlis.logAddChar(lydo, this.id);
                    return (short) this.arrItemBox[i].index;
                }
            }
            for (int i = 0; i < this.arrItemBox.length; i++) {
                if (this.arrItemBox[i] == null) {
                    item.index = (short) i;
                    this.arrItemBox[i] = item;

                    lydo = "ADD ITEM BAG to BOX XẾP CHỒNG NEW " + item.getItemTemplate().name + ", SL: " + item.amount
                            + " lý do: " + lydo;
                    Utlis.logAddChar(lydo, this.id);
                    return (short) this.arrItemBox[i].index;
                }
            }
        } else {
            for (int i = 0; i < this.arrItemBox.length; i++) {
                if (this.arrItemBox[i] == null) {
                    item.index = (short) i;
                    this.arrItemBox[i] = item;
                    lydo = "ADD ITEM BAG to BOX NEW " + item.getItemTemplate().name + ", SL: " + item.amount
                            + " lý do: " + lydo;
                    Utlis.logAddChar(lydo, this.id);
                    return (short) this.arrItemBox[i].index;
                }
            }
        }
        return -1;
    }

    public synchronized boolean checkAddItem(Item item) {
        if (item.getItemTemplate().isXepChong) {
            for (int i = 0; i < this.arrItemBag.length; i++) {
                if (this.arrItemBag[i] != null && this.arrItemBag[i].id == item.id
                        && item.expiry == this.arrItemBag[i].expiry && item.isLock == this.arrItemBag[i].isLock) {
                    return true;
                }
            }
            for (int i = 0; i < this.arrItemBag.length; i++) {
                if (this.arrItemBag[i] == null) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < this.arrItemBag.length; i++) {
                if (this.arrItemBag[i] == null) {
                    return true;
                }
            }
        }
        return false;
    }

    public int level() {
        long var1 = this.infoChar.exp;

        int var3;
        for (var3 = 0; var3 < DataCenter.gI().exps.length && var1 >= DataCenter.gI().exps[var3]; ++var3) {
            var1 -= DataCenter.gI().exps[var3];
        }

        return var3;
    }

    public int getPlusExp() {
        int var1 = this.infoChar.exp_plus + infoChar.exp_rank;
        return var1;
    }

    public void goiPhanThan() {
        if (infoChar.sachChienDau != 18)
            return;
        int xdame = DataCache.dataDamePhanThan[infoChar.levelPhanThan];
        if (infoChar.isGoiPhanThanFree) {
            Item bua1 = getItemBagById(779);
            Item bua2 = getItemBagById(782);
            if (bua1 != null) {
                this.addEffect(new Effect(99, xdame, System.currentTimeMillis(), 1800000));
                removeItemBag(bua1, "Gọi phân thân");
                return;
            } else if (bua2 != null) {
                this.addEffect(new Effect(99, xdame, System.currentTimeMillis(), 3600000));
                removeItemBag(bua2, "Gọi phân thân");
                return;
            }
            client.session.serivce.ShowMessGold("Không có Bùa phân thân");

        }
        {
            infoChar.isGoiPhanThanFree = true;
            this.addEffect(new Effect(99, xdame, System.currentTimeMillis(), 3600000));
        }
    }

    public void msgRemoveItemBag(Item item) {
        try {
            Writer writer = new Writer();
            writer.writeShort(item.index);
            client.session.serivce.removeItemBag(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void msgUpdateItemBag(Item item) {
        try {
            Writer writer = new Writer();
            item.write(writer);
            client.session.serivce.updateItemBag(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void msgUseItem(Item item) {
        try {
            Writer writer = new Writer();
            writer.writeShort(item.index);
            writer.writeBoolean(item.isLock);
            if (item.getItemTemplate().type == 28) {
                writer.writeShort(arrItemBag.length);
            } else {
                writer.writeInt(item.getAmount());
            }
            client.session.serivce.useItemBag(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void msgAddItemBag(Item item) {
        try {
            Writer writer = new Writer();
            item.write(writer);
            client.session.serivce.addItemBag(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    // public void msgUseItemBag(Item item) {
    // Util.Debug("msgUseItemBag");
    // try {
    // Writer writer = new Writer();
    // writer.writeShort(item.index);
    // writer.writeBoolean(item.isLock);
    // if (item.isTypeTrangBi()) {
    //
    // } else if (item.getItemTemplate().type == 28) {
    // writer.writeShort(arrItemBag.length);
    // } else {
    // writer.writeInt(item.getAmount());
    // }
    // client.session.serivce.useItemBag(writer);
    // } catch (Exception ex) {
    // Utlis.logError(Char.class, ex , "Da say ra loi:\n" + ex.getMessage());
    // }
    // if (item.getAmount() <= 0) {
    // msgRemoveItemBag(item);
    // } else {
    // msgUpdateItemBag(item);
    // }
    // }

    public int getCountNullItemBag() {
        int c = 0;

        for (int i = 0; i < arrItemBag.length; i++) {
            if (arrItemBag[i] == null) {
                c++;
            }
        }
        return c;
    }

    public synchronized boolean removeItemBodyByIndex(int index, String lydo) {

        if (arrItemBody[index] == null) {
            return false;
        } else {
            lydo = "REMOVE ITEM BODY by index " + arrItemBody[index].getItemTemplate().name + ", SL: "
                    + arrItemBody[index].amount + " lý do: " + lydo;
            Utlis.logRemoveChar(lydo, this.id);
            arrItemBody[index] = null;
            return true;
        }
    }

    public synchronized boolean removeItemBody2ByIndex(int index, String lydo) {

        if (arrItemBody2[index] == null) {
            return false;
        } else {
            lydo = "REMOVE ITEM BODY by index " + arrItemBody2[index].getItemTemplate().name + ", SL: "
                    + arrItemBody2[index].amount + " lý do: " + lydo;
            Utlis.logRemoveChar(lydo, this.id);
            arrItemBody2[index] = null;

            return true;
        }
    }

    public synchronized boolean removeItemBagByIndex(int index, String lydo) {

        if (arrItemBag[index] == null) {
            return false;
        } else {
            lydo = "REMOVE ITEM BAG by index " + arrItemBag[index].getItemTemplate().name + ", SL: "
                    + arrItemBag[index].amount + " lý do: " + lydo;
            Utlis.logRemoveChar(lydo, this.id);
            arrItemBag[index] = null;

            return true;
        }
    }

    public synchronized boolean removeItemBag(Item item, String lydo) {
        if (item == null) {
            return false;
        }
        for (int i = 0; i < arrItemBag.length; i++) {
            if (arrItemBag[i] == item) {
                item.index = (short) i;
                lydo = "REMOVE ITEM BAG " + item.getItemTemplate().name + ", SL: " + item.amount + " lý do: " + lydo;
                Utlis.logRemoveChar(lydo, this.id);
                if (item.getAmount() > 1) {
                    arrItemBag[i].setAmount(arrItemBag[i].getAmount() - 1);
                    msgUpdateItemBag(item);
                } else {
                    msgRemoveItemBag(item);
                    arrItemBag[i].setAmount(0);
                    arrItemBag[i] = null;
                }
                return true;
            }
        }
        return false;
    }

    public synchronized void removeAmountAllItemBagById(int idItem, int soluong, String lydo) {
        int daRemove = soluong;
        for (Item value : this.arrItemBag) {
            if (value != null) {
                if (daRemove > 0 && value.id == idItem) {
                    if (value.getAmount() > daRemove) {
                        removeItemBag(value, daRemove, lydo);
                        break;
                    } else {
                        daRemove -= value.getAmount();
                        removeItemBag(value, true, lydo);
                    }

                }
            }
        }
    }

    public synchronized boolean removeItemBag(Item item, boolean clean, String lydo) {
        if (item == null) {
            return false;
        }
        for (int i = 0; i < arrItemBag.length; i++) {
            if (arrItemBag[i] == item) {
                item.index = (short) i;
                lydo = "REMOVE ITEM BAG " + item.getItemTemplate().name + ", SL: " + item.amount + " lý do: " + lydo;
                Utlis.logRemoveChar(lydo, this.id);
                if (item.getAmount() > 1 && !clean) {
                    arrItemBag[i].setAmount(arrItemBag[i].getAmount() - 1);
                    msgUpdateItemBag(item);
                } else {
                    msgRemoveItemBag(item);
                    arrItemBag[i].setAmount(0);
                    arrItemBag[i] = null;
                }

                return true;
            }
        }

        return false;
    }

    public synchronized boolean removeItemBag(Item item, int amount, String lydo) {
        if (item == null) {
            return false;
        }
        for (int i = 0; i < arrItemBag.length; i++) {
            if (arrItemBag[i] == item) {
                item.index = (short) i;

                lydo = "REMOVE ITEM BAG " + item.getItemTemplate().name + ", SL: " + item.amount + " lý do: " + lydo;
                Utlis.logRemoveChar(lydo, this.id);

                if (item.getAmount() > amount) {
                    arrItemBag[i].setAmount(arrItemBag[i].getAmount() - amount);
                    msgUpdateItemBag(item);
                } else {
                    msgRemoveItemBag(item);
                    arrItemBag[i].setAmount(0);
                    arrItemBag[i] = null;
                }
                return true;
            }
        }
        return false;
    }

    public synchronized boolean removeItemBagNoUpdate(Item item, boolean clean, String lydo) {
        if (item == null) {
            return false;
        }
        for (int i = 0; i < arrItemBag.length; i++) {
            if (arrItemBag[i] == item) {
                item.index = (short) i;
                lydo = "REMOVE ITEM BAG " + item.getItemTemplate().name + ", SL: " + item.amount + " lý do: " + lydo;
                Utlis.logRemoveChar(lydo, this.id);
                if (item.getAmount() > 1 && !clean) {
                    arrItemBag[i].setAmount(arrItemBag[i].getAmount() - 1);
                } else {
                    arrItemBag[i].setAmount(0);
                    arrItemBag[i] = null;
                }

                return true;
            }
        }

        return false;
    }

    public void msgUseItemBagx(Item item) {
        try {
            Writer writer = new Writer();
            writer.writeShort(item.index);
            writer.writeBoolean(item.isLock);
            if (item.isTypeTrangBi()) {

            } else if (item.getItemTemplate().type == 28) {
                writer.writeShort(this.arrItemBag.length);
            } else {
                writer.writeInt(item.getAmount());
            }
            client.session.serivce.useItemBag(writer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (item.getAmount() <= 0) {
            msgRemoveItemBag(item);
        } else {
            msgUpdateItemBag(item);
        }
    }

    public int getAmountAllById(int idItem) {
        int a = 0;
        for (Item value : this.arrItemBag) {
            if (value != null) {
                if (value.id == idItem) {
                    a += value.getAmount();
                }
            }
        }
        return a;
    }

    public Item getItemBagById(int item) {
        for (Item value : this.arrItemBag) {
            if (value != null) {
                if (value.id == item) {
                    return value;
                }
            }
        }
        return null;
    }

    public synchronized Item getItemBagById(int item, boolean isLock) {

        for (Item value : this.arrItemBag) {
            if (value != null) {
                if (value.id == item && isLock == value.isLock) {
                    return value;
                }
            }
        }

        return null;
    }

    public synchronized Item getItemBagByIndex(int index) {
        if (index >= 0 && index < this.arrItemBag.length && this.arrItemBag[index] != null) {
            return this.arrItemBag[index];
        }
        return null;
    }

    public synchronized Item getItemBodyByIndex(int index) {
        if (index >= 0 && index < this.arrItemBody.length && this.arrItemBody[index] != null) {
            return this.arrItemBody[index];
        }
        return null;
    }

    public synchronized Item getItemBody2ByIndex(int index) {
        if (index >= 0 && index < this.arrItemBody2.length && this.arrItemBody2[index] != null) {
            return this.arrItemBody2[index];
        }
        return null;
    }

    public synchronized Item getItemByType(byte type, int index) {
        Item item = getItemBagByIndex(index);
        if (type == 2) {
            item = getItemBodyByIndex(index);
        } else if (type == 3) {
            item = getItemBody2ByIndex(index);
        }
        return item;
    }

    public int getSlotItemExtend(Item item) {
        if (item != null) {
            switch (item.id) {
                case 185:
                    return 9;
                case 186:
                    return 18;
                case 187:
                    return 27;
                case 468:
                    return 36;
            }
        }
        return 0;
    }

    public void updateItemBag() {

        int numAdd = 0;
        for (int i = 0; i < this.arrItemExtend.length; i++) {
            if (this.arrItemExtend[i] != null) {
                numAdd = numAdd + getSlotItemExtend(this.arrItemExtend[i]);
            }
        }
        Item[] _arrItemBag = new Item[27 + numAdd];
        for (int i = 0; i < _arrItemBag.length && i < this.arrItemBag.length; i++) {
            _arrItemBag[i] = arrItemBag[i];
        }
        this.arrItemBag = _arrItemBag;
    }

    public void addExp(long exp) {
        if (infoChar.isKhoaCap)
            return;
        // Sử dụng biến cấu hình thay vì hardcode
        exp *= PKoolVNDB.EXP_MULTIPLIER;
        int level = this.level();
        this.infoChar.exp += exp;
        int levelNew = this.level();
        if (levelNew >= 100) {
            levelNew = 100;
            this.infoChar.exp = DataCenter.gI().GetExpFormLevel(100);
        }
        infoChar.level = levelNew;
        if (level != levelNew) {
            upLevel(level, levelNew);
        }
        msgAddExp();
    }

    public void setExp(long exp) {
        if (infoChar.isKhoaCap)
            return;
        int level = this.level();
        this.infoChar.exp = exp;
        int levelNew = this.level();
        if (levelNew >= 100) {
            levelNew = 100;
            this.infoChar.exp = DataCenter.gI().GetExpFormLevel(100);
        }
        infoChar.level = levelNew;
        if (level != levelNew) {
            upLevel(level, levelNew);
        }
        msgAddExp();
    }

    public void msgAddExp() {
        try {
            Writer writer = new Writer();
            writer.writeLong(this.infoChar.exp);
            writer.writeInt(this.id);
            zone.addExpToAllChar(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void sortItem(byte type) {
        if (type == 0) {
            sortItem(this.arrItemBag);
            msgSortItem(type);
        } else if (type == 1) {
            sortItem(this.arrItemBox);
            msgSortItem(type);
        }
    }

    public void msgSortItem(byte type) {
        try {
            Writer writer = new Writer();
            writer.writeByte(type);
            client.session.serivce.sortItem(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void itemExtendToBag(byte index) {
        Item item = this.arrItemExtend[index];
        if (item == null) {
            return;
        }
        int num = getSlotItemExtend(item);
        int num_new = this.arrItemBag.length - num;
        ArrayList<Item> listItemBag = new ArrayList<Item>();
        Utlis.getArrayListNotNull(this.arrItemBag, listItemBag);

        if (listItemBag.size() < num_new) {
            this.arrItemExtend[index] = null;
            updateItemBag();
            sortItem(this.arrItemBag);
            addItem(item, "ITEM EXTEND to Bag");
            msgItemExtendToBag(index);
        } else {
            client.session.serivce.ShowMessGold(" Hành trang đang chứa quá ô. vui lòng xóa bớt trước.! ");
        }

    }

    public void msgItemExtendToBag(byte index) {
        try {
            Writer writer = new Writer();
            writer.writeByte(index);
            writer.writeShort(arrItemBag.length);
            client.session.serivce.itemExtendToBag(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void vutItem(short index) {
        if (index < 0 || index >= this.arrItemBag.length || getItemBagByIndex(index) == null
                || getItemBagByIndex(index).isLock) {
            return;
        }
        ItemMap itemMap = new ItemMap(id);
        itemMap.idEntity = DataCache.getIDItemMap();
        itemMap.item = getItemBagByIndex(index);

        msgRemoveItemBag(getItemBagByIndex(index));
        this.removeItemBagByIndex(index, "Vứt item");
        try {
            int x = Utlis.nextInt(50);
            itemMap.cx = (short) (cx + (Utlis.rnd.nextBoolean() ? x : -x));
            itemMap.cy = cy;
            itemMap.cy = zone.getXYBlockMap(cx, cy).cy;
        } catch (Exception e) {

        }
        try {
            synchronized (zone.vecItemMap) {
                Writer writer = new Writer();
                writer.writeInt(id);
                itemMap.write(writer, -1, -1, zone);
                zone.vecItemMap.add(itemMap);
                zone.vutItemToAllChar(writer);
            }
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void tachItem(short index, int amount) {
        if (index < 0 || index >= this.arrItemBag.length || getItemBagByIndex(index) == null
                || getItemBagByIndex(index).getAmount() <= amount || amount > Short.MAX_VALUE || amount < 0) {
            return;
        }
        Item itemMain = getItemBagByIndex(index);
        Item item2 = getItemBagByIndex(index).cloneItem();
        itemMain.setAmount(itemMain.getAmount() - amount);
        item2.setAmount(amount);
        for (int i = 0; i < this.arrItemBag.length; i++) {
            if (this.arrItemBag[i] == null) {
                item2.index = (short) i;
                this.arrItemBag[i] = item2;
                msgTachItem(itemMain, item2);
                return;
            }
        }
    }

    public void msgTachItem(Item itemMain, Item item2) {
        try {
            Writer writer = new Writer();
            writer.writeShort(itemMain.index);
            writer.writeInt(itemMain.getAmount());
            writer.writeShort(item2.index);
            writer.writeInt(item2.getAmount());
            client.session.serivce.tachItem(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }

    }

    public void msgSendArrItemBag() {
        try {
            Writer writer = new Writer();
            writer.writeInt(infoChar.bac);
            writeItemBag(writer, arrItemBag);
            client.session.serivce.sendArrItemBag(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void chatPublic(String str) throws IOException {
        if (client.player.role == 2) {
            if (str.startsWith("m")) {
                String[] arr = Utlis.split(str, " ");
                try {
                    int idMap = Integer.parseInt(arr[1]);
                    if (!DataCenter.gI().MapTemplate[idMap].notBlock) {
                        Map.maps[idMap].addChar(client);
                    }
                } catch (Exception ex) {
                }
            } else if (str.startsWith("item")) {
                String[] arr = Utlis.split(str, " ");
                try {
                    int id = Integer.parseInt(arr[1]);
                    int amount = 1;
                    if (arr.length > 2) {
                        try {
                            amount = Integer.parseInt(arr[2]);
                        } catch (Exception ex) {

                        }
                    }
                    Item item = new Item(id, false, amount);
                    this.addItem(item, "Lệnh chat ADMIN");
                    this.msgAddItemBag(item);
                } catch (Exception ex) {
                }
            } else if (str.startsWith("lv")) {
                String[] arr = Utlis.split(str, " ");
                try {
                    int id = Integer.parseInt(arr[1]);
                    setExp(DataCenter.gI().GetExpFormLevel(id));
                } catch (Exception ex) {
                }
            } else if (str.startsWith("e")) {
                String[] arr = Utlis.split(str, " ");
                try {
                    int id = Integer.parseInt(arr[1]);
                    this.addEffect(new Effect(id, 60, System.currentTimeMillis(), 20 * 1000));
                } catch (Exception ex) {
                }
            } else if (str.startsWith("a")) {

                // mob = new Mob();
                // mob.id = 130;
                // mob.level = 1;
                // mob.cx = cx;
                // mob.cy = cy;
                //
                // mob.hpGoc = mob.hp = mob.hpFull = 1000000;
                // mob.expGoc = mob.hpGoc / 8;
                // mob.paintMiniMap = false;
                // mob.isHoiSinhMob = false;
                // mob.createNewEffectList();
                // mob.idEntity = DataCache.getIDMob();
                // mob.reSpawnMobHoatDong(1, true);
                // zone.addMobToZone(mob);
            } else if (str.startsWith("b")) {

            }
        }
        try {

            Message msg = new Message((byte) 21);
            msg.writeUTF(infoChar.name);
            msg.writeUTF(str);
            client.mChar.zone.SendZoneMessage(msg);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void addFriend(Char c) {

        for (int i = 0; i < listFriend.size(); i++) {
            Friend friend = listFriend.get(i);
            if (c.client == null)
                break;
            if (friend.name.equals(c.infoChar.name)) {
                if (friend.stt == 2) {
                    friend.stt = 0;
                    for (int y = 0; y < c.listFriend.size(); y++) {
                        Friend friend2 = c.listFriend.get(y);
                        if (friend2.name.equals(this.infoChar.name)) {
                            friend2.stt = 0;
                            break;
                        }
                    }
                    client.session.serivce.msgUpdateFr(c.infoChar.name, (byte) 0, true);
                    c.client.session.serivce.msgUpdateFr(infoChar.name, (byte) 0, true);
                    c.client.session.serivce.ShowMessWhite(infoChar.name + " đã đồng ý kết bạn.");
                } else {
                    c.client.session.serivce.ShowMessWhite("Vui lòng đợi " + infoChar.name + " đồng ý kết bạn");
                }
                return;
            }

        }

        Friend newfr = new Friend();
        newfr.name = c.infoChar.name;
        newfr.stt = 1;
        newfr.id = c.id;
        listFriend.add(newfr);
        client.session.serivce.msgSendAddFr(c.infoChar.name, (byte) 1);
        Friend my = new Friend();
        my.name = infoChar.name;
        my.stt = 2;
        my.id = this.id;
        c.listFriend.add(my);
        c.client.session.serivce.msgAddFr(infoChar.name, (byte) 2, true);
    }

    public void removeFr(String c) {

        for (int i = 0; i < listFriend.size(); i++) {
            Friend friend = listFriend.get(i);
            if (friend.name.equals(c)) {
                listFriend.remove(friend);
                client.session.serivce.msgRemoveFr(c);
            }

        }

    }

    public void removeEnemy(String c) {

        for (int i = 0; i < listEnemy.size(); i++) {
            Enemy e = listEnemy.get(i);
            if (e.name.equals(c)) {
                listEnemy.remove(e);
                client.session.serivce.msgRemoveEn(c);
            }

        }

    }

    public synchronized void chapNhanTyVo(String str) {
        Char getPlayer = zone.findCharInMap(str);
        if (getPlayer == null || infoChar.isDie)
            return;
        if (getPlayer.info.typePK == 1 && getPlayer.info.idCharPk != -1 || info.typePK == 1 && info.idCharPk != -1)
            return;
        info.typePK = 1;
        info.idCharPk = getPlayer.id;

        getPlayer.info.typePK = 1;
        getPlayer.info.idCharPk = this.id;

        client.session.serivce.startTyVo(this.id, getPlayer.id);
        getPlayer.client.session.serivce.startTyVo(getPlayer.id, this.id);
    }

    public void cuuSat(String str) {
        if (infoChar.lvPk > 20) {
            this.client.session.serivce.ShowMessGold("Cấp PK của bạn quá cao không thể Cừu sát.");
            return;
        }
        Char getPlayer = zone.findCharInMap(str);
        if (getPlayer == null)
            return;

        if (getPlayer.info.typePK == 1 && getPlayer.info.idCharPk != -1 || info.typePK == 1 && info.idCharPk != -1)
            return;
        if (getPlayer.level() < 15) {
            this.client.session.serivce.ShowMessGold("Không thể cừu sát người dưới cấp 15");
            return;
        }
        if (Math.abs(this.level() - getPlayer.level()) > 10) {
            this.client.session.serivce.ShowMessGold("Không thể cừu sát người chênh lệch quá 10 cấp");
            return;
        }

        if (info.typePK > 0) {
            info.typePK = 0;
            client.session.serivce.sendTypePK(this.id, info.typePK);
        }
        if (getPlayer.info.typePK > 0) {
            getPlayer.info.typePK = 0;
            getPlayer.client.session.serivce.sendTypePK(getPlayer.id, getPlayer.info.typePK);
        }
        info.idCharPk = getPlayer.id;
        getPlayer.info.idCharPk = this.id;
        getPlayer.info.isCuuSat = true;

        client.session.serivce.startCuuSat(this.id, getPlayer.id);
        getPlayer.client.session.serivce.startCuuSat(this.id, getPlayer.id);
    }

    public synchronized void BuyCho(long id) {
        if (getCountNullItemBag() < 1) {
            this.client.session.serivce.ShowMessRed("Hành trang không đủ chỗ chứa.");
            return;
        }

        ChoTemplate itemcho = DataCenter.gI().findChoById(id);
        if (itemcho == null) {
            this.client.session.serivce.ShowMessRed("Vật phẩm này đã được người khác mua trước bạn.");
            return;
        }

        if (itemcho.isBuy != 0 || Cho.checkIsBuy(id) != 0) {
            this.client.session.serivce.ShowMessRed("Vật phẩm này đã được người khác mua trước bạn.");
            return;
        }

        if (this.infoChar.bac < itemcho.bac) {
            this.client.session.serivce.ShowMessRed("Bạn không đủ bạc.");
            return;
        }

        if (Cho.update(id)) {
            itemcho.isBuy = 1;
            this.mineBac(itemcho.bac, false, false, "Mua item chợ");
            addItem(itemcho.item, true, "Mua từ chợ");
            this.msgAddItemBag(itemcho.item);
            this.client.session.serivce.doneMuaCho(this.infoChar.bac);
        } else {
            this.client.session.serivce.ShowMessRed("Đã sảy ra lỗi vui lòng thử lại");
        }
    }

    public synchronized void DangBanCho(int index, int time, int bac) {
        if (client.mChar.level() < 25) {
            this.client.session.serivce.ShowMessRed("Cấp 25 trở lên mới có thể đăng bán");
            return;
        }
        Item itembag = this.getItemBagByIndex(index);
        if (itembag == null || itembag.isLock || itembag.expiry > 0 || bac < 0 || bac > 2000000000) {
            this.client.session.serivce.ShowMessRed("Không thể đăng bán vật phẩm này.");
        } else {
            int phiban = 10;
            int tinhtime = 28800;
            if (time == 1) {
                phiban = 20;
                tinhtime = 57600;
            } else if (time == 2) {
                phiban = 30;
                tinhtime = 86400;
            } else if (time == 3) {
                phiban = 60;
                tinhtime = 172800;
            } else if (time == 4) {
                phiban = 100;
                tinhtime = 259200;
            }

            if (infoChar.bacKhoa < phiban) {
                this.client.session.serivce.ShowMessRed("Không đủ phí bạc Khóa.");
            } else {

                ChoTemplate choTemplate = new ChoTemplate();
                choTemplate.item = itembag;
                choTemplate.bac = bac;
                choTemplate.character_id = this.id;
                choTemplate.character_name = this.infoChar.name;
                choTemplate.time = (int) (System.currentTimeMillis() / 1000L) + tinhtime;
                choTemplate.id = Cho.insert(choTemplate);
                if (choTemplate.id > 0) {
                    DataCenter.gI().DataCho.add(choTemplate);
                    mineBacKhoa(phiban, false, false, "Phí bán chợ");
                    removeItemBagByIndex(itembag.index, "Đăng bán chợ");
                    client.session.serivce.DangBanThanhCong(this.infoChar.bacKhoa, (short) itembag.index);
                } else {
                    this.client.session.serivce.ShowMessRed("Đã sảy ra lỗi vui lòng thử lại.");
                }

            }
        }

    }

    public void RaoBan(long id) {
        try {
            ChoTemplate itemcho = DataCenter.gI().findChoById(id);
            if (itemcho == null || itemcho.character_id != this.id)
                return;
            if (infoChar.vang < 1) {
                this.client.session.serivce.ShowMessRed("Không đủ vàng để rao bán");
            } else {
                mineVang(1, true, true, "Rao bán");
                String str = "" + itemcho.item.getItemTemplate().name + " bán với giá "
                        + Utlis.numberFormat(itemcho.bac) + " bạc tại chợ.";
                Message msg = new Message((byte) 22);
                msg.writeByte(1);
                msg.writeUTF(infoChar.name);
                msg.writeUTF(str);
                PlayerManager.getInstance().sendMessageAllChar(msg);
            }

        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void MoiGiaoDich(String nick) {
        try {
            if (this.level() < 20) {
                this.client.session.serivce.ShowMessRed("Cấp 20 trở lên mới có thể giao dịch");
            } else if (this.info.Trade > System.currentTimeMillis() && !PKoolVNDB.isDebug) {
                long secondsLeft = (this.info.Trade - System.currentTimeMillis()) / 1000;
                String message = "Chỉ có thể giao dịch sau " + secondsLeft + " giây nữa.";
                this.client.session.serivce.NhacNhoMessage(message);
            } else if (this.zone.findCharInMap(nick) == null) {
                this.client.session.serivce.ShowMessRed("Người chơi đã đi quá xa.");
            } else if (this.traDe.IsTrade) {
                this.client.session.serivce.ShowMessRed("Không thể thực hiện khi đang giao dịch.");
            } else if (this.zone.findCharInMap(nick).traDe.IsTrade) {
                this.client.session.serivce.ShowMessRed(nick + " đang có giao dịch với người khác.");
            } else {
                this.zone.findCharInMap(nick).traDe.Name_Char_Send = this.infoChar.name;
                this.zone.findCharInMap(nick).client.session.serivce.MoiGiaoDich(this.infoChar.name);
                this.client.session.serivce.ShowMessWhite("Đã gửi lời mời giao dịch tới " + nick);
            }
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void ChapNhanGiaoDich() {
        try {
            if (this.level() < 20) {
                this.client.session.serivce.ShowMessRed("Cấp 20 trở lên mới có thể giao dịch");
                return;
            }
            if (!this.traDe.IsTrade) {
                if (this.info.Trade > System.currentTimeMillis() && !PKoolVNDB.isDebug) {
                    long secondsLeft = (this.info.Trade - System.currentTimeMillis()) / 1000;
                    String message = "Chỉ có thể giao dịch sau " + secondsLeft + " giây nữa.";
                    this.client.session.serivce.NhacNhoMessage(message);
                    return;
                }
                if (this.traDe.Name_Char_Send == null || this.zone.findCharInMap(this.traDe.Name_Char_Send) == null) {
                    return;
                }
                if (this.zone.findCharInMap(this.traDe.Name_Char_Send).traDe.IsTrade) {
                    this.client.session.serivce
                            .ShowMessRed(this.traDe.Name_Char_Send + " đang có giao dịch với người khác.");
                } else {
                    // setup giao dịch nick mời
                    this.zone.findCharInMap(this.traDe.Name_Char_Send).traDe.IsTrade = true;
                    this.zone.findCharInMap(this.traDe.Name_Char_Send).traDe.Id_Char_Trade = id;
                    this.zone.findCharInMap(this.traDe.Name_Char_Send).client.session.serivce
                            .OpenTabGiaoDich(this.infoChar.name);

                    // setup giao dịch me
                    this.traDe.IsTrade = true;
                    this.traDe.Id_Char_Trade = this.zone.findCharInMap(this.traDe.Name_Char_Send).id;
                    this.client.session.serivce
                            .OpenTabGiaoDich(this.zone.findCharInMap(this.traDe.Name_Char_Send).infoChar.name);
                }

            } else {
                this.client.session.serivce.ShowMessRed("Không thể thực hiện khi đang giao dịch.");
            }
        } catch (Exception ex) {
            ClearGiaoDich(true, 2);
        }
    }

    public void KhoaGiaoDich(int bac, List<Item> Items) {
        try {
            if (traDe.IsLock || traDe.IsHold)
                return;
            if (this.traDe.IsTrade) {
                if (this.zone.findCharInMap(this.traDe.Id_Char_Trade) == null) {
                    ClearGiaoDich(true, 3);
                    return;
                }

                if (bac < 0 || infoChar.bac < bac) {
                    this.client.session.serivce.ShowMessRed("Không đủ bạc để giao dịch");
                } else {
                    if (!this.zone.findCharInMap(this.traDe.Id_Char_Trade).traDe.IsTrade
                            || this.zone.findCharInMap(this.traDe.Id_Char_Trade).traDe.Id_Char_Trade != id) {
                        ClearGiaoDich(true, 4);
                        return;
                    }
                    traDe.IsLock = true;
                    traDe.bac = bac;
                    traDe.Items = Items;
                    this.zone.findCharInMap(this.traDe.Id_Char_Trade).client.session.serivce.khoaGiaoDich(traDe.bac,
                            Items);
                }

            } else {
                ClearGiaoDich(true, 5);

            }
        } catch (Exception ex) {
            ClearGiaoDich(true, 6);

        }

    }

    public void DoneGiaoDich() {
        try {

            if (!this.traDe.IsTrade || !this.traDe.IsLock || this.traDe.IsHold)
                return;
            this.traDe.IsHold = true;

            Char player = this.zone.findCharInMap(this.traDe.Id_Char_Trade);
            if (player == null) {
                this.ClearGiaoDich(true, 7);
                return;
            }

            if (player.traDe.Id_Char_Trade == id) {

                if (player.traDe.IsHold) {

                    List<Item> listItemMe = this.traDe.Items;

                    List<Item> listItemPlayer = player.traDe.Items;

                    int bacMe = this.traDe.bac;
                    int bacPlayer = player.traDe.bac;

                    String logPlayer = "";
                    String logMe = "";

                    if (player.getCountNullItemBag() < listItemMe.size()
                            || ((long) player.infoChar.bac + bacMe) > 2100000000) {
                        this.client.session.serivce.ShowMessRed(
                                "Giao dịch bị hủy bỏ do hành trang đối phương không đủ chỗ chứa item - xu");
                        player.client.session.serivce
                                .ShowMessRed("Giao dịch bị hủy bỏ do hành trang không đủ chỗ chứa item - xu");
                        player.ClearGiaoDich(true, 8);
                        this.ClearGiaoDich(true, 9);
                        return;
                    }
                    if (this.getCountNullItemBag() < listItemPlayer.size()
                            || ((long) this.infoChar.bac + bacPlayer) > 2100000000) {
                        this.client.session.serivce
                                .ShowMessRed("Giao dịch bị hủy bỏ do hành trang không đủ chỗ chứa item - xu");
                        player.client.session.serivce.ShowMessRed(
                                "Giao dịch bị hủy bỏ do hành trang đối phương không đủ chỗ chứa item - xu");
                        player.ClearGiaoDich(true, 10);
                        this.ClearGiaoDich(true, 11);
                        return;
                    }
                    if (bacMe > infoChar.bac) {
                        this.client.session.serivce.ShowMessRed("Giao dịch bị hủy bỏ do không đủ bạc");
                        player.client.session.serivce.ShowMessRed("Giao dịch bị hủy bỏ do đối phương không đủ bạc");
                        player.ClearGiaoDich(true, 12);
                        this.ClearGiaoDich(true, 13);
                        return;
                    }
                    if (bacPlayer > player.infoChar.bac) {
                        this.client.session.serivce.ShowMessRed("Giao dịch bị hủy bỏ do đối phương không đủ bạc");
                        player.client.session.serivce.ShowMessRed("Giao dịch bị hủy bỏ do không đủ bạc");
                        player.ClearGiaoDich(true, 14);
                        this.ClearGiaoDich(true, 15);
                        return;
                    }
                    // Check error item trade
                    boolean huy = false;

                    for (Item item : listItemMe) {
                        Item itembag = this.getItemBagByIndex(item.index);
                        if (item.id != itembag.id || item.isLock != itembag.isLock || item.amount != itembag.amount
                                || !item.strOptions.equals(itembag.strOptions)) {
                            huy = true;
                        }
                    }
                    for (Item item : listItemPlayer) {
                        Item itembag = player.getItemBagByIndex(item.index);
                        if (item.id != itembag.id || item.isLock != itembag.isLock || item.amount != itembag.amount
                                || !item.strOptions.equals(itembag.strOptions)) {
                            huy = true;
                        }
                    }

                    if (huy) {
                        this.client.session.serivce.ShowMessRed("Giao dịch bị hủy bỏ do item đã bị xáo trộn");
                        player.client.session.serivce.ShowMessRed("Giao dịch bị hủy bỏ do item đã bị xáo trộn");
                        player.ClearGiaoDich(true, 16);
                        this.ClearGiaoDich(true, 17);

                        return;
                    }
                    logPlayer = "(" + player.infoChar.name + " ID:" + player.id + ") giao dịch với ("
                            + this.infoChar.name + " ID: " + this.id + ") ->: ";

                    logMe = "(" + this.infoChar.name + " ID:" + this.id + ") giao dịch với (" + player.infoChar.name
                            + " ID: " + player.id + ") ->: ";
                    if (bacMe > 0) {
                        mineBac(bacMe, false, false, "Giao dịch với: " + player.infoChar.name);
                        player.addBac(bacMe, false, false, "Giao dịch với: " + infoChar.name);
                        logMe += "(Cho -" + bacMe + " bạc, ";
                        logPlayer += "(Nhận +" + bacMe + " bạc, ";
                    }
                    if (bacPlayer > 0) {
                        addBac(bacPlayer, false, false, "Giao dịch với: " + player.infoChar.name);
                        player.mineBac(bacPlayer, false, false, "Giao dịch với: " + infoChar.name);
                        logPlayer += "(Cho -" + bacPlayer + " bạc, ";
                        logMe += "(Nhận +" + bacPlayer + " bạc, ";
                    }
                    // remove item Me
                    for (Item item : listItemMe) {
                        logMe += "(Cho -" + item.amount + " " + (item.getItemTemplate().name + "), ");
                        this.removeItemBagByIndex(item.index, "Giao dịch với " + player.infoChar.name);
                    }
                    // remove item Player
                    for (Item item : listItemPlayer) {
                        logPlayer += "(Cho -" + item.amount + " " + (item.getItemTemplate().name + "), ");
                        player.removeItemBagByIndex(item.index, "Giao dịch với " + this.infoChar.name);
                    }

                    List<Item> listItemAddMe = new ArrayList<>();
                    List<Item> listItemAddPlayer = new ArrayList<>();

                    // add item + add to list
                    for (Item item : listItemMe) {
                        logMe += "(Nhận +" + item.amount + " " + (item.getItemTemplate().name + "), ");
                        listItemAddPlayer.add(player.addItem(item, true, "Nhận VP GD từ " + this.infoChar.name));
                        player.msgAddItemBag(item);
                    }

                    for (Item item : listItemPlayer) {
                        logMe += "(Nhận +" + item.amount + " " + (item.getItemTemplate().name + "), ");
                        listItemAddMe.add(this.addItem(item, true, "Nhận VP GD từ " + player.infoChar.name));
                        this.msgAddItemBag(item);
                    }
                    this.client.session.serivce.doneGiaoDich(this.infoChar.bac, listItemAddMe);
                    player.client.session.serivce.doneGiaoDich(player.infoChar.bac, listItemAddPlayer);
                    this.ClearGiaoDich(false, 18);
                    player.ClearGiaoDich(false, 19);
                    Utlis.logTrade(logMe);
                    Utlis.logTrade(logPlayer);
                    CharDB.Update(this.client.mChar);
                    CharDB.Update(player.client.mChar);
                }

            } else {
                this.ClearGiaoDich(true, 20);

            }

        } catch (Exception ex) {
            if (this.zone.findCharInMap(this.traDe.Id_Char_Trade) != null) {
                this.zone.findCharInMap(this.traDe.Id_Char_Trade).ClearGiaoDich(true, 21);
            }
            this.ClearGiaoDich(true, 22);

        }
    }

    public void HuyGiaoDich() {
        try {

            if (this.zone.findCharInMap(this.traDe.Id_Char_Trade) != null) {
                this.zone.findCharInMap(this.traDe.Id_Char_Trade).ClearGiaoDich(true, 23);
            }
            this.ClearGiaoDich(false, 24);
        } catch (Exception ex) {
            ClearGiaoDich(false, 25);

        }
    }

    public void ClearGiaoDich(boolean closetab, int debug) {
        UTPKoolVN.Debug("debug gdddd:" + debug);
        try {
            this.traDe.Id_Char_Trade = -1;
            this.traDe.IsTrade = false;
            this.traDe.Name_Char_Send = null;
            this.traDe.IsLock = false;
            this.traDe.IsHold = false;
            this.traDe.bac = 0;
            this.traDe.Items.clear();
            this.info.Trade = System.currentTimeMillis() + 10000;
            if (closetab) {
                this.client.session.serivce.closeTab();
                this.client.session.serivce.ShowMessRed("Giao dịch bị hủy bỏ");
            }
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void chatWord(String str) {
        try {
            if (info.chatTg > System.currentTimeMillis()) {
                long time = (info.chatTg - System.currentTimeMillis()) / 1000;
                this.client.session.serivce.ShowMessRed("Bạn chỉ có thể chat sau " + time + " giây nữa.");
                return;
            }
            if (infoChar.vang < 2) {
                this.client.session.serivce.ShowMessRed("Bạn không đủ vàng.");
                return;
            }
            mineVang(2, false, true, "Chat Word");
            Message msg = new Message((byte) 22);
            msg.writeByte(1);
            msg.writeUTF(infoChar.name);
            msg.writeUTF(str);
            PlayerManager.getInstance().sendMessageAllChar(msg);
            info.chatTg = System.currentTimeMillis() + 20000;
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void chatGroup(String str) {
        try {
            if (infoChar.groupId == -1) {
                this.client.session.serivce.ShowMessRed("Bạn chưa có nhóm");
                return;
            }
            Message msg = new Message((byte) 26);
            msg.writeUTF(infoChar.name);
            msg.writeUTF(str);
            Group.gI().sendMessageAllMember(msg, infoChar.groupId);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void chatFamily(String str) {
        try {
            if (infoChar.familyId == -1) {
                this.client.session.serivce.ShowMessRed("Bạn chưa có gia tộc");
                return;
            }
            FamilyTemplate giatoc = Family.gI().getGiaToc(this);
            if (giatoc == null)
                return;
            Family_Member member = Family.gI().getMe(this, giatoc);
            if (member == null)
                return;
            if (member.isCamChat) {
                this.client.session.serivce.ShowMessRed("Bạn đã bị cầm chat");
                return;
            }
            Family.gI().sendChat(infoChar.name, str, infoChar.familyId);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void roiGroup() {
        try {
            if (infoChar.groupId == -1) {
                this.client.session.serivce.ShowMessRed("Bạn chưa có nhóm");
                return;
            }
            GroupTemplate group = Group.gI().getGroup(infoChar.groupId);
            if (group != null && group.getChar(this.id) != null) {
                group.out(client);
                client.session.serivce.outGroup();
            }
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void thuVanMay() { // open sủa quày lucky
        if (getCountNullItemBag() == 0) {
            client.session.serivce.ShowMessGold("Túi không đủ chỗ chứa");
            return;
        }
        if (infoChar.vang < 25) {
            client.session.serivce.ShowMessGold("Không đủ vàng");
            return;
        }
        int random = Utlis.nextInt(0, DataCenter.gI().DataLucky.size() - 1);
        LuckyTpl luckyTpl = DataCenter.gI().DataLucky.get(random);
        if (luckyTpl != null) {
            mineVang(25, true, true, "Thử vận may");
            client.session.serivce.sendQuayLucky((short) luckyTpl.idItem, luckyTpl.amount);
            if (luckyTpl.idItem == 163) {
                addBacKhoa(luckyTpl.amount, true, true, "Thử vận may");
                return;
            }
            if (luckyTpl.idItem == 191) {
                addBac(luckyTpl.amount, true, true, "Thử vận may");
                return;
            }

            Item itemAdd = new Item(luckyTpl.idItem, true, luckyTpl.amount);
            itemAdd.strOptions = luckyTpl.STROP;
            if (luckyTpl.expiry > 0)
                itemAdd.expiry = System.currentTimeMillis() + luckyTpl.expiry;
            addItem(itemAdd, "Thử vận may");
            msgAddItemBag(itemAdd);
        }

        // for (int i = 0; i < DataCenter.gI().DataLucky.size(); i++){
        // LuckyTpl luckyTpl = DataCenter.gI().DataLucky.get(i);
        // if(random == luckyTpl.tile) {
        // client.session.serivce.sendQuayLucky((short) luckyTpl.idItem,
        // luckyTpl.amount);
        // Item itemAdd = new Item(luckyTpl.idItem, true, luckyTpl.amount);
        // itemAdd.strOptions = luckyTpl.STROP;
        // if(luckyTpl.expiry > 0) itemAdd.expiry =
        // System.currentTimeMillis()+luckyTpl.expiry;
        // addItem(itemAdd, "Thử vận may");
        // msgAddItemBag(itemAdd);
        // break;
        // }
        // }
    }

    public void cheTao(byte type) {
        try {
            if (type < 0 || type > 8)
                return;
            int levelCheTao = (infoChar.levelCheTao / 100 + 1);
            if (levelCheTao <= type) {
                client.session.serivce.ShowMessGold("Bạn chưa thể chế tạo cấp này");
            } else {

                Item itemMine2 = getItemBagById(160, false);
                if (type == 5) {
                    itemMine2 = getItemBagById(562);
                } else if (type == 6) {
                    itemMine2 = getItemBagById(564);
                } else if (type == 7) {
                    itemMine2 = getItemBagById(566);
                } else if (type == 8) {
                    itemMine2 = getItemBagById(354);
                }

                int mineHoatLuc = DataCenter.gI().getHoatLucCheTao(type);
                int valuaChetao = DataCenter.gI().getValueCheTao(type);

                if (infoChar.hoatLuc < mineHoatLuc) {
                    client.session.serivce.ShowMessGold("Không đủ hoạt lực");
                    return;
                }
                if (itemMine2 == null || itemMine2.getAmount() < valuaChetao) {
                    client.session.serivce.ShowMessGold("Không đủ nguyên liệu");
                    return;
                }
                if (getCountNullItemBag() == 0) {
                    client.session.serivce.ShowMessGold("Hành trang không đủ chỗ chứa");
                    return;
                }
                client.session.serivce.loadPhanTram(this, 500, "Đang chế tạo");
                Thread.sleep(500L);
                client.session.serivce.xoaTab(this);

                //

                infoChar.hoatLuc -= mineHoatLuc;

                removeItemBag(itemMine2, valuaChetao, "Chế tạo");
                Item itemAdd = new Item(DataCenter.gI().getItemAddCheTao(type));
                if (type == 1)
                    itemAdd.amount = 10;
                addItem(itemAdd, "Chế tạo");
                msgAddItemBag(itemAdd);
                infoChar.levelCheTao += (type + 1) * 3;

                Writer writer = new Writer();
                writer.writeInt(infoChar.levelCheTao);
                writer.writeInt(infoChar.hoatLuc);
                if (itemMine2 != null && itemMine2.getAmount() > 0) {
                    writer.writeShort(1);
                    itemMine2.write(writer);
                } else {
                    writer.writeShort(0);
                }

                this.client.session.serivce.doneCheTao(writer);
            }

        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void msgUpdateHp() {
        if (infoChar.hp > infoChar.hpFull) {
            infoChar.hp = infoChar.hpFull;
        }
        try {
            Writer writer = new Writer();
            writer.writeInt(infoChar.hp);
            client.session.serivce.updateHp_Me(writer);

        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
        try {
            Writer writer = new Writer();
            writer.writeInt(this.id);
            writer.writeInt(infoChar.hp);
            zone.updateHp_Orther(client, writer);

        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }

    }

    public void msgUpdateHpFull() {
        if (infoChar.hp > infoChar.hpFull) {
            infoChar.hp = infoChar.hpFull;
        }
        try {
            Writer writer = new Writer();
            writer.writeInt(infoChar.hpFull);
            writer.writeInt(infoChar.hp);
            client.session.serivce.updateHpFull_Me(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
        if (zone != null) {
            try {
                Writer writer = new Writer();
                writer.writeInt(this.id);
                writer.writeInt(infoChar.hpFull);
                writer.writeInt(infoChar.hp);
                zone.updateHpFull_Orther(client, writer);
            } catch (Exception ex) {
                Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
    }

    public void msgUpdateMp() {
        if (infoChar.mp > infoChar.mpFull) {
            infoChar.mp = infoChar.mpFull;
        }
        try {
            Writer writer = new Writer();
            writer.writeInt(infoChar.mp);
            client.session.serivce.updateMp_Me(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }

        try {
            Writer writer = new Writer();
            writer.writeInt(this.id);
            writer.writeInt(infoChar.mp);
            zone.updateMp_Orther(client, writer);

        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }

    }

    public void msgUpdateMpFull() {
        if (infoChar.mp > infoChar.mpFull) {
            infoChar.mp = infoChar.mpFull;
        }
        try {
            Writer writer = new Writer();
            writer.writeInt(infoChar.mpFull);
            writer.writeInt(infoChar.mp);
            client.session.serivce.updateMpFull_Me(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
        if (zone != null) {
            try {
                Writer writer = new Writer();
                writer.writeInt(this.id);
                writer.writeInt(infoChar.mpFull);
                writer.writeInt(infoChar.mp);
                zone.updateMpFull_Orther(client, writer);

            } catch (Exception ex) {
                Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
    }

    public void MineHp(int hp) {
        try {
            if (hp <= 0 || infoChar.isDie)
                return;
            infoChar.hp -= hp;
            if (infoChar.hp <= 0) {
                infoChar.hp = 0;
                infoChar.isDie = true;
            }
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void updatePK(byte typeTyVo) {
        if (info.typePK == 1 && info.idCharPk != -1) {
            client.session.serivce.endTyVo(this, this.id, info.idCharPk, (byte) typeTyVo);
            Char get = PlayerManager.getInstance().getChar(info.idCharPk);
            if (get != null && get.info.idCharPk == this.id) {
                get.info.typePK = 0;
                get.info.idCharPk = -1;
            }
            info.typePK = 0;
            info.idCharPk = -1;
        } else if (info.idCharPk != -1) {

            if (info.isCuuSat) {
                info.isCuuSat = false;
                client.session.serivce.huyCuuSat(info.idCharPk, false);
            } else {
                client.session.serivce.huyCuuSat(this.id, false);
            }
            Char get = PlayerManager.getInstance().getChar(info.idCharPk);
            if (get != null && get.info.idCharPk == this.id) {
                if (get.info.isCuuSat) {
                    get.info.isCuuSat = false;
                    get.client.session.serivce.huyCuuSat(get.info.idCharPk, false);
                } else {
                    get.client.session.serivce.huyCuuSat(get.id, false);
                }
                get.info.idCharPk = -1;
            }
            info.idCharPk = -1;
        }
    }

    public int getValueEff(int... idEff) {
        int value = 0;

        for (int j = 0; idEff != null && j < idEff.length; j++) {
            for (int i = 0; i < listEffect.size(); i++) {
                Effect effect = this.listEffect.get(i);
                if (effect != null && effect.id == idEff[j]) {
                    value += effect.value;
                }
            }
        }
        return value;
    }

    public void MineMp(int mp) {
        if (mp <= 0 || infoChar.isDie)
            return;

        infoChar.mp -= mp;
        if (infoChar.mp <= 0) {
            infoChar.mp = 0;
        }
    }

    public void PlusHp(int hp) {
        if (hp <= 0 || infoChar.isDie)
            return;
        infoChar.hp += hp;
        if (infoChar.hp >= infoChar.hpFull) {
            infoChar.hp = infoChar.hpFull;
        }
    }

    public void PlusMp(int mp) {
        if (mp <= 0 || infoChar.isDie)
            return;
        infoChar.mp += mp;
        if (infoChar.mp >= infoChar.mpFull) {
            infoChar.mp = infoChar.mpFull;
        }
    }

    public void msgUpdateHpMpWhenAttack(boolean cm, String name) {
        try {
            Writer writer = new Writer();
            writer.writeInt(this.id);
            writer.writeInt(infoChar.mp);
            writer.writeInt(infoChar.hp);
            writer.writeBoolean(cm);
            if (infoChar.isDie) {
                writer.writeShort(cx);
                writer.writeShort(cy);
                writer.writeUTF(name);
            }
            zone.updateHpMpWhenAttack(writer);

            if (infoChar.isDie && name.length() > 0) {
                Char get = PlayerManager.getInstance().getChar(name);
                if (get != null) {
                    if (get.info.typePK == 3) {
                        get.infoChar.lvPk++;
                        get.client.session.serivce.ShowMessGold(
                                "Bạn đã đánh trọng thương " + this.infoChar.name + " bạn bị tăng 1 cấp PK");
                        Enemy newEnmy = new Enemy();
                        newEnmy.name = get.infoChar.name;
                        newEnmy.id = get.id;
                        addEnemy(newEnmy);

                    } else if (get.info.typePK == 0 && get.info.idCharPk == this.id && !get.info.isCuuSat) {
                        get.infoChar.lvPk += 2;
                        get.client.session.serivce.ShowMessGold(
                                "Bạn đã đánh trọng thương " + this.infoChar.name + " bạn bị tăng 2 cấp PK");
                        Enemy newEnmy = new Enemy();
                        newEnmy.name = get.infoChar.name;
                        newEnmy.id = get.id;
                        addEnemy(newEnmy);
                        if (get.info.isBuaUeTho) {
                            client.session.serivce.sendBuaUeTho(get.infoChar.name, this);
                        }
                    }
                    updatePK((byte) 3);
                }
            }

        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void msgOpenTabConfig(Item item, String arr) {
        try {
            Writer writer = new Writer();
            writer.writeShort(item.index);

            writer.writeUTF(arr);
            this.client.session.serivce.openTabItem(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());

        }
    }

    public void msgOpenTabSaoCuongHoa(Item item) {
        try {

            ArrayList<String> text = new ArrayList<String>();
            ArrayList<IActionItem> action = new ArrayList<IActionItem>();
            for (int i = 0; i < this.arrItemBody.length; i++) {
                if (this.arrItemBody[i] != null) {
                    if (i >= 0 && i <= 9) {
                        int index = i;

                        if (this.arrItemBody[i].u()) {
                            action.add(new IActionItem() {
                                @Override
                                public void action(Client client) {
                                    Char.this.arrItemBody[index].a(Char.this.arrItemBody[index].level + 4);
                                    setUpInfo(true);
                                }
                            });
                            text.add("Nâng cấp cho: " + (this.arrItemBody[i].getItemTemplate().name) + "(+"
                                    + this.arrItemBody[i].level + " lên +" + (this.arrItemBody[i].level + 4) + ")");
                        }
                    }
                }
            }
            String s = "";
            for (int i = 0; i < text.size(); i++) {
                s += text.get(i);
                if (i < text.size() - 1) {
                    s += ";";
                }
            }
            if (s.length() > 0) {
                item.arrayAction = action.toArray(new IActionItem[action.size()]);
                Writer writer = new Writer();
                writer.writeShort(item.index);
                writer.writeUTF(s);
                this.client.session.serivce.openTabItem(writer);
            }

        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());

        }
    }

    public void upLevel(int levelOld, int levelNew) {
        int levelUp = levelNew - levelOld;
        if (levelUp <= 0) {
            return;
        }
        infoChar.diemTiemNang += (levelUp * 10);
        infoChar.diemKyNang += (levelUp * 1);
        msgUpdateDataChar();
    }

    public void msgDataBag() {
        try {
            Writer writer = new Writer();
            writer.writeInt(infoChar.bac);
            this.writeItemBag(writer, arrItemBag);
            writer.writeByte(infoChar.levelPhanThan);
            writer.writeByte(infoChar.MaxLevelPhanThan);
            writer.writeInt(infoChar.expPhanThan);
            this.client.session.serivce.dataBag(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());

        }
    }

    public void addEnemy(Enemy enemy) {
        removeEnemy(enemy.name);
        listEnemy.add(enemy);
        client.session.serivce.msgAddEn(enemy.name, true);
    }

    public void SendBox() {
        try {
            Writer writer = new Writer();
            writer.writeByte(50);
            writer.writeShort(arrItemBox.length); // sl item

            this.writeItemBag(writer, arrItemBox);
            if (infoChar.timeGiuRuong < System.currentTimeMillis()) {
                writer.writeBoolean(false);
            } else {
                writer.writeBoolean(true);
            }
            writer.writeInt(infoChar.bac_Ruong);
            writer.writeInt(infoChar.bacKhoa_Ruong);
            writer.writeInt(infoChar.vang_Ruong);
            writer.writeInt(infoChar.vangKhoa_Ruong);
            this.client.session.serivce.SendBox(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());

        }
    }

    public void rutTien(byte type, int sl) {
        if (type == 0) {
            if (sl <= 0 || this.infoChar.bac_Ruong < sl) {
                client.session.serivce.ShowMessGold("Không đủ bạc trong rương.");
            } else if (((long) this.infoChar.bac + sl) > 2100000000) {
                client.session.serivce.ShowMessGold("Số tiền quá mức chứa vui lòng giảm bớt.");
            } else {
                this.infoChar.bac_Ruong -= sl;
                addBac(sl, false, true, "Rút từ rương");
                SendBox();
            }
        } else if (type == 1) {
            if (sl <= 0 || this.infoChar.bacKhoa_Ruong < sl) {
                client.session.serivce.ShowMessGold("Không đủ bạc khóa trong rương.");
            } else if (((long) this.infoChar.bacKhoa + sl) > 2100000000) {
                client.session.serivce.ShowMessGold("Số tiền quá mức chứa vui lòng giảm bớt.");
            } else {
                this.infoChar.bacKhoa_Ruong -= sl;
                addBacKhoa(sl, false, true, "Rút từ rương");
                SendBox();
            }
        } else if (type == 2) {
            if (sl <= 0 || this.infoChar.vang_Ruong < sl) {
                client.session.serivce.ShowMessGold("Không đủ vàng trong rương.");
            } else if (((long) this.infoChar.vang + sl) > 2100000000) {
                client.session.serivce.ShowMessGold("Số tiền quá mức chứa vui lòng giảm bớt.");
            } else {
                this.infoChar.vang_Ruong -= sl;
                addVang(sl, false, true, "Rút từ rương");
                SendBox();
            }
        } else if (type == 3) {
            if (sl <= 0 || this.infoChar.vangKhoa_Ruong < sl) {
                client.session.serivce.ShowMessGold("Không đủ vàng khóa trong rương.");
            } else if (((long) this.infoChar.vangKhoa + sl) > 2100000000) {
                client.session.serivce.ShowMessGold("Số tiền quá mức chứa vui lòng giảm bớt.");
            } else {
                this.infoChar.vangKhoa_Ruong -= sl;
                addVangKhoa(sl, false, true, "Rút từ rương");
                SendBox();
            }
        }

    }

    public void catTien(byte type, int sl) {
        if (type == 0) {
            if (sl <= 0 || this.infoChar.bac < sl) {
                client.session.serivce.ShowMessGold("Không đủ bạc trên người.");
            } else if (((long) this.infoChar.bac_Ruong + sl) > 2100000000) {
                client.session.serivce.ShowMessGold("Số tiền quá mức chứa vui lòng giảm bớt");
            } else {
                this.infoChar.bac_Ruong += sl;
                mineBac(sl, false, true, "Cất vào rương");
                SendBox();
            }
        } else if (type == 1) {
            if (sl <= 0 || this.infoChar.bacKhoa < sl) {
                client.session.serivce.ShowMessGold("Không đủ bạc khóa trên người.");
            } else if (((long) this.infoChar.bacKhoa_Ruong + sl) > 2100000000) {
                client.session.serivce.ShowMessGold("Số tiền quá mức chứa vui lòng giảm bớt");
            } else {
                this.infoChar.bacKhoa_Ruong += sl;
                mineBacKhoa(sl, false, true, "Cất vào rương");
                SendBox();
            }
        } else if (type == 2) {
            if (sl <= 0 || this.infoChar.vang < sl) {
                client.session.serivce.ShowMessGold("Không đủ vàng trên người.");
            } else if (((long) this.infoChar.vang_Ruong + sl) > 2100000000) {
                client.session.serivce.ShowMessGold("Số tiền quá mức chứa vui lòng giảm bớt");
            } else {
                this.infoChar.vang_Ruong += sl;
                mineVang(sl, false, true, "Cất vào rương");
                SendBox();
            }
        } else if (type == 3) {
            if (sl <= 0 || this.infoChar.vangKhoa < sl) {
                client.session.serivce.ShowMessGold("Không đủ vàng khóa trên người.");
            } else if (((long) this.infoChar.vangKhoa_Ruong + sl) > 2100000000) {
                client.session.serivce.ShowMessGold("Số tiền quá mức chứa vui lòng giảm bớt");
            } else {
                this.infoChar.vangKhoa_Ruong += sl;
                mineVangKhoa(sl, false, true, "Cất vào rương");
                SendBox();
            }
        }

    }

    public void addLengthBox() {
        if (infoChar.vang < 90) {
            client.session.serivce.ShowMessGold("Không có đủ vàng.");
        } else if (arrItemBox.length >= 217) {
            client.session.serivce.ShowMessGold("Rương đã được mở rộng tối đa.");
        } else {
            mineVang(90, true, true, "Mở rộng rương");
            Item[] new_arrItemBox = new Item[arrItemBox.length + 9];
            for (int i = 0; i < new_arrItemBox.length && i < this.arrItemBox.length; i++) {
                new_arrItemBox[i] = arrItemBox[i];
            }
            this.arrItemBox = new_arrItemBox;
            SendBox();
        }
    }

    public void UpdateLogin() {
        // check hsd item bag
        for (int i = 0; i < this.arrItemBag.length; i++) {
            Item item = this.arrItemBag[i];
            if (item != null && item.expiry >= 0L) {
                if (item.expiry < System.currentTimeMillis()) {
                    this.removeItemBagByIndex(i, "HẾT HẠN SD");
                }
            }
        }
        // check hsd item body
        for (int i = 0; i < this.arrItemBody.length; i++) {
            Item item = this.arrItemBody[i];
            if (item != null && item.expiry >= 0L) {
                if (item.expiry < System.currentTimeMillis()) {
                    this.removeItemBodyByIndex(i, "HẾT HẠN SD");
                }
            }
        }
        // check hsd item body 2
        for (int i = 0; i < this.arrItemBody2.length; i++) {
            Item item = this.arrItemBody2[i];
            if (item != null && item.expiry >= 0L) {
                if (item.expiry < System.currentTimeMillis()) {
                    this.removeItemBody2ByIndex(i, "HẾT HẠN SD");
                }
            }
        }

        // check hsd danh hiệu
        for (int i = this.listDanhHieu.size() - 1; i >= 0; i--) {
            if (this.listDanhHieu.get(i).hsd >= 0L) {
                if (this.listDanhHieu.get(i).hsd < System.currentTimeMillis()) {
                    this.listDanhHieu.remove(i);
                }
            }
        }
        // check Thư
        for (int i = this.listThu.size() - 1; i >= 0; i--) {
            if (this.listThu.get(i).time < (System.currentTimeMillis() / 1000L)) {
                this.listThu.remove(i);
            }
        }

        // check group
        if (infoChar.groupId != -1) {
            GroupTemplate group = Group.gI().getGroup(infoChar.groupId);
            if (group == null || group.getChar(this.id) == null) {
                infoChar.groupId = -1;
            }
        }
    }

    public void updateSendChar() {
        Family.gI().updateFamily(this);
        updateInfoToFamily();
        updatePhucLoiHangNgay();
        updatePhucLoiHangTuan();
    }

    public void msgUpdateDataChar() {
        try {
            Writer writer = new Writer();
            writer.writeShort(infoChar.diemTiemNang);
            writer.writeShort(infoChar.diemKyNang);
            writer.writeByte(0);
            writer.writeByte(0);
            writer.writeByte(0);
            writer.writeBoolean(true);
            writer.writeInt(infoChar.pointNAP);
            this.client.session.serivce.updateDataChar(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());

        }
    }

    public void msgGetInfo(Client clientsend) {
        try {

            Writer writer = new Writer();
            writer.writeInt(infoChar.levelCheTao);
            writer.writeInt(infoChar.hoatLuc);
            writer.writeByte(infoChar.sachChienDau);
            writer.writeShort(infoChar.diemKyNang);
            writer.writeShort(infoChar.diemTiemNang);
            writer.writeBoolean(false);
            writer.writeByte(0);
            writer.writeShort(arrayTiemNang[0]);
            writer.writeShort(arrayTiemNang[1]);
            writer.writeShort(arrayTiemNang[2]);
            writer.writeShort(arrayTiemNang[3]);
            writer.writeInt(getDame());
            writer.writeInt(getDameMob());

            /*
             * 0. Chính xác
             */
            writer.writeShort(getChinhXac());/*
                                              * 1. Bỏ qua né tránh
                                              */
            writer.writeShort(getBoQuaNeTranh());/*
                                                  * 2. Chí mạng
                                                  */
            writer.writeShort(getChiMang());/*
                                             * 3. Tấn công khi đánh chí mạng
                                             */
            writer.writeShort(getTangTanCongChiMang());/*
                                                        * 4. Tăng tấn công lên hệ Lôi
                                                        */
            writer.writeShort(getTangTanCongLenLoi());/*
                                                       * 5. Tăng tấn công lên hệ Thổ
                                                       */
            writer.writeShort(getTangTanCongLenTho());/*
                                                       * 6. Tăng tấn công lên hệ Thủy
                                                       */
            writer.writeShort(getTangTanCongLenThuy());/*
                                                        * 
                                                        * 7. Tăng tấn công lên hệ Hỏa
                                                        */
            writer.writeShort(getTangTanCongLenHoa());/*
                                                       * 8. Tăng tấn công lên hệ Phong
                                                       */
            writer.writeShort(getTangTanCongLenPhong());/*
                                                         * 9. Gây suy yếu
                                                         */
            writer.writeShort(getGaySuyYeu());/*
                                               * 10. Gây trúng độc
                                               */
            writer.writeShort(getGayTrungDoc());/*
                                                 * 11. Gây làm chậm
                                                 */
            writer.writeShort(getGayLamCham());/*
                                                * 12. Gây bỏng
                                                */
            writer.writeShort(getGayBong());/*
                                             * 13. Gây choáng
                                             */
            writer.writeShort(getGayChoang());/*
                                               * 14. Bỏ qua kháng tính
                                               */
            writer.writeShort(getBoQuaKhangTinh());/*
                                                    * 15. Kháng Lôi
                                                    */
            writer.writeShort(getKhangLoi());/*
                                              * 16. Kháng Thổ
                                              */
            writer.writeShort(getKhangTho());/*
                                              * 17. Kháng Thủy
                                              */
            writer.writeShort(getKhangThuy());/*
                                               * 18. Kháng Hỏa
                                               */
            writer.writeShort(getKhangHoa());/*
                                              * 19. Kháng Phong
                                              */
            writer.writeShort(getKhangPhong());/*
                                                * 20. Giảm sát thương
                                                */
            writer.writeShort(getGiamSatThuong());/*
                                                   * 21. Tốc độ di chuyển
                                                   */
            writer.writeShort(infoChar.speedMove);/*
                                                   * 22. Né tránh
                                                   */
            writer.writeShort(getNeTranh());/*
                                             * 23. Phản đòn
                                             */
            writer.writeShort(getPhanDon());/*
                                             * 24. Phòng chí mạng
                                             */
            writer.writeShort(getGiamTanCongKhiBiCM());/*
                                                        * 25. Tương khắc lên hệ
                                                        */
            writer.writeShort(getTangTuongKhac());/*
                                                   * 26. Giảm tương khắc của hệ
                                                   */
            writer.writeShort(getGiamTuongKhac());/*
                                                   * 27. Giảm gây suy yếu
                                                   */
            writer.writeShort(getGiamSuyYeu());/*
                                                * 28. Giảm gây trúng độc
                                                */
            writer.writeShort(getGiamTrungDoc());/*
                                                  * 29. Giảm gây làm chậm
                                                  */
            writer.writeShort(getGiamLamCham());/*
                                                 * 30. Giảm gây bỏng
                                                 */
            writer.writeShort(getGiamGayBong());/*
                                                 * 31. Giảm gây choáng
                                                 */
            writer.writeShort(getGiamChoang());/*
                                                * 32. Giảm trừ chí mạng
                                                */
            writer.writeShort(getGiamTruChiMang());/*
                                                   */

            clientsend.session.serivce.getInfo(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());

        }
    }

    public void effAttackPlayer(Char playerAttack, Skill skill) {

        int suyYeu = getGaySuyYeu(skill) - playerAttack.getGiamSuyYeu();
        int trungDoc = getGayTrungDoc(skill) - playerAttack.getGiamTrungDoc();
        int lamCham = getGayLamCham(skill) - playerAttack.getGiamLamCham();
        int bong = getGayBong(skill) - playerAttack.getGiamGayBong();
        int choang = getGayChoang(skill) - playerAttack.getGiamChoang();
        createEffectPlayer(playerAttack, 8, suyYeu, true);
        createEffectPlayer(playerAttack, 9, trungDoc, true);
        createEffectPlayer(playerAttack, 38, lamCham, true);
        createEffectPlayer(playerAttack, 11, bong, true);
        createEffectPlayer(playerAttack, 12, choang, true);

        if (getHieuUngNgauNhien() > 0 && Utlis.nextInt(555) < getHieuUngNgauNhien()) {
            int randomType = Utlis.nextInt(5) + 8;
            createEffectPlayer(playerAttack, randomType, Utlis.nextInt(50, 180), false);
        }
    }

    private void createEffectPlayer(Char playerAttack, int effectID, int value, boolean isRand) {
        if (value > 0 && Utlis.nextInt(888) < value || !isRand) {
            int time = Math.min(value * 30, 5555);
            Effect newEff = new Effect(effectID, value, System.currentTimeMillis(), time);
            newEff.charAttack = this;
            playerAttack.addEffect(newEff);
        }
    }

    public void effAttackMob(Mob mobAttack, Skill skill) {

        createEffectMob(mobAttack, 8, getGaySuyYeu(skill), true);
        createEffectMob(mobAttack, 9, getGayTrungDoc(skill), true);
        createEffectMob(mobAttack, 38, getGayLamCham(skill), true);
        createEffectMob(mobAttack, 11, getGayBong(skill), true);
        createEffectMob(mobAttack, 12, getGayChoang(skill), true);

        if (getHieuUngNgauNhien() > 0 && Utlis.nextInt(555) < getHieuUngNgauNhien()) {
            int randomType = Utlis.nextInt(5) + 8;
            createEffectMob(mobAttack, randomType, Utlis.nextInt(50, 180), false);
        }
    }

    private void createEffectMob(Mob mobAttack, int effectID, int value, boolean isRand) {
        if (value > 0 && Utlis.nextInt(888) < value || !isRand) {
            int time = Math.min(value * 50, 8000);
            Effect newEff = new Effect(effectID, value, System.currentTimeMillis(), time);
            newEff.charAttack = this;
            mobAttack.addEff(newEff);
            zone.addEffMob(newEff, (short) mobAttack.idEntity);
        }
    }

    public void nangCapSkill(short id) {
        Skill skill = this.getSkillWithIdTemplate(id);
        if (skill == null || skill.level >= skill.getSkillTemplate().levelMax
                || DataCenter.gI().getSkillWithIdAndLevel(id, skill.level + 1) != null
                        && DataCenter.gI().getSkillWithIdAndLevel(id, skill.level + 1).levelNeed > this.level()
                || this.infoChar.diemKyNang <= 0) {
            return;
        }
        this.infoChar.diemKyNang--;
        for (int i = 0; i < arraySkill.length; i++) {
            if (arraySkill[i] == skill) {
                arraySkill[i] = DataCenter.gI().getSkillWithIdAndLevel(id, skill.level + 1);
                if (arraySkill[i].idTemplate == skillFight.idTemplate) {
                    skillFight = arraySkill[i];
                }
                break;
            }
        }
        msgGetInfo(client);
        msgUpdateDataChar();
        msgUpdateSkill();
        client.mChar.setUpInfo(true);
        if (client.mChar.infoChar.idTask == 8 && client.mChar.infoChar.idStep == 11)
            TaskHandler.gI().PlusTask(client.mChar);
    }

    public void msgUpdateSkill() {
        try {
            Writer writer = new Writer();
            this.writeSkill(writer);
            this.client.session.serivce.updateSkill(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());

        }
    }

    public void focusSkill(short id) {
        Skill skill = this.getSkillWithIdTemplate(id);
        if (skill == null || skill.level <= 0 || skill.levelNeed > this.level()) {
            return;
        }
        this.skillFight = skill;
        msgUpdateSkill();
    }

    public void veMapMacDinh() {
        try {
            Map.maps[client.mChar.infoChar.mapDefault].addChar(client);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void reSpawn() {
        this.infoChar.hp = this.infoChar.hpFull;
        this.infoChar.mp = this.infoChar.mpFull;
        this.infoChar.isDie = false;
        try {
            Writer writer = new Writer();
            writer.writeInt(id);
            writer.writeInt(infoChar.hp);
            writer.writeInt(infoChar.mp);
            zone.reSpawn(writer);
        } catch (Exception ex) {

        }
        try {
            Writer writer = new Writer();
            writer.writeInt(id);
            writeXY(writer);
            zone.setXYChar(writer);
        } catch (Exception ex) {

        }

    }

    public void addEff_ItemBody(short type) {
        if (type == 14) {
            Item item = this.arrItemBody[14];
            if (item != null) {
                if (item.id == 788) {
                    this.addEffect(new Effect(101, Utlis.nextInt(100, 501), System.currentTimeMillis(), 120 * 1000));
                }
            }
        } else if (type == 16) {
            Item item = this.arrItemBody[16];
            if (item != null) {
                if (item.id == 812) {
                    this.addEffect(new Effect(100, 222, System.currentTimeMillis(), 300 * 1000));
                } else if (item.id == 789) {
                    this.addEffect(new Effect(100, Utlis.nextInt(500, 2000), System.currentTimeMillis(), 300 * 1000));
                } else if (item.id == 880) {
                    this.addEffect(new Effect(100, Utlis.nextInt(500, 2000), System.currentTimeMillis(), 300 * 1000));
                }
            }
        }

    }

    public void addEffect(Effect effect) {

        for (int i = listEffect.size() - 1; i >= 0; i--) {
            if (listEffect.get(i).getEffectTemplate().type == effect.getEffectTemplate().type) {
                if (listEffect.get(i).getEffectTemplate().id == effect.getEffectTemplate().id) {
                    long l = ((long) effect.maintain) + ((long) listEffect.get(i).getMaintain());
                    if (l > 2000000000) {
                        l = 2000000000;
                    } else if (l < 0) {
                        l = 0;
                    }
                    effect.maintain = (int) l;
                }
                Effect.setEff(this, listEffect.get(i), true);
                listEffect.remove(i);
                break;
            }
        }
        if (effect.getEffectTemplate().type == 6 || effect.getEffectTemplate().type == 7) {
            if (effect.maintain >= 3000) {
                effect.maintain = 3000;
            }
        } else if (effect.getEffectTemplate().type == 8 || effect.getEffectTemplate().type == 9
                || effect.getEffectTemplate().type == 11 || effect.getEffectTemplate().type == 12
                || effect.getEffectTemplate().type == 38) {
            if (effect.maintain >= 5555) {
                effect.maintain = 5555;
            }
        }

        if (effect.maintain < 0)
            return;
        this.listEffect.add(effect);
        msgAddEffect(effect);
        Effect.setEff(this, effect, false);
    }

    public void msgAddEffect(Effect effect) {
        try {
            Writer writer = new Writer();
            writer.writeInt(this.id);
            effect.write(writer);
            zone.addEffect(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }

    }

    public void msgRemoveEffect(Effect effect) {
        try {
            Writer writer = new Writer();
            writer.writeInt(this.id);
            writer.writeShort(effect.id);
            zone.removeEffect(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }

    }

    public void itemBodyToBag(byte index) {
        if (index < 0 || index >= this.arrItemBody.length || this.arrItemBody[index] == null
                || !this.checkAddItem(this.arrItemBody[index])) {
            return;
        }
        Item item = this.arrItemBody[index];
        this.addItem(item, "Tháo từ BODY");
        this.removeItemBodyByIndex(index, "Tháo từ BODY");
        msgItemBodyToBag(index, item.index);
        setUpInfo(true);
    }

    public void msgItemBodyToBag(byte indexBody, int indexBag) {
        try {
            Writer writer = new Writer();
            writer.writeByte(indexBody);
            writer.writeShort(indexBag);
            this.client.session.serivce.itemBodyToBag_Me(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
        msgUpdateItemBody_Orther();

    }

    public void msgUpdateItemBody_Me() {
        try {
            Writer writer = new Writer();
            this.writeItemBody(writer, arrItemBody);
            this.writeItemBody(writer, arrItemBody2);
            this.client.session.serivce.updateItemBody_Me(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void msgUpdateItemBody_Orther() {
        try {
            Writer writer = new Writer();
            writer.writeInt(this.id);
            this.writeItemBody(writer, arrItemBody);
            zone.updateItemBody_Orther(client, writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public int getDame() {
        int dame = TuongKhac.TanCong;
        dame += getTanCongCoBan();
        if (getPhatHuyLucDanhCoBan() > 0) {
            long dameplus = (long) dame * getPhatHuyLucDanhCoBan() / 200; // open phải sửa /100
            dame += dameplus;
        }
        return dame;
    }

    public int getDameMob() {
        return TuongKhac.TanCongQuai;
    }

    public int getDameMob(Mob mob) {
        int dame = getDameMob();

        if (mob.he == 1) {
            dame += this.getTangTanCongLenLoi();
        }
        if (mob.he == 2) {
            dame += this.getTangTanCongLenTho();
        }
        if (mob.he == 3) {
            dame += this.getTangTanCongLenThuy();
        }
        if (mob.he == 4) {
            dame += this.getTangTanCongLenHoa();
        }
        if (mob.he == 5) {
            dame += this.getTangTanCongLenPhong();
        }

        return dame;
    }

    public void msgUpdateItemBody() {
        msgUpdateItemBody_Me();
        msgUpdateItemBody_Orther();
    }

    //

    public int getPhanDon() {
        int value = this.TuongKhac.PhanDon;

        return value;
    }

    public int getGiamTanCongKhiBiCM() {
        int value = this.TuongKhac.GiamTanCongKhiBiChiMang;

        return value;
    }

    public int getTangTuongKhac() {
        int value = this.TuongKhac.TangTuongKhac;

        return value;
    }

    public int getGiamTuongKhac() {
        int value = this.TuongKhac.GiamTuongKhac;

        return value;
    }

    public int getGiamSuyYeu() {
        int value = this.TuongKhac.GiamSuyYeu;

        return value;
    }

    public int getGiamTrungDoc() {
        int value = this.TuongKhac.GiamTrungDoc;

        return value;
    }

    public int getGiamLamCham() {
        int value = this.TuongKhac.GiamLamCham;

        return value;
    }

    public int getGiamGayBong() {
        int value = this.TuongKhac.GiamGayBong;

        return value;
    }

    public int getGiamChoang() {
        int value = this.TuongKhac.GiamGayChoang;

        return value;
    }

    public int getGiamTruChiMang() {
        int value = this.TuongKhac.GiamTruChiMang;

        return value;
    }

    public int getTanCongCoBan() {
        int value = this.TuongKhac.TanCongCoBan;

        return value;
    }

    public int getPhatHuyLucDanhCoBan() {
        int value = this.TuongKhac.PhatHuyLucDanhCoban;

        return value;
    }

    public int getHieuUngNgauNhien() {
        int value = this.TuongKhac.HieuUngNgauNhien;

        return value;
    }

    public int getChinhXac() {
        int value = this.TuongKhac.ChinhXac;
        if (value < 0)
            value = 0;
        return value;
    }

    public int getBoQuaNeTranh() {

        int value = this.TuongKhac.BoQuaNeTranh;
        if (value < 0)
            value = 0;
        return value;
    }

    public int getChiMang() {
        int value = this.TuongKhac.ChiMang;

        return value;
    }

    //
    public int getTangTanCongChiMang() {
        int value = this.TuongKhac.TangTanCongChiMang;

        return value;
    }

    public int getSatThuongChuyenHp() {
        int value = this.TuongKhac.satThuongChuyenHp;

        return value;
    }

    public int getTangTanCongLenLoi() {
        int value = this.TuongKhac.TangTanCongLenLoi;

        return value;
    }

    public int getTangTanCongLenTho() {
        int value = this.TuongKhac.TangTanCongLenTho;

        return value;
    }

    public int getTangTanCongLenThuy() {
        int value = this.TuongKhac.TangTanCongLenThuy;

        return value;
    }

    public int getTangTanCongLenHoa() {
        int value = this.TuongKhac.TangTanCongLenHoa;

        return value;
    }

    public int getTangTanCongLenPhong() {
        int value = this.TuongKhac.TangTanCongLenPhong;

        return value;
    }

    public int getGaySuyYeu() {
        int value = this.TuongKhac.GaySuyYeu;

        return value;
    }

    public int getGayTrungDoc() {
        int value = this.TuongKhac.GayTrungDoc;

        return value;
    }

    public int getGayLamCham() {
        int value = this.TuongKhac.GayLamCham;

        return value;
    }

    public int getGayBong() {
        int value = this.TuongKhac.GayBong;

        return value;
    }

    //
    public int getGayChoang() {
        int value = this.TuongKhac.GayChoang;

        return value;
    }

    public int getBoQuaKhangTinh() {
        int value = this.TuongKhac.BoQuaKhangTinh;

        return value;
    }

    public int getKhangLoi() {
        int value = this.TuongKhac.KhangLoi;

        return value;
    }

    public int getKhangTho() {
        int value = this.TuongKhac.KhangTho;

        return value;
    }

    public int getKhangTatCa() {
        int value = this.TuongKhac.KhangTatCa;
        if (value < 0)
            value = 0;
        return value;
    }

    public int getKhangThuy() {
        int value = this.TuongKhac.KhangThuy;

        return value;
    }

    public int getKhangHoa() {
        int value = this.TuongKhac.KhangHoa;

        return value;
    }

    public int getKhangPhong() {
        int value = this.TuongKhac.KhangPhong;

        return value;
    }

    public int getGiamSatThuong() {
        int value = this.TuongKhac.GiamSatThuong;
        if (value < 0)
            value = 0;
        return value;
    }

    public int getGaySuyYeu(Skill skill) {
        int gay = this.TuongKhac.GaySuyYeu;
        gay += getChiSoFormSkill(skill, 68);
        return gay;
    }

    public int getGayTrungDoc(Skill skill) {
        int gay = this.TuongKhac.GayTrungDoc;
        gay += getChiSoFormSkill(skill, 69);
        return gay;
    }

    public int getGayLamCham(Skill skill) {
        int gay = this.TuongKhac.GayLamCham;

        gay += getChiSoFormSkill(skill, 70);
        return gay;
    }

    public int getGayBong(Skill skill) {
        int gay = this.TuongKhac.GayBong;

        int effValue = getValueEff(53);
        effValue += getChiSoFormSkill(skill, 71);
        if (effValue > 0) {
            gay += effValue;
        }
        return gay;
    }

    public int getGayChoang(Skill skill) {
        int gay = this.TuongKhac.GayChoang;

        int effValue = getValueEff(77);
        gay += getChiSoFormSkill(skill, 72);
        if (effValue > 0) {
            gay += gay * effValue / 100;
        }
        return gay;
    }

    public int getNeTranh() {
        int ne = this.TuongKhac.NeTranh;
        if (ne < 0)
            ne = 0;
        ne += getValueEff(52);
        return ne;
    }

    public int getChiSoFormSkill(int... array) {

        int c = 0;
        for (int i = 0; i < this.arraySkill.length; i++) {
            if (arraySkill[i] != null && arraySkill[i].getSkillTemplate().type >= 5) {
                c += arraySkill[i].getChiSo(array);
            }
        }

        return c;
    }

    public int getChiSoFormSkill(Skill skill, int... array) {

        return skill.getChiSo(array);
    }

    public void setItemDuPhong(byte action) {
        if (action < 0 || action > 3)
            return;
        if (action == 0) {
            client.session.serivce.ShowMessGold("Áo, Giày, Ông tiêu đã được thay đổi.");
            int index1 = 2;
            int index2 = 7;
            int index3 = 8;
            for (int i = 1; i <= 8; i++) {
                if (i == index1 || i == index2 || i == index3) {
                    Item body2;
                    if ((body2 = this.arrItemBody2[i]) != null) {
                        Item body;
                        if ((body = this.arrItemBody[i]) != null) {
                            this.arrItemBody[i] = body2;
                            this.arrItemBody2[i] = body;
                        } else {
                            this.arrItemBody[i] = body2;
                            this.removeItemBody2ByIndex(i, "Set dự phòng");
                        }
                    }
                }
            }
        } else if (action == 1) {
            client.session.serivce.ShowMessGold("Quần, Móc sắt, Túi đã được thay đổi.");
            int index1 = 5;
            int index2 = 6;
            int index3 = 9;
            for (int i = 1; i <= 9; i++) {
                if (i == index1 || i == index2 || i == index3) {
                    Item body2;
                    if ((body2 = this.arrItemBody2[i]) != null) {
                        Item body;
                        if ((body = this.arrItemBody[i]) != null) {
                            this.arrItemBody[i] = body2;
                            this.arrItemBody2[i] = body;
                        } else {
                            this.arrItemBody[i] = body2;
                            this.removeItemBody2ByIndex(i, "Set dự phòng");
                        }
                    }
                }
            }
        } else if (action == 2) {
            client.session.serivce.ShowMessGold("Nón, Bao tay, Dây thừng đã được thay đổi.");
            int index1 = 0;
            int index2 = 3;
            int index3 = 4;
            for (int i = 0; i <= 4; i++) {
                if (i == index1 || i == index2 || i == index3) {
                    Item body2;
                    if ((body2 = this.arrItemBody2[i]) != null) {
                        Item body;
                        if ((body = this.arrItemBody[i]) != null) {
                            this.arrItemBody[i] = body2;
                            this.arrItemBody2[i] = body;
                        } else {
                            this.arrItemBody[i] = body2;
                            this.removeItemBody2ByIndex(i, "Set dự phòng");
                        }
                    }
                }
            }
        } else {
            client.session.serivce.ShowMessGold("Tất cả trang bị đã được thay đổi.");
            for (int i = 0; i < this.arrItemBody2.length; i++) {
                Item body2;
                if ((body2 = this.arrItemBody2[i]) != null) {
                    Item body;
                    if ((body = this.arrItemBody[i]) != null) {
                        this.arrItemBody[i] = body2;
                        this.arrItemBody2[i] = body;
                    } else {
                        this.arrItemBody[i] = body2;
                        this.removeItemBody2ByIndex(i, "Set dự phòng");
                    }
                }
            }
        }
        msgUpdateItemBody();
        setUpInfo(true);
    }

    public void useItemBodyDuPhong(short index) {
        if (index < 0 || index >= this.arrItemBag.length || arrItemBag[index] == null
                || !arrItemBag[index].isTypeTrangBi()) {
            return;
        }
        Item item = arrItemBag[index];
        ItemTemplate itemTemplate = item.getItemTemplate();
        if (itemTemplate.levelNeed > this.level()
                || (itemTemplate.gioiTinh != 2 && itemTemplate.gioiTinh != infoChar.gioiTinh)
                || (itemTemplate.idClass != 0 && itemTemplate.idClass != infoChar.idClass)) {
            client.session.serivce.ShowMessGold("Không thể sử dụng trang bị này.");
            return;
        }
        item.isLock = true;
        Item var3;
        if ((var3 = this.arrItemBody2[item.getItemTemplate().type]) != null) {
            var3.index = item.index;
            this.arrItemBag[index] = var3;
        } else {
            this.arrItemBag[index].setAmount(0);
            this.removeItemBagByIndex(index, "Sử dụng vào ITEM dự phòng");
        }

        item.index = item.getItemTemplate().type;
        this.arrItemBody2[item.index] = item;

        msgItemBodyDuPhong(index);
    }

    public void msgItemBodyDuPhong(int index) {
        try {
            Writer writer = new Writer();
            writer.writeShort(index);
            client.session.serivce.itemBodyDuPhong(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void itemBodyDuPhongToBag(byte index) {
        if (index < 0 || index >= this.arrItemBody2.length || arrItemBody2[index] == null
                || !this.checkAddItem(this.arrItemBody2[index])) {
            return;
        }
        Item item = arrItemBody2[index];
        removeItemBody2ByIndex(index, "Tháo từ BODY 2");
        addItem(item, "Tháo từ BODY 2");
        msgItemBodyDuPhongToBag(index, item.index);
    }

    public void msgItemBodyDuPhongToBag(int indexBody, int indexBag) {
        try {
            Writer writer = new Writer();
            writer.writeByte(indexBody);
            writer.writeShort(indexBag);
            client.session.serivce.itemBodyDuPhongToBag(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void boxToBag(short index) {
        if (this.arrItemBox[index] != null) {
            short indexbag = addItemBoxToBag(this.arrItemBox[index], "BOX TO BAG");
            if (indexbag != -1) {
                client.session.serivce.boxToBag(indexbag, index);
                String lydo = "REMOVE ITEM BOX " + arrItemBox[index].getItemTemplate().name + ", SL: "
                        + arrItemBox[index].amount + " lý do: Lấy từ box qua bag";
                Utlis.logRemoveChar(lydo, this.id);
                this.arrItemBox[index] = null;
            } else {
                client.session.serivce.ShowMessGold("Túi đồ đã đầy.");
            }
        }

    }

    public void bagToBox(short index) {
        if (this.arrItemBag[index] != null) {
            short indexbox = addItemBagToBox(this.arrItemBag[index], "BAG TO BOX");
            if (indexbox != -1) {
                client.session.serivce.bagToBox(index, indexbox);
                this.removeItemBagByIndex(index, "Bag to box");
            } else {
                client.session.serivce.ShowMessGold("Rương đã đầy.");
            }
        }
    }

    public void ghepDa(boolean bac, Item[] da) {

        if (da.length == 0) {

            return;
        }
        long num = 0;

        for (int i = 0; i < da.length; i++) {
            if (da[i] != null) {
                num += DataCenter.gI().pointGhepDa[da[i].id];
            }
        }
        int level = 0;
        if (num > 0L) {
            for (level = DataCenter.gI().pointGhepDa.length - 1; level >= 0
                    && num <= DataCenter.gI().pointGhepDa[level]; --level) {

            }
        }
        int levelUP = level + 1;
        if (levelUP >= 12)
            levelUP = 11;
        long phantram = num * 100L / DataCenter.gI().pointGhepDa[levelUP];
        if (phantram < 40 || bac && this.infoChar.bacKhoa < DataCenter.gI().bacKhoaGhepDa[levelUP]
                || !bac && this.infoChar.bac < DataCenter.gI().bacKhoaGhepDa[levelUP]) {
            return;
        }
        boolean isDaKhoa = false;
        boolean ghep = Utlis.randomBoolean(100, phantram);
        for (int i = 0; i < da.length; i++) {
            if (da[i] != null) {
                if (da[i].isLock) {
                    isDaKhoa = true;

                }
                removeItemBag(da[i], "Ghép đá");
            }
        }
        if (!isDaKhoa) {
            isDaKhoa = !bac;
        }
        if (bac) {
            mineBacKhoa(DataCenter.gI().bacKhoaGhepDa[levelUP], false, false, "Ghép đá");
        } else {
            mineBac(DataCenter.gI().bacKhoaGhepDa[levelUP], false, false, "Ghép đá");
        }
        Item daGhep = null;
        if (ghep) {
            daGhep = (new Item(levelUP, isDaKhoa));
        } else {
            daGhep = (new Item(level, isDaKhoa));
        }
        addItem(daGhep, "Ghép đá");
        msgGhepDa(da, daGhep);
    }

    public void msgGhepDa(Item[] da, Item daGhep) {
        try {
            Writer writer = new Writer();
            writer.writeInt(infoChar.bac);
            writer.writeInt(infoChar.bacKhoa);
            ArrayList list = Utlis.getArrayListNotNull(da, null);
            writer.writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                writer.writeShort(((Item) list.get(i)).index);
            }
            daGhep.write(writer);
            client.session.serivce.ghepDa(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public Item[] h(int var1) {
        switch (var1) {
            case 0:
                return this.arrItemBag;
            case 1:
                return this.arrItemBox;
            case 2:
                return this.arrItemBody;
            case 3:
                return this.arrItemBody2;
            case 4:
                return this.arrItemExtend;
            default:
                return null;
        }
    }

    public void cuongHoa(int type_item, int index_item, int index_bua, Item[] da) {
        Item[] array = h(type_item);
        if (array == null || index_item < 0 || index_item >= array.length || array[index_item] == null
                || da.length == 0) {
            return;
        }
        Item bua = index_bua == -1 ? null : this.arrItemBag[index_bua];
        if (bua != null && bua.id != 162) {
            bua = null;
        }
        Item itemCuongHoa = array[index_item];
        if (!itemCuongHoa.isItemTrangBi() || !itemCuongHoa.u()) {
            return;
        }
        int y = 0;
        long w = 0;
        long v = 0;
        long phamTram = 0;
        for (int i = 0; i < da.length; i++) {
            if (da[i] != null) {
                v += DataCenter.gI().pointGhepDa[da[i].id];
            }
        }

        if (itemCuongHoa.isVuKhi() && itemCuongHoa.level + 1 <= 19) {
            y = DataCenter.gI().bacKhoaUpgradeVuKhi[itemCuongHoa.level + 1];
            w = DataCenter.gI().pointUpgradeVuKhi[itemCuongHoa.level + 1];
        } else if (itemCuongHoa.isTrangBi() && itemCuongHoa.level + 1 <= 19) {
            y = DataCenter.gI().bacKhoaUpgradeTrangBi[itemCuongHoa.level + 1];
            w = DataCenter.gI().pointUpgradeTrangBi[itemCuongHoa.level + 1];
        } else if (itemCuongHoa.isPhuKien() && itemCuongHoa.level + 1 <= 19) {
            y = DataCenter.gI().bacKhoaUpgradePhuKien[itemCuongHoa.level + 1];
            w = DataCenter.gI().pointUpgradePhuKien[itemCuongHoa.level + 1];
        }

        if ((long) infoChar.bac + (long) infoChar.bacKhoa < (long) y) {
            return;
        }

        if (w > 0L) {
            phamTram = v * 100L / w;
        }
        if (bua != null) {
            phamTram += 3;
        }
        if (infoChar.bacKhoa >= y) {
            infoChar.bacKhoa -= y;
        } else {
            infoChar.bacKhoa = 0;
            infoChar.bac = (infoChar.bac - (y - infoChar.bacKhoa));
        }
        for (int i = 0; i < da.length; i++) {
            if (da[i] != null) {
                removeItemBag(da[i], "Cường hóa");
            }
        }
        boolean CuongHoa = Utlis.randomBoolean(100, phamTram);
        if (CuongHoa) {
            itemCuongHoa.a(itemCuongHoa.level + 1);
            infoChar.tongCuongHoa += itemCuongHoa.level;
            infoChar.cuongHoaTuan += itemCuongHoa.level;
        }
        itemCuongHoa.isLock = true;
        msgCuongHoa(CuongHoa, false, da, itemCuongHoa, bua, type_item);
        msgUpdateItemBody_Orther();
        if (type_item != 0)
            setUpInfo(true);
        if (client.mChar.infoChar.idTask == 9 && client.mChar.infoChar.idStep == 10)
            TaskHandler.gI().PlusTask(client.mChar);
    }

    public void setMapDefault() {
        infoChar.mapDefault = infoChar.mapId;
        client.session.serivce.ShowMessGold("Bạn đã đặt Map mặc định tại đây");
        if (client.mChar.infoChar.idTask == 10 && client.mChar.infoChar.idStep == 0)
            TaskHandler.gI().PlusTask(client.mChar);
    }

    public void dichChuyen(short type1, short index1, short type2, short index2, short index3) {

        Item item1 = getItemBagByIndex(index1);
        Item item2 = getItemBagByIndex(index2);
        Item item3 = getItemBagByIndex(index3);

        if (type1 == 2) {
            item1 = getItemBodyByIndex(index1);
        } else if (type1 == 3) {
            item1 = getItemBody2ByIndex(index1);
        }

        if (type2 == 2) {
            item2 = getItemBodyByIndex(index2);
        } else if (type2 == 3) {
            item2 = getItemBody2ByIndex(index2);
        }

        if (item1 == null || item2 == null || item3 == null) {
            client.session.serivce.ShowMessGold("Có lỗi sảy ra vui lòng thoát ra vào lại.");
            return;
        }
        if (item1.getItemTemplate().type != item2.getItemTemplate().type
                || item1.getItemTemplate().idClass != item2.getItemTemplate().idClass
                || item1.getItemTemplate().levelNeed > item2.getItemTemplate().levelNeed || item1.level < item2.level
                || !item1.isTypeTrangBi() || !item2.isTypeTrangBi()) {
            client.session.serivce.ShowMessGold("Trang bị không phù hợp.");
            return;
        }
        if (item3.id < 156 || item3.id > 158) {
            client.session.serivce.ShowMessGold("Cần bỏ Bùa dịch chuyển trang bị.");
            return;
        }
        if (item1.level >= 16 && item3.id != 158) {
            client.session.serivce.ShowMessGold("Cần bỏ Bùa dịch chuyển trang bị (Cao).");
            return;
        } else if (item1.level >= 10 && item1.level <= 14 && item3.id != 157) {
            client.session.serivce.ShowMessGold("Cần bỏ Bùa dịch chuyển trang bị (Trung).");
            return;
        }
        item2.a(item1.level);
        item1.a(0);
        try {
            Writer writer = new Writer();
            item1.write(writer);
            writer.writeByte(type1);
            item2.write(writer);
            writer.writeByte(type2);
            writer.writeShort(index3);
            client.session.serivce.dichChuyen(writer);
            removeItemBag(item3, true, "Dịch chuyển trang bị");
            if (type1 != 0 || type2 != 0)
                setUpInfo(true);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void shellItemBag(short index) {
        Item itembag = getItemBagByIndex(index);
        if (itembag != null) {
            if (itembag.isLock) {
                if (addBacKhoa(itembag.giaBan(), true, true, "Tiền bán đồ cho shop")) {
                    removeItemBag(itembag, true, "Bán cho shop");
                }
            } else {
                if (addBac(itembag.giaBan(), true, true, "Tiền bán đồ cho shop")) {
                    removeItemBag(itembag, true, "Bán cho shop");
                }
            }
        }
    }

    public void moRongKhamNgoc() {
        if (infoChar.levelKhamNgoc >= 3) {
            client.session.serivce.ShowMessGold("Đã mở rộng tối đa.");
        } else {
            int vang = 500;
            if (infoChar.levelKhamNgoc > 0)
                vang *= infoChar.levelKhamNgoc + 1;
            if (infoChar.vang < vang) {
                client.session.serivce.ShowMessGold("Không có đủ vàng");
            } else {
                mineVang(vang, true, true, "Mở rộng khảm");
                infoChar.levelKhamNgoc++;
                client.session.serivce.openTabKhamNgoc((byte) infoChar.levelKhamNgoc);
            }
        }
    }

    public void goKhamNgoc(byte type, short index, short typeda) {
        try {
            Item item = getItemByType(type, index);
            if (item == null || !item.isItemTrangBi()
                    || item.getDiemChiSo(client, 199, 200, 201, 202, 203, 204, 205, 206, 344, 345) <= 0) {
                client.session.serivce.ShowMessGold("Trang bị không hợp lệ");
                return;
            }
            if (typeda == -1) {
                ItemOption[] itemOptions = item.getItemOption();
                java.util.Map<Integer, Integer> listitem = new java.util.HashMap<>();
                int vang = 0;
                for (int i = 0; i < itemOptions.length; i++) {
                    if (itemOptions[i] != null) {
                        if (itemOptions[i].getItemOptionTemplate() != null
                                && itemOptions[i].getItemOptionTemplate().type == 8 && itemOptions[i].a.length > 2) {
                            int iditem = UTPKoolVN.getOptionToItem(itemOptions[i].a[0]);
                            int lv = itemOptions[i].a[3];
                            int amount = 0;
                            for (int j = 0; j <= lv; ++j) {
                                amount += DataCenter.gI().ngocKhamUpgrade[j];
                            }
                            vang += amount;
                            listitem.put(iditem, amount);
                        }
                    }
                }

                if (vang > 600) {
                    vang = 600;
                }
                if (infoChar.vang < vang) {
                    client.session.serivce.ShowMessGold("Không đủ vàng");
                    return;
                }
                if (getCountNullItemBag() <= listitem.size()) {
                    client.session.serivce.ShowMessGold("Hành trang không đủ chỗ chứa");
                    return;
                }
                mineVang(vang, true, true, "Tách khảm ngọc");
                for (java.util.Map.Entry<Integer, Integer> entry : listitem.entrySet()) {
                    item.removeItemOption(UTPKoolVN.getItemToOption(entry.getKey()), -1, 2);
                    Item itemAdd = new Item(entry.getKey(), true, entry.getValue());
                    addItem(itemAdd, "Tách ngọc khảm");
                    msgAddItemBag(itemAdd);
                }

            } else {
                int level = item.getChiSo(3, client, UTPKoolVN.getItemToOption(typeda));
                int amount = 0;
                for (int j = 0; j <= level; ++j) {
                    amount += DataCenter.gI().ngocKhamUpgrade[j];
                }
                if (amount > 0) {
                    if (getCountNullItemBag() <= 0) {
                        client.session.serivce.ShowMessGold("Hành trang không đủ chỗ chứa");
                        return;
                    }
                    int vang = amount;
                    if (vang > 600) {
                        vang = 600;
                    }
                    if (infoChar.vang < vang) {
                        client.session.serivce.ShowMessGold("Không đủ vàng");
                        return;
                    }
                    mineVang(vang, true, true, "Tách khảm ngọc");
                    item.removeItemOption(UTPKoolVN.getItemToOption(typeda), -1, 2);
                    Item itemAdd = new Item(typeda, true, amount);
                    addItem(itemAdd, "Tách ngọc khảm");
                    msgAddItemBag(itemAdd);
                }
            }
            Writer writer = new Writer();
            item.write(writer);
            writer.writeByte(type);
            this.client.session.serivce.doneTachNgoc(writer);
            if (type != 0)
                setUpInfo(true);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void khamNgoc(byte type, short index, Item[] da) {
        try {

            Item item = getItemByType(type, index);
            if (item == null || !item.isItemTrangBi() || item.getDiemChiSo(client, 199, 200, 201, 202, 203, 204, 205,
                    206, 344, 345) >= infoChar.levelKhamNgoc + 3) {
                client.session.serivce.ShowMessGold("Cần mở rộng thêm ô khảm");
                return;
            }

            if (da[0] == null) {
                return;
            }
            int amount = 0;
            int optionKham = da[0].getOptionKham();
            int idDa = da[0].id;
            int level = item.getChiSo(3, client, optionKham);

            if (level >= 16) {
                client.session.serivce.ShowMessGold("Trang bị đã đạt cấp tối đa");
                return;
            }
            for (int i = 0; i < da.length; i++) {
                if (da[i] != null) {
                    amount += da[i].getAmount();
                    if (da[i].id != idDa) {
                        return;
                    }
                }
            }
            if (amount < 1) {
                return;
            }

            if (amount < DataCenter.gI().ngocKhamUpgrade[level + 1]) {
                client.session.serivce.ShowMessGold("Không đủ ngọc");
                return;
            }
            if (item.getDiemChiSo(client, optionKham) <= 0) {
                item.addItemOption(new ItemOption(optionKham + ",0,-1,0"));
            }
            item.setKhamNgoc(amount, idDa);
            Writer writer = new Writer();
            writer.writeByte(da.length);
            for (int i = 0; i < da.length; i++) {
                if (da[i] != null) {
                    writer.writeShort(da[i].index);
                    removeItemBag(da[i], true, "Khảm ngọc");
                }
            }
            item.write(writer);
            writer.writeByte(type);
            this.client.session.serivce.doneKhamNgoc(writer);
            if (type != 0)
                setUpInfo(true);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void confirmTachCaiTrang(byte type, int index) {
        try {
            Item item = getItemByType(type, index);

            if (item != null && item.graftCaiTrang != null) {

                if (item.graftCaiTrang.size() > getCountNullItemBag()) {
                    client.session.serivce.ShowMessGold("Hành trang cần " + item.graftCaiTrang.size() + " ô chứa");
                    return;
                }

                if (type == 2) {
                    removeItemBodyByIndex(index, "Tách cải trang");
                    setUpInfo(true);
                } else if (type == 3) {
                    removeItemBody2ByIndex(index, "Tách cải trang");
                    setUpInfo(true);
                } else {
                    removeItemBag(item, true, "Tách cải trang");
                }

                Writer writer = new Writer();
                writer.writeByte(type);
                writer.writeShort(index);
                for (int k = 0; k < item.graftCaiTrang.size(); ++k) {
                    GraftCaiTrang g = item.graftCaiTrang.get(k);
                    Item newItem = new Item(g.idItem);
                    newItem.isLock = true;
                    newItem.he = g.he;
                    newItem.strOptions = g.StrOption;
                    addItem(newItem, "Tách cải trang");
                    msgAddItemBag(newItem);
                    if (k == 0) {
                        newItem.write(writer);
                    }
                }
                this.client.session.serivce.doneTachCaiTrang(writer);
                if (type != 0)
                    setUpInfo(true);
            }
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void tachCaiTrang(byte type, int index) {
        try {
            Item item = getItemByType(type, index);
            if (item != null && item.graftCaiTrang != null) {
                Writer writer = new Writer();
                writer.writeByte((byte) item.graftCaiTrang.size());
                for (int k = 0; k < item.graftCaiTrang.size(); ++k) {
                    GraftCaiTrang g = item.graftCaiTrang.get(k);
                    Item newItem = new Item(g.idItem);
                    newItem.isLock = true;
                    newItem.he = g.he;
                    newItem.strOptions = g.StrOption;
                    newItem.write(writer);
                }
                this.client.session.serivce.tachCaiTrang(writer);
            }
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void ghepCaiTrang(List<Item> listItem) {
        try {

            if (listItem.size() == 0) {
                client.session.serivce.ShowMessGold("Có lỗi sảy ra vui lòng sắp xếp lại hành trang và thử lại");
                return;
            }
            List<ItemOption> allOption = new ArrayList<>();

            List<ItemOption> optionNameCT = new ArrayList<>();

            Item itemMacDinh = listItem.get(0);

            if (itemMacDinh.graftCaiTrang.size() > 0)
                return;

            for (int i = 0; i < listItem.size(); ++i) {
                ItemOption[] itemOptions;
                Item item = listItem.get(i);
                if (item == null)
                    continue;
                if (item.graftCaiTrang.size() > 0)
                    return;
                GraftCaiTrang graftCaiTrang = new GraftCaiTrang();
                graftCaiTrang.idItem = item.id;
                graftCaiTrang.he = item.he;
                graftCaiTrang.StrOption = item.strOptions;
                itemMacDinh.graftCaiTrang.add(graftCaiTrang);
                if ((itemOptions = item.L()) != null) {
                    int j;
                    if (!item.equals(itemMacDinh)) {

                        if (item.TYPE_TEMP == 0) {
                            Item itemRemove = getItemBagByIndex(item.index);
                            if (itemRemove != item)
                                return;
                            removeItemBag(itemRemove, true, "Ghép cải trang");
                        } else if (item.TYPE_TEMP == 2) {
                            Item itemRemove = getItemBodyByIndex(item.index);
                            if (itemRemove != item)
                                return;
                            removeItemBodyByIndex(itemRemove.index, "Ghép cải trang");
                        } else if (item.TYPE_TEMP == 3) {
                            Item itemRemove = getItemBody2ByIndex(item.index);
                            if (itemRemove != item)
                                return;
                            removeItemBody2ByIndex(itemRemove.index, "Ghép cải trang");
                        }

                        for (j = 0; j < DataCenter.gI().ItemOptionTemplate.length; ++j) {
                            if (DataCenter.gI().ItemOptionTemplate[j].name.trim().toLowerCase()
                                    .equals(item.getItemTemplate().name.trim().toLowerCase())) {
                                optionNameCT.add(new ItemOption(DataCenter.gI().ItemOptionTemplate[j].id + ",0,0"));
                                break;
                            }
                        }
                    }

                    for (j = 0; j < itemOptions.length; ++j) {
                        boolean var6 = true;

                        for (int var7 = 0; var7 < allOption.size(); ++var7) {
                            if (allOption.get(var7).a[0] == itemOptions[j].a[0]) {
                                allOption.get(var7).c(allOption.get(var7).a[1] + itemOptions[j].a[1]);
                                var6 = false;
                                break;
                            }
                        }
                        if (var6) {
                            allOption.add(itemOptions[j]);
                        }
                    }
                }
            }

            allOption.addAll(optionNameCT);

            if (itemMacDinh.TYPE_TEMP == 0) {
                Item itemOK = getItemBagByIndex(itemMacDinh.index);
                if (itemOK != itemMacDinh)
                    return;
                itemOK.strOptions = Item.a(allOption);
                itemOK.level = (byte) ((byte) listItem.size() - 1);
                itemOK.isLock = true;
                itemOK.graftCaiTrang = itemMacDinh.graftCaiTrang;
            } else if (itemMacDinh.TYPE_TEMP == 2) {
                Item itemOK = getItemBodyByIndex(itemMacDinh.index);
                if (itemOK != itemMacDinh)
                    return;
                itemOK.strOptions = Item.a(allOption);
                itemOK.level = (byte) ((byte) listItem.size() - 1);
                itemOK.isLock = true;
                itemOK.graftCaiTrang = itemMacDinh.graftCaiTrang;
            } else if (itemMacDinh.TYPE_TEMP == 3) {
                Item itemOK = getItemBody2ByIndex(itemMacDinh.index);
                if (itemOK != itemMacDinh)
                    return;
                itemOK.strOptions = Item.a(allOption);
                itemOK.level = (byte) listItem.size();
                itemOK.isLock = true;
                itemOK.graftCaiTrang = itemMacDinh.graftCaiTrang;
            }
            itemMacDinh.strOptions = Item.a(allOption);
            itemMacDinh.level = (byte) ((byte) listItem.size() - 1);
            itemMacDinh.isLock = true;
            if (itemMacDinh.TYPE_TEMP != 0)
                setUpInfo(true);
            Writer writer = new Writer();

            writer.writeByte(listItem.size());

            for (int k = 0; k < listItem.size(); ++k) {
                Item item = listItem.get(k);
                writer.writeByte(item.TYPE_TEMP);
                if (k == 0) {
                    itemMacDinh.write(writer);
                } else {
                    writer.writeShort(item.index);
                }
            }
            this.client.session.serivce.ghepCaiTrang(writer);

            mineVang((listItem.size() * 6L) - 6, true, true, "Ghép cải trang");
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }

    }

    public void msgCuongHoa(boolean CuongHoa, boolean b, Item[] da, Item itemCuongHoa, Item bua, int type_item) {
        try {
            Writer writer = new Writer();
            writer.writeBoolean(CuongHoa);
            writer.writeBoolean(b);
            writer.writeInt(infoChar.bac);
            writer.writeInt(infoChar.bacKhoa);
            ArrayList list = Utlis.getArrayListNotNull(da, null);
            writer.writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                writer.writeShort(((Item) list.get(i)).index);
            }
            if (bua == null) {
                writer.writeShort(-1);
            } else {
                writer.writeShort(bua.index);
            }
            itemCuongHoa.write(writer);
            writer.writeByte(type_item);
            client.session.serivce.cuongHoa(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void msgUpdateSkillViThu() {
        try {
            Writer writer = new Writer();
            writeSkillViThu(writer);
            this.client.session.serivce.updateSkillViThu(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void msgUpdateStatusChar() {
        try {
            Writer writer = new Writer();
            writer.writeInt(id);
            writer.writeByte(infoChar.lvPk);
            writer.writeInt(infoChar.taiPhu);
            writer.writeShort(infoChar.speedMove);
            writer.writeByte(infoChar.statusGD);
            zone.updateStatusChar(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void BuyShop(int idbuy, int sl) {
        ItemShop itemShop = null;
        for (List<ItemShop> shop : DataShop.getInstance().shopTemplates.values()) {
            for (ItemShop items : shop) {
                if ((items.id_buy == idbuy) && (items.idclass == 0 || items.idclass == infoChar.idClass)
                        && (items.sex == -1 || items.sex == infoChar.gioiTinh)) {
                    itemShop = items;
                }
            }
        }

        if (itemShop == null || sl <= 0) {
            client.session.serivce.NhacNhoMessage("Đã xảy ra lỗi vui lòng liên hệ admin.");
        } else {
            ItemTemplate tpl = new Item(itemShop.id_item).getItemTemplate();
            if (itemShop.id_item == 345)
                itemShop.gia_ban_vang += 500;
            if (tpl.isXepChong) {
                int tinhthachBag = getAmountAllById(160);
                long giabac = (long) itemShop.gia_ban_bac * sl;
                long giabackhoa = (long) itemShop.gia_ban_bac_khoa * sl;
                long giavang = (long) itemShop.gia_ban_vang * sl;
                long giavangkhoa = (long) itemShop.gia_ban_vang_khoa * sl;
                long giatinhthach = (long) itemShop.gia_ban_tinh_thach * sl;
                long moneyNew = (long) itemShop.moneyNew * sl;
                if (giabac > 2100000000 || giabackhoa > 2100000000 || giavang > 2100000000 || giavangkhoa > 2100000000
                        || giatinhthach > 2100000000 || moneyNew > 2100000000) {
                    client.session.serivce.ShowMessRed("Số lượng mua vượt mức cho phép.");
                } else if (giabac > infoChar.bac || giabackhoa > infoChar.bacKhoa || giavang > infoChar.vang
                        || giavangkhoa > infoChar.vangKhoa) {
                    client.session.serivce.ShowMessRed("Không đủ tiền vui lòng kiểm tra lại.");
                } else if (tpl.type <= 9 && moneyNew > infoChar.diem_Hokage[tpl.type]) {
                    client.session.serivce.ShowMessRed("Không đủ điểm Hokage");
                } else if (getCountNullItemBag() == 0) {
                    client.session.serivce.ShowMessRed("Hành trang không đủ chỗ chứa.");
                } else {
                    if (giatinhthach > 0) {
                        if (tinhthachBag < giatinhthach) {
                            client.session.serivce.ShowMessRed("Không đủ tinh thạch.");
                            return;
                        }
                        removeAmountAllItemBagById(160, (int) giatinhthach, "Mua cửa hàng");
                    }
                    if (giabac > 0)
                        mineBac(giabac, true, true, "MUA SHOP");
                    if (giabackhoa > 0)
                        mineBacKhoa(giabackhoa, true, true, "MUA SHOP");
                    if (giavang > 0)
                        mineVang(giavang, true, true, "MUA SHOP");
                    if (giavangkhoa > 0)
                        mineVangKhoa(giavangkhoa, true, true, "MUA SHOP");
                    // if(tpl.type <= 9 && moneyNew > 0) infoChar.diem_Hokage[tpl.type] -= moneyNew;
                    Item itemAdd = new Item(itemShop.id_item, itemShop.isLock, sl);
                    if (itemShop.expiry > 0)
                        itemAdd.expiry = System.currentTimeMillis() + itemShop.expiry;
                    itemAdd.he = (byte) itemShop.idhe;
                    itemAdd.strOptions = itemShop.strOptions;
                    if (itemAdd.isItemTrangBi())
                        itemAdd.createItemOptions();
                    addItem(itemAdd, "Mua từ shop");
                    msgAddItemBag(itemAdd);
                    TaskHandler.gI().checkDoneBuyItem(client.mChar, itemAdd.id);
                }
            } else {
                for (int i = 0; i < sl; i++) {
                    int tinhthachBag = getAmountAllById(160);
                    long giabac = itemShop.gia_ban_bac;
                    long giabackhoa = itemShop.gia_ban_bac_khoa;
                    long giavang = itemShop.gia_ban_vang;
                    long giavangkhoa = itemShop.gia_ban_vang_khoa;
                    long giatinhthach = itemShop.gia_ban_tinh_thach;
                    long moneyNew = itemShop.moneyNew;
                    if (giabac > infoChar.bac || giabackhoa > infoChar.bacKhoa || giavang > infoChar.vang
                            || giavangkhoa > infoChar.vangKhoa) {
                        client.session.serivce.ShowMessRed("Không đủ tiền vui lòng kiểm tra lại.");
                        return;
                    }
                    if (getCountNullItemBag() == 0) {
                        client.session.serivce.ShowMessRed("Hành trang không đủ chỗ chứa.");
                        return;
                    }
                    if (tpl.type <= 9 && moneyNew > infoChar.diem_Hokage[tpl.type]) {
                        client.session.serivce.ShowMessRed("Không đủ điểm Hokage");
                        return;
                    }
                    if (giatinhthach > 0) {
                        if (tinhthachBag < giatinhthach) {
                            client.session.serivce.ShowMessRed("Không đủ tinh thạch.");
                            return;
                        }
                        removeAmountAllItemBagById(160, (int) giatinhthach, "Mua cửa hàng");
                    }
                    if (giabac > 0)
                        mineBac(giabac, true, true, "MUA SHOP");
                    if (giabackhoa > 0)
                        mineBacKhoa(giabackhoa, true, true, "MUA SHOP");
                    if (giavang > 0)
                        mineVang(giavang, true, true, "MUA SHOP");
                    if (giavangkhoa > 0)
                        mineVangKhoa(giavangkhoa, true, true, "MUA SHOP");
                    // if(tpl.type <= 9 && moneyNew > 0) infoChar.diem_Hokage[tpl.type] -= moneyNew;
                    Item itemAdd = new Item(itemShop.id_item, itemShop.isLock, 1);
                    if (itemShop.expiry > 0)
                        itemAdd.expiry = System.currentTimeMillis() + itemShop.expiry;
                    itemAdd.he = (byte) itemShop.idhe;
                    itemAdd.strOptions = itemShop.strOptions;
                    if (itemAdd.isItemTrangBi())
                        itemAdd.createItemOptions();
                    addItem(itemAdd, "Mua từ shop");
                    msgAddItemBag(itemAdd);
                    TaskHandler.gI().checkDoneBuyItem(client.mChar, itemAdd.id);
                }
            }

            //
        }
    }

    public void msgUpdateSachChienDau() {
        try {
            Writer writer = new Writer();
            writer.writeByte(infoChar.sachChienDau);
            this.client.session.serivce.updateSachChienDau(writer);
            setUpInfo(true);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void tachCuongHoa(int type_item, int index_item) {
        Item[] array = h(type_item);
        if (array == null || index_item < 0 || index_item >= array.length || array[index_item] == null) {
            return;
        }
        long var7 = 0L;
        int var3 = 0;
        int var4;
        if (array[index_item].isVuKhi()) {
            for (var4 = array[index_item].level; var4 > 0; --var4) {
                var3 += DataCenter.gI().bacKhoaUpgradeVuKhi[var4];
                var7 += DataCenter.gI().pointUpgradeVuKhi[var4];
            }
        } else if (array[index_item].isTrangBi()) {
            for (var4 = array[index_item].level; var4 > 0; --var4) {
                var3 += DataCenter.gI().bacKhoaUpgradeTrangBi[var4];
                var7 += DataCenter.gI().pointUpgradeTrangBi[var4];
            }
        } else if (array[index_item].isPhuKien()) {
            for (var4 = array[index_item].level; var4 > 0; --var4) {
                var3 += DataCenter.gI().bacKhoaUpgradePhuKien[var4];
                var7 += DataCenter.gI().pointUpgradePhuKien[var4];
            }
        } else {
            return;
        }

        var7 /= 3L;
        Vector var8 = new Vector();

        int var5;
        for (var5 = DataCenter.gI().pointGhepDa.length - 1; var5 >= 0 && var7 > 0L; --var5) {
            if (var7 >= DataCenter.gI().pointGhepDa[var5]) {
                var7 -= DataCenter.gI().pointGhepDa[var5];
                Item var6;
                var6 = new Item((short) var5);
                var6.expiry = -1L;
                var6.isLock = true;
                var8.add(var6);
                var5 = DataCenter.gI().pointGhepDa.length;
                if (var8.size() >= 16) {
                    break;
                }
            }
        }
        if (getCountNullItemBag() >= var8.size()) {
            long bacKhoa = var3 / 3;
            array[index_item].a(0);
            msgTachCuongHoa(array[index_item], type_item);
            addBacKhoa(bacKhoa, true, true, "Tách cường hóa");
            for (int i = 0; i < var8.size(); i++) {
                addItem(((Item) (var8.get(i))), "Tách cường hóa");
            }
            this.msgUpdateItemBody_Orther();
            if (type_item != 0)
                setUpInfo(true);
        }

    }

    private void msgTachCuongHoa(Item item, int type_item) {
        try {
            Writer writer = new Writer();
            item.write(writer);
            writer.writeByte(type_item);
            this.client.session.serivce.tachCuongHoa(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }

    }

    public synchronized boolean addBacKhoa(long bacKhoa, boolean thongbao, boolean sendserver, String lydo) {
        long l = ((long) this.infoChar.bacKhoa) + ((long) bacKhoa);
        if (l > Integer.MAX_VALUE) {
            return false;
        }
        this.infoChar.bacKhoa = (int) l;
        lydo = "ADD BẠC KHÓA +" + bacKhoa + ", tổng: " + this.infoChar.bacKhoa + " lý do: " + lydo;
        Utlis.logAddChar(lydo, this.id);
        if (!sendserver)
            return true;
        try {
            Writer writer = new Writer();
            writer.writeInt(this.infoChar.bacKhoa);
            writer.writeBoolean(thongbao);
            this.client.session.serivce.updateBacKhoa(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
        return true;
    }

    public synchronized boolean addBac(long bac, boolean thongbao, boolean sendserver, String lydo) {
        long l = ((long) this.infoChar.bac) + ((long) bac);
        if (l > Integer.MAX_VALUE) {
            return false;
        }
        this.infoChar.bac = (int) l;
        lydo = "ADD BẠC +" + bac + ", tổng: " + this.infoChar.bac + " lý do: " + lydo;
        Utlis.logAddChar(lydo, this.id);
        if (!sendserver)
            return true;
        try {
            Writer writer = new Writer();
            writer.writeInt(this.infoChar.bac);
            writer.writeBoolean(thongbao);
            this.client.session.serivce.updateBac(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
        return true;
    }

    public synchronized boolean addVang(long vang, boolean thongbao, boolean sendserver, String lydo) {
        long l = ((long) this.infoChar.vang) + ((long) vang);
        if (l > Integer.MAX_VALUE) {
            return false;
        }
        this.infoChar.vang = (int) l;
        lydo = "ADD VÀNG +" + vang + ", tổng: " + this.infoChar.vang + " lý do: " + lydo;
        Utlis.logAddChar(lydo, this.id);
        if (!sendserver)
            return true;
        try {
            Writer writer = new Writer();
            writer.writeInt(this.infoChar.vang);
            writer.writeBoolean(thongbao);
            this.client.session.serivce.updateVang(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
        return true;
    }

    public synchronized boolean addVangKhoa(long vangkhoa, boolean thongbao, boolean sendserver, String lydo) {
        long l = ((long) this.infoChar.vangKhoa) + ((long) vangkhoa);
        if (l > Integer.MAX_VALUE) {
            return false;
        }
        this.infoChar.vangKhoa = (int) l;
        lydo = "ADD VÀNG KHÓA +" + vangkhoa + ", tổng: " + this.infoChar.vangKhoa + " lý do: " + lydo;
        Utlis.logAddChar(lydo, this.id);
        if (!sendserver)
            return true;
        try {
            Writer writer = new Writer();
            writer.writeInt(this.infoChar.vangKhoa);
            writer.writeBoolean(thongbao);
            this.client.session.serivce.updateVangKhoa(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
        return true;
    }

    public synchronized void mineBacKhoa(long bacKhoa, boolean thongbao, boolean sendserver, String lydo) {

        this.infoChar.bacKhoa -= bacKhoa;
        lydo = "MINE BẠC KHÓA -" + bacKhoa + ", tổng: " + this.infoChar.bacKhoa + " lý do: " + lydo;
        Utlis.logRemoveChar(lydo, this.id);
        if (!sendserver)
            return;
        try {
            Writer writer = new Writer();
            writer.writeInt(this.infoChar.bacKhoa);
            writer.writeBoolean(thongbao);
            this.client.session.serivce.updateBacKhoa(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public synchronized void mineBac(long bac, boolean thongbao, boolean sendserver, String lydo) {

        this.infoChar.bac -= bac;
        lydo = "MINE BẠC -" + bac + ", tổng: " + this.infoChar.bac + " lý do: " + lydo;
        Utlis.logRemoveChar(lydo, this.id);
        if (!sendserver)
            return;
        try {
            Writer writer = new Writer();
            writer.writeInt(this.infoChar.bac);
            writer.writeBoolean(thongbao);
            this.client.session.serivce.updateBac(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public synchronized void mineVang(long vang, boolean thongbao, boolean sendserver, String lydo) {
        this.infoChar.vang -= vang;
        this.phucLoi.vangTieuTuan += vang;
        this.phucLoi.vangTieuHomNay += vang;
        lydo = "MINE VÀNG -" + vang + ", tổng: " + this.infoChar.vang + " lý do: " + lydo;
        Utlis.logRemoveChar(lydo, this.id);
        if (!sendserver)
            return;
        try {
            Writer writer = new Writer();
            writer.writeInt(this.infoChar.vang);
            writer.writeBoolean(thongbao);
            this.client.session.serivce.updateVang(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public synchronized void mineVangKhoa(long vangkhoa, boolean thongbao, boolean sendserver, String lydo) {
        this.infoChar.vangKhoa -= vangkhoa;
        lydo = "MINE VÀNG KHÓA -" + vangkhoa + ", tổng: " + this.infoChar.vangKhoa + " lý do: " + lydo;
        Utlis.logRemoveChar(lydo, this.id);
        if (!sendserver)
            return;
        try {
            Writer writer = new Writer();
            writer.writeInt(this.infoChar.vangKhoa);
            writer.writeBoolean(thongbao);
            this.client.session.serivce.updateVangKhoa(writer);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    private boolean inGame() {
        return zone != null;
    }

    public synchronized static void sortItem(Item[] var0) {
        try {
            Vector var1 = new Vector();

            for (int var2 = 0; var2 < var0.length; ++var2) {
                if (var0[var2] != null) {
                    var1.add(var0[var2]);
                }
                var0[var2] = null;
            }

            for (int var4 = 0; var4 < var1.size(); ++var4) {
                Item var7;
                if ((var7 = (Item) var1.get(var4)).getItemTemplate().isXepChong) {
                    for (int var5 = var1.size() - 1; var5 > var4; --var5) {
                        Item var3;
                        if ((var3 = (Item) var1.get(var5)).id == var7.id && var3.isLock == var7.isLock
                                && var3.expiry == var7.expiry) {
                            var7.setAmount(var7.getAmount() + var3.getAmount());
                            var1.remove(var5);
                        }
                    }
                }
            }

            for (short var8 = 0; var8 < var1.size(); var0[var8].index = var8++) {
                var0[var8] = (Item) var1.get(var8);
            }

        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }
}
