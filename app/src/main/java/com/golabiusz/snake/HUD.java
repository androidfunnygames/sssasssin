package com.golabiusz.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import org.jetbrains.annotations.NotNull;

class HUD {
  private Context context;

  private int textFormatting;
  private int screenHeight;
  private int screenWidth;

  HUD(Context context, @NotNull Point size) {
    this.context = context;

    screenHeight = size.y;
    screenWidth = size.x;
    textFormatting = size.x / 50;
  }

  void draw(@NotNull Canvas canvas, @NotNull Paint paint, @NotNull GameState gameState) {
    paint.setColor(Color.argb(255, 255, 255, 255));
    paint.setTextSize(textFormatting);

    canvas.drawText("Hi: " + gameState.getHighScore(), textFormatting, textFormatting, paint);
    canvas.drawText("Score: " + gameState.getScore(), textFormatting, textFormatting * 2, paint);

    if (gameState.isGameOver()) {
      drawNewGameMessage(canvas, paint);
    }
  }

  private void drawNewGameMessage(@NotNull Canvas canvas, @NotNull Paint paint) {
    paint.setTextSize(textFormatting * 3);
    canvas.drawText(
        this.context.getResources().getString(R.string.tap_to_play),
        screenWidth / 4,
        screenHeight / 2,
        paint);
  }
}
