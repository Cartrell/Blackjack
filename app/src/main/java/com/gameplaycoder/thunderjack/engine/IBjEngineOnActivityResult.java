package com.gameplaycoder.thunderjack.engine;

import android.content.Intent;

public interface IBjEngineOnActivityResult {
  void onResult(int requestCode, int resultCode, Intent data);
}
