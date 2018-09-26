package com.gameplaycoder.thunderjack.engine;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.widget.ImageView;

class BackgroundViewManager {
  //=========================================================================
  // const
  //=========================================================================

  //=========================================================================
  // members
  //=========================================================================
  private ImageView m_backgroundImage;
  private ColorMatrix m_colorMatrix;
  private float m_redScale;
  private float m_greenScale;
  private float m_blueScale;

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  BackgroundViewManager(ImageView backgroundImage) {
    m_colorMatrix = new ColorMatrix();
    m_backgroundImage = backgroundImage;
    setRandomColor();
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // applyColorMatrixFilter
  //-------------------------------------------------------------------------
  private void applyColorMatrixFilter() {
    final float ALPHA_SCALE = 1.0f;
    m_colorMatrix.setScale(m_redScale, m_greenScale, m_blueScale, ALPHA_SCALE);
    ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(m_colorMatrix);
    m_backgroundImage.setColorFilter(colorFilter);
  }

  //-------------------------------------------------------------------------
  // setRandomColor
  //-------------------------------------------------------------------------
  private void setRandomColor() {
    m_redScale = 1.0f + (float)Math.random();
    m_greenScale = 1.0f + (float)Math.random();
    m_blueScale = 1.0f + (float)Math.random();
    applyColorMatrixFilter();
  }
}