package com.gameplaycoder.thunderjack.sound;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class SoundSystem {
  //=========================================================================
  // static / const
  //=========================================================================
  private static final String LOG_NAME = SoundSystem.class.getName();

  //=========================================================================
  // members
  //=========================================================================
  private SoundPoolManager m_soundPoolManager;
  private MediaPlayerManager m_mediaPlayerManager;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public SoundSystem(Context context, int maxSoundPoolStreams) {
    if (context == null) {
      Log.i(LOG_NAME, "ctor. Context parameter is required");
      return;
    }

    m_soundPoolManager = new SoundPoolManager(context, maxSoundPoolStreams);
    m_mediaPlayerManager = new MediaPlayerManager(this, context);
  }

  //-----------------------------------------------------------------------------------
  // getSoundCompleteListener
  //-----------------------------------------------------------------------------------
  public SoundSystem.OnSoundCompleteListener getSoundCompleteListener() {
    return(m_mediaPlayerManager.getSoundCompleteListener());
  }

  //-------------------------------------------------------------------------
  // load
  //-------------------------------------------------------------------------
  public SoundChannel load(int soundResourceId, int priority, boolean autoPlay) {
    return(m_soundPoolManager.load(soundResourceId, priority, autoPlay));
  }

  //-------------------------------------------------------------------------
  // playMedia
  //-------------------------------------------------------------------------
  public void playMedia(int... soundResourceIds) {
    m_mediaPlayerManager.play(soundResourceIds);
  }

  //-------------------------------------------------------------------------
  // playMedia
  //-------------------------------------------------------------------------
  public void playMedia(OnSoundCompleteListener onSoundCompleteListener, int... soundResourceIds) {
    setSoundCompleteListener(onSoundCompleteListener);
    m_mediaPlayerManager.play(soundResourceIds);
  }

  //-------------------------------------------------------------------------
  // playRandomSound
  //-------------------------------------------------------------------------
  public int playRandomSound(HashMap<Integer, SoundChannel> soundChannelsByResourceId_in_out) {
    if (soundChannelsByResourceId_in_out == null || soundChannelsByResourceId_in_out.size() == 0) {
      return (0);
    }

    Set<Integer> resourceIdsSet = soundChannelsByResourceId_in_out.keySet();
    Integer[] resourceIds = resourceIdsSet.toArray(new Integer[soundChannelsByResourceId_in_out.size()]);

    Random rnd = new Random();
    int index = rnd.nextInt(resourceIds.length);
    Integer resourceId = resourceIds[index];

    SoundChannel soundChannel = soundChannelsByResourceId_in_out.get(resourceId);
    soundChannel = playSound(soundChannel, resourceId);
    soundChannelsByResourceId_in_out.put(resourceId, soundChannel);
    return(resourceId);
  }

  //-------------------------------------------------------------------------
  // playSound
  //-------------------------------------------------------------------------
  public SoundChannel playSound(SoundChannel channel, int soundResourceId) {
    final int priority = 1;
    final boolean autoPlay = true;
    return(playSound(channel, soundResourceId, priority, autoPlay));
  }

  //-------------------------------------------------------------------------
  // playSound
  //-------------------------------------------------------------------------
  public SoundChannel playSound(SoundChannel channel, int soundResourceId, int priority,
  boolean autoPlay) {
    return(m_soundPoolManager.play(channel, soundResourceId, priority, autoPlay));
  }

  //-----------------------------------------------------------------------------------
  // setSoundCompleteListener
  //-----------------------------------------------------------------------------------
  public void setSoundCompleteListener(SoundSystem.OnSoundCompleteListener listener) {
    m_mediaPlayerManager.setSoundCompleteListener(listener);
  }

  //-----------------------------------------------------------------------------------
  // uninit
  //-----------------------------------------------------------------------------------
  public void uninit() {
    uninitMediaPlayerManager();
    uninitSoundPoolManager();
  }

  //=========================================================================
  // callback interfaces
  //=========================================================================

  //-----------------------------------------------------------------------------------
  // OnSoundCompleteListener
  //-----------------------------------------------------------------------------------
  public interface OnSoundCompleteListener {
    void onComplete(SoundSystem soundSystem);
  }

  //=========================================================================
  // private
  //=========================================================================

  //-----------------------------------------------------------------------------------
  // uninitMediaPlayerManager
  //-----------------------------------------------------------------------------------
  private void uninitMediaPlayerManager() {
    if (m_mediaPlayerManager != null) {
      m_mediaPlayerManager.uninit();
      m_mediaPlayerManager = null;
    }
  }

  //-----------------------------------------------------------------------------------
  // uninitSoundPoolManager
  //-----------------------------------------------------------------------------------
  private void uninitSoundPoolManager() {
    if (m_soundPoolManager != null) {
      m_soundPoolManager.uninit();
      m_soundPoolManager = null;
    }
  }
}