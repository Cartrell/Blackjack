package com.gameplaycoder.thunderjack.engine.sound;

import android.content.Context;

import com.gameplaycoder.thunderjack.R;
import com.gameplaycoder.thunderjack.sound.SoundChannel;
import com.gameplaycoder.thunderjack.sound.SoundSystem;

import java.util.HashMap;

public class BjSoundSystem {
  //=========================================================================
  // members
  //=========================================================================
  private HashMap<Integer, SoundChannel> m_surrenderSoundChannels;
  private HashMap<Integer, SoundChannel> m_autoWinSoundChannels;
  private HashMap<Integer, SoundChannel> m_splitSoundChannels;
  private HashMap<Integer, SoundChannel> m_dealCardSoundChannels;

  private BjSoundSystem.OnSoundCompleteListener m_bjSoundSystemCompleteListener;
  private BjWinSound m_winSound;

  private SoundSystem.OnSoundCompleteListener m_soundSystemCompleteListener;
  private SoundSystem m_soundSystem;

  private SoundChannel m_sndChHit;
  private SoundChannel m_sndChStand;
  private SoundChannel m_sndChBust;
  private SoundChannel m_sndChThunderjack;
  private SoundChannel m_sndChDealerBj;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public BjSoundSystem(Context context, int maxSoundPoolStreams) {
    m_soundSystem = new SoundSystem(context, maxSoundPoolStreams);
    m_winSound = new BjWinSound();

    initAutoWinSoundChannels();
    initSurrenderSoundChannels();
    initSplitSoundChannels();
    initDealCardSoundChannels();

    initSoundSystemCompleteListener();
  }

  //-------------------------------------------------------------------------
  // playAddBet
  //-------------------------------------------------------------------------
  public void playAddBet() {
    m_soundSystem.playSound(null, R.raw.snd_bet_add);
  }

  //-------------------------------------------------------------------------
  // playBjBlitz
  //-------------------------------------------------------------------------
  public void playBjBlitz() {
    m_soundSystem.playRandomSound(m_autoWinSoundChannels);
  }

  //-------------------------------------------------------------------------
  // playBust
  //-------------------------------------------------------------------------
  public void playBust() {
    m_sndChBust = m_soundSystem.playSound(m_sndChBust, R.raw.snd_bust);
  }

  //-------------------------------------------------------------------------
  // playDealCard
  //-------------------------------------------------------------------------
  public void playDealCard() {
    m_soundSystem.playRandomSound(m_dealCardSoundChannels);
  }

  //-------------------------------------------------------------------------
  // playDealerBj
  //-------------------------------------------------------------------------
  public void playDealerBj() {
    m_sndChDealerBj = m_soundSystem.playSound(m_sndChDealerBj, R.raw.snd_dealer_bj);
  }

  //-------------------------------------------------------------------------
  // playDealerBust
  //-------------------------------------------------------------------------
  public void playDealerBust(final BjSoundSystem.OnSoundCompleteListener listener) {
    m_bjSoundSystemCompleteListener = listener;
    m_soundSystem.playMedia(m_soundSystemCompleteListener, R.raw.snd_dealer_bust1, R.raw.snd_dealer_bust2);
  }

  //-------------------------------------------------------------------------
  // playDoubleDown
  //-------------------------------------------------------------------------
  public void playDoubleDown() {
    m_soundSystem.playSound(null, R.raw.snd_double_down);
  }

  //-------------------------------------------------------------------------
  // playHit
  //-------------------------------------------------------------------------
  public void playHit() {
    m_sndChHit = m_soundSystem.playSound(m_sndChHit, R.raw.snd_hit);
  }

  //-------------------------------------------------------------------------
  // playShuffle
  //-------------------------------------------------------------------------
  public void playShuffle(BjSoundSystem.OnSoundCompleteListener listener) {
    m_bjSoundSystemCompleteListener = listener;
    m_soundSystem.playMedia(m_soundSystemCompleteListener, R.raw.snd_card_shuffle0, R.raw.snd_card_shuffle1);
  }

  //-------------------------------------------------------------------------
  // playSplit
  //-------------------------------------------------------------------------
  public void playSplit() {
    m_soundSystem.playRandomSound(m_splitSoundChannels);
  }

  //-------------------------------------------------------------------------
  // playStand
  //-------------------------------------------------------------------------
  public void playStand() {
    m_sndChStand = m_soundSystem.playSound(m_sndChStand, R.raw.snd_stand);
  }

  //-------------------------------------------------------------------------
  // playSurrender
  //-------------------------------------------------------------------------
  public void playSurrender() {
    m_soundSystem.playRandomSound(m_surrenderSoundChannels);
  }

  //-------------------------------------------------------------------------
  // playTj
  //-------------------------------------------------------------------------
  public void playTj() {
    m_sndChThunderjack = m_soundSystem.playSound(m_sndChThunderjack, R.raw.snd_thunderjack);
  }

  //-------------------------------------------------------------------------
  // playWin
  //-------------------------------------------------------------------------
  public void playWin() {
    int soundResourceId = m_winSound.advanceSoundId();
    m_soundSystem.playSound(null, soundResourceId);
  }

  //-------------------------------------------------------------------------
  // retractWin
  //-------------------------------------------------------------------------
  public void retractWin() {
    m_winSound.retract();
  }

  //=========================================================================
  // callback interfaces
  //=========================================================================

  //-------------------------------------------------------------------------
  // OnSoundCompleteListener
  //-------------------------------------------------------------------------
  public interface OnSoundCompleteListener {
    void onComplete(BjSoundSystem soundSystem);
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // initAutoWinSoundChannels
  //-------------------------------------------------------------------------
  private void initAutoWinSoundChannels() {
    m_autoWinSoundChannels = initSoundChannelsMap(R.raw.snd_auto_win1, R.raw.snd_auto_win2,
      R.raw.snd_auto_win3, R.raw.snd_auto_win4);
  }

  //-------------------------------------------------------------------------
  // initDealCardSoundChannels
  //-------------------------------------------------------------------------
  private void initDealCardSoundChannels() {
    m_dealCardSoundChannels = initSoundChannelsMap(R.raw.snd_card_deal0, R.raw.snd_card_deal1,
      R.raw.snd_card_deal2);
  }

  //-------------------------------------------------------------------------
  // initSoundSystemCompleteListener
  //-------------------------------------------------------------------------
  private void initSoundSystemCompleteListener() {
    final BjSoundSystem _this = this;
    m_soundSystemCompleteListener = new SoundSystem.OnSoundCompleteListener() {
      //-------------------------------------------------------------------------
      // onComplete
      //-------------------------------------------------------------------------
      @Override
      public void onComplete(SoundSystem soundSystem) {
        m_soundSystem.setSoundCompleteListener(null);
        if (m_bjSoundSystemCompleteListener != null) {
          m_bjSoundSystemCompleteListener.onComplete(_this);
        }
      }
    };
  }

  //-------------------------------------------------------------------------
  // initSoundChannelsMap
  //-------------------------------------------------------------------------
  private HashMap<Integer, SoundChannel> initSoundChannelsMap(int... soundResourceIds) {
    HashMap<Integer, SoundChannel> soundChannels = new HashMap<>();
    for (int soundResourceId : soundResourceIds) {
      soundChannels.put(soundResourceId, null);
    }

    return(soundChannels);
  }

  //-------------------------------------------------------------------------
  // initSplitSoundChannels
  //-------------------------------------------------------------------------
  private void initSplitSoundChannels() {
    m_splitSoundChannels = initSoundChannelsMap(R.raw.snd_split1, R.raw.snd_split2);
  }

  //-------------------------------------------------------------------------
  // initSurrenderSoundChannels
  //-------------------------------------------------------------------------
  private void initSurrenderSoundChannels() {
    m_surrenderSoundChannels = initSoundChannelsMap(R.raw.snd_surrender1, R.raw.snd_surrender2);
  }
}