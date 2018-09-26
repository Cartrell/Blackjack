package com.gameplaycoder.thunderjack.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.gameplaycoder.thunderjack.MainActivity;

public class SettingsStorage {
  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public SettingsStorage() {
  }

  //-------------------------------------------------------------------------
  // read
  //-------------------------------------------------------------------------
  public void read(Settings targetSettings) {
    SharedPreferences sharedPreferences = MainActivity.sm_mainActivity.getPreferences(Context.MODE_PRIVATE);
    targetSettings.setCredits(sharedPreferences.getInt(SettingsKeys.CREDITS, 0));
    targetSettings.setIsSoundOn(sharedPreferences.getBoolean(SettingsKeys.IS_SOUND_ON, true));
    targetSettings.setTotalGamesPlayed(sharedPreferences.getInt(SettingsKeys.TOTAL_GAMES_PLAYED, 0));
    targetSettings.setTotalGamesWon(sharedPreferences.getInt(SettingsKeys.TOTAL_GAMES_WON, 0));
    targetSettings.setNumBj(sharedPreferences.getInt(SettingsKeys.NUM_BJS, 0));
    targetSettings.setNumSplits(sharedPreferences.getInt(SettingsKeys.NUM_SPLITS, 0));
    targetSettings.setNumSurrenders(sharedPreferences.getInt(SettingsKeys.NUM_SURRENDERS, 0));
    targetSettings.setNumBlitzs(sharedPreferences.getInt(SettingsKeys.NUM_BLITZS, 0));
    targetSettings.setNumThunderjacks(sharedPreferences.getInt(SettingsKeys.NUM_THUNDERJACKS, 0));
    targetSettings.appRanAtLeastOnce(sharedPreferences.getBoolean(SettingsKeys.HAS_APP_RUN_AT_LEAST_ONCE, false));
  }

  //-------------------------------------------------------------------------
  // write
  //-------------------------------------------------------------------------
  public void write(Settings sourceSettings) {
    SharedPreferences sharedPreferences = MainActivity.sm_mainActivity.getPreferences(Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putInt(SettingsKeys.CREDITS, sourceSettings.getCredits());
    editor.putBoolean(SettingsKeys.IS_SOUND_ON, sourceSettings.getIsSoundOn());
    editor.putInt(SettingsKeys.TOTAL_GAMES_PLAYED, sourceSettings.getTotalGamesPlayed());
    editor.putInt(SettingsKeys.TOTAL_GAMES_WON, sourceSettings.getTotalGamesWon());
    editor.putInt(SettingsKeys.NUM_BJS, sourceSettings.getNumBj());
    editor.putInt(SettingsKeys.NUM_SPLITS, sourceSettings.getNumSplits());
    editor.putInt(SettingsKeys.NUM_SURRENDERS, sourceSettings.getNumSurrenders());
    editor.putInt(SettingsKeys.NUM_BLITZS, sourceSettings.getNumBlitzs());
    editor.putInt(SettingsKeys.NUM_THUNDERJACKS, sourceSettings.getNumThunderjacks());
    editor.putBoolean(SettingsKeys.HAS_APP_RUN_AT_LEAST_ONCE, sourceSettings.hasAppRanAtLeastOnce());
    editor.apply();
  }
}