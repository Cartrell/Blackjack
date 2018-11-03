package com.gameplaycoder.thunderjack.engine;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Handler;
import android.widget.ImageView;

import com.gameplaycoder.thunderjack.utils.ColorUtils;

class BackgroundViewManager {
  //=========================================================================
  // members
  //=========================================================================
  private float[] m_hsv;
  private float[] m_rgbScale;
  private ImageView m_backgroundImage;
  private ColorMatrix m_colorMatrix;
  private Handler m_handler;
  private Runnable m_runnable;

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  BackgroundViewManager(ImageView backgroundImage) {
    m_backgroundImage = backgroundImage;

    m_colorMatrix = new ColorMatrix();
    m_rgbScale = new float[ColorUtils.ARRAY_SIZE];
    initHsv();

    setRandomColor();
    initTimerHandler();
    initTimerRunnable();
  }

  //-------------------------------------------------------------------------
  // startAnimation
  //-------------------------------------------------------------------------
  void startAnimation() {
    m_handler.postDelayed(m_runnable, 1000);
  }

  //-------------------------------------------------------------------------
  // stopAnimation
  //-------------------------------------------------------------------------
  void stopAnimation() {
    m_handler.removeCallbacks(m_runnable);
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // applyColorMatrixFilter
  //-------------------------------------------------------------------------
  private void applyColorMatrixFilter() {
    ColorUtils.HSVToRGBScale(m_hsv, m_rgbScale);

    final float ALPHA_SCALE = 1f;
    m_colorMatrix.setScale(
      m_rgbScale[ColorUtils.RED_INDEX],
      m_rgbScale[ColorUtils.GREEN_INDEX],
      m_rgbScale[ColorUtils.BLUE_INDEX],
      ALPHA_SCALE);

    ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(m_colorMatrix);
    m_backgroundImage.setColorFilter(colorFilter);
  }

  //-------------------------------------------------------------------------
  // initHsv
  //-------------------------------------------------------------------------
  private void initHsv() {
    m_hsv = new float[ColorUtils.ARRAY_SIZE];
    m_hsv[ColorUtils.SAT_INDEX] = 1f;
    m_hsv[ColorUtils.VAL_INDEX] = 1f;
  }

  //-------------------------------------------------------------------------
  // initTimerRunnable
  //-------------------------------------------------------------------------
  private void initTimerRunnable() {
    m_runnable = new Runnable() {
      //-------------------------------------------------------------------------
      // run
      //-------------------------------------------------------------------------
      @Override
      public void run() {
        setNextColor();
        startAnimation();
      }
    };
  }

  //-------------------------------------------------------------------------
  // initTimerHandler
  //-------------------------------------------------------------------------
  private void initTimerHandler() {
    m_handler = new Handler();
  }

  //-------------------------------------------------------------------------
  // setNextColor
  //-------------------------------------------------------------------------
  private void setNextColor() {
    float hue = (m_hsv[ColorUtils.HUE_INDEX] + 1) % ColorUtils.MAX_HUE;
    m_hsv[ColorUtils.HUE_INDEX] = hue;
    applyColorMatrixFilter();
  }

  //-------------------------------------------------------------------------
  // setRandomColor
  //-------------------------------------------------------------------------
  private void setRandomColor() {
    m_hsv[ColorUtils.HUE_INDEX] = (int)(Math.random() * ColorUtils.MAX_HUE);
    applyColorMatrixFilter();
  }
}