package com.example.cartrell.blackjack;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.cartrell.blackjack.databinding.ActivityMainBinding;
import com.example.cartrell.blackjack.engine.BjEngine;
import com.example.cartrell.blackjack.players.PlayerIds;

public class MainActivity extends AppCompatActivity {
  private ActivityMainBinding m_binding;
  private BjEngine m_engine;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // onBetChipClick
  //-------------------------------------------------------------------------
  /*public void onBetChipClick(View view) {
    m_engine.onBetChipClick(view);
  }*/

  //-------------------------------------------------------------------------
  // onClearClick
  //-------------------------------------------------------------------------
  /*public void onClearClick(View view) {
    m_engine.onClearClick();
  }*/

  //-------------------------------------------------------------------------
  // onDealClick
  //-------------------------------------------------------------------------
  /*public void onDealClick(View view) {
    m_engine.onDealClick();
  }*/

  //-------------------------------------------------------------------------
  // beginDouble
  //-------------------------------------------------------------------------
  /*public void onDoubleClick(View view) {
    m_engine.onDoubleClick();
  }*/

  //-------------------------------------------------------------------------
  // beginHit
  //-------------------------------------------------------------------------
  /*public void onHitClick(View view) {
    m_engine.onHitClick();
  }*/

  //-------------------------------------------------------------------------
  // onPlayerLeftPlaceBetClick
  //-------------------------------------------------------------------------
  public void onPlayerLeftPlaceBetClick(View view) {
    m_engine.placeBet(PlayerIds.LEFT_BOTTOM);
  }

  //-------------------------------------------------------------------------
  // onPlayerMidPlaceBetClick
  //-------------------------------------------------------------------------
  public void onPlayerMidPlaceBetClick(View view) {
    m_engine.placeBet(PlayerIds.MIDDLE_BOTTOM);
  }

  //-------------------------------------------------------------------------
  // onPlayerRightPlaceBetClick
  //-------------------------------------------------------------------------
  public void onPlayerRightPlaceBetClick(View view) {
    m_engine.placeBet(PlayerIds.RIGHT_BOTTOM);
  }

  //-------------------------------------------------------------------------
  // beginSplit
  //-------------------------------------------------------------------------
  public void onSplitClick(View view) {
    //m_engine.onSplitClick();
  }

  //-------------------------------------------------------------------------
  // onStandClick
  //-------------------------------------------------------------------------
  /*public void onStandClick(View view) {
    m_engine.onStandClick();
  }*/

  //-------------------------------------------------------------------------
  // onSurrenderClick
  //-------------------------------------------------------------------------
  /*public void onSurrenderClick(View view) {
    m_engine.onSurrenderClick();
  }*/

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
    initEngine();
  }
}
