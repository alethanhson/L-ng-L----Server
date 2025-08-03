package org.jdesktop.animation.timing.triggers;

public class TimingTriggerEvent extends TriggerEvent {
   public static final TimingTriggerEvent START = new TimingTriggerEvent("Start");
   public static final TimingTriggerEvent STOP = new TimingTriggerEvent("Stop");
   public static final TimingTriggerEvent REPEAT = new TimingTriggerEvent("Repeat");

   public  TimingTriggerEvent(String name) {
      super(name);
   }

   public TriggerEvent getOppositeEvent() {
      if (this.equals(START)) {
         return STOP;
      } else {
         return this.equals(STOP) ? START : this;
      }
   }
}
