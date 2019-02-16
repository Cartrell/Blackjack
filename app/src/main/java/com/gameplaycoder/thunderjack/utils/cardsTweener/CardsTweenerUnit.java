package com.gameplaycoder.thunderjack.utils.cardsTweener;

import android.view.View;

public class CardsTweenerUnit {
  //=========================================================================
  // members
  //=========================================================================
  private View m_cardImage;

  private float m_xStart;
  private float m_yStart;
  private float m_xAlphaStart;

  private float m_xTarget;
  private float m_yTarget;
  private float m_xAlphaTarget;

  private float m_durationMs;
  private float m_startDelayMs;

  private boolean m_isFirstUpdate;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public CardsTweenerUnit(View cardImage, float xTarget, float yTarget, float alphaTarget,
  float durationMs, float startDelayMs) {
    m_cardImage = cardImage;

    m_xTarget = xTarget;
    m_yTarget = yTarget;
    m_xAlphaTarget = alphaTarget;

    m_durationMs = durationMs;
    m_startDelayMs = startDelayMs;

    m_isFirstUpdate = true;
  }

  //-------------------------------------------------------------------------
  // getAlphaTarget
  //-------------------------------------------------------------------------
  public float getAlphaTarget() {
    return(m_xAlphaTarget);
  }

  //-------------------------------------------------------------------------
  // getCardImage
  //-------------------------------------------------------------------------
  public View getCardImage() {
    return(m_cardImage);
  }

  //-------------------------------------------------------------------------
  // getDuration
  //-------------------------------------------------------------------------
  public float getDuration() {
    return(m_durationMs);
  }

  //-------------------------------------------------------------------------
  // getStartDelay
  //-------------------------------------------------------------------------
  public float getStartDelay() {
    return(m_startDelayMs);
  }

  //-------------------------------------------------------------------------
  // getXTarget
  //-------------------------------------------------------------------------
  public float getXTarget() {
    return(m_xTarget);
  }

  //-------------------------------------------------------------------------
  // getYTarget
  //-------------------------------------------------------------------------
  public float getYTarget() {
    return(m_yTarget);
  }

  //-------------------------------------------------------------------------
  // update
  //-------------------------------------------------------------------------
  public boolean update(float progress) {
    boolean wasFirstUpdate = isFirstUpdate();

    if (wasFirstUpdate) {
      initStartingValues();
    }

    updateProperties(progress);
    return(wasFirstUpdate);
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // initStartingValues
  //-------------------------------------------------------------------------
  private void initStartingValues() {
    m_xStart = m_cardImage.getX();
    m_yStart = m_cardImage.getY();
    m_xAlphaStart = m_cardImage.getAlpha();
  }

  //-------------------------------------------------------------------------
  // isFirstUpdate
  //-------------------------------------------------------------------------
  private boolean isFirstUpdate() {
    boolean prevUpdateStatus = m_isFirstUpdate;
    m_isFirstUpdate = false;
    return(prevUpdateStatus);
  }

  //-------------------------------------------------------------------------
  // lerp
  //-------------------------------------------------------------------------
  private float lerp(float start, float end, float progress) {
    return(progress * (end - start) + start);
  }

  //-------------------------------------------------------------------------
  // updateAlpha
  //-------------------------------------------------------------------------
  private void updateAlpha(float progress) {
    if (!Float.isNaN(m_xAlphaTarget)) {
      float value = lerp(m_xAlphaStart, m_xAlphaTarget, progress);
      m_cardImage.setAlpha(value);
    }
  }

  //-------------------------------------------------------------------------
  // updateProperties
  //-------------------------------------------------------------------------
  private void updateProperties(float progress) {
    progress = Math.min(Math.max(0f, progress), 1f);
    updateAlpha(progress);
    updateX(progress);
    updateY(progress);
  }

  //-------------------------------------------------------------------------
  // updateX
  //-------------------------------------------------------------------------
  private void updateX(float progress) {
    if (!Float.isNaN(m_xTarget)) {
      float value = lerp(m_xStart, m_xTarget, progress);
      m_cardImage.setX(value);
    }
  }

  //-------------------------------------------------------------------------
  // updateY
  //-------------------------------------------------------------------------
  private void updateY(float progress) {
    if (!Float.isNaN(m_yTarget)) {
      float value = lerp(m_yStart, m_yTarget, progress);
      m_cardImage.setY(value);
    }
  }
}