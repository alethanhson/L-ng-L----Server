package com.langla.server.lib;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Reader {

    public DataInputStream dis = null;

    public Reader(Socket socket) throws IOException {
        this.dis = new DataInputStream(socket.getInputStream());
    }

    public Reader(DataInputStream var1) {
        this.dis = var1;
    }

    public Reader(InputStream var1) {
        this.dis = new DataInputStream(var1);
    }

    public Reader(byte[] var1) {
        this.dis = new DataInputStream(new ByteArrayInputStream(var1));
    }

    public byte[] read() throws java.io.IOException {
        byte[] var1 = new byte[this.dis.readInt()];
        this.dis.read(var1);
        return var1;
    }

    public byte[] read(byte[] var1, int var2, int var3) throws java.io.IOException {
        this.dis.read(var1, var2, var3);
        return var1;
    }

    public byte[] readFully() throws java.io.IOException {
        byte[] var1 = new byte[this.dis.available()];
        this.dis.read(var1);
        return var1;
    }

    public static String k = " 0123456789+-*='\"\\/_?.,ˋˊ~ˀ:;|<>[]{}!@#$%^&*()aáàảãạâấầẩẫậăắằẳẵặbcdđeéèẻẽẹêếềểễệfghiíìỉĩịjklmnoóòỏõọôốồổỗộơớờởỡợpqrstuúùủũụưứừửữựvxyýỳỷỹỵzwAÁÀẢÃẠÂẤẦẨẪẬĂẮẰẲẴẶBCDĐEÉÈẺẼẸÊẾỀỂỄỆFGHIÍÌỈĨỊJKLMNOÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢPQRSTUÚÙỦŨỤƯỨỪỬỮỰVXYÝỲỶỸỴZW";

    public String readUTF() throws java.io.IOException {
        short var1;
        if ((var1 = (short) this.dis.readUnsignedByte()) == 0) {
            return this.dis.readUTF();
        } else {
            String var2 = "";

            for (int var3 = 0; var3 < var1; ++var3) {
                var2 = var2 + k.charAt(this.dis.readUnsignedByte());
            }

            return var2;
        }
    }

    public byte readByte() throws IOException {
        return dis.readByte();
    }

    public boolean readBoolean() throws IOException {
        return dis.readBoolean();
    }

    public short readShort() throws IOException {
        return dis.readShort();
    }

    public int readUnsignedShort() throws IOException {
        return dis.readUnsignedShort();
    }

    public int readInt() throws IOException {
        return dis.readInt();
    }

    public long readLong() throws IOException {
        return dis.readLong();
    }

    public short readUnsignedByte() throws java.io.IOException {
        return (short) this.dis.readUnsignedByte();
    }

    public void close() {
        try {
            if (this.dis != null) {
                this.dis.close();
            }
        } catch (Exception var1) {
        }
    }
}
