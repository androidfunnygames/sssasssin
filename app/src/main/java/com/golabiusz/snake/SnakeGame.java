package com.golabiusz.snake;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.io.IOException;

public class SnakeGame extends SurfaceView implements Runnable {
  private final int MILLIS_IN_SECOND = 1000;
  private final int NUM_BLOCKS_WIDE = 40;

  private Thread gameThread = null;
  private volatile boolean isPaused;
  private boolean isPlaying = false;
  private long nextFrameTime;

  private Canvas canvas;
  private Paint paint;

  private Point screenSize;
  private int numBlocksHigh;

  private SoundPool sp;
  private int crashSoundId = -1;
  private int eatSoundId = -1;

  private Snake snake;
  private Apple apple;

  private int score = 0;
  private int bestScore = 0;

  public SnakeGame(Context context, Point screenSize) {
    super(context);

    this.screenSize = screenSize;

    int blockSize = screenSize.x / NUM_BLOCKS_WIDE;
    numBlocksHigh = screenSize.y / blockSize;

    paint = new Paint();

    this.loadSounds(context);

    apple = new Apple(context, new Point(NUM_BLOCKS_WIDE, numBlocksHigh), blockSize);

    newGame();
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
      if (isPlaying && updateRequired()) {
        updateSnakePosition();
        detectEating();
        detectCollisions();
      }

      draw();
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent motionEvent) {
    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
      case MotionEvent.ACTION_UP:
        if (!isPlaying) {
          isPlaying = true;
          newGame();

          return true;
        }

        // Let the Snake class handle the input
        break;

      default:
        break;
    }

    return true;
  }

  private void loadSounds(Context context) {
    sp = new SoundPoolBuilder().build();

    try {
      AssetManager assetManager = context.getAssets();
      AssetFileDescriptor descriptor;

      descriptor = assetManager.openFd("snake_death.ogg");
      crashSoundId = sp.load(descriptor, 0);

      descriptor = assetManager.openFd("get_apple.ogg");
      eatSoundId = sp.load(descriptor, 0);
    } catch (IOException e) {
      Log.e("error", "failed to load sound files");
    }
  }

  private void newGame() {
    // reset the snake

    apple.spawn();

    bestScore = Math.max(bestScore, score);
    score = 0;

    nextFrameTime = System.currentTimeMillis();
  }

  private boolean updateRequired() {
    final int TARGET_FPS = 10;
    long now = System.currentTimeMillis();

    if (nextFrameTime <= now) {
      nextFrameTime = now + MILLIS_IN_SECOND / TARGET_FPS;

      return true;
    }

    return false;
  }

  private void updateSnakePosition() {
    // TODO update snake position
  }

  private void detectEating() {
    // TODO detect snake ate apple
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
      canvas.drawColor(Color.argb(255, 26, 128, 182));
      paint.setColor(Color.argb(255, 255, 255, 255));

      drawHUD();
      apple.draw(canvas, paint);
      drawSnake();

      if (!isPlaying) {
        drawPauseMessage();
      }

      getHolder().unlockCanvasAndPost(canvas);
    }
  }

  private void drawHUD() {
    paint.setTextSize(120);

    canvas.drawText("" + score, 20, 120, paint);
  }

  private void drawSnake() {
    // TODO draw snake
  }

  private void drawPauseMessage() {
    paint.setColor(Color.argb(255, 255, 255, 255));
    paint.setTextSize(150);

    canvas.drawText(getResources().getString(R.string.tap_to_play), 50, 600, paint);
  }
}
