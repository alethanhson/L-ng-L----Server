package com.langla.server.main;

import com.langla.real.player.PlayerManager;
import com.langla.server.lib.Message;
import com.langla.utlis.UTPKoolVN;

import java.io.IOException;

/**
 * @author PKoolVN
 **/
public class Maintenance extends Thread {

    public static  boolean isRuning = false;
    private static Maintenance i;

    private int min;

    private Maintenance() {

    }

    public static Maintenance gI() {
        if (i == null) {
            i = new Maintenance();
        }
        return i;
    }

    public void start(int min) {
        this.min = min;
        if (!isRuning) {
            this.start();
            isRuning = true;
        }
    }
    public void WordNhacNho(String text) throws IOException {
        Message msg = new Message((byte) -110);
        msg.writeUTF(text);
        PlayerManager.getInstance().sendMessageAllChar(msg);
    }
    @Override
    public void run() {
        int count = 0; // Biến đếm cho thông báo
        while (this.min > 0) {
            this.min--;
            count++;
            if(this.min < 60){
                try {
                    WordNhacNho("Hệ thống sẽ bảo trì sau " + min
                            + " giây nữa, vui lòng thoát game để tránh mất vật phẩm");
                    UTPKoolVN.Print("Server bao tri sau: " + min +" giay");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (count >= 60) {
                try {
                    WordNhacNho("Hệ thống sẽ bảo trì sau " + min/60
                            + " phút nữa, vui lòng thoát game để tránh mất vật phẩm");
                    UTPKoolVN.Print("Server bao tri sau: " + min/60 +" phut");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                count = 0;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Main.Stop();
    }

}
