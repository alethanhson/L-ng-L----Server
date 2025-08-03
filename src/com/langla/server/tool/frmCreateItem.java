/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.langla.server.tool;

import com.PKoolVNDB;
import com.langla.data.DataCenter;
import com.langla.data.ItemOption;
import com.langla.real.item.Item;
import com.langla.real.item.Item.LangLa_gp;
import com.langla.real.item.ItemShop;
import com.langla.server.main.PKoolVN;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * @author PKoolVN
 **/


public class frmCreateItem extends javax.swing.JFrame {

    /**
     * Creates new form frmCreateItem
     */
    public Item item = null;

    public frmCreateItem() {
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
        jComboBox3.removeAllItems();
        for (int i = 0; i < DataCenter.gI().DataNameClass.length; i++) {
            jComboBox3.addItem("" + DataCenter.gI().DataNameClass[i].name);
        }
        jComboBox4.removeAllItems(); // Xóa tất cả mục hiện tại
        for (Map.Entry<Integer, String> entry : DataCenter.gI().shopNames.entrySet()) {
            jComboBox4.addItem("(SHOP: " + entry.getKey() + ") "+ entry.getValue());
            shopMap.put(jComboBox4.getItemCount() - 1, entry.getKey());
        }
        jComboBox1.setEditable(true);
        jComboBox2.setEditable(true);
        jComboBox4.setEditable(true);
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
        JTextField tf3 = (JTextField) jComboBox4.getEditor().getEditorComponent();
        tf3.addKeyListener(
                new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                            boolean b = false;
                            for (int i = 0; i < jComboBox4.getItemCount(); i++) {
                                if (jComboBox4.getItemAt(i).toLowerCase().contains(tf3.getText().toLowerCase())) {
                                    jComboBox4.setSelectedIndex(i);
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<String>();
        jComboBox2 = new javax.swing.JComboBox<String>();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jComboBox3 = new javax.swing.JComboBox();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jComboBox4 = new javax.swing.JComboBox<String>();
        jTextField10 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox();

        jMenuItem1.setText("jMenuItem1");

        jMenuItem2.setText("jMenuItem2");

        jMenuItem1.setText("jMenuItem1");

        jMenuItem2.setText("jMenuItem2");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel1.setName(""); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                .addGap(39, 39, 39))
        );

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
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
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1011, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jButton1.setText("Thêm Options");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Value 1: ");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Value 2: ");

        jLabel5.setText("Value 3:");

        jLabel6.setText("StrOptions: ");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton2.setText("Xóa Options");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel7.setText("isLock:");

        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jLabel8.setText("HSD (/1 ngày):");

        jLabel9.setText("Hệ:");

        jLabel10.setText("Giá tinh thạch:");

        jLabel11.setText("Giá vàng:");

        jLabel12.setText("Giá vàng khóa:");

        jLabel13.setText("Giá bạc:");

        jLabel14.setText("Giá bạc khóa:");

        jLabel15.setText("moneyNew:");

        jTextField4.setText("-1");
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item box 3", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });
        jTextField5.setText("0");
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });



        jTextField6.setText("0");
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jTextField7.setText("0");
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jTextField8.setText("0");
        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        jTextField9.setText("0");
        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        jTextField10.setText("0");
        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });
        jButton3.setText("Thêm Item");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jLabel16.setText("Lớp:");

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Auto","Lớp kiếm", "Lớp ám khí", "Lớp gậy", "Lớp đao", "Lớp dao"}));
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });

        jLabel17.setText("Sex:");

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tất cả", "Nam", "Nữ"}));
        jComboBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox6ActionPerformed(evt);
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
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.TRAILING, 0, 865, Short.MAX_VALUE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                .addComponent(jLabel6))))
                                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                                .addComponent(jButton3))
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                                                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                .addComponent(jTextField4))
                                                                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                                                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                        .addComponent(jCheckBox1))
                                                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                .addComponent(jComboBox5, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                .addComponent(jComboBox6, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                        .addComponent(jLabel10)
                                                                                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addComponent(jLabel15))
                                                                                                .addGap(16, 16, 16)
                                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                                                        .addComponent(jTextField5)
                                                                                                        .addComponent(jTextField6)
                                                                                                        .addComponent(jTextField7)
                                                                                                        .addComponent(jTextField8)
                                                                                                        .addComponent(jTextField9)
                                                                                                        .addComponent(jTextField10, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))))
                                                                                .addGap(0, 0, Short.MAX_VALUE)))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                                                                                        .addComponent(jButton1))
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                        .addComponent(jCheckBox1))
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                        .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {

        int index = this.jComboBox1.getSelectedIndex();
        if (index >= 0 && index < DataCenter.gI().ItemTemplate.length) {
            try {
                this.jLabel1.setIcon(new ImageIcon(ImageIO.read(new File("data\\pkoolvn\\IconClient\\" + DataCenter.gI().ItemTemplate[index].idIcon + ".png.png"))));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            item = new Item(index);
            if (item.isItemTrangBi()) {
                item.he = (byte) idhe;
                int level = item.getItemTemplate().levelNeed / 10 * 10;
                item.strOptions = null;
                if (item.isVuKhi()) {
                    if(idhe <= 0) {
                        infoBox("Vui lòng chọn hệ");
                        return;
                    }
                    if(PKoolVNDB.addshop_hokage){
                        Item.setOptionsVuKhi_hokage(item, level);
                    } else {
                        Item.setOptionsVuKhi(item, level);
                    }

                } else {
                    if(idhe <= 0) {
                        infoBox("Vui lòng chọn hệ");
                        return;
                    }
                    if(PKoolVNDB.addshop_hokage){
                        Item.setOptionsTrangBiPhuKien_hokage(item, level);
                    } else {
                        Item.setOptionsTrangBiPhuKien(item, level);
                    }

                }
            }
//            if (item.getItemTemplate().type == 12) {
//                item.he = (byte) idhe;
//                int level = item.getItemTemplate().levelNeed / 10 * 10;
//                item.strOptions = null;
//                Item.setOptionsAoChoang(item, level);
//            }
            setItem();

        }

    }
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        expiry = Integer.parseInt(this.jTextField4.getText());
        gia_ban_tinh_thach = Integer.parseInt(this.jTextField5.getText());
        idhe = this.jComboBox3.getSelectedIndex();
        gia_ban_vang = Integer.parseInt(this.jTextField6.getText());
        gia_ban_vang_khoa = Integer.parseInt(this.jTextField7.getText());
        gia_ban_bac = Integer.parseInt(this.jTextField8.getText());
        gia_ban_bac_khoa = Integer.parseInt(this.jTextField9.getText());
        moneyNew = Integer.parseInt(this.jTextField10.getText());
        setItem();
        ItemShop newItem = new ItemShop();
        newItem.id_item = item.id;
        newItem.isLock = isLock;
        switch (idshop) {
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 32:
            case 35:
            case 36:
                newItem.isLock = true;
        }
        if (expiry>0) newItem.expiry = 86400000L*expiry;
        newItem.idhe = idhe;
        if(idclass == 0) {
            newItem.idclass = item.getItemTemplate().idClass;
        } else {
            newItem.idclass = idclass;
        }
        newItem.sex = sex;
        newItem.strOptions = item.strOptions;
        newItem.gia_ban_tinh_thach = gia_ban_tinh_thach;
        newItem.gia_ban_vang = gia_ban_vang;
        newItem.gia_ban_vang_khoa = gia_ban_vang_khoa;
        newItem.gia_ban_bac_khoa = gia_ban_bac_khoa;
        newItem.gia_ban_bac = gia_ban_bac;
        newItem.moneyNew = moneyNew;
        DataCenter.gI().shopTemplates.computeIfAbsent(idshop, k -> new ArrayList<>()).add(newItem);
        DataCenter.gI().updateShopToData(idshop);
        infoBox("Thêm thành công item: "+item.getItemTemplate().name);
    }

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {
        idclass = this.jComboBox5.getSelectedIndex();
        setItem();
    }
    private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {
        int index = this.jComboBox6.getSelectedIndex();
        if(index == 1){
            sex = 1;
        } else if(index == 2){
            sex = 0;
        }else {
            sex = -1;
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

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        isLock = this.jCheckBox1.isSelected();
        setItem();
    }

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {
        expiry = Integer.parseInt(this.jTextField4.getText());
        setItem();
    }

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {
        gia_ban_tinh_thach = Integer.parseInt(this.jTextField5.getText());
        setItem();
    }

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {
        int index = this.jComboBox4.getSelectedIndex();
        if (shopMap.containsKey(index)) {
            idshop = shopMap.get(index);
        }

        setItem();
    }

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {
        idhe = this.jComboBox3.getSelectedIndex();
        if (item.isItemTrangBi()) {
            item.he = (byte) idhe;
            if(item.he == 0) item.he = 1;
            int level = item.getItemTemplate().levelNeed / 10 * 10;
            item.strOptions = null;
            if (item.isVuKhi()) {
                if(idhe <= 0) {
                    infoBox("Vui lòng chọn hệ");
                    return;
                }
                if(PKoolVNDB.addshop_hokage){
                    Item.setOptionsVuKhi_hokage(item, level);
                } else {
                    Item.setOptionsVuKhi(item, level);
                }

            } else {
                if(idhe <= 0) {
                    infoBox("Vui lòng chọn hệ");
                    return;
                }
                if(PKoolVNDB.addshop_hokage){
                    Item.setOptionsTrangBiPhuKien_hokage(item, level);
                } else {
                    Item.setOptionsTrangBiPhuKien(item, level);
                }

            }
        }
        setItem();
    }
    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {
        gia_ban_vang = Integer.parseInt(this.jTextField6.getText());
        setItem();
    }

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {
        gia_ban_vang_khoa = Integer.parseInt(this.jTextField7.getText());
        setItem();
    }

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {
        gia_ban_bac = Integer.parseInt(this.jTextField8.getText());
        setItem();
    }

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {
        gia_ban_bac_khoa = Integer.parseInt(this.jTextField9.getText());
        setItem();
    }

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {
        moneyNew = Integer.parseInt(this.jTextField10.getText());
        setItem();
    }
    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//
//        //DataCenter.gI().readArrDataGame(true);
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new frmCreateItem().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration
    private Map<Integer, Integer> shopMap = new HashMap<>();
    private int idshop = 0;
    private boolean isLock = false;
    private int expiry = -1;

    private int idhe = 0;

    private int gia_ban_tinh_thach = 0;

    private int gia_ban_vang = 0;

    private int gia_ban_vang_khoa = 0;

    private int gia_ban_bac_khoa = 0;

    private int gia_ban_bac = 0;

    private int moneyNew = 0;

    private int idclass = 0;
    private int sex = -1;
    private void setItem() {
        Vector vec = Item.getTextVec(item);
        String s = "";
        for (int i = 0; i < vec.size(); i++) {
            s += ((LangLa_gp) vec.get(i)).a;
            s += "<br>";
        }
        //   System.out.println(item.strOptions);
        this.jLabel2.setText("<html><br>" + s + "</html>");
        this.jTextArea1.setText(item.strOptions);
    }
}
