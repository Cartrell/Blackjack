package com.gameplaycoder.thunderjack.cards;

public enum CardValues {
  TWO(2, "2"),
  THREE(3, "3"),
  FOUR(4, "4"),
  FIVE(5, "5"),
  SIX(6, "6"),
  SEVEN(7, "7"),
  EIGHT(8, "8"),
  NINE(9, "9"),
  TEN(10, "10"),
  JACK(10, "j"),
  QUEEN(10,"q"),
  KING(10, "k"),
  ACE(1, "a", 11);

  private final int m_value;
  private final int m_secondValue;
  private final String m_key;

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  CardValues(int value, String key) {
    m_value = value;
    m_key = key;
    m_secondValue = 0;
  }

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  CardValues(int value, String key, int secondValue) {
    m_value = value;
    m_key = key;
    m_secondValue = secondValue;
  }

  //-------------------------------------------------------------------------
  // getKey
  //-------------------------------------------------------------------------
  public String getKey() {
    return(m_key);
  }

  //-------------------------------------------------------------------------
  // getSecondValue
  //-------------------------------------------------------------------------
  public int getSecondValue() {
    return(m_secondValue);
  }

  //-------------------------------------------------------------------------
  // getValue
  //-------------------------------------------------------------------------
  public int getValue() {
    return(m_value);
  }

  //-------------------------------------------------------------------------
  // GetValueFromCardKey
  //-------------------------------------------------------------------------
  public static CardValues GetValueFromCardKey(String cardKey) {
    if (cardKey == null) {
      return(null);
    }

    String[] parts = cardKey.split("_");
    if (parts.length != 3) {
      return(null);
    }

    final String valueKey = parts[0];
    for (CardValues value : CardValues.values()) {
      if (value.getKey().equals(valueKey)) {
        return(value);
      }
    }

    return(null);
  }
}
