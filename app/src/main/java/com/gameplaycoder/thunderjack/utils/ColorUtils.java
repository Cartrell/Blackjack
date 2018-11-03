package com.gameplaycoder.thunderjack.utils;

import android.graphics.Color;

public final class ColorUtils {
  //=========================================================================
  // static / const
  //=========================================================================
  public static final int RED_INDEX = 0;
  public static final int GREEN_INDEX = 1;
  public static final int BLUE_INDEX = 2;
  public static final int ARRAY_SIZE = 3;

  public static final int HUE_INDEX = 0;
  public static final int SAT_INDEX = 1;
  public static final int VAL_INDEX = 2;

  public static final int MAX_HUE = 360;

  //-------------------------------------------------------------------------
  // HSVToRGBScale
  //-------------------------------------------------------------------------
  public static void HSVToRGBScale(float[] hsv, float[] rgb_out) {

    if (hsv == null || hsv.length < ARRAY_SIZE || rgb_out == null || rgb_out.length < ARRAY_SIZE) {
      //invalid params
      return;
    }

    int color = Color.HSVToColor(hsv);

    rgb_out[RED_INDEX] = (float)((color & 0xff0000) >> 16) / 255;
    rgb_out[GREEN_INDEX] = (float)((color & 0xff00) >> 8) / 255;
    rgb_out[BLUE_INDEX] = (float)(color & 0xff) / 255;
  }
}
