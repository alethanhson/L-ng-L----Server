package com.langla.utlis;

import com.PKoolVNDB;
import com.langla.lib.Utlis;
import com.langla.real.player.Player;
import com.langla.server.main.PKoolVN;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * @author PKoolVN
 **/
public class UTPKoolVN {

    private final static Locale locale = new Locale("vi");
    private final static NumberFormat en = NumberFormat.getInstance(locale);
    private final static Random rand = new Random();
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static SimpleDateFormat dateFormatWeek = new SimpleDateFormat("yyyy-MM-ww");
    private final static SimpleDateFormat dateFormatDay= new SimpleDateFormat("yyyy-MM-dd");

    public static Date getDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException ex) {
            Utlis.logError(Player.class, ex , "Da say ra loi:\n" + ex.getMessage());
        }
        return null;
    }

    public static long TimeDay(int nDays) {
        return System.currentTimeMillis() + (nDays * 86400000L);
    }

    public static long TimeHours(int nHours) {
        return System.currentTimeMillis() + (+nHours * 3600000L);
    }

    public static long TimeMinutes(int nMinutes) {
        return System.currentTimeMillis() + (nMinutes * 60000L);
    }

    public static long TimeSeconds(long nSeconds) {
        return System.currentTimeMillis() + (nSeconds * 1000L);
    }

    public static long TimeMillis(long nMillis) {
        return System.currentTimeMillis() + nMillis;
    }


    public static int CurrentTimeSecond() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static long CurrentTimeMillis() {
        return System.currentTimeMillis();
    }


    public static Date DateDay(int nDays) {
        Date dat = new Date();
        dat.setTime(dat.getTime() + nDays * 86400000L);
        return dat;
    }

    public static String toDateString(Date date) {
        return dateFormat.format(date);
    }

    public static Date DateHours(int nHours) {
        Date dat = new Date();
        dat.setTime(dat.getTime() + nHours * 3600000L);
        return dat;
    }

    public static Date DateMinutes(int nMinutes) {
        Date dat = new Date();
        dat.setTime(dat.getTime() + nMinutes * 60000L);
        return dat;
    }

    public static Date DateSeconds(long nSeconds) {
        Date dat = new Date();
        dat.setTime(dat.getTime() + nSeconds * 1000L);
        return dat;
    }

    public static int getHour() {
        LocalTime currentTime = LocalTime.now();
        return currentTime.getHour();
    }
    public static int getMinute() {
        LocalTime currentTime = LocalTime.now();
        return currentTime.getMinute();
    }
    public static int getSecond() {
        LocalTime currentTime = LocalTime.now();
        return currentTime.getSecond();
    }
    public static String getFormatNumber(long num) {
        return en.format(num);
    }

    public static boolean compare_Week(Date now, Date when) {
        try {
            Date date1 = dateFormatWeek.parse(dateFormatWeek.format(now));
            Date date2 = dateFormatWeek.parse(dateFormatWeek.format(when));
            if (date1.equals(date2))
                return false;
            else
                return !date1.before(date2);
        } catch (ParseException p) {
            p.printStackTrace();
        }
        return false;
    }
    public static int getRandomList(List<Integer> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }
    public synchronized static boolean compare_Day(Date now, Date when) {
        try {
            Date date1 = dateFormatDay.parse(dateFormatDay.format(now));
            Date date2 = dateFormatDay.parse(dateFormatDay.format(when));
            if (date1.equals(date2))
                return false;
            else
                return !date1.before(date2);
        } catch (ParseException p) {
            p.printStackTrace();
        }
        return false;
    }


    public static int getItemToOption(int iditem) {
        switch (iditem) {
            case 406:
                return 199;
            case 407:
                return 200;
            case 408:
                return 201;
            case 409:
                return 202;
            case 410:
                return 203;
            case 411:
                return 204;
            case 412:
                return 205;
            case 413:
                return 206;
            case 826:
                return 344;
            case 827:
                return 345;
            default:
                return -1;
        }
    }

    public static int getOptionToItem(int idoption) {
        switch (idoption) {
            case 199:
                return 406;
            case 200:
                return 407;
            case 201:
                return 408;
            case 202:
                return 409;
            case 203:
                return 410;
            case 204:
                return 411;
            case 205:
                return 412;
            case 206:
                return 413;
            case 344:
                return 826;
            case 345:
                return 827;
            default:
                return -1;
        }
    }
    public static boolean checkNumInt(String num) {
        return Pattern.compile("^[0-9]+$").matcher(num).find();
    }

    public static int UnsignedByte(byte b) {
        int ch = b;
        if (ch < 0) {
            return ch + 256;
        }
        return ch;
    }

    public static String parseString(String str, String wall) {
        return (!str.contains(wall)) ? null : str.substring(str.indexOf(wall) + 1);
    }

    public static boolean CheckString(String str, String c) {
        return Pattern.compile(c).matcher(str).find();
    }

    public static String strSQL(String str) {
        return str.replaceAll("['\"\\\\]", "\\\\$0");
    }

    public static int nextInt(int x1, int x2) {
        int to = x2;
        int from = x1;
        if (x2 < x1) {
            to = x1;
            from = x2;
        }
        return from + rand.nextInt((to + 1) - from);
    }
    // Phương thức tạo captcha

    // Phương thức tạo hình ảnh từ captcha và chuyển thành mảng byte
    // Phương thức tạo hình ảnh từ captcha và chuyển thành mảng byte
    public static byte[] createImage(String captcha) {
        try {
            int width = 160;
            int height = 60;

            // Tạo một BufferedImage
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            // Lấy đối tượng Graphics2D từ BufferedImage
            Graphics2D g2d = image.createGraphics();

            // Thiết lập màu nền trắng cho hình ảnh
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, width, height);
            // Thêm noise (nhiễu) vào hình ảnh captcha
            for (int i = 0; i < 150; i++) {
                int x = (int) (Math.random() * width);
                int y = (int) (Math.random() * height);
                int gray = (int) (Math.random() * 128) + 128; // Màu nền xám
                Color color = new Color(gray, gray, gray);
                g2d.setColor(color);
                g2d.fillRect(x, y, 1, 1);
            }
            // Thiết lập font cho văn bản captcha
            Font font = new Font("Arial", Font.BOLD, 30);
            g2d.setFont(font);

            // Sắp xếp ngẫu nhiên các ký tự và màu chữ
            char[] chars = captcha.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                // Thiết lập màu chữ ngẫu nhiên cho từng ký tự
                Color textColor = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
                g2d.setColor(textColor);

                int x = 20 + (i * 30) + (int) (Math.random() * 10);
                int y = 30 + (int) (Math.random() * 20);
                g2d.drawString(chars[i] + "", x, y);
            }

            // Giải phóng đối tượng Graphics2D
            g2d.dispose();

            // Chuyển đổi hình ảnh thành mảng byte
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageData = baos.toByteArray();
            baos.close();

            return imageData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String generateCaptcha() {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int captchaLength = 4;
        StringBuilder captcha = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < captchaLength; i++) {
            int index = random.nextInt(charSet.length());
            char randomChar = charSet.charAt(index);
            captcha.append(randomChar);
        }

        return captcha.toString();
    }
    public static int nextInt(int max) {
        return rand.nextInt(max);
    }


    public static void Print(String v) {
        System.out.println("!-PKoolVN =>>>| " + v);
    }
    public static void Debug(String v) {
        if (PKoolVNDB.isDebug){
            System.out.println("!-PK_DEBUG =>>>| " + v);
        }
    }
}
