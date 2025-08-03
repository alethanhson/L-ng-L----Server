package com.langla.real.other;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.langla.data.DataCache;
import com.langla.data.DataCenter;
import com.langla.data.ItemOption;
import com.langla.lib.Utlis;
import com.langla.real.item.Item;
import com.langla.real.player.Client;
import com.langla.server.main.PKoolVN;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author PKoolVN
 **/
public class Giftcode {
    public static void useCode(String code, Client client){
        List<GiftcodeTemplate> datacode = checkCodeFormData(code, client);
        if(datacode == null){
            client.session.serivce.ShowMessRed("Mã quà tặng không chính xác hoặc đã hết hạn.");
            return;
        }
        if(checkCharacterAlreadyUsedCode(code, client.mChar.id)){
            client.session.serivce.ShowMessRed("Bạn đã sử dụng mã quà tặng này rồi.");
            return;
        }
        if(usedCode(code, client.mChar.id)){
            client.session.serivce.ShowMessGold("Nhận mã Code: "+code+" thành công, vui lòng kiểm tra hộp thư.");
            for (GiftcodeTemplate gift : datacode) {
                Thu thu = new Thu();
                thu.id = client.mChar.baseIdThu++;
                thu.chuDe = "Mã quà tặng: "+code;
                thu.nguoiGui = "Hệ thống";
                thu.noiDung = "Phần thưởng mã quà tặng: "+code;
                thu.bac = gift.bac;
                thu.bacKhoa = gift.bacKhoa;
                thu.vang = gift.vang;
                thu.vangKhoa = gift.vangKhoa;
                thu.exp = gift.exp;
                if (gift.item != null) {
                    Item itemAdd = itemTest(gift.item);
                    thu.item = itemAdd;
                    if(itemAdd.expiry > 0)  thu.item.expiry = System.currentTimeMillis() + itemAdd.expiry;
                }
                client.mChar.listThu.add(thu);
            }
            client.session.serivce.updateThu();
        } else {
            client.session.serivce.ShowMessRed("Có lỗi sảy ra vui lòng thử lại sau.");
        }

    }

    public static Item itemTest(Item itemThu){ // open phải xóa
        if(itemThu.getItemTemplate().id == 789 || itemThu.getItemTemplate().id == 880){
            itemThu.strOptions = "";
            itemThu.addItemOption(new ItemOption(0, 2000,9000));
            itemThu.addItemOption(new ItemOption(1, 2000,9000));
            itemThu.addItemOption(new ItemOption(2, 3000,6666));
            itemThu.addItemOption(new ItemOption(3, 4000,8888));
            itemThu.createItemOptions();
        } else if(itemThu.getItemTemplate().id == 329){
            if(DataCenter.gI().dataCaiTrang.size() == 0){
                Item.taoDataCaiTrang();
            }
            int next = Utlis.nextInt(0,DataCenter.gI().dataCaiTrang.size()-1);
            Item caiTrangs = DataCenter.gI().dataCaiTrang.get(next);
            Item caiTrang = new Item(caiTrangs.id);
            List<Integer> optionRandom = DataCache.OptionCaiTrangVIP.get(Utlis.nextInt(DataCache.OptionCaiTrangVIP.size()));

            int id = optionRandom.get(0);
            int param = Utlis.nextInt(optionRandom.get(1),optionRandom.get(2));

            List<Integer> optionRandom2 = DataCache.OptionCaiTrangVIP.get(Utlis.nextInt(DataCache.OptionCaiTrangVIP.size()));

            int id2 = optionRandom.get(0);
            int param2 = Utlis.nextInt(optionRandom.get(1),optionRandom.get(2));


            caiTrang.addItemOption(new ItemOption(id, param, -1));
            caiTrang.addItemOption(new ItemOption(id2, param2, -1));
            caiTrang.addItemOption(new ItemOption(209, Utlis.nextInt(1000,2500), -1));
            return caiTrang;
        }
        return itemThu;
    }

    public static List<GiftcodeTemplate> checkCodeFormData(String code, Client client){
        //Load Shop
        String query = "SELECT * FROM `giftcode` WHERE  `code` = ? AND count > 0 AND time_expire >= CURRENT_TIMESTAMP";
        try (Connection con = PKoolVN.getConnection();
             PreparedStatement pstmt =  con.prepareStatement(query)) {
            pstmt.setString(1, code);
            ResultSet red = pstmt.executeQuery();
            if (red != null && red.first()) {
                int char_id = red.getInt("char_id");
                if(char_id > 0){
                    if(char_id != client.mChar.id){
                        client.session.serivce.ShowMessRed("Bạn không có quyền nhận code này.");
                        return null;
                    }
                }
                ObjectMapper mapper = DataCenter.gI().mapper;

                return mapper.readValue(red.getString("list_item"),  TypeFactory.defaultInstance().constructCollectionType(List.class, GiftcodeTemplate.class));
            } else {
                return null;
            }
        }catch (SQLException | IOException e) {
            Utlis.logError(Giftcode.class, e , "Da say ra loi:\n" + e.getMessage());
        }
        return null;
    }
    public static boolean checkCharacterAlreadyUsedCode(String code, int character) {
        String query = "SELECT `code` FROM `giftcode_used` WHERE `code` = ? AND `character_id` = ?";
        try (Connection con = PKoolVN.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, code);
            pstmt.setInt(2, character);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
                return false;
            }
        } catch (SQLException e) {
            Utlis.logError(Giftcode.class, e , "Da say ra loi:\n" + e.getMessage());
            return false;
        }
    }

//    public static boolean usedCode(String code, int character) {
//        String updateQuery = "UPDATE `giftcode` SET count = count - 1 WHERE `code` = ? AND count > 0";
//        String insertQuery = "INSERT INTO `giftcode_used` (`code`, `character_id`, `time_used`) VALUES (?, ?, CURRENT_TIMESTAMP)";
//
//        try (Connection con = PKoolVNDB.getConnection()) {
//            con.setAutoCommit(false);
//
//            // Cập nhật số lượng mã
//            try (PreparedStatement pstmt1 = con.prepareStatement(updateQuery)) {
//                pstmt1.setString(1, code);
//                int updateCount = pstmt1.executeUpdate();
//                if (updateCount == 0) {
//                    con.rollback();
//                    return false;
//                }
//            }
//
//            // Thêm vào bảng mã đã sử dụng
//            try (PreparedStatement pstmt2 = con.prepareStatement(insertQuery)) {
//                pstmt2.setString(1, code);
//                pstmt2.setInt(2, character);
//                pstmt2.executeUpdate();
//            }
//
//            con.commit();
//            // Đặt lại chế độ auto-commit thành true
//            con.setAutoCommit(true);
//            return true;
//        } catch (SQLException e) {
//            Utlis.logError(Giftcode.class, e, "Da say ra loi:\n" + e.getMessage());
//            return false;
//        }
//    }

    public static boolean usedCode(String code, int character) {
        String updateQuery = "UPDATE `giftcode` SET count = count - 1 WHERE `code` = ? AND count > 1";
        String deleteQuery = "DELETE FROM `giftcode` WHERE `code` = ? AND count <= 1";
        String insertQuery = "INSERT INTO `giftcode_used` (`code`, `character_id`, `time_used`) VALUES (?, ?, CURRENT_TIMESTAMP)";
        Connection con = null; // Khai báo biến con trước khối try

        try {
            con = PKoolVN.getConnection(); // Khởi tạo kết nối
            con.setAutoCommit(false);

            // Cập nhật số lượng mã hoặc xóa nếu count <= 1
            try (PreparedStatement pstmtUpdate = con.prepareStatement(updateQuery)) {
                pstmtUpdate.setString(1, code);
                int updateCount = pstmtUpdate.executeUpdate();
                if (updateCount == 0) {
                    try (PreparedStatement pstmtDelete = con.prepareStatement(deleteQuery)) {
                        pstmtDelete.setString(1, code);
                        int deleteCount = pstmtDelete.executeUpdate();
                        if (deleteCount == 0) {
                            con.rollback();
                            return false;
                        }
                    }
                }
            }

            // Thêm vào bảng mã đã sử dụng
            try (PreparedStatement pstmtInsert = con.prepareStatement(insertQuery)) {
                pstmtInsert.setString(1, code);
                pstmtInsert.setInt(2, character);
                pstmtInsert.executeUpdate();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            Utlis.logError(Giftcode.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    Utlis.logError(Giftcode.class, ex, "Lỗi khi rollback:\n" + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException e) {
                    Utlis.logError(Giftcode.class, e, "Lỗi khi đặt lại auto-commit:\n" + e.getMessage());
                } finally {
                    try {
                        con.close(); // Đóng kết nối trong khối finally
                    } catch (SQLException e) {
                        Utlis.logError(Giftcode.class, e, "Lỗi khi đóng kết nối:\n" + e.getMessage());
                    }
                }
            }
        }
    }




}
