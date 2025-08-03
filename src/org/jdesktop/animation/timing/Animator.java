package org.jdesktop.animation.timing;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;
import org.jdesktop.animation.timing.interpolation.Interpolator;
import org.jdesktop.animation.timing.interpolation.LinearInterpolator;

public final class Animator {
   public  TimingSource timer;
   public  TimingSource swingTimer;
   public  Animator.TimingSourceTarget timingSourceTarget;
   public  ArrayList<TimingTarget> targets;
   public  long startTime;
   public  long currentStartTime;
   public  int currentCycle;
   public  boolean intRepeatCount;
   public  boolean timeToStop;
   public  boolean hasBegun;
   public  long pauseBeginTime;
   public  boolean running;
   public  double repeatCount;
   public  int startDelay;
   public  Animator.RepeatBehavior repeatBehavior;
   public  Animator.EndBehavior endBehavior;
   public  int duration;
   public  int resolution;
   public  float acceleration;
   public  float deceleration;
   public  float startFraction;
   public  Animator.Direction direction;
   public  Interpolator interpolator;
   public static final int INFINITE = -1;

   public  void validateRepeatCount(double repeatCount) {
      if (repeatCount < 1.0D && repeatCount != -1.0D) {
         throw new IllegalArgumentException("repeatCount (" + repeatCount + ") cannot be <= 0");
      }
   }

   public Animator(int duration) {
      this(duration, (TimingTarget)null);
   }

   public Animator(int duration, TimingTarget target) {
      this.targets = new ArrayList();
      this.currentCycle = 0;
      this.intRepeatCount = true;
      this.timeToStop = false;
      this.hasBegun = false;
      this.pauseBeginTime = 0L;
      this.running = false;
      this.repeatCount = 1.0D;
      this.repeatBehavior = Animator.RepeatBehavior.REVERSE;
      this.endBehavior = Animator.EndBehavior.HOLD;
      this.resolution = 20;
      this.acceleration = 0.0F;
      this.deceleration = 0.0F;
      this.startFraction = 0.0F;
      this.direction = Animator.Direction.FORWARD;
      this.interpolator = LinearInterpolator.getInstance();
      this.duration = duration;
      this.addTarget(target);
      Toolkit tk = Toolkit.getDefaultToolkit();
      this.swingTimer = new Animator.SwingTimingSource();
      this.timer = this.swingTimer;
   }

   public Animator(int duration, double repeatCount, Animator.RepeatBehavior repeatBehavior, TimingTarget target) {
      this(duration, target);
      this.validateRepeatCount(repeatCount);
      this.repeatCount = repeatCount;
      this.repeatBehavior = repeatBehavior != null ? repeatBehavior : Animator.RepeatBehavior.REVERSE;
      this.intRepeatCount = Math.rint(repeatCount) == repeatCount;
   }

   public Animator.Direction getStartDirection() {
      return this.direction;
   }

   public void setStartDirection(Animator.Direction startDirection) {
      this.throwExceptionIfRunning();
      this.direction = startDirection;
   }

   public Interpolator getInterpolator() {
      return this.interpolator;
   }

   public void setInterpolator(Interpolator interpolator) {
      this.throwExceptionIfRunning();
      this.interpolator = interpolator;
   }

   public void setAcceleration(float acceleration) {
      this.throwExceptionIfRunning();
      if (!(acceleration < 0.0F) && !(acceleration > 1.0F)) {
         if (acceleration > 1.0F - this.deceleration) {
            throw new IllegalArgumentException("Acceleration value cannot be greater than (1 - deceleration)");
         } else {
            this.acceleration = acceleration;
         }
      } else {
         throw new IllegalArgumentException("Acceleration value cannot lie outside [0,1] range");
      }
   }

   public void setDeceleration(float deceleration) {
      this.throwExceptionIfRunning();
      if (!(deceleration < 0.0F) && !(deceleration > 1.0F)) {
         if (deceleration > 1.0F - this.acceleration) {
            throw new IllegalArgumentException("Deceleration value cannot be greater than (1 - acceleration)");
         } else {
            this.deceleration = deceleration;
         }
      } else {
         throw new IllegalArgumentException("Deceleration value cannot lie outside [0,1] range");
      }
   }

   public float getAcceleration() {
      return this.acceleration;
   }

   public float getDeceleration() {
      return this.deceleration;
   }

   public void addTarget(TimingTarget target) {
      if (target != null) {
         synchronized(this.targets) {
            if (!this.targets.contains(target)) {
               this.targets.add(target);
            }
         }
      }

   }

   public void removeTarget(TimingTarget target) {
      synchronized(this.targets) {
         this.targets.remove(target);
      }
   }

   public  void throwExceptionIfRunning() {
      if (this.isRunning()) {
         throw new IllegalStateException("Cannot perform this operation while Animator is running");
      }
   }

   public int getResolution() {
      return this.resolution;
   }

   public void setResolution(int resolution) {
      if (resolution < 0) {
         throw new IllegalArgumentException("resolution must be >= 0");
      } else {
         this.throwExceptionIfRunning();
         this.resolution = resolution;
         this.timer.setResolution(resolution);
      }
   }

   public int getDuration() {
      return this.duration;
   }

   public void setDuration(int duration) {
      this.throwExceptionIfRunning();
      this.duration = duration;
   }

   public double getRepeatCount() {
      return this.repeatCount;
   }

   public void setRepeatCount(double repeatCount) {
      this.validateRepeatCount(repeatCount);
      this.throwExceptionIfRunning();
      this.repeatCount = repeatCount;
   }

   public int getStartDelay() {
      return this.startDelay;
   }

   public void setStartDelay(int startDelay) {
      if (startDelay < 0) {
         throw new IllegalArgumentException("startDelay (" + startDelay + ") cannot be < 0");
      } else {
         this.throwExceptionIfRunning();
         this.startDelay = startDelay;
         this.timer.setStartDelay(startDelay);
      }
   }

   public Animator.RepeatBehavior getRepeatBehavior() {
      return this.repeatBehavior;
   }

   public void setRepeatBehavior(Animator.RepeatBehavior repeatBehavior) {
      this.throwExceptionIfRunning();
      this.repeatBehavior = repeatBehavior != null ? repeatBehavior : Animator.RepeatBehavior.REVERSE;
   }

   public Animator.EndBehavior getEndBehavior() {
      return this.endBehavior;
   }

   public void setEndBehavior(Animator.EndBehavior endBehavior) {
      this.throwExceptionIfRunning();
      this.endBehavior = endBehavior;
   }

   public float getStartFraction() {
      return this.startFraction;
   }

   public void setStartFraction(float startFraction) {
      if (!(startFraction < 0.0F) && !(startFraction > 1.0F)) {
         this.throwExceptionIfRunning();
         this.startFraction = startFraction;
      } else {
         throw new IllegalArgumentException("initialFraction must be between 0 and 1");
      }
   }

   public void start() {
      this.throwExceptionIfRunning();
      this.hasBegun = false;
      this.running = true;
      this.startTime = System.nanoTime() / 1000000L + (long)this.getStartDelay();
      if (this.duration != -1 && (this.direction == Animator.Direction.FORWARD && this.startFraction > 0.0F || this.direction == Animator.Direction.BACKWARD && this.startFraction < 1.0F)) {
         float offsetFraction = this.direction == Animator.Direction.FORWARD ? this.startFraction : 1.0F - this.startFraction;
         long startDelta = (long)((float)this.duration * offsetFraction);
         this.startTime -= startDelta;
      }

      this.currentStartTime = this.startTime;
      this.timer.start();
   }

   public boolean isRunning() {
      return this.running;
   }

   public void stop() {
      this.timer.stop();
      this.end();
      this.timeToStop = false;
      this.running = false;
      this.pauseBeginTime = 0L;
   }

   public void cancel() {
      this.timer.stop();
      this.timeToStop = false;
      this.running = false;
      this.pauseBeginTime = 0L;
   }

   public void pause() {
      if (this.isRunning()) {
         this.pauseBeginTime = System.nanoTime();
         this.running = false;
         this.timer.stop();
      }

   }

   public void resume() {
      if (this.pauseBeginTime > 0L) {
         long pauseDelta = (System.nanoTime() - this.pauseBeginTime) / 1000000L;
         this.startTime += pauseDelta;
         this.currentStartTime += pauseDelta;
         this.timer.start();
         this.pauseBeginTime = 0L;
         this.running = true;
      }

   }

   public  void timingEvent(float fraction) {
      synchronized(this.targets) {
         int i = 0;

         while(true) {
            if (i >= this.targets.size()) {
               break;
            }

            TimingTarget target = (TimingTarget)this.targets.get(i);
            target.timingEvent(fraction);
            ++i;
         }
      }

      if (this.timeToStop) {
         this.stop();
      }

   }

   public  void begin() {
      synchronized(this.targets) {
         for(int i = 0; i < this.targets.size(); ++i) {
            TimingTarget target = (TimingTarget)this.targets.get(i);
            target.begin();
         }

      }
   }

   public  void end() {
      synchronized(this.targets) {
         for(int i = 0; i < this.targets.size(); ++i) {
            TimingTarget target = (TimingTarget)this.targets.get(i);
            target.end();
         }

      }
   }

   public  void repeat() {
      synchronized(this.targets) {
         for(int i = 0; i < this.targets.size(); ++i) {
            TimingTarget target = (TimingTarget)this.targets.get(i);
            target.repeat();
         }

      }
   }

   public  float timingEventPreprocessor(float fraction) {
      if (this.acceleration != 0.0F || this.deceleration != 0.0F) {
         float runRate = 1.0F / (1.0F - this.acceleration / 2.0F - this.deceleration / 2.0F);
         float tdec;
         if (fraction < this.acceleration) {
            tdec = runRate * (fraction / this.acceleration) / 2.0F;
            fraction *= tdec;
         } else if (fraction > 1.0F - this.deceleration) {
            tdec = fraction - (1.0F - this.deceleration);
            float pdec = tdec / this.deceleration;
            fraction = runRate * (1.0F - this.acceleration / 2.0F - this.deceleration + tdec * (2.0F - pdec) / 2.0F);
         } else {
            fraction = runRate * (fraction - this.acceleration / 2.0F);
         }

         if (fraction < 0.0F) {
            fraction = 0.0F;
         } else if (fraction > 1.0F) {
            fraction = 1.0F;
         }
      }

      return this.interpolator.interpolate(fraction);
   }

   public long getTotalElapsedTime(long currentTime) {
      return currentTime - this.startTime;
   }

   public long getTotalElapsedTime() {
      long currentTime = System.nanoTime() / 1000000L;
      return this.getTotalElapsedTime(currentTime);
   }

   public long getCycleElapsedTime(long currentTime) {
      return currentTime - this.currentStartTime;
   }

   public long getCycleElapsedTime() {
      long currentTime = System.nanoTime() / 1000000L;
      return this.getCycleElapsedTime(currentTime);
   }

   public float getTimingFraction() {
      long currentTime = System.nanoTime() / 1000000L;
      long cycleElapsedTime = this.getCycleElapsedTime(currentTime);
      long totalElapsedTime = this.getTotalElapsedTime(currentTime);
      double currentCycle = (double)totalElapsedTime / (double)this.duration;
      if (!this.hasBegun) {
         this.begin();
         this.hasBegun = true;
      }

      float fraction;
      if (this.duration != -1 && this.repeatCount != -1.0D && currentCycle >= this.repeatCount) {
         switch(this.endBehavior) {
         case HOLD:
            if (this.intRepeatCount) {
               if (this.direction == Animator.Direction.BACKWARD) {
                  fraction = 0.0F;
               } else {
                  fraction = 1.0F;
               }
            } else {
               fraction = Math.min(1.0F, (float)cycleElapsedTime / (float)this.duration);
            }
            break;
         case RESET:
            fraction = 0.0F;
            break;
         default:
            fraction = 0.0F;
         }

         this.timeToStop = true;
      } else if (this.duration != -1 && cycleElapsedTime > (long)this.duration) {
         long actualCycleTime = cycleElapsedTime % (long)this.duration;
         fraction = (float)actualCycleTime / (float)this.duration;
         this.currentStartTime = currentTime - actualCycleTime;
         if (this.repeatBehavior == Animator.RepeatBehavior.REVERSE) {
            boolean oddCycles = (int)(cycleElapsedTime / (long)this.duration) % 2 > 0;
            if (oddCycles) {
               this.direction = this.direction == Animator.Direction.FORWARD ? Animator.Direction.BACKWARD : Animator.Direction.FORWARD;
            }

            if (this.direction == Animator.Direction.BACKWARD) {
               fraction = 1.0F - fraction;
            }
         }

         this.repeat();
      } else {
         fraction = 0.0F;
         if (this.duration != -1) {
            fraction = (float)cycleElapsedTime / (float)this.duration;
            if (this.direction == Animator.Direction.BACKWARD) {
               fraction = 1.0F - fraction;
            }

            fraction = Math.min(fraction, 1.0F);
            fraction = Math.max(fraction, 0.0F);
         }
      }

      return this.timingEventPreprocessor(fraction);
   }

   public synchronized void setTimer(TimingSource timer) {
      this.throwExceptionIfRunning();
      if (this.timer != this.swingTimer) {
         this.timer.removeEventListener(this.timingSourceTarget);
      }

      if (timer == null) {
         this.timer = this.swingTimer;
      } else {
         this.timer = timer;
         if (this.timingSourceTarget == null) {
            this.timingSourceTarget = new Animator.TimingSourceTarget();
         }

         timer.addEventListener(this.timingSourceTarget);
      }

      this.timer.setResolution(this.resolution);
      this.timer.setStartDelay(this.startDelay);
   }

   public  class TimerTarget implements ActionListener {
      public  TimerTarget() {
      }

      public void actionPerformed(ActionEvent e) {
         Animator.this.timingEvent(Animator.this.getTimingFraction());
      }

      // $FF: synthetic method
      TimerTarget(Object x1) {
         this();
      }
   }

   public  class SwingTimingSource extends TimingSource {
      Timer timer;

      public SwingTimingSource() {
         this.timer = new Timer(Animator.this.resolution, Animator.this.new TimerTarget());
      }

      public void start() {
         this.timer.start();
      }

      public void stop() {
         this.timer.stop();
      }

      public void setResolution(int resolution) {
         this.timer.setDelay(resolution);
      }

      public void setStartDelay(int delay) {
         this.timer.setInitialDelay(delay);
      }
   }

   class TimingSourceTarget implements TimingEventListener {
      public void timingSourceEvent(TimingSource timingSource) {
         if (Animator.this.timer == timingSource && Animator.this.running) {
            Animator.this.timingEvent(Animator.this.getTimingFraction());
         }

      }
   }

   public static enum RepeatBehavior {
      LOOP,
      REVERSE;
   }

   public static enum Direction {
      FORWARD,
      BACKWARD;
   }

   public static enum EndBehavior {
      HOLD,
      RESET;
   }
}
