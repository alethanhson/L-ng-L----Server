package com.langla.real.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.langla.data.DataCenter;
import com.langla.data.InfoChar;
import com.langla.data.Skill;
import com.langla.data.SkillClan;
import com.langla.lib.Utlis;
import com.langla.real.item.Item;
import com.langla.real.other.*;
import com.langla.real.phucloi.PhucLoi;
import com.langla.server.lib.Writer;
import com.langla.server.main.PKoolVN;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.sql.*;

import java.util.ArrayList;

/**
 * @author PKoolVN
 **/
public class CharDB {

    public synchronized static Char GetById (Client client, int id){

        String query = "SELECT * FROM `character` WHERE `id` = ?";
        try (Connection con = PKoolVN.getConnection();
             PreparedStatement pstmt =  con.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet red = pstmt.executeQuery();
            if (red != null && red.first()) {
                Char newchar = new Char(client);
                ObjectMapper mapper = DataCenter.gI().mapper;

                newchar.id = red.getInt("id");

                newchar.infoChar = mapper.readValue(red.getString("InfoChar"), InfoChar.class);

                newchar.arraySkill = mapper.readValue(red.getString("arraySkill"), Skill[].class);

                newchar.skillFight = mapper.readValue(red.getString("skillFight"), Skill.class);

                newchar.arrayTiemNang = mapper.readValue(red.getString("arrayTiemNang"), int[].class);

                newchar.listEffect = mapper.readValue(red.getString("listEffect"), TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Effect.class));

                newchar.arrItemBag = mapper.readValue(red.getString("ItemBag"), Item[].class);

                newchar.arrItemBox = mapper.readValue(red.getString("ItemBox"), Item[].class);

                newchar.arrItemBody = mapper.readValue(red.getString("ItemBody"), Item[].class);

                newchar.arrItemBody2 = mapper.readValue(red.getString("ItemBody2"), Item[].class);

                newchar.arrItemExtend = mapper.readValue(red.getString("ItemExtend"), Item[].class);

                newchar.listSkillViThu = mapper.readValue(red.getString("ItemSkillViThu"), TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, SkillClan.class));

                newchar.listDanhHieu = mapper.readValue(red.getString("DanhHieu"), TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, DanhHieu.class));

                ArrayList<Thu>  listThu = mapper.readValue(red.getString("listThư"), TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Thu.class));

                for (Thu thu : listThu) {
                    thu.id = newchar.baseIdThu++; // Thiết lập id thư
                }

                newchar.listThu = listThu;

                newchar.infoChar.statusGD = 0;

                newchar.listFriend = mapper.readValue(red.getString("listFriend"), TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Friend.class));

                newchar.phucLoi = mapper.readValue(red.getString("phucLoi"), PhucLoi.class);

                newchar.listEnemy = mapper.readValue(red.getString("listEnemy"), TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Enemy.class));


                return newchar;
            } else {
                return null;
            }
        } catch (SQLException | IOException e) {
            Utlis.logError(CharDB.class, e , "Da say ra loi:\n" + e.getMessage());
        }
        return null;
    }
    public static Writer writerOffline (String name){

        String query = "SELECT * FROM `character` WHERE  `name` LIKE ?";
        try (Connection con = PKoolVN.getConnection();
             PreparedStatement pstmt =  con.prepareStatement(query)) {
            pstmt.setString(1, name);
            ResultSet red = pstmt.executeQuery();
            if (red != null && red.first()) {
                Char newchar = new Char();
                ObjectMapper mapper = DataCenter.gI().mapper;
                newchar.id = red.getInt("id");
                newchar.infoChar = mapper.readValue(red.getString("InfoChar"), InfoChar.class);
                newchar.arraySkill = mapper.readValue(red.getString("arraySkill"), Skill[].class);
                newchar.skillFight = mapper.readValue(red.getString("skillFight"), Skill.class);
                newchar.arrayTiemNang = mapper.readValue(red.getString("arrayTiemNang"), int[].class);
                newchar.arrItemBody = mapper.readValue(red.getString("ItemBody"), Item[].class);
                newchar.arrItemBody2 = mapper.readValue(red.getString("ItemBody2"), Item[].class);

                newchar.listSkillViThu = mapper.readValue(red.getString("ItemSkillViThu"),
                        TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, SkillClan.class));

                newchar.listDanhHieu = mapper.readValue(red.getString("DanhHieu"), TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, DanhHieu.class));
                Writer writer = new Writer();
                newchar.writeView(writer);
                return writer;
            } else {
                return null;
            }
        } catch (SQLException | IOException e) {
            Utlis.logError(CharDB.class, e , "Da say ra loi:\n" + e.getMessage());
        }
        return null;
    }
    public synchronized static void Update (Char character){
        try {
            String query = "UPDATE `character` SET `Name` = ?, `InfoChar` = ?, `arraySkill` = ?," +
                    " `arrayTiemNang` = ?, `skillFight` = ?, `listEffect` = ?, `ItemBody` = ?," +
                    " `ItemBody2` = ?, `ItemBag` = ?, `ItemBox` = ?, `ItemExtend` = ?, `ItemSkillViThu` = ?," +
                    " `DanhHieu` = ?, `listThư` = ?, `listFriend` = ?, `phucLoi` = ?,`listEnemy` = ? WHERE `ID` = ?;";

            try (Connection con = PKoolVN.getConnection();
                 PreparedStatement pstmt = con.prepareStatement(query)) {
                ObjectMapper mapper = DataCenter.gI().mapper;
                pstmt.setString(1, character.infoChar.name);
                pstmt.setString(2, mapper.writeValueAsString(character.infoChar));
                pstmt.setString(3, mapper.writeValueAsString(character.arraySkill));
                pstmt.setString(4, mapper.writeValueAsString(character.arrayTiemNang));
                pstmt.setString(5, mapper.writeValueAsString(character.skillFight));
                pstmt.setString(6, mapper.writeValueAsString(character.listEffect));
                pstmt.setString(7, mapper.writeValueAsString(character.arrItemBody));
                pstmt.setString(8, mapper.writeValueAsString(character.arrItemBody2));
                pstmt.setString(9, mapper.writeValueAsString(character.arrItemBag));
                pstmt.setString(10, mapper.writeValueAsString(character.arrItemBox));
                pstmt.setString(11, mapper.writeValueAsString(character.arrItemExtend));
                pstmt.setString(12, mapper.writeValueAsString(character.listSkillViThu));
                pstmt.setString(13, mapper.writeValueAsString(character.listDanhHieu));
                pstmt.setString(14, mapper.writeValueAsString(character.listThu));
                pstmt.setString(15, mapper.writeValueAsString(character.listFriend));
                pstmt.setString(16, mapper.writeValueAsString(character.phucLoi));
                pstmt.setString(17, mapper.writeValueAsString(character.listEnemy));
                pstmt.setInt(18, character.id);
                pstmt.executeUpdate();
            }  catch (SQLException e ) {
                Utlis.logError(CharDB.class, e , "Da say ra loi:\n" + e.getMessage());
            }
        } catch (Exception ex) {
            Utlis.logError(CharDB.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }

    }
//    private static Char deepCopy(Char original) {
//        Char copy = new Char();
//        copy.id = original.id;
//        copy.infoChar = original.infoChar;
//        copy.arraySkill = original.arraySkill;
//        copy.arrayTiemNang = original.arrayTiemNang;
//        copy.skillFight = original.skillFight;
//        copy.listEffect = original.listEffect;
//        copy.arrItemBody = original.arrItemBody;
//        copy.arrItemBody2 = original.arrItemBody2;
//        copy.arrItemBag = original.arrItemBag;
//        copy.arrItemBox = original.arrItemBox;
//        copy.arrItemExtend = original.arrItemExtend;
//        copy.listSkillViThu = original.listSkillViThu;
//        copy.listDanhHieu = original.listDanhHieu;
//        copy.listThu = original.listThu;
//        copy.listFriend = original.listFriend;
//        copy.phucLoi = original.phucLoi;
//        copy.listEnemy = original.listEnemy;
//        return copy;
//    }
    public synchronized static boolean CheckNameChar (String name) {
        String query = "SELECT `id` FROM `player` WHERE `characterName` LIKE ?";
        try (Connection con = PKoolVN.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, name);
            ResultSet red = pstmt.executeQuery();
            return red == null || red.first();
        } catch (SQLException e) {
            Utlis.logError(CharDB.class, e , "Co loi tai:\n" + e.getMessage());
        }
        return false;
    }

    public synchronized static int InsertCharacter(Char character) {
        int generatedId = -1;
        // Chuẩn bị một câu lệnh SQL để chèn dữ liệu, sử dụng PreparedStatement để tránh SQL Injection
        String query = "INSERT INTO `character` (`Name`, `InfoChar`, `arraySkill`, `arrayTiemNang`," +
                " `skillFight`,`listEffect`,`ItemBody`,`ItemBody2`,`ItemBag`,`ItemBox`,`ItemExtend`,`ItemSkillViThu`," +
                "`DanhHieu`,`listThư`,`listFriend`,`phucLoi`,`listEnemy`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection con = PKoolVN.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            // Thiết lập giá trị cho các tham số
            ObjectMapper mapper = DataCenter.gI().mapper;
            pstmt.setString(1, character.infoChar.name);
            pstmt.setString(2, mapper.writeValueAsString(character.infoChar));
            pstmt.setString(3, mapper.writeValueAsString(character.arraySkill));
            pstmt.setString(4, mapper.writeValueAsString(character.arrayTiemNang));
            pstmt.setString(5, mapper.writeValueAsString(character.skillFight));
            pstmt.setString(6, mapper.writeValueAsString(character.listEffect));
            pstmt.setString(7, mapper.writeValueAsString(character.arrItemBody));
            pstmt.setString(8, mapper.writeValueAsString(character.arrItemBody2));
            pstmt.setString(9, mapper.writeValueAsString(character.arrItemBag));
            pstmt.setString(10, mapper.writeValueAsString(character.arrItemBox));
            pstmt.setString(11, mapper.writeValueAsString(character.arrItemExtend));
            pstmt.setString(12, mapper.writeValueAsString(character.listSkillViThu));
            pstmt.setString(13, mapper.writeValueAsString(character.listDanhHieu));
            pstmt.setString(14, mapper.writeValueAsString(character.listThu));
            pstmt.setString(15, mapper.writeValueAsString(character.listFriend));
            pstmt.setString(16, mapper.writeValueAsString(character.phucLoi));
            pstmt.setString(17, mapper.writeValueAsString(character.listEnemy));
            // Thực thi câu lệnh
            pstmt.executeUpdate();
            // Lấy id được tạo tự động
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                generatedId = rs.getInt(1); // Giả sử rằng id là cột đầu tiên
            }
        } catch (SQLException | IOException e) {
            Utlis.logError(CharDB.class, e , "Da say ra loi:\n" + e.getMessage());
        }
        return generatedId; // Trả về id, -1 nếu có lỗi
    }


    public static void guiThuOffline(String nguoinhan, Thu thu) {
        ArrayList<Thu> thuList = getListThu(nguoinhan);
        if(thuList == null) return;
        thuList.add(thu);
        UpdateThu(thuList, nguoinhan);
    }
    public static ArrayList<Thu> getListThu(String tennick) {
        String query = "SELECT `listThư` FROM `character` WHERE `name` LIKE ?";
        try (Connection con = PKoolVN.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, tennick);

            ResultSet red = pstmt.executeQuery();
            if (red != null && red.first()) {
                ObjectMapper mapper = DataCenter.gI().mapper;
                return mapper.readValue(red.getString("listThư"),  TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Thu.class));
            } else {
                return null;
            }
        } catch (SQLException | IOException e) {
            Utlis.logError(CharDB.class, e, "Da say ra loi:\n" + e.getMessage());
        }
        return null;
    }
    public static void UpdateThu (ArrayList<Thu> listThu, String tennick){
        String query = "UPDATE `character` SET `listThư` = ? WHERE `name` = ?;";

        try (Connection con = PKoolVN.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            ObjectMapper mapper = DataCenter.gI().mapper;
            pstmt.setString(1, mapper.writeValueAsString(listThu));
            pstmt.setString(2, tennick);
            pstmt.executeUpdate();

        } catch (SQLException | IOException e) {
            Utlis.logError(CharDB.class, e , "Da say ra loi:\n" + e.getMessage());
        }
    }



    public static void guiThuOffline(int idChar, Thu thu) {
        ArrayList<Thu> thuList = getListThu(idChar);
        if(thuList == null) return;
        thuList.add(thu);
        UpdateThu(thuList, idChar);
    }
    public static ArrayList<Thu> getListThu(int idChar) {
        String query = "SELECT `listThư` FROM `character` WHERE `id` = ?";
        try (Connection con = PKoolVN.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, idChar);

            ResultSet red = pstmt.executeQuery();
            if (red != null && red.first()) {
                ObjectMapper mapper = DataCenter.gI().mapper;
                return mapper.readValue(red.getString("listThư"),  TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Thu.class));
            } else {
                return null;
            }
        } catch (SQLException | IOException e) {
            Utlis.logError(CharDB.class, e, "Da say ra loi:\n" + e.getMessage());
        }
        return null;
    }
    public static void UpdateThu (ArrayList<Thu> listThu, int id){
        String query = "UPDATE `character` SET `listThư` = ? WHERE `id` = ?;";

        try (Connection con = PKoolVN.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            ObjectMapper mapper = DataCenter.gI().mapper;
            pstmt.setString(1, mapper.writeValueAsString(listThu));
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

        } catch (SQLException | IOException e) {
            Utlis.logError(CharDB.class, e , "Da say ra loi:\n" + e.getMessage());
        }
    }
}
