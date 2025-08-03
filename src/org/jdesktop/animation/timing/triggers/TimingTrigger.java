package org.jdesktop.animation.timing.triggers;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;

public class TimingTrigger extends Trigger implements TimingTarget {
   public  Animator source;
   public  TimingTriggerEvent event;

   public static TimingTrigger addTrigger(Animator source, Animator target, TimingTriggerEvent event) {
      return addTrigger(source, target, event, false);
   }

   public static TimingTrigger addTrigger(Animator source, Animator target, TimingTriggerEvent event, boolean autoReverse) {
      TimingTrigger trigger = new TimingTrigger(target, event, autoReverse);
      source.addTarget(trigger);
      return trigger;
   }

   public TimingTrigger(Animator animator, TimingTriggerEvent event) {
      this(animator, event, false);
   }

   public TimingTrigger(Animator animator, TimingTriggerEvent event, boolean autoReverse) {
      super(animator, event, autoReverse);
   }

   public void timingEvent(float fraction) {
   }

   public void begin() {
      this.fire(TimingTriggerEvent.START);
   }

   public void end() {
      this.fire(TimingTriggerEvent.STOP);
   }

   public void repeat() {
      this.fire(TimingTriggerEvent.REPEAT);
   }
}
