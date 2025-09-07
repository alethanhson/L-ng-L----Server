/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.langla.server.tool;

import com.langla.data.DataCenter;
import com.langla.data.center.DataShop;
import com.langla.data.ItemOption;
import com.langla.real.item.Item;
import com.langla.real.item.Item.LangLa_gp;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * @author PKoolVN
 **/

public class Test extends javax.swing.JFrame {

    public Item item = null;
    public Test() {
        try {
            initComponents();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        jComboBox1.removeAllItems();
        for (int i = 0; i < DataCenter.gI().ItemTemplate.length; i++) {
            jComboBox1.addItem("(ITEM: " + i + ") " + DataCenter.gI().ItemTemplate[i].name);
        }
        jComboBox2.removeAllItems();
        for (int i = 0; i < DataCenter.gI().ItemOptionTemplate.length; i++) {
            jComboBox2.addItem("(OPTION: " + i + ") " + DataCenter.gI().ItemOptionTemplate[i].name);
        }

        TypeShop.removeAllItems(); // Xóa tất cả mục hiện tại
        for (Map.Entry<Integer, String> entry : DataShop.getInstance().shopNames.entrySet()) {
            TypeShop.addItem("(SHOP: " + entry.getKey() + ") "+ entry.getValue());
            shopMap.put(TypeShop.getItemCount() - 1, entry.getKey());
        }
        jComboBox1.setEditable(true);
        jComboBox2.setEditable(true);
        TypeShop.setEditable(true);
        JTextField tf1 = (JTextField) jComboBox1.getEditor().getEditorComponent();

        tf1.addKeyListener(
                new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                            boolean b = false;
                            for (int i = 0; i < jComboBox1.getItemCount(); i++) {
                                if (jComboBox1.getItemAt(i).toLowerCase().contains(tf1.getText().toLowerCase())) {
                                    jComboBox1.setSelectedIndex(i);
                                    b = true;
                                    break;
                                }
                            }
                            if (!b) {
                                infoBox("Không tìm thấy Vật Phẩm này.");
                            }
                        }
                    }
                });

        JTextField tf2 = (JTextField) jComboBox2.getEditor().getEditorComponent();
        tf2.addKeyListener(
                new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                            boolean b = false;
                            for (int i = 0; i < jComboBox2.getItemCount(); i++) {
                                if (jComboBox2.getItemAt(i).toLowerCase().contains(tf2.getText().toLowerCase())) {
                                    jComboBox2.setSelectedIndex(i);
                                    b = true;
                                    break;
                                }
                            }
                            if (!b) {
                                infoBox("Không tìm thấy Option này.");
                            }
                        }
                    }
                });

        JTextField tf3 = (JTextField) TypeShop.getEditor().getEditorComponent();
        tf3.addKeyListener(
                new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                            boolean b = false;
                            for (int i = 0; i < TypeShop.getItemCount(); i++) {
                                if (TypeShop.getItemAt(i).toLowerCase().contains(tf3.getText().toLowerCase())) {
                                    TypeShop.setSelectedIndex(i);
                                    b = true;
                                    break;
                                }
                            }
                            if (!b) {
                                infoBox("Không tìm thấy Shop này.");
                            }
                        }
                    }
                });

    }

    public static void infoBox(String infoMessage) {
        JOptionPane.showMessageDialog(null, infoMessage, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }



    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        TypeShop = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();



        jisLock = new javax.swing.JCheckBox();
        jexpiry = new javax.swing.JTextField();


        jhe = new javax.swing.JComboBox<>(new String[] {"Phong", "Thái"});
        jgia_ban_tinh_thach = new javax.swing.JTextField();

        jgia_ban_vang = new javax.swing.JTextField();

        jgia_ban_vang_khoa = new javax.swing.JTextField();

        jgia_ban_bac_khoa = new javax.swing.JTextField();

        gia_ban_bac = new javax.swing.JTextField();

        moneyNew = new javax.swing.JTextField();


        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel1.setName("");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
        );
        TypeShop.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        TypeShop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setTypeShopActionPerformed(evt);
            }
        });
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setText("jLabel2");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
        );

        jButton1.setText("Thêm Options");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Value 1: ");

        jLabel4.setText("Value 2: ");

        jLabel5.setText("Value 3:");

        jLabel6.setText("StrOptions: ");

        jLabel7.setText("isLock: ");
        jLabel8.setText("HSD: ");
        jLabel9.setText("Hệ: ");
        jLabel10.setText("Giá tinh thạch: ");
        jLabel11.setText("Giá vàng: ");
        jLabel12.setText("Giá vàng khóa: ");
        jLabel13.setText("Giá bạc: ");
        jLabel14.setText("Giá bạc khóa: ");
        jLabel15.setText("moneyNew: ");


        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton2.setText("Xóa Options");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(TypeShop, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.TRAILING, 0, 632, Short.MAX_VALUE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(jLabel3)
                                                                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                                                                                        .addComponent(jTextField2))

                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                .addGap(126, 126, 126)
                                                                                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                .addGap(8, 8, 8)
                                                                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                .addGap(126, 126, 126)
                                                                                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))

                                                                                )))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jScrollPane1)))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(TypeShop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabel3)
                                                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jLabel6))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabel4)
                                                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabel5)
                                                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))

                                                        )
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)





                                .addContainerGap())



        );

        pack();
    }

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {

        int index = this.jComboBox1.getSelectedIndex();
        if (index >= 0 && index < DataCenter.gI().ItemTemplate.length) {
            try {
                this.jLabel1.setIcon(new ImageIcon(ImageIO.read(new File("data\\pkoolvn\\IconClient\\" + DataCenter.gI().ItemTemplate[index].idIcon + ".png.png"))));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Item itemOld = item;
            item = new Item(index);
            if (itemOld != null) {
                item.strOptions = itemOld.strOptions;
            }
            setItem();

        }

    }

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    private void setTypeShopActionPerformed(java.awt.event.ActionEvent evt) {
        int index = this.TypeShop.getSelectedIndex();
        if (shopMap.containsKey(index)) {
            idshop = shopMap.get(index);
        }

        setItem();
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String str1 = this.jTextField1.getText();
        String str2 = this.jTextField2.getText();
        String str3 = this.jTextField3.getText();
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        int num1 = -1;
        int num2 = -1;
        int num3 = -1;
        try {
            num1 = Integer.parseInt(str1);
        } catch (Exception êx) {

        }
        try {
            num2 = Integer.parseInt(str2);
        } catch (Exception êx) {

        }
        try {
            num3 = Integer.parseInt(str3);
        } catch (Exception êx) {

        }

        ItemOption itemoption = null;
        if (num1 > -1 && num2 > -1 && num3 > -1) {
            itemoption = new ItemOption(new int[]{jComboBox2.getSelectedIndex(), num1, num2, num3});
        } else if (num1 > -1 && num2 > -1) {
            itemoption = new ItemOption(new int[]{jComboBox2.getSelectedIndex(), num1, num2});
        } else if (num1 > -1) {
            itemoption = new ItemOption(new int[]{jComboBox2.getSelectedIndex(), num1});
        }

        if (itemoption == null) {
            return;
        }
        item.removeItemOption(itemoption);
        item.addItemOption(itemoption);
        setItem();

    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        item.strOptions = null;
        setItem();
    }
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> TypeShop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private Map<Integer, Integer> shopMap = new HashMap<>();


    private javax.swing.JCheckBox jisLock;
    private javax.swing.JTextField jexpiry;
    private javax.swing.JComboBox<String> jhe;

    private javax.swing.JTextField jgia_ban_tinh_thach;

    private javax.swing.JTextField jgia_ban_vang;

    private javax.swing.JTextField jgia_ban_vang_khoa;

    private javax.swing.JTextField jgia_ban_bac_khoa;

    private javax.swing.JTextField gia_ban_bac;

    private javax.swing.JTextField moneyNew;
    private int idshop = 0;
    private void setItem() {
        Vector vec = Item.getTextVec(item);
        String s = "";
        for (int i = 0; i < vec.size(); i++) {
            s += ((LangLa_gp) vec.get(i)).a;
            s += "<br>";
        }
        this.jLabel2.setText("<html>ADD SHOP: "+idshop+"<br> " + s + "</html>");
        this.jTextArea1.setText(item.strOptions);
    }
}
