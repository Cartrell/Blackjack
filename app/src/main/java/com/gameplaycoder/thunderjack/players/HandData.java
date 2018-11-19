package com.gameplaycoder.thunderjack.players;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gameplaycoder.thunderjack.engine.BjBetChip;
import com.gameplaycoder.thunderjack.utils.Metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class HandData extends BaseHandData {
  //=========================================================================
  // const
  //=========================================================================
  private final int MAX_CHIPS = 4;

  //=========================================================================
  // members
  //=========================================================================
  private ArrayList<BjBetChip> m_betChips;
  private ViewDistributor m_betChipsViewDistributor;
  private ImageView m_turnIndicatorImage;
  private TextView m_amountWonValueText;
  private TextView m_betValueText;
  private ConstraintLayout m_betChipsContainer;
  private Point m_chipSize;
  private float m_xChipsLeft;
  private float m_xChipsRight;
  private float m_yChipsTop;

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  HandData(ViewGroup viewGroup, float xDeck, float yDeck, int maxCardsPerHand,
  Guideline guideCardsLeft, Guideline guideCardsRight, Guideline guideCardsTop,
  Guideline guideCardsBottom, Guideline guideCardsUi, int cardImageWidth, TextView scoreText,
  ImageView resultImage, HashMap<String, Object> extraParams) {
    super(viewGroup, xDeck, yDeck, maxCardsPerHand, guideCardsLeft, guideCardsRight,
      guideCardsTop, guideCardsBottom, guideCardsUi, cardImageWidth, scoreText, resultImage,
      extraParams);
    m_betChips = new ArrayList<>();
    initHdComponents(extraParams);
  }

  //-------------------------------------------------------------------------
  // addBetChip
  //-------------------------------------------------------------------------
  void addBetChip(BjBetChip betChip) {
    m_betChips.add(betChip);

    ImageView betChipImage = betChip.getImage();

    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(m_chipSize.x, m_chipSize.y);
    m_betChipsContainer.addView(betChipImage, params);

    if (m_betChipsViewDistributor == null) {
      m_betChipsViewDistributor = new ViewDistributor(m_xChipsLeft,
        m_xChipsRight - m_chipSize.x, m_yChipsTop, MAX_CHIPS, m_chipSize.x);
    }

    m_betChipsViewDistributor.add(betChipImage);
    updateBetChipPositions();
  }

  //-------------------------------------------------------------------------
  // hideBetChips
  //-------------------------------------------------------------------------
  void hideBetChips() {
    for (BjBetChip betChip : m_betChips) {
      betChip.getImage().setVisibility(View.INVISIBLE);
    }
  }

  //-------------------------------------------------------------------------
  // removeBetChips
  //-------------------------------------------------------------------------
  void removeBetChips() {
    for (BjBetChip betChip : m_betChips) {
      m_betChipsContainer.removeView(betChip.getImage());
    }

    m_betChips.clear();

    if (m_betChipsViewDistributor != null) {
      m_betChipsViewDistributor.removeAll();
    }
  }

  //-------------------------------------------------------------------------
  // setAmountWonValue
  //-------------------------------------------------------------------------
  void setAmountWonValue(int value) {
    setText(m_amountWonValueText, value);
  }

  //-------------------------------------------------------------------------
  // setAmountWonValueVisible
  //-------------------------------------------------------------------------
  void setAmountWonValueVisible(boolean isVisible) {
    showView(m_amountWonValueText, isVisible);
  }

  //-------------------------------------------------------------------------
  // setBetValue
  //-------------------------------------------------------------------------
  void setBetValue(int value) {
    setText(m_betValueText, value);
  }

  //-------------------------------------------------------------------------
  // setBetValueVisible
  //-------------------------------------------------------------------------
  void setBetValueVisible(boolean isVisible) {
    showView(m_betValueText, isVisible);
  }

  //-------------------------------------------------------------------------
  // showBetChips
  //-------------------------------------------------------------------------
  void showBetChips() {
    for (BjBetChip betChip : m_betChips) {
      betChip.getImage().setVisibility(View.VISIBLE);
    }
  }

  //-------------------------------------------------------------------------
  // showTurnIndicatorImage
  //-------------------------------------------------------------------------
  void showTurnIndicatorImage(boolean isVisible) {
    showView(m_turnIndicatorImage, isVisible);
    animateDrawable(m_turnIndicatorImage, isVisible);
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // animateDrawable
  //-------------------------------------------------------------------------
  private void animateDrawable(ImageView imageView, boolean doAnimate) {
    AnimationDrawable animation = imageView != null ? (AnimationDrawable)imageView.getBackground() : null;
    if (animation == null) {
      return; //sanity checks
    }

    if (doAnimate) {
      animation.start();
    } else {
      animation.stop();
    }
  }

  //-------------------------------------------------------------------------
  // initHdComponents
  //-------------------------------------------------------------------------
  private void initHdComponents(HashMap<String, Object> extraParams) {
    m_amountWonValueText = (TextView)extraParams.get("amountWonValueText");
    m_betValueText = (TextView)extraParams.get("betValueText");
    m_xChipsLeft = Metrics.CalcGuidelinePosition((Guideline)extraParams.get("guideChipsLeft"));
    m_xChipsRight = Metrics.CalcGuidelinePosition((Guideline)extraParams.get("guideChipsRight"));
    m_yChipsTop = Metrics.CalcGuidelinePosition((Guideline)extraParams.get("guideChipsTop"));
    m_chipSize = (Point)extraParams.get("chipSize");
    m_turnIndicatorImage = (ImageView)extraParams.get("turnIndicatorImage");
    m_betChipsContainer = (ConstraintLayout)extraParams.get("betChipsContainer");

    showTurnIndicatorImage(false);
  }

  //-------------------------------------------------------------------------
  // updateBetChipPositions
  //-------------------------------------------------------------------------
  private void updateBetChipPositions() {
    HashMap<View, PointF>positions = m_betChipsViewDistributor.getPositions();

    Iterator iterator = positions.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry pair = (Map.Entry)iterator.next();
      ImageView betChipImage = (ImageView)pair.getKey();
      PointF position = (PointF)pair.getValue();
      betChipImage.setX(position.x);
      betChipImage.setY(position.y);
    }
  }
}