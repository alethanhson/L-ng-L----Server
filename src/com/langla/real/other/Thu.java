/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.langla.real.other;

import com.langla.real.item.Item;

/**
 * @author PKoolVN
 **/
public class Thu {
    public int id;
    public String nguoiGui = "";
    public String chuDe = "";
    public String noiDung = "";

    public Item item;
    public int bac = 0;
    public int bacKhoa = 0;
    public int vang = 0;
    public int vangKhoa = 0;
    public long exp = 0;
    public boolean dadoc = false;

    public int time = (int)(System.currentTimeMillis()/1000L)+172800;

    public Thu (){
    }

}
