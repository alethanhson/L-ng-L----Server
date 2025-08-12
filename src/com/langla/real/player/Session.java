package com.langla.real.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.langla.data.DataCache;
import com.langla.data.DataCenter;
import com.langla.data.ItemOption;
import com.langla.data.SkillClan;
import com.langla.lib.Utlis;
import com.langla.real.bangxephang.BangXepHang;
import com.langla.real.bangxephang.Bxh_Tpl;
import com.langla.real.cho.ChoTemplate;
import com.langla.real.family.Family;
import com.langla.real.family.FamilyTemplate;
import com.langla.real.family.Family_Member;
import com.langla.real.group.Group;
import com.langla.real.group.GroupTemplate;
import com.langla.real.item.Item;
import com.langla.real.item.ItemHandle;
import com.langla.real.item.ItemShop;
import com.langla.real.khobau.KhoBau;
import com.langla.real.lucky.LuckyTpl;
import com.langla.real.map.Map;
import com.langla.real.map.Mob;
import com.langla.real.npc.NPC_Action;
import com.langla.real.other.Giftcode;
import com.langla.real.other.InputClient;
import com.langla.real.other.UseOtherFunctions;
import com.langla.real.other.nhacNhoConfirm;
import com.langla.real.task.TaskHandler;
import com.langla.real.trangbi.UpgradeTrangBi;
import com.langla.server.handler.IMessageHandler;
import com.langla.server.lib.Message;
import com.langla.server.lib.Reader;
import com.langla.server.lib.Writer;
import com.langla.server.main.Maintenance;
import com.langla.utlis.UTPKoolVN;
import java.net.Socket;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.regex.Pattern;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Session{

    @JsonIgnore
    public Client client;
    @JsonIgnore
    public Socket socket;
    @JsonIgnore

    public Reader reader;
    @JsonIgnore

    public Writer writer;
    @JsonIgnore
    public boolean isClean;
    @JsonIgnore
    public Thread threadSend;
    @JsonIgnore
    public Thread threadRecv;
    @JsonIgnore
    public Vector vecMessage = new Vector();
    @JsonIgnore

    public IMessageHandler handler;
    @JsonIgnore

    public Serivce serivce;
    @JsonIgnore

    public long timeConnect = System.currentTimeMillis()+60000;
    @JsonIgnore
    public Session(Client client) {

        this.client = client;
        try {
            this.socket = client.socket;
            this.reader = new Reader(this.socket);
            this.writer = new Writer(this.socket);
            threadSend = new Thread(()
                    -> {
                while (this.isConnected()) {
                    if(this.client == null || this.client.player == null || client.session == null){
                        if (timeConnect < System.currentTimeMillis()){
                            clean();
                            return;
                        }
                    }
                    try {
                        while (vecMessage.size() > 0) {
                            if (vecMessage.size() >= 20) {
                                Writer writer = new Writer();
                                int c = vecMessage.size();
                                while (vecMessage.size() > 0) {
                                    Message message = (Message) vecMessage.remove(0);
                                    byte cmd = message.cmd;
                                    byte[] data = message.getData();
                                    if (cmd == -86) {
                                        writer.writeByte(cmd);
                                    } else if (cmd == -84 || cmd == 123) {
                                        writer.writeByte(cmd);
                                        writer.dos.write(data);
                                    } else {
                                        if (!message.inflate) {
                                            if (data.length <= Short.MAX_VALUE) {
                                                writer.writeByte(cmd);
                                                writer.writeShort(data.length);
                                                writer.dos.write(data);
                                            } else {
                                                writer.writeByte(-128);
                                                writer.writeByte(cmd);
                                                writer.writeInt(data.length);
                                                writer.dos.write(data);
                                            }
                                        } else {
                                            data = Utlis.deflateByteArray(data);
                                            if (data.length <= Short.MAX_VALUE) {
                                                writer.writeByte(-80);
                                                writer.writeByte(cmd);
                                                writer.writeShort(data.length);
                                                writer.dos.write(data);
                                            } else {
                                                UTPKoolVN.Print("ERR LENGTH TO LONG");
                                            }
                                        }
                                    }
                                }
                                byte[] data = writer.baos.toByteArray();
                                data = Utlis.deflateByteArray(data);
                                this.writer.writeByte(-79);
                                this.writer.writeShort(c);
                                this.writer.writeShort(data.length);
                                for (int i = 0; i < data.length; i++) {
                                    this.writer.writeByte(data[i]);
                                }
                                this.writer.dos.flush();
                                break;
                            } else {
                                Message message = (Message) vecMessage.remove(0);
                                byte cmd = message.cmd;
                                byte[] data = message.getData();
                                if (cmd == -86) {
                                    writer.writeByte(cmd);
                                } else if (cmd == -84 || cmd == 123) {
                                    writer.writeByte(cmd);
                                    writer.dos.write(data);
                                } else {
                                    if (!message.inflate) {
                                        if (data.length <= Short.MAX_VALUE) {
                                            writer.writeByte(cmd);
                                            writer.writeShort(data.length);
                                            for (int i = 0; i < data.length; i++) {

                                                this.writer.writeByte(data[i]);
                                            }
                                        } else {
                                            writer.writeByte(-128);
                                            writer.writeByte(cmd);
                                            writer.writeInt(data.length);
                                            for (int i = 0; i < data.length; i++) {

                                                this.writer.writeByte(data[i]);
                                            }
                                        }
                                    } else {
                                        data = Utlis.deflateByteArray(data);
                                        if (data.length <= Short.MAX_VALUE) {
                                            writer.writeByte(-80);
                                            writer.writeByte(cmd);
                                            writer.writeShort(data.length);
                                            for (int i = 0; i < data.length; i++) {

                                                this.writer.writeByte(data[i]);
                                            }
                                        } else {
                                            UTPKoolVN.Print("ERR LENGTH TO LONG");
                                        }
                                    }
                                }
                            }

                            writer.dos.flush();
                        }
                        Thread.sleep(50);
                    } catch (Exception ex) {
                        clean();
                        return;
                    }
                }
            });

            threadRecv = new Thread(()
                    -> {
                while (this.isConnected()) {
                    try {
                        byte cmd = reader.readByte();
                        boolean isDeflate;
                        int length;
                        if (CMD_MOVE(cmd)) {
                            boolean when_move = cmd == 123 || cmd == 124 || cmd == 125;
                            boolean xy = cmd == 123 || cmd == -84;
                            boolean send_x = cmd == 125 || cmd == -82;
                            int x = 0;
                            int y = 0;
                            if (xy) {
                                x = reader.readShort();
                                y = reader.readShort();
                                client.mChar.setXY(x, y);
                            } else {
                                if (send_x) {
                                    x = reader.readByte();
                                } else {
                                    y = reader.readByte();
                                }
                                client.mChar.setXY(client.mChar.cx + x, client.mChar.cy + y);
                            }
                            if (x != 0 || y != 0) {
                                this.client.mChar.zone.updateXYChar(client, when_move);
                            }
                            continue;
                        } else if (cmd == -128) {
                            cmd = reader.readByte();
                            length = reader.readByte() << 24 & 255 | reader.readByte() << 16 & 255 | reader.readByte() << 8 & 255 | reader.readByte() << 0 & 255;
                            isDeflate = true;
                        } else if (cmd == -80) {
                            cmd = reader.readByte();
                            length = reader.readByte() << 8 & 255 | reader.readByte() << 0 & 255;
                            isDeflate = true;
                        } else {
                            length = reader.readByte() << 8 & 255 | reader.readByte() << 0 & 255;
                            isDeflate = false;
                        }
                        byte[] data = new byte[length];
                        int off = 0;

                        while (length > 0) {
                            int len;
                            if (length - 2048 <= 0) {
                                len = length;
                            } else {
                                len = 2048;
                            }

                            int available = reader.dis.available();
                            if (available == 0) {
                                Utlis.sleep(1L);
                            } else {
                                if (len > available) {
                                    len = available;
                                }

                                reader.read(data, off, len);
                                off += len;
                                length -= len;
                            }
                        }
                        if (isDeflate) {
                            data = Utlis.inflateByteArray(data);
                        }
                        if (handler != null) {
                            handler.readMessage(new Message((byte) cmd, data));
                        }
                    } catch (Exception ex) {
                        clean();
                        return;
                    }
                }
            });
            handler = new IMessageHandler() {
                private long long_38;
                @Override
                public void readMessage(Message msg) {
                    try {
                        UTPKoolVN.Debug("readMessage CMD: " + msg.cmd);

                        switch (msg.cmd) {
                            case -127:
                                String keyApp = msg.readUTF();
                                String path = new String(msg.read(), "UTF-8");
//                                UTPKoolVN.Print("KEY: "+keyApp+" pat: "+path);
                                break;
                            case -113:
                                readTypeClient(msg);
                                break;
                            case -122:
                                // login game
                                int mess127 = msg.readByte();
                                if(mess127 == -127){
                                    int idshortchar = msg.readByte();
                                    if(client.mChar == null || client.player.CharacterID <= 0) break;
                                    Session.this.serivce.sendChar();
                                    client.mChar.setXY(client.mChar.infoChar.cx,  client.mChar.infoChar.cy);
                                    Map.maps[client.mChar.infoChar.mapId].addChar(client);
                                    client.mChar.msgUpdateDataChar();
                                    // Kiểm tra nếu người chơi là admin
                                    if (client.player.role == 2) {

                                        int playerCount = PlayerManager.getInstance().player_size();
                                        // Tạo và gửi thông điệp
                                        Session.this.serivce.NhacNhoMessage("- Nick Admin\\n" +
                                                "- Số người chơi online hiện tại: " + playerCount + " người");
                                    } else {
                                        // Tạo và gửi thông điệp
                                        Session.this.serivce.NhacNhoMessage("Máy chủ Plus.\\nHiện đang test cách nhận vàng test Free -> Tới Trường Konoha -> Gặp NPC. Ngân Khố Quan và quy đổi tiền đã nạp sang vàng");
                                    }

                                    client.mChar.updateSendChar();
                                } else {
                                    nhacNhoConfirm.Confirm(client);
                                }

                                break;
                            case -123:
                            case -124:
                                readMessage123(msg);
                                break;
                            case -87:

                            case -3:
                                if(client.mChar == null) break;
                                short indexS = -1;
                                try {
                                    indexS = msg.readShort();
                                } catch (Exception e){

                                }
                                if(indexS != -1) ItemHandle.useItem(client.mChar, indexS);
                                break;
                            case -33:
                                if(client.mChar == null) break;
                                InputClient.Handler(msg, client);
                                break;
                            case 5:
                                if(client.mChar == null) break;
                                NPC_Action.NPCMenu(client, msg.readByte());
                                break;
                            case 7:
                                if(client.mChar == null) break;
                                Session.this.serivce.xoaTab(client.mChar);
                            case 38:
                            case 40:
//                                msg.readUTF();
                                break;
                            case 39:
                                if(client.mChar == null) break;

                                String nickmoi = msg.readUTF();
                                Char charmoi = PlayerManager.getInstance().getChar(nickmoi);
                                if(charmoi == null) {
                                    Session.this.serivce.ShowMessWhite("Người chơi đã offline.");
                                    break;
                                } else {
                                    GroupTemplate gr = Group.gI().getGroup(charmoi.infoChar.groupId);
                                    if(gr != null){

                                        if(gr.getKey().infoChar.groupLock){
                                            charmoi.client.session.serivce.xinGroup(client.mChar.infoChar.name);
                                            Session.this.serivce.ShowMessWhite("Đã gửi yêu cầu gia nhập nhóm");
                                            return;
                                        }
                                        if(gr.getChar(client.mChar.id) != null || client.mChar.infoChar.groupId != -1){
                                            Session.this.serivce.ShowMessWhite("Bạn đã ở trong nhóm");
                                            return;
                                        }
                                        if(gr.ListMember.size() >= 10){
                                            Session.this.serivce.ShowMessWhite("Nhóm đã đầy");
                                            return;
                                        }
                                        gr.addMember(client.mChar);
                                        Session.this.serivce.ShowMessWhite("Bạn đã gia tham gia nhóm của: "+charmoi.infoChar.name);

                                        Group.gI().LoadMemberALL(gr.id);
                                        client.mChar.chatGroup("đã tham gia nhóm từ lời mời của "+charmoi.infoChar.name);

                                    } else {
                                        Session.this.serivce.ShowMessWhite("Nhóm đã bị giải tán");
                                    }
                                }
                                break;
                            case 41:
                                if(client.mChar == null) break;
                                try {
                                    String name = msg.readUTF();

                                    Char doiphuong = client.mChar.zone.findCharInMap(name);

                                    if(doiphuong == null) break;

                                    if(client.mChar.infoChar.groupId != -1){ // đã có nhóm

                                        if (doiphuong.infoChar.groupId != -1){
                                            Session.this.serivce.ShowMessWhite("Đối phương đã có nhóm");
                                            return;
                                        }

                                        GroupTemplate gr = Group.gI().getGroup(client.mChar.infoChar.groupId);
                                        if(gr != null){
                                            if(gr.getChar(doiphuong.id) != null){
                                                Session.this.serivce.ShowMessWhite("Đối phương đã ở trong nhóm");
                                                return;
                                            }
                                            doiphuong.client.session.serivce.moiGroup(client.mChar.infoChar.name);
                                            Session.this.serivce.ShowMessWhite("Đã gửi lời mời tổ đội tới "+doiphuong.infoChar.name);
                                        }
                                    } else {

                                        if(doiphuong.infoChar.groupId != -1){ // đã có nhóm
                                            doiphuong.client.session.serivce.xinGroup(client.mChar.infoChar.name);
                                            Session.this.serivce.ShowMessWhite("Đã yêu cầu tham gia tổ đội tới "+doiphuong.infoChar.name);
                                        } else {
                                            Group.gI().newGroup(client);
                                            GroupTemplate gr = Group.gI().getGroup(client.mChar.infoChar.groupId);
                                            if(gr != null){
                                                doiphuong.client.session.serivce.moiGroup(client.mChar.infoChar.name);
                                                Session.this.serivce.ShowMessWhite("Đã gửi lời mời tổ đội tới "+doiphuong.infoChar.name);
                                            }
                                        }
                                    }
                                } catch (Exception x) {
                                    Group.gI().newGroup(client);
                                }
                                break;
                            case -24:
                                if(client.mChar == null) break;
                                client.mChar.cheTao(msg.readByte());
                                break;
                            case 42:
                                if(client.mChar == null) break;
                                Group.gI().lockGroup(client);
                                break;
                            case 43:
                                if(client.mChar == null) break;
                                Session.this.serivce.MeGroup();
                                break;
                            case 44:
                                if(client.mChar == null) break;
                                client.mChar.roiGroup();
                                break;
                            case 45:
                                if(client.mChar == null) break;
                                Session.this.serivce.searchGroup(client);
                                break;
                            case 46: // nhường trưởng nhóm
                                if(client.mChar == null) break;
                                String read_newkey = msg.readUTF();
                                Char newkey = PlayerManager.getInstance().getChar(read_newkey);
                                GroupTemplate gr = Group.gI().getGroup(client.mChar.infoChar.groupId);
                                if(gr != null && gr.getKey().id == client.mChar.id && gr.getChar(newkey.id) != null){
                                    if(gr.chuyenKey(client.mChar, newkey)){
                                        Group.gI().LoadMemberALL(client.mChar.infoChar.groupId);
                                        client.mChar.chatGroup("đã chuyển trưởng nhóm cho: "+read_newkey);
                                    }
                                }
                                break;
                            case 47: // đuổi khỏi nhóm
                                if(client.mChar == null) break;
                                String read_memduoi = msg.readUTF();
                                Char memduoi = PlayerManager.getInstance().getChar(read_memduoi);
                                GroupTemplate megroup = Group.gI().getGroup(client.mChar.infoChar.groupId);

                                if(megroup != null && megroup.getKey().id == client.mChar.id && megroup.getChar(memduoi.id) != null){
                                    if(megroup.duoiMem(memduoi)){
                                        Group.gI().LoadMemberALL(client.mChar.infoChar.groupId);
                                        client.mChar.chatGroup("đã đuổi "+read_memduoi+" ra khỏi nhóm");
                                        memduoi.client.session.serivce.ShowMessRed("Bạn bị đuổi ra khỏi nhóm");
                                        memduoi.client.session.serivce.outGroup();
                                    }
                                }
                                break;
                            case 20:
                                if(client.mChar == null) break;
                                int idSkills = msg.readShort();
                                int idPlayer = msg.readInt();
                                if(client.mChar.infoChar.lvPk > 20){
                                    client.session.serivce.ShowMessGold("Cấp PK của bạn quá cao không thể PK.");
                                    return;
                                }
                                client.mChar.zone.attackPlayer(client, idSkills, idPlayer);
                                //attack Mob
                                break;
                            case 61:
                                if(client.mChar == null) break;
                                int idSkill = msg.readShort();
                                int idMob = -1;
                                try {
                                    idMob = msg.readShort();
                                } catch (Exception x) {

                                }
                                if (idMob == -1) {
                                    HandleUseSkill.HanderSkillNotFocus(client, client.mChar.getSkillWithIdTemplate(idSkill));
                                    break;
                                }
                                client.mChar.zone.attackMob(client, idSkill, idMob);
                                //attack Mob
                                break;
                            case 74:
                                if(client.mChar == null) break;
                                try {
                                    KhoBau.gI().NhanThuong(client.mChar, msg.readByte());
                                } catch (Exception x){
                                    KhoBau.gI().NhanThuong(client.mChar, (short) 177);
                                }
                                break;
                            case 127:
                                //nextMap
                                if(client.mChar == null) break;
                                client.mChar.zone.map.nextMap(client);
                                break;
                            case -6:
                                if(client.mChar == null) break;
                                client.mChar.zone.openTabZone(client);
                                break;
                            case -7:
                                if(client.mChar == null) break;
                                client.mChar.zone.changeZone(client, msg.readByte());
                            case -15:
                                if(client.mChar == null) break;
                                try {
                                    if(client.mChar.info.idCharPk != -1) break;
                                    client.mChar.info.typePK = msg.readByte();
                                    Session.this.serivce.sendTypePK(client.mChar.id, client.mChar.info.typePK);
                                    Session.this.serivce.ShowMessGold("Bạn đã đổi trạng thái cờ.");
                                } catch (Exception e){

                                }
                                break;
                            case -18:
                                if(client.mChar == null) break;
                                client.mChar.removeEnemy(msg.readUTF());
                                break;
                            case -19:
                                if(client.mChar == null) break;
                                client.mChar.RaoBan(msg.readLong());
                                break;
                            case 59:
                                if(client.mChar == null) break;
                                client.mChar.zone.pickUpItem(client, msg.readShort());
                                break;
                            case 76:
                                if(client.mChar == null) break;
                                client.mChar.removeFr(msg.readUTF());
                                break;
                            case 79:
                                if(client.mChar == null) break;
                                String n = msg.readUTF();
                                Char c = PlayerManager.getInstance().getChar(n);
                                if(c == null){
                                    Session.this.serivce.ShowMessRed("Người chơi đã offline");
                                } else {
                                    if(n.equals(client.mChar.infoChar.name)) break;
                                    client.mChar.addFriend(c);
                                }
                                break;
                            case -35:
                                if(client.mChar == null) break;
                                byte typeTB = msg.readByte();
                                short indexTB = msg.readShort();
                                byte slItemBag = msg.readByte();
                                Item[] itemBag = new Item[slItemBag];

                                for (int i = 0; i < slItemBag; i++) {
                                    int index = msg.readShort();
                                    if (index >= 0 && index < client.mChar.arrItemBag.length) {
                                        itemBag[i] = client.mChar.getItemBagByIndex(index);
                                        if (itemBag[i] != null) {
                                            if (itemBag[i].getItemTemplate().type != 98) {
                                                itemBag[i] = null;
                                            }
                                        }
                                    }
                                }
                                UpgradeTrangBi.gI().handle(client.mChar, typeTB, indexTB, itemBag);
                                break;
                            case 106:
                                if(client.mChar == null) break;
                                byte typeNC = msg.readByte();
                                typeTB = msg.readByte();
                                indexTB = msg.readShort();
                                slItemBag = msg.readByte();
                                itemBag = new Item[slItemBag];

                                for (int i = 0; i < slItemBag; i++) {
                                    int index = msg.readShort();
                                    if (index >= 0 && index < client.mChar.arrItemBag.length) {
                                        itemBag[i] = client.mChar.getItemBagByIndex(index);
                                        if (itemBag[i] != null) {
                                            if (itemBag[i].getItemTemplate().id != 160) {
                                                itemBag[i] = null;
                                            }
                                        }
                                    }
                                }
                                UpgradeTrangBi.gI().NangCapBuaNo(client.mChar,typeNC, typeTB, indexTB, itemBag);
                                break;
                            case -56:
                                if(client.mChar == null) break;
                                int num = msg.readShort();
                                if (num != Utlis.getArrayListNotNull(client.mChar.arrItemBag, null).size()) {
                                    client.mChar.msgSendArrItemBag();
                                }
                                break;
                            case -50: // ghép cải trang
                                if(client.mChar == null) break;
                                byte sizeCT = msg.readByte();
                                List<Item> listCT = new ArrayList<>();
                                for (int i = 0; i < sizeCT; i++) {
                                    int type = msg.readByte();
                                    int index = msg.readShort();
                                    if (type == 0 && index >= 0 && index < client.mChar.arrItemBag.length) {
                                        Item item = client.mChar.getItemBagByIndex(index);
                                        if(item != null && item.getItemTemplate().type == 14){
                                            item.TYPE_TEMP = type;
                                            listCT.add(item);
                                        }
                                    } else if (type == 2 && index >= 0 && index < client.mChar.arrItemBody.length) {
                                        Item item = client.mChar.getItemBodyByIndex(index);
                                        if(item != null && item.getItemTemplate().type == 14){
                                            item.TYPE_TEMP = type;
                                            listCT.add(item);
                                        }
                                    } else if (type == 3 && index >= 0 && index < client.mChar.arrItemBody2.length) {
                                        Item item = client.mChar.getItemBody2ByIndex(index);
                                        if(item != null && item.getItemTemplate().type == 14){
                                            item.TYPE_TEMP = type;
                                            listCT.add(item);
                                        }
                                    }
                                }
                                if(listCT.size() <= client.mChar.infoChar.maxGhepCaiTrang) {
                                    client.mChar.ghepCaiTrang(listCT);
                                }
                                break;
                            case -46: //khảm ngọc
                                if(client.mChar == null) break;
                                byte typeTrangBi = msg.readByte();
                                short indexTrangBi = msg.readShort();

                                int sizeda = msg.readByte();
                                Item[] daKham = new Item[sizeda];
                                for (int i = 0; i < sizeda; i++) {
                                    int index = msg.readShort();
                                    if (index >= 0 && index < client.mChar.arrItemBag.length) {
                                        daKham[i] = client.mChar.getItemBagByIndex(index);
                                        if (daKham[i] != null) {
                                            if (!daKham[i].isNgocKham() || i > 0 && daKham[i-1].id != daKham[i].id) {
                                                daKham[i] = null;
                                            }
                                        }
                                    }
                                }

                                if(daKham.length > 0) client.mChar.khamNgoc(typeTrangBi, indexTrangBi, daKham);
                                break;
                            case -47: // gỡ khảm
                                if(client.mChar == null) break;
                                typeTrangBi = msg.readByte();
                                indexTrangBi = msg.readShort();
                                short typeDa = msg.readShort();
                                client.mChar.goKhamNgoc(typeTrangBi, indexTrangBi, typeDa);
                                break;
                            case -51: // confirm tách cải trang
                                if(client.mChar == null) break;
                                client.mChar.confirmTachCaiTrang(msg.readByte(),msg.readShort());
                                break;
                            case -52: // tách cải trang
                                if(client.mChar == null) break;
                                client.mChar.tachCaiTrang(msg.readByte(),msg.readShort());
                                break;
                            case 117:
                                if(client.mChar == null) break;
                                client.mChar.sortItem(msg.readByte());
                                break;
                            case 119:
                                if(client.mChar == null) break;
                                client.mChar.shellItemBag(msg.readShort());
                                break;
                            case 116:
                                if(client.mChar == null) break;
                                ItemHandle.useItem(client.mChar, msg.readShort());
                                break;
                            case 36:
                                if(client.mChar == null) break;
                                client.mChar.useItemBodyDuPhong(msg.readShort());
                                break;
                            case 37:
                                if(client.mChar == null) break;
                                client.mChar.itemBodyDuPhongToBag(msg.readByte());
                                break;
                            case -38:
                                if (System.currentTimeMillis() - long_38 >= 10000L) {
                                    long_38 = System.currentTimeMillis();
                                    client.session.serivce.sendArrMap(client.mChar.zone.map.mapID);
                                    client.session.serivce.sendIntoMap();
                                }
                                break;
                            case 112:
                                if(client.mChar == null) break;
                                client.mChar.itemExtendToBag(msg.readByte());
                                break;
                            case 113:
                                if(client.mChar == null) break;
                                client.mChar.itemBodyToBag(msg.readByte());
                                break;
                            case 118:
                                if(client.mChar == null) break;
                                client.mChar.tachItem(msg.readShort(), msg.readShort());
                                break;
                            case 111:
                                if(client.mChar == null) break;
                                client.mChar.vutItem(msg.readShort());
                                break;
                            case 21:
                                if(client.mChar == null) break;
                                client.mChar.chatPublic(msg.readUTF());
                                break;
                            case 22:
                                if(client.mChar == null) break;
                                boolean a = msg.readBoolean();
                                client.mChar.chatWord(msg.readUTF());
                                break;
                            case 25:
                                if(client.mChar == null) break;
                                client.mChar.chatFamily(msg.readUTF());
                                break;
                            case 26:
                                if(client.mChar == null) break;
                                client.mChar.chatGroup(msg.readUTF());
                                break;
                            case -95:
                                if(client.mChar == null) break;
                                client.mChar.msgDataBag();
                                break;
                            case 62:
                                if(client.mChar == null) break;
                                if (client.mChar.infoChar.idClass == 0) {
                                    client.mChar.msgUpdateDataChar();
                                    client.mChar.msgGetInfo(client);
                                    client.session.serivce.ShowMessRed("Chưa vào lớp");
                                    return;
                                }
                                int[] array = new int[4];
                                for (int i = 0; i < array.length; i++) {
                                    array[i] = msg.readShort();
                                }

                                if (client.mChar.infoChar.idClass == 1 || client.mChar.infoChar.idClass == 5) {

                                } else {
                                    if (array[0] != 0) {
                                        client.mChar.msgUpdateDataChar();
                                        client.mChar.msgGetInfo(client);
                                        return;
                                    }
                                }
                                int numz = 0;
                                for (int i = 0; i < client.mChar.arrayTiemNang.length; i++) {
                                    if (client.mChar.arrayTiemNang[i] > array[i]) {
                                        return;
                                    }
                                    numz = numz + (array[i] - client.mChar.arrayTiemNang[i]);
                                }
                                if (numz > client.mChar.infoChar.diemTiemNang) {
                                    return;
                                }
                                client.mChar.infoChar.diemTiemNang -= numz;
                                client.mChar.msgUpdateDataChar();
                                for (int i = 0; i < client.mChar.arrayTiemNang.length; i++) {
                                    client.mChar.arrayTiemNang[i] = array[i];
                                }
                                client.mChar.setUpInfo(true);
                                client.mChar.msgGetInfo(client);
                                if(client.mChar.infoChar.idTask == 8 && client.mChar.infoChar.idStep == 10) TaskHandler.gI().PlusTask(client.mChar);
                                break;
                            case 63:
                                if(client.mChar == null) break;
                                String nickGet = msg.readUTF();
                                if(nickGet.equals(client.mChar.infoChar.name)){
                                    client.mChar.msgGetInfo(client);
                                } else {
                                    Char getchars = PlayerManager.getInstance().getChar(nickGet);
                                    if(getchars == null){
                                        Session.this.serivce.ShowMessGold("Đối phương đã Offline không thể xem chi tiết thông tin");
                                    } else {
                                        getchars.msgGetInfo(client);
                                    }
                                }

                                break;
                            case 8:
                                if(client.mChar == null) break;
                                int idEntry = msg.readShort();
                                Mob modGet = client.mChar.zone.findMobInMap(idEntry);
                                if(modGet != null){
                                    Thread.sleep(modGet.getMobTemplate().timeThuHoach);
                                    client.session.serivce.xoaTab(client.mChar);
                                    Item itemMob = Item.getItemWithName(modGet.getMobTemplate().name, "Vật phẩm nhiệm vụ");
                                    if(modGet.id == 130)  itemMob = Item.getItemWithName(modGet.getMobTemplate().name, "Sử dụng sẽ làm đầu óc choáng váng, tinh thần bất định. Vật phẩm chỉ có tác dụng trong ải gia tộc.");
                                    if(itemMob != null){
                                        itemMob.amount = 1;
                                        client.mChar.addItem(itemMob, "Nhiệm vụ");
                                        client.mChar.msgAddItemBag(itemMob);
                                        client.mChar.zone.setDameMobDuoc(client, client.mChar.zone, modGet, 1, false);
                                    }
                                }
                                break;
                            case 9:
                                if(client.mChar == null) break;
                                TaskHandler.gI().huyNhiemVu(client.mChar);
                                break;
                            case 10:
                                if(client.mChar == null) break;
                                int slvp = 0;
                                try {
                                    slvp = msg.readByte();
                                } catch (Exception xx) {

                                }

                                if(slvp > 0){
                                    Item[] vatPham = new Item[slvp];
                                    for (int i = 0; i < slvp; i++) {
                                        int index = msg.readShort();
                                        if (index >= 0 && index < client.mChar.arrItemBag.length) {
                                            vatPham[i] = client.mChar.getItemBagByIndex(index);
                                        }
                                    }
                                    TaskHandler.gI().hoanThanhNhiemVu(client.mChar, vatPham);
                                } else {
                                    TaskHandler.gI().hoanThanhNhiemVu(client.mChar);
                                }
                                break;
                            case 11:
                                if(client.mChar == null) break;
                                TaskHandler.gI().nhanNhiemVu(client.mChar);
                                break;
                            case 12:
                                if(client.mChar == null) break;
                                TaskHandler.gI().noiChuyenXong(client.mChar);
                                break;
                            case 14:
                                if(client.mChar == null) break;
                                client.mChar.nangCapSkill(msg.readShort());
                                break;
                            case 126:
                                if(client.mChar == null) break;
                                client.mChar.focusSkill(msg.readShort());
                                break;
                            case 48:
                                if(client.mChar == null) break;
                                client.mChar.veMapMacDinh();
                                client.mChar.reSpawn();
                                break;
                            case 49:
                                if(client.mChar == null) break;
                                if(client.mChar.infoChar.vangKhoa > 0) {
                                    client.mChar.reSpawn();
                                    client.mChar.mineVangKhoa(1, true, true, "Hồi sinh");
                                } else {
                                    if(client.mChar.infoChar.vang > 0) {
                                        client.mChar.reSpawn();
                                        client.mChar.mineVang(1, true, true, "Hồi sinh");
                                    } else {
                                        Session.this.serivce.ShowMessRed("Không đủ vàng");
                                    }
                                }
                                break;
                            case 54:
                                if(client.mChar == null) break;
                                client.mChar.zone.openNpc(client, msg.readShort());
                                break;
                            case 53:
                                if(client.mChar == null) break;
                                int indexNpc = msg.readShort();
                                int index1 = msg.readByte();
                                int index2 = -1;

                                try {
                                    index2 = msg.readByte();
                                } catch (Exception xx) {

                                }
                                client.mChar.zone.selectNpc(client, indexNpc, index1, index2);
                                break;
                            case 108:
                                if(client.mChar == null) break;
                                boolean bac = msg.readBoolean();
                                int size = msg.readByte();
                                Item[] da = new Item[size];
                                for (int i = 0; i < size; i++) {
                                    int index = msg.readShort();
                                    if (index >= 0 && index < client.mChar.arrItemBag.length) {
                                        da[i] = client.mChar.getItemBagByIndex(index);
                                        if (da[i] != null) {
                                            if (!da[i].isDaCuongHoa()) {
                                                da[i] = null;
                                            }
                                        }
                                    }
                                }
                                client.mChar.ghepDa(bac, da);
                                break;
                            case 105:
                                if(client.mChar == null) break;
                                int type_item = msg.readByte();
                                int index_item = msg.readShort();
                                client.mChar.tachCuongHoa(type_item, index_item);
                                break;
                            case 107:
                                if(client.mChar == null) break;
                                type_item = msg.readByte();
                                index_item = msg.readShort();
                                int index_bua = msg.readShort();
                                size = msg.readByte();
                                da = new Item[size];
                                for (int i = 0; i < size; i++) {
                                    int index = msg.readShort();
                                    if (index >= 0 && index < client.mChar.arrItemBag.length) {
                                        da[i] = client.mChar.getItemBagByIndex(index);
                                        if (da[i] != null) {
                                            if (!da[i].isDaCuongHoa()) {
                                                da[i] = null;
                                            }
                                        }
                                    }
                                }
                                client.mChar.cuongHoa(type_item, index_item, index_bua, da);
                                break;
                            case 104: // dịch chuyển
                                if(client.mChar == null) break;
                                short typeItem1 = msg.readByte();
                                short indexItem1 = msg.readShort();
                                short typeItem2 = msg.readByte();
                                short indexItem2 = msg.readShort();
                                short indexItem3 = msg.readShort();

                                client.mChar.dichChuyen(typeItem1, indexItem1, typeItem2, indexItem2, indexItem3);
                                break;
                            case -20:
                                if(client.mChar == null) break;
                                int slitem = msg.readByte();
                                int sltinhthach = 0;

                                for (int i = 0; i < slitem; i++) {
                                    int type = msg.readByte();
                                    int index = msg.readShort();

                                    Item itemdoi = null;

                                    if (type == 0 && index >= 0 && index < client.mChar.arrItemBag.length) {
                                        itemdoi = client.mChar.getItemBagByIndex(index);
                                        if(!client.mChar.removeItemBag(itemdoi, true, "Đổi tinh thạch")) continue;
                                    } else if (type == 2 && index >= 0 && index < client.mChar.arrItemBody.length) {
                                        itemdoi = client.mChar.getItemBodyByIndex(index);
                                        if(!client.mChar.removeItemBodyByIndex(itemdoi.index, "Đổi tinh thạch")) continue;
                                        client.mChar.setUpInfo(true);
                                    } else if (type == 3 && index >= 0 && index < client.mChar.arrItemBody2.length) {
                                        itemdoi= client.mChar.getItemBody2ByIndex(index);
                                        if(!client.mChar.removeItemBody2ByIndex(itemdoi.index, "Đổi tinh thạch")) continue;
                                        client.mChar.setUpInfo(true);
                                    }
                                    if(itemdoi == null) continue;
                                    sltinhthach += itemdoi.getTinhThach();

                                    if (itemdoi.W() || itemdoi.X()) {
                                        int var1 = itemdoi.getItemTemplate().levelNeed / 10 * 100 - 100;
                                        if (itemdoi.getItemTemplate().levelNeed / 10 == 6) {
                                            var1 = 600;
                                        }
                                        if (itemdoi.X()) {
                                            var1 *= 2;
                                        }
                                        sltinhthach += var1;
                                    }

                                }

                                try {
                                    byte xx = msg.readByte();
                                } catch (Exception xx) {

                                }

                                if(sltinhthach > 0){
                                    Item itemAdd = new Item(160, true, sltinhthach);
                                    client.mChar.addItem(itemAdd, "Đổi tinh thạch");
                                    client.mChar.msgAddItemBag(itemAdd);
                                    Session.this.serivce.openTabTinhThach();
                                }
                                break;
                            case -22:
                                if(client.mChar == null) break;

                                int tabBangXepHang = msg.readByte();  // 0 là level, 1 là cao thủ

                                UTPKoolVN.Debug("BXH - "+tabBangXepHang);
                                int classNhanVat = msg.readByte();
                                if(tabBangXepHang == 0 ){
                                    ArrayList<Bxh_Tpl> list = BangXepHang.gI().getListCaoThu(classNhanVat);
                                    if(list != null) client.session.serivce.bangXepHang(list, (short) tabBangXepHang);
                                } else if(tabBangXepHang == 9){
                                    ArrayList<Bxh_Tpl> list = BangXepHang.gI().getListNapNhieu(classNhanVat);
                                    if(list != null) client.session.serivce.bangXepHang(list, (short) tabBangXepHang);
                                } else if(tabBangXepHang == 1){
                                ArrayList<Bxh_Tpl> list = BangXepHang.gI().getListCuaCai(classNhanVat);
                                if(list != null) client.session.serivce.bangXepHang(list, (short) tabBangXepHang);
                                } else if(tabBangXepHang == 2){
                                    ArrayList<Bxh_Tpl> list = BangXepHang.gI().getListTaiPhu(classNhanVat);
                                    if(list != null) client.session.serivce.bangXepHang(list, (short) tabBangXepHang);
                                } else if(tabBangXepHang == 4){
                                    ArrayList<Bxh_Tpl> list = BangXepHang.gI().getListGiaToc();
                                    if(list != null) client.session.serivce.bangXepHangGiaToc(list);
                                }  else if(tabBangXepHang == 8){
                                    ArrayList<Bxh_Tpl> list = BangXepHang.gI().getListCuongHoa(classNhanVat);
                                    if(list != null) client.session.serivce.bangXepHang(list, (short) tabBangXepHang);
                                }else {
                                    client.session.serivce.bangXepHangOFF();
                                }
                                break;
                            case -25:
                                if(client.mChar == null) break;
                                int indexItem = msg.readShort();
                                index1 = msg.readByte();
                                index2 = -1;

                                try {
                                    index2 = msg.readByte();
                                } catch (Exception xx) {

                                }
                                ItemHandle.useItemWithTab(client.mChar, indexItem, index1, index2);
                                break;
                            case 121:
                                if(client.mChar == null) break;
                                int idbuy = msg.readShort();
                                int slbuy = msg.readShort();
                                client.mChar.BuyShop(idbuy, slbuy);
                                break;
                            case 122:
                                if(client.mChar == null) break;
                                int index_1 = msg.readByte();
                                byte index_2 = -1; // id hệ
                                try {
                                    index_2 = (byte) (msg.readByte()-1);
                                } catch (Exception xx) {

                                }
                                if(index_1 == 54){ // gia tộc
                                    if(client.mChar.infoChar.idTask == 14 && client.mChar.infoChar.idStep == 0){
                                        TaskHandler.gI().PlusTask(client.mChar);
                                    }
                                    if(client.mChar.infoChar.familyId == -1){
                                        client.session.serivce.listGiaToc(client, "");
                                    } else {
                                        client.session.serivce.sendGiaToc(client.mChar);
                                    }
                                    break;
                                }
                                if(index_1 == 50){ // rương
                                   client.mChar.SendBox();
                                    break;
                                }
                                if(index_1 == 56){ // hoạt động
                                    client.session.serivce.openTabHoatDong();
                                    break;
                                }
                                if(index_1 == 72){ // nhiệm vụ
                                    client.session.serivce.openTabNhiemVu();
                                    break;
                                }
                                if(index_1 == 81){ // ghép đá
                                    client.session.serivce.openTabGhepDa();
                                    break;
                                }

                                if(index_1 == 82){ // ghép đá
                                    client.session.serivce.openTabCuongHoa();
                                    break;
                                }
                                if(index_1 == 83){ // ghép đá
                                    client.session.serivce.openTabBuaNo();
                                    break;
                                }
                                if(index_1 == 84){ // ghép đá
                                    client.session.serivce.openTabTachCuongHoa();
                                    break;
                                }
                                if(index_1 == 85){ // ghép đá
                                    client.session.serivce.openTabDichChuyen();
                                    break;
                                }
                                if(index_1 == 89){ // nạp vàng
                                    client.session.serivce.openTabNapTien();
                                    break;
                                }
                                if(index_1 == 87){ // ghép đá
                                    client.session.serivce.openTabKhamNgoc((byte) client.mChar.infoChar.levelKhamNgoc);
                                    break;
                                }
                                if(index_1 == 90){ // ghép đá
                                    client.session.serivce.openTabTachNgocKham();
                                    break;
                                }
                                if(index_1 == 94){ // ghép đá
                                    client.session.serivce.openTabGhepCaiTrang(client.mChar);
                                    break;
                                }
                                if(index_1 == 95){ // ghép đá
                                    client.session.serivce.openTabTachCaiTrang();
                                    break;
                                }

                                if(index_1 == 58){ // hoạt động

                                    break;
                                }

                                if(index_1 == 86){ // kho báu

                                    KhoBau.gI().Star(client.mChar);
                                    break;
                                }
                                if(index_1 == 88){ // phúc lợi
                                    client.session.serivce.openPhucLoi(client.mChar);
                                    break;
                                }
                                if(index_1 == 92 && index_2 == -1){ // nhiệm vụ
                                    TaskHandler.gI().checkDoneSeting(client.mChar);
                                    break;
                                }
                                if(index_2 != -1){
                                    client.session.serivce.SendShop(index_1, index_2);
                                } else {
                                    client.session.serivce.SendShop(index_1);
                                }
                                UTPKoolVN.Debug("INDEX1:"+index_1+" INDEX2:"+index_2);
                                break;
                            case 13: // view mob
                                if(client.mChar.zone == null) break;
                                short idmob = msg.readShort();
                                Mob mobFind = client.mChar.zone.findMobInMap(idmob);
                                if(mobFind == null) break;
                                Session.this.serivce.viewMob(mobFind);
                                break;
                            case 19: // cừu sát
                                if(client.mChar == null) break;
                                String str = msg.readUTF();
                                if(str.equals(client.mChar.infoChar.name)) break;
                                client.mChar.cuuSat(str);
                                break;
                            case 31: // chấp nhận tỷ võ
                                if(client.mChar == null) break;
                                str = msg.readUTF();
                                if(str.equals(client.mChar.infoChar.name)) break;
                                client.mChar.chapNhanTyVo(str);
                                break;
                            case 32: // tỷ võ
                                if(client.mChar == null) break;
                                str = msg.readUTF();
                                if(str.equals(client.mChar.infoChar.name)) break;
                                Char getPlayer = client.mChar.zone.findCharInMap(str);
                                if(getPlayer == null) break;
                                if(getPlayer.info.typePK == 1 && getPlayer.info.idCharPk != -1 || client.mChar.info.typePK == 1 && client.mChar.info.idCharPk != -1) break;
                                getPlayer.client.session.serivce.sendMoiTyVo(""+client.mChar.infoChar.name);
                                client.session.serivce.ShowMessWhite("Đã gửi lời mời tỷ võ tới "+str);
                                break;
                            case 34: // xem thông tin
                                if(client.mChar == null) break;
                                if(client.mChar.info.viewChar > System.currentTimeMillis()){
                                    Session.this.serivce.ShowMessRed("Tháo tác quá nhanh thử lại sau: " +
                                            String.format("%.1f", (client.mChar.info.viewChar - System.currentTimeMillis()) / 1000.0) + " giây");
                                    break;
                                }
                                String nameChar = msg.readUTF();
                                if(nameChar.equals(client.mChar.infoChar.name)){
                                    client.session.serivce.ShowMessRed("Không thể tự xem thông tin của mình");
                                    break;
                                }
                                Char getchars = PlayerManager.getInstance().getChar(nameChar);
                                if(getchars != null){
                                    Session.this.serivce.viewChar(getchars);
                                    getchars.client.session.serivce.ShowMessGold(""+client.mChar.infoChar.name+" đang xem thông tin của bạn.");
                                    client.mChar.info.viewChar = System.currentTimeMillis()+1000;
                                    client.session.serivce.ShowMessWhite(""+nameChar+" đang Online");
                                } else {
                                    Writer getData = CharDB.writerOffline(nameChar);
                                    if(getData != null){
                                        Session.this.serivce.viewCharOffline(getData);
                                        client.session.serivce.ShowMessRed(""+nameChar+" đã Offline");
                                    } else {
                                        client.session.serivce.ShowMessRed(""+nameChar+" đã Offline không thể xem thông tin");
                                    }
                                    client.mChar.info.viewChar = System.currentTimeMillis()+4000;
                                }
                                break;
                            case 35: // set item dự phòng
                                if(client.mChar == null) break;
                                byte type = msg.readByte();
                                client.mChar.setItemDuPhong(type);
                                break;
                            case 72: //kho báu
                                if(client.mChar == null) break;
                                byte sl = msg.readByte();
                                KhoBau.gI().Quay(client.mChar, sl);
                                break;
                            case 114: // box to bag
                                if(client.mChar == null) break;
                                client.mChar.boxToBag(msg.readShort());
                                break;
                            case 115: // bag to box
                                if(client.mChar == null) break;
                                client.mChar.bagToBox(msg.readShort());
                                break;
                            case 28: // inbox riêng
                                if(client.mChar == null) break;
                                String text = msg.readUTF();
                                try {
                                    String[] parts = text.split(": "); // Chia chuỗi dựa trên ": "
                                    String tennick = parts[0].substring(1);
                                    String noidung = parts[1];
                                    Char getchar = PlayerManager.getInstance().getChar(tennick);
                                    if(getchar == null){
                                        Session.this.serivce.ShowMessRed("Người chơi  đã offline.");
                                    } else if(tennick.equals(client.mChar.infoChar.name)) {
                                        Session.this.serivce.ShowMessRed("Không thể tự gửi cho mình");
                                    } else {
                                        getchar.client.session.serivce.NhanTinRieng(client.mChar.infoChar.name, noidung);
                                    }
                                } catch (Exception ex){
                                    Session.this.serivce.ShowMessRed("Nội dung không họp lệ vui lòng thử lại.");
                                }
                                break;
                            case 87: // gửi hộp thư
                                if(client.mChar == null) break;
                                String nguoinhan = msg.readUTF();
                                String chude = msg.readUTF();
                                String noidung = msg.readUTF();
                                int bacdinhkem = msg.readInt();
                                short item = msg.readShort();
                                if(chude.length() == 0 || nguoinhan.length() == 0 || noidung.length() == 0){
                                    Session.this.serivce.ShowMessRed("Chủ đề, người nhận, nội dung không được bỏ trống");
                                } else if(client.mChar.infoChar.bac < 10) {
                                    Session.this.serivce.ShowMessRed("Không đủ bạc");
                                } else if(nguoinhan.equals(client.mChar.infoChar.name)) {
                                    Session.this.serivce.ShowMessRed("Không thể tự gửi cho mình");
                                } else{
                                    Char getchar = PlayerManager.getInstance().getChar(nguoinhan);
                                    if(getchar == null){
                                        Session.this.serivce.ShowMessRed("Người nhận không tồn tại hoặc đã Offline");
                                    } else {
                                        client.mChar.guiThu(chude, getchar, noidung, bacdinhkem, item);
                                    }
                                }
                                break;
                            case 95: // nhận thư
                                if(client.mChar == null) break;
                                short idthunhan = msg.readShort();
                                client.mChar.nhanItemThu(idthunhan);
                                break;
                            case 96:
                                if(client.mChar == null) break;
                                short idthu = msg.readShort();
                                client.mChar.updateDocThu(idthu);
                                break;
                            case 81: // hoàn thành giao dịch
                                if(client.mChar == null) break;
                                client.mChar.DoneGiaoDich();
                                break;
                            case 82: // Khóa giao dịch
                                if(client.mChar == null) break;
                                int bacgd = msg.readInt(); // bạc
                                int sizeitem = msg.readByte(); // size index

                                List<Item> itembag = new ArrayList<>();

                                for (int i = 0; i < sizeitem; i++) {
                                    int index = msg.readShort();
                                    if (index >= 0 && index < client.mChar.arrItemBag.length) {
                                        Item items = client.mChar.getItemBagByIndex(index);
                                        if (items != null && !items.isLock) {
                                            itembag.add(items);
                                        }
                                    }
                                }

                                client.mChar.KhoaGiaoDich(bacgd, itembag);
                                break;
                            case 83: // hủy giao dịch
                                if(client.mChar == null) break;
                                client.mChar.HuyGiaoDich();
                                break;
                            case 85: // chấp nhận giao dịch
                                if(client.mChar == null) break;
                                client.mChar.ChapNhanGiaoDich();
                                break;
                            case 86:
                                if(client.mChar == null) break;
                                client.mChar.MoiGiaoDich(msg.readUTF());
                                break;
                            case 88:
                                if(client.mChar == null) break;
                                short sizexoa = msg.readShort();
                                for (int i = 0; i < sizexoa; i++){
                                    short idxoa = msg.readShort();
                                    client.mChar.removeThu(idxoa);
                                }
                                client.session.serivce.updateThu();
                                break;
                            case 98: // Mua
                                if(client.mChar == null) break;
                                if(Maintenance.isRuning){
                                    Session.this.serivce.ShowMessRed("Không thể thực hiện do: Máy chủ sắp bảo trì.");
                                    break;
                                }
                                client.mChar.BuyCho(msg.readLong());
                                break;
                            case 99: // đăng bán
                                if(client.mChar == null) break;
                                if(Maintenance.isRuning){
                                    Session.this.serivce.ShowMessRed("Không thể thực hiện do: Máy chủ sắp bảo trì.");
                                    break;
                                }
                                client.mChar.DangBanCho(msg.readShort(), msg.readByte(), msg.readInt());
                                break;
                            case 100: // treo market me
                                if(client.mChar == null) break;
                                client.session.serivce.sendDataCho_Me(client);
                                break;
                            case 101: // send market
                                if(client.mChar == null) break;
                                Item items = new Item(0, false);
                                client.session.serivce.sendDataCho(msg.readByte(),msg.readByte(),msg.readShort());
                                break;
                            default:
                                if(client.mChar == null) break;
                                UTPKoolVN.Debug("recv: " + msg.cmd);
                                Session.this.serivce.ShowMessRed("Chức năng sắp đuợc cập nhật.");
                                break;
                        }
                    } catch (Exception ex) {
                        Utlis.logError(Session.class, ex , "Da say ra loi:\n" + ex.getMessage());
                    } finally {
                        try {
                            msg.close();
                        } catch (Exception ex) {
                        }

                    }
                }
                @JsonIgnore
                private void readTypeClient(Message msg) {
                    try {
                        byte byte_1 = msg.readByte();
                        byte os = msg.readByte();
                        short widthScreen = msg.readShort();
                        short heightScreen = msg.readShort();
                        byte zoomLevelScreen = msg.readByte();
                        byte type_cfg_image = msg.readByte();
                        short ver1 = msg.readShort();
                        short ver2 = msg.readShort();
                        byte typeArr = msg.readByte();
                        int int_1 = msg.readInt();
                        int int_2 = msg.readInt();
                        int int_3 = msg.readInt();
                        int int_4 = msg.readInt();
                        short short_1 = msg.readShort();
                    } catch (Exception ex) {

                    }
                }
                @JsonIgnore
                private void readMessage123(Message msg) {
                    try {
                        msg.cmd = msg.readByte();
                        UTPKoolVN.Debug("readMessage123 MSG: "+msg.cmd);
                        switch (msg.cmd) {
                            case -127:
                                String username = msg.readUTF();
                                String password = msg.readUTF();
                                int ver1 = msg.readInt();
                                int ver2 = msg.readInt();

                                Player p = Player.login(Session.this, username, password);
                                if (p != null) {
                                    Player p2 = PlayerManager.getInstance().getPlayerLogin(p.id);
                                    if (p2 != null) {
                                        Session.this.serivce.NhacNhoMessage("Bạn đang đăng nhập tại máy khác. Hãy thử đăng nhập lại");
                                        if(PlayerManager.getInstance().isCheckSession(p2.client.session)){
                                            PlayerManager.getInstance().kickSession(p2.client.session);
                                        } else {
                                            PlayerManager.getInstance().remove(p2);
                                        }

                                    } else {
                                        if (!client.isSendArrData) {
                                            client.isSendArrData = true;
                                            Session.this.serivce.sendArrDataGame2();
                                        }
                                        PlayerManager.getInstance().put(p);
                                        client.player = p;
                                        Player.sendTabSelectChar(client);
                                    }
                                }
                                break;
                            case -128:
                                int selectChar = msg.readByte();
                                String name = msg.readUTF();
                                // new game
                                if(client.player.CharacterID > 0){
                                    Session.this.serivce.ShowMessRed("Để tránh clone mỗi nick chỉ được phép tạo 1 nhân vật");
                                    break;
                                } else if (!Pattern.matches("^[a-zA-Z0-9]{4,15}$", name)) {
                                    Session.this.serivce.ShowMessRed("Tên nhân vật phải từ 4-15 kí tự. Viết liền không dấu không có ký tự đặc biệt");
                                    break;
                                } else if (name.contains("adm")) {
                                    Session.this.serivce.ShowMessRed("Tên nhân vật có ký tự không cho phép");
                                    break;
                                } else if (CharDB.CheckNameChar(name)) {
                                    Session.this.serivce.ShowMessRed("Tên nhân vật này đã có người sử dụng");
                                    break;
                                }


                                if (client.mChar == null) {
                                    client.mChar = new Char(client);
                                    //setup info
                                    client.mChar.infoChar.name = name;
                                    client.mChar.infoChar.idNVChar = (byte) selectChar;
                                    client.mChar.infoChar.username = client.player.username;

                                    if (client.mChar.infoChar.idNVChar >= 5 && client.mChar.infoChar.idNVChar <= 8) {
                                        client.mChar.infoChar.gioiTinh = 0;
                                    } else {
                                        client.mChar.infoChar.gioiTinh = 1;
                                    }

//                                    client.mChar.infoChar.idClass = Char.getLopFormSelectChar(client.mChar.infoChar.idNVChar);
//                                    client.mChar.infoChar.idhe = client.mChar.infoChar.idClass;


                                    if (client.mChar.arraySkill == null) {
                                        client.mChar.arraySkill = client.mChar.skills_0.clone();
                                    }
                                    client.mChar.skillFight = client.mChar.arraySkill[0];

                                    client.mChar.setXY(150, 257);
                                    client.mChar.id = CharDB.InsertCharacter(client.mChar);

                                    if(client.mChar.id == -1){
                                        Session.this.serivce.ShowMessRed("Có lỗi sảy ra vui lòng thử lại sau.");
                                        client.mChar = null;
                                        break;
                                    }
                                    // new game quà tân thủ
                                    Item item = new Item(28, false);
                                    item.he = 0;
                                    item.strOptions = "2,50;3,20";
                                    client.mChar.addItem(item, "New game");
                                    client.mChar.addBacKhoa(100000000, false, false, "New game");
                                    client.mChar.addVangKhoa(10000, false, false, "New game");

                                    // open phải sửa
                                    item = new Item(329, false);
                                    item.amount = 100;
                                    client.mChar.addItem(item, "New game");
                                    //
                                    Player.UpdateCharID(client.player.id, client.mChar.id, client.mChar.infoChar.name);
                                    Session.this.serivce.sendChar();
                                    Map.maps[75].addChar(client);
                                    client.mChar.msgUpdateDataChar();
                                    Session.this.serivce.sendTextNewGame();
                                }
                                break;
                            case -17:
                                if(client.mChar == null) break;
                                byte typeTB = msg.readByte();
                                short indexTB = msg.readShort();
                                byte slItemBag = msg.readByte();
                                Item[] itemBag = new Item[slItemBag];

                                for (int i = 0; i < slItemBag; i++) {
                                    int index = msg.readShort();
                                    if (index >= 0 && index < client.mChar.arrItemBag.length) {
                                        itemBag[i] = client.mChar.getItemBagByIndex(index);
                                        if (itemBag[i] != null) {
                                            if (itemBag[i].getItemTemplate().type != 98) {
                                                itemBag[i] = null;
                                            }
                                        }
                                    }
                                }
                                UpgradeTrangBi.gI().upgradeLucDao(client.mChar, typeTB, indexTB, itemBag);
                                break;
                            case -20:
                                if(client.mChar == null) break;
                                client.mChar.goiPhanThan();
                                break;
                            case -21:
                                if(client.mChar == null) break;
                                int var2 = 100 + (client.mChar.infoChar.MaxLevelPhanThan - 10) * 50;
                                if(client.mChar.infoChar.vang <  var2){
                                    Session.this.serivce.ShowMessGold("Không đủ vàng");
                                    break;
                                }
                                if(client.mChar.infoChar.MaxLevelPhanThan >= 30){
                                    Session.this.serivce.ShowMessGold("Đã đạt giới hạn tối đa");
                                    break;
                                }
                                client.mChar.infoChar.MaxLevelPhanThan++;
                                client.mChar.mineVang(var2, true, true, "Mở giới hạn phân thân");
                                client.mChar.msgDataBag();
                                break;
                            case -44:
                                if(client.mChar == null) break;
                                String inputCaptcha = msg.readUTF();
                                if(inputCaptcha.equals(client.mChar.info.captcha)){
                                    client.session.serivce.ShowMessGold("Giải bùa uể thổ thành công");
                                    client.session.serivce.closeTab();
                                    client.mChar.info.captcha = "";
                                } else {
                                    client.session.serivce.ShowMessRed("Captcha không hợp lệ");
                                }
                                break;
                            case -48:
                                UpgradeTrangBi.gI().DoiBuaNo(client.mChar,msg.readByte(), msg.readShort());
                                break;
                            case -68:
                                if(client.mChar == null) break;
                                Family.gI().KhaiMoSkill(client.mChar, msg.readByte());
                                break;
                            case -106:
                                if(client.mChar == null) break;
                                Family.gI().TaoGiaToc(client.mChar, msg.readUTF());
                                break;
                            case -91:
                                if(client.mChar == null) break;
                                Family.gI().camChat(client.mChar, msg.readUTF());
                                break;
                            case -92:
                                if(client.mChar == null) break;
                                Family.gI().XinVaoGiaToc(client.mChar, msg.readUTF());
                                break;
                            case -94:
                                if(client.mChar == null) break;
                                Family.gI().rutQuy(client.mChar, msg.readInt());
                                break;
                            case -98:
                                if(client.mChar == null) break;
                                Family.gI().phatLuong(client.mChar,msg.readUTF(), msg.readInt());
                                break;
                            case -95:
                                if(client.mChar == null) break;
                                Family.gI().gopQuy(client.mChar, msg.readInt());
                                break;
                            case -96:
                                if(client.mChar == null) break;
                                Family.gI().roiRaToc(client.mChar);
                                break;
                            case -97:
                                if(client.mChar == null) break;
                                Family.gI().kickMember(client.mChar, msg.readUTF());
                                break;
                            case -99:
                                if(client.mChar == null) break;
                                Family.gI().setRole(client.mChar, msg.readUTF(), msg.readByte());
                                break;
                            case -100:
                                if(client.mChar == null) break;
                                Family.gI().dongYVaoToc(client.mChar, msg.readUTF());
                                break;
                            case -101:
                                if(client.mChar == null) break;
                                Family.gI().duyetMember(client.mChar, msg.readUTF());
                                break;
                            case -104:
                                if(client.mChar == null) break;
                                Family.gI().XinVaoGiaTocNameChar(client.mChar, msg.readUTF());
                                break;
                            case -105:
                                if(client.mChar == null) break;
                                Family.gI().MoiVaoGiaToc(client.mChar, msg.readUTF());
                                break;
                            case -93:
                                if(client.mChar == null) break;
                                Family.gI().setThongBao(client.mChar, msg.readUTF());
                                break;
                            case -34: // chát mic
//                                Session.this.serivce.test();
                                break;
                            case -85:
                                if(client.mChar == null) break;
                                client.mChar.thuVanMay();
                                break;
                            case -73:
                                if(client.mChar == null) break;
                                Session.this.serivce.sendInfo();
                                break;
                            case -63:
                                if(client.mChar == null) break;
                                if(client.mChar.phucLoi.isGoiHaoHoa) break;
                                if(client.mChar.infoChar.vang > 200){
                                    client.mChar.mineVang(200, true, true, "Mua gói hào hoa");
                                    client.mChar.phucLoi.isGoiHaoHoa = true;
                                    DataCenter.gI().phucLoiInfo.TongDauTu++;
                                    DataCenter.gI().updatePhucLoi(2, DataCenter.gI().phucLoiInfo.TongDauTu);
                                    Session.this.serivce.sendPhucLoi(client.mChar);
                                } else {
                                    Session.this.serivce.ShowMessRed("Không đủ vàng");
                                }
                                break;
                            case -62:
                                if(client.mChar == null) break;
                                if(client.mChar.phucLoi.isGoiChiTon) break;
                                if(client.mChar.infoChar.vang > 300){
                                    client.mChar.mineVang(300, true, true, "Mua gói chí tôn");
                                    client.mChar.phucLoi.isGoiChiTon = true;
                                    DataCenter.gI().phucLoiInfo.TongDauTu++;
                                    DataCenter.gI().updatePhucLoi(2, DataCenter.gI().phucLoiInfo.TongDauTu);
                                    Session.this.serivce.sendPhucLoi(client.mChar);
                                } else {
                                    Session.this.serivce.ShowMessRed("Không đủ vàng");
                                }
                                break;
                            case -65:
                                if(client.mChar == null) break;
                                if(client.mChar.phucLoi.timeTheVinhVinhVien >= 0) break;
                                if(client.mChar.infoChar.vang > 300){
                                    client.mChar.mineVang(300, true, true, "Mua gói thẻ vĩnh viễn");
                                    client.mChar.addVangKhoa(300, true, true, "Mua gói thẻ tháng");
                                    client.mChar.phucLoi.timeTheVinhVinhVien = 0L;
                                    DataCenter.gI().phucLoiInfo.TongSoLanMuaTheThang++;
                                    DataCenter.gI().updatePhucLoi(3, DataCenter.gI().phucLoiInfo.TongSoLanMuaTheThang);
                                    Session.this.serivce.sendPhucLoi(client.mChar);
                                } else {
                                    Session.this.serivce.ShowMessRed("Không đủ vàng");
                                }
                                break;
                            case -60:
                                if(client.mChar == null) break;

                                int slquay = DataCenter.gI().getVongQuayNap(client.mChar.phucLoi.diemTichLuyVongQuay, client.mChar.phucLoi.solanQuay);

                                if(slquay > 0){
                                    int vang = DataCenter.gI().getDiem(client.mChar.phucLoi.diemTichLuyVongQuay);

                                    int tilequay;
                                    int rand = Utlis.nextInt(100);

                                    if(rand < 3){
                                        tilequay = 5;
                                    } else if(rand < 8){
                                        tilequay = 4;
                                    } else if(rand < 15){
                                        tilequay = 3;
                                    } else if(rand < 25){
                                        tilequay = 2;
                                    } else if(rand < 40){
                                        tilequay = 1;
                                    } else {
                                        tilequay = 0;
                                    }


                                    if(tilequay == 1){
                                        vang *= 1.2;
                                    }  else if(tilequay == 2){
                                        vang *= 1.3;
                                    } else if(tilequay == 3){
                                        vang *= 1.4;
                                    } else if(tilequay == 4){
                                        vang *= 1.5;
                                    }  else if(tilequay == 5){
                                        vang *= 1.8;
                                    } else {
                                        vang *= 1.1;
                                    }
                                    Session.this.serivce.sendVongQuayNap((byte) client.mChar.phucLoi.solanQuay, (byte) tilequay, (int) vang);
                                    client.mChar.addVangKhoa(vang, false, false, "Vòng quay nạp");
                                    client.mChar.phucLoi.solanQuay++;
                                    if(client.mChar.phucLoi.solanQuay > 5) {
                                        client.mChar.phucLoi.solanQuay = 0;
                                        client.mChar.phucLoi.diemTichLuyVongQuay = 0;
                                    }
                                    Session.this.serivce.sendPhucLoi(client.mChar);

                                }
                                break;
                            case -66:
                                if(client.mChar == null) break;
                                if(client.mChar.phucLoi.timeTheThang > System.currentTimeMillis()) break;
                                if(client.mChar.infoChar.vang > 100){
                                    client.mChar.mineVang(100, true, true, "Mua gói thẻ tháng");
                                    client.mChar.addVangKhoa(100, true, true, "Mua gói thẻ tháng");
                                    client.mChar.phucLoi.timeTheThang = System.currentTimeMillis()+2592000000L;
                                    DataCenter.gI().phucLoiInfo.TongSoLanMuaTheThang++;
                                    DataCenter.gI().updatePhucLoi(3, DataCenter.gI().phucLoiInfo.TongSoLanMuaTheThang);
                                    Session.this.serivce.sendPhucLoi(client.mChar);
                                } else {
                                    Session.this.serivce.ShowMessRed("Không đủ vàng");
                                }
                                break;
                            case -70:
                                if(client.mChar == null) break;
                                client.mChar.nhanPhucLoi(msg.readShort());
                                break;
                            case -74:
                                if(client.mChar == null) break;
                                byte selectDanhHieu = msg.readByte();
                                if(client.mChar.listDanhHieu.size() > 0){
                                    client.mChar.infoChar.selectDanhHieu = selectDanhHieu;
                                    Session.this.serivce.sendDanhHieu(client.mChar);
                                }
                                break;
                            case -126:
                                serivce.ClosetoMainLogin();
                                break;
                            case -52: // dịch chuyển nhanh tới người chơi
                                if(client.mChar == null) break;
                                if(client.mChar.getItemBagById(569) == null){
                                    Session.this.serivce.ShowMessWhite("Cần có đồng hành phù để dịch chuyển");
                                    break;
                                }
                                String string = msg.readUTF();
                                Char chardichchuyen = PlayerManager.getInstance().getChar(string);
                                if(chardichchuyen == null){
                                    Session.this.serivce.ShowMessWhite("Người chơi đã offline");
                                } else {
                                    int idMap = chardichchuyen.infoChar.mapId;
                                    if (!DataCenter.gI().MapTemplate[idMap].notBlock) {
                                        client.mChar.setXY(chardichchuyen.cx, chardichchuyen.cy);
                                        Map.maps[idMap].addChar(client);
                                        client.mChar.chatPublic("Hế lô, bất ngờ chưa bạn già..!");
                                    }
                                }
                                break;
                            case -45:
                                if(client.mChar == null) break;
                                client.mChar.rutTien(msg.readByte(), msg.readInt());
                                break;
                            case -46:
                                if(client.mChar == null) break;
                                client.mChar.catTien(msg.readByte(), msg.readInt());
                                break;
                            case -58:
                                if(client.mChar == null) break;
                                client.mChar.selectCaiTrang(msg.readByte());
                                break;
                            case -82:
                                if(client.mChar == null) break;
                                client.mChar.addLengthBox();
                                break;
                            case -110: //giftcode
                                if(client.mChar == null) break;
                                Giftcode.useCode(msg.readUTF(), client);
                                break;
                            case -50: //nhận all thư
                                if(client.mChar == null) break;
                                client.mChar.nhanAllItemThu();
                                break;
                            case -18:
                                if(client.mChar == null) break;
                                UseOtherFunctions.moRongViThu(client.mChar, msg);
                                break;
                            case -26:
                                if(client.mChar == null) break;
                                UseOtherFunctions.xoaViThu(client.mChar, msg);
                                break;
                            case -28:
                                if(client.mChar == null) break;
                                UseOtherFunctions.nangCapViThu(client.mChar, msg);
                                break;
                            case -19:
                                if(client.mChar == null) break;
                                byte type = msg.readByte();
                                client.mChar.addEff_ItemBody(type);
                                break;
                            case -69:
                                if(client.mChar == null) break;
                                client.mChar.moRongKhamNgoc();
                                break;
                            default:
                                if(client.mChar == null) break;
                                UTPKoolVN.Debug("recv(123): " + msg.cmd);
                                Session.this.serivce.ShowMessRed("Chức năng sắp đuợc cập nhật.");
                                break;
                        }
                    } catch (Exception ex) {
                        Utlis.logError(Session.class, ex , "Da say ra loi:\n" + ex.getMessage());
                    }
                }
            };
            serivce = new Serivce();
        } catch (Exception ex) {
            Utlis.logError(Session.class, ex , "Da say ra loi:\n" + ex.getMessage());
            clean();
        }
    }
    @JsonIgnore
    public boolean isStart;
    @JsonIgnore
    public void start() {
        if (!isStart) {
            isStart = true;
            threadSend.start();
            threadRecv.start();
        }
    }
    @JsonIgnore
    public synchronized void sendMessage(Message message) {
        vecMessage.add(message);
    }
    @JsonIgnore
    private boolean CMD_MOVE(byte cmd) {
        return cmd == 123 || cmd == 124 || cmd == 125 || cmd == -82 || cmd == -83 || cmd == -84;
    }


    @JsonIgnore
    public boolean isConnected() {
        return !isClean && socket != null && socket.isConnected();
    }
    @JsonIgnore
    public void clean() {
        if (isClean) {
            return;
        }
        PlayerManager.getInstance().remove(this);
        isClean = true;
        isStart = false;
        if (reader != null) {
            try {
                reader.close();
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex , "Da say ra loi:\n" + ex.getMessage());
            }

            reader = null;
        }
        if (writer != null) {
            try {

                writer.close();
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex , "Da say ra loi:\n" + ex.getMessage());
            }

            writer = null;
        }
        if (threadSend != null) {
            try {
                threadSend.interrupt();
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex , "Da say ra loi:\n" + ex.getMessage());

            }
            threadSend = null;
        }
        if (threadRecv != null) {
            try {
                threadRecv.interrupt();
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex , "Da say ra loi:\n" + ex.getMessage());

            }
            threadRecv = null;
        }
        if (vecMessage != null) {
            try {
                vecMessage.clear();
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex , "Da say ra loi:\n" + ex.getMessage());
            }
        }

        if (socket != null) {
            try {
                socket.close();
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex , "Da say ra loi:\n" + ex.getMessage());
            }

            socket = null;
        }
        if (client != null) {
            try {
                client.clean();
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex , "Da say ra loi:\n" + ex.getMessage());
            }
        }

    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Serivce {

        public void ClosetoMainLogin() {
            try {
                Message msg = new Message((byte) -86);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void SendText(String text) {
            try {
                Message msg = new Message((byte) -8);
                msg.writeUTF(text);
                msg.writeUTF("");
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
//        public void OpenMenu(String text) {
//            try {
//                Message msg = new Message((byte) -8);
//                msg.writeUTF(text);
//                msg.writeUTF("Hii; Hix");
//                Session.this.sendMessage(msg);
//            } catch (Exception ex) {
//                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
//            }
//        }

        public void OpenMenu(int npcmenu,int typemenu, String text, String menu) {
            try {
                Message msg = new Message((byte) 5);
                msg.writeUTF(text);
                msg.writeUTF(menu);
                Session.this.client.NPCMenu = npcmenu;
                Session.this.client.TypeMenu = typemenu;
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void SendTextMenu(String text) {
            try {
                Message msg = new Message((byte) 5);
                msg.writeUTF(text);
                msg.writeUTF("");
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void SendShop(int idshop) {
            try {
                if((idshop == 8 || idshop == 20 || idshop == 19) && client.mChar.infoChar.idClass == 0){
                    Session.this.serivce.SendText("Ta không bán đồ cho người vô gia cư.");
                    return;
                }
                Message msg = new Message((byte) 122);
                List<ItemShop> shop = DataCenter.gI().shopTemplates.get(idshop);
                if(shop == null) return;
                if(DataCache.IdShop2.contains(idshop)) SendShop(idshop, (byte) 0);

                List<ItemShop> filteredShop = new ArrayList<>();
                for (ItemShop item : shop) {
                    if ((item.idclass == 0 || item.idclass == client.mChar.infoChar.idClass) && (item.sex == -1 || item.sex == client.mChar.infoChar.gioiTinh)) {
                        filteredShop.add(item);
                    }
                }
                msg.writeByte(idshop); // id loại shop
                msg.writeShort(filteredShop.size()); // sl item

                for (ItemShop item : filteredShop) {
                    msg.writeShort(item.id_buy); // id buy
                    msg.writeShort(item.id_item); // id item
                    msg.writeBoolean(item.isLock); // islock
                    msg.writeLong(item.expiry); // exp
                    msg.writeUTF(item.strOptions); // Str otp
                    msg.writeInt(item.gia_ban_tinh_thach); // tinh thạch buy
                    msg.writeInt(item.gia_ban_vang); // vàng buy
                    msg.writeInt(item.gia_ban_vang_khoa); // vàng khóa buy
                    msg.writeInt(item.gia_ban_bac_khoa); // bạc khóa buy
                    msg.writeInt(item.gia_ban_bac); // bạc buy
                }
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void SendShop(int idshop, byte he) {
            try {

                Message msg = new Message((byte) 122);
                List<ItemShop> shop = DataCenter.gI().shopTemplates.get(idshop);
                if(shop == null) return;
                List<ItemShop> filteredShop = new ArrayList<>();
                for (ItemShop item : shop) {
                    if ((item.idhe == he+1) && (item.idclass == 0 || item.idclass == client.mChar.infoChar.idClass) && (item.sex == -1 || item.sex == client.mChar.infoChar.gioiTinh)) {
                        filteredShop.add(item);
                    }
                }

                msg.writeByte(idshop); // id loại shop
                msg.writeByte(he+1); // hệ
                msg.writeShort(filteredShop.size()); // sl item

                for (ItemShop item : filteredShop) {
                    msg.writeShort(item.id_buy); // id buy
                    msg.writeShort(item.id_item); // id item
                    msg.writeLong(item.expiry); // exp
                    msg.writeUTF(item.strOptions); // Str otp
                    msg.writeInt(item.gia_ban_tinh_thach); // tinh thạch buy
                    msg.writeInt(item.gia_ban_vang); // vàng buy
                    msg.writeInt(item.gia_ban_vang_khoa); // vàng khóa buy
                    msg.writeInt(item.gia_ban_bac_khoa); // bạc khóa buy
                    msg.writeInt(item.gia_ban_bac); // bạc buy
                    msg.writeInt(item.moneyNew); // bạc buy
                }
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public void openPhucLoi(Char c) {
            try {
                //
                Writer writer = new Writer();
                writer.writeByte(88);
                c.writePhucLoi(writer);
                Session.this.sendMessage(new Message((byte) 122, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public void sendPhucLoi(Char c) {
            try {
                //
                Writer writer = new Writer();
                writer.writeByte(-61);
                c.writePhucLoi(writer);
                Session.this.sendMessage(new Message((byte) -123, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void openTabTinhThach() {
            try {

                Message msg = new Message((byte) 122);
                msg.writeByte(78);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void openTabHoatDong() {
            try {

                Message msg = new Message((byte) 122);
                msg.writeByte(56);
                msg.writeUTF("Làng Lá Plus\\n Chúc bạn chơi game vui vẻ.!");
                msg.writeByte(18);
                for (int i = 0; i < DataCache.camNang.length; i++){
                    msg.writeUTF(DataCache.camNang[i]);
                }
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public void openTabLucky() {
            try {

                Message msg = new Message((byte) 122);
                msg.writeByte(74);
                msg.writeShort(DataCenter.gI().DataLucky.size());
                for (int i = 0; i < DataCenter.gI().DataLucky.size(); i++){
                    LuckyTpl luckyTpl = DataCenter.gI().DataLucky.get(i);
                    Item itemLuck = new Item(luckyTpl.idItem, true, luckyTpl.amount);
                    itemLuck.expiry = luckyTpl.expiry;
                    itemLuck.strOptions = luckyTpl.STROP;
                    itemLuck.write(msg.writer);
                }
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void openTabBuaNo() {
            try {

                Message msg = new Message((byte) 122);
                msg.writeByte(83);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void openTabNhiemVu() {
            try {

                Message msg = new Message((byte) 122);
                msg.writeByte(72);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void openTabDichChuyen() {
            try {

                Message msg = new Message((byte) 122);
                msg.writeByte(85);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void openTabNapTien() {
            try {


                Writer writer = new Writer();
                writer.writeByte(89);
                Item item = new Item(277, true, 20);
                item.write(writer);

                item = new Item(784, true);
                item.addItemOption(new ItemOption(143, 50));
                item.addItemOption(new ItemOption(209, 25));
                item.write(writer);

                item = new Item(416, true);
                item.write(writer);

                item = new Item(445, true);
                item.write(writer);

                Session.this.sendMessage(new Message((byte) 122, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void openTabLuyenBiKip() {
            try {

                Message msg = new Message((byte) 122);
                msg.writeByte(76);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public void openTabLucDao() {
            try {

                Message msg = new Message((byte) 122);
                msg.writeByte(100);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void openTabKhamNgoc(byte sl) {
            try {

                Message msg = new Message((byte) 122);
                msg.writeByte(87);
                msg.writeByte(sl);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void openTabTachNgocKham() {
            try {

                Message msg = new Message((byte) 122);
                msg.writeByte(90);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void openTabGhepCaiTrang(Char c) {
            try {
                if(c.infoChar.maxGhepCaiTrang > 17) sendMaxGhepCaiTrang(c);
                Message msg = new Message((byte) 122);
                msg.writeByte(94);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public void sendMaxGhepCaiTrang(Char c) {
            try {
                Message msg = Message.c((byte) -25);
                msg.writeByte(c.infoChar.maxGhepCaiTrang);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void openTabTachCaiTrang() {
            try {

                Message msg = new Message((byte) 122);
                msg.writeByte(95);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public void open122(byte caseId) {
            try {

                Message msg = new Message((byte) 122);
                msg.writeByte(caseId);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void SendKhoBau() {
            try {

                Message msg = new Message((byte) 122);
                msg.writeByte(86); // id loại shop
                msg.writeBoolean(false);
                msg.writeInt(client.mChar.infoChar.levelCheTao);

                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public void sendTask(short idTask, byte idStep, short require) {
            try {

                Message msg = new Message((byte) 103);
                msg.writeShort(idTask);
                msg.writeByte(idStep);
                msg.writeShort(require);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void openDoiBiKip() {
            try {

                Message msg = new Message((byte) 122);
                msg.writeByte(75); // id loại shop
                msg.writeBoolean(false);
                msg.writeInt(client.mChar.infoChar.levelCheTao);

                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void sendQuayKhoBau(byte sao, byte itemchay, byte idloaithuong, boolean ruong) {
            try {

                Message msg = new Message((byte) 73);
                msg.writeByte(sao); // số sao 0-6, -2 là không có gì
                msg.writeByte(itemchay); // vật phẩm thưởng chạy dừng 0-21
                msg.writeByte(idloaithuong); // vp thưởng , 1 hỏa lực, 2 bạc, 3 vàng, 4 đá
                msg.writeBoolean(ruong);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void ResetKhoBau() {
            try {

                Message msg = new Message((byte) 74);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void sendGiaToc(Char c) {
            try {

                Message msg = new Message((byte) 122);
                msg.writeByte(54);

                FamilyTemplate giaToc = Family.gI().getGiaToc(c);

                if(giaToc == null) return;
                msg.writeUTF(giaToc.name); // tên gia tộc
                msg.writeUTF("");
                msg.writeLong(giaToc.info.timeCreateLog);
                msg.writeShort(giaToc.info.level); // level
                msg.writeInt(giaToc.info.exp);
                msg.writeInt(giaToc.info.getMaxExp());
                msg.writeInt(giaToc.info.congHienTuan);
                msg.writeInt(giaToc.info.nganSach);
                msg.writeUTF(giaToc.info.thongBao); // thông báo
                msg.writeByte(giaToc.info.soLanMoAi);

                msg.writeShort(giaToc.listMember.size()); // sl tv

                Comparator<Family_Member> roleComparator = new Comparator<Family_Member>() {
                    @Override
                    public int compare(Family_Member member1, Family_Member member2) {
                        return Integer.compare(member2.role, member1.role);
                    }
                };
                giaToc.listMember.sort(roleComparator);
                for (int i = 0; i < giaToc.listMember.size(); i++) {
                    Family_Member get = giaToc.listMember.get(i);
                    msg.writeByte(get.role);
                    msg.writeByte(get.infoChar.idClass);
                    msg.writeByte(get.infoChar.idNVChar);
                    msg.writeShort(get.infoChar.level);
                    msg.writeUTF(get.infoChar.name);
                    msg.writeInt(get.congHien);
                    msg.writeInt(get.congHienTuan);
                    Char getChar = PlayerManager.getInstance().getChar(get.idCharacter);
                    msg.writeBoolean(getChar != null);
                    msg.writeBoolean(get.isCamChat);
                }
                msg.writeShort(giaToc.dataLog.size());
                for (int i = giaToc.dataLog.size() - 1; i >= 0; i--) {
                    msg.writeUTF(giaToc.dataLog.get(i).log);
                }

                msg.writeShort(giaToc.litsItem.size());
                for (int i = 0; i < giaToc.litsItem.size(); i++){
                    Item get = giaToc.litsItem.get(i);
                    get.write(msg.writer);
                }
                msg.writeByte(giaToc.listSkill.size());
                for (int i = 0; i < giaToc.listSkill.size(); i++){
                    SkillClan sk = giaToc.listSkill.get(i);
                    msg.writeByte(sk.id);
                }
                msg.writeLong(0);
                msg.writeShort(giaToc.info.soLuongThuNapTrongNgay);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void listGiaToc(Client client,  String name) {
            try {

                Message msg = new Message((byte) 122);

                msg.writeByte(91);

                if(name.length() > 0){
                    Vector<FamilyTemplate> listFamilySearch = new Vector<>();
                    for (int i = 0; i < Family.gI().listFamily.size(); i++){
                        FamilyTemplate f = Family.gI().listFamily.get(i);
                        if(f.name.contains(name)){
                            listFamilySearch.add(f);
                        }
                    }
                    if(listFamilySearch.size() == 0){
                        client.session.serivce.ShowMessGold("Không tìm thấy gia tộc này");
                        return;
                    }
                    client.session.serivce.closeTab();
                    listFamilySearch.sort(Comparator.comparingInt(f -> -f.info.level));
                    msg.writeShort(listFamilySearch.size());
                    for (FamilyTemplate f : listFamilySearch) {
                        msg.writeUTF(f.info.nameKey);
                        msg.writeInt(f.info.level);
                        msg.writeInt(f.info.exp);
                        msg.writeInt(f.listMember.size());
                        msg.writeInt(f.info.maxMember);
                        msg.writeUTF(f.name);
                    }

                } else {
                    Vector<FamilyTemplate> listFamilySearch = new Vector<>();
                    for (int i = 0; i < Family.gI().listFamily.size(); i++){
                        FamilyTemplate f = Family.gI().listFamily.get(i);
                        if(PlayerManager.getInstance().getChar(f.info.nameKey) != null){
                            listFamilySearch.add(f);
                        }
                    }

                    listFamilySearch.sort(Comparator.comparingInt(f -> -f.info.level));
                    msg.writeShort(listFamilySearch.size());
                    for (FamilyTemplate f : listFamilySearch) {
                        msg.writeUTF(f.info.nameKey);
                        msg.writeInt(f.info.level);
                        msg.writeInt(f.info.exp);
                        msg.writeInt(f.listMember.size());
                        msg.writeInt(f.info.maxMember);
                        msg.writeUTF(f.name);
                    }
                }

                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void listGiaTocTest() {
            try {

                Message msg = new Message((byte) 122);

                msg.writeByte(91);
                msg.writeBoolean(false);
                msg.writeByte(1);


                msg.writeUTF("Test");
                msg.writeInt(1);
                msg.writeInt(2);
                msg.writeInt(3);
                msg.writeInt(4);
                msg.writeUTF("xxxx");
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void bangXepHang(ArrayList<Bxh_Tpl> list, short tab) {
            try {

                Message msg = new Message((byte) -22);

                msg.writeBoolean(true);
                msg.writeByte(list.size());

                for (int i = 0; i < list.size(); i++){
                    msg.writeByte(i);
                    if(tab == 4){
                        msg.writeUTF(list.get(i).GiaTocInfo.nameKey);
                    } else {
                        msg.writeUTF(list.get(i).infoChar.name);
                    }

                    if(tab == 0) {
                        msg.writeShort(list.get(i).infoChar.level);
                        msg.writeLong(list.get(i).infoChar.exp);
                    } else if(tab == 1) {
                        msg.writeShort(list.get(i).infoChar.level);
                        msg.writeLong(list.get(i).infoChar.cuaCai);
                    } else if(tab == 2) {
                        msg.writeShort(list.get(i).infoChar.level);
                        msg.writeLong(list.get(i).infoChar.taiPhu);
                    } else if(tab == 4) {
                        msg.writeShort(list.get(i).GiaTocInfo.level);
                        msg.writeLong(list.get(i).CountMem);
                    } else if(tab == 8){
                        msg.writeShort(list.get(i).infoChar.level);
                        msg.writeLong(list.get(i).infoChar.tongCuongHoa);
                    } else if(tab == 9){
                        msg.writeShort(list.get(i).infoChar.level);
                        msg.writeLong(list.get(i).infoChar.tongVangNap);
                    }
                    if(tab == 4) {
                        msg.writeByte(list.get(i).GiaTocInfo.maxMember);
                        msg.writeUTF(list.get(i).NameGiaToc);// gia tộc
                    } else {
                        msg.writeByte(list.get(i).infoChar.idClass);
                        if (list.get(i).infoChar.familyId != -1) {
                            msg.writeUTF(list.get(i).infoChar.familyName);// gia tộc
                        } else {
                            msg.writeUTF("Chưa có gia tộc");// gia tộc
                        }
                    }
                }
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public void bangXepHangGiaToc(ArrayList<Bxh_Tpl> list) {
            try {

                Message msg = new Message((byte) -30);
                msg.writeBoolean(true);
                msg.writeByte(list.size());

                for (int i = 0; i < list.size(); i++){
                    msg.writeUTF(list.get(i).GiaTocInfo.nameKey);
                    msg.writeInt(list.get(i).GiaTocInfo.level);

                    int exp = list.get(i).GiaTocInfo.exp;

                    long ptLevel = ((long) exp * 100) / list.get(i).GiaTocInfo.getMaxExp();
                    msg.writeInt((int) ptLevel);
                    msg.writeInt(list.get(i).CountMem);
                    msg.writeInt(list.get(i).GiaTocInfo.maxMember);
                    msg.writeUTF(list.get(i).NameGiaToc);// gia tộc
                }
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }


        public void bangXepHangOFF() {
            try {

                Message msg = new Message((byte) -22);

                msg.writeBoolean(false);
                msg.writeByte(0);


                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void OpenTabGiaoDich(String nick) {
            try {
                Message msg = new Message((byte) 122);
                msg.writeByte(59); // id loại shop
                msg.writeUTF(nick);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void NhacNhoMessage(String text) {
            try {
                Message msg = new Message((byte) -110);
                msg.writeUTF(text);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public void ShowMessWhite(String text) {
            try {
                Message msg = new Message((byte) -107);
                msg.writeUTF(text);
                msg.writeBoolean(true);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void sendMoiTyVo(String text) {
            try {
                Message msg = new Message((byte) 32);
                msg.writeUTF(text);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void startTyVo(int id, int idPlayer) {
            try {
                Message msg = new Message((byte) 31);
                msg.writeInt(id);
                msg.writeByte(1);
                msg.writeInt(idPlayer);
                msg.writeByte(1);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void endTyVo(Char c, int id, int idPlayer, byte type) {
            try {
                Message msg = new Message((byte) 29);
                msg.writeByte(type);
                msg.writeInt(id);
                msg.writeInt(idPlayer);
               c.zone.SendZoneMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void startCuuSat(int id, int idPlayer) {
            try {
                Message msg = new Message((byte) 19);
                msg.writeInt(id);
                msg.writeInt(idPlayer);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void huyCuuSat(int id, boolean isThongbao) {
            try {
                Message msg = new Message((byte) 18);
                msg.writeInt(id);
                msg.writeBoolean(isThongbao);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void ShowMessRed(String text) {
            try {
                Message msg = new Message((byte) -105);
                msg.writeUTF(text);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void msgUpdateGuiThu(int bac, int backhoa, short index) {
            try {
                Message msg = new Message((byte) 87);
                msg.writeInt(bac); // bạc
                msg.writeInt(backhoa); // bạc khóa
                msg.writeShort(index); // index item
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void updateThu() {
            try {
                Writer writer = new Writer();
                client.mChar.writeThu(writer);
                Session.this.sendMessage(new Message((byte) 97, writer));

            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void ShowMessGold(String text) {
            try {
                Message msg = new Message((byte) -106);
                msg.writeUTF(text);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void sendBuaUeTho(String nameChar, Char _myChar) {
            try {
                Message msg = Message.c((byte) -44);
                msg.writeUTF(nameChar);
                // Tạo captcha
                String captcha = UTPKoolVN.generateCaptcha();
                _myChar.info.captcha = captcha;
                // Tạo hình ảnh từ captcha
                byte[] imageData = UTPKoolVN.createImage(captcha);
                msg.writer.write(imageData);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void sendArrDataGame2() {
            try {
                Message msg = Message.d((byte) -113);
                msg.writer.dos.write(DataCenter.gI().writerArrDataGame2.baos.toByteArray());
                msg.inflate = true;
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void closeTab() {
            try {
                Message msg = Message.c((byte) -43);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void test() {
            try {
                Message msg = Message.c((byte) -34);
                msg.writeBoolean(true);
                msg.writeUTF("Hii");
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void xinVaoGiaToc(Char c) {
            try {
                Message msg = Message.c((byte) -104);
                msg.writeUTF(c.infoChar.name);
                msg.writeShort(c.infoChar.level);
                msg.writeInt(c.infoChar.taiPhu);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void moiVaoGiaToc(Char c, byte role) {
            try {
                Message msg = Message.c((byte) -105);
                msg.writeUTF(c.infoChar.name);
                msg.writeUTF(c.infoChar.familyName);
                msg.writeByte(role);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void sendQuayLucky(short idItem, int Amount) {
            try {
                Message msg = Message.c((byte) -85);
                msg.writeShort(idItem);
                msg.writeInt(Amount);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void sendArrMap(int mapID) {
            try {
                Message msg = Message.f((byte) 3);
                msg.writeShort(mapID);
                msg.writer.dos.write(DataCenter.gI().MapTemplate[mapID].arrMap);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void sendIntoMap() {
            try {
                sendIntoGame();
                Message msg = new Message((byte) -103);
                client.mChar.zone.write(client, msg.writer);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        private void sendIntoGame() {
            try {
                Session.this.sendMessage(new Message((byte) -104));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void sendChar() {
            try {
                Message msg = Message.d((byte) -127);
                client.mChar.writeMe(msg.writer);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        private void sendInfo() {
            try {
                Message msg = Message.c((byte) -73);
                client.mChar.writeInfo(msg.writer);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void sendInfoGiaTocAllChar(Char c) {
            try {
                Message msg = new Message((byte) -88);
                msg.writeInt(c.id);
                msg.writeUTF(c.infoChar.familyName);
                if(c.infoChar.familyName.length() > 0){
                    FamilyTemplate giatoc = Family.gI().getGiaToc(c);
                    if(giatoc != null){
                        Family_Member member = Family.gI().getMe(c, giatoc);
                        if(member != null){
                            msg.writeUTF(c.infoChar.familyName);
                            msg.writeByte(member.role);
                        }
                    }
                }
                c.zone.SendZoneMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void sendInfoGiaTocToMe(Char c) {
            try {
                Message msg = new Message((byte) -88);
                msg.writeInt(c.id);
                msg.writeUTF(c.infoChar.familyName);
                if(c.infoChar.familyName.length() > 0){
                    FamilyTemplate giatoc = Family.gI().getGiaToc(c);
                    if(giatoc != null){
                        Family_Member member = Family.gI().getMe(c, giatoc);
                        if(member != null){
                            msg.writeUTF("");
                            msg.writeByte(member.role);
                        }
                    }
                }
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void sendTypePK(int id, byte type) {
            try {
                Message msg = new Message((byte) -15);
                msg.writeInt(id);
                msg.writeByte(type);
                Session.this.client.mChar.zone.SendZoneMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void removeItemBag(short index) {
            try {
                // type = 0 (bag),1 (item box) 2 (body), 3(body2), 4 (arrItemExtend)
                Message msg = new Message((byte) -16);
                msg.writeByte(0);
                msg.writeShort(index);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void removeItemBox(short index) {
            try {
                // type = 0 (bag),1 (item box) 2 (body), 3(body2), 4 (arrItemExtend)
                Message msg = new Message((byte) -16);
                msg.writeByte(1);
                msg.writeShort(index);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void removeItemBody(short index) {
            try {
                // type = 0 (bag),1 (item box) 2 (body), 3(body2), 4 (arrItemExtend)
                Message msg = new Message((byte) -16);
                msg.writeByte(2);
                msg.writeShort(index);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void removeItemBody2(short index) {
            try {
                // type = 0 (bag),1 (item box) 2 (body), 3(body2), 4 (arrItemExtend)
                Message msg = new Message((byte) -16);
                msg.writeByte(3);
                msg.writeShort(index);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void removeItemExtend(short index) {
            try {
                // type = 0 (bag),1 (item box) 2 (body), 3(body2), 4 (arrItemExtend)
                Message msg = new Message((byte) -16);
                msg.writeByte(4);
                msg.writeInt(index);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        private void sendTabSelectChar() {
            try {
                Message msg = Message.d((byte) -128);
                msg.writeByte(0);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void addCharIntoMap(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) -102, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void removeCharIntoMap(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) -101, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void updateXYChar(Writer writer, boolean when_move) {
            try {
                Message msg = null;
                if (when_move) {
                    msg = new Message((byte) 123, writer);
                } else {
                    msg = new Message((byte) -84, writer);
                }
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void sendHpMob(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 52, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void setXYAllZone(Client client) {
            try {
                Message msg = new Message((byte) 102);
                msg.writeInt(client.mChar.id);
                msg.writeShort(client.mChar.cx);
                msg.writeShort(client.mChar.cy);
                client.mChar.zone.SendZoneMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Map.class, ex , "Da say ra loi updateXYChar:\n" + ex.getMessage());
            }
        }
        public void sendMobReSpawn(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 57, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void addMod(Mob mob) {
            try {
                Message msg = new Message((byte) 1);
                mob.write(msg.writer);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void camCo(short id, short cx, short cy, byte type) {
            try {
                Message msg = new Message((byte) -45);
                msg.writeShort(id);
                msg.writeShort(cx);
                msg.writeShort(cy);
                msg.writeByte(type);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void sendAttackMob(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 61, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void sendAttackPlayer(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 20, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void setXYChar() {
            try {
                Message msg = new Message((byte) 102);
                msg.writeInt(client.mChar.id);
                client.mChar.writeXY(msg.writer);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void setXYChar(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 102, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void sendOpenTabZone(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) -6, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void sendItemDropFormMob(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 60, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void removeItemMap(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 58, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void pickUpItem(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 59, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void removeItemBag(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 110, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void updateItemBag(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) -4, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void addItemBag(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 109, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void useItemBag(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 116, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void addExp(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 94, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void sortItem(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 117, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void itemExtendToBag(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 112, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void tachItem(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 118, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void sendArrItemBag(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 83, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void vutItem(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 111, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void updateHpMpWhenAttack(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 55, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void mobAttackChar(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 56, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void updateMp_Me(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 64, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void updateMpFull_Me(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 65, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void updateHp_Me(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 66, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void updateHpFull_Me(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 67, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void updateMp_Orther(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 68, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void updateMpFull_Orther(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 69, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void updateHp_Orther(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 70, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void updateHpFull_Orther(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 71, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void openTabItem(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) -25, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void updateDataChar(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) -49, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void dataBag(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) -95, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void getInfo(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 63, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void updateSkill(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 126, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void reSpawn(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 49, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void addEffect(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 50, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void removeEffect(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 51, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void itemBodyToBag_Me(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 113, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void updateItemBody_Orther(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) -99, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void itemBodyDuPhong(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 36, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void itemBodyDuPhongToBag(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 37, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void openNpc(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 54, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void openTabGhepDa() {
            try {
                Message msg = new Message((byte) 122);
                msg.writeByte(81);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void openTabGiaoVatPham() {
            try {
                Message msg = new Message((byte) 122);
                msg.writeByte(80);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void openTabCuongHoa() {
            try {
                Message msg = new Message((byte) 122);
                msg.writeByte(82);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void SendBox(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 122, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void sendStrTask(byte id) {
            try {
                Message msg = new Message((byte) 12);
                msg.writeByte(id);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void ghepDa(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 108, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void cuongHoa(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 107, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void dichChuyen(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 104, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void updateStatusChar(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 33, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void updateItemBody_Me(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 35, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void sendDanhHieu(Char chars) {
            try {
                Writer writer = new Writer();
                writer.writeByte(-75);
                writer.writeInt(chars.id);
                chars.writeDanhHieu(writer);
                Session.this.sendMessage(new Message((byte) -123, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void updateTimeSkill(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) -85, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void viewChar(Char mchar) {
            try {
                Writer writer = new Writer();
                mchar.writeView(writer);
                Session.this.sendMessage(new Message((byte) 34, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void viewMob(Mob mob) {
            try {
                Writer writer = new Writer();
                mob.writeView(writer);
                Session.this.sendMessage(new Message((byte) 13, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void viewCharOffline(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 34, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void openTabTachCuongHoa() {
            try {
                Message msg = new Message((byte) 122);
                msg.writeByte(84);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void updateSachChienDau(Writer writer) {
            try {
                Message msg = Message.c((byte) -67);
                msg.writer.dos.write(writer.baos.toByteArray());
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void updateSkillViThu(Writer writer) {
            try {
                Message msg = Message.c((byte) -29);
                msg.writer.dos.write(writer.baos.toByteArray());
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void addMobToZone(Char character, Mob mob) {
            try {
                Message msg = new Message((byte) 1);
                mob.write(msg.writer);
                character.zone.SendZoneMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(TaskHandler.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void updateTimeChatColor(Client client) {
            try {
                Message msg = Message.c((byte) -35);
               msg.writeInt(client.mChar.infoChar.timeChatColor);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void tachCuongHoa(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 105, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void tachCaiTrang(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) -52, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void doneTachCaiTrang(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) -51, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void doneKhamNgoc(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) -46, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void doneTachNgoc(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) -47, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void doneCheTao(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) -24, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void sendHoatLuc(Char chars) {
            try {
                Message msg = new Message((byte) -23);
                msg.writeInt(chars.infoChar.hoatLuc);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void ghepCaiTrang(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) -50, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void updateBac(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 90, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public void updateBacKhoa(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 91, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public void updateVang(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 92, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public void updateVangKhoa(Writer writer) {
            try {
                Session.this.sendMessage(new Message((byte) 93, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public void screenLogin() {
            try {
                Message msg = Message.c((byte) -126);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void NhanTinRieng(String tennick, String noidung) {
            try {
                Message msg = new Message((byte) 28);
                msg.writeUTF(tennick);
                msg.writeUTF(noidung);
                msg.writeUTF(tennick);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void MoiGiaoDich(String nickmoi) {
            try {
                Message msg = new Message((byte) 86);
                msg.writeUTF(nickmoi); // người gửi
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void khoaGiaoDich(int bac, List<Item> Items) {
            try {
                Message msg = new Message((byte) 82);
                msg.writeInt(bac);
                msg.writeByte((byte)Items.size());
                for (Item item : Items) {
                    item.write(msg.writer);
                }
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void doneGiaoDich(int bac, List<Item> Items) {
            try {
                Message msg = new Message((byte) 80);
                msg.writeInt(bac);
                msg.writeByte((byte)Items.size());
                for (Item item : Items) {
                    item.write(msg.writer);
                }
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void doneMuaCho(int bac) {
            try {
                Message msg = new Message((byte) 98);
                msg.writeInt(bac);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void sendDataCho_Me(Client client) {
            try {

                List<ChoTemplate> listItemCho = new ArrayList<>();
                List<ChoTemplate> DataCho = DataCenter.gI().DataCho;

                for (ChoTemplate cho : DataCho) {
                    if (cho.character_id == client.mChar.id && cho.isBuy == 0 && cho.character_name.equals(client.mChar.infoChar.name) && cho.time > System.currentTimeMillis() / 1000L) {
                        listItemCho.add(cho);
                    }
                }
                Message msg = new Message((byte) 100);
                msg.writeShort(listItemCho.size()); // sl item

                for (int i = DataCho.size() - 1; i >= 0; i--) {
                    ChoTemplate cho = DataCho.get(i);
                    if (cho.character_id == client.mChar.id && cho.isBuy == 0 && cho.character_name.equals(client.mChar.infoChar.name) && cho.time > System.currentTimeMillis() / 1000L) {
                        listItemCho.add(cho);
                        msg.writeLong(cho.id); // id item market
                        msg.writeInt(cho.bac); // giá bạc
                        msg.writeInt(cho.time); // time
                        cho.item.write(msg.writer);
                    }
                }
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void sendDataCho(byte timtheo, byte sapxep, short page) {
            try {
                List<ChoTemplate> DataCho = DataCenter.gI().DataCho;
                List<ChoTemplate> DataCho_OK = new ArrayList<>();
                for (ChoTemplate cho : DataCho) {
                    if (cho.isBuy == 0 && cho.time > System.currentTimeMillis() / 1000L) {
                        DataCho_OK.add(cho);
                    }
                }
                // 1. Lọc danh sách dựa trên `timtheo`
                List<ChoTemplate> filteredList = filterList(DataCho_OK, timtheo);

                // 2. Sắp xếp danh sách dựa trên `sapxep`
                sortList(filteredList, sapxep);

                // mỗi trang có 30 items
                int itemsPerPage = 30;
                int totalItems = filteredList.size();
                int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);
                if(page >=  totalPages) page = (short) ((short) totalPages-1);
                if(page <= Short.MIN_VALUE) page = 0;

                // 3. Phân trang
                List<ChoTemplate> pageItems = getPageItems(filteredList, page, itemsPerPage);

                Message msg = new Message((byte) 101);
                msg.writeShort(page);
                msg.writeShort(pageItems.size()); // số lượng items trong trang hiện tại

                for (ChoTemplate cho : pageItems) {
                    msg.writeLong(cho.id); // id item
                    msg.writeUTF(cho.character_name); // tên người bán
                    msg.writeInt(cho.bac); // giá bạc
                    msg.writeInt(cho.time); // thời gian
                    cho.item.write(msg.writer);
                }
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        private List<ChoTemplate> getPageItems(List<ChoTemplate> list, int page, int itemsPerPage) {
            int startIndex =  page * itemsPerPage;
            // Kiểm tra để đảm bảo không vượt quá kích thước danh sách
            if (startIndex > list.size()) {
                startIndex = list.size()-itemsPerPage;
            }
            if (startIndex < 0) {
                startIndex = 0;
            }
            int endIndex = startIndex + itemsPerPage;


            if (endIndex > list.size()) {
                endIndex = list.size();
            }

            return new ArrayList<>(list.subList(startIndex, endIndex));
        }

        private List<ChoTemplate> filterList(List<ChoTemplate> DataCho, byte timtheo) {
            List<ChoTemplate> filteredList = new ArrayList<>();
            for (ChoTemplate cho : DataCho) {
                if(cho.isBuy != 0) continue;
                if (timtheo == 0) { // Tất cả
                    filteredList.add(cho);
                } else if (timtheo >= 1 && timtheo <= 27) {
                    if (matchesCriteria(cho, timtheo)) {
                        filteredList.add(cho);
                    }
                } else if (timtheo == 28) {
                    if (!matchesAnyCriteria(cho, (byte) 1, (byte) 27)) {
                        filteredList.add(cho);
                    }
                }
            }
            return filteredList;
        }

        private boolean matchesCriteria(ChoTemplate cho, byte criteria) {
            switch (criteria) {
                case 1: return cho.item.getItemTemplate().type == 21; // tất cả đá
                case 2: return cho.item.getItemTemplate().id == 0; // Đá cấp 1
                case 3: return cho.item.getItemTemplate().id == 1; // Đá cấp 2
                case 4: return cho.item.getItemTemplate().id == 2; // Đá cấp 3
                case 5: return cho.item.getItemTemplate().id == 3; // Đá cấp 4
                case 6: return cho.item.getItemTemplate().id == 4; // Đá cấp 5
                case 7: return cho.item.getItemTemplate().id == 5; // Đá cấp 6
                case 8: return cho.item.getItemTemplate().id == 6; // Đá cấp 7
                case 9: return cho.item.getItemTemplate().id == 7; // Đá cấp 8
                case 10: return cho.item.getItemTemplate().id == 8; // Đá cấp 9
                case 11: return cho.item.getItemTemplate().id == 9; // Đá cấp 10
                case 12: return cho.item.getItemTemplate().id == 10; // Đá cấp 11
                case 13: return cho.item.getItemTemplate().id == 11; // Đá cấp 12
                case 14: return cho.item.isTypeTrangBi(); // tất cả trang bị
                case 15: return cho.item.getItemTemplate().type == 1; // vũ khí
                case 16: return cho.item.getItemTemplate().type == 3; // dây thừng
                case 17: return cho.item.getItemTemplate().type == 5; // móc sắt
                case 18: return cho.item.getItemTemplate().type == 7; // Ống tiêu
                case 19: return cho.item.getItemTemplate().type == 9; // Túi Nhẫn Giả
                case 20: return cho.item.getItemTemplate().type == 0; // đai
                case 21: return cho.item.getItemTemplate().type == 2; // Áo
                case 22: return cho.item.getItemTemplate().type == 4; // Bao tay
                case 23: return cho.item.getItemTemplate().type == 6; // Quần
                case 24: return cho.item.getItemTemplate().type == 8; // Giày
                case 25: return cho.item.getItemTemplate().name.contains("Lệnh bài"); // Lệnh bài
                case 26: return cho.item.getItemTemplate().name.contains("Vỏ sò"); // vỏ sò
                case 27: return cho.item.getItemTemplate().type == 32; // ngọc khảm
                default: return false;
            }
        }

        private boolean matchesAnyCriteria(ChoTemplate cho, byte start, byte end) {
            for (byte i = start; i <= end; i++) {
                if (matchesCriteria(cho, i)) {
                    return true;
                }
            }
            return false;
        }

        private void sortList(List<ChoTemplate> list, byte sapxep) {
            Comparator<ChoTemplate> comparator;

            switch (sapxep) {
                case 0: // Mới nhất
                    comparator = Comparator.comparing(ChoTemplate::getTime).reversed();
                    break;
                case 1: // Giá
                    comparator = Comparator.comparing(ChoTemplate::getBac);
                    break;
                case 2: // Loại vật phẩm
                    comparator = Comparator.comparing(ChoTemplate::getItemType);
                    break;
                case 3: // Thời gian bán
                    comparator = Comparator.comparing(ChoTemplate::getTime);
                    break;
                case 4: // Tên người dùng
                    comparator = Comparator.comparing(ChoTemplate::getCharacterName);
                    break;
                case 5: // Cấp vật phẩm
                    comparator = Comparator.comparing(ChoTemplate::getItemLevel);
                    break;
                default:
                    return; // Không sắp xếp nếu không có tiêu chí phù hợp
            }

            list.sort(comparator);
        }

        public void DangBanThanhCong(int bac, short index) {
            try {
                Message msg = new Message((byte) 99);

                msg.writeInt(bac);
                msg.writeShort(index);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void bagToBox(short itembag, short itembox) {
            try {
                Message msg = new Message((byte) 115);
                msg.writeShort(itembag);
                msg.writeShort(itembox);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void boxToBag(short itembag, short itembox) {
            try {
                Message msg = new Message((byte) 114);
                msg.writeShort(itembox);
                msg.writeShort(itembag);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void nullopen() {
        }

        public void nhacNhoConfirm(String text, int TypeConfirm) {
            try {
                if(Session.this.client != null){
                    Session.this.client.TypeConfirm = TypeConfirm;
                }
                Message msg = new Message((byte) -109);
                msg.writeUTF(text);
                msg.writeByte(-112);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void InputClient(String text, short type) {
            try {
                Message msg = new Message((byte) 122);
                msg.writeByte(73);
                msg.writeUTF(text);
                msg.writeShort(type);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void outGroup() {
            try {
                Message msg = new Message((byte) 44);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void searchGroup(Client client) {
            try {
                Message msg = new Message((byte) 45);
                List<GroupTemplate> listgroup = new ArrayList<>();
                List<Char> listchar = client.mChar.zone.vecChar;
                Set<Integer> addedGroupIds = new HashSet<>();

                for (int i = 0; i < listchar.size(); i++) {
                    Char c = listchar.get(i);
                    if (c.infoChar.groupId != -1 && !addedGroupIds.contains(c.infoChar.groupId)) {
                        GroupTemplate group = Group.gI().getGroup(c.infoChar.groupId);
                        if (group != null) {
                            listgroup.add(group);
                            addedGroupIds.add(c.infoChar.groupId);
                        }
                    }
                }

                msg.writer.writeByte(listgroup.size()); // sl nhóm
                for (int i = 0; i < listgroup.size(); i++) {
                    GroupTemplate g = listgroup.get(i);
                    Char key = g.getKey();
                    if(key != null){
                        msg.writer.writeBoolean(key.infoChar.groupLock); // trạng thái khóa , không khóa
                        msg.writer.writeByte(g.ListMember.size()); // sl tv
                        msg.writer.writeByte(key.infoChar.idClass); // id class
                        msg.writer.writeByte(key.infoChar.idNVChar); // id nv char
                        msg.writer.writeShort(key.level()); // level chủ nhóm
                        msg.writer.writeUTF(key.infoChar.name); // tên chủ nhóm
                    }
                }


                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void MeGroup() {
            try {
                Message msg = new Message((byte) 43);


                msg.writer.writeBoolean(client.mChar.infoChar.groupLock); // trạng thái khóa , không khóa

                GroupTemplate megroup = Group.gI().getGroup(client.mChar.infoChar.groupId);
                byte slmen = 0;
                if(megroup != null){
                    slmen = (byte) megroup.ListMember.size();
                }

                msg.writer.writeByte(slmen); // sl tv

                if (megroup != null) {
                    for (int i = 0; i < megroup.ListMember.size(); i++){
                        Char member = megroup.ListMember.get(i);
                        msg.writer.writeByte(member.infoChar.idClass); // id class

                        msg.writer.writeByte(member.infoChar.idNVChar); // id nv char

                        msg.writer.writeShort(member.level()); // level chủ nhóm

                        msg.writer.writeUTF(member.infoChar.name); // tên chủ nhóm
                    }

                }

                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public void moiGroup(String name) {
            try {
                Message msg = new Message((byte) 41);
                msg.writer.writeUTF(name);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public void xinGroup(String name) {
            try {
                Message msg = new Message((byte) 39);
                msg.writer.writeUTF(name);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void msgAddFr(String name, byte stt, boolean online) {
            try {
                Message msg = new Message((byte) 79);
                msg.writer.writeUTF(name);
                msg.writer.writeByte(stt);
                msg.writer.writeBoolean(online);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public void msgUpdateFr(String name, byte stt, boolean online) {
            try {
                Message msg = new Message((byte) 78);
                msg.writer.writeUTF(name);
                msg.writer.writeByte(stt);
                msg.writer.writeBoolean(online);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public void msgRemoveFr(String name) {
            try {
                Message msg = new Message((byte) 76);
                msg.writer.writeUTF(name);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void msgRemoveEn(String name) {
            try {
                Message msg = new Message((byte) -18);
                msg.writer.writeUTF(name);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public void msgAddEn(String name, boolean isOnline) {
            try {
                Message msg = new Message((byte) -17);
                msg.writer.writeUTF(name);
                msg.writer.writeBoolean(isOnline);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void msgSendAddFr(String name, byte stt) {
            try {
                Message msg = new Message((byte) 77);
                msg.writer.writeUTF(name);
                msg.writer.writeByte(stt);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void loadPhanTram(Char chars, int time, String text) {
            try {
                Message msg = new Message((byte) 4);
                msg.writer.writeInt(time);
                msg.writer.writeUTF(text);
                msg.writeByte(4);
                msg.writeInt(chars.id);
                msg.writeShort(-1);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void xoaTab(Char c) {
            try {
                Message msg = new Message((byte) 7);
                msg.writeInt(c.id);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public void moveMob(short idMob, short cx, short cy) {
            try {
                Message msg = new Message((byte) -1);
                msg.writeShort(idMob);
                msg.writeShort(cx);
                msg.writeShort(cy);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public void loadRank(Char c) {
            try {
                Message msg = new Message((byte) -123);
                msg.writeByte(-72);
                msg.writeInt(c.id);
                msg.writeByte(c.infoChar.rank);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }
        public void updateSelectCaiTrang(byte type) {
            try {
                Message msg = Message.c((byte) -58);
                msg.writer.writeByte(type);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void sendVongQuayNap(byte tile, byte test, int vang) {
            try {
                Message msg = Message.c((byte) -60);
                msg.writer.writeByte(tile);
                msg.writer.writeByte(test);
                msg.writer.writeInt(vang);
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }
        public void sendTextNewGame() {
            try {
                Message msg = new Message((byte) -97);
                msg.writeByte(1);
                msg.writeUTF("PHẦN 1: CON ĐƯỜNG TRỞ THÀNH NHẪN GIẢ\\n Làng Lá Plus");
                msg.writeUTF("3@^, Tỉnh dậy mau!!!\\n-1@Ơ ơ ơ, ta đang ở đâu thế này\\n3@Trời ạ, đã xế trưa rồi mà còn nằm đây ngủ, Bà Konoha Utatane đang tìm anh đó\\n-1@Ơ ơ ơ, ta biết rồi, để ta đi tìm bà Konoha Utatane xem có chuyện gì đang xảy ra");
                Session.this.sendMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

    }

}
