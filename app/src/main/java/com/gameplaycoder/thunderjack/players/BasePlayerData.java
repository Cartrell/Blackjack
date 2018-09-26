package com.gameplaycoder.thunderjack.players;

import android.support.constraint.Guideline;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gameplaycoder.thunderjack.cards.Card;
import com.gameplaycoder.thunderjack.utils.CardsMover;

import java.util.ArrayList;
import java.util.HashMap;

public class BasePlayerData {
  //=========================================================================
  // members
  //=========================================================================
  private final PlayerIds m_playerId;

  private ArrayList<String>m_cardKeys;
  private BaseHandData m_handData;
  private boolean m_hasBlackjack;
  private boolean m_isBust;
  private int m_score;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public BasePlayerData(ViewGroup viewGroup, PlayerIds playerId, float xDeck,
  float yDeck, int maxCardsPerHand, Guideline guideCardsLeft, Guideline guideCardsRight,
  Guideline guideCardsTop, Guideline guideCardsBottom, Guideline guideCardsUi,
  int cardImageWidth, TextView scoreText, ImageView resultImage,
  HashMap<String, Object> extraParams) {
    m_cardKeys = new ArrayList<>();
    m_playerId = playerId;
    m_handData = onCreateHandData(viewGroup, xDeck, yDeck, maxCardsPerHand,
      guideCardsLeft, guideCardsRight, guideCardsTop, guideCardsBottom, guideCardsUi,
      cardImageWidth, scoreText, resultImage, extraParams);
    m_score = 0;
    resetStatus();
  }

  //-------------------------------------------------------------------------
  // addCard
  //-------------------------------------------------------------------------
  public void addCard(Card card, CardsMover cardsMover, long startDelay, long moveDuration,
  boolean startAnimation, int cardImageIndex) {
    if (card == null || cardsMover == null) {
      return; //sanity check
    }

    m_cardKeys.add(card.getKey());
    m_handData.addCard(card, cardsMover, startDelay, moveDuration, startAnimation, cardImageIndex);
  }

  //-------------------------------------------------------------------------
  // fadeOutAllCards
  //-------------------------------------------------------------------------
  public void fadeOutAllCards(CardsMover cardsMover, long fadeOutDelay,
  boolean startAnimation) {
    m_handData.fadeOutAllCards(cardsMover, fadeOutDelay, startAnimation);
  }

  //-------------------------------------------------------------------------
  // getCardKetAt
  //-------------------------------------------------------------------------
  public String getCardKetAt(int index) {
    return(0 <= index && index < m_cardKeys.size() ? m_cardKeys.get(index) : null);
  }

  //-------------------------------------------------------------------------
  // getCardKeys
  //-------------------------------------------------------------------------
  public ArrayList<String> getCardKeys() {
    int size = m_cardKeys.size();
    ArrayList<String> out_keys = new ArrayList<String>(size);
    for (int index = 0; index < size; index++) {
      out_keys.add(m_cardKeys.get(index));
    }
    return(out_keys);
  }

  //-------------------------------------------------------------------------
  // getCardFaceUp
  //-------------------------------------------------------------------------
  public boolean getCardFaceUp(int cardIndex) {
    return(m_handData.getCardFaceUp(cardIndex));
  }

  //-------------------------------------------------------------------------
  // getFaceUpCardKeys
  //-------------------------------------------------------------------------
  public ArrayList<String> getFaceUpCardKeys(boolean isFaceUp) {
    ArrayList<String> out_keys = new ArrayList<>();
    int size = m_cardKeys.size();
    for (int index = 0; index < size; index++) {
      if (m_handData.getCardFaceUp(index) == isFaceUp) {
        out_keys.add(m_cardKeys.get(index));
      }
    }
    return(out_keys);
  }

  //-------------------------------------------------------------------------
  // getHasBlackjack
  //-------------------------------------------------------------------------
  public boolean getHasBlackjack() {
    return(m_hasBlackjack);
  }

  //-------------------------------------------------------------------------
  // getId
  //-------------------------------------------------------------------------
  public PlayerIds getId() {
    return(m_playerId);
  }

  //-------------------------------------------------------------------------
  // getIsBust
  //-------------------------------------------------------------------------
  public boolean getIsBust() {
    return(m_isBust);
  }

  //-------------------------------------------------------------------------
  // getNumCards
  //-------------------------------------------------------------------------
  public int getNumCards() {
    return(m_cardKeys.size());
  }

  //-------------------------------------------------------------------------
  // getScore
  //-------------------------------------------------------------------------
  public int getScore() {
    return(m_score);
  }

  //-------------------------------------------------------------------------
  // removeAllCards
  //-------------------------------------------------------------------------
  public void removeAllCards() {
    m_cardKeys.clear();
    m_handData.removeAllCards();
  }

  //-------------------------------------------------------------------------
  // popTopCard
  //-------------------------------------------------------------------------
  public Card popTopCard(CardsMover cardsMover, long startDelay, long moveDuration,
  boolean startAnimation) {
    if (cardsMover == null) {
      return(null); //sanity check
    }

    if (m_cardKeys.size() == 0) {
      return(null); //nothing to remove
    }

    m_cardKeys.remove(m_cardKeys.size() - 1);
    return(m_handData.popTopCard(cardsMover, startDelay, moveDuration, startAnimation));
  }

  //-------------------------------------------------------------------------
  // resetStatus
  //-------------------------------------------------------------------------
  public void resetStatus() {
    m_hasBlackjack = m_isBust = false;
  }

  //-------------------------------------------------------------------------
  // setBlackjack
  //-------------------------------------------------------------------------
  public void setBlackjack() {
    m_hasBlackjack = true;
  }

  //-------------------------------------------------------------------------
  // setBust
  //-------------------------------------------------------------------------
  public void setBust() {
    m_isBust = true;
  }

  //-------------------------------------------------------------------------
  // setCardFaceUp
  //-------------------------------------------------------------------------
  public void setCardFaceUp(int cardIndex, boolean isFaceUp) {
    m_handData.setCardFaceUp(cardIndex, isFaceUp);
  }

  //-------------------------------------------------------------------------
  // setCardMoveStartListener
  //-------------------------------------------------------------------------
  public void setCardMoveStartListener(BaseHandData.OnCardMoveStartListener cardMoveStartListener) {
    m_handData.setCardMoveStartListener(cardMoveStartListener);
  }

  //-------------------------------------------------------------------------
  // setResultImage
  //-------------------------------------------------------------------------
  public void setResultImage(int drawableResourceId) {
    m_handData.setResultImage(drawableResourceId);
  }

  //-------------------------------------------------------------------------
  // setResultImageVisible
  //-------------------------------------------------------------------------
  public void setResultImageVisible(boolean isVisible) {
    m_handData.setResultImageVisible(isVisible);
  }

  //-------------------------------------------------------------------------
  // setScore
  //-------------------------------------------------------------------------
  public void setScore(int value) {
    m_score = value;
    m_handData.setScoreText(m_score);
  }

  //-------------------------------------------------------------------------
  // setScoreTextVisible
  //-------------------------------------------------------------------------
  public void setScoreTextVisible(boolean isVisible) {
    m_handData.setScoreTextVisible(isVisible);
  }

  //=========================================================================
  // protected
  //=========================================================================

  //-------------------------------------------------------------------------
  // getHandData
  //-------------------------------------------------------------------------
  protected BaseHandData getHandData() {
    return(m_handData);
  }

  //-------------------------------------------------------------------------
  // onCreateHandData
  //-------------------------------------------------------------------------
  protected BaseHandData onCreateHandData(ViewGroup viewGroup, float xDeck,
  float yDeck, int maxCardsPerHand, Guideline guideCardsLeft, Guideline guideCardsRight,
  Guideline guideCardsTop, Guideline guideCardsBottom, Guideline guideCardsUi,
  int cardImageWidth, TextView textView, ImageView resultImage, HashMap<String, Object> extraParams) {
    return(new BaseHandData(viewGroup, xDeck, yDeck, maxCardsPerHand, guideCardsLeft,
      guideCardsRight, guideCardsTop, guideCardsBottom, guideCardsUi, cardImageWidth, textView,
      resultImage, extraParams));
  }
}