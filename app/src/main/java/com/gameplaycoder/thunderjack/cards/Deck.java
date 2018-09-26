package com.gameplaycoder.thunderjack.cards;

import android.content.Context;

import java.util.ArrayList;

public class Deck {
  //=========================================================================
  // members
  //=========================================================================
  private ArrayList<Card> m_cards;
  private int m_index;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public Deck(Context context, int numDecks) {
    m_cards = new ArrayList<>();
    m_index = 0;

    //add all cards for each deck
    for (int deckIndex = 0; deckIndex < numDecks; deckIndex++) {
      addDeckOfCards(context, deckIndex);
    }
  }

  //-------------------------------------------------------------------------
  // getCard
  //-------------------------------------------------------------------------
  public Card getCard(String cardKey) {
    for (Card card : m_cards) {
      if (cardKey.equals(card.getKey())) {
        return(card);
      }
    }
    return(null);
  }

  //-------------------------------------------------------------------------
  // getIndex
  //-------------------------------------------------------------------------
  public int getIndex() {
    return(m_index);
  }

  //-------------------------------------------------------------------------
  // getNumCards
  //-------------------------------------------------------------------------
  public int getNumCards() {
    return(m_cards.size());
  }

  //-------------------------------------------------------------------------
  // next
  //-------------------------------------------------------------------------
  public Card next() {
    if (m_index < 0 || m_index >= m_cards.size()) {
      return(null);
    }

    return(m_cards.get(m_index++));
  }

  //-------------------------------------------------------------------------
  // setCardAt
  //-------------------------------------------------------------------------
  public void setCardAt(String cardKey, int targetIndex) {
    if (targetIndex >= getNumCards()) {
      return;
    }

    int sourceIndex = getIndexOf(cardKey);
    if (sourceIndex > -1) {
      swapCardElementsAt(sourceIndex, targetIndex);
    }
  }

  //-------------------------------------------------------------------------
  // setIndex
  //-------------------------------------------------------------------------
  public void setIndex(int value) {
    m_index = Math.min(Math.max(0, value), m_cards.size() - 1);
  }

  //-------------------------------------------------------------------------
  // shuffle
  //-------------------------------------------------------------------------
  public void shuffle() {
    int numCards = getNumCards();
    for (int index = 0; index < numCards; index++) {
      int randomIndex = (int)(Math.random() * numCards);
      swapCardElementsAt(index, randomIndex);
    }
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // addDeckOfCards
  //-------------------------------------------------------------------------
  private void addDeckOfCards(Context context, int deckIndex) {
    for (CardValues value : CardValues.values()) {
      for (CardSuits suit : CardSuits.values()) {
        m_cards.add(new Card(context, value, suit, deckIndex));
      }
    }
  }

  //-------------------------------------------------------------------------
  // getIndexOf
  //-------------------------------------------------------------------------
  private int getIndexOf(String cardKey) {
    int size = m_cards.size();
    for (int index = 0; index < size; index++) {
      if (m_cards.get(index).getKey().equals(cardKey)) {
        return(index);
      }
    }

    return(-1);
  }

  //-------------------------------------------------------------------------
  // swapCardElementsAt
  //-------------------------------------------------------------------------
  private void swapCardElementsAt(int index1, int index2) {
    Card temp = m_cards.get(index1);
    m_cards.set(index1,  m_cards.get(index2));
    m_cards.set(index2, temp);
  }
}
