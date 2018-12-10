package com.gameplaycoder.thunderjack.utils;

import com.gameplaycoder.thunderjack.cards.CardSuits;
import com.gameplaycoder.thunderjack.cards.CardValues;
import com.gameplaycoder.thunderjack.engine.IBjEngine;
import com.gameplaycoder.thunderjack.players.BasePlayerData;
import com.gameplaycoder.thunderjack.players.PlayerData;

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

    String cardKey1 = playerData.getCardKetAt(1);
    CardValues card1Value = CardValues.GetValueFromCardKey(cardKey1);

    return(
      card0Value != null &&
      card1Value != null &&
      card0Value.getKey().equals(card1Value.getKey()));
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
  /**
   * Determines if the player's hand is a Thunderjack. A Thunderjack follows the same rules of
   * a blackjack, with the following conditions:
   * 1) The hand is a flush (both cards are of the same suit)
   * 2) The non-ace card is a jack or higher
   * @param playerData - The player whos hand is being evaluated
   * @param engine - Reference to the game engine.
   * @return - True if the player's hand is a Thunderjack.
   */
  public boolean doesPlayerHaveThunderjack(BasePlayerData playerData, IBjEngine engine) {
    if (!doesPlayerHaveBlackjack(playerData, engine) || !isFlush(playerData)) {
      return(false);
    }

    ArrayList<String> cardKeys = playerData.getCardKeys();
    CardValues cardValue = CardValues.GetValueFromCardKey(cardKeys.get(0));
    if (cardValue.equals(CardValues.ACE)) {
      //we're interested in the non-ace card
      cardValue = CardValues.GetValueFromCardKey(cardKeys.get(1));
    }

    return(cardValue.equals(CardValues.JACK) ||
      cardValue.equals(CardValues.QUEEN) ||
      cardValue.equals(CardValues.KING));
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