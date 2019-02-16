package com.gameplaycoder.thunderjack.utils.cardsTweener;

import android.util.Log;
import android.view.Choreographer;
import android.view.View;

import java.util.ArrayList;

public class CardsTweener {
  //=========================================================================
  // static / const
  //=========================================================================
  private static final float NS_TO_MS = 1f / 1000000f;
  private static final long FRAME_DELAY_MS = 25;

  //=========================================================================
  // members
  //=========================================================================
  private ArrayList<CardsTweenerUnit> m_units;
  private OnCompletedListener m_onCompletedListener;
  private OnUnitStartedListener m_onUnitStartedListener;
  private Choreographer m_choreographer;
  private Choreographer.FrameCallback m_chorCallback;
  private long m_lastFrameTimeMs;
  private long m_elapsedTimeSinceLastFrameMs;
  private long m_elapsedTimeSinceStart;
  private long m_startTime;
  private boolean m_firstFrameSinceStart;
  private boolean m_isStopRequested;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public CardsTweener() {
    m_units = new ArrayList<>();
    initChorCallback();
  }

  //-------------------------------------------------------------------------
  // add
  //-------------------------------------------------------------------------
  public void add(View cardImage, float xTarget, float yTarget, float alphaTarget, float durationMs,
  float startDelayMs) {
    if (cardImage == null) {
      return;
    }

    CardsTweenerUnit unit = new CardsTweenerUnit(cardImage, xTarget, yTarget, alphaTarget,
      durationMs, startDelayMs);
    m_units.add(unit);
  }

  //-------------------------------------------------------------------------
  // addAlpha
  //-------------------------------------------------------------------------
  public void addAlpha(View cardImage, float alphaTarget, float durationMs, float startDelayMs) {
    if (cardImage == null) {
      return;
    }

    CardsTweenerUnit unit = new CardsTweenerUnit(cardImage, Float.NaN, Float.NaN, alphaTarget,
      durationMs, startDelayMs);
    m_units.add(unit);
  }

  //-------------------------------------------------------------------------
  // addPosition
  //-------------------------------------------------------------------------
  public void addPosition(View cardImage, float xTarget, float yTarget, float durationMs,
  float startDelayMs) {
    if (cardImage == null) {
      return;
    }

    CardsTweenerUnit unit = new CardsTweenerUnit(cardImage, xTarget, yTarget, Float.NaN,
      durationMs, startDelayMs);
    m_units.add(unit);
  }

  //-------------------------------------------------------------------------
  // play
  //-------------------------------------------------------------------------
  public boolean play() {
    try {
      m_choreographer = Choreographer.getInstance();
    } catch (IllegalStateException exception) {
      Log.e(CardsTweener.class.getName(), "play(). Error: Thread does not have a looper.");
      return(false);
    }

    m_firstFrameSinceStart = true;
    m_isStopRequested = false;
    m_choreographer.postFrameCallbackDelayed(m_chorCallback, 50);
    return(true);
  }

  //-------------------------------------------------------------------------
  // setOnCompletedListener
  //-------------------------------------------------------------------------
  public void setOnCompletedListener(OnCompletedListener value) {
    m_onCompletedListener = value;
  }

  //-------------------------------------------------------------------------
  // setOnUnitStartedListener
  //-------------------------------------------------------------------------
  public void setOnUnitStartedListener(OnUnitStartedListener value) {
    m_onUnitStartedListener = value;
  }

  //=========================================================================
  // callbacks
  //=========================================================================
  public interface OnCompletedListener {
    void onCompleted(CardsTweener cardsTweener);
  }

  public interface OnUnitStartedListener {
    void onStarted(CardsTweener cardsTweener, View cardImage);
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // initChorCallback
  //-------------------------------------------------------------------------
  private void initChorCallback() {
    m_chorCallback = new Choreographer.FrameCallback() {
      //-------------------------------------------------------------------------
      // doFrame
      //-------------------------------------------------------------------------
      @Override
      public void doFrame(long frameTimeNanos) {
        long currentFrameTimeMs = (long)(frameTimeNanos * NS_TO_MS);

        if (m_firstFrameSinceStart) {
          m_firstFrameSinceStart = false;
          m_startTime = currentFrameTimeMs;
          m_elapsedTimeSinceLastFrameMs = m_lastFrameTimeMs = currentFrameTimeMs;
        }

        m_elapsedTimeSinceStart = currentFrameTimeMs - m_startTime;
        m_elapsedTimeSinceLastFrameMs = currentFrameTimeMs - m_lastFrameTimeMs;
        m_lastFrameTimeMs = currentFrameTimeMs;

        boolean shouldNotifyComplete = false;

        if (m_elapsedTimeSinceLastFrameMs > 0) {
          updateUnits();

          if (isEmpty()) {
            m_isStopRequested = true;
            shouldNotifyComplete = true;
          }
        }

        if (!m_isStopRequested) {
          m_choreographer.postFrameCallbackDelayed(m_chorCallback, FRAME_DELAY_MS);
        } else if (shouldNotifyComplete) {
          notifyCompleted();
        }
      }
    };
  }

  //-------------------------------------------------------------------------
  // isEmpty
  //-------------------------------------------------------------------------
  private boolean isEmpty() {
    return(m_units.size() == 0);
  }

  //-------------------------------------------------------------------------
  // notifyCompleted
  //-------------------------------------------------------------------------
  private void notifyCompleted() {
    if (m_onCompletedListener != null) {
      m_onCompletedListener.onCompleted(this);
    }
  }

  //-------------------------------------------------------------------------
  // notifyUnitStarted
  //-------------------------------------------------------------------------
  private void notifyUnitStarted(CardsTweenerUnit unit) {
    if (m_onUnitStartedListener != null) {
      m_onUnitStartedListener.onStarted(this, unit.getCardImage());
    }
  }

  //-------------------------------------------------------------------------
  // updateUnit
  //-------------------------------------------------------------------------
  private boolean updateUnit(CardsTweenerUnit unit) {
    float startDelay = unit.getStartDelay();
    if (m_elapsedTimeSinceStart < startDelay) {
      //too early for this unit to start
      return(false);
    }

    float duration = unit.getDuration();
    if (duration <= 0) {
      //unity finishes as soon as it starts (and to prevent divide by zero in most cases)
      unit.update(1.0f);
      return(true);
    }

    float startTimeOfThisUnit = m_elapsedTimeSinceStart - startDelay;
    float progress = startTimeOfThisUnit / duration;

    boolean isFirstUpdate = unit.update(progress);
    if (isFirstUpdate) {
      notifyUnitStarted(unit);
    }

    return(progress >= 1.0f);
  }

  //-------------------------------------------------------------------------
  // updateUnits
  //-------------------------------------------------------------------------
  private void updateUnits() {
    int length = m_units.size();
    int index = 0;
    while (index < length) {
      CardsTweenerUnit unit = m_units.get(index);
      boolean isUnitComplete = updateUnit(unit);
      if (isUnitComplete) {
        m_units.remove(index);
        length--;
      } else {
        index++;
      }
    }
  }
}