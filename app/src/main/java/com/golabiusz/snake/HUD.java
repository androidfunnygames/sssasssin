package com.golabiusz.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.Rect;
import java.text.NumberFormat;
import org.jetbrains.annotations.NotNull;

class HUD {
  private Context context;

  private int textFormatting;
  private int screenHeight;
  private int screenWidth;

  private Bitmap bitmapHead;
  private Bitmap bitmapOK;
  private Rect pauseButton;

  HUD(Context context, @NotNull Point size) {
    this.context = context;

    screenHeight = size.y;
    screenWidth = size.x;
    textFormatting = size.x / 50;

    bitmapHead = BitmapFactory.decodeResource(context.getResources(), R.drawable.sssasssin);
    bitmapHead = Bitmap.createScaledBitmap(bitmapHead, size.x / 15, size.x / 15, false);
    bitmapOK = BitmapFactory.decodeResource(context.getResources(), R.drawable.ok);
    bitmapOK = Bitmap.createScaledBitmap(bitmapOK, size.x / 15, size.x / 15, false);
  }

  void draw(@NotNull Canvas canvas, @NotNull Paint paint, @NotNull GameState gameState) {
    paint.setColor(Color.argb(255, 255, 255, 255));
    paint.setTextAlign(Align.LEFT);
    paint.setTextSize(textFormatting);

    canvas.drawText(
        "Hi: " + NumberFormat.getCurrencyInstance().format(gameState.getHighScore()),
        textFormatting,
        textFormatting,
        paint);
    canvas.drawText(
        this.context.getResources().getString(R.string.money) + ": " + NumberFormat.getCurrencyInstance().format(gameState.getScore()),
        textFormatting,
        textFormatting * 2,
        paint);

    if (gameState.isGameOver()) {
      drawNewGameMessage(canvas, paint);
    } else if (gameState.isPaused()) {
      drawContinueMessage(canvas, paint);
    } else {
      pauseButton = null;
    }
  }

  private void drawNewGameMessage(@NotNull Canvas canvas, @NotNull Paint paint) {
    paint.setTextAlign(Align.CENTER);
    paint.setTextSize(textFormatting * 3);
    canvas.drawText(
        this.context.getResources().getString(R.string.tap_to_play),
        screenWidth / 2,
        screenHeight / 2,
        paint);
  }

  private void drawContinueMessage(@NotNull Canvas canvas, @NotNull Paint paint) {
    canvas.drawBitmap(
        bitmapHead,
        screenWidth / 2 - bitmapHead.getWidth() / 2,
        screenHeight / 4 - bitmapHead.getHeight() / 2,
        paint);

    paint.setTextAlign(Align.CENTER);
    paint.setTextSize(textFormatting * 3);
    canvas.drawText(
        this.context.getResources().getString(R.string.tap_to_continue),
        screenWidth / 2,
        screenHeight / 2,
        paint);

    int buttonWidth = bitmapOK.getWidth();
    pauseButton = new Rect(
        screenWidth / 2 - buttonWidth / 2,
        screenHeight * 3 / 4 - buttonWidth / 2,
        screenWidth / 2 + buttonWidth / 2,
        screenHeight * 3 / 4 + buttonWidth / 2);
    canvas.drawBitmap(
        bitmapOK,
        screenWidth / 2 - bitmapOK.getWidth() / 2,
        screenHeight * 3 / 4 - bitmapOK.getHeight() / 2,
        paint);
    paint.setAlpha(0);
    canvas.drawRect(pauseButton.left, pauseButton.top, pauseButton.right, pauseButton.bottom, paint);
    paint.setAlpha(255);
  }

  Rect getPauseButton() {
    return pauseButton;
  }
}
