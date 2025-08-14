package com.langla.real.npc;

import com.langla.data.DataCenter;
import com.langla.data.NpcTemplate;
import com.langla.lib.Utlis;
import com.langla.real.baucua.BauCua;
import com.langla.real.baucua.BauCuaTpl;
import com.langla.real.player.Entity;
import com.langla.real.map.Map;
import com.langla.real.map.Map.Zone;
import com.langla.real.player.Client;
import com.langla.real.player.Player;
import com.langla.real.task.TaskHandler;
import com.langla.server.handler.IActionNpc;

import java.util.ArrayList;

public class Npc extends Entity implements Cloneable {

    public static String[] getTextNpc(Zone zone, Npc npc, Client client) {

        ArrayList<String> list = new ArrayList<String>();
        ActionNpc[] array = getActionNpc(zone, npc, client);
        for (int i = 0; i < array.length; i++) {
            list.add(array[i].text);
        }
        return list.toArray(new String[list.size()]);
    }

    public static ActionNpc[] getActionNpc(Zone zone, Npc npc, Client client) {
        if (NpcTemplate.listAction == null) {
            NpcTemplate.listAction = new ArrayList[DataCenter.gI().NpcTemplate.length];
        }

        NpcTemplate.listAction[npc.id] = initializeActionList(npc, client);

        return NpcTemplate.listAction[npc.id].toArray(new ActionNpc[0]);
    }

    public static ArrayList<ActionNpc> initializeActionList(Npc npc, Client clients) {
        ArrayList<ActionNpc> list = new ArrayList<>();
        if (TaskHandler.gI().checkNPC(clients.mChar, npc.id) != null) {
            list = TaskHandler.gI().checkNPC(clients.mChar, npc.id);
        }
        switch (npc.id) {
            case 4: // lớp
            case 5:
            case 6:
            case 7:
            case 8:
                if (clients.mChar.infoChar.idClass > 0) {
                    addAction(list, "Tẩy tiềm năng",
                            client -> client.session.serivce.OpenMenu(npc.id, 1,
                                    " Bạn có chắc chắn muốn tẩy toàn bộ tiềm năng không?",
                                    "Đồng ý"));
                    addAction(list, "Tẩy kỹ năng",
                            client -> client.session.serivce.OpenMenu(npc.id, 2,
                                    " Bạn có chắc chắn muốn tẩy toàn bộ kỹ năng không?",
                                    "Đồng ý"));
                    addAction(list, "Luyện bí kíp", client -> client.session.serivce.openTabLuyenBiKip());
                    addAction(list, "Đổi bí kíp", client -> client.session.serivce.OpenMenu(npc.id, 3, "",
                            "Sách cao cấp; Sách hiền nhân Lôi (Đã tu Luyện 16000/18000);Sách hiền nhân Mộc (Đã tu Luyện 16000/18000);Sách hiền nhân Thủy (Đã tu Luyện 16000/18000);Sách hiền nhân Hỏa (Đã tu Luyện 16000/18000);Sách hiền nhân Phong (Đã tu Luyện 16000/18000);"));

                    addAction(list, "Đổi bùa nổ", client -> client.session.serivce.OpenMenu(npc.id, 4, "",
                            "Bùa nổ cao cấp (Trù 800 vàng); Bùa nổ siêu cấp (Trù 3.000 vàng); Hướng dẫn"));
                    if (clients.mChar.infoChar.isKhoaCap) {
                        addAction(list, "Mở khóa cấp nhân vật",
                                client -> client.session.serivce.OpenMenu(npc.id, 5,
                                        " Bạn có chắc chắn muốn mở khóa cấp không? Phí 50 vàng",
                                        "Đồng ý"));
                    } else {
                        addAction(list, "Khóa cấp nhân vật",
                                client -> client.session.serivce.OpenMenu(npc.id, 5,
                                        " Bạn có chắc chắn muốn khóa cấp không? Phí 50 vàng",
                                        "Đồng ý"));
                    }

                    addAction(list, "Trang bị lục đạo", client -> client.session.serivce.openTabLucDao());
                    addAction(list, "Đổi mật khẩu",
                            client -> client.session.serivce.InputClient("Nhập mật khẩu hiện tại", (short) 1));
                } else {
                    if (clients.mChar.infoChar.idTask == 8 && clients.mChar.infoChar.idStep == 8) {
                        clients.TypeMenu = 0;
                        addAction(list, ":-?Xin hãy nhận em làm đệ tử",
                                client -> NPC_Action.TypeMenu(clients, (int) getLopFormNPC(npc.id)));
                    }

                }
                break;
            case 21:
                BauCuaTpl bauCuaTpl = BauCua.gI().checkPlayer(clients.mChar.id);
                if (bauCuaTpl != null) {
                    addAction(list, "Xem kết quả Bầu Cua",
                            client -> client.session.serivce.OpenMenu(npc.id, 1, "Bạn cược " + bauCuaTpl.cuocText + ": "
                                    + Utlis.numberFormat(bauCuaTpl.vangCuoc) + " (Vàng)\\nTổng tiền đặt TÔM: "
                                    + Utlis.numberFormat(BauCua.gI().tongCuoc1) + " (Vàng)\\nTổng tiền đặt CUA: "
                                    + Utlis.numberFormat(BauCua.gI().tongCuoc2) + " (Vàng)\\nKết quả lần trước: "
                                    + BauCua.gI().ketQuaText + "\\n  Kết quả mới sau: "
                                    + Utlis.k((int) ((BauCua.gI().time - System.currentTimeMillis())) / 1000) + "",
                                    "Cập nhật; Hướng dẫn"));
                } else {
                    addAction(list, "Xem kết quả Bầu Cua",
                            client -> client.session.serivce.OpenMenu(npc.id, 0, "Tổng tiền đặt TÔM: "
                                    + Utlis.numberFormat(BauCua.gI().tongCuoc1) + " (Vàng)\\nTổng tiền đặt CUA: "
                                    + Utlis.numberFormat(BauCua.gI().tongCuoc2) + " (Vàng)\\nKết quả lần trước: "
                                    + BauCua.gI().ketQuaText + "\\n  Kết quả mới sau: "
                                    + Utlis.k((int) ((BauCua.gI().time - System.currentTimeMillis())) / 1000) + "",
                                    "Cập nhật;Đặt cược TÔM;Đặt cược CUA; Hướng dẫn"));
                }
                break;
            case 93: // dược phẩm
                addAction(list, "Dược phẩm", client -> client.session.serivce.SendShop(0));

                addAction(list, "Khu luyện tập", client -> client.session.serivce.OpenMenu(npc.id, 0, "Khu luyện tập",
                        "Tổ đội 3 người tăng (100%);" + "Tổ đội 6 người tăng (75%)"));
                break;
            case 99: // quán ăn
                addAction(list, "Tiệm thức ăn", client -> client.session.serivce.SendShop(4));
                break;
            case 100: // tạp hóa
                addAction(list, "Tiệm tạp hóa", client -> client.session.serivce.SendShop(2));
                break;
            case 92: // ngoại trang
                addAction(list, "Áo choàng (Theo hệ)", client -> client.session.serivce.SendShop(36));
                addAction(list, "Ngoại trang", client -> client.session.serivce.SendShop(18));
                break;
            case 91: // Phụ kiện
                addAction(list, "Dây thừng", client -> client.session.serivce.SendShop(14));
                addAction(list, "Móc sắt", client -> client.session.serivce.SendShop(15));
                addAction(list, "Ống tiêu", client -> client.session.serivce.SendShop(16));
                addAction(list, "Túi nhẫn giả", client -> client.session.serivce.SendShop(17));
                break;
            case 96: // binh khí
                addAction(list, "Binh khí", client -> client.session.serivce.SendShop(8));
                break;
            case 29: // Trang phục
                addAction(list, "Đai trán", client -> client.session.serivce.SendShop(9));
                addAction(list, "Áo", client -> client.session.serivce.SendShop(10));
                addAction(list, "Bao tay", client -> client.session.serivce.SendShop(11));
                addAction(list, "Quần", client -> client.session.serivce.SendShop(12));
                addAction(list, "Giày", client -> client.session.serivce.SendShop(13));
                break;
            case 31: // Hokage
                addAction(list, "Binh khí", client -> client.session.serivce.SendShop(20));
                addAction(list, "Đai trán", client -> client.session.serivce.SendShop(21));
                addAction(list, "Áo", client -> client.session.serivce.SendShop(22));
                addAction(list, "Bao tay", client -> client.session.serivce.SendShop(23));
                addAction(list, "Quần", client -> client.session.serivce.SendShop(24));
                addAction(list, "Dây thừng", client -> client.session.serivce.SendShop(26));
                addAction(list, "Móc sắt", client -> client.session.serivce.SendShop(27));
                addAction(list, "Ống tiêu", client -> client.session.serivce.SendShop(28));
                addAction(list, "Túi nhẫn giả", client -> client.session.serivce.SendShop(29));
                addAction(list, "Giày", client -> client.session.serivce.SendShop(25));
                break;
            case 28:
                addAction(list, "Ghép đá", client -> client.session.serivce.openTabGhepDa());
                addAction(list, "Cường hóa", client -> client.session.serivce.openTabCuongHoa());
                addAction(list, "Tách cường hóa", client -> client.session.serivce.openTabTachCuongHoa());
                addAction(list, "Nâng cấp bùa nổ", client -> client.session.serivce.openTabBuaNo());
                addAction(list, "Dịch chuyển trang bị", client -> client.session.serivce.openTabDichChuyen());
                addAction(list, "Khảm ngọc",
                        client -> client.session.serivce.openTabKhamNgoc((byte) client.mChar.infoChar.levelKhamNgoc));
                addAction(list, "Tách khảm ngọc", client -> client.session.serivce.openTabTachNgocKham());
                addAction(list, "Ghép cải trang", client -> client.session.serivce.openTabGhepCaiTrang(client.mChar));
                addAction(list, "Tách cải trang", client -> client.session.serivce.openTabTachCaiTrang());
                break;
            case 47:
                addAction(list, "Rương đồ", client -> client.mChar.SendBox());
                addAction(list, "Ở lại nơi này", client -> client.mChar.setMapDefault());
                break;
            case 73:
                addAction(list, "Thử vận may (25 vàng)", client -> client.session.serivce.openTabLucky());
                break;
            case 59:
                addAction(list, "Nhận chìa khóa", client -> client.session.serivce.nullopen());
                addAction(list, "Địa cung (Sơ cấp)", client -> client.session.serivce.nullopen());
                addAction(list, "Địa cung (Trung cấp)", client -> client.session.serivce.nullopen());
                addAction(list, "Địa cung (Cao cấp)", client -> client.session.serivce.nullopen());
                addAction(list, "Địa cung (Thượng cấp)", client -> client.session.serivce.nullopen());
                break;
            case 102:
                addAction(list, "Nhiệm vụ tuần hoàn", client -> client.session.serivce.nullopen());
                break;
            case 45:
            case 101:
                addAction(list, "Trường Konoha", client -> Map.maps[86].addChar(client));
                addAction(list, "Làng Lá", client -> Map.maps[75].addChar(client));
                addAction(list, "Làng Sương Mù", client -> Map.maps[60].addChar(client));
                addAction(list, "Làng Mây", client -> Map.maps[69].addChar(client));
                addAction(list, "Làng Đá", client -> Map.maps[85].addChar(client));
                addAction(list, "Làng Cát", client -> Map.maps[59].addChar(client));
                addAction(list, "Làng Cỏ", client -> Map.maps[68].addChar(client));
                addAction(list, "Làng Mưa", client -> Map.maps[102].addChar(client));
                break;
            case 97:
                int vnd = Player.getVNDByPlayerId(clients.player);
                addAction(list, "Đổi tinh thạch", client -> client.session.serivce.openTabTinhThach());
                addAction(list, "Đổi tiền đã nạp",
                        client -> client.session.serivce.OpenMenu(npc.id, 1,
                                "Số dư đã nạp " + Utlis.numberFormat(vnd) + "đ, nạp tiền tại trang chủ: aa.com",
                                "10.000 đổi 1.000 vàng;" +
                                        "20.000 đổi 2.000 vàng;" +
                                        "50.000 đổi 5.000 vàng;" +
                                        "100.000 đổi 11.000 vàng;" +
                                        "200.000 đổi 23.000 vàng;" +
                                        "500.000 đổi 59.000 vàng;" +
                                        "1.000.000 đổi 120.000 vàng"));
                addAction(list, "Đổi vàng trên người",
                        client -> client.session.serivce.OpenMenu(npc.id, 2, "Chọn số lượng cần đổi",
                                "10 vàng đổi 50.000 bạc;" +
                                        "100 vàng đổi 600.000 bạc;" +
                                        "1.000 vàng đổi 7.000.000 bạc;" +
                                        "10 vàng đổi 300.000 bạc khóa;" +
                                        "100 vàng đổi 4.000.000 bạc khóa;" +
                                        "1.000 vàng đổi 50.000.000 bạc khóa"));
                break;
            case 98:
                addAction(list, "Vĩ thú", client -> client.session.serivce.OpenMenu(npc.id, 0, "",
                        "Kích hoạt sức mạnh; Đổi nhất vĩ; Đổi thêm đuôi"));
                addAction(list, "Tách chakara vĩ thú", client -> client.session.serivce.OpenMenu(npc.id, 1, "",
                        "Vĩ thú ở trang bị chính (trừ 200 vàng)"));
                break;
            case 32:
                addAction(list, "Đại chiến nhẫn giả lần III", client -> client.session.serivce.nullopen());
                addAction(list, "Đại hội nhẫn giả", client -> client.session.serivce.nullopen());
                addAction(list, "Gia tộc", client -> client.session.serivce.OpenMenu(npc.id, 0, "Gia Tộc",
                        "Thành lập;" + "Tìm gia tộc;" +
                                "Mở cửa ải gia tộc;" +
                                "Vào ải gia tộc"));
                addAction(list, "Cấm thuật Izanami",
                        client -> client.session.serivce.OpenMenu(npc.id, 1,
                                "Hãy tập hợp tất cả đồng đội trong nhóm tại đây",
                                "Tham gia"));
                addAction(list, "Trang bị Sharingan", client -> client.session.serivce.open122((byte) 65));
                addAction(list, "Trang bị Byakugan", client -> client.session.serivce.open122((byte) 66));
                addAction(list, "Trang bị Rinnegan", client -> client.session.serivce.open122((byte) 67));
                break;
            case 105:
                addAction(list, "Trang bị Myobuku", client -> client.session.serivce.open122((byte) 64));
                break;
        }
        return list;
    }

    public static int getLopFormNPC(int idnpc) {
        switch (idnpc) {
            case 4:
                return 1;
            case 5:
                return 5;
            case 6:
                return 3;
            case 7:
                return 2;
            case 8:
                return 4;
        }
        return 1;
    }

    public static void addAction(ArrayList<ActionNpc> list, String actionName, IActionNpc action) {
        list.add(new ActionNpc(actionName, action));
    }

    public int id;

    public Npc cloneNpc() {
        try {
            return (Npc) super.clone();
        } catch (Exception e) {
            Utlis.logError(Npc.class, e, "Da say ra loi:\n" + e.getMessage());
            return null;
        }
    }

    public static class ActionNpc {

        public String text;
        public IActionNpc action;

        public ActionNpc(String text, IActionNpc actionNpc) {
            this.text = text;
            this.action = actionNpc;
        }
    }
}
