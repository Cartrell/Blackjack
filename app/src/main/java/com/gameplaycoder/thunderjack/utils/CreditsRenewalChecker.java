package com.gameplaycoder.thunderjack.utils;

import android.util.Log;

import com.gameplaycoder.thunderjack.R;
import com.gameplaycoder.thunderjack.engine.IBjEngine;

import java.util.Date;

public class CreditsRenewalChecker {
  //=========================================================================
  // static / const
  //=========================================================================

  //=========================================================================
  // members
  //=========================================================================
  private IBjEngine m_engine;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public CreditsRenewalChecker(IBjEngine engine) {
    m_engine = engine;
  }

  //-------------------------------------------------------------------------
  // isEligibleForCreditsRenewal
  //-------------------------------------------------------------------------
  public boolean isEligibleForCreditsRenewal(int credits) {
    return(
      areCreditsLowEnough(credits) &&
      hasEnoughTimePassed());
  }


  //-------------------------------------------------------------------------
  // shouldTimeBeUpdated
  //-------------------------------------------------------------------------
  public boolean shouldTimeBeUpdated(int credits) {
    if (!areCreditsLowEnough(credits)) {
      return(false);
    }

    long lastGameClosedTime = m_engine.getSettings().getLastGameClosedTimeWithLowCredits();
    return(lastGameClosedTime == 0);
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // areCreditsLowEnough
  //-------------------------------------------------------------------------
  private boolean areCreditsLowEnough(int credits) {
    int lowCreditsThreshold = Integer.parseInt(m_engine.getContext().getString(R.string.lowCreditsThreshold));
    return(credits <= lowCreditsThreshold);
  }

  //-------------------------------------------------------------------------
  // hasEnoughTimePassed
  //-------------------------------------------------------------------------
  private boolean hasEnoughTimePassed() {
    long currentTime = new Date().getTime();
    long lastGameClosedTime = m_engine.getSettings().getLastGameClosedTimeWithLowCredits();
    long renewalTime = Long.parseLong(m_engine.getContext().getString(R.string.renewalTime));
    long elapsedTime = currentTime - lastGameClosedTime;
    Log.i(CreditsRenewalChecker.class.getName(),currentTime + ", " + lastGameClosedTime + ", " + renewalTime);
    return(elapsedTime >= renewalTime);
  }
}
