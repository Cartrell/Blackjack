package com.gameplaycoder.thunderjack.engine.thunderjackvfx;

import android.view.View;
import android.widget.ImageView;

import com.gameplaycoder.thunderjack.engine.IBjEngine;
import com.gameplaycoder.thunderjack.layouts.ThunderboltImages;
import com.gameplaycoder.thunderjack.players.PlayerIds;
import com.gameplaycoder.thunderjack.utils.timer.Timer;

class ThunderjackVfxBolt {
  //=========================================================================
  // static / const
  //=========================================================================
  private static final long BOLT_DELAY = 75;

  //=========================================================================
  // members
  //=========================================================================
  private ThunderboltImages m_thunderboltImages;
  private ImageView m_boltImage;
  private Timer m_timer;

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  ThunderjackVfxBolt(IBjEngine engine) {
    m_thunderboltImages = engine.getLayoutComps().thunderboltImages;
    initTimer();
  }

  //-------------------------------------------------------------------------
  // begin
  //-------------------------------------------------------------------------
  void begin(PlayerIds playerId) {
    m_boltImage = getBoltImage(playerId);
    if (m_boltImage == null) {
      return;
    }

    m_thunderboltImages.setIsVisible(true);
    m_timer.start();
    m_boltImage.setVisibility(View.VISIBLE);
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // getBoltImage
  //-------------------------------------------------------------------------
  private ImageView getBoltImage(PlayerIds playerId) {
    switch (playerId) {
      case LEFT_BOTTOM:
        return(m_thunderboltImages.leftBoltImage);

      case MIDDLE_BOTTOM:
        return(m_thunderboltImages.middleBoltImage);

      case RIGHT_BOTTOM:
        return(m_thunderboltImages.rightBoltImage);

      default:
        return(null);
    }
  }

  //-------------------------------------------------------------------------
  // initTimer
  //-------------------------------------------------------------------------
  private void initTimer() {
    m_timer = new Timer(BOLT_DELAY);
    initTimerOnCompleteListener();
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
        m_boltImage.setVisibility(View.INVISIBLE);
        m_thunderboltImages.setIsVisible(false);
      }
    });
  }
}
