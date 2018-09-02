package com.example.cartrell.blackjack.engine;

import com.example.cartrell.blackjack.players.PlayerData;
import com.example.cartrell.blackjack.players.PlayerIds;
import com.example.cartrell.blackjack.settings.Settings;

class BjPlaySettingsManager {
  //=========================================================================
  // members
  //=========================================================================
  private IBjEngine m_engine;

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  BjPlaySettingsManager(IBjEngine engine) {
    m_engine = engine;
  }

  //-------------------------------------------------------------------------
  // update
  //-------------------------------------------------------------------------
  void update() {
    incGamesPlayed();
    updateForPlayers();
    updateGamesWon();
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // isDealerId
  //-------------------------------------------------------------------------
  private boolean isDealerId(PlayerIds playerId) {
    return(PlayerIds.DEALER.equals(playerId));
  }

  //-------------------------------------------------------------------------
  // incGamesPlayed
  //-------------------------------------------------------------------------
  private void incGamesPlayed() {
    Settings settings = m_engine.getSettings();
    settings.setTotalGamesPlayed(settings.getTotalGamesPlayed() + 1);
  }

  //-------------------------------------------------------------------------
  // incGamesWon
  //-------------------------------------------------------------------------
  private void incGamesWon() {
    Settings settings = m_engine.getSettings();
    settings.setTotalGamesWon(settings.getTotalGamesWon() + 1);
  }

  //-------------------------------------------------------------------------
  // incNumBjs
  //-------------------------------------------------------------------------
  private void incNumBjs() {
    Settings settings = m_engine.getSettings();
    settings.setNumBj(settings.getNumBj() + 1);
  }

  //-------------------------------------------------------------------------
  // incNumBlitzs
  //-------------------------------------------------------------------------
  private void incNumBlitzs() {
    Settings settings = m_engine.getSettings();
    settings.setNumBlitzs(settings.getNumBlitzs() + 1);
  }

  //-------------------------------------------------------------------------
  // incNumDoublesWon
  //-------------------------------------------------------------------------
  private void incNumDoublesWon() {
    Settings settings = m_engine.getSettings();
    settings.setNumDoublesWon(settings.getNumDoubles() + 1);
  }

  //-------------------------------------------------------------------------
  // incNumSplitsWon
  //-------------------------------------------------------------------------
  private void incNumSplitsWon() {
    Settings settings = m_engine.getSettings();
    settings.setNumSplits(settings.getNumSplits() + 1);
  }

  //-------------------------------------------------------------------------
  // incNumSurrenders
  //-------------------------------------------------------------------------
  private void incNumSurrenders() {
    Settings settings = m_engine.getSettings();
    settings.setNumSurrenders(settings.getNumSurrenders() + 1);
  }

  //-------------------------------------------------------------------------
  // updateForPlayers
  //-------------------------------------------------------------------------
  private void updateForPlayers() {
    for (PlayerIds playerId : PlayerIds.values()) {
      if (isDealerId(playerId)) {
        continue;
      }

      PlayerData playerData = m_engine.getPlayer(playerId);
      if (playerData.getIsNormalWin()) {
        if (playerData.getHasSplit()) {
          incNumSplitsWon();
        }

        if (playerData.getIsDoubleDown()) {
          incNumDoublesWon();
        }
      }

      if (playerData.getHasSurrendered()) {
        incNumSurrenders();
      }

      if (playerData.getIsBlitzWin()) {
        incNumBlitzs();
      }

      if (playerData.getHasBlackjack()) {
        incNumBjs();
      }
    }
  }

  //-------------------------------------------------------------------------
  // updateGamesWon
  //-------------------------------------------------------------------------
  private void updateGamesWon() {
    if (m_engine.getCredits() > m_engine.getCreditsAtStartOfRound()) {
      incGamesWon();
    }
  }
}