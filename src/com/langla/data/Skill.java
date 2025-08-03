package com.langla.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.langla.lib.Utlis;
import com.langla.real.map.Mob;
import com.langla.real.player.Char;
import com.langla.real.player.Client;
import com.langla.utlis.UTPKoolVN;

import java.util.Comparator;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Skill implements Cloneable {

    public static Comparator a = new LangLa_it();
    public static Comparator b = new LangLa_iu();
    public short id;
    public short idTemplate;
    public byte level;
    public short levelNeed;
    public short mpUse;
    public int coolDown;
    public short rangeNgang;
    public short rangeDoc;
    public byte maxTarget;
    public String strOptions;
    public long time;
    private int o = 0;
    public boolean myUse;
    public int index;
    @JsonIgnore
    public ItemOption[] getItemOption() {
        if (strOptions != null && strOptions.length() > 0) {
            String[] var1;
            ItemOption[] itemOption = new ItemOption[(var1 = Utlis.split(strOptions, ";")).length];

            for (int var3 = 0; var3 < var1.length; ++var3) {
                try {
                    itemOption[var3] = new ItemOption(var1[var3]);
                } catch (Exception ex) {
                    Utlis.logError(Skill.class, ex , "Da say ra loi:\n" + ex.getMessage());
                }
            }

            return itemOption;
        } else {
            return new ItemOption[0];
        }
    }
    @JsonIgnore
    public Skill cloneSkill() {
        try {
            return (Skill) super.clone();
        } catch (Exception e) {
            Utlis.logError(Skill.class, e , "Da say ra loi:\n" + e.getMessage());
            return null;
        }
    }
    @JsonIgnore
    public SkillTemplate getSkillTemplate() {
        return DataCenter.gI().SkillTemplate[this.idTemplate];
    }

    public boolean c() {
        return this.getSkillTemplate().type < 5 || this.getSkillTemplate().type == 6;
    }

    public boolean d() {
        return this.getSkillTemplate().type == 1;
    }

    //   public boolean a(Char var1) {
//      if (this.time < Utlis.time() && var1.mp >= this.mpUse) {
//         this.o = 0;
//         return true;
//      } else {
//         ++this.o;
//         if (this.o >= 2) {
//            this.o = 0;
//         }
//
//         if (var1.mpFull < this.mpUse && var1.isMe()) {
//            DataCenter.gI().currentScreen.showMessage(Caption.lj + " " + this.getSkillTemplate().name, -65536);
//         } else if (SettingsTab.j && var1.mp < this.mpUse && var1.isMe()) {
//            SettingsTab.j = false;
//            DataCenter.gI().currentScreen.showMessage(Caption.lj + " " + this.getSkillTemplate().name, -65536);
//         }
//
//         return false;
//      }
//   }
//
//   public void e() {
//      this.time = Utlis.time() + (long)this.coolDown;
//   }
//
//   public float f() {
//      return (float)(this.time - Utlis.time());
//   }
//
//   public ItemOption[] g() {
//      if (this.strOptions != null && this.strOptions.length() > 0) {
//         String[] var1;
//         ItemOption[] var2 = new ItemOption[(var1 = Utlis.split(this.strOptions, ";")).length];
//
//         for(int var3 = 0; var3 < var1.length; ++var3) {
//            var2[var3] = new ItemOption(var1[var3]);
//         }
//
//         return var2;
//      } else {
//         return null;
//      }
//   }
//
//   public boolean h() {
//      return this.idTemplate == 11 || this.idTemplate == 36;
//   }
    public Object clone() {
        return this.cloneSkill();
    }

    public int getDameMob(Client client, Mob mob) {
        int dame = 0;
        int boQuaNeTranh = 0;
        ItemOption[] array = getItemOption();
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] != null) {
                    try {
                        if (array[i].a[0] == 78 || array[i].a[0] == 89) {
                            dame += array[i].a[1];
                        } else if (array[i].a[0] == 147) { // bỏ qua né tránh
                            boQuaNeTranh += array[i].a[1];
                        } else if (array[i].a[0] == 138) { // tương khắc lên thổ
                            if (mob.he == 1) {
                                dame += array[i].a[1];
                            }
                        } else if (array[i].a[0] == 139) { // tương khắc lên thủy
                            if (mob.he == 1) {
                                dame += array[i].a[1];
                            }
                        } else if (array[i].a[0] == 140) { // tương khắc lên hỏa
                            if (mob.he == 1) {
                                dame += array[i].a[1];
                            }
                        } else if (array[i].a[0] == 141) { // tương khắc lên phong
                            if (mob.he == 1) {
                                dame += array[i].a[1];
                            }
                        } else if (array[i].a[0] == 73) {//1
                            dame += array[i].a[1];
                            if (mob.he == 2) {
                                dame += array[i].a[1] * 50 / 100;
                            } else if (mob.he == 5) {
                                dame -= array[i].a[1] * 50 / 100;
                            }
                        } else if (array[i].a[0] == 74) {//2
                            dame += array[i].a[1];
                            if (mob.he == 3) {
                                dame += array[i].a[1] * 50 / 100;
                            } else if (mob.he == 1) {
                                dame -= array[i].a[1] * 50 / 100;
                            }
                        } else if (array[i].a[0] == 75) {//3
                            dame += array[i].a[1];
                            if (mob.he == 4) {
                                dame += array[i].a[1] * 50 / 100;
                            } else if (mob.he == 2) {
                                dame -= array[i].a[1] * 50 / 100;
                            }
                        } else if (array[i].a[0] == 76) {//4
                            dame += array[i].a[1];
                            if (mob.he == 5) {
                                dame += array[i].a[1] * 50 / 100;
                            } else if (mob.he == 3) {
                                dame -= array[i].a[1] * 50 / 100;
                            }
                        } else if (array[i].a[0] == 77) {//5
                            dame += array[i].a[1];
                            if (mob.he == 1) {
                                dame += array[i].a[1] * 50 / 100;
                            } else if (mob.he == 4) {
                                dame -= array[i].a[1] * 50 / 100;
                            }
                        }
                        //      System.out.println(mob.he + ": " + array[i].a[0]);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            }
        }

        if (this.idTemplate == SkillTemplate.LOI_KIEM) {
            Skill skill = client.mChar.getSkillWithIdTemplate(SkillTemplate.TAM_TRUNG_OAN_THU_THUAT);
            if (skill != null) {
                int cX = skill.getChiSo( 92);
                if (cX != 0) {
                    dame = dame + (dame * cX / 100);
                }
            }
        } else if (this.idTemplate == SkillTemplate.ANH_PHONG_XA) {
            Skill skill = client.mChar.getSkillWithIdTemplate(SkillTemplate.SONG_THANG_LONG);
            if (skill != null) {
                int cX = skill.getChiSo( 92);
                if (cX != 0) {
                    dame = dame + (dame * cX / 100);
                }
            }
        } else if (this.idTemplate == SkillTemplate.AI_BOC_BO_THUAT) {
            Skill skill = client.mChar.getSkillWithIdTemplate(SkillTemplate.THUY_LONG_AN_THUAT);
            if (skill != null) {
                int cX = skill.getChiSo(92);
                if (cX != 0) {
                    dame = dame + (dame * cX / 100);
                }
            }
        } else if (this.idTemplate == SkillTemplate.HAO_HOA_CAU_THUAT) {
            Skill skill = client.mChar.getSkillWithIdTemplate(SkillTemplate.AM_SAT_THUAT);
            if (skill != null) {
                int cX = skill.getChiSo( 92);
                if (cX != 0) {
                    dame = dame + (dame * cX / 100);
                }
            }
        } else if (this.idTemplate == SkillTemplate.PHI_LOI_THAN_THUAT) {
            Skill skill = client.mChar.getSkillWithIdTemplate(SkillTemplate.THIEN_SAT_THUY_PHI);
            if (skill != null) {
                int cX = skill.getChiSo( 92);
                if (cX != 0) {
                    dame = dame + (dame * cX / 100);
                }
            }
        }
        boQuaNeTranh += client.mChar.getBoQuaNeTranh();
        boQuaNeTranh += client.mChar.getChinhXac();
        int neTranh = mob.NeTranh-boQuaNeTranh;
        if(neTranh < 0) neTranh = 0;
        neTranh /= 100;
        boolean ne_Tranh = Utlis.randomBoolean(100, neTranh);
        if (ne_Tranh && neTranh > 0) {
            dame = 0;
        }
        return dame;
    }
    public int getDamePlayer(Client client, Char player, int dame) {
        int boQuaNeTranh = 0;

        ItemOption[] array = getItemOption();
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] != null) {
                    try {
                        if (array[i].a[0] == 78) {
                            dame += array[i].a[1];
                        } else if (array[i].a[0] == 147) { // bỏ qua né tránh
                            boQuaNeTranh += array[i].a[1];
                        } else if (array[i].a[0] == 138) { // tương khắc lên thổ
                            if (player.infoChar.idhe == 1) {
                                dame += array[i].a[1];
                            }
                        } else if (array[i].a[0] == 139) { // tương khắc lên thủy
                            if (player.infoChar.idhe == 1) {
                                dame += array[i].a[1];
                            }
                        } else if (array[i].a[0] == 140) { // tương khắc lên hỏa
                            if (player.infoChar.idhe == 1) {
                                dame += array[i].a[1];
                            }
                        } else if (array[i].a[0] == 141) { // tương khắc lên phong
                            if (player.infoChar.idhe == 1) {
                                dame += array[i].a[1];
                            }
                        } else if (array[i].a[0] == 73) {//1
                            dame += array[i].a[1];
                            if (player.infoChar.idhe == 2) {
                                dame += array[i].a[1] * 50 / 100;
                            } else if (player.infoChar.idhe == 5) {
                                dame -= array[i].a[1] * 50 / 100;
                            }
                        } else if (array[i].a[0] == 74) {//2
                            dame += array[i].a[1];
                            if (player.infoChar.idhe == 3) {
                                dame += array[i].a[1] * 50 / 100;
                            } else if (player.infoChar.idhe == 1) {
                                dame -= array[i].a[1] * 50 / 100;
                            }
                        } else if (array[i].a[0] == 75) {//3
                            dame += array[i].a[1];
                            if (player.infoChar.idhe == 4) {
                                dame += array[i].a[1] * 50 / 100;
                            } else if (player.infoChar.idhe == 2) {
                                dame -= array[i].a[1] * 50 / 100;
                            }
                        } else if (array[i].a[0] == 76) {//4
                            dame += array[i].a[1];
                            if (player.infoChar.idhe == 5) {
                                dame += array[i].a[1] * 50 / 100;
                            } else if (player.infoChar.idhe == 3) {
                                dame -= array[i].a[1] * 50 / 100;
                            }
                        } else if (array[i].a[0] == 77) {//5
                            dame += array[i].a[1];
                            if (player.infoChar.idhe== 1) {
                                dame += array[i].a[1] * 50 / 100;
                            } else if (player.infoChar.idhe == 4) {
                                dame -= array[i].a[1] * 50 / 100;
                            }
                        } else if (array[i].a[0] == 279) { // nha thông nha
                            dame += player.infoChar.hp * array[i].a[1] / 100;
                        }

                    } catch (Exception ex) {
                        Utlis.logError(Skill.class, ex , "Da say ra loi:\n" + ex.getMessage());
                    }

                }
            }
        }

        if (this.idTemplate == SkillTemplate.LOI_KIEM) {
            Skill skill = client.mChar.getSkillWithIdTemplate(SkillTemplate.TAM_TRUNG_OAN_THU_THUAT);
            if (skill != null) {
                int cX = skill.getChiSo( 92);
                if (cX != 0) {
                    dame = dame + (dame * cX / 100);
                }
            }
        } else if (this.idTemplate == SkillTemplate.ANH_PHONG_XA) {
            Skill skill = client.mChar.getSkillWithIdTemplate(SkillTemplate.SONG_THANG_LONG);
            if (skill != null) {
                int cX = skill.getChiSo( 92);
                if (cX != 0) {
                    dame = dame + (dame * cX / 100);
                }
            }
        } else if (this.idTemplate == SkillTemplate.AI_BOC_BO_THUAT) {
            Skill skill = client.mChar.getSkillWithIdTemplate(SkillTemplate.THUY_LONG_AN_THUAT);
            if (skill != null) {
                int cX = skill.getChiSo(92);
                if (cX != 0) {
                    dame = dame + (dame * cX / 100);
                }
            }
        } else if (this.idTemplate == SkillTemplate.HAO_HOA_CAU_THUAT) {
            Skill skill = client.mChar.getSkillWithIdTemplate(SkillTemplate.AM_SAT_THUAT);
            if (skill != null) {
                int cX = skill.getChiSo( 92);
                if (cX != 0) {
                    dame = dame + (dame * cX / 100);
                }
            }
        } else if (this.idTemplate == SkillTemplate.PHI_LOI_THAN_THUAT) {
            Skill skill = client.mChar.getSkillWithIdTemplate(SkillTemplate.THIEN_SAT_THUY_PHI);
            if (skill != null) {
                int cX = skill.getChiSo( 92);
                if (cX != 0) {
                    dame = dame + (dame * cX / 100);
                }
            }
        }
        boQuaNeTranh += client.mChar.getBoQuaNeTranh();
        boQuaNeTranh += client.mChar.getChinhXac();
        int neTranh = player.getNeTranh()-boQuaNeTranh;
        if(neTranh < 0) neTranh = 0;
        neTranh /= 100;
        boolean ne_Tranh = Utlis.randomBoolean(100, neTranh);
        if (ne_Tranh && neTranh > 0) {
            dame = 0;
        }
        return dame;
    }
    public int getChiSo(int... arrChiSo) {
        int c = 0;
        ItemOption[] array = getItemOption();
        if (arrChiSo == null) {
            for (int i = 0; array != null && i < array.length; i++) {
                if (array[i] != null) {
                    try {

                        c += array[i].a[1];
                    } catch (Exception ex) {

                    }
                }
            }
        } else {
            for (int j = 0; arrChiSo != null && j < arrChiSo.length; j++) {
                if (array != null) {
                    for (int i = 0; i < array.length; i++) {
                        if (array[i] != null) {
                            try {
                                if (array[i].a[0] == arrChiSo[j]) {
                                    c += array[i].a[1];
                                }
                            } catch (Exception ex) {

                            }
                        }
                    }
                }
            }
        }
        return c;
    }
}

/*
78: Tấn công: +0
89: Sát thương quái: +0
78: Tấn công: +50
89: Sát thương quái: +20
65: Chính xác: +0
73: Lôi độn: +0
89: Sát thương quái: +0
68: Gây suy yếu: +0
92: Cộng thêm: +0% sức tấn công của chiêu @
93: Cộng thêm: +0% sức tấn công của chiêu @
65: Chính xác: +50
73: Lôi độn: +200
89: Sát thương quái: +20
68: Gây suy yếu: +2
92: Cộng thêm: +3% sức tấn công của chiêu @
93: Cộng thêm: +3% sức tấn công của chiêu @
65: Chính xác: +55
73: Lôi độn: +230
89: Sát thương quái: +40
68: Gây suy yếu: +4
92: Cộng thêm: +6% sức tấn công của chiêu @
93: Cộng thêm: +6% sức tấn công của chiêu @
65: Chính xác: +60
73: Lôi độn: +260
89: Sát thương quái: +60
68: Gây suy yếu: +6
92: Cộng thêm: +9% sức tấn công của chiêu @
93: Cộng thêm: +9% sức tấn công của chiêu @
65: Chính xác: +65
73: Lôi độn: +290
89: Sát thương quái: +80
68: Gây suy yếu: +8
92: Cộng thêm: +12% sức tấn công của chiêu @
93: Cộng thêm: +12% sức tấn công của chiêu @
65: Chính xác: +70
73: Lôi độn: +320
89: Sát thương quái: +100
68: Gây suy yếu: +10
92: Cộng thêm: +15% sức tấn công của chiêu @
93: Cộng thêm: +15% sức tấn công của chiêu @
65: Chính xác: +75
73: Lôi độn: +350
89: Sát thương quái: +120
68: Gây suy yếu: +12
92: Cộng thêm: +18% sức tấn công của chiêu @
93: Cộng thêm: +18% sức tấn công của chiêu @
65: Chính xác: +80
73: Lôi độn: +380
89: Sát thương quái: +140
68: Gây suy yếu: +14
92: Cộng thêm: +21% sức tấn công của chiêu @
93: Cộng thêm: +21% sức tấn công của chiêu @
65: Chính xác: +85
73: Lôi độn: +410
89: Sát thương quái: +160
68: Gây suy yếu: +16
92: Cộng thêm: +24% sức tấn công của chiêu @
93: Cộng thêm: +24% sức tấn công của chiêu @
65: Chính xác: +90
73: Lôi độn: +440
89: Sát thương quái: +180
68: Gây suy yếu: +18
92: Cộng thêm: +27% sức tấn công của chiêu @
93: Cộng thêm: +27% sức tấn công của chiêu @
65: Chính xác: +95
73: Lôi độn: +470
89: Sát thương quái: +200
68: Gây suy yếu: +20
92: Cộng thêm: +30% sức tấn công của chiêu @
93: Cộng thêm: +30% sức tấn công của chiêu @
175: Hp:+0
67: Phản đòn: +0%
91: Tốc độ di chuyển: +0
139: Tương khắc lên hệ Thổ: +0
175: Hp:+150
67: Phản đòn: +1%
91: Tốc độ di chuyển: +10
139: Tương khắc lên hệ Thổ: +40
175: Hp:+300
67: Phản đòn: +2%
91: Tốc độ di chuyển: +20
139: Tương khắc lên hệ Thổ: +50
175: Hp:+450
67: Phản đòn: +3%
91: Tốc độ di chuyển: +30
139: Tương khắc lên hệ Thổ: +60
175: Hp:+600
67: Phản đòn: +4%
91: Tốc độ di chuyển: +40
139: Tương khắc lên hệ Thổ: +70
175: Hp:+750
67: Phản đòn: +5%
91: Tốc độ di chuyển: +50
139: Tương khắc lên hệ Thổ: +80
175: Hp:+900
67: Phản đòn: +6%
91: Tốc độ di chuyển: +60
139: Tương khắc lên hệ Thổ: +90
175: Hp:+1.050
67: Phản đòn: +7%
91: Tốc độ di chuyển: +70
139: Tương khắc lên hệ Thổ: +100
175: Hp:+1.200
67: Phản đòn: +8%
91: Tốc độ di chuyển: +80
139: Tương khắc lên hệ Thổ: +110
175: Hp:+1.350
67: Phản đòn: +9%
91: Tốc độ di chuyển: +90
139: Tương khắc lên hệ Thổ: +120
175: Hp:+1.500
67: Phản đòn: +10%
91: Tốc độ di chuyển: +100
139: Tương khắc lên hệ Thổ: +130
65: Chính xác: +100
73: Lôi độn: +500
89: Sát thương quái: +220
68: Gây suy yếu: +22
92: Cộng thêm: +33% sức tấn công của chiêu @
93: Cộng thêm: +33% sức tấn công của chiêu @
65: Chính xác: +105
73: Lôi độn: +530
89: Sát thương quái: +240
68: Gây suy yếu: +24
92: Cộng thêm: +36% sức tấn công của chiêu @
93: Cộng thêm: +36% sức tấn công của chiêu @
65: Chính xác: +100
77: Thủy độn: +500
89: Sát thương quái: +220
70: Gây làm chậm: +22
92: Cộng thêm: +33% sức tấn công của chiêu @
93: Cộng thêm: +33% sức tấn công của chiêu @
65: Chính xác: +105
77: Thủy độn: +530
89: Sát thương quái: +240
70: Gây làm chậm: +24
92: Cộng thêm: +36% sức tấn công của chiêu @
93: Cộng thêm: +36% sức tấn công của chiêu @
65: Chính xác: +110
77: Thủy độn: +560
89: Sát thương quái: +260
70: Gây làm chậm: +26
92: Cộng thêm: +39% sức tấn công của chiêu @
93: Cộng thêm: +39% sức tấn công của chiêu @
65: Chính xác: +115
77: Thủy độn: +590
89: Sát thương quái: +280
70: Gây làm chậm: +28
92: Cộng thêm: +42% sức tấn công của chiêu @
93: Cộng thêm: +42% sức tấn công của chiêu @
65: Chính xác: +120
77: Thủy độn: +620
89: Sát thương quái: +300
70: Gây làm chậm: +30
92: Cộng thêm: +45% sức tấn công của chiêu @
93: Cộng thêm: +45% sức tấn công của chiêu @
65: Chính xác: +125
77: Thủy độn: +650
89: Sát thương quái: +320
70: Gây làm chậm: +32
92: Cộng thêm: +48% sức tấn công của chiêu @
93: Cộng thêm: +48% sức tấn công của chiêu @
65: Chính xác: +130
77: Thủy độn: +680
89: Sát thương quái: +340
70: Gây làm chậm: +34
92: Cộng thêm: +51% sức tấn công của chiêu @
93: Cộng thêm: +51% sức tấn công của chiêu @
65: Chính xác: +135
77: Thủy độn: +710
89: Sát thương quái: +360
70: Gây làm chậm: +36
92: Cộng thêm: +54% sức tấn công của chiêu @
93: Cộng thêm: +54% sức tấn công của chiêu @
65: Chính xác: +140
77: Thủy độn: +740
89: Sát thương quái: +380
70: Gây làm chậm: +38
92: Cộng thêm: +57% sức tấn công của chiêu @
93: Cộng thêm: +57% sức tấn công của chiêu @
65: Chính xác: +150
77: Thủy độn: +800
89: Sát thương quái: +400
70: Gây làm chậm: +40
92: Cộng thêm: +60% sức tấn công của chiêu @
93: Cộng thêm: +60% sức tấn công của chiêu @
65: Chính xác: +155
77: Thủy độn: +1.020
89: Sát thương quái: +460
70: Gây làm chậm: +51
78: Tấn công: +0
87: Di chuyển nhanh đến mục tiêu
103: Làm tê liệt đối phương: duy trì 0 giây
78: Tấn công: +1.600
87: Di chuyển nhanh đến mục tiêu
103: Làm tê liệt đối phương: duy trì 600 giây
78: Tấn công: +1.700
87: Di chuyển nhanh đến mục tiêu
103: Làm tê liệt đối phương: duy trì 700 giây
78: Tấn công: +1.800
87: Di chuyển nhanh đến mục tiêu
103: Làm tê liệt đối phương: duy trì 800 giây
78: Tấn công: +1.900
87: Di chuyển nhanh đến mục tiêu
103: Làm tê liệt đối phương: duy trì 900 giây
78: Tấn công: +2.000
87: Di chuyển nhanh đến mục tiêu
103: Làm tê liệt đối phương: duy trì 1.000 giây
78: Tấn công: +2.100
87: Di chuyển nhanh đến mục tiêu
103: Làm tê liệt đối phương: duy trì 1.100 giây
78: Tấn công: +2.200
87: Di chuyển nhanh đến mục tiêu
103: Làm tê liệt đối phương: duy trì 1.200 giây
78: Tấn công: +2.300
87: Di chuyển nhanh đến mục tiêu
103: Làm tê liệt đối phương: duy trì 1.300 giây
78: Tấn công: +2.400
87: Di chuyển nhanh đến mục tiêu
103: Làm tê liệt đối phương: duy trì 1.400 giây
78: Tấn công: +2.500
87: Di chuyển nhanh đến mục tiêu
103: Làm tê liệt đối phương: duy trì 1.500 giây
65: Chính xác: +110
73: Lôi độn: +560
89: Sát thương quái: +260
68: Gây suy yếu: +26
92: Cộng thêm: +39% sức tấn công của chiêu @
93: Cộng thêm: +39% sức tấn công của chiêu @
65: Chính xác: +115
73: Lôi độn: +590
89: Sát thương quái: +280
68: Gây suy yếu: +28
92: Cộng thêm: +42% sức tấn công của chiêu @
93: Cộng thêm: +42% sức tấn công của chiêu @
65: Chính xác: +0
73: Lôi độn: +0
89: Sát thương quái: +0
68: Gây suy yếu: +0
65: Chính xác: +105
73: Lôi độn: +820
89: Sát thương quái: +360
68: Gây suy yếu: +41
65: Chính xác: +110
73: Lôi độn: +840
89: Sát thương quái: +370
68: Gây suy yếu: +42
65: Chính xác: +115
73: Lôi độn: +860
89: Sát thương quái: +380
68: Gây suy yếu: +43
65: Chính xác: +120
73: Lôi độn: +880
89: Sát thương quái: +390
68: Gây suy yếu: +44
65: Chính xác: +125
73: Lôi độn: +900
89: Sát thương quái: +400
68: Gây suy yếu: +45
65: Chính xác: +130
73: Lôi độn: +920
89: Sát thương quái: +410
68: Gây suy yếu: +46
65: Chính xác: +135
73: Lôi độn: +940
89: Sát thương quái: +420
68: Gây suy yếu: +47
65: Chính xác: +140
73: Lôi độn: +960
89: Sát thương quái: +430
68: Gây suy yếu: +48
65: Chính xác: +145
73: Lôi độn: +980
89: Sát thương quái: +440
68: Gây suy yếu: +49
65: Chính xác: +150
73: Lôi độn: +1.000
89: Sát thương quái: +450
68: Gây suy yếu: +50
65: Chính xác: +120
73: Lôi độn: +620
89: Sát thương quái: +300
68: Gây suy yếu: +30
92: Cộng thêm: +45% sức tấn công của chiêu @
93: Cộng thêm: +45% sức tấn công của chiêu @
65: Chính xác: +125
73: Lôi độn: +650
89: Sát thương quái: +320
68: Gây suy yếu: +32
92: Cộng thêm: +48% sức tấn công của chiêu @
93: Cộng thêm: +48% sức tấn công của chiêu @
65: Chính xác: +0
74: Thổ độn: +0
89: Sát thương quái: +0
69: Gây trúng độc: +0
92: Cộng thêm: +0% sức tấn công của chiêu @
93: Cộng thêm: +0% sức tấn công của chiêu @
65: Chính xác: +50
74: Thổ độn: +200
89: Sát thương quái: +20
69: Gây trúng độc: +2
92: Cộng thêm: +3% sức tấn công của chiêu @
93: Cộng thêm: +3% sức tấn công của chiêu @
65: Chính xác: +55
74: Thổ độn: +230
89: Sát thương quái: +40
69: Gây trúng độc: +4
92: Cộng thêm: +6% sức tấn công của chiêu @
93: Cộng thêm: +6% sức tấn công của chiêu @
65: Chính xác: +60
74: Thổ độn: +260
89: Sát thương quái: +60
69: Gây trúng độc: +6
92: Cộng thêm: +9% sức tấn công của chiêu @
93: Cộng thêm: +9% sức tấn công của chiêu @
65: Chính xác: +65
74: Thổ độn: +290
89: Sát thương quái: +80
69: Gây trúng độc: +8
92: Cộng thêm: +12% sức tấn công của chiêu @
93: Cộng thêm: +12% sức tấn công của chiêu @
65: Chính xác: +70
74: Thổ độn: +320
89: Sát thương quái: +100
69: Gây trúng độc: +10
92: Cộng thêm: +15% sức tấn công của chiêu @
93: Cộng thêm: +15% sức tấn công của chiêu @
65: Chính xác: +75
74: Thổ độn: +350
89: Sát thương quái: +120
69: Gây trúng độc: +12
92: Cộng thêm: +18% sức tấn công của chiêu @
93: Cộng thêm: +18% sức tấn công của chiêu @
65: Chính xác: +80
74: Thổ độn: +380
89: Sát thương quái: +140
69: Gây trúng độc: +14
92: Cộng thêm: +21% sức tấn công của chiêu @
93: Cộng thêm: +21% sức tấn công của chiêu @
65: Chính xác: +85
74: Thổ độn: +410
89: Sát thương quái: +160
69: Gây trúng độc: +16
92: Cộng thêm: +24% sức tấn công của chiêu @
93: Cộng thêm: +24% sức tấn công của chiêu @
65: Chính xác: +90
74: Thổ độn: +440
89: Sát thương quái: +180
69: Gây trúng độc: +18
92: Cộng thêm: +27% sức tấn công của chiêu @
93: Cộng thêm: +27% sức tấn công của chiêu @
65: Chính xác: +95
74: Thổ độn: +470
89: Sát thương quái: +200
69: Gây trúng độc: +20
92: Cộng thêm: +30% sức tấn công của chiêu @
93: Cộng thêm: +30% sức tấn công của chiêu @
63: Chí mạng: +0
65: Chính xác: +0
91: Tốc độ di chuyển: +0
140: Tương khắc lên hệ Thủy: +0
63: Chí mạng: +10
65: Chính xác: +10
91: Tốc độ di chuyển: +5
140: Tương khắc lên hệ Thủy: +40
63: Chí mạng: +20
65: Chính xác: +20
91: Tốc độ di chuyển: +10
140: Tương khắc lên hệ Thủy: +50
63: Chí mạng: +30
65: Chính xác: +30
91: Tốc độ di chuyển: +15
140: Tương khắc lên hệ Thủy: +60
63: Chí mạng: +40
65: Chính xác: +40
91: Tốc độ di chuyển: +20
140: Tương khắc lên hệ Thủy: +70
63: Chí mạng: +50
65: Chính xác: +50
91: Tốc độ di chuyển: +25
140: Tương khắc lên hệ Thủy: +80
63: Chí mạng: +60
65: Chính xác: +60
91: Tốc độ di chuyển: +30
140: Tương khắc lên hệ Thủy: +90
63: Chí mạng: +70
65: Chính xác: +70
91: Tốc độ di chuyển: +35
140: Tương khắc lên hệ Thủy: +100
63: Chí mạng: +80
65: Chính xác: +80
91: Tốc độ di chuyển: +40
140: Tương khắc lên hệ Thủy: +110
63: Chí mạng: +90
65: Chính xác: +90
91: Tốc độ di chuyển: +45
140: Tương khắc lên hệ Thủy: +120
63: Chí mạng: +100
65: Chính xác: +100
91: Tốc độ di chuyển: +50
140: Tương khắc lên hệ Thủy: +130
65: Chính xác: +130
73: Lôi độn: +680
89: Sát thương quái: +340
68: Gây suy yếu: +34
92: Cộng thêm: +51% sức tấn công của chiêu @
93: Cộng thêm: +51% sức tấn công của chiêu @
65: Chính xác: +135
73: Lôi độn: +710
89: Sát thương quái: +360
68: Gây suy yếu: +36
92: Cộng thêm: +54% sức tấn công của chiêu @
93: Cộng thêm: +54% sức tấn công của chiêu @
65: Chính xác: +160
77: Thủy độn: +1.040
89: Sát thương quái: +470
70: Gây làm chậm: +52
65: Chính xác: +165
77: Thủy độn: +1.060
89: Sát thương quái: +480
70: Gây làm chậm: +53
65: Chính xác: +170
77: Thủy độn: +1.080
89: Sát thương quái: +490
70: Gây làm chậm: +54
65: Chính xác: +175
77: Thủy độn: +1.100
89: Sát thương quái: +500
70: Gây làm chậm: +55
65: Chính xác: +180
77: Thủy độn: +1.120
89: Sát thương quái: +510
70: Gây làm chậm: +56
65: Chính xác: +185
77: Thủy độn: +1.140
89: Sát thương quái: +520
70: Gây làm chậm: +57
65: Chính xác: +190
77: Thủy độn: +1.160
89: Sát thương quái: +530
70: Gây làm chậm: +58
65: Chính xác: +195
77: Thủy độn: +1.180
89: Sát thương quái: +540
70: Gây làm chậm: +59
65: Chính xác: +200
77: Thủy độn: +1.200
89: Sát thương quái: +550
70: Gây làm chậm: +60
65: Chính xác: +100
76: Hỏa độn: +500
89: Sát thương quái: +220
71: Gây bỏng:+22
92: Cộng thêm: +33% sức tấn công của chiêu @
93: Cộng thêm: +33% sức tấn công của chiêu @
65: Chính xác: +105
76: Hỏa độn: +530
89: Sát thương quái: +240
71: Gây bỏng:+24
92: Cộng thêm: +36% sức tấn công của chiêu @
93: Cộng thêm: +36% sức tấn công của chiêu @
78: Tấn công: +0
181: Giảm kháng tất cả: -0 (duy trì 10 giây)
182: Triệt tiêu né tránh và giảm trừ sát thương: -0 (duy trì 10 giây)
78: Tấn công: +1.000
181: Giảm kháng tất cả: -120 (duy trì 10 giây)
182: Triệt tiêu né tránh và giảm trừ sát thương: -55 (duy trì 10 giây)
78: Tấn công: +1.100
181: Giảm kháng tất cả: -140 (duy trì 10 giây)
182: Triệt tiêu né tránh và giảm trừ sát thương: -60 (duy trì 10 giây)
78: Tấn công: +1.200
181: Giảm kháng tất cả: -160 (duy trì 10 giây)
182: Triệt tiêu né tránh và giảm trừ sát thương: -65 (duy trì 10 giây)
78: Tấn công: +1.300
181: Giảm kháng tất cả: -180 (duy trì 10 giây)
182: Triệt tiêu né tránh và giảm trừ sát thương: -70 (duy trì 10 giây)
78: Tấn công: +1.400
181: Giảm kháng tất cả: -200 (duy trì 10 giây)
182: Triệt tiêu né tránh và giảm trừ sát thương: -75 (duy trì 10 giây)
78: Tấn công: +1.500
181: Giảm kháng tất cả: -220 (duy trì 10 giây)
182: Triệt tiêu né tránh và giảm trừ sát thương: -80 (duy trì 10 giây)
78: Tấn công: +1.600
181: Giảm kháng tất cả: -240 (duy trì 10 giây)
182: Triệt tiêu né tránh và giảm trừ sát thương: -85 (duy trì 10 giây)
78: Tấn công: +1.700
181: Giảm kháng tất cả: -260 (duy trì 10 giây)
182: Triệt tiêu né tránh và giảm trừ sát thương: -90 (duy trì 10 giây)
78: Tấn công: +1.800
181: Giảm kháng tất cả: -280 (duy trì 10 giây)
182: Triệt tiêu né tránh và giảm trừ sát thương: -95 (duy trì 10 giây)
78: Tấn công: +1.900
181: Giảm kháng tất cả: -300 (duy trì 10 giây)
182: Triệt tiêu né tránh và giảm trừ sát thương: -100 (duy trì 10 giây)
65: Chính xác: +155
74: Thổ độn: +1.020
89: Sát thương quái: +460
69: Gây trúng độc: +51
65: Chính xác: +160
74: Thổ độn: +1.040
89: Sát thương quái: +470
69: Gây trúng độc: +52
65: Chính xác: +0
74: Thổ độn: +0
89: Sát thương quái: +0
69: Gây trúng độc: +0
65: Chính xác: +105
74: Thổ độn: +820
89: Sát thương quái: +360
69: Gây trúng độc: +41
65: Chính xác: +110
74: Thổ độn: +840
89: Sát thương quái: +370
69: Gây trúng độc: +42
65: Chính xác: +115
74: Thổ độn: +860
89: Sát thương quái: +380
69: Gây trúng độc: +43
65: Chính xác: +120
74: Thổ độn: +880
89: Sát thương quái: +390
69: Gây trúng độc: +44
65: Chính xác: +125
74: Thổ độn: +900
89: Sát thương quái: +400
69: Gây trúng độc: +45
65: Chính xác: +130
74: Thổ độn: +920
89: Sát thương quái: +410
69: Gây trúng độc: +46
65: Chính xác: +135
74: Thổ độn: +940
89: Sát thương quái: +420
69: Gây trúng độc: +47
65: Chính xác: +140
74: Thổ độn: +960
89: Sát thương quái: +430
69: Gây trúng độc: +48
65: Chính xác: +145
74: Thổ độn: +980
89: Sát thương quái: +440
69: Gây trúng độc: +49
65: Chính xác: +150
74: Thổ độn: +1.000
89: Sát thương quái: +450
69: Gây trúng độc: +50
65: Chính xác: +110
76: Hỏa độn: +560
89: Sát thương quái: +260
71: Gây bỏng:+26
92: Cộng thêm: +39% sức tấn công của chiêu @
93: Cộng thêm: +39% sức tấn công của chiêu @
65: Chính xác: +115
76: Hỏa độn: +590
89: Sát thương quái: +280
71: Gây bỏng:+28
92: Cộng thêm: +42% sức tấn công của chiêu @
93: Cộng thêm: +42% sức tấn công của chiêu @
65: Chính xác: +0
77: Thủy độn: +0
89: Sát thương quái: +0
70: Gây làm chậm: +0
92: Cộng thêm: +0% sức tấn công của chiêu @
93: Cộng thêm: +0% sức tấn công của chiêu @
65: Chính xác: +50
77: Thủy độn: +200
89: Sát thương quái: +20
70: Gây làm chậm: +2
92: Cộng thêm: +3% sức tấn công của chiêu @
93: Cộng thêm: +3% sức tấn công của chiêu @
65: Chính xác: +55
77: Thủy độn: +230
89: Sát thương quái: +40
70: Gây làm chậm: +4
92: Cộng thêm: +6% sức tấn công của chiêu @
93: Cộng thêm: +6% sức tấn công của chiêu @
65: Chính xác: +60
77: Thủy độn: +260
89: Sát thương quái: +60
70: Gây làm chậm: +6
92: Cộng thêm: +9% sức tấn công của chiêu @
93: Cộng thêm: +9% sức tấn công của chiêu @
65: Chính xác: +65
77: Thủy độn: +290
89: Sát thương quái: +80
70: Gây làm chậm: +8
92: Cộng thêm: +12% sức tấn công của chiêu @
93: Cộng thêm: +12% sức tấn công của chiêu @
65: Chính xác: +70
77: Thủy độn: +320
89: Sát thương quái: +100
70: Gây làm chậm: +10
92: Cộng thêm: +15% sức tấn công của chiêu @
93: Cộng thêm: +15% sức tấn công của chiêu @
65: Chính xác: +75
77: Thủy độn: +350
89: Sát thương quái: +120
70: Gây làm chậm: +12
92: Cộng thêm: +18% sức tấn công của chiêu @
93: Cộng thêm: +18% sức tấn công của chiêu @
65: Chính xác: +80
77: Thủy độn: +380
89: Sát thương quái: +140
70: Gây làm chậm: +14
92: Cộng thêm: +21% sức tấn công của chiêu @
93: Cộng thêm: +21% sức tấn công của chiêu @
65: Chính xác: +85
77: Thủy độn: +410
89: Sát thương quái: +160
70: Gây làm chậm: +16
92: Cộng thêm: +24% sức tấn công của chiêu @
93: Cộng thêm: +24% sức tấn công của chiêu @
65: Chính xác: +90
77: Thủy độn: +440
89: Sát thương quái: +180
70: Gây làm chậm: +18
92: Cộng thêm: +27% sức tấn công của chiêu @
93: Cộng thêm: +27% sức tấn công của chiêu @
65: Chính xác: +95
77: Thủy độn: +470
89: Sát thương quái: +200
70: Gây làm chậm: +20
92: Cộng thêm: +30% sức tấn công của chiêu @
93: Cộng thêm: +30% sức tấn công của chiêu @
66: Tăng kinh nghiệm đánh quái: +0%
79: Hp tối đa: +0%
91: Tốc độ di chuyển: +0
104: Hỗ trợ đồng đội tăng kinh nghiệm khi đánh quái: +0%
141: Tương khắc lên hệ Hỏa: +0
66: Tăng kinh nghiệm đánh quái: +8%
104: Hỗ trợ đồng đội tăng kinh nghiệm khi đánh quái: +6%
79: Hp tối đa: +15%
91: Tốc độ di chuyển: +5
141: Tương khắc lên hệ Hỏa: +40
66: Tăng kinh nghiệm đánh quái: +11%
104: Hỗ trợ đồng đội tăng kinh nghiệm khi đánh quái: +7%
79: Hp tối đa: +20%
91: Tốc độ di chuyển: +10
141: Tương khắc lên hệ Hỏa: +50
66: Tăng kinh nghiệm đánh quái: +14%
104: Hỗ trợ đồng đội tăng kinh nghiệm khi đánh quái: +8%
79: Hp tối đa: +25%
91: Tốc độ di chuyển: +15
141: Tương khắc lên hệ Hỏa: +60
66: Tăng kinh nghiệm đánh quái: +17%
104: Hỗ trợ đồng đội tăng kinh nghiệm khi đánh quái: +9%
79: Hp tối đa: +30%
91: Tốc độ di chuyển: +20
141: Tương khắc lên hệ Hỏa: +70
66: Tăng kinh nghiệm đánh quái: +20%
104: Hỗ trợ đồng đội tăng kinh nghiệm khi đánh quái: +10%
79: Hp tối đa: +35%
91: Tốc độ di chuyển: +25
141: Tương khắc lên hệ Hỏa: +80
66: Tăng kinh nghiệm đánh quái: +23%
104: Hỗ trợ đồng đội tăng kinh nghiệm khi đánh quái: +11%
79: Hp tối đa: +40%
91: Tốc độ di chuyển: +30
141: Tương khắc lên hệ Hỏa: +90
66: Tăng kinh nghiệm đánh quái: +26%
104: Hỗ trợ đồng đội tăng kinh nghiệm khi đánh quái: +12%
79: Hp tối đa: +45%
91: Tốc độ di chuyển: +35
141: Tương khắc lên hệ Hỏa: +100
66: Tăng kinh nghiệm đánh quái: +29%
104: Hỗ trợ đồng đội tăng kinh nghiệm khi đánh quái: +13%
79: Hp tối đa: +50%
91: Tốc độ di chuyển: +40
141: Tương khắc lên hệ Hỏa: +110
66: Tăng kinh nghiệm đánh quái: +32%
104: Hỗ trợ đồng đội tăng kinh nghiệm khi đánh quái: +14%
79: Hp tối đa: +55%
91: Tốc độ di chuyển: +45
141: Tương khắc lên hệ Hỏa: +120
66: Tăng kinh nghiệm đánh quái: +35%
104: Hỗ trợ đồng đội tăng kinh nghiệm khi đánh quái: +15%
79: Hp tối đa: +60%
91: Tốc độ di chuyển: +50
141: Tương khắc lên hệ Hỏa: +130
65: Chính xác: +140
73: Lôi độn: +740
89: Sát thương quái: +380
68: Gây suy yếu: +38
92: Cộng thêm: +57% sức tấn công của chiêu @
93: Cộng thêm: +57% sức tấn công của chiêu @
65: Chính xác: +150
73: Lôi độn: +800
89: Sát thương quái: +400
68: Gây suy yếu: +40
92: Cộng thêm: +60% sức tấn công của chiêu @
93: Cộng thêm: +60% sức tấn công của chiêu @
65: Chính xác: +120
76: Hỏa độn: +620
89: Sát thương quái: +300
71: Gây bỏng:+30
92: Cộng thêm: +45% sức tấn công của chiêu @
93: Cộng thêm: +45% sức tấn công của chiêu @
65: Chính xác: +125
76: Hỏa độn: +650
89: Sát thương quái: +320
71: Gây bỏng:+32
92: Cộng thêm: +48% sức tấn công của chiêu @
93: Cộng thêm: +48% sức tấn công của chiêu @
65: Chính xác: +130
76: Hỏa độn: +680
89: Sát thương quái: +340
71: Gây bỏng:+34
92: Cộng thêm: +51% sức tấn công của chiêu @
93: Cộng thêm: +51% sức tấn công của chiêu @
65: Chính xác: +135
76: Hỏa độn: +710
89: Sát thương quái: +360
71: Gây bỏng:+36
92: Cộng thêm: +54% sức tấn công của chiêu @
93: Cộng thêm: +54% sức tấn công của chiêu @
65: Chính xác: +140
76: Hỏa độn: +740
89: Sát thương quái: +380
71: Gây bỏng:+38
92: Cộng thêm: +57% sức tấn công của chiêu @
93: Cộng thêm: +57% sức tấn công của chiêu @
65: Chính xác: +150
76: Hỏa độn: +800
89: Sát thương quái: +400
71: Gây bỏng:+40
92: Cộng thêm: +60% sức tấn công của chiêu @
93: Cộng thêm: +60% sức tấn công của chiêu @
65: Chính xác: +155
76: Hỏa độn: +1.020
89: Sát thương quái: +460
71: Gây bỏng:+51
65: Chính xác: +160
76: Hỏa độn: +1.040
89: Sát thương quái: +470
71: Gây bỏng:+52
65: Chính xác: +165
76: Hỏa độn: +1.060
89: Sát thương quái: +480
71: Gây bỏng:+53
65: Chính xác: +170
76: Hỏa độn: +1.080
89: Sát thương quái: +490
71: Gây bỏng:+54
65: Chính xác: +175
76: Hỏa độn: +1.100
89: Sát thương quái: +500
71: Gây bỏng:+55
96: Mỗi nữa giây phục hồi Hp cho mình và đồng đội: +0 (duy trì 5 giây)
96: Mỗi nữa giây phục hồi Hp cho mình và đồng đội: +25 (duy trì 5 giây)
96: Mỗi nữa giây phục hồi Hp cho mình và đồng đội: +50 (duy trì 5 giây)
96: Mỗi nữa giây phục hồi Hp cho mình và đồng đội: +75 (duy trì 5 giây)
96: Mỗi nữa giây phục hồi Hp cho mình và đồng đội: +100 (duy trì 5 giây)
96: Mỗi nữa giây phục hồi Hp cho mình và đồng đội: +125 (duy trì 5 giây)
96: Mỗi nữa giây phục hồi Hp cho mình và đồng đội: +150 (duy trì 5 giây)
96: Mỗi nữa giây phục hồi Hp cho mình và đồng đội: +175 (duy trì 5 giây)
96: Mỗi nữa giây phục hồi Hp cho mình và đồng đội: +200 (duy trì 5 giây)
96: Mỗi nữa giây phục hồi Hp cho mình và đồng đội: +225 (duy trì 5 giây)
96: Mỗi nữa giây phục hồi Hp cho mình và đồng đội: +250 (duy trì 5 giây)
65: Chính xác: +180
76: Hỏa độn: +1.120
89: Sát thương quái: +510
71: Gây bỏng:+56
65: Chính xác: +185
76: Hỏa độn: +1.140
89: Sát thương quái: +520
71: Gây bỏng:+57
65: Chính xác: +0
77: Thủy độn: +0
89: Sát thương quái: +0
70: Gây làm chậm: +0
65: Chính xác: +105
77: Thủy độn: +820
89: Sát thương quái: +360
70: Gây làm chậm: +41
65: Chính xác: +110
77: Thủy độn: +840
89: Sát thương quái: +370
70: Gây làm chậm: +42
65: Chính xác: +115
77: Thủy độn: +860
89: Sát thương quái: +380
70: Gây làm chậm: +43
65: Chính xác: +120
77: Thủy độn: +880
89: Sát thương quái: +390
70: Gây làm chậm: +44
65: Chính xác: +125
77: Thủy độn: +900
89: Sát thương quái: +400
70: Gây làm chậm: +45
65: Chính xác: +130
77: Thủy độn: +920
89: Sát thương quái: +410
70: Gây làm chậm: +46
65: Chính xác: +135
77: Thủy độn: +940
89: Sát thương quái: +420
70: Gây làm chậm: +47
65: Chính xác: +140
77: Thủy độn: +960
89: Sát thương quái: +430
70: Gây làm chậm: +48
65: Chính xác: +145
77: Thủy độn: +980
89: Sát thương quái: +440
70: Gây làm chậm: +49
65: Chính xác: +150
77: Thủy độn: +1.000
89: Sát thương quái: +450
70: Gây làm chậm: +50
65: Chính xác: +190
76: Hỏa độn: +1.160
89: Sát thương quái: +530
71: Gây bỏng:+58
65: Chính xác: +195
76: Hỏa độn: +1.180
89: Sát thương quái: +540
71: Gây bỏng:+59
65: Chính xác: +0
76: Hỏa độn: +0
89: Sát thương quái: +0
71: Gây bỏng:+0
92: Cộng thêm: +0% sức tấn công của chiêu @
93: Cộng thêm: +0% sức tấn công của chiêu @
65: Chính xác: +50
76: Hỏa độn: +200
89: Sát thương quái: +20
71: Gây bỏng:+2
92: Cộng thêm: +3% sức tấn công của chiêu @
93: Cộng thêm: +3% sức tấn công của chiêu @
65: Chính xác: +55
76: Hỏa độn: +230
89: Sát thương quái: +40
71: Gây bỏng:+4
92: Cộng thêm: +6% sức tấn công của chiêu @
93: Cộng thêm: +6% sức tấn công của chiêu @
65: Chính xác: +60
76: Hỏa độn: +260
89: Sát thương quái: +60
71: Gây bỏng:+6
92: Cộng thêm: +9% sức tấn công của chiêu @
93: Cộng thêm: +9% sức tấn công của chiêu @
65: Chính xác: +65
76: Hỏa độn: +290
89: Sát thương quái: +80
71: Gây bỏng:+8
92: Cộng thêm: +12% sức tấn công của chiêu @
93: Cộng thêm: +12% sức tấn công của chiêu @
65: Chính xác: +70
76: Hỏa độn: +320
89: Sát thương quái: +100
71: Gây bỏng:+10
92: Cộng thêm: +15% sức tấn công của chiêu @
93: Cộng thêm: +15% sức tấn công của chiêu @
65: Chính xác: +75
76: Hỏa độn: +350
89: Sát thương quái: +120
71: Gây bỏng:+12
92: Cộng thêm: +18% sức tấn công của chiêu @
93: Cộng thêm: +18% sức tấn công của chiêu @
65: Chính xác: +80
76: Hỏa độn: +380
89: Sát thương quái: +140
71: Gây bỏng:+14
92: Cộng thêm: +21% sức tấn công của chiêu @
93: Cộng thêm: +21% sức tấn công của chiêu @
65: Chính xác: +85
76: Hỏa độn: +410
89: Sát thương quái: +160
71: Gây bỏng:+16
92: Cộng thêm: +24% sức tấn công của chiêu @
93: Cộng thêm: +24% sức tấn công của chiêu @
65: Chính xác: +90
76: Hỏa độn: +440
89: Sát thương quái: +180
71: Gây bỏng:+18
92: Cộng thêm: +27% sức tấn công của chiêu @
93: Cộng thêm: +27% sức tấn công của chiêu @
65: Chính xác: +95
76: Hỏa độn: +470
89: Sát thương quái: +200
71: Gây bỏng:+20
92: Cộng thêm: +30% sức tấn công của chiêu @
93: Cộng thêm: +30% sức tấn công của chiêu @
80: Mp tối đa: +0%
81: Kháng tất cả: +0
91: Tốc độ di chuyển: +0
142: Tương khắc lên hệ Phong: +0
80: Mp tối đa: +5%
81: Kháng tất cả: +10
91: Tốc độ di chuyển: +5
142: Tương khắc lên hệ Phong: +40
80: Mp tối đa: +10%
81: Kháng tất cả: +20
91: Tốc độ di chuyển: +10
142: Tương khắc lên hệ Phong: +50
80: Mp tối đa: +15%
81: Kháng tất cả: +30
91: Tốc độ di chuyển: +15
142: Tương khắc lên hệ Phong: +60
80: Mp tối đa: +20%
81: Kháng tất cả: +40
91: Tốc độ di chuyển: +20
142: Tương khắc lên hệ Phong: +70
80: Mp tối đa: +25%
81: Kháng tất cả: +50
91: Tốc độ di chuyển: +25
142: Tương khắc lên hệ Phong: +80
80: Mp tối đa: +30%
81: Kháng tất cả: +60
91: Tốc độ di chuyển: +30
142: Tương khắc lên hệ Phong: +90
80: Mp tối đa: +35%
81: Kháng tất cả: +70
91: Tốc độ di chuyển: +35
142: Tương khắc lên hệ Phong: +100
80: Mp tối đa: +40%
81: Kháng tất cả: +80
91: Tốc độ di chuyển: +40
142: Tương khắc lên hệ Phong: +110
80: Mp tối đa: +45%
81: Kháng tất cả: +90
91: Tốc độ di chuyển: +45
142: Tương khắc lên hệ Phong: +120
80: Mp tối đa: +50%
81: Kháng tất cả: +100
91: Tốc độ di chuyển: +50
142: Tương khắc lên hệ Phong: +130
65: Chính xác: +155
73: Lôi độn: +1.020
89: Sát thương quái: +460
68: Gây suy yếu: +51
65: Chính xác: +160
73: Lôi độn: +1.040
89: Sát thương quái: +470
68: Gây suy yếu: +52
65: Chính xác: +200
76: Hỏa độn: +1.200
89: Sát thương quái: +550
71: Gây bỏng:+60
65: Chính xác: +100
75: Phong độn: +500
89: Sát thương quái: +220
72: Gây choáng: +22
92: Cộng thêm: +33% sức tấn công của chiêu @
93: Cộng thêm: +33% sức tấn công của chiêu @
65: Chính xác: +105
75: Phong độn: +530
89: Sát thương quái: +240
72: Gây choáng: +24
92: Cộng thêm: +36% sức tấn công của chiêu @
93: Cộng thêm: +36% sức tấn công của chiêu @
65: Chính xác: +110
75: Phong độn: +560
89: Sát thương quái: +260
72: Gây choáng: +26
92: Cộng thêm: +39% sức tấn công của chiêu @
93: Cộng thêm: +39% sức tấn công của chiêu @
65: Chính xác: +115
75: Phong độn: +590
89: Sát thương quái: +280
72: Gây choáng: +28
92: Cộng thêm: +42% sức tấn công của chiêu @
93: Cộng thêm: +42% sức tấn công của chiêu @
65: Chính xác: +120
75: Phong độn: +620
89: Sát thương quái: +300
72: Gây choáng: +30
92: Cộng thêm: +45% sức tấn công của chiêu @
93: Cộng thêm: +45% sức tấn công của chiêu @
65: Chính xác: +125
75: Phong độn: +650
89: Sát thương quái: +320
72: Gây choáng: +32
92: Cộng thêm: +48% sức tấn công của chiêu @
93: Cộng thêm: +48% sức tấn công của chiêu @
65: Chính xác: +130
75: Phong độn: +680
89: Sát thương quái: +340
72: Gây choáng: +34
92: Cộng thêm: +51% sức tấn công của chiêu @
93: Cộng thêm: +51% sức tấn công của chiêu @
65: Chính xác: +135
75: Phong độn: +710
89: Sát thương quái: +360
72: Gây choáng: +36
92: Cộng thêm: +54% sức tấn công của chiêu @
93: Cộng thêm: +54% sức tấn công của chiêu @
65: Chính xác: +140
75: Phong độn: +740
89: Sát thương quái: +380
72: Gây choáng: +38
92: Cộng thêm: +57% sức tấn công của chiêu @
93: Cộng thêm: +57% sức tấn công của chiêu @
65: Chính xác: +150
75: Phong độn: +800
89: Sát thương quái: +400
72: Gây choáng: +40
92: Cộng thêm: +60% sức tấn công của chiêu @
93: Cộng thêm: +60% sức tấn công của chiêu @
178: Ẩn thân: duy trì +0 giây, khi tấn công đối phương hiệu ứng sẽ tự biến mất
179: Gây bỏng: +0
180: Chính xác: +0
178: Ẩn thân: duy trì +3.000 giây, khi tấn công đối phương hiệu ứng sẽ tự biến mất
179: Gây bỏng: +550
180: Chính xác: +100
178: Ẩn thân: duy trì +3.500 giây, khi tấn công đối phương hiệu ứng sẽ tự biến mất
179: Gây bỏng: +600
180: Chính xác: +200
178: Ẩn thân: duy trì +4.000 giây, khi tấn công đối phương hiệu ứng sẽ tự biến mất
179: Gây bỏng: +650
180: Chính xác: +300
178: Ẩn thân: duy trì +4.500 giây, khi tấn công đối phương hiệu ứng sẽ tự biến mất
179: Gây bỏng: +700
180: Chính xác: +400
178: Ẩn thân: duy trì +5.000 giây, khi tấn công đối phương hiệu ứng sẽ tự biến mất
179: Gây bỏng: +750
180: Chính xác: +500
178: Ẩn thân: duy trì +5.500 giây, khi tấn công đối phương hiệu ứng sẽ tự biến mất
179: Gây bỏng: +800
180: Chính xác: +600
178: Ẩn thân: duy trì +6.000 giây, khi tấn công đối phương hiệu ứng sẽ tự biến mất
179: Gây bỏng: +850
180: Chính xác: +700
178: Ẩn thân: duy trì +6.500 giây, khi tấn công đối phương hiệu ứng sẽ tự biến mất
179: Gây bỏng: +900
180: Chính xác: +800
178: Ẩn thân: duy trì +7.000 giây, khi tấn công đối phương hiệu ứng sẽ tự biến mất
179: Gây bỏng: +950
180: Chính xác: +900
65: Chính xác: +155
75: Phong độn: +1.020
89: Sát thương quái: +460
72: Gây choáng: +51
65: Chính xác: +160
75: Phong độn: +1.040
89: Sát thương quái: +470
72: Gây choáng: +52
65: Chính xác: +0
76: Hỏa độn: +0
89: Sát thương quái: +0
71: Gây bỏng:+0
65: Chính xác: +105
76: Hỏa độn: +820
89: Sát thương quái: +360
71: Gây bỏng:+41
65: Chính xác: +110
76: Hỏa độn: +840
89: Sát thương quái: +370
71: Gây bỏng:+42
65: Chính xác: +115
76: Hỏa độn: +860
89: Sát thương quái: +380
71: Gây bỏng:+43
65: Chính xác: +120
76: Hỏa độn: +880
89: Sát thương quái: +390
71: Gây bỏng:+44
65: Chính xác: +125
76: Hỏa độn: +900
89: Sát thương quái: +400
71: Gây bỏng:+45
65: Chính xác: +130
76: Hỏa độn: +920
89: Sát thương quái: +410
71: Gây bỏng:+46
65: Chính xác: +135
76: Hỏa độn: +940
89: Sát thương quái: +420
71: Gây bỏng:+47
65: Chính xác: +140
76: Hỏa độn: +960
89: Sát thương quái: +430
71: Gây bỏng:+48
65: Chính xác: +145
76: Hỏa độn: +980
89: Sát thương quái: +440
71: Gây bỏng:+49
65: Chính xác: +150
76: Hỏa độn: +1.000
89: Sát thương quái: +450
71: Gây bỏng:+50
65: Chính xác: +165
75: Phong độn: +1.060
89: Sát thương quái: +480
72: Gây choáng: +53
65: Chính xác: +170
75: Phong độn: +1.080
89: Sát thương quái: +490
72: Gây choáng: +54
65: Chính xác: +0
75: Phong độn: +0
89: Sát thương quái: +0
72: Gây choáng: +0
92: Cộng thêm: +0% sức tấn công của chiêu @
93: Cộng thêm: +0% sức tấn công của chiêu @
65: Chính xác: +50
75: Phong độn: +200
89: Sát thương quái: +20
72: Gây choáng: +2
92: Cộng thêm: +3% sức tấn công của chiêu @
93: Cộng thêm: +3% sức tấn công của chiêu @
65: Chính xác: +55
75: Phong độn: +230
89: Sát thương quái: +40
72: Gây choáng: +4
92: Cộng thêm: +6% sức tấn công của chiêu @
93: Cộng thêm: +6% sức tấn công của chiêu @
65: Chính xác: +60
75: Phong độn: +260
89: Sát thương quái: +60
72: Gây choáng: +6
92: Cộng thêm: +9% sức tấn công của chiêu @
93: Cộng thêm: +9% sức tấn công của chiêu @
65: Chính xác: +65
75: Phong độn: +290
89: Sát thương quái: +80
72: Gây choáng: +8
92: Cộng thêm: +12% sức tấn công của chiêu @
93: Cộng thêm: +12% sức tấn công của chiêu @
65: Chính xác: +70
75: Phong độn: +320
89: Sát thương quái: +100
72: Gây choáng: +10
92: Cộng thêm: +15% sức tấn công của chiêu @
93: Cộng thêm: +15% sức tấn công của chiêu @
65: Chính xác: +75
75: Phong độn: +350
89: Sát thương quái: +120
72: Gây choáng: +12
92: Cộng thêm: +18% sức tấn công của chiêu @
93: Cộng thêm: +18% sức tấn công của chiêu @
65: Chính xác: +80
75: Phong độn: +380
89: Sát thương quái: +140
72: Gây choáng: +14
92: Cộng thêm: +21% sức tấn công của chiêu @
93: Cộng thêm: +21% sức tấn công của chiêu @
65: Chính xác: +85
75: Phong độn: +410
89: Sát thương quái: +160
72: Gây choáng: +16
92: Cộng thêm: +24% sức tấn công của chiêu @
93: Cộng thêm: +24% sức tấn công của chiêu @
65: Chính xác: +90
75: Phong độn: +440
89: Sát thương quái: +180
72: Gây choáng: +18
92: Cộng thêm: +27% sức tấn công của chiêu @
93: Cộng thêm: +27% sức tấn công của chiêu @
65: Chính xác: +95
75: Phong độn: +470
89: Sát thương quái: +200
72: Gây choáng: +20
92: Cộng thêm: +30% sức tấn công của chiêu @
93: Cộng thêm: +30% sức tấn công của chiêu @
78: Tấn công: +0
147: Bỏ qua né tránh: +0
91: Tốc độ di chuyển: +0
138: Tương khắc lên hệ Lôi: +0
78: Tấn công: +7
147: Bỏ qua né tránh: +10
91: Tốc độ di chuyển: +10
138: Tương khắc lên hệ Lôi: +40
78: Tấn công: +14
147: Bỏ qua né tránh: +20
91: Tốc độ di chuyển: +20
138: Tương khắc lên hệ Lôi: +50
78: Tấn công: +21
147: Bỏ qua né tránh: +30
91: Tốc độ di chuyển: +30
138: Tương khắc lên hệ Lôi: +60
78: Tấn công: +28
147: Bỏ qua né tránh: +40
91: Tốc độ di chuyển: +40
138: Tương khắc lên hệ Lôi: +70
78: Tấn công: +35
147: Bỏ qua né tránh: +50
91: Tốc độ di chuyển: +50
138: Tương khắc lên hệ Lôi: +80
78: Tấn công: +42
147: Bỏ qua né tránh: +60
91: Tốc độ di chuyển: +60
138: Tương khắc lên hệ Lôi: +90
78: Tấn công: +49
147: Bỏ qua né tránh: +70
91: Tốc độ di chuyển: +70
138: Tương khắc lên hệ Lôi: +100
78: Tấn công: +56
147: Bỏ qua né tránh: +80
91: Tốc độ di chuyển: +80
138: Tương khắc lên hệ Lôi: +110
78: Tấn công: +63
147: Bỏ qua né tránh: +90
91: Tốc độ di chuyển: +90
138: Tương khắc lên hệ Lôi: +120
78: Tấn công: +70
147: Bỏ qua né tránh: +100
91: Tốc độ di chuyển: +100
138: Tương khắc lên hệ Lôi: +130
65: Chính xác: +165
73: Lôi độn: +1.060
89: Sát thương quái: +480
68: Gây suy yếu: +53
65: Chính xác: +170
73: Lôi độn: +1.080
89: Sát thương quái: +490
68: Gây suy yếu: +54
65: Chính xác: +175
75: Phong độn: +1.100
89: Sát thương quái: +500
72: Gây choáng: +55
65: Chính xác: +180
75: Phong độn: +1.120
89: Sát thương quái: +510
72: Gây choáng: +56
65: Chính xác: +185
75: Phong độn: +1.140
89: Sát thương quái: +520
72: Gây choáng: +57
65: Chính xác: +190
75: Phong độn: +1.160
89: Sát thương quái: +530
72: Gây choáng: +58
65: Chính xác: +195
75: Phong độn: +1.180
89: Sát thương quái: +540
72: Gây choáng: +59
65: Chính xác: +200
75: Phong độn: +1.200
89: Sát thương quái: +550
72: Gây choáng: +60
176: Tăng tốc độ di chuyển: +0 (duy trì 15 giây)
177: Tăng né tránh: +0 (duy trì 15 giây)
176: Tăng tốc độ di chuyển: +110 (duy trì 15 giây)
177: Tăng né tránh: +30 (duy trì 15 giây)
176: Tăng tốc độ di chuyển: +120 (duy trì 15 giây)
177: Tăng né tránh: +60 (duy trì 15 giây)
176: Tăng tốc độ di chuyển: +130 (duy trì 15 giây)
177: Tăng né tránh: +90 (duy trì 15 giây)
176: Tăng tốc độ di chuyển: +140 (duy trì 15 giây)
177: Tăng né tránh: +120 (duy trì 15 giây)
176: Tăng tốc độ di chuyển: +150 (duy trì 15 giây)
177: Tăng né tránh: +150 (duy trì 15 giây)
176: Tăng tốc độ di chuyển: +160 (duy trì 15 giây)
177: Tăng né tránh: +180 (duy trì 15 giây)
176: Tăng tốc độ di chuyển: +170 (duy trì 15 giây)
177: Tăng né tránh: +210 (duy trì 15 giây)
176: Tăng tốc độ di chuyển: +180 (duy trì 15 giây)
177: Tăng né tránh: +240 (duy trì 15 giây)
176: Tăng tốc độ di chuyển: +190 (duy trì 15 giây)
177: Tăng né tránh: +270 (duy trì 15 giây)
176: Tăng tốc độ di chuyển: +200 (duy trì 15 giây)
177: Tăng né tránh: +300 (duy trì 15 giây)
65: Chính xác: +0
75: Phong độn: +0
89: Sát thương quái: +0
72: Gây choáng: +0
65: Chính xác: +105
75: Phong độn: +820
89: Sát thương quái: +360
72: Gây choáng: +41
65: Chính xác: +110
75: Phong độn: +840
89: Sát thương quái: +370
72: Gây choáng: +42
65: Chính xác: +115
75: Phong độn: +860
89: Sát thương quái: +380
72: Gây choáng: +43
65: Chính xác: +120
75: Phong độn: +880
89: Sát thương quái: +390
72: Gây choáng: +44
65: Chính xác: +125
75: Phong độn: +900
89: Sát thương quái: +400
72: Gây choáng: +45
65: Chính xác: +130
75: Phong độn: +920
89: Sát thương quái: +410
72: Gây choáng: +46
65: Chính xác: +135
75: Phong độn: +940
89: Sát thương quái: +420
72: Gây choáng: +47
65: Chính xác: +140
75: Phong độn: +960
89: Sát thương quái: +430
72: Gây choáng: +48
65: Chính xác: +145
75: Phong độn: +980
89: Sát thương quái: +440
72: Gây choáng: +49
65: Chính xác: +150
75: Phong độn: +1.000
89: Sát thương quái: +450
72: Gây choáng: +50
65: Chính xác: +175
73: Lôi độn: +1.100
89: Sát thương quái: +500
68: Gây suy yếu: +55
65: Chính xác: +180
73: Lôi độn: +1.120
89: Sát thương quái: +510
68: Gây suy yếu: +56
65: Chính xác: +185
73: Lôi độn: +1.140
89: Sát thương quái: +520
68: Gây suy yếu: +57
65: Chính xác: +190
73: Lôi độn: +1.160
89: Sát thương quái: +530
68: Gây suy yếu: +58
65: Chính xác: +195
73: Lôi độn: +1.180
89: Sát thương quái: +540
68: Gây suy yếu: +59
65: Chính xác: +200
73: Lôi độn: +1.200
89: Sát thương quái: +550
68: Gây suy yếu: +60
65: Chính xác: +100
74: Thổ độn: +500
89: Sát thương quái: +220
69: Gây trúng độc: +22
92: Cộng thêm: +33% sức tấn công của chiêu @
93: Cộng thêm: +33% sức tấn công của chiêu @
65: Chính xác: +105
74: Thổ độn: +530
89: Sát thương quái: +240
69: Gây trúng độc: +24
92: Cộng thêm: +36% sức tấn công của chiêu @
93: Cộng thêm: +36% sức tấn công của chiêu @
65: Chính xác: +110
74: Thổ độn: +560
89: Sát thương quái: +260
69: Gây trúng độc: +26
92: Cộng thêm: +39% sức tấn công của chiêu @
93: Cộng thêm: +39% sức tấn công của chiêu @
65: Chính xác: +115
74: Thổ độn: +590
89: Sát thương quái: +280
69: Gây trúng độc: +28
92: Cộng thêm: +42% sức tấn công của chiêu @
93: Cộng thêm: +42% sức tấn công của chiêu @
65: Chính xác: +120
74: Thổ độn: +620
89: Sát thương quái: +300
69: Gây trúng độc: +30
92: Cộng thêm: +45% sức tấn công của chiêu @
93: Cộng thêm: +45% sức tấn công của chiêu @
65: Chính xác: +125
74: Thổ độn: +650
89: Sát thương quái: +320
69: Gây trúng độc: +32
92: Cộng thêm: +48% sức tấn công của chiêu @
93: Cộng thêm: +48% sức tấn công của chiêu @
65: Chính xác: +130
74: Thổ độn: +680
89: Sát thương quái: +340
69: Gây trúng độc: +34
92: Cộng thêm: +51% sức tấn công của chiêu @
93: Cộng thêm: +51% sức tấn công của chiêu @
65: Chính xác: +135
74: Thổ độn: +710
89: Sát thương quái: +360
69: Gây trúng độc: +36
92: Cộng thêm: +54% sức tấn công của chiêu @
93: Cộng thêm: +54% sức tấn công của chiêu @
65: Chính xác: +140
74: Thổ độn: +740
89: Sát thương quái: +380
69: Gây trúng độc: +38
92: Cộng thêm: +57% sức tấn công của chiêu @
93: Cộng thêm: +57% sức tấn công của chiêu @
65: Chính xác: +150
74: Thổ độn: +800
89: Sát thương quái: +400
69: Gây trúng độc: +40
92: Cộng thêm: +60% sức tấn công của chiêu @
93: Cộng thêm: +60% sức tấn công của chiêu @
65: Chính xác: +165
74: Thổ độn: +1.060
89: Sát thương quái: +480
69: Gây trúng độc: +53
65: Chính xác: +170
74: Thổ độn: +1.080
89: Sát thương quái: +490
69: Gây trúng độc: +54
65: Chính xác: +175
74: Thổ độn: +1.100
89: Sát thương quái: +500
69: Gây trúng độc: +55
65: Chính xác: +180
74: Thổ độn: +1.120
89: Sát thương quái: +510
69: Gây trúng độc: +56
65: Chính xác: +185
74: Thổ độn: +1.140
89: Sát thương quái: +520
69: Gây trúng độc: +57
65: Chính xác: +190
74: Thổ độn: +1.160
89: Sát thương quái: +530
69: Gây trúng độc: +58
65: Chính xác: +195
74: Thổ độn: +1.180
89: Sát thương quái: +540
69: Gây trúng độc: +59
65: Chính xác: +200
74: Thổ độn: +1.200
89: Sát thương quái: +550
69: Gây trúng độc: +60
178: Ẩn thân: duy trì +7.500 giây, khi tấn công đối phương hiệu ứng sẽ tự biến mất
179: Gây bỏng: +1.000
180: Chính xác: +1.000
183: Triệu hồi chim yêu quái: duy trì 0 giây
184: Sức tấn công: +0
188: Gây làm chậm: +0
183: Triệu hồi chim yêu quái: duy trì 33.000 giây
184: Sức tấn công: +1.100
188: Gây làm chậm: +15
183: Triệu hồi chim yêu quái: duy trì 36.000 giây
184: Sức tấn công: +1.200
188: Gây làm chậm: +30
183: Triệu hồi chim yêu quái: duy trì 39.000 giây
184: Sức tấn công: +1.300
188: Gây làm chậm: +45
183: Triệu hồi chim yêu quái: duy trì 42.000 giây
184: Sức tấn công: +1.400
188: Gây làm chậm: +60
183: Triệu hồi chim yêu quái: duy trì 45.000 giây
184: Sức tấn công: +1.500
188: Gây làm chậm: +75
183: Triệu hồi chim yêu quái: duy trì 48.000 giây
184: Sức tấn công: +1.600
188: Gây làm chậm: +90
183: Triệu hồi chim yêu quái: duy trì 51.000 giây
184: Sức tấn công: +1.700
188: Gây làm chậm: +105
183: Triệu hồi chim yêu quái: duy trì 54.000 giây
184: Sức tấn công: +1.800
188: Gây làm chậm: +120
183: Triệu hồi chim yêu quái: duy trì 57.000 giây
184: Sức tấn công: +1.900
188: Gây làm chậm: +135
183: Triệu hồi chim yêu quái: duy trì 60.000 giây
184: Sức tấn công: +2.000
188: Gây làm chậm: +150
190: Triệu hồi chim khổng lồ: duy trì 0 giây
191: Mỗi 2 giây phục hồi +0 Hp
192: Hp: +0
190: Triệu hồi chim khổng lồ: duy trì 33.000 giây
191: Mỗi 2 giây phục hồi +50 Hp
192: Hp: +1.100
190: Triệu hồi chim khổng lồ: duy trì 36.000 giây
191: Mỗi 2 giây phục hồi +100 Hp
192: Hp: +1.200
190: Triệu hồi chim khổng lồ: duy trì 39.000 giây
191: Mỗi 2 giây phục hồi +150 Hp
192: Hp: +1.300
190: Triệu hồi chim khổng lồ: duy trì 42.000 giây
191: Mỗi 2 giây phục hồi +200 Hp
192: Hp: +1.400
190: Triệu hồi chim khổng lồ: duy trì 45.000 giây
191: Mỗi 2 giây phục hồi +250 Hp
192: Hp: +1.500
190: Triệu hồi chim khổng lồ: duy trì 48.000 giây
191: Mỗi 2 giây phục hồi +300 Hp
192: Hp: +1.600
190: Triệu hồi chim khổng lồ: duy trì 51.000 giây
191: Mỗi 2 giây phục hồi +350 Hp
192: Hp: +1.700
190: Triệu hồi chim khổng lồ: duy trì 54.000 giây
191: Mỗi 2 giây phục hồi +400 Hp
192: Hp: +1.800
190: Triệu hồi chim khổng lồ: duy trì 57.000 giây
191: Mỗi 2 giây phục hồi +450 Hp
192: Hp: +1.900
190: Triệu hồi chim khổng lồ: duy trì 60.000 giây
191: Mỗi 2 giây phục hồi +500 Hp
192: Hp: +2.000
101: Mỗi lần thọ thương sẽ tự động tăng thêm Hp: +0 (duy trì 3 giây)
193: Tấn công: +0 (duy trì 3 giây)
101: Mỗi lần thọ thương sẽ tự động tăng thêm Hp: +700 (duy trì 3 giây)
193: Tấn công: +150 (duy trì 3 giây)
101: Mỗi lần thọ thương sẽ tự động tăng thêm Hp: +900 (duy trì 3 giây)
193: Tấn công: +300 (duy trì 3 giây)
101: Mỗi lần thọ thương sẽ tự động tăng thêm Hp: +1.100 (duy trì 3 giây)
193: Tấn công: +450 (duy trì 3 giây)
101: Mỗi lần thọ thương sẽ tự động tăng thêm Hp: +1.300 (duy trì 3 giây)
193: Tấn công: +600 (duy trì 3 giây)
101: Mỗi lần thọ thương sẽ tự động tăng thêm Hp: +1.500 (duy trì 3 giây)
193: Tấn công: +750 (duy trì 3 giây)
101: Mỗi lần thọ thương sẽ tự động tăng thêm Hp: +1.700 (duy trì 3 giây)
193: Tấn công: +900 (duy trì 3 giây)
101: Mỗi lần thọ thương sẽ tự động tăng thêm Hp: +1.900 (duy trì 3 giây)
193: Tấn công: +1.050 (duy trì 3 giây)
101: Mỗi lần thọ thương sẽ tự động tăng thêm Hp: +2.100 (duy trì 3 giây)
193: Tấn công: +1.200 (duy trì 3 giây)
101: Mỗi lần thọ thương sẽ tự động tăng thêm Hp: +2.300 (duy trì 3 giây)
193: Tấn công: +1.350 (duy trì 3 giây)
101: Mỗi lần thọ thương sẽ tự động tăng thêm Hp: +2.500 (duy trì 3 giây)
193: Tấn công: +1.500 (duy trì 3 giây)
194: Triệu hồi dơi hút máu: duy trì 0 giây
95: Tăng tấn công khi đánh chí mạng: +0%
195: Hút Hp: +0
194: Triệu hồi dơi hút máu: duy trì 33.000 giây
95: Tăng tấn công khi đánh chí mạng: +7%
195: Hút Hp: +50
194: Triệu hồi dơi hút máu: duy trì 36.000 giây
95: Tăng tấn công khi đánh chí mạng: +14%
195: Hút Hp: +100
194: Triệu hồi dơi hút máu: duy trì 39.000 giây
95: Tăng tấn công khi đánh chí mạng: +21%
195: Hút Hp: +150
194: Triệu hồi dơi hút máu: duy trì 42.000 giây
95: Tăng tấn công khi đánh chí mạng: +28%
195: Hút Hp: +200
194: Triệu hồi dơi hút máu: duy trì 45.000 giây
95: Tăng tấn công khi đánh chí mạng: +35%
195: Hút Hp: +250
194: Triệu hồi dơi hút máu: duy trì 48.000 giây
95: Tăng tấn công khi đánh chí mạng: +42%
195: Hút Hp: +300
194: Triệu hồi dơi hút máu: duy trì 51.000 giây
95: Tăng tấn công khi đánh chí mạng: +49%
195: Hút Hp: +350
194: Triệu hồi dơi hút máu: duy trì 54.000 giây
95: Tăng tấn công khi đánh chí mạng: +56%
195: Hút Hp: +400
194: Triệu hồi dơi hút máu: duy trì 57.000 giây
95: Tăng tấn công khi đánh chí mạng: +63%
195: Hút Hp: +450
194: Triệu hồi dơi hút máu: duy trì 60.000 giây
95: Tăng tấn công khi đánh chí mạng: +70%
195: Hút Hp: +500
198: Trạng thái hiền nhân: duy trì 0 giây
197: Bỏ qua kháng tính: +0%
196: Làm tiêu hao Mp: -0
198: Trạng thái hiền nhân: duy trì 33.000 giây
197: Bỏ qua kháng tính: +3%
196: Làm tiêu hao Mp: -1.000
198: Trạng thái hiền nhân: duy trì 36.000 giây
197: Bỏ qua kháng tính: +6%
196: Làm tiêu hao Mp: -1.500
198: Trạng thái hiền nhân: duy trì 39.000 giây
197: Bỏ qua kháng tính: +9%
196: Làm tiêu hao Mp: -2.000
198: Trạng thái hiền nhân: duy trì 42.000 giây
197: Bỏ qua kháng tính: +12%
196: Làm tiêu hao Mp: -2.500
198: Trạng thái hiền nhân: duy trì 45.000 giây
197: Bỏ qua kháng tính: +15%
196: Làm tiêu hao Mp: -3.000
198: Trạng thái hiền nhân: duy trì 48.000 giây
197: Bỏ qua kháng tính: +18%
196: Làm tiêu hao Mp: -3.500
198: Trạng thái hiền nhân: duy trì 51.000 giây
197: Bỏ qua kháng tính: +20%
196: Làm tiêu hao Mp: -4.000
198: Trạng thái hiền nhân: duy trì 54.000 giây
197: Bỏ qua kháng tính: +22%
196: Làm tiêu hao Mp: -4.500
198: Trạng thái hiền nhân: duy trì 57.000 giây
197: Bỏ qua kháng tính: +24%
196: Làm tiêu hao Mp: -5.000
198: Trạng thái hiền nhân: duy trì 60.000 giây
197: Bỏ qua kháng tính: +26%
196: Làm tiêu hao Mp: -5.500
275: Mỗi 1 giây phục hồi tỉ lệ Hp: +0% (duy trì 10 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +0 giây
275: Mỗi 1 giây phục hồi tỉ lệ Hp: +1% (duy trì 10 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +500 giây
275: Mỗi 1 giây phục hồi tỉ lệ Hp: +2% (duy trì 10 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +640 giây
275: Mỗi 1 giây phục hồi tỉ lệ Hp: +3% (duy trì 10 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +780 giây
275: Mỗi 1 giây phục hồi tỉ lệ Hp: +4% (duy trì 10 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +920 giây
275: Mỗi 1 giây phục hồi tỉ lệ Hp: +5% (duy trì 10 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +1.060 giây
275: Mỗi 1 giây phục hồi tỉ lệ Hp: +6% (duy trì 10 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +1.200 giây
275: Mỗi 1 giây phục hồi tỉ lệ Hp: +7% (duy trì 10 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +1.340 giây
275: Mỗi 1 giây phục hồi tỉ lệ Hp: +8% (duy trì 10 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +1.480 giây
275: Mỗi 1 giây phục hồi tỉ lệ Hp: +9% (duy trì 10 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +1.620 giây
275: Mỗi 1 giây phục hồi tỉ lệ Hp: +10% (duy trì 10 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +1.760 giây
276: Dùng Mp hút: +0% sát thương (duy trì 20 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +0 giây
276: Dùng Mp hút: +8% sát thương (duy trì 20 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +500 giây
276: Dùng Mp hút: +10% sát thương (duy trì 20 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +685 giây
276: Dùng Mp hút: +12% sát thương (duy trì 20 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +870 giây
276: Dùng Mp hút: +14% sát thương (duy trì 20 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +1.055 giây
276: Dùng Mp hút: +16% sát thương (duy trì 20 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +1.240 giây
276: Dùng Mp hút: +18% sát thương (duy trì 20 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +1.425 giây
276: Dùng Mp hút: +20% sát thương (duy trì 20 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +1.610 giây
276: Dùng Mp hút: +22% sát thương (duy trì 20 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +1.795 giây
276: Dùng Mp hút: +24% sát thương (duy trì 20 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +1.980 giây
276: Dùng Mp hút: +26% sát thương (duy trì 20 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +2.165 giây
269: Giảm kháng tất cả: -0
270: Giảm Chakra: -0
269: Giảm kháng tất cả: -35
270: Giảm Chakra: -25
269: Giảm kháng tất cả: -50
270: Giảm Chakra: -50
269: Giảm kháng tất cả: -65
270: Giảm Chakra: -75
269: Giảm kháng tất cả: -80
270: Giảm Chakra: -100
269: Giảm kháng tất cả: -95
270: Giảm Chakra: -125
269: Giảm kháng tất cả: -110
270: Giảm Chakra: -150
269: Giảm kháng tất cả: -125
270: Giảm Chakra: -175
269: Giảm kháng tất cả: -140
270: Giảm Chakra: -200
269: Giảm kháng tất cả: -155
270: Giảm Chakra: -225
269: Giảm kháng tất cả: -170
270: Giảm Chakra: -250
78: Tấn công: +0
272: Giữ chặt không cho đối phương di chuyển: duy trì +0 giây
78: Tấn công: +2.000
272: Giữ chặt không cho đối phương di chuyển: duy trì +3.000 giây
78: Tấn công: +2.140
272: Giữ chặt không cho đối phương di chuyển: duy trì +3.500 giây
78: Tấn công: +2.280
272: Giữ chặt không cho đối phương di chuyển: duy trì +4.000 giây
78: Tấn công: +2.420
272: Giữ chặt không cho đối phương di chuyển: duy trì +4.500 giây
78: Tấn công: +2.560
272: Giữ chặt không cho đối phương di chuyển: duy trì +5.000 giây
78: Tấn công: +2.700
272: Giữ chặt không cho đối phương di chuyển: duy trì +5.500 giây
78: Tấn công: +2.840
272: Giữ chặt không cho đối phương di chuyển: duy trì +6.000 giây
78: Tấn công: +2.980
272: Giữ chặt không cho đối phương di chuyển: duy trì +6.500 giây
78: Tấn công: +3.120
272: Giữ chặt không cho đối phương di chuyển: duy trì +7.000 giây
78: Tấn công: +3.260
272: Giữ chặt không cho đối phương di chuyển: duy trì +7.500 giây
279: Tăng thêm tấn công theo tỉ lệ Hp của đối phương: +0% (không có tác dụng với quái)
89: Sát thương quái: +0
279: Tăng thêm tấn công theo tỉ lệ Hp của đối phương: +12% (không có tác dụng với quái)
89: Sát thương quái: +2.000
279: Tăng thêm tấn công theo tỉ lệ Hp của đối phương: +14% (không có tác dụng với quái)
89: Sát thương quái: +2.200
279: Tăng thêm tấn công theo tỉ lệ Hp của đối phương: +16% (không có tác dụng với quái)
89: Sát thương quái: +2.400
279: Tăng thêm tấn công theo tỉ lệ Hp của đối phương: +18% (không có tác dụng với quái)
89: Sát thương quái: +2.600
279: Tăng thêm tấn công theo tỉ lệ Hp của đối phương: +20% (không có tác dụng với quái)
89: Sát thương quái: +2.800
279: Tăng thêm tấn công theo tỉ lệ Hp của đối phương: +22% (không có tác dụng với quái)
89: Sát thương quái: +3.000
279: Tăng thêm tấn công theo tỉ lệ Hp của đối phương: +24% (không có tác dụng với quái)
89: Sát thương quái: +3.200
279: Tăng thêm tấn công theo tỉ lệ Hp của đối phương: +26% (không có tác dụng với quái)
89: Sát thương quái: +3.400
279: Tăng thêm tấn công theo tỉ lệ Hp của đối phương: +28% (không có tác dụng với quái)
89: Sát thương quái: +3.600
279: Tăng thêm tấn công theo tỉ lệ Hp của đối phương: +30% (không có tác dụng với quái)
89: Sát thương quái: +3.800
278: Mỗi lần xuất chiêu sẽ có xác xuất tăng thêm 1 nhát đánh: duy trì +0 giây
280: Chính xác:+0
278: Mỗi lần xuất chiêu sẽ có xác xuất tăng thêm 1 nhát đánh: duy trì +3.500 giây
280: Chính xác:+100
278: Mỗi lần xuất chiêu sẽ có xác xuất tăng thêm 1 nhát đánh: duy trì +4.000 giây
280: Chính xác:+110
278: Mỗi lần xuất chiêu sẽ có xác xuất tăng thêm 1 nhát đánh: duy trì +4.500 giây
280: Chính xác:+120
278: Mỗi lần xuất chiêu sẽ có xác xuất tăng thêm 1 nhát đánh: duy trì +5.000 giây
280: Chính xác:+130
278: Mỗi lần xuất chiêu sẽ có xác xuất tăng thêm 1 nhát đánh: duy trì +5.500 giây
280: Chính xác:+140
278: Mỗi lần xuất chiêu sẽ có xác xuất tăng thêm 1 nhát đánh: duy trì +6.000 giây
280: Chính xác:+150
278: Mỗi lần xuất chiêu sẽ có xác xuất tăng thêm 1 nhát đánh: duy trì +6.500 giây
280: Chính xác:+160
278: Mỗi lần xuất chiêu sẽ có xác xuất tăng thêm 1 nhát đánh: duy trì +7.000 giây
280: Chính xác:+170
278: Mỗi lần xuất chiêu sẽ có xác xuất tăng thêm 1 nhát đánh: duy trì +7.500 giây
280: Chính xác:+180
278: Mỗi lần xuất chiêu sẽ có xác xuất tăng thêm 1 nhát đánh: duy trì +8.000 giây
280: Chính xác:+190
78: Tấn công: +0
268: Khóa chặt đối phương: duy trì +0 giây
78: Tấn công: +2.000
268: Khóa chặt đối phương: duy trì +3.250 giây
78: Tấn công: +2.150
268: Khóa chặt đối phương: duy trì +3.500 giây
78: Tấn công: +2.300
268: Khóa chặt đối phương: duy trì +3.750 giây
78: Tấn công: +2.450
268: Khóa chặt đối phương: duy trì +4.000 giây
78: Tấn công: +2.600
268: Khóa chặt đối phương: duy trì +4.250 giây
78: Tấn công: +2.750
268: Khóa chặt đối phương: duy trì +4.500 giây
78: Tấn công: +2.900
268: Khóa chặt đối phương: duy trì +4.750 giây
78: Tấn công: +3.050
268: Khóa chặt đối phương: duy trì +5.000 giây
78: Tấn công: +3.200
268: Khóa chặt đối phương: duy trì +5.250 giây
78: Tấn công: +3.350
268: Khóa chặt đối phương: duy trì +5.500 giây
99: Giải trừ 5 hiệu ứng cơ bản (suy yếu, trúng độc, làm chậm, bỏng, choáng): duy trì +0 giây
280: Chính xác:+0
99: Giải trừ 5 hiệu ứng cơ bản (suy yếu, trúng độc, làm chậm, bỏng, choáng): duy trì +4.000 giây
280: Chính xác:+60
99: Giải trừ 5 hiệu ứng cơ bản (suy yếu, trúng độc, làm chậm, bỏng, choáng): duy trì +4.500 giây
280: Chính xác:+80
99: Giải trừ 5 hiệu ứng cơ bản (suy yếu, trúng độc, làm chậm, bỏng, choáng): duy trì +5.000 giây
280: Chính xác:+100
99: Giải trừ 5 hiệu ứng cơ bản (suy yếu, trúng độc, làm chậm, bỏng, choáng): duy trì +5.500 giây
280: Chính xác:+120
99: Giải trừ 5 hiệu ứng cơ bản (suy yếu, trúng độc, làm chậm, bỏng, choáng): duy trì +6.000 giây
280: Chính xác:+140
99: Giải trừ 5 hiệu ứng cơ bản (suy yếu, trúng độc, làm chậm, bỏng, choáng): duy trì +6.500 giây
280: Chính xác:+160
99: Giải trừ 5 hiệu ứng cơ bản (suy yếu, trúng độc, làm chậm, bỏng, choáng): duy trì +7.000 giây
280: Chính xác:+180
99: Giải trừ 5 hiệu ứng cơ bản (suy yếu, trúng độc, làm chậm, bỏng, choáng): duy trì +7.500 giây
280: Chính xác:+200
99: Giải trừ 5 hiệu ứng cơ bản (suy yếu, trúng độc, làm chậm, bỏng, choáng): duy trì +8.000 giây
280: Chính xác:+220
99: Giải trừ 5 hiệu ứng cơ bản (suy yếu, trúng độc, làm chậm, bỏng, choáng): duy trì +8.500 giây
280: Chính xác:+240
273: Tăng Chakra: +0 (duy trì 10 giây)
274: Tỉ lệ gây choáng nữa giây: +0%
273: Tăng Chakra: +10 (duy trì 10 giây)
274: Tỉ lệ gây choáng nữa giây: +6%
273: Tăng Chakra: +20 (duy trì 10 giây)
274: Tỉ lệ gây choáng nữa giây: +8%
273: Tăng Chakra: +30 (duy trì 10 giây)
274: Tỉ lệ gây choáng nữa giây: +10%
273: Tăng Chakra: +40 (duy trì 10 giây)
274: Tỉ lệ gây choáng nữa giây: +12%
273: Tăng Chakra: +50 (duy trì 10 giây)
274: Tỉ lệ gây choáng nữa giây: +14%
273: Tăng Chakra: +60 (duy trì 10 giây)
274: Tỉ lệ gây choáng nữa giây: +16%
273: Tăng Chakra: +70 (duy trì 10 giây)
274: Tỉ lệ gây choáng nữa giây: +18%
273: Tăng Chakra: +80 (duy trì 10 giây)
274: Tỉ lệ gây choáng nữa giây: +20%
273: Tăng Chakra: +90 (duy trì 10 giây)
274: Tỉ lệ gây choáng nữa giây: +22%
273: Tăng Chakra: +100 (duy trì 10 giây)
274: Tỉ lệ gây choáng nữa giây: +24%
276: Dùng Mp hút: +28% sát thương (duy trì 20 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +2.350 giây
276: Dùng Mp hút: +30% sát thương (duy trì 20 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +2.535 giây
275: Mỗi 1 giây phục hồi tỉ lệ Hp: +11% (duy trì 10 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +1.900 giây
275: Mỗi 1 giây phục hồi tỉ lệ Hp: +12% (duy trì 10 giây)
277: Tăng thời gian gây tê liệt khi sử dụng chiêu dịch chuyển chi thuật: +2.040 giây
78: Tấn công: +3.400
272: Giữ chặt không cho đối phương di chuyển: duy trì +8.000 giây
78: Tấn công: +3.540
272: Giữ chặt không cho đối phương di chuyển: duy trì +8.500 giây
269: Giảm kháng tất cả: -185
270: Giảm Chakra: -275
269: Giảm kháng tất cả: -200
270: Giảm Chakra: -300
278: Mỗi lần xuất chiêu sẽ có xác xuất tăng thêm 1 nhát đánh: duy trì +8.500 giây
280: Chính xác:+200
278: Mỗi lần xuất chiêu sẽ có xác xuất tăng thêm 1 nhát đánh: duy trì +9.000 giây
280: Chính xác:+210
279: Tăng thêm tấn công theo tỉ lệ Hp của đối phương: +32% (không có tác dụng với quái)
89: Sát thương quái: +4.000
279: Tăng thêm tấn công theo tỉ lệ Hp của đối phương: +34% (không có tác dụng với quái)
89: Sát thương quái: +4.200
78: Tấn công: +3.500
268: Khóa chặt đối phương: duy trì +5.750 giây
78: Tấn công: +3.650
268: Khóa chặt đối phương: duy trì +6.000 giây
99: Giải trừ 5 hiệu ứng cơ bản (suy yếu, trúng độc, làm chậm, bỏng, choáng): duy trì +9.000 giây
280: Chính xác:+260
99: Giải trừ 5 hiệu ứng cơ bản (suy yếu, trúng độc, làm chậm, bỏng, choáng): duy trì +9.500 giây
280: Chính xác:+280
273: Tăng Chakra: +110 (duy trì 10 giây)
274: Tỉ lệ gây choáng nữa giây: +26%
273: Tăng Chakra: +120 (duy trì 10 giây)
274: Tỉ lệ gây choáng nữa giây: +28%
369: Susano Itachi: duy trì 30 giây
369: Susano Itachi: duy trì 30 giây
78: Tấn công: +0
318: Làm suy giảm né tránh của đổi phương: -0 (duy trì 15 giây)
78: Tấn công: +2.000
318: Làm suy giảm né tránh của đổi phương: -300 (duy trì 15 giây)
78: Tấn công: +2.100
318: Làm suy giảm né tránh của đổi phương: -450 (duy trì 15 giây)
78: Tấn công: +2.200
318: Làm suy giảm né tránh của đổi phương: -600 (duy trì 15 giây)
78: Tấn công: +2.300
318: Làm suy giảm né tránh của đổi phương: -750 (duy trì 15 giây)
78: Tấn công: +2.400
318: Làm suy giảm né tránh của đổi phương: -900 (duy trì 15 giây)
78: Tấn công: +2.500
318: Làm suy giảm né tránh của đổi phương: -1.050 (duy trì 15 giây)
78: Tấn công: +2.600
318: Làm suy giảm né tránh của đổi phương: -1.200 (duy trì 15 giây)
78: Tấn công: +2.700
318: Làm suy giảm né tránh của đổi phương: -1.350 (duy trì 15 giây)
78: Tấn công: +0
315: Ngăn chặn thi triển nhẫn thuật: duy trì 0 giây
78: Tấn công: +2.000
315: Ngăn chặn thi triển nhẫn thuật: duy trì 16.000 giây
78: Tấn công: +2.100
315: Ngăn chặn thi triển nhẫn thuật: duy trì 18.000 giây
78: Tấn công: +2.200
315: Ngăn chặn thi triển nhẫn thuật: duy trì 20.000 giây
78: Tấn công: +2.300
315: Ngăn chặn thi triển nhẫn thuật: duy trì 22.000 giây
78: Tấn công: +2.400
315: Ngăn chặn thi triển nhẫn thuật: duy trì 24.000 giây
78: Tấn công: +2.500
315: Ngăn chặn thi triển nhẫn thuật: duy trì 26.000 giây
78: Tấn công: +2.600
315: Ngăn chặn thi triển nhẫn thuật: duy trì 28.000 giây
78: Tấn công: +2.700
315: Ngăn chặn thi triển nhẫn thuật: duy trì 30.000 giây
78: Tấn công: +0
317: Ngăn chặn không cho đối phương sử dụng dược phẩm: duy trì 0 giây
78: Tấn công: +2.000
317: Ngăn chặn không cho đối phương sử dụng dược phẩm: duy trì 16.000 giây
78: Tấn công: +2.100
317: Ngăn chặn không cho đối phương sử dụng dược phẩm: duy trì 18.000 giây
78: Tấn công: +2.200
317: Ngăn chặn không cho đối phương sử dụng dược phẩm: duy trì 20.000 giây
78: Tấn công: +2.300
317: Ngăn chặn không cho đối phương sử dụng dược phẩm: duy trì 22.000 giây
78: Tấn công: +2.400
317: Ngăn chặn không cho đối phương sử dụng dược phẩm: duy trì 24.000 giây
78: Tấn công: +2.500
317: Ngăn chặn không cho đối phương sử dụng dược phẩm: duy trì 26.000 giây
78: Tấn công: +2.600
317: Ngăn chặn không cho đối phương sử dụng dược phẩm: duy trì 28.000 giây
78: Tấn công: +2.700
317: Ngăn chặn không cho đối phương sử dụng dược phẩm: duy trì 30.000 giây
78: Tấn công: +0
316: Hút đối phương lại gần
78: Tấn công: +1.500
316: Hút đối phương lại gần
78: Tấn công: +1.550
316: Hút đối phương lại gần
78: Tấn công: +1.600
316: Hút đối phương lại gần
78: Tấn công: +1.650
316: Hút đối phương lại gần
78: Tấn công: +1.700
316: Hút đối phương lại gần
78: Tấn công: +1.750
316: Hút đối phương lại gần
78: Tấn công: +1.800
316: Hút đối phương lại gần
78: Tấn công: +1.850
316: Hút đối phương lại gần
78: Tấn công: +0
314: Làm suy giảm chính xác của đối phương: -0 (duy trì 15 giây)
78: Tấn công: +2.800
314: Làm suy giảm chính xác của đối phương: -250 (duy trì 15 giây)
78: Tấn công: +2.900
314: Làm suy giảm chính xác của đối phương: -500 (duy trì 15 giây)
78: Tấn công: +3.000
314: Làm suy giảm chính xác của đối phương: -750 (duy trì 15 giây)
78: Tấn công: +3.100
314: Làm suy giảm chính xác của đối phương: -1.000 (duy trì 15 giây)
78: Tấn công: +3.200
314: Làm suy giảm chính xác của đối phương: -1.250 (duy trì 15 giây)
78: Tấn công: +3.300
314: Làm suy giảm chính xác của đối phương: -1.500 (duy trì 15 giây)
78: Tấn công: +3.400
314: Làm suy giảm chính xác của đối phương: -1.750 (duy trì 15 giây)
78: Tấn công: +3.500
314: Làm suy giảm chính xác của đối phương: -2.000 (duy trì 15 giây)
*/