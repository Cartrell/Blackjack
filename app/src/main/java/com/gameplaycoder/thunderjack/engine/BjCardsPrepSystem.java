package com.gameplaycoder.thunderjack.engine;

import android.util.Log;

import com.gameplaycoder.thunderjack.cards.Deck;
import com.gameplaycoder.thunderjack.engine.sound.BjSoundSystem;
import com.gameplaycoder.thunderjack.players.PlayerData;
import com.gameplaycoder.thunderjack.utils.cardsTweener.CardsTweener;

import java.util.Iterator;
import java.util.Map;

class BjCardsPrepSystem {
  //=========================================================================
  // const
  //=========================================================================
  private final String LOG_TAG = "BjCardsPrepSystem";
  private static final float DECK_USED_THRESHOLD = 0.70f;

  //=========================================================================
  // members
  //=========================================================================
  private IBjEngine m_engine;
  private boolean m_isRemovingAllCards;
  private CardsTweener m_cardsTweener;
  private CardsTweener.OnCompletedListener m_cardsTweenerCompleteListener;
  private BjSoundSystem.OnSoundCompleteListener m_shuffleSoundCompleteListener;

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  BjCardsPrepSystem(IBjEngine engine) {
    m_engine = engine;
    initCardsTweener();
    initShuffleSoundCompleteListener();
  }

  //-------------------------------------------------------------------------
  // begin
  //-------------------------------------------------------------------------
  void begin() {
    if (m_engine.atLeastOneRoundPlayed()) {
      fadeOutAllCards();
    } else {
      refreshDeck();
    }
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // beginPlay
  //-------------------------------------------------------------------------
  private void beginPlay() {
    if (m_isRemovingAllCards) {
      m_isRemovingAllCards = false;
      removeAllPlayersCards();
    }

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
      playerData.fadeOutAllCards(m_cardsTweener, FADE_OUT_DELAY, false);
    }

    m_engine.getDealerData().fadeOutAllCards(m_cardsTweener, FADE_OUT_DELAY, true);
  }

  //-------------------------------------------------------------------------
  // initCardsTweener
  //-------------------------------------------------------------------------
  private void initCardsTweener() {
    m_cardsTweener = new CardsTweener();
    initCardsTweenerCompleteListener();
  }

  //-------------------------------------------------------------------------
  // initCardsTweenerCompleteListener
  //-------------------------------------------------------------------------
  private void initCardsTweenerCompleteListener() {
    m_cardsTweenerCompleteListener = new CardsTweener.OnCompletedListener() {
      @Override
      //-------------------------------------------------------------------------
      // onCompleted
      //-------------------------------------------------------------------------
      public void onCompleted(CardsTweener cardsTweener) {
        if (!m_isRemovingAllCards) {
          return;
        }

        if (shouldDeckBeRefreshed()) {
          refreshDeck();
        } else {
          beginPlay();
        }
      }
    };

    m_cardsTweener.setOnCompletedListener(m_cardsTweenerCompleteListener);
  }

  //-------------------------------------------------------------------------
  // initShuffleSoundCompleteListener
  //-------------------------------------------------------------------------
  private void initShuffleSoundCompleteListener() {
    m_shuffleSoundCompleteListener = new BjSoundSystem.OnSoundCompleteListener() {
      //-------------------------------------------------------------------------
      // onCompleted
      //-------------------------------------------------------------------------
      @Override
      public void onComplete(BjSoundSystem soundSystem) {
        beginPlay();
      }
    };
  }

  //-------------------------------------------------------------------------
  // refreshDeck
  //-------------------------------------------------------------------------
  private void refreshDeck() {
    Log.i(LOG_TAG, "refreshDeck");
    Deck deck = m_engine.getDeck();
    deck.shuffle();
    deck.setIndex(0);

    m_engine.getSoundSystem().playShuffle(m_shuffleSoundCompleteListener);
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