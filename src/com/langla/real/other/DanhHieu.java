/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.langla.real.other;

import com.langla.data.DataCenter;
import com.tgame.model.Caption;

/**
 * @author PKoolVN
 **/
public class DanhHieu {

   public int id;

   public String name;
   public int hsd;

   public int detail;

   public DanhHieu(int id, String name, int hsd, int detail) {
      this.id = id;
      this.name = name;
      this.hsd = hsd;
      this.detail = detail;
   }

   public DanhHieu(){

   }

}
