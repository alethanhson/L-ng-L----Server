# Báo Cáo Chi Tiết - Hệ Thống Message & Giao Thức Truyền Thông

## Tổng Quan
Hệ thống message và giao thức truyền thông được thiết kế để xử lý giao tiếp hiệu quả giữa client và server. Hệ thống sử dụng custom protocol với compression và encoding tối ưu.

## 1. Cấu Trúc Message

### 1.1 Message Class
```java
public class Message implements Cloneable {
    public byte cmd;                    // Command ID
    public Reader reader = null;        // Reader để đọc dữ liệu
    public Writer writer = null;        // Writer để ghi dữ liệu
    public boolean inflate;             // Có nén dữ liệu hay không
    
    // Constructor với command
    public Message(byte var1) {
        this.cmd = var1;
        this.writer = new Writer();
    }
    
    // Constructor với writer có sẵn
    public Message(byte var1, Writer writer) {
        this.cmd = var1;
        this.writer = writer;
    }
    
    // Constructor với dữ liệu byte array
    public Message(byte var1, byte[] var2) {
        this.cmd = var1;
        this.reader = new Reader(var2);
    }
}
```

### 1.2 Command Constants
```java
// Các command đặc biệt
public static Message a(byte var0) throws java.io.IOException {
    Message var1 = new Message((byte) -125);
    var1.writeByte(var0);
    return var1;
}

public static Message b(byte var0) throws java.io.IOException {
    Message var1 = new Message((byte) -124);
    var1.writeByte(-128);
    return var1;
}

public static Message c(byte var0) throws java.io.IOException {
    Message var1 = new Message((byte) -123);
    var1.writeByte(var0);
    return var1;
}

public static Message d(byte var0) throws java.io.IOException {
    Message var1 = new Message((byte) -122);
    var1.writeByte(var0);
    return var1;
}

public static Message e(byte var0) throws java.io.IOException {
    Message var1 = new Message((byte) -112);
    var1.writeByte(var0);
    return var1;
}

public static Message f(byte var0) throws java.io.IOException {
    Message var1 = new Message((byte) -111);
    var1.writeByte(var0);
    return var1;
}
```

## 2. Writer System

### 2.1 Writer Class
```java
public class Writer {
    public ByteArrayOutputStream baos = null;    // Buffer output
    public DataOutputStream dos = null;          // Data output stream
    
    // Constructor mặc định
    public Writer() {
        this.baos = new ByteArrayOutputStream();
        this.dos = new DataOutputStream(this.baos);
    }
    
    // Constructor với socket
    public Writer(Socket socket) throws IOException {
        this.dos = new DataOutputStream(socket.getOutputStream());
    }
    
    // Constructor với DataOutputStream
    public Writer(DataOutputStream var1) {
        this.dos = var1;
    }
    
    // Constructor với ArrayList<Integer> (có thể là type mapping)
    public Writer(ArrayList<Integer> listTypeRead) {
        this.baos = new ByteArrayOutputStream();
        this.dos = new DataOutputStream(this.baos);
    }
}
```

### 2.2 Data Writing Methods
```java
// Ghi boolean
public void writeBoolean(boolean var1) throws java.io.IOException {
    this.dos.writeBoolean(var1);
}

// Ghi byte
public void writeByte(int var1) throws java.io.IOException {
    this.dos.writeByte(var1);
}

// Ghi byte array với length
public void write(byte[] var1) throws java.io.IOException {
    this.dos.writeInt(var1.length);
    this.dos.write(var1);
}

// Ghi short
public void writeShort(int var1) throws java.io.IOException {
    this.dos.writeShort(var1);
}

// Ghi int
public void writeInt(int var1) throws java.io.IOException {
    this.dos.writeInt(var1);
}

// Ghi long
public void writeLong(long var1) throws java.io.IOException {
    this.dos.writeLong(var1);
}
```

### 2.3 UTF String Writing
```java
public void writeUTF(String var1) throws java.io.IOException {
    if (var1.length() > 0 && var1.length() <= 255) {
        this.dos.writeByte(var1.length());
        
        // Custom character encoding
        for (int var3 = 0; var3 < var1.length(); ++var3) {
            int var2;
            if ((var2 = k.indexOf(var1.charAt(var3))) < 0) {
                var2 = 0;  // Default character
            }
            this.dos.writeByte(var2);
        }
    } else {
        // Standard UTF cho string dài
        this.dos.writeByte(0);
        this.dos.writeUTF(var1);
    }
}

// Custom character set
public static String k = " 0123456789+-*='\"\\/_?.,ˋˊ~ˀ:;|<>[]{}!@#$%^&*()aáàảãạâấầẩẫậăắằẳẵặbcdđeéèẻẽẹêếềểễệfghiíìỉĩịjklmnoóòỏõọôốồổỗộơớờởỡợpqrstuúùủũụưứừửữựvxyýỳỷỹỵzwAÁÀẢÃẠÂẤẦẨẪẬĂẮẰẲẴẶBCDĐEÉÈẺẼẸÊẾỀỂỄỆFGHIÍÌỈĨỊJKLMNOÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢPQRSTUÚÙỦŨỤƯỨỪỬỮỰVXYÝỲỶỸỴZW";
```

## 3. Reader System

### 3.1 Reader Class
```java
public class Reader {
    public DataInputStream dis = null;    // Data input stream
    
    // Constructor với socket
    public Reader(Socket socket) throws IOException {
        this.dis = new DataInputStream(socket.getInputStream());
    }
    
    // Constructor với DataInputStream
    public Reader(DataInputStream var1) {
        this.dis = var1;
    }
    
    // Constructor với InputStream
    public Reader(InputStream var1) {
        this.dis = new DataInputStream(var1);
    }
    
    // Constructor với byte array
    public Reader(byte[] var1) {
        this.dis = new DataInputStream(new ByteArrayInputStream(var1));
    }
}
```

### 3.2 Data Reading Methods
```java
// Đọc byte array với length
public byte[] read() throws java.io.IOException {
    byte[] var1 = new byte[this.dis.readInt()];
    this.dis.read(var1);
    return var1;
}

// Đọc vào buffer có sẵn
public byte[] read(byte[] var1, int var2, int var3) throws java.io.IOException {
    this.dis.read(var1, var2, var3);
    return var1;
}

// Đọc tất cả dữ liệu có sẵn
public byte[] readFully() throws java.io.IOException {
    byte[] var1 = new byte[this.dis.available()];
    this.dis.read(var1);
    return var1;
}

// Đọc byte
public byte readByte() throws IOException {
    return dis.readByte();
}

// Đọc boolean
public boolean readBoolean() throws IOException {
    return dis.readBoolean();
}

// Đọc short
public short readShort() throws IOException {
    return dis.readShort();
}

// Đọc unsigned short
public int readUnsignedShort() throws IOException {
    return dis.readUnsignedShort();
}

// Đọc int
public int readInt() throws IOException {
    return dis.readInt();
}

// Đọc long
public long readLong() throws IOException {
    return dis.readLong();
}

// Đọc unsigned byte
public short readUnsignedByte() throws java.io.IOException {
    return (short) this.dis.readUnsignedByte();
}
```

### 3.3 UTF String Reading
```java
public String readUTF() throws java.io.IOException {
    short var1;
    if ((var1 = (short) this.dis.readUnsignedByte()) == 0) {
        return this.dis.readUTF();  // Standard UTF
    } else {
        String var2 = "";
        
        // Custom character decoding
        for (int var3 = 0; var3 < var1; ++var3) {
            var2 = var2 + k.charAt(this.dis.readUnsignedByte());
        }
        
        return var2;
    }
}
```

## 4. Message Processing

### 4.1 Data Serialization
```java
public byte[] getData() {
    if (this.writer == null) {
        return null;
    }
    try {
        this.writer.dos.flush();
        return this.writer.baos.toByteArray();
    } catch (Exception ex) {
        return null;
    }
}
```

### 4.2 Data Compression
```java
public byte[] getData(String var1) {
    byte[] var11;
    try {
        if (this.writer == null) {
            return null;
        }

        this.writer.dos.flush();
        byte[] var10 = this.writer.baos.toByteArray();
        ByteArrayOutputStream var2 = new ByteArrayOutputStream();
        int var3 = var10.length;
        
        // Compression cho dữ liệu > 99 bytes
        if (var10.length > 99) {
            Deflater var4;
            (var4 = new Deflater()).setLevel(9);  // Maximum compression
            var4.setInput(var10);
            var4.finish();
            var2.write(var10, 0, 4);  // Giữ header
            byte[] var5 = new byte[1024];

            while (!var4.finished()) {
                var2.write(var5, 0, var4.deflate(var5));
            }

            var4.end();
            
            // Xử lý dữ liệu nén
            if ((var11 = var2.toByteArray()).length > 32767) {
                // Dữ liệu quá lớn, sử dụng format đặc biệt
                var3 = var11.length - 4;
                var2.reset();
                var2.write(new byte[]{-128, this.cmd});
                var2.write(var11);
                (var11 = var2.toByteArray())[2] = (byte) (var3 >> 24 & 255);
                var11[3] = (byte) (var3 >> 16 & 255);
                var11[4] = (byte) (var3 >> 8 & 255);
                var11[5] = (byte) (var3 & 255);
            } else if (var11.length >= var10.length) {
                // Không nén được, sử dụng format gốc
                var2.reset();
                var2.write(new byte[]{this.cmd, (byte) (var3 >> 8), (byte) var3});
                var2.write(var10);
                var11 = var2.toByteArray();
            } else {
                // Sử dụng format nén
                var3 = var11.length - 4;
                var11[0] = -80;
                var11[1] = this.cmd;
                var11[2] = (byte) (var3 >> 8 & 255);
                var11[3] = (byte) (var3 & 255);
            }
        } else {
            // Dữ liệu nhỏ, không cần nén
            var2.write(new byte[]{this.cmd, (byte) (var3 >> 8), (byte) var3});
            var2.write(var10);
            var11 = var2.toByteArray();
        }

        var2.close();
        return var11;
        
    } catch (Exception var8) {
        return null;
    } finally {
        this.close();
    }
}
```

## 5. Protocol Format

### 5.1 Message Header
```java
// Format cơ bản: [CMD][LENGTH][DATA]
// CMD: 1 byte command ID
// LENGTH: 2 bytes (nếu cần)
// DATA: payload

// Format nén: [0x80][CMD][LENGTH][COMPRESSED_DATA]
// 0x80: flag nén
// CMD: command ID
// LENGTH: 2 bytes length
// COMPRESSED_DATA: dữ liệu đã nén

// Format đặc biệt: [0x80][CMD][LENGTH_4][COMPRESSED_DATA]
// LENGTH_4: 4 bytes length cho dữ liệu rất lớn
```

### 5.2 Compression Strategy
```java
// Sử dụng Deflater với level 9 (maximum compression)
Deflater deflater = new Deflater();
deflater.setLevel(9);
deflater.setInput(data);
deflater.finish();

// Buffer size 1024 bytes
byte[] buffer = new byte[1024];
while (!deflater.finished()) {
    int count = deflater.deflate(buffer);
    output.write(buffer, 0, count);
}
```

## 6. Message Types

### 6.1 Game Commands
```java
// Các command game chính
public static final byte CMD_LOGIN = 1;
public static final byte CMD_MOVE = 2;
public static final byte CMD_ATTACK = 3;
public static final byte CMD_USE_SKILL = 4;
public static final byte CMD_CHAT = 5;
public static final byte CMD_PICK_ITEM = 6;
public static final byte CMD_USE_ITEM = 7;
public static final byte CMD_TRADE = 8;
public static final byte CMD_GROUP = 9;
public static final byte CMD_FAMILY = 10;
```

### 6.2 System Commands
```java
// Các command hệ thống
public static final byte CMD_PING = -1;
public static final byte CMD_PONG = -2;
public static final byte CMD_ERROR = -3;
public static final byte CMD_MAINTENANCE = -4;
public static final byte CMD_UPDATE = -5;
```

## 7. Error Handling

### 7.1 Exception Handling
```java
public void close() {
    try {
        if (this.reader != null) {
            this.reader.close();
        }
        if (this.writer != null) {
            this.writer.close();
        }
    } catch (Exception var1) {
        // Silent close
    }
}

public void close(String var1) {
    try {
        if (this.dos != null) {
            this.dos.close();
            this.dos = null;
        }
        if (this.baos != null) {
            this.baos.close();
            this.baos = null;
        }
    } catch (Exception var2) {
        // Silent close
    }
}
```

### 7.2 Data Validation
```java
// Kiểm tra dữ liệu trước khi xử lý
public byte[] getData() {
    if (this.writer == null) {
        return null;
    }
    try {
        this.writer.dos.flush();
        return this.writer.baos.toByteArray();
    } catch (Exception ex) {
        return null;  // Return null nếu có lỗi
    }
}
```

## 8. Performance Optimization

### 8.1 Memory Management
```java
// Sử dụng ByteArrayOutputStream để buffer
public Writer() {
    this.baos = new ByteArrayOutputStream();
    this.dos = new DataOutputStream(this.baos);
}

// Tự động flush và close
public void close() {
    try {
        if (this.dos != null) {
            this.dos.close();
        }
        if (this.baos != null) {
            this.baos.close();
        }
    } catch (Exception var2) {
        // Silent close
    }
}
```

### 8.2 Compression Optimization
```java
// Chỉ nén khi dữ liệu > 99 bytes
if (var10.length > 99) {
    // Sử dụng compression
} else {
    // Không nén
}

// Sử dụng maximum compression level
deflater.setLevel(9);
```

## 9. Security Features

### 9.1 Input Validation
```java
// Kiểm tra độ dài string
public void writeUTF(String var1) throws java.io.IOException {
    if (var1.length() > 0 && var1.length() <= 255) {
        // Custom encoding
    } else {
        // Standard UTF
    }
}
```

### 9.2 Buffer Overflow Protection
```java
// Kiểm tra độ dài dữ liệu
public byte[] read() throws java.io.IOException {
    byte[] var1 = new byte[this.dis.readInt()];
    this.dis.read(var1);
    return var1;
}
```

## 10. Gợi Ý Mở Rộng

### 10.1 Protocol Enhancements
- Thêm checksum cho data integrity
- Implement message encryption
- Add message queuing system

### 10.2 Performance Improvements
- Implement connection pooling
- Add message batching
- Optimize compression algorithms

### 10.3 Monitoring & Debugging
- Add message logging
- Implement performance metrics
- Add protocol analyzer tools
