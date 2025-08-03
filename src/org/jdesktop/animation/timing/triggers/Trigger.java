package org.jdesktop.animation.timing.triggers;

import org.jdesktop.animation.timing.Animator;

public abstract class Trigger {
   public  boolean disarmed;
   public  Animator animator;
   public  Animator reverseAnimator;
   public  TriggerEvent triggerEvent;
   public  boolean autoReverse;

   protected Trigger(Animator animator) {
      this(animator, (TriggerEvent)null);
   }

   protected Trigger(Animator animator, TriggerEvent triggerEvent) {
      this(animator, triggerEvent, false);
   }

   protected Trigger(Animator animator, TriggerEvent triggerEvent, boolean autoReverse) {
      this.disarmed = false;
      this.autoReverse = false;
      this.animator = animator;
      this.triggerEvent = triggerEvent;
      this.autoReverse = autoReverse;
   }

   public void disarm() {
      this.disarmed = true;
   }

   protected void fire(TriggerEvent currentEvent) {
      if (!this.disarmed) {
         float f;
         if (currentEvent == this.triggerEvent) {
            if (this.autoReverse) {
               if (this.animator.isRunning()) {
                  f = this.animator.getTimingFraction();
                  this.animator.stop();
                  this.animator.setStartFraction(f);
               } else {
                  this.animator.setStartFraction(0.0F);
               }
            }

            if (this.animator.isRunning()) {
               this.animator.stop();
            }

            this.animator.setStartDirection(Animator.Direction.FORWARD);
            this.fire();
         } else if (this.triggerEvent != null && currentEvent == this.triggerEvent.getOppositeEvent() && this.autoReverse) {
            if (this.animator.isRunning()) {
               f = this.animator.getTimingFraction();
               this.animator.stop();
               this.animator.setStartFraction(f);
            } else {
               this.animator.setStartFraction(1.0F - this.animator.getStartFraction());
            }

            this.animator.setStartDirection(Animator.Direction.BACKWARD);
            this.fire();
         }

      }
   }

   protected void fire() {
      if (!this.disarmed) {
         if (this.animator.isRunning()) {
            this.animator.stop();
         }

         this.animator.start();
      }
   }
}
