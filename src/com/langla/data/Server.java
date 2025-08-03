package com.langla.data;

public class Server {
   public short id;
   public String name;
   public String ip;
   public short port;
   public short portCheck;
   public String STR_STATUS = "";
   public String TEXT = "";
   public boolean SELECT;

   public Server cloneServer() {
      Server var1;
      (var1 = new Server()).id = this.id;
      var1.name = this.name;
      var1.ip = this.ip;
      var1.port = this.port;
      var1.portCheck = this.portCheck;
      var1.STR_STATUS = this.STR_STATUS;
      var1.TEXT = this.TEXT;
      return var1;
   }

   public String getName() {
      String var1 = this.name.trim();
      if (this.STR_STATUS.length() > 0) {
         var1 = var1 + " (" + this.STR_STATUS + ")";
      }

      return var1;
   }

   
   public Object clone() {
      return this.cloneServer();
   }
}
