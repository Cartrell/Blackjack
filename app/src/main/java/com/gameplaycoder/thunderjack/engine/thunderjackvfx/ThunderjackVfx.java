package com.gameplaycoder.thunderjack.engine.thunderjackvfx;

import com.gameplaycoder.thunderjack.engine.IBjEngine;
import com.gameplaycoder.thunderjack.players.PlayerIds;

public class ThunderjackVfx {
  //=========================================================================
  // members
  //=========================================================================
  private ThunderjackVfxFlash m_flash;
  private ThunderjackVfxBolt m_bolt;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public ThunderjackVfx(IBjEngine engine) {
    m_flash = new ThunderjackVfxFlash(engine);
    m_bolt = new ThunderjackVfxBolt(engine);
  }

  //-------------------------------------------------------------------------
  // begin
  //-------------------------------------------------------------------------
  public void begin(PlayerIds playerId) {
    m_flash.begin();
    m_bolt.begin(playerId);
  }
}
