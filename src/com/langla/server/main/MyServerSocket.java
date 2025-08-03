package com.langla.server.main;

import com.PKoolVNDB;
import com.langla.real.family.Family;
import com.langla.real.player.PlayerManager;
import com.langla.server.handler.ServerSocketHandler;
import com.langla.utlis.UTPKoolVN;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServerSocket {

    public int PORT = -1;
    public ServerSocketHandler handler;
    public ServerSocket server;
    public boolean RUN = false;

    public Thread thread;

    public MyServerSocket(int PORT, ServerSocketHandler handler) {
        this.PORT = PORT;
        this.handler = handler;
    }

    public void open() {
        if (RUN) {
            return;
        }

        RUN = true;
        thread = new Thread(()
                -> {
            try {
                if (PORT == -1) {
                    UTPKoolVN.Print("CHUA NHAP PORT");
                    return;
                }
                server = new ServerSocket(PORT);
                UTPKoolVN.Print("OPEN SERVER " + PORT);
                while (RUN) {
                    try {
                        Socket socket = server.accept();
                        if (handler != null) {
                            handler.socketConnet(socket);
                        }
                    } catch (Exception ex) {
                        RUN = false;
                    }
                }
                if(PORT == PKoolVNDB.PORT_SERVER) SaveData();
            } catch (IOException ex) {
//                UTPKoolVN.Print(ex.getMessage());
            }
        });
        thread.start();
    }
    public void stop(){
        try {
            if(RUN) {
                server.close();
                server = null;
                UTPKoolVN.Print("CLOSE SERVER: " + PORT);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void SaveData(){
        PlayerManager.getInstance().Clear();
        Family.gI().saveData();
        //kiểm tra xem có reboot server không

        String filePath = "restart.flag";
        File file = new File(filePath);
        if (file.exists()) {
            UTPKoolVN.Print("Reboot Server Sucess!!");
            System.exit(0);
        }
    }
}
