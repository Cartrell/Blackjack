package com.gameplaycoder.thunderjack;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gameplaycoder.thunderjack.databinding.ActivityMainBinding;
import com.gameplaycoder.thunderjack.engine.BjEngine;
import com.gameplaycoder.thunderjack.utils.CreditsRenewalChecker;
import com.gameplaycoder.thunderjack.utils.cardsTweener.CardsTweener;

public class MainActivity extends AppCompatActivity {
  //=========================================================================
  // static / const
  //=========================================================================
  public static MainActivity sm_mainActivity;

  //=========================================================================
  // members
  //=========================================================================
  private ActivityMainBinding m_binding;
  private BjEngine m_engine;

  //=========================================================================
  // protected
  //=========================================================================

  //-------------------------------------------------------------------------
  // onActivityResult
  //-------------------------------------------------------------------------
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (m_engine != null) {
      m_engine.onActivityResult(requestCode, resultCode, data);
    }
  }

  //-------------------------------------------------------------------------
  // onCreate
  //-------------------------------------------------------------------------
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    m_binding = DataBindingUtil.setContentView(this,  R.layout.activity_main);
    sm_mainActivity = this;
    initEngine();
  }

  //-------------------------------------------------------------------------
  // onPause
  //-------------------------------------------------------------------------
  @Override
  protected void onPause() {
    super.onPause();
    if (m_engine != null) {
      m_engine.pause();
    }
  }

  //-------------------------------------------------------------------------
  // onResume
  //-------------------------------------------------------------------------
  @Override
  protected void onResume() {
    super.onResume();
    if (m_engine != null) {
      m_engine.resume();
    }
  }

  //-------------------------------------------------------------------------
  // onStop
  //-------------------------------------------------------------------------
  @Override
  protected void onStop() {
    super.onStop();
    if (m_engine != null) {
      m_engine.stop();
    }
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // initEngine
  //-------------------------------------------------------------------------
  private void initEngine() {
    m_engine = new BjEngine(this, m_binding);
  }

}