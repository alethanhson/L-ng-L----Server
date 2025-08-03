package com.langla.real.group;

import com.langla.real.player.Char;
import com.langla.real.player.Client;

import java.util.IdentityHashMap;
import java.util.Vector;

/**
 * @author PKoolVN
 **/
public class GroupTemplate {

    public int id;

    public short idZoneLuyenTap = 0;
    public short idZoneCamThuat = 0;
    public Vector<Char> ListMember = new Vector<>();

    public GroupTemplate(){

    }
    public GroupTemplate(Char key){
        id = Group.gI().baseId++;
        key.infoChar.groupId = id;
        this.ListMember.add(key);
    }


    public Char getChar(int charId) {
        for (Char member : ListMember) {
            if (member.id == charId) {
                return member;
            }
        }
        return null;
    }

    public Char getChar(String charName) {
        for (Char member : ListMember) {
            if (member.infoChar.name.equals(charName)) {
                return member;
            }
        }
        return null;
    }


    public Char getKey() {
        return ListMember.get(0);
    }

    public void addMember(Char chars) {
        if(getChar(chars.id) != null) return;
        ListMember.add(chars);
        chars.infoChar.groupId = id;
    }

    public void removeMember(int id) {
        for (Char c : ListMember) {
            if (c.id == id) {
                ListMember.remove(c);
                return;
            }
        }
    }
    public boolean chuyenKey(Char key, Char newkey) {
       if(getChar(key.id) != null && getChar(newkey.id) != null){
           removeMember(key.id);
            removeMember(newkey.id);
            ListMember.add(0, newkey);
            addMember(key);
            return true;
        }
      return false;
    }

    public boolean duoiMem(Char mem) {
        if(getChar(mem.id) != null){
            removeMember(mem.id);
            mem.infoChar.groupId = -1;
            return true;
        }
        return false;
    }

    public void out(Client client) {
        Char getchar = getChar(client.mChar.id);
        if(getchar != null){
            if(getchar.id == getKey().id){
                if(ListMember.size() <= 1){
                    Group.gI().removeGroup(client.mChar.infoChar.groupId);
                    client.mChar.infoChar.groupId = -1;
                } else {
                    removeMember(client.mChar.id);
                    Group.gI().LoadMemberALL(client.mChar.infoChar.groupId);
                    client.mChar.chatGroup("đã rời khỏi nhóm");
                    client.mChar.infoChar.groupId = -1;
                }
            } else {
                removeMember(client.mChar.id);
                Group.gI().LoadMemberALL(client.mChar.infoChar.groupId);
                client.mChar.chatGroup("đã rời khỏi nhóm");
                client.mChar.infoChar.groupId = -1;
            }
        }
    }

}
