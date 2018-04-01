package com.example.cartrell.blackjack.engine;

import java.util.EnumSet;

enum BjGameButtonFlags {
  DOUBLE,
  HIT,
  STAND,
  SPLIT,
  SURRENDER;

  public static final EnumSet<BjGameButtonFlags> ALL = EnumSet.allOf(BjGameButtonFlags.class);
  public static final EnumSet<BjGameButtonFlags> NONE = EnumSet.noneOf(BjGameButtonFlags.class);
}
