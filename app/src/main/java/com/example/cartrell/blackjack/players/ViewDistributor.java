package com.example.cartrell.blackjack.players;

import android.graphics.PointF;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

class ViewDistributor {
  //=========================================================================
  // const
  //=========================================================================

  //=========================================================================
  // members
  //=========================================================================
  private ArrayList<View> m_views;
  private HashMap<View, PointF>m_viewPositions;
  private View m_leftView;
  private View m_rightView;
  private float m_yTop;
  private int m_maxViews;

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  ViewDistributor(View leftView, View rightView, float yTop, int maxViews) {
    m_views = new ArrayList<>();
    m_leftView = leftView;
    m_rightView = rightView;
    m_yTop = yTop;
    m_maxViews = maxViews;
    m_viewPositions = new HashMap<>();
  }

  //-------------------------------------------------------------------------
  // add
  //-------------------------------------------------------------------------
  void add(View view) {
    if (view == null || m_views.contains(view)) {
      return; //sanity checks
    }

    m_views.add(view);
    calculateViewPositions();
  }

  //-------------------------------------------------------------------------
  // getPositions
  //-------------------------------------------------------------------------
  HashMap<View, PointF> getPositions() {
    return(m_viewPositions);
  }

  //-------------------------------------------------------------------------
  // remove
  //-------------------------------------------------------------------------
  void remove(View view) {
    if (view == null || !m_views.contains(view)) {
      return; //sanity checks
    }

    m_views.remove(view);
    calculateViewPositions();
  }

  //-------------------------------------------------------------------------
  // removeAll
  //-------------------------------------------------------------------------
  void removeAll() {
    m_views.clear();
    calculateViewPositions();
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // calculateViewsOffset
  //-------------------------------------------------------------------------
  private float calculateViewsOffset() {
    float xLeft = m_leftView.getX();
    float xRight = m_rightView.getX();
    float xRange = xRight - xLeft;

    int currNumViews = m_views.size();
    int maxViews = currNumViews <= m_maxViews ? m_maxViews : currNumViews;

    return(xRange / (maxViews - 1));
  }

  //-------------------------------------------------------------------------
  // calculateViewsWidthRange
  //-------------------------------------------------------------------------
  private float calculateViewsWidthRange() {
    float xLeft = m_leftView.getX();
    float xRight = m_rightView.getX();
    return(xRight - xLeft + m_rightView.getWidth());
  }

  //-------------------------------------------------------------------------
  // calculateTotalViewsWidth
  //-------------------------------------------------------------------------
  private float calculateTotalViewsWidth() {
    float xOffset = calculateViewsOffset();
    return(m_leftView.getWidth() + xOffset * (m_views.size() -1));
  }

  //-------------------------------------------------------------------------
  // calculateViewPositions
  //-------------------------------------------------------------------------
  private void calculateViewPositions() {
    float x = 0;
    float xOffset = calculateViewsOffset();
    boolean isFirstViewSet = false;

    m_viewPositions.clear();

    for (int index = 0; index < m_views.size(); index++) {
      View view = m_views.get(index);
      if (!isFirstViewSet) {
        isFirstViewSet = true;
        float totalViewsWidth = calculateTotalViewsWidth();
        float range = calculateViewsWidthRange();
        x = m_leftView.getX() + (range - totalViewsWidth) / 2;
      } else {
        x += xOffset;
      }

      m_viewPositions.put(view, new PointF(x, m_yTop));
    }
  }
}
