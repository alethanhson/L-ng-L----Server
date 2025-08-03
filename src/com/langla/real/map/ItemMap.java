/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.langla.real.map;

import com.langla.data.DataCache;
import com.langla.real.player.Entity;
import com.langla.real.map.Map.Zone;
import com.langla.real.item.Item;
import com.langla.server.lib.Writer;
import java.io.IOException;

/**
 * @author PKoolVN
 **/
public class ItemMap extends Entity implements Cloneable {


    public int idChar;
    public boolean isSystem = false;
    public Item item;
    public long timeCreate = 0L;

    public ItemMap() {
        idChar = -1;
        this.idEntity = DataCache.getIDItemMap();
        timeCreate = System.currentTimeMillis();
    }

    public ItemMap(int idChar) {
        this.idChar = idChar;
        this.idEntity = DataCache.getIDItemMap();
        timeCreate = System.currentTimeMillis();
    }

    public static ItemMap createItemMap(Item item, int cx, int cy) {
        ItemMap itemMap = new ItemMap();
        itemMap.setXY(cx, cy);
        itemMap.item = item;
        return itemMap;
    }

    public void write(Writer writer, int cx, int cy, Zone zone) throws IOException {
        if (cx == -1) {
            cx = this.cx;
        }
        if (cy == -1) {
            cy = this.cy;
        }
        this.cx = (short) cx;
        this.cy = (short) cy;
        writer.writeInt(idChar);
        writer.writeShort(idEntity);
        try {
            this.cy = zone.getXYBlockMap(cx, cy).cy;
        } catch (Exception e) {

        }
        writeXY(writer);
        item.write(writer);
    }

    void update(Zone zone) {
        if (System.currentTimeMillis() - timeCreate >= (3 * 60000)) {
            try {
                Writer writer = new Writer();
                writer.writeShort(idEntity);
                zone.removeItemMap(writer);
            } catch (Exception ex) {
            }
            synchronized (zone.vecItemMap) {
                zone.vecItemMap.remove(this);
            }
        } else if (System.currentTimeMillis() - timeCreate >= (1 * 60000)) {
            if (idChar != -1) {
                idChar = -1;
            }
        }
    }

}
