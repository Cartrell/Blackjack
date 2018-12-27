package com.gameplaycoder.thunderjack.engine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.AsyncLayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.gameplaycoder.thunderjack.R;
import com.gameplaycoder.thunderjack.StatsActivity;
import com.gameplaycoder.thunderjack.cards.Deck;
import com.gameplaycoder.thunderjack.databinding.ActivityMainBinding;
import com.gameplaycoder.thunderjack.players.BaseHandData;
import com.gameplaycoder.thunderjack.players.BasePlayerData;
import com.gameplaycoder.thunderjack.players.PlayerData;
import com.gameplaycoder.thunderjack.players.PlayerIds;
import com.gameplaycoder.thunderjack.settings.Settings;
import com.gameplaycoder.thunderjack.settings.SettingsStorage;
import com.gameplaycoder.thunderjack.sound.SoundChannel;
import com.gameplaycoder.thunderjack.sound.SoundSystem;
import com.gameplaycoder.thunderjack.utils.CreditsRenewalChecker;
import com.gameplaycoder.thunderjack.utils.Metrics;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BjEngine implements IBjEngine {
  //=========================================================================
  // members
  //=========================================================================
  private HashMap<PlayerIds, PlayerData> m_players;

  private IBjEngineOnActivityResult m_onActivityResult;

  private BjBetChips m_betChips;
  private BjBetSystem m_betSystem;
  private BjCardsPrepSystem m_cardsPrepSystem;
  private BjPlaySystem m_playSystem;
  private BjLayoutComps m_layoutComps;

  private Deck m_deck;
  private BasePlayerData m_dealer;
  private SoundSystem m_soundSystem;
  private BaseHandData.OnCardMoveStartListener m_cardMoveStartListener;
  private BackgroundViewManager m_bgViewMgr;
  private Settings m_settings;

  private Activity m_activity;
  private Context m_context;
  private ActivityMainBinding m_binding;

  private int m_credits;
  private int m_betValue;
  private int m_creditsAtStartOfRound;
  private boolean m_atLeastOneRoundPlayed;
  private SoundChannel m_sndChCardDeal1;
  private SoundChannel m_sndChCardDeal2;
  private SoundChannel m_sndChCardDeal3;

  private AsyncLayoutInflater.OnInflateFinishedListener m_onInflateFinished;

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
    initSettings();
    initCardMoveStartListener();
    initDecks();
    initUi();
    m_soundSystem = new SoundSystem(m_context, getIntegerResource(R.integer.maxSoundPoolStreams));
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
  // getLayoutComps
  //-------------------------------------------------------------------------
  @Override
  public BjLayoutComps getLayoutComps() {
    return (m_layoutComps);
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
  // isSplitPlayerId
  //-------------------------------------------------------------------------
  public boolean isSplitPlayerId(PlayerIds playerId) {
    return (
      PlayerIds.RIGHT_TOP.equals(playerId) ||
      PlayerIds.MIDDLE_TOP.equals(playerId) ||
      PlayerIds.LEFT_TOP.equals(playerId));
  }

  //-------------------------------------------------------------------------
  // onActivityResult
  //-------------------------------------------------------------------------
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (m_onActivityResult != null) {
      m_onActivityResult.onResult(requestCode, resultCode, data);
    }
  }

  //-------------------------------------------------------------------------
  // pause
  //-------------------------------------------------------------------------
  public void pause() {
    if (m_bgViewMgr != null) {
      m_bgViewMgr.stopAnimation();
    }
  }

  //-------------------------------------------------------------------------
  // resume
  //-------------------------------------------------------------------------
  public void resume() {
    if (m_bgViewMgr != null) {
      m_bgViewMgr.startAnimation();
    }
  }

  //-------------------------------------------------------------------------
  // setAtLeastOneRoundPlayed
  //-------------------------------------------------------------------------
  @Override
  public void setAtLeastOneRoundPlayed() {
    m_atLeastOneRoundPlayed = true;
  }

  //-------------------------------------------------------------------------
  // setCredits
  //-------------------------------------------------------------------------
  @Override
  public void setCredits(int value) {
    m_credits = value;
    m_layoutComps.betAndCreditsTexts.setCredits(value);
    writeCreditsToSettings();
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
  // setOnActivityResult
  //-------------------------------------------------------------------------
  public void setOnActivityResult(IBjEngineOnActivityResult value) {
    m_onActivityResult = value;
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
    showView(m_layoutComps.gameButtons.doubleButton, flags.contains(BjGameButtonFlags.DOUBLE));
    showView(m_layoutComps.gameButtons.hitButton, flags.contains(BjGameButtonFlags.HIT));
    showView(m_layoutComps.gameButtons.splitButton, flags.contains(BjGameButtonFlags.SPLIT));
    showView(m_layoutComps.gameButtons.standButton, flags.contains(BjGameButtonFlags.STAND));
    showView(m_layoutComps.gameButtons.surrenderButton, flags.contains(BjGameButtonFlags.SURRENDER));
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
  // stop
  //-------------------------------------------------------------------------
  public void stop() {
    CreditsRenewalChecker creditsRenewalChecker = new CreditsRenewalChecker(this);
    int credits = getCredits();
    if (creditsRenewalChecker.shouldTimeBeUpdated(credits)) {
      updateLastGameClosedTimeWithLowCredits();
    }
  }

  //-------------------------------------------------------------------------
  // updateBetValue
  //-------------------------------------------------------------------------
  @Override
  public void updateBetValue() {
    int betValue = 0;

    for (Map.Entry entry : m_players.entrySet()) {
      PlayerData playerData = (PlayerData) entry.getValue();
      betValue += playerData.getBetValue();
    }

    m_betValue = betValue;
    m_layoutComps.betAndCreditsTexts.setBetValue(m_betValue);
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
    SettingsStorage storage = new SettingsStorage();
    storage.write(m_settings);
  }

  //=========================================================================
  // private
  //=========================================================================

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
  // initBackgroundViewManager
  //-------------------------------------------------------------------------
  private void initBackgroundViewManager() {
    m_bgViewMgr = new BackgroundViewManager(m_layoutComps.gameBackground.image);
    m_bgViewMgr.startAnimation();
  }

  //-------------------------------------------------------------------------
  // initCardMoveStartListener
  //-------------------------------------------------------------------------
  private void initCardMoveStartListener() {
    m_cardMoveStartListener = new BaseHandData.OnCardMoveStartListener() {

      //-------------------------------------------------------------------------
      // onComplete
      //-------------------------------------------------------------------------
      @Override
      public void onComplete(BaseHandData baseHandData) {
        int index = (int)(Math.random() * 3);
        if (index == 0) {
          m_sndChCardDeal1 = m_soundSystem.playSound(m_sndChCardDeal1, R.raw.snd_card_deal0,
            1, true);
        } else if (index == 1) {
          m_sndChCardDeal2 = m_soundSystem.playSound(m_sndChCardDeal2, R.raw.snd_card_deal1,
            1, true);
        } else if (index == 2) {
          m_sndChCardDeal3 = m_soundSystem.playSound(m_sndChCardDeal3, R.raw.snd_card_deal2,
            1, true);
        }
      }
    };
  }

  //-------------------------------------------------------------------------
  // initDecks
  //-------------------------------------------------------------------------
  private void initDecks() {
    final int NUM_DECKS = Math.max(1, getIntegerResource(R.integer.numDecks));
    m_deck = new Deck(m_context, NUM_DECKS);
  }

  //-------------------------------------------------------------------------
  // initInflateFinishedListener
  //-------------------------------------------------------------------------
  private void initInflateFinishedListener() {
    final IBjEngine engine = this;

    m_onInflateFinished = new AsyncLayoutInflater.OnInflateFinishedListener() {
      //-------------------------------------------------------------------------
      // isEligibleForCreditsRenewal
      //-------------------------------------------------------------------------
      private boolean isEligibleForCreditsRenewal() {
        CreditsRenewalChecker creditsRenewalChecker = new CreditsRenewalChecker(engine);
        int credits = m_settings.getCredits();
        return(creditsRenewalChecker.isEligibleForCreditsRenewal(credits));
      }

      //-------------------------------------------------------------------------
      // onInflateFinished
      //-------------------------------------------------------------------------
      @Override
      public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {
        m_binding.activityMain.addView(view);

        final View _view = view;
        _view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
            _view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            Metrics.SetLayoutSize(
              m_binding.activityMain.getWidth(),
              m_binding.activityMain.getHeight());

            m_layoutComps = new BjLayoutComps(m_context, m_binding.activityMain);

            m_binding.activityMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
              @Override
              public void onGlobalLayout() {
                m_binding.progressBar.setVisibility(View.GONE);

                m_binding.activityMain.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                float xDeck = m_layoutComps.settingsButtonAndDeck.deckImage.getX();
                float yDeck = m_layoutComps.settingsButtonAndDeck.deckImage.getY();
                final int MAX_CARDS = getIntegerResource(R.integer.maxCardsPerHand);

                PlayersCreator creator = new PlayersCreator(m_layoutComps, m_binding.activityMain, xDeck,
                  yDeck, MAX_CARDS, m_cardMoveStartListener);

                initBackgroundViewManager();

                m_dealer = creator.getDealer();
                m_players = creator.getPlayers();
                //m_betChips = new BjBetChips(engine);
                m_betChips = new BjBetChips(m_context);
                m_betSystem = new BjBetSystem(engine, m_activity);
                m_cardsPrepSystem = new BjCardsPrepSystem(engine);
                m_playSystem = new BjPlaySystem(engine);

                initSettingsButton();

                if (m_settings.hasAppRanAtLeastOnce()) {
                  if (isEligibleForCreditsRenewal()) {
                    renewCredits();
                  } else {
                    setCredits(m_settings.getCredits(), false);
                  }
                } else {
                  setCredits(Integer.parseInt(m_context.getString(R.string.startingCredits)));
                }

                m_settings.appRanAtLeastOnce(true);

                updateBetValue();
                beginRound();
              }
            });
          }
        });
      }

      //-------------------------------------------------------------------------
      // renewCredits
      //-------------------------------------------------------------------------
      private void renewCredits() {
        setCredits(Integer.parseInt(m_context.getString(R.string.startingCredits)));

        //clear last time closed
        m_settings.setLastGameClosedTimeWithLowCredits(0);
        writeSettings();
      }
    };
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
    initInflateFinishedListener();
    AsyncLayoutInflater inflater = new AsyncLayoutInflater(m_context);
    inflater.inflate(R.layout.main_template, null, m_onInflateFinished);
  }

  //-------------------------------------------------------------------------
  // initSettingsButton
  //-------------------------------------------------------------------------
  private void initSettingsButton() {
    m_layoutComps.settingsButtonAndDeck.settingsButton.setOnClickListener(new View.OnClickListener() {

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
    SettingsStorage storage = new SettingsStorage();
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

  //-------------------------------------------------------------------------
  // updateLastGameClosedTimeWithLowCredits
  //-------------------------------------------------------------------------
  private void updateLastGameClosedTimeWithLowCredits() {
    long time = new Date().getTime();
    m_settings.setLastGameClosedTimeWithLowCredits(time);
    writeSettings();
  }

  //-------------------------------------------------------------------------
  // writeCreditsToSettings
  //-------------------------------------------------------------------------
  private void writeCreditsToSettings() {
    m_settings.setCredits(m_credits);
    writeSettings();
  }
}