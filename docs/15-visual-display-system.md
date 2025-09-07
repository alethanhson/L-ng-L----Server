# Báo Cáo Chi Tiết - Hệ Thống Hiển Thị & Giao Diện

## Tổng Quan
Hệ thống hiển thị và giao diện của game bao gồm nhiều thành phần: render map, hiệu ứng kỹ năng, hoạt ảnh nhân vật, giao diện người dùng, và các thành phần đồ họa khác. Hệ thống được thiết kế để tối ưu hiệu suất và trải nghiệm người dùng.

## 1. Hệ Thống Render Map

### Cấu Trúc Map & Zone
```java
public class Map {
    public static Map[] maps;
    public static final int NUM_ZONE = 15;  // Mỗi map có 15 zone
    public ArrayList<Zone> listZone = new ArrayList<Zone>();
    public ArrayList<Zone> listZoneCusTom = new ArrayList<Zone>();
    
    private void createZone(int NUM_ZONE) {
        for (int i = 0; i < NUM_ZONE; i++) {
            Zone zone = new Zone(this, listZone.size());
            zone.createNpc();      // Tạo NPC
            zone.createMob();      // Tạo quái
            zone.createThread();   // Tạo thread riêng cho zone
            listZone.add(zone);
        }
    }
}
```

### Thread Render Zone
```java
public void createThread() {
    Thread thread = new Thread(() -> {
        while (!Maintenance.isRunning) {
            long l = System.currentTimeMillis();
            try {
                // Cập nhật nhân vật
                for (Char c : vecChar) {
                    if (c.client != null) c.update();
                }
                
                // Cập nhật vật phẩm rơi
                for (ItemMap mItemMap : vecItemMap) {
                    if (mItemMap != null) mItemMap.update(Zone.this);
                }
                
                // Cập nhật quái
                for (Mob mob : vecMob) {
                    if (System.currentTimeMillis() - mob.delayUpdate >= 1000L) {
                        mob.update(Zone.this);
                        mob.delayUpdate = System.currentTimeMillis();
                    }
                    
                    // Xử lý respawn
                    if (mob.isReSpawn && mob.isHoiSinhMob) {
                        if (mob.timeRemove == 0 && !mob.isBoss && 
                            System.currentTimeMillis() - mob.timeDie >= 2500L) {
                            mob.reSpawn();
                            reSpawnMobToAllChar(mob);
                        }
                    }
                    
                    // AI tấn công
                    if (mob.CanAttack()) {
                        for (Char c : vecChar) {
                            if (!c.infoChar.isDie && mob.getRe(c) < 50 + mob.getMobTemplate().speedMove) {
                                if (System.currentTimeMillis() - mob.delayAttack >= 5000) {
                                    Zone.this.mobAttackChar(mob, c.client);
                                    mob.delayAttack = System.currentTimeMillis();
                                }
                            }
                        }
                    }
                }
                
                // Đảm bảo 100ms mỗi frame
                long sleep = (100 - (System.currentTimeMillis() - l));
                if (sleep < 1) sleep = 1;
                Utlis.sleep(sleep);
                
            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi UPDATE:\n" + ex.getMessage());
            }
        }
    });
    thread.start();
}
```

## 2. Hệ Thống Hiệu Ứng (Effect)

### Cấu Trúc Effect
```java
public class Effect {
    public int id;           // ID hiệu ứng
    public int value;        // Giá trị hiệu ứng
    public long timeStart;   // Thời điểm bắt đầu
    public long maintain;    // Thời gian duy trì
    public Char charAttack;  // Người gây hiệu ứng
    private long delay;      // Delay giữa các lần trigger
    List<Integer> listChiSo = new ArrayList<>(); // Lưu chỉ số gốc
}
```

### Các Loại Hiệu Ứng Chính

#### Hiệu Ứng Trạng Thái
```java
// Hiệu ứng Suy Yếu (ID 8)
case 8:
    if(isRemove) {
        // Khôi phục chỉ số
        _myChar.TuongKhac.TanCong += effect.listChiSo.get(0);
        _myChar.TuongKhac.KhangHoa += effect.listChiSo.get(1);
        // ... khôi phục các chỉ số khác
    } else {
        // Giảm một nửa chỉ số
        int tanCong = _myChar.TuongKhac.TanCong/2;
        _myChar.TuongKhac.TanCong -= tanCong;
        effect.listChiSo.add(tanCong); // Lưu để khôi phục
    }
    break;

// Hiệu ứng Bóng (ID 11)
case 11:
    if(isRemove) {
        _myChar.info.isBiBong = false;
    } else {
        _myChar.info.isBiBong = true;
    }
    break;

// Hiệu ứng Choáng (ID 12)
case 12:
    if(isRemove) {
        _myChar.info.isBiChoang = false;
    } else {
        _myChar.info.isBiChoang = true;
    }
    break;
```

#### Hiệu Ứng Hồi Phục
```java
// Hồi HP/MP liên tục (Type 0)
case 0:
    if (System.currentTimeMillis() - delay >= 500) {
        if (aThis.infoChar.hp < aThis.infoChar.hpFull) {
            aThis.PlusHp(value);
            aThis.msgUpdateHp();
        }
        if (aThis.infoChar.mp < aThis.infoChar.mpFull) {
            aThis.PlusMp(value);
            aThis.msgUpdateMp();
        }
        delay = System.currentTimeMillis();
    }
    break;

// Hồi HP liên tục (Type 6, 11, 15)
case 6: case 11: case 15:
    if (System.currentTimeMillis() - delay >= 500) {
        if (aThis.infoChar.hp < aThis.infoChar.hpFull) {
            aThis.PlusHp(value);
            aThis.msgUpdateHp();
        }
        delay = System.currentTimeMillis();
    }
    break;
```

#### Hiệu Ứng Độc Hại
```java
// Trúng độc (Type 2)
case 2:
    if (System.currentTimeMillis() - delay >= 350) {
        int hpMine = value * 2;
        hpMine = Utlis.nextInt(hpMine * 90 / 100, hpMine);
        if (charAttack != null && charAttack.client != null) {
            charAttack.setAttackPlayer(aThis, hpMine, false);
        }
        delay = System.currentTimeMillis();
    }
    break;
```

### Cập Nhật Hiệu Ứng
```java
public void updateChar(Char aThis) {
    long l = System.currentTimeMillis();
    
    // Kiểm tra hết hạn
    if (l - timeStart >= maintain || maintain < 0) {
        setEff(aThis, this, true);  // Xóa hiệu ứng
        aThis.listEffect.remove(this);
        aThis.msgRemoveEffect(this);
        return;
    }
    
    // Không cập nhật nếu nhân vật chết
    if (aThis.infoChar.hp <= 0) return;
    
    // Xử lý theo type hiệu ứng
    EffectTemplate eff = getEffectTemplate();
    switch (eff.type) {
        case 0: // Hồi HP/MP
        case 2: // Trúng độc
        case 6: // Hồi HP
        // ... các type khác
    }
}
```

## 3. Hệ Thống Kỹ Năng & Hiệu Ứng

### Xử Lý Kỹ Năng Không Focus
```java
public static void HanderSkillNotFocus(Client client, Skill skill) {
    // Kiểm tra điều kiện sử dụng
    if (skill == null || skill.level == 0 || 
        skill.mpUse > client.mChar.infoChar.mp || 
        skill.levelNeed > client.mChar.level() || 
        System.currentTimeMillis() - skill.time < skill.coolDown) {
        return;
    }
    
    skill.time = System.currentTimeMillis();
    client.mChar.MineMp(skill.mpUse);
    client.mChar.msgUpdateMp();
    
    // Kỹ năng hỗ trợ (Type 1, 6)
    if(skill.getSkillTemplate().type == 1 || skill.getSkillTemplate().type == 6) {
        ItemOption[] array = skill.getItemOption();
        if(array.length > 0) {
            // Triệu hồi đối chi thuật
            if(skill.getSkillTemplate().id == SkillTemplate.TRIEU_HOI_DOI_CHI_THUAT) {
                client.mChar.addEffect(new Effect(62, array[1].a[1], System.currentTimeMillis(), array[0].a[1]));
                client.mChar.addEffect(new Effect(63, array[2].a[1], System.currentTimeMillis(), array[0].a[1]));
            }
            // Thủy lao thuật
            else if(skill.getSkillTemplate().id == SkillTemplate.THUY_LAO_THUAT) {
                client.mChar.addEffect(new Effect(34, array[0].a[1], System.currentTimeMillis(), 5000));
                client.mChar.addEffect(new Effect(61, array[1].a[1], System.currentTimeMillis(), 5000));
            }
            // Byakugan
            else if(skill.getSkillTemplate().id == SkillTemplate.BYAKUGAN_19) {
                client.mChar.addEffect(new Effect(31, array[0].a[1], System.currentTimeMillis(), array[0].a[1]));
                client.mChar.addEffect(new Effect(73, array[1].a[1], System.currentTimeMillis(), array[0].a[1]));
            }
            // ... các kỹ năng khác
        }
    }
}
```

### Hiệu Ứng Kỹ Năng Tấn Công
```java
public static void attackEffMob(Client client, Mob mob, Skill skill) {
    // Ấn chú chi thuật
    if(skill.getSkillTemplate().id == SkillTemplate.AN_CHU_CHI_THUAT) {
        ItemOption[] array = skill.getItemOption();
        if(array.length > 0) {
            Effect eff1 = new Effect(55, array[0].a[1], System.currentTimeMillis(), 10000);
            Effect eff2 = new Effect(56, array[1].a[1], System.currentTimeMillis(), 10000);
            mob.addEff(eff1);
            mob.addEff(eff2);
            client.mChar.zone.addEffMob(eff1, (short) mob.idEntity);
            client.mChar.zone.addEffMob(eff2, (short) mob.idEntity);
        }
    }
    // Dịch chuyển chi thuật
    else if(skill.getSkillTemplate().id == SkillTemplate.DICH_CHUYEN_CHI_THUAT) {
        ItemOption[] array = skill.getItemOption();
        if(array.length > 0) {
            int tang = client.mChar.getChiSoFormSkill(277);
            int sec = array[2].a[1] + tang;
            Effect eff1 = new Effect(36, 1, System.currentTimeMillis(), sec);
            mob.addEff(eff1);
            client.mChar.zone.addEffMob(eff1, (short) mob.idEntity);
            
            // Dịch chuyển đến vị trí mob
            client.mChar.setXY(mob.cx, mob.cy);
            client.session.serivce.setXYAllZone(client);
        }
    }
}
```

## 4. Hệ Thống Giao Diện Người Dùng

### Scroll Bar Tùy Chỉnh
```java
public class ScrollBarCustomUI extends BasicScrollBarUI {
    public boolean isMin, isMax;
    
    @Override
    protected void paintThumb(Graphics grphcs, JComponent jc, Rectangle rctngl) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Màu sắc theo trạng thái
        if (isDragging) {
            g2.setColor(new Color(130, 130, 130));
        } else if (isThumbRollover()) {
            g2.setColor(new Color(150, 150, 150));
        } else {
            g2.setColor(new Color(180, 180, 180));
        }
        
        // Vẽ thumb bo tròn
        int round = 2;
        int spaceX = 2, spaceY = 8;
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            g2.fill(new RoundRectangle2D.Double(
                rctngl.getX() + spaceX, rctngl.getY() + spaceY,
                rctngl.getWidth() - spaceX * 2, rctngl.getHeight() - spaceY * 2,
                round, round));
        }
        g2.dispose();
    }
}
```

### Bảng Tùy Chỉnh
```java
public class TableCustom {
    public static void apply(JFrame frmMain, JScrollPane scroll, TableType type, MouseListener mouseEvent) {
        JTable table = (JTable) scroll.getViewport().getComponent(0);
        
        // Tạo hover effect
        HoverIndex hoverRow = new HoverIndex(frmMain);
        
        // Renderer tùy chỉnh
        TableCellRenderer cellRender;
        if (type == TableType.DEFAULT) {
            cellRender = new TableCustomCellRender(hoverRow);
        } else {
            cellRender = new TextAreaCellRenderer(hoverRow);
        }
        table.setDefaultRenderer(Object.class, cellRender);
        
        // Thiết lập giao diện
        table.setShowVerticalLines(true);
        table.setGridColor(new Color(220, 220, 220));
        table.setForeground(new Color(51, 51, 51));
        table.setSelectionForeground(new Color(51, 51, 51));
        scroll.setBorder(new LineBorder(new Color(220, 220, 220)));
    }
}
```

### Cell Renderer Tùy Chỉnh
```java
public class TableCustomCellRender extends DefaultTableCellRenderer {
    public final HoverIndex hoverRow;
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        Component com = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Màu sắc theo trạng thái
        if (row == hoverRow.getIndex()) {
            com.setBackground(table.getSelectionBackground());
        } else {
            if (row % 2 == 0) {
                com.setBackground(Color.WHITE);
            } else {
                com.setBackground(new Color(242, 242, 242));
            }
        }
        return com;
    }
}
```

## 5. Hệ Thống Đồ Họa & Hình Ảnh

### Tạo Captcha
```java
public static byte[] createImage(String captcha) {
    int width = 160, height = 60;
    
    // Tạo BufferedImage
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();
    
    // Nền trắng
    g2d.setColor(Color.WHITE);
    g2d.fillRect(0, 0, width, height);
    
    // Thêm noise
    for (int i = 0; i < 150; i++) {
        int x = (int) (Math.random() * width);
        int y = (int) (Math.random() * height);
        int gray = (int) (Math.random() * 128) + 128;
        g2d.setColor(new Color(gray, gray, gray));
        g2d.fillRect(x, y, 1, 1);
    }
    
    // Vẽ text
    Font font = new Font("Arial", Font.BOLD, 30);
    g2d.setFont(font);
    
    char[] chars = captcha.toCharArray();
    for (int i = 0; i < chars.length; i++) {
        // Màu ngẫu nhiên cho từng ký tự
        Color textColor = new Color(
            (int) (Math.random() * 256),
            (int) (Math.random() * 256),
            (int) (Math.random() * 256)
        );
        g2d.setColor(textColor);
        
        // Vị trí ngẫu nhiên
        int x = 20 + (i * 30) + (int) (Math.random() * 10);
        int y = 30 + (int) (Math.random() * 20);
        g2d.drawString(chars[i] + "", x, y);
    }
    
    g2d.dispose();
    
    // Chuyển thành byte array
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
        ImageIO.write(image, "PNG", baos);
        return baos.toByteArray();
    } catch (IOException e) {
        return new byte[0];
    }
}
```

### Scroll Button Animation
```java
public class TableScrollButton extends JComponent {
    private float animate = 0f;
    private boolean show = false;
    private boolean mousePressed = false;
    private boolean mouseHovered = false;
    private Shape shape;
    private Image image;
    
    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
        JScrollPane scroll = (JScrollPane) ((JLayer) c).getView();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Màu theo trạng thái
        if (mousePressed) {
            g2.setColor(new Color(10, 92, 137));
        } else if (mouseHovered) {
            g2.setColor(new Color(14, 122, 181));
        } else {
            g2.setColor(new Color(18, 149, 220));
        }
        
        // Vẽ button với animation
        int gapx = scroll.getVerticalScrollBar().isShowing() ? scroll.getVerticalScrollBar().getWidth() : 0;
        int gapy = scroll.getHorizontalScrollBar().isShowing() ? scroll.getHorizontalScrollBar().getHeight() : 0;
        int y_over = 50 + gapy;
        int x = c.getWidth() - 50 - gapx;
        int y = (int) ((c.getHeight() - 50 - gapy) + (y_over * (1f - animate)));
        
        shape = new Ellipse2D.Double(x, y, 40, 40);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, animate * 0.7f));
        g2.fill(shape);
        g2.drawImage(image, x + 10, y + 10, null);
        g2.dispose();
        
        // Kiểm tra hiển thị
        if (scroll.getViewport().getViewRect().y > 0) {
            if (!show) start(true);
        } else if (show) {
            start(false);
        }
    }
}
```

## 6. Tối Ưu Hóa Hiệu Suất

### Multi-threading Zone
- Mỗi zone có thread riêng để xử lý cập nhật
- Frame rate cố định 100ms để đảm bảo mượt mà
- Xử lý song song các entity (Char, Mob, ItemMap)

### Lazy Loading
- Chỉ cập nhật Mob khi cần thiết (delayUpdate >= 1000ms)
- Respawn mob theo thời gian chết (timeDie + 2500ms)
- Xử lý AI tấn công với delay 5000ms

### Memory Management
- Sử dụng ArrayList để quản lý entity
- Xóa entity khi không cần thiết
- Giải phóng Graphics2D sau khi sử dụng

## 7. Gợi Ý Mở Rộng

### Hiệu Ứng Nâng Cao
- Particle system cho kỹ năng
- Animation frame cho nhân vật
- Shader effects cho map

### Giao Diện
- Theme system
- Responsive design
- Accessibility features

### Performance
- GPU acceleration
- Level of detail (LOD)
- Occlusion culling
