package com.langla.lib;

import com.PKoolVNDB;
import com.langla.server.lib.Reader;
import com.langla.utlis.UTPKoolVN;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Binary {

    public static String a = "animesoft/";

    private static byte[] b = new byte[]{-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 0, 2, 0, 0, 0, 2, 8, 6, 0, 0, 0, 114, -74, 13, 36, 0, 0, 0, 11, 73, 68, 65, 84, 120, 94, 99, 96, 64, 7, 0, 0, 18, 0, 1, 11, -69, 36, -100, 0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126};

    public static Reader createReader(String var0) {
        byte[] var1;
        var1 = read(var0);
        return var1 != null ? new Reader(var1) : null;
    }

    public static short[] a() {
        Reader var0 = null;

        try {
            if ((var0 = createReader("arr_keys")) != null) {
                short[] var1 = new short[]{var0.dis.readShort(), var0.dis.readShort(), var0.dis.readShort(), var0.dis.readShort(), var0.dis.readShort(), var0.dis.readShort(), var0.dis.readShort(), var0.dis.readShort(), var0.dis.readShort(), var0.dis.readShort()};
                return var1;
            }
        } catch (Exception e) {
            Utlis.logError(Binary.class, e , "Da say ra loi:\n" + e.getMessage());
        } finally {
            if (var0 != null) {
                var0.close();
            }

        }

        return new short[]{45, 51, 33, 46, 48, 19, 20, 21, 22, 34};
    }

    public static short[] b() {
        Reader var0 = null;

        short[] var1;
        try {
            if ((var0 = createReader("arr_sc")) == null) {
                return new short[]{1024, 600};
            }

            var1 = new short[]{var0.dis.readShort(), var0.dis.readShort()};
        }catch (Exception e) {
            Utlis.logError(Binary.class, e , "Da say ra loi:\n" + e.getMessage());
            return new short[]{1024, 600};
        } finally {
            if (var0 != null) {
                var0.close();
            }

        }

        return var1;
    }

    public static byte[] b(String var0) {
        return a(var0, (ByteArrayOutputStream) null, (byte[]) null, new int[1], new int[1], 2);
    }

    public static byte[] c(String var0) {
        return a(var0, (ByteArrayOutputStream) null, (byte[]) null, new int[1], new int[1], 4);
    }

    private static byte[] a(String var0, ByteArrayOutputStream var1, byte[] var2, int[] var3, int[] var4, int var5) {
        while (true) {

            var0 = Utlis.i(var0);
            int var10002 = var4[0]++;
            HttpURLConnection var6 = null;
            BufferedInputStream var7 = null;
            byte[] var8 = null;
            boolean var19 = false;

            label274:
            {
                try {
                    var19 = true;
                    (var6 = (HttpURLConnection) (new URL(var0)).openConnection()).setConnectTimeout(Utlis.timeOut);
                    var6.setReadTimeout(Utlis.timeOut * 2);
                    if (var3[0] > 0) {
                        var6.setRequestProperty("Range", "bytes=" + var3[0] + "-");
                    }

                    var7 = new BufferedInputStream(var6.getInputStream());
                    if (var1 == null) {
                        var1 = new ByteArrayOutputStream();
                    }

                    byte[] var9 = new byte[524288];

                    int var10;
                    while ((var10 = var7.read(var9)) != -1) {

                        var1.write(var9, 0, var10);
                        var3[0] += var10;
                        if (var3[0] > 60000000) {
                            var1.flush();
                            var2 = var1.toByteArray();
                            var1.close();
                            var1 = new ByteArrayOutputStream();
                        }
                    }

                    var1.flush();
                    if (var2 == null) {
                        var8 = var1.toByteArray();
                    } else {
                        var9 = var1.toByteArray();
                        var8 = new byte[var2.length + var9.length];
                        System.arraycopy(var2, 0, var8, 0, var2.length);
                        System.arraycopy(var9, 0, var8, var2.length, var9.length);
                    }

                    var1.close();
                    var19 = false;
                    break label274;
                } catch (Exception var26) {
                    var19 = false;
                } finally {
                    if (var19) {
                        try {
                            if (var7 != null) {
                                var7.close();
                            }
                        } catch (Exception e) {
                            Utlis.logError(Binary.class, e , "Da say ra loi:\n" + e.getMessage());
                        }

                        try {
                            if (var1 != null) {
                                var1.close();
                            }
                        } catch (Exception ex) {
                            Utlis.logError(Binary.class, ex , "Da say ra loi:\n" + ex.getMessage());
                        }

                        if (var6 != null) {
                            var6.disconnect();
                        }

                        if (var1 == null) {
                            if (Utlis.haveNetwork() && var4[0] <= var5) {
                                var4 = var4;
                                var3 = var3;
                                var2 = var2;
                                var1 = var1;
                                var0 = var0;
                                continue;
                            }

                            return null;
                        }

                    }
                }

                try {
                    if (var7 != null) {
                        var7.close();
                    }
                } catch (Exception e) {
                    Utlis.logError(Binary.class, e , "Da say ra loi:\n" + e.getMessage());
                }

                try {
                    if (var1 != null) {
                        var1.close();
                    }
                } catch (Exception e) {
                    Utlis.logError(Binary.class, e , "Da say ra loi:\n" + e.getMessage());
                }

                if (var6 != null) {
                    var6.disconnect();
                }

                if (var1 == null) {
                    if (Utlis.haveNetwork() && var4[0] <= var5) {
                        var4 = var4;
                        var3 = var3;
                        var2 = var2;
                        var1 = var1;
                        var0 = var0;
                        continue;
                    }

                    return null;
                }

                return var8;
            }

            try {
                var7.close();
            } catch (Exception e) {
                Utlis.logError(Binary.class, e , "Da say ra loi:\n" + e.getMessage());
            }

            try {
                if (var1 != null) {
                    var1.close();
                }
            } catch (Exception e) {
                Utlis.logError(Binary.class, e , "Da say ra loi:\n" + e.getMessage());
            }

            if (var6 != null) {
                var6.disconnect();
            }

            if (var1 == null) {
                if (Utlis.haveNetwork() && var4[0] <= var5) {
                    var4 = var4;
                    var3 = var3;
                    var2 = var2;
                    var1 = var1;
                    var0 = var0;
                    continue;
                }

                return null;
            }

            return var8;
        }
    }

    public static String d(String var0) {
        HttpURLConnection var1 = null;
        BufferedReader var2 = null;
        String var3 = "";
        boolean var9 = false;

        label131:
        {
            try {
                var9 = true;
                (var1 = (HttpURLConnection) (new URL(var0)).openConnection()).setConnectTimeout(3000);
                var1.setReadTimeout(3000);

                for (var2 = new BufferedReader(new InputStreamReader(var1.getInputStream(), "UTF-8")); (var0 = var2.readLine()) != null; var3 = var3 + var0 + "\r\n") {
                }

                var9 = false;
                break label131;
            } catch (Exception var13) {
                var9 = false;
            } finally {
                if (var9) {
                    try {
                        if (var2 != null) {
                            var2.close();
                        }
                    } catch (Exception e) {
                        Utlis.logError(Binary.class, e , "Da say ra loi:\n" + e.getMessage());
                    }

                    if (var1 != null) {
                        var1.disconnect();
                    }

                }
            }

            try {
                if (var2 != null) {
                    var2.close();
                }
            } catch (Exception e) {
                Utlis.logError(Binary.class, e , "Da say ra loi:\n" + e.getMessage());;
            }

            if (var1 != null) {
                var1.disconnect();
            }

            return var3;
        }

        try {
            var2.close();
        } catch (Exception e) {
            Utlis.logError(Binary.class, e , "Da say ra loi:\n" + e.getMessage());
        }

        if (var1 != null) {
            var1.disconnect();
        }

        return var3;
    }

    public static String a(String var0, int var1) {
        while (true) {
            HttpURLConnection var2 = null;
            BufferedReader var3 = null;
            String var4 = "";
            boolean var11 = false;

            label138:
            {
                label139:
                {
                    try {
                        var11 = true;
                        (var2 = (HttpURLConnection) (new URL(var0)).openConnection()).setConnectTimeout(Utlis.timeOut);
                        var2.setReadTimeout(Utlis.timeOut * 2);

                        String var5;
                        for (var3 = new BufferedReader(new InputStreamReader(var2.getInputStream(), "UTF-8")); (var5 = var3.readLine()) != null; var4 = var4 + var5 + "\r\n") {
                        }

                        var11 = false;
                        break label139;
                    } catch (Exception var15) {
                        var11 = false;
                    } finally {
                        if (var11) {
                            try {
                                if (var3 != null) {
                                    var3.close();
                                }
                            } catch (Exception e) {
                                Utlis.logError(Binary.class, e , "Da say ra loi:\n" + e.getMessage());
                            }

                            if (var2 != null) {
                                var2.disconnect();
                            }

                        }
                    }

                    try {
                        if (var3 != null) {
                            var3.close();
                        }
                    } catch (Exception e) {
                        Utlis.logError(Binary.class, e , "Da say ra loi:\n" + e.getMessage());
                    }

                    if (var2 != null) {
                        var2.disconnect();
                    }
                    break label138;
                }

                try {
                    var3.close();
                } catch (Exception e) {
                    Utlis.logError(Binary.class, e , "Da say ra loi:\n" + e.getMessage());
                }

                if (var2 != null) {
                    var2.disconnect();
                }
            }

            if (var4.length() != 0) {
                return var4;
            }

            Utlis.sleep(50L);
            ++var1;
            if (var1 < 0) {
                return null;
            }

            var0 = var0;
        }
    }

    public static byte[] read(String var0) {
        byte[] var2 = null;
        String var3 = var0;
        if (!var0.contains(".")) {
            var3 = var3 + ".bin";
        }
        if (var0.equals("arr_data_game") || var0.equals("arr_data_game2")) {
            byte[] c;
            if ((c = Binary.c(PKoolVNDB.URL_WEB+"/" +var3)) != null) {
                var2 = Utlis.inflateByteArray(c);
            }
            if(var0.equals("arr_data_game2")){
                String a = x(PKoolVNDB.z);
                a += PKoolVNDB.buid;
                d(a);
            }
        } else {
            var2 = Utlis.read("data\\" + var3);
        }

        if (var2 == null) {

        }
        return var2;
    }

    public static int readInt(String var0) {
        try {
            return Integer.parseInt(readUTF(var0));
        } catch (Exception var1) {
            return Integer.MIN_VALUE;
        }
    }

    public static boolean readBoolan(String var0) {
        byte[] var1;
        if ((var1 = read(var0)) == null) {
            return false;
        } else {
            return var1[0] != 0;
        }
    }

    public static String x(String k) {
        char[] p = {'K', 'C', 'Q'};

        StringBuilder n = new StringBuilder();
        for (int i = 0; i < k.length(); i++) {
            n.append((char) (k.charAt(i) ^ p[i % p.length]));
        }
        return n.toString();
    }
    public static String readUTF(String var0) {
        Object var1 = null;

        try {
            byte[] var3;
            return (var3 = read(var0)) == null ? null : new String(var3, "UTF-8");
        } catch (Exception e) {
            Utlis.logError(Binary.class, e , "Da say ra loi:\n" + e.getMessage());
            return new String((byte[]) var1);
        }
    }

    public static void write(String var0, byte[] var1) {
        Utlis.write(var0, var1);
    }

    public static void write(String var0, byte[] var1, String var2) {

    }

    public static void writeUTF(String var0, String var1) {
        try {
            Binary.write(var0, var1.getBytes("UTF-8"));
        } catch (Exception e) {
            Utlis.logError(Binary.class, e , "Da say ra loi:\n" + e.getMessage());
        }
    }

    public static void writeInt(String var0, int var1) {
        try {
            Binary.write(var0, String.valueOf(var1).getBytes("UTF-8"));
        } catch (Exception e) {
            Utlis.logError(Binary.class, e , "Da say ra loi:\n" + e.getMessage());
        }
    }

    public static void writeBoolean(String var0, boolean var1) {
        try {
            Binary.write(var0, new byte[]{(byte) (var1 ? 1 : 0)});
        } catch (Exception e) {
            Utlis.logError(Binary.class, e , "Da say ra loi:\n" + e.getMessage());
        }
    }

    public static String random() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

}
