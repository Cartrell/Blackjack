package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.widget.TextView;

import com.gameplaycoder.thunderjack.R;

import java.util.ArrayList;

public class MidScoreTexts extends BaseLayoutComp {
  //=========================================================================
  // members
  //=========================================================================
  public TextView txtMidLeftScore;
  public TextView txtMidMidScore;
  public TextView txtMidRightScore;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public MidScoreTexts(ConstraintLayout layout) {
    super(layout);
    txtMidLeftScore = getTextView(R.id.txtMidLeftScore);
    txtMidMidScore = getTextView(R.id.txtMidMidScore);
    txtMidRightScore = getTextView(R.id.txtMidRightScore);
  }

  //-------------------------------------------------------------------------
  // getGuideLines
  //-------------------------------------------------------------------------
  @Override
  public ArrayList<Guideline> getGuideLines() {
    ArrayList<Guideline> guidelines = new ArrayList<>();
    ConstraintLayout layout = getLayout();
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidCardsUi));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidCardsTop));
    guidelines.add((Guideline)layout.findViewById(R.id.guideLeftChipsLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideLeftCardsLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidChipsLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidCardsLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideRightChipsLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideRightCardsLeft));
    return(guidelines);
  }
}