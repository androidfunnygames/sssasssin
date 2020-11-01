package com.golabiusz.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class Snake {
  // For tracking movement Heading
  private enum Heading {
    UP,
    RIGHT,
    DOWN,
    LEFT
  }

  // Start by heading to the right
  private Heading heading = Heading.RIGHT;

  // The location in the grid of all the segments
  private ArrayList<Point> segmentLocations;

  // How big is each segment of the snake?
  private int segmentSize;

  // A bitmap for each direction the head can face
  private Bitmap bitmapHeadRight;
  private Bitmap bitmapHeadLeft;
  private Bitmap bitmapHeadUp;
  private Bitmap bitmapHeadDown;

  // A bitmap for the body
  private Bitmap bitmapBody;

  Snake(Context context, int segmentSize) {
    // Initialize our ArrayList
    segmentLocations = new ArrayList<>();

    // Initialize the segment size and movement
    // range from the passed in parameters
    this.segmentSize = segmentSize;

    createBitmaps(context);
  }

  private void createBitmaps(@NotNull Context context)
  {
    // Create and scale the bitmaps
    bitmapHeadRight = BitmapFactory.decodeResource(context.getResources(), R.drawable.head);
    bitmapHeadLeft = BitmapFactory.decodeResource(context.getResources(), R.drawable.head);
    bitmapHeadUp = BitmapFactory.decodeResource(context.getResources(), R.drawable.head);
    bitmapHeadDown = BitmapFactory.decodeResource(context.getResources(), R.drawable.head);
    bitmapBody = BitmapFactory.decodeResource(context.getResources(), R.drawable.body);

    // Modify the bitmaps to face the snake head in the correct direction
    bitmapHeadRight = Bitmap.createScaledBitmap(bitmapHeadRight, segmentSize, segmentSize, false);

    // A matrix for scaling
    Matrix matrix = new Matrix();
    matrix.preScale(-1, 1);

    bitmapHeadLeft =
        Bitmap.createBitmap(bitmapHeadRight, 0, 0, segmentSize, segmentSize, matrix, true);

    // A matrix for rotating
    matrix.preRotate(-90);
    bitmapHeadUp =
        Bitmap.createBitmap(bitmapHeadRight, 0, 0, segmentSize, segmentSize, matrix, true);

    // Matrix operations are cumulative so rotate by 180 to face down
    matrix.preRotate(180);
    bitmapHeadDown =
        Bitmap.createBitmap(bitmapHeadRight, 0, 0, segmentSize, segmentSize, matrix, true);

    // Scale the body
    bitmapBody = Bitmap.createScaledBitmap(bitmapBody, segmentSize, segmentSize, false);
  }

  public void reset(@NotNull Point moveRange) {
    // Reset the heading
    heading = Heading.RIGHT;

    // Delete the old contents of the ArrayList
    segmentLocations.clear();

    // Start with a single snake segment
    segmentLocations.add(new Point(moveRange.x / 2, moveRange.y / 2));
  }

  public void rotateRight() {
    switch (heading) {
      case UP:
        heading = Heading.RIGHT;
        break;
      case RIGHT:
        heading = Heading.DOWN;
        break;
      case DOWN:
        heading = Heading.LEFT;
        break;
      case LEFT:
        heading = Heading.UP;
        break;
    }
  }

  public void rotateLeft() {
    switch (heading) {
      case UP:
        heading = Heading.LEFT;
        break;
      case LEFT:
        heading = Heading.DOWN;
        break;
      case DOWN:
        heading = Heading.RIGHT;
        break;
      case RIGHT:
        heading = Heading.UP;
        break;
    }
  }

  public void move() {
    moveBody();
    moveHead();
  }

  private void moveBody() {
    // Start at the back and move it to the position of the segment in front of it
    for (int i = segmentLocations.size() - 1; i > 0; i--) {
      // Make it the same value as the next segment going forwards towards the head
      segmentLocations.get(i).x = segmentLocations.get(i - 1).x;
      segmentLocations.get(i).y = segmentLocations.get(i - 1).y;
    }
  }

  private void moveHead() {
    // Get the existing head position
    Point point = segmentLocations.get(0);

    switch (heading) {
      case UP:
        point.y--;
        break;

      case RIGHT:
        point.x++;
        break;

      case DOWN:
        point.y++;
        break;

      case LEFT:
        point.x--;
        break;
    }

    // Insert the adjusted point back into position 0
    segmentLocations.set(0, point);
  }

  public boolean hasDied(@NotNull Point moveRange) {
    return hasHitWall(moveRange) || hasEatenHimself();
  }

  private boolean hasHitWall(@NotNull Point moveRange) {
    return segmentLocations.get(0).x == -1
        || segmentLocations.get(0).x > moveRange.x
        || segmentLocations.get(0).y == -1
        || segmentLocations.get(0).y > moveRange.y;
  }

  private boolean hasEatenHimself() {
    for (int i = segmentLocations.size() - 1; i > 0; i--) {
      // Have any of the sections collided with the head
      if (segmentLocations.get(0).x == segmentLocations.get(i).x
          && segmentLocations.get(0).y == segmentLocations.get(i).y) {

        return true;
      }
    }

    return false;
  }

  public boolean checkDinner(@NotNull Point appleLocation) {
    if (segmentLocations.get(0).x == appleLocation.x
        && segmentLocations.get(0).y == appleLocation.y) {
      // Add a new Point to the list located off-screen.
      // This is OK because on the next call to move it will take the position of
      // the segment in front of it
      segmentLocations.add(new Point(-10, -10));

      return true;
    }

    return false;
  }

  public void draw(Canvas canvas, Paint paint) {
    if (!segmentLocations.isEmpty()) {
      drawHead(canvas, paint);
      drawBody(canvas, paint);
    }
  }

  private void drawHead(Canvas canvas, Paint paint) {
    Bitmap bitmap;

    switch (heading) {
      case UP:
        bitmap = bitmapHeadUp;
        break;

      case DOWN:
        bitmap = bitmapHeadDown;
        break;

      case LEFT:
        bitmap = bitmapHeadLeft;
        break;

      case RIGHT:
      default:
        bitmap = bitmapHeadRight;
        break;
    }

    canvas.drawBitmap(
        bitmap,
        segmentLocations.get(0).x * segmentSize,
        segmentLocations.get(0).y * segmentSize,
        paint);
  }

  private void drawBody(Canvas canvas, Paint paint) {
    for (int i = 1; i < segmentLocations.size(); i++) {
      canvas.drawBitmap(
          bitmapBody,
          segmentLocations.get(i).x * segmentSize,
          segmentLocations.get(i).y * segmentSize,
          paint);
    }
  }
}
