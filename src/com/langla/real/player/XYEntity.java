package com.langla.real.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.langla.data.InfoChar;
import com.langla.lib.Utlis;
import com.langla.server.lib.Writer;
import java.io.IOException;
import java.util.Comparator;
import java.util.Vector;
@JsonIgnoreProperties(ignoreUnknown = true)
public class XYEntity {

    public static Comparator da = new LangLa_fj();
    public static Comparator db = new LangLa_fk();
    public short cx;
    public short cy;
    public Vector vec = new Vector();

    public static XYEntity create(int var0, int var1) {
        XYEntity var2;
        (var2 = new XYEntity()).setXY(var0, var1);
        return var2;
    }
    @JsonIgnore
    public void setXY(int var1, int var2) {
        if (vec.size() > 10) {
            vec.removeElementAt(0);
        }
        vec.add(new int[]{cx, cy});
        this.cx = (short) var1;
        this.cy = (short) var2;
    }
    @JsonIgnore
    public void writeXY(Writer writer) throws IOException {
        writer.writeShort(cx);
        writer.writeShort(cy);
    }
    @JsonIgnore
    public int getRe(XYEntity XYEntity) {
        return Utlis.getRange(XYEntity, this);
    }
    @JsonIgnore
    public void backXY() {
        if (vec.size() <= 0) {
            setXY(500, 500);
            return;
        }
        int[] data = (int[]) vec.get(vec.size() / 2);
        vec.clear();
        setXY(data[0], data[1]);
    }
}
