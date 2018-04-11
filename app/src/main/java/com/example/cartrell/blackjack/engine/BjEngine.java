package com.example.cartrell.blackjack.engine;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

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

  private Views m_views;

  private Context m_context;
  private ActivityMainBinding m_binding;

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
    initDecks();
    initUi();
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
  // getViews
  //-------------------------------------------------------------------------
  @Override
  public Views getViews() {
    return(m_views);
  }

  //-------------------------------------------------------------------------
  // onBetChipClick
  //-------------------------------------------------------------------------
  /*public void onBetChipClick(View view) {
    m_betSystem.onBetChipClick((String)view.getTag());
  }*/

  //-------------------------------------------------------------------------
  // onClearClick
  //-------------------------------------------------------------------------
  /*public void onClearClick() {
    m_betSystem.onBetClearClick();
  }*/

  //-------------------------------------------------------------------------
  // onDealClick
  //-------------------------------------------------------------------------
  /*public void onDealClick() {
    m_betSystem.onDealClick();
    m_atLeastOneRoundPlayed = true;
  }*/

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
  // setAtLeastOneRoundPlayed
  //-------------------------------------------------------------------------
  @Override
  public void setAtLeastOneRoundPlayed() {
    m_atLeastOneRoundPlayed = true;
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
    playerData.setBetAmountWonValue(betValue);
    playerData.setBetAmountWonValueVisible(true);
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
    m_views.setCredits(value);
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
    /*showView(m_binding.btnDouble, flags.contains(BjGameButtonFlags.DOUBLE));
    showView(m_binding.btnHit, flags.contains(BjGameButtonFlags.HIT));
    showView(m_binding.btnSplit, flags.contains(BjGameButtonFlags.SPLIT));
    showView(m_binding.btnStand, flags.contains(BjGameButtonFlags.STAND));
    showView(m_binding.btnSurrender, flags.contains(BjGameButtonFlags.SURRENDER));*/
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
    m_views.setBetValue(m_betValue);
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
  // initUi
  //-------------------------------------------------------------------------
  private void initUi() {
    final ConstraintLayout constraintLayout = (ConstraintLayout)m_binding.getRoot();
    final IBjEngine engine = this;

    LayoutInflater inflater = LayoutInflater.from(m_context);
    final ConstraintLayout templateLayout = (ConstraintLayout)inflater.inflate(R.layout.main_template,
      (ViewGroup)m_binding.stubLayout, true);

    templateLayout.getViewTreeObserver().addOnGlobalLayoutListener(
      new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
          templateLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

          m_views = new Views(constraintLayout, templateLayout);

          float xDeck = m_views.getDeckImage().getX();
          float yDeck = m_views.getDeckImage().getY();
          final int MAX_CARDS = getIntegerResource(R.integer.maxCardsPerHand);
          PlayersCreator creator = new PlayersCreator(templateLayout, m_binding, xDeck,
            yDeck, MAX_CARDS, m_views);

          constraintLayout.removeView(templateLayout);

          m_dealer = creator.getDealer();
          m_players = creator.getPlayers();
          m_betChips = new BjBetChips(engine);
          m_betSystem = new BjBetSystem(engine);
          m_cardsPrepSystem = new BjCardsPrepSystem(engine);
          m_playSystem = new BjPlaySystem(engine);

          setCredits(Integer.parseInt(m_context.getString(R.string.startingCredits)));
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