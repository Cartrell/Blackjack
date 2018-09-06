package com.example.cartrell.blackjack.engine;

import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.cartrell.blackjack.R;
import com.example.cartrell.blackjack.cards.Card;
import com.example.cartrell.blackjack.cards.CardValues;
import com.example.cartrell.blackjack.cards.Deck;
import com.example.cartrell.blackjack.players.BasePlayerData;
import com.example.cartrell.blackjack.players.PlayerData;
import com.example.cartrell.blackjack.players.PlayerIds;
import com.example.cartrell.blackjack.sound.SoundChannel;
import com.example.cartrell.blackjack.sound.SoundSystem;
import com.example.cartrell.blackjack.utils.CalculateScore;
import com.example.cartrell.blackjack.utils.CardsMatcher;
import com.example.cartrell.blackjack.utils.CardsMover;
import com.example.cartrell.blackjack.utils.ICardsMoverCallbacks;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class BjPlaySystem implements ICardsMoverCallbacks {
  //=========================================================================
  // members
  //=========================================================================
  private final String LOG_TAG = BjPlaySystem.class.getName();

  private final String[] DEBUG_CARD_KEYS = {
    "10_c_0", "10_d_0", "5_c_0", "5_h_0",
    "10_h_0", "4_d_0", "5_s_0"
  };

  private ArrayList<PlayerIds> m_playerIdsOrder;
  private IBjEngine m_engine;
  private BjPlayStates m_state;
  private BjPlaySettingsManager m_playSettingsManager;
  private CardsMover m_cardsMover;
  private CardsMatcher m_cardsMatcher;
  private SoundSystem.OnSoundCompleteListener m_onDealerBustSoundCompleteListener;
  private BjWinSound m_winSound;
  private PlayerIds m_turnPlayerId;
  private int m_baseCardImageChildIndex;
  private int m_nextCardImageChildIndex;
  private int m_totalCreditsWonOnRound;
  private boolean m_wereAcesSplit;
  private SoundChannel m_sndChHit;
  private SoundChannel m_sndChStand;
  private SoundChannel m_sndChSplit1;
  private SoundChannel m_sndChSplit2;
  private SoundChannel m_sndChSurrender1;
  private SoundChannel m_sndChSurrender2;
  private SoundChannel m_sndChBust;
  private SoundChannel m_sndChThunderjack;
  private SoundChannel m_sndChDealerBj;
  private SoundChannel m_sndChAutoWin1;
  private SoundChannel m_sndChAutoWin2;
  private SoundChannel m_sndChAutoWin3;
  private SoundChannel m_sndChAutoWin4;

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
    m_cardsMatcher = new CardsMatcher(m_engine.getIntegerResource(R.integer.blackjackPoints),
      m_engine.getIntegerResource(R.integer.maxCardsPerHand));
    m_baseCardImageChildIndex = m_engine.getIndexOf(m_engine.getViews().getDeckImage());
    m_playSettingsManager = new BjPlaySettingsManager(m_engine);
    m_winSound = new BjWinSound(m_engine);
    initGameButtons();
    initDealerBustSoundCompleteListener();
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
    dealerData.setBlackjack();
    dealerData.setResultImage(R.drawable.result_label_blackjack);
    dealerData.setResultImageVisible(true);

    revealDealerSecondCard();

    for (PlayerIds playerId : m_playerIdsOrder) {
      BasePlayerData playerData = getPlayerData(playerId);
      if (m_cardsMatcher.doesPlayerHaveThunderjack(playerData, m_engine)) {
        beginPlayerThunderjack(playerId);
      } else if (m_cardsMatcher.doesPlayerHaveBlackjack(playerData, m_engine)) {
        beginPlayerBlackjackPush(playerId);
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

    m_engine.getSoundSystem().playMedia(m_onDealerBustSoundCompleteListener,
      R.raw.snd_dealer_bust1, R.raw.snd_dealer_bust2);
  }

  //-------------------------------------------------------------------------
  // beginDealerStand
  //-------------------------------------------------------------------------
  private void beginDealerStand() {
    beginNextTurnPlayer();
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
      beginDealerStand();
      return;
    }

    if (doesTurnPlayerHaveMaxCards()) {
      if (shouldDealerStand()) {
        beginDealerStand();
      } else {
        beginDealerBust();
      }
    } else if (shouldDealerStand()) {
      beginDealerStand();
    } else {
      beginHit();
    }
  }

  //-------------------------------------------------------------------------
  // beginDouble
  //-------------------------------------------------------------------------
  private void beginDouble() {
    PlayerData playerData = (PlayerData)getTurnPlayerData();
    int playerBetValue = playerData.getBetValue();
    m_engine.setCredits(-playerBetValue, true);
    playerData.setDoubleDown();
    m_engine.setPlayerBet(playerData.getId(), playerBetValue * 2, false);

    m_state = BjPlayStates.DOUBLE;
    drawCard(m_turnPlayerId, true, 0, true);

    SoundChannel m_sndChDouble = m_engine.getSoundSystem().playSound(null,
      R.raw.snd_double_down, 1, true);
  }

  //-------------------------------------------------------------------------
  // beginHit
  //-------------------------------------------------------------------------
  private void beginHit() {
    m_state = BjPlayStates.HIT;
    drawCard(m_turnPlayerId, true, 0, true);
    m_sndChHit = m_engine.getSoundSystem().playSound(m_sndChHit, R.raw.snd_hit, 1, true);
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
    playerData.setBetValueVisible(false);

    int creditsWon = calcCreditsWon(playerId, m_engine.getStringResource(R.string.winRatioBlackjack));
    playerData.setAmountWonValue(creditsWon);
    playerData.setAmountWonValueVisible(true);
    m_totalCreditsWonOnRound += creditsWon;
  }

  //-------------------------------------------------------------------------
  // beginPlayerBlackjackPush
  //-------------------------------------------------------------------------
  private void beginPlayerBlackjackPush(PlayerIds playerId) {
    PlayerData playerData = (PlayerData)getPlayerData(playerId);
    playerData.setBlackjack();
    beginPlayerPush(playerId);
  }

  //-------------------------------------------------------------------------
  // beginPlayerBlitzWin
  //-------------------------------------------------------------------------
  private void beginPlayerBlitzWin() {
    PlayerData playerData = (PlayerData)getTurnPlayerData();
    playerData.setBlitzWin();
    playerData.setResultImage(R.drawable.result_label_blitz);
    playerData.setResultImageVisible(true);
    playerData.setBetValueVisible(false);

    int creditsWon = calcCreditsWon(m_turnPlayerId, m_engine.getStringResource(R.string.winRatioBlitz));
    playerData.setAmountWonValue(creditsWon);
    playerData.setAmountWonValueVisible(true);
    m_totalCreditsWonOnRound += creditsWon;

    playBjBlitzSound();
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
    playerData.setIsPush();
    playerData.setResultImage(R.drawable.result_label_push);
    playerData.setResultImageVisible(true);
    playerData.setBetValueVisible(false);

    int creditsWon = calcCreditsWon(playerId, m_engine.getStringResource(R.string.winRatioPush));
    playerData.setAmountWonValue(creditsWon);
    playerData.setAmountWonValueVisible(true);
    m_totalCreditsWonOnRound += creditsWon;
  }

  //-------------------------------------------------------------------------
  // beginPlayerStand
  //-------------------------------------------------------------------------
  private void beginPlayerStand() {
    m_sndChStand = m_engine.getSoundSystem().playSound(m_sndChStand, R.raw.snd_stand, 1, true);
    beginNextTurnPlayer();
  }

  //-------------------------------------------------------------------------
  // beginPlayerThunderjack
  //-------------------------------------------------------------------------
  private void beginPlayerThunderjack(PlayerIds playerId) {
    PlayerData playerData = (PlayerData)getPlayerData(playerId);
    playerData.setBlackjack();
    playerData.setThunderjack();
    playerData.setResultImage(R.drawable.result_label_thunderjack);
    playerData.setResultImageVisible(true);
    playerData.setBetValueVisible(false);

    int creditsWon = calcCreditsWon(playerId, m_engine.getStringResource(R.string.winRatioThunderjack));
    playerData.setAmountWonValue(creditsWon);
    playerData.setAmountWonValueVisible(true);
    m_totalCreditsWonOnRound += creditsWon;
  }

  //-------------------------------------------------------------------------
  // beginPlayerWon
  //-------------------------------------------------------------------------
  private void beginPlayerWon(PlayerIds playerId) {
    PlayerData playerData = (PlayerData)getPlayerData(playerId);
    playerData.setNormalWin();
    playerData.setResultImage(R.drawable.result_label_win);
    playerData.setResultImageVisible(true);
    playerData.setBetValueVisible(false);

    int creditsWon = calcCreditsWon(playerId, m_engine.getStringResource(R.string.winRatioNormal));
    playerData.setAmountWonValue(creditsWon);
    playerData.setAmountWonValueVisible(true);
    m_totalCreditsWonOnRound += creditsWon;
  }

  //-------------------------------------------------------------------------
  // beginRoundStart
  //-------------------------------------------------------------------------
  private void beginRoundStart() {
    updatePlayersPoints();

    if (m_cardsMatcher.doesPlayerHaveBlackjack(getPlayerData(PlayerIds.DEALER), m_engine)) {
      beginDealerBlackjack();
    } else {
      checkPlayersForBlackjack();
      beginNextTurnPlayer();
    }
  }

  //-------------------------------------------------------------------------
  // beginSplit
  //-------------------------------------------------------------------------
  private void beginSplit() {
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

    if (Math.random() > 0.5) {
      m_sndChSplit1 = m_engine.getSoundSystem().playSound(m_sndChSplit1, R.raw.snd_split1,
        1, true);
    } else {
      m_sndChSplit2 = m_engine.getSoundSystem().playSound(m_sndChSplit2, R.raw.snd_split1,
        1, true);
    }
  }

  //-------------------------------------------------------------------------
  // beginSurrender
  //-------------------------------------------------------------------------
  private void beginSurrender() {
    PlayerData playerData = (PlayerData)getTurnPlayerData();
    playerData.setSurrendered();
    playerData.setResultImage(R.drawable.result_label_surrender);
    playerData.setResultImageVisible(true);

    int creditsWon = calcCreditsWon(m_turnPlayerId, m_engine.getStringResource(R.string.winRatioSurrender));
    playerData.setBetValue(creditsWon);
    playerData.setBetValueVisible(true);
    m_totalCreditsWonOnRound += creditsWon;

    if (Math.random() > 0.5) {
      m_sndChSurrender1 = m_engine.getSoundSystem().playSound(m_sndChSurrender1, R.raw.snd_surrender1,
        1, true);
    } else {
      m_sndChSurrender2 = m_engine.getSoundSystem().playSound(m_sndChSurrender2, R.raw.snd_surrender2,
        1, true);
    }

    beginNextTurnPlayer();
  }

  //-------------------------------------------------------------------------
  // beginTurnPlayerBust
  //-------------------------------------------------------------------------
  private void beginTurnPlayerBust() {
    m_sndChBust = m_engine.getSoundSystem().playSound(m_sndChBust, R.raw.snd_bust, 1, true);
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
    if (playerData.getIsDoubleDown() && !playerData.getIsPush()) {
      amountWon *= 2;
    }
    return(amountWon);
  }

  //-------------------------------------------------------------------------
  // checkPlayersForBlackjack
  //-------------------------------------------------------------------------
  private void checkPlayersForBlackjack() {
    Iterator<PlayerIds>iterator = m_playerIdsOrder.iterator();
    int numBjs = 0;
    int numTjs = 0;
    while (iterator.hasNext()) {
      PlayerIds playerId = iterator.next();
      BasePlayerData playerData = getPlayerData(playerId);
      if (m_cardsMatcher.doesPlayerHaveThunderjack(playerData, m_engine)) {
        numTjs++;
        beginPlayerThunderjack(playerId);
        iterator.remove();
      } else if (m_cardsMatcher.doesPlayerHaveBlackjack(playerData, m_engine)) {
        numBjs++;
        beginPlayerBlackjack(playerId);
        iterator.remove();
      }
    }

    if (numTjs > 0) {
      m_sndChThunderjack = m_engine.getSoundSystem().playSound(m_sndChThunderjack,
        R.raw.snd_thunderjack,1, true);
    } else if (numBjs > 0) {
      playBjBlitzSound();
    }
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

    revealDealerSecondCard();

    int dealerScore = dealerData.getScore();

    for (PlayerIds playerId : PlayerIds.values()) {
      if (isDealerId(playerId)) {
        continue; //cant compare dealer with itself!
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
  // doesTurnPlayerHaveMaxCards
  //-------------------------------------------------------------------------
  private boolean doesTurnPlayerHaveMaxCards() {
    return(m_cardsMatcher.doesPlayerHaveMaxCards(getTurnPlayerData()));
  }

  //-------------------------------------------------------------------------
  // drawCard
  //-------------------------------------------------------------------------
  private void drawCard(PlayerIds playerId, boolean isFaceUp, long moveStartDelay,
  boolean startAnimation) {
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
    updatePlaySettings();
    playEndRoundSound();
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
  // initDealerBustSoundCompleteListener
  //-------------------------------------------------------------------------
  private void initDealerBustSoundCompleteListener() {
    m_onDealerBustSoundCompleteListener = new SoundSystem.OnSoundCompleteListener() {

      //-------------------------------------------------------------------------
      // onComplete
      //-------------------------------------------------------------------------
      @Override
      public void onComplete(SoundSystem soundSystem) {
        m_engine.getSoundSystem().setSoundCompleteListener(null);
        endRound();
      }
    };
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
        beginPlayerStand();
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
    resetDealer();
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
    return(m_cardsMatcher.canPlayerSplit((PlayerData)getTurnPlayerData()));
  }

  //-------------------------------------------------------------------------
  // isSurrenderAvailable
  //-------------------------------------------------------------------------
  private boolean isSurrenderAvailable() {
    PlayerData playerData = (PlayerData)getTurnPlayerData();
    return(playerData.getNumCards() == 2);
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
  // playBjBlitzSound
  //-------------------------------------------------------------------------
  private void playBjBlitzSound() {
    int index = (int)(Math.random() * 4);
    if (index == 0) {
      m_sndChAutoWin1 = m_engine.getSoundSystem().playSound(m_sndChAutoWin1, R.raw.snd_auto_win1,
        1, true);
    } else if (index == 1) {
      m_sndChAutoWin2 = m_engine.getSoundSystem().playSound(m_sndChAutoWin2, R.raw.snd_auto_win1,
        1, true);
    } else if (index == 2) {
      m_sndChAutoWin3 = m_engine.getSoundSystem().playSound(m_sndChAutoWin3, R.raw.snd_auto_win1,
        1, true);
    } else if (index == 3) {
      m_sndChAutoWin4 = m_engine.getSoundSystem().playSound(m_sndChAutoWin4, R.raw.snd_auto_win1,
        1, true);
    }
  }

  //-------------------------------------------------------------------------
  // playEndRoundSound
  //-------------------------------------------------------------------------
  private void playEndRoundSound() {
    int endCredits = m_engine.getCredits();
    int startCredits = m_engine.getCreditsAtStartOfRound();

    if (endCredits > startCredits) {
      m_winSound.play();
    } else if (endCredits < startCredits) {
      m_winSound.retract();
      if (m_engine.getDealerData().getHasBlackjack()) {
        m_sndChDealerBj = m_engine.getSoundSystem().playSound(m_sndChDealerBj,
          R.raw.snd_dealer_bj, 1, true);
      }
    }
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
  // resetDealer
  //-------------------------------------------------------------------------
  private void resetDealer() {
    m_engine.getDealerData().resetStatus();
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

      if (m_engine.isSplitPlayerId(playerData.getId())) {
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
      beginPlayerBlitzWin();
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
      beginPlayerStand();
      beginPlayerStand();
    }

    m_engine.updateBetValue();
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
  // updatePlaySettings
  //-------------------------------------------------------------------------
  private void updatePlaySettings() {
    m_playSettingsManager.update();
    m_engine.writeSettings();
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