package com.langla.real.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.langla.server.main.Main;
import com.langla.utlis.UTPKoolVN;

import java.net.Socket;

public class Client {
    @JsonIgnore

    public int id;
    @JsonIgnore
    public Session session;
    @JsonIgnore
    public Socket socket;
    @JsonIgnore
    public boolean isClean;

    public Char mChar;

    public Player player;
    @JsonIgnore
    public int TypeMenu = -1;
    @JsonIgnore
    public int TypeConfirm = -1;
    @JsonIgnore
    public int NPCMenu = -1;
    @JsonIgnore
    public boolean isSendArrData;
    @JsonIgnore
    public boolean isChangePass = false;
    @JsonIgnore
    public Client(Socket socket) {
        this.socket = socket;
        this.session = new Session(this);
    }
    @JsonIgnore
    public boolean isConnected() {
        return !isClean && this.session.isConnected();
    }
    @JsonIgnore
    public void clean() {
        if (isClean) {
            return;
        }
        UTPKoolVN.Print("CLIENT: " + id +
                " IP: " + socket.getInetAddress().getHostAddress() +
                " >> Disconnect.!");
        isClean = true;
//        if (mChar != null) {
//            mChar = null;
//        }
//        if(player != null){
//            player = null;
//        }
//        if (session != null) {
//            session = null;
//        }
        Main.removeClient(this);
    }
    @JsonIgnore
    public void create() {
    }

}
