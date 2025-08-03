package com.langla.real.family;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.langla.data.DataCenter;
import com.langla.lib.Utlis;
import com.langla.real.player.CharDB;
import com.langla.server.main.PKoolVN;

import java.sql.*;

/**
 * @author PKoolVN
 **/
public class FamilyDB {
    public synchronized static boolean CheckName (String name) {
        String query = "SELECT `id` FROM `giatoc` WHERE `name` LIKE ?";
        try (Connection con = PKoolVN.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query);) {
            pstmt.setString(1, name);
            ResultSet red = pstmt.executeQuery();
            return red == null || red.first();
        }  catch (SQLException e) {
            Utlis.logError(CharDB.class, e , "Co loi tai:\n" + e.getMessage());
        }
        return false;
    }

    public synchronized static int Insert(FamilyTemplate family) {
        int generatedId = -1;
        // Chuẩn bị một câu lệnh SQL để chèn dữ liệu, sử dụng PreparedStatement để tránh SQL Injection
        String query = "INSERT INTO `giatoc` (`name`, `info`,`item`,`member`,`skill`,`log` ) VALUES (?, ?, ?, ?, ?, ?);";

        try (Connection con = PKoolVN.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            // Thiết lập giá trị cho các tham số
            ObjectMapper mapper = DataCenter.gI().mapper;
            pstmt.setString(1, family.name);
            pstmt.setString(2, mapper.writeValueAsString(family.info));
            pstmt.setString(3, mapper.writeValueAsString(family.litsItem));
            pstmt.setString(4, mapper.writeValueAsString(family.listMember));
            pstmt.setString(5, mapper.writeValueAsString(family.listSkill));
            pstmt.setString(6, mapper.writeValueAsString(family.dataLog));
            // Thực thi câu lệnh
            pstmt.executeUpdate();
            // Lấy id được tạo tự động
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                generatedId = rs.getInt(1); // Giả sử rằng id là cột đầu tiên
            }
        } catch (Exception e) {
            Utlis.logError(CharDB.class, e , "Da say ra loi:\n" + e.getMessage());
        }
        return generatedId; // Trả về id, -1 nếu có lỗi
    }

    public static void Update (FamilyTemplate family){
        String query = "UPDATE `giatoc` SET `name` = ?, `info` = ?, `item` = ?, `member` = ?, `skill` = ?, `log` = ? WHERE `ID` = ?;";

        try (Connection con = PKoolVN.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            ObjectMapper mapper = DataCenter.gI().mapper;
            pstmt.setString(1, family.name);
            pstmt.setString(2, mapper.writeValueAsString(family.info));
            pstmt.setString(3, mapper.writeValueAsString(family.litsItem));
            pstmt.setString(4, mapper.writeValueAsString(family.listMember));
            pstmt.setString(5, mapper.writeValueAsString(family.listSkill));
            pstmt.setString(6, mapper.writeValueAsString(family.dataLog));
            pstmt.setInt(7, family.id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            Utlis.logError(CharDB.class, e , "Da say ra loi:\n" + e.getMessage());
        }
    }
}
