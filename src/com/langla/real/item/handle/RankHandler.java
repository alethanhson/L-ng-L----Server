package com.langla.real.item.handle;

import com.langla.data.DataCenter;
import com.langla.real.item.Item;
import com.langla.data.ItemOption;
import com.langla.real.player.Char;

/**
 * Class xử lý logic rank cho người chơi
 * 
 * @author PKoolVN
 */
public class RankHandler {

    /**
     * Xử lý sử dụng item rank
     * 
     * @param character Người chơi
     * @param itemId    ID của item rank
     * @return true nếu thành công, false nếu thất bại
     */
    public static boolean useRankItem(Char character, int itemId) {
        try {
            switch (itemId) {
                case 417: // Rank 1
                    return handleRank1(character);
                case 418: // Rank 2
                    return handleRank2(character);
                case 419: // Rank 3
                    return handleRank3(character);
                case 420: // Rank 4
                    return handleRank4(character);
                case 421: // Rank 5
                    return handleRank5(character);
                case 422: // Rank 6
                    return handleRank6(character);
                case 423: // Rank 7
                    return handleRank7(character);
                case 424: // Rank 8
                    return handleRank8(character);
                case 425: // Rank 9
                    return handleRank9(character);
                case 426: // Rank 10
                    return handleRank10(character);
                default:
                    character.client.session.serivce.ShowMessRed("Item rank không hợp lệ!");
                    return false;
            }
        } catch (Exception ex) {
            character.client.session.serivce.ShowMessRed("Có lỗi xảy ra khi sử dụng item rank!");
            return false;
        }
    }

    /**
     * Xử lý rank 1
     */
    private static boolean handleRank1(Char character) {
        if (character.infoChar.rank != 0) {
            character.client.session.serivce.ShowMessRed("Bạn phải có rank 0 để sử dụng item này!");
            return false;
        }

        // Thêm item thưởng (KHÓA)
        Item itemNew = new Item(161, true); // true = khóa
        character.addItem(itemNew, "Mở rank");
        character.msgAddItemBag(itemNew);

        // Cập nhật rank
        character.infoChar.rank = 1;
        character.client.session.serivce.loadRank(character);

        // Cập nhật DataCenter
        updateDataCenterRank(1);

        return true;
    }

    /**
     * Xử lý rank 2
     */
    private static boolean handleRank2(Char character) {
        if (character.infoChar.rank != 1) {
            character.client.session.serivce.ShowMessRed("Bạn phải có rank 1 để sử dụng item này!");
            return false;
        }

        // Thêm item thưởng (KHÓA)
        Item itemNew = new Item(277, true); // true = khóa
        character.addItem(itemNew, "Mở rank");
        character.msgAddItemBag(itemNew);

        // Cập nhật rank
        character.infoChar.rank = 2;
        character.infoChar.exp_rank = 5;
        character.client.session.serivce.loadRank(character);

        // Cập nhật DataCenter
        updateDataCenterRank(2);

        return true;
    }

    /**
     * Xử lý rank 3
     */
    private static boolean handleRank3(Char character) {
        if (character.infoChar.rank != 2) {
            character.client.session.serivce.ShowMessRed("Bạn phải có rank 2 để sử dụng item này!");
            return false;
        }

        // Thêm item thưởng (KHÓA)
        Item itemNew = new Item(266, true, 5); // true = khóa
        character.addItem(itemNew, "Mở rank");
        character.msgAddItemBag(itemNew);

        // Cập nhật rank
        character.infoChar.rank = 3;
        character.infoChar.exp_rank = 10;
        character.client.session.serivce.loadRank(character);

        // Cập nhật DataCenter
        updateDataCenterRank(3);

        return true;
    }

    /**
     * Xử lý rank 4
     */
    private static boolean handleRank4(Char character) {
        if (character.infoChar.rank != 3) {
            character.client.session.serivce.ShowMessRed("Bạn phải có rank 3 để sử dụng item này!");
            return false;
        }

        // Thêm item thưởng (KHÓA)
        Item itemNew = new Item(347, true, 5); // true = khóa
        character.addItem(itemNew, "Mở rank");
        character.msgAddItemBag(itemNew);

        // Cập nhật rank
        character.infoChar.rank = 4;
        character.infoChar.exp_rank = 10;
        character.client.session.serivce.loadRank(character);

        // Cập nhật DataCenter
        updateDataCenterRank(4);

        return true;
    }

    /**
     * Xử lý rank 5
     */
    private static boolean handleRank5(Char character) {
        if (character.infoChar.rank != 4) {
            character.client.session.serivce.ShowMessRed("Bạn phải có rank 4 để sử dụng item này!");
            return false;
        }

        // Thêm item thưởng (KHÓA)
        Item itemNew = new Item(7, true, 1); // true = khóa
        character.addItem(itemNew, "Mở rank");
        character.msgAddItemBag(itemNew);

        // Cập nhật rank
        character.infoChar.rank = 5;
        character.infoChar.exp_rank = 20;
        character.client.session.serivce.loadRank(character);

        // Cập nhật DataCenter
        updateDataCenterRank(5);

        return true;
    }

    /**
     * Xử lý rank 6
     */
    private static boolean handleRank6(Char character) {
        if (character.infoChar.rank != 5) {
            character.client.session.serivce.ShowMessRed("Bạn phải có rank 5 để sử dụng item này!");
            return false;
        }

        // Thêm item thưởng (KHÓA)
        Item itemNew = new Item(277, true, 15); // true = khóa
        character.addItem(itemNew, "Mở rank");
        character.msgAddItemBag(itemNew);

        // Cập nhật rank
        character.infoChar.rank = 6;
        character.infoChar.exp_rank = 20;
        character.client.session.serivce.loadRank(character);

        // Cập nhật DataCenter
        updateDataCenterRank(6);

        return true;
    }

    /**
     * Xử lý rank 7
     */
    private static boolean handleRank7(Char character) {
        if (character.infoChar.rank != 6) {
            character.client.session.serivce.ShowMessRed("Bạn phải có rank 6 để sử dụng item này!");
            return false;
        }

        // Thêm item thưởng (KHÓA)
        Item itemNew = new Item(161, true, 10); // true = khóa
        character.addItem(itemNew, "Mở rank");
        character.msgAddItemBag(itemNew);

        // Cập nhật rank
        character.infoChar.rank = 7;
        character.infoChar.exp_rank = 30;
        character.client.session.serivce.loadRank(character);

        // Cập nhật DataCenter
        updateDataCenterRank(7);

        return true;
    }

    /**
     * Xử lý rank 8
     */
    private static boolean handleRank8(Char character) {
        if (character.infoChar.rank != 7) {
            character.client.session.serivce.ShowMessRed("Bạn phải có rank 7 để sử dụng item này!");
            return false;
        }

        // Thêm item thưởng (KHÓA)
        Item itemNew = new Item(152, true, 1); // true = khóa
        character.addItem(itemNew, "Mở rank");
        character.msgAddItemBag(itemNew);

        // Cập nhật rank
        character.infoChar.rank = 8;
        character.infoChar.exp_rank = 30;
        character.client.session.serivce.loadRank(character);

        // Cập nhật DataCenter
        updateDataCenterRank(8);

        return true;
    }

    /**
     * Xử lý rank 9
     */
    private static boolean handleRank9(Char character) {
        if (character.infoChar.rank != 8) {
            character.client.session.serivce.ShowMessRed("Bạn phải có rank 8 để sử dụng item này!");
            return false;
        }

        // Thêm item thưởng (KHÓA)
        Item itemNew = new Item(155, true, 1); // true = khóa
        character.addItem(itemNew, "Mở rank");
        character.msgAddItemBag(itemNew);

        // Cập nhật rank
        character.infoChar.rank = 9;
        character.infoChar.exp_rank = 40;
        character.client.session.serivce.loadRank(character);

        // Cập nhật DataCenter
        updateDataCenterRank(9);

        return true;
    }

    /**
     * Xử lý rank 10
     */
    private static boolean handleRank10(Char character) {
        if (character.infoChar.rank != 9) {
            character.client.session.serivce.ShowMessRed("Bạn phải có rank 9 để sử dụng item này!");
            return false;
        }

        // Item 1: Danh hiệu lục đạo (KHÓA)
        Item itemRank10_1 = new Item(467, true, 1); // true = khóa
        character.addItem(itemRank10_1, "Mở rank");
        character.msgAddItemBag(itemRank10_1);

        // Item 2: Cải trang lục đạo với options (KHÓA)
        Item itemRank10_2 = new Item(463, true); // true = khóa
        itemRank10_2.addItemOption(new ItemOption(0, 150));
        itemRank10_2.addItemOption(new ItemOption(1, 150));
        itemRank10_2.addItemOption(new ItemOption(2, 150));
        itemRank10_2.addItemOption(new ItemOption(209, 100));
        character.addItem(itemRank10_2, "Mở rank");
        character.msgAddItemBag(itemRank10_2);

        // Cập nhật rank
        character.infoChar.rank = 10;
        character.infoChar.exp_rank = 40;
        character.client.session.serivce.loadRank(character);

        // Cập nhật DataCenter
        updateDataCenterRank(10);

        return true;
    }

    /**
     * Cập nhật DataCenter rank
     */
    private static void updateDataCenterRank(int rank) {
        if (DataCenter.gI().phucLoiInfo.RankCaoNhat < rank) {
            DataCenter.gI().phucLoiInfo.RankCaoNhat = rank;
            DataCenter.gI().updatePhucLoi(1, DataCenter.gI().phucLoiInfo.RankCaoNhat);
        }
        DataCenter.gI().phucLoiInfo.TongRank++;
        DataCenter.gI().updatePhucLoi(0, DataCenter.gI().phucLoiInfo.TongRank);
    }
}
