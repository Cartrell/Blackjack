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
  private int m_betValue;
  private int m_origBetValue;
  private boolean m_isDoubleDown;
  private boolean m_isCharlieWin;
  private boolean m_hasSurrendered;
  private boolean m_hasSplit;
  private boolean m_isNormalWin;
  private boolean m_isPush;

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
    m_origBetValue = 0;
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
  // getIsNormalWin
  //-------------------------------------------------------------------------
  public boolean getIsNormalWin() {
    return(m_isNormalWin);
  }

  //-------------------------------------------------------------------------
  // getIsPush
  //-------------------------------------------------------------------------
  public boolean getIsPush() {
    return(m_isPush);
  }

  //-------------------------------------------------------------------------
  // getOrigBetValue
  //-------------------------------------------------------------------------
  public int getOrigBetValue() {
    return(m_origBetValue);
  }

  //-------------------------------------------------------------------------
  // hideBetChips
  //-------------------------------------------------------------------------
  public void hideBetChips() {
    HandData handData = (HandData)getHandData();
    handData.hideBetChips();
  }

  //-------------------------------------------------------------------------
  // isVsDealer
  //-------------------------------------------------------------------------
  public boolean isVsDealer() {
    return(m_origBetValue > 0 &&
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
    m_isDoubleDown = m_isCharlieWin = m_hasSurrendered = m_hasSplit = m_isNormalWin = m_isPush = false;
  }

  //-------------------------------------------------------------------------
  // setBetChips
  //-------------------------------------------------------------------------
  public void setBetChips(ArrayList<BjBetChip> betChips) {
    removeBetChips();
    for (BjBetChip betChip : betChips) {
      addBetChip(betChip);
    }
  }

  //-------------------------------------------------------------------------
  // setAmountWonValue
  //-------------------------------------------------------------------------
  public void setAmountWonValue(int value) {
    value = Math.max(0, value);
    HandData handData = (HandData)getHandData();
    handData.setAmountWonValue(value);
  }

  //-------------------------------------------------------------------------
  // setAmountWonValueVisible
  //-------------------------------------------------------------------------
  public void setAmountWonValueVisible(boolean isVisible) {
    HandData handData = (HandData)getHandData();
    handData.setAmountWonValueVisible(isVisible);
  }

  //-------------------------------------------------------------------------
  // setBetValue
  //-------------------------------------------------------------------------
  public void setBetValue(int value) {
    m_betValue = Math.max(0, value);
    HandData handData = (HandData)getHandData();
    handData.setBetValue(m_betValue);
  }

  //-------------------------------------------------------------------------
  // setBetValueVisible
  //-------------------------------------------------------------------------
  public void setBetValueVisible(boolean isVisible) {
    HandData handData = (HandData)getHandData();
    handData.setBetValueVisible(isVisible);
  }

  //-------------------------------------------------------------------------
  // setOrigBetValue
  //-------------------------------------------------------------------------
  public void setOrigBetValue(int value) {
    m_origBetValue = Math.max(0, value);
    HandData handData = (HandData)getHandData();
    handData.setBetValue(m_origBetValue);
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
  // setIsPush
  //-------------------------------------------------------------------------
  public void setIsPush() {
    m_isPush = true;
  }

  //-------------------------------------------------------------------------
  // setNormalWin
  //-------------------------------------------------------------------------
  public void setNormalWin() {
    m_isNormalWin = true;
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

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // addBetChip
  //-------------------------------------------------------------------------
  private void addBetChip(BjBetChip betChip) {
    if (betChip == null) {
      return; //sanity check
    }

    HandData handData = (HandData)getHandData();
    handData.addBetChip(betChip);
  }
}