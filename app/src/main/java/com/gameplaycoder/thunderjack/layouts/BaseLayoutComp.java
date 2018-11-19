package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public abstract class BaseLayoutComp {
  //=========================================================================
  // members
  //=========================================================================
  private ConstraintLayout m_layout;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // getGuideLines
  //-------------------------------------------------------------------------
  public ArrayList<Guideline> getGuideLines() {
    return(null);
  }

  //-------------------------------------------------------------------------
  // getLayout
  //-------------------------------------------------------------------------
  public ConstraintLayout getLayout() {
    return(m_layout);
  }

  //-------------------------------------------------------------------------
  // isVisible
  //-------------------------------------------------------------------------
  public boolean isVisible() {
    return(getLayout().getVisibility() == View.VISIBLE);
  }

  //-------------------------------------------------------------------------
  // setIsVisible
  //-------------------------------------------------------------------------
  public void setIsVisible(boolean isVisible) {
    getLayout().setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
  }

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  BaseLayoutComp(ConstraintLayout layout) {
    m_layout = layout;
  }

  //-------------------------------------------------------------------------
  // getImageButton
  //-------------------------------------------------------------------------
  ImageButton getImageButton(int resourceId) {
    return((ImageButton)m_layout.findViewById(resourceId));
  }

  //-------------------------------------------------------------------------
  // getImageView
  //-------------------------------------------------------------------------
  ImageView getImageView(int resourceId) {
    return((ImageView)m_layout.findViewById(resourceId));
  }

  //-------------------------------------------------------------------------
  // getTextView
  //-------------------------------------------------------------------------
  AppCompatTextView getTextView(int resourceId) {
    return((AppCompatTextView)m_layout.findViewById(resourceId));
  }

  //-------------------------------------------------------------------------
  // getToggleButton
  //-------------------------------------------------------------------------
  ToggleButton getToggleButton(int resourceId) {
    return((ToggleButton)m_layout.findViewById(resourceId));
  }

  //-------------------------------------------------------------------------
  // setText
  //-------------------------------------------------------------------------
  void setText(AppCompatTextView textView, String text) {
    if (textView != null) {
      textView.setText(text);
    }
  }
}
