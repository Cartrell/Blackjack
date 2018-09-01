package com.example.cartrell.blackjack.engine;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.example.cartrell.blackjack.R;
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
  private static final String LOG_TAG = BjBetSystem.class.getName();

  //=========================================================================
  // members
  //=========================================================================
  private IBjEngine m_engine;
  private String m_selectedChipId;
  private boolean m_restorePlayersOnFirstBet;

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

    m_restorePlayersOnFirstBet = true;
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
    Views views = m_engine.getViews();
    m_engine.showView(views.getBetButtonLeft(), false);
    m_engine.showView(views.getBetButtonMid(), false);
    m_engine.showView(views.getBetButtonRight(), false);
  }

  //-------------------------------------------------------------------------
  // hideTotalWon
  //-------------------------------------------------------------------------
  private void hideTotalWon() {
    m_engine.showView(m_engine.getViews().getTextTotalWon(), false);
  }

  //-------------------------------------------------------------------------
  // initBetButtons
  //-------------------------------------------------------------------------
  private void initBetButtons() {
    View.OnClickListener listener = new View.OnClickListener() {

      //-------------------------------------------------------------------------
      // onClick
      //-------------------------------------------------------------------------
      @Override
      public void onClick(View view) {
        int id = view.getId();
        switch (id) {
          case R.id.btnLeftPlayerBet:
            placeBet(PlayerIds.LEFT_BOTTOM);
            break;

          case R.id.btnMidPlayerBet:
            placeBet(PlayerIds.MIDDLE_BOTTOM);
            break;

          case R.id.btnRightPlayerBet:
            placeBet(PlayerIds.RIGHT_BOTTOM);
            break;
        }
      }
    };

    Views views = m_engine.getViews();
    views.getBetButtonLeft().setOnClickListener(listener);
    views.getBetButtonMid().setOnClickListener(listener);
    views.getBetButtonRight().setOnClickListener(listener);
  }

  //-------------------------------------------------------------------------
  // initBetChipButtons
  //-------------------------------------------------------------------------
  private void initBetChipButtons() {
    View.OnClickListener listener = new View.OnClickListener() {

      //-------------------------------------------------------------------------
      // onClick
      //-------------------------------------------------------------------------
      @Override
      public void onClick(View view) {
        String chipId = (String) view.getTag();
        BjBetChipData betChipData = m_engine.getBetChipData(chipId);
        if (betChipData == null) {
          Log.w(LOG_TAG, "initBetChipButtons.onClick. Warning: Invalid chip id: " + chipId);
          return;
        }

        unselectAllBetChips();
        selectBetChip(chipId);
      }
    };

    Views views = m_engine.getViews();
    views.getBetChipBlue().setOnClickListener(listener);
    views.getBetChipGreen().setOnClickListener(listener);
    views.getBetChipPurple().setOnClickListener(listener);
    views.getBetChipRed().setOnClickListener(listener);
  }

  //-------------------------------------------------------------------------
  // initClearButton
  //-------------------------------------------------------------------------
  private void initClearButton() {
    ImageButton button = m_engine.getViews().getClearButton();

    button.setOnClickListener(new View.OnClickListener() {
      //-------------------------------------------------------------------------
      // onClick
      //-------------------------------------------------------------------------
      @Override
      public void onClick(View view) {
        m_engine.getSoundSystem().play(R.raw.snd_bet_remove);
        removePlayersBets();
        showBetChipButtons();
        updateDealButtonEnability();
      }
    });
  }

  //-------------------------------------------------------------------------
  // initDealButton
  //-------------------------------------------------------------------------
  private void initDealButton() {
    ImageButton button = m_engine.getViews().getDealButton();

    button.setOnClickListener(new View.OnClickListener() {

      //-------------------------------------------------------------------------
      // onClick
      //-------------------------------------------------------------------------
      @Override
      public void onClick(View view) {
        hideBetChipButtons();
        hidePlaceBetButtons();
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
    initClearButton();
    initDealButton();
    initBetChipButtons();
    initBetButtons();
  }

  //-------------------------------------------------------------------------
  // placeBet
  //-------------------------------------------------------------------------
  private void placeBet(PlayerIds playerId) {
    restorePlayersOnFirstBet();

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

    m_engine.getSoundSystem().play(R.raw.snd_bet_add);

    int betChipValue = betChipData.getValue();
    m_engine.setPlayerBet(playerId, playerData.getOrigBetValue() + betChipValue, true);
    m_engine.updateBetValue();
    showBetChipButtons();
    updateDealButtonEnability();
  }

  //-------------------------------------------------------------------------
  // removePlayersBets
  //-------------------------------------------------------------------------
  private void removePlayersBets() {
    Set<PlayerIds>playerIds = m_engine.getPlayers().keySet();
    for (PlayerIds playerId : playerIds) {
      m_engine.setPlayerBet(playerId, 0, true);
    }

    m_restorePlayersOnFirstBet = false;
    m_engine.updateBetValue();
    showBetChipButtons();
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
  // restorePlayersOnFirstBet
  //-------------------------------------------------------------------------
  private void restorePlayersOnFirstBet() {
    if (!m_restorePlayersOnFirstBet) {
      return;
    }

    m_restorePlayersOnFirstBet = false;
    restoreDealer();
    restoreNormalPlayers();
    removeSplitPlayers();
    m_engine.updateBetValue();
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
    Views views = m_engine.getViews();
    m_engine.showView(views.getClearButton(), isVisible);
    m_engine.showView(views.getDealButton(), isVisible);
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
    m_engine.showView(view, true);
    view.getParent().bringChildToFront(view);
  }

  //-------------------------------------------------------------------------
  // showPlaceBetButtons
  //-------------------------------------------------------------------------
  private void showPlaceBetButtons() {
    Views views = m_engine.getViews();
    showPlaceBetButton(views.getBetButtonLeft());
    showPlaceBetButton(views.getBetButtonMid());
    showPlaceBetButton(views.getBetButtonRight());
  }

  //-------------------------------------------------------------------------
  // updateDealButtonEnability
  //-------------------------------------------------------------------------
  private void updateDealButtonEnability() {
    m_engine.getViews().getDealButton().setEnabled(m_engine.getBetValue() > 0);
  }

  //-------------------------------------------------------------------------
  // unselectAllBetChips
  //-------------------------------------------------------------------------
  private void unselectAllBetChips() {
    Views views = m_engine.getViews();
    unselectBetChip(views.getBetChipBlue());
    unselectBetChip(views.getBetChipGreen());
    unselectBetChip(views.getBetChipPurple());
    unselectBetChip(views.getBetChipRed());
    m_selectedChipId = null;
  }

  //-------------------------------------------------------------------------
  // unselectBetChip
  //-------------------------------------------------------------------------
  private void unselectBetChip(ToggleButton button) {
    if (button != null) {
      button.setChecked(false);
      button.setEnabled(true);
    }
  }
}