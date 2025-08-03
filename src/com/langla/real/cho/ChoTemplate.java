package com.langla.real.cho;

import com.langla.real.item.Item;

/**
 * @author PKoolVN
 **/
public class ChoTemplate {

    public long id;
    public int character_id;
    public String character_name;

    public int isBuy = 0;

    public Item item;

    public int time;

    public int bac = 0;

    public ChoTemplate (){
    }

    public int getTime() {
        return time;
    }

    public int getBac() {
        return bac;
    }

    public int getItemType() {
        return item.getItemTemplate().type;
    }

    public String getCharacterName() {
        return character_name;
    }

    public int getItemLevel() {
        return item.getItemTemplate().levelNeed;
    }
}
