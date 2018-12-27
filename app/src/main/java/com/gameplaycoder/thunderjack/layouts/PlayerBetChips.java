package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;

import com.gameplaycoder.thunderjack.R;

import java.util.ArrayList;

public class PlayerBetChips extends BaseLayoutComp {
  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public PlayerBetChips(ConstraintLayout layout) {
    super(layout);
  }

  //-------------------------------------------------------------------------
  // getGuideLines
  //-------------------------------------------------------------------------
  @Override
  public ArrayList<Guideline> getGuideLines() {
    ArrayList<Guideline> guidelines = new ArrayList<>();
    ConstraintLayout layout = getLayout();
    guidelines.add((Guideline)layout.findViewById(R.id.guideRightChipsRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideRightCardsRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidChipsRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidCardsRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideLeftChipsRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideLeftCardsRight));
    return(guidelines);
  }
}