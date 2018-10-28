package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.widget.TextView;

import com.gameplaycoder.thunderjack.R;

import java.util.ArrayList;

public class MidScoreAmountWonTexts extends BaseLayoutComp {
  //=========================================================================
  // members
  //=========================================================================
  public TextView txtMidLeftAmountWon;
  public TextView txtMidMidAmountWon;
  public TextView txtMidRightAmountWon;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public MidScoreAmountWonTexts(ConstraintLayout layout) {
    super(layout);
    txtMidLeftAmountWon = getTextView(R.id.txtMidLeftAmountWon);
    txtMidMidAmountWon = getTextView(R.id.txtMidMidAmountWon);
    txtMidRightAmountWon = getTextView(R.id.txtMidRightAmountWon);
  }

  //-------------------------------------------------------------------------
  // getGuideLines
  //-------------------------------------------------------------------------
  @Override
  public ArrayList<Guideline> getGuideLines() {
    ArrayList<Guideline> guidelines = new ArrayList<>();
    ConstraintLayout layout = getLayout();
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidCardsBottom));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidChipsTop));
    guidelines.add((Guideline)layout.findViewById(R.id.guideLeftCardsRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidChipsLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidCardsRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideRightChipsLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideRightCardsRight));
    return(guidelines);
  }
}