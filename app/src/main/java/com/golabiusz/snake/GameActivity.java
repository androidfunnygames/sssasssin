package com.golabiusz.snake;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class GameActivity extends Activity {
  GameEngine gameEngine;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Display display = getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);

    gameEngine = new GameEngine(this, size);

    setContentView(gameEngine);
  }

  @Override
  protected void onResume() {
    super.onResume();

    gameEngine.startThread();
  }

  @Override
  protected void onPause() {
    super.onPause();

    gameEngine.stopThread();
  }
}
