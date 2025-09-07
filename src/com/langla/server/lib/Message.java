package com.langla.server.lib;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;

public class Message implements Cloneable {

    public byte cmd;
    public Reader reader = null;
    public Writer writer = null;
    public boolean inflate;

    public static Message a(byte var0) throws java.io.IOException {
        Message var1;
        (var1 = new Message((byte) -125)).writeByte(var0);
        return var1;
    }

    public static Message b(byte var0) throws java.io.IOException {
        Message var1;
        (var1 = new Message((byte) -124)).writeByte(-128);
        return var1;
    }

    public static Message c(byte var0) throws java.io.IOException {
        Message var1;
        (var1 = new Message((byte) -123)).writeByte(var0);
        return var1;
    }

    public static Message d(byte var0) throws java.io.IOException {
        Message var1;
        (var1 = new Message((byte) -122)).writeByte(var0);
        return var1;
    }

    public static Message e(byte var0) throws java.io.IOException {
        Message var1;
        (var1 = new Message((byte) -112)).writeByte(var0);
        return var1;
    }

    public static Message f(byte var0) throws java.io.IOException {
        Message var1;
        (var1 = new Message((byte) -111)).writeByte(var0);
        return var1;
    }

    public Message(byte var1) {
        this.cmd = var1;
        this.writer = new Writer();
    }

    public Message(byte var1, Writer writer) {
        this.cmd = var1;
        this.writer = writer;
    }

    public Message(byte var1, byte[] var2) {
        this.cmd = var1;
        this.reader = new Reader(var2);
    }

    public byte[] getData() {
        if (this.writer == null) {
            return null;
        }
        try {
            this.writer.dos.flush();
            return this.writer.baos.toByteArray();
        } catch (Exception ex) {
        }
        return null;
    }

    public byte[] getData(String var1) {
        byte[] var11;
        try {
            if (this.writer == null) {
                return null;
            }

            this.writer.dos.flush();
            byte[] var10 = this.writer.baos.toByteArray();
            ByteArrayOutputStream var2 = new ByteArrayOutputStream();
            int var3 = var10.length;
            if (var10.length > 99) {
                Deflater var4;
                (var4 = new Deflater()).setLevel(9);
                var4.setInput(var10);
                var4.finish();
                var2.write(var10, 0, 4);
                byte[] var5 = new byte[1024];

                while (!var4.finished()) {
                    var2.write(var5, 0, var4.deflate(var5));
                }

                var4.end();
                if ((var11 = var2.toByteArray()).length > 32767) {
                    var3 = var11.length - 4;
                    var2.reset();
                    var2.write(new byte[] { -128, this.cmd });
                    var2.write(var11);
                    (var11 = var2.toByteArray())[2] = (byte) (var3 >> 24 & 255);
                    var11[3] = (byte) (var3 >> 16 & 255);
                    var11[4] = (byte) (var3 >> 8 & 255);
                    var11[5] = (byte) (var3 & 255);
                } else if (var11.length >= var10.length) {
                    var2.reset();
                    var2.write(new byte[] { this.cmd, (byte) (var3 >> 8), (byte) var3 });
                    var2.write(var10);
                    var11 = var2.toByteArray();
                } else {
                    var3 = var11.length - 4;
                    var11[0] = -80;
                    var11[1] = this.cmd;
                    var11[2] = (byte) (var3 >> 8 & 255);
                    var11[3] = (byte) (var3 & 255);
                }
            } else {
                var2.write(new byte[] { this.cmd, (byte) (var3 >> 8), (byte) var3 });
                var2.write(var10);
                var11 = var2.toByteArray();
            }

            var2.close();
            var11 = var11;
        } catch (Exception var8) {
            // Utlis.println(var8);
            return null;
        } finally {
            this.close();
        }

        return var11;
    }

    public void close() {
        if (this.reader != null) {
            this.reader.close();
        }

        if (this.writer != null) {
            this.writer.close();
        }

    }

    public boolean readBoolean() throws java.io.IOException {
        return this.reader.readBoolean();
    }

    public byte readByte() throws java.io.IOException {
        return this.reader.readByte();
    }

    public byte[] read() throws java.io.IOException {
        return this.reader.read();
    }

    public short readShort() throws java.io.IOException {
        return this.reader.readShort();
    }

    public int readUnsignedShort() throws java.io.IOException {
        return this.reader.readUnsignedShort();
    }

    public long readLong() throws java.io.IOException {
        return this.reader.readLong();
    }

    public int readInt() throws java.io.IOException {
        return this.reader.readInt();
    }

    public String readUTF() throws java.io.IOException {
        return this.reader.readUTF();
    }

    public void writeBoolean(boolean var1) throws java.io.IOException {
        this.writer.writeBoolean(var1);
    }

    public void writeByte(int var1) throws java.io.IOException {
        this.writer.writeByte(var1);
    }

    public void write(byte[] var1) throws java.io.IOException {
        this.writer.write(var1);
    }

    public void writeShort(int var1) throws java.io.IOException {
        this.writer.writeShort(var1);
    }

    public void writeInt(int var1) throws java.io.IOException {
        this.writer.writeInt(var1);
    }

    public void writeLong(long var1) throws java.io.IOException {
        this.writer.writeLong(var1);
    }

    public void writeUTF(String var1) throws java.io.IOException {
        this.writer.writeUTF(var1);
    }

    // public void createSession() {
    // while(Session.gI().isConnected()) {
    // Session.gI().close();
    // Utlis.sleep(100L);
    // }
    //
    // Session.gI().vMessage.clear();
    // this.newSession();
    // }
    //
    // public void send() {
    // if (Session.gI().isConnected()) {
    // Session.gI().sendMessage(this);
    // this.close();
    // }
    // }
    //
    // private void newSession() {
    // try {
    // if (!Session.gI().isConnecting) {
    // DataCenter.gI().createSession();
    // }
    //
    // int var1 = 20;
    //
    // while(var1 > 0 && !Session.gI().isConnected()) {
    // --var1;
    // Utlis.sleep(500L);
    // }
    // } catch (Exception var2) {
    // Utlis.println(var2);
    // }
    //
    // Session.gI().sendMessage(this);
    // this.close();
    // }
}
