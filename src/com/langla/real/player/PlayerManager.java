package com.langla.real.player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.langla.lib.Utlis;
import com.langla.server.lib.Message;
import com.langla.utlis.UTPKoolVN;

/**
 * @author PKoolVN
 **/
public class PlayerManager {

    protected static PlayerManager instance;

    public static PlayerManager getInstance() {
        if (instance == null)
            instance = new PlayerManager();
        return instance;
    }

    private final ArrayList<Session> conns = new ArrayList<Session>();

    private final Map<Integer, Player> players_id = new HashMap<Integer, Player>();

    private final Map<Integer, Char> char_id = new HashMap<Integer, Char>();
    private final Map<String, Char> char_name = new HashMap<String, Char>();


    public void chatWord (String text){
        try {
            Message msg = new Message((byte) 22);
            msg.writeByte(1);
            msg.writeUTF("Hệ thống");
            msg.writeUTF(text);
            sendMessageAllChar(msg);
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex , "Da say ra loi:\n" + ex.getMessage());
        }
    }
    public void sendMessageAllChar(Message m) {
        synchronized (conns) {
            for (int i = conns.size()-1; i >= 0; i--)
                if (conns.get(i).client.player != null && conns.get(i).client.mChar != null)
                    conns.get(i).sendMessage(m);
        }
    }
    public boolean isCheckSession(Session s) {
        return conns.contains(s);
    }

    public synchronized Player getPlayerLogin (int id) {
        return players_id.get(id);
    }
    public Char getChar(int id) {
        return char_id.get(id);
    }
    public Map<Integer, Char> getlistChar() {
        return char_id;
    }
    public Char getChar(String name) {
        return char_name.get(name);
    }
    public int char_size() {
        return char_id.size();
    }
    public int conn_size() {
        return conns.size();
    }
    public int player_size() {
        return players_id.size();
    }
    public void put(Session conn) {
        if (!conns.contains(conn)) {
            conns.add(conn);
        }
    }


    public  void put(Char n) {
        if (!char_id.containsKey(n.id)) char_id.put(n.id, n);
        if (!char_name.containsKey(n.infoChar.name)) char_name.put(n.infoChar.name, n);
    }


    public  void put(Player p) {
        if (!players_id.containsKey(p.id)) players_id.put(p.id, p);
    }


    public void remove(Session conn) {
        if (conn.client.player != null) {
            remove(conn.client.player);
            Player.Update(conn.client.player);
        }
        if (conn.client.mChar != null) {
            remove(conn.client.mChar);
            conn.client.mChar.clean();
            CharDB.Update(conn.client.mChar);
        }
        conns.remove(conn);// remove session
    }

    public void remove(Player p) {
        players_id.remove(p.id);
    }

    public void remove(Char n) {
        char_id.remove(n.id);
        char_name.remove(n.infoChar.name);
    }
    public void kickSession(Session conn) {
        conn.clean();
    }
    public void ClearPlayer() throws InterruptedException {
        for (int i = conns.size()-1; i >= 0; i--){
            if (conns.get(i).client.player != null) kickSession(conns.get(i));
            Thread.sleep(10);
        }
        UTPKoolVN.Print("Clear Player Sucess!!!");
    }
    public void Clear() {
        while(!conns.isEmpty()){
            kickSession(conns.get(0));
        }
        UTPKoolVN.Print("Clear Session Sucess!!!");
    }
}
