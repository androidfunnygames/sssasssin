package com.golabiusz.snake;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class SnakeActivity extends Activity {
  SnakeGame snakeGame;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Display display = getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);

    snakeGame = new SnakeGame(this, size);

    setContentView(snakeGame);
  }

  @Override
  protected void onResume() {
    super.onResume();

    snakeGame.resume();
  }

  @Override
  protected void onPause() {
    super.onPause();

    snakeGame.pause();
  }
}
