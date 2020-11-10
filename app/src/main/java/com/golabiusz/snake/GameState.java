package com.golabiusz.snake;

import android.content.Context;
import android.content.SharedPreferences;
import org.jetbrains.annotations.NotNull;

class GameState {
  private static volatile boolean isThreadRunning = false;
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
  }

  void endGame() {
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

  boolean isGameOver() {
    return isGameOver;
  }

  int getScore() {
    return score;
  }

  void increaseScore() {
    score++;
  }

  int getHighScore() {
    return highScore;
  }
}
