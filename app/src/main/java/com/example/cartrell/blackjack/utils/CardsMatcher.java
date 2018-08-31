package com.example.cartrell.blackjack.utils;

import com.example.cartrell.blackjack.cards.CardSuits;
import com.example.cartrell.blackjack.cards.CardValues;
import com.example.cartrell.blackjack.engine.IBjEngine;
import com.example.cartrell.blackjack.players.BasePlayerData;
import com.example.cartrell.blackjack.players.PlayerData;

import java.util.ArrayList;

public final class CardsMatcher {
  //=========================================================================
  // members
  //=========================================================================
  private static final int SM_NUM_CARDS_FOR_BLACKJACK = 2;
  private static final int SM_NUM_CARDS_FOR_SPLIT = 2;
  private final int M_BLACKJACK_POINTS;
  private final int M_MAX_CARDS_PER_HAND;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public CardsMatcher(final int BLACKJACK_POINTS, final int MAX_CARDS_PER_HAND) {
    M_BLACKJACK_POINTS = BLACKJACK_POINTS;
    M_MAX_CARDS_PER_HAND = MAX_CARDS_PER_HAND;
  }

  //-------------------------------------------------------------------------
  // canPlayerSplit
  //-------------------------------------------------------------------------
  public boolean canPlayerSplit(PlayerData playerData) {
    if (playerData == null) {
      return(false);
    }

    if (playerData.getNumCards() != SM_NUM_CARDS_FOR_SPLIT) {
      return(false);
    }

    if (playerData.getHasSplit()) {
      return(false); //player has already split, and only allowed to split once
    }

    String cardKey0 = playerData.getCardKetAt(0);
    CardValues card0Value = CardValues.GetValueFromCardKey(cardKey0);
    if (card0Value == null) {
      return(false);
    }

    String cardKey1 = playerData.getCardKetAt(1);
    CardValues card1Value = CardValues.GetValueFromCardKey(cardKey1);
    if (card1Value == null) {
      return(false);
    }

    return(card0Value.getValue() == card1Value.getValue());

  }

  //-------------------------------------------------------------------------
  // doesPlayerHaveBlackjack
  //-------------------------------------------------------------------------
  public boolean doesPlayerHaveBlackjack(BasePlayerData playerData, IBjEngine engine) {
    if (playerData == null || playerData.getNumCards() != SM_NUM_CARDS_FOR_BLACKJACK) {
      return(false);
    }

    ArrayList<String> cardKeys = playerData.getCardKeys();
    int score = CalculateScore.Run(engine, cardKeys);
    return(score == M_BLACKJACK_POINTS);
  }

  //-------------------------------------------------------------------------
  // doesPlayerHaveMaxCards
  //-------------------------------------------------------------------------
  public boolean doesPlayerHaveMaxCards(BasePlayerData playerData) {
    return(
      playerData != null &&
      playerData.getNumCards() == M_MAX_CARDS_PER_HAND);
  }

  //-------------------------------------------------------------------------
  // doesPlayerHaveThunderjack
  //-------------------------------------------------------------------------
  public boolean doesPlayerHaveThunderjack(BasePlayerData playerData, IBjEngine engine) {
    return(doesPlayerHaveBlackjack(playerData, engine) && isFlush(playerData));
  }

  //-------------------------------------------------------------------------
  // isFlush
  //-------------------------------------------------------------------------
  public boolean isFlush(BasePlayerData playerData) {
    if (playerData == null) {
      return(false); //sanity check
    }

    ArrayList<String> cardKeys = playerData.getCardKeys();
    if (cardKeys == null) {
      return(false); //sanity check
    }

    CardSuits baseCardSuit = null;
    for (String cardKey : cardKeys) {
      CardSuits cardSuit = CardSuits.GetSuitFromCardKey(cardKey);
      if (baseCardSuit == null) {
        if (cardSuit == null) {
          return(false);
        }

        baseCardSuit = cardSuit;
      } else {
        if (!baseCardSuit.equals(cardSuit)) {
          return(false);
        }
      }
    }

    return(true);
  }
}