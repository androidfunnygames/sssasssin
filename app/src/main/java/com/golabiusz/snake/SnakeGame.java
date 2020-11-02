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
import org.jetbrains.annotations.NotNull;
import java.io.IOException;

public class SnakeGame extends SurfaceView implements Runnable {
  private final int MILLIS_IN_SECOND = 1000;
  private final int NUM_BLOCKS_WIDE = 40;

  private Thread gameThread = null;
  private volatile boolean isPaused;
  private boolean isPlaying = false;
  private long nextFrameTime;

  private Canvas canvas;
  private final Paint paint;

  private SoundPool sp;
  private int crashSoundId = -1;
  private int eatSoundId = -1;

  private final Snake snake;
  private final Apple apple;

  private int score = 0;
  private int bestScore = 0;

  private final int horizontalScreenCenter;
  // How big is the entire grid
  private final Point furthestPoint;

  public SnakeGame(Context context, @NotNull Point screenSize) {
    super(context);

    horizontalScreenCenter = screenSize.x / 2;
    int blockSize = screenSize.x / NUM_BLOCKS_WIDE;
    int numBlocksHigh = screenSize.y / blockSize;
    furthestPoint = new Point(NUM_BLOCKS_WIDE, numBlocksHigh);

    paint = new Paint();

    loadSounds(context);

    apple = new Apple(context, blockSize);
    snake = new Snake(context, blockSize);

    newGame();
  }

  private void loadSounds(@NotNull Context context) {
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
    snake.reset(furthestPoint);
    apple.spawn(furthestPoint);

    bestScore = Math.max(bestScore, score);
    score = 0;

    nextFrameTime = System.currentTimeMillis();
  }

  @Override
  public void run() {
    while (!isPaused) {
      if (isPlaying && updateRequired()) {
        update();
      }

      draw();
    }
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

  private void update() {
    snake.move();

    if (snake.checkDinner(apple.getLocation())) {
      apple.spawn(furthestPoint);

      ++score;

      sp.play(eatSoundId, 1, 1, 0, 0, 1);
    }

    if (snake.hasDied(furthestPoint)) {
      sp.play(crashSoundId, 1, 1, 0, 0, 1);

      isPlaying = false;
    }
  }

  private void draw() {
    if (getHolder().getSurface().isValid()) {
      canvas = getHolder().lockCanvas();
      canvas.drawColor(Color.argb(255, 26, 128, 182));
      paint.setColor(Color.argb(255, 255, 255, 255));

      drawHUD();
      apple.draw(canvas, paint);
      snake.draw(canvas, paint);

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

  private void drawPauseMessage() {
    paint.setColor(Color.argb(255, 255, 255, 255));
    paint.setTextSize(150);

    canvas.drawText(getResources().getString(R.string.tap_to_play), 50, 600, paint);
  }

  @Override
  public boolean onTouchEvent(@NotNull MotionEvent motionEvent) {
    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
      case MotionEvent.ACTION_UP:
        if (!isPlaying) {
          isPlaying = true;
          newGame();

          return true;
        }

        if (motionEvent.getX() >= horizontalScreenCenter) {
          snake.rotateRight();
        } else {
          snake.rotateLeft();
        }
        break;

      default:
        break;
    }

    return true;
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
}
