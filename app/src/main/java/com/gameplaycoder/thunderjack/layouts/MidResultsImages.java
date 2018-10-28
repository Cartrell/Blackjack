package com.gameplaycoder.thunderjack.layouts;

import android.os.Debug;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.gameplaycoder.thunderjack.R;

import java.util.ArrayList;

public class MidResultsImages extends BaseLayoutComp {
  //=========================================================================
  // members
  //=========================================================================
  public ImageView resultsMidLeft;
  public ImageView resultsMidMid;
  public ImageView resultsMidRight;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public MidResultsImages(ConstraintLayout layout) {
    super(layout);
    resultsMidLeft = getImageView(R.id.resultsMidLeft);
    resultsMidMid = getImageView(R.id.resultsMidMid);
    resultsMidRight = getImageView(R.id.resultsMidRight);
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