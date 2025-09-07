package com.langla.real.bangxephang;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.langla.data.DataCenter;
import com.langla.lib.Utlis;
import com.langla.real.family.Family;
import com.langla.real.family.FamilyTemplate;
import com.langla.real.player.Char;
import com.langla.real.player.PlayerManager;
import com.langla.server.main.PKoolVN;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class BangXepHang {
    protected static BangXepHang Instance;

    public static BangXepHang gI() {
        if (Instance == null)
            Instance = new BangXepHang();
        return Instance;
    }

    public int MaxPlayer = 200;
    public int MaxValue = 50;
    public ArrayList<Bxh_Tpl> listCaoThu = new ArrayList<Bxh_Tpl>();

    public ArrayList<Bxh_Tpl> listNapNhieu = new ArrayList<Bxh_Tpl>();

    public ArrayList<Bxh_Tpl> listCuaCai = new ArrayList<Bxh_Tpl>();

    public ArrayList<Bxh_Tpl> listTaiPhu = new ArrayList<Bxh_Tpl>();

    public ArrayList<Bxh_Tpl> listCuongHoa = new ArrayList<Bxh_Tpl>();

    public ArrayList<Bxh_Tpl> listGiaToc = new ArrayList<Bxh_Tpl>();

    public ArrayList<BossKillerRanking> listBossKiller = new ArrayList<BossKillerRanking>();

    private final Comparator<Bxh_Tpl> levelComparator = new Comparator<Bxh_Tpl>() {
        @Override
        public int compare(Bxh_Tpl c1, Bxh_Tpl c2) {
            // So sánh theo level tăng dần
            return Integer.compare(c2.infoChar.level, c1.infoChar.level);
        }
    };
    private final Comparator<Char> levelComparatorChar = new Comparator<Char>() {
        @Override
        public int compare(Char c1, Char c2) {
            // So sánh theo level tăng dần
            return Integer.compare(c2.level(), c1.level());
        }
    };
    private final Comparator<Bxh_Tpl> napComparator = new Comparator<Bxh_Tpl>() {
        @Override
        public int compare(Bxh_Tpl c1, Bxh_Tpl c2) {
            // So sánh theo level tăng dần
            return Integer.compare(c2.infoChar.tongVangNap, c1.infoChar.tongVangNap);
        }
    };

    private final Comparator<Char> napComparatorChar = new Comparator<Char>() {
        @Override
        public int compare(Char c1, Char c2) {
            // So sánh theo level tăng dần
            return Integer.compare(c2.infoChar.tongVangNap, c1.infoChar.tongVangNap);
        }
    };

    private final Comparator<Bxh_Tpl> cuaCaiComparator = new Comparator<Bxh_Tpl>() {
        @Override
        public int compare(Bxh_Tpl c1, Bxh_Tpl c2) {
            return Integer.compare(c2.infoChar.cuaCai, c1.infoChar.cuaCai);
        }
    };

    private final Comparator<Char> cuaCaiComparatorChar = new Comparator<Char>() {
        @Override
        public int compare(Char c1, Char c2) {
            // So sánh theo level tăng dần
            return Integer.compare(c2.infoChar.cuaCai, c1.infoChar.cuaCai);
        }
    };

    private final Comparator<Bxh_Tpl> taiPhuComparator = new Comparator<Bxh_Tpl>() {
        @Override
        public int compare(Bxh_Tpl c1, Bxh_Tpl c2) {
            return Integer.compare(c2.infoChar.taiPhu, c1.infoChar.taiPhu);
        }
    };

    private final Comparator<Char> taiPhuComparatorChar = new Comparator<Char>() {
        @Override
        public int compare(Char c1, Char c2) {
            // So sánh theo level tăng dần
            return Integer.compare(c2.infoChar.taiPhu, c1.infoChar.taiPhu);
        }
    };

    private final Comparator<Bxh_Tpl> cuongHoaComparator = new Comparator<Bxh_Tpl>() {
        @Override
        public int compare(Bxh_Tpl c1, Bxh_Tpl c2) {
            return Integer.compare(c2.infoChar.tongCuongHoa, c1.infoChar.tongCuongHoa);
        }
    };

    private final Comparator<Char> cuongHoaComparatorChar = new Comparator<Char>() {
        @Override
        public int compare(Char c1, Char c2) {
            // So sánh theo level tăng dần
            return Integer.compare(c2.infoChar.tongCuongHoa, c1.infoChar.tongCuongHoa);
        }
    };

    private final Comparator<Bxh_Tpl> giaTocComparator = new Comparator<Bxh_Tpl>() {
        @Override
        public int compare(Bxh_Tpl c1, Bxh_Tpl c2) {
            return Integer.compare(c2.GiaTocInfo.exp, c1.GiaTocInfo.exp);
        }
    };

    private final Comparator<FamilyTemplate> giaTocComparatorGoc = new Comparator<FamilyTemplate>() {
        @Override
        public int compare(FamilyTemplate c1, FamilyTemplate c2) {
            // So sánh theo level tăng dần
            return Integer.compare(c2.info.exp, c1.info.exp);
        }
    };

    private final Comparator<BossKillerRanking> bossKillerComparator = new Comparator<BossKillerRanking>() {
        @Override
        public int compare(BossKillerRanking r1, BossKillerRanking r2) {
            // Ưu tiên số boss tiêu diệt, sau đó là level
            int bossCompare = Integer.compare(r2.bossKills, r1.bossKills);
            if (bossCompare != 0)
                return bossCompare;
            return Integer.compare(r2.level, r1.level);
        }
    };

    public BangXepHang() {
    }

    public void addCaoThu(Bxh_Tpl news) {
        try {
            boolean found = false;
            for (int i = 0; i < listCaoThu.size(); i++) {
                if (listCaoThu.get(i).idBxh == news.idBxh) { // Kiểm tra nếu character đã tồn tại trong listCaoThu
                    found = true;
                    listCaoThu.set(i, news);
                    break;
                }
            }

            if (!found) { // Nếu character chưa tồn tại trong listCaoThu, thêm mới hoặc ghi đè lên

                if (listCaoThu.size() < MaxPlayer) { // Nếu danh sách chưa đầy phần tử, thêm mới
                    listCaoThu.add(news);
                } else { // Nếu danh sách đã đầy, thực hiện ghi đè
                    int minIndex = 0;
                    int minLevel = listCaoThu.get(0).infoChar.level;

                    // Tìm level nhỏ nhất trong danh sách
                    for (int i = 1; i < listCaoThu.size(); i++) {
                        if (listCaoThu.get(i).infoChar.level < minLevel) {
                            minLevel = listCaoThu.get(i).infoChar.level;
                            minIndex = i;
                        }
                    }

                    if (news.infoChar.level > minLevel) {
                        listCaoThu.remove(minIndex);
                        listCaoThu.add(news);
                    }
                }
            }
            listCaoThu.sort(levelComparator); // Sắp xếp lại danh sách sau khi thay đổi
        } catch (Exception ex) {
            Utlis.logError(BangXepHang.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }

    }

    public void addNapNhieu(Bxh_Tpl news) {
        try {
            boolean founds = false;
            for (int i = 0; i < listNapNhieu.size(); i++) {
                if (listNapNhieu.get(i).idBxh == news.idBxh) {
                    founds = true;
                    listNapNhieu.set(i, news);
                    break;
                }
            }

            if (!founds) {
                if (listNapNhieu.size() < MaxPlayer) {
                    listNapNhieu.add(news);
                } else {
                    int minIndex = 0;
                    int minLevel = listNapNhieu.get(0).infoChar.tongVangNap;

                    for (int i = 1; i < listNapNhieu.size(); i++) {
                        if (listNapNhieu.get(i).infoChar.tongVangNap < minLevel) {
                            minLevel = listNapNhieu.get(i).infoChar.tongVangNap;
                            minIndex = i;
                        }
                    }
                    if (news.infoChar.tongVangNap > minLevel) {
                        listNapNhieu.remove(minIndex);
                        listNapNhieu.add(news);
                    }
                }
            }
            listNapNhieu.sort(napComparator); // Sắp xếp lại danh sách sau khi thay đổi
        } catch (Exception ex) {
            Utlis.logError(BangXepHang.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }

    }

    public void addCuaCai(Bxh_Tpl news) {
        try {
            boolean founds = false;
            for (int i = 0; i < listCuaCai.size(); i++) {
                if (listCuaCai.get(i).idBxh == news.idBxh) {
                    founds = true;
                    listCuaCai.set(i, news);
                    break;
                }
            }

            if (!founds) {
                if (listCuaCai.size() < MaxPlayer) {
                    listCuaCai.add(news);
                } else {
                    int minIndex = 0;
                    int minLevel = listCuaCai.get(0).infoChar.cuaCai;

                    for (int i = 1; i < listCuaCai.size(); i++) {
                        if (listCuaCai.get(i).infoChar.cuaCai < minLevel) {
                            minLevel = listCuaCai.get(i).infoChar.cuaCai;
                            minIndex = i;
                        }
                    }
                    if (news.infoChar.cuaCai > minLevel) {
                        listCuaCai.remove(minIndex);
                        listCuaCai.add(news);
                    }
                }
            }
            listCuaCai.sort(cuaCaiComparator); // Sắp xếp lại danh sách sau khi thay đổi
        } catch (Exception ex) {
            Utlis.logError(BangXepHang.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }

    }

    public void addtaiPhu(Bxh_Tpl news) {
        try {
            boolean founds = false;
            for (int i = 0; i < listTaiPhu.size(); i++) {
                if (listTaiPhu.get(i).idBxh == news.idBxh) {
                    founds = true;
                    listTaiPhu.set(i, news);
                    break;
                }
            }

            if (!founds) {
                if (listTaiPhu.size() < MaxPlayer) {
                    listTaiPhu.add(news);
                } else {
                    int minIndex = 0;
                    int minLevel = listTaiPhu.get(0).infoChar.taiPhu;

                    for (int i = 1; i < listTaiPhu.size(); i++) {
                        if (listTaiPhu.get(i).infoChar.taiPhu < minLevel) {
                            minLevel = listTaiPhu.get(i).infoChar.taiPhu;
                            minIndex = i;
                        }
                    }
                    if (news.infoChar.taiPhu > minLevel) {
                        listTaiPhu.remove(minIndex);
                        listTaiPhu.add(news);
                    }
                }
            }
            listTaiPhu.sort(taiPhuComparator); // Sắp xếp lại danh sách sau khi thay đổi
        } catch (Exception ex) {
            Utlis.logError(BangXepHang.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }

    }

    public void addCuongHoa(Bxh_Tpl news) {
        try {
            boolean found = false;
            for (int i = 0; i < listCuongHoa.size(); i++) {
                if (listCuongHoa.get(i).idBxh == news.idBxh) { // Kiểm tra nếu character đã tồn tại trong listCaoThu
                    found = true;
                    listCuongHoa.set(i, news);
                    break;
                }
            }

            if (!found) { // Nếu character chưa tồn tại trong listCaoThu, thêm mới hoặc ghi đè lên

                if (listCuongHoa.size() < MaxPlayer) { // Nếu danh sách chưa đầy phần tử, thêm mới
                    listCuongHoa.add(news);
                } else { // Nếu danh sách đã đầy, thực hiện ghi đè
                    int minIndex = 0;
                    int minLevel = listCuongHoa.get(0).infoChar.tongCuongHoa;

                    // Tìm level nhỏ nhất trong danh sách
                    for (int i = 1; i < listCuongHoa.size(); i++) {
                        if (listCuongHoa.get(i).infoChar.tongCuongHoa < minLevel) {
                            minLevel = listCuongHoa.get(i).infoChar.tongCuongHoa;
                            minIndex = i;
                        }
                    }

                    if (news.infoChar.tongCuongHoa > minLevel) {
                        listCuongHoa.remove(minIndex);
                        listCuongHoa.add(news);
                    }
                }
            }
            listCuongHoa.sort(cuongHoaComparator); // Sắp xếp lại danh sách sau khi thay đổi
        } catch (Exception ex) {
            Utlis.logError(BangXepHang.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }

    }

    public void addGiaToc(Bxh_Tpl news) {
        try {
            boolean found = false;
            for (int i = 0; i < listGiaToc.size(); i++) {
                if (listGiaToc.get(i).idBxh == news.idBxh) { // Kiểm tra nếu character đã tồn tại trong listCaoThu
                    found = true;
                    listGiaToc.set(i, news);
                    break;
                }
            }

            if (!found) { // Nếu character chưa tồn tại trong listCaoThu, thêm mới hoặc ghi đè lên

                if (listGiaToc.size() < MaxPlayer) { // Nếu danh sách chưa đầy phần tử, thêm mới
                    listGiaToc.add(news);
                } else { // Nếu danh sách đã đầy, thực hiện ghi đè
                    int minIndex = 0;
                    int minLevel = listGiaToc.get(0).GiaTocInfo.exp;

                    // Tìm level nhỏ nhất trong danh sách
                    for (int i = 1; i < listGiaToc.size(); i++) {
                        if (listGiaToc.get(i).GiaTocInfo.exp < minLevel) {
                            minLevel = listGiaToc.get(i).GiaTocInfo.exp;
                            minIndex = i;
                        }
                    }

                    // Nếu level của character mới lớn hơn level nhỏ nhất trong danh sách, thực hiện
                    // ghi đè
                    if (news.GiaTocInfo.exp > minLevel) {
                        listGiaToc.remove(minIndex);
                        listGiaToc.add(news);
                    }
                }
            }
            listGiaToc.sort(giaTocComparator); // Sắp xếp lại danh sách sau khi thay đổi
        } catch (Exception ex) {
            Utlis.logError(BangXepHang.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }

    }

    public ArrayList<Bxh_Tpl> getListGiaToc() {

        return new ArrayList<>(listGiaToc.subList(0, Math.min(MaxValue, listGiaToc.size())));
    }

    public ArrayList<Bxh_Tpl> getListCuongHoa(int idClass) {
        ArrayList<Bxh_Tpl> resultList = new ArrayList<>();

        if (idClass > 0) {
            ArrayList<Bxh_Tpl> filteredList = new ArrayList<>();

            for (Bxh_Tpl bxhTpl : listCuongHoa) {
                if (bxhTpl.infoChar.idClass == idClass) {
                    filteredList.add(bxhTpl);
                }
            }
            filteredList.sort(cuongHoaComparator);

            resultList.addAll(filteredList.subList(0, Math.min(MaxValue, filteredList.size())));
        } else {
            resultList.addAll(listCuongHoa.subList(0, Math.min(MaxValue, listCuongHoa.size())));
        }
        return resultList;
    }

    public ArrayList<Bxh_Tpl> getListCuaCai(int idClass) {
        ArrayList<Bxh_Tpl> resultList = new ArrayList<>();

        if (idClass > 0) {
            ArrayList<Bxh_Tpl> filteredList = new ArrayList<>();

            for (Bxh_Tpl bxhTpl : listCuaCai) {
                if (bxhTpl.infoChar.idClass == idClass) {
                    filteredList.add(bxhTpl);
                }
            }
            filteredList.sort(cuaCaiComparator);

            resultList.addAll(filteredList.subList(0, Math.min(MaxValue, filteredList.size())));
        } else {
            resultList.addAll(listCuaCai.subList(0, Math.min(MaxValue, listCuaCai.size())));
        }
        return resultList;
    }

    public ArrayList<Bxh_Tpl> getListTaiPhu(int idClass) {
        ArrayList<Bxh_Tpl> resultList = new ArrayList<>();

        if (idClass > 0) {
            ArrayList<Bxh_Tpl> filteredList = new ArrayList<>();

            for (Bxh_Tpl bxhTpl : listTaiPhu) {
                if (bxhTpl.infoChar.idClass == idClass) {
                    filteredList.add(bxhTpl);
                }
            }
            filteredList.sort(taiPhuComparator);

            resultList.addAll(filteredList.subList(0, Math.min(MaxValue, filteredList.size())));
        } else {
            resultList.addAll(listTaiPhu.subList(0, Math.min(MaxValue, listTaiPhu.size())));
        }
        return resultList;
    }

    public ArrayList<Bxh_Tpl> getListCaoThu(int idClass) {
        ArrayList<Bxh_Tpl> resultList = new ArrayList<>();

        if (idClass > 0) {
            ArrayList<Bxh_Tpl> filteredList = new ArrayList<>();

            for (Bxh_Tpl bxhTpl : listCaoThu) {
                if (bxhTpl.infoChar.idClass == idClass) {
                    filteredList.add(bxhTpl);
                }
            }
            filteredList.sort(levelComparator);

            resultList.addAll(filteredList.subList(0, Math.min(MaxValue, filteredList.size())));
        } else {
            resultList.addAll(listCaoThu.subList(0, Math.min(MaxValue, listCaoThu.size())));
        }
        return resultList;
    }

    public ArrayList<Bxh_Tpl> getListNapNhieu(int idClass) {
        ArrayList<Bxh_Tpl> resultList = new ArrayList<>();

        if (idClass > 0) {
            ArrayList<Bxh_Tpl> filteredList = new ArrayList<>();

            for (Bxh_Tpl bxhTpl : listNapNhieu) {
                if (bxhTpl.infoChar.idClass == idClass) {
                    filteredList.add(bxhTpl);
                }
            }
            filteredList.sort(napComparator);

            resultList.addAll(filteredList.subList(0, Math.min(MaxValue, filteredList.size())));
        } else {
            resultList.addAll(listNapNhieu.subList(0, Math.min(MaxValue, listNapNhieu.size())));
        }
        return resultList;
    }

    public void update() {
        try {
            Map<Integer, Char> listAllChar = PlayerManager.getInstance().getlistChar();

            List<Char> allChars = new ArrayList<>(listAllChar.values());

            // top level
            List<Char> topLevel = allChars;

            topLevel.sort(levelComparatorChar);

            topLevel = topLevel.subList(0, Math.min(MaxPlayer, topLevel.size()));

            for (Char character : topLevel) {
                Bxh_Tpl newADD = new Bxh_Tpl();
                newADD.idBxh = character.id;
                newADD.infoChar = character.infoChar;
                addCaoThu(newADD);
            }
            ObjectMapper mapper = DataCenter.gI().mapper;
            String itemListJson = mapper.writeValueAsString(listCaoThu);
            updateSQL(itemListJson, 1);

            // top nạp nhiều
            List<Char> topNap = allChars;

            topNap.sort(napComparatorChar);

            topNap = topNap.subList(0, Math.min(MaxPlayer, topNap.size()));

            for (Char character : topNap) {
                Bxh_Tpl newADD = new Bxh_Tpl();
                newADD.idBxh = character.id;
                newADD.infoChar = character.infoChar;
                addNapNhieu(newADD);
            }

            itemListJson = mapper.writeValueAsString(listNapNhieu);
            updateSQL(itemListJson, 8);

            // của cải
            List<Char> cuaCai = allChars;

            cuaCai.sort(cuaCaiComparatorChar);

            cuaCai = cuaCai.subList(0, Math.min(MaxPlayer, cuaCai.size()));

            for (Char character : cuaCai) {
                Bxh_Tpl newADD = new Bxh_Tpl();
                newADD.idBxh = character.id;
                newADD.infoChar = character.infoChar;
                addCuaCai(newADD);
            }

            itemListJson = mapper.writeValueAsString(listCuaCai);
            updateSQL(itemListJson, 2);

            // tài phú
            List<Char> taiPhu = allChars;

            taiPhu.sort(taiPhuComparatorChar);

            taiPhu = taiPhu.subList(0, Math.min(MaxPlayer, taiPhu.size()));

            for (Char character : taiPhu) {
                Bxh_Tpl newADD = new Bxh_Tpl();
                newADD.idBxh = character.id;
                newADD.infoChar = character.infoChar;
                addtaiPhu(newADD);
            }

            itemListJson = mapper.writeValueAsString(listTaiPhu);
            updateSQL(itemListJson, 3);

            // cường hóa
            List<Char> cuongHoa = allChars;

            cuongHoa.sort(cuongHoaComparatorChar);

            cuongHoa = cuongHoa.subList(0, Math.min(MaxPlayer, cuongHoa.size()));

            for (Char character : cuongHoa) {
                Bxh_Tpl newADD = new Bxh_Tpl();
                newADD.idBxh = character.id;
                newADD.infoChar = character.infoChar;
                addCuongHoa(newADD);
            }

            itemListJson = mapper.writeValueAsString(listCuongHoa);
            updateSQL(itemListJson, 7);

            // gia tộc
            List<FamilyTemplate> listGiaToc = Family.gI().listFamily;

            listGiaToc.sort(giaTocComparatorGoc);

            listGiaToc = listGiaToc.subList(0, Math.min(MaxPlayer, listGiaToc.size()));

            for (FamilyTemplate f : listGiaToc) {
                Bxh_Tpl newADD = new Bxh_Tpl();
                newADD.idBxh = f.id;
                newADD.GiaTocInfo = f.info;
                newADD.NameGiaToc = f.name;
                newADD.CountMem = f.listMember.size();
                addGiaToc(newADD);
            }

            itemListJson = mapper.writeValueAsString(listGiaToc);
            updateSQL(itemListJson, 9);

        } catch (Exception ex) {
            Utlis.logError(BangXepHang.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void updateSQL(String itemListJson, int id) {
        // Kết nối đến cơ sở dữ liệu

        try (Connection con = PKoolVN.getConnection()) {

            String updateQuery = "UPDATE bangxephang SET list = ? WHERE id = ?";
            try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
                pstmt.setString(1, itemListJson);
                pstmt.setInt(2, id);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                Utlis.logError(DataCenter.class, e, "Da say ra loi:\n" + e.getMessage());
            }
        } catch (SQLException e) {
            Utlis.logError(DataCenter.class, e, "Da say ra loi:\n" + e.getMessage());
        }
    }

    public void addBossKillPoint(Char player) {
        try {
            // Tìm hoặc tạo ranking cho player
            BossKillerRanking ranking = getBossKillerRanking(player.id);
            if (ranking == null) {
                ranking = new BossKillerRanking(player);
                listBossKiller.add(ranking);
            }

            // Cập nhật thống kê
            ranking.bossKills++;
            ranking.lastBossKillTime = System.currentTimeMillis();
            ranking.lastBossName = "Nhẫn Giả Huyền Thoại";

            // Cập nhật xếp hạng
            updateBossKillerRanking();

            // Lưu vào database
            ObjectMapper mapper = DataCenter.gI().mapper;
            String itemListJson = mapper.writeValueAsString(listBossKiller);
            updateSQL(itemListJson, 10);

        } catch (Exception ex) {
            Utlis.logError(BangXepHang.class, ex, "Lỗi ghi nhận tiêu diệt boss: " + ex.getMessage());
        }
    }

    /**
     * Lấy ranking Boss Killer của player
     */
    public BossKillerRanking getBossKillerRanking(int playerId) {
        for (BossKillerRanking ranking : listBossKiller) {
            if (ranking.playerId == playerId) {
                return ranking;
            }
        }
        return null;
    }

    /**
     * Cập nhật xếp hạng Boss Killer
     */
    public void updateBossKillerRanking() {
        try {
            // Sắp xếp theo số boss tiêu diệt
            listBossKiller.sort(bossKillerComparator);

            // Cập nhật thứ hạng
            for (int i = 0; i < listBossKiller.size(); i++) {
                listBossKiller.get(i).rank = i + 1;
            }

        } catch (Exception ex) {
            Utlis.logError(BangXepHang.class, ex, "Lỗi cập nhật xếp hạng Boss Killer: " + ex.getMessage());
        }
    }
}
