package com.example.cartrell.blackjack.players;

import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Guideline;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cartrell.blackjack.engine.BjBetChip;

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
  private Guideline m_guideChipTop;
  private Guideline m_guideChipLeft;
  private ImageView m_turnIndicatorImage;
  private TextView m_betValueText;
  private TextView m_amountWonText;
  private View m_leftChip;
  private View m_rightChip;

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  HandData(ConstraintLayout constraintLayout, String uiPositionCode, float xDeck, float yDeck,
  int maxCardsPerHand) {
    super(constraintLayout, uiPositionCode, xDeck, yDeck, maxCardsPerHand);
    m_betChips = new ArrayList<>();
    initHdComponentsFromId(uiPositionCode);
  }

  //-------------------------------------------------------------------------
  // addBetChip
  //-------------------------------------------------------------------------
  void addBetChip(BjBetChip betChip) {
    m_betChips.add(betChip);
    addBetChipToConstraints(betChip);

    //inner functions cant access local variables unless they are declared final
    final ImageView _betChipImage = betChip.getImage();

    betChip.getImage().getViewTreeObserver().addOnGlobalLayoutListener(
      new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
          _betChipImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);

          if (m_betChipsViewDistributor == null) {
            m_betChipsViewDistributor = new ViewDistributor(m_leftChip, m_rightChip,
              m_guideChipTop.getY(), MAX_CHIPS);
          }

          m_betChipsViewDistributor.add(_betChipImage);
          updateBetChipPositions();
        }
      });
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
      m_constraintLayout.removeView(betChip.getImage());
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
    setText(m_amountWonText, value);
  }

  //-------------------------------------------------------------------------
  // setAmountWonVisible
  //-------------------------------------------------------------------------
  void setAmountWonVisible(boolean isVisible) {
    showView(m_amountWonText, isVisible);
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
  // addBetChipToConstraints
  //-------------------------------------------------------------------------
  private void addBetChipToConstraints(BjBetChip betChip) {
    if (betChip == null) {
      return; //sanity check
    }

    ImageView betChipImage = betChip.getImage();
    if (betChipImage == null) {
      return; //sanity check
    }

    m_constraintLayout.addView(betChipImage);

    ConstraintSet set = new ConstraintSet();
    int imageId = betChipImage.getId();
    set.constrainWidth(imageId, ConstraintSet.WRAP_CONTENT);
    set.constrainHeight(imageId, 0);
    set.connect(imageId, ConstraintSet.LEFT, m_guideChipLeft.getId(), ConstraintSet.RIGHT);
    set.connect(imageId, ConstraintSet.TOP, m_guideChipTop.getId(), ConstraintSet.BOTTOM);
    set.connect(imageId, ConstraintSet.BOTTOM, m_guideBottom.getId(), ConstraintSet.BOTTOM);
    set.applyTo(m_constraintLayout);
  }

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
  private void initHdComponents(ImageView turnIndicatorImage, View leftChip, View rightChip,
  Guideline guideChipTop, Guideline guideChipLeft, TextView betValueText, TextView amountWonText) {
    m_turnIndicatorImage = turnIndicatorImage;
    m_guideChipTop = guideChipTop;
    m_guideChipLeft = guideChipLeft;
    m_leftChip = leftChip;
    m_rightChip = rightChip;
    m_betValueText = betValueText;
    m_amountWonText = amountWonText;
    showTurnIndicatorImage(false);
  }

  //-------------------------------------------------------------------------
  // initHdComponentsFromId
  //-------------------------------------------------------------------------
  private void initHdComponentsFromId(String uiPositionCode) {
    ImageView turnIndicatorImage = (ImageView)getViewImageFromStringId("player" + uiPositionCode + "TurnIndicator");
    View leftChip = getViewImageFromStringId("player" + uiPositionCode + "LeftChip");
    View rightChip = getViewImageFromStringId("player" + uiPositionCode + "RightChip");
    Guideline guideChipTop = (Guideline)getViewImageFromStringId("guidePlayer" + uiPositionCode + "Chips");
    Guideline guideChipLeft = (Guideline)getViewImageFromStringId("guidePlayer" + uiPositionCode + "LeftChip");
    TextView betValueText = (TextView)getViewImageFromStringId("txtPlayer" + uiPositionCode + "BetValue");
    TextView amountWonText = (TextView)getViewImageFromStringId("txtPlayer" + uiPositionCode + "AmountWon");

    initHdComponents(turnIndicatorImage, leftChip, rightChip, guideChipTop, guideChipLeft,
      betValueText, amountWonText);

    showView(leftChip, false);
    showView(rightChip, false);
    //leftChip.setAlpha(0.5f);
    //rightChip.setAlpha(0.5f);
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