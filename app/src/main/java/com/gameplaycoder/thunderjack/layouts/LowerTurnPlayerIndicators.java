package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.widget.ImageView;

import com.gameplaycoder.thunderjack.R;

import java.util.ArrayList;

public class LowerTurnPlayerIndicators extends BaseLayoutComp {
  //=========================================================================
  // members
  //=========================================================================
  public ImageView turnPlayerLowerLeft;
  public ImageView turnPlayerLowerMid;
  public ImageView turnPlayerLowerRight;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public LowerTurnPlayerIndicators(ConstraintLayout layout) {
    super(layout);
    turnPlayerLowerLeft = getImageView(R.id.turnPlayerLowerLeft);
    turnPlayerLowerMid = getImageView(R.id.turnPlayerLowerMid);
    turnPlayerLowerRight = getImageView(R.id.turnPlayerLowerRight);
  }

  //-------------------------------------------------------------------------
  // getGuideLines
  //-------------------------------------------------------------------------
  @Override
  public ArrayList<Guideline> getGuideLines() {
    ArrayList<Guideline> guidelines = new ArrayList<>();
    ConstraintLayout layout = getLayout();
    guidelines.add((Guideline)layout.findViewById(R.id.guideLeftCardsLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideLeftCardsRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidCardsBottom));
    guidelines.add((Guideline)layout.findViewById(R.id.guideLowerCardsTop));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidCardsLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidCardsRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideRightCardsLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideRightCardsRight));
    return(guidelines);
  }
}