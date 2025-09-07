package com;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PKoolVNDB {
    
    // Database configuration
    public static String DRIVER = "com.mysql.jdbc.Driver";
    public static String URL = "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&useSSL=false";
    public static String DB_HOST = "127.0.0.1";
    public static String DB_PORT = "3307";
    public static String DB_NAME = "langla";
    public static String DB_USER = "langla_user";
    public static String DB_PASSWORD = "langla123";
    public static int MIN_CONN = 1;
    public static int MAX_CONN = 10;
    
    // Server configuration
    public static String HOST = "127.0.0.1";
    public static int PORT_SERVER = 2907;
    public static int PORT_CHECK_ONLINE = 2908;
    public static String URL_WEB = "http://127.0.0.1:8888";
    public static int version = 131;
    
    // Security settings
    public static int MAX_CONNECTIONS_PER_IP = 50;
    public static long RATE_LIMIT_RESET_TIME = 50000;
    public static boolean isFw = false;
    public static boolean isDebug = false;
    public static boolean addshop = false;
    public static boolean addshop_hokage = false;
    
    // EXP configuration
    public static int EXP_MULTIPLIER = 5; // Giá trị mặc định
    
    // Additional fields
    public static String z = "";
    public static String buid = "";
    
    private static Properties properties;
    
    public static void loadProperties() {
        try {
            properties = new Properties();
            properties.load(new FileInputStream("PKoolVN_Config.properties"));
            
            // Load database settings
            DRIVER = properties.getProperty("pkoolvn.driver", DRIVER);
            DB_HOST = properties.getProperty("pkoolvn.host", DB_HOST).trim();
            DB_PORT = properties.getProperty("pkoolvn.mysql-port", DB_PORT);
            DB_NAME = properties.getProperty("pkoolvn.mysql-database", DB_NAME);
            DB_USER = properties.getProperty("pkoolvn.mysql-user", DB_USER);
            DB_PASSWORD = properties.getProperty("pkoolvn.mysql-password", DB_PASSWORD);
            MIN_CONN = Integer.parseInt(properties.getProperty("pkoolvn.min", String.valueOf(MIN_CONN)));
            MAX_CONN = Integer.parseInt(properties.getProperty("pkoolvn.max", String.valueOf(MAX_CONN)));
            
            // Load server settings
            HOST = properties.getProperty("pkoolvn.host", HOST).trim();
            PORT_SERVER = Integer.parseInt(properties.getProperty("pkoolvn.port-server", String.valueOf(PORT_SERVER)));
            PORT_CHECK_ONLINE = Integer.parseInt(properties.getProperty("pkoolvn.port-check-online", String.valueOf(PORT_CHECK_ONLINE)));
            // URL_WEB = properties.getProperty("pkoolvn.url-web", URL_WEB);
            URL_WEB = "http://" + HOST + ":8080";
            version = Integer.parseInt(properties.getProperty("pkoolvn.version", String.valueOf(version)));
            
            // Load security settings
            MAX_CONNECTIONS_PER_IP = Integer.parseInt(properties.getProperty("pkoolvn.MAX_CONNECTIONS_PER_IP", String.valueOf(MAX_CONNECTIONS_PER_IP)));
            RATE_LIMIT_RESET_TIME = Long.parseLong(properties.getProperty("pkoolvn.RATE_LIMIT_RESET_TIME", String.valueOf(RATE_LIMIT_RESET_TIME)));
            isFw = Boolean.parseBoolean(properties.getProperty("pkoolvn.FW", String.valueOf(isFw)));
            isDebug = Boolean.parseBoolean(properties.getProperty("pkoolvn.debug", String.valueOf(isDebug)));
            addshop = Boolean.parseBoolean(properties.getProperty("pkoolvn.tool-addshop", String.valueOf(addshop)));
            addshop_hokage = Boolean.parseBoolean(properties.getProperty("pkoolvn.tool-addshop-hokage", String.valueOf(addshop_hokage)));
            
            // Load EXP multiplier
            EXP_MULTIPLIER = Integer.parseInt(properties.getProperty("pkoolvn.exp-multiplier", String.valueOf(EXP_MULTIPLIER)));
            
        } catch (IOException e) {
            System.err.println("Không thể đọc file cấu hình: " + e.getMessage());
        }
    }
    
    public static String getIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return HOST;
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            String.format(URL, DB_HOST, DB_PORT, DB_NAME),
            DB_USER,
            DB_PASSWORD
        );
    }
} 