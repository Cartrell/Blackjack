package com.example.cartrell.blackjack.engine;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

class TextViews {
  //=========================================================================
  // members
  //=========================================================================
  private AppCompatTextView m_txtBet;
  private AppCompatTextView m_txtCredits;
  private AppCompatTextView m_txtTotalWon;


  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // getTotalWonTextView
  //-------------------------------------------------------------------------
  AppCompatTextView getTotalWonTextView() {
    return(m_txtTotalWon);
  }

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  TextViews(ConstraintLayout layout) {
    m_txtCredits = new AppCompatTextView(layout.getContext());
    m_txtCredits.setId(View.generateViewId());
    //layout.addView(m_txtCredits);

    m_txtBet = new AppCompatTextView(layout.getContext());
    m_txtBet.setId(View.generateViewId());
    m_txtBet.setY(m_txtCredits.getY() + m_txtCredits.getHeight());
    layout.addView(m_txtBet);

    m_txtTotalWon = new AppCompatTextView(layout.getContext());
    m_txtTotalWon.setId(View.generateViewId());
    m_txtTotalWon.setY(m_txtBet.getY() + m_txtBet.getHeight());
    //layout.addView(m_txtTotalWon);
  }

  //-------------------------------------------------------------------------
  // setBetValue
  //-------------------------------------------------------------------------
  void setBetValue(int betValue) {
    m_txtBet.setText(String.valueOf(betValue));
  }

  //-------------------------------------------------------------------------
  // setCredits
  //-------------------------------------------------------------------------
  void setCredits(int credits) {
    m_txtCredits.setText(String.valueOf(credits));
  }

  //-------------------------------------------------------------------------
  // setTotalWon
  //-------------------------------------------------------------------------
  void setTotalWon(int totalWon) {
    m_txtTotalWon.setText("WON: " + String.valueOf(totalWon));
  }
}
