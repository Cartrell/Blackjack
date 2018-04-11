package com.example.cartrell.blackjack.players;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.constraint.Guideline;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cartrell.blackjack.cards.Card;
import com.example.cartrell.blackjack.cards.CardsMover;
import com.example.cartrell.blackjack.utils.Metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BaseHandData {
  //=========================================================================
  // const
  //=========================================================================
  private final String LOG_TAG = "BaseHandData";

  //=========================================================================
  // members
  //=========================================================================
  ViewGroup m_viewGroup;
  float m_yCardsBottom;

  private float m_xDeck;
  private float m_yDeck;
  private float m_yCardsUi;
  private float m_xCardsLeft;
  private float m_xCardsRight;
  private float m_yCardsTop;
  private int m_maxCardsPerHand;
  private int m_cardImageWidth;

  private ArrayList<Card> m_cards;
  private ViewDistributor m_cardsViewDistributor;
  private TextView m_scoreText;
  private ImageView m_resultImage;

  //=========================================================================
  // protected
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
  void addCard(Card card, CardsMover cardsMover, long moveStartDelay, long moveDuration,
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
      m_viewGroup.addView(cardImage, params);
    }
    card.setPosition(xCard, yCard);

    if (m_cardsViewDistributor == null) {
      m_cardsViewDistributor = new ViewDistributor(m_xCardsLeft,
        m_xCardsRight - m_cardImageWidth, m_yCardsTop, m_maxCardsPerHand, m_cardImageWidth);
    }

    m_cardsViewDistributor.add(cardImage);
    updateCardPositions(cardsMover, moveStartDelay, moveDuration, startAnimation);
  }

  //-------------------------------------------------------------------------
  // fadeOutAllCards
  //-------------------------------------------------------------------------
  void fadeOutAllCards(CardsMover cardsMover, long fadeOutDelay, boolean startAnimation) {
    AnimatorSet animSetAlpha = cardsMover.getAnimatorSet();
    for (Card card : m_cards) {
      ObjectAnimator anim = ObjectAnimator.ofFloat(card.getImage(), "alpha", 0f);
      anim.setDuration(fadeOutDelay);
      animSetAlpha.play(anim);
    }

    if (startAnimation) {
      animSetAlpha.start();
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
  Card popTopCard(CardsMover cardsMover, long startDelay, long moveDuration,
  boolean startAnimation) {
    if (m_cards.size() == 0) {
      return(null); //nothing to remove
    }

    Card card = m_cards.remove(m_cards.size() - 1);
    m_cardsViewDistributor.remove(card.getImage());
    updateCardPositions(cardsMover, startDelay, moveDuration, startAnimation);
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
      return; //sanity checl
    }

    Card card = m_cards.get(index);
    card.setFaceUp(isFaceUp);
  }

  //-------------------------------------------------------------------------
  // setResultImage
  //-------------------------------------------------------------------------
  void setResultImage(int drawableResourceId) {
    if (m_resultImage != null) {
      m_resultImage.setImageResource(drawableResourceId);
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
  // protected
  //=========================================================================

  //-------------------------------------------------------------------------
  // getViewFromStringId
  //-------------------------------------------------------------------------
  protected View getViewImageFromStringId(String stringId) {
    Context context = m_viewGroup.getContext();
    int resourceId = context.getResources().getIdentifier(stringId, "id",
      context.getPackageName());

    if (resourceId == 0) {
      Log.w(LOG_TAG, "getViewImageFromStringId. Failed to get id for " + stringId);
      return(null);
    }

    return(m_viewGroup.findViewById(resourceId));
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
    m_xCardsLeft = guideCardsLeft.getX();
    m_xCardsRight = guideCardsRight.getX();
    m_yCardsTop = guideCardsTop.getY();
    m_yCardsBottom = guideCardsBottom.getY();
    m_yCardsUi = guideCardsUi.getY();
    m_scoreText = scoreText;
    m_resultImage = resultImage;
  }

  //-------------------------------------------------------------------------
  // initResultsImage
  //-------------------------------------------------------------------------
  private void initResultsImage(ImageView resultImage) {
    /*m_resultImage = new ImageView(m_viewGroup.getContext());
    m_resultImage.setImageResource(R.drawable.result_label_blackjack);

    Point size = Metrics.CalcSize(m_resultImage, m_xCardsRight, m_xCardsLeft, true);
    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(size.x, size.y);
    m_resultImage.setX(m_xCardsLeft);
    m_resultImage.setY(m_yCardsTop - size.y);
    m_viewGroup.addView(m_resultImage, params);*/

    //setResultImageVisible(false);
  }

  //-------------------------------------------------------------------------
  // updateCardPositions
  //-------------------------------------------------------------------------
  private void updateCardPositions(CardsMover cardsMover, long moveStartDelay, long moveDuration,
  boolean startAnimation) {
    HashMap<View, PointF>positions = m_cardsViewDistributor.getPositions();
    AnimatorSet animSetXY = cardsMover.getAnimatorSet();

    Iterator iterator = positions.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry)iterator.next();
      final View cardImage = (View)entry.getKey();
      PointF position = (PointF)entry.getValue();

      ObjectAnimator animX = ObjectAnimator.ofFloat(cardImage, "x", position.x);
      animX.setDuration(moveDuration);
      animX.addListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
          cardImage.setVisibility(View.VISIBLE);
        }
      });

      animX.setStartDelay(moveStartDelay);

      ObjectAnimator animY = ObjectAnimator.ofFloat(cardImage, "y", position.y);
      animY.setDuration(moveDuration);

      animSetXY.playTogether(animX, animY);
    }

    if (startAnimation) {
      animSetXY.start();
    }
  }
}