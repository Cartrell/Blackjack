package com.example.cartrell.blackjack.engine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.cartrell.blackjack.R;
import com.example.cartrell.blackjack.StatsActivity;
import com.example.cartrell.blackjack.cards.Deck;
import com.example.cartrell.blackjack.databinding.ActivityMainBinding;
import com.example.cartrell.blackjack.players.BasePlayerData;
import com.example.cartrell.blackjack.players.PlayerData;
import com.example.cartrell.blackjack.players.PlayerIds;
import com.example.cartrell.blackjack.settings.Settings;
import com.example.cartrell.blackjack.settings.SettingsStorage;
import com.example.cartrell.blackjack.sound.SoundSystem;

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
  private SoundSystem m_soundSystem;

  private Views m_views;
  private BackgroundViewManager m_bgViewMgr;

  private Settings m_settings;

  private Activity m_activity;
  private Context m_context;
  private ActivityMainBinding m_binding;

  private int m_credits;
  private int m_betValue;
  private int m_creditsAtStartOfRound;
  private boolean m_atLeastOneRoundPlayed;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public BjEngine(Activity activity, ActivityMainBinding binding) {
    m_activity = activity;
    m_context = m_activity;
    m_binding = binding;
    //initSettings();
    initDecks();
    initUi();
    m_soundSystem = new SoundSystem(m_context);
  }

  //-------------------------------------------------------------------------
  // atLeastOneRoundPlayed
  //-------------------------------------------------------------------------
  @Override
  public boolean atLeastOneRoundPlayed() {
    return (m_atLeastOneRoundPlayed);
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
    return (m_betChips.getData(betChipId));
  }

  //-------------------------------------------------------------------------
  // getBetChipIds
  //-------------------------------------------------------------------------
  @Override
  public List<String> getBetChipIds() {
    return (m_betChips.getChipIds());
  }

  //-------------------------------------------------------------------------
  // getBetValue
  //-------------------------------------------------------------------------
  @Override
  public int getBetValue() {
    return (m_betValue);
  }

  //-------------------------------------------------------------------------
  // getBinding
  //-------------------------------------------------------------------------
  @Override
  public ActivityMainBinding getBinding() {
    return (m_binding);
  }

  //-------------------------------------------------------------------------
  // getColorResource
  //-------------------------------------------------------------------------
  @Override
  public int getColorResource(int resourceId) {
    return (ContextCompat.getColor(m_context, resourceId));
  }

  //-------------------------------------------------------------------------
  // getContext
  //-------------------------------------------------------------------------
  @Override
  public Context getContext() {
    return (m_context);
  }

  //-------------------------------------------------------------------------
  // getCredits
  //-------------------------------------------------------------------------
  @Override
  public int getCredits() {
    return (m_credits);
  }

  //-------------------------------------------------------------------------
  // getCreditsAtStartOfRound
  //-------------------------------------------------------------------------
  @Override
  public int getCreditsAtStartOfRound() {
    return (m_creditsAtStartOfRound);
  }

  //-------------------------------------------------------------------------
  // getDealerData
  //-------------------------------------------------------------------------
  @Override
  public BasePlayerData getDealerData() {
    return (m_dealer);
  }

  //-------------------------------------------------------------------------
  // getDeck
  //-------------------------------------------------------------------------
  @Override
  public Deck getDeck() {
    return (m_deck);
  }

  //-------------------------------------------------------------------------
  // getIntegerResource
  //-------------------------------------------------------------------------
  @Override
  public int getIntegerResource(int resourceId) {
    return (m_context.getResources().getInteger(resourceId));
  }

  //-------------------------------------------------------------------------
  // getPlayer
  //-------------------------------------------------------------------------
  @Override
  public PlayerData getPlayer(PlayerIds playerId) {
    return (m_players.get(playerId));
  }

  //-------------------------------------------------------------------------
  // getPlayers
  //-------------------------------------------------------------------------
  @Override
  public HashMap<PlayerIds, PlayerData> getPlayers() {
    return (m_players);
  }

  //-------------------------------------------------------------------------
  // getStringResource
  //-------------------------------------------------------------------------
  @Override
  public String getStringResource(int resourceId) {
    return (m_context.getResources().getString(resourceId));
  }

  //-------------------------------------------------------------------------
  // getIndexOf
  //-------------------------------------------------------------------------
  @Override
  public int getIndexOf(View view) {
    if (view == null) {
      return (-1); //sanity check
    }
    ViewGroup group = (ViewGroup) view.getParent();
    return (group != null ? group.indexOfChild(view) : -1);
  }

  //-------------------------------------------------------------------------
  // getSettings
  //-------------------------------------------------------------------------
  @Override
  public Settings getSettings() {
    return (m_settings);
  }

  //-------------------------------------------------------------------------
  // getSoundSystem
  //-------------------------------------------------------------------------
  public SoundSystem getSoundSystem() {
    return(m_soundSystem);
  }

  //-------------------------------------------------------------------------
  // getViews
  //-------------------------------------------------------------------------
  @Override
  public Views getViews() {
    return (m_views);
  }

  //-------------------------------------------------------------------------
  // isSplitPlayerId
  //-------------------------------------------------------------------------
  public boolean isSplitPlayerId(PlayerIds playerId) {
    return (
      PlayerIds.RIGHT_TOP.equals(playerId) ||
        PlayerIds.MIDDLE_TOP.equals(playerId) ||
        PlayerIds.LEFT_TOP.equals(playerId));
  }

  //-------------------------------------------------------------------------
  // onResume
  //-------------------------------------------------------------------------
  public void onResume() {
    initSettings();
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
  // setPlayerBet
  //-------------------------------------------------------------------------
  @Override
  public void setPlayerBet(PlayerIds playerId, int betValue, boolean isOrigBet) {
    PlayerData playerData = m_players.get(playerId);
    playerData.setBetValueVisible(betValue > 0);

    //hide this in case player won the last round - amount-won text and bet-value occupy the
    // same space (only one can be visible at a time)
    playerData.setAmountWonValueVisible(false);

    if (isOrigBet) {
      playerData.setOrigBetValue(betValue);
    }

    playerData.setBetValue(betValue);
    ArrayList<BjBetChip> betChips = createBetChips(m_betChips.getChipIdsFor(betValue));
    playerData.setBetChips(betChips);
  }

  //-------------------------------------------------------------------------
  // showGameButtons
  //-------------------------------------------------------------------------
  @Override
  public void showGameButtons(EnumSet<BjGameButtonFlags> flags) {
    showView(m_views.getDoubleButton(), flags.contains(BjGameButtonFlags.DOUBLE));
    showView(m_views.getHitButton(), flags.contains(BjGameButtonFlags.HIT));
    showView(m_views.getSplitButton(), flags.contains(BjGameButtonFlags.SPLIT));
    showView(m_views.getStandButton(), flags.contains(BjGameButtonFlags.STAND));
    showView(m_views.getSurrenderButton(), flags.contains(BjGameButtonFlags.SURRENDER));
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
      Map.Entry entry = (Map.Entry) iterator.next();
      PlayerData playerData = (PlayerData) entry.getValue();
      betValue += playerData.getBetValue();
    }

    m_betValue = betValue;
    m_views.setBetValue(m_betValue);
  }

  //-------------------------------------------------------------------------
  // updateCreditsAtStartOfRound
  //-------------------------------------------------------------------------
  @Override
  public void updateCreditsAtStartOfRound() {
    m_creditsAtStartOfRound = getCredits();
  }

  //-------------------------------------------------------------------------
  // writeSettings
  //-------------------------------------------------------------------------
  public void writeSettings() {
    SettingsStorage storage = new SettingsStorage(m_activity);
    storage.write(m_settings);
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // changeBackgroundColor
  //-------------------------------------------------------------------------
  private void changeBackgroundColor() {
    m_bgViewMgr = new BackgroundViewManager(m_views.getBackgroundImage());
  }

  //-------------------------------------------------------------------------
  // createBetChips
  //-------------------------------------------------------------------------
  private ArrayList<BjBetChip> createBetChips(ArrayList<String> chipIds) {
    ArrayList<BjBetChip> betChips = new ArrayList<>();
    for (String chipId : chipIds) {
      betChips.add(new BjBetChip(m_context, chipId));
    }
    return (betChips);
  }

  //-------------------------------------------------------------------------
  // initDecks
  //-------------------------------------------------------------------------
  private void initDecks() {
    final int NUM_DECKS = Math.max(1, getIntegerResource(R.integer.numDecks));
    m_deck = new Deck(m_context, NUM_DECKS);
  }

  //-------------------------------------------------------------------------
  // initSettings
  //-------------------------------------------------------------------------
  private void initSettings() {
    m_settings = new Settings();
    readSettings();
  }

  //-------------------------------------------------------------------------
  // initUi
  //-------------------------------------------------------------------------
  private void initUi() {
    final ConstraintLayout constraintLayout = (ConstraintLayout) m_binding.getRoot();
    final IBjEngine engine = this;

    LayoutInflater inflater = LayoutInflater.from(m_context);
    final ConstraintLayout templateLayout = (ConstraintLayout) inflater.inflate(R.layout.main_template,
      (ViewGroup) m_binding.stubLayout, true);

    templateLayout.getViewTreeObserver().addOnGlobalLayoutListener(
      new ViewTreeObserver.OnGlobalLayoutListener() {

        //-------------------------------------------------------------------------
        // onGlobalLayout
        //-------------------------------------------------------------------------
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

          changeBackgroundColor();

          m_dealer = creator.getDealer();
          m_players = creator.getPlayers();
          m_betChips = new BjBetChips(engine);
          m_betSystem = new BjBetSystem(engine);
          m_cardsPrepSystem = new BjCardsPrepSystem(engine);
          m_playSystem = new BjPlaySystem(engine);

          //initSettings();
          initSettingsButton();

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
    return (playerData != null && playerData.getNumCards() >= maxCardsPerHand);
  }

  //-------------------------------------------------------------------------
  // initSettingsButton
  //-------------------------------------------------------------------------
  private void initSettingsButton() {
    m_views.getSettingsButton().setOnClickListener(new View.OnClickListener() {

      //-------------------------------------------------------------------------
      // onClick
      //-------------------------------------------------------------------------
      @Override
      public void onClick(View v) {
        startStatsActivity();
      }
    });
  }

  //-------------------------------------------------------------------------
  // readSettings
  //-------------------------------------------------------------------------
  private void readSettings() {
    SettingsStorage storage = new SettingsStorage(m_activity);
    storage.read(m_settings);
  }

  //-------------------------------------------------------------------------
  // startStatsActivity
  //-------------------------------------------------------------------------
  private void startStatsActivity() {
    Intent intent = new Intent(m_context, StatsActivity.class);
    intent.putExtra(Settings.INTENT_KEY, m_settings);
    m_activity.startActivity(intent);
  }
}

