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
  PlayersCreator(ViewGroup templateLayout, ActivityMainBinding binding, float xDeck,
  float yDeck, int maxCards, Views views, BaseHandData.OnCardMoveStartListener cardMoveStartListener) {
    ViewGroup targetLayout = binding.activityMain;
    Point cardSize = views.getCardSize();
    Point chipSize = views.getChipSize();

    m_dealer = new BasePlayerData(
      targetLayout,
      PlayerIds.DEALER,
      xDeck,
      yDeck,
      maxCards,
      (Guideline)templateLayout.findViewById(R.id.guideMidCardsLeft),
      (Guideline)templateLayout.findViewById(R.id.guideMidCardsRight),
      (Guideline)templateLayout.findViewById(R.id.guideUpperCardsTop),
      (Guideline)templateLayout.findViewById(R.id.guideUpperCardsBottom),
      (Guideline)templateLayout.findViewById(R.id.guideUpperCardsUi),
      cardSize.x,
      views.getUpperMidScoreTextView(),
      views.getResultsUpperMid(),
      null);

    m_players = new HashMap<>();

    initPlayerData(PlayerIds.RIGHT_BOTTOM,
      targetLayout,
      xDeck,
      yDeck,
      maxCards,
      (Guideline)templateLayout.findViewById(R.id.guideRightCardsLeft),
      (Guideline)templateLayout.findViewById(R.id.guideRightCardsRight),
      (Guideline)templateLayout.findViewById(R.id.guideLowerCardsTop),
      (Guideline)templateLayout.findViewById(R.id.guideBottomPanel),
      (Guideline)templateLayout.findViewById(R.id.guideLowerCardsUi),
      cardSize.x,
      (Guideline)templateLayout.findViewById(R.id.guideRightChipsLeft),
      (Guideline)templateLayout.findViewById(R.id.guideRightChipsRight),
      chipSize,
      views.getLowerRightScoreTextView(),
      views.getResultsLowerRight(),
      (Guideline)templateLayout.findViewById(R.id.guideLowerChipsTop),
      views.getLowerRightBetValueTextView(),
      views.getLowerRightAmountWonTextView(),
      views.getTurnPlayerLowerRight());

    initPlayerData(PlayerIds.MIDDLE_BOTTOM,
      targetLayout,
      xDeck,
      yDeck,
      maxCards,
      (Guideline)templateLayout.findViewById(R.id.guideMidCardsLeft),
      (Guideline)templateLayout.findViewById(R.id.guideMidCardsRight),
      (Guideline)templateLayout.findViewById(R.id.guideLowerCardsTop),
      (Guideline)templateLayout.findViewById(R.id.guideBottomPanel),
      (Guideline)templateLayout.findViewById(R.id.guideLowerCardsUi),
      cardSize.x,
      (Guideline)templateLayout.findViewById(R.id.guideMidChipsLeft),
      (Guideline)templateLayout.findViewById(R.id.guideMidChipsRight),
      chipSize,
      views.getLowerMidScoreTextView(),
      views.getResultsLowerMid(),
      (Guideline)templateLayout.findViewById(R.id.guideLowerChipsTop),
      views.getLowerMidBetValueTextView(),
      views.getLowerMidAmountWonTextView(),
      views.getTurnPlayerLowerMid());

    initPlayerData(PlayerIds.LEFT_BOTTOM,
      targetLayout,
      xDeck,
      yDeck,
      maxCards,
      (Guideline)templateLayout.findViewById(R.id.guideLeftCardsLeft),
      (Guideline)templateLayout.findViewById(R.id.guideLeftCardsRight),
      (Guideline)templateLayout.findViewById(R.id.guideLowerCardsTop),
      (Guideline)templateLayout.findViewById(R.id.guideBottomPanel),
      (Guideline)templateLayout.findViewById(R.id.guideLowerCardsUi),
      cardSize.x,
      (Guideline)templateLayout.findViewById(R.id.guideLeftChipsLeft),
      (Guideline)templateLayout.findViewById(R.id.guideLeftChipsRight),
      chipSize,
      views.getLowerLeftScoreTextView(),
      views.getResultsLowerLeft(),
      (Guideline)templateLayout.findViewById(R.id.guideLowerChipsTop),
      views.getLowerLeftBetValueTextView(),
      views.getLowerLeftAmountWonTextView(),
      views.getTurnPlayerLowerLeft());

    initPlayerData(PlayerIds.RIGHT_TOP,
      targetLayout,
      xDeck,
      yDeck,
      maxCards,
      (Guideline)templateLayout.findViewById(R.id.guideRightCardsLeft),
      (Guideline)templateLayout.findViewById(R.id.guideRightCardsRight),
      (Guideline)templateLayout.findViewById(R.id.guideMidCardsTop),
      (Guideline)templateLayout.findViewById(R.id.guideMidCardsBottom),
      (Guideline)templateLayout.findViewById(R.id.guideMidCardsUi),
      cardSize.x,
      (Guideline)templateLayout.findViewById(R.id.guideRightChipsLeft),
      (Guideline)templateLayout.findViewById(R.id.guideRightChipsRight),
      chipSize,
      views.getMidRightScoreTextView(),
      views.getResultsMidRight(),
      (Guideline)templateLayout.findViewById(R.id.guideMidChipsTop),
      views.getMidRightBetValueTextView(),
      views.getMidRightAmountWonTextView(),
      views.getTurnPlayerMidRight());

    initPlayerData(PlayerIds.MIDDLE_TOP,
      targetLayout,
      xDeck,
      yDeck,
      maxCards,
      (Guideline)templateLayout.findViewById(R.id.guideMidCardsLeft),
      (Guideline)templateLayout.findViewById(R.id.guideMidCardsRight),
      (Guideline)templateLayout.findViewById(R.id.guideMidCardsTop),
      (Guideline)templateLayout.findViewById(R.id.guideMidCardsBottom),
      (Guideline)templateLayout.findViewById(R.id.guideMidCardsUi),
      cardSize.x,
      (Guideline)templateLayout.findViewById(R.id.guideMidChipsLeft),
      (Guideline)templateLayout.findViewById(R.id.guideMidChipsRight),
      chipSize,
      views.getMidMidScoreTextView(),
      views.getResultsMidMid(),
      (Guideline)templateLayout.findViewById(R.id.guideMidChipsTop),
      views.getMidMidBetValueTextView(),
      views.getMidMidAmountWonTextView(),
      views.getTurnPlayerMidMid());

    initPlayerData(PlayerIds.LEFT_TOP,
      targetLayout,
      xDeck,
      yDeck,
      maxCards,
      (Guideline)templateLayout.findViewById(R.id.guideLeftCardsLeft),
      (Guideline)templateLayout.findViewById(R.id.guideLeftCardsRight),
      (Guideline)templateLayout.findViewById(R.id.guideMidCardsTop),
      (Guideline)templateLayout.findViewById(R.id.guideMidCardsBottom),
      (Guideline)templateLayout.findViewById(R.id.guideMidCardsUi),
      cardSize.x,
      (Guideline)templateLayout.findViewById(R.id.guideLeftChipsLeft),
      (Guideline)templateLayout.findViewById(R.id.guideLeftChipsRight),
      chipSize,
      views.getMidLeftScoreTextView(),
      views.getResultsMidLeft(),
      (Guideline)templateLayout.findViewById(R.id.guideMidChipsTop),
      views.getMidLeftBetValueTextView(),
      views.getMidLeftAmountWonTextView(),
      views.getTurnPlayerMidLeft());

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