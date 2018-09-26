package com.gameplaycoder.thunderjack.players;

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
  private float m_xLeft;
  private float m_xRight;
  private float m_yTop;
  private int m_maxViews;
  private int m_viewWidth;

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  ViewDistributor(float xLeft, float xRight, float yTop, int maxViews, int viewWidth) {
    m_views = new ArrayList<>();
    m_xLeft = xLeft;
    m_xRight = xRight;
    m_yTop = yTop;
    m_maxViews = maxViews;
    m_viewWidth = viewWidth;
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
    float xRange = m_xRight - m_xLeft;

    int currNumViews = m_views.size();
    int maxViews = currNumViews <= m_maxViews ? m_maxViews : currNumViews;

    return(xRange / (maxViews - 1));
  }

  //-------------------------------------------------------------------------
  // calculateViewsWidthRange
  //-------------------------------------------------------------------------
  private float calculateViewsWidthRange() {
    return(m_xRight - m_xLeft + m_viewWidth);
  }

  //-------------------------------------------------------------------------
  // calculateTotalViewsWidth
  //-------------------------------------------------------------------------
  private float calculateTotalViewsWidth() {
    float xOffset = calculateViewsOffset();
    return(m_viewWidth + xOffset * (m_views.size() - 1));
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
        x = m_xLeft + (range - totalViewsWidth) / 2;
      } else {
        x += xOffset;
      }

      m_viewPositions.put(view, new PointF(x, m_yTop));
    }
  }
}
