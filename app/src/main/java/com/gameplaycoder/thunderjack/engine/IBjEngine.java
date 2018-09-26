package com.gameplaycoder.thunderjack.engine;

import android.content.Context;
import android.view.View;

import com.gameplaycoder.thunderjack.cards.Deck;
import com.gameplaycoder.thunderjack.databinding.ActivityMainBinding;
import com.gameplaycoder.thunderjack.players.BasePlayerData;
import com.gameplaycoder.thunderjack.players.PlayerData;
import com.gameplaycoder.thunderjack.players.PlayerIds;
import com.gameplaycoder.thunderjack.settings.Settings;
import com.gameplaycoder.thunderjack.sound.SoundSystem;

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
  int getCreditsAtStartOfRound();
  BasePlayerData getDealerData();
  Deck getDeck();
  int getColorResource(int resourceId);
  int getIndexOf(View view);
  PlayerData getPlayer(PlayerIds playerId);
  HashMap<PlayerIds, PlayerData> getPlayers();
  int getIntegerResource(int resourceId);
  Settings getSettings();
  public SoundSystem getSoundSystem();
  String getStringResource(int resourceId);
  Views getViews();
  boolean isSplitPlayerId(PlayerIds playerId);
  void setAtLeastOneRoundPlayed();
  void setBetChipVisibility(String chipId, boolean isVisible);
  void setCredits(int value);
  void setCredits(int credits, boolean isOffset);
  void setPlayerBet(PlayerIds playerId, int betValue, boolean isOrigBet);
  void showGameButtons(EnumSet<BjGameButtonFlags> flags);
  void showView(View view, boolean isVisible);
  void updateBetValue();
  void updateCreditsAtStartOfRound();
  void writeSettings();
}