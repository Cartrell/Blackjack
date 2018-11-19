package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.view.View;

public class ScreenFlashImage extends BaseLayoutComp {
  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public ScreenFlashImage(ConstraintLayout layout) {
    super(layout);
    setIsVisible(false);
  }
}
