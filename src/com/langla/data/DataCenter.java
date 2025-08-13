package com.langla.data;

import com.PKoolVNDB;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.langla.lib.Utlis;
import com.langla.lib.Binary;
import com.langla.real.bangxephang.BangXepHang;
import com.langla.real.bangxephang.Bxh_Tpl;
import com.langla.real.cho.ChoTemplate;
import com.langla.real.family.*;
import com.langla.real.item.Item;
import com.langla.real.item.ItemShop;
import com.langla.real.lucky.LuckyTpl;
import com.langla.real.map.BossRunTime;
import com.langla.real.map.BossTpl;
import com.langla.real.phucloi.PhucLoiInfo;
import com.langla.real.phucloi.PhucLoiTpl;
import com.langla.real.player.Player;
import com.langla.server.main.PKoolVN;
import com.langla.server.lib.Message;
import com.langla.server.lib.Reader;
import com.langla.server.lib.Writer;
import com.langla.utlis.UTPKoolVN;
import com.tgame.model.Caption;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.json.JSONArray;

import static com.langla.lib.Binary.x;

public class DataCenter {

    static {
        DataCenter.bg = null;
        DataCenter.bh = new Object();
        DataCenter.be = new Vector();

        PKoolVNDB.loadProperties();
        // Xóa 2 dòng này khỏi static block
        // DataCenter.gI().readArrDataGame(true);
        // DataCenter.gI().Load_Data();
    }
    public String ipServer;
    public int portServer;
    public String c;
    public int d;
    public int e;
    public boolean f;
    public boolean INPUT_CAPTCHA;
    public int h;
    public int i;
    public int j;
    public int k;
    public int l;
    public int m;
    public int n;
    public int widthScreen;
    public int heightScreen;
    public int q;
    public int r;
    public int zoomLevelScreen;
    public int typeArr;
    public int zoomLevel;
    public String v;
    public int typeOS;
    public int VER1;
    public int VER2;
    private boolean bf;
    public float z;
    public int A;
    public int B;
    public int C;
    public int D;
    public LangLa_gv[] E;
    public LangLa_jr[] F;
    public LangLa_jt[] G;
    public LangLa_js[] H;
    public LangLa_hj[] I;
    public LangLa_hn[] J;
    public Hashtable K;
    public LangLa_jy[] L;
    public NpcTemplate[] NpcTemplate;
    public MobTemplate[] MobTemplate;
    public MapTemplate[] MapTemplate;
    public DataTemplateAchievement[] DataTemplateAchievement;
    public DataTaskDay[] DataTaskDay;
    public Task[] Task;
    public DataNameChar[] DataNameChar;
    public DataIconChar[] DataIconChar;
    public DataNameClass[] DataNameClass;

    public ItemTemplate[] ItemTemplate;
    public java.util.Map<Integer, List<ItemShop>> shopTemplates = new java.util.HashMap<>();
    public java.util.Map<Integer, String> shopNames = new java.util.HashMap<>();
    public java.util.Map<Integer, Integer> lockMap = new java.util.HashMap<>();
    public Vector<ChoTemplate> DataCho = new Vector<ChoTemplate>();
    public int[] arrVongQuayNap = new int[] { 0, 80, 480, 880, 1880, 3280 };
    public List<PhucLoiTpl> DataPhucLoi = new ArrayList<>();
    public List<LuckyTpl> DataLucky = new ArrayList<>();

    public List<Item> dataCaiTrang = new ArrayList<>();

    public PhucLoiInfo phucLoiInfo = new PhucLoiInfo();
    public ItemOptionTemplate[] ItemOptionTemplate;
    public SkillTemplate[] SkillTemplate;
    public SkillClan[] SkillClan;
    public Vector vSkillViThu;
    public EffectTemplate[] EffectTemplate;
    public Skill[] Skill;
    public DataTypeItemBody[] DataTypeItemBody;
    public short[][] dataWayPoint;
    public int[][] dataGiftQuaySo;
    public byte[][] af;
    public Vector ag;
    public Vector ah;
    public Vector ai;
    public Hashtable aj;
    public Hashtable ak;
    public Hashtable al;
    public Hashtable am;
    public Hashtable an;
    private static DataCenter bg;
    private static Object bh;
    public static boolean ar;
    public static boolean as;
    public static boolean at;
    public int[] au;
    public int[] bacKhoaGhepDa;
    public int[] bacKhoaUpgradeVuKhi;
    public int[] bacKhoaUpgradeTrangBi;
    public int[] bacKhoaUpgradePhuKien;
    public int[] ngocKhamUpgrade;
    public long[] pointGhepDa;
    public long[] pointUpgradeVuKhi;
    public long[] pointUpgradeTrangBi;
    public long[] pointUpgradePhuKien;
    public String[][] dataTreoCho;
    public long[] exps;
    public boolean aG;
    public boolean aH;
    public boolean aI;
    public boolean aJ;
    public boolean aK;
    public boolean aL;
    public boolean aM;
    public boolean aN;
    public LangLa_gg aO;
    public static int aQ;
    public static int aR;
    public int aS;
    public int aT;
    public String aU;
    public String aV;
    public String aW;
    public String aX;
    public String aY;
    public String aZ;
    public boolean ba;
    public boolean bb;
    public boolean bc;
    public static int bd;
    public static Vector be;
    private DataImgEntity[] DataImgEntity;
    public Writer writerArrDataGame2;
    public final ObjectMapper mapper = createObjectMapper();

    public ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); // Không gặp lỗi khi serialize đối
                                                                                 // tượng trống

        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    public DataCenter() {
        this.ipServer = "http://localhost:8080/";
        this.portServer = 80;
        this.c = "";
        this.f = false;
        this.v = "abcdefg";
        this.typeOS = 0;
        this.VER1 = PKoolVNDB.version;
        this.VER2 = PKoolVNDB.version;
        this.bf = true;
        this.z = 1.0f;
        this.vSkillViThu = new Vector();
        this.ag = new Vector();
        this.ah = new Vector();
        this.ai = new Vector();
        this.aj = new Hashtable();
        this.ak = new Hashtable();
        this.al = new Hashtable();
        this.am = new Hashtable();
        this.an = new Hashtable();
        this.dataTreoCho = new String[2][];
        this.aU = "http://localhost:8080/";
        this.aV = this.aU + "checkpc.txt";
        this.aW = this.aU + "checkandroid.txt";
        this.aX = this.aU + "checkios.txt";
        this.aY = "";
        this.aZ = "";
    }

    public static DataCenter gI() {
        if (DataCenter.bg == null) {
            DataCenter.bg = new DataCenter();
        }
        return DataCenter.bg;
    }

    private void read1(Reader reader) throws java.io.IOException {
        byte[][] af = new byte[reader.readByte()][];
        for (int i = 0; i < af.length; ++i) {
            af[i] = new byte[reader.readByte()];
            for (int j = 0; j < af[i].length; ++j) {
                af[i][j] = reader.readByte();
            }
        }
        if (this.af == null || this.af.length == 0) {
            this.af = af;
        }
    }

    private void read2(Reader reader) throws java.io.IOException {
        LangLa_hj[] i = new LangLa_hj[reader.readShort()];
        for (int j = 0; j < i.length; ++j) {
            i[j] = new LangLa_hj();
            i[j].a = reader.readByte();
            i[j].b = reader.readByte();
            i[j].c = reader.readByte();
            i[j].d = reader.readByte();
            i[j].e = reader.readBoolean();
            i[j].f = reader.readBoolean();
            i[j].g = new short[reader.readByte()];
            for (int k = 0; k < i[j].g.length; ++k) {
                addVec(i[j].g[k] = reader.readShort());
            }
            i[j].h = new short[reader.readByte()][];
            for (int l = 0; l < i[j].h.length; ++l) {
                i[j].h[l] = new short[reader.readByte()];
                for (int n = 0; n < i[j].h[l].length; ++n) {
                    i[j].h[l][n] = reader.readShort();
                }
            }
        }
        if (this.I == null || this.I.length == 0) {
            this.I = i;
        }
    }

    private void read3(Reader reader) throws java.io.IOException {
        Vector<Short> c = new Vector<Short>();
        LangLa_hn[] j = new LangLa_hn[reader.readShort()];
        for (int i = 0; i < j.length; ++i) {
            j[i] = new LangLa_hn();
            j[i].a = reader.readByte();
            j[i].b = new LangLa_ho[reader.readUnsignedByte()];
            for (int k = 0; k < j[i].b.length; ++k) {
                j[i].b[k] = new LangLa_ho();
                j[i].b[k].a = reader.readShort();
                if (!c.contains(j[i].b[k].a)) {
                    c.add(j[i].b[k].a);
                }
                addVec(j[i].b[k].a);
                j[i].b[k].b = j[i].b[k].b2 = reader.readByte();
                if (j[i].b[k].b >= 30) {
                    LangLa_ho langLa_ho = j[i].b[k];
                    langLa_ho.b -= 30;
                    j[i].b[k].c = reader.readShort();
                    j[i].b[k].d = reader.readShort();
                } else if (j[i].b[k].b >= 20) {
                    LangLa_ho langLa_ho2 = j[i].b[k];
                    langLa_ho2.b -= 20;
                    j[i].b[k].d = reader.readShort();
                } else if (j[i].b[k].b >= 10) {
                    LangLa_ho langLa_ho3 = j[i].b[k];
                    langLa_ho3.b -= 10;
                    j[i].b[k].c = reader.readShort();
                }
            }
        }
        if (this.J == null || this.J.length == 0) {
            this.J = j;
            this.ah.clear();
            this.ah.addAll(c);
        }
    }

    private void read4(Reader reader) throws java.io.IOException {
        short sl = reader.readShort();
        LangLa_jy[] l = new LangLa_jy[sl];
        for (int i = 0; i < l.length; ++i) {

            l[i] = new LangLa_jy();
            l[i].a = reader.readByte();
            l[i].b = new LangLa_jx[reader.readUnsignedByte()];

            for (int j = 0; j < l[i].b.length; ++j) {
                l[i].b[j] = new LangLa_jx();
                l[i].b[j].a = reader.readShort();
                l[i].b[j].b = reader.readByte();
                byte byte1 = reader.readByte();
                l[i].b[j].num = byte1;
                if (byte1 >= 30) {
                    byte1 -= 30;
                    l[i].b[j].d = reader.readByte();
                    l[i].b[j].e = reader.readByte();
                } else if (byte1 >= 20) {
                    byte1 -= 20;
                    l[i].b[j].e = reader.readByte();
                } else if (byte1 >= 10) {
                    byte1 -= 10;
                    l[i].b[j].d = reader.readByte();
                }
                l[i].b[j].c = (byte1 != 0);

            }
        }

        if (this.L == null || this.L.length == 0) {
            this.L = l;
        }
    }

    public LangLa_iw[] LangLa_iw;

    private void read5(Reader reader) throws java.io.IOException {
        Hashtable<Short, LangLa_iw> k = new Hashtable<Short, LangLa_iw>();
        short short1 = reader.readShort();
        LangLa_iw = new LangLa_iw[short1];
        for (short n = 0; n < short1; ++n) {
            LangLa_iw value;
            (value = new LangLa_iw()).a = reader.readShort();
            value.b = reader.readShort();
            value.c = reader.readByte();
            value.d = new LangLa_ix[reader.readUnsignedByte()][];
            for (int i = 0; i < value.d.length; ++i) {
                value.d[i] = new LangLa_ix[reader.readUnsignedByte()];
                for (int j = 0; j < value.d[i].length; ++j) {
                    value.d[i][j] = new LangLa_ix();
                    value.d[i][j].a = reader.readByte();
                    value.d[i][j].b = value.d[i][j].b2 = reader.readByte();
                    if (value.d[i][j].b >= 30) {
                        LangLa_ix langLa_ix = value.d[i][j];
                        langLa_ix.b -= 30;
                        value.d[i][j].c = reader.readShort();
                        value.d[i][j].d = reader.readByte();
                        value.d[i][j].e = reader.readByte();
                        value.d[i][j].f = reader.readShort();
                        value.d[i][j].g = reader.readByte();
                        value.d[i][j].h = reader.readByte();
                    } else if (value.d[i][j].b >= 20) {
                        LangLa_ix langLa_ix2 = value.d[i][j];
                        langLa_ix2.b -= 20;
                        value.d[i][j].f = reader.readShort();
                        value.d[i][j].g = reader.readByte();
                        value.d[i][j].h = reader.readByte();
                    } else if (value.d[i][j].b >= 10) {
                        LangLa_ix langLa_ix3 = value.d[i][j];
                        langLa_ix3.b -= 10;
                        value.d[i][j].c = reader.readShort();
                        value.d[i][j].d = reader.readByte();
                        value.d[i][j].e = reader.readByte();
                    }
                }
            }
            LangLa_iw[n] = value;
            k.put(value.a, value);
        }
        if (this.K == null || this.K.size() == 0) {
            this.K = k;
        }
    }

    private void read6(Reader reader) throws java.io.IOException {
        short[][] dataWayPoint = new short[reader.readShort()][14];
        for (int index = 0; index < dataWayPoint.length; ++index) {
            dataWayPoint[index][0] = reader.readShort();
            dataWayPoint[index][1] = reader.readShort();
            dataWayPoint[index][2] = reader.readShort();
            dataWayPoint[index][3] = reader.readShort();
            dataWayPoint[index][4] = reader.readShort();
            dataWayPoint[index][10] = reader.readShort();
            dataWayPoint[index][11] = reader.readShort();
            dataWayPoint[index][5] = reader.readShort();
            dataWayPoint[index][6] = reader.readShort();
            dataWayPoint[index][7] = reader.readShort();
            dataWayPoint[index][8] = reader.readShort();
            dataWayPoint[index][9] = reader.readShort();
            dataWayPoint[index][12] = reader.readShort();
            dataWayPoint[index][13] = reader.readShort();
        }
        if (this.dataWayPoint == null || this.dataWayPoint.length == 0) {
            this.dataWayPoint = dataWayPoint;
        }
    }

    private static Hashtable getHashtable1(Reader reader) throws java.io.IOException {
        Hashtable<Short, LangLa_et> hashtable = new Hashtable<Short, LangLa_et>();
        short short1 = reader.readShort();
        DataHashtable1.data = new DataHashtable1[short1][0];
        for (short n = 0; n < short1; ++n) {
            short short2 = reader.readShort();
            DataHashtable1.data[n] = new DataHashtable1[short2];
            for (short n2 = 0; n2 < short2; ++n2) {
                Short s = Short.valueOf(reader.readShort());
                LangLa_et et = new LangLa_et(n, reader.readUnsignedByte(), reader.readUnsignedByte(),
                        reader.readShort(), reader.readShort());
                DataHashtable1.data[n][n2] = new DataHashtable1();
                DataHashtable1.data[n][n2].id = s;
                DataHashtable1.data[n][n2].et = et;
                hashtable.put(s, et);
            }
        }
        return hashtable;
    }

    public static class DataHashtable1 {

        public static DataHashtable1[][] data;
        public static DataHashtable1[][] data2;
        public static DataHashtable1[][] data3;
        public short id;
        public LangLa_et et;
    }

    private static Hashtable getHashtable2(Reader reader) throws java.io.IOException {
        Hashtable<Short, LangLa_et> hashtable = new Hashtable<Short, LangLa_et>();
        short short1 = reader.readShort();
        DataHashtable2.data = new DataHashtable2[short1];
        for (short n = 0; n < short1; ++n) {
            DataHashtable2.data[n] = new DataHashtable2();
            Short s = Short.valueOf(reader.readShort());
            LangLa_et et = new LangLa_et((short) (-1), (short) 0, (short) 0, reader.readShort(), reader.readShort());
            DataHashtable2.data[n].id = s;
            DataHashtable2.data[n].et = et;
            hashtable.put(s, et);
        }
        return hashtable;
    }

    public static class DataHashtable2 {

        public static DataHashtable2[] data;
        public static DataHashtable2[] data2;
        public static DataHashtable2[] data3;
        public short id;
        public LangLa_et et;
    }

    private static void addVec(short n) {
        if (!DataCenter.be.contains(n)) {
            DataCenter.be.addElement(n);
        }
    }

    public void readArrDataGame2(Message msg) {

        Reader readerArrDataGame2 = msg.reader;
        try {
            Vector vec = new Vector();
            ItemOptionTemplate[] ItemOptionTemplate = new ItemOptionTemplate[msg.readShort()];
            for (int index = 0; index < ItemOptionTemplate.length; ++index) {
                ItemOptionTemplate[index] = new ItemOptionTemplate(index);
                ItemOptionTemplate[index].name = Caption.check(msg.readUTF());
                ItemOptionTemplate[index].type = msg.readByte();
                ItemOptionTemplate[index].level = msg.readByte();
                ItemOptionTemplate[index].strOption = msg.readUTF();
            }
            this.ItemOptionTemplate = ItemOptionTemplate;
            EffectTemplate[] EffectTemplate = new EffectTemplate[msg.readByte()];
            for (int index = 0; index < EffectTemplate.length; ++index) {
                EffectTemplate[index] = new EffectTemplate(index);
                EffectTemplate[index].name = msg.readUTF();
                EffectTemplate[index].detail = msg.readUTF();
                EffectTemplate[index].type = msg.reader.readUnsignedByte();
                EffectTemplate[index].idIcon = msg.readShort();
                EffectTemplate[index].idMob = msg.readShort();
            }
            this.EffectTemplate = EffectTemplate;
            ItemTemplate[] ItemTemplate = new ItemTemplate[msg.readShort()];
            for (int index = 0; index < ItemTemplate.length; ++index) {
                ItemTemplate[index] = new ItemTemplate(index);
                ItemTemplate[index].name = Caption.check(msg.readUTF());
                ItemTemplate[index].detail = msg.readUTF();
                ItemTemplate[index].isXepChong = msg.readBoolean();
                ItemTemplate[index].gioiTinh = msg.readByte();
                ItemTemplate[index].type = msg.readByte();
                ItemTemplate[index].idClass = msg.readByte();
                ItemTemplate[index].idIcon = msg.readShort();
                ItemTemplate[index].levelNeed = msg.reader.readUnsignedByte();
                ItemTemplate[index].taiPhuNeed = msg.readUnsignedShort();
                ItemTemplate[index].idMob = msg.readShort();
                ItemTemplate[index].idChar = msg.readShort();
            }
            this.ItemTemplate = ItemTemplate;
            this.read1(msg.reader);
            this.read2(msg.reader);
            this.read3(msg.reader);
            this.read4(msg.reader);
            this.read5(msg.reader);
            this.read6(msg.reader);
            // Binary.writeUTF("ItemOptionTemplate.txt",
            // mArrays.toString(ItemOptionTemplate));
        } catch (Exception e) {
            Utlis.logError(DataCenter.class, e, "Da say ra loi:\n" + e.getMessage());
        } finally {
            if (msg != null) {
                msg.close();
            }
        }
    }

    public void readArrDataGame(boolean b) {
        Reader readerArrDataGame = null;
        try {
            System.out.println("[DEBUG] Bắt đầu đọc arr_data_game...");

            if ((readerArrDataGame = Binary.createReader("arr_data_game")) == null) {
                System.err.println("[ERROR] Không thể tạo Reader cho arr_data_game");
                throw new RuntimeException("File arr_data_game.bin không tồn tại hoặc bị lỗi");
            }

            this.DataIconChar = new DataIconChar[readerArrDataGame.readByte()];
            for (int i = 0; i < this.DataIconChar.length; ++i) {
                this.DataIconChar[i] = new DataIconChar(i);
                this.DataIconChar[i].idIcon = readerArrDataGame.readShort();
            }

            this.DataNameClass = new DataNameClass[readerArrDataGame.readByte()];
            for (int j = 0; j < this.DataNameClass.length; ++j) {
                this.DataNameClass[j] = new DataNameClass(j);
                this.DataNameClass[j].name = readerArrDataGame.readUTF();
            }
            this.DataNameChar = new DataNameChar[readerArrDataGame.readByte()];
            for (int k = 0; k < this.DataNameChar.length; ++k) {
                this.DataNameChar[k] = new DataNameChar(k);
                this.DataNameChar[k].name = readerArrDataGame.readUTF();
                this.DataNameChar[k].b = readerArrDataGame.readByte();
                this.DataNameChar[k].d = readerArrDataGame.readShort();
            }
            this.DataTemplateAchievement = new DataTemplateAchievement[readerArrDataGame.readUnsignedByte()];
            for (int l = 0; l < this.DataTemplateAchievement.length; ++l) {
                this.DataTemplateAchievement[l] = new DataTemplateAchievement();
                this.DataTemplateAchievement[l].id = readerArrDataGame.readByte();
                this.DataTemplateAchievement[l].name = readerArrDataGame.readUTF();
                this.DataTemplateAchievement[l].c = readerArrDataGame.readInt();
                this.DataTemplateAchievement[l].amountExp = readerArrDataGame.readInt();
                this.DataTemplateAchievement[l].amountVangKhoa = readerArrDataGame.readInt();
                this.DataTemplateAchievement[l].amountBac = readerArrDataGame.readInt();
                this.DataTemplateAchievement[l].amountBacKhoa = readerArrDataGame.readInt();
                this.DataTemplateAchievement[l].strItem = readerArrDataGame.readUTF();
            }
            this.Task = new Task[readerArrDataGame.readShort()];
            for (int a2 = 0; a2 < this.Task.length; ++a2) {
                this.Task[a2] = new Task();
                this.Task[a2].id = a2;
                this.Task[a2].name = readerArrDataGame.readUTF();
                this.Task[a2].levelNeed = readerArrDataGame.readShort();
                this.Task[a2].idNpc = readerArrDataGame.readShort();
                this.Task[a2].idMap = readerArrDataGame.readShort();
                this.Task[a2].x = readerArrDataGame.readShort();
                this.Task[a2].y = readerArrDataGame.readShort();
                this.Task[a2].STR1 = readerArrDataGame.readUTF();
                this.Task[a2].STR2 = readerArrDataGame.readUTF();
                this.Task[a2].STR3 = readerArrDataGame.readUTF();
                this.Task[a2].amountExp = readerArrDataGame.readInt();
                this.Task[a2].amountVangKhoa = readerArrDataGame.readInt();
                this.Task[a2].amountBac = readerArrDataGame.readInt();
                this.Task[a2].amountBacKhoa = readerArrDataGame.readInt();
                this.Task[a2].strItem = readerArrDataGame.readUTF();
                for (byte byte1 = readerArrDataGame.readByte(), b2 = 0; b2 < byte1; ++b2) {
                    Step obj;
                    (obj = new Step()).id = readerArrDataGame.readByte();
                    obj.name = readerArrDataGame.readUTF();
                    obj.idItem = readerArrDataGame.readShort();
                    obj.idNpc = readerArrDataGame.readShort();
                    obj.idMob = readerArrDataGame.readShort();
                    obj.idMap = readerArrDataGame.readShort();
                    obj.x = readerArrDataGame.readShort();
                    obj.y = readerArrDataGame.readShort();
                    obj.require = readerArrDataGame.readShort();
                    obj.STR = readerArrDataGame.readUTF();
                    obj.STR_ITEM = readerArrDataGame.readUTF();
                    this.Task[a2].vStep.addElement(obj);
                }

            }
            this.DataTaskDay = new DataTaskDay[readerArrDataGame.readUnsignedByte()];
            for (int n = 0; n < this.DataTaskDay.length; ++n) {
                this.DataTaskDay[n] = new DataTaskDay();
                this.DataTaskDay[n].id = readerArrDataGame.readByte();
                this.DataTaskDay[n].name = readerArrDataGame.readUTF();
                this.DataTaskDay[n].c = readerArrDataGame.readShort();
            }
            MapTemplate[] o = new MapTemplate[readerArrDataGame.readShort()];
            for (int n2 = 0; n2 < o.length; ++n2) {
                o[n2] = new MapTemplate(n2);
                o[n2].name = readerArrDataGame.readUTF();
                o[n2].typeBlockMap = readerArrDataGame.readUnsignedByte();
                o[n2].type = readerArrDataGame.readByte();
                o[n2].arrMap = Binary.read("ArrMap\\arr_map_" + n2);
                o[n2].notBlock = true;
                if (o[n2].arrMap != null) {
                    try {
                        o[n2].createBlock(new Reader(o[n2].arrMap));
                        o[n2].notBlock = false;
                    } catch (Exception ex) {
                        Utlis.logError(Player.class, ex, "Da say ra loi:\n" + ex.getMessage());
                    }
                }
                String jsonNpc = Binary.readUTF("Npc\\" + n2 + ".json");
                String jsonMob = Binary.readUTF("Mob\\" + n2 + ".json");
                if (jsonNpc != null) {
                    JSONArray jsonArray = new JSONArray(jsonNpc);
                    o[n2].loadNpc(jsonArray);
                }
                if (jsonMob != null) {
                    JSONArray jsonArray = new JSONArray(jsonMob);
                    o[n2].loadMob(jsonArray);
                }

            }
            this.MapTemplate = o;
            ItemOptionTemplate[] w = new ItemOptionTemplate[readerArrDataGame.readShort()];
            for (int n3 = 0; n3 < w.length; ++n3) {
                w[n3] = new ItemOptionTemplate(n3);
                w[n3].name = Caption.check(readerArrDataGame.readUTF());
                w[n3].type = readerArrDataGame.readByte();
                w[n3].level = readerArrDataGame.readByte();
                w[n3].strOption = readerArrDataGame.readUTF();
            }
            if (this.ItemOptionTemplate == null || this.ItemOptionTemplate.length == 0) {
                this.ItemOptionTemplate = w;
            }
            EffectTemplate[] aa = new EffectTemplate[readerArrDataGame.readByte()];
            for (int n4 = 0; n4 < aa.length; ++n4) {
                aa[n4] = new EffectTemplate(n4);
                aa[n4].name = readerArrDataGame.readUTF();
                aa[n4].detail = readerArrDataGame.readUTF();
                aa[n4].type = readerArrDataGame.readUnsignedByte();
                aa[n4].idIcon = readerArrDataGame.readShort();
                aa[n4].idMob = readerArrDataGame.readShort();
            }
            if (this.EffectTemplate == null || this.EffectTemplate.length == 0) {
                this.EffectTemplate = aa;
            }

            // Khởi tạo ItemTemplate
            ItemTemplate[] v = new ItemTemplate[readerArrDataGame.readShort()];
            System.out.println("[DEBUG] Đang đọc " + v.length + " ItemTemplate...");

            for (int n5 = 0; n5 < v.length; ++n5) {
                v[n5] = new ItemTemplate(n5);
                v[n5].name = Caption.check(readerArrDataGame.readUTF());
                v[n5].detail = readerArrDataGame.readUTF();
                v[n5].isXepChong = readerArrDataGame.readBoolean();
                v[n5].gioiTinh = readerArrDataGame.readByte();
                v[n5].type = readerArrDataGame.readByte();
                v[n5].idClass = readerArrDataGame.readByte();
                v[n5].idIcon = readerArrDataGame.readShort();
                v[n5].levelNeed = readerArrDataGame.readUnsignedByte();
                v[n5].taiPhuNeed = readerArrDataGame.readUnsignedShort();
                v[n5].idMob = readerArrDataGame.readShort();
                v[n5].idChar = readerArrDataGame.readShort();
            }

            if (this.ItemTemplate == null || this.ItemTemplate.length == 0) {
                this.ItemTemplate = v;
                System.out.println("[DEBUG] ItemTemplate đã được khởi tạo: " + this.ItemTemplate.length + " items");
            }

            this.MobTemplate = new MobTemplate[readerArrDataGame.readShort()];
            for (int n6 = 0; n6 < this.MobTemplate.length; ++n6) {

                this.MobTemplate[n6] = new MobTemplate(n6);
                this.MobTemplate[n6].g = readerArrDataGame.readShort();
                this.MobTemplate[n6].name = readerArrDataGame.readUTF();
                this.MobTemplate[n6].detail = readerArrDataGame.readUTF();
                this.MobTemplate[n6].speedMove = readerArrDataGame.readUnsignedByte();
                this.MobTemplate[n6].type = readerArrDataGame.readByte();
                this.MobTemplate[n6].speedMoveByte = readerArrDataGame.readByte();
                this.MobTemplate[n6].p = readerArrDataGame.readByte();
                this.MobTemplate[n6].indexData = readerArrDataGame.readShort();
                this.MobTemplate[n6].timeThuHoach = readerArrDataGame.readShort();
                MobTemplate mobTemplate = this.MobTemplate[n6];
                String utf = this.MobTemplate[n6].utf = readerArrDataGame.readUTF();
                String utf2 = this.MobTemplate[n6].utf2 = readerArrDataGame.readUTF();

                String s = utf;
                MobTemplate mobTemplate2 = mobTemplate;
                String[] a3 = Utlis.split(s, ",");
                mobTemplate2.k = new int[a3.length];
                for (int n7 = 0; n7 < a3.length; ++n7) {
                    mobTemplate2.k[n7] = Integer.parseInt(a3[n7]);
                }
                String[] a4 = Utlis.split(utf2, ",");
                mobTemplate2.l = new int[a4.length];
                for (int n8 = 0; n8 < a4.length; ++n8) {
                    mobTemplate2.l[n8] = Integer.parseInt(a4[n8]);
                }
            }

            this.NpcTemplate = new NpcTemplate[readerArrDataGame.readShort()];
            for (int n9 = 0; n9 < this.NpcTemplate.length; ++n9) {
                this.NpcTemplate[n9] = new NpcTemplate(n9);
                this.NpcTemplate[n9].name = Caption.check(readerArrDataGame.readUTF());
                this.NpcTemplate[n9].detail = readerArrDataGame.readUTF();
                this.NpcTemplate[n9].indexData = readerArrDataGame.readShort();
                this.NpcTemplate[n9].hp = readerArrDataGame.readInt();
                this.NpcTemplate[n9].mp = readerArrDataGame.readInt();
                this.NpcTemplate[n9].g = readerArrDataGame.readShort();
            }
            this.SkillTemplate = new SkillTemplate[readerArrDataGame.readShort()];
            for (int n10 = 0; n10 < this.SkillTemplate.length; ++n10) {
                this.SkillTemplate[n10] = new SkillTemplate(n10);
                this.SkillTemplate[n10].name = readerArrDataGame.readUTF();
                this.SkillTemplate[n10].detail = readerArrDataGame.readUTF();
                this.SkillTemplate[n10].levelNeed = readerArrDataGame.readShort();
                this.SkillTemplate[n10].idChar = readerArrDataGame.readByte();
                this.SkillTemplate[n10].levelMax = readerArrDataGame.readByte();
                this.SkillTemplate[n10].type = readerArrDataGame.readByte();
                this.SkillTemplate[n10].idIcon = readerArrDataGame.readShort();

            }
            this.Skill = new Skill[readerArrDataGame.readShort()];
            for (int n11 = 0; n11 < this.Skill.length; n11 = (short) (n11 + 1)) {
                Skill skill;
                (skill = new Skill()).id = readerArrDataGame.readShort();
                skill.idTemplate = readerArrDataGame.readShort();
                skill.level = readerArrDataGame.readByte();
                skill.levelNeed = readerArrDataGame.readUnsignedByte();
                skill.mpUse = readerArrDataGame.readShort();
                skill.coolDown = readerArrDataGame.readInt();
                skill.rangeNgang = readerArrDataGame.readShort();
                skill.rangeDoc = readerArrDataGame.readShort();
                skill.maxTarget = readerArrDataGame.readByte();
                skill.strOptions = readerArrDataGame.readUTF();

                skill.index = n11;
                this.Skill[skill.id] = skill;
                if (skill.rangeDoc <= 0) {
                    skill.rangeDoc = skill.rangeNgang;
                }
            }
            this.SkillClan = new SkillClan[readerArrDataGame.readUnsignedByte()];
            for (int a5 = 0; a5 < this.SkillClan.length; a5 = (short) (a5 + 1)) {
                SkillClan skillClan;
                (skillClan = new SkillClan()).id = a5;
                skillClan.name = readerArrDataGame.readUTF();
                skillClan.detail = readerArrDataGame.readUTF();
                skillClan.levelNeed = readerArrDataGame.readUnsignedByte();
                skillClan.strOptions = readerArrDataGame.readUTF();
                skillClan.idIcon = readerArrDataGame.readShort();
                skillClan.moneyBuy = readerArrDataGame.readInt();
                this.SkillClan[a5] = skillClan;
            }
            this.DataTypeItemBody = new DataTypeItemBody[readerArrDataGame.readByte()];
            for (int n12 = 0; n12 < this.DataTypeItemBody.length; ++n12) {
                this.DataTypeItemBody[n12] = new DataTypeItemBody();
                this.DataTypeItemBody[n12].type = readerArrDataGame.readByte();
            }
            this.aj = getHashtable1(readerArrDataGame);
            DataHashtable1.data2 = DataHashtable1.data;
            this.al = getHashtable2(readerArrDataGame);
            DataHashtable2.data2 = DataHashtable2.data;
            this.ak = getHashtable1(readerArrDataGame);
            DataHashtable1.data3 = DataHashtable1.data;
            this.am = getHashtable2(readerArrDataGame);
            DataHashtable2.data3 = DataHashtable2.data;

            LangLa_gv[] e = new LangLa_gv[readerArrDataGame.readUnsignedByte()];
            for (int n13 = 0; n13 < e.length; ++n13) {
                e[n13] = new LangLa_gv();
                e[n13].a = readerArrDataGame.readShort();
                e[n13].b = readerArrDataGame.readShort();
                e[n13].c = readerArrDataGame.readShort();
                e[n13].d = readerArrDataGame.readByte();
            }
            this.E = e;
            this.read1(readerArrDataGame);
            Reader reader2 = readerArrDataGame;
            short unsignedByte = reader2.readUnsignedByte();
            LangLa_jr[] f = new LangLa_jr[reader2.readShort()];
            for (int a6 = 0; a6 < f.length; ++a6) {
                f[a6] = new LangLa_jr();
                f[a6].a = a6;
                f[a6].b = reader2.readByte();
                f[a6].d = new LangLa_ju[unsignedByte];
                for (short n14 = 0; n14 < unsignedByte; ++n14) {
                    f[a6].d[n14] = new LangLa_ju(reader2.readShort());
                    if (f[a6].d[n14].a != 0) {
                        f[a6].d[n14].b = reader2.readByte();
                        f[a6].d[n14].c = reader2.readByte();
                        f[a6].d[n14].d = reader2.readByte();
                        f[a6].d[n14].e = reader2.readByte();
                        addVec(f[a6].d[n14].a);
                    }
                }
            }
            this.F = f;
            LangLa_jt[] g = new LangLa_jt[reader2.readShort()];
            for (int n15 = 0; n15 < g.length; ++n15) {
                g[n15] = new LangLa_jt();
                byte byte2 = reader2.readByte();
                g[n15].a = new short[byte2];
                for (byte b3 = 0; b3 < byte2; ++b3) {
                    g[n15].a[b3] = reader2.readShort();
                }
            }
            this.G = g;
            LangLa_js[] h = new LangLa_js[reader2.readShort()];
            for (int n16 = 0; n16 < h.length; ++n16) {
                h[n16] = new LangLa_js();
                String[] split = (h[n16].utf = reader2.readUTF()).split(",");
                h[n16].a[0] = new short[split.length];
                for (int n17 = 0; n17 < split.length; ++n17) {
                    try {
                        h[n16].a[0][n17] = Short.parseShort(split[n17]);
                    } catch (Exception ex) {
                        h[n16].a[0][n17] = 0;
                    }
                }
                String[] split2 = (h[n16].utf2 = reader2.readUTF()).split(",");
                h[n16].a[1] = new short[split2.length];
                for (int n18 = 0; n18 < split2.length; ++n18) {
                    try {
                        h[n16].a[1][n18] = Short.parseShort(split2[n18]);
                    } catch (Exception ex2) {
                        h[n16].a[1][n18] = 0;
                    }
                }
                String[] split3 = (h[n16].utf3 = reader2.readUTF()).split(",");
                h[n16].a[2] = new short[split3.length];
                for (int n19 = 0; n19 < split3.length; ++n19) {
                    try {
                        h[n16].a[2][n19] = Short.parseShort(split3[n19]);
                    } catch (Exception ex3) {
                        h[n16].a[2][n19] = 0;
                    }
                }
            }
            this.H = h;
            this.read2(readerArrDataGame);
            this.read3(readerArrDataGame);
            this.read4(readerArrDataGame);
            this.read5(readerArrDataGame);
            this.read6(readerArrDataGame);
            Reader reader3 = readerArrDataGame;
            short short1 = reader3.readShort();
            this.ag.clear();
            this.DataImgEntity = new DataImgEntity[short1];
            for (short i = 0; i < short1; ++i) {
                DataImgEntity[i] = new DataImgEntity();
                short short2 = DataImgEntity[i].s2 = reader3.readShort();
                short short3 = DataImgEntity[i].s3 = reader3.readShort();
                if (i == 235) {
                    short2 = 40;
                }
                byte[][] array = new byte[reader3.readByte()][];
                for (int index = 0; index < array.length; ++index) {
                    array[index] = new byte[reader3.readByte()];
                    for (int index2 = 0; index2 < array[index].length; ++index2) {
                        array[index][index2] = reader3.readByte();
                    }
                }
                DataImgEntity[i].array = array;
                LangLa_hz[] array2 = new LangLa_hz[reader3.readByte()];
                for (int index = 0; index < array2.length; ++index) {
                    array2[index] = new LangLa_hz();
                    array2[index].a = new LangLa_ia[reader3.readByte()];
                    for (int index2 = 0; index2 < array2[index].a.length; ++index2) {
                        array2[index].a[index2] = new LangLa_ia();
                        addVec(array2[index].a[index2].a = reader3.readShort());
                        if (!this.ag.contains(array2[index].a[index2].a)) {
                            this.ag.add(array2[index].a[index2].a);
                        }
                        array2[index].a[index2].d2 = array2[index].a[index2].d = reader3.readByte();
                        if (array2[index].a[index2].d >= 30) {
                            LangLa_ia langLa_ia = array2[index].a[index2];
                            langLa_ia.d -= 30;
                            array2[index].a[index2].b = reader3.readShort();
                            array2[index].a[index2].c = reader3.readShort();
                        } else if (array2[index].a[index2].d >= 20) {
                            LangLa_ia langLa_ia2 = array2[index].a[index2];
                            langLa_ia2.d -= 20;
                            array2[index].a[index2].c = reader3.readShort();
                        } else if (array2[index].a[index2].d >= 10) {
                            LangLa_ia langLa_ia3 = array2[index].a[index2];
                            langLa_ia3.d -= 10;
                            array2[index].a[index2].b = reader3.readShort();
                        }
                    }
                }
                DataImgEntity[i].array2 = array2;
                for (int index = 0; index < this.NpcTemplate.length; ++index) {
                    if (this.NpcTemplate[index].indexData == i) {
                        this.NpcTemplate[index].width = short2;
                        this.NpcTemplate[index].height = short3;
                        this.NpcTemplate[index].b = array2;
                        this.NpcTemplate[index].a = array;
                    }
                }
                for (int index = 0; index < this.MobTemplate.length; ++index) {
                    if (this.MobTemplate[index].indexData == i) {
                        this.MobTemplate[index].width = short2;
                        this.MobTemplate[index].height = short3;
                        switch (this.MobTemplate[index].indexData) {
                            case 47: {
                                MobTemplate mobTemplate3 = this.MobTemplate[index];
                                mobTemplate3.height -= 5;
                                break;
                            }
                            case 48: {
                                MobTemplate mobTemplate4 = this.MobTemplate[index];
                                mobTemplate4.height -= 50;
                                break;
                            }
                            case 49:
                            case 151:
                            case 193: {
                                MobTemplate mobTemplate5 = this.MobTemplate[index];
                                mobTemplate5.height -= 22;
                                break;
                            }
                            case 52:
                            case 227: {
                                MobTemplate mobTemplate6 = this.MobTemplate[index];
                                mobTemplate6.height -= 7;
                                break;
                            }
                            case 28:
                            case 53: {
                                MobTemplate mobTemplate7 = this.MobTemplate[index];
                                mobTemplate7.height -= 9;
                                break;
                            }
                            case 54:
                            case 59:
                            case 177: {
                                MobTemplate mobTemplate8 = this.MobTemplate[index];
                                mobTemplate8.height -= 65;
                                break;
                            }
                            case 55:
                            case 190: {
                                MobTemplate mobTemplate9 = this.MobTemplate[index];
                                mobTemplate9.height -= 20;
                                break;
                            }
                            case 56: {
                                MobTemplate mobTemplate10 = this.MobTemplate[index];
                                mobTemplate10.height += 3;
                                break;
                            }
                            case 57: {
                                MobTemplate mobTemplate11 = this.MobTemplate[index];
                                mobTemplate11.height += 2;
                                break;
                            }
                            case 60: {
                                MobTemplate mobTemplate12 = this.MobTemplate[index];
                                mobTemplate12.height += 5;
                                break;
                            }
                            case 63: {
                                MobTemplate mobTemplate13 = this.MobTemplate[index];
                                mobTemplate13.height -= 34;
                                break;
                            }
                            case 26: {
                                MobTemplate mobTemplate14 = this.MobTemplate[index];
                                mobTemplate14.height -= 20;
                                break;
                            }
                            case 9:
                            case 123: {
                                MobTemplate mobTemplate15 = this.MobTemplate[index];
                                mobTemplate15.height -= 4;
                                break;
                            }
                            case 157:
                            case 169:
                            case 170: {
                                MobTemplate mobTemplate16 = this.MobTemplate[index];
                                mobTemplate16.height -= 8;
                                break;
                            }
                            case 154:
                            case 155:
                            case 156:
                            case 158:
                            case 195:
                            case 220:
                            case 224:
                            case 226: {
                                MobTemplate mobTemplate17 = this.MobTemplate[index];
                                mobTemplate17.height -= 2;
                                break;
                            }
                            case 58:
                            case 124: {
                                MobTemplate mobTemplate18 = this.MobTemplate[index];
                                mobTemplate18.height -= 10;
                                break;
                            }
                            case 178:
                            case 192:
                            case 197:
                            case 198: {
                                MobTemplate mobTemplate19 = this.MobTemplate[index];
                                mobTemplate19.height -= 5;
                                break;
                            }
                            case 191:
                            case 196:
                            case 212:
                            case 225: {
                                MobTemplate mobTemplate20 = this.MobTemplate[index];
                                mobTemplate20.height -= 15;
                                break;
                            }
                            case 217: {
                                MobTemplate mobTemplate21 = this.MobTemplate[index];
                                mobTemplate21.height += 13;
                                break;
                            }
                        }
                        this.MobTemplate[index].b = array2;
                        this.MobTemplate[index].a = array;
                    }
                }
            }
            this.ai.clear();
            this.ai.add("iconClient.zip");
            this.ai.add("iconChar.zip");
            Reader reader4;
            ArrayServer[] arrayServer = new ArrayServer[(reader4 = readerArrDataGame).readByte()];
            for (int index = 0; index < arrayServer.length; ++index) {
                arrayServer[index] = new ArrayServer();
                arrayServer[index].nameServers = reader4.readUTF();
                arrayServer[index].servers = new Server[reader4.readByte()];
                for (int index2 = 0; index2 < arrayServer[index].servers.length; ++index2) {
                    arrayServer[index].servers[index2] = new Server();
                    arrayServer[index].servers[index2].id = reader4.readShort();
                    arrayServer[index].servers[index2].name = reader4.readUTF();
                    arrayServer[index].servers[index2].ip = reader4.readUTF();
                    arrayServer[index].servers[index2].port = reader4.readShort();
                    arrayServer[index].servers[index2].portCheck = reader4.readShort();
                }
            }
            Reader reader5;
            int[] au = new int[(reader5 = readerArrDataGame).readInt()];
            for (int index = 0; index < au.length; ++index) {
                au[index] = reader5.readShort();
            }
            this.au = au;

            this.bacKhoaGhepDa = readArrayInt(readerArrDataGame);
            this.bacKhoaUpgradeVuKhi = readArrayInt(readerArrDataGame);
            this.bacKhoaUpgradeTrangBi = readArrayInt(readerArrDataGame);
            this.bacKhoaUpgradePhuKien = readArrayInt(readerArrDataGame);
            this.pointGhepDa = readArrayLong(readerArrDataGame);
            this.pointUpgradeVuKhi = readArrayLong(readerArrDataGame);
            this.pointUpgradeTrangBi = readArrayLong(readerArrDataGame);
            this.pointUpgradePhuKien = readArrayLong(readerArrDataGame);
            this.ngocKhamUpgrade = readArrayInt2(readerArrDataGame);
            this.dataTreoCho[0] = readArrayString2(readerArrDataGame);
            this.dataTreoCho[1] = readArrayString2(readerArrDataGame);

            this.exps = readArrayLong(readerArrDataGame);
            this.dataGiftQuaySo = new int[readerArrDataGame.readByte()][];
            for (int index = 0; index < this.dataGiftQuaySo.length; ++index) {
                this.dataGiftQuaySo[index] = readArrayInt(readerArrDataGame);
            }

            this.vSkillViThu.clear();
            try {
                for (byte size = readerArrDataGame.readByte(), index = 0; index < size; ++index) {
                    SkillClan skillClan;
                    (skillClan = new SkillClan()).id = readerArrDataGame.readByte();
                    skillClan.name = readerArrDataGame.readUTF();
                    skillClan.detail = readerArrDataGame.readUTF();
                    skillClan.levelNeed = readerArrDataGame.readUnsignedByte();
                    skillClan.strOptions = readerArrDataGame.readUTF();
                    skillClan.idIcon = readerArrDataGame.readShort();
                    this.vSkillViThu.add(skillClan);
                }
            } catch (Exception ex4) {
            }
            this.typeArr = readerArrDataGame.readByte();
            byte[] data = Binary.read("arr_data_game2");
            writerArrDataGame2 = new Writer();
            for (int i = 0; i < data.length; i++) {
                writerArrDataGame2.writeByte(data[i]);
            }
            this.readArrDataGame2(new Message((byte) 0, data));
            // for (int n11 = 0; n11 < this.Skill.length; n11 = (short) (n11 + 1)) {
            //
            // Skill[n11].getItemOption();
            // }
        } catch (Exception ex5) {
            System.err.println("[ERROR] Lỗi khi đọc arr_data_game: " + ex5.getMessage());
            ex5.printStackTrace();

            // Thử tải từ web
            try {
                byte[] c = Binary.c(PKoolVNDB.URL_WEB + "/arr_data_game.bin");
                if (c != null) {
                    Binary.write("arr_data_game", c);
                    System.out.println("[DEBUG] Thử đọc lại arr_data_game từ web...");
                    this.readArrDataGame(b);
                } else {
                    throw new RuntimeException("Không thể tải file arr_data_game.bin từ web");
                }
            } catch (Exception e) {
                System.err.println("[ERROR] Không thể khôi phục dữ liệu: " + e.getMessage());
                throw new RuntimeException("Không thể load dữ liệu game", e);
            }
        } finally {
            if (readerArrDataGame != null) {
                readerArrDataGame.close();
            }
            this.ba = true;
        }
    }

    public ChoTemplate findChoById(long id) {
        for (ChoTemplate cho : this.DataCho) {
            if (cho.id == id) {
                return cho; // Trả về phần tử có id phù hợp
            }
        }
        return null; // Trả về null nếu không tìm thấy id phù hợp trong danh sách
    }

    public boolean deleteChoById(long id) {
        for (Iterator<ChoTemplate> iterator = this.DataCho.iterator(); iterator.hasNext();) {
            ChoTemplate cho = iterator.next();
            if (cho.id == id) {
                iterator.remove(); // Xóa phần tử có id phù hợp
                return true; // Trả về true nếu xóa thành công
            }
        }
        return false; // Trả về false nếu không tìm thấy id phù hợp trong danh sách
    }

    public void Load_Data() {
        // Load Shop
        String query = "SELECT * FROM `shop`";
        try (Connection con = PKoolVN.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            ResultSet red = pstmt.executeQuery();
            while (red.next()) {
                int id_shop = red.getInt("id_shop");
                String shopName = red.getString("Tên_Shop"); // Giả sử cột tên cửa hàng là "shop_name"
                shopNames.put(id_shop, shopName);
                ObjectMapper mapper = DataCenter.gI().mapper;
                List<ItemShop> itemList = mapper.readValue(red.getString("list_item"),
                        TypeFactory.defaultInstance().constructCollectionType(List.class, ItemShop.class));

                for (ItemShop item : itemList) {
                    item.id_buy = DataCache.idbuyshop++; // Thiết lập id_buy
                }
                shopTemplates.computeIfAbsent(id_shop, k -> new ArrayList<>()).addAll(itemList);
            }
        } catch (SQLException | IOException e) {
            Utlis.logError(DataCenter.class, e, "Da say ra loi:\n" + e.getMessage());
        }
        // Load Lock Map
        query = "SELECT * FROM `map_lock`";
        try (Connection con = PKoolVN.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            ResultSet red = pstmt.executeQuery();
            while (red.next()) {
                int id = red.getInt("id");
                int lv = red.getInt("level");
                lockMap.put(id, lv);
            }
        } catch (SQLException e) {
            Utlis.logError(DataCenter.class, e, "Da say ra loi:\n" + e.getMessage());
        }
        // load market
        query = "SELECT * FROM `market`";
        try (Connection con = PKoolVN.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            ResultSet red = pstmt.executeQuery();
            while (red.next()) {
                ObjectMapper mapper = DataCenter.gI().mapper;
                ChoTemplate cho = new ChoTemplate();
                cho.id = red.getLong("id");
                cho.character_id = red.getInt("character_id");
                cho.character_name = red.getString("character_name");
                cho.isBuy = red.getInt("isBuy");
                cho.item = mapper.readValue(red.getString("item"), Item.class);
                cho.time = red.getInt("time");
                cho.bac = red.getInt("bac");
                this.DataCho.add(cho);
            }
        } catch (SQLException | IOException e) {
            Utlis.logError(DataCenter.class, e, "Da say ra loi:\n" + e.getMessage());
        }
        // Load Boss
        query = "SELECT * FROM `boss`";
        try (Connection con = PKoolVN.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            ResultSet red = pstmt.executeQuery();
            while (red.next()) {
                BossTpl newboss = new BossTpl();
                newboss.id = red.getInt("id");
                newboss.name = red.getString("name");
                newboss.level = red.getInt("level");
                newboss.cx = red.getInt("cx");
                newboss.cy = red.getInt("cy");
                newboss.map = red.getInt("map");
                newboss.hp = red.getInt("hp");
                newboss.damage = red.getInt("damage");
                newboss.exp = red.getInt("exp");
                newboss.min_spam = red.getInt("minute_respam");
                newboss.hou_spam = red.getInt("hour_respam");
                newboss.timeDelay = System.currentTimeMillis() + Utlis.nextInt(10000, 60000);
                BossRunTime.gI().listBoss.add(newboss);
            }
        } catch (SQLException e) {
            Utlis.logError(DataCenter.class, e, "Da say ra loi:\n" + e.getMessage());
        }
        // load phúc lợi
        query = "SELECT * FROM `phuc_loi`";
        try (Connection con = PKoolVN.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            ResultSet red = pstmt.executeQuery();
            while (red.next()) {
                ObjectMapper mapper = DataCenter.gI().mapper;

                this.DataPhucLoi = mapper.readValue(red.getString("str"),
                        TypeFactory.defaultInstance().constructCollectionType(List.class, PhucLoiTpl.class));
            }
        } catch (SQLException | IOException e) {
            Utlis.logError(DataCenter.class, e, "Da say ra loi:\n" + e.getMessage());
        }
        // load lucky
        query = "SELECT * FROM `lucky`";
        try (Connection con = PKoolVN.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            ResultSet red = pstmt.executeQuery();
            while (red.next()) {
                ObjectMapper mapper = DataCenter.gI().mapper;

                this.DataLucky = mapper.readValue(red.getString("str"),
                        TypeFactory.defaultInstance().constructCollectionType(List.class, LuckyTpl.class));
            }
        } catch (SQLException | IOException e) {
            Utlis.logError(DataCenter.class, e, "Da say ra loi:\n" + e.getMessage());
        }
        // load gia tộc
        query = "SELECT * FROM `giatoc`";
        try (Connection con = PKoolVN.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            ResultSet red = pstmt.executeQuery();
            Family.gI().listFamily.clear();
            while (red.next()) {
                FamilyTemplate familyTemplate = new FamilyTemplate();
                familyTemplate.id = red.getInt("id");
                familyTemplate.name = red.getString("name");

                ObjectMapper mapper = DataCenter.gI().mapper;

                familyTemplate.info = mapper.readValue(red.getString("info"), FamilyInfo.class);

                familyTemplate.litsItem = mapper.readValue(red.getString("item"),
                        TypeFactory.defaultInstance().constructCollectionType(List.class, Item.class));

                familyTemplate.listMember = mapper.readValue(red.getString("member"),
                        TypeFactory.defaultInstance().constructCollectionType(List.class, Family_Member.class));

                familyTemplate.listSkill = mapper.readValue(red.getString("skill"),
                        TypeFactory.defaultInstance().constructCollectionType(List.class, SkillClan.class));

                familyTemplate.dataLog = mapper.readValue(red.getString("log"),
                        TypeFactory.defaultInstance().constructCollectionType(List.class, FamilyLog.class));

                Family.gI().listFamily.add(familyTemplate);

            }
        } catch (SQLException | IOException e) {
            Utlis.logError(DataCenter.class, e, "Da say ra loi:\n" + e.getMessage());
        }
        // Gson gsons = new Gson();
        // String itemListJsons = gsons.toJson(this.DataPhucLoi);
        // // Phân tách các JSON bằng dấu xuống dòng
        // itemListJsons = itemListJsons.replace("},", "},\n\n\n");
        // System.out.println(itemListJsons);
        query = "SELECT * FROM `phucloi_info`";
        try (Connection con = PKoolVN.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            ResultSet red = pstmt.executeQuery();
            while (red.next()) {
                int id = red.getInt("id");
                long value = red.getLong("value");
                if (id == 0) {
                    phucLoiInfo.TongRank = (int) value;
                } else if (id == 1) {
                    phucLoiInfo.RankCaoNhat = (int) value;
                } else if (id == 2) {
                    phucLoiInfo.TongDauTu = (int) value;
                } else if (id == 3) {
                    phucLoiInfo.TongSoLanMuaTheThang = (int) value;
                } else if (id == 4) {
                    phucLoiInfo.ThoiGianX2Online = value;
                }
            }
        } catch (SQLException e) {
            Utlis.logError(DataCenter.class, e, "Da say ra loi:\n" + e.getMessage());
        }

        query = "SELECT * FROM `bangxephang`";
        try (Connection con = PKoolVN.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            ResultSet red = pstmt.executeQuery();
            while (red.next()) {
                int id = red.getInt("id");
                ObjectMapper mapper = DataCenter.gI().mapper;

                if (id == 1) {
                    BangXepHang.gI().listCaoThu = mapper.readValue(red.getString("list"),
                            TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Bxh_Tpl.class));
                } else if (id == 2) {
                    BangXepHang.gI().listCuaCai = mapper.readValue(red.getString("list"),
                            TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Bxh_Tpl.class));
                } else if (id == 3) {
                    BangXepHang.gI().listTaiPhu = mapper.readValue(red.getString("list"),
                            TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Bxh_Tpl.class));
                } else if (id == 7) {
                    BangXepHang.gI().listCuongHoa = mapper.readValue(red.getString("list"),
                            TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Bxh_Tpl.class));
                } else if (id == 8) {
                    BangXepHang.gI().listNapNhieu = mapper.readValue(red.getString("list"),
                            TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Bxh_Tpl.class));
                }
            }
        } catch (SQLException | IOException e) {
            Utlis.logError(DataCenter.class, e, "Da say ra loi:\n" + e.getMessage());
        }

        UTPKoolVN.Print("Import Database Successfully.!");
    }

    public void updateShopToData(int idshop) {
        // Kết nối đến cơ sở dữ liệu
        try (Connection con = PKoolVN.getConnection()) {
            // Lấy danh sách List<ItemShop> tương ứng với idshop
            List<ItemShop> itemList = shopTemplates.get(idshop);

            if (itemList != null) {
                ObjectMapper mapper = DataCenter.gI().mapper;
                String itemListJson = mapper.writeValueAsString(itemList);
                String updateQuery = "UPDATE shop SET list_item = ? WHERE id_shop = ?";
                try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
                    pstmt.setString(1, itemListJson);
                    pstmt.setInt(2, idshop);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    Utlis.logError(DataCenter.class, e, "Da say ra loi:\n" + e.getMessage());
                }
            }
        } catch (SQLException | IOException e) {
            Utlis.logError(DataCenter.class, e, "Da say ra loi:\n" + e.getMessage());
        }
    }

    public void updatePhucLoi(int id, int value) {
        // Kết nối đến cơ sở dữ liệu
        try (Connection con = PKoolVN.getConnection()) {
            String updateQuery = "UPDATE phucloi_info SET value = ? WHERE id = ?";
            try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
                pstmt.setInt(1, value);
                pstmt.setInt(2, id);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                Utlis.logError(DataCenter.class, e, "Da say ra loi:\n" + e.getMessage());
            }
        } catch (SQLException e) {
            Utlis.logError(DataCenter.class, e, "Da say ra loi:\n" + e.getMessage());
        }
    }

    public int getVongQuayNap(int diem, int tile) {
        for (int var1 = this.arrVongQuayNap.length - 1; var1 >= 0; --var1) {
            if (diem >= this.arrVongQuayNap[var1]) {
                return var1 - tile;
            }
        }

        return 0;
    }

    public int getTile(int diem) {
        for (int var1 = this.arrVongQuayNap.length - 1; var1 >= 0; --var1) {
            if (diem >= this.arrVongQuayNap[var1]) {
                return var1;
            }
        }

        return 0;
    }

    public int getDiem(int diem) {
        for (int var1 = this.arrVongQuayNap.length - 1; var1 >= 0; --var1) {
            if (diem >= this.arrVongQuayNap[var1]) {
                return this.arrVongQuayNap[var1];
            }
        }

        return 0;
    }

    public PhucLoiTpl getPhucLoi_Tpl(short idRequest) {
        for (int i = 0; i < DataCenter.gI().DataPhucLoi.size(); i++) {
            PhucLoiTpl phucLoi = DataCenter.gI().DataPhucLoi.get(i);
            if (phucLoi.idRequest == idRequest) {
                return phucLoi;
            }
        }
        return null;
    }

    public SkillClan getSkillClan(byte id) {
        for (int i = 0; i < DataCenter.gI().SkillClan.length; i++) {
            SkillClan skillClan = DataCenter.gI().SkillClan[id];
            if (skillClan != null) {
                return skillClan;
            }
        }
        return null;
    }

    public SkillClan getSkillViThu(byte id) {
        for (int i = 0; i < DataCenter.gI().vSkillViThu.size(); i++) {
            SkillClan skillClan = (SkillClan) DataCenter.gI().vSkillViThu.get(i);
            if (skillClan != null && skillClan.id == id) {
                return skillClan;
            }
        }
        return null;
    }

    public int getLockMap(int id) {
        return lockMap.getOrDefault(id, -1);
    }

    public int getValueCheTao(int id) {
        switch (id) {
            case 0:
                return 1;
            case 1:
            case 4:
                return 10;
            case 2:
                return 100;
            case 3:
            case 6:
            case 5:
            case 7:
                return 5;
            case 8:
                return 3;
        }
        return -1;
    }

    public int getHoatLucCheTao(int id) {
        switch (id) {
            case 0:
                return 5;
            case 1:
                return 50;
            case 2:
                return 500;
            case 3:
                return 30;
            case 4:
                return 70;
            case 5:
                return 35;
            case 6:
                return 35;
            case 7:
                return 35;
            case 8:
                return 50;
        }
        return -1;
    }

    public int getItemAddCheTao(int id) {
        switch (id) {
            case 0:
                return 176;
            case 1:
                return 176;
            case 2:
                return 231;
            case 3:
                return 361;
            case 4:
                return 362;
            case 5:
                return 563;
            case 6:
                return 565;
            case 7:
                return 567;
            case 8:
                return 353;
        }
        return -1;
    }

    public static class DataImgEntity {

        private short s2;
        private short s3;
        private byte[][] array;
        private LangLa_hz[] array2;

    }

    private static int[] readArrayInt(Reader reader) throws java.io.IOException {
        int[] array = new int[reader.readInt()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = reader.readInt();
        }
        return array;
    }

    private static int[] readArrayInt2(Reader reader) throws java.io.IOException {
        int[] array = new int[reader.readInt()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = reader.readInt();
            // UPKoolVN.Print(""+array[i]);
        }
        return array;
    }

    private static long[] readArrayLong(Reader reader) throws java.io.IOException {
        long[] array = new long[reader.readInt()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = reader.readLong();
        }
        return array;
    }

    private static String[] readArrayString(Reader reader) throws java.io.IOException {
        String[] array = new String[reader.readInt()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = reader.readUTF();
        }
        return array;
    }

    private static String[] readArrayString2(Reader reader) throws java.io.IOException {
        String[] array = new String[reader.readInt()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = reader.readUTF();
        }
        return array;
    }

    public static Vector h() {
        Vector<short[]> vector = new Vector<short[]>();
        for (int i = 0; i < gI().dataWayPoint.length; ++i) {
            vector.add(gI().dataWayPoint[i]);
        }
        return vector;
    }

    public Skill getSkillWithIdAndLevel(int id, int level) {
        for (int i = 0; i < this.Skill.length; i++) {
            if (this.Skill[i].idTemplate == id && this.Skill[i].level == level) {
                return this.Skill[i].cloneSkill();
            }

        }
        return null;
    }

    public static int GetLevelFormExp(long var1) {

        int var3;
        for (var3 = 0; var3 < DataCenter.gI().exps.length && var1 >= DataCenter.gI().exps[var3]; ++var3) {
            var1 -= DataCenter.gI().exps[var3];
        }

        return var3;
    }

    public static long GetExpFormLevel(int lv) {

        long l = 0;
        int var3;
        for (var3 = 0; var3 < DataCenter.gI().exps.length && var3 < lv; ++var3) {
            l += DataCenter.gI().exps[var3];
        }

        return l;
    }

}
