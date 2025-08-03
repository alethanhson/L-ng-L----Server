package org.jdesktop.animation.timing.triggers;

public class TriggerEvent {
   public  String name;

   protected TriggerEvent(String name) {
      this.name = name;
   }

   public TriggerEvent getOppositeEvent() {
      return this;
   }
}
