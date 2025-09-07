package com.langla.server.lib;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Writer {

    public ByteArrayOutputStream baos = null;
    public DataOutputStream dos = null;

    public Writer(DataOutputStream var1) {
        this.dos = var1;
    }

    public Writer(Socket socket) throws IOException {
        this.dos = new DataOutputStream(socket.getOutputStream());
    }

    public Writer() {
        this.baos = new ByteArrayOutputStream();
        this.dos = new DataOutputStream(this.baos);
    }

    public Writer(ArrayList<Integer> listTypeRead) {
        this.baos = new ByteArrayOutputStream();
        this.dos = new DataOutputStream(this.baos);
    }

    public void writeBoolean(boolean var1) throws java.io.IOException {
        this.dos.writeBoolean(var1);
    }

    public void writeByte(int var1) throws java.io.IOException {
        this.dos.writeByte(var1);
    }

    public void write(byte[] var1) throws java.io.IOException {
        this.dos.writeInt(var1.length);
        this.dos.write(var1);
    }

    public void writeShort(int var1) throws java.io.IOException {
        this.dos.writeShort(var1);
    }

    public void writeInt(int var1) throws java.io.IOException {
        this.dos.writeInt(var1);
    }

    public void writeLong(long var1) throws java.io.IOException {
        this.dos.writeLong(var1);
    }

    public static String k = " 0123456789+-*='\"\\/_?.,ˋˊ~ˀ:;|<>[]{}!@#$%^&*()aáàảãạâấầẩẫậăắằẳẵặbcdđeéèẻẽẹêếềểễệfghiíìỉĩịjklmnoóòỏõọôốồổỗộơớờởỡợpqrstuúùủũụưứừửữựvxyýỳỷỹỵzwAÁÀẢÃẠÂẤẦẨẪẬĂẮẰẲẴẶBCDĐEÉÈẺẼẸÊẾỀỂỄỆFGHIÍÌỈĨỊJKLMNOÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢPQRSTUÚÙỦŨỤƯỨỪỬỮỰVXYÝỲỶỸỴZW";

    public void writeUTF(String var1) throws java.io.IOException {
        if (var1.length() > 0 && var1.length() <= 255) {
            this.dos.writeByte(var1.length());

            for (int var3 = 0; var3 < var1.length(); ++var3) {
                int var2;
                if ((var2 = k.indexOf(var1.charAt(var3))) < 0) {
                    var2 = 0;
                }

                this.dos.writeByte(var2);
            }

        } else {
            this.dos.writeByte(0);
            this.dos.writeUTF(var1);
        }
    }

    public void close() {
        try {
            if (this.dos != null) {
                this.dos.close();
            }

            if (this.baos != null) {
                this.baos.close();
            }

        } catch (Exception var2) {
            // Utlis.println(var2);
        }
    }

    public void close(String var1) {
        try {
            // Binary.write(var1, this.baos.toByteArray());
            if (this.dos != null) {
                this.dos.close();
                this.dos = null;
            }

            if (this.baos != null) {
                this.baos.close();
                this.baos = null;
            }

        } catch (Exception var2) {
            // Utlis.println(var2);
        }
    }
}
