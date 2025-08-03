package com.langla.real.group;

import com.langla.real.player.Char;
import com.langla.real.player.Client;
import com.langla.real.player.PlayerManager;
import com.langla.server.lib.Message;

import java.util.Vector;

/**
 * @author PKoolVN
 **/
public class Group {

    protected static Group Instance;

    public static Group gI() {
        if (Instance == null)
            Instance = new Group();
        return Instance;
    }
    public int baseId = 1;
    public Vector<GroupTemplate> group = new Vector<>();


    public void newGroup(Client client){
        if(client.mChar.infoChar.groupId != -1) return;
        GroupTemplate gr = new GroupTemplate(client.mChar);
        group.add(gr);
        client.session.serivce.MeGroup();
    }
    public void lockGroup(Client client){
        if(client.mChar.infoChar.groupId == -1 || getGroup(client.mChar.infoChar.groupId) == null) return;
        client.mChar.infoChar.groupLock = !client.mChar.infoChar.groupLock;
    }
    public GroupTemplate getGroup(int id) {
        for (GroupTemplate groupTemplate : group) {
            if (groupTemplate.id == id) {
                return groupTemplate;
            }
        }
        return null;
    }
    public void removeGroup(int id) {
        for (GroupTemplate groupTemplate : group) {
            if (groupTemplate.id == id) {
                group.remove(groupTemplate);
                return;
            }
        }
    }

    public void sendMessageAllMember(Message m, int id) {
        GroupTemplate gr = getGroup(id);
        if(gr != null){
            for (int i = 0; i < gr.ListMember.size(); i++){
                Char getchar = PlayerManager.getInstance().getChar(gr.ListMember.get(i).id);
                if(getchar != null) getchar.client.session.sendMessage(m);
            }
        }

    }
    public void LoadMemberALL(int id) {
        GroupTemplate gr = getGroup(id);
        if(gr != null){
            for (int i = 0; i < gr.ListMember.size(); i++){
                Char getchar = PlayerManager.getInstance().getChar(gr.ListMember.get(i).id);
                if(getchar != null) getchar.client.session.serivce.MeGroup();
            }

        }

    }



}
