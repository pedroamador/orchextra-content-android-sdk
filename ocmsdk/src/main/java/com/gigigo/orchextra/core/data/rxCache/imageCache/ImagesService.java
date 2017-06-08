package com.gigigo.orchextra.core.data.rxCache.imageCache;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

/**
 * Created by francisco.hernandez on 7/6/17.
 */

@Singleton public class ImagesService extends Service {

  private final OcmImageCache ocmImageCache;

  @Inject public ImagesService(OcmImageCache ocmImageCache) {
    this.ocmImageCache = ocmImageCache;
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }
}
