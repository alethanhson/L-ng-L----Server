package com.langla.server.main;

import java.sql.Timestamp;

/**
 * @author PKoolVN
 **/

public interface PKoolVNResultSet {
    byte getByte(int var1) throws Exception;
    byte getByte(String var1) throws Exception;

    int getInt(int var1) throws Exception;
    int getInt(String var1) throws Exception;
    int getRows() throws Exception;

    short getShort(int var1) throws Exception;
    short getShort(String var1) throws Exception;

    float getFloat(int var1) throws Exception;
    float getFloat(String var1) throws Exception;

    double getDouble(int var1) throws Exception;
    double getDouble(String var1) throws Exception;

    long getLong(int var1) throws Exception;
    long getLong(String var1) throws Exception;

    String getString(int var1) throws Exception;
    String getString(String var1) throws Exception;

    Object getObject(int var1) throws Exception;
    Object getObject(String var1) throws Exception;

    Timestamp getTimestamp(int var1) throws Exception;
    Timestamp getTimestamp(String var1) throws Exception;

    boolean next() throws Exception;
    boolean first() throws Exception;
    boolean gotoResult(int var1) throws Exception;
    boolean gotoFirst() throws Exception;
    boolean getBoolean(int var1) throws Exception;
    boolean getBoolean(String var1) throws Exception;
    boolean gotoLast() throws Exception;

    void gotoBeforeFirst();
    void dispose();
}
