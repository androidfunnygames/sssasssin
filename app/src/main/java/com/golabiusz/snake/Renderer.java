package com.golabiusz.snake;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import org.jetbrains.annotations.NotNull;

class Renderer {
  private final SurfaceHolder surfaceHolder;
  private final Paint paint;

  Renderer(@NotNull SurfaceView surfaceView) {
    surfaceHolder = surfaceView.getHolder();
    paint = new Paint();
  }

  void draw(GameState gameState, HUD hud, Snake snake, Apple apple) {
    if (surfaceHolder.getSurface().isValid()) {
      Canvas canvas = surfaceHolder.lockCanvas();
      canvas.drawColor(Color.argb(255, 26, 128, 182));
      paint.setColor(Color.argb(255, 255, 255, 255));

      snake.draw(canvas, paint);
      hud.draw(canvas, paint, gameState);
      apple.draw(canvas, paint);

      surfaceHolder.unlockCanvasAndPost(canvas);
    }
  }
}
