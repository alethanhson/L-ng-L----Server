package com.langla.real.npc;

import com.langla.data.DataCenter;
import com.langla.data.ItemOption;
import com.langla.data.Skill;
import com.langla.lib.Utlis;
import com.langla.real.family.Family;
import com.langla.real.family.FamilyTemplate;
import com.langla.real.family.Family_Member;
import com.langla.real.group.Group;
import com.langla.real.group.GroupTemplate;
import com.langla.real.item.Item;
import com.langla.real.map.Map;
import com.langla.real.other.VNDExchangeService;
import com.langla.real.player.Char;
import com.langla.real.player.Client;
import com.langla.real.task.TaskHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PKoolVN
 **/
public class NPC_Action {
    public static void TypeMenu(Client client, int select) {
        if (client.TypeMenu == 0) { // chưa có lớp
            if (client.mChar.infoChar.idClass > 0) {
                client.session.serivce.SendText("Bạn đã tham gia lớp từ trước đó");
                return;
            }
            int idclass = client.mChar.getLopFormSelectChar(client.mChar.infoChar.idNVChar);
            if (idclass != select) {
                client.session.serivce.SendText("Bạn hãy chọn đúng lớp của mình nhé.");
                return;
            }
            if (client.mChar.level() < 10) {
                client.session.serivce.SendText("Yêu cầu đạt cấp 10 trở lên.!");
                return;
            }
            client.mChar.infoChar.idClass = (byte) select;
            client.mChar.infoChar.idhe = (byte) select;

            Skill[][] _arraySkill = new Skill[][] {
                    client.mChar.skills_0.clone(),
                    client.mChar.skills_1.clone(),
                    client.mChar.skills_2.clone(),
                    client.mChar.skills_3.clone(),
                    client.mChar.skills_4.clone(),
                    client.mChar.skills_5.clone(), };
            client.mChar.arraySkill = _arraySkill[idclass];
            client.mChar.skillFight = client.mChar.arraySkill[0];
            if (select == 1 || select == 5) {
                client.mChar.arrayTiemNang[0] = 10;
                client.mChar.arrayTiemNang[1] = 0;
                client.mChar.arrayTiemNang[2] = 5;
                client.mChar.arrayTiemNang[3] = 5;
            } else {
                client.mChar.arrayTiemNang[0] = 0;
                client.mChar.arrayTiemNang[1] = 0;
                client.mChar.arrayTiemNang[2] = 15;
                client.mChar.arrayTiemNang[3] = 5;
            }
            client.session.serivce.sendChar();
            client.session.serivce.SendText("Chúc mừng bạn đã gia nhập lớp thành công.!");
            Item itemAdd = Item.getItemWithTypeAndLevel(1, 10, client.mChar.infoChar.gioiTinh,
                    client.mChar.infoChar.idClass);
            if (itemAdd != null) {
                itemAdd.he = client.mChar.infoChar.idhe;
                Item.setOptionsVuKhi(itemAdd, 10);
                itemAdd.createItemOptions();
                client.mChar.addItem(itemAdd, "Gia nhập học");
                client.mChar.msgAddItemBag(itemAdd);
            }

            TaskHandler.gI().PlusTask(client.mChar);
        }

    }

    public static void NPCMenu(Client client, byte select) {
        client.session.serivce.closeTab();
        if (client.NPCMenu >= 4 && client.NPCMenu <= 8) {
            switch (client.TypeMenu) {
                case 1:
                    if (select == 0) {
                        Item itembag = client.mChar.getItemBagById(169);
                        if (itembag == null || itembag.amount < 1) {
                            client.session.serivce.SendTextMenu("Không có tiềm năng lệnh");
                        } else {
                            client.mChar.removeItemBag(itembag, 1, "Sử dụng tẩy");
                            int diem = 0;
                            diem += client.mChar.arrayTiemNang[0];
                            diem += client.mChar.arrayTiemNang[1];
                            diem += client.mChar.arrayTiemNang[2];
                            diem += client.mChar.arrayTiemNang[3];
                            if (diem < 1) {
                                client.session.serivce.SendTextMenu("Tiềm năng của bạn đã được tẩy sạch.");
                                break;
                            }
                            if (client.mChar.infoChar.idClass == 1 || client.mChar.infoChar.idClass == 5) {
                                client.mChar.arrayTiemNang[0] = 10;
                                client.mChar.arrayTiemNang[1] = 0;
                                client.mChar.arrayTiemNang[2] = 5;
                                client.mChar.arrayTiemNang[3] = 5;
                            } else {
                                client.mChar.arrayTiemNang[0] = 0;
                                client.mChar.arrayTiemNang[1] = 0;
                                client.mChar.arrayTiemNang[2] = 15;
                                client.mChar.arrayTiemNang[3] = 5;
                            }
                            diem -= 20;
                            client.mChar.infoChar.diemTiemNang += diem;
                            client.mChar.msgUpdateDataChar();
                            client.mChar.setUpInfo(true);
                            client.session.serivce.SendTextMenu("Tiềm năng của bạn đã được tẩy sạch");
                        }
                    }
                    break;//
                case 2:
                    if (select == 0) {
                        Item itembag = client.mChar.getItemBagById(170);
                        if (itembag == null || itembag.amount < 1) {
                            client.session.serivce.SendTextMenu("Không có kỹ năng lệnh");
                        } else {
                            client.mChar.removeItemBag(itembag, "Sử dụng tẩy ");
                            int diem = 0;
                            for (int i = 0; i < client.mChar.arraySkill.length; i++) {
                                Skill skill = client.mChar.arraySkill[i];
                                diem += skill.level;
                                Skill newSkill = DataCenter.gI().getSkillWithIdAndLevel(skill.idTemplate, 0);
                                if (i == 0) {
                                    newSkill = DataCenter.gI().getSkillWithIdAndLevel(skill.idTemplate, 1);
                                }
                                client.mChar.arraySkill[i] = newSkill;

                            }
                            if (diem < 1) {
                                client.session.serivce.SendTextMenu("Kỹ năng của bạn đã được tẩy sạch.");
                                break;
                            }
                            client.mChar.skillFight = client.mChar.arraySkill[0];
                            client.mChar.infoChar.diemKyNang += diem;
                            client.mChar.msgUpdateDataChar();
                            client.mChar.msgUpdateSkill();
                            client.mChar.setUpInfo(true);
                            client.session.serivce.SendTextMenu("Kỹ năng của bạn đã được tẩy sạch ");
                        }
                    }
                    break;//
                case 3:
                    if (select == 0) {
                        client.session.serivce.openDoiBiKip();
                    }
                    break;//
                case 4:
                    if (select == 2) {
                        client.session.serivce.SendTextMenu(
                                "Cần đem theo bùa nổ trên người nhân vật và bùa nổ phải được nâng cấp tối đa. Sau khi đổi số cao của bùa nổ sẽ giảm đi phân nửa");
                    } else {
                        client.session.serivce.open122((byte) 97);
                    }
                    break;//
                case 5:
                    if (select == 0) {

                        if (client.mChar.infoChar.vang < 50) {
                            client.session.serivce.SendTextMenu("Không đủ vàng");
                        } else {
                            client.mChar.mineVang(50, true, true, "Khóa/Mở cấp");
                            client.mChar.infoChar.isKhoaCap = !client.mChar.infoChar.isKhoaCap;
                            client.session.serivce.SendTextMenu("Thao tác Thành công.!");
                        }
                    }
                    break;//
            }

            //
        } else if (client.NPCMenu == 97) { // ngân khổ quan
            if (client.TypeMenu == 1) {
                switch (select) {
                    case 0:
                        if (VNDExchangeService.exchangeVNDToGold(client.player, client.mChar, 10000)) {
                            client.session.serivce.SendTextMenu("Đổi thành công! 10,000đ = 1,000 vàng");
                        } else {
                            client.session.serivce.SendTextMenu("Không đủ tiền hoặc có lỗi xảy ra!");
                        }
                        break;

                    case 1: // 20,000đ = 2,000 vàng
                        if (VNDExchangeService.exchangeVNDToGold(client.player, client.mChar, 20000)) {
                            client.session.serivce.SendTextMenu("Đổi thành công! 20,000đ = 2,000 vàng");
                        } else {
                            client.session.serivce.SendTextMenu("Không đủ tiền hoặc có lỗi xảy ra!");
                        }
                        break;

                    case 2: // 50,000đ = 5,000 vàng
                        if (VNDExchangeService.exchangeVNDToGold(client.player, client.mChar, 50000)) {
                            client.session.serivce.SendTextMenu("Đổi thành công! 50,000đ = 5,000 vàng");
                        } else {
                            client.session.serivce.SendTextMenu("Không đủ tiền hoặc có lỗi xảy ra!");
                        }
                        break;

                    case 3: // 100,000đ = 11,000 vàng
                        if (VNDExchangeService.exchangeVNDToGold(client.player, client.mChar, 100000)) {
                            client.session.serivce.SendTextMenu("Đổi thành công! 100,000đ = 11,000 vàng");
                        } else {
                            client.session.serivce.SendTextMenu("Không đủ tiền hoặc có lỗi xảy ra!");
                        }
                        break;

                    case 4: // 200,000đ = 23,000 vàng
                        if (VNDExchangeService.exchangeVNDToGold(client.player, client.mChar, 200000)) {
                            client.session.serivce.SendTextMenu("Đổi thành công! 200,000đ = 23,000 vàng");
                        } else {
                            client.session.serivce.SendTextMenu("Không đủ tiền hoặc có lỗi xảy ra!");
                        }
                        break;

                    case 5: // 500,000đ = 59,000 vàng
                        if (VNDExchangeService.exchangeVNDToGold(client.player, client.mChar, 500000)) {
                            client.session.serivce.SendTextMenu("Đổi thành công! 500,000đ = 59,000 vàng");
                        } else {
                            client.session.serivce.SendTextMenu("Không đủ tiền hoặc có lỗi xảy ra!");
                        }
                        break;

                    case 6: // 1,000,000đ = 120,000 vàng
                        if (VNDExchangeService.exchangeVNDToGold(client.player, client.mChar, 1000000)) {
                            client.session.serivce.SendTextMenu("Đổi thành công! 1,000,000đ = 120,000 vàng");
                        } else {
                            client.session.serivce.SendTextMenu("Không đủ tiền hoặc có lỗi xảy ra!");
                        }
                        break;

                    default:
                        break;
                }
            }
            if (client.TypeMenu == 2) {
                switch (select) {
                    case 0:
                        if (client.mChar.infoChar.vang < 10) {
                            client.session.serivce.SendTextMenu("Không đủ vàng");
                        } else {
                            if (client.mChar.addBac(50000, true, true, "Đổi từ ngân khổ quan")) {
                                client.mChar.mineVang(10, true, true, "Đổi từ ngân khổ quan");
                            }
                        }
                        break;
                    case 1:
                        if (client.mChar.infoChar.vang < 100) {
                            client.session.serivce.SendTextMenu("Không đủ vàng");
                        } else {
                            if (client.mChar.addBac(600000, true, true, "Đổi từ ngân khổ quan")) {
                                client.mChar.mineVang(100, true, true, "Đổi từ ngân khổ quan");
                            }
                        }
                        break;
                    case 2:
                        if (client.mChar.infoChar.vang < 1000) {
                            client.session.serivce.SendTextMenu("Không đủ vàng");
                        } else {
                            if (client.mChar.addBac(7000000, true, true, "Đổi từ ngân khổ quan")) {
                                client.mChar.mineVang(1000, true, true, "Đổi từ ngân khổ quan");
                            }
                        }
                        break;
                    case 3:
                        if (client.mChar.infoChar.vang < 10) {
                            client.session.serivce.SendTextMenu("Không đủ vàng");
                        } else {
                            if (client.mChar.addBacKhoa(300000, true, true, "Đổi từ ngân khổ quan")) {
                                client.mChar.mineVang(10, true, true, "Đổi từ ngân khổ quan");
                            }
                        }
                        break;
                    case 4:
                        if (client.mChar.infoChar.vang < 100) {
                            client.session.serivce.SendTextMenu("Không đủ vàng");
                        } else {
                            if (client.mChar.addBacKhoa(4000000, true, true, "Đổi từ ngân khổ quan")) {
                                client.mChar.mineVang(100, true, true, "Đổi từ ngân khổ quan");
                            }
                        }
                        break;
                    case 5:
                        if (client.mChar.infoChar.vang < 1000) {
                            client.session.serivce.SendTextMenu("Không đủ vàng");
                        } else {
                            if (client.mChar.addBacKhoa(50000000, true, true, "Đổi từ ngân khổ quan")) {
                                client.mChar.mineVang(1000, true, true, "Đổi từ ngân khổ quan");
                            }
                        }
                        break;
                }
            }

        } else if (client.NPCMenu == 32) {
            if (client.TypeMenu == 0) {
                switch (select) {
                    case 0:
                        client.session.serivce.open122((byte) 53);
                        break;
                    case 1:
                        client.session.serivce.InputClient("Nhập tên gia tộc cần tìm:", (short) 3);
                        break;
                    case 2:
                        if (client.mChar.infoChar.familyId != -1) {
                            FamilyTemplate giaToc = Family.gI().getGiaToc(client.mChar);
                            if (giaToc != null) {

                                if (giaToc.info.soLanMoAi < 1) {
                                    client.session.serivce
                                            .ShowMessGold("Số lần mở ải đã hết, hãy quay lại vào ngày mai");
                                    break;
                                }
                                if (giaToc.MapAi.size() > 0) {
                                    client.session.serivce.ShowMessGold("Ải đã được mở hãy chọn vào ải");
                                    break;
                                }
                                Family_Member getMem = Family.gI().getMe(client.mChar, giaToc);
                                if (getMem != null && getMem.role >= 4) {
                                    giaToc.MapAi.clear();
                                    short id = Map.maps[46].createZoneCustom(-1, giaToc.id, 120000,
                                            System.currentTimeMillis(), false, false, client.mChar.level(), 500, 1,
                                            false);
                                    short id2 = Map.maps[47].createZoneCustom(-1, giaToc.id, 120000,
                                            System.currentTimeMillis(), false, false, client.mChar.level(), 500, 1,
                                            false);
                                    client.mChar.chatFamily("Đã mở cửa Ải Gia Tộc");
                                    Map.maps[46].addCharInMapCustom(client, id);
                                    giaToc.MapAi.put(46, id);
                                    giaToc.MapAi.put(47, id2);
                                    giaToc.info.soLanMoAi -= 1;
                                } else {
                                    client.session.serivce
                                            .ShowMessGold("Chỉ tộc trưởng hoặc tộc phó mới có quyền mở cửa ải");
                                }
                            } else {
                                client.session.serivce.ShowMessGold("Bạn chưa có gia tộc [2]");
                            }
                        } else {
                            client.session.serivce.ShowMessGold("Bạn chưa có gia tộc");
                        }
                        break;
                    case 3:
                        if (client.mChar.infoChar.familyId != -1) {
                            FamilyTemplate giaToc = Family.gI().getGiaToc(client.mChar);
                            if (giaToc != null) {
                                if (giaToc.MapAi.size() == 0) {
                                    client.session.serivce
                                            .NhacNhoMessage("Ải chưa được mở hay đợi tộc trưởng, tộc phó mở cửa");
                                    break;
                                }
                                Family_Member getMem = Family.gI().getMe(client.mChar, giaToc);
                                if (getMem != null) {
                                    Map.maps[46].addCharInMapCustom(client, giaToc.MapAi.get(46));
                                } else {
                                    client.session.serivce.ShowMessGold("Bạn chưa có gia tộc [3]");
                                }
                            } else {
                                client.session.serivce.ShowMessGold("Bạn chưa có gia tộc [2]");
                            }
                        } else {
                            client.session.serivce.ShowMessGold("Bạn chưa có gia tộc");
                        }
                        break;
                }
            }
            if (client.TypeMenu == 1) {
                switch (select) {
                    case 0:
                        if (client.mChar.infoChar.soLanCamThuat <= 0 && client.player.role < 2) {
                            client.mChar.client.session.serivce.NhacNhoMessage(
                                    "Số lần tham gia cấm thuật hôm nay đã hết quay lại vào ngày mai, hoặc hãy sử dụng Lệnh bài Izanami");
                            break;
                        }
                        client.mChar.infoChar.soLanCamThuat -= 1;

                        if (client.mChar.infoChar.groupId == -1) {
                            // tham gia cấm thuật
                            short id = Map.maps[89].createZoneCustom(client.mChar.infoChar.groupId, -1, 300000,
                                    System.currentTimeMillis(), false, false, client.mChar.level(), 500, 10000, false);
                            Map.maps[89].addCharInMapCustom(client, id);
                        } else {
                            GroupTemplate group = Group.gI().getGroup(client.mChar.infoChar.groupId);
                            if (group == null || group.getChar(client.mChar.id) == null) {
                                client.session.serivce.ShowMessGold("Thử lại sau");
                                break;
                            }
                            Char key = group.getKey();
                            if (group.idZoneCamThuat == 0) {
                                if (key == null || key.id != client.mChar.id) {
                                    client.session.serivce.NhacNhoMessage("Chỉ trưởng nhóm mới có thể mở cấm thuật");
                                    break;
                                }

                                short id = Map.maps[89].createZoneCustom(client.mChar.infoChar.groupId, -1, 300000,
                                        System.currentTimeMillis(), false, false, client.mChar.level(), 500, 1, false);
                                group.idZoneCamThuat = id;
                                client.mChar.chatGroup("Đã mở cửa cấm thuât Izanami.");
                                List<Char> charList = client.mChar.zone.getVecChar();
                                List<Char> listCharGroup = new ArrayList<>();

                                for (Char c : charList) {
                                    if (c.client != null && c.infoChar.groupId == client.mChar.infoChar.groupId) {
                                        listCharGroup.add(c);
                                    }
                                }

                                for (Char c : listCharGroup) {
                                    Map.maps[89].addCharInMapCustom(c.client, id);
                                }

                            } else {
                                Map.maps[89].addCharInMapCustom(client, group.idZoneCamThuat);
                            }
                        }
                        break;
                }
            }
        } else if (client.NPCMenu == 93) {
            switch (select) {
                case 0:
                case 1:
                    if (client.mChar.infoChar.groupId == -1) {
                        client.session.serivce.ShowMessGold("Bạn chưa có nhóm");
                        break;
                    }
                    GroupTemplate group = Group.gI().getGroup(client.mChar.infoChar.groupId);
                    if (group == null || group.getChar(client.mChar.id) == null) {
                        client.session.serivce.ShowMessGold("Bạn chưa có nhóm");
                        break;
                    }
                    Char key = group.getKey();
                    if (group.idZoneLuyenTap == 0) {
                        if (key == null || key.id != client.mChar.id) {
                            client.session.serivce.NhacNhoMessage(
                                    "Chỉ trưởng nhóm mới có thể mở khu luyện tập. Sau khi trưởng nhóm mở tất cả thành viên mới có thể vô khu luyện tập.");
                            break;
                        }
                        Item getItem = client.mChar.getItemBagById(371);
                        if (getItem == null) {
                            client.session.serivce.ShowMessGold("Không có thẻ luyện tập");
                            break;
                        }

                        client.mChar.removeItemBag(getItem, 1, "Mở khu luyện tập");
                        short id = Map.maps[84].createZoneCustom(client.mChar.infoChar.groupId, -1, 18000000,
                                System.currentTimeMillis(), false, false, client.mChar.level() + 6, 3500, 3500, true); // open
                                                                                                                       // phải
                                                                                                                       // sửa
                        Map.maps[84].addCharInMapCustom(client, id);
                        group.idZoneLuyenTap = id;
                        client.mChar.chatGroup("Đã mở khu luyện tâp.");
                    } else {
                        Map.maps[84].addCharInMapCustom(client, group.idZoneLuyenTap);
                    }

                    break;

            }
        } else if (client.NPCMenu == 98) {
            if (client.TypeMenu == 0) {
                switch (select) {
                    case 0: // kích hoạt sức mạnh
                        if (client.mChar.infoChar.bac < 1000000) {
                            client.session.serivce.ShowMessGold("Cần 1.000.000 bạc");
                            break;
                        }
                        Item itemKichHoat = client.mChar.arrItemBody[10];
                        if (itemKichHoat == null || !itemKichHoat.isViThu()) {
                            client.session.serivce.ShowMessGold("Cần mang vĩ thú trên người");
                            break;
                        }
                        if (itemKichHoat.getDiemChiSo(client, 305) > 0) {
                            client.session.serivce.ShowMessGold("Vĩ thú đã được kích hoạt sức mạnh rồi");
                            break;
                        }
                        client.mChar.mineBac(1000000, true, true, "Kích hoạt sức mạnh vĩ thú");

                        client.session.serivce.ShowMessGold("Kích hoạt sức mạnh vĩ thú thành công");
                        itemKichHoat.addItemOptionIndex0(new ItemOption(305, 0, 180000)); // sức mạnh
                        client.mChar.updateItemBody(itemKichHoat);
                        break;
                    case 1: // đổi nhất vĩ
                        if (client.mChar.infoChar.bac < 1000000) {
                            client.session.serivce.ShowMessGold("Cần 1.000.000 bạc");
                            break;
                        }
                        if (client.mChar.getAmountAllById(687) < 1000) {
                            client.session.serivce.ShowMessGold("Cần 1000 lông vĩ thú để đổi");
                            break;
                        }
                        if (client.mChar.getCountNullItemBag() < 1) {
                            client.session.serivce.ShowMessGold("Túi không đủ chỗ chứa");
                            break;
                        }

                        client.mChar.removeAmountAllItemBagById(687, 1000, "Đổi nhất vĩ");
                        Item nhatVi = new Item(476);

                        nhatVi.addItemOption(new ItemOption(0, 300, 500)); // hp
                        nhatVi.addItemOption(new ItemOption(1, 300, 500)); // mp
                        nhatVi.addItemOption(new ItemOption(2, 100, 150)); // dame
                        nhatVi.addItemOption(new ItemOption(166, 40, 50)); // chí mạng
                        nhatVi.addItemOption(new ItemOption(136, 15, 20)); // hồi hp
                        nhatVi.addItemOption(new ItemOption(137, 15, 20)); // hồi mp
                        nhatVi.addItemOption(new ItemOption(149, 1, 5)); // bỏ qua kháng tính
                        nhatVi.addItemOption(new ItemOption(151, 40, 50)); // né
                        nhatVi.addItemOption(new ItemOption(180, 100, 120)); // chính xác
                        nhatVi.addItemOption(new ItemOption(209, 40, 50)); // tăng char
                        nhatVi.createItemOptions();
                        client.mChar.addItem(nhatVi, "Đổi từ NPC " + client.TypeMenu);
                        client.mChar.msgAddItemBag(nhatVi);
                        client.mChar.mineBac(1000000, true, true, "Đổi nhất vĩ");
                        break;
                    case 2: // đổi thêm đuôi

                        if (client.mChar.getAmountAllById(687) < 3000) {
                            client.session.serivce.ShowMessGold("Cần 3000 lông vĩ thú để đổi");
                            break;
                        }
                        if (client.mChar.getCountNullItemBag() < 1) {
                            client.session.serivce.ShowMessGold("Túi không đủ chỗ chứa");
                            break;
                        }
                        Item itemBody = client.mChar.arrItemBody[10];
                        if (itemBody == null || !itemBody.isViThu()) {
                            client.session.serivce.ShowMessGold("Cần mang vĩ thú trên người để đổi");
                            break;
                        }
                        if (itemBody.getItemTemplate().id == 484) {
                            client.session.serivce.ShowMessGold("Vĩ thú đã đạt cấp tối đa");
                            break;
                        }
                        int min = itemBody.getChiSo(1, client, 305);
                        if (min < 180000) {
                            client.session.serivce.ShowMessGold("Sức mạnh vĩ thú cần đạt cấp độ tối đa mới có thể đổi");
                            break;
                        }
                        int capViThu = (itemBody.getItemTemplate().id + 1) - 476;
                        int bac = 3000000 * capViThu;
                        if (client.mChar.infoChar.bac < bac) {
                            client.session.serivce.ShowMessGold("Cần " + Utlis.numberFormat(bac) + " bạc");
                            break;
                        }
                        Item viThu = new Item(itemBody.getItemTemplate().id + 1);

                        viThu.addItemOption(new ItemOption(0, 500 + (capViThu * 60), 600 + (capViThu * 60))); // hp
                        viThu.addItemOption(new ItemOption(1, 500 + (capViThu * 60), 600 + (capViThu * 60))); // mp
                        viThu.addItemOption(new ItemOption(2, 130 + (capViThu * 10), 160 + (capViThu * 10))); // dame
                        viThu.addItemOption(new ItemOption(166, 40 + (capViThu * 3), 50 + (capViThu * 3))); // chí mạng
                        viThu.addItemOption(new ItemOption(136, 15 + (capViThu * 3), 20 + (capViThu * 3))); // hồi hp
                        viThu.addItemOption(new ItemOption(137, 15 + (capViThu * 3), 20 + (capViThu * 3))); // hồi mp
                        viThu.addItemOption(new ItemOption(149, 1 + (capViThu / 3), 5 + (capViThu / 3))); // bỏ qua
                                                                                                          // kháng tính
                        viThu.addItemOption(new ItemOption(151, 40 + (capViThu * 3), 50 + (capViThu * 3))); // né
                        viThu.addItemOption(new ItemOption(180, 100 + (capViThu * 10), 120 + (capViThu * 10))); // chính
                                                                                                                // xác
                        viThu.addItemOption(new ItemOption(209, 40 + (capViThu * 3), 50 + (capViThu * 3))); // tăng char
                        viThu.createItemOptions();
                        client.mChar.addItem(viThu, "Đổi từ NPC " + client.TypeMenu);
                        client.mChar.msgAddItemBag(viThu);
                        client.mChar.removeAmountAllItemBagById(687, 3000, "Đổi thêm đuôi");
                        client.session.serivce.removeItemBody((short) itemBody.index);
                        client.mChar.removeItemBodyByIndex(itemBody.index, "Đổi thêm đuôi");
                        client.mChar.mineBac(bac, true, true, "Đổi thêm đuôi");
                        break;
                }
            } else if (client.TypeMenu == 1) {
                switch (select) {
                    case 0:
                        if (client.mChar.infoChar.vang < 200) {
                            client.session.serivce.ShowMessGold("Cần 200 vàng");
                            break;
                        }
                        if (client.mChar.getCountNullItemBag() < 1) {
                            client.session.serivce.ShowMessGold("Túi không đủ chỗ chứa");
                            break;
                        }
                        Item itemBody = client.mChar.arrItemBody[10];
                        if (itemBody == null || !itemBody.isViThu()) {
                            client.session.serivce.ShowMessGold("Cần mang vĩ thú trên người để thực hiện");
                            break;
                        }

                        int min = itemBody.getChiSo(1, client, 305);
                        if (min <= 0) {
                            client.session.serivce.ShowMessGold("Vĩ thú không có chakara");
                            break;
                        }
                        Item charka = new Item(763, true);
                        charka.amount = (int) (min * 0.95);
                        client.mChar.addItem(charka, "Tách charka vĩ thú");
                        client.mChar.msgAddItemBag(charka);
                        client.mChar.mineVang(200, true, true, "Tách charka vĩ thú");

                        int lv = min / 10000;
                        itemBody.level = 0;
                        if (lv > 0) {
                            itemBody.mainOptionViThu();
                            client.mChar.setUpInfo(true);
                        }
                        client.mChar.updateItemBody(itemBody);
                        break;
                }
            }
        } else if (client.NPCMenu == 21) {
            if (client.TypeMenu == 0) {
                switch (select) {
                    case 0:
                        client.mChar.zone.selectNpc(client, 11, 0, -1);
                        break;
                    case 1:
                        client.session.serivce.InputClient("Nhập số vàng muốn cược (TÔM)", (short) 4);
                        break;
                    case 2:
                        client.session.serivce.InputClient("Nhập số vàng muốn cược (CUA)", (short) 5);
                        break;
                    case 3:
                        client.session.serivce.SendText(
                                "Cứ mỗi 60 giây hệ thống sẽ random TÔM hoặc CUA\\nNếu bạn đặt cược trúng sẽ được thưởng ngẫu nhiên:\\n"
                                        +
                                        "1. Vàng * (1.5 - 2.0) vàng cược\\n2. Vỏ sò * (0.05 - 0.1) vàng cược \\n3. Chakra vĩ thú * (10 - 15) vàng cược\\n - Phần thưởng sẽ được trao vào Thư");
                        break;
                }
            } else if (client.TypeMenu == 1) {
                switch (select) {
                    case 0:
                        client.mChar.zone.selectNpc(client, 11, 0, -1);
                        break;
                    case 3:
                        client.session.serivce.SendText(
                                "Cứ mỗi 60 giây hệ thống sẽ random TÔM hoặc CUA\\nNếu bạn đặt cược trúng sẽ được thưởng ngẫu nhiên:\\n"
                                        +
                                        "1. Vàng * (1.5 - 2.0) vàng cược\\n2. Vỏ sò * (0.05 - 0.1) vàng cược \\n3. Chakra vĩ thú * (10 - 15) vàng cược\\n - Phần thưởng sẽ được trao vào Thư");
                        break;
                }
            }
        }

        ///

    }

    //
}
