package com.gameplaycoder.thunderjack.engine.thunderjackvfx;

import com.gameplaycoder.thunderjack.engine.IBjEngine;
import com.gameplaycoder.thunderjack.layouts.ScreenFlashImage;
import com.gameplaycoder.thunderjack.utils.timer.Timer;

class ThunderjackVfxFlash {
  //=========================================================================
  // static / const
  //=========================================================================
  private static final long FLASH_DELAY = 30;
  private static final int NUM_FLASHES = 8;

  //=========================================================================
  // members
  //=========================================================================
  private ScreenFlashImage m_screenFlashImage;
  private Timer m_timer;

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  ThunderjackVfxFlash(IBjEngine engine) {
    m_screenFlashImage = engine.getLayoutComps().screenFlashImage;
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
    m_timer = new Timer(FLASH_DELAY, NUM_FLASHES);
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
        m_screenFlashImage.setIsVisible(false);
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
        boolean wasVisible = m_screenFlashImage.isVisible();
        m_screenFlashImage.setIsVisible(!wasVisible);
      }
    });
  }
}