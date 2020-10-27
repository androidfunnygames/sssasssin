package com.golabiusz.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class Apple {
  // The location of the apple on the grid
  // Not in pixels
  private Point location = new Point();

  // The range of values we can choose from to spawn an apple
  private Point spawnRange;
  private int size;

  private Bitmap bitmap;

  Apple(Context context, Point spawnRange, int size) {
    this.spawnRange = spawnRange;
    this.size = size;
    // Hide the apple off-screen until the game starts
    location.x = -10;

    bitmap = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(context.getResources(), R.drawable.apple),
        size,
        size,
        false
    );
  }

  public void spawn() {
    Random random = new Random();
    location.x = random.nextInt(spawnRange.x) + 1;
    location.y = random.nextInt(spawnRange.y - 1) + 1;
  }

  public Point getLocation() {
    return location;
  }

  void draw(Canvas canvas, Paint paint) {
    canvas.drawBitmap(bitmap, location.x * size, location.y * size, paint);
  }
}
