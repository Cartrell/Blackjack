package com.gameplaycoder.thunderjack.engine;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BjBetChips {
  //=========================================================================
  // const
  //=========================================================================
  private static final String LOG_TAG = "BjBetChips";

  //=========================================================================
  // members
  //=========================================================================
  private HashMap<String, BjBetChipData> m_betChipData;
  private List<String> m_chipIds;
  private Context m_context;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public BjBetChips(Context context) {
    m_context = context;
    initBetChips();
  }

  //-------------------------------------------------------------------------
  // getChipIdsFor
  //-------------------------------------------------------------------------
  public ArrayList<String> getChipIdsFor(int betValue) {
    ArrayList<String> betChipIds = new ArrayList<>();

    while (betValue > 0) {
      String highestId = getHighestIdUnderValue(betValue);
      betChipIds.add(highestId);
      BjBetChipData betChipData = m_betChipData.get(highestId);
      betValue -= betChipData.getValue();
    }

    return(betChipIds);
  }

  //-------------------------------------------------------------------------
  // getChipIds
  //-------------------------------------------------------------------------
  public List<String> getChipIds() {
    return(m_chipIds);
  }

  //-------------------------------------------------------------------------
  // getData
  //-------------------------------------------------------------------------
  public BjBetChipData getData(String betChipId) {
    return(m_betChipData.get(betChipId));
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // getHighestIdUnderValue
  //-------------------------------------------------------------------------
  private String getHighestIdUnderValue(int betValue) {
    BjBetChipData highestBetChipData = null;

    Iterator iterator = m_betChipData.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry)iterator.next();
      BjBetChipData betChipData = (BjBetChipData)entry.getValue();

      if (betChipData.getValue() <= betValue) {
        if (highestBetChipData == null) {
          highestBetChipData = betChipData;
        } else if (betChipData.getValue() > highestBetChipData.getValue()) {
          highestBetChipData = betChipData;
        }
      }
    }

    return(highestBetChipData != null ? highestBetChipData.getId() : null);
  }

  //-------------------------------------------------------------------------
  // initBetChips
  //-------------------------------------------------------------------------
  private void initBetChips() {
    m_betChipData = new HashMap<>();

    AssetManager manager = m_context.getResources().getAssets();

    try {
      InputStream stream = manager.open("betChips.json");
      JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));

      reader.beginArray();
      while (reader.hasNext()) {
        readBetChipData(reader);
      }
      reader.endArray();

    } catch (IOException exception) {
      Log.e(LOG_TAG, "initBetChips. error: " + exception);
    }

    int numKeys = m_betChipData.keySet().size();
    String[] buffer = new String[numKeys];
    buffer = m_betChipData.keySet().toArray(buffer);

    m_chipIds = Collections.unmodifiableList(Arrays.asList(buffer));
  }

  //-------------------------------------------------------------------------
  // readBetChipData
  //-------------------------------------------------------------------------
  private void readBetChipData(JsonReader reader) throws IOException {
    int value = 0;
    String id = null;
    String drawableId = null;

    reader.beginObject();
    while (reader.hasNext()) {
      String name = reader.nextName();
      if (name.equals("id")) {
        id = reader.nextString();
      } else if (name.equals("value")) {
        value = reader.nextInt();
      } else if (name.equals("drawableId")) {
        drawableId = reader.nextString();
      } else {
        reader.skipValue();
      }
    }
    reader.endObject();

    if (id != null && drawableId != null) {
      m_betChipData.put(id, new BjBetChipData(id, value, drawableId));
    }
  }
}