package com.langla.server.main;
import com.langla.server.main.PKoolVNResultSet;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * @author PKoolVN
 **/
//

public class ResultSetImpl implements PKoolVNResultSet {
    private Map<String, Object>[] data;
    private Object[][] values;
    private int indexData = -1;

    public ResultSetImpl(ResultSet rs) throws Exception {
        try {
            rs.last();
            int nRow = rs.getRow();
            rs.beforeFirst();
            ResultSetMetaData rsmd = rs.getMetaData();
            int nColumn = rsmd.getColumnCount();
            this.data = new HashMap[nRow];

            int index;
            for(index = 0; index < this.data.length; ++index) {
                this.data[index] = new HashMap();
            }

            this.values = new Object[nRow][nColumn];

            for(index = 0; rs.next(); ++index) {
                for(int i = 1; i <= nColumn; ++i) {
                    String tableName = rsmd.getTableName(i);
                    String columnName = rsmd.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    this.data[index].put(columnName.toLowerCase(), columnValue);
                    this.data[index].put(tableName.toLowerCase() + "." + columnName.toLowerCase(), columnValue);
                    this.values[index][i - 1] = columnValue;
                }
            }
        } catch (Exception var17) {
            throw var17;
        } finally {
            if (rs != null) {
                try {
                    rs.getStatement().close();
                    rs.close();
                } catch (Exception var16) {
                }
            }

        }

    }

    public void dispose() {
        Map[] var1 = this.data;
        int var2 = var1.length;

        int var3;
        Map map;
        for(var3 = 0; var3 < var2; ++var3) {
            map = var1[var3];
            map.clear();
            map = null;
        }

        this.data = null;
        Object[][] var9 = this.values;
        var2 = var9.length;

        for(var3 = 0; var3 < var2; ++var3) {
            Object[] obj = var9[var3];
            Object[] var5 = obj;
            int var6 = obj.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                Object var10000 = var5[var7];
                Object var8 = null;
            }

            map = null;
        }

        this.values = (Object[][])null;
    }

    public boolean next() throws Exception {
        if (this.data == null) {
            throw new Exception("No data available");
        } else {
            ++this.indexData;
            return this.indexData < this.data.length;
        }
    }

    public boolean first() throws Exception {
        if (this.data == null) {
            throw new Exception("No data available");
        } else {
            ++this.indexData;
            return this.indexData == this.data.length - 1;
        }
    }

    public boolean gotoResult(int index) throws Exception {
        if (this.data == null) {
            throw new Exception("No data available");
        } else if (this.indexData >= 0 && this.indexData < this.data.length) {
            this.indexData = index;
            return true;
        } else {
            throw new Exception("Index out of bound");
        }
    }

    public boolean gotoFirst() throws Exception {
        if (this.data != null && this.data.length != 0) {
            this.indexData = 0;
            return true;
        } else {
            throw new Exception("No data available");
        }
    }

    public void gotoBeforeFirst() {
        this.indexData = -1;
    }

    public boolean gotoLast() throws Exception {
        if (this.data == null) {
            throw new Exception("No data available");
        } else {
            this.indexData = this.data.length - 1;
            return true;
        }
    }

    public int getRows() throws Exception {
        if (this.data == null) {
            throw new Exception("No data available");
        } else {
            return this.data.length;
        }
    }

    public byte getByte(int column) throws Exception {
        if (this.values == null) {
            throw new Exception("No data available");
        } else if (this.indexData == -1) {
            throw new Exception("Results need to be prepared in advance");
        } else {
            Object value = this.values[this.indexData][column - 1];
            if (value instanceof Integer) {
                return ((Integer) value).byteValue();
            } else {
                throw new Exception("The value is not an Integer");
            }
        }
    }

    public byte getByte(String column) throws Exception {
        if (this.data == null) {
            throw new Exception("No data available");
        } else if (this.indexData == -1) {
            throw new Exception("Results need to be prepared in advance");
        } else {
            Object value = this.data[this.indexData].get(column.toLowerCase());
            if (value instanceof Integer) {
                return ((Integer) value).byteValue();
            } else {
                throw new Exception("The value is not an Integer");
            }
        }
    }

    public int getInt(int column) throws Exception {
        if (this.values == null) {
            throw new Exception("No data available");
        } else if (this.indexData == -1) {
            throw new Exception("Results need to be prepared in advance");
        } else {
            Object value = this.values[this.indexData][column - 1];
            if (value instanceof Long) {
                return ((Long) value).intValue();
            } else {
                throw new Exception("The value is not a Long");
            }
        }
    }

    public int getInt(String column) throws Exception {
        if (this.data == null) {
            throw new Exception("No data available");
        } else if (this.indexData == -1) {
            throw new Exception("Results need to be prepared in advance");
        } else {
            return (Integer)this.data[this.indexData].get(column.toLowerCase());
        }
    }

    public float getFloat(int column) throws Exception {
        if (this.values == null) {
            throw new Exception("No data available");
        } else if (this.indexData == -1) {
            throw new Exception("Results need to be prepared in advance");
        } else {
            return (Float)this.values[this.indexData][column - 1];
        }
    }

    public float getFloat(String column) throws Exception {
        if (this.data == null) {
            throw new Exception("No data available");
        } else if (this.indexData == -1) {
            throw new Exception("Results need to be prepared in advance");
        } else {
            return (Float)this.data[this.indexData].get(column.toLowerCase());
        }
    }

    public double getDouble(int column) throws Exception {
        if (this.values == null) {
            throw new Exception("No data available");
        } else if (this.indexData == -1) {
            throw new Exception("Results need to be prepared in advance");
        } else {
            return (Double)this.values[this.indexData][column - 1];
        }
    }

    public double getDouble(String column) throws Exception {
        if (this.data == null) {
            throw new Exception("No data available");
        } else if (this.indexData == -1) {
            throw new Exception("Results need to be prepared in advance");
        } else {
            return (Double)this.data[this.indexData].get(column.toLowerCase());
        }
    }

    public long getLong(int column) throws Exception {
        if (this.values == null) {
            throw new Exception("No data available");
        } else if (this.indexData == -1) {
            throw new Exception("Results need to be prepared in advance");
        } else {
            return (Long)this.values[this.indexData][column - 1];
        }
    }

    public long getLong(String column) throws Exception {
        if (this.data == null) {
            throw new Exception("No data available");
        } else if (this.indexData == -1) {
            throw new Exception("Results need to be prepared in advance");
        } else {
            return (Long)this.data[this.indexData].get(column.toLowerCase());
        }
    }

    public String getString(int column) throws Exception {
        if (this.values == null) {
            throw new Exception("No data available");
        } else if (this.indexData == -1) {
            throw new Exception("Results need to be prepared in advance");
        } else {
            return String.valueOf(this.values[this.indexData][column - 1]);
        }
    }

    public String getString(String column) throws Exception {
        if (this.data == null) {
            throw new Exception("No data available");
        } else if (this.indexData == -1) {
            throw new Exception("Results need to be prepared in advance");
        } else {
            return String.valueOf(this.data[this.indexData].get(column.toLowerCase()));
        }
    }

    public Object getObject(int column) throws Exception {
        if (this.values == null) {
            throw new Exception("No data available");
        } else if (this.indexData == -1) {
            throw new Exception("Results need to be prepared in advance");
        } else {
            return this.values[this.indexData][column - 1];
        }
    }

    public Object getObject(String column) throws Exception {
        if (this.data == null) {
            throw new Exception("No data available");
        } else if (this.indexData == -1) {
            throw new Exception("Results need to be prepared in advance");
        } else {
            return this.data[this.indexData].get(column.toLowerCase());
        }
    }

    public boolean getBoolean(int column) throws Exception {
        if (this.values == null) {
            throw new Exception("No data available");
        } else if (this.indexData == -1) {
            throw new Exception("Results need to be prepared in advance");
        } else {
            try {
                return (Integer)this.values[this.indexData][column - 1] == 1;
            } catch (Exception var3) {
                return (Boolean)this.values[this.indexData][column - 1];
            }
        }
    }

    public boolean getBoolean(String column) throws Exception {
        if (this.data == null) {
            throw new Exception("No data available");
        } else if (this.indexData == -1) {
            throw new Exception("Results need to be prepared in advance");
        } else {
            try {
                return (Integer)this.data[this.indexData].get(column.toLowerCase()) == 1;
            } catch (Exception var3) {
                return (Boolean)this.data[this.indexData].get(column.toLowerCase());
            }
        }
    }

    public Timestamp getTimestamp(int column) throws Exception {
        if (this.values == null) {
            throw new Exception("No data available");
        } else if (this.indexData == -1) {
            throw new Exception("Results need to be prepared in advance");
        } else {
            return (Timestamp)this.values[this.indexData][column - 1];
        }
    }

    public Timestamp getTimestamp(String column) throws Exception {
        if (this.data == null) {
            throw new Exception("No data available");
        } else if (this.indexData == -1) {
            throw new Exception("Results need to be prepared in advance");
        } else {
            return (Timestamp)this.data[this.indexData].get(column.toLowerCase());
        }
    }

    public short getShort(int column) throws Exception {
        if (this.values == null) {
            throw new Exception("No data available");
        } else if (this.indexData == -1) {
            throw new Exception("Results need to be prepared in advance");
        } else {
            Object value = this.values[this.indexData][column - 1];
            if (value instanceof Integer) {
                return ((Integer) value).shortValue();
            } else {
                throw new Exception("The value is not an Integer");
            }
        }
    }

    public short getShort(String column) throws Exception {
        if (this.data == null) {
            throw new Exception("No data available");
        } else if (this.indexData == -1) {
            throw new Exception("Results need to be prepared in advance");
        } else {
            Object value = this.data[this.indexData].get(column.toLowerCase());
            if (value instanceof Integer) {
                return ((Integer) value).shortValue();
            } else {
                throw new Exception("The value is not an Integer");
            }
        }
    }
}
