package com.langla.real.player;

import com.PKoolVNDB;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.langla.lib.Utlis;
import com.langla.server.main.PKoolVN;
import com.langla.server.lib.Message;
import com.langla.server.lib.Writer;
import com.langla.utlis.UTPKoolVN;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author PKoolVN
 **/
public class Player extends Actor {
    public String username = null;
    public String version = null;
    @JsonIgnore
    public Client client;

    public static synchronized Player login(Session conn, String user, String pass) {
        Player p;
        String query = "SELECT * FROM `player` WHERE `username` = ? AND `password` = ?";
        try (Connection con = PKoolVN.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query);) {
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            ResultSet red = pstmt.executeQuery();
            if (red != null && red.first()) {
                int iddb = red.getInt("id");
                String username = red.getString("username");
                byte role = red.getByte("role");
                int idchar = red.getInt("character");
                long timelog = red.getLong("timelog");
                if (role == -1) {
                    conn.serivce.ShowMessWhite("Tài khoản này chưa kích hoạt");
                    return null;
                } else if (role == 0) {
                    conn.serivce.ShowMessWhite("Tài khoản này đã bị khóa bởi admin.");
                    return null;
                } else if (timelog > UTPKoolVN.CurrentTimeSecond() && !PKoolVNDB.isDebug) {
                    conn.serivce.ShowMessWhite(
                            "Vui lòng đăng nhập sau " + (timelog - UTPKoolVN.CurrentTimeSecond()) + " giây nữa");
                    return null;
                }
                p = new Player();
                p.id = iddb;
                p.username = username;
                p.CharacterID = idchar;
                p.role = role;
                p.timelog = timelog;
                p.client = conn.client;
                if (conn.client == null)
                    return null;
                return p;
            } else {
                conn.serivce.ShowMessWhite("Thông tin tài khoản hoặc mật khẩu không chính xác");
            }
        } catch (SQLException ex) {
            Utlis.logError(Player.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
        return null;
    }

    public static void UpdateCharID(int idplayer, int id, String name) {
        String query = "UPDATE `player` SET `character` = ?, `characterName` = ? WHERE `id` = ? LIMIT 1";
        try (Connection con = PKoolVN.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query);) {

            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, idplayer);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Utlis.logError(Player.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public static void Update(Player player) {
        String query = "UPDATE `player` SET `timelog` = ? WHERE `id` = ? LIMIT 1";
        try (Connection con = PKoolVN.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setLong(1, UTPKoolVN.CurrentTimeSecond() + 2);
            pstmt.setInt(2, player.id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Utlis.logError(Player.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public static void UpdateInfo(Player player, String name, int level) {
        if (player == null)
            return;
        String query = "UPDATE `player` SET `characterName` = ?, `top-level` = ? WHERE `id` = ? LIMIT 1";
        try (Connection con = PKoolVN.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, level);
            pstmt.setInt(3, player.id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Utlis.logError(Player.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public static int getVNDByPlayerId(Player player) {
        String query = "SELECT `vnd` FROM `player` WHERE `id` = ? LIMIT 1";
        int vnd = 0;
        try (Connection con = PKoolVN.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, player.id);
            try (ResultSet rs = pstmt.executeQuery();) {
                if (rs.next()) {
                    vnd = rs.getInt("vnd"); // Lấy giá trị vnd từ kết quả truy vấn
                }
            }
        } catch (SQLException ex) {
            Utlis.logError(Player.class, ex, "Đã xảy ra lỗi:\n" + ex.getMessage());
        }
        return vnd;
    }

    public static boolean mineVND(Player player, int vnd) {
        String query = "UPDATE `player` SET `vnd` = `vnd` - ? WHERE `id` = ? LIMIT 1";
        boolean result = false;
        try (Connection con = PKoolVN.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, vnd);
            pstmt.setInt(2, player.id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                result = true;
            }
        } catch (SQLException ex) {
            Utlis.logError(Player.class, ex, "Đã xảy ra lỗi:\n" + ex.getMessage());
        }
        return result;
    }

    public static boolean verifyPassword(Player player, String inputPassword) {
        String query = "SELECT `password` FROM `player` WHERE `id` = ? LIMIT 1";
        try (Connection con = PKoolVN.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, player.id);
            try (ResultSet rs = pstmt.executeQuery();) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    return inputPassword.equals(storedPassword);
                }
            }
        } catch (SQLException ex) {
            Utlis.logError(Player.class, ex, "Đã xảy ra lỗi:\n" + ex.getMessage());
        }
        return false;
    }

    public synchronized static boolean updatePassword(Player player, String inputPassword) {

        String query = "UPDATE `player` SET `password` = ? WHERE `id` = ? LIMIT 1";
        try (Connection con = PKoolVN.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, inputPassword);
            pstmt.setInt(2, player.id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Utlis.logError(Player.class, ex, "Đã xảy ra lỗi:\n" + ex.getMessage());
        }
        return false;
    }

    public static void sendTabSelectChar(Client client) {
        try {
            Writer writer = new Writer();
            writer.writeByte(128);
            if (client.player == null || client.session == null) {
                return;
            }
            if (client.player.CharacterID > 0) {
                Char newchar = CharDB.GetById(client, client.player.CharacterID);
                if (newchar != null) {
                    client.mChar = newchar;
                    PlayerManager.getInstance().put(client.mChar);
                    client.mChar.UpdateLogin();
                    writer.writeByte(1);
                    writer.writeInt(client.player.CharacterID);
                    client.mChar.write(writer);
                } else {
                    PlayerManager.getInstance().kickSession(client.session);
                }
            } else {
                writer.writeByte(0); // số lượng nhân vật
            }
            client.session.sendMessage(new Message((byte) -122, writer));
        } catch (Exception ex) {
            Utlis.logError(Player.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }
}
