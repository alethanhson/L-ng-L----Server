package org.jdesktop.animation.timing;

public interface TimingTarget {
   void timingEvent(float var1);

   void begin();

   void end();

   void repeat();
}
