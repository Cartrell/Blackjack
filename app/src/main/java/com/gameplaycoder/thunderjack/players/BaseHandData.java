package com.gameplaycoder.thunderjack.players;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.constraint.Guideline;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gameplaycoder.thunderjack.cards.Card;
import com.gameplaycoder.thunderjack.utils.Metrics;
import com.gameplaycoder.thunderjack.utils.cardsTweener.CardsTweener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaseHandData {
  //=========================================================================
  // members
  //=========================================================================
  private ViewGroup m_viewGroup;

  private float m_xDeck;
  private float m_yDeck;
  private float m_xCardsLeft;
  private float m_xCardsRight;
  private float m_yCardsTop;
  private float m_yCardsBottom;
  private int m_maxCardsPerHand;
  private int m_cardImageWidth;

  private ArrayList<Card> m_cards;
  private ViewDistributor m_cardsViewDistributor;
  private TextView m_scoreText;
  private ImageView m_resultImage;

  //=========================================================================
  // public
  //=========================================================================

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  BaseHandData(ViewGroup viewGroup, float xDeck, float yDeck, int maxCardsPerHand,
  Guideline guideCardsLeft, Guideline guideCardsRight, Guideline guideCardsTop,
  Guideline guideCardsBottom, Guideline guideCardsUi, int cardImageWidth, TextView scoreText,
  ImageView resultImage, HashMap<String, Object> extraParams) {
    m_cards = new ArrayList<>();
    m_viewGroup = viewGroup;
    m_xDeck = xDeck;
    m_yDeck = yDeck;
    m_maxCardsPerHand = maxCardsPerHand;
    m_cardImageWidth = cardImageWidth;

    initComponents(guideCardsLeft, guideCardsRight, guideCardsTop, guideCardsBottom, guideCardsUi,
      scoreText, resultImage);
  }

  //-------------------------------------------------------------------------
  // addCard
  //-------------------------------------------------------------------------
  void addCard(Card card, CardsTweener cardsTweener, long moveStartDelay, long moveDuration,
  boolean startAnimation, int cardImageIndex) {
    m_cards.add(card);

    ImageView cardImage = card.getImage();
    boolean isCardAlreadyInPlay = m_viewGroup.indexOfChild(cardImage) != -1;
    float xCard;
    float yCard;

    if (isCardAlreadyInPlay) {
      xCard = cardImage.getX();
      yCard = cardImage.getY();
    } else {
      xCard = m_xDeck;
      yCard = m_yDeck;
    }

    Point size = Metrics.CalcSize(cardImage, m_yCardsBottom, m_yCardsTop, false);
    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(size.x, size.y);
    if (!isCardAlreadyInPlay) {
      cardImage.setVisibility(View.INVISIBLE);
      cardImage.setAlpha(1.0f);
      m_viewGroup.addView(cardImage, cardImageIndex, params);
    }
    card.setPosition(xCard, yCard);

    if (m_cardsViewDistributor == null) {
      m_cardsViewDistributor = new ViewDistributor(m_xCardsLeft,
        m_xCardsRight - m_cardImageWidth, m_yCardsTop, m_maxCardsPerHand, m_cardImageWidth);
    }

    m_cardsViewDistributor.add(cardImage);
    updateCardPositions(cardsTweener, moveStartDelay, moveDuration, startAnimation);
  }

  //-------------------------------------------------------------------------
  // fadeOutAllCards
  //-------------------------------------------------------------------------
  void fadeOutAllCards(CardsTweener cardsTweener, long fadeOutDelay, boolean startAnimation) {
    for (Card card : m_cards) {
      cardsTweener.addAlpha(card.getImage(), 0f, fadeOutDelay, 0);
    }

    if (startAnimation) {
      cardsTweener.play();
    }
  }

  //-------------------------------------------------------------------------
  // getCardFaceUp
  //-------------------------------------------------------------------------
  boolean getCardFaceUp(int index) {
    return(0 <= index && index < m_cards.size() && m_cards.get(index).getFaceUp());
  }

  //-------------------------------------------------------------------------
  // popTopCard
  //-------------------------------------------------------------------------
  Card popTopCard(CardsTweener cardsTweener, long startDelay, long moveDuration,
  boolean startAnimation) {
    if (m_cards.size() == 0) {
      return(null); //nothing to remove
    }

    Card card = m_cards.remove(m_cards.size() - 1);
    m_cardsViewDistributor.remove(card.getImage());
    updateCardPositions(cardsTweener, startDelay, moveDuration, startAnimation);
    return(card);
  }

  //-------------------------------------------------------------------------
  // removeAllCards
  //-------------------------------------------------------------------------
  void removeAllCards() {
    for (Card card : m_cards) {
      m_viewGroup.removeView(card.getImage());
    }

    m_cards.clear();

    if (m_cardsViewDistributor != null) {
      m_cardsViewDistributor.removeAll();
    }
  }

  //-------------------------------------------------------------------------
  // setCardFaceUp
  //-------------------------------------------------------------------------
  void setCardFaceUp(int index, boolean isFaceUp) {
    if (index >= m_cards.size()) {
      return; //sanity check
    }

    Card card = m_cards.get(index);
    card.setFaceUp(isFaceUp);
  }

  //-------------------------------------------------------------------------
  // setResultImage
  //-------------------------------------------------------------------------
  void setResultImage(int drawableResourceId) {
    if (m_resultImage == null) {
      return;
    }

    m_resultImage.setImageResource(drawableResourceId);

    Drawable drawable = m_resultImage.getDrawable();
    if (drawable instanceof Animatable) {
      ((Animatable)drawable).start();
    }
  }

  //-------------------------------------------------------------------------
  // setResultImageVisible
  //-------------------------------------------------------------------------
  void setResultImageVisible(boolean isVisible) {
    showView(m_resultImage, isVisible);
    if (m_resultImage != null) {
      m_resultImage.getParent().bringChildToFront(m_resultImage);
    }
  }

  //-------------------------------------------------------------------------
  // setScore
  //-------------------------------------------------------------------------
  void setScoreText(int score) {
    setText(m_scoreText, score);
  }

  //-------------------------------------------------------------------------
  // setScoreTextVisible
  //-------------------------------------------------------------------------
  void setScoreTextVisible(boolean isVisible) {
    showView(m_scoreText, isVisible);
  }

  //-------------------------------------------------------------------------
  // setText
  //-------------------------------------------------------------------------
  void setText(TextView textView, Object text) {
    if (textView != null && text != null) {
      textView.setText(String.valueOf(text));
    }
  }

  //-------------------------------------------------------------------------
  // showView
  //-------------------------------------------------------------------------
  void showView(View view, boolean isVisible) {
    if (view != null) {
      view.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // initComponents
  //-------------------------------------------------------------------------
  private void initComponents(Guideline guideCardsLeft, Guideline guideCardsRight,
  Guideline guideCardsTop, Guideline guideCardsBottom, Guideline guideCardsUi, TextView scoreText,
  ImageView resultImage) {
    m_xCardsLeft = Metrics.CalcGuidelinePosition(guideCardsLeft);
    m_xCardsRight = Metrics.CalcGuidelinePosition(guideCardsRight);
    m_yCardsTop = Metrics.CalcGuidelinePosition(guideCardsTop);
    m_yCardsBottom = Metrics.CalcGuidelinePosition(guideCardsBottom);
    m_scoreText = scoreText;
    m_resultImage = resultImage;
  }

  //-------------------------------------------------------------------------
  // updateCardPositions
  //-------------------------------------------------------------------------
  private void updateCardPositions(CardsTweener cardsTweener, long moveStartDelay, long moveDuration,
  boolean startAnimation) {
    HashMap<View, PointF>positions = m_cardsViewDistributor.getPositions();

    for (Map.Entry entry : positions.entrySet()) {
      final View cardImage = (View)entry.getKey();
      PointF position = (PointF)entry.getValue();
      cardsTweener.addPosition(cardImage, position.x, position.y, moveDuration, moveStartDelay);
    }

    if (startAnimation) {
      cardsTweener.play();
    }
  }
}