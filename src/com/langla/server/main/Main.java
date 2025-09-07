package com.langla.server.main;

import com.PKoolVNDB;
import com.langla.data.DataCenter;
import com.langla.lib.Binary;
import com.langla.lib.Utlis;
import com.langla.real.bangxephang.BangXepHang;
import com.langla.real.baucua.BauCua;
import com.langla.real.cho.Cho;
import com.langla.real.map.Map;

import com.langla.real.player.Client;
import com.langla.real.player.Player;
import com.langla.real.player.PlayerManager;
import com.langla.server.handler.ServerSocketHandler;
import com.langla.server.tool.CreateCode;
import com.langla.server.tool.frmCreateItem;
import com.langla.utlis.UTPKoolVN;
import com.langla.real.event.BossEvent;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

import javax.swing.*;

public class Main {

    public static MyServerSocket serverMain;
    public static MyServerSocket serverCheckOnline;
    public static Vector vecClient = new Vector();
    public static int NUM_CLIENTS = 0;
    public static boolean logData = false;
    private static final java.util.Map<String, Integer> ipConnectionCounts = new HashMap<>();
    private static final java.util.Map<String, Long> ipLastResetTime = new HashMap<>();

    public static void main(String[] args) {
        printBanner();
        System.out.println("SERVER IP => " + PKoolVNDB.getIp());

        try {
            // Bước 1: Khởi tạo DataCenter
            DataCenter dataCenter = DataCenter.gI();
            System.out.println("!-PKoolVN =>>>| DataCenter initialized successfully!");

            // Bước 2: Load dữ liệu game
            dataCenter.readArrDataGame(true);
            System.out.println("!-PKoolVN =>>>| Game data loaded successfully!");

            // Bước 3: Kiểm tra ItemTemplate đã được load
            if (dataCenter.ItemTemplate == null || dataCenter.ItemTemplate.length == 0) {
                throw new RuntimeException("ItemTemplate không được load thành công!");
            }
            System.out.println("!-PKoolVN =>>>| ItemTemplate loaded: " + dataCenter.ItemTemplate.length + " items");

            dataCenter.Load_Data();
            System.out.println("!-PKoolVN =>>>| All data loaded successfully!");

            // Bước 4: Tạo Map sau khi có đủ dữ liệu
            Map.createMap();
        } catch (Exception e) {
            System.err.println("!-PKoolVN =>>>| ERROR during initialization: " + e.getMessage());
            e.printStackTrace();
            return; // Dừng server nếu có lỗi
        }

        // Tiếp tục khởi động các module khác
        openServerSocket();
        activeCommandLine();
        returnCho();
        BangXepHang();
        // BauCua.gI().Start();
        BossEvent.gI().startEvent();

        if (!PKoolVNDB.isDebug) {
            SwingUtilities.invokeLater(Main::createAndShowGUI);
            AutoBaoTri();
        }
        if (PKoolVNDB.addshop) {
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new frmCreateItem().setVisible(true);
                }
            });
        }
    }

    private static void printBanner() {
        System.out.println(".______    __  ___   ______     ______    __      ____    ____ .__   __. ");
        System.out.println("|   _  \\  |  |/  /  /  __  \\   /  __  \\  |  |     \\   \\  /   / |  \\ |  | ");
        System.out.println("|  |_)  | |  '  /  |  |  |  | |  |  |  | |  |      \\   \\/   /  |   \\|  | ");
        System.out.println("|   ___/  |    <   |  |  |  | |  |  |  | |  |       \\      /   |  . `  | ");
        System.out.println("|  |      |  .  \\  |  `--'  | |  `--'  | |  `----.   \\    /    |  |\\   | ");
        System.out.println("| _|      |__|\\__\\  \\______/   \\______/  |_______|    \\__/     |__| \\__|"); // Reset
                                                                                                              // the
                                                                                                              // color
                                                                                                              // at the
                                                                                                              // end of
                                                                                                              // the
                                                                                                              // banner
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("PKoolVN Server Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 350);
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel maintenanceLabel = new JLabel("Đặt thời gian bảo trì /phút:");
        maintenanceLabel.setBounds(80, 20, 300, 25);
        panel.add(maintenanceLabel);

        JComboBox<Integer> maintenanceTime = new JComboBox<>();
        maintenanceTime.addItem(1);
        maintenanceTime.addItem(3);
        maintenanceTime.addItem(5);
        maintenanceTime.addItem(10);
        maintenanceTime.addItem(15);
        maintenanceTime.addItem(20);
        maintenanceTime.addItem(25);
        maintenanceTime.addItem(30);
        maintenanceTime.setBounds(80, 50, 200, 25);
        panel.add(maintenanceTime);

        JButton maintenanceButton = new JButton("Đặt Bảo Trì");
        maintenanceButton.setBounds(80, 80, 200, 25);
        panel.add(maintenanceButton);

        JButton saveDataButton = new JButton("Stop Server");
        saveDataButton.setBounds(80, 170, 200, 25);
        panel.add(saveDataButton);

        JButton clearPlayerButton = new JButton("Clear Player");
        clearPlayerButton.setBounds(80, 200, 200, 25);
        panel.add(clearPlayerButton);

        JButton fw = new JButton("Bật/Tắt Chống DDoS");
        fw.setBounds(80, 230, 200, 25);
        panel.add(fw);

        // Các hành động khi nhấn nút
        maintenanceButton.addActionListener(e -> {
            int timeSelectedItem = (int) maintenanceTime.getSelectedItem();
            Maintenance.gI().start(60 * timeSelectedItem);
        });

        saveDataButton.addActionListener(e -> {
            Stop();
        });

        clearPlayerButton.addActionListener(e -> {
            try {
                PlayerManager.getInstance().ClearPlayer();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        fw.addActionListener(e -> {
            if (PKoolVNDB.isFw) {
                PKoolVNDB.isFw = false;
                UTPKoolVN.Print("Đã tắt FW");
            } else {
                PKoolVNDB.isFw = true;
                UTPKoolVN.Print("Đã bật FW (Chống DDoS)");
            }
        });
    }

    private static void AutoBaoTri() {
        new Thread(() -> {

            while (true) {
                if (UTPKoolVN.getHour() == 4 && UTPKoolVN.getMinute() == 0 && !Maintenance.isRunning) {
                    try {
                        String flagFilePath = "restart.flag";
                        try {
                            File restartFlagFile = new File(flagFilePath);
                            // Tạo tệp restart.flag
                            boolean created = restartFlagFile.createNewFile();
                            if (created) {
                                Maintenance.gI().start(60 * 5);
                            } else {
                                UTPKoolVN.Print("Reboot Server Failed!!");
                            }
                        } catch (IOException ex) {
                            Utlis.logError(PlayerManager.class, ex, "Da say ra loi:\n" + ex.getMessage());
                        }
                    } catch (Exception ex) {
                        Utlis.logError(PlayerManager.class, ex, "Da say ra loi:\n" + ex.getMessage());
                    }
                }
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "Bảo trì tự động").start();
    }

    public static void activeCommandLine() {
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String line = sc.nextLine();
                if (line.equals("c")) {
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            new CreateCode().setVisible(true);
                        }
                    });
                }
            }
        }, "Active line").start();
    }

    public static void returnCho() {
        new Thread(() -> {
            while (!Maintenance.isRunning) {
                Cho.AutoUpdate();
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "Return Chợ").start();
    }

    public static void BangXepHang() {
        new Thread(() -> {
            while (!Maintenance.isRunning) {
                UTPKoolVN.Print("Update Bang Xep Hang");
                try {
                    BangXepHang.gI().update();
                    Thread.sleep(60000);
                } catch (Exception ex) {
                    Utlis.logError(Main.class, ex, "Da say ra loi:\n" + ex.getMessage());
                }
            }
        }, "Bang Xep Hang").start();
    }

    public static String getIp(Socket soc) {
        return split(soc.getRemoteSocketAddress().toString(), ":", 0)[0].replaceAll("/", "");
    }

    public static String getPort(Socket soc) {
        return split(soc.getRemoteSocketAddress().toString(), ":", 0)[1];
    }

    public static String[] split(String var0, String var1, int var2) {
        int var3;
        String[] var4;
        if ((var3 = var0.indexOf(var1)) >= 0) {
            var4 = split(var0.substring(var3 + var1.length()), var1, var2 + 1);
        } else {
            var4 = new String[var2 + 1];
            var3 = var0.length();
        }

        var4[var2] = var0.substring(0, var3);
        return var4;
    }

    private static void checkAndResetRateLimit(String ip) {
        Long lastResetTime = ipLastResetTime.getOrDefault(ip, 0L);
        if (System.currentTimeMillis() - lastResetTime > PKoolVNDB.RATE_LIMIT_RESET_TIME) {
            ipConnectionCounts.put(ip, 0);
            ipLastResetTime.put(ip, System.currentTimeMillis());
        }
    }

    private static boolean isRateLimitExceeded(String ip) {
        checkAndResetRateLimit(ip);
        int connections = ipConnectionCounts.getOrDefault(ip, 0);
        if (connections >= PKoolVNDB.MAX_CONNECTIONS_PER_IP) {
            return true;
        }
        ipConnectionCounts.put(ip, connections + 1);
        return false;
    }

    private static void openServerSocket() {
        serverCheckOnline = new MyServerSocket(PKoolVNDB.PORT_CHECK_ONLINE, new ServerSocketHandler() {
            @Override
            public void socketConnect(Socket socket) {
                try {
                    String ip = socket.getInetAddress().getHostAddress();
                    try {
                        if (PKoolVNDB.isFw && isRateLimitExceeded(ip)) {
                            UTPKoolVN.Print("Kết nối từ IP: " + ip + " bị từ chối do vượt quá giới hạn.");
                            socket.close();
                            return;
                        }
                    } catch (Exception e) {
                    }
                    socket.getOutputStream().write(0);
                    socket.getOutputStream().flush();
                    UTPKoolVN.Print("IP: " + ip +
                            " >> Check Online");
                } catch (Exception ex) {
                    Utlis.logError(Player.class, ex, "Da say ra loi:\n" + ex.getMessage());
                } finally {
                    try {
                        socket.close();
                    } catch (Exception ex) {
                    }
                }
            }

        });
        serverMain = new MyServerSocket(PKoolVNDB.PORT_SERVER, new ServerSocketHandler() {
            @Override
            public void socketConnect(Socket socket) {
                long startTime = System.currentTimeMillis();
                String ip = socket.getInetAddress().getHostAddress();
                try {
                    if (PKoolVNDB.isFw && isRateLimitExceeded(ip)) {
                        UTPKoolVN.Print("Kết nối từ IP: " + ip + " bị từ chối do vượt quá giới hạn.");
                        socket.close();
                        return;
                    }
                } catch (Exception e) {
                }
                Client client = new Client(socket);
                client.id = NUM_CLIENTS++;
                if (client.isConnected()) {
                    Main.addClient(client);
                    client.session.start();
                    PlayerManager.getInstance().put(client.session);
                    long endTime = System.currentTimeMillis(); // Thời gian sau khi Accept
                    long duration = endTime - startTime; // Tổng thời gian để Accept
                    UTPKoolVN.Print("CLIENT: " + client.id +
                            " IP: " + ip +
                            " >> Start - " + duration + "ms.");
                } else {
                    client.session.clean();
                }
            }
        });
        serverCheckOnline.open();
        serverMain.open();
    }

    public static synchronized void addClient(Client aThis) {
        if (vecClient.contains(aThis)) {
            return;
        }
        vecClient.add(aThis);
        // try {
        // aThis.create();
        // } catch (Exception ex) {
        // Utlis.logError(Player.class, ex , "Da say ra loi:\n" + ex.getMessage());
        // aThis.session.clean();
        // return;
        // }

    }

    public static synchronized void removeClient(Client aThis) {
        try {
            vecClient.remove(aThis);
        } catch (Exception ex) {
            Utlis.logError(Player.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public static void Stop() {
        serverCheckOnline.stop();
        serverMain.stop();
    }
}
