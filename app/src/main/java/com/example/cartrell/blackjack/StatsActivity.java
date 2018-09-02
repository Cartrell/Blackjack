package com.example.cartrell.blackjack;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.cartrell.blackjack.databinding.ActivityStatsBinding;
import com.example.cartrell.blackjack.settings.Settings;
import com.example.cartrell.blackjack.settings.SettingsStorage;
import com.example.cartrell.blackjack.sound.SoundSystem;

public class StatsActivity extends AppCompatActivity {
  //=========================================================================
  // members
  //=========================================================================
  private Settings m_settings;
  private ActivityStatsBinding m_binding;
  private SoundSystem m_soundSystem;

  //=========================================================================
  // public
  //=========================================================================

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
    m_soundSystem = new SoundSystem(this);

    Intent intent = getIntent();
    m_settings = intent.getParcelableExtra(Settings.INTENT_KEY);
    initResetButton();
    initCloseButton();
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
  // protected
  //=========================================================================

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
        m_soundSystem.play(R.raw.snd_reset_settings);
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
    setTextView(m_binding.txtTotalGamesPlayed, m_settings.getTotalGamesPlayed());
    setTextView(m_binding.txtTotalGamesWon, m_settings.getTotalGamesWon());
    setTextView(m_binding.txtNumBj, m_settings.getNumBj());
    setTextView(m_binding.txtNumSplits, m_settings.getNumSplits());
    setTextView(m_binding.txtNumBlitzs, m_settings.getNumBlitzs());
    setTextView(m_binding.txtNumDoubles, m_settings.getNumDoubles());
    setTextView(m_binding.txtNumSurrenders, m_settings.getNumSurrenders());
  }

  //-------------------------------------------------------------------------
  // writeSettings
  //-------------------------------------------------------------------------
  public void writeSettings() {
    SettingsStorage storage = new SettingsStorage(this);
    storage.write(m_settings);
  }
}