package com.gameplaycoder.thunderjack.placeBets;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.gameplaycoder.thunderjack.R;
import com.gameplaycoder.thunderjack.databinding.ActivityPlaceBetsBinding;
import com.gameplaycoder.thunderjack.engine.BjBetChipData;
import com.gameplaycoder.thunderjack.engine.BjBetChips;

public class PlaceBetsActivity extends AppCompatActivity {
  //=========================================================================
  // static / const
  //=========================================================================
  private static final String LOG_TAG = PlaceBetsActivity.class.getName();

  //=========================================================================
  // members
  //=========================================================================
  private BjBetChips m_betChips;

  private ActivityPlaceBetsBinding m_binding;

  private String m_selectedChipId;

  private int m_credits;
  private int m_leftPlayerBetValue;
  private int m_middlePlayerBetValue;
  private int m_rightPlayerBetValue;

  //=========================================================================
  // protected
  //=========================================================================

  //-------------------------------------------------------------------------
  // onCreate
  //-------------------------------------------------------------------------
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    m_binding = DataBindingUtil.setContentView(this,  R.layout.activity_place_bets);
    m_betChips = new BjBetChips(this);
    initData();
    initUi();
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // clearAllBets
  //-------------------------------------------------------------------------
  private void clearAllBets() {
    setLeftPlayerBetValue(0);
    setMiddlePlayerBetValue(0);
    setRightPlayerBetValue(0);
    updateTotalBetValue();
    updateOkButtonEnability();
  }

  //-------------------------------------------------------------------------
  // complete
  //-------------------------------------------------------------------------
  private void complete() {
    Intent intent = new Intent();
    intent.putExtra(PlaceBetsIntentKeys.LEFT_PLAYER_BET_VALUE, m_leftPlayerBetValue);
    intent.putExtra(PlaceBetsIntentKeys.MIDDLE_PLAYER_BET_VALUE, m_middlePlayerBetValue);
    intent.putExtra(PlaceBetsIntentKeys.RIGHT_PLAYER_BET_VALUE, m_rightPlayerBetValue);
    setResult(Activity.RESULT_OK, intent);
    finish();
  }

  //-------------------------------------------------------------------------
  // getTotalBetValue
  //-------------------------------------------------------------------------
  private int getTotalBetValue() {
    return(m_leftPlayerBetValue + m_middlePlayerBetValue + m_rightPlayerBetValue);
  }

  //-------------------------------------------------------------------------
  // initData
  //-------------------------------------------------------------------------
  private void initData() {
    Intent intent = getIntent();

    setCredits(intent.getIntExtra(PlaceBetsIntentKeys.CREDITS, 0));

    setLeftPlayerBetValue(intent.getIntExtra(PlaceBetsIntentKeys.LEFT_PLAYER_BET_VALUE, 0));
    setMiddlePlayerBetValue(intent.getIntExtra(PlaceBetsIntentKeys.MIDDLE_PLAYER_BET_VALUE, 0));
    setRightPlayerBetValue(intent.getIntExtra(PlaceBetsIntentKeys.RIGHT_PLAYER_BET_VALUE, 0));

    updateTotalBetValue();
  }

  //-------------------------------------------------------------------------
  // initUi
  //-------------------------------------------------------------------------
  private void initUi() {
    initUiOkButton();
    initUiClearButton();
    initUiBetButtons();
    initUiBetChipToggleButtons();
  }

  //-------------------------------------------------------------------------
  // initUiBetButtons
  //-------------------------------------------------------------------------
  private void initUiBetButtons() {
    View.OnClickListener listener = new View.OnClickListener() {

      //-------------------------------------------------------------------------
      // onClick
      //-------------------------------------------------------------------------
      @Override
      public void onClick(View view) {
        int id = view.getId();
        switch (id) {
          case R.id.btnBetLeft:
            placeLeftBet();
            break;

          case R.id.btnBetMiddle:
            placeMiddleBet();
            break;

          case R.id.btnBetRight:
            placeRightBet();
            break;
        }
      }
    };

    m_binding.btnBetLeft.setOnClickListener(listener);
    m_binding.btnBetMiddle.setOnClickListener(listener);
    m_binding.btnBetRight.setOnClickListener(listener);
  }

  //-------------------------------------------------------------------------
  // initUiBetChipToggleButtons
  //-------------------------------------------------------------------------
  private void initUiBetChipToggleButtons() {
    View.OnClickListener listener = new View.OnClickListener() {

      //-------------------------------------------------------------------------
      // onClick
      //-------------------------------------------------------------------------
      @Override
      public void onClick(View view) {
        String chipId = (String) view.getTag();
        BjBetChipData betChipData = m_betChips.getData(chipId);
        if (betChipData == null) {
          Log.w(LOG_TAG, "initUiBetChipToggleButtons.onClick. Warning: Invalid chip id: " + chipId);
          return;
        }

        unselectAllBetChips();
        selectBetChip(chipId);
      }
    };

    m_binding.chipButtonBlue.setOnClickListener(listener);
    m_binding.chipButtonGreen.setOnClickListener(listener);
    m_binding.chipButtonPurple.setOnClickListener(listener);
    m_binding.chipButtonRed.setOnClickListener(listener);
  }

  //-------------------------------------------------------------------------
  // initUiClearButton
  //-------------------------------------------------------------------------
  private void initUiClearButton() {
    m_binding.btnClear.setOnClickListener(new View.OnClickListener() {
      //-------------------------------------------------------------------------
      // onClick
      //-------------------------------------------------------------------------
      @Override
      public void onClick(View v) {
        clearAllBets();
      }
    });
  }

  //-------------------------------------------------------------------------
  // initUiOkButton
  //-------------------------------------------------------------------------
  private void initUiOkButton() {
    m_binding.btnOk.setOnClickListener(new View.OnClickListener() {
      //-------------------------------------------------------------------------
      // onClick
      //-------------------------------------------------------------------------
      @Override
      public void onClick(View v) {
        complete();
      }
    });
  }

  //-------------------------------------------------------------------------
  // placeLeftBet
  //-------------------------------------------------------------------------
  private void placeLeftBet() {
    BjBetChipData betChipData = m_betChips.getData(m_selectedChipId);
    if (betChipData == null) {
      Log.w(LOG_TAG, "placeLeftBet. Invalid chip id: " + m_selectedChipId);
      return;
    }

    int betChipValue = betChipData.getValue();
    setLeftPlayerBetValue(m_leftPlayerBetValue + betChipValue);
    updateTotalBetValue();
    updateOkButtonEnability();
  }

  //-------------------------------------------------------------------------
  // placeMiddleBet
  //-------------------------------------------------------------------------
  private void placeMiddleBet() {
    BjBetChipData betChipData = m_betChips.getData(m_selectedChipId);
    if (betChipData == null) {
      Log.w(LOG_TAG, "placeMiddleBet. Invalid chip id: " + m_selectedChipId);
      return;
    }

    int betChipValue = betChipData.getValue();
    setMiddlePlayerBetValue(m_middlePlayerBetValue + betChipValue);
    updateTotalBetValue();
    updateOkButtonEnability();
  }

  //-------------------------------------------------------------------------
  // placeRightBet
  //-------------------------------------------------------------------------
  private void placeRightBet() {
    BjBetChipData betChipData = m_betChips.getData(m_selectedChipId);
    if (betChipData == null) {
      Log.w(LOG_TAG, "placeRightBet. Invalid chip id: " + m_selectedChipId);
      return;
    }

    int betChipValue = betChipData.getValue();
    setRightPlayerBetValue(m_rightPlayerBetValue + betChipValue);
    updateTotalBetValue();
    updateOkButtonEnability();
  }

  //-------------------------------------------------------------------------
  // selectBetChip
  //-------------------------------------------------------------------------
  private void selectBetChip(String chipId) {
    ToggleButton button = m_binding.getRoot().findViewWithTag(chipId);
    if (button == null) {
      return;
    }

    m_selectedChipId = chipId;
    button.setEnabled(false);
    button.setChecked(true);
  }

  //-------------------------------------------------------------------------
  // setCredits
  //-------------------------------------------------------------------------
  private void setCredits(int value) {
    m_credits = value;
    updateTextView(m_binding.txtCreditsValue, m_credits);
  }

  //-------------------------------------------------------------------------
  // setLeftPlayerBetValue
  //-------------------------------------------------------------------------
  private void setLeftPlayerBetValue(int value) {
    m_leftPlayerBetValue = value;
    updateTextView(m_binding.txtBetValueLeft, m_leftPlayerBetValue);
  }

  //-------------------------------------------------------------------------
  // setMiddlePlayerBetValue
  //-------------------------------------------------------------------------
  private void setMiddlePlayerBetValue(int value) {
    m_middlePlayerBetValue = value;
    updateTextView(m_binding.txtBetValueMiddle, m_middlePlayerBetValue);
  }

  //-------------------------------------------------------------------------
  // setRightPlayerBetValue
  //-------------------------------------------------------------------------
  private void setRightPlayerBetValue(int value) {
    m_rightPlayerBetValue = value;
    updateTextView(m_binding.txtBetValueRight, m_rightPlayerBetValue);
  }

  //-------------------------------------------------------------------------
  // unselectAllBetChips
  //-------------------------------------------------------------------------
  private void unselectAllBetChips() {
    unselectBetChip(m_binding.chipButtonBlue);
    unselectBetChip(m_binding.chipButtonGreen);
    unselectBetChip(m_binding.chipButtonPurple);
    unselectBetChip(m_binding.chipButtonRed);
    m_selectedChipId = null;
  }

  //-------------------------------------------------------------------------
  // unselectBetChip
  //-------------------------------------------------------------------------
  private void unselectBetChip(ToggleButton button) {
    if (button != null) {
      button.setChecked(false);
      button.setEnabled(true);
    }
  }

  //-------------------------------------------------------------------------
  // updateOkButtonEnability
  //-------------------------------------------------------------------------
  private void updateOkButtonEnability() {
    int totalBetValue = getTotalBetValue();
    boolean hasPlacedAtLeastOneBet = totalBetValue > 0;
    boolean canAffordBets = m_credits >= totalBetValue;
    m_binding.btnOk.setEnabled(hasPlacedAtLeastOneBet && canAffordBets);
  }

  //-------------------------------------------------------------------------
  // updateTotalBetValue
  //-------------------------------------------------------------------------
  private void updateTotalBetValue() {
    int totalBetValue = getTotalBetValue();
    updateTextView(m_binding.txtBetValue, totalBetValue);
  }

  //-------------------------------------------------------------------------
  // updateTextView
  //-------------------------------------------------------------------------
  private void updateTextView(TextView textView, int value) {
    textView.setText(String.valueOf(value));
  }
}