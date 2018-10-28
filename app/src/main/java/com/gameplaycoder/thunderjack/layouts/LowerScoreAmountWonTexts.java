package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.widget.TextView;

import com.gameplaycoder.thunderjack.R;

import java.util.ArrayList;

public class LowerScoreAmountWonTexts extends BaseLayoutComp {
  //=========================================================================
  // members
  //=========================================================================
  public TextView txtLowerLeftAmountWon;
  public TextView txtLowerMidAmountWon;
  public TextView txtLowerRightAmountWon;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public LowerScoreAmountWonTexts(ConstraintLayout layout) {
    super(layout);
    txtLowerLeftAmountWon = getTextView(R.id.txtLowerLeftAmountWon);
    txtLowerMidAmountWon = getTextView(R.id.txtLowerMidAmountWon);
    txtLowerRightAmountWon = getTextView(R.id.txtLowerRightAmountWon);
  }

  //-------------------------------------------------------------------------
  // getGuideLines
  //-------------------------------------------------------------------------
  @Override
  public ArrayList<Guideline> getGuideLines() {
    ArrayList<Guideline> guidelines = new ArrayList<>();
    ConstraintLayout layout = getLayout();
    guidelines.add((Guideline)layout.findViewById(R.id.guideBottomPanel));
    guidelines.add((Guideline)layout.findViewById(R.id.guideLowerChipsTop));
    guidelines.add((Guideline)layout.findViewById(R.id.guideLeftCardsRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidChipsLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidCardsRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideRightChipsLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideRightCardsRight));
    return(guidelines);
  }
}