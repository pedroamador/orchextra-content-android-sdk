package com.gigigo.sample;

import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import com.squareup.leakcanary.LeakCanary;

public class App extends MultiDexApplication {

  @Override public void onCreate() {
    enableStrictMode();
    super.onCreate();
    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return;
    }
    LeakCanary.install(this);
    MultiDex.install(this);

    ContentManager contentManager = ContentManager.getInstance();
    contentManager.init(this);
  }

  private void enableStrictMode() {
    StrictMode.setThreadPolicy(
        new StrictMode.ThreadPolicy.Builder().detectAll()   // or .detectAll() for all detectable problems
            .penaltyLog().build());
    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().build());
  }
}
