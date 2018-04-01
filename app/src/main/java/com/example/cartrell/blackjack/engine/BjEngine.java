package com.example.cartrell.blackjack.engine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.example.cartrell.blackjack.R;
import com.example.cartrell.blackjack.cards.Deck;
import com.example.cartrell.blackjack.databinding.ActivityMainBinding;
import com.example.cartrell.blackjack.players.BasePlayerData;
import com.example.cartrell.blackjack.players.PlayerData;
import com.example.cartrell.blackjack.players.PlayerIds;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BjEngine implements IBjEngine {
  //=========================================================================
  // members
  //=========================================================================
  private HashMap<PlayerIds, PlayerData> m_players;

  private Deck m_deck;
  private BjBetChips m_betChips;
  private BjBetSystem m_betSystem;
  private BjCardsPrepSystem m_cardsPrepSystem;
  private BjPlaySystem m_playSystem;
  private BasePlayerData m_dealer;

  private Context m_context;
  private ActivityMainBinding m_binding;
  private ImageView m_deckImage;

  private int m_credits;
  private int m_betValue;
  private boolean m_atLeastOneRoundPlayed;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public BjEngine(Context context, ActivityMainBinding binding) {
    m_context = context;
    m_binding = binding;

    m_deckImage = m_binding.deckImage;
    m_betChips = new BjBetChips(this);
    m_betSystem = new BjBetSystem(this);
    m_cardsPrepSystem = new BjCardsPrepSystem(this);
    m_playSystem = new BjPlaySystem(this);

    initDecks();

    ConstraintLayout layout = (ConstraintLayout)m_binding.getRoot();
    initPlayersData(layout);
    setCredits(Integer.parseInt(m_context.getString(R.string.startingCredits)));
  }

  //-------------------------------------------------------------------------
  // atLeastOneRoundPlayed
  //-------------------------------------------------------------------------
  @Override
  public boolean atLeastOneRoundPlayed() {
    return(m_atLeastOneRoundPlayed);
  }

  //-------------------------------------------------------------------------
  // beginCardsPrep
  //-------------------------------------------------------------------------
  @Override
  public void beginCardsPrep() {
    m_cardsPrepSystem.begin();
  }

  //-------------------------------------------------------------------------
  // beginPlay
  //-------------------------------------------------------------------------
  @Override
  public void beginPlay() {
    m_playSystem.begin();
  }

  //-------------------------------------------------------------------------
  // beginRound
  //-------------------------------------------------------------------------
  @Override
  public void beginRound() {
    m_betSystem.begin();
  }

  //-------------------------------------------------------------------------
  // getBetChipData
  //-------------------------------------------------------------------------
  @Override
  public BjBetChipData getBetChipData(String betChipId) {
    return(m_betChips.getData(betChipId));
  }

  //-------------------------------------------------------------------------
  // getBetChipIds
  //-------------------------------------------------------------------------
  @Override
  public List<String> getBetChipIds() {
    return(m_betChips.getChipIds());
  }

  //-------------------------------------------------------------------------
  // getBetValue
  //-------------------------------------------------------------------------
  @Override
  public int getBetValue() {
    return(m_betValue);
  }

  //-------------------------------------------------------------------------
  // getBinding
  //-------------------------------------------------------------------------
  @Override
  public ActivityMainBinding getBinding() {
    return(m_binding);
  }

  //-------------------------------------------------------------------------
  // getColorResource
  //-------------------------------------------------------------------------
  @Override
  public int getColorResource(int resourceId) {
    return(ContextCompat.getColor(m_context, resourceId));
  }

  //-------------------------------------------------------------------------
  // getContext
  //-------------------------------------------------------------------------
  @Override
  public Context getContext() {
    return(m_context);
  }

  //-------------------------------------------------------------------------
  // getCredits
  //-------------------------------------------------------------------------
  @Override
  public int getCredits() {
    return(m_credits);
  }

  //-------------------------------------------------------------------------
  // getDealerData
  //-------------------------------------------------------------------------
  @Override
  public BasePlayerData getDealerData() {
    return(m_dealer);
  }

  //-------------------------------------------------------------------------
  // getDeck
  //-------------------------------------------------------------------------
  @Override
  public Deck getDeck() {
    return(m_deck);
  }

  //-------------------------------------------------------------------------
  // getDeckImage
  //-------------------------------------------------------------------------
  @Override
  public ImageView getDeckImage() {
    return(m_deckImage);
  }

  //-------------------------------------------------------------------------
  // getIntegerResource
  //-------------------------------------------------------------------------
  @Override
  public int getIntegerResource(int resourceId) {
    return(m_context.getResources().getInteger(resourceId));
  }

  //-------------------------------------------------------------------------
  // getPlayer
  //-------------------------------------------------------------------------
  @Override
  public PlayerData getPlayer(PlayerIds playerId) {
    return(m_players.get(playerId));
  }

  //-------------------------------------------------------------------------
  // getPlayers
  //-------------------------------------------------------------------------
  @Override
  public HashMap<PlayerIds, PlayerData> getPlayers() {
    return(m_players);
  }

  //-------------------------------------------------------------------------
  // getStringResource
  //-------------------------------------------------------------------------
  @Override
  public String getStringResource(int resourceId) {
    return(m_context.getResources().getString(resourceId));
  }

  //-------------------------------------------------------------------------
  // getIndexOf
  //-------------------------------------------------------------------------
  @Override
  public int getIndexOf(View view) {
    if (view == null) {
      return(-1); //sanity check
    }
    ViewGroup group = (ViewGroup)view.getParent();
    return(group != null ? group.indexOfChild(view) : -1);
  }

  //-------------------------------------------------------------------------
  // onBetChipClick
  //-------------------------------------------------------------------------
  public void onBetChipClick(View view) {
    m_betSystem.onBetChipClick((String)view.getTag());
  }

  //-------------------------------------------------------------------------
  // onClearClick
  //-------------------------------------------------------------------------
  public void onClearClick() {
    m_betSystem.onBetClearClick();
  }

  //-------------------------------------------------------------------------
  // onDealClick
  //-------------------------------------------------------------------------
  public void onDealClick() {
    m_betSystem.onDealClick();
    m_atLeastOneRoundPlayed = true;
  }

  //-------------------------------------------------------------------------
  // onDoubleClick
  //-------------------------------------------------------------------------
  public void onDoubleClick() {
    m_playSystem.beginDouble();
  }

  //-------------------------------------------------------------------------
  // onHitClick
  //-------------------------------------------------------------------------
  public void onHitClick() {
    m_playSystem.beginHit();
  }

  //-------------------------------------------------------------------------
  // onSplitClick
  //-------------------------------------------------------------------------
  public void onSplitClick() {
    m_playSystem.beginSplit();
  }

  //-------------------------------------------------------------------------
  // onStandClick
  //-------------------------------------------------------------------------
  public void onStandClick() {
    m_playSystem.beginStand();
  }

  //-------------------------------------------------------------------------
  // onSurrenderClick
  //-------------------------------------------------------------------------
  public void onSurrenderClick() {
    m_playSystem.beginSurrender();
  }

  //-------------------------------------------------------------------------
  // placeBet
  //-------------------------------------------------------------------------
  public void placeBet(PlayerIds playerId) {
    m_betSystem.placeBet(playerId);
  }

  //-------------------------------------------------------------------------
  // setBetChipVisibility
  //-------------------------------------------------------------------------
  @Override
  public void setBetChipVisibility(String chipId, boolean isVisible) {
    View chipImage = m_binding.getRoot().findViewWithTag(chipId);
    showView(chipImage, isVisible);
  }

  //-------------------------------------------------------------------------
  // setPlayerBet
  //-------------------------------------------------------------------------
  @Override
  public void setPlayerBet(PlayerIds playerId, int betValue) {
    PlayerData playerData = m_players.get(playerId);
    playerData.setBetValue(betValue);
    playerData.setBetValueVisible(true);
    playerData.removeBetChips();

    ArrayList<String> betChipIds = m_betChips.getChipIdsFor(betValue);
    for (String betChipId : betChipIds) {
      BjBetChip betChip = new BjBetChip(m_context, betChipId);
      playerData.addBetChip(betChip);
    }
  }

  //-------------------------------------------------------------------------
  // setCredits
  //-------------------------------------------------------------------------
  @Override
  public void setCredits(int value) {
    m_credits = value;
    m_binding.txtCredits.setText("CREDITS: " + String.valueOf(m_credits));
  }

  //-------------------------------------------------------------------------
  // setCredits
  //-------------------------------------------------------------------------
  @Override
  public void setCredits(int credits, boolean isOffset) {
    if (isOffset) {
      setCredits(m_credits + credits);
    } else {
      setCredits(credits);
    }
  }

  //-------------------------------------------------------------------------
  // showGameButtons
  //-------------------------------------------------------------------------
  @Override
  public void showGameButtons(EnumSet<BjGameButtonFlags> flags) {
    showView(m_binding.btnDouble, flags.contains(BjGameButtonFlags.DOUBLE));
    showView(m_binding.btnHit, flags.contains(BjGameButtonFlags.HIT));
    showView(m_binding.btnSplit, flags.contains(BjGameButtonFlags.SPLIT));
    showView(m_binding.btnStand, flags.contains(BjGameButtonFlags.STAND));
    showView(m_binding.btnSurrender, flags.contains(BjGameButtonFlags.SURRENDER));
  }

  //-------------------------------------------------------------------------
  // showView
  //-------------------------------------------------------------------------
  @Override
  public void showView(View view, boolean isVisible) {
    if (view != null) {
      view.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }
  }

  //-------------------------------------------------------------------------
  // updateBetValue
  //-------------------------------------------------------------------------
  @Override
  public void updateBetValue() {
    int betValue = 0;

    Iterator iterator = m_players.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry)iterator.next();
      PlayerData playerData = (PlayerData)entry.getValue();
      betValue += playerData.getBetValue();
    }

    m_betValue = betValue;
    m_binding.txtBet.setText("BET: " + String.valueOf(m_betValue));
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // initDecks
  //-------------------------------------------------------------------------
  private void initDecks() {
    final int NUM_DECKS = Math.max(1, getIntegerResource(R.integer.numDecks));
    m_deck = new Deck(m_context, NUM_DECKS);
  }

  //-------------------------------------------------------------------------
  // initPlayerData
  //-------------------------------------------------------------------------
  private void initPlayerData(PlayerIds playerId, String uiPositionCode,
  ConstraintLayout constraintLayout, float xDeck, float yDeck) {
    final int MAX_CARDS = getIntegerResource(R.integer.maxCardsPerHand);
    m_players.put(playerId, new PlayerData(constraintLayout, playerId,
      uiPositionCode, xDeck, yDeck, MAX_CARDS));
  }

  //-------------------------------------------------------------------------
  // initPlayersData
  //-------------------------------------------------------------------------
  private void initPlayersData(ConstraintLayout constraintLayout) {
    final ConstraintLayout _constraintLayout = constraintLayout;

    m_deckImage.getViewTreeObserver().addOnGlobalLayoutListener(
      new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
          m_deckImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);

          float xDeck = m_deckImage.getX();
          float yDeck = m_deckImage.getY();
          final int MAX_CARDS = getIntegerResource(R.integer.maxCardsPerHand);

          m_dealer = new BasePlayerData(_constraintLayout, PlayerIds.DEALER, "30",
            xDeck, yDeck, MAX_CARDS);

          m_players = new HashMap<>();
          initPlayerData(PlayerIds.RIGHT_BOTTOM, "00",_constraintLayout, xDeck, yDeck);
          initPlayerData(PlayerIds.RIGHT_TOP, "01",_constraintLayout, xDeck, yDeck);
          initPlayerData(PlayerIds.MIDDLE_BOTTOM, "10",_constraintLayout, xDeck, yDeck);
          initPlayerData(PlayerIds.MIDDLE_TOP, "11",_constraintLayout, xDeck, yDeck);
          initPlayerData(PlayerIds.LEFT_BOTTOM, "20",_constraintLayout, xDeck, yDeck);
          initPlayerData(PlayerIds.LEFT_TOP, "21",_constraintLayout, xDeck, yDeck);

          updateBetValue();
          beginRound();
        }
      });
  }

  //-------------------------------------------------------------------------
  // isPlayerHandFull
  //-------------------------------------------------------------------------
  private boolean isPlayerHandFull(PlayerIds playerId) {
    int maxCardsPerHand = getIntegerResource(R.integer.maxCardsPerHand);
    PlayerData playerData = m_players.get(playerId);
    return(playerData != null && playerData.getNumCards() >= maxCardsPerHand);
  }
}
