package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.view.View;
import android.widget.ImageView;

import com.gameplaycoder.thunderjack.R;

import java.util.ArrayList;

public class GameBackground extends BaseLayoutComp {
  //=========================================================================
  // members
  //=========================================================================
  public ImageView image;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public GameBackground(ConstraintLayout layout) {
    super(layout);
    image = getImageView(R.id.backgroundImage);
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
    return(guidelines);
  }
}
