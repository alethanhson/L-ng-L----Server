package com.langla.data.center;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.langla.lib.Utlis;
import com.langla.real.phucloi.PhucLoiInfo;
import com.langla.real.phucloi.PhucLoiTpl;
import com.langla.server.main.PKoolVN;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhucLoi {
    
    private static PhucLoi instance;
    private ObjectMapper mapper;
    public List<PhucLoiTpl> DataPhucLoi = new ArrayList<>();
    public PhucLoiInfo phucLoiInfo = new PhucLoiInfo();
    
    private PhucLoi() {
        this.mapper = createObjectMapper();
    }
    
    public static PhucLoi getInstance() {
        if (instance == null) {
            instance = new PhucLoi();
        }
        return instance;
    }
    
    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        return objectMapper;
    }
    
    public void loadPhucLoiData() {
        // Load phúc lợi
        String phucLoiQuery = "SELECT * FROM `phuc_loi`";
        try (Connection con = PKoolVN.getConnection();
                PreparedStatement pstmt = con.prepareStatement(phucLoiQuery)) {
            ResultSet red = pstmt.executeQuery();
            while (red.next()) {
                this.DataPhucLoi = mapper.readValue(red.getString("str"),
                        TypeFactory.defaultInstance().constructCollectionType(List.class, PhucLoiTpl.class));
            }
        } catch (SQLException | IOException e) {
            Utlis.logError(PhucLoi.class, e, "Da say ra loi:\n" + e.getMessage());
        }
    }
    
    public void loadPhucLoiInfoData() {
        // Load phúc lợi info
        String phucloiInfoQuery = "SELECT * FROM `phucloi_info`";
        try (Connection con = PKoolVN.getConnection();
                PreparedStatement pstmt = con.prepareStatement(phucloiInfoQuery)) {
            ResultSet red = pstmt.executeQuery();
            while (red.next()) {
                int id = red.getInt("id");
                long value = red.getLong("value");
                if (id == 0) {
                    phucLoiInfo.TongRank = (int) value;
                } else if (id == 1) {
                    phucLoiInfo.RankCaoNhat = (int) value;
                } else if (id == 2) {
                    phucLoiInfo.TongDauTu = (int) value;
                } else if (id == 3) {
                    phucLoiInfo.TongSoLanMuaTheThang = (int) value;
                } else if (id == 4) {
                    phucLoiInfo.ThoiGianX2Online = value;
                }
            }
        } catch (SQLException e) {
            Utlis.logError(PhucLoi.class, e, "Da say ra loi:\n" + e.getMessage());
        }
    }
    
    public void updatePhucLoi(int id, int value) {
        try (Connection con = PKoolVN.getConnection()) {
            String updateQuery = "UPDATE phucloi_info SET value = ? WHERE id = ?";
            try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
                pstmt.setInt(1, value);
                pstmt.setInt(2, id);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                Utlis.logError(PhucLoi.class, e, "Da say ra loi:\n" + e.getMessage());
            }
        } catch (SQLException e) {
            Utlis.logError(PhucLoi.class, e, "Da say ra loi:\n" + e.getMessage());
        }
    }
    
    public PhucLoiTpl getPhucLoi_Tpl(short idRequest) {
        for (int i = 0; i < DataPhucLoi.size(); i++) {
            PhucLoiTpl phucLoi = DataPhucLoi.get(i);
            if (phucLoi.idRequest == idRequest) {
                return phucLoi;
            }
        }
        return null;
    }
}