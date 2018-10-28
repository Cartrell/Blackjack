package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.widget.AppCompatTextView;
import android.widget.TextView;

import com.gameplaycoder.thunderjack.R;

import java.util.ArrayList;

public class BetAndCreditsTexts extends BaseLayoutComp {
  //=========================================================================
  // members
  //=========================================================================
  public AppCompatTextView txtCredits;
  public AppCompatTextView txtBet;
  public AppCompatTextView txtTotalWon;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public BetAndCreditsTexts(ConstraintLayout layout) {
    super(layout);
    txtCredits = getTextView(R.id.txtCredits);
    txtBet = getTextView(R.id.txtBet);
    txtTotalWon = getTextView(R.id.txtTotalWon);
  }

  //-------------------------------------------------------------------------
  // setBetValue
  //-------------------------------------------------------------------------
  public void setBetValue(int value) {
    setText(txtBet, "BET: " + String.valueOf(value));
  }

  //-------------------------------------------------------------------------
  // setCredits
  //-------------------------------------------------------------------------
  public void setCredits(int value) {
    setText(txtCredits, "CREDITS: " + String.valueOf(value));
  }

  //-------------------------------------------------------------------------
  // setTotalWon
  //-------------------------------------------------------------------------
  public void setTotalWon(int value) {
    setText(txtTotalWon, "TOTAL WON: " + String.valueOf(value));
  }

  //-------------------------------------------------------------------------
  // getGuideLines
  //-------------------------------------------------------------------------
  @Override
  public ArrayList<Guideline> getGuideLines() {
    ArrayList<Guideline> guidelines = new ArrayList<>();
    ConstraintLayout layout = getLayout();
    guidelines.add((Guideline)layout.findViewById(R.id.guideCreditTextsEnd));
    guidelines.add((Guideline)layout.findViewById(R.id.parentLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.parentTop));
    guidelines.add((Guideline)layout.findViewById(R.id.guideUpperCardsUi));
    return(guidelines);
  }
}
