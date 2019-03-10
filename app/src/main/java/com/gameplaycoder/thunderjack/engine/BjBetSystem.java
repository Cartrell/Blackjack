package com.gameplaycoder.thunderjack.engine;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import com.gameplaycoder.thunderjack.R;
import com.gameplaycoder.thunderjack.placeBets.PlaceBetsActivity;
import com.gameplaycoder.thunderjack.placeBets.PlaceBetsIntentKeys;
import com.gameplaycoder.thunderjack.players.BasePlayerData;
import com.gameplaycoder.thunderjack.players.PlayerData;
import com.gameplaycoder.thunderjack.players.PlayerIds;

import java.util.Iterator;
import java.util.Map;

class BjBetSystem {
  //=========================================================================
  // members
  //=========================================================================
  private IBjEngine m_engine;
  private Activity m_activity;

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  BjBetSystem(IBjEngine engine, Activity activity) {
    m_engine = engine;
    m_activity = activity;
  }

  //-------------------------------------------------------------------------
  // begin
  //-------------------------------------------------------------------------
  void begin() {
    m_engine.showGameButtons(BjGameButtonFlags.NONE);

    boolean isNewBet;
    if (!m_engine.atLeastOneRoundPlayed()) {
      initUis();
      placeStartingPlayerBets();
      isNewBet = true;
    } else {
      isNewBet = false;
    }

    setBetButtonsVisibility(true);
    updateDealButtonEnability();
    updateDealButtonMode(isNewBet);
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // hideTotalWon
  //-------------------------------------------------------------------------
  private void hideTotalWon() {
    m_engine.showView(m_engine.getLayoutComps().betAndCreditsTexts.txtTotalWon, false);
  }

  //-------------------------------------------------------------------------
  // initDealButton
  //-------------------------------------------------------------------------
  private void initDealButton() {
    ImageButton button = m_engine.getLayoutComps().betButtons.dealButton;
    button.setOnClickListener(new View.OnClickListener() {
      //-------------------------------------------------------------------------
      // onClick
      //-------------------------------------------------------------------------
      @Override
      public void onClick(View v) {
        hideTotalWon();
        setBetButtonsVisibility(false);
        restoreDealer();
        restoreNormalPlayers();
        removeSplitPlayers();
        m_engine.updateCreditsAtStartOfRound();
        m_engine.updateBetValue();
        m_engine.setCredits(-m_engine.getBetValue(), true);
        m_engine.beginCardsPrep();
        m_engine.setAtLeastOneRoundPlayed();
      }
    });
  }

  //-------------------------------------------------------------------------
  // initNewBetButton
  //-------------------------------------------------------------------------
  private void initNewBetButton() {
    ImageButton button = m_engine.getLayoutComps().betButtons.newBetButton;
    button.setOnClickListener(new View.OnClickListener() {
      //-------------------------------------------------------------------------
      // onClick
      //-------------------------------------------------------------------------
      @Override
      public void onClick(View v) {
        startPlaceBetsActivity();
      }
    });
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
      playerData.setAmountWonValueVisible(false);
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
    initNewBetButton();
    initDealButton();
  }

  //-------------------------------------------------------------------------
  // placeBets
  //-------------------------------------------------------------------------
  private void placeBets(int leftPlayerBetValue, int middlePlayerBetValue, int rightPlayerBetValue) {
    final boolean IS_ORIG_BET = true;
    m_engine.setPlayerBet(PlayerIds.LEFT_BOTTOM, leftPlayerBetValue, IS_ORIG_BET);
    m_engine.setPlayerBet(PlayerIds.MIDDLE_BOTTOM, middlePlayerBetValue, IS_ORIG_BET);
    m_engine.setPlayerBet(PlayerIds.RIGHT_BOTTOM, rightPlayerBetValue, IS_ORIG_BET);
    m_engine.updateBetValue();
    updateDealButtonEnability();
  }

  //-------------------------------------------------------------------------
  // placeStartingPlayerBets
  //-------------------------------------------------------------------------
  private void placeStartingPlayerBets() {
    placeBets(
      m_engine.getIntegerResource(R.integer.startingBetLeftPlayer),
      m_engine.getIntegerResource(R.integer.startingBetMiddlePlayer),
      m_engine.getIntegerResource(R.integer.startingBetRightPlayer));
  }

  //-------------------------------------------------------------------------
  // removeSplitPlayer
  //-------------------------------------------------------------------------
  private void removeSplitPlayer(PlayerIds playerId) {
    m_engine.setPlayerBet(playerId, 0, true);
    PlayerData playerData = m_engine.getPlayer(playerId);
    playerData.setScoreTextVisible(false);
    playerData.setResultImageVisible(false);
    playerData.setAmountWonValueVisible(false);
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
    if (playerData.getOrigBetValue() > 0) {
      m_engine.setPlayerBet(playerId, playerData.getOrigBetValue(), false);
    }

    playerData.setScoreTextVisible(false);
    playerData.setResultImageVisible(false);
    playerData.setAmountWonValueVisible(false);
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
  // setBetButtonsVisibility
  //-------------------------------------------------------------------------
  private void setBetButtonsVisibility(boolean isVisible) {
    m_engine.getLayoutComps().betButtons.setIsVisible(isVisible);
  }

  //-------------------------------------------------------------------------
  // startPlaceBetsActivity
  //-------------------------------------------------------------------------
  private void startPlaceBetsActivity() {
    Intent intent = new Intent(m_engine.getContext(), PlaceBetsActivity.class);
    intent.putExtra(PlaceBetsIntentKeys.CREDITS, m_engine.getCredits());
    intent.putExtra(PlaceBetsIntentKeys.LEFT_PLAYER_BET_VALUE, m_engine.getPlayer(PlayerIds.LEFT_BOTTOM).getOrigBetValue());
    intent.putExtra(PlaceBetsIntentKeys.MIDDLE_PLAYER_BET_VALUE, m_engine.getPlayer(PlayerIds.MIDDLE_BOTTOM).getOrigBetValue());
    intent.putExtra(PlaceBetsIntentKeys.RIGHT_PLAYER_BET_VALUE, m_engine.getPlayer(PlayerIds.RIGHT_BOTTOM).getOrigBetValue());
    m_activity.startActivityForResult(intent, 0);

    m_engine.setOnActivityResult(new IBjEngineOnActivityResult() {
      //-------------------------------------------------------------------------
      // onResult
      //-------------------------------------------------------------------------
      @Override
      public void onResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
          case Activity.RESULT_OK:
            placeBets(
              data.getIntExtra(PlaceBetsIntentKeys.LEFT_PLAYER_BET_VALUE, 0),
              data.getIntExtra(PlaceBetsIntentKeys.MIDDLE_PLAYER_BET_VALUE, 0),
              data.getIntExtra(PlaceBetsIntentKeys.RIGHT_PLAYER_BET_VALUE, 0));
            m_engine.getSoundSystem().playAddBet();
            updateDealButtonMode(true);
            break;
        }
      }
    });
  }

  //-------------------------------------------------------------------------
  // updateDealButtonEnability
  //-------------------------------------------------------------------------
  private void updateDealButtonEnability() {
    int betValue = m_engine.getBetValue();
    boolean isBetPlaced = betValue > 0;
    boolean canAffordBet = m_engine.getCredits() >= betValue;
    m_engine.getLayoutComps().betButtons.dealButton.setEnabled(isBetPlaced && canAffordBet);
  }

  //-------------------------------------------------------------------------
  // updateDealButtonMode
  //-------------------------------------------------------------------------
  private void updateDealButtonMode(boolean isNewBet) {
    if (isNewBet) {
      m_engine.getLayoutComps().betButtons.setDealButton();
    } else {
      m_engine.getLayoutComps().betButtons.setRebetButton();
    }
  }
}