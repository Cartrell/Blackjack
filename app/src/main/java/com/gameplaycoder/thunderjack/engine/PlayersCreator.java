package com.gameplaycoder.thunderjack.engine;

import android.graphics.Point;
import android.support.constraint.Guideline;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gameplaycoder.thunderjack.R;
import com.gameplaycoder.thunderjack.databinding.ActivityMainBinding;
import com.gameplaycoder.thunderjack.players.BaseHandData;
import com.gameplaycoder.thunderjack.players.BasePlayerData;
import com.gameplaycoder.thunderjack.players.PlayerData;
import com.gameplaycoder.thunderjack.players.PlayerIds;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class PlayersCreator {
  //=========================================================================
  // members
  //=========================================================================
  private HashMap<PlayerIds, PlayerData> m_players;
  private BasePlayerData m_dealer;

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  PlayersCreator(BjLayoutComps layoutComps, ViewGroup targetLayout, float xDeck,
  float yDeck, int maxCards, BaseHandData.OnCardMoveStartListener cardMoveStartListener) {
    Point cardSize = layoutComps.getCardSize();
    Point chipSize = layoutComps.getChipSize();

    m_dealer = new BasePlayerData(
      targetLayout,
      PlayerIds.DEALER,
      xDeck,
      yDeck,
      maxCards,
      layoutComps.getGuideline(R.id.guideMidCardsLeft),
      layoutComps.getGuideline(R.id.guideMidCardsRight),
      layoutComps.getGuideline(R.id.guideUpperCardsTop),
      layoutComps.getGuideline(R.id.guideUpperCardsBottom),
      layoutComps.getGuideline(R.id.guideUpperCardsUi),
      cardSize.x,
      layoutComps.upperScoreTexts.txtUpperMidScore,
      layoutComps.upperResultsImages.resultsUpperMid,
      null);

    m_players = new HashMap<>();

    initPlayerData(PlayerIds.RIGHT_BOTTOM,
      targetLayout,
      xDeck,
      yDeck,
      maxCards,
      layoutComps.getGuideline(R.id.guideRightCardsLeft),
      layoutComps.getGuideline(R.id.guideRightCardsRight),
      layoutComps.getGuideline(R.id.guideLowerCardsTop),
      layoutComps.getGuideline(R.id.guideBottomPanel),
      layoutComps.getGuideline(R.id.guideLowerCardsUi),
      cardSize.x,
      layoutComps.getGuideline(R.id.guideRightChipsLeft),
      layoutComps.getGuideline(R.id.guideRightChipsRight),
      chipSize,
      layoutComps.lowerScoreTexts.txtLowerRightScore,
      layoutComps.lowerResultsImages.imgLowerRight,
      layoutComps.getGuideline(R.id.guideLowerChipsTop),
      layoutComps.lowerScoreBetValueTexts.txtLowerRightBetValue,
      layoutComps.lowerScoreAmountWonTexts.txtLowerRightAmountWon,
      layoutComps.lowerTurnPlayerIndicators.turnPlayerLowerRight);

    initPlayerData(PlayerIds.MIDDLE_BOTTOM,
      targetLayout,
      xDeck,
      yDeck,
      maxCards,
      layoutComps.getGuideline(R.id.guideMidCardsLeft),
      layoutComps.getGuideline(R.id.guideMidCardsRight),
      layoutComps.getGuideline(R.id.guideLowerCardsTop),
      layoutComps.getGuideline(R.id.guideBottomPanel),
      layoutComps.getGuideline(R.id.guideLowerCardsUi),
      cardSize.x,
      layoutComps.getGuideline(R.id.guideMidChipsLeft),
      layoutComps.getGuideline(R.id.guideMidChipsRight),
      chipSize,
      layoutComps.lowerScoreTexts.txtLowerMidScore,
      layoutComps.lowerResultsImages.imgLowerMid,
      layoutComps.getGuideline(R.id.guideLowerChipsTop),
      layoutComps.lowerScoreBetValueTexts.txtLowerMidBetValue,
      layoutComps.lowerScoreAmountWonTexts.txtLowerMidAmountWon,
      layoutComps.lowerTurnPlayerIndicators.turnPlayerLowerMid);

    initPlayerData(PlayerIds.LEFT_BOTTOM,
      targetLayout,
      xDeck,
      yDeck,
      maxCards,
      layoutComps.getGuideline(R.id.guideLeftCardsLeft),
      layoutComps.getGuideline(R.id.guideLeftCardsRight),
      layoutComps.getGuideline(R.id.guideLowerCardsTop),
      layoutComps.getGuideline(R.id.guideBottomPanel),
      layoutComps.getGuideline(R.id.guideLowerCardsUi),
      cardSize.x,
      layoutComps.getGuideline(R.id.guideLeftChipsLeft),
      layoutComps.getGuideline(R.id.guideLeftChipsRight),
      chipSize,
      layoutComps.lowerScoreTexts.txtLowerLeftScore,
      layoutComps.lowerResultsImages.imgLowerLeft,
      layoutComps.getGuideline(R.id.guideLowerChipsTop),
      layoutComps.lowerScoreBetValueTexts.txtLowerLeftBetValue,
      layoutComps.lowerScoreAmountWonTexts.txtLowerLeftAmountWon,
      layoutComps.lowerTurnPlayerIndicators.turnPlayerLowerLeft);

    initPlayerData(PlayerIds.RIGHT_TOP,
      targetLayout,
      xDeck,
      yDeck,
      maxCards,
      layoutComps.getGuideline(R.id.guideRightCardsLeft),
      layoutComps.getGuideline(R.id.guideRightCardsRight),
      layoutComps.getGuideline(R.id.guideMidCardsTop),
      layoutComps.getGuideline(R.id.guideMidCardsBottom),
      layoutComps.getGuideline(R.id.guideMidCardsUi),
      cardSize.x,
      layoutComps.getGuideline(R.id.guideRightChipsLeft),
      layoutComps.getGuideline(R.id.guideRightChipsRight),
      chipSize,
      layoutComps.midScoreTexts.txtMidRightScore,
      layoutComps.midResultsImages.resultsMidRight,
      layoutComps.getGuideline(R.id.guideMidChipsTop),
      layoutComps.midScoreBetValueTexts.txtMidRightBetValue,
      layoutComps.midScoreAmountWonTexts.txtMidRightAmountWon,
      layoutComps.midTurnPlayerIndicators.turnPlayerMidRight);

    initPlayerData(PlayerIds.MIDDLE_TOP,
      targetLayout,
      xDeck,
      yDeck,
      maxCards,
      layoutComps.getGuideline(R.id.guideMidCardsLeft),
      layoutComps.getGuideline(R.id.guideMidCardsRight),
      layoutComps.getGuideline(R.id.guideMidCardsTop),
      layoutComps.getGuideline(R.id.guideMidCardsBottom),
      layoutComps.getGuideline(R.id.guideMidCardsUi),
      cardSize.x,
      layoutComps.getGuideline(R.id.guideMidChipsLeft),
      layoutComps.getGuideline(R.id.guideMidChipsRight),
      chipSize,
      layoutComps.midScoreTexts.txtMidMidScore,
      layoutComps.midResultsImages.resultsMidMid,
      layoutComps.getGuideline(R.id.guideMidChipsTop),
      layoutComps.midScoreBetValueTexts.txtMidMidBetValue,
      layoutComps.midScoreAmountWonTexts.txtMidMidAmountWon,
      layoutComps.midTurnPlayerIndicators.turnPlayerMidMid);

    initPlayerData(PlayerIds.LEFT_TOP,
      targetLayout,
      xDeck,
      yDeck,
      maxCards,
      layoutComps.getGuideline(R.id.guideLeftCardsLeft),
      layoutComps.getGuideline(R.id.guideLeftCardsRight),
      layoutComps.getGuideline(R.id.guideMidCardsTop),
      layoutComps.getGuideline(R.id.guideMidCardsBottom),
      layoutComps.getGuideline(R.id.guideMidCardsUi),
      cardSize.x,
      layoutComps.getGuideline(R.id.guideLeftChipsLeft),
      layoutComps.getGuideline(R.id.guideLeftChipsRight),
      chipSize,
      layoutComps.midScoreTexts.txtMidLeftScore,
      layoutComps.midResultsImages.resultsMidLeft,
      layoutComps.getGuideline(R.id.guideMidChipsTop),
      layoutComps.midScoreBetValueTexts.txtMidLeftBetValue,
      layoutComps.midScoreAmountWonTexts.txtMidLeftAmountWon,
      layoutComps.midTurnPlayerIndicators.turnPlayerMidLeft);

    setCardMoveStartListeners(cardMoveStartListener);
  }

  //-------------------------------------------------------------------------
  // getDealer
  //-------------------------------------------------------------------------
  BasePlayerData getDealer() {
    return(m_dealer);
  }

  //-------------------------------------------------------------------------
  // getPlayers
  //-------------------------------------------------------------------------
  HashMap<PlayerIds, PlayerData> getPlayers() {
    return(m_players);
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // initPlayerData
  //-------------------------------------------------------------------------
  private void initPlayerData(PlayerIds playerId, ViewGroup viewGroup, float xDeck,
  float yDeck, int maxCards, Guideline guideCardsLeft, Guideline guideCardsRight,
  Guideline guideCardsTop, Guideline guideCardsBottom, Guideline guideCardsUi, int cardImageWidth,
  Guideline guideChipsLeft, Guideline guideChipsRight, Point chipSize, TextView scoreText,
  ImageView resultImage, Guideline guideChipsTop, TextView betValueText,
  TextView amountWonValueText, ImageView turnIndicatorImage) {
    HashMap<String, Object> extraParams = new HashMap<>();
    extraParams.put("guideChipsLeft", guideChipsLeft);
    extraParams.put("guideChipsRight", guideChipsRight);
    extraParams.put("chipSize", chipSize);
    extraParams.put("guideChipsTop", guideChipsTop);
    extraParams.put("betValueText", betValueText);
    extraParams.put("turnIndicatorImage", turnIndicatorImage);
    extraParams.put("amountWonValueText", amountWonValueText);

    m_players.put(playerId, new PlayerData(viewGroup, playerId, xDeck, yDeck, maxCards,
      guideCardsLeft, guideCardsRight, guideCardsTop, guideCardsBottom, guideCardsUi,
      cardImageWidth, scoreText, resultImage, extraParams));
  }

  //-------------------------------------------------------------------------
  // setCardMoveStartListeners
  //-------------------------------------------------------------------------
  private void setCardMoveStartListeners(BaseHandData.OnCardMoveStartListener cardMoveStartListener) {
    m_dealer.setCardMoveStartListener(cardMoveStartListener);

    Iterator iterator = m_players.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry) iterator.next();
      PlayerData playerData = (PlayerData) entry.getValue();
      playerData.setCardMoveStartListener(cardMoveStartListener);
    }
  }
}