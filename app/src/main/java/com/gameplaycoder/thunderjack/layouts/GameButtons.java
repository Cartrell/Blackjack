package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.widget.ImageButton;

import com.gameplaycoder.thunderjack.R;

import java.util.ArrayList;

public class GameButtons extends BaseLayoutComp {
  //=========================================================================
  // members
  //=========================================================================
  public ImageButton surrenderButton;
  public ImageButton doubleButton;
  public ImageButton hitButton;
  public ImageButton standButton;
  public ImageButton splitButton;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public GameButtons(ConstraintLayout layout) {
    super(layout);
    surrenderButton = getImageButton(R.id.btnSurrender);
    doubleButton = getImageButton(R.id.btnDouble);
    hitButton = getImageButton(R.id.btnHit);
    standButton = getImageButton(R.id.btnStand);
    splitButton = getImageButton(R.id.btnSplit);
  }

  //-------------------------------------------------------------------------
  // getGuideLines
  //-------------------------------------------------------------------------
  @Override
  public ArrayList<Guideline> getGuideLines() {
    ArrayList<Guideline> guidelines = new ArrayList<>();
    ConstraintLayout layout = getLayout();
    guidelines.add((Guideline)layout.findViewById(R.id.parentBottom));
    guidelines.add((Guideline)layout.findViewById(R.id.parentLeft));
    guidelines.add((Guideline)layout.findViewById(R.id.parentRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideBottomPanelBtm));
    return(guidelines);
  }
}
