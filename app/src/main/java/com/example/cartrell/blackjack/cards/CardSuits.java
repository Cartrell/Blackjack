package com.example.cartrell.blackjack.cards;

public enum CardSuits {
  HEARTS("h"),
  DIAMONDS("d"),
  CLUBS("c"),
  SPADES("s");

  private final String m_key;

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  CardSuits(String key) {
    m_key = key;
  }

  //-------------------------------------------------------------------------
  // getKey
  //-------------------------------------------------------------------------
  public String getKey() {
    return(m_key);
  }

  //-------------------------------------------------------------------------
  // GetSuitFromCardKey
  //-------------------------------------------------------------------------
  public static CardSuits GetSuitFromCardKey(String cardKey) {
    if (cardKey == null) {
      return(null);
    }

    String[] parts = cardKey.split("_");
    if (parts.length != 3) {
      return(null);
    }

    final String suitKey = parts[1];
    for (CardSuits suit : CardSuits.values()) {
      if (suit.getKey().equals(suitKey)) {
        return(suit);
      }
    }

    return(null);
  }
}
