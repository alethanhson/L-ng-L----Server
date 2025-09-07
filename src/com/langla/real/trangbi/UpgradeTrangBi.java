package com.langla.real.trangbi;

import com.langla.data.DataCenter;
import com.langla.data.ItemOption;
import com.langla.lib.Utlis;
import com.langla.real.item.Item;
import com.langla.real.player.Char;
import com.langla.server.lib.Message;

import java.util.Vector;

/**
 * @author PKoolVN
 **/
public class UpgradeTrangBi {
    protected static UpgradeTrangBi Instance;

    public static UpgradeTrangBi gI() {
        if (Instance == null)
            Instance = new UpgradeTrangBi();
        return Instance;
    }

    public void DoiBuaNo(Char character, byte typeItem, short index) {
        try {
            int bac = 1000000;
            int vang = 800;
            Item itemUpgrade = character.getItemByType(typeItem, index);
            if (itemUpgrade == null || itemUpgrade.getItemTemplate().type != 13) {
                character.client.session.serivce.ShowMessGold("Lỗi vật phẩm không hợp lệ");
                return;
            }
            if (itemUpgrade.id == 811) {
                character.client.session.serivce
                        .ShowMessGold("Bùa nổ đã được đổi tối đa. Hãy sử dụng siêu bùa nổ để nâng cấp bùa");
                return;
            }
            if (itemUpgrade.id == 602)
                vang = 3000;
            ItemOption[] itemOptions = itemUpgrade.getItemOption();
            for (int i = 0; i < itemOptions.length; i++) {
                try {
                    if (itemOptions[i].a[1] < itemOptions[i].a[2]) {
                        character.client.session.serivce.ShowMessGold("Bùa nổ chưa được nâng cấp tối đa");
                        return;
                    }
                } catch (Exception x) {

                }
            }
            if (character.getCountNullItemBag() < 0) {
                character.client.session.serivce.ShowMessGold("Túi không đủ chỗ chứa");
                return;
            }
            if (character.infoChar.bac < bac) {
                character.client.session.serivce.ShowMessGold("Không đủ bạc");
                return;
            }
            if (character.infoChar.vang < bac) {
                character.client.session.serivce.ShowMessGold("Không đủ vàng");
                return;
            }
            Item itemAdd = new Item(602);

            if (itemUpgrade.id == 602) {
                itemAdd.id = 811;
            }

            String[] he1 = new String[] { "54,0,1000;62,0,1000;2,100,-1;5,50,-1;149,3,-1;122,5,-1",
                    "54,0,1500;62,0,1500;2,150,-1;5,150,-1;167,150,-1;149,3,-1;122,15,-1" };
            String[] he2 = new String[] { "55,0,1000;58,0,1000;2,100,-1;5,50,-1;149,3,-1;122,5,-1",
                    "55,0,1500;58,0,1500;2,150,-1;5,150,-1;167,150,-1;149,3,-1;122,15,-1" };
            String[] he3 = new String[] { "56,0,1000;59,0,1000;2,100,-1;5,50,-1;149,3,-1;122,5,-1",
                    "56,0,1500;59,0,1500;2,150,-1;5,150,-1;167,150,-1;149,3,-1;122,15,-1" };
            String[] he4 = new String[] { "57,0,1000;60,0,1000;2,100,-1;5,50,-1;149,3,-1;122,5,-1",
                    "57,0,1500;60,0,1500;2,150,-1;5,150,-1;167,150,-1;149,3,-1;122,15,-1" };
            String[] he5 = new String[] { "53,0,1000;61,0,1000;2,100,-1;5,50,-1;149,3,-1;122,5,-1",
                    "53,0,1500;61,0,1500;2,150,-1;5,150,-1;167,150,-1;149,3,-1;122,15,-1" };

            itemAdd.he = character.infoChar.idhe;
            switch (itemAdd.he) {
                case 1:
                    itemAdd.strOptions = he1[0];
                    if (itemAdd.id == 811)
                        itemAdd.strOptions = he1[1];
                    break;
                case 2:
                    itemAdd.strOptions = he2[0];
                    if (itemAdd.id == 811)
                        itemAdd.strOptions = he2[1];
                    break;
                case 3:
                    itemAdd.strOptions = he3[0];
                    if (itemAdd.id == 811)
                        itemAdd.strOptions = he3[1];
                    break;
                case 4:
                    itemAdd.strOptions = he4[0];
                    if (itemAdd.id == 811)
                        itemAdd.strOptions = he4[1];
                    break;
                case 5:
                    itemAdd.strOptions = he5[0];
                    if (itemAdd.id == 811)
                        itemAdd.strOptions = he5[1];
            }
            character.client.session.serivce.closeTab();
            if (typeItem == 0) {
                character.removeItemBag(itemUpgrade, true, "Đổi bùa nổ");
            } else if (typeItem == 2) {
                character.removeItemBodyByIndex(itemUpgrade.index, "Đổi bùa nổ");
                character.msgUpdateItemBody();
            } else if (typeItem == 3) {
                character.removeItemBody2ByIndex(itemUpgrade.index, "Đổi bùa nổ");
                character.msgUpdateItemBody();
            }
            character.addItem(itemAdd, "Đổi bùa ");
            character.msgAddItemBag(itemAdd);
            character.mineVang(vang, true, true, "Đổi bùa nổ");
            character.mineBac(bac, true, true, "Đổi bùa nổ");
            character.client.session.serivce
                    .ShowMessGold("Bạn đã đổi thành công " + itemUpgrade.getItemTemplate().name);
            if (typeItem != 0)
                character.setUpInfo(true);
        } catch (Exception ex) {
            Utlis.logError(UpgradeTrangBi.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void NangCapBuaNo(Char character, byte typeNC, byte typeItem, short index, Item[] item) {
        try {
            Item itemUpgrade = character.getItemByType(typeItem, index);
            if (itemUpgrade == null || itemUpgrade.getItemTemplate().type != 13) {
                character.client.session.serivce.ShowMessGold("Lỗi vật phẩm vui lòng sắp xếp và thử lại");
                return;
            }

            int max = itemUpgrade.getChiSoI(typeNC, 2, character.client);
            int min = itemUpgrade.getChiSoI(typeNC, 1, character.client);

            if (typeNC == 0 && min >= max) {
                character.client.session.serivce.ShowMessGold("Chỉ số này đã được nâng cấp tối đa");
                return;
            }

            if (item.length == 0) {
                character.client.session.serivce.ShowMessGold("Chưa bỏ tinh thạch");
                return;
            }
            int tinhThach = 0;
            for (int i = 0; i < item.length; i++) {
                Item get = character.getItemBagByIndex(item[i].index);
                if (get != item[i]) {
                    character.client.session.serivce.ShowMessGold("Lỗi vật phẩm vui lòng sắp xếp và thử lại");
                    return;
                } else {
                    if (get.id != 160) {
                        character.client.session.serivce.ShowMessGold("Lỗi tinh thạch không hợp lệ");
                        return;
                    }
                    tinhThach += get.getAmount();
                }
            }
            if (tinhThach < 10) {
                character.client.session.serivce.ShowMessGold("Số lượng tinh thạch tối thiểu là 10");
                return;
            }
            int diem = tinhThach / 10;

            int tinhdiem = min + diem;
            if (tinhdiem >= max) {
                diem = max - min;
                if (itemUpgrade.id == 811) {
                    itemUpgrade.plusOptionByIndex(2, 1, 25);
                    itemUpgrade.plusOptionByIndex(3, 1, 25);
                    itemUpgrade.plusOptionByIndex(4, 1, 25);
                    itemUpgrade.plusOptionByIndex(5, 1, 1);
                    itemUpgrade.plusOptionByIndex(6, 1, 2);
                }
            }
            itemUpgrade.plusOptionByIndex(typeNC, 1, diem);
            Message msg = new Message((byte) 106);
            itemUpgrade.write(msg.writer);
            msg.writeByte(typeItem);
            msg.writeByte(item.length);
            for (Item value : item) {
                msg.writeShort(value.index);
            }
            character.client.session.sendMessage(msg);

            for (int i = 0; i < item.length; i++) {
                character.removeItemBag(item[i], true, "Nâng cấp bùa nổ");
            }

            if (typeItem != 0)
                character.setUpInfo(true);
        } catch (Exception ex) {
            Utlis.logError(UpgradeTrangBi.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void handle(Char character, byte typeItem, short index, Item[] item) {

        try {
            Item itemUpgrade = character.getItemByType(typeItem, index);
            if (itemUpgrade == null) {
                character.client.session.serivce.ShowMessGold("Lỗi vật phẩm vui lòng sắp xếp và thử lại");
                return;
            }
            if (itemUpgrade.getDiemChiSo(character.client, 165, 159, 163, 164) >= 1) {
                character.client.session.serivce.ShowMessGold("Trang bị này đã được nâng cấp");
                return;
            }
            if (item.length == 0) {
                character.client.session.serivce.ShowMessGold("Chưa bỏ ngọc vào");
                return;
            }
            if (itemUpgrade.getItemTemplate().type >= 10) {
                character.client.session.serivce.ShowMessGold("Trang bị không hợp lệ");
                return;
            }
            int ngocUpgrade = 0;
            int bacUpgrade = 0;

            if (itemUpgrade.getItemTemplate().type == 2 || itemUpgrade.getItemTemplate().type == 7
                    || itemUpgrade.getItemTemplate().type == 8) {
                bacUpgrade = 15000000;
                ngocUpgrade = itemUpgrade.getItemTemplate().levelNeed / 10 * 100;
                if (itemUpgrade.getItemTemplate().levelNeed / 10 == 5) {
                    bacUpgrade = 20000000;
                } else if (itemUpgrade.getItemTemplate().levelNeed / 10 == 6) {
                    bacUpgrade = 40000000;
                    ngocUpgrade = 700;
                }
            } else {
                bacUpgrade = 25000000;
                ngocUpgrade = itemUpgrade.getItemTemplate().levelNeed / 10 * 100;
                if (itemUpgrade.getItemTemplate().levelNeed / 10 == 5) {
                    bacUpgrade = 30000000;
                } else if (itemUpgrade.getItemTemplate().levelNeed / 10 == 6) {
                    bacUpgrade = 40000000;
                    ngocUpgrade = 700;
                }
            }

            if (bacUpgrade > character.infoChar.bacKhoa) {
                character.client.session.serivce.ShowMessGold("Không đủ bạc khóa");
                return;
            }
            int amountNgocBag = 0;
            for (int i = 0; i < item.length; i++) {
                Item get = character.getItemBagByIndex(item[i].index);
                if (get != item[i]) {
                    character.client.session.serivce.ShowMessGold("Lỗi vật phẩm vui lòng sắp xếp và thử lại");
                    return;
                } else {
                    if (get.id != itemUpgrade.ngocUpgrade()) {
                        character.client.session.serivce.ShowMessGold("Lỗi ngọc không hợp lệ");
                        return;
                    }
                    amountNgocBag += get.getAmount();
                }
            }
            if (amountNgocBag < ngocUpgrade) {
                character.client.session.serivce.ShowMessGold("Không đủ Ngọc");
                return;
            }

            // set str
            if (itemUpgrade.getItemTemplate().type == 1) { // vũ khí
                byte var1 = itemUpgrade.level;
                itemUpgrade.a(0);
                itemUpgrade.isLock = true;
                ItemOption[] var2 = itemUpgrade.L();
                Vector var3 = new Vector();
                String[] var4 = new String[] { "", "168,10,-1", "169,10,-1", "170,10,-1", "171,10,-1", "172,10,-1" };
                String[] var5 = new String[] { "", "259,80,-1", "260,80,-1", "261,80,-1", "262,80,-1", "263,80,-1" };
                boolean var6 = false;
                boolean var7 = false;

                for (int var8 = 0; var8 < var2.length; ++var8) {
                    if (var2[var8].a[0] != 148) {
                        if (var2[var8].getItemOptionTemplate().type == 2 && !var6) {
                            var3.add(new ItemOption(var4[itemUpgrade.he]));
                            var3.add(new ItemOption("254,10,-1"));
                            var6 = true;
                        }

                        var3.add(var2[var8]);
                        if (!var7) {
                            if (itemUpgrade.getItemTemplate().levelNeed / 10 == 4
                                    && var2[var8].getItemOptionTemplate().type == 6) {
                                var3.add(new ItemOption("47,150,-1"));
                                var7 = true;
                            }

                            if (var2[var8].getItemOptionTemplate().type == 7) {
                                if (itemUpgrade.getItemTemplate().levelNeed / 10 >= 5) {
                                    var3.add(new ItemOption("252,5,-1"));
                                    var3.add(new ItemOption(var5[itemUpgrade.he]));
                                }

                                if (itemUpgrade.getItemTemplate().levelNeed / 10 >= 6) {
                                    var3.add(new ItemOption("286,300,-1"));
                                }

                                var7 = true;
                            }
                        }
                    }
                }
                var3.add(new ItemOption("165,0,-1"));
                itemUpgrade.strOptions = Item.a(var3);
                itemUpgrade.a(var1);
            } else if (itemUpgrade.getItemTemplate().type == 2 || itemUpgrade.getItemTemplate().type == 7
                    || itemUpgrade.getItemTemplate().type == 8) {

                byte var1 = itemUpgrade.level;
                itemUpgrade.a(0);
                itemUpgrade.isLock = true;
                ItemOption[] var2 = itemUpgrade.L();
                Vector var3 = new Vector();
                String[] var4 = null;
                if (itemUpgrade.getItemTemplate().type == 8) {
                    var4 = new String[] { "161,85,-1", "173,20,-1", "253,2000,-1", "159,0,-1" };
                } else if (itemUpgrade.getItemTemplate().type == 2) {
                    var4 = new String[] { "162,1,-1", "173,20,-1", "253,2000,-1", "159,0,-1" };
                } else if (itemUpgrade.getItemTemplate().type == 7) {
                    var4 = new String[] { "167,70,-1", "173,20,-1", "253,2000,-1", "159,0,-1" };
                }

                boolean var5 = false;
                boolean var6 = false;

                for (int var7 = 0; var7 < var2.length; ++var7) {
                    if (var2[var7].a[0] != 148) {
                        if (var2[var7].getItemOptionTemplate().type == 2 && !var5) {
                            var3.add(new ItemOption(var4[0]));
                            var3.add(new ItemOption(var4[1]));
                            var5 = true;
                        }

                        var3.add(var2[var7]);
                        if (!var6) {
                            if (itemUpgrade.getItemTemplate().levelNeed / 10 == 4
                                    && var2[var7].getItemOptionTemplate().type == 6) {
                                var3.add(new ItemOption("42,20,-1"));
                                var6 = true;
                            }

                            if (var2[var7].getItemOptionTemplate().type == 7) {
                                if (itemUpgrade.getItemTemplate().levelNeed / 10 >= 5) {
                                    var3.add(new ItemOption(var4[2]));
                                }

                                if (itemUpgrade.getItemTemplate().levelNeed / 10 >= 6) {
                                    itemUpgrade.b(var3);
                                }

                                var6 = true;
                            }
                        }
                    }
                }

                var3.add(new ItemOption(var4[3]));
                itemUpgrade.strOptions = Item.a(var3);
                itemUpgrade.a(var1);
            } else if (itemUpgrade.getItemTemplate().type == 5 || itemUpgrade.getItemTemplate().type == 6
                    || itemUpgrade.getItemTemplate().type == 9) {

                byte var1 = itemUpgrade.level;
                itemUpgrade.a(0);
                itemUpgrade.isLock = true;
                ItemOption[] var2 = itemUpgrade.L();
                Vector var3 = new Vector();
                String[] var4 = null;
                if (itemUpgrade.getItemTemplate().type == 6) {
                    var4 = new String[] { "166,60,-1", "173,20,-1", "253,2000,-1", "163,0,-1" };
                } else if (itemUpgrade.getItemTemplate().type == 5) {
                    var4 = new String[] { "174,2,-1", "173,20,-1", "253,2000,-1", "163,0,-1" };
                } else if (itemUpgrade.getItemTemplate().type == 9) {
                    var4 = new String[] { "255,2,-1", "173,20,-1", "253,2000,-1", "163,0,-1" };
                }

                boolean var5 = false;
                boolean var6 = false;

                for (int var7 = 0; var7 < var2.length; ++var7) {
                    if (var2[var7].a[0] != 148) {
                        if (var2[var7].getItemOptionTemplate().type == 2 && !var5) {
                            var3.add(new ItemOption(var4[0]));
                            var3.add(new ItemOption(var4[1]));
                            var5 = true;
                        }

                        var3.add(var2[var7]);
                        if (!var6) {
                            if (itemUpgrade.getItemTemplate().levelNeed / 10 == 4
                                    && var2[var7].getItemOptionTemplate().type == 6) {
                                var3.add(new ItemOption("42,20,-1"));
                                var6 = true;
                            }

                            if (var2[var7].getItemOptionTemplate().type == 7) {
                                if (itemUpgrade.getItemTemplate().levelNeed / 10 >= 5) {
                                    var3.add(new ItemOption(var4[2]));
                                }

                                if (itemUpgrade.getItemTemplate().levelNeed / 10 >= 6) {
                                    itemUpgrade.b(var3);
                                }

                                var6 = true;
                            }
                        }
                    }
                }

                var3.add(new ItemOption(var4[3]));
                itemUpgrade.strOptions = Item.a(var3);
                itemUpgrade.a(var1);
            } else if (itemUpgrade.getItemTemplate().type == 0 || itemUpgrade.getItemTemplate().type == 3
                    || itemUpgrade.getItemTemplate().type == 4) {
                byte var1 = itemUpgrade.level;
                itemUpgrade.a(0);
                itemUpgrade.isLock = true;
                ItemOption[] var2 = itemUpgrade.L();
                Vector var3 = new Vector();
                String[] var4 = null;
                if (itemUpgrade.getItemTemplate().type == 0) {
                    var4 = new String[] { "256,4,-1", "173,20,-1", "253,2000,-1", "164,0,-1" };
                } else if (itemUpgrade.getItemTemplate().type == 4) {
                    var4 = new String[] { "257,4,-1", "173,20,-1", "253,2000,-1", "164,0,-1" };
                } else if (itemUpgrade.getItemTemplate().type == 3) {
                    var4 = new String[] { "258,25,-1", "173,20,-1", "253,2000,-1", "164,0,-1" };
                }

                boolean var5 = false;
                boolean var6 = false;

                for (int var7 = 0; var7 < var2.length; ++var7) {
                    if (var2[var7].a[0] != 148) {
                        if (var2[var7].getItemOptionTemplate().type == 2 && !var5) {
                            var3.add(new ItemOption(var4[0]));
                            var3.add(new ItemOption(var4[1]));
                            var5 = true;
                        }

                        var3.add(var2[var7]);
                        if (!var6) {
                            if (itemUpgrade.getItemTemplate().levelNeed / 10 == 4
                                    && var2[var7].getItemOptionTemplate().type == 6) {
                                var3.add(new ItemOption("42,20,-1"));
                                var6 = true;
                            }

                            if (var2[var7].getItemOptionTemplate().type == 7) {
                                if (itemUpgrade.getItemTemplate().levelNeed / 10 >= 5) {
                                    var3.add(new ItemOption(var4[2]));
                                }

                                if (itemUpgrade.getItemTemplate().levelNeed / 10 >= 6) {
                                    itemUpgrade.b(var3);
                                }

                                var6 = true;
                            }
                        }
                    }
                }

                var3.add(new ItemOption(var4[3]));
                itemUpgrade.strOptions = Item.a(var3);
                itemUpgrade.a(var1);
            }

            Message msg = new Message((byte) -35);
            itemUpgrade.write(msg.writer);
            msg.writeByte(typeItem);
            msg.writeByte(item.length);
            for (Item value : item) {
                msg.writeShort(value.index);
            }
            character.client.session.sendMessage(msg);

            character.mineBacKhoa(bacUpgrade, true, true, "Nâng cấp trang bị");
            for (int i = 0; i < item.length; i++) {
                character.removeItemBag(item[i], true, "Nâng cấp trang bị");
            }

            if (typeItem != 0)
                character.setUpInfo(true);
        } catch (Exception ex) {
            Utlis.logError(UpgradeTrangBi.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }

    }

    public void upgradeLucDao(Char character, byte typeItem, short index, Item[] item) {
        try {
            Item itemUpgrade = character.getItemByType(typeItem, index);
            if (itemUpgrade == null) {
                character.client.session.serivce.ShowMessGold("Lỗi vật phẩm vui lòng sắp xếp và thử lại");
                return;
            }
            if (itemUpgrade.getDiemChiSo(character.client, 361) >= 1) {
                character.client.session.serivce.ShowMessGold("Trang bị này đã được nâng cấp");
                return;
            }
            if (itemUpgrade.getDiemChiSo(character.client, 165, 159, 163, 164) == 0) {
                character.client.session.serivce.ShowMessGold("Trang bị không hợp lệ");
                return;
            }
            if (item.length == 0) {
                character.client.session.serivce.ShowMessGold("Chưa bỏ ngọc vào");
                return;
            }
            if (itemUpgrade.getItemTemplate().type >= 10) {
                character.client.session.serivce.ShowMessGold("Trang bị không hợp lệ");
                return;
            }
            int ngocUpgrade = 0;
            int bacUpgrade = 0;

            ngocUpgrade = itemUpgrade.getItemTemplate().levelNeed / 10 * 100 + 150;
            bacUpgrade = itemUpgrade.getItemTemplate().levelNeed / 10 * 10000000 + 15000000;

            if (bacUpgrade > character.infoChar.bacKhoa) {
                character.client.session.serivce.ShowMessGold("Không đủ bạc khóa");
                return;
            }
            int amountNgocBag = 0;
            for (int i = 0; i < item.length; i++) {
                Item get = character.getItemBagByIndex(item[i].index);
                if (get != item[i]) {
                    character.client.session.serivce.ShowMessGold("Lỗi vật phẩm vui lòng sắp xếp và thử lại");
                    return;
                } else {

                    if (get.id != itemUpgrade.ngocUpgrade()) {
                        character.client.session.serivce.ShowMessGold(
                                "Cần bỏ vào ngọc " + DataCenter.gI().ItemTemplate[itemUpgrade.ngocUpgrade()].name);
                        return;
                    }
                    amountNgocBag += get.getAmount();
                }
            }
            if (amountNgocBag < ngocUpgrade) {
                character.client.session.serivce.ShowMessGold("Không đủ Ngọc");
                return;
            }

            // set str
            if (itemUpgrade.getItemTemplate().type == 1) { // vũ khí
                byte levelTB = itemUpgrade.level;
                itemUpgrade.a(0);
                itemUpgrade.isLock = true;
                ItemOption[] optionGocTrangBi = itemUpgrade.L();
                Vector listOPNew = new Vector();

                String[] listOPS = new String[] { "350,500,-1", "351,500,-1", "352,500,-1", "353,500,-1", "354,500,-1",
                        "355,100,-1", "356,100,-1", "357,100,-1", "358,100,-1", "359,100,-1" };
                boolean var6 = false;

                for (int i = 0; i < optionGocTrangBi.length; ++i) { // for op gốc

                    if (optionGocTrangBi[i].a[0] != 165) {
                        if (optionGocTrangBi[i].getItemOptionTemplate().type == 3 && !var6) {
                            listOPNew.add(new ItemOption("349,50,-1")); // +Hiệu ứng ngẫu nhiên
                            var6 = true;
                        }

                        listOPNew.add(optionGocTrangBi[i]);

                    }
                }
                if (itemUpgrade.getItemTemplate().levelNeed / 10 >= 6) {
                    int rand = Utlis.nextInt(0, 8);
                    listOPNew.add(new ItemOption(listOPS[rand]));
                    listOPNew.add(new ItemOption("360,5,-1"));
                    listOPNew.add(new ItemOption("361,8,-1")); // +8 % trang bị lục đạo 4x
                } else if (itemUpgrade.getItemTemplate().levelNeed / 10 == 5) {
                    listOPNew.add(new ItemOption("286,300,-1"));
                    listOPNew.add(new ItemOption("361,7,-1")); // +7 % trang bị lục đạo 4x
                } else {
                    listOPNew.add(new ItemOption("252,5,-1")); // +17 đồ 4x
                    listOPNew.add(new ItemOption("361,6,-1")); // +6 % trang bị lục đạo 4x
                }
                itemUpgrade.strOptions = Item.a(listOPNew);
                itemUpgrade.a(levelTB);
            } else if (itemUpgrade.getItemTemplate().type == 2 || itemUpgrade.getItemTemplate().type == 7
                    || itemUpgrade.getItemTemplate().type == 8) {

                byte var1 = itemUpgrade.level;
                itemUpgrade.a(0);
                itemUpgrade.isLock = true;
                ItemOption[] var2 = itemUpgrade.L();
                Vector var3 = new Vector();
                String[] var4 = null;
                if (itemUpgrade.getItemTemplate().type == 8) {
                    var4 = new String[] { "161,85,-1", "173,20,-1", "253,2000,-1", "159,0,-1" };
                } else if (itemUpgrade.getItemTemplate().type == 2) {
                    var4 = new String[] { "162,1,-1", "173,20,-1", "253,2000,-1", "159,0,-1" };
                } else if (itemUpgrade.getItemTemplate().type == 7) {
                    var4 = new String[] { "167,70,-1", "173,20,-1", "253,2000,-1", "159,0,-1" };
                }

                String[] listOPS = new String[] { "350,500,-1", "351,500,-1", "352,500,-1", "353,500,-1", "354,500,-1",
                        "355,100,-1", "356,100,-1", "357,100,-1", "358,100,-1", "359,100,-1" };

                boolean var5 = false;
                boolean var6 = false;

                for (int var7 = 0; var7 < var2.length; ++var7) {
                    if (var2[var7].a[0] != 159) {
                        if (var2[var7].getItemOptionTemplate().type == 3 && !var5) {
                            var3.add(new ItemOption("348,100,-1")); // Giảm trừ chí maạng
                            var5 = true;
                        }

                        var3.add(var2[var7]);

                    }
                }
                if (itemUpgrade.getItemTemplate().levelNeed / 10 >= 6) {
                    int rand = Utlis.nextInt(0, 8);
                    var3.add(new ItemOption(listOPS[rand]));
                    var3.add(new ItemOption("361,8,-1")); // +8 % trang bị lục đạo 4x
                } else if (itemUpgrade.getItemTemplate().levelNeed / 10 == 5) {
                    itemUpgrade.lucdao(var3);
                    var3.add(new ItemOption("361,7,-1")); // +7 % trang bị lục đạo 4x
                } else {
                    var3.add(new ItemOption(var4[2]));
                    var3.add(new ItemOption("361,6,-1")); // +6 % trang bị lục đạo 4x
                }
                itemUpgrade.strOptions = Item.a(var3);
                itemUpgrade.a(var1);
            } else if (itemUpgrade.getItemTemplate().type == 5 || itemUpgrade.getItemTemplate().type == 6
                    || itemUpgrade.getItemTemplate().type == 9) {

                byte var1 = itemUpgrade.level;
                itemUpgrade.a(0);
                itemUpgrade.isLock = true;
                ItemOption[] var2 = itemUpgrade.L();
                Vector var3 = new Vector();
                String[] var4 = null;
                if (itemUpgrade.getItemTemplate().type == 6) {
                    var4 = new String[] { "166,60,-1", "173,20,-1", "253,2000,-1", "163,0,-1" };
                } else if (itemUpgrade.getItemTemplate().type == 5) {
                    var4 = new String[] { "174,2,-1", "173,20,-1", "253,2000,-1", "163,0,-1" };
                } else if (itemUpgrade.getItemTemplate().type == 9) {
                    var4 = new String[] { "255,2,-1", "173,20,-1", "253,2000,-1", "163,0,-1" };
                }

                String[] listOPS = new String[] { "350,500,-1", "351,500,-1", "352,500,-1", "353,500,-1", "354,500,-1",
                        "355,100,-1", "356,100,-1", "357,100,-1", "358,100,-1", "359,100,-1" };

                boolean var5 = false;
                boolean var6 = false;

                for (int var7 = 0; var7 < var2.length; ++var7) {
                    if (var2[var7].a[0] != 163) {
                        if (var2[var7].getItemOptionTemplate().type == 3 && !var5) {
                            var3.add(new ItemOption("348,100,-1")); // Giảm trừ chí maạng
                            var5 = true;
                        }

                        var3.add(var2[var7]);

                    }
                }
                if (itemUpgrade.getItemTemplate().levelNeed / 10 >= 6) {
                    int rand = Utlis.nextInt(0, 8);
                    var3.add(new ItemOption(listOPS[rand]));
                    var3.add(new ItemOption("361,8,-1")); // +8 % trang bị lục đạo 4x
                } else if (itemUpgrade.getItemTemplate().levelNeed / 10 == 5) {
                    itemUpgrade.lucdao(var3);
                    var3.add(new ItemOption("361,7,-1")); // +7 % trang bị lục đạo 4x
                } else {
                    var3.add(new ItemOption(var4[2]));
                    var3.add(new ItemOption("361,6,-1")); // +6 % trang bị lục đạo 4x
                }
                itemUpgrade.strOptions = Item.a(var3);
                itemUpgrade.a(var1);
            } else if (itemUpgrade.getItemTemplate().type == 0 || itemUpgrade.getItemTemplate().type == 3
                    || itemUpgrade.getItemTemplate().type == 4) {
                byte var1 = itemUpgrade.level;
                itemUpgrade.a(0);
                itemUpgrade.isLock = true;
                ItemOption[] var2 = itemUpgrade.L();
                Vector var3 = new Vector();
                String[] var4 = null;
                if (itemUpgrade.getItemTemplate().type == 0) {
                    var4 = new String[] { "256,4,-1", "173,20,-1", "253,2000,-1", "164,0,-1" };
                } else if (itemUpgrade.getItemTemplate().type == 4) {
                    var4 = new String[] { "257,4,-1", "173,20,-1", "253,2000,-1", "164,0,-1" };
                } else if (itemUpgrade.getItemTemplate().type == 3) {
                    var4 = new String[] { "258,25,-1", "173,20,-1", "253,2000,-1", "164,0,-1" };
                }

                String[] listOPS = new String[] { "350,500,-1", "351,500,-1", "352,500,-1", "353,500,-1", "354,500,-1",
                        "355,100,-1", "356,100,-1", "357,100,-1", "358,100,-1", "359,100,-1" };

                boolean var5 = false;
                boolean var6 = false;

                for (int var7 = 0; var7 < var2.length; ++var7) {
                    if (var2[var7].a[0] != 164) {
                        if (var2[var7].getItemOptionTemplate().type == 3 && !var5) {
                            var3.add(new ItemOption("348,100,-1")); // Giảm trừ chí maạng
                            var5 = true;
                        }

                        var3.add(var2[var7]);

                    }
                }
                if (itemUpgrade.getItemTemplate().levelNeed / 10 >= 6) {
                    int rand = Utlis.nextInt(0, 8);
                    var3.add(new ItemOption(listOPS[rand]));
                    var3.add(new ItemOption("361,8,-1")); // +8 % trang bị lục đạo 4x
                } else if (itemUpgrade.getItemTemplate().levelNeed / 10 == 5) {
                    itemUpgrade.lucdao(var3);
                    var3.add(new ItemOption("361,7,-1")); // +7 % trang bị lục đạo 4x
                } else {
                    var3.add(new ItemOption(var4[2]));
                    var3.add(new ItemOption("361,6,-1")); // +6 % trang bị lục đạo 4x
                }
                itemUpgrade.strOptions = Item.a(var3);
                itemUpgrade.a(var1);
            }

            Message msg = new Message((byte) -35);
            itemUpgrade.write(msg.writer);
            msg.writeByte(typeItem);
            msg.writeByte(item.length);
            for (Item value : item) {
                msg.writeShort(value.index);
            }
            character.client.session.sendMessage(msg);

            character.mineBacKhoa(bacUpgrade, true, true, "Nâng cấp trang bị");
            for (int i = 0; i < item.length; i++) {
                character.removeItemBag(item[i], true, "Nâng cấp trang bị");
            }
            if (typeItem != 0)
                character.setUpInfo(true);
        } catch (Exception ex) {
            Utlis.logError(UpgradeTrangBi.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }
}
