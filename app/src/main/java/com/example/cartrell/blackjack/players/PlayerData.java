package com.example.cartrell.blackjack.players;

import android.support.constraint.Guideline;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cartrell.blackjack.engine.BjBetChip;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerData extends BasePlayerData {
  //=========================================================================
  // members
  //=========================================================================
  private ArrayList<String>m_cardKeys;
  private int m_betValue;
  private boolean m_isDoubleDown;
  private boolean m_isCharlieWin;
  private boolean m_hasSurrendered;
  private boolean m_hasSplit;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public PlayerData(ViewGroup viewGroup, PlayerIds playerId, float xDeck,
  float yDeck, int maxCardsPerHand, Guideline guideCardsLeft, Guideline guideCardsRight,
  Guideline guideCardsTop, Guideline guideCardsBottom, Guideline guideCardsUi,
  int cardImageWidth, TextView scoreText, ImageView resultImage, HashMap<String, Object> extraParams) {
    super(viewGroup, playerId, xDeck, yDeck, maxCardsPerHand, guideCardsLeft,
      guideCardsRight, guideCardsTop, guideCardsBottom, guideCardsUi, cardImageWidth, scoreText,
      resultImage, extraParams);

    m_betValue = 0;
  }

  //-------------------------------------------------------------------------
  // addBetChip
  //-------------------------------------------------------------------------
  public void addBetChip(BjBetChip betChip) {
    if (betChip == null) {
      return; //sanity check
    }

    HandData handData = (HandData)getHandData();
    handData.addBetChip(betChip);
  }

  //-------------------------------------------------------------------------
  // addBetValue
  //-------------------------------------------------------------------------
  public void addBetValue(int betChipValue) {
    setBetAmountWonValue(m_betValue + betChipValue);
    setBetAmountWonValueVisible(true);
  }

  //-------------------------------------------------------------------------
  // getBetValue
  //-------------------------------------------------------------------------
  public int getBetValue() {
    return(m_betValue);
  }

  //-------------------------------------------------------------------------
  // getHasSplit
  //-------------------------------------------------------------------------
  public boolean getHasSplit() {
    return(m_hasSplit);
  }

  //-------------------------------------------------------------------------
  // getHasSurrendered
  //-------------------------------------------------------------------------
  public boolean getHasSurrendered() {
    return(m_hasSurrendered);
  }

  //-------------------------------------------------------------------------
  // getIsCharlieWin
  //-------------------------------------------------------------------------
  public boolean getIsCharlieWin() {
    return(m_isCharlieWin);
  }

  //-------------------------------------------------------------------------
  // getIsDoubleDown
  //-------------------------------------------------------------------------
  public boolean getIsDoubleDown() {
    return(m_isDoubleDown);
  }

  //-------------------------------------------------------------------------
  // hideBetChips
  //-------------------------------------------------------------------------
  public void hideBetChips() {
    HandData handData = (HandData)getHandData();
    handData.hideBetChips();
  }

  //-------------------------------------------------------------------------
  // isLeftVsDealer
  //-------------------------------------------------------------------------
  public boolean isLeftVsDealer() {
    return(m_betValue > 0 &&
      !getHasBlackjack() &&
      !getIsBust() &&
      !getIsCharlieWin() &&
      !getHasSurrendered());
  }

  //-------------------------------------------------------------------------
  // removeBetChips
  //-------------------------------------------------------------------------
  public void removeBetChips() {
    HandData handData = (HandData)getHandData();
    handData.removeBetChips();
  }

  //-------------------------------------------------------------------------
  // resetStatus
  //-------------------------------------------------------------------------
  @Override
  public void resetStatus() {
    super.resetStatus();
    m_isDoubleDown = m_isCharlieWin = m_hasSurrendered = m_hasSplit = false;
  }

  //-------------------------------------------------------------------------
  // setBetAmountWonValue
  //-------------------------------------------------------------------------
  public void setBetAmountWonValue(int value) {
    m_betValue = Math.max(0, value);
    HandData handData = (HandData)getHandData();
    handData.setBetAmountWonValue(m_betValue);
  }

  //-------------------------------------------------------------------------
  // setBetAmountWonValueVisible
  //-------------------------------------------------------------------------
  public void setBetAmountWonValueVisible(boolean isVisible) {
    HandData handData = (HandData)getHandData();
    handData.setBetAmountWonValueVisible(isVisible);
  }

  //-------------------------------------------------------------------------
  // setCharlieWin
  //-------------------------------------------------------------------------
  public void setCharlieWin() {
    m_isCharlieWin = true;
  }

  //-------------------------------------------------------------------------
  // setDoubleDown
  //-------------------------------------------------------------------------
  public void setDoubleDown() {
    m_isDoubleDown = true;
  }

  //-------------------------------------------------------------------------
  // setSplit
  //-------------------------------------------------------------------------
  public void setSplit() {
    m_hasSplit = true;
  }

  //-------------------------------------------------------------------------
  // setSurrendered
  //-------------------------------------------------------------------------
  public void setSurrendered() {
    m_hasSurrendered = true;
  }

  //-------------------------------------------------------------------------
  // setTurnIndicatorVisible
  //-------------------------------------------------------------------------
  public void setTurnIndicatorVisible(boolean isVisible) {
    HandData handData = (HandData)getHandData();
    handData.showTurnIndicatorImage(isVisible);
  }

  //-------------------------------------------------------------------------
  // showBetChips
  //-------------------------------------------------------------------------
  public void showBetChips() {
    HandData handData = (HandData)getHandData();
    handData.showBetChips();
  }

  //=========================================================================
  // protected
  //=========================================================================

  //-------------------------------------------------------------------------
  // onCreateHandData
  //-------------------------------------------------------------------------
  @Override
  protected BaseHandData onCreateHandData(ViewGroup viewGroup, float xDeck,
  float yDeck, int maxCardsPerHand, Guideline guideCardsLeft, Guideline guideCardsRight,
  Guideline guideCardsTop, Guideline guideCardsBottom, Guideline guideCardsUi,
  int cardImageWidth, TextView scoreText, ImageView resultImage,
  HashMap<String, Object> extraParams) {
    return(new HandData(viewGroup, xDeck, yDeck, maxCardsPerHand, guideCardsLeft,
      guideCardsRight, guideCardsTop, guideCardsBottom, guideCardsUi, cardImageWidth,
      scoreText, resultImage, extraParams));
  }
}