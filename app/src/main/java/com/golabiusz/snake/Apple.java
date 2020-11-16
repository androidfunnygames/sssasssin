package com.golabiusz.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import org.jetbrains.annotations.NotNull;
import java.util.Random;

public class Apple {
  // The location of the apple on the grid (not in pixels)
  private final Point location = new Point();

  private final int size;

  private final Bitmap bitmap;

  Apple(@NotNull Context context, int size) {
    this.size = size;
    // Hide the apple off-screen until the game starts
    location.x = -10;

    bitmap =
        Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(context.getResources(), R.drawable.moneybag),
            size,
            size,
            false);
  }

  public void spawn(@NotNull Point spawnRange) {
    Random random = new Random();
    location.x = random.nextInt(spawnRange.x) + 1;
    location.y = random.nextInt(spawnRange.y - 1) + 1;
  }

  public Point getLocation() {
    return location;
  }

  void draw(@NotNull Canvas canvas, Paint paint) {
    canvas.drawBitmap(bitmap, location.x * size, location.y * size, paint);
  }
}
