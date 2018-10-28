package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.widget.ImageButton;

import com.gameplaycoder.thunderjack.R;

import java.util.ArrayList;

public class PlayerBetButtons extends BaseLayoutComp {
  //=========================================================================
  // members
  //=========================================================================
  public ImageButton btnRightPlayerBet;
  public ImageButton btnMidPlayerBet;
  public ImageButton btnLeftPlayerBet;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public PlayerBetButtons(ConstraintLayout layout) {
    super(layout);
    btnRightPlayerBet = getImageButton(R.id.btnRightPlayerBet);
    btnMidPlayerBet = getImageButton(R.id.btnMidPlayerBet);
    btnLeftPlayerBet = getImageButton(R.id.btnLeftPlayerBet);
  }

  //-------------------------------------------------------------------------
  // getGuideLines
  //-------------------------------------------------------------------------
  @Override
  public ArrayList<Guideline> getGuideLines() {
    ArrayList<Guideline> guidelines = new ArrayList<>();
    ConstraintLayout layout = getLayout();
    guidelines.add((Guideline)layout.findViewById(R.id.guideBottomPanel));
    guidelines.add((Guideline)layout.findViewById(R.id.guideRightChipsRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideRightCardsRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidChipsRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideMidCardsRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideLeftChipsRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideLeftCardsRight));
    return(guidelines);
  }
}