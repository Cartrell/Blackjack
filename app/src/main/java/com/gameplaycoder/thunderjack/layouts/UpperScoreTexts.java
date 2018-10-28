package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.widget.TextView;

import com.gameplaycoder.thunderjack.R;

import java.util.ArrayList;

public class UpperScoreTexts extends BaseLayoutComp {
  //=========================================================================
  // members
  //=========================================================================
  public TextView txtUpperMidScore;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public UpperScoreTexts(ConstraintLayout layout) {
    super(layout);
    txtUpperMidScore = getTextView(R.id.txtUpperMidScore);
  }

  //-------------------------------------------------------------------------
  // getGuideLines
  //-------------------------------------------------------------------------
  @Override
  public ArrayList<Guideline> getGuideLines() {
    ArrayList<Guideline> guidelines = new ArrayList<>();
    ConstraintLayout layout = getLayout();
    guidelines.add((Guideline)layout.findViewById(R.id.guideUpperCardsUi));
    guidelines.add((Guideline)layout.findViewById(R.id.guideUpperCardsTop));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidChipsLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidCardsLeft));
    return(guidelines);
  }
}