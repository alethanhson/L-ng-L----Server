package com.langla.data;

import java.util.Comparator;

 public class LangLa_iu implements Comparator {
   
   public int compare(Object var1, Object var2) {
      Skill var10000 = (Skill)var1;
      Skill var4 = (Skill)var2;
      Skill var3 = var10000;
      return var4.getSkillTemplate().levelNeed - var3.getSkillTemplate().levelNeed;
   }
}
