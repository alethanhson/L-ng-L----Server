package com.langla.real.task;

import com.langla.data.DataCache;
import com.langla.data.DataCenter;
import com.langla.data.Step;
import com.langla.data.Task;
import com.langla.lib.Utlis;
import com.langla.real.item.Item;
import com.langla.real.map.*;
import com.langla.real.npc.Npc;
import com.langla.real.player.Char;
import com.langla.server.lib.Message;
import com.langla.utlis.UTPKoolVN;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;

import static com.langla.real.npc.Npc.addAction;

/**
 * @author PKoolVN
 **/
public class TaskHandler {

    protected static TaskHandler Instance;

    public static TaskHandler gI() {
        if (Instance == null)
            Instance = new TaskHandler();
        return Instance;
    }

    public ArrayList<Npc.ActionNpc> checkNPC(Char character, int npcId)
    {
        if(character.infoChar.idTask > DataCenter.gI().Task.length || MyTaskTpl(character) == null) return null;
        if(character.infoChar.idStep == -1 && MyTaskTpl(character).idNpc == npcId){
            ArrayList<Npc.ActionNpc> list = new ArrayList<>();
            addAction(list, ":-?"+MyTaskTpl(character).name, client -> client.session.serivce.sendStrTask((byte) -1));
            return list;
        }
        if(MyStepTpl(character) == null ) return null;
        if(isDoneTask(character) && MyTaskTpl(character).idNpc == npcId){
            ArrayList<Npc.ActionNpc> list = new ArrayList<>();
            addAction(list, ":-!"+MyTaskTpl(character).name, client -> client.session.serivce.sendStrTask((byte) -2));
            return list;
        }
        if( MyStepTpl(character).name.contains("Nói chuyện với") && MyStepTpl(character).idNpc == npcId){
            ArrayList<Npc.ActionNpc> list = new ArrayList<>();
            addAction(list, ":-?Nói chuyện", client -> client.session.serivce.sendStrTask((byte) client.mChar.infoChar.idStep));
            return list;
        }
        if(MyStepTpl(character).name.contains("Làm quen") || MyStepTpl(character).name.contains("Gặp Rokusho Aoi")&& MyStepTpl(character).idNpc == npcId){
            ArrayList<Npc.ActionNpc> list = new ArrayList<>();
            addAction(list, ":-?"+MyStepTpl(character).name, client -> client.session.serivce.sendStrTask((byte) client.mChar.infoChar.idStep));
            return list;
        }
        if(MyStepTpl(character).name.contains("Tìm") && MyStepTpl(character).idNpc == npcId){
            ArrayList<Npc.ActionNpc> list = new ArrayList<>();
            addAction(list, ":-?Nói chuyện", client -> client.session.serivce.sendStrTask((byte) client.mChar.infoChar.idStep));
            return list;
        }

        if(MyStepTpl(character).STR.contains("Về nhà thôi") || MyStepTpl(character).name.contains("Hộ tống") || MyStepTpl(character).name.contains("Giải cứu") && MyStepTpl(character).idNpc == npcId){
            ArrayList<Npc.ActionNpc> list = new ArrayList<>();
            addAction(list, ":-?"+MyStepTpl(character).STR, client -> veNha(client.mChar));
            return list;
        }
        if((MyStepTpl(character).name.contains("Đánh bại") || MyStepTpl(character).name.contains("Giao đấu") || MyStepTpl(character).name.contains("So tài") || MyStepTpl(character).name.contains("Sức mạnh của Gaara")) && MyStepTpl(character).idNpc == npcId){
            ArrayList<Npc.ActionNpc> list = new ArrayList<>();
            addAction(list, ":-?"+MyStepTpl(character).name, client -> danhBai(client.mChar));
            return list;
        }
        if((MyStepTpl(character).name.contains("Từ biệt") || MyStepTpl(character).name.contains("Gặp mặt") )&& MyStepTpl(character).idNpc == npcId){
            ArrayList<Npc.ActionNpc> list = new ArrayList<>();
            addAction(list, ":-?"+MyStepTpl(character).name, client -> client.session.serivce.sendStrTask((byte) client.mChar.infoChar.idStep));
            return list;
        }
        if(MyStepTpl(character).STR.contains("Giao thư") || MyStepTpl(character).STR.contains("Giao cá chép")  || MyStepTpl(character).name.contains("Giao trả")&& MyStepTpl(character).idNpc == npcId){
            ArrayList<Npc.ActionNpc> list = new ArrayList<>();
            addAction(list, ":-?"+MyStepTpl(character).name, client -> client.session.serivce.openTabGiaoVatPham());
            return list;
        }
        return null;
    }

    public Task MyTaskTpl(Char character){
        if(character.infoChar.idTask < DataCenter.gI().Task.length){
            return DataCenter.gI().Task[character.infoChar.idTask];
        } else {
            return null;
        }
    }

    public Step MyStepTpl(Char character){
        if(character.infoChar.idTask < DataCenter.gI().Task.length && character.infoChar.idStep >= 0){
            if(isDoneTask(character)){
                return (Step) DataCenter.gI().Task[character.infoChar.idTask].vStep.get(DataCenter.gI().Task[character.infoChar.idTask].vStep.size()-1);
            } else {
                return (Step) DataCenter.gI().Task[character.infoChar.idTask].vStep.get(character.infoChar.idStep);
            }
        } else {
            return null;
        }
    }
    public void huyNhiemVu(Char character){
        if(character.infoChar.idTask >= DataCenter.gI().Task.length) return;
        character.infoChar.requireTask = 0;
        character.infoChar.idStep = -1;
        sendInfoTask(character);
    }
    public void nhanNhiemVu(Char character){
        if(character.infoChar.idTask >= DataCenter.gI().Task.length) return;
        if(character.infoChar.idStep == -1){
            if(character.infoChar.idTask == 6){
                if(character.getCountNullItemBag() == 0){
                    character.client.session.serivce.ShowMessRed("Hành trang không đủ chỗ chứa");
                    return;
                }
                Item itemAdd = new Item(380, false);
                itemAdd.amount = 3;
                character.addItem(itemAdd, "Nhiệm vụ");
                character.msgAddItemBag(itemAdd);
            }
            if(character.infoChar.idTask == 11){
                if(character.getCountNullItemBag() == 0){
                    character.client.session.serivce.ShowMessRed("Hành trang không đủ chỗ chứa");
                    return;
                }
                Item itemAdd = new Item(383, false);
                itemAdd.amount = 3;
                character.addItem(itemAdd, "Nhiệm vụ");
                character.msgAddItemBag(itemAdd);
            }
            if(character.infoChar.idTask == 13){
                if(character.getCountNullItemBag() <= 4){
                    character.client.session.serivce.ShowMessRed("Hành trang không đủ chỗ chứa");
                    return;
                }
                Item itemAdd = new Item(386, false);
                itemAdd.amount = 1;
                character.addItem(itemAdd, "Nhiệm vụ");
                character.msgAddItemBag(itemAdd);

                itemAdd = new Item(387, false);
                itemAdd.amount = 1;
                character.addItem(itemAdd, "Nhiệm vụ");
                character.msgAddItemBag(itemAdd);

                itemAdd = new Item(388, false);
                itemAdd.amount = 1;
                character.addItem(itemAdd, "Nhiệm vụ");
                character.msgAddItemBag(itemAdd);

                itemAdd = new Item(389, false);
                itemAdd.amount = 1;
                character.addItem(itemAdd, "Nhiệm vụ");
                character.msgAddItemBag(itemAdd);

                itemAdd = new Item(390, false);
                itemAdd.amount = 1;
                character.addItem(itemAdd, "Nhiệm vụ");
                character.msgAddItemBag(itemAdd);
            }
            if(character.infoChar.idTask == 15){
                if(character.getCountNullItemBag() == 0){
                    character.client.session.serivce.ShowMessRed("Hành trang không đủ chỗ chứa");
                    return;
                }
                Item itemAdd = new Item(205, false);
                itemAdd.amount = 5;
                character.addItem(itemAdd, "Nhiệm vụ");
                character.msgAddItemBag(itemAdd);
            }
            if(character.infoChar.idTask == 17 || character.infoChar.idTask == 28){
                if(character.getCountNullItemBag() == 0){
                    character.client.session.serivce.ShowMessRed("Hành trang không đủ chỗ chứa");
                    return;
                }
                Item itemAdd = new Item(209, false);
                itemAdd.amount = 1;
                character.addItem(itemAdd, "Nhiệm vụ");
                character.msgAddItemBag(itemAdd);
            }
            if(character.infoChar.idTask == 18){
                if(character.getCountNullItemBag() == 0){
                    character.client.session.serivce.ShowMessRed("Hành trang không đủ chỗ chứa");
                    return;
                }
                Item itemAdd = new Item(394, false);
                itemAdd.amount = 1;
                character.addItem(itemAdd, "Nhiệm vụ");
                character.msgAddItemBag(itemAdd);
            }
            if(character.infoChar.idTask == 29){
                if(character.getCountNullItemBag() < 4){
                    character.client.session.serivce.ShowMessRed("Hành trang không đủ chỗ chứa");
                    return;
                }
                Item itemAdd = new Item(235, false);
                itemAdd.amount = 60;
                character.addItem(itemAdd, "Nhiệm vụ");
                character.msgAddItemBag(itemAdd);
                itemAdd = new Item(400, false);
                itemAdd.amount = 1;
                character.addItem(itemAdd, "Nhiệm vụ");
                character.msgAddItemBag(itemAdd);
                itemAdd = new Item(398, false);
                itemAdd.amount = 1;
                character.addItem(itemAdd, "Nhiệm vụ");
                character.msgAddItemBag(itemAdd);
                itemAdd = new Item(399, false);
                itemAdd.amount = 1;
                character.addItem(itemAdd, "Nhiệm vụ");
                character.msgAddItemBag(itemAdd);
            }
            character.infoChar.idStep++;
            character.infoChar.requireTask = 0;
            sendInfoTask(character);
        }
    }
    public void noiChuyenXong(Char character){
        if(MyStepTpl(character) != null && MyStepTpl(character).name.contains("Tìm")
                || MyStepTpl(character).name.contains("Từ biệt")
                || MyStepTpl(character).name.contains("Nói chuyện với")
                || MyStepTpl(character).name.contains("Làm quen")
                || MyStepTpl(character).name.contains("Gặp mặt")
                || MyStepTpl(character).name.contains("Gặp Rokusho Aoi")){
            character.infoChar.idStep++;
            character.infoChar.requireTask = 0;
            sendInfoTask(character);
        }
    }
    public void hoanThanhNhiemVu(Char character){
        if(character.infoChar.idTask >= DataCenter.gI().Task.length && MyStepTpl(character) == null) return;
        if(isDoneTask(character)){
            if(isGiaoVatPham(MyStepTpl(character).id)){
                character.client.session.serivce.openTabGiaoVatPham();
            } else {
                if(character.getCountNullItemBag() == 0){
                    character.client.session.serivce.ShowMessGold("Túi không đủ chỗ chứa");
                    return;
                }
                doneNhiemVu(character);
            }
        }
    }
    public void update(Char character){
        if(character.infoChar.idTask == 20 && character.infoChar.idStep == 1
                || character.infoChar.idTask == 21 && character.infoChar.idStep == 0
                || character.infoChar.idTask == 22 && (character.infoChar.idStep == 0
                || character.infoChar.idStep == 1)
                || character.infoChar.idTask == 35 && character.infoChar.idStep == 0
        ){
            PlusTask(character);
        }
    }
    public void hoanThanhNhiemVu(Char character, Item[] item){
        if(character.getCountNullItemBag() == 0){
            character.client.session.serivce.ShowMessGold("Túi không đủ chỗ chứa");
            return;
        }

        if (isDoneTask(character)) {
            List<Step> listStep = new ArrayList<>();

            // Lọc các bước đã hoàn thành
            for(int i = 0; i < MyTaskTpl(character).vStep.size(); i++) {
                Step var10;
                if ((var10 = (Step)MyTaskTpl(character).vStep.elementAt(i)).a()) {
                    listStep.add(var10);
                }
            }

            // Kiểm tra mỗi bước nhiệm vụ
            for (Step step : listStep) {
                int idItem = step.b();
                boolean foundItem = false;

                // Kiểm tra mỗi item trong mảng
                for (Item anItem : item) {
                    if (anItem != null && anItem.id == idItem && anItem.getAmount() >= step.require) {
                        character.removeItemBag(anItem, step.require, "Nhiệm vụ");
                        foundItem = true;
                        break; // Tìm thấy item phù hợp, thoát khỏi vòng lặp
                    }
                }

                // Nếu không tìm thấy item phù hợp, thông báo cho người chơi và thoát
                if (!foundItem) {
                    character.client.session.serivce.NhacNhoMessage("Không đủ số lượng yêu cầu, hoặc có nhiều item tách (Hãy gộp lại)!");
                    return;
                }
            }

            doneNhiemVu(character);
            character.client.session.serivce.closeTab();
        } else {

        Step var4 = MyStepTpl(character);
            if ( var4 != null) {
                int var5 = MyStepTpl(character).b();
                int sl = 0;
                for (Item element : item) {
                    if (element.id == var5) {
                        sl += element.getAmount();
                    }
                }
                if(sl < var4.require){
                    character.client.session.serivce.NhacNhoMessage("Không đủ số lượng yêu cầu. Nếu item tách thành nhiều vui lòng gộp và thử lại");
                    return;
                }
                for (Item element : item) {
                    if (element.id == var5) {
                        character.removeItemBag(element,"Nhiệm vụ");
                    }
                }
                nextStep(character);
                character.client.session.serivce.closeTab();
            }
        }


    }
    public void doneNhiemVu(Char character){

        if(isDoneTask(character)){
            if(MyTaskTpl(character).getItem(character) != null){
                if(character.getCountNullItemBag() == 0){
                    character.client.session.serivce.ShowMessGold("Túi không đủ chỗ chứa");
                    return;
                }
                character.addItem(MyTaskTpl(character).getItem(character), "Nhiệm vụ");
            }
            if(MyTaskTpl(character).amountBac > 0){
                character.addBac(MyTaskTpl(character).amountBac, true, true, "Nhiệm vụ");
            }
            if(MyTaskTpl(character).amountBacKhoa > 0){
                character.addBacKhoa(MyTaskTpl(character).amountBacKhoa, true, true, "Nhiệm vụ");
            }
            if(MyTaskTpl(character).amountVangKhoa > 0){
                character.addVangKhoa(MyTaskTpl(character).amountVangKhoa, true, true, "Nhiệm vụ");
            }
            if(MyTaskTpl(character).amountExp > 0){
                character.addExp(MyTaskTpl(character).amountExp);
            }
            character.infoChar.idTask++;
            character.infoChar.idStep = -1;
            character.infoChar.requireTask = 0;
            sendInfoTask(character);

        }
    }
    public void checkDoneUseItem(Char character, Item item){
        if(item.getItemTemplate().type == 1 && (character.infoChar.idTask == 0 && character.infoChar.idStep == 0) || (character.infoChar.idTask == 8 && character.infoChar.idStep == 9)){
            PlusTask(character);
        }
        if(item.id == 22 && character.infoChar.idTask == 3 && character.infoChar.idStep == 1){
            PlusTask(character);
        }
    }

    public void checkDoneBuyItem(Char character, int idItem){
        if(character.infoChar.idTask >= DataCenter.gI().Task.length || MyStepTpl(character) == null) return;
        if(idItem == MyStepTpl(character).idItem){
            PlusTask(character);
        }
    }
    public void checkDoneSeting(Char character){
        if(character.infoChar.idTask == 0 && character.infoChar.idStep == 1){
            PlusTask(character);
        }
    }
    public void checkDoneJoinMap(Char character){
        int idTask = character.infoChar.idTask;
        int idStep = character.infoChar.idStep;

        int[] ids = {idTask, idStep};

        switch (ids[0] * 10 + ids[1]) {
            case 82: // idTask = 1 và idStep = 1
                PlusTask(character);
                break;
            default:

                break;
        }
    }

    public void createMobJoinMap(Char character){
        if(character.infoChar.idTask == 32 && character.zone.map.mapID == 56){
            Mob mob2 = createMob(character, MyStepTpl(character).idMob, character.infoChar.hpFull*3, MyStepTpl(character).x, MyStepTpl(character).y, false);
            for (int i = 0; i < character.zone.vecMob.size(); i++){
                Mob m = character.zone.vecMob.get(i);
                if(m.id == mob2.id){
                    return;
                }
            }
            addMobToZone(character, mob2);
        }
    }
    public void checkDoneKillMob(Char character, Mob mob){
        if(character.infoChar.idTask >= DataCenter.gI().Task.length) return;
        boolean isOk = false;

        if(MyStepTpl(character) != null){
            if(isGiaoVatPham(MyStepTpl(character).id) && Utlis.nextInt(100) < 30){ // tỉ lệ rớt vật phẩm nhiệm vụ
                isOk = true;
            }
        }

        if(isOk && character.infoChar.idTask != 15){
            ItemMap itemMap;
            Item item = new Item(MyStepTpl(character).idItem, false);
            itemMap = ItemMap.createItemMap(item, mob.cx, mob.cy);
            itemMap.idEntity = DataCache.getIDItemMap();
            itemMap.idChar = character.id;
            itemMap.isSystem = true;
            character.zone.createItemMap(itemMap, mob);
        }
        if(isDoneTask(character) || MyStepTpl(character) == null) return;

        if(MyStepTpl(character).idMob == mob.id){

            if(isGiaoVatPham(MyStepTpl(character).id)){
                if (isOk) {
                    PlusTask(character);
                }
            } else {
                PlusTask(character);
            }

        }
    }
    public void checkDoneKillMob2(Char character, Mob mob){
        if(character.infoChar.idTask >= DataCenter.gI().Task.length) return;

        if(isDoneTask(character) || MyStepTpl(character) == null) return;

        if(MyStepTpl(character).idMob == mob.id){

            if(isGiaoVatPham(MyStepTpl(character).id)){
                PlusTask(character);
            } else {
                PlusTask(character);
            }

        }
    }

    public void PlusTask(Char character){
        if(character.infoChar.idTask >= DataCenter.gI().Task.length) return;
        character.infoChar.requireTask++;
        if(character.infoChar.requireTask >= MyStepTpl(character).require){
            character.infoChar.requireTask = 0;
            character.infoChar.idStep++;
        }
        sendInfoTask(character);

    }
    public void nextStep(Char character){
        if(character.infoChar.idTask >= DataCenter.gI().Task.length) return;
        character.infoChar.requireTask = 0;
        character.infoChar.idStep++;
        sendInfoTask(character);

    }
    public void danhBai(Char character) {
        try {
            Mob mob2 = createMob(character, MyStepTpl(character).idMob, character.infoChar.hpFull*3,  character.cx, character.cy, false);
            for (int i = 0; i < character.zone.vecMob.size(); i++){
                Mob m = character.zone.vecMob.get(i);
                if(m.id == mob2.id && m.nameChar.equals(mob2.nameChar)){
                    return;
                }
            }
            addMobToZone(character, mob2);
        } catch (Exception ex) {
            Utlis.logError(TaskHandler.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }
    public void veNha(Char character) {
        try {
            // Kiểm tra nếu ExecutorService đang chạy, thì trả về ngay
            if (character.executor != null && !character.executor.isShutdown()) {
                return;
            }
            // Tạo một đối tượng Mob mới
            Mob mob2 = createMob(character, MyStepTpl(character).idMob, 999,  character.cx, character.cy, true);
            for (int i = 0; i < character.zone.vecMob.size(); i++){
                Mob m = character.zone.vecMob.get(i);
                if(m.id == mob2.id && m.nameChar.equals(mob2.nameChar)){
                    return;
                }
            }

            addMobToZone(character, mob2);
            // Tạo và thực thi luồng di chuyển Mob
            character.executor = Executors.newSingleThreadExecutor();
            character.executor.execute(() -> {
                moveMob(character, mob2);
                character.executor.shutdown(); // Dừng ExecutorService sau khi moveMob() hoàn thành
            });

        } catch (Exception ex) {
            Utlis.logError(TaskHandler.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    private Mob createMob(Char character, int id, int hp, int cx, int cy, boolean isName) {
        Mob mob2 = new Mob();
        mob2.createNewEffectList();
        mob2.idEntity = DataCache.getIDMob();
        mob2.id = id;
        mob2.hp = hp;
        mob2.hpGoc = hp;
        mob2.hpFull = hp;
        mob2.exp = hp/10;
        mob2.expGoc = hp/10;
        mob2.level = 1;
        mob2.levelBoss = 0;
        mob2.paintMiniMap = true;
        mob2.isBoss = false;
        mob2.status = 0;
        mob2.timeRemove = 300000+System.currentTimeMillis();
        if(isName){
            mob2.nameChar = character.infoChar.name;
        }
        mob2.setXY((short) cx, (short) cy);
        mob2.reSpawn();
        return mob2;
    }

    private void addMobToZone(Char character, Mob mob) {
        try {
            character.zone.vecMob.add(mob);
            character.zone.addMobToZone(mob);
        } catch (Exception ex) {
        Utlis.logError(TaskHandler.class, ex, "Da say ra loi:\n" + ex.getMessage());
       }
    }


    private void removeMobToZone(Char character, short idEntry) {
        try {
            for (int i = 0; i < character.zone.vecMob.size(); i++){
                Mob m = character.zone.vecMob.get(i);
                if(m.idEntity == idEntry){
                    character.zone.vecMob.remove(i);
                    break;
                }
            }
            Message msg = new Message((byte) 0);
            msg.writeShort(idEntry);
            character.zone.SendZoneMessage(msg);
        } catch (Exception ex) {
            Utlis.logError(TaskHandler.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }
    private void moveMobToZone(Char character, short idMob, short cx, short cy) {
        try {
            Message msg = new Message((byte) -1);
            msg.writeShort(idMob);
            msg.writeShort(cx);
            msg.writeShort(cy);
            character.zone.SendZoneMessage(msg);
        } catch (Exception ex) {
            Utlis.logError(TaskHandler.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }
    private void moveMob(Char character, Mob mob) {
        String strPositions = MyStepTpl(character).STR_ITEM;
        try {
            String[] pairs = strPositions.split("\\}, \\{");

            for (int i = 0; i < pairs.length; i++) {
                // Loại bỏ dấu ngoặc nhọn ở đầu và cuối cùng của cặp
                String pair = pairs[i].replaceAll("[{}]", "");
                // Tách thành hai phần bằng dấu phẩy
                String[] nums = pair.split(",");

                // Khởi tạo mảng 1 chiều để lưu cặp số nguyên
                int[] pairArray = new int[2];
                // Chuyển đổi từng phần tử trong cặp từ kiểu String sang kiểu int và gán vào mảng
                pairArray[0] = Integer.parseInt(nums[0].trim());
                pairArray[1] = Integer.parseInt(nums[1].trim());
                moveMobToZone(character, (short) mob.idEntity, (short) pairArray[0], (short) pairArray[1]);
                if(i == pairs.length-1){
                    removeMobToZone(character, (short) mob.idEntity);
                    PlusTask(character);
                }
                TimeUnit.SECONDS.sleep(3);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // Xử lý ngoại lệ hoặc dừng việc di chuyển Mob nếu cần
        }
    }



    public boolean isDoneTask(Char character) {
        return character.infoChar.idStep >= MyTaskTpl(character).vStep.size();
    }


    public void sendInfoTask(Char character){
        update(character);
        character.client.session.serivce.sendTask((short) character.infoChar.idTask, (byte) character.infoChar.idStep, (short) character.infoChar.requireTask);
    }

    public Step getStep(int idTask, int idStep){
        return (Step) DataCenter.gI().Task[idTask].vStep.get(idStep);
    }

    public boolean isGiaoVatPham(int idStep){
        switch (idStep){
            case 1:
            case 2:
            case 12:
            case 10:
            case 29:
                return true;
        }
        return false;
    }


}
