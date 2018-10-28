package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.widget.ImageView;

import com.gameplaycoder.thunderjack.R;

import java.util.ArrayList;

public class UpperResultsImages extends BaseLayoutComp {
  //=========================================================================
  // members
  //=========================================================================
  public ImageView resultsUpperMid;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public UpperResultsImages(ConstraintLayout layout) {
    super(layout);
    resultsUpperMid = getImageView(R.id.resultsUpperMid);
  }

  //-------------------------------------------------------------------------
  // getGuideLines
  //-------------------------------------------------------------------------
  @Override
  public ArrayList<Guideline> getGuideLines() {
    ArrayList<Guideline> guidelines = new ArrayList<>();
    ConstraintLayout layout = getLayout();
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidCardsLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidCardsRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideDeckBottom));
    guidelines.add((Guideline)layout.findViewById(R.id.guideUpperCardsTop));
    return(guidelines);
  }
}