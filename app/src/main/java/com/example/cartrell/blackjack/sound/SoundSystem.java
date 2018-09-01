package com.example.cartrell.blackjack.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.Random;

public class SoundSystem {
  //=========================================================================
  // static / const
  //=========================================================================
  private static final String LOG_NAME = SoundSystem.class.getName();

  //=========================================================================
  // members
  //=========================================================================
  private Context m_context;
  private MediaPlayer m_mediaPlayer;
  private AudioManager m_audioManager;
  private MediaPlayer.OnCompletionListener m_mediaPlayerCompletionListener;
  private AudioManager.OnAudioFocusChangeListener m_audioFocusChangeListener;
  private SoundSystem.OnSoundCompleteListener m_soundSystemCompleteListener;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public SoundSystem(Context context) {
    m_context = context;
    if (m_context == null) {
      Log.i(LOG_NAME, "ctor. Context parameter is required");
      return;
    }

    m_audioManager = (AudioManager)m_context.getSystemService(Context.AUDIO_SERVICE);
    if (m_audioManager == null) {
      Log.i(LOG_NAME, "ctor. Unable to obtain audio manager");
    }

    initMediaPlayerCompleteListener();
    initAudioFocusChangeListener();
  }

  //-----------------------------------------------------------------------------------
  // getOnSoundCompleteListener
  //-----------------------------------------------------------------------------------
  public SoundSystem.OnSoundCompleteListener getOnSoundCompleteListener() {
    return(m_soundSystemCompleteListener);
  }

  //-------------------------------------------------------------------------
  // play
  //-------------------------------------------------------------------------
  public void play(int... soundResourceIds) {
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

  //-------------------------------------------------------------------------
  // play
  //-------------------------------------------------------------------------
  public void play(OnSoundCompleteListener onSoundCompleteListener, int... soundResourceIds) {
    setOnSoundCompleteListener(onSoundCompleteListener);
    play(soundResourceIds);
  }

  //-----------------------------------------------------------------------------------
  // setOnSoundCompleteListener
  //-----------------------------------------------------------------------------------
  public void setOnSoundCompleteListener(SoundSystem.OnSoundCompleteListener listener) {
    m_soundSystemCompleteListener = listener;
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
  // initMediaPlayerCompleteListener
  //-----------------------------------------------------------------------------------
  private void initMediaPlayerCompleteListener() {
    final SoundSystem thisSoundSystem = this;

    m_mediaPlayerCompletionListener = new MediaPlayer.OnCompletionListener() {

      //-------------------------------------------------------------------------------
      // onCompletion
      //-------------------------------------------------------------------------------
      @Override
      public void onCompletion(MediaPlayer mediaPlayer) {
        releaseMediaPlayer();

        if (m_soundSystemCompleteListener != null) {
          m_soundSystemCompleteListener.onComplete(thisSoundSystem);
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

  //=========================================================================
  // OnSoundCompleteListener
  //=========================================================================
  public interface OnSoundCompleteListener {
    void onComplete(SoundSystem soundSystem);
  }
}
