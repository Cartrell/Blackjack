package com.example.cartrell.blackjack.engine;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

public class BjBetChip {
  //=========================================================================
  // members
  //=========================================================================
  private Context m_context;
  private ImageView m_image;
  private String m_id;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // getId
  //-------------------------------------------------------------------------
  public String getId() {
    return(m_id);
  }

  //-------------------------------------------------------------------------
  // getImage
  //-------------------------------------------------------------------------
  public ImageView getImage() {
    if (m_image == null) {
      initImage();
    }
    return(m_image);
  }

  //=========================================================================
  // package-private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  BjBetChip(Context context, String id) {
    m_context = context;
    m_id = id;
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // initImage
  //-------------------------------------------------------------------------
  private void initImage() {
    String name = "chip_" + m_id;
    int resourceId = m_context.getResources().getIdentifier(name, "drawable",
      m_context.getPackageName());
    if (resourceId == 0) {
      Log.w("BjBetChip", "initImage. No resource id for " + name);
      return;
    }

    m_image = new ImageView(m_context);

    //string id format: chip_[chip id][random]
    String idString = name + String.valueOf(Math.random());
    m_image.setId(idString.hashCode());
    m_image.setAdjustViewBounds(true);
    m_image.setImageResource(resourceId);
  }
}