package org.jdesktop.animation.timing.triggers;

public class FocusTriggerEvent extends TriggerEvent {
   public static final FocusTriggerEvent IN = new FocusTriggerEvent("FocusIn");
   public static final FocusTriggerEvent OUT = new FocusTriggerEvent("FocusOut");

   public  FocusTriggerEvent(String name) {
      super(name);
   }

   public TriggerEvent getOppositeEvent() {
      return this == IN ? OUT : IN;
   }
}
