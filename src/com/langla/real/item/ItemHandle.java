package com.langla.real.item;

import com.langla.data.*;
import com.langla.lib.Utlis;
import com.langla.real.map.Map;
import com.langla.real.other.DanhHieu;
import com.langla.real.other.Effect;
import com.langla.real.player.Char;
import com.langla.real.task.TaskHandler;
import com.langla.utlis.UTPKoolVN;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author PKoolVN
 **/
public class ItemHandle {
    public static void useItem(Char character, short index) {
        try {
            if (index < 0 || index >= character.arrItemBag.length || character.getItemBagByIndex(index) == null) {
                return;
            }
            Item item = character.getItemBagByIndex(index);
            ItemTemplate itemTemplate = item.getItemTemplate();
            Item itemAdd;
            if (itemTemplate.levelNeed > character.level() || (itemTemplate.gioiTinh != 2 && itemTemplate.gioiTinh != character.infoChar.gioiTinh) || (itemTemplate.idClass != 0 && itemTemplate.idClass != character.infoChar.idClass)) {
                character.client.session.serivce.ShowMessGold("Không thể sử dụng trang bị này.");
                return;
            }
            TaskHandler.gI().checkDoneUseItem(character, item);
            if (item.isTypeTrangBi()) {
                item.isLock = true;
                character.msgUseItem(item);
                Item var6;
                if ((var6 = character.arrItemBody[item.getItemTemplate().type]) != null) {
                    var6.index = item.index;
                    character.arrItemBag[index] = var6;
                } else {
                    character.arrItemBag[index].setAmount(0);
                    character.removeItemBagByIndex(index, "Mặc vào người");
                }
                item.index = item.getItemTemplate().type;
                character.arrItemBody[item.index] = item;
                character.msgUpdateItemBody_Orther();
                character.setUpInfo(true);
            } else if (item.getItemTemplate().type == 24) {
                if(character.info.isBiChoang || character.info.isThanhSatChar) return;
                character.removeItemBag(item, "Sử dụng");
                for (int i = 0; i < DataCenter.gI().EffectTemplate.length; i++) {
                    if (DataCenter.gI().EffectTemplate[i].name.equals(item.getItemTemplate().name)) {
                        character.addEffect(new Effect(i, Effect.getValueEffectFormIdItem(item.id), System.currentTimeMillis(), 30 * (60 * 1000)));
                        return;
                    }
                }
            } else if (item.getItemTemplate().type == 22 || item.getItemTemplate().type == 23) {
                if(character.info.isBiChoang || character.info.isThanhSatChar) return;
                character.removeItemBag(item, "Sử dụng");
                for (int i = 0; i < DataCenter.gI().EffectTemplate.length; i++) {
                    if (DataCenter.gI().EffectTemplate[i].name.equals(item.getItemTemplate().name)) {
                        character.addEffect(new Effect(i, Effect.getValueEffectFormIdItem(item.id), System.currentTimeMillis(), 3 * 1000));
                        return;
                    }
                }
            } else if (item.getItemTemplate().id == 159 || item.getItemTemplate().id == 281 || item.getItemTemplate().id == 347) {
                character.removeItemBag(item, "Sử dụng");
                character.addEffect(new Effect(42, Effect.getValueEffectFormIdItem(item.id), System.currentTimeMillis(), (5 * (60 * (60 * 1000)))));

            } else if(item.getItemTemplate().type == 33){
                switch (item.id){
                    case 417:
                        Item itemNew = new Item(161, item.isLock);
                        character.addItem(itemNew, "Mở rank");
                        character.msgAddItemBag(itemNew);
                        character.removeItemBag(item, "Sử dụng");
                        if(character.infoChar.rank == 0){
                            character.infoChar.rank = 1;
                            character.client.session.serivce.loadRank(character);
                        }

                        if(DataCenter.gI().phucLoiInfo.RankCaoNhat < 1) {
                            DataCenter.gI().phucLoiInfo.RankCaoNhat = 1;
                            DataCenter.gI().updatePhucLoi(1, DataCenter.gI().phucLoiInfo.RankCaoNhat);
                        }
                        break;
                    case 418:

                        itemNew = new Item(277, item.isLock);
                        character.addItem(itemNew, "Mở rank");
                        character.msgAddItemBag(itemNew);

                        character.removeItemBag(item, "Sử dụng");
                        if(character.infoChar.rank < 1){
                            character.infoChar.rank = 2;
                            character.infoChar.exp_rank = 5;
                            character.client.session.serivce.loadRank(character);
                        }
                        if(DataCenter.gI().phucLoiInfo.RankCaoNhat < 2) {
                            DataCenter.gI().phucLoiInfo.RankCaoNhat = 2;
                            DataCenter.gI().updatePhucLoi(1, DataCenter.gI().phucLoiInfo.RankCaoNhat);
                        }
                        break;
                    case 419:

                        itemNew = new Item(266, item.isLock, 5);
                        character.addItem(itemNew, "Mở rank");
                        character.msgAddItemBag(itemNew);


                        character.removeItemBag(item, "Sử dụng");
                        if(character.infoChar.rank < 2){
                            character.infoChar.rank = 3;
                            character.infoChar.exp_rank = 10;
                            character.client.session.serivce.loadRank(character);
                        }
                        if(DataCenter.gI().phucLoiInfo.RankCaoNhat < 3) {
                            DataCenter.gI().phucLoiInfo.RankCaoNhat = 3;
                            DataCenter.gI().updatePhucLoi(1, DataCenter.gI().phucLoiInfo.RankCaoNhat);
                        }
                        break;
                    case 420:

                        itemNew = new Item(347, item.isLock, 5);
                        character.addItem(itemNew, "Mở rank");
                        character.msgAddItemBag(itemNew);


                        character.removeItemBag(item, "Sử dụng");
                        if(character.infoChar.rank < 3){
                            character.infoChar.rank = 4;
                            character.infoChar.exp_rank = 10;

                            character.client.session.serivce.loadRank(character);
                        }
                        if(DataCenter.gI().phucLoiInfo.RankCaoNhat < 4) {
                            DataCenter.gI().phucLoiInfo.RankCaoNhat = 4;
                            DataCenter.gI().updatePhucLoi(1, DataCenter.gI().phucLoiInfo.RankCaoNhat);
                        }
                        break;
                    case 421:


                        itemNew = new Item(7, item.isLock, 1);
                        character.addItem(itemNew, "Mở rank");
                        character.msgAddItemBag(itemNew);

                        character.removeItemBag(item, "Sử dụng");
                        if(character.infoChar.rank < 4){
                            character.infoChar.rank = 5;
                            character.infoChar.exp_rank = 20;
                            character.client.session.serivce.loadRank(character);
                        }
                        if(DataCenter.gI().phucLoiInfo.RankCaoNhat < 5) {
                            DataCenter.gI().phucLoiInfo.RankCaoNhat = 5;
                            DataCenter.gI().updatePhucLoi(1, DataCenter.gI().phucLoiInfo.RankCaoNhat);
                        }
                        break;
                    case 422:


                        itemNew = new Item(277, item.isLock, 15);
                        character.addItem(itemNew, "Mở rank");
                        character.msgAddItemBag(itemNew);


                        character.removeItemBag(item, "Sử dụng");
                        if(character.infoChar.rank < 5){
                            character.infoChar.rank = 6;
                            character.infoChar.exp_rank = 20;
                            character.client.session.serivce.loadRank(character);
                        }
                        if(DataCenter.gI().phucLoiInfo.RankCaoNhat < 6) {
                            DataCenter.gI().phucLoiInfo.RankCaoNhat = 6;
                            DataCenter.gI().updatePhucLoi(1, DataCenter.gI().phucLoiInfo.RankCaoNhat);
                        }
                        break;
                    case 423:


                        itemNew = new Item(161, item.isLock, 10);
                        character.addItem(itemNew, "Mở rank");
                        character.msgAddItemBag(itemNew);


                        character.removeItemBag(item, "Sử dụng");
                        if(character.infoChar.rank < 6){
                            character.infoChar.rank = 7;
                            character.infoChar.exp_rank = 30;
                            character.client.session.serivce.loadRank(character);
                        }
                        if(DataCenter.gI().phucLoiInfo.RankCaoNhat < 7) {
                            DataCenter.gI().phucLoiInfo.RankCaoNhat = 7;
                            DataCenter.gI().updatePhucLoi(1, DataCenter.gI().phucLoiInfo.RankCaoNhat);
                        }
                        break;
                    case 424:

                        itemNew = new Item(152, item.isLock, 1);
                        character.addItem(itemNew, "Mở rank");
                        character.msgAddItemBag(itemNew);


                        character.removeItemBag(item, "Sử dụng");
                        if(character.infoChar.rank < 6){
                            character.infoChar.rank = 7;
                            character.infoChar.exp_rank = 30;
                            character.client.session.serivce.loadRank(character);
                        }
                        character.removeItemBag(item, "Sử dụng");
                        if(character.infoChar.rank < 7){
                            character.infoChar.rank = 8;
                            character.infoChar.exp_rank = 30;
                            character.client.session.serivce.loadRank(character);
                        }
                        if(DataCenter.gI().phucLoiInfo.RankCaoNhat < 8) {
                            DataCenter.gI().phucLoiInfo.RankCaoNhat = 8;
                            DataCenter.gI().updatePhucLoi(1, DataCenter.gI().phucLoiInfo.RankCaoNhat);
                        }
                        break;
                    case 425:

                        itemNew = new Item(155, item.isLock, 1);
                        character.addItem(itemNew, "Mở rank");
                        character.msgAddItemBag(itemNew);

                        character.removeItemBag(item, "Sử dụng");
                        if(character.infoChar.rank < 8){
                            character.infoChar.rank = 9;
                            character.infoChar.exp_rank = 40;
                            character.client.session.serivce.loadRank(character);
                        }
                        if(DataCenter.gI().phucLoiInfo.RankCaoNhat < 9) {
                            DataCenter.gI().phucLoiInfo.RankCaoNhat = 9;
                            DataCenter.gI().updatePhucLoi(1, DataCenter.gI().phucLoiInfo.RankCaoNhat);
                        }
                        break;
                    case 426:
                        itemNew = new Item(467, item.isLock, 1);
                        character.addItem(itemNew, "Mở rank");
                        character.msgAddItemBag(itemNew);


                        itemNew = new Item(463, item.isLock);
                        itemNew.addItemOption(new ItemOption(0, 150));
                        itemNew.addItemOption(new ItemOption(1, 150));
                        itemNew.addItemOption(new ItemOption(2, 150));
                        itemNew.addItemOption(new ItemOption(209, 100));
                        character.addItem(itemNew, "Mở rank");
                        character.msgAddItemBag(itemNew);


                        character.removeItemBag(item, "Sử dụng");
                        if(character.infoChar.rank < 9){
                            character.infoChar.rank = 10;
                            character.infoChar.exp_rank = 40;
                            character.client.session.serivce.loadRank(character);
                        }
                        if(DataCenter.gI().phucLoiInfo.RankCaoNhat < 10) {
                            DataCenter.gI().phucLoiInfo.RankCaoNhat = 10;
                            DataCenter.gI().updatePhucLoi(1, DataCenter.gI().phucLoiInfo.RankCaoNhat);
                        }
                        break;
                }
                DataCenter.gI().phucLoiInfo.TongRank++;
                DataCenter.gI().updatePhucLoi(0, DataCenter.gI().phucLoiInfo.TongRank);
            } else if (item.getItemTemplate().type == 100) {

                switch (item.id) {
                    case 161:
                        itemAdd = new Item(160, item.isLock, 100);
                        character.addItem(itemAdd, "Mở rương tinh thạch");
                        character.msgAddItemBag(itemAdd);
                        character.removeItemBag(item, "Sử dụng");
                        break;
                    case 177:
                        character.removeItemBag(item, "Sử dụng");
                        List<Integer> listDa = Arrays.asList(7,7,7,8,8,9);
                        int idDa = UTPKoolVN.getRandomList(listDa);
                        itemAdd = new Item(idDa);
                        character.addItem(itemAdd, "Sử dụng Rương vừa đẹp vừa cao quý");
                        character.msgAddItemBag(itemAdd);
                        break;
                    case 163:
                        character.addBacKhoa(item.getAmount(), true, true, "Sử dụng item bạc khóa");
                        character.removeItemBag(item, true,"Sử dụng");
                        break;
                    case 191:
                        character.addBac(item.getAmount(), true, true, "Sử dụng item bạc");
                        character.removeItemBag(item, true,"Sử dụng");
                        break;
                    case 174:
                        character.removeItemBag(item, "Sử dụng");
                        character.infoChar.diem_Hokage[6] += 5;
                        character.client.session.serivce.ShowMessGold("Tăng thêm 5 điểm Hokage Quần");
                        break;
                    case 175:
                        character.removeItemBag(item, "Sử dụng");
                        character.infoChar.diem_Hokage[4] += 5;
                        character.client.session.serivce.ShowMessGold("Tăng thêm 5 điểm Hokage Bao Tay");
                        break;
                    case 179:
                        character.removeItemBag(item, "Sử dụng");
                        character.infoChar.diem_Hokage[0] += 5;
                        character.client.session.serivce.ShowMessGold("Tăng thêm 5 điểm Hokage Đai Trán");
                        break;
                    case 216:
                        character.removeItemBag(item, "Sử dụng");

                        character.infoChar.diem_Hokage[8] += 5;
                        character.client.session.serivce.ShowMessGold("Tăng thêm 5 điểm Hokage Giày");
                        break;
                    case 217:
                        character.removeItemBag(item, "Sử dụng");

                        character.infoChar.diem_Hokage[9] += 5;
                        character.client.session.serivce.ShowMessGold("Tăng thêm 5 điểm Hokage Túi Nhẫn Giả");
                        break;
                    case 218:
                        character.removeItemBag(item, "Sử dụng");
                        character.infoChar.diem_Hokage[1] += 5;
                        character.client.session.serivce.ShowMessGold("Tăng thêm 5 điểm Hokage Vũ Khí");
                        break;
                    case 248:
                        character.removeItemBag(item, "Sử dụng");
                        character.infoChar.diem_Hokage[2] += 5;
                        character.client.session.serivce.ShowMessGold("Tăng thêm 5 điểm Hokage Áo");
                        break;
                    case 278:
                        character.removeItemBag(item, "Sử dụng");
                        character.infoChar.diem_Hokage[3] += 5;
                        character.client.session.serivce.ShowMessGold("Tăng thêm 5 điểm Hokage Dây Thừng");
                        break;
                    case 302:
                        character.removeItemBag(item, "Sử dụng");
                        character.infoChar.diem_Hokage[5] += 5;
                        character.client.session.serivce.ShowMessGold("Tăng thêm 5 điểm Hokage Móc Sắt");
                        break;
                    case 315:
                        character.removeItemBag(item, "Sử dụng");
                        character.infoChar.diem_Hokage[7] += 5;
                        character.client.session.serivce.ShowMessGold("Tăng thêm 5 điểm Hokage Ống Tiêu");
                        break;
                    case 266:
                        if(character.infoChar.lvPk <= 0) break;
                        character.removeItemBag(item, "Sử dụng");
                        character.infoChar.lvPk -= 5;
                        if(character.infoChar.lvPk < 0) character.infoChar.lvPk = 0;
                        character.client.session.serivce.ShowMessGold("Đã giảm 5 cấp PK");
                        break;
                    case 361:
                        if (character.getCountNullItemBag() >= 3) {
                            for (int i = 171; i <= 173; i++) {
                                Item itemNew = new Item(i, item.isLock);
                                character.addItem(itemNew, "Sử dụng ITEM ID: 361");
                                character.msgAddItemBag(itemNew);
                            }
                            character.removeItemBag(item, "Sử dụng");
                        } else {
                            character.client.session.serivce.ShowMessGold("Hành trang không đủ chỗ chứa");
                        }
                        break;
                    case 362:
                        if (character.getCountNullItemBag() >= 3) {
                            for (int i = 355; i <= 357; i++) {
                                Item itemNew = new Item(i, item.isLock);
                                character.addItem(itemNew, "Sử dụng ITEM ID: 362");
                                character.msgAddItemBag(itemNew);
                            }
                            character.removeItemBag(item, "Sử dụng");
                        } else {
                            character.client.session.serivce.ShowMessGold("Hành trang không đủ chỗ chứa");
                        }
                        break;
                    case 363:
                        if (character.getCountNullItemBag() >= 3) {
                            for (int i = 358; i <= 360; i++) {
                                Item itemNew = new Item(i, item.isLock);
                                character.addItem(itemNew, "Sử dụng ITEM ID: 363");
                                character.msgAddItemBag(itemNew);
                            }
                            character.removeItemBag(item, "Sử dụng");
                        } else {
                            character.client.session.serivce.ShowMessGold("Hành trang không đủ chỗ chứa");
                        }
                        break;
                    case 428:
                        if (character.getCountNullItemBag() >= 0) {
                            character.client.session.serivce.ShowMessGold("Hành trang không đủ chỗ chứa");
                            break;
                        }
                        character.removeItemBag(item, "Sử dụng");
                        int itemRand = Utlis.nextInt(406, 413);
                        Item itemNew = new Item(itemRand, item.isLock);
                        character.addItem(itemNew, "Sử dụng ITEM ID: 428");
                        character.msgAddItemBag(itemNew);
                        break;
                    case 435:
                        if (character.infoChar.sachChienDau < 16) {
                            character.removeItemBag(item, "Sử dụng");
                            character.infoChar.sachChienDau++;
                            character.infoChar.itemSach = new Item(435);
                            character.infoChar.itemSach.strOptions = "207,0,-1;208,0,-1";
                            character.infoChar.itemSach.a(character.infoChar.sachChienDau);
                            character.msgUpdateSachChienDau();
                        }
                        break;
                    case 763:
                        Item viThu = character.arrItemBody[10];
                        if(viThu == null || !viThu.isViThu()){
                            character.client.session.serivce.ShowMessGold("Hãy mặc vĩ thú trên người trước");
                            break;
                        }
                        if(viThu.getDiemChiSo(character.client, 305) == 0){
                            character.client.session.serivce.ShowMessGold("Vĩ thú chưa được kích hoạt sức mạnh");
                            break;
                        }

                        int min = viThu.getChiSo(1, character.client, 305);
                        if(min >= 180000){
                            character.client.session.serivce.ShowMessGold("Sức mạnh vĩ thú đã đạt cấp tối đa");
                            break;
                        }
                        int plus = item.getAmount();
                        int all = min + item.getAmount();

                        if(all > 180000) plus = 180000-min;

                        if(all >= 10000){
                            int lv = (plus + min) / 10000;
                            lv = lv - viThu.level;
                            viThu.level += (byte) lv;
                            if(lv > 0) {
                                viThu.setOptionViThu(lv);
                                character.setUpInfo(true);
                            }
                        }
                        viThu.plusOption(305, 1, plus);
                        character.updateItemBody(viThu);
                        character.removeItemBag(item,true, "Sử dụng");
                        character.client.session.serivce.ShowMessGold("Vĩ thú đã được tăng sức mạnh");

                        break;
                    case 719:
                        if (character.infoChar.sachChienDau <= 15) {
                            character.client.session.serivce.ShowMessGold("Chưa thể sử dụng vật phẩm này");
                            break;
                        }
                        if (character.infoChar.sachChienDau >= 17) {
                            character.client.session.serivce.ShowMessGold("Không thể sử dụng vật phẩm này");
                            break;
                        }
                        character.removeItemBag(item, "Sử dụng");
                        character.infoChar.sachChienDau = 17;
                        character.infoChar.itemSach.a(character.infoChar.sachChienDau);
                        character.infoChar.itemSach.addItemOption(new ItemOption(311, 150, -1));
                        character.msgUpdateSachChienDau();

                        break;
                    case 778:
                        if (character.infoChar.sachChienDau <= 16) {
                            character.client.session.serivce.ShowMessGold("Chưa thể sử dụng vật phẩm này");
                            break;
                        }
                        if (character.infoChar.sachChienDau >= 18) {
                            character.client.session.serivce.ShowMessGold("Không thể sử dụng vật phẩm này");
                            break;
                        }
                        character.removeItemBag(item, "Sử dụng");
                        character.infoChar.sachChienDau = 18;
                        character.infoChar.itemSach.a(character.infoChar.sachChienDau);
                        character.infoChar.itemSach.plusOption(311, 1, 150);
                        character.msgUpdateSachChienDau();

                        break;
                    case 380:
                        character.removeItemBag(item, "Sử dụng");
                        character.client.session.serivce.loadPhanTram(character, 2000, "Đang cắm cờ");
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        character.client.session.serivce.camCo((short) 262, character.cx, character.cy, (byte) -1);
                        character.client.session.serivce.xoaTab(character);

                        if(character.infoChar.idTask == 6) TaskHandler.gI().PlusTask(character);
                        break;
                    case 383:
                        if(character.getCountNullItemBag() == 0){
                            character.client.session.serivce.ShowMessGold("Túi không đủ chỗ chứa");
                            break;
                        }
                        character.removeItemBag(item, "Sử dụng");
                        character.client.session.serivce.loadPhanTram(character, 5000, "Đang bẫy chim");
                        try {
                            TimeUnit.SECONDS.sleep(5);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        character.client.session.serivce.xoaTab(character);
                        if(character.infoChar.idTask == 11 && character.infoChar.idStep == 0){

                            Item newitem = new Item(382);
                            newitem.amount = 1;
                            character.addItem(newitem, "Nhiệm vụ");
                            character.msgAddItemBag(newitem);
                        }
                        if(character.infoChar.idTask == 11 && character.infoChar.idStep == 1){
                            Item newitem = new Item(384);
                            newitem.amount = 1;
                            character.addItem(newitem, "Nhiệm vụ");
                            character.msgAddItemBag(newitem);
                        }
                        if(character.infoChar.idTask == 11 && character.infoChar.idStep == 2){
                            Item newitem = new Item(385);
                            newitem.amount = 1;
                            character.addItem(newitem, "Nhiệm vụ");
                            character.msgAddItemBag(newitem);
                        }
                        if(character.infoChar.idTask == 11 && (character.infoChar.idStep >= 0 && character.infoChar.idStep <= 2)) TaskHandler.gI().PlusTask(character);
                        break;
                    case 205:
                        if(character.getCountNullItemBag() == 0){
                            character.client.session.serivce.ShowMessGold("Túi không đủ chỗ chứa");
                            break;
                        }
                        character.removeItemBag(item, "Sử dụng");
                        character.client.session.serivce.loadPhanTram(character, 5000, "Đang lấy nước");
                        try {
                            TimeUnit.SECONDS.sleep(5);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        character.client.session.serivce.xoaTab(character);
                        Item newitem = new Item(206);
                        newitem.amount = 1;
                        character.addItem(newitem, "Nhiệm vụ");
                        character.msgAddItemBag(newitem);
                        if(character.infoChar.idTask == 15 && character.infoChar.idStep == 2){
                            TaskHandler.gI().PlusTask(character);
                        }
                        break;
                    case 235:
                        if(character.getCountNullItemBag() == 0){
                            character.client.session.serivce.ShowMessGold("Túi không đủ chỗ chứa");
                            break;
                        }
                        if(character.infoChar.mapId != 85 || character.cy != 692){
                            character.client.session.serivce.ShowMessGold("Không thể câu cá tại đây");
                            break;
                        }
                        character.removeItemBag(item, "Sử dụng");
                        int rand = Utlis.nextInt(1000,5000);
                        character.client.session.serivce.loadPhanTram(character, rand, "Đang câu cá");
                        try {
                            TimeUnit.SECONDS.sleep(rand/1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        character.client.session.serivce.xoaTab(character);
                        newitem = new Item(236);
                        newitem.amount = 1;
                        character.addItem(newitem, "Nhiệm vụ");
                        character.msgAddItemBag(newitem);
                        if(character.infoChar.idTask == 29 && character.infoChar.idStep == 0){
                            TaskHandler.gI().PlusTask(character);
                        }
                        break;
                    case 394:
                        if(character.getCountNullItemBag() == 0){
                            character.client.session.serivce.ShowMessGold("Túi không đủ chỗ chứa");
                            break;
                        }
                        character.client.session.serivce.loadPhanTram(character, 5000, "Đang đốt bí kíp");
                        try {
                            TimeUnit.SECONDS.sleep(5);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        character.client.session.serivce.xoaTab(character);
                        character.removeItemBag(item, "Sử dụng");
                        if(character.infoChar.idTask == 18 && character.infoChar.idStep == 0){
                            TaskHandler.gI().PlusTask(character);
                        }
                        break;
                    case 329:
                        if(character.getCountNullItemBag() == 0){
                            character.client.session.serivce.ShowMessGold("Túi không đủ chỗ chứa");
                            break;
                        }
                        if(DataCenter.gI().dataCaiTrang.size() == 0){
                            Item.taoDataCaiTrang();
                        }
                        int next = Utlis.nextInt(0,DataCenter.gI().dataCaiTrang.size()-1);
                        Item caiTrangs = DataCenter.gI().dataCaiTrang.get(next);
                        Item caiTrang = new Item(caiTrangs.id);
                        List<Integer> optionRandom = DataCache.OptionCaiTrang.get(Utlis.nextInt(DataCache.OptionCaiTrang.size()));

                        int id = optionRandom.get(0);
                        int param = Utlis.nextInt(optionRandom.get(1),optionRandom.get(2));

                        caiTrang.addItemOption(new ItemOption(id, param, -1));
                        caiTrang.addItemOption(new ItemOption(209, Utlis.nextInt(10,101), -1));
                        character.addItem(caiTrang, "Mở từ rương cải trang");
                        character.msgAddItemBag(caiTrang);
                        character.removeItemBag(item, "Sử dụng");
                        break;
                    case 308:
                        character.addEffect(new Effect(49, 1, System.currentTimeMillis(), 300000));
                        character.client.session.serivce.ShowMessGold("Bắt đầu cảm thấy choáng váng");
                        character.removeItemBag(item, "Sử dụng");
                        break;
                    case 779:
                        if(character.infoChar.sachChienDau != 18) break;
                        int xdame = DataCache.dataDamePhanThan[character.infoChar.levelPhanThan];
                        character.addEffect(new Effect(99, xdame, System.currentTimeMillis(), 1800000));
                        character.removeItemBag(item, "Sử dụng");
                        break;
                    case 782:
                        if(character.infoChar.sachChienDau != 18) break;
                        xdame = DataCache.dataDamePhanThan[character.infoChar.levelPhanThan];
                        character.addEffect(new Effect(99, xdame, System.currentTimeMillis(), 3600000));
                        character.removeItemBag(item, "Sử dụng");
                        break;
                    case 277:
                        if (character.getCountNullItemBag() >= 3) {
                            character.removeItemBag(item, "Sử dụng");
                            for (int i = 4; i <= 6; i++) {
                                Item itemDa = new Item(i, true);
                                character.addItem(itemDa, "Sử dụng ITEM ID: 277");
                                character.msgAddItemBag(itemDa);
                            }
                        }
                        break;
                    case 267:
                        character.removeItemBag(item, "Sử dụng");
                        int level = character.level() / 5 * 5;
                        if (level >= 60) {
                            level = 60;
                        }

                        for (int j = 0; j < DataCenter.gI().ItemTemplate.length; j++) {
                            if (DataCenter.gI().ItemTemplate[j].type == 24 && DataCenter.gI().ItemTemplate[j].levelNeed == level) {
                                for (int i = 0; i < DataCenter.gI().EffectTemplate.length; i++) {
                                    if (DataCenter.gI().EffectTemplate[i].name.equals(DataCenter.gI().ItemTemplate[j].name)) {
                                        character.addEffect(new Effect(i, Effect.getValueEffectFormIdItem(DataCenter.gI().ItemTemplate[j].id), System.currentTimeMillis(), (15 * (60 * (60 * 1000)))));
                                        return;
                                    }
                                }
                                return;
                            }
                        }
                        break;
                    case 171:
                    case 172:
                    case 173:
                    case 355:
                    case 356:
                    case 357:
                    case 358:
                    case 359:
                    case 360:
                        character.removeItemBag(item, "Sử dụng");
                        character.addEffect(new Effect(Effect.getIDEffectFormIdItem(item.id), Effect.getValueEffectFormIdItem(item.id), System.currentTimeMillis(), (5 * (60 * 1000))));
                        break;
                    case 265:
                    case 285:
                        character.removeItemBag(item, "Sử dụng");
                        character.addEffect(new Effect(Effect.getIDEffectFormIdItem(item.id), Effect.getValueEffectFormIdItem(item.id), System.currentTimeMillis(), (25 * (60 * 1000))));
                        break;
                    case 498:
                        if(character.infoChar.sdIzanami > 0){
                            character.client.session.serivce.ShowMessGold("Hôm nay bạn đã sử dụng vật phẩm này rồi");
                            break;
                        }
                        character.infoChar.sdIzanami++;
                        character.infoChar.soLanCamThuat += 1;
                        character.client.session.serivce.ShowMessGold("Đã tăng thêm 1 lần vào cấm thuật Izanami");
                        character.removeItemBag(item, "Sử dụng");
                        break;
                    case 568:
                        if(character.infoChar.sdIzanami2 > 0){
                            character.client.session.serivce.ShowMessGold("Hôm nay bạn đã sử dụng vật phẩm này rồi");
                            break;
                        }
                        character.infoChar.soLanCamThuat += 2;
                        character.infoChar.sdIzanami2++;
                        character.client.session.serivce.ShowMessGold("Đã tăng thêm 2 lần vào cấm thuật Izanami");
                        character.removeItemBag(item, "Sử dụng");
                        break;
                    case 593:
                        character.removeItemBag(item, "Sử dụng");
                        long tinhtime = (System.currentTimeMillis()+604800000L)/1000L;
                        character.infoChar.timeChatColor = (int)tinhtime;
                        character.client.session.serivce.updateTimeChatColor(character.client);
                        break;
                    case 182:
                        character.removeItemBag(item, "Sử dụng");
                        character.infoChar.hoatLuc += 50;
                        character.client.session.serivce.sendHoatLuc(character);
                        break;
                    case 231:
                        itemAdd = new Item(176, item.isLock, 100);
                        character.addItem(itemAdd, "Mở rương vỏ sò");
                        character.msgAddItemBag(itemAdd);
                        character.removeItemBag(item, "Sử dụng");
                        break;
                    case 913:
                        character.msgOpenTabConfig(item, "test1;test2,test_1;test3,test_1,test_2;test4,test_1,test_2,test_3");
                        break;
                    case 914:
                        character.msgOpenTabSaoCuongHoa(item);
                        break;
                    case 616:
                        character.removeItemBag(item, true, "Sử dụng");;
                        if(character.infoChar.timeGiuRuong > System.currentTimeMillis()){
                            character.infoChar.timeGiuRuong += 2592000000L;
                        } else {
                            character.infoChar.timeGiuRuong = System.currentTimeMillis()+2592000000L;
                        }
                        String text = "Thời gian giữ tiền trong rương của bạn còn "+Utlis.getTextTimeFormSeconds((int)((character.infoChar.timeGiuRuong-System.currentTimeMillis())/1000L));
                        character.client.session.serivce.ShowMessGold(text);
                        break;
                    case 190:
                        int exp = item.getAmount();
                        character.removeItemBag(item, true, "Sử dụng");;
                        character.addExp(exp);
                        break;
                    case 558:
                        exp = 35000000;
                        character.removeItemBag(item, true, "Sử dụng");;
                        character.addExp(exp);
                        break;
                    case 643:
                        character.removeItemBag(item, "Sử dụng");
                        character.addEffect(new Effect(85, 100, System.currentTimeMillis(), (60 * (60 * 1000))));
                        break;
                    case 599:
                        if(item.getAmount() < 1000){
                            character.client.session.serivce.ShowMessGold("Cần 1000 mảnh");
                            break;
                        }
                        character.removeItemBag(item, 1000,"Sử dụng");
                        Item itemNews = new Item(600, true);
                        character.addItem(itemNews, "Sử dụng ITEM ID: 599");
                        character.msgAddItemBag(itemNews);
                        break;
                    case 434:
                        if(item.getAmount() < 1000){
                            character.client.session.serivce.ShowMessGold("Cần 1000 mảnh");
                            break;
                        }
                        character.removeItemBag(item, 1000,"Sử dụng");
                        Item itemNewAdd = new Item(435, true);
                        character.addItem(itemNewAdd, "Sử dụng ITEM");
                        character.msgAddItemBag(itemNewAdd);
                        break;
                    case 617:
                        character.removeItemBag(item, "Sử dụng");
                        character.addEffect(new Effect(81, 100, System.currentTimeMillis(), 300000));
                        break;
                    case 600:
                        for (int i = 0; i < character.arraySkill.length; i++){
                            Skill skill = character.arraySkill[i];
                            if(skill.getSkillTemplate().levelNeed == 57){
                                character.client.session.serivce.ShowMessGold("Mỗi nhân vật chỉ được sử dụng 1 lần");
                                return;
                            }
                        }

                        // Tạo một mảng mới có kích thước lớn hơn một phần so với mảng hiện tại
                        Skill[] newArray = new Skill[character.arraySkill.length + 1];

                        System.arraycopy(character.arraySkill, 0, newArray, 0, character.arraySkill.length);

                        if(character.infoChar.idClass == 5){
                            Skill newSkill = DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.CHARKRA_CUU_VI_HINH, 0);
                            newArray[newArray.length - 1] = newSkill;
                        }
                        if(character.infoChar.idClass == 4){
                            if(character.infoChar.gioiTinh == 0){ // nữ
                                Skill newSkill = DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.BYAKUGAN_19, 0);
                                newArray[newArray.length - 1] = newSkill;
                            } else if(character.infoChar.gioiTinh == 1){ // nam
                                Skill newSkill = DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.THAO_CU_THIEN_TOA, 0);
                                newArray[newArray.length - 1] = newSkill;
                            }
                        }
                        if(character.infoChar.idClass == 3){
                            if(character.infoChar.gioiTinh == 0){ // nữ
                                Skill newSkill = DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.BYAKUGAN_13, 0);
                                newArray[newArray.length - 1] = newSkill;
                            } else if(character.infoChar.gioiTinh == 1){ // nam
                                Skill newSkill = DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.NHA_THONG_NHA, 0);
                                newArray[newArray.length - 1] = newSkill;
                            }
                        }
                        if(character.infoChar.idClass == 2){
                            if(character.infoChar.gioiTinh == 0){ // nữ
                                Skill newSkill = DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.TAM_CHUYEN_THAN_THUAT, 0);
                                newArray[newArray.length - 1] = newSkill;
                            } else if(character.infoChar.gioiTinh == 1){ // nam
                                Skill newSkill = DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.ANH_THU_PHUOC_CHI_THUAT, 0);
                                newArray[newArray.length - 1] = newSkill;
                            }
                        }
                        if(character.infoChar.idClass == 1){
                            if(character.infoChar.gioiTinh == 0){ // nữ
                                Skill newSkill = DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.BACH_HAO_CHI_THUAT, 0);
                                newArray[newArray.length - 1] = newSkill;
                            } else if(character.infoChar.gioiTinh == 1){ // nam
                                Skill newSkill = DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.SUSANOO, 0);
                                newArray[newArray.length - 1] = newSkill;
                            }
                        }
                        character.arraySkill = newArray;
                        character.removeItemBag(item,"Sử dụng");
//                    character.msgUseItemBag(item);
                        character.client.session.serivce.ShowMessGold("Bạn đã được đánh thức huyết kế giới hạn");
                        character.client.mChar.msgUpdateSkill();
                        character.client.session.serivce.closeTab();
                        break;
                    case 688:
                        int count = 0;
                        for (int i = 0; i < character.listSkillViThu.size(); i++){
                            SkillClan s = character.listSkillViThu.get(i);
                            if(s.id != 19){
                                count++;
                            }
                        }
                        if(count >= 6){
                            character.client.session.serivce.ShowMessGold("Ô skill vĩ thú đã đạt tối đa");
                            break;
                        }
                        Item itemViThu =character.arrItemBody[10];
                        if(itemViThu == null || !itemViThu.isViThu()){
                            character.client.session.serivce.ShowMessGold("Cần mặc vĩ thú vĩnh viễn trên người mới có thể khai mở skill");
                            break;
                        }
                        int randomID = Utlis.nextInt(DataCenter.gI().vSkillViThu.size()-1);

                        SkillClan skillViThuTPL = (SkillClan) DataCenter.gI().vSkillViThu.get(randomID);
                        SkillClan skillViThu = skillViThuTPL.a();
                        if(skillViThu != null){
                            skillViThu.setLevelViThu(0);
                            character.removeItemBag(item,"Sử dụng");
                            character.listSkillViThu.add(skillViThu);
                            character.client.session.serivce.ShowMessGold("Vĩ thú đã khai mở kỹ năng "+skillViThu.name);
                            character.msgUpdateSkillViThu();
                            character.setUpInfo(true);
                        } else {
                            character.client.session.serivce.ShowMessGold("Có lỗi sảy ra thử lại sau ");
                        }

                        break;
                    case 860:
                        skillViThuTPL = (SkillClan) DataCenter.gI().vSkillViThu.get(6);
                        skillViThu = skillViThuTPL.a();
                        for (int i = 0; i < character.listSkillViThu.size(); i++){
                            SkillClan s = character.listSkillViThu.get(i);
                            if(s.id == 19){
                                character.client.session.serivce.ShowMessGold("Bạn đã học skill này rồi");
                                return;
                            }
                        }
                       itemViThu = character.arrItemBody[10];
                        if(itemViThu == null || itemViThu.getItemTemplate().id != 484){
                            character.client.session.serivce.ShowMessGold("Cần mặc Cửu vĩ mới có thể khai mở skill đặc biệt");
                            break;
                        }

                        skillViThu.setLevelViThu(0);
                        character.removeItemBag(item,"Sử dụng");
                        character.listSkillViThu.add(skillViThu);
                        character.client.session.serivce.ShowMessGold("Vĩ thú đã khai mở kỹ năng "+skillViThu.name);
                        character.msgUpdateSkillViThu();
                        character.setUpInfo(true);

                        break;
                    case 723:
                        for (int i = 0; i < character.arraySkill.length; i++){
                            Skill skill = character.arraySkill[i];
                            if(skill.getSkillTemplate().levelNeed == 60){
                                character.client.session.serivce.ShowMessGold("Mỗi nhân vật chỉ được sử dụng 1 lần");
                                return;
                            }
                        }

                        // Tạo một mảng mới có kích thước lớn hơn một phần so với mảng hiện tại
                        newArray = new Skill[character.arraySkill.length + 1];

                        System.arraycopy(character.arraySkill, 0, newArray, 0, character.arraySkill.length);

                        if(character.infoChar.idClass == 5){
                            Skill newSkill = DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.LOA_TOAN_LIEN_THU_LI_KIEM, 0);
                            newArray[newArray.length - 1] = newSkill;
                        }
                        if(character.infoChar.idClass == 4){
                            Skill newSkill = DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.KHONG_THOI_GIAN_THUAT, 0);
                            newArray[newArray.length - 1] = newSkill;
                        }
                        if(character.infoChar.idClass == 3){
                            Skill newSkill = DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.THANH_SAT_CHAKRA, 0);
                            newArray[newArray.length - 1] = newSkill;
                        }
                        if(character.infoChar.idClass == 2){
                            Skill newSkill = DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.BIET_THIEN_THAN, 0);
                            newArray[newArray.length - 1] = newSkill;
                        }
                        if(character.infoChar.idClass == 1){
                            Skill newSkill = DataCenter.gI().getSkillWithIdAndLevel(SkillTemplate.THIEN_CHIEU, 0);
                            newArray[newArray.length - 1] = newSkill;
                        }
                        character.arraySkill = newArray;
                        character.removeItemBag(item,"Sử dụng");
//                    character.msgUseItemBag(item);
                        character.client.session.serivce.ShowMessGold("Bạn đã học đuợc kỹ năng đặc biệt");
                        character.client.mChar.msgUpdateSkill();
                        character.client.session.serivce.closeTab();
                        break;
//                case 269:
//                    Item[] _269 = new Item[]{
//                            Item.getItemWithTypeAndLevel(0, 60, character.infoChar.gioiTinh, character.infoChar.idClass),
//                            Item.getItemWithTypeAndLevel(1, 60, character.infoChar.gioiTinh, character.infoChar.idClass),
//                            Item.getItemWithTypeAndLevel(2, 60, character.infoChar.gioiTinh, character.infoChar.idClass),
//                            Item.getItemWithTypeAndLevel(3, 60, character.infoChar.gioiTinh, character.infoChar.idClass),
//                            Item.getItemWithTypeAndLevel(4, 60, character.infoChar.gioiTinh, character.infoChar.idClass),
//                            Item.getItemWithTypeAndLevel(5, 60, character.infoChar.gioiTinh, character.infoChar.idClass),
//                            Item.getItemWithTypeAndLevel(6, 60, character.infoChar.gioiTinh, character.infoChar.idClass),
//                            Item.getItemWithTypeAndLevel(7, 60, character.infoChar.gioiTinh, character.infoChar.idClass),
//                            Item.getItemWithTypeAndLevel(8, 60, character.infoChar.gioiTinh, character.infoChar.idClass),
//                            Item.getItemWithTypeAndLevel(9, 60, character.infoChar.gioiTinh, character.infoChar.idClass),};
//                    Item[] itemRQ = _269;
//
//                    if (character.getCountNullItemBag() >= itemRQ.length) {
//                        item.isLock = true;
//                        for (int i = 0; i < itemRQ.length; i++) {
//                            if (itemRQ[i] == null) {
//                                itemRQ[i] = Item.getItemWithTypeAndLevel(i, 50, character.infoChar.gioiTinh, character.infoChar.idClass);
//                            }
//                            level = itemRQ[i].getItemTemplate().levelNeed / 10 * 10;
//                            if (itemRQ[i].isItemTrangBi()) {
//                                if (itemRQ[i].isVuKhi()) {
//                                    itemRQ[i].he = character.infoChar.idClass;
//                                    Item.setOptionsVuKhi(itemRQ[i], level);
//                                } else {
//
//                                    itemRQ[i].he = character.infoChar.idClass;
//                                    Item.setOptionsTrangBiPhuKien(itemRQ[i], level);
//                                }
//                                itemRQ[i].createItemOptions();
//                                itemRQ[i].a(16);
//                            }
//                            character.addItem(itemRQ[i], "Sử dụng ITEM ID: 269");
//                        }
//                        character.removeItemBag(item, "Sử dụng");
//                        character.msgUseItemBag(item);
//                        try {
//                            for (int i = 0; i < itemRQ.length; i++) {
//                                character.msgAddItemBag(itemRQ[i]);
//                            }
//                        } catch (Exception ex) {
//                            Utlis.logError(Char.class, ex , "Da say ra loi:\n" + ex.getMessage());
//                        }
//                    }
//
//
//                    itemRQ = ItemsFormItem.getGift(item.id);
//                    if (itemRQ != null) {
//                        if (character.getCountNullItemBag() >= itemRQ.length) {
//                            item.isLock = true;
//                            for (int i = 0; i < itemRQ.length; i++) {
//                                if (itemRQ[i].isTypeTrangBi()) {
//                                    if (itemRQ[i].isVuKhi()) {
//                                        itemRQ[i].he = character.infoChar.idClass;
//                                        Item.setOptionsVuKhi(itemRQ[i], 1);
//                                    }
//                                    itemRQ[i].createItemOptions();
//                                }
//                                character.addItem(itemRQ[i],"Sử dụng ITEM ID: 269");
//                            }
//                            character.removeItemBag(item, "Sử dụng");
//                            character.msgUseItemBag(item);
//                            try {
//                                for (int i = 0; i < itemRQ.length; i++) {
//                                    character.msgAddItemBag(itemRQ[i]);
//                                }
//                            } catch (Exception ex) {
//                                Utlis.logError(Char.class, ex , "Da say ra loi:\n" + ex.getMessage());
//                            }
//                        }
//                    }
//                    break;

                    case 551:
                    case 552:
                    case 553:
                    case 554:
                    case 405:
                    case 416:
                        character.msgOpenTabConfig(item, "Lôi; Thổ; Thủy; Hỏa; Phong");
                        break;
                    case 870:
                        Item itemBuaNo = character.arrItemBody[13];
                        if(itemBuaNo == null){
                            character.client.session.serivce.ShowMessGold("Hãy mặc bùa nổ vào trước");
                            break;
                        }
                        if(itemBuaNo.id != 811){
                            character.client.session.serivce.ShowMessGold("Chỉ sử dụng được với Bùa nổ siêu cấp");
                            break;
                        }
                        int max = itemBuaNo.getChiSoI(0,2, character.client);

                        if(max >= 2100){
                            character.client.session.serivce.ShowMessGold("Mỗi bùa nổ chỉ được sử dụng 4 lần");
                            break;
                        }
                        itemBuaNo.plusOptionByIndex(0, 2, 150);
                        itemBuaNo.plusOptionByIndex(1, 2, 150);
                        character.msgUpdateItemBody();
                        character.removeItemBag(item, "Sử dụng");
                        character.client.session.serivce.ShowMessGold("Đã mở rộng thêm giới hạn của bùa nổ");
                        break;

                    case 704:
                        if(character.infoChar.maxGhepCaiTrang != 17){
                            character.client.session.serivce.ShowMessGold("Bạn không đủ điều kiện sử dụng vật phẩm này");
                            break;
                        }
                        character.removeItemBag(item, 1,"Sử dụng");
                        character.infoChar.maxGhepCaiTrang = 18;
                        character.client.session.serivce.ShowMessGold("Đã nâng giới hạn ghép cải trang lên +17");
                        break;


                    case 790:
                        if(character.infoChar.maxGhepCaiTrang != 18){
                            character.client.session.serivce.ShowMessGold("Hãy sử dụng Nhẫn thuật sao chép sơ cấp trước");
                            break;
                        }
                        character.removeItemBag(item, 1,"Sử dụng");
                        character.infoChar.maxGhepCaiTrang = 19;
                        character.client.session.serivce.ShowMessGold("Đã nâng giới hạn ghép cải trang lên +18");
                        break;
                    ///


                    default:
                        break;
                }

            } else if (item.getItemTemplate().type == 28) {
                Item ex = item.cloneItem();
                for (int i = 0; i < character.arrItemExtend.length; i++) {
                    if (character.arrItemExtend[i] == null) {
                        ex.index = i;
                        character.arrItemExtend[i] = ex;
                        character.updateItemBag();
                        break;
                    }
                }
                character.msgUseItem(item);
                character.removeItemBagNoUpdate(item, true, "Sử dụng");
            } else if (item.getItemTemplate().type == 34) {

                // Chuyển đổi chuỗi thành số nguyên
                int detailInt = Integer.parseInt(itemTemplate.detail);
                if(detailInt < 0) detailInt = Math.abs(detailInt);
                long timehsd = item.expiry;
                if(timehsd >= 0){
                    timehsd /= 1000L;
                }
                String modifiedName =  item.getItemTemplate().name.replace("Danh hiệu ","");
                character.infoChar.selectDanhHieu = (byte) character.listDanhHieu.size();
                character.listDanhHieu.add(new DanhHieu(character.listDanhHieu.size(), modifiedName, (int) timehsd, detailInt));
                character.removeItemBag(item, "Sử dụng");
                character.client.session.serivce.sendDanhHieu(character);
            } else {
                switch (item.id){
                    case 167:
                    case 168:
                        character.msgOpenTabConfig(item, "Trường Konoha;Làng,Làng Lá,Làng Sương Mù,Làng Mây,Làng Đá,Làng Cát,Làng Cỏ,Làng Mưa;Khu rừng chết;Đại chiến nhẫn giả lần III;Đại hội nhẫn giả ");
                        break;

                    case 153:
                        if (character.infoChar.getLogUserItem(item.id) >= 3){
                            character.client.session.serivce.ShowMessWhite("Số lần sử dụng sách đã đạt tối đa");
                            break;
                        }
                        character.removeItemBag(item, "Sử dụng");
                        character.infoChar.diemTiemNang += 10;
                        character.infoChar.logUserItem.add((int) item.id);
                        character.client.session.serivce.ShowMessWhite("Bạn nhận được 10 điểm tiềm năng");
                        break;
                    case 154:
                        if (character.infoChar.getLogUserItem(item.id) >= 2){
                            character.client.session.serivce.ShowMessWhite("Số lần sử dụng sách đã đạt tối đa");
                            break;
                        }
                        character.removeItemBag(item, "Sử dụng");
                        character.infoChar.diemTiemNang += 20;
                        character.infoChar.logUserItem.add((int) item.id);
                        character.client.session.serivce.ShowMessWhite("Bạn nhận được 20 điểm tiềm năng");
                        break;
                    case 155:
                        if (character.infoChar.getLogUserItem(item.id) > 0){
                            character.client.session.serivce.ShowMessWhite("Số lần sử dụng sách đã đạt tối đa");
                            break;
                        }
                        character.removeItemBag(item, "Sử dụng");
                        character.infoChar.diemTiemNang += 30;
                        character.infoChar.logUserItem.add((int) item.id);
                        character.client.session.serivce.ShowMessWhite("Bạn nhận được 30 điểm tiềm năng");
                        break;
                    case 150:
                        if (character.infoChar.getLogUserItem(item.id) >= 3){
                            character.client.session.serivce.ShowMessWhite("Số lần sử dụng sách đã đạt tối đa");
                            break;
                        }
                        character.removeItemBag(item, "Sử dụng");
                        character.infoChar.diemKyNang += 1;
                        character.infoChar.logUserItem.add((int) item.id);
                        character.msgUpdateSkill();
                        character.client.session.serivce.ShowMessWhite("Bạn nhận được 1 điểm kỹ năng");
                        break;
                    case 151:
                        if (character.infoChar.getLogUserItem(item.id) >= 2){
                            character.client.session.serivce.ShowMessWhite("Số lần sử dụng sách đã đạt tối đa");
                            break;
                        }
                        character.removeItemBag(item, "Sử dụng");
                        character.infoChar.diemKyNang += 2;
                        character.infoChar.logUserItem.add((int) item.id);
                        character.msgUpdateSkill();
                        character.client.session.serivce.ShowMessWhite("Bạn nhận được 2 điểm kỹ năng");
                        break;
                    case 152:
                        if (character.infoChar.getLogUserItem(item.id) > 0){
                            character.client.session.serivce.ShowMessWhite("Số lần sử dụng sách đã đạt tối đa");
                            break;
                        }
                        character.removeItemBag(item, "Sử dụng");
                        character.infoChar.diemKyNang += 3;
                        character.infoChar.logUserItem.add((int) item.id);
                        character.msgUpdateSkill();
                        character.client.session.serivce.ShowMessWhite("Bạn nhận được 3 điểm kỹ năng");
                        break;
                    case 209:
                        if(character.getCountNullItemBag() == 0){
                            character.client.session.serivce.ShowMessGold("Túi không đủ chỗ chứa");
                            break;
                        }
                        character.removeItemBag(item, "Sử dụng");
                        character.client.session.serivce.loadPhanTram(character, 5000, "Đang đào bảo vật");
                        try {
                            TimeUnit.SECONDS.sleep(5);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        character.client.session.serivce.xoaTab(character);
                        Item newitem = new Item(399);
                        newitem.amount = 1;
                        character.addItem(newitem, "Nhiệm vụ");
                        character.msgAddItemBag(newitem);
                        if(character.infoChar.idTask == 17 && character.infoChar.idStep == 0 || character.infoChar.idTask == 28 && character.infoChar.idStep == 0){
                            TaskHandler.gI().PlusTask(character);
                        }
                        break;
                    case 165:
                        character.removeItemBag(item, "Sử dụng");
                        character.veMapMacDinh();
                        break;
                    case 166:
                        character.veMapMacDinh();
                        break;
                }
            }
        }catch (Exception ex) {
            Utlis.logError(ItemHandle.class, ex , "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public static void useItemWithTab(Char character, int indexItem, int index1, int index2) {
        try {
            if (indexItem < 0 || indexItem >= character.arrItemBag.length) {
                return;
            }
            Item itembag = character.arrItemBag[indexItem];
            if(itembag == null) return;
            switch (itembag.id) {
                case 914:
                    if (itembag.arrayAction == null || itembag.arrayAction.length <= index1) {
                        return;
                    }
                    itembag.arrayAction[index1].action(character.client);

                    character.removeItemBag(itembag, "Sử dụng Withtab");
                    character.msgUpdateItemBody();
                    break;
                case 167:
                case 168:
                    if (index1 == 0) {
                        Map.maps[86].addChar(character.client);
                    } else if (index1 == 1) {
                        if(index2 == 0){
                            Map.maps[75].addChar(character.client);
                        } else if(index2 == 1){
                            Map.maps[60].addChar(character.client);
                        } else if(index2 == 2){
                            Map.maps[69].addChar(character.client);
                        } else if(index2 == 3){
                            Map.maps[85].addChar(character.client);
                        } else if(index2 == 4){
                            Map.maps[59].addChar(character.client);
                        } else if(index2 == 5){
                            Map.maps[68].addChar(character.client);
                        } else if(index2 == 6){
                            Map.maps[102].addChar(character.client);
                        }
                    }
                    break;
                case 435:
                    switch (index1){
                        case 0:
                            Item getsach = character.getItemBagById(435);
                            if(character.infoChar.vang < 1500){
                                character.client.session.serivce.ShowMessGold("Không đủ vàng");
                            } else if(character.getCountNullItemBag() <= 0){
                                character.client.session.serivce.ShowMessGold("Hành trang cần 1 ô chứa ");
                            } else if(getsach == null || getsach.getAmount() < 10){
                                character.client.session.serivce.ShowMessGold("Cần 10 sách kỹ năng chiến đấu");
                            } else {
                                character.removeItemBag(getsach, 10," Đổi sách");
                                character.mineVang(1500, true, true, "đổi sách");
                                Item itemAdd = new Item(719, true);
                                character.addItem(itemAdd, " đổi sách");
                                character.msgAddItemBag(itemAdd);
                            }
                            break;
                        case 1:
                            Item sach2 = character.getItemBagById(435);
                            if(character.infoChar.vang < 3000){
                                character.client.session.serivce.ShowMessGold("Không đủ vàng");
                            } else if(character.getCountNullItemBag() <= 0){
                                character.client.session.serivce.ShowMessGold("Hành trang cần 1 ô chứa ");
                            } else if(sach2 == null || sach2.getAmount() < 100){
                                character.client.session.serivce.ShowMessGold("Cần 100 sách kỹ năng chiến đấu");
                            } else {
                                character.removeItemBag(sach2, 100," Đổi sách");
                                character.mineVang(3000, true, true, "đổi sách");
                                Item itemAdd = new Item(778, true);
                                character.addItem(itemAdd, " đổi sách");
                                character.msgAddItemBag(itemAdd);
                            }
                            break;
                    }
                    break;
                case 405:
                case 416:
                    if(index1 < 0 || index1 >= 5) break;
                    try {

                        Item itemRQ = Item.getItemWithTypeAndLevel(1, itembag.getItemTemplate().levelNeed, character.infoChar.gioiTinh, character.infoChar.idClass);
                        if (character.getCountNullItemBag() >= 1) {
                            if (itemRQ != null) {
                                itemRQ.he = (byte) index1;
                                Item.setOptionsVuKhi_hokage(itemRQ, itembag.getItemTemplate().levelNeed);
                                itemRQ.a(4);
                                character.addItem(itemRQ, "Sử dụng ITEM ID: "+itembag.id);
                                character.msgAddItemBag(itemRQ);
                                character.removeItemBag(itembag, true,"Sử dụng");
                            }
                        } else {
                            character.client.session.serivce.ShowMessGold("Hành trang cần 1 ô chứa ");
                        }
                    } catch (Exception ex) {
                        Utlis.logError(Char.class, ex , "Da say ra loi:\n" + ex.getMessage());
                    }
                    break;
                case 551:
                case 552:
                case 553:
                case 554:
                    if(index1 < 0 || index1 >= 5) break;
                    if(character.level() < 30) break;
                    try {
                        Item[] listAdd = new Item[]{
                                Item.getItemWithTypeAndLevel(0, 30, character.infoChar.gioiTinh, character.infoChar.idClass),
                                Item.getItemWithTypeAndLevel(1, 30, character.infoChar.gioiTinh, character.infoChar.idClass),
                                Item.getItemWithTypeAndLevel(2, 30, character.infoChar.gioiTinh, character.infoChar.idClass),
                                Item.getItemWithTypeAndLevel(3, 30, character.infoChar.gioiTinh, character.infoChar.idClass),
                                Item.getItemWithTypeAndLevel(4, 30, character.infoChar.gioiTinh, character.infoChar.idClass),
                                Item.getItemWithTypeAndLevel(5, 30, character.infoChar.gioiTinh, character.infoChar.idClass),
                                Item.getItemWithTypeAndLevel(6, 30, character.infoChar.gioiTinh, character.infoChar.idClass),
                                Item.getItemWithTypeAndLevel(7, 30, character.infoChar.gioiTinh, character.infoChar.idClass),
                                Item.getItemWithTypeAndLevel(8, 30, character.infoChar.gioiTinh, character.infoChar.idClass),
                                Item.getItemWithTypeAndLevel(9, 30, character.infoChar.gioiTinh, character.infoChar.idClass),};
                        Item[] itemRQ = listAdd;

                        if (character.getCountNullItemBag() >= itemRQ.length) {

                            for (int i = 0; i < itemRQ.length; i++) {
                                if (itemRQ[i] == null) {
                                    itemRQ[i] = Item.getItemWithTypeAndLevel(i, 30, character.infoChar.gioiTinh, character.infoChar.idClass);
                                }
                                if (itemRQ[i].isItemTrangBi()) {
                                    if (itemRQ[i].isVuKhi()) {
                                        itemRQ[i].he = (byte) index1;
                                        Item.setOptionsVuKhi(itemRQ[i], 30);
                                    } else {

                                        itemRQ[i].he = (byte) index1;
                                        Item.setOptionsTrangBiPhuKien(itemRQ[i], 30);
                                    }
                                    itemRQ[i].createItemOptions();
                                    if (itemRQ[i].isVuKhi()) {
                                        itemRQ[i].a(8);
                                    } else {
                                        itemRQ[i].a(6);
                                    }
                                }
                                character.addItem(itemRQ[i], "Sử dụng ITEM ID: Phúc lợi hiền nhân");
                                character.msgAddItemBag(itemRQ[i]);
                            }
                            character.removeItemBag(itembag, true,"Sử dụng");
                        } else {
                            character.client.session.serivce.ShowMessGold("Hành trang cần 10 ô chứa ");
                        }
                    } catch (Exception ex) {
                        Utlis.logError(ItemHandle.class, ex , "Da say ra loi:\n" + ex.getMessage());
                    }
                    break;
            }
        } catch (Exception ex) {
            Utlis.logError(ItemHandle.class, ex , "Da say ra loi:\n" + ex.getMessage());
        }
    }
    public static class ItemsFormItem {

        public static Item[] _268 = new Item[]{
                new Item(185, true),
                new Item(185, true),
                new Item(185, true),
                new Item(169, true, 10),
                new Item(170, true, 10),
                new Item(267, true, 10),
                new Item(12, true, 1000),
                new Item(17, true, 1000),
                new Item(28, true),
                new Item(914, true, 100),
                new Item(269, true)
        };

        public static Item[] getGift(int id) {
            switch (id) {
                case 268:
                    return Utlis.cloneArray(_268);
//                case 269:
//                    return Utlis.cloneArray(_269);
            }
            return null;
        }
    }
}
