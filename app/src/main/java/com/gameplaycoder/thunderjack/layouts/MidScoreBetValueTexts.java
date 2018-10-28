package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.widget.TextView;

import com.gameplaycoder.thunderjack.R;

import java.util.ArrayList;

public class MidScoreBetValueTexts extends BaseLayoutComp {
  //=========================================================================
  // members
  //=========================================================================
  public TextView txtMidLeftBetValue;
  public TextView txtMidMidBetValue;
  public TextView txtMidRightBetValue;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public MidScoreBetValueTexts(ConstraintLayout layout) {
    super(layout);
    txtMidLeftBetValue = getTextView(R.id.txtMidLeftBetValue);
    txtMidMidBetValue = getTextView(R.id.txtMidMidBetValue);
    txtMidRightBetValue = getTextView(R.id.txtMidRightBetValue);
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