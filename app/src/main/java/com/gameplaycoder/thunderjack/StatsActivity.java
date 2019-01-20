package com.gameplaycoder.thunderjack;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.gameplaycoder.thunderjack.databinding.ActivityStatsBinding;
import com.gameplaycoder.thunderjack.settings.Settings;
import com.gameplaycoder.thunderjack.settings.SettingsStorage;
import com.gameplaycoder.thunderjack.sound.SoundChannel;
import com.gameplaycoder.thunderjack.sound.SoundSystem;

public class StatsActivity extends AppCompatActivity {
  //=========================================================================
  // members
  //=========================================================================
  private Settings m_settings;
  private ActivityStatsBinding m_binding;
  private SoundSystem m_soundSystem;
  private SoundChannel m_sndChReset;

  //=========================================================================
  // protected
  //=========================================================================

  //-------------------------------------------------------------------------
  // onCreate
  //-------------------------------------------------------------------------
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    m_binding = DataBindingUtil.setContentView(this,  R.layout.activity_stats);
    m_soundSystem = new SoundSystem(this,
      getApplicationContext().getResources().getInteger(R.integer.maxSoundPoolStreams));

    Intent intent = getIntent();
    m_settings = intent.getParcelableExtra(Settings.INTENT_KEY);
    initResetButton();
    initCloseButton();
    initAboutButton();
    updateUi();
  }

  //-------------------------------------------------------------------------
  // onDestroy
  //-------------------------------------------------------------------------
  @Override
  protected void onDestroy() {
    super.onDestroy();
    m_soundSystem.uninit();
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // initAboutButton
  //-------------------------------------------------------------------------
  private void initAboutButton() {
    m_binding.btnAbout.setOnClickListener(new View.OnClickListener() {
      //-------------------------------------------------------------------------
      // onClick
      //-------------------------------------------------------------------------
      @Override
      public void onClick(View v) {
        openAboutActivity();
      }
    });
  }

  //-------------------------------------------------------------------------
  // initCloseButton
  //-------------------------------------------------------------------------
  private void initCloseButton() {
    m_binding.btnClose.setOnClickListener(new View.OnClickListener() {
      //-------------------------------------------------------------------------
      // onClick
      //-------------------------------------------------------------------------
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  //-------------------------------------------------------------------------
  // initResetButton
  //-------------------------------------------------------------------------
  private void initResetButton() {
    m_binding.btnReset.setOnClickListener(new View.OnClickListener() {
      //-------------------------------------------------------------------------
      // onClick
      //-------------------------------------------------------------------------
      @Override
      public void onClick(View v) {
        showResetConfirmationDialog();
      }
    });
  }

  //-------------------------------------------------------------------------
  // openAboutActivity
  //-------------------------------------------------------------------------
  private void openAboutActivity() {
    Intent intent = new Intent(this, AboutActivity.class);
    startActivity(intent);
  }

  //-------------------------------------------------------------------------
  // resetSettings
  //-------------------------------------------------------------------------
  private void resetSettings() {
    m_settings.reset();
    writeSettings();
    updateUi();
  }

  //-------------------------------------------------------------------------
  // setTextView
  //-------------------------------------------------------------------------
  private void setTextView(TextView textView, int value) {
    textView.setText(String.valueOf(value));
  }

  //------------------------------------------------------------------------------
  // showResetConfirmationDialog
  //------------------------------------------------------------------------------
  private void showResetConfirmationDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(R.string.ask_reset_stats);

    builder.setPositiveButton(R.string.yeah_do_it, new DialogInterface.OnClickListener() {

      //--------------------------------------------------------------------------
      // onClick
      //--------------------------------------------------------------------------
      public void onClick(DialogInterface dialog, int id) {
        resetSettings();
        m_sndChReset = m_soundSystem.playSound(m_sndChReset, R.raw.snd_reset_settings, 1, true);
      }
    });

    builder.setNegativeButton(R.string.no_way_man, new DialogInterface.OnClickListener() {

      //--------------------------------------------------------------------------
      // onClick
      //--------------------------------------------------------------------------
      public void onClick(DialogInterface dialog, int id) {
        if (dialog != null) {
          dialog.dismiss();
        }
      }
    });

    AlertDialog alertDialog = builder.create();
    alertDialog.show();
  }

  //-------------------------------------------------------------------------
  // updateUi
  //-------------------------------------------------------------------------
  private void updateUi() {
    setTextView(m_binding.txtStatsTotalGamesPlayedValue, m_settings.getTotalGamesPlayed());
    setTextView(m_binding.txtStatsTotalGamesWonValue, m_settings.getTotalGamesWon());
    setTextView(m_binding.txtStatsNumOfBjsValue, m_settings.getNumBj());
    setTextView(m_binding.txtStatsNumOfSplitsValue, m_settings.getNumSplits());
    setTextView(m_binding.txtStatsNumOfBlitzsValue, m_settings.getNumBlitzs());
    setTextView(m_binding.txtStatsNumOfDoublesValue, m_settings.getNumDoubles());
    setTextView(m_binding.txtStatsNumOfSurrendersValue, m_settings.getNumSurrenders());
    setTextView(m_binding.txtStatsNumOfTjsValue, m_settings.getNumThunderjacks());
  }

  //-------------------------------------------------------------------------
  // writeSettings
  //-------------------------------------------------------------------------
  public void writeSettings() {
    SettingsStorage storage = new SettingsStorage();
    storage.write(m_settings);
  }
}