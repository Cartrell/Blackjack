package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.widget.AppCompatTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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
  abstract public ArrayList<Guideline> getGuideLines();

  //-------------------------------------------------------------------------
  // getLayout
  //-------------------------------------------------------------------------
  public ConstraintLayout getLayout() {
    return(m_layout);
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
  public void setText(AppCompatTextView textView, String text) {
    if (textView != null) {
      textView.setText(text);
    }
  }
}
