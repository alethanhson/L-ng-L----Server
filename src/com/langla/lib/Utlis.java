package com.langla.lib;
import com.langla.utlis.UTPKoolVN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.langla.real.item.Item;
import com.langla.real.player.XYEntity;
import com.tgame.model.Caption;
import java.awt.Color;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.security.MessageDigest;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Random;
import java.util.TimeZone;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Utlis {

    public static Random rnd = new Random();
    private static Hashtable hashFileHandle = new Hashtable();
    public static long timeAdd;
    public static int timeOut = 30000;
    private static final String LOG_DIRECTORY = "data/pkoolvn/logs/error/";

    private static final String LOG_TRADE = "data/pkoolvn/logs/trade/";

    private static final String LOG_ADD_CHAR = "data/pkoolvn/logs/char/add/";

    private static final String LOG_ADD_RE = "data/pkoolvn/logs/char/remove/";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger logger = LoggerFactory.getLogger(Utlis.class);

    public static void logUpdate(String message) {

        String logFileName = LOG_DIRECTORY + "Alog-UPDATECHAR.txt";
        try (FileWriter fileWriter = new FileWriter(logFileName, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm a");
            String dateTime = LocalDateTime.now().format(formatter);
            printWriter.println(""+message+" TIME: " + dateTime + System.lineSeparator());
        } catch (IOException ioException) {
            logger.error("Loi khi ghi log", ioException);
        }
    }

    public static void logError(Class<?> clazz, Exception e, String message) {
        Logger logger = LoggerFactory.getLogger(clazz);

        String logFileName = LOG_DIRECTORY + "log-" + dateFormat.format(new Date()) + ".txt";
        try (FileWriter fileWriter = new FileWriter(logFileName, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("Timestamp: " + new Date());
            printWriter.println(message);

            logger.error(message, e); // Sử dụng logger.error

            e.printStackTrace(printWriter);
        } catch (IOException ioException) {
            logger.error("Loi khi ghi log", ioException);
        }
    }

    public static void logTrade(String message) {

        String logFileName = LOG_TRADE + "log-" + dateFormat.format(new Date()) + ".txt";
        try (FileWriter fileWriter = new FileWriter(logFileName, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm a");
            String dateTime = LocalDateTime.now().format(formatter);
            printWriter.println(""+message+" TIME: " + dateTime + System.lineSeparator());
        } catch (IOException ioException) {
            logger.error("Loi khi ghi log", ioException);
        }
    }

    public static void logAddChar(String message, int id) {

        String logFileName = LOG_ADD_CHAR + "" + id + ".txt";
        try (FileWriter fileWriter = new FileWriter(logFileName, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm a");
            String dateTime = LocalDateTime.now().format(formatter);
            printWriter.println(""+message+", TIME: " + dateTime + System.lineSeparator());
        } catch (IOException ioException) {
            logger.error("Loi khi ghi log", ioException);
        }
    }

    public static void logRemoveChar(String message, int id) {

        String logFileName = LOG_ADD_RE + "" + id + ".txt";
        try (FileWriter fileWriter = new FileWriter(logFileName, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm a");
            String dateTime = LocalDateTime.now().format(formatter);
            printWriter.println(""+message+", TIME: " + dateTime + System.lineSeparator());
        } catch (IOException ioException) {
            logger.error("Loi khi ghi log", ioException);
        }
    }
    public static long time() {
        return System.currentTimeMillis() + timeAdd;
    }

    public static int getDay() {
        Calendar calendar;
        (calendar = Calendar.getInstance()).setTimeInMillis(System.currentTimeMillis() + timeAdd);
        byte day = 0;
        switch (calendar.get(7)) {
            case 1:
                day = 6;
                break;
            case 2:
                day = 0;
                break;
            case 3:
                day = 1;
                break;
            case 4:
                day = 2;
                break;
            case 5:
                day = 3;
                break;
            case 6:
                day = 4;
                break;
            case 7:
                day = 5;
        }

        return day;
    }

    public  static  int checkDirection(int cx, int cx2) {
        if (cx < cx2) {
            return 2;
        } else {
            return 3;
        }
    }

    public static void sleep(long var0) {
        try {
            Thread.sleep(var0);
        } catch (InterruptedException var2) {
        }
    }

    public static int nextInt(int var0) {
        return var0 <= 0 ? 0 : rnd.nextInt(var0);
    }

    public static int nextInt(int var0, int var1) {
        return var1 == var0 && var1 == 0 ? 0 : var0 + Utlis.nextInt(var1 - var0 + 1);
    }

    public static int nextInt(int var0, int var1, int... value) {
        int c = nextInt(var0, var1);
        for (int i = 0; i < value.length; i++) {
            if (c == value[i]) {
                return nextInt(var0, var1, value);
            }
        }
        return c;
    }

    public static boolean nextBoolean() {
        return rnd.nextInt(2) != 0;
    }

    public static int nextInt(int[] var0) {
        return var0[Utlis.nextInt((int) 3)];
    }

    public static Color createColor(long var0) {
        float var2 = (float) ((var0 & 4278190080L) >> 24);
        float var3 = (float) ((var0 & 16711680L) >> 16);
        float var4 = (float) ((var0 & 65280L) >> 8);
        float var5 = (float) (var0 & 255L);
        return new Color(var3 / 255.0F, var4 / 255.0F, var5 / 255.0F, var2 / 255.0F);
    }

    public static boolean inRange(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        return (var4 <= var0 && var0 <= var6 || var4 <= var2 && var2 <= var6 || var0 <= var4 && var2 >= var6) && (var5 <= var1 && var1 <= var7 || var5 <= var3 && var3 <= var7 || var1 <= var5 && var3 >= var7) || (var0 <= var4 && var4 <= var2 || var0 <= var6 && var6 <= var2 || var4 <= var0 && var6 >= var2) && (var1 <= var5 && var5 <= var3 || var1 <= var7 && var7 <= var3 || var5 <= var1 && var7 >= var3);
    }

    public static boolean inRange(int var0, int var1, int var2, int var3, int var4, int var5) {
        return var0 <= var4 && var4 <= var2 && var1 <= var5 && var5 <= var3;
    }

    public static short a(byte var0) {
        switch (var0) {
            case 0:
                return 0;
            case 1:
                return -180;
            case 2:
                return -90;
            case 3:
                return -270;
            case 4:
                return 180;
            case 5:
                return 90;
            case 6:
                return 270;
            case 7:
                return -360;
            default:
                return 0;
        }
    }

    public static short b(byte var0) {
        switch (var0) {
            case 0:
                return -360;
            case 1:
                return 180;
            case 2:
                return 90;
            case 3:
                return 270;
            case 4:
                return -180;
            case 5:
                return -90;
            case 6:
                return -270;
            case 7:
                return 0;
            default:
                return -360;
        }
    }

    public static int positive(int var0) {
        return var0 > 0 ? var0 : -var0;
    }

    public static XYEntity[] a(XYEntity[] var0) {
        XYEntity[] var1 = new XYEntity[var0.length];
        int var2 = 0;

        for (int var3 = var1.length - 1; var3 >= 0; --var3) {
            var1[var2] = new XYEntity();
            var1[var2].cx = (short) (-var0[var3].cx);
            var1[var2].cy = var0[var3].cy;
            ++var2;
        }

        return var1;
    }

    public static int getRange(int var0, int var1) {
        return positive(var0 - var1);
    }

    public static int getRange2(int var0, int var1, int var2, int var3) {
        return (int) Math.sqrt((double) (positive((var0 - var2) * (var0 - var2)) + positive((var1 - var3) * (var1 - var3))));
    }

    public static int getRange(XYEntity var0, XYEntity var1) {
        return getRange(var0.cx, var0.cy, var1.cx, var1.cy);
    }

    public static int getRange(int var0, int var1, int var2, int var3) {
        var0 = positive(var0 - var2);
        var1 = positive(var1 - var3);
        return var0 > var1 ? var0 : var1;
    }

    public static void addAll(Vector var0, Vector var1) {
        for (int var2 = 0; var2 < var1.size(); ++var2) {
            var0.addElement(var1.elementAt(var2));
        }

    }

    public static String numberFormat(String var0) {
        try {
            Long.parseLong(var0);
        } catch (Exception var4) {
            return var0;
        }

        String var1 = "";
        String var2 = "";
        if (var0.charAt(0) == '-') {
            var2 = "-";
            var0 = var0.substring(1);
        }

        for (int var3 = var0.length() - 1; var3 >= 0; --var3) {
            if ((var0.length() - 1 - var3) % 3 == 0 && var0.length() - 1 - var3 > 0) {
                var1 = var0.charAt(var3) + "." + var1;
            } else {
                var1 = var0.charAt(var3) + var1;
            }
        }

        return var2 + var1;
    }

    public static String numberFormat(long var0) {
        return Utlis.numberFormat(String.valueOf(var0));
    }

    public static String numberFormat(int var0) {
        return Utlis.numberFormat(String.valueOf(var0));
    }

    public static String[] split(String var0, String var1) {
        Vector var2 = new Vector();

        for (int var3 = var0.indexOf(var1); var3 >= 0; var3 = (var0 = var0.substring(var3 + var1.length())).indexOf(var1)) {
            var2.addElement(var0.substring(0, var3));
        }

        var2.addElement(var0);
        String[] var4 = new String[var2.size()];
        var2.copyInto(var4);
        return var4;
    }

    public static int c(int var0, int var1) {
        return var0 / var1 + (var0 % var1 != 0 ? 1 : 0);
    }

    public static String replaceAll(String var0, String var1) {
        return var0.replaceAll("#", var1);
    }

//    public static String encodeString(String var0) {
//        try {
//            String var1 = AppListener.keyApp;
//            var0 = Base64Coder.encodeString(var0);
//            byte[] var4 = (var1 + var0).getBytes("UTF-8");
//
//            for (int var2 = 0; var2 < var4.length; ++var2) {
//                var4[var2] = (byte) (var4[var2] + var1.length());
//            }
//
//            return Base64Coder.encodeLines(var4).replaceAll("\n", "#");
//        } catch (Exception var3) {
//            return null;
//        }
//    }
//
//    public static String decodeString(String var0) {
//        try {
//            String var1 = AppListener.keyApp;
//            byte[] var4 = Base64Coder.decodeLines(var0.replaceAll("#", "\n"));
//
//            for (int var2 = 0; var2 < var4.length; ++var2) {
//                var4[var2] = (byte) (var4[var2] - var1.length());
//            }
//
//            return Base64Coder.decodeString((new String(var4, "UTF-8")).replaceAll(var1, "")).replaceAll("#", "\n");
//        } catch (Exception var3) {
//            return null;
//        }
//    }
    public static String nextUTF(int var0) {
        String var3 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder var1 = new StringBuilder(var0);

        for (int var2 = 0; var2 < var0; ++var2) {
            var1.append(var3.charAt(Utlis.nextInt(var3.length())));
        }

        return var1.toString();
    }

    private static double[] a(double var0, double var2, double var4, double var6, double var8, double var10) {
        double[] var12 = null;
        double var13 = var0 * var8 - var6 * var2;
        double var15 = var4 * var8 - var2 * var10;
        double var17 = var0 * var10 - var6 * var4;
        if (var13 != 0.0D) {
            var12 = new double[]{var15 / var13, var17 / var13};
        }

        return var12;
    }

    public static XYEntity a(XYEntity var0, XYEntity var1, XYEntity var2) {
        try {
            double[] var3 = a((double) var1.cx, 1.0D, (double) var1.cy, (double) var2.cx, 1.0D, (double) var2.cy);
            XYEntity var4 = XYEntity.create(var2.cx - var1.cx, var2.cy - var1.cy);
            double[] var7;
            var0 = XYEntity.create((int) (var7 = a(var3[0], -1.0D, -var3[1], (double) var4.cx, (double) var4.cy, (double) (var4.cx * var0.cx + var4.cy * var0.cy)))[0], (int) var7[1]);
            short var9 = var1.cx > var2.cx ? var1.cx : var2.cx;
            short var10 = var1.cy > var2.cy ? var1.cy : var2.cy;
            short var5 = var1.cx < var2.cx ? var1.cx : var2.cx;
            short var8 = var1.cy < var2.cy ? var1.cy : var2.cy;
            if (var0.cx >= var5 - 2 && var0.cx <= var9 + 2 && var0.cy >= var8 - 2 && var0.cy <= var10 + 2) {
                return var0;
            }
        } catch (Exception var6) {
        }

        return null;
    }

    private static boolean a(int var0, int var1, XYEntity var2, XYEntity var3) {
        return (var3.cx > var2.cx && var2.cx <= var0 && var0 <= var3.cx || var2.cx >= var3.cx && var2.cx >= var0 && var0 >= var3.cx) && (var3.cy > var2.cy && var2.cy <= var1 && var1 <= var3.cy || var2.cy >= var3.cy && var2.cy >= var1 && var1 >= var3.cy);
    }

    public static XYEntity a(XYEntity var0, XYEntity var1, XYEntity var2, XYEntity var3) {
        double var9 = (double) var0.cx;
        double var11 = (double) var1.cx;
        double var13 = (double) var2.cx;
        double var15 = (double) var3.cx;
        double var17 = (double) var0.cy;
        double var19 = (double) var1.cy;
        double var21 = (double) var2.cy;
        double var23 = (double) var3.cy;
        XYEntity var10000;
        double var25;
        if ((var25 = (var9 - var11) * (var21 - var23) - (var17 - var19) * (var13 - var15)) == 0.0D) {
            var10000 = null;
        } else {
            double var27 = ((var13 - var15) * (var9 * var19 - var17 * var11) - (var9 - var11) * (var13 * var23 - var21 * var15)) / var25;
            double var29 = ((var21 - var23) * (var9 * var19 - var17 * var11) - (var17 - var19) * (var13 * var23 - var21 * var15)) / var25;
            var10000 = XYEntity.create((int) var27, (int) var29);
        }

        XYEntity var4 = var10000;
        return var10000 != null && a(var4.cx, var4.cy, var0, var1) && a(var4.cx, var4.cy, var2, var3) ? var4 : null;
    }

    public static String e(int var0) {
        String var1 = String.valueOf(var0);
        if (var0 < 100) {
            var1 = "0" + var1;
        }

        StringBuilder var3 = new StringBuilder();

        for (int var2 = 0; var2 < var1.length(); ++var2) {
            if (var2 == 0) {
                var3.append(var1.charAt(var2));
            } else {
                var3.append("." + var1.charAt(var2));
            }
        }

        return var3.toString();
    }

    public static String getSTRTimeFormSeconds(int second) {
        int var1 = 0;
        if (second > 60) {
            var1 = second / 60;
            second %= 60;
        }

        int var2 = 0;
        if (var1 > 60) {
            var2 = var1 / 60;
            var1 %= 60;
        }

        int var3 = 0;
        if (var2 > 24) {
            var3 = var2 / 24;
            var2 %= 24;
        }

        String var4 = "";
        if (var3 > 0) {
            var4 = var4 + var3;
            var4 = var4 + "d";
            var4 = var4 + var2 + "h";
        } else if (var2 > 0) {
            var4 = var4 + var2;
            var4 = var4 + "h";
            var4 = var4 + var1 + "'";
        } else {
            if (var1 > 9) {
                var4 = var4 + var1;
            } else {
                var4 = var4 + "0" + var1;
            }

            var4 = var4 + ":";
            if (second > 9) {
                var4 = var4 + second;
            } else {
                var4 = var4 + "0" + second;
            }
        }

        return var4;
    }
    public static int millisecondsToMinutes(long milliseconds) {
        // Chuyển đổi milliseconds sang phút
        return (int) (milliseconds / (1000 * 60));
    }
    public static String getTextTimeFormSeconds(int var0) {
        int var1 = 0;
        if (var0 > 60) {
            var1 = var0 / 60;
            var0 %= 60;
        }

        int var2 = 0;
        if (var1 > 60) {
            var2 = var1 / 60;
            var1 %= 60;
        }

        int var3 = 0;
        if (var2 > 24) {
            var3 = var2 / 24;
            var2 %= 24;
        }

        String var4 = "";
        if (var3 > 0) {
            var4 = var4 + var3;
            var4 = var4 + " " + Caption.jZ;
            var4 = var4 + var2 + " " + Caption.ka;
        } else if (var2 > 0) {
            var4 = var4 + var2;
            var4 = var4 + " " + Caption.kc;
            var4 = var4 + var1 + " " + Caption.kb;
        } else {
            if (var1 > 9) {
                var4 = var4 + var1;
            } else {
                var4 = var4 + "0" + var1;
            }

            var4 = var4 + ":";
            if (var0 > 9) {
                var4 = var4 + var0;
            } else {
                var4 = var4 + "0" + var0;
            }
        }

        return var4;
    }

    public static int h(int var0) {
        switch (var0) {
            case 1:
                return -2303742;
            case 2:
                return -16663468;
            case 3:
                return -16744706;
            case 4:
                return -2031616;
            case 5:
                return -4275264;
            default:
                return 0;
        }
    }

    public static String i(int var0) {
        switch (var0) {
            case 1:
                return "c#kim";
            case 2:
                return "c#moc";
            case 3:
                return "c#thuy";
            case 4:
                return "c#hoa";
            case 5:
                return "c#tho";
            default:
                return "";
        }
    }

    public static int currentTimeSeconds() {
        return (int) ((System.currentTimeMillis() + timeAdd) / 1000L);
    }

    public static String j(int var0) {
        int var2;
        if (var0 >= 3600) {
            int var1 = var0 / 3600;
            if ((var2 = var0 % 3600) > 60) {
                var2 /= 60;
            }

            return var2 == 0 ? var1 + ":00" : var1 + ":" + (var2 < 10 ? "0" + var2 : var2);
        } else if (var0 >= 60) {
            var2 = var0 / 60;
            return (var0 %= 60) == 0 ? var2 + ":00" : var2 + ":" + (var0 < 10 ? "0" + var0 : var0);
        } else if (var0 >= 0) {
            var0 %= 60;
            return "00:" + (var0 < 10 ? "0" + var0 : var0);
        } else {
            return "0";
        }
    }

    public static String k(int var0) {
        int var2;
        int var3;
        if (var0 > 86400) {
            int var1 = var0 / 86400;
            var2 = (var0 %= 86400) / 3600;
            if ((var3 = var0 % 3600) > 60) {
                var3 /= 60;
            }

            return var3 == 0 ? var1 + Caption.kd + var2 + " " + Caption.kc : var1 + Caption.kd + var2 + " " + Caption.kc + " " + var3 + " " + Caption.kb;
        } else if (var0 >= 3600) {
            var2 = var0 / 3600;
            if ((var3 = var0 % 3600) > 60) {
                var3 /= 60;
            }

            return var3 == 0 ? var2 + " " + Caption.kc : var2 + " " + Caption.kc + " " + var3 + " " + Caption.kb;
        } else if (var0 >= 60) {
            var3 = var0 / 60;
            return (var0 %= 60) == 0 ? var3 + " " + Caption.kb : var3 + " " + Caption.kb + " " + var0 + " " + Caption.ke;
        } else if (var0 >= 0) {
            var0 %= 60;
            return var0 + " " + Caption.ke;
        } else {
            return "0 " + Caption.ke;
        }
    }

    public static String e(String var0) {
        int var1;
        for (var1 = var0.length() - 1; var1 >= 0 && Character.isWhitespace(var0.charAt(var1)); --var1) {
        }

        return var0.substring(0, var1 + 1);
    }

    public static String l(int var0) {
        return "" + (float) var0 / 1000.0F;
    }

    public static byte[] a(byte[] var0) {
        try {
            if (var0 != null) {
                int var1;
                if ((var1 = var0.length) > 51) {
                    var1 = 51;
                }

                byte[] var4 = new byte[var1];

                int var2;
                for (var2 = 0; var2 < var4.length; ++var2) {
                    var4[50 - var2] = var0[var2];
                }

                for (var2 = 0; var2 < var4.length; ++var2) {
                    var0[var2] = var4[var2];
                }
            }
        } catch (Exception var3) {
        }

        return var0;
    }

    public static byte[] b(byte[] var0) {
        try {
            if (var0 != null) {
                int var1;
                byte var5;
                if ((var5 = (byte) (var1 = var0.length)) == 0) {
                    var5 = 100;
                }

                byte[] var2 = new byte[var0.length];

                for (int var3 = 0; var3 < var0.length; ++var3) {
                    var2[var3] = (byte) (var0[var3] - var5);
                }

                return var2;
            }
        } catch (Exception var4) {
        }

        return null;
    }

//    public static FileHandle internal(String var0) {
//        String var1 = "internal" + var0;
//        if (!var0.contains("/")) {
//
//            var0 = "C:\\LangLa\\resource\\" + var0;
//        }
//
//        if (hashFileHandle.get(var1) != null) {
//            return (FileHandle) hashFileHandle.get(var1);
//        } else {
//            FileHandle var2 = Gdx.files.internal(var0);
//            hashFileHandle.put(var1, var2);
//            return var2;
//        }
//    }
//
//    public static FileHandle external(String var0) {
//        String var1 = "external" + var0;
//        if (hashFileHandle.get(var1) != null) {
//            return (FileHandle) hashFileHandle.get(var1);
//        } else {
//            FileHandle var2 = Gdx.files.external(var0);
//            hashFileHandle.put(var1, var2);
//            return var2;
//        }
//    }
//
//    public static void e() {
//        try {
//            LangLa_q.e = true;
//            LangLa_fq.d();
//            if (AppListener.c != null) {
//                AppListener.c.a();
//            }
//
//            if (AppListener.d != null) {
//                AppListener.d.a();
//            }
//
//            if (AppListener.e != null) {
//                AppListener.e.a();
//            }
//
//            AppListener.g = false;
//            AppListener.gI().createCanvas();
//            Session.gI();
//        } catch (Exception var0) {
//        }
//    }
//
//    public static boolean f() {
//        return DataCenter.gI().currentScreen.cE instanceof LangLa_bo;
//    }
    private static String o(int var0) {
        return var0 < 10 ? "0" + var0 : "" + var0;
    }

    public static String d(long var0) {
        SimpleDateFormat var2;
        (var2 = new SimpleDateFormat("yyyy-MM-dd HH:mm")).setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        return var2.format(new Date(var0));
    }

    public static String e(long var0) {
        SimpleDateFormat var2;
        (var2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        return var2.format(new Date(var0));
    }

    public static String m(int var0) {
        Calendar var1;
        (var1 = Calendar.getInstance()).setTime(new Date(946573200129L + (long) var0 * 1000L));
        return o(var1.get(11)) + ":" + o(var1.get(12));
    }

    public static boolean d(int var0, int var1) {
        Calendar var2;
        (var2 = Calendar.getInstance()).setTimeInMillis(System.currentTimeMillis() + timeAdd);
        var2.set(11, 0);
        var2.set(12, 0);
        var2.set(13, 0);
        var2.setTimeInMillis(var2.getTimeInMillis() + (long) var0 * 1000L);
        Calendar var6;
        (var6 = Calendar.getInstance()).setTimeInMillis(System.currentTimeMillis() + timeAdd);
        var6.set(11, 0);
        var6.set(12, 0);
        var6.set(13, 0);
        var6.setTimeInMillis(var6.getTimeInMillis() + (long) var1 * 1000L);
        long var4 = System.currentTimeMillis() + timeAdd;
        return var2.getTimeInMillis() <= var4 && var4 <= var6.getTimeInMillis();
    }

    public static int n(int var0) {
        Calendar var1;
        (var1 = Calendar.getInstance()).setTimeInMillis(System.currentTimeMillis() + timeAdd);
        var1.set(11, 0);
        var1.set(12, 0);
        var1.set(13, 0);
        var1.setTimeInMillis(var1.getTimeInMillis() + (long) var0 * 1000L);
        long var2 = System.currentTimeMillis() + timeAdd;
        return (int) (var1.getTimeInMillis() - var2) / 1000;
    }

    public static String f(long var0) {
        Calendar var2;
        (var2 = Calendar.getInstance()).setTime(new Date(var0));
        String var5 = o(var2.get(11));
        String var1 = o(var2.get(12));
        String var3 = o(var2.get(5));
        String var4 = o(var2.get(2) + 1);
        int var6 = var2.get(1);
        return var3 + "/" + var4 + "/" + var6 + " " + var5 + "h" + var1 + "'";
    }

//    public static void a(Graphics var0, int var1, int var2) {
//        var0.f(8732953);
//        var0.b(0, 0, var1 - 1, var2 - 1);
//        var0.f(6501888);
//        var0.b(1, 1, var1 - 2 - 1, var2 - 2 - 1);
//    }
    public static String a(String var0, int var1) {
        return (var0 = var0.substring(0, 1).toUpperCase() + var0.substring(1, var0.length()).toLowerCase()).length() > var1 ? var0.substring(0, var1) + "..." : var0;
    }

    public static boolean h(String var0) {
        return var0 == null || var0.equals("");
    }

    public static String i(String var0) {
        if (!var0.toLowerCase().contains(".zip")) {
            var0 = var0.replaceAll("imgfake1", "img").replaceAll("imgfake", "img").replaceAll("imgios", "img");
        }

        return var0;
    }

//    public static boolean g() {
//        return a((String) DataCenter.gI().ipServer, DataCenter.gI().portServer, 1);
//    }
    public static boolean b(String var0, int var1) {
        return a((String) var0, var1, 1);
    }

    private static boolean a(String var0, int var1, int var2) {
        while (true) {
            Socket var3 = null;

            try {
                new Socket();
                if (var1 + 1 == 6869) {
                    var1 = 6969;
                } else {
                    ++var1;
                }

                var3 = new Socket();
                InetSocketAddress var4 = new InetSocketAddress(var0, var1);
                var3.connect(var4, 1000 * var2);
                var3.setSoTimeout(1000 * var2);
                if (var3.isConnected()) {
                    return true;
                }
            } catch (Exception var18) {
            } finally {
                try {
                    var3.close();
                } catch (Exception var17) {
                }

            }

            if (var2 >= 6) {
                return false;
            }

            ++var2;
//            if (var2 == 3 && !var0.equals("localhost")) {
//                short var21 = DataCenter.gI().aP.id;
//                i();
//                ArrayServer[] var20;
//                int var5 = (var20 = DataCenter.gI().arrayServers).length;
//
//                for (int var6 = 0; var6 < var5; ++var6) {
//                    Server[] var7;
//                    int var8 = (var7 = var20[var6].servers).length;
//
//                    for (int var9 = 0; var9 < var8; ++var9) {
//                        Server var10;
//                        if ((var10 = var7[var9]).id == var21) {
//                            DataCenter.gI().ipServer = var10.ip;
//                            var0 = var10.ip;
//                            DataCenter.gI().aP = var10;
//                            break;
//                        }
//                    }
//                }
//            }

            var1 = var1;
            var0 = var0;
        }
    }

//    private static boolean i() {
//        try {
//            Vector var0 = new Vector();
//            String var1;
//            if (DataCenter.ar && !AppListener.isDesktop()) {
//                var1 = Binary.d("https://sg188.vcmedia.vn/servetestip.txt");
//            } else {
//                var1 = Binary.d("https://sg188.vcmedia.vn/serverlistip.txt");
//            }
//
//            if (var1 != null && var1.length() != 0) {
//                DataCenter.gI().aP = null;
//                String[] var13 = var1.replaceAll(" =", "=").replaceAll("= ", "=").split("id=");
//                boolean var3 = true;
//
//                ArrayServer var2;
//                int var4;
//                for (var4 = 0; var4 < var13.length; ++var4) {
//                    if (var13[var4].trim().length() > 0) {
//                        String[] var5 = var13[var4].trim().split("\r\n");
//                        (var2 = new ArrayServer()).nameServers = var5[0];
//                        Vector var6 = new Vector();
//
//                        int var7;
//                        for (var7 = 1; var7 < var5.length; ++var7) {
//                            if (var5[var7].trim().length() > 0) {
//                                String[] var8;
//                                String[] var9 = (var8 = var5[var7].split("="))[0].split(":");
//                                String[] var10 = var8[1].split(":");
//                                DataCenter.gI().ipServer = var10[1];
//                                DataCenter.gI().portServer = Integer.parseInt(var10[2]);
//                                Server var11 = new Server();
//                                if (var9.length <= 1) {
//                                    var11.name = var8[0];
//                                } else {
//                                    var11.TEXT = var9[0];
//                                    var11.name = var9[1];
//                                }
//
//                                if (!var11.TEXT.toUpperCase().equals(Caption.nB)) {
//                                    var11.id = Short.parseShort(var10[0]);
//                                    var11.ip = var10[1];
//                                    var11.port = Short.parseShort(var10[2]);
//                                    var11.portCheck = Short.parseShort(var10[3]);
//                                    if (var10.length > 4) {
//                                        var11.STR_STATUS = var10[4];
//                                    }
//
//                                    if (var10.length > 5) {
//                                        var11.SELECT = Boolean.parseBoolean(var10[5]);
//                                    }
//
//                                    if (var11.id == 101) {
//                                        var3 = false;
//                                        DataCenter.gI().selectServer = var11;
//                                    } else {
//                                        var3 = true;
//                                        var6.add(var11);
//                                    }
//                                }
//                            }
//                        }
//
//                        if (var3) {
//                            var2.servers = new Server[var6.size()];
//
//                            for (var7 = 0; var7 < var6.size(); ++var7) {
//                                var2.servers[var7] = (Server) var6.get(var7);
//                                if (var2.servers[var7].SELECT) {
//                                    DataCenter.gI().aP = var2.servers[var7];
//                                }
//                            }
//
//                            var0.add(var2);
//                        }
//                    }
//                }
//
//                DataCenter.gI().arrayServers = new ArrayServer[var0.size()];
//
//                for (var4 = 0; var4 < var0.size(); ++var4) {
//                    DataCenter.gI().arrayServers[var4] = (ArrayServer) var0.get(var4);
//                }
//
//                if (DataCenter.gI().aP == null) {
//                    var2 = DataCenter.gI().arrayServers[DataCenter.gI().arrayServers.length - 1];
//                    DataCenter.gI().aP = var2.servers[var2.servers.length - 1];
//                }
//
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception var12) {
//            return false;
//        }
//    }
    public static boolean haveNetwork() {
        return c("google.com", 80) || c("facebook.com", 80);
    }

    private static boolean c(String var0, int var1) {
        Socket var16 = null;

        try {
            try {
                var16 = new Socket();
                InetSocketAddress var17 = new InetSocketAddress(var0, 80);
                var16.connect(var17, 3000);
                return true;
            } catch (Exception var11) {
            }

            try {
                var0 = "https://" + var0;
                HttpURLConnection var14;
                (var14 = (HttpURLConnection) (new URL(var0)).openConnection()).setConnectTimeout(3000);
                var14.setReadTimeout(3000);
                var14.setRequestMethod("HEAD");
                int var2 = var14.getResponseCode();
                var14.disconnect();
                boolean var15 = 200 <= var2 && var2 <= 399;
                return var15;
            } catch (IOException var12) {
            }
        } finally {
            try {
                var16.close();
            } catch (Exception var10) {
            }

        }

        return false;
    }

//    public static boolean a(byte[] var0, String var1) {
//        try {
//            mSocket var2 = null;
//            DataOutputStream var3 = null;
//            DataInputStream var4 = null;
//            boolean var8 = false;
//
//            label127:
//            {
//                label128:
//                {
//                    try {
//                        var8 = true;
//                        var4 = (var2 = new mSocket(DataCenter.gI().c, DataCenter.gI().d)).b();
//                        var3 = var2.a();
//                        byte[] var12 = var1.getBytes("UTF-8");
//                        var3.write(new byte[]{(byte) var12.length});
//                        var3.write(var12);
//                        int var13 = var0.length;
//                        var3.write(new byte[]{(byte) (var13 >>> 24), (byte) (var13 >>> 16), (byte) (var13 >>> 8), (byte) var13});
//                        var3.write(var0);
//                        var3.flush();
//                        if (var4.readByte() == 1) {
//                            var8 = false;
//                            break label127;
//                        }
//
//                        var8 = false;
//                        break label128;
//                    } catch (Exception var9) {
//                        var8 = false;
//                    } finally {
//                        if (var8) {
//                            if (var4 != null) {
//                                var4.close();
//                            }
//
//                            if (var3 != null) {
//                                var3.close();
//                            }
//
//                            if (var2 != null) {
//                                var2.c();
//                            }
//
//                        }
//                    }
//
//                    if (var4 != null) {
//                        var4.close();
//                    }
//
//                    if (var3 != null) {
//                        var3.close();
//                    }
//
//                    if (var2 != null) {
//                        var2.c();
//                    }
//
//                    return false;
//                }
//
//                var4.close();
//                var3.close();
//                var2.c();
//                return false;
//            }
//
//            var4.close();
//            var3.close();
//            var2.c();
//            return true;
//        } catch (Exception var11) {
//            return false;
//        }
//    }
//
//    public static byte[] j(String var0) {
//        try {
//            mSocket var1 = null;
//            DataOutputStream var2 = null;
//            DataInputStream var3 = null;
//            boolean var11 = false;
//
//            label133:
//            {
//                byte[] var16;
//                try {
//                    var11 = true;
//                    var3 = (var1 = new mSocket(DataCenter.gI().c, DataCenter.gI().e)).b();
//                    var2 = var1.a();
//                    byte[] var15 = var0.getBytes("UTF-8");
//                    var2.write(new byte[]{(byte) var15.length});
//                    var2.write(var15);
//                    var2.flush();
//                    int var4;
//                    var15 = new byte[var4 = var3.readInt()];
//                    int var5 = 0;
//
//                    while (true) {
//                        if (var4 <= 0) {
//                            var16 = var15;
//                            var11 = false;
//                            break;
//                        }
//
//                        int var6;
//                        if (var4 - 3072 <= 0) {
//                            var6 = var4;
//                        } else {
//                            var6 = 3072;
//                        }
//
//                        int var7;
//                        if ((var7 = var3.available()) == 0) {
//                            sleep(1L);
//                        } else {
//                            if (var6 > var7) {
//                                var6 = var7;
//                            }
//
//                            var3.read(var15, var5, var6);
//                            var5 += var6;
//                            var4 -= var6;
//                        }
//                    }
//                } catch (Exception var12) {
//                    var11 = false;
//                    break label133;
//                } finally {
//                    if (var11) {
//                        if (var3 != null) {
//                            var3.close();
//                        }
//
//                        if (var2 != null) {
//                            var2.close();
//                        }
//
//                        if (var1 != null) {
//                            var1.c();
//                        }
//
//                    }
//                }
//
//                var3.close();
//                var2.close();
//                var1.c();
//                return var16;
//            }
//
//            if (var3 != null) {
//                var3.close();
//            }
//
//            if (var2 != null) {
//                var2.close();
//            }
//
//            if (var1 != null) {
//                var1.c();
//            }
//        } catch (Exception var14) {
//        }
//
//        return null;
//    }
    public static String k(String var0) {
        StringBuilder var1 = new StringBuilder();

        try {
            MessageDigest var2;
            (var2 = MessageDigest.getInstance("MD5")).update(var0.getBytes("UTF8"));
            byte[] var4 = var2.digest();

            for (int var5 = 0; var5 < var4.length; ++var5) {
                if ((var4[var5] & 255) < 16) {
                    var1.append("0");
                }

                var1.append(Long.toString((long) (var4[var5] & 255), 16));
            }
        } catch (Exception var3) {
        }

        return var1.toString();
    }

    public static void write(String var0, byte[] var1) {
        Object var2 = null;
        Object var3 = null;

        try {
            FileOutputStream var12;
            (var12 = new FileOutputStream(var0)).write(var1);
            var12.close();
            return;
        } catch (Exception var10) {
        } finally {
            try {
                if (var3 != null) {
                    ((BufferedWriter) var3).close();
                }

                if (var2 != null) {
                    ((OutputStreamWriter) var2).close();
                }
            } catch (Exception var9) {
            }

        }

    }

    public static int readLength(String var0) {
        byte[] var1;
        return (var1 = read(var0)) != null ? var1.length : 0;
    }

    public static byte[] read(String var0) {
        FileInputStream var1 = null;
        boolean var8 = false;

        label117:
        {
            byte[] var16;
            label118:
            {
                try {
                    var8 = true;
                    File var15;
                    if (!(var15 = new File(var0)).exists()) {
                        var0 = null;
                        var8 = false;
                        break label117;
                    }

                    var1 = new FileInputStream(var15);
                    var16 = new byte[(int) var15.length()];
                    var1.read(var16, 0, var16.length);
                    var16 = var16;
                    var8 = false;
                    break label118;
                } catch (Exception var13) {
                    var8 = false;
                } finally {
                    if (var8) {
                        try {
                            if (var1 != null) {
                                var1.close();
                            }
                        } catch (Exception var10) {
                        }

                    }
                }

                try {
                    if (var1 != null) {
                        var1.close();
                    }
                } catch (Exception var9) {
                }

                return null;
            }

            try {
                var1.close();
            } catch (Exception var12) {
            }

            return var16;
        }

        try {
            if (var1 != null) {
                var1.close();
            }
        } catch (Exception var11) {
        }

        return null;
    }

    public static byte[] inflateByteArray(byte[] var0) {
        Inflater var2;
        (var2 = new Inflater()).setInput(var0);
        ByteArrayOutputStream var1 = new ByteArrayOutputStream();
        byte[] var3 = new byte[4096];

        try {
            while (!var2.finished()) {
                int var4 = var2.inflate(var3);
                var1.write(var3, 0, var4);
            }

            byte[] var13 = var1.toByteArray();
            return var13;
        } catch (Exception var11) {
        } finally {
            try {
                var1.close();
            } catch (Exception var10) {
            }

        }

        return var0;
    }

    public static byte[] deflateByteArray(byte[] var0) {
        ByteArrayOutputStream var2 = new ByteArrayOutputStream();
        Deflater var4;
        (var4 = new Deflater()).setLevel(9);
        var4.setInput(var0);
        var4.finish();
        byte[] var5 = new byte[4096];

        while (!var4.finished()) {
            var2.write(var5, 0, var4.deflate(var5));
        }

        var4.end();
        return var2.toByteArray();
    }

    public static ArrayList getArrayListNotNull(Object[] arrItemBody, ArrayList list) {
        if (list == null) {
            list = new ArrayList();
        }
        if (arrItemBody != null) {
            for (int i = 0; i < arrItemBody.length; i++) {
                if (arrItemBody[i] != null) {
                    list.add(arrItemBody[i]);
                }
            }
        }
        return list;
    }

    public static Item[] cloneArray(Item[] arr) {
        Item[] array = new Item[arr.length];
        for (int i = 0; i < array.length; i++) {
            try {
                array[i] = arr[i].cloneItem();
            } catch (Exception ex) {
            }

        }
        return array;
    }

    public static String cleantext(String is) {
        is = deAccent(is.trim());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < is.length(); i++) {
            char var2 = is.toUpperCase().charAt(i);
            if (!(var2 < 'A' || var2 > 'Z') || var2 >= '0' && var2 <= '9' || var2 == ' ') {
                sb.append(is.charAt(i));
            }
        }

        return sb.toString().trim();
    }

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    public static boolean randomBoolean(float max, float numz) {
        if (numz <= 0) {
            return false;
        }
        if (numz >= max) {
            return true;
        }
        max *= 100;
        numz *= 100;
        return Utlis.nextInt((int) max) <= max * (numz / 10000);
    }
    public static ByteArrayOutputStream loadFile(String url) {
        FileInputStream openFileInput;
        try {
            openFileInput = new FileInputStream(url);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[1024];
            while (true) {
                int read = openFileInput.read(bArr);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
            openFileInput.close();
            return byteArrayOutputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveFile(String url, byte[] data) {
        try {
            File f = new File(url);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(url);
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
