package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.widget.ImageView;

import com.gameplaycoder.thunderjack.R;

import java.util.ArrayList;

public class MidTurnPlayerIndicators extends BaseLayoutComp {
  //=========================================================================
  // members
  //=========================================================================
  public ImageView turnPlayerMidLeft;
  public ImageView turnPlayerMidMid;
  public ImageView turnPlayerMidRight;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public MidTurnPlayerIndicators(ConstraintLayout layout) {
    super(layout);
    turnPlayerMidLeft = getImageView(R.id.turnPlayerMidLeft);
    turnPlayerMidMid = getImageView(R.id.turnPlayerMidMid);
    turnPlayerMidRight = getImageView(R.id.turnPlayerMidRight);
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
    guidelines.add((Guideline)layout.findViewById(R.id.guideUpperCardsBottom));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidCardsTop));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidCardsLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidCardsRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideRightCardsLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideRightCardsRight));
    return(guidelines);
  }
}