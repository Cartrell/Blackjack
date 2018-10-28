package com.gameplaycoder.thunderjack.utils;

import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public final class Metrics {

  //=========================================================================
  // static / const
  //=========================================================================
  private static float sm_layoutWidth;
  private static float sm_layoutHeight;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // CalcGuidelinePosition
  //-------------------------------------------------------------------------
  public static float CalcGuidelinePosition(Guideline guideline) {
    if (guideline == null) {
      return(0);
    }

    float position;
    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)guideline.getLayoutParams();
    if (isGuidelineHorizontal(guideline)) {
      //position = Resources.getSystem().getDisplayMetrics().heightPixels * params.guidePercent;
      position = sm_layoutHeight * params.guidePercent;
    } else {
      //position = Resources.getSystem().getDisplayMetrics().widthPixels * params.guidePercent;
      position = sm_layoutWidth * params.guidePercent;
    }

    return(position);
  }

  //-------------------------------------------------------------------------
  // CalcSize
  //-------------------------------------------------------------------------
  public static Point CalcSize(ImageView imageView, float xyHigh, float xyLow, boolean resizeByWidth) {
    Drawable drawable = resolveDrawable(imageView);
    if (drawable == null) {
      Log.w("Metrics", "CalcSize. Warning: No drawable found.");
      return(null);
    }

    int width;
    int height;
    float scale;

    if (resizeByWidth) {
      width = (int)(xyHigh - xyLow);
      scale = (float)width / (float)drawable.getIntrinsicWidth();
      height = (int)((float)drawable.getIntrinsicHeight() * scale);
    } else {
      height = (int)(xyHigh - xyLow);
      scale = (float)height / (float)drawable.getIntrinsicHeight();
      width = (int)((float)drawable.getIntrinsicWidth() * scale);
    }

    return(new Point(width, height));
  }

  //-------------------------------------------------------------------------
  // CalcSize
  //-------------------------------------------------------------------------
  /*
  public static Point CalcSize(ToggleButton view, float xyHigh, float xyLow, boolean resizeByWidth) {
    Drawable drawable = resolveDrawable(view);
    if (drawable == null) {
      Log.w("Metrics", "CalcSize. Warning: No drawable found.");
      return(null);
    }

    int width;
    int height;
    float scale;

    if (resizeByWidth) {
      width = (int)(xyHigh - xyLow);
      scale = (float)width / (float)drawable.getIntrinsicWidth();
      height = (int)((float)drawable.getIntrinsicHeight() * scale);
    } else {
      height = (int)(xyHigh - xyLow);
      scale = (float)height / (float)drawable.getIntrinsicHeight();
      width = (int)((float)drawable.getIntrinsicWidth() * scale);
    }

    return(new Point(width, height));
  }
*/

  //-------------------------------------------------------------------------
  // CalcSize
  //-------------------------------------------------------------------------
  /*
  public static Point CalcSize(TextView view) {
    if (view == null) {
      Log.w("Metrics", "CalcSize. Warning: No TextView specified.");
      return(null);
    }

    view.measure(0, 0);
    return(new Point(view.getMeasuredWidth(), view.getMeasuredHeight()));
  }
  */
/*
  //-------------------------------------------------------------------------
  // CalcSize
  //-------------------------------------------------------------------------
  public static Point CalcSize(ImageView view, Guideline high, Guideline low) {
    float min = CalcGuidelinePosition(low);
    float max = CalcGuidelinePosition(high);
    boolean resizeByWidth = Metrics.isGuidelineHorizontal(high);
    return(CalcSize(view, max, min, resizeByWidth));
  }
*/

  /*
  //-------------------------------------------------------------------------
  // CalcSize
  //-------------------------------------------------------------------------
  public static Point CalcSize(ImageView view, View baseViewHigh, View baseViewLow, boolean resizeByWidth) {
    float min;
    float max;

    if (resizeByWidth) {
      max = baseViewHigh != null ? baseViewHigh.getX() : 0;
      min = baseViewLow != null ? baseViewLow.getX() : 0;
    } else {
      max = baseViewHigh != null ? baseViewHigh.getY() : 0;
      min = baseViewLow != null ? baseViewLow.getY() : 0;
    }

    return(CalcSize(view, max, min, resizeByWidth));
  }
*/

  /*
  //-------------------------------------------------------------------------
  // CalcSize
  //-------------------------------------------------------------------------
  public static Point CalcSize(ToggleButton view, View baseViewHigh, View baseViewLow, boolean resizeByWidth) {
    float min;
    float max;

    if (resizeByWidth) {
      max = baseViewHigh != null ? baseViewHigh.getX() : 0;
      min = baseViewLow != null ? baseViewLow.getX() : 0;
    } else {
      max = baseViewHigh != null ? baseViewHigh.getY() : 0;
      min = baseViewLow != null ? baseViewLow.getY() : 0;
    }

    return(CalcSize(view, max, min, resizeByWidth));
  }
*/

  //-------------------------------------------------------------------------
  // SetLayoutSize
  //-------------------------------------------------------------------------
  public static void SetLayoutSize(float width, float height) {
    sm_layoutWidth = width;
    sm_layoutHeight = height;
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // calcDrawableSize
  //-------------------------------------------------------------------------
  /*
  public static Point calcDrawableSize(Drawable drawable, float xyHigh, float xyLow, boolean resizeByWidth) {
    if (drawable == null) {
      Log.w("Metrics", "calcDrawableSize. Warning: No drawable found.");
      return(null);
    }

    int width;
    int height;
    float scale;

    if (resizeByWidth) {
      width = (int)(xyHigh - xyLow);
      scale = (float)width / (float)drawable.getIntrinsicWidth();
      height = (int)((float)drawable.getIntrinsicHeight() * scale);
    } else {
      height = (int)(xyHigh - xyLow);
      scale = (float)height / (float)drawable.getIntrinsicHeight();
      width = (int)((float)drawable.getIntrinsicWidth() * scale);
    }

    return(new Point(width, height));
  }
  */

  //-------------------------------------------------------------------------
  // isGuidelineHorizontal
  //-------------------------------------------------------------------------
  private static boolean isGuidelineHorizontal(Guideline guideline) {
    if (guideline == null) {
      return(false);
    }

    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)guideline.getLayoutParams();
    return(params.orientation == ConstraintLayout.LayoutParams.HORIZONTAL);
  }

  //-------------------------------------------------------------------------
  // resolveDrawable
  //-------------------------------------------------------------------------
  private static Drawable resolveDrawable(ImageView view) {
    if (view == null) {
      return(null);
    }

    Drawable drawable = view.getDrawable();
    if (drawable == null) {
      drawable = view.getBackground();
    }

    return(drawable);
  }

  /*
  //-------------------------------------------------------------------------
  // resolveDrawable
  //-------------------------------------------------------------------------
  private static Drawable resolveDrawable(ToggleButton view) {
    return(view != null ? view.getBackground() : null);
  }
  */
}