package com.langla.real.other;

import com.langla.real.player.Player;
import com.langla.real.player.Char;
import com.langla.real.item.Item;
import com.langla.data.ItemOption;
import java.time.LocalDate;

public class VNDExchangeService {

    public static boolean exchangeVNDToGold(Player player, Char character, int vndAmount) {
        try {
            // Kiểm tra số dư VND
            int vnd = Player.getVNDByPlayerId(player);
            if (vnd < vndAmount) {
                return false; // Không đủ tiền
            }

            // Trừ tiền VND
            if (!Player.mineVND(player, vndAmount)) {
                return false; // Lỗi khi trừ tiền
            }

            // Chuyển đổi sang vàng
            int vang = (int) convertVNDToGold(vndAmount);

            // Cộng vàng vào tài khoản
            if (!character.addVang(vang, true, true, "Quy đổi từ VND")) {
                return false; // Lỗi khi cộng vàng
            }

            // Cập nhật phúc lợi nạp tiền
            updatePhucLoi(character, vang);

            // Xử lý quà nạp đầu
            if (!character.infoChar.isNapDau) {
                giveFirstTimeReward(character);
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private static void updatePhucLoi(Char character, int vang) {
        character.phucLoi.vangNapTichLuy += vang;
        character.phucLoi.vangNapHomNay += vang;
        character.phucLoi.vangNapTuan += vang;
        character.phucLoi.vangNapDon += vang;
        character.phucLoi.vangNapMoc += vang;
        character.infoChar.tongVangNap += vang;

        int diemTichQuay = vang / 100;
        character.phucLoi.diemTichLuyVongQuay += diemTichQuay;

        LocalDate currentDate = LocalDate.now();
        if (character.phucLoi.lastNapLienTucUpdate == null ||
                !character.phucLoi.lastNapLienTucUpdate.isEqual(currentDate)) {
            character.phucLoi.soNgayNapLienTuc++;
            character.phucLoi.lastNapLienTucUpdate = currentDate;
        }
    }

    private static void giveFirstTimeReward(Char character) {
        character.infoChar.isNapDau = true;

        // Item 277 (20 cái)
        Item item = new Item(277, true, 20);
        Thu thu = new Thu();
        thu.id = character.baseIdThu++;
        thu.chuDe = "Quà nạp lần đầu";
        thu.nguoiGui = "Hệ thống";
        thu.noiDung = "Phần thưởng nạp lần đầu";
        thu.item = item;
        character.listThu.add(thu);

        // Item 784 với options
        item = new Item(784, true);
        item.addItemOption(new ItemOption(143, 50));
        item.addItemOption(new ItemOption(209, 25));
        thu = new Thu();
        thu.id = character.baseIdThu++;
        thu.chuDe = "Quà nạp lần đầu";
        thu.nguoiGui = "Hệ thống";
        thu.noiDung = "Phần thưởng nạp lần đầu";
        thu.item = item;
        character.listThu.add(thu);

        // Item 416
        item = new Item(416, true);
        thu = new Thu();
        thu.id = character.baseIdThu++;
        thu.chuDe = "Quà nạp lần đầu";
        thu.nguoiGui = "Hệ thống";
        thu.noiDung = "Phần thưởng nạp lần đầu";
        thu.item = item;
        character.listThu.add(thu);

        // Item 445
        item = new Item(445, true);
        thu = new Thu();
        thu.id = character.baseIdThu++;
        thu.chuDe = "Quà nạp lần đầu";
        thu.nguoiGui = "Hệ thống";
        thu.noiDung = "Phần thưởng nạp lần đầu";
        thu.item = item;
        character.listThu.add(thu);

        character.client.session.serivce.updateThu();
    }

    private static double convertVNDToGold(int vnd) {
        // Logic chuyển đổi VND sang vàng theo từng case cụ thể
        switch (vnd) {
            case 10000: // Case 0: 10,000đ = 1,000 vàng
                return 1000;
            case 20000: // Case 1: 20,000đ = 2,000 vàng
                return 2000;
            case 50000: // Case 2: 50,000đ = 5,000 vàng
                return 5000;
            case 100000: // Case 3: 100,000đ = 11,000 vàng
                return 11000;
            case 200000: // Case 4: 200,000đ = 23,000 vàng
                return 23000;
            case 500000: // Case 5: 500,000đ = 59,000 vàng
                return 59000;
            case 1000000: // Case 6: 1,000,000đ = 120,000 vàng
                return 120000;
            default:
                return 0; // Trường hợp không hợp lệ
        }
    }
}
