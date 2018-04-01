package com.example.cartrell.blackjack.engine;

import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

import com.example.cartrell.blackjack.databinding.ActivityMainBinding;
import com.example.cartrell.blackjack.players.BasePlayerData;
import com.example.cartrell.blackjack.players.PlayerData;
import com.example.cartrell.blackjack.players.PlayerIds;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

class BjBetSystem {
  //=========================================================================
  // const
  //=========================================================================
  private static final String LOG_TAG = "BjBetSystem";

  //=========================================================================
  // members
  //=========================================================================
  private IBjEngine m_engine;
  private String m_selectedChipId;

  //=========================================================================
  // public
  //=========================================================================

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  BjBetSystem(IBjEngine engine) {
    m_engine = engine;
  }

  //-------------------------------------------------------------------------
  // begin
  //-------------------------------------------------------------------------
  void begin() {
    m_engine.showGameButtons(BjGameButtonFlags.NONE);
    showPlaceBetButtons();

    if (!m_engine.atLeastOneRoundPlayed()) {
      initUis();
    }

    showBetChipButtons();
    setBetButtonsVisibility(true);
    updateDealButtonEnability();
  }

  //-------------------------------------------------------------------------
  // onBetChipClick
  //-------------------------------------------------------------------------
  void onBetChipClick(String chipId) {
    BjBetChipData betChipData = m_engine.getBetChipData(chipId);
    if (betChipData == null) {
      Log.w(LOG_TAG, "onBetChipClick. Invalid chip id: " + chipId);
      return;
    }

    unselectAllBetChips();
    selectBetChip(chipId);
  }

  //-------------------------------------------------------------------------
  // onBetClearClick
  //-------------------------------------------------------------------------
  void onBetClearClick() {
    removePlayersBets();
    showBetChipButtons();
    updateDealButtonEnability();
  }

  //-------------------------------------------------------------------------
  // onDealClick
  //-------------------------------------------------------------------------
  void onDealClick() {
    m_engine.setCredits(-m_engine.getBetValue(), true);
    hideBetChipButtons();
    hidePlaceBetButtons();
    hideTotalWon();
    setBetButtonsVisibility(false);
    restoreDealer();
    restoreNormalPlayers();
    removeSplitPlayers();
    m_engine.beginCardsPrep();
  }

  //-------------------------------------------------------------------------
  // placeBet
  //-------------------------------------------------------------------------
  void placeBet(PlayerIds playerId) {
    PlayerData playerData = m_engine.getPlayer(playerId);
    if (playerData == null) {
      Log.w(LOG_TAG, "placeBet. Invalid player id: " + playerId);
      return;
    }

    BjBetChipData betChipData = m_engine.getBetChipData(m_selectedChipId);
    if (betChipData == null) {
      Log.w(LOG_TAG, "placeBet. Invalid chip id: " + m_selectedChipId);
      return;
    }

    int betChipValue = betChipData.getValue();
    m_engine.setPlayerBet(playerId, playerData.getBetValue() + betChipValue);
    m_engine.updateBetValue();
    showBetChipButtons();
    updateDealButtonEnability();
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // hideBetChipButtons
  //-------------------------------------------------------------------------
  private void hideBetChipButtons() {
    List<String> betChipIds = m_engine.getBetChipIds();
    for (String betChipId : betChipIds) {
      m_engine.setBetChipVisibility(betChipId, false);
    }
  }

  //-------------------------------------------------------------------------
  // hidePlaceBetButtons
  //-------------------------------------------------------------------------
  private void hidePlaceBetButtons() {
    ActivityMainBinding binding = m_engine.getBinding();
    m_engine.showView(binding.btnPlayer0PlaceBet, false);
    m_engine.showView(binding.btnPlayer1PlaceBet, false);
    m_engine.showView(binding.btnPlayer2PlaceBet, false);
  }

  //-------------------------------------------------------------------------
  // hideTotalWon
  //-------------------------------------------------------------------------
  private void hideTotalWon() {
    m_engine.showView(m_engine.getBinding().txtTotalWon, false);
  }

  //-------------------------------------------------------------------------
  // initPlayersUis
  //-------------------------------------------------------------------------
  private void initPlayersUis() {
    Iterator iterator = m_engine.getPlayers().entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry)iterator.next();
      PlayerData playerData = (PlayerData)entry.getValue();
      playerData.setResultImageVisible(false);
      playerData.setScoreTextVisible(false);
      playerData.setBetValueVisible(false);
      playerData.setAmountWonVisible(false);
    }

    BasePlayerData dealerData = m_engine.getDealerData();
    dealerData.setResultImageVisible(false);
    dealerData.setScoreTextVisible(false);
  }

  //-------------------------------------------------------------------------
  // initUis
  //-------------------------------------------------------------------------
  private void initUis() {
    initPlayersUis();
    hideTotalWon();
  }

  //-------------------------------------------------------------------------
  // removePlayerBet
  //-------------------------------------------------------------------------
  private void removePlayerBet(PlayerIds playerId) {
    PlayerData playerData = m_engine.getPlayer(playerId);
    playerData.setBetValue(0);
    playerData.removeBetChips();
    playerData.setBetValueVisible(false);
    playerData.setResultImageVisible(false);
    playerData.setScoreTextVisible(false);
    playerData.setAmountWonVisible(false);
  }

  //-------------------------------------------------------------------------
  // removePlayersBets
  //-------------------------------------------------------------------------
  private void removePlayersBets() {
    Set<PlayerIds>playerIds = m_engine.getPlayers().keySet();
    for (PlayerIds playerId : playerIds) {
      removePlayerBet(playerId);
    }

    m_engine.updateBetValue();
    showBetChipButtons();
  }

  //-------------------------------------------------------------------------
  // removeSplitPlayer
  //-------------------------------------------------------------------------
  private void removeSplitPlayer(PlayerIds playerId) {
    PlayerData playerData = m_engine.getPlayer(playerId);
    playerData.setBetValue(0);
    playerData.setBetValueVisible(false);
    playerData.setScoreTextVisible(false);
    playerData.setAmountWonVisible(false);
    playerData.setResultImageVisible(false);
    playerData.removeBetChips();
  }

  //-------------------------------------------------------------------------
  // removeSplitPlayers
  //-------------------------------------------------------------------------
  private void removeSplitPlayers() {
    removeSplitPlayer(PlayerIds.LEFT_TOP);
    removeSplitPlayer(PlayerIds.MIDDLE_TOP);
    removeSplitPlayer(PlayerIds.RIGHT_TOP);
  }

  //-------------------------------------------------------------------------
  // restoreDealer
  //-------------------------------------------------------------------------
  private void restoreDealer() {
    BasePlayerData dealerData = m_engine.getDealerData();
    dealerData.setResultImageVisible(false);
    dealerData.setScoreTextVisible(false);
  }

  //-------------------------------------------------------------------------
  // restoreNormalPlayer
  //-------------------------------------------------------------------------
  private void restoreNormalPlayer(PlayerIds playerId) {
    PlayerData playerData = m_engine.getPlayer(playerId);
    if (playerData.getBetValue() > 0) {
      playerData.setBetValueVisible(true);
      playerData.setScoreTextVisible(false);
      playerData.setResultImageVisible(false);
      playerData.setAmountWonVisible(false);
      playerData.showBetChips();
    }
  }

  //-------------------------------------------------------------------------
  // restoreNormalPlayers
  //-------------------------------------------------------------------------
  private void restoreNormalPlayers() {
    restoreNormalPlayer(PlayerIds.RIGHT_BOTTOM);
    restoreNormalPlayer(PlayerIds.LEFT_BOTTOM);
    restoreNormalPlayer(PlayerIds.MIDDLE_BOTTOM);
  }

  //-------------------------------------------------------------------------
  // selectBetChip
  //-------------------------------------------------------------------------
  private void selectBetChip(String chipId) {
    m_selectedChipId = chipId;
    ToggleButton button = m_engine.getBinding().getRoot().findViewWithTag(m_selectedChipId);
    if (button != null) {
      button.setEnabled(false);
      button.setChecked(true);
    }
  }

  //-------------------------------------------------------------------------
  // setBetButtonsVisibility
  //-------------------------------------------------------------------------
  private void setBetButtonsVisibility(boolean isVisible) {
    ActivityMainBinding binding = m_engine.getBinding();
    m_engine.showView(binding.btnClear, isVisible);
    m_engine.showView(binding.btnDeal, isVisible);
  }

  //-------------------------------------------------------------------------
  // showBetChipButtons
  //-------------------------------------------------------------------------
  private void showBetChipButtons() {
    int reserveCredits = m_engine.getCredits() - m_engine.getBetValue();
    List<String> betChipIds = m_engine.getBetChipIds();
    for (String betChipId : betChipIds) {
      BjBetChipData betChipData = m_engine.getBetChipData(betChipId);
      boolean canAffordChip = reserveCredits >= betChipData.getValue();
      m_engine.setBetChipVisibility(betChipId, canAffordChip);
    }
  }

  //-------------------------------------------------------------------------
  // showPlaceBetButton
  //-------------------------------------------------------------------------
  private void showPlaceBetButton(View view) {
    view.setVisibility(View.VISIBLE);
    view.getParent().bringChildToFront(view);
  }

  //-------------------------------------------------------------------------
  // showPlaceBetButtons
  //-------------------------------------------------------------------------
  private void showPlaceBetButtons() {
    ActivityMainBinding binding = m_engine.getBinding();
    showPlaceBetButton(binding.btnPlayer0PlaceBet);
    showPlaceBetButton(binding.btnPlayer1PlaceBet);
    showPlaceBetButton(binding.btnPlayer2PlaceBet);
  }

  //-------------------------------------------------------------------------
  // updateDealButtonEnability
  //-------------------------------------------------------------------------
  private void updateDealButtonEnability() {
    m_engine.getBinding().btnDeal.setEnabled(m_engine.getBetValue() > 0);
  }

  //-------------------------------------------------------------------------
  // unselectAllBetChips
  //-------------------------------------------------------------------------
  private void unselectAllBetChips() {
    ActivityMainBinding binding = m_engine.getBinding();
    unselectBetChip(binding.chipButtonBlue);
    unselectBetChip(binding.chipButtonGreen);
    unselectBetChip(binding.chipButtonPurple);
    unselectBetChip(binding.chipButtonRed);
    m_selectedChipId = null;
  }

  //-------------------------------------------------------------------------
  // unselectBetChip
  //-------------------------------------------------------------------------
  private void unselectBetChip(ToggleButton button) {
    button.setChecked(false);
    button.setEnabled(true);
  }
}