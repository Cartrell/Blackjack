package com.example.cartrell.blackjack.engine;

import android.util.Log;

import com.example.cartrell.blackjack.cards.CardsMover;
import com.example.cartrell.blackjack.cards.Deck;
import com.example.cartrell.blackjack.cards.ICardsMoverCallbacks;
import com.example.cartrell.blackjack.players.PlayerData;

import java.util.Iterator;
import java.util.Map;

class BjCardsPrepSystem implements ICardsMoverCallbacks {
  //=========================================================================
  // const
  //=========================================================================
  private final String LOG_TAG = "BjCardsPrepSystem";
  private static final float DECK_USED_THRESHOLD = 0.70f;

  //=========================================================================
  // members
  //=========================================================================
  private IBjEngine m_engine;
  private CardsMover m_cardsMover;
  private boolean m_isRemovingAllCards;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // cardsMoverOnComplete
  //-------------------------------------------------------------------------
  @Override
  public void cardsMoverOnComplete(CardsMover cardsMover) {
    if (m_isRemovingAllCards) {
      m_isRemovingAllCards = false;
      if (shouldDeckBeRefreshed()) {
        refreshDeck();
      }

      beginPlay();
    }
  }

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  BjCardsPrepSystem(IBjEngine engine) {
    m_engine = engine;
    m_cardsMover = new CardsMover(this);
  }

  //-------------------------------------------------------------------------
  // begin
  //-------------------------------------------------------------------------
  void begin() {
    if (m_engine.atLeastOneRoundPlayed()) {
      fadeOutAllCards();
    } else {
      refreshDeck();
      m_engine.beginPlay();
    }
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // beginPlay
  //-------------------------------------------------------------------------
  private void beginPlay() {
    removeAllPlayersCards();
    m_engine.beginPlay();
  }

  //-------------------------------------------------------------------------
  // fadeOutAllCards
  //-------------------------------------------------------------------------
  private void fadeOutAllCards() {
    m_isRemovingAllCards = true;

    final long FADE_OUT_DELAY = 500;
    Iterator iterator = m_engine.getPlayers().entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry)iterator.next();
      PlayerData playerData = (PlayerData) entry.getValue();
      playerData.fadeOutAllCards(m_cardsMover, FADE_OUT_DELAY, false);
    }

    m_engine.getDealerData().fadeOutAllCards(m_cardsMover, FADE_OUT_DELAY, true);
  }

  //-------------------------------------------------------------------------
  // refreshDeck
  //-------------------------------------------------------------------------
  private void refreshDeck() {
    Log.i(LOG_TAG, "refreshDeck");
    Deck deck = m_engine.getDeck();
    deck.shuffle();
    deck.setIndex(0);
  }

  //-------------------------------------------------------------------------
  // removeAllPlayersCards
  //-------------------------------------------------------------------------
  private void removeAllPlayersCards() {
    Iterator iterator = m_engine.getPlayers().entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry)iterator.next();
      PlayerData playerData = (PlayerData) entry.getValue();
      playerData.removeAllCards();
    }

    m_engine.getDealerData().removeAllCards();
  }

  //-------------------------------------------------------------------------
  // shouldDeckBeRefreshed
  //-------------------------------------------------------------------------
  private boolean shouldDeckBeRefreshed() {
    Deck deck = m_engine.getDeck();
    float percentOfDeckUsed = (float)deck.getIndex() / (float)deck.getNumCards();
    Log.i(LOG_TAG, "shouldDeckBeRefreshed " + String.valueOf(deck.getIndex()) + "/" +
      String.valueOf(deck.getNumCards()) + "=" + percentOfDeckUsed);
    return(percentOfDeckUsed >= DECK_USED_THRESHOLD);
  }
}