package com.langla.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.langla.lib.Utlis;
import com.langla.real.item.Item;
import com.langla.real.player.Client;
import com.langla.utlis.UTPKoolVN;

public class SkillClan {
   public int id;
   public String name;
   public String detail;
   public int levelNeed;
   public String strOptions;
   public int idIcon;
   public int moneyBuy;


   public SkillClan(){

   }
   public SkillClan a() {
      SkillClan var1;
      (var1 = new SkillClan()).id = this.id;
      var1.name = this.name;
      var1.detail = this.detail;
      var1.levelNeed = this.levelNeed;
      var1.strOptions = this.strOptions;
      var1.idIcon = this.idIcon;
      var1.moneyBuy = this.moneyBuy;
      return var1;
   }
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
   @JsonIgnore
   public void setLevelViThu(int level) {
      this.levelNeed = level;

      if(level <= 0) return;
      ItemOption[] arraymy = getItemOption();

      if (arraymy != null) {
         for (int i = 0; i < arraymy.length; i++) {
            if (arraymy[i] != null) {
               int plus = arraymy[i].a[1]/level;
               arraymy[i].a[1] += plus;

            }
         }
         this.strOptions = Item.getStrOptionsFormItemOption(arraymy);
      }

   }
   @JsonIgnore
   public Object clone() {
      return this.a();
   }
}
