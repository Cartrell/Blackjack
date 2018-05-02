package com.example.cartrell.blackjack.utils;

import com.example.cartrell.blackjack.R;
import com.example.cartrell.blackjack.cards.Card;
import com.example.cartrell.blackjack.cards.CardValues;
import com.example.cartrell.blackjack.engine.IBjEngine;

import java.util.ArrayList;

public final class CalculateScore {

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // Run
  //-------------------------------------------------------------------------
  public static int Run(IBjEngine engine, ArrayList<String> cardKeys) {
    int points = 0;
    boolean containsAtLeastOneAce = false;

    for (String cardKey : cardKeys) {
      Card card = engine.getDeck().getCard(cardKey);
      CardValues cardValue = card.getValue();
      points += cardValue.getValue();

      if (cardValue.equals(CardValues.ACE)) {
        containsAtLeastOneAce = true;
      }
    }

    if (containsAtLeastOneAce) {
      final int ACE_POINTS_REMAINING = CardValues.ACE.getSecondValue() - 1;
      final int BLACKJACK_POINTS = engine.getIntegerResource(R.integer.blackjackPoints);
      if (points + ACE_POINTS_REMAINING <= BLACKJACK_POINTS) {
        points = points + ACE_POINTS_REMAINING;
      }
    }

    return(points);
  }
}
