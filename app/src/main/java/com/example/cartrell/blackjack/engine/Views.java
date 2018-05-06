package com.example.cartrell.blackjack.engine;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.example.cartrell.blackjack.R;
import com.example.cartrell.blackjack.utils.Metrics;

class Views {
  //=========================================================================
  // members
  //=========================================================================
  private AppCompatTextView m_txtCredits;
  private AppCompatTextView m_txtBet;
  private AppCompatTextView m_txtTotalWon;

  private AppCompatTextView m_txtLowerLeftBetValue;
  private AppCompatTextView m_txtLowerMidBetValue;
  private AppCompatTextView m_txtLowerRightBetValue;
  private AppCompatTextView m_txtMidLeftBetValue;
  private AppCompatTextView m_txtMidMidBetValue;
  private AppCompatTextView m_txtMidRightBetValue;

  private AppCompatTextView m_txtLowerLeftScore;
  private AppCompatTextView m_txtLowerMidScore;
  private AppCompatTextView m_txtLowerRightScore;
  private AppCompatTextView m_txtMidLeftScore;
  private AppCompatTextView m_txtMidMidScore;
  private AppCompatTextView m_txtMidRightScore;
  private AppCompatTextView m_txtUpperMidScore;

  private ImageView m_deckImage;

  private ImageView m_resultsLowerLeft;
  private ImageView m_resultsLowerMid;
  private ImageView m_resultsLowerRight;
  private ImageView m_resultsMidLeft;
  private ImageView m_resultsMidMid;
  private ImageView m_resultsMidRight;
  private ImageView m_resultsUpperMid;

  private ImageView m_turnPlayerLowerLeft;
  private ImageView m_turnPlayerLowerMid;
  private ImageView m_turnPlayerLowerRight;
  private ImageView m_turnPlayerMidLeft;
  private ImageView m_turnPlayerMidMid;
  private ImageView m_turnPlayerMidRight;

  private ToggleButton m_btnBetChipBlue;
  private ToggleButton m_btnBetChipRed;
  private ToggleButton m_btnBetChipPurple;
  private ToggleButton m_btnBetChipGreen;

  private ImageButton m_betButtonLeft;
  private ImageButton m_betButtonMid;
  private ImageButton m_betButtonRight;

  private ImageButton m_clearButton;
  private ImageButton m_dealButton;

  private ImageButton m_hitButton;
  private ImageButton m_standButton;
  private ImageButton m_doubleButton;
  private ImageButton m_splitButton;
  private ImageButton m_surrenderButton;

  private Point m_cardSize;
  private Point m_chipSize;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // getCardSize
  //-------------------------------------------------------------------------
  public Point getCardSize() {
    return(m_cardSize);
  }

  //-------------------------------------------------------------------------
  // getChipSize
  //-------------------------------------------------------------------------
  public Point getChipSize() {
    return(m_chipSize);
  }

  //-------------------------------------------------------------------------
  // getDeckImage
  //-------------------------------------------------------------------------
  public ImageView getDeckImage() {
    return(m_deckImage);
  }

  public AppCompatTextView getLowerLeftBetValueTextView() {
    return m_txtLowerLeftBetValue;
  }

  public AppCompatTextView getLowerMidBetValueTextView() {
    return m_txtLowerMidBetValue;
  }

  public AppCompatTextView getLowerRightBetValueTextView() {
    return m_txtLowerRightBetValue;
  }

  public AppCompatTextView getMidLeftBetValueTextView() {
    return m_txtMidLeftBetValue;
  }

  public AppCompatTextView getMidMidBetValueTextView() {
    return m_txtMidMidBetValue;
  }

  public AppCompatTextView getMidRightBetValueTextView() {
    return m_txtMidRightBetValue;
  }

  //-------------------------------------------------------------------------
  // getTextTotalWon
  //-------------------------------------------------------------------------
  public AppCompatTextView getTextTotalWon() {
    return(m_txtTotalWon);
  }

  //=========================================================================
  // package-private
  //=========================================================================
  
  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  Views(ViewGroup targetGroup, ViewGroup sourceGroup) {
    init(targetGroup, sourceGroup);
  }

  //-------------------------------------------------------------------------
  // setBetValue
  //-------------------------------------------------------------------------
  void setBetValue(int value) {
    setText(m_txtBet, "BET: " + String.valueOf(value));
  }

  //-------------------------------------------------------------------------
  // setCredits
  //-------------------------------------------------------------------------
  void setCredits(int value) {
    setText(m_txtCredits, "CREDITS: " + String.valueOf(value));
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // init
  //-------------------------------------------------------------------------
  private void init(ViewGroup targetGroup, ViewGroup sourceGroup) {
    initCardSize(sourceGroup);
    initChipSize(sourceGroup);

    m_txtCredits = initTextView(targetGroup, sourceGroup, R.id.txtCredits);
    m_txtBet = initTextView(targetGroup, sourceGroup, R.id.txtBet);
    m_txtTotalWon = initTextView(targetGroup, sourceGroup, R.id.txtTotalWon);
    
    m_deckImage = initImageView(targetGroup, sourceGroup, R.id.deckImage, R.id.guideDeckBottom,
      0, false);
    
    m_txtLowerLeftBetValue = initTextView(targetGroup, sourceGroup, R.id.txtLowerLeftBetValue);
    m_txtLowerMidBetValue = initTextView(targetGroup, sourceGroup, R.id.txtLowerMidBetValue);
    m_txtLowerRightBetValue = initTextView(targetGroup, sourceGroup, R.id.txtLowerRightBetValue);
    m_txtMidLeftBetValue = initTextView(targetGroup, sourceGroup, R.id.txtMidLeftBetValue);
    m_txtMidMidBetValue = initTextView(targetGroup, sourceGroup, R.id.txtMidMidBetValue);
    m_txtMidRightBetValue = initTextView(targetGroup, sourceGroup, R.id.txtMidRightBetValue);

    m_txtLowerLeftScore = initTextView(targetGroup, sourceGroup, R.id.txtLowerLeftScore);
    m_txtLowerMidScore = initTextView(targetGroup, sourceGroup, R.id.txtLowerMidScore);
    m_txtLowerRightScore = initTextView(targetGroup, sourceGroup, R.id.txtLowerRightScore);
    m_txtMidLeftScore = initTextView(targetGroup, sourceGroup, R.id.txtMidLeftScore);
    m_txtMidMidScore = initTextView(targetGroup, sourceGroup, R.id.txtMidMidScore);
    m_txtMidRightScore = initTextView(targetGroup, sourceGroup, R.id.txtMidRightScore);
    m_txtUpperMidScore = initTextView(targetGroup, sourceGroup, R.id.txtUpperMidScore);

    m_resultsLowerLeft = initImageView(targetGroup, sourceGroup, R.id.resultsLowerLeft,
      R.id.guideLeftCardsRight, R.id.guideLeftCardsLeft, true);
    m_resultsLowerMid = initImageView(targetGroup, sourceGroup, R.id.resultsLowerMid,
      R.id.guideMidCardsRight, R.id.guideMidCardsLeft, true);
    m_resultsLowerRight = initImageView(targetGroup, sourceGroup, R.id.resultsLowerRight,
      R.id.guideRightCardsRight, R.id.guideRightCardsLeft, true);

    m_resultsMidLeft = initImageView(targetGroup, sourceGroup, R.id.resultsMidLeft,
      R.id.guideMidCardsRight, R.id.guideMidCardsLeft, true);
    m_resultsMidMid = initImageView(targetGroup, sourceGroup, R.id.resultsMidMid,
      R.id.guideMidCardsRight, R.id.guideMidCardsLeft, true);
    m_resultsMidRight = initImageView(targetGroup, sourceGroup, R.id.resultsMidRight,
      R.id.guideRightCardsRight, R.id.guideRightCardsLeft, true);

    m_resultsUpperMid = initImageView(targetGroup, sourceGroup, R.id.resultsUpperMid,
      R.id.guideMidCardsRight, R.id.guideMidCardsLeft, true);

    m_betButtonLeft = initImageButton(targetGroup, sourceGroup, R.id.btnLeftPlayerBet,
      R.id.guideLeftCardsRight, R.id.guideLeftChipsRight, true);
    m_betButtonMid = initImageButton(targetGroup, sourceGroup, R.id.btnMidPlayerBet,
      R.id.guideMidCardsRight, R.id.guideMidChipsRight, true);
    m_betButtonRight = initImageButton(targetGroup, sourceGroup, R.id.btnRightPlayerBet,
      R.id.guideRightCardsRight, R.id.guideRightChipsRight, true);

    m_clearButton = initImageButton(targetGroup, sourceGroup, R.id.btnClear,
      R.id.parentBottom, R.id.guideBottomPanelBtm, false);
    m_dealButton = initImageButton(targetGroup, sourceGroup, R.id.btnDeal,
      R.id.parentBottom, R.id.guideBottomPanelBtm, false);

    m_btnBetChipGreen = initToggleButton(targetGroup, sourceGroup, R.id.chipButtonGreen,
      R.id.parentBottom, R.id.guideBottomPanelBtm, false);
    m_btnBetChipPurple = initToggleButton(targetGroup, sourceGroup, R.id.chipButtonPurple,
      R.id.parentBottom, R.id.guideBottomPanelBtm, false);
    m_btnBetChipRed = initToggleButton(targetGroup, sourceGroup, R.id.chipButtonRed,
      R.id.parentBottom, R.id.guideBottomPanelBtm, false);
    m_btnBetChipBlue = initToggleButton(targetGroup, sourceGroup, R.id.chipButtonBlue,
      R.id.parentBottom, R.id.guideBottomPanelBtm, false);

    m_hitButton = initImageButton(targetGroup, sourceGroup, R.id.btnHit,
      R.id.parentBottom, R.id.guideBottomPanelBtm, false);
    m_standButton = initImageButton(targetGroup, sourceGroup, R.id.btnStand,
      R.id.parentBottom, R.id.guideBottomPanelBtm, false);
    m_doubleButton = initImageButton(targetGroup, sourceGroup, R.id.btnDouble,
      R.id.parentBottom, R.id.guideBottomPanelBtm, false);
    m_splitButton = initImageButton(targetGroup, sourceGroup, R.id.btnSplit,
      R.id.parentBottom, R.id.guideBottomPanelBtm, false);
    m_surrenderButton = initImageButton(targetGroup, sourceGroup, R.id.btnSurrender,
      R.id.parentBottom, R.id.guideBottomPanelBtm, false);

    m_turnPlayerLowerLeft = initImageView(targetGroup, sourceGroup, R.id.turnPlayerLowerLeft,
      R.id.guideLowerCardsTop, R.id.guideMidCardsBottom, false);
    m_turnPlayerLowerMid = initImageView(targetGroup, sourceGroup, R.id.turnPlayerLowerMid,
      R.id.guideLowerCardsTop, R.id.guideMidCardsBottom, false);
    m_turnPlayerLowerRight = initImageView(targetGroup, sourceGroup, R.id.turnPlayerLowerRight,
      R.id.guideLowerCardsTop, R.id.guideMidCardsBottom, false);

    m_turnPlayerMidLeft = initImageView(targetGroup, sourceGroup, R.id.turnPlayerMidLeft,
      R.id.guideMidCardsTop, R.id.guideUpperCardsBottom, false);
    m_turnPlayerMidMid = initImageView(targetGroup, sourceGroup, R.id.turnPlayerMidMid,
      R.id.guideMidCardsTop, R.id.guideUpperCardsBottom, false);
    m_turnPlayerMidRight = initImageView(targetGroup, sourceGroup, R.id.turnPlayerMidRight,
      R.id.guideMidCardsTop, R.id.guideUpperCardsBottom, false);

  }

  //-------------------------------------------------------------------------
  // initCardSize
  //-------------------------------------------------------------------------
  private void initCardSize(ViewGroup sourceGroup) {
    Context context = sourceGroup.getContext();
    String imageKey = "card_as";

    ImageView image = new ImageView(context);
    image.setImageResource(context.getResources().getIdentifier(imageKey,"drawable",
      context.getPackageName()));
    image.setAdjustViewBounds(true);

    m_cardSize = Metrics.CalcSize(image,
      sourceGroup.findViewById(R.id.guideMidCardsBottom),
      sourceGroup.findViewById(R.id.guideMidCardsTop),
      false);
  }

  //-------------------------------------------------------------------------
  // initChipSize
  //-------------------------------------------------------------------------
  private void initChipSize(ViewGroup sourceGroup) {
    Context context = sourceGroup.getContext();
    String imageKey = "chip_blue";

    ImageView image = new ImageView(context);
    image.setImageResource(context.getResources().getIdentifier(imageKey,"drawable",
      context.getPackageName()));
    image.setAdjustViewBounds(true);

    m_chipSize = Metrics.CalcSize(image,
      sourceGroup.findViewById(R.id.guideMidCardsBottom),
      sourceGroup.findViewById(R.id.guideMidChipsTop),
      false);
  }

  //-------------------------------------------------------------------------
  // initImageButton
  //-------------------------------------------------------------------------
  private ImageButton initImageButton(ViewGroup targetGroup, ViewGroup sourceGroup,
  int sourceResourceId, int baseViewHighResourceId, int baseViewLowResourceId,
  boolean resizeByWidth) {
    Context context = sourceGroup.getContext();
    ImageButton targetButton = new ImageButton(context);
    ImageButton sourceButton = sourceGroup.findViewById(sourceResourceId);
    targetButton.setBackground(sourceButton.getBackground());

    View baseViewHigh = sourceGroup.findViewById(baseViewHighResourceId);
    View baseViewLow = sourceGroup.findViewById(baseViewLowResourceId);
    Point size = Metrics.CalcSize(sourceButton, baseViewHigh, baseViewLow, resizeByWidth);
    ViewGroup.LayoutParams targetParams = new ViewGroup.LayoutParams(size.x, size.y);

    targetButton.setId(sourceButton.getId());
    targetButton.setTag(sourceButton.getTag());
    targetButton.setX(sourceButton.getX());
    targetButton.setY(sourceButton.getY());
    targetGroup.addView(targetButton, targetParams);
    return(targetButton);
  }

  //-------------------------------------------------------------------------
  // initImageView
  //-------------------------------------------------------------------------
  private ImageView initImageView(ViewGroup targetGroup, ViewGroup sourceGroup, int sourceResourceId,
  int baseViewHighResourceId, int baseViewLowResourceId, boolean resizeByWidth) {
    ImageView sourceImageView = sourceGroup.findViewById(sourceResourceId);
    ImageView targetImageView = new ImageView(sourceGroup.getContext());
    targetImageView.setImageDrawable(sourceImageView.getDrawable());

    View baseViewHigh = sourceGroup.findViewById(baseViewHighResourceId);
    View baseViewLow = sourceGroup.findViewById(baseViewLowResourceId);
    Point size = Metrics.CalcSize(sourceImageView, baseViewHigh, baseViewLow, resizeByWidth);
    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(size.x, size.y);

    targetImageView.setTag(sourceImageView.getTag());
    targetImageView.setX(sourceImageView.getX());
    targetImageView.setY(sourceImageView.getY());
    targetGroup.addView(targetImageView, params);
    return(targetImageView);
  }

  //-------------------------------------------------------------------------
  // initTextView
  //-------------------------------------------------------------------------
  private AppCompatTextView initTextView(final ViewGroup targetGroup, ViewGroup sourceGroup,
  int sourceResourceId) {
    final AppCompatTextView targetTextView = new AppCompatTextView(sourceGroup.getContext());
    final AppCompatTextView sourceTextView = sourceGroup.findViewById(sourceResourceId);

    targetTextView.setTag(sourceTextView.getTag());
    targetTextView.setTextColor(sourceTextView.getTextColors());
    targetTextView.setTextSize(sourceTextView.getTextSize());
    targetTextView.setTypeface(sourceTextView.getTypeface());
    targetTextView.setText(sourceTextView.getText());
    targetTextView.setX(sourceTextView.getX());
    targetTextView.setY(sourceTextView.getY());

    //set auto-size properties
    TextViewCompat.setAutoSizeTextTypeWithDefaults(targetTextView,
      TextViewCompat.getAutoSizeTextType(sourceTextView));
    TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
      targetTextView,
      TextViewCompat.getAutoSizeMinTextSize(sourceTextView),
      TextViewCompat.getAutoSizeMaxTextSize(sourceTextView),
      TextViewCompat.getAutoSizeStepGranularity(sourceTextView),
      TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

    targetTextView.setGravity(sourceTextView.getGravity());

    if (sourceTextView.getBackground() instanceof ColorDrawable) {
      ColorDrawable colorDrawable = (ColorDrawable) sourceTextView.getBackground();
      targetTextView.setBackgroundColor(colorDrawable.getColor());
    } else {
      targetTextView.setBackground(sourceTextView.getBackground());
    }

    sourceTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        sourceTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        int width = sourceTextView.getWidth();
        int height = sourceTextView.getHeight();
        ConstraintLayout.LayoutParams targetParams = new ConstraintLayout.LayoutParams(width, height);
        targetTextView.setLayoutParams(targetParams);
      }
    });

    targetGroup.addView(targetTextView);
    return(targetTextView);
  }

  //-------------------------------------------------------------------------
  // initToggleButton
  //-------------------------------------------------------------------------
  private ToggleButton initToggleButton(ViewGroup targetGroup, ViewGroup sourceGroup,
  int sourceResourceId, int baseViewHighResourceId, int baseViewLowResourceId, boolean resizeByWidth) {
    Context context = sourceGroup.getContext();
    ToggleButton targetButton = new ToggleButton(context);
    ToggleButton sourceButton = sourceGroup.findViewById(sourceResourceId);
    targetButton.setBackground(sourceButton.getBackground());

    View baseViewHigh = sourceGroup.findViewById(baseViewHighResourceId);
    View baseViewLow = sourceGroup.findViewById(baseViewLowResourceId);
    Point size = Metrics.CalcSize(sourceButton, baseViewHigh, baseViewLow, resizeByWidth);
    ViewGroup.LayoutParams targetParams = new ViewGroup.LayoutParams(size.x, size.y);

    targetButton.setTag(sourceButton.getTag());
    targetButton.setTextOff(sourceButton.getTextOff());
    targetButton.setTextOn(sourceButton.getTextOn());
    targetButton.setText(sourceButton.getText());
    targetButton.setX(sourceButton.getX());
    targetButton.setY(sourceButton.getY());
    targetGroup.addView(targetButton, targetParams);
    return(targetButton);
  }

  //-------------------------------------------------------------------------
  // setText
  //-------------------------------------------------------------------------
  private void setText(AppCompatTextView textView, String text) {
    if (textView != null) {
      textView.setText(text);
    }
  }

  public AppCompatTextView getLowerLeftScoreTextView() {
    return m_txtLowerLeftScore;
  }

  public AppCompatTextView getLowerMidScoreTextView() {
    return m_txtLowerMidScore;
  }

  public AppCompatTextView getLowerRightScoreTextView() {
    return m_txtLowerRightScore;
  }

  public AppCompatTextView getMidLeftScoreTextView() {
    return m_txtMidLeftScore;
  }

  public AppCompatTextView getMidMidScoreTextView() {
    return m_txtMidMidScore;
  }

  public AppCompatTextView getMidRightScoreTextView() {
    return m_txtMidRightScore;
  }

  public AppCompatTextView getUpperMidScoreTextView() {
    return m_txtUpperMidScore;
  }

  public ImageView getResultsLowerLeft() {
    return m_resultsLowerLeft;
  }

  public ImageView getResultsLowerMid() {
    return m_resultsLowerMid;
  }

  public ImageView getResultsLowerRight() {
    return m_resultsLowerRight;
  }

  public ImageView getResultsMidLeft() {
    return m_resultsMidLeft;
  }

  public ImageView getResultsMidMid() {
    return m_resultsMidMid;
  }

  public ImageView getResultsMidRight() {
    return m_resultsMidRight;
  }

  public ImageView getResultsUpperMid() {
    return m_resultsUpperMid;
  }

  public ImageButton getBetButtonLeft() {
    return m_betButtonLeft;
  }

  public ImageButton getBetButtonMid() {
    return m_betButtonMid;
  }

  public ImageButton getBetButtonRight() {
    return m_betButtonRight;
  }

  public ToggleButton getBetChipBlue() {
    return m_btnBetChipBlue;
  }

  public ToggleButton getBetChipRed() {
    return m_btnBetChipRed;
  }

  public ToggleButton getBetChipPurple() {
    return m_btnBetChipPurple;
  }

  public ToggleButton getBetChipGreen() {
    return m_btnBetChipGreen;
  }

  public ImageButton getClearButton() {
    return m_clearButton;
  }

  public ImageButton getDealButton() {
    return m_dealButton;
  }

  public ImageButton getHitButton() {
    return m_hitButton;
  }

  public ImageButton getStandButton() {
    return m_standButton;
  }

  public ImageButton getDoubleButton() {
    return m_doubleButton;
  }

  public ImageButton getSplitButton() {
    return m_splitButton;
  }

  public ImageButton getSurrenderButton() {
    return m_surrenderButton;
  }

  public ImageView getTurnPlayerLowerLeft() {
    return m_turnPlayerLowerLeft;
  }

  public ImageView getTurnPlayerLowerMid() {
    return m_turnPlayerLowerMid;
  }

  public ImageView getTurnPlayerLowerRight() {
    return m_turnPlayerLowerRight;
  }

  public ImageView getTurnPlayerMidLeft() {
    return m_turnPlayerMidLeft;
  }

  public ImageView getTurnPlayerMidMid() {
    return m_turnPlayerMidMid;
  }

  public ImageView getTurnPlayerMidRight() {
    return m_turnPlayerMidRight;
  }
}
