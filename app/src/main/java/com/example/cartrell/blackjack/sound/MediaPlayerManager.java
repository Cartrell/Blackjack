package com.example.cartrell.blackjack.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.Random;

class MediaPlayerManager {
  //=========================================================================
  // static / const
  //=========================================================================
  private static final String LOG_NAME = MediaPlayerManager.class.getName();

  //=========================================================================
  // members
  //=========================================================================
  private SoundSystem m_soundSystem;
  private SoundSystem.OnSoundCompleteListener m_soundSystemCompleteListener;

  private Context m_context;

  private MediaPlayer m_mediaPlayer;
  private AudioManager m_audioManager;
  private MediaPlayer.OnCompletionListener m_mediaPlayerCompletionListener;
  private AudioManager.OnAudioFocusChangeListener m_audioFocusChangeListener;

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  MediaPlayerManager(SoundSystem soundSystem, Context context) {
    m_soundSystem = soundSystem;
    m_context = context;
    initAudioManager();
    initAudioFocusChangeListener();
    initMediaPlayerCompleteListener();
  }

  //-----------------------------------------------------------------------------------
  // getSoundCompleteListener
  //-----------------------------------------------------------------------------------
  public SoundSystem.OnSoundCompleteListener getSoundCompleteListener() {
    return(m_soundSystemCompleteListener);
  }

  //-------------------------------------------------------------------------
  // play
  //-------------------------------------------------------------------------
  void play(int... soundResourceIds) {
    if (m_audioManager == null) {
      return;
    }

    //stop any currently playing media player first
    releaseMediaPlayer();

    //if more than one sound resource id is specified, pick a random one
    Random rnd = new Random();
    int index = rnd.nextInt(soundResourceIds.length);

    m_mediaPlayer = MediaPlayer.create(m_context, soundResourceIds[index]);
    m_mediaPlayer.setOnCompletionListener(m_mediaPlayerCompletionListener);

    int request = m_audioManager.requestAudioFocus(m_audioFocusChangeListener,
      AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

    if (request == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
      m_mediaPlayer.start();
    } else if (request == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
      releaseMediaPlayer();
    }
  }

  //-----------------------------------------------------------------------------------
  // setSoundCompleteListener
  //-----------------------------------------------------------------------------------
  public void setSoundCompleteListener(SoundSystem.OnSoundCompleteListener listener) {
    m_soundSystemCompleteListener = listener;
  }

  //-------------------------------------------------------------------------
  // uninit
  //-------------------------------------------------------------------------
  void uninit() {
    releaseMediaPlayer();
    m_soundSystem = null;
    m_context = null;
    setSoundCompleteListener(null);
  }

  //=========================================================================
  // private
  //=========================================================================

  //-----------------------------------------------------------------------------------
  // initAudioFocusChangeListener
  //-----------------------------------------------------------------------------------
  private void initAudioFocusChangeListener() {
    m_audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {

      //-------------------------------------------------------------------------------
      // onAudioFocusChange
      //-------------------------------------------------------------------------------
      @Override
      public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
          case AudioManager.AUDIOFOCUS_GAIN:
          case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
            m_mediaPlayer.start();
            break;

          case AudioManager.AUDIOFOCUS_LOSS:
            releaseMediaPlayer();
            break;

          case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
          case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
            m_mediaPlayer.pause();
            m_mediaPlayer.seekTo(0);
            break;
        }
      }
    };
  }

  //-----------------------------------------------------------------------------------
  // initAudioManager
  //-----------------------------------------------------------------------------------
  private void initAudioManager() {
    m_audioManager = (AudioManager)m_context.getSystemService(Context.AUDIO_SERVICE);
    if (m_audioManager == null) {
      Log.i(LOG_NAME, "initAudioManager. Unable to obtain audio manager");
    }
  }

  //-----------------------------------------------------------------------------------
  // initMediaPlayerCompleteListener
  //-----------------------------------------------------------------------------------
  private void initMediaPlayerCompleteListener() {
    m_mediaPlayerCompletionListener = new MediaPlayer.OnCompletionListener() {

      //-------------------------------------------------------------------------------
      // onCompletion
      //-------------------------------------------------------------------------------
      @Override
      public void onCompletion(MediaPlayer mediaPlayer) {
        releaseMediaPlayer();

        if (m_soundSystemCompleteListener != null) {
          m_soundSystemCompleteListener.onComplete(m_soundSystem);
        }
      }
    };
  }

  //-----------------------------------------------------------------------------------
  // releaseMediaPlayer
  //-----------------------------------------------------------------------------------
  private void releaseMediaPlayer() {
    if (m_mediaPlayer != null) {
      m_mediaPlayer.release();
      m_mediaPlayer = null;
    }

    if (m_audioManager != null) {
      m_audioManager.abandonAudioFocus(m_audioFocusChangeListener);
    }
  }

}