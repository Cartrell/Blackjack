package com.gameplaycoder.thunderjack.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.util.ArrayList;

class SoundPoolManager {
  //=========================================================================
  // static / const
  //=========================================================================
  private static final String LOG_NAME = SoundPoolManager.class.getName();

  //=========================================================================
  // members
  //=========================================================================
  private ArrayList<SoundChannel> m_channels;
  private Context m_context;
  private SoundPool m_soundPool;
  private SoundPool.OnLoadCompleteListener m_onSoundLoadCompleteListener;

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  SoundPoolManager(Context context, int maxStreams) {
    init(context, maxStreams);
  }

  //-------------------------------------------------------------------------
  // load
  //-------------------------------------------------------------------------
  SoundChannel load(int soundResourceId, int priority, boolean autoPlay) {
    if (m_soundPool == null) {
      return(null);
    }

    SoundChannel channel = null;
    int soundId = m_soundPool.load(m_context, soundResourceId, priority);
    if (soundId != 0) {
      channel = new SoundChannel();
      channel.soundResourceId = soundResourceId;
      channel.soundId = soundId;
      channel.priority = priority;
      channel.autoPlay = autoPlay;
      m_channels.add(channel);
    }
    return(channel);
  }

  //-------------------------------------------------------------------------
  // play
  //-------------------------------------------------------------------------
  SoundChannel play(SoundChannel channel, int soundResourceId, int priority, boolean autoPlay) {
    if (m_soundPool == null) {
      return(channel); //sanity checks
    }

    if (channel == null) {
      return(load(soundResourceId, priority, autoPlay));
    }

    channel.streamId = m_soundPool.play(channel.soundId, 1f, 1f,
      channel.priority, 0, 1f);

    if (channel.streamId == 0 && channel.soundId > 0) {
      channel.autoPlay = true;
      channel.soundId = m_soundPool.load(m_context, channel.soundResourceId, channel.priority);
    }

    return(channel);
  }

  //-------------------------------------------------------------------------
  // uninit
  //-------------------------------------------------------------------------
  void uninit() {
    m_channels.clear();
    uninitSoundPool();
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // getSoundChannelBySoundId
  //-------------------------------------------------------------------------
  private SoundChannel getSoundChannelBySoundId(int soundId) {
    for (SoundChannel channel : m_channels) {
      if (channel.soundId == soundId) {
        return(channel);
      }
    }
    return(null);
  }

  //-------------------------------------------------------------------------
  // init
  //-------------------------------------------------------------------------
  private void init(Context context, int maxStreams) {
    m_context = context;
    m_channels = new ArrayList<>();
    initSoundPool(maxStreams);
  }

  //-------------------------------------------------------------------------
  // initSoundLoadCompleteListener
  //-------------------------------------------------------------------------
  private void initSoundLoadCompleteListener() {
    m_onSoundLoadCompleteListener = new SoundPool.OnLoadCompleteListener() {
      //-----------------------------------------------------------------------------------
      // onLoadComplete
      //-----------------------------------------------------------------------------------
      @Override
      public void onLoadComplete(SoundPool soundPool, int soundId, int status) {
        if (status != 0) {
          Log.i(LOG_NAME, "initSoundLoadCompleteListener. Error loading a sound");
          return;
        }

        SoundChannel channel = getSoundChannelBySoundId(soundId);
        if (channel != null && channel.autoPlay) {
          channel.streamId = soundPool.play(soundId, 1.0f, 1.0f,
            channel.priority, 0, 1.0f);
        }
      }
    };
  }

  //-------------------------------------------------------------------------
  // initSoundPool
  //-------------------------------------------------------------------------
  private void initSoundPool(int maxStreams) {
    m_soundPool = new SoundPool(maxStreams,  AudioManager.STREAM_MUSIC, 0);
    initSoundLoadCompleteListener();
    m_soundPool.setOnLoadCompleteListener(m_onSoundLoadCompleteListener);
  }

  //-------------------------------------------------------------------------
  // uninitSoundPool
  //-------------------------------------------------------------------------
  private void uninitSoundPool() {
    if (m_soundPool != null) {
      m_soundPool.release();
      m_soundPool = null;
    }
  }
}