package com.gameplaycoder.thunderjack.layouts;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.gameplaycoder.thunderjack.R;

import java.util.ArrayList;

public class SettingsButtonAndDeck extends BaseLayoutComp {
  //=========================================================================
  // members
  //=========================================================================
  public ImageButton settingsButton;
  public ImageView deckImage;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public SettingsButtonAndDeck(ConstraintLayout layout) {
    super(layout);
    settingsButton = getImageButton(R.id.btnSettings);
    deckImage = getImageView(R.id.deckImage);
  }

  //-------------------------------------------------------------------------
  // getGuideLines
  //-------------------------------------------------------------------------
  @Override
  public ArrayList<Guideline> getGuideLines() {
    ArrayList<Guideline> guidelines = new ArrayList<>();
    ConstraintLayout layout = getLayout();
    guidelines.add((Guideline)layout.findViewById(R.id.parentRight));
    guidelines.add((Guideline)layout.findViewById(R.id.guideSettingsButtonBottom));
    guidelines.add((Guideline)layout.findViewById(R.id.parentTop));
    guidelines.add((Guideline)layout.findViewById(R.id.guideDeckBottom));
    return(guidelines);
  }
}