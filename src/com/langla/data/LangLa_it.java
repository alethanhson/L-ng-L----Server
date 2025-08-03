package com.langla.data;

import java.util.Comparator;

 public class LangLa_it implements Comparator {
   
   public int compare(Object var1, Object var2) {
      Skill var10000 = (Skill)var1;
      Skill var3 = (Skill)var2;
      return var10000.getSkillTemplate().levelNeed - var3.getSkillTemplate().levelNeed;
   }
}
