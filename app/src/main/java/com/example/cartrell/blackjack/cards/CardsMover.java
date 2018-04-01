package com.example.cartrell.blackjack.cards;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;

public class CardsMover {
  //=========================================================================
  // const
  //=========================================================================

  //=========================================================================
  // members
  //=========================================================================
  private ICardsMoverCallbacks m_callbacks;
  private AnimatorSet m_animatorSet;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public CardsMover(ICardsMoverCallbacks callbacks) {
    m_callbacks = callbacks;
  }

  //-------------------------------------------------------------------------
  // getAnimatorSet
  //-------------------------------------------------------------------------
  public AnimatorSet getAnimatorSet() {
    if (m_animatorSet != null) {
      return(m_animatorSet);
    }

    final CardsMover cardsMover = this;

    m_animatorSet = new AnimatorSet();
    m_animatorSet.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        m_animatorSet.removeAllListeners();
        m_animatorSet = null;
        if (m_callbacks != null) {
          m_callbacks.cardsMoverOnComplete(cardsMover);
        }
      }
    });

    return(m_animatorSet);
  }
}
