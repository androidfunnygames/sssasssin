package com.golabiusz.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.SoundPool;
import android.util.Log;
import android.view.SurfaceView;

public class SnakeGame extends SurfaceView implements Runnable {
  private final int MILLIS_IN_SECOND = 1000;

  private Thread gameThread = null;
  private volatile boolean isPaused;
  private boolean isPlaying = false;

  private Canvas canvas;
  private Paint paint;

  private long fps;

  private Point screenSize;

  private SoundPool sp;
  private int eatSoundId = -1;
  private int crashSoundId = -1;

  private Snake snake;
  private Apple apple;

  private int score = 0;
  private int bestScore = 0;

  public SnakeGame(Context context, Point screenSize) {
    super(context);

    this.screenSize = screenSize;

    paint = new Paint();

    this.loadSounds(context);

    startGame();
  }

  public void resume() {
    isPaused = false;

    gameThread = new Thread(this);
    gameThread.start();
  }

  public void pause() {
    isPaused = true;
    try {
      gameThread.join();
    } catch (InterruptedException e) {
      Log.e("Error:", "stopping thread");
    }
  }

  @Override
  public void run() {
    while (!isPaused) {
      long frameStartTime = System.currentTimeMillis();

      if (isPlaying) {
        updateSnakePosition();
        detectCollisions();
      }

      draw();

      long timeThisFrame = System.currentTimeMillis() - frameStartTime;
      if (timeThisFrame > 0) {
        fps = MILLIS_IN_SECOND / timeThisFrame;
      }
    }
  }

  private void loadSounds(Context context) {
    sp = new SoundPoolBuilder().build();

    // TODO load sounds
  }

  private void startGame() {
    // TODO reset state

    bestScore = Math.max(bestScore, score);
    score = 0;
  }

  private void updateSnakePosition() {
    // TODO update snake position
  }

  private void detectCollisions() {
    detectWallCollisions();
    detectSnakeCollisions();
  }

  private void detectWallCollisions() {
    // TODO detect wall collisions
  }

  private void detectSnakeCollisions() {
    // TODO detect snake collisions (snake collisions with itself)
  }

  private void draw() {
    if (getHolder().getSurface().isValid()) {
      canvas = getHolder().lockCanvas();
      canvas.drawColor(Color.argb(255, 243, 111, 36));
      paint.setColor(Color.argb(255, 255, 255, 255));

      drawApples();
      drawSnake();
      drawHUD();

      getHolder().unlockCanvasAndPost(canvas);
    }
  }

  private void drawApples() {
    // TODO draw apples
  }

  private void drawSnake() {
    // TODO draw snake
  }

  private void drawHUD() {
    // TODO draw HUD
  }
}
