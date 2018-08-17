package com.example.cartrell.blackjack;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.cartrell.blackjack.databinding.ActivityMainBinding;
import com.example.cartrell.blackjack.engine.BjEngine;

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
  // public
  //=========================================================================

  //=========================================================================
  // protected
  //=========================================================================

  //-------------------------------------------------------------------------
  // initEngine
  //-------------------------------------------------------------------------
  private void initEngine() {
    m_engine = new BjEngine(this, m_binding);
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
  // onResume
  //-------------------------------------------------------------------------
  @Override
  protected void onResume() {
    super.onResume();
    m_engine.onResume();
  }
}
