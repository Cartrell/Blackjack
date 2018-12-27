package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.gameplaycoder.thunderjack.R;

import java.util.ArrayList;

public class BetAndChipButtons extends BaseLayoutComp {
  //=========================================================================
  // members
  //=========================================================================
  public ImageButton clearButton;
  public ImageButton dealButton;

  public ToggleButton greenChipButton;
  public ToggleButton purpleChipButton;
  public ToggleButton redChipButton;
  public ToggleButton blueChipButton;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public BetAndChipButtons(ConstraintLayout layout) {
    super(layout);
    clearButton = getImageButton(R.id.btnClear);
    //dealButton = getImageButton(R.id.btnDeal);
    greenChipButton = getToggleButton(R.id.chipButtonGreen);
    purpleChipButton = getToggleButton(R.id.chipButtonPurple);
    redChipButton = getToggleButton(R.id.chipButtonRed);
    blueChipButton = getToggleButton(R.id.chipButtonBlue);
  }

  //-------------------------------------------------------------------------
  // getGuideLines
  //-------------------------------------------------------------------------
  @Override
  public ArrayList<Guideline> getGuideLines() {
    ArrayList<Guideline> guidelines = new ArrayList<>();
    ConstraintLayout layout = getLayout();
    guidelines.add((Guideline)layout.findViewById(R.id.parentBottom));
    guidelines.add((Guideline)layout.findViewById(R.id.parentRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideBottomPanelBtm));
    return(guidelines);
  }
}
