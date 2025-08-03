package com.langla.data;

import com.langla.real.npc.Npc;
import java.util.ArrayList;

public class NpcTemplate {

    public static ArrayList<Npc.ActionNpc>[] listAction;
   public byte[][] a;
   public LangLa_hz[] b;
   public short id;
   public short width;
   public short height;
   public short indexData;
   public short g;
   public String name;
   public String detail;
   public int hp;
   public int mp;
   

   public NpcTemplate(int var1) {
      this.id = (short)var1;
   }
}
