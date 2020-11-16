package com.golabiusz.snake;

import android.content.Context;
import android.content.SharedPreferences;
import org.jetbrains.annotations.NotNull;

class GameState {
  private static volatile boolean isThreadRunning = false;
  private static volatile boolean isPaused = true;
  private static volatile boolean isGameOver = true;

  private final SharedPreferences.Editor editor;

  private long nextFrameTime;
  private int highScore;
  private int score = 0;

  GameState(@NotNull Context context) {
    SharedPreferences prefs;
    prefs = context.getSharedPreferences("HiScore", Context.MODE_PRIVATE);

    editor = prefs.edit();
    highScore = prefs.getInt("hi_score", 0);
  }

  void startGame() {
    nextFrameTime = System.currentTimeMillis();
    score = 0;
    isGameOver = false;
    resume();
  }

  void endGame() {
    pause();
    isGameOver = true;

    if (score > highScore) {
      highScore = score;

      editor.putInt("hi_score", highScore);
      editor.commit();
    }
  }

  boolean updateRequired() {
    long now = System.currentTimeMillis();

    if (nextFrameTime <= now) {
      int MILLIS_IN_SECOND = 1000;
      int TARGET_FPS = 10;
      nextFrameTime = now + MILLIS_IN_SECOND / TARGET_FPS;

      return true;
    }

    return false;
  }

  boolean isThreadRunning() {
    return isThreadRunning;
  }

  void startThread() {
    isThreadRunning = true;
  }

  void stopThread() {
    isThreadRunning = false;
  }

  boolean isPaused() {
    return isPaused;
  }

  void pause() {
    isPaused = true;
  }

  void resume() {
    isPaused = false;
  }

  boolean isGameOver() {
    return isGameOver;
  }

  int getScore() {
    return score;
  }

  void increaseScore() {
    score += 1000000;

    int SCORE_TO_ACHIEVE = 70000000;
    if (score == SCORE_TO_ACHIEVE) {
      pause();
    }
  }

  int getHighScore() {
    return highScore;
  }
}
