package com.gameplaycoder.thunderjack.engine.thunderjackvfx;

import android.view.View;

import com.gameplaycoder.thunderjack.engine.IBjEngine;
import com.gameplaycoder.thunderjack.utils.timer.Timer;

class ThunderjackVfxTremor {
  //=========================================================================
  // static / const
  //=========================================================================
  private static final long DELAY = 35;
  private static final int NUM_REPEATS = 8;
  private static final float TREMOR_RANGE = 10;

  //=========================================================================
  // members
  //=========================================================================
  private View m_view;
  private Timer m_timer;

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  ThunderjackVfxTremor(IBjEngine engine) {
    m_view = engine.getBinding().activityMain;
    initTimer();
  }

  //-------------------------------------------------------------------------
  // begin
  //-------------------------------------------------------------------------
  void begin() {
    m_timer.start();
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // initTimer
  //-------------------------------------------------------------------------
  private void initTimer() {
    m_timer = new Timer(DELAY, NUM_REPEATS);
    initTimerOnCompleteListener();
    initTimerOnTickListener();
  }

  //-------------------------------------------------------------------------
  // initTimerOnCompleteListener
  //-------------------------------------------------------------------------
  private void initTimerOnCompleteListener() {
    m_timer.setOnCompleteListener(new Timer.OnCompleteListener() {
      //-------------------------------------------------------------------------
      // onComplete
      //-------------------------------------------------------------------------
      @Override
      public void onComplete(Timer timer) {
        m_view.setX(0);
        m_view.setY(0);
      }
    });
  }

  //-------------------------------------------------------------------------
  // initTimerOnTickListener
  //-------------------------------------------------------------------------
  private void initTimerOnTickListener() {
    m_timer.setOnTickListener(new Timer.OnTickListener() {
      //-------------------------------------------------------------------------
      // onTick
      //-------------------------------------------------------------------------
      @Override
      public void onTick(Timer timer) {
        float x = -TREMOR_RANGE + (float)Math.floor((float)Math.random() * TREMOR_RANGE);
        m_view.setX(x);

        float y = -TREMOR_RANGE + (float)Math.floor((float)Math.random() * TREMOR_RANGE);
        m_view.setY(y);
      }
    });
  }
}
