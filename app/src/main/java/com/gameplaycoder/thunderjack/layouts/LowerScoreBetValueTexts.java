package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.widget.TextView;

import com.gameplaycoder.thunderjack.R;

import java.util.ArrayList;

public class LowerScoreBetValueTexts extends BaseLayoutComp {
  //=========================================================================
  // members
  //=========================================================================
  public TextView txtLowerLeftBetValue;
  public TextView txtLowerMidBetValue;
  public TextView txtLowerRightBetValue;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public LowerScoreBetValueTexts(ConstraintLayout layout) {
    super(layout);
    txtLowerLeftBetValue = getTextView(R.id.txtLowerLeftBetValue);
    txtLowerMidBetValue = getTextView(R.id.txtLowerMidBetValue);
    txtLowerRightBetValue = getTextView(R.id.txtLowerRightBetValue);
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