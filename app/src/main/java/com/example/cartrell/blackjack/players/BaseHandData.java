package com.example.cartrell.blackjack.players;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Guideline;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cartrell.blackjack.R;
import com.example.cartrell.blackjack.cards.Card;
import com.example.cartrell.blackjack.cards.CardsMover;

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
  protected ConstraintLayout m_constraintLayout;
  protected Guideline m_guideBottom;

  private ArrayList<Card> m_cards;
  private ViewDistributor m_cardsViewDistributor;
  private Guideline m_guideLeft;
  private Guideline m_guideRight;
  private Guideline m_guideTop;
  private TextView m_scoreText;
  private ImageView m_resultImage;
  private View m_leftCard;
  private View m_rightCard;
  private float m_xDeck;
  private float m_yDeck;
  private int m_maxCardsPerHand;

  //=========================================================================
  // protected
  //=========================================================================

  //-------------------------------------------------------------------------
  // setText
  //-------------------------------------------------------------------------
  protected void setText(TextView textView, Object text) {
    if (textView != null && text != null) {
      textView.setText(String.valueOf(text));
    }
  }

  //-------------------------------------------------------------------------
  // showView
  //-------------------------------------------------------------------------
  protected void showView(View view, boolean isVisible) {
    if (view != null) {
      view.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }
  }

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  BaseHandData(ConstraintLayout constraintLayout, String uiPositionCode, float xDeck, float yDeck,
  int maxCardsPerHand) {
    m_cards = new ArrayList<>();
    m_constraintLayout = constraintLayout;
    m_xDeck = xDeck;
    m_yDeck = yDeck;
    m_maxCardsPerHand = maxCardsPerHand;

    if (uiPositionCode != null) {
      initComponentsFromId(uiPositionCode);
    }
  }

  //-------------------------------------------------------------------------
  // addCard
  //-------------------------------------------------------------------------
  void addCard(Card card, CardsMover cardsMover, long moveStartDelay, long moveDuration,
  boolean startAnimation, int cardImageIndex) {
    m_cards.add(card);

    //inner functions cant access local variables unless they are declared final
    final Card _card = card;
    final ImageView cardImage = _card.getImage();
    final CardsMover _cardsMover = cardsMover;
    final boolean isCardAlreadyInPlay = m_constraintLayout.indexOfChild(cardImage) != -1;
    final long _moveStartDelay = moveStartDelay;
    final long _moveDuration = moveDuration;
    final boolean _startAnimation = startAnimation;
    final float xCard;
    final float yCard;

    if (isCardAlreadyInPlay) {
      xCard = cardImage.getX();
      yCard = cardImage.getY();
    } else {
      xCard = m_xDeck;
      yCard = m_yDeck;
    }

    addCardToConstraints(card, cardImageIndex);

    card.getImage().getViewTreeObserver().addOnGlobalLayoutListener(
      new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
          cardImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
          _card.setPosition(xCard, yCard);

          if (!isCardAlreadyInPlay) {
            cardImage.setVisibility(View.INVISIBLE);
          }

          if (m_cardsViewDistributor == null) {
            m_cardsViewDistributor = new ViewDistributor(m_leftCard, m_rightCard, m_guideTop.getY(),
              m_maxCardsPerHand);
          }

          m_cardsViewDistributor.add(cardImage);
          updateCardPositions(_cardsMover, _moveStartDelay, _moveDuration, _startAnimation);
        }
      });
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
      m_constraintLayout.removeView(card.getImage());
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

  //=========================================================================
  // protected
  //=========================================================================

  //-------------------------------------------------------------------------
  // getViewFromStringId
  //-------------------------------------------------------------------------
  protected View getViewImageFromStringId(String stringId) {
    Context context = m_constraintLayout.getContext();
    int resourceId = context.getResources().getIdentifier(stringId, "id",
      context.getPackageName());

    if (resourceId == 0) {
      Log.w(LOG_TAG, "getViewImageFromStringId. Failed to get id for " + stringId);
      return(null);
    }

    return(m_constraintLayout.findViewById(resourceId));
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // addCardToConstraints
  //-------------------------------------------------------------------------
  private void addCardToConstraints(Card card, int cardImageIndex) {
    if (card == null) {
      return; //sanity check
    }

    ImageView cardImage = card.getImage();
    if (cardImage == null) {
      return; //sanity check
    }

    ConstraintSet set = new ConstraintSet();
    int cardImageId = cardImage.getId();

    if (m_constraintLayout.indexOfChild(cardImage) == -1) {
      m_constraintLayout.addView(cardImage, cardImageIndex);
    } else {
      set.clone(m_constraintLayout);
      set.clear(cardImageId);
    }

    set.constrainWidth(cardImageId, ConstraintSet.WRAP_CONTENT);
    set.constrainHeight(cardImageId, 0);
    set.connect(cardImageId, ConstraintSet.TOP, m_guideTop.getId(), ConstraintSet.TOP);
    set.connect(cardImageId, ConstraintSet.BOTTOM, m_guideBottom.getId(), ConstraintSet.BOTTOM);
    set.applyTo(m_constraintLayout);
  }

  //-------------------------------------------------------------------------
  // initComponents
  //-------------------------------------------------------------------------
  private void initComponents(View leftCard, View rightCard, Guideline guideLeft,
  Guideline guideRight, Guideline guideTop, Guideline guideBottom, TextView scoreText,
  ImageView resultsImage) {
    m_leftCard = leftCard;
    m_rightCard = rightCard;
    m_guideLeft = guideLeft;
    m_guideRight = guideRight;
    m_guideTop = guideTop;
    m_guideBottom = guideBottom;
    m_scoreText = scoreText;
    m_resultImage = resultsImage;

    setScoreTextVisible(false);
    setResultImageVisible(false);
  }

  //-------------------------------------------------------------------------
  // initComponentsFromId
  //-------------------------------------------------------------------------
  private void initComponentsFromId(String uiPositionCode) {
    View leftCard = getViewImageFromStringId("player" + uiPositionCode + "LeftCard");
    View rightCard = getViewImageFromStringId("player" + uiPositionCode + "RightCard");
    Guideline guideTop = (Guideline)getViewImageFromStringId("guidePlayer" + uiPositionCode + "CardsTop");
    Guideline guideBottom = (Guideline)getViewImageFromStringId("guidePlayer" + uiPositionCode + "CardsBottom");
    TextView scoreText = (TextView)getViewImageFromStringId("txtPlayer" + uiPositionCode + "Score");
    ImageView resultImage = (ImageView)getViewImageFromStringId("player" + uiPositionCode + "Result");

    String positionId = uiPositionCode.substring(0, 1);
    Guideline guideLeft = (Guideline)getViewImageFromStringId("guidePlayer" + positionId + "CardsLeft");
    Guideline guideRight = (Guideline)getViewImageFromStringId("guidePlayer" + positionId + "CardsRight");

    initComponents(leftCard, rightCard, guideLeft, guideRight, guideTop, guideBottom,
      scoreText, resultImage);

    showView(leftCard, false);
    showView(rightCard, false);
    //leftCard.setAlpha(0.5f);
    //rightCard.setAlpha(0.5f);
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