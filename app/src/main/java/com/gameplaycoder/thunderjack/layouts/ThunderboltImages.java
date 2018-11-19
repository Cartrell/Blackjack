package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.view.View;
import android.widget.ImageView;

import com.gameplaycoder.thunderjack.R;

import java.util.ArrayList;

public class ThunderboltImages extends BaseLayoutComp {
  //=========================================================================
  // members
  //=========================================================================
  public ImageView leftBoltImage;
  public ImageView middleBoltImage;
  public ImageView rightBoltImage;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public ThunderboltImages(ConstraintLayout layout) {
    super(layout);

    leftBoltImage = initBoltImage(R.id.thunderboltLeft);
    middleBoltImage = initBoltImage(R.id.thunderboltMid);
    rightBoltImage = initBoltImage(R.id.thunderboltRight);

    setIsVisible(false);
  }

  //-------------------------------------------------------------------------
  // getGuideLines
  //-------------------------------------------------------------------------
  @Override
  public ArrayList<Guideline> getGuideLines() {
    ArrayList<Guideline> guidelines = new ArrayList<>();
    ConstraintLayout layout = getLayout();
    guidelines.add((Guideline)layout.findViewById(R.id.parentTop));
    guidelines.add((Guideline)layout.findViewById(R.id.parentBottom));
    guidelines.add((Guideline)layout.findViewById(R.id.guideLeftBoltLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideLeftBoltRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidBoltLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidBoltRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideRightBoltLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.guideRightBoltRight));
    return(guidelines);
  }

  //-------------------------------------------------------------------------
  // initBoltImage
  //-------------------------------------------------------------------------
  private ImageView initBoltImage(int imageResourceId) {
    ImageView imageView = getImageView(imageResourceId);
    if (imageView != null) {
      imageView.setVisibility(View.INVISIBLE);
    }
    return(imageView);
  }
}