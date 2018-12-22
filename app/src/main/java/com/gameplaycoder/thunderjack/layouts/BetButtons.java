package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.widget.ImageButton;

import com.gameplaycoder.thunderjack.R;

import java.util.ArrayList;

public class BetButtons extends BaseLayoutComp {
  //=========================================================================
  // members
  //=========================================================================
  public ImageButton rebetButton;
  public ImageButton newBetButton;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public BetButtons(ConstraintLayout layout) {
    super(layout);
    rebetButton = getImageButton(R.id.btnRebet);
    newBetButton = getImageButton(R.id.btnNewBet);
  }

  //-------------------------------------------------------------------------
  // getGuideLines
  //-------------------------------------------------------------------------
  @Override
  public ArrayList<Guideline> getGuideLines() {
    ArrayList<Guideline> guidelines = new ArrayList<>();
    ConstraintLayout layout = getLayout();
    guidelines.add((Guideline)layout.findViewById(R.id.parentLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.parentRight));
    guidelines.add((Guideline)layout.findViewById(R.id.betButtonsTopGuide));
    guidelines.add((Guideline)layout.findViewById(R.id.betButtonsBottomGuide));
    return(guidelines);
  }
}
