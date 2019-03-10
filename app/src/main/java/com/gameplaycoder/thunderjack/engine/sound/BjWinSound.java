package com.gameplaycoder.thunderjack.engine.sound;

import com.gameplaycoder.thunderjack.R;
import com.gameplaycoder.thunderjack.engine.IBjEngine;
import com.gameplaycoder.thunderjack.sound.SoundSystem;

final class BjWinSound {
  //=========================================================================
  // package-private
  //=========================================================================
  private int[] m_soundResourceIds;
  private int m_soundIndex;

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  BjWinSound() {
    initSoundResourceIds();
    m_soundIndex = 0;
  }

  //-------------------------------------------------------------------------
  // advanceSoundId
  //-------------------------------------------------------------------------
  int advanceSoundId() {
    int currentSoundIndex = m_soundIndex;
    m_soundIndex = (m_soundIndex + 1) % m_soundResourceIds.length;
    return(m_soundResourceIds[currentSoundIndex]);
  }

  //-------------------------------------------------------------------------
  // retract
  //-------------------------------------------------------------------------
  void retract() {
    m_soundIndex = Math.max(m_soundIndex - 1, 0);
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // initSoundResourceIds
  //-------------------------------------------------------------------------
  private void initSoundResourceIds() {
    m_soundResourceIds = new int[]{
      R.raw.snd_win01_12,
      R.raw.snd_win02_12,
      R.raw.snd_win03_12,
      R.raw.snd_win04_12,
      R.raw.snd_win05_12,
      R.raw.snd_win06_12,
      R.raw.snd_win07_12,
      R.raw.snd_win08_12,
      R.raw.snd_win09_12,
      R.raw.snd_win10_12,
      R.raw.snd_win11_12,
      R.raw.snd_win12_12
    };
  }
}