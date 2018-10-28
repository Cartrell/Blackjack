package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.widget.TextView;

import com.gameplaycoder.thunderjack.R;

import java.util.ArrayList;

public class LowerScoreTexts extends BaseLayoutComp {
  //=========================================================================
  // members
  //=========================================================================
  public TextView txtLowerLeftScore;
  public TextView txtLowerMidScore;
  public TextView txtLowerRightScore;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public LowerScoreTexts(ConstraintLayout layout) {
    super(layout);
    txtLowerLeftScore = getTextView(R.id.txtLowerLeftScore);
    txtLowerMidScore = getTextView(R.id.txtLowerMidScore);
    txtLowerRightScore = getTextView(R.id.txtLowerRightScore);
  }

  //-------------------------------------------------------------------------
  // getGuideLines
  //-------------------------------------------------------------------------
  @Override
  public ArrayList<Guideline> getGuideLines() {
    ArrayList<Guideline> guidelines = new ArrayList<>();
    ConstraintLayout layout = getLayout();
    guidelines.add((Guideline)layout.findViewById(R.id.guideLowerCardsUi));
    guidelines.add((Guideline)layout.findViewById(R.id.guideLowerCardsTop));
    guidelines.add((Guideline)layout.findViewById(R.id.guideLeftChipsLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidChipsLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidCardsLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideRightChipsLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideRightCardsLeft));
    return(guidelines);
  }
}