package com.example.cartrell.blackjack.engine;

import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.cartrell.blackjack.R;
import com.example.cartrell.blackjack.cards.CalculateScore;
import com.example.cartrell.blackjack.cards.Card;
import com.example.cartrell.blackjack.cards.CardValues;
import com.example.cartrell.blackjack.cards.CardsMover;
import com.example.cartrell.blackjack.cards.Deck;
import com.example.cartrell.blackjack.cards.ICardsMoverCallbacks;
import com.example.cartrell.blackjack.players.BasePlayerData;
import com.example.cartrell.blackjack.players.PlayerData;
import com.example.cartrell.blackjack.players.PlayerIds;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class BjPlaySystem implements ICardsMoverCallbacks {
  //=========================================================================
  // members
  //=========================================================================
  private final String LOG_TAG = "BjPlaySystem";

  private final String[] DEBUG_CARD_KEYS = {
  };

  private ArrayList<PlayerIds> m_playerIdsOrder;
  private IBjEngine m_engine;
  private BjPlayStates m_state;
  private CardsMover m_cardsMover;
  private PlayerIds m_turnPlayerId;
  private int m_baseCardImageChildIndex;
  private int m_nextCardImageChildIndex;
  private int m_totalCreditsWonOnRound;
  private boolean m_wereAcesSplit;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // cardsMoverOnComplete
  //-------------------------------------------------------------------------
  @Override
  public void cardsMoverOnComplete(CardsMover cardsMover) {
    updatePlayersPoints();

    switch (m_state) {
      case DEAL_CARDS:
        beginRoundStart();
        break;

      case HIT:
      case DOUBLE:
      case SPLIT:
        resolveCardsAfterHit();
        break;
    }
  }

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  BjPlaySystem(IBjEngine engine) {
    m_engine = engine;
    m_playerIdsOrder  = new ArrayList<>();
    m_cardsMover = new CardsMover(this);
    m_baseCardImageChildIndex = m_engine.getIndexOf(m_engine.getViews().getDeckImage());
    initGameButtons();
  }

  //-------------------------------------------------------------------------
  // begin
  //-------------------------------------------------------------------------
  void begin() {
    m_state = BjPlayStates.DEAL_CARDS;
    m_nextCardImageChildIndex = m_baseCardImageChildIndex + 1;
    m_totalCreditsWonOnRound = 0;

    TextView txtTotalWon = m_engine.getViews().getTextTotalWon();
    if (txtTotalWon != null) {
      m_engine.showView(txtTotalWon, false);
    }

    initPlayers();
    dealCards();
  }

  //-------------------------------------------------------------------------
  // beginDouble
  //-------------------------------------------------------------------------
  void beginDouble() {
    PlayerData playerData = (PlayerData)getTurnPlayerData();
    int playerBetValue = playerData.getBetValue();
    m_engine.setCredits(-playerBetValue, true);
    m_engine.setPlayerBet(playerData.getId(), playerBetValue * 2, false);

    m_state = BjPlayStates.DOUBLE;
    drawCard(m_turnPlayerId, true, 0, true);
  }

  //-------------------------------------------------------------------------
  // beginHit
  //-------------------------------------------------------------------------
  void beginHit() {
    m_state = BjPlayStates.HIT;
    drawCard(m_turnPlayerId, true, 0, true);
  }

  //-------------------------------------------------------------------------
  // beginSplit
  //-------------------------------------------------------------------------
  void beginSplit() {
    PlayerData turnPlayerData = (PlayerData)getTurnPlayerData();
    PlayerData splitPlayerData = getSplitPlayerData(m_turnPlayerId);
    if (splitPlayerData == null) {
      return; //sanity check
    }

    m_state = BjPlayStates.SPLIT;
    turnPlayerData.setSplit();
    splitPlayerData.setSplit();
    m_engine.setCredits(-turnPlayerData.getBetValue(), true);

    m_wereAcesSplit = wereAcesSplit();
    moveTopCardFromTo(turnPlayerData, splitPlayerData);
  }

  //-------------------------------------------------------------------------
  // beginStand
  //-------------------------------------------------------------------------
  void beginStand() {
    beginNextTurnPlayer();
  }

  //-------------------------------------------------------------------------
  // beginSurrender
  //-------------------------------------------------------------------------
  void beginSurrender() {
    PlayerData playerData = (PlayerData)getTurnPlayerData();
    playerData.setSurrendered();
    playerData.setResultImage(R.drawable.result_label_surrender);
    playerData.setResultImageVisible(true);

    int creditsWon = calcCreditsWon(m_turnPlayerId, m_engine.getStringResource(R.string.winRatioSurrender));
    playerData.setBetValue(creditsWon);
    playerData.setBetValueVisible(true);
    m_totalCreditsWonOnRound += creditsWon;

    beginNextTurnPlayer();
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // aceUpSleeve
  //-------------------------------------------------------------------------
  private void aceUpSleeve() {
    Deck deck = m_engine.getDeck();
    int targetIndex = 0;
    for (String cardKey : DEBUG_CARD_KEYS) {
      deck.setCardAt(cardKey, targetIndex++);
    }
  }

  //-------------------------------------------------------------------------
  // arePlayersLeftVsDealer
  //-------------------------------------------------------------------------
  private boolean arePlayersLeftVsDealer() {
    for (PlayerIds playerId : PlayerIds.values()) {
      if (isDealerId(playerId)) {
        continue;
      }

      PlayerData playerData = (PlayerData)getPlayerData(playerId);
      if (playerData.isVsDealer()) {
        return(true);
      }
    }

    return(false);
  }

  //-------------------------------------------------------------------------
  // beginDealerBlackjack
  //-------------------------------------------------------------------------
  private void beginDealerBlackjack() {
    BasePlayerData dealerData = m_engine.getDealerData();
    dealerData.setResultImage(R.drawable.result_label_blackjack);
    dealerData.setResultImageVisible(true);

    for (PlayerIds playerId : m_playerIdsOrder) {
      BasePlayerData playerData = getPlayerData(playerId);
      if (playerData.getHasBlackjack()) {
        beginPlayerPush(playerId);
      } else {
        beginPlayerLost(playerId);
      }
    }

    endRound();
  }

  //-------------------------------------------------------------------------
  // beginDealerBust
  //-------------------------------------------------------------------------
  private void beginDealerBust() {
    BasePlayerData dealerData = getTurnPlayerData();
    dealerData.setBust();
    dealerData.setResultImage(R.drawable.result_label_bust);
    dealerData.setResultImageVisible(true);

    //all players eligible vs dealer automatically win
    for (PlayerIds playerId : PlayerIds.values()) {
      if (isDealerId(playerId)) {
        continue; //dealer cant compete with self! c'mon now! :P
      }

      PlayerData playerData = (PlayerData)getPlayerData(playerId);
      if (playerData.isVsDealer()) {
        beginPlayerWon(playerId);
      }
    }

    endRound();
  }

  //-------------------------------------------------------------------------
  // beginDealerTurn
  //-------------------------------------------------------------------------
  private void beginDealerTurn() {
    boolean shouldRevealSecondCard = !PlayerIds.DEALER.equals(m_turnPlayerId);
    m_turnPlayerId = PlayerIds.DEALER;

    if (shouldRevealSecondCard) {
      revealDealerSecondCard();
    }

    if (!arePlayersLeftVsDealer()) {
      beginStand();
      return;
    }

    if (doesTurnPlayerHaveMaxCards()) {
      if (shouldDealerStand()) {
        beginStand();
      } else {
        beginDealerBust();
      }
    } else if (shouldDealerStand()) {
      beginStand();
    } else {
      beginHit();
    }
  }

  //-------------------------------------------------------------------------
  // beginNextTurnPlayer
  //-------------------------------------------------------------------------
  private void beginNextTurnPlayer() {
    hideTurnPlayerIndicator();

    if (m_playerIdsOrder.size() > 0) {
      beginUserPlayerTurn();
    } else {
      //if the turn player is the dealer, then end the round
      //otherwise, make the dealer the turn player
      if (isDealersTurn()) {
        endRound();
      } else {
        //not currently the
        beginDealerTurn();
      }
    }
  }

  //-------------------------------------------------------------------------
  // beginPlayerBlackjack
  //-------------------------------------------------------------------------
  private void beginPlayerBlackjack(PlayerIds playerId) {
    PlayerData playerData = (PlayerData)getPlayerData(playerId);
    playerData.setBlackjack();
    playerData.setResultImage(R.drawable.result_label_blackjack);
    playerData.setResultImageVisible(true);

    int creditsWon = calcCreditsWon(playerId, m_engine.getStringResource(R.string.winRatioBlackjack));
    playerData.setBetValue(creditsWon);
    playerData.setBetValueVisible(true);
    m_totalCreditsWonOnRound += creditsWon;
  }

  //-------------------------------------------------------------------------
  // beginPlayerCharlieWin
  //-------------------------------------------------------------------------
  private void beginPlayerCharlieWin() {
    PlayerData playerData = (PlayerData)getTurnPlayerData();
    playerData.setCharlieWin();
    playerData.setResultImage(R.drawable.result_label_charlie);
    playerData.setResultImageVisible(true);

    int creditsWon = calcCreditsWon(m_turnPlayerId, m_engine.getStringResource(R.string.winRatioCharlie));
    playerData.setBetValue(creditsWon);
    playerData.setBetValueVisible(true);
    m_totalCreditsWonOnRound += creditsWon;

    beginNextTurnPlayer();
  }

  //-------------------------------------------------------------------------
  // beginPlayerLost
  //-------------------------------------------------------------------------
  private void beginPlayerLost(PlayerIds playerId) {
    if (isDealerId(playerId)) {
      return; //this function for non-dealer players only
    }

    PlayerData playerData = (PlayerData)getPlayerData(playerId);
    playerData.hideBetChips();
    playerData.setBetValueVisible(false);
  }

  //-------------------------------------------------------------------------
  // beginPlayerPush
  //-------------------------------------------------------------------------
  private void beginPlayerPush(PlayerIds playerId) {
    PlayerData playerData = (PlayerData)getPlayerData(playerId);
    playerData.setResultImage(R.drawable.result_label_push);
    playerData.setResultImageVisible(true);
    int creditsWon = calcCreditsWon(playerId, m_engine.getStringResource(R.string.winRatioPush));
    playerData.setBetValue(creditsWon);
    playerData.setBetValueVisible(true);
    m_totalCreditsWonOnRound += creditsWon;
  }

  //-------------------------------------------------------------------------
  // beginPlayerWon
  //-------------------------------------------------------------------------
  private void beginPlayerWon(PlayerIds playerId) {
    PlayerData playerData = (PlayerData)getPlayerData(playerId);
    playerData.setResultImage(R.drawable.result_label_win);
    playerData.setResultImageVisible(true);
    int creditsWon = calcCreditsWon(playerId, m_engine.getStringResource(R.string.winRatioNormal));
    playerData.setBetValue(creditsWon);
    playerData.setBetValueVisible(true);
    m_totalCreditsWonOnRound += creditsWon;
  }

  //-------------------------------------------------------------------------
  // beginRoundStart
  //-------------------------------------------------------------------------
  private void beginRoundStart() {
    updatePlayersPoints();

    if (doesDealerHaveBlackjack()) {
      beginDealerBlackjack();
    } else {
      checkPlayersForBlackjack();
      beginNextTurnPlayer();
    }
  }

  //-------------------------------------------------------------------------
  // beginTurnPlayerBust
  //-------------------------------------------------------------------------
  private void beginTurnPlayerBust() {
    BasePlayerData playerData = getTurnPlayerData();
    playerData.setBust();
    playerData.setResultImage(R.drawable.result_label_bust);
    playerData.setResultImageVisible(true);
    beginPlayerLost(m_turnPlayerId);
    beginNextTurnPlayer();
  }

  //-------------------------------------------------------------------------
  // beginUserPlayerTurn
  //-------------------------------------------------------------------------
  private void beginUserPlayerTurn() {
    m_turnPlayerId = m_playerIdsOrder.get(0);
    m_playerIdsOrder.remove(0);
    PlayerData playerData = (PlayerData)getTurnPlayerData();
    playerData.setTurnIndicatorVisible(true);
    showGameButtons();
    m_state = BjPlayStates.PLAYER_MOVE;
  }

  //-------------------------------------------------------------------------
  // checkPlayersForBlackjack
  //-------------------------------------------------------------------------
  private void checkPlayersForBlackjack() {
    Iterator<PlayerIds>iterator = m_playerIdsOrder.iterator();
    while (iterator.hasNext()) {
      PlayerIds playerId = iterator.next();
      if (doesPlayerHaveBlackjack(playerId)) {
        beginPlayerBlackjack(playerId);
        iterator.remove();
      }
    }
  }

  //-------------------------------------------------------------------------
  // calcCreditsWon
  //-------------------------------------------------------------------------
  private int calcCreditsWon(PlayerIds playerId, String winRatioFormat) {
    //win ratio formats are in x:y, for example, if the format is 3:2, credits won would
    // be 3/2, or 1.5 times original bet value

    PlayerData playerData = (PlayerData)getPlayerData(playerId);
    if (winRatioFormat == null) {
      Log.w(LOG_TAG, "calcCreditsWon. Win ratio format is null");
      return(playerData.getBetValue());

    }

    int delimiterIndex = winRatioFormat.indexOf(":");
    if (delimiterIndex == -1) {
      Log.w(LOG_TAG, "calcCreditsWon. Invalid win ratio format: " + winRatioFormat);
      return(playerData.getBetValue());
    }

    Float divisor;
    Float dividend;
    try {
      divisor = Float.valueOf(winRatioFormat.substring(0, delimiterIndex));
      dividend = Float.valueOf(winRatioFormat.substring(delimiterIndex + 1));
    } catch (Exception error) {
      Log.w(LOG_TAG, "calcCreditsWon. Invalid win ratio format: " + winRatioFormat);
      return(playerData.getBetValue());
    }

    float winRatio = divisor / dividend;
    float betValue = (float)playerData.getBetValue();
    float amountWonF = betValue + betValue * winRatio;
    int amountWon = (int)amountWonF;
    if (playerData.getIsDoubleDown()) {
      amountWon *= 2;
    }
    return(amountWon);
  }

  //-------------------------------------------------------------------------
  // compareHands
  //-------------------------------------------------------------------------
  private void compareHands() {
    //compare every eligible player vs dealer
    BasePlayerData dealerData = m_engine.getDealerData();
    if (dealerData.getIsBust()) {
      //dealer busted - no cards to compare
      return;
    }

    int dealerScore = dealerData.getScore();

    for (PlayerIds playerId : PlayerIds.values()) {
      if (isDealerId(playerId)) {
        continue; //cant compare dealer with self!
      }

      PlayerData playerData = (PlayerData)getPlayerData(playerId);
      if (!playerData.isVsDealer()) {
        continue; //player is not eligible vs dealer
      }

      int playerScore = playerData.getScore();
      if (playerScore > dealerScore) {
        beginPlayerWon(playerId);
      } else if (playerScore < dealerScore) {
        beginPlayerLost(playerId);
      } else {
        beginPlayerPush(playerId);
      }
    }
  }

  //-------------------------------------------------------------------------
  // dealCards
  //-------------------------------------------------------------------------
  private void dealCards() {
    aceUpSleeve();

    long moveStartDelay = 0;
    final long MOVE_DURATION = m_engine.getIntegerResource(R.integer.cardMoveDuration);

    for (int cardsRound = 0; cardsRound < 2; cardsRound++) {
      for (PlayerIds playerId : m_playerIdsOrder) {
        drawCard(playerId, true, moveStartDelay, false);
        moveStartDelay += MOVE_DURATION;
      }

      //only the dealer's first card is face up
      drawCard(PlayerIds.DEALER, cardsRound == 0, moveStartDelay, true);
      moveStartDelay += MOVE_DURATION;
    }
  }

  //-------------------------------------------------------------------------
  // didTurnPlayerBust
  //-------------------------------------------------------------------------
  private boolean didTurnPlayerBust() {
    final int BLACK_JACK = m_engine.getIntegerResource(R.integer.blackjackPoints);
    return(getTurnPlayerData().getScore() > BLACK_JACK);
  }

  //-------------------------------------------------------------------------
  // doesDealerHaveBlackjack
  //-------------------------------------------------------------------------
  private boolean doesDealerHaveBlackjack() {
    return(doesPlayerHaveBlackjack(PlayerIds.DEALER));
  }

  //-------------------------------------------------------------------------
  // doesPlayerHaveBlackjack
  //-------------------------------------------------------------------------
  private boolean doesPlayerHaveBlackjack(PlayerIds playerId) {
    BasePlayerData playerData = getPlayerData(playerId);
    return(
      playerData.getNumCards() == 2 &&
      playerData.getScore() == m_engine.getIntegerResource(R.integer.blackjackPoints));
  }

  //-------------------------------------------------------------------------
  // doesTurnPlayerHaveMaxCards
  //-------------------------------------------------------------------------
  private boolean doesTurnPlayerHaveMaxCards() {
    return(getTurnPlayerData().getNumCards() ==
      m_engine.getIntegerResource(R.integer.maxCardsPerHand));
  }

  //-------------------------------------------------------------------------
  // drawCard
  //-------------------------------------------------------------------------
  private void drawCard(PlayerIds playerId, boolean isFaceUp, long moveStartDelay, boolean startAnimation) {
    Card card = m_engine.getDeck().next();
    if (card == null) {
      //sanity check
      Log.w(LOG_TAG, "drawCard. Warning: No cards left in deck...");
      endRound();
      return;
    }

    m_engine.showGameButtons(BjGameButtonFlags.NONE);

    BasePlayerData basePlayerData = getPlayerData(playerId);
    final long MOVE_DURATION = m_engine.getIntegerResource(R.integer.cardMoveDuration);
    card.setFaceUp(isFaceUp);
    basePlayerData.addCard(card, m_cardsMover, moveStartDelay, MOVE_DURATION, startAnimation,
      m_nextCardImageChildIndex++);
  }

  //-------------------------------------------------------------------------
  // endRound
  //-------------------------------------------------------------------------
  private void endRound() {
    m_engine.showGameButtons(BjGameButtonFlags.NONE);
    compareHands();
    presentWonCredits();
    m_engine.beginRound();
  }

  //-------------------------------------------------------------------------
  // getPlayerData
  //-------------------------------------------------------------------------
  private BasePlayerData getPlayerData(PlayerIds playerId) {
    return(isDealerId(playerId) ? m_engine.getDealerData() : m_engine.getPlayer(playerId));
  }

  //-------------------------------------------------------------------------
  // getSplitPlayerData
  //-------------------------------------------------------------------------
  private PlayerData getSplitPlayerData(PlayerIds playerId) {
    PlayerData splitPlayerData;

    switch (playerId) {
      case LEFT_BOTTOM:
        splitPlayerData = (PlayerData)getPlayerData(PlayerIds.LEFT_TOP);
        break;

      case MIDDLE_BOTTOM:
        splitPlayerData = (PlayerData)getPlayerData(PlayerIds.MIDDLE_TOP);
        break;

      case RIGHT_BOTTOM:
        splitPlayerData = (PlayerData)getPlayerData(PlayerIds.RIGHT_TOP);
        break;

      default:
        splitPlayerData = null;
        break;
    }

    return(splitPlayerData);
  }

  //-------------------------------------------------------------------------
  // getTurnPlayerData
  //-------------------------------------------------------------------------
  private BasePlayerData getTurnPlayerData() {
    return(getPlayerData(m_turnPlayerId));
  }

  //-------------------------------------------------------------------------
  // hideTurnPlayerIndicator
  //-------------------------------------------------------------------------
  private void hideTurnPlayerIndicator() {
    if (m_turnPlayerId == null) {
      return;
    }

    if (!isDealersTurn()) {
      PlayerData playerData = (PlayerData)getTurnPlayerData();
      playerData.setTurnIndicatorVisible(false);
    }
  }

  //-------------------------------------------------------------------------
  // initGameButtonDouble
  //-------------------------------------------------------------------------
  private void initGameButtonDouble() {
    m_engine.getViews().getDoubleButton().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        beginDouble();
      }
    });
  }

  //-------------------------------------------------------------------------
  // initGameButtonHit
  //-------------------------------------------------------------------------
  private void initGameButtonHit() {
    m_engine.getViews().getHitButton().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        beginHit();
      }
    });
  }

  //-------------------------------------------------------------------------
  // initGameButtons
  //-------------------------------------------------------------------------
  private void initGameButtons() {
    initGameButtonHit();
    initGameButtonStand();
    initGameButtonDouble();
    initGameButtonSplit();
    initGameButtonSurrender();
  }

  //-------------------------------------------------------------------------
  // initGameButtonSplit
  //-------------------------------------------------------------------------
  private void initGameButtonSplit() {
    m_engine.getViews().getSplitButton().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        beginSplit();
      }
    });
  }

  //-------------------------------------------------------------------------
  // initGameButtonStand
  //-------------------------------------------------------------------------
  private void initGameButtonStand() {
    m_engine.getViews().getStandButton().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        beginStand();
      }
    });
  }

  //-------------------------------------------------------------------------
  // initGameButtonSurrender
  //-------------------------------------------------------------------------
  private void initGameButtonSurrender() {
    m_engine.getViews().getSurrenderButton().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        beginSurrender();
      }
    });
  }

  //-------------------------------------------------------------------------
  // initPlayers
  //-------------------------------------------------------------------------
  private void initPlayers() {
    resetPlayers();
    initPlayersOrder();
  }

  //-------------------------------------------------------------------------
  // initPlayersOrder
  //-------------------------------------------------------------------------
  private void initPlayersOrder() {
    m_playerIdsOrder.clear();

    final PlayerIds[] PLAYER_IDS = new PlayerIds[] {
      PlayerIds.RIGHT_BOTTOM,
      PlayerIds.MIDDLE_BOTTOM,
      PlayerIds.LEFT_BOTTOM
    };

    for (PlayerIds playerId : PLAYER_IDS) {
      PlayerData playerData = m_engine.getPlayer(playerId);
      if (playerData.getBetValue() > 0f) {
        m_playerIdsOrder.add(playerId);
      }
    }
  }

  //-------------------------------------------------------------------------
  // isDealerId
  //-------------------------------------------------------------------------
  private boolean isDealerId(PlayerIds playerId) {
    return(PlayerIds.DEALER.equals(playerId));
  }

  //-------------------------------------------------------------------------
  // isDealersTurn
  //-------------------------------------------------------------------------
  private boolean isDealersTurn() {
    return(isDealerId(m_turnPlayerId));
  }

  //-------------------------------------------------------------------------
  // isDoubleAvailable
  //-------------------------------------------------------------------------
  private boolean isDoubleAvailable() {
    PlayerData playerData = (PlayerData)getTurnPlayerData();
    return(
      m_engine.getCredits() >= playerData.getBetValue() &&
      playerData.getNumCards() == 2);
  }

  //-------------------------------------------------------------------------
  // isSplitAvailable
  //-------------------------------------------------------------------------
  private boolean isSplitAvailable() {
    PlayerData playerData = (PlayerData)getTurnPlayerData();
    if (playerData.getNumCards() != 2) {
      return(false);
    }

    if (playerData.getHasSplit()) {
      return(false);
    }

    Deck deck = m_engine.getDeck();

    Card card0 = deck.getCard(playerData.getCardKetAt(0));
    CardValues card0Value = card0.getValue();

    Card card1 = deck.getCard(playerData.getCardKetAt(1));
    CardValues card1Value = card1.getValue();

    return(card0Value.getValue() == card1Value.getValue());
  }

  //-------------------------------------------------------------------------
  // isSurrenderAvailable
  //-------------------------------------------------------------------------
  private boolean isSurrenderAvailable() {
    PlayerData playerData = (PlayerData)getTurnPlayerData();
    return(playerData.getNumCards() == 2);
  }

  //-------------------------------------------------------------------------
  // isSplitPlayerId
  //-------------------------------------------------------------------------
  private boolean isSplitPlayerId(PlayerIds playerId) {
    return(
      PlayerIds.RIGHT_TOP.equals(playerId) ||
      PlayerIds.MIDDLE_TOP.equals(playerId) ||
      PlayerIds.LEFT_TOP.equals(playerId));
  }

  //-------------------------------------------------------------------------
  // moveTopCardFromTo
  //-------------------------------------------------------------------------
  private void moveTopCardFromTo(PlayerData sourcePlayerData, PlayerData targetPlayerData) {
    m_engine.showGameButtons(BjGameButtonFlags.NONE);

    final long MOVE_DURATION = m_engine.getIntegerResource(R.integer.cardMoveDuration);
    Card card = sourcePlayerData.popTopCard(m_cardsMover, 0, MOVE_DURATION, false);
    targetPlayerData.addCard(card, m_cardsMover, 0, MOVE_DURATION, false, -1);
    drawCard(sourcePlayerData.getId(), true, MOVE_DURATION, false);
    drawCard(targetPlayerData.getId(), true, MOVE_DURATION * 2, true);
  }

  //-------------------------------------------------------------------------
  // presentWonCredits
  //-------------------------------------------------------------------------
  private void presentWonCredits() {
    if (m_totalCreditsWonOnRound == 0) {
      return; //no credits won this round
    }

    m_engine.setCredits(m_totalCreditsWonOnRound, true);

    AppCompatTextView txtTotalWon = m_engine.getViews().getTextTotalWon();
    txtTotalWon.setText("TOTAL WON: " + String.valueOf(m_totalCreditsWonOnRound));
    m_engine.showView(txtTotalWon, true);

    m_totalCreditsWonOnRound = 0;
  }

  //-------------------------------------------------------------------------
  // resetPlayers
  //-------------------------------------------------------------------------
  private void resetPlayers() {
    HashMap<PlayerIds, PlayerData> players = m_engine.getPlayers();
    Iterator iterator = players.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry)iterator.next();
      PlayerData playerData = (PlayerData)entry.getValue();
      playerData.resetStatus();

      if (isSplitPlayerId(playerData.getId())) {
        playerData.removeBetChips();
        playerData.setBetValueVisible(false);
      }
    }
  }

  //-------------------------------------------------------------------------
  // resolveCardsAfterHit
  //-------------------------------------------------------------------------
  private void resolveCardsAfterHit() {
    if (isDealersTurn()) {
      resolveDealerCardsAfterHit();
    } else {
      resolvePlayerCardsAfterHit();
    }
  }

  //-------------------------------------------------------------------------
  // resolveDealerCardsAfterHit
  //-------------------------------------------------------------------------
  private void resolveDealerCardsAfterHit() {
    if (didTurnPlayerBust()) {
      beginDealerBust();
      return;
    }

    beginDealerTurn();
  }

  //-------------------------------------------------------------------------
  // resolvePlayerCardsAfterHit
  //-------------------------------------------------------------------------
  private void resolvePlayerCardsAfterHit() {
    if (didTurnPlayerBust()) {
      beginTurnPlayerBust();
      return;
    }

    if (doesTurnPlayerHaveMaxCards()) {
      beginPlayerCharlieWin();
      return;
    }

    if (BjPlayStates.DOUBLE.equals(m_state)) {
      beginNextTurnPlayer();
      return;
    }

    if (BjPlayStates.SPLIT.equals(m_state)) {
      resolvePlayersAfterSplit();
    }

    resumePlayerTurn();
  }

  //-------------------------------------------------------------------------
  // resolvePlayersAfterSplit
  //-------------------------------------------------------------------------
  private void resolvePlayersAfterSplit() {
    m_engine.updateBetValue();

    updatePlayerPoints(m_turnPlayerId);

    //setup the new split player
    PlayerData splitPlayerData = getSplitPlayerData(m_turnPlayerId);
    if (splitPlayerData == null) {
      //sanity check
      Log.w(LOG_TAG, "resolvePlayersAfterSplit. Unable to obtain split player for ID: " +
        m_turnPlayerId);
      return;
    }

    PlayerData playerData = (PlayerData)getTurnPlayerData();
    PlayerIds splitPlayerId = splitPlayerData.getId();
    m_engine.setPlayerBet(splitPlayerId, playerData.getBetValue(), true);

    m_playerIdsOrder.add(0, splitPlayerId);
    updatePlayerPoints(splitPlayerId);

    if (m_wereAcesSplit) {
      m_wereAcesSplit = false;
      beginStand();
      beginStand();
    }
  }

  //-------------------------------------------------------------------------
  // resumePlayerTurn
  //-------------------------------------------------------------------------
  private void resumePlayerTurn() {
    if (isDealersTurn()) {
      beginDealerTurn();
    } else {
      showGameButtons();
      m_state = BjPlayStates.PLAYER_MOVE;
    }
  }

  //-------------------------------------------------------------------------
  // revealDealerSecondCard
  //-------------------------------------------------------------------------
  private void revealDealerSecondCard() {
    BasePlayerData dealerData = m_engine.getDealerData();
    dealerData.setCardFaceUp(1, true);
    updatePlayerPoints(PlayerIds.DEALER);
  }

  //-------------------------------------------------------------------------
  // shouldDealerStand
  //-------------------------------------------------------------------------
  private boolean shouldDealerStand() {
    final int DEALER_POINTS_THRESHOLD = m_engine.getIntegerResource(R.integer.dealerPointsThreshold);
    return(getTurnPlayerData().getScore() >= DEALER_POINTS_THRESHOLD);
  }

  //-------------------------------------------------------------------------
  // showGameButtons
  //-------------------------------------------------------------------------
  private void showGameButtons() {
    //Hit and stand are always available. Always.
    EnumSet<BjGameButtonFlags> flags = EnumSet.of(BjGameButtonFlags.HIT, BjGameButtonFlags.STAND);

    if (isSurrenderAvailable()) {
      flags.add(BjGameButtonFlags.SURRENDER);
    }

    if (isSplitAvailable()) {
      flags.add(BjGameButtonFlags.SPLIT);
    }

    if (isDoubleAvailable()) {
      flags.add(BjGameButtonFlags.DOUBLE);
    }

    m_engine.showGameButtons(flags);
  }

  //-------------------------------------------------------------------------
  // updatePlayerPoints
  //-------------------------------------------------------------------------
  private void updatePlayerPoints(PlayerIds playerId) {
    BasePlayerData playerData = getPlayerData(playerId);

    if (!isDealerId(playerId)) {
      PlayerData pdata = (PlayerData)playerData;
      if (pdata.getBetValue() == 0) {
        return; //not an active player
      }
    }

    ArrayList<String> cardKeys = playerData.getFaceUpCardKeys(true);
    int score = CalculateScore.Run(m_engine, cardKeys);
    playerData.setScore(score);
    playerData.setScoreTextVisible(true);
  }

  //-------------------------------------------------------------------------
  // updatePlayersPoints
  //-------------------------------------------------------------------------
  private void updatePlayersPoints() {
    for (PlayerIds playerId : PlayerIds.values()) {
      updatePlayerPoints(playerId);
    }

    updatePlayerPoints(PlayerIds.DEALER);
  }

  //-------------------------------------------------------------------------
  // wereAcesSplit
  //-------------------------------------------------------------------------
  private boolean wereAcesSplit() {
    PlayerData playerData = (PlayerData)getTurnPlayerData();
    Card card = m_engine.getDeck().getCard(playerData.getCardKetAt(0));
    if (!CardValues.ACE.equals(card.getValue())) {
      return(false);
    }

    card = m_engine.getDeck().getCard(playerData.getCardKetAt(1));
    return(CardValues.ACE.equals(card.getValue()));
  }
}