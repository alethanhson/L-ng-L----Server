package com.langla.data.center;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.langla.lib.Utlis;
import com.langla.real.item.ItemShop;
import com.langla.server.main.PKoolVN;
import com.langla.data.DataCache;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataShop {

    private static DataShop instance;
    private ObjectMapper mapper;
    public Map<Integer, List<ItemShop>> shopTemplates = new HashMap<>();
    public Map<Integer, String> shopNames = new HashMap<>();

    private DataShop() {
        this.mapper = createObjectMapper();
    }

    public static DataShop getInstance() {
        if (instance == null) {
            instance = new DataShop();
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

    public void loadShopData() {
        // Load Shop
        String query = "SELECT * FROM `shop`";
        try (Connection con = PKoolVN.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            ResultSet red = pstmt.executeQuery();
            while (red.next()) {
                int id_shop = red.getInt("id_shop");
                String shopName = red.getString("Tên_Shop");
                shopNames.put(id_shop, shopName);
                List<ItemShop> itemList = mapper.readValue(red.getString("list_item"),
                        TypeFactory.defaultInstance().constructCollectionType(List.class, ItemShop.class));

                for (ItemShop item : itemList) {
                    item.id_buy = DataCache.idbuyshop++;
                }
                shopTemplates.computeIfAbsent(id_shop, k -> new ArrayList<>()).addAll(itemList);
            }
        } catch (SQLException | IOException e) {
            Utlis.logError(DataShop.class, e, "Da say ra loi:\n" + e.getMessage());
        }
    }

    public void updateShopToData(int idshop, List<ItemShop> itemList) {
        // Kết nối đến cơ sở dữ liệu
        try (Connection con = PKoolVN.getConnection()) {
            if (itemList != null) {
                String itemListJson = mapper.writeValueAsString(itemList);
                String updateQuery = "UPDATE shop SET list_item = ? WHERE id_shop = ?";
                try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
                    pstmt.setString(1, itemListJson);
                    pstmt.setInt(2, idshop);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    Utlis.logError(DataShop.class, e, "Da say ra loi:\n" + e.getMessage());
                }
            }
        } catch (SQLException | IOException e) {
            Utlis.logError(DataShop.class, e, "Da say ra loi:\n" + e.getMessage());
        }
    }
}
