package com.langla.real.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.json.JSONException;
import org.json.JSONObject;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Entity extends XYEntity {

    public int idEntity;
    public byte dir;
    public byte status;
    @JsonIgnore
    public void setXY(JSONObject json) {
        try {
            setXY(json.getInt("cx"), json.getInt("cy"));
        } catch (JSONException ex) {
        }
    }
    @JsonIgnore
    public void setDir(Entity var1) {
        if (var1 != null) {
            if (this.cx < var1.cx) {
                this.dir = 2;
            } else {
                this.dir = 3;
            }
        }
    }
}
