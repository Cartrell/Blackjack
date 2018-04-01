package com.example.cartrell.blackjack.cards;

public enum CardSuits {
  HEARTS("h"),
  DIAMONDS("d"),
  CLUBS("c"),
  SPADES("s");

  private final String m_key;

  CardSuits(String key) {
    m_key = key;
  }

  public String getKey() {
    return(m_key);
  }
}
