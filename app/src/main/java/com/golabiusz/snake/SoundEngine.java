package com.golabiusz.snake;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

class SoundEngine {
  private SoundPool soundPool;

  private int crashSoundId = -1;
  private int eatSoundId = -1;

  SoundEngine(Context context) {
    buildSoundPool();
    loadSounds(context);
  }

  private void buildSoundPool() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      AudioAttributes audioAttributes =
          new AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_MEDIA)
              .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
              .build();

      soundPool =
          new SoundPool.Builder().setMaxStreams(5).setAudioAttributes(audioAttributes).build();
    } else {
      soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
    }
  }

  private void loadSounds(@NotNull Context context) {
    try {
      AssetManager assetManager = context.getAssets();
      AssetFileDescriptor descriptor;

      descriptor = assetManager.openFd("snake_death.ogg");
      crashSoundId = soundPool.load(descriptor, 0);

      descriptor = assetManager.openFd("get_apple.ogg");
      eatSoundId = soundPool.load(descriptor, 0);
    } catch (IOException e) {
      Log.e("error", "failed to load sound files");
    }
  }

  void playCrash() {
    soundPool.play(crashSoundId,1, 1, 0, 0, 1);
  }

  void playEat() {
    soundPool.play(eatSoundId,1, 1, 0, 0, 1);
  }
}
