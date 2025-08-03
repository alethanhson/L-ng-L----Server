package com.langla.data;

import com.langla.real.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PKoolVN
 **/
public class Trade {
    public boolean IsTrade;
    public int Id_Char_Trade;
    public String Name_Char_Send;
    public boolean IsLock;

    public boolean IsHold;
    public List<Item> Items;

    public int bac;


    public Trade()
    {
        IsTrade = false;
        IsLock = false;
        Id_Char_Trade = -1;
        Items = new ArrayList<>();
        bac = 0;
        IsHold = false;
    }
}
