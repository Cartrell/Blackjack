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
  public ImageButton okButton;
  public ImageButton newBetButton;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public BetButtons(ConstraintLayout layout) {
    super(layout);
    okButton = getImageButton(R.id.btnOk);
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

  //-------------------------------------------------------------------------
  // setDealButton
  //-------------------------------------------------------------------------
  public void setDealButton() {
    okButton.setBackgroundResource(R.drawable.button_selector_deal);
  }

  //-------------------------------------------------------------------------
  // setRebetButton
  //-------------------------------------------------------------------------
  public void setRebetButton() {
    okButton.setBackgroundResource(R.drawable.button_selector_rebet);
  }
}
