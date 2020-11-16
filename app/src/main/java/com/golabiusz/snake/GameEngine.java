package com.golabiusz.snake;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import org.jetbrains.annotations.NotNull;

class GameEngine extends SurfaceView implements Runnable {
  private Thread gameThread = null;

  private final GameState gameState;
  private final Renderer renderer;
  private final SoundEngine soundEngine;

  private final HUD hud;
  private final Snake snake;
  private final Apple apple;

  private final int horizontalScreenCenter;
  private final Point furthestPoint;

  public GameEngine(Context context, @NotNull Point screenSize) {
    super(context);

    horizontalScreenCenter = screenSize.x / 2;
    int NUM_BLOCKS_WIDE = 40;
    int blockSize = screenSize.x / NUM_BLOCKS_WIDE;
    int numBlocksHigh = screenSize.y / blockSize;
    furthestPoint = new Point(NUM_BLOCKS_WIDE, numBlocksHigh);

    soundEngine = new SoundEngine(context);
    gameState = new GameState(context);
    renderer = new Renderer(this);

    hud = new HUD(context, screenSize);
    apple = new Apple(context, blockSize);
    snake = new Snake(context, blockSize);
  }

  @Override
  public void run() {
    while (gameState.isThreadRunning()) {
      if (!gameState.isPaused() && gameState.updateRequired()) {
        update();
      }

      renderer.draw(gameState, hud, snake, apple);
    }
  }

  private void update() {
    snake.move();

    if (snake.checkDinner(apple.getLocation())) {
      apple.spawn(furthestPoint);

      gameState.increaseScore();

      soundEngine.playEat();
    }

    if (snake.hasDied(furthestPoint)) {
      soundEngine.playCrash();
      gameState.endGame();
    }
  }

  @Override
  public boolean onTouchEvent(@NotNull MotionEvent motionEvent) {
    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
      case MotionEvent.ACTION_UP:
        if (gameState.isGameOver()) {
          newGame();

          return true;
        } else if (gameState.isPaused() && hud.getPauseButton().contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
          gameState.resume();
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

  private void newGame() {
    gameState.startGame();
    snake.reset(furthestPoint);
    apple.spawn(furthestPoint);
  }

  void startThread() {
    gameState.startThread();

    gameThread = new Thread(this);
    gameThread.start();
  }

  void stopThread() {
    gameState.endGame();
    gameState.stopThread();

    try {
      gameThread.join();
    } catch (InterruptedException e) {
      Log.e("Exception","stopThread()" + e.getMessage());
    }
  }
}
