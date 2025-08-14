/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.langla.real.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.langla.data.DataCenter;
import com.langla.data.ItemOption;
import com.langla.data.ItemOptionTemplate;
import com.langla.data.ItemTemplate;
import com.langla.lib.Utlis;
import com.langla.real.player.Client;
import com.langla.server.handler.IActionItem;
import com.langla.server.lib.Message;
import com.langla.server.lib.Writer;
import com.tgame.model.Caption;
import com.tgame.model.LangLa_hg;
import java.io.IOException;
import java.util.*;

/**
 * @author PKoolVN
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item implements Cloneable {

    static Item getItemWithTypeAndLevel(int type, int level) {
        for (int i = 0; i < DataCenter.gI().ItemTemplate.length; i++) {
            if (DataCenter.gI().ItemTemplate[i].type == type
                    && DataCenter.gI().ItemTemplate[i].levelNeed / 10 * 10 == level / 10 * 10) {
                return new Item(i, true);
            }
        }
        return null;
    }

    public static void setOptionsVuKhi(Item item, int level) {
        item.addItemOption(new ItemOption(2, (50 * level / 10), 50 + (50 * level / 10)));
        item.addItemOption(new ItemOption(3, level * 2, (level * 2) + 10));
        item.addItemOption(new ItemOption(20, level * 2, (level * 2) + 10));
        if (level < 60) {
            if (item.he == 5) {
                item.addItemOption(new ItemOption(21, level, level + 10));
            } else {
                item.addItemOption(new ItemOption(item.he + 21, level, level + 10));
            }
        } else {
            if (item.he == 5) {
                item.addItemOption(new ItemOption(21, (level) * 2, (level + 10) * 2));
            } else {
                item.addItemOption(new ItemOption(item.he + 21, (level) * 2, (level + 10) * 2));
            }
        }
        if (level >= 10) {
            item.addItemOption(new ItemOption((item.he - 1) + 48, 5 * (level / 10)));
        }
        if (level >= 20) {
            item.addItemOption(new ItemOption(28, (level / 10) * 30));
        }
        if (level >= 30) {
            item.addItemOption(new ItemOption(31, (100 + ((level / 10) * 50))));
        }
        if (level >= 40) {
            if (level >= 60) {
                item.addItemOption(new ItemOption(41, 120));
            } else if (level >= 50) {
                item.addItemOption(new ItemOption(41, 110));
            } else if (level >= 40) {
                item.addItemOption(new ItemOption(41, 95));
            }
        }
        if (level >= 50) {
            if (level >= 60) {
                item.addItemOption(new ItemOption(47, 220));
            } else if (level >= 50) {
                item.addItemOption(new ItemOption(47, 200));
            }
        }
    }

    public static void setOptionsTrangBiPhuKien(Item item, int level) {
        item.addItemOption(new ItemOption(0, 20 * (level / 10), (20 * (level / 10)) + 10));
        item.addItemOption(new ItemOption(1, 20 * (level / 10), (20 * (level / 10)) + 10));

        int num1 = 5 * ((level / 10));
        int num2 = 5 * ((level / 10) + 1);

        int num3 = 20 + (20 * ((level / 10) - 1));
        int num4 = 30 + (20 * ((level / 10) - 1));
        if (item.he == 5) {
            item.addItemOption(new ItemOption(7, num1, num2));
        } else {
            item.addItemOption(new ItemOption(item.he + 7, num1, num2));
        }

        if (item.getItemTemplate().type == 9) {
            item.addItemOption(new ItemOption(12, num1, num2));

        } else if (item.getItemTemplate().type == 7 || item.getItemTemplate().type == 5) {
            item.addItemOption(new ItemOption(14, num1, num2));

        } else if (item.getItemTemplate().type == 3) {
            item.addItemOption(new ItemOption(15, num1, num2));

        } else if (item.getItemTemplate().type == 0 || item.getItemTemplate().type == 2
                || item.getItemTemplate().type == 4 || item.getItemTemplate().type == 6) {
            item.addItemOption(new ItemOption(13, num1, num2));

        } else if (item.getItemTemplate().type == 8) {
            item.addItemOption(new ItemOption(18, num3, num4));
        }

        if (item.getItemTemplate().type == 8) {
            item.addItemOption(new ItemOption(17, 40 + (20 * (level / 10))));
        } else if (item.getItemTemplate().type == 0 || item.getItemTemplate().type == 2
                || item.getItemTemplate().type == 4 || item.getItemTemplate().type == 6) {
            item.addItemOption(new ItemOption(26, 2 * (level / 10)));
        } else {
            item.addItemOption(new ItemOption(27, 2 * (level / 10)));
        }
        if (level >= 20) {
            num1 = 50 + (((level / 10) - 1) * 50);
            if (item.getItemTemplate().type == 0 || item.getItemTemplate().type == 2 || item.getItemTemplate().type == 4
                    || item.getItemTemplate().type == 6 || item.getItemTemplate().type == 8) {
                item.addItemOption(new ItemOption(29, num1));
            } else {
                item.addItemOption(new ItemOption(30, num1));

            }
        }
        if (level >= 30) {
            num1 = 5 * (level / 10);
            if (item.getItemTemplate().type == 0 || item.getItemTemplate().type == 2 || item.getItemTemplate().type == 4
                    || item.getItemTemplate().type == 6 || item.getItemTemplate().type == 8) {
                item.addItemOption(new ItemOption(32, num1));
            } else {
                item.addItemOption(new ItemOption(33, num1));
            }
        }
        if (level >= 40) {
            num1 = 100 + (25 * ((level / 10) - 4));
            if (item.he == 5) {
                item.addItemOption(new ItemOption(35, num1));
            } else {
                item.addItemOption(new ItemOption(item.he + 35, num1));
            }
        }
        if (level >= 50) {
            num1 = 5 * (level / 10);
            item.addItemOption(new ItemOption(42, num1));
        }
    }

    public static void setOptionsTrangBiPhuKien_hokage(Item item, int level) {
        item.addItemOption(
                new ItemOption(0, (int) ((20 * (level / 10)) * 1.5), (int) (((20 * (level / 10)) + 10) * 1.55)));
        item.addItemOption(
                new ItemOption(1, (int) ((20 * (level / 10)) * 1.5), (int) (((20 * (level / 10)) + 10) * 1.55)));

        int num1 = (int) ((5 * ((level / 10))) * 2.3);
        int num2 = (int) ((5 * ((level / 10) + 1)) * 2.3);

        int num3 = (int) ((20 + (20 * ((level / 10) - 1))) * 1.55);
        int num4 = (int) ((30 + (20 * ((level / 10) - 1))) * 1.55);

        if (level < 60)
            num3 = (int) ((20 + (20 * ((level / 10) - 1))) * 1.5);
        if (level < 60)
            num4 = (int) ((30 + (20 * ((level / 10) - 1))) * 1.5);
        if (item.he == 5) {
            item.addItemOption(new ItemOption(7, num1, num2));
        } else {
            item.addItemOption(new ItemOption(item.he + 7, num1, num2)); // dòng 3
        }

        if (item.getItemTemplate().type == 9) {
            item.addItemOption(new ItemOption(12, num1, num2));

        } else if (item.getItemTemplate().type == 7 || item.getItemTemplate().type == 5) {
            item.addItemOption(new ItemOption(14, num1, num2));

        } else if (item.getItemTemplate().type == 3) {
            item.addItemOption(new ItemOption(15, num1, num2));

        } else if (item.getItemTemplate().type == 0 || item.getItemTemplate().type == 2
                || item.getItemTemplate().type == 4 || item.getItemTemplate().type == 6) {
            item.addItemOption(new ItemOption(13, num1, num2));

        } else if (item.getItemTemplate().type == 8) {
            item.addItemOption(new ItemOption(18, num3, num4));
        }

        if (item.getItemTemplate().type == 8) {
            item.addItemOption(new ItemOption(17, (int) (40 + (20 * (level / 10)) * 1.4)));
        } else if (item.getItemTemplate().type == 0 || item.getItemTemplate().type == 2
                || item.getItemTemplate().type == 4 || item.getItemTemplate().type == 6) {
            item.addItemOption(new ItemOption(26, (int) ((2 * (level / 10)) * 1.5)));
        } else {
            item.addItemOption(new ItemOption(27, (int) ((2 * (level / 10)) * 1.5)));
        }
        if (level >= 20) {
            num1 = (int) ((50 + (((level / 10) - 1) * 50)) * 1.4);
            if (level >= 60)
                num1 = (int) ((50 + (((level / 10) - 1) * 50)) * 1.6);
            if (item.getItemTemplate().type == 0 || item.getItemTemplate().type == 2 || item.getItemTemplate().type == 4
                    || item.getItemTemplate().type == 6 || item.getItemTemplate().type == 8) {
                item.addItemOption(new ItemOption(29, num1));
            } else {
                item.addItemOption(new ItemOption(30, num1));

            }
        }
        if (level >= 30) {
            num1 = (int) ((5 * (level / 10)) * 1.5);
            if (item.getItemTemplate().type == 0 || item.getItemTemplate().type == 2 || item.getItemTemplate().type == 4
                    || item.getItemTemplate().type == 6 || item.getItemTemplate().type == 8) {
                item.addItemOption(new ItemOption(32, num1));
            } else {
                item.addItemOption(new ItemOption(33, num1));
            }
        }
        if (level >= 40) {
            num1 = (int) ((100 + (25 * ((level / 10) - 4))) * 1.2);
            if (item.he == 5) {
                item.addItemOption(new ItemOption(35, num1));
            } else {
                item.addItemOption(new ItemOption(item.he + 35, num1));
            }
        }
        if (level >= 50) {
            num1 = (int) (5 * (level / 10) * 1.66);
            item.addItemOption(new ItemOption(42, num1));
        }
        item.addItemOption(new ItemOption(148, item.getItemTemplate().type));
    }

    public static void setOptionsVuKhi_hokage(Item item, int level) {
        item.addItemOption(new ItemOption(2, (int) ((50 * level / 10) * 1.5), (int) ((50 + (50 * level / 10)) * 1.5)));
        item.addItemOption(new ItemOption(3, (int) ((level * 2) * 1.461538461538462),
                (int) (((level * 2) + 10) * 1.461538461538462)));

        item.addItemOption(new ItemOption(20, (int) ((level * 2) * 1.461538461538462),
                (int) (((level * 2) + 10) * 1.461538461538462)));

        if (level < 60) {
            if (item.he == 5) {
                item.addItemOption(new ItemOption(21, (int) (level * 1.142857142857143),
                        (int) ((level + 10) * 1.142857142857143)));
            } else {
                item.addItemOption(new ItemOption(item.he + 21, (int) (level * 1.142857142857143),
                        (int) ((level + 10) * 1.142857142857143)));
            }
        } else {
            if (item.he == 5) {
                item.addItemOption(new ItemOption(21, (int) (((level) * 2) * 1.142857142857143),
                        (int) (((level + 10) * 2) * 1.142857142857143)));
            } else {
                item.addItemOption(new ItemOption(item.he + 21, (int) (((level) * 2) * 1.142857142857143),
                        (int) (((level + 10) * 2) * 1.142857142857143)));
            }
        }
        if (level >= 10) {
            item.addItemOption(new ItemOption((item.he - 1) + 48, (int) ((5 * (level / 10)) * 1.833333333333333)));
        }
        if (level >= 20) {
            item.addItemOption(new ItemOption(28, (int) (((level / 10) * 30) * 1.111111111111111)));
        }
        if (level >= 30) {
            item.addItemOption(new ItemOption(31, (int) ((100 + ((level / 10) * 50)))));
        }
        if (level >= 40) {
            if (level >= 60) {
                item.addItemOption(new ItemOption(41, (int) (120 * 1.5)));
            } else if (level >= 50) {
                item.addItemOption(new ItemOption(41, (int) (110 * 1.5)));
            } else if (level >= 40) {
                item.addItemOption(new ItemOption(41, (int) (95 * 1.5)));
            }
        }
        if (level >= 50) {
            if (level >= 60) {
                item.addItemOption(new ItemOption(47, 240));
            } else if (level >= 50) {
                item.addItemOption(new ItemOption(47, 220));
            }
        }
        item.addItemOption(new ItemOption(148, item.getItemTemplate().type));
    }

    public static void setOptionsAoChoang(Item item, int level) {
        item.addItemOption(new ItemOption(256, 30 * (level / 10), (30 * (level / 10)) + 5));
        item.addItemOption(new ItemOption(257, 30 * (level / 10), (30 * (level / 10)) + 5));
        if (item.he == 0) {
            if (level < 45) {
                item.addItemOption(new ItemOption(0, 30 * (level / 10), (30 * (level / 10)) + 5));
            }
        }

    }

    public boolean u() {
        if (this.getItemTemplate().type >= 0 && this.getItemTemplate().type <= 9) {
            if (this.X()) {
                if (this.getItemTemplate().levelNeed >= 60 && this.level < 19
                        || this.getItemTemplate().levelNeed >= 50 && this.level < 18
                        || this.getItemTemplate().levelNeed >= 40 && this.level < 17) {
                    return true;
                }
            } else if (this.W()) {
                if (this.getItemTemplate().levelNeed >= 60 && this.level < 18
                        || this.getItemTemplate().levelNeed >= 50 && this.level < 17
                        || this.getItemTemplate().levelNeed >= 40 && this.level < 16) {
                    return true;
                }
            } else if (this.getItemTemplate().levelNeed >= 50 && this.level < 16
                    || this.getItemTemplate().levelNeed >= 40 && this.level < 14
                    || this.getItemTemplate().levelNeed >= 30 && this.level < 12
                    || this.getItemTemplate().levelNeed >= 20 && this.level < 8 || this.level < 4) {
                return true;
            }
        }

        return false;
    }

    public static Item getItemWithTypeAndLevel(int type, int level, int gioiTinh, int idClass) {
        for (int i = 0; i < DataCenter.gI().ItemTemplate.length; i++) {
            if (DataCenter.gI().ItemTemplate[i].type == type
                    && DataCenter.gI().ItemTemplate[i].levelNeed / 10 * 10 == level / 10 * 10) {
                if ((DataCenter.gI().ItemTemplate[i].gioiTinh == 2
                        || DataCenter.gI().ItemTemplate[i].gioiTinh == gioiTinh)
                        && (DataCenter.gI().ItemTemplate[i].idClass == 0
                                || DataCenter.gI().ItemTemplate[i].idClass == idClass)) {
                    return new Item(i, true);
                } else {
                }
            }
        }
        return null;
    }

    public synchronized static void taoDataCaiTrang() {
        DataCenter.gI().dataCaiTrang.clear();
        for (int i = 0; i < DataCenter.gI().ItemTemplate.length; i++) {
            if (DataCenter.gI().ItemTemplate[i].type == 14) {
                Item newItem = new Item(DataCenter.gI().ItemTemplate[i].id);
                DataCenter.gI().dataCaiTrang.add(newItem);
            }
        }
    }

    public static Item getItemWithName(String name, String detail) {
        for (int i = 0; i < DataCenter.gI().ItemTemplate.length; i++) {
            if (DataCenter.gI().ItemTemplate[i].name.equals(name)
                    && DataCenter.gI().ItemTemplate[i].detail.equals(detail)) {
                return new Item(i, false, 1);
            }
        }
        return null;
    }

    public short id = 0;

    public boolean isLock;

    public long expiry = -1L;

    public byte he = -1;

    public byte level;

    public int index;

    public String strOptions = "";

    public int amount;
    public List<GraftCaiTrang> graftCaiTrang = new ArrayList<GraftCaiTrang>();
    @JsonIgnore
    public int TYPE_TEMP = -1;
    @JsonIgnore
    public String strOptionsMain = "";
    @JsonIgnore
    public IActionItem[] arrayAction;

    @JsonIgnore
    public boolean isVuKhi() {
        return this.getItemTemplate().type == 1;
    }

    @JsonIgnore
    public boolean isTrangBi() {
        return this.getItemTemplate().type == 0 || this.getItemTemplate().type == 2 || this.getItemTemplate().type == 4
                || this.getItemTemplate().type == 6 || this.getItemTemplate().type == 8;
    }

    @JsonIgnore
    public boolean isPhuKien() {
        return this.getItemTemplate().type == 3 || this.getItemTemplate().type == 5 || this.getItemTemplate().type == 7
                || this.getItemTemplate().type == 9;
    }

    public Object clone() {
        return this.cloneItem();
    }

    public Item cloneItem() {
        try {
            return (Item) super.clone();
        } catch (Exception e) {
            Utlis.logError(Item.class, e, "Da say ra loi:\n" + e.getMessage());
            return null;
        }
    }

    public Item(int id) {
        this.id = (short) id;
        this.amount = 1;
        if (this.isTypeTrangBi() && this.getItemTemplate().type != 14) {
            he = (byte) Utlis.nextInt(1, 5);
        }
    }

    public Item() {
        this.amount = 1;
        System.out.println("[DEBUG] Item constructor called - id: " + this.id);

        // Check DataCenter status
        if (DataCenter.gI() == null) {
            System.out.println("[DEBUG] DataCenter.gI() is NULL");
            return;
        }

        if (DataCenter.gI().ItemTemplate == null) {
            System.out.println("[DEBUG] ItemTemplate array is NULL");
            return;
        }

        System.out.println("[DEBUG] ItemTemplate array length: " + DataCenter.gI().ItemTemplate.length);

        // Thêm log trước khi gọi isTypeTrangBi
        System.out.println("[DEBUG] About to call isTypeTrangBi()");

        if (this.isTypeTrangBi() && this.getItemTemplate().type != 14) {
            he = (byte) Utlis.nextInt(1, 5);
            System.out.println("[DEBUG] Set he value: " + he);
        }
    }

    public Item(int id, boolean lock) {
        this.id = (short) id;
        this.isLock = lock;
        this.amount = 1;
        if (this.isTypeTrangBi() && this.getItemTemplate().type != 14) {
            he = (byte) Utlis.nextInt(1, 5);
        }
    }

    public Item(int id, boolean lock, int amount) {
        this.id = (short) id;
        this.isLock = lock;
        this.amount = amount;
        if (this.isTypeTrangBi() && this.getItemTemplate().type != 14) {
            he = (byte) Utlis.nextInt(1, 5);
        }
    }

    public Item(int id, boolean lock, int amount, int level) {
        this.id = (short) id;
        this.isLock = lock;
        this.amount = amount;
        this.level = (byte) level;
        if (this.isTypeTrangBi() && this.getItemTemplate().type != 14) {
            he = (byte) Utlis.nextInt(1, 5);
        }
    }

    public Item(int id, boolean lock, int amount, int level, String strOptions) {
        this.id = (short) id;
        this.isLock = lock;
        this.amount = amount;
        this.level = (byte) level;
        if (this.isTypeTrangBi() && this.getItemTemplate().type != 14) {
            he = (byte) Utlis.nextInt(1, 5);
        }
        this.strOptions = strOptions;
        this.strOptionsMain = strOptions;
    }

    @JsonIgnore
    public ItemTemplate getItemTemplate() {
        System.out.println("[DEBUG] getItemTemplate called - id: " + this.id);

        if (DataCenter.gI() == null) {
            System.out.println("[DEBUG] DataCenter.gI() is NULL in getItemTemplate");
            return null;
        }

        if (DataCenter.gI().ItemTemplate == null) {
            System.out.println("[DEBUG] ItemTemplate array is NULL in getItemTemplate");
            return null;
        }

        if (this.id < 0) {
            System.out.println("[DEBUG] Item id is negative: " + this.id);
            return null;
        }

        if (this.id >= DataCenter.gI().ItemTemplate.length) {
            System.out.println(
                    "[DEBUG] Item id out of bounds: " + this.id + " >= " + DataCenter.gI().ItemTemplate.length);
            return null;
        }

        ItemTemplate template = DataCenter.gI().ItemTemplate[id];
        System.out.println("[DEBUG] ItemTemplate found: " + (template != null ? "YES" : "NO"));
        return template;
    }

    @JsonIgnore
    public boolean isTypeTrangBi() {
        System.out.println("[DEBUG] isTypeTrangBi called - id: " + this.id);

        // Thêm log ngay đầu để bắt lỗi
        try {
            if (DataCenter.gI() == null) {
                System.out.println("[DEBUG] DataCenter.gI() is NULL in isTypeTrangBi");
                return false;
            }

            if (DataCenter.gI().DataTypeItemBody == null) {
                System.out.println("[DEBUG] DataTypeItemBody array is NULL in isTypeTrangBi");
                return false;
            }

            System.out.println("[DEBUG] DataTypeItemBody array length: " + DataCenter.gI().DataTypeItemBody.length);

            ItemTemplate template = getItemTemplate();
            if (template == null) {
                System.out.println("[DEBUG] ItemTemplate is NULL in isTypeTrangBi");
                return false;
            }

            System.out.println("[DEBUG] ItemTemplate type: " + template.type);

            for (int var1 = 0; var1 < DataCenter.gI().DataTypeItemBody.length; ++var1) {
                if (DataCenter.gI().DataTypeItemBody[var1].type == template.type) {
                    System.out.println("[DEBUG] Found matching type at index: " + var1);
                    return true;
                }
            }

            System.out.println("[DEBUG] No matching type found");
            return false;
        } catch (Exception e) {
            System.out.println("[DEBUG] Exception in isTypeTrangBi: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public int ngocUpgrade() {
        if (getItemTemplate().type == 2 || getItemTemplate().type == 7 || getItemTemplate().type == 8) {
            return 565;
        } else if (getItemTemplate().type == 5 || getItemTemplate().type == 6 || getItemTemplate().type == 9) {
            return 563;
        } else if (getItemTemplate().type == 0 || getItemTemplate().type == 3 || getItemTemplate().type == 4) {
            return 567;
        } else if (getItemTemplate().type == 1) {
            return 353;
        }
        return -1;
    }

    public boolean W() {
        ItemOption[] var1;
        if ((var1 = this.getItemOption()) != null) {
            for (int var2 = 0; var2 < var1.length; ++var2) {
                if (var1[var2].j()) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean X() {
        ItemOption[] var1;
        if ((var1 = this.getItemOption()) != null) {
            for (int var2 = 0; var2 < var1.length; ++var2) {
                if (var1[var2].k()) {
                    return true;
                }
            }
        }

        return false;
    }

    @JsonIgnore
    public boolean isItemTrangBi() {
        return this.isVuKhi() || this.isTrangBi() || this.isPhuKien();
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        // if (amount <= 0) {
        // amount = 1;
        // }

        this.amount = amount;
    }

    public void read(Message var1) throws IOException {
        this.id = var1.readShort();
        if (this.id >= 0) {
            this.isLock = var1.readBoolean();
            this.expiry = var1.readLong();
            if (this.isTypeTrangBi()) {
                this.he = var1.readByte();
                this.level = var1.readByte();
                this.strOptions = var1.reader.readUTF();
            } else {
                this.setAmount(var1.readInt());
            }

            if (this.getItemTemplate().type == 99) {
                this.strOptions = var1.reader.readUTF();
            }

            this.index = var1.readShort();
        }
    }

    public void write(Writer var1) throws IOException {
        var1.writeShort(id);
        if (id >= 0) {
            var1.writeBoolean(isLock);
            var1.writeLong(expiry);
            if (this.isTypeTrangBi()) {
                var1.writeByte(this.he);
                var1.writeByte(this.level);
                var1.writeUTF(strOptions);
            } else {
                var1.writeInt(this.getAmount());
            }
            if (this.getItemTemplate().type == 99) {
                var1.writeUTF(strOptions);
            }
            var1.writeShort(index);

        }
    }

    @JsonIgnore
    public ItemOption[] getItemOption() {
        if (strOptions != null && strOptions.length() > 0) {
            String[] var1;
            ItemOption[] itemOption = new ItemOption[(var1 = Utlis.split(strOptions, ";")).length];

            for (int var3 = 0; var3 < var1.length; ++var3) {
                try {
                    itemOption[var3] = new ItemOption(var1[var3]);
                } catch (Exception ex) {

                }
            }

            return itemOption;
        } else {
            return new ItemOption[0];
        }
    }

    public static ItemOption[] getItemOptionFormStrOptions(String strOptions) {
        if (strOptions != null && strOptions.length() > 0) {
            String[] var1;
            ItemOption[] itemOption = new ItemOption[(var1 = Utlis.split(strOptions, ";")).length];

            for (int var3 = 0; var3 < var1.length; ++var3) {
                try {
                    itemOption[var3] = new ItemOption(var1[var3]);
                } catch (Exception ex) {

                }
            }

            return itemOption;
        } else {
            return new ItemOption[0];
        }
    }

    public void addItemOption(ItemOption itemOption) {
        if (itemOption == null) {
            return;
        }
        ItemOption[] dataOld = getItemOptionFormStrOptions(strOptions);
        ItemOption[] data = new ItemOption[dataOld.length + 1];
        for (int i = 0; i < dataOld.length; i++) {
            data[i] = dataOld[i];
        }
        data[data.length - 1] = itemOption;
        strOptions = getStrOptionsFormItemOption(data);
    }

    public void addItemOptionIndex0(ItemOption itemOption) {
        if (itemOption == null) {
            return;
        }
        ItemOption[] dataOld = getItemOptionFormStrOptions(strOptions);
        ItemOption[] data = new ItemOption[dataOld.length + 1];

        // Dịch chuyển tất cả các phần tử sang phải một vị trí
        for (int i = dataOld.length - 1; i >= 0; i--) {
            data[i + 1] = dataOld[i];
        }

        // Đặt itemOption mới vào vị trí đầu tiên
        data[0] = itemOption;
        strOptions = getStrOptionsFormItemOption(data);
    }

    public static String getStrOptionsFormItemOption(ItemOption[] itemOption) {
        String var1 = "";
        if (itemOption != null) {
            for (int var2 = 0; var2 < itemOption.length; ++var2) {
                if (itemOption[var2] != null) {
                    var1 = var1 + itemOption[var2].g();
                    if (var2 < itemOption.length - 1) {
                        var1 = var1 + ";";
                    }
                }

            }
        }

        return var1;
    }

    public void removeItemOption(ItemOption itemoption) {
        if (itemoption == null) {
            return;
        }
        ItemOption[] items = getItemOption();
        for (int i = 0; i < items.length; i++) {
            try {
                if (items[i].a[0] == itemoption.a[0]) {
                    items[i] = null;
                }
            } catch (Exception x) {

            }

        }
        String var1 = "";
        if (items != null) {
            for (int var2 = 0; var2 < items.length; ++var2) {
                if (items[var2] != null) {
                    var1 = var1 + items[var2].g();
                    if (var2 < items.length - 1) {
                        var1 = var1 + ";";
                    }
                }
            }
        }
        strOptions = var1;

    }

    public void removeItemOption(int id, int sl, int sttmine) {
        ItemOption[] items = getItemOption();
        List<ItemOption> itemList = new ArrayList<ItemOption>(Arrays.asList(items)); // Chuyển mảng thành danh sách để
                                                                                     // dễ dàng thao tác

        Iterator<ItemOption> iterator = itemList.iterator();
        while (iterator.hasNext()) {
            ItemOption item = iterator.next();
            try {
                if (item.a[0] == id) {
                    if (sl > 0 && item.a[sttmine] > 0) {
                        item.a[sttmine] -= sl;
                    } else {
                        iterator.remove(); // Xóa phần tử khỏi danh sách nếu sl <= 0
                    }
                }
            } catch (Exception x) {
                // Xử lý hoặc ghi lại ngoại lệ cụ thể tại đây
            }
        }

        // Chuyển đổi danh sách trở lại thành một mảng để cập nhật
        this.strOptions = getStrOptionsFormItemOption(itemList.toArray(new ItemOption[0]));
    }

    public void plusOption(int id, int stt, int sl) {
        ItemOption[] items = getItemOption();
        List<ItemOption> itemList = new ArrayList<ItemOption>(Arrays.asList(items)); // Chuyển mảng thành danh sách để
                                                                                     // dễ dàng thao tác

        Iterator<ItemOption> iterator = itemList.iterator();
        while (iterator.hasNext()) {
            ItemOption item = iterator.next();
            try {
                if (item.a[0] == id) {
                    if (item.a.length >= stt) {
                        item.a[stt] += sl;
                    }
                }
            } catch (Exception x) {
                // Xử lý hoặc ghi lại ngoại lệ cụ thể tại đây
            }
        }

        this.strOptions = getStrOptionsFormItemOption(itemList.toArray(new ItemOption[0]));
    }

    public void plusOptionByIndex(int index, int stt, int sl) {
        ItemOption[] items = getItemOption();
        if (items[index] != null) {
            items[index].a[stt] += sl;
        }
        this.strOptions = getStrOptionsFormItemOption(items);
    }

    public void setOptionViThu(int level) {
        ItemOption[] items = getItemOption();
        for (int i = 0; i < items.length; i++) {
            try {
                if (items[i].a[0] != 305) {
                    if (items[i].a[0] == 0 || items[i].a[0] == 1 || items[i].a[0] == 2 || items[i].a[0] == 180) {
                        items[i].a[1] += items[i].a[1] * level / 100;
                    } else if (items[i].a[0] == 166 || items[i].a[0] == 151 || items[i].a[0] == 209) {
                        items[i].a[1] += level;
                    }
                }
            } catch (Exception x) {

            }

        }
        this.strOptions = getStrOptionsFormItemOption(items);
    }

    public void mainOptionViThu() {

        this.strOptions = this.strOptionsMain;
    }

    public void createItemOptions() {

        ItemOption[] array = getItemOptionFormStrOptions(this.strOptions);
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] != null) {
                    try {
                        if (array[i].canCreateItemOption()) {
                            int[] arrayChiSo = new int[2];
                            arrayChiSo[0] = array[i].a[0];
                            arrayChiSo[1] = Utlis.nextInt(array[i].a[1], array[i].f());
                            array[i].a = arrayChiSo;
                        }
                    } catch (Exception ex) {

                    }
                }
            }
        }
        // if (this.level > 0) {
        // for (int index = 0; index < this.level; index++) {
        // if (array != null) {
        // for (int i = 0; i < array.length; i++) {
        // if (array[i] != null) {
        // try {
        // array[i].a[1] += getChiSoMaxFormStrOptionMain(array[i].a[0]);
        // } catch (Exception ex) {
        //
        // }
        // }
        // }
        // }
        // }
        // }
        this.strOptions = getStrOptionsFormItemOption(array);
        this.strOptionsMain = getStrOptionsFormItemOption(array);
        // int lv = level;
        // this.level = 0;
        // this.a(lv);
    }

    @JsonIgnore
    public boolean isViThu() {
        return this.expiry == -1L && this.id >= 476 && this.id <= 484;
    }

    public void a(int var1) {
        ItemOption[] var2;
        if ((var2 = this.getItemOption()) != null) {
            int var3;
            int var4;
            int[] var5;
            if (var1 >= this.level) {
                for (var3 = this.level + 1; var3 <= var1; ++var3) {
                    for (var4 = 0; var4 < var2.length; ++var4) {
                        if (var2[var4].getItemOptionTemplate().type != 8) {
                            var5 = var2[var4].getItemOptionTemplate().a();
                            if (var3 <= var5.length) {
                                var2[var4].d(var5[var3 - 1]);
                            }
                        }
                    }
                }
            } else {
                for (var3 = this.level; var3 > var1; --var3) {
                    for (var4 = 0; var4 < var2.length; ++var4) {
                        if (var2[var4].getItemOptionTemplate().type != 8) {
                            var5 = var2[var4].getItemOptionTemplate().a();
                            if (var3 <= var5.length) {
                                var2[var4].d(-var5[var3 - 1]);
                            }
                        }
                    }
                }
            }
        }

        this.level = (byte) var1;
        this.strOptions = getStrOptionsFormItemOption(var2);
    }

    public static String a(List<ItemOption> var0) {
        ItemOption[] var1 = new ItemOption[var0.size()];

        for (int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2] = (ItemOption) var0.get(var2);
        }

        return a(var1);
    }

    public void b(Vector var1) {
        switch (this.getItemTemplate().type) {

            case 6:
                var1.add(new ItemOption("323,200,-1"));
                return;
            case 7:
                var1.add(new ItemOption("324,250,-1"));
            default:
                return;
            case 8:
                var1.add(new ItemOption("304,300,-1"));
                return;
            case 9:
                var1.add(new ItemOption("310,250,-1"));
        }
    }

    public void lucdao(Vector var1) {
        switch (this.getItemTemplate().type) {
            case 0:
                var1.add(new ItemOption("304,200,-1"));
                return;
            case 2:
                var1.add(new ItemOption("323,150,-1"));
                return;
            case 3:
                var1.add(new ItemOption("304,200,-1"));
                return;
            case 6:
                var1.add(new ItemOption("323,200,-1"));
                return;
            case 7:
                var1.add(new ItemOption("324,250,-1"));
            default:
                return;
            case 8:
                var1.add(new ItemOption("304,300,-1"));
                return;
            case 9:
                var1.add(new ItemOption("310,250,-1"));
        }
    }

    public static String a(ItemOption[] var0) {
        String var1 = "";
        if (var0 != null) {
            for (int var2 = 0; var2 < var0.length; ++var2) {
                var1 = var1 + var0[var2].g();
                if (var2 < var0.length - 1) {
                    var1 = var1 + ";";
                }
            }
        }

        return var1;
    }

    public int getChiSoMaxFormStrOptionMain(int id) {
        ItemOption[] array = getItemOptionFormStrOptions(this.strOptionsMain);
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] != null) {
                    try {
                        if (array[i].canCreateItemOption() && array[i].a[0] == id) {
                            return array[i].f();
                        }
                    } catch (Exception ex) {

                    }
                }
            }
        }
        return 0;
    }

    @JsonIgnore
    public int getOptionKham() {
        short var1 = -1;
        switch (this.id) {
            case 406:
                var1 = 199;
                break;
            case 407:
                var1 = 200;
                break;
            case 408:
                var1 = 201;
                break;
            case 409:
                var1 = 202;
                break;
            case 410:
                var1 = 203;
                break;
            case 411:
                var1 = 204;
                break;
            case 412:
                var1 = 205;
                break;
            case 413:
                var1 = 206;
                break;
            case 826:
                var1 = 344;
                break;
            case 827:
                var1 = 345;
        }
        return var1;

    }

    @JsonIgnore
    public int getTinhThach() {
        if (this.id >= 125 && this.id <= 133 || this.id == 535) {
            short var1 = 0;
            switch (this.id) {
                case 125:
                    var1 = 120;
                    break;
                case 126:
                    var1 = 240;
                    break;
                case 127:
                    var1 = 360;
                    break;
                case 128:
                    var1 = 480;
                    break;
                case 129:
                    var1 = 600;
                    break;
                case 130:
                    var1 = 720;
                    break;
                case 131:
                    var1 = 800;
                    break;
                case 132:
                    var1 = 1000;
                    break;
                case 133:
                    var1 = 1200;
                    break;
                case 535:
                    var1 = 1600;
            }

            long var2;
            int var4;
            if ((var2 = (this.expiry - Utlis.time()) / 86400000L) > 0L && (var4 = (int) (30L - var2) * var1 / 30) > 0
                    && var4 < var1) {
                return var1 - var4;
            }
        }

        if (this.getItemTemplate().type == 10 && this.expiry == -1L) {
            try {
                return Integer.parseInt(this.getItemTemplate().detail);
            } catch (Exception var7) {
                return 0;
            }
        } else {
            int var10 = 0;
            int var8 = 0;
            int var3 = 0;
            ItemOption[] var9;
            if ((var9 = this.L()) != null) {
                float var5 = 1.0F;

                for (int var6 = 0; var6 < var9.length; ++var6) {
                    if (var9[var6].getItemOptionTemplate().type <= 0) {
                        ++var10;
                    } else if (var9[var6].getItemOptionTemplate().type == 2) {
                        ++var8;
                    } else if (var9[var6].getItemOptionTemplate().type > 2
                            && var9[var6].getItemOptionTemplate().type <= 7) {
                        ++var3;
                    }

                    if (var9[var6].getItemOptionTemplate().id == 148) {
                        var5 = 1.2F;
                    } else if (var9[var6].getItemOptionTemplate().type == 9) {
                        var5 = 1.4F;
                    }
                }

                if (var10 >= 2 && var8 >= 2) {
                    byte var11 = 2;
                    if (this.getItemTemplate().levelNeed >= 50) {
                        var11 = 5;
                    } else if (this.getItemTemplate().levelNeed >= 40) {
                        var11 = 4;
                    } else if (this.getItemTemplate().levelNeed >= 30) {
                        var11 = 3;
                    } else if (this.getItemTemplate().levelNeed >= 20) {
                        var11 = 2;
                    } else if (this.getItemTemplate().levelNeed >= 10) {
                        var11 = 1;
                    }

                    if (var3 >= var11) {
                        if (this.getItemTemplate().levelNeed < 20) {
                            return (int) (1.0F * var5);
                        }

                        if (this.getItemTemplate().levelNeed < 30) {
                            return (int) (3.0F * var5);
                        }

                        return (int) ((float) (var10 + var8 + var3) * var5);
                    }
                }
            }

            return 0;
        }
    }

    public ItemOption[] L() {
        if (this.strOptions != null && this.strOptions.length() > 0) {
            String[] var1;
            ItemOption[] var2 = new ItemOption[(var1 = Utlis.split(this.strOptions, ";")).length];

            for (int var3 = 0; var3 < var1.length; ++var3) {
                var2[var3] = new ItemOption(var1[var3]);
            }

            return var2;
        } else {
            return null;
        }
    }

    public int getDiemChiSo(Client client, int... arrChiSo) {
        int c = 0;
        ItemOption[] array = getItemOption();
        if (arrChiSo.length > 0) {
            for (int j = 0; j < arrChiSo.length; j++) {
                if (array != null) {
                    for (int i = 0; i < array.length; i++) {
                        if (array[i] != null) {
                            try {
                                if (array[i].a[0] == arrChiSo[j]) {
                                    if (this.checkItemCanAddItem(client, array[i])) {
                                        c += 1;
                                    }
                                }
                            } catch (Exception ex) {

                            }
                        }
                    }
                }
            }
        }
        return c;
    }

    public int getChiSo(Client client, int... arrChiSo) {
        int c = 0;
        ItemOption[] array = getItemOption();
        if (arrChiSo.length == 0) {
            for (int i = 0; array != null && i < array.length; i++) {
                if (array[i] != null) {
                    try {
                        if (this.checkItemCanAddItem(client, array[i])) {
                            c += array[i].a[1];
                        }
                    } catch (Exception ex) {

                    }
                }
            }
        } else {
            for (int j = 0; j < arrChiSo.length; j++) {
                if (array != null) {
                    for (int i = 0; i < array.length; i++) {
                        if (array[i] != null) {
                            try {
                                if (array[i].a[0] == arrChiSo[j]) {
                                    if (this.checkItemCanAddItem(client, array[i])) {
                                        c += array[i].a[1];
                                    }
                                }
                            } catch (Exception ex) {

                            }
                        }
                    }
                }
            }
        }
        return c;
    }

    public int getChiSo(int stt, Client client, int... arrChiSo) {
        int c = 0;
        ItemOption[] array = getItemOption();
        if (arrChiSo.length == 0) {
            for (int i = 0; array != null && i < array.length; i++) {
                if (array[i] != null) {
                    try {
                        if (this.checkItemCanAddItem(client, array[i])) {
                            c += array[i].a[stt];
                        }
                    } catch (Exception ex) {

                    }
                }
            }
        } else {
            for (int j = 0; j < arrChiSo.length; j++) {
                if (array != null) {
                    for (int i = 0; i < array.length; i++) {
                        if (array[i] != null) {
                            try {
                                if (array[i].a[0] == arrChiSo[j]) {
                                    if (this.checkItemCanAddItem(client, array[i])) {
                                        c += array[i].a[stt];
                                    }
                                }
                            } catch (Exception ex) {

                            }
                        }
                    }
                }
            }
        }
        return c;
    }

    public int getChiSoI(int i, int stt, Client client) {
        int c = 0;
        ItemOption[] array = getItemOption();
        if (array != null) {
            if (array[i] != null) {
                if (this.checkItemCanAddItem(client, array[i])) {
                    c += array[i].a[stt];
                }
            }
        }
        return c;
    }

    public int giaBan() {
        ItemOption[] var1 = this.L();
        int var2 = 0;
        if (this.isTypeTrangBi() && var1 != null) {
            var2 = 0 + var1.length;
        }

        if (this.getItemTemplate().type == 34) {
            var2 = 50000;
        }

        return var2;
    }

    @JsonIgnore
    public int getChiSoDame() {
        int dame = 0;
        ItemOption[] array = getItemOption();
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] != null) {
                    try {
                        if (array[i].a[0] == 2 || array[i].a[0] == 146) { // 2, 146
                            dame += array[i].a[1];
                        }
                    } catch (Exception ex) {

                    }
                }
            }
        }
        return dame;
    }

    @JsonIgnore
    public int getChiSoDameMob() {
        int dame = 0;
        ItemOption[] array = getItemOption();
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] != null) {
                    try {
                        if (array[i].a[0] == 3) {
                            dame += array[i].a[1];
                        }
                    } catch (Exception ex) {

                    }
                }
            }
        }
        return dame;
    }

    @JsonIgnore
    public boolean isDaCuongHoa() {
        return this.getItemTemplate().type == 21;
    }

    @JsonIgnore
    public boolean isNgocKham() {
        return this.getItemTemplate().type == 32;
    }

    @JsonIgnore
    public boolean isSet2() {
        return this.getItemTemplate().type == 2 || this.getItemTemplate().type == 8 || this.getItemTemplate().type == 7;
    }

    @JsonIgnore
    public boolean isSet3() {
        return this.getItemTemplate().type == 6 || this.getItemTemplate().type == 5 || this.getItemTemplate().type == 9;
    }

    @JsonIgnore
    public boolean isSet1() {
        return this.getItemTemplate().type == 0 || this.getItemTemplate().type == 4 || this.getItemTemplate().type == 3;
    }

    @JsonIgnore
    public boolean checkKichAn(Client client) {
        if (isSet1()) {
            if (client.mChar.arrItemBody[0] != null && client.mChar.arrItemBody[4] != null
                    && client.mChar.arrItemBody[3] != null) {
                int he1 = client.mChar.arrItemBody[0].he;
                int he2 = client.mChar.arrItemBody[3].he;
                int he3 = client.mChar.arrItemBody[4].he;
                return he1 == he2 && he2 == he3;
            }
        }
        if (isSet2()) {
            if (client.mChar.arrItemBody[2] != null && client.mChar.arrItemBody[8] != null
                    && client.mChar.arrItemBody[7] != null) {
                int he1 = client.mChar.arrItemBody[2].he;
                int he2 = client.mChar.arrItemBody[8].he;
                int he3 = client.mChar.arrItemBody[7].he;
                return he1 == he2 && he2 == he3;
            }
        }
        if (isSet3()) {
            if (client.mChar.arrItemBody[6] != null && client.mChar.arrItemBody[5] != null
                    && client.mChar.arrItemBody[9] != null) {
                int he1 = client.mChar.arrItemBody[6].he;
                int he2 = client.mChar.arrItemBody[5].he;
                int he3 = client.mChar.arrItemBody[9].he;
                return he1 == he2 && he2 == he3;
            }
        }
        if (this.getItemTemplate().type == 1) {
            if (client.mChar.arrItemBody[1] != null) {
                return client.mChar.infoChar.idhe == client.mChar.arrItemBody[1].he
                        || client.mChar.arrItemBody[15] != null
                                && client.mChar.arrItemBody[15].he == client.mChar.arrItemBody[1].he;

            }
        }
        return false;
    }

    public boolean checkItemCanAddItem(Client client, ItemOptionTemplate itemOption) {
        switch (itemOption.type) {
            case 3:
                return this.level >= 4;
            case 4:
                return this.level >= 8;
            case 5:
                return this.level >= 12;
            case 6:
                return this.level >= 14;
            case 7:
                return this.level >= 16;
            case 10:
                return this.level >= 17;
            case 11:
                return this.level >= 18;
            case 19:
                return this.level >= 16;
            case 2:
                return checkKichAn(client);
        }
        return true;
    }

    private boolean checkItemCanAddItem(Client client, ItemOption itemOption) {
        switch (itemOption.getItemOptionTemplate().type) {
            case 3:
                return this.level >= 4;
            case 4:
                return this.level >= 8;
            case 5:
                return this.level >= 12;
            case 6:
                return this.level >= 14;
            case 7:
                return this.level >= 16;
            case 10:
                return this.level >= 17;
            case 11:
                return this.level >= 18;
            case 19:
                return this.level >= 16;
            case 2:
                return checkKichAn(client);
        }
        return true;
    }

    public static class LangLa_gp {

        public String a;
        public int b;
        public int c;

        @Override
        public String toString() {
            return a;
        }

        public LangLa_gp(String var1, int var2, int var3) {
            this.a = var1;
            this.b = var2;
            this.c = var3;
        }
    }

    private static String a(int var0, int var1) {
        try {
            switch (var0) {
                case 0:
                    return Caption.rq[3] + " (" + Caption.ro[var1 - 1] + "), " + Caption.rq[4] + " ("
                            + Caption.ro[var1 - 1] + ")";
                case 1:
                    return Caption.aq + " (" + Caption.ro[var1 - 1] + ") " + Caption.aw + " (" + Caption.ro[var1 - 1]
                            + ")";
                case 2:
                    return Caption.rq[7] + " (" + Caption.ro[var1 - 1] + "), " + Caption.rq[8] + " ("
                            + Caption.ro[var1 - 1] + ")";
                case 3:
                    return Caption.rq[0] + " (" + Caption.ro[var1 - 1] + "), " + Caption.rq[4] + " ("
                            + Caption.ro[var1 - 1] + ")";
                case 4:
                    return Caption.rq[0] + " (" + Caption.ro[var1 - 1] + "), " + Caption.rq[3] + " ("
                            + Caption.ro[var1 - 1] + ")";
                case 5:
                    return Caption.rq[6] + " (" + Caption.ro[var1 - 1] + "), " + Caption.rq[9] + " ("
                            + Caption.ro[var1 - 1] + ")";
                case 6:
                    return Caption.rq[5] + " (" + Caption.ro[var1 - 1] + "), " + Caption.rq[9] + " ("
                            + Caption.ro[var1 - 1] + ")";
                case 7:
                    return Caption.rq[2] + " (" + Caption.ro[var1 - 1] + "), " + Caption.rq[8] + " ("
                            + Caption.ro[var1 - 1] + ")";
                case 8:
                    return Caption.rq[2] + " (" + Caption.ro[var1 - 1] + "), " + Caption.rq[7] + " ("
                            + Caption.ro[var1 - 1] + ")";
                case 9:
                    return Caption.rq[6] + " (" + Caption.ro[var1 - 1] + "), " + Caption.rq[5] + " ("
                            + Caption.ro[var1 - 1] + ")";
            }
        } catch (Exception e) {
            Utlis.logError(Item.class, e, "Da say ra loi:\n" + e.getMessage());
        }

        return "";
    }

    public static Vector getTextVec(Item var5) {
        Vector h = new Vector();
        ItemOption[] var45 = getItemOptionFormStrOptions(var5.strOptions);
        int var1 = 1;
        if (var45 != null) {
            int var3 = 0;

            boolean var21 = false;
            boolean var6 = false;

            for (int var7 = 0; var7 < var45.length; ++var7) {
                if ((var45[var7].getItemOptionTemplate().type < 3 || var45[var7].getItemOptionTemplate().type > 7)
                        && var45[var7].getItemOptionTemplate().type != 10
                        && var45[var7].getItemOptionTemplate().type != 11
                        && var45[var7].getItemOptionTemplate().type != 15
                        && var45[var7].getItemOptionTemplate().type != 16) {
                    if (var45[var7].getItemOptionTemplate().type == 2) {
                        if (!var6) {
                            var6 = true;
                            h.addElement(
                                    new LangLa_gp(Caption.fM + a(var5.getItemTemplate().type, var5.he), -1, -11184811));
                        }

                        if (var3 > 0) {
                            h.addElement(new LangLa_gp(var45[var7].b(), -7812062, -16777216));
                            --var3;
                        } else {
                            h.addElement(new LangLa_gp(var45[var7].b(), -7631732, -16777216));
                        }
                    } else if (var45[var7].getItemOptionTemplate().type == 8) {
                        h.addElement(new LangLa_gp("(" + DataCenter.gI().ItemTemplate[var45[var7].h()].name + " "
                                + Caption.dn + " " + var45[var7].i() + ") " + var45[var7].b(), -7340813, -16777216));
                    } else if (var45[var7].getItemOptionTemplate().type == 14) {
                        h.addElement(new LangLa_gp(var1 + ". " + var45[var7].b(), -16742145, -16777216));
                        ++var1;
                    } else if (var45[var7].getItemOptionTemplate().id >= 53
                            && var45[var7].getItemOptionTemplate().id <= 62) {
                        h.addElement(new LangLa_gp(var45[var7].c(), -10831436, -16777216));
                    } else if (var45[var7].getItemOptionTemplate().id != 128
                            && var45[var7].getItemOptionTemplate().id != 305) {
                        if (var45[var7].getItemOptionTemplate().id == 336) {
                            h.addElement(new LangLa_gp(var45[var7].c(), -623877, -16777216));
                        } else if (var45[var7].getItemOptionTemplate().id == 337) {
                            h.addElement(new LangLa_gp(
                                    var45[var7].b() + Utlis.numberFormat((var45[var7 - 1].e() + 1) * 5000000), -2560,
                                    -16777216));
                        } else if (var45[var7].d() == 105) {
                            Vector var10000 = h;
                            StringBuilder var10003 = (new StringBuilder()).append(Caption.fN).append(LangLa_hg.b());
                            int[] var10005 = var45[var7].a;
                            var10000.addElement(new LangLa_gp(
                                    var10003.append(DataCenter.gI().MapTemplate[var10005[1]].name).toString(), -1,
                                    -16777216));
                            h.addElement(new LangLa_gp(Caption.fO, -1, -16777216));
                        } else if (var45[var7].d() == 148) {
                            h.addElement(new LangLa_gp(var45[var7].b(), -3407617, -16777216));
                        } else if (var45[var7].d() == 159) {
                            h.addElement(new LangLa_gp(var45[var7].b(), -196483, -16777216));
                        } else if (var45[var7].d() == 163) {
                            h.addElement(new LangLa_gp(var45[var7].b(), -48128, -16777216));
                        } else if (var45[var7].d() != 164 && var45[var7].getItemOptionTemplate().id != 340) {
                            if (var45[var7].d() == 165) {
                                h.addElement(new LangLa_gp(var45[var7].b(), -16712186, -16777216));
                            } else if (var45[var7].d() == 361) {
                                h.addElement(new LangLa_gp(var45[var7].b(), -13176412, -16777216));
                            } else {
                                h.addElement(new LangLa_gp(var45[var7].b(), -10831436, -16777216));
                            }
                        } else {
                            h.addElement(new LangLa_gp(var45[var7].b(), -4588032, -16777216));
                        }
                    } else {
                        h.addElement(new LangLa_gp(var45[var7].c(), -2560, -16777216));
                    }
                } else {
                    if (!var21) {
                        var21 = true;
                        if (var5.getItemTemplate().type == 11) {
                            h.addElement(new LangLa_gp(Caption.fK, -1, -11184811));
                        } else {
                            h.addElement(new LangLa_gp(Caption.fL, -1, -11184811));
                        }
                    }

                    if ((var45[var7].getItemOptionTemplate().type != 15 || var5.level < 2)
                            && (var45[var7].getItemOptionTemplate().type != 3 || var5.level < 4)
                            && (var45[var7].getItemOptionTemplate().type != 4 || var5.level < 8)
                            && (var45[var7].getItemOptionTemplate().type != 5 || var5.level < 12)
                            && (var45[var7].getItemOptionTemplate().type != 6 || var5.level < 14)
                            && (var45[var7].getItemOptionTemplate().type != 7 || var5.level < 16)
                            && (var45[var7].getItemOptionTemplate().type != 10 || var5.level < 17)
                            && (var45[var7].getItemOptionTemplate().type != 11 || var5.level < 18)
                            && (var45[var7].getItemOptionTemplate().type != 16 || var5.level < 19)) {
                        h.addElement(new LangLa_gp(var45[var7].b(), -7631732, -16777216));
                    } else {
                        h.addElement(new LangLa_gp(var45[var7].b(), -2560, -16777216));
                    }
                }
            }
        }
        return h;
    }

    public void setKhamNgoc(int var1, int var2) {
        ItemOption[] var3;
        if ((var3 = this.L()) == null) {
        } else {
            int var4 = ItemOption.f(var2);

            int var6 = this.Y();

            for (int var7 = 0; var7 < var3.length; ++var7) {
                if (var3[var7].getItemOptionTemplate().type == 8 && var3[var7].a[0] == var4) {
                    int var8 = var3[var7].i();

                    int[] var9 = var3[var7].getItemOptionTemplate().a();

                    for (int var10 = var8 + 1; var10 <= var9.length; ++var10) {
                        if (var1 >= DataCenter.gI().ngocKhamUpgrade[var10] && var10 <= var6) {
                            var1 -= DataCenter.gI().ngocKhamUpgrade[var10];
                            var3[var7].d(var9[var10 - 1]);
                            var3[var7].a[3] = var10;
                        }
                    }
                }
            }
            this.strOptions = a(var3);
        }
    }

    public int Y() {
        ItemOption[] var1;
        if ((var1 = this.L()) != null) {
            for (int var2 = 0; var2 < var1.length; ++var2) {
                if (var1[var2].k()) {
                    return 18;
                }

                if (var1[var2].j()) {
                    return 17;
                }
            }
        }
        return 16;
    }
}
