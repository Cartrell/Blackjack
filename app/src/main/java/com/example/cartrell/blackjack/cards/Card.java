package com.example.cartrell.blackjack.cards;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.example.cartrell.blackjack.R;

public class Card {
  //=========================================================================
  // members
  //=========================================================================
  private Context m_context;
  private ImageView m_image;
  private CardValues m_value;
  private CardSuits m_suit;
  private String m_key;
  private boolean m_isFaceUp;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // getImage
  //-------------------------------------------------------------------------
  public boolean getFaceUp() {
    return(m_isFaceUp);
  }

  //-------------------------------------------------------------------------
  // getImage
  //-------------------------------------------------------------------------
  public ImageView getImage() {
    if (m_image == null) {
      setImage();
    }
    return(m_image);
  }

  //-------------------------------------------------------------------------
  // getKey
  //-------------------------------------------------------------------------
  public String getKey() {
    return(m_key);
  }

  //-------------------------------------------------------------------------
  // getSuit
  //-------------------------------------------------------------------------
  public CardSuits getSuit() {
    return(m_suit);
  }

  //-------------------------------------------------------------------------
  // getValue
  //-------------------------------------------------------------------------
  public CardValues getValue() {
    return(m_value);
  }

  //-------------------------------------------------------------------------
  // getX
  //-------------------------------------------------------------------------
  public float getX() {
    return(m_image != null ? m_image.getX() : 0);
  }

  //-------------------------------------------------------------------------
  // getY
  //-------------------------------------------------------------------------
  public float getY() {
    return(m_image != null ? m_image.getY() : 0);
  }

  //-------------------------------------------------------------------------
  // setFaceUp
  //-------------------------------------------------------------------------
  public void setFaceUp(boolean value) {
    if (value == m_isFaceUp) {
      return;
    }

    m_isFaceUp = value;

    if (m_image != null) {
      setImage();
    }
  }

  //-------------------------------------------------------------------------
  // setPosition
  //-------------------------------------------------------------------------
  public void setPosition(float x, float y) {
    setX(x);
    setY(y);
  }

  //-------------------------------------------------------------------------
  // setX
  //-------------------------------------------------------------------------
  public void setX(float value) {
    if (m_image != null) {
      m_image.setX(value);
    }
  }

  //-------------------------------------------------------------------------
  // setY
  //-------------------------------------------------------------------------
  public void setY(float value) {
    if (m_image != null) {
      m_image.setY(value);
    }
  }

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  Card(Context context, CardValues value, CardSuits suit, int deckIndex) {
    m_context = context;
    m_value = value;
    m_suit = suit;
    m_key = m_value.getKey() + "_" + m_suit.getKey() + "_" + String.valueOf(deckIndex);
    m_isFaceUp = true;
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // createImage
  //-------------------------------------------------------------------------
  private void createImage() {
    if (m_image != null) {
      return; //image already created
    }

    m_image = new ImageView(m_context);
    int imageId = m_key.hashCode(); //each card image must have a unique id
    m_image.setId(imageId);
    m_image.setAdjustViewBounds(true);
  }

  //-------------------------------------------------------------------------
  // getCardBackImageResourceId
  //-------------------------------------------------------------------------
  private int getCardBackImageResourceId() {
    return(R.drawable.card_back_red);
  }

  //-------------------------------------------------------------------------
  // getCardImageResourceId
  //-------------------------------------------------------------------------
  private int getCardImageResourceId() {
    String name = "card_" + m_value.getKey() + m_suit.getKey();
    return(m_context.getResources().getIdentifier(name, "drawable",
      m_context.getPackageName()));
  }

  //-------------------------------------------------------------------------
  // setImage
  //-------------------------------------------------------------------------
  private void setImage() {
    int resourceId = m_isFaceUp ? getCardImageResourceId() : getCardBackImageResourceId();
    if (resourceId == 0) {
      Log.w("Card", "setImage. No resource id for card " + m_key + " is face up: " +
        m_isFaceUp);
      return;
    }

    createImage();
    m_image.setImageResource(resourceId);
  }
}
