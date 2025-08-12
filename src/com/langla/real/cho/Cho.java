package com.langla.real.cho;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.langla.data.DataCenter;
import com.langla.lib.Utlis;
import com.langla.real.player.Char;
import com.langla.real.player.PlayerManager;
import com.langla.real.other.Thu;
import com.langla.server.main.PKoolVN;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PKoolVN
 **/
public class Cho {


    public static void AutoUpdate(){
        try {
            List<ChoTemplate> listItemCho = new ArrayList<>();
            List<ChoTemplate> DataCho = DataCenter.gI().DataCho;
            long currentTimeMillis = System.currentTimeMillis() / 1000L;
            for (ChoTemplate cho : DataCho) {
                if (cho.isBuy != 0 || cho.time < currentTimeMillis) {
                    listItemCho.add(cho);
                }
            }
            for (ChoTemplate cho : listItemCho) {
                Char get = PlayerManager.getInstance().getChar(cho.character_id);
                if(get != null){
                    if(cho.isBuy == 1){
                        if(delete(cho.id) && DataCenter.gI().deleteChoById(cho.id)){
                            double result = cho.bac * 0.99;
                            Thu thu = new Thu();
                            thu.id = get.baseIdThu++;
                            thu.chuDe = "Vật phẩm "+cho.item.getItemTemplate().name+" đã bán thành công";
                            thu.nguoiGui = "Hệ Thống";
                            thu.noiDung = "Chúc mừng bạn đã bán được vật phẩm "+cho.item.getItemTemplate().name+", và bạn bị trừ -1% bạc phí bán qua chợ.!";
                            thu.bac = (int) result;
                            get.listThu.add(thu);
                            get.client.session.serivce.updateThu();
                        }
                    } else {
                        if(delete(cho.id) && DataCenter.gI().deleteChoById(cho.id)) {
                            Thu thu = new Thu();
                            thu.id = get.baseIdThu++;
                            thu.chuDe = "Trả lại Vật phẩm rao bán " + cho.item.getItemTemplate().name;
                            thu.nguoiGui = "Hệ Thống";
                            thu.noiDung = "Rất tiếc vật phẩm của bạn đã quá hạn và không có ai mua chúc bạn may mắn lần sau.!";
                            thu.item = cho.item;
                            get.listThu.add(thu);
                            get.client.session.serivce.updateThu();
                        }
                    }
                }
                Thread.sleep(100);
            }
        } catch (Exception e) {
            Utlis.logError(Cho.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
        }

    }


    public static boolean delete(long id) {
        String query = "DELETE FROM `market` WHERE id = ?";
        try (Connection con = PKoolVN.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setLong(1, id);

            int deleteCount = pstmt.executeUpdate(); // Thực thi lệnh delete

            // Kiểm tra xem có hàng nào được xóa hay không
            if (deleteCount > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            Utlis.logError(Cho.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
            return false;
        }
    }


    public static long insert(ChoTemplate cho) {
        String query = "INSERT INTO `market` (character_id, character_name, isBuy, item, time, bac) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = PKoolVN.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ObjectMapper mapper = DataCenter.gI().mapper;

            pstmt.setInt(1, cho.character_id);
            pstmt.setString(2, cho.character_name);
            pstmt.setInt(3, cho.isBuy);
            pstmt.setString(4, mapper.writeValueAsString(cho.item));
            pstmt.setInt(5, cho.time);
            pstmt.setInt(6, cho.bac);

            int insertCount = pstmt.executeUpdate(); // Thực thi lệnh insert

            if (insertCount > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getLong(1); // Trả về ID được tạo
                    }
                }
            }
            return -1; // Trả về -1 nếu không có bản ghi nào được chèn
        } catch (SQLException | IOException e) {
            Utlis.logError(Cho.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
            return -1; // Trả về -1 nếu có lỗi
        }
    }



    public static boolean update(long id) {
        String query = "UPDATE `market` SET isBuy = ? WHERE id = ?";
        try (Connection con = PKoolVN.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, 1);
            pstmt.setLong(2, id);
            int updateCounts = pstmt.executeUpdate(); // Thực thi
            // Kiểm tra xem có hàng nào được xóa hay không
            if (updateCounts > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            Utlis.logError(Cho.class, e , "Đã xảy ra lỗi:\n" + e.getMessage());
            return false;
        }
    }

    public static int checkIsBuy(long id) {
        String query = "SELECT isBuy FROM `market` WHERE id = ?";
        try (Connection con = PKoolVN.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("isBuy");
            } else {
                return -1; // Không có bản ghi có id tương ứng
            }
        } catch (SQLException e) {
            Utlis.logError(Cho.class, e, "Đã xảy ra lỗi:\n" + e.getMessage());
            return -1; // Trả về false nếu có lỗi
        }
    }


}
