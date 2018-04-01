package com.example.cartrell.blackjack.engine;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.example.cartrell.blackjack.cards.Deck;
import com.example.cartrell.blackjack.databinding.ActivityMainBinding;
import com.example.cartrell.blackjack.players.BasePlayerData;
import com.example.cartrell.blackjack.players.PlayerData;
import com.example.cartrell.blackjack.players.PlayerIds;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public interface IBjEngine {
  boolean atLeastOneRoundPlayed();
  void beginCardsPrep();
  void beginPlay();
  void beginRound();
  Context getContext();
  BjBetChipData getBetChipData(String betChipId);
  List<String> getBetChipIds();
  int getBetValue();
  ActivityMainBinding getBinding();
  int getCredits();
  BasePlayerData getDealerData();
  Deck getDeck();
  ImageView getDeckImage();
  int getColorResource(int resourceId);
  int getIndexOf(View view);
  PlayerData getPlayer(PlayerIds playerId);
  HashMap<PlayerIds, PlayerData> getPlayers();
  int getIntegerResource(int resourceId);
  String getStringResource(int resourceId);
  void setBetChipVisibility(String chipId, boolean isVisible);
  void setCredits(int value);
  void setCredits(int credits, boolean isOffset);
  void setPlayerBet(PlayerIds playerId, int betValue);
  void showGameButtons(EnumSet<BjGameButtonFlags> flags);
  void showView(View view, boolean isVisible);
  void updateBetValue();
}
