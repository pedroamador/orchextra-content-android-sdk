package com.gigigo.orchextra.core.sdk.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.support.customtabs.CustomTabsIntent;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.gigigo.ggglib.device.AndroidSdkVersion;
import com.gigigo.orchextra.core.sdk.ui.OcmWebViewActivity;
import com.gigigo.orchextra.ocmsdk.R;

public class DeviceUtils {

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
  public static int calculateHeightDeviceInImmersiveMode(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

    DisplayMetrics metrics = new DisplayMetrics();
    Display display = wm.getDefaultDisplay();

    if (AndroidSdkVersion.hasJellyBean17()) {
      display.getRealMetrics(metrics);
      return metrics.heightPixels;
    } else {
      Point size = new Point();
      display.getSize(size);
      return size.y;
    }
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
  public static int calculateRealWidthDeviceInImmersiveMode(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

    DisplayMetrics metrics = new DisplayMetrics();
    Display display = wm.getDefaultDisplay();

    if (AndroidSdkVersion.hasJellyBean17()) {
      display.getRealMetrics(metrics);
      return metrics.widthPixels;
    } else {
      Point size = new Point();
      display.getSize(size);
      return size.x;
    }
  }

  public static void openChromeTabs(Activity activity, String url) {
    if (activity != null && url != null) {
      try {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(activity.getResources().getColor(R.color.oc_background_detail_toolbar));
        //builder.setCloseButtonIcon(BitmapFactory.decodeResource(
        //    getResources(), android.R.drawable.ic_menu_b));
        CustomTabsIntent customTabsIntent = builder.build();

        //asv hack this never work if we use other builttools like 25, now we are using 24.0.3
        // customTabsIntent.launchUrl(activity, Uri.parse(url));
        //asv this is the same that launchUrl
        customTabsIntent.intent.setData(Uri.parse(url));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
          activity.startActivity(customTabsIntent.intent);
        }
      } catch (Exception e) {
        OcmWebViewActivity.open(activity, url);
      }
    }
  }

  public static int calculateWidthDevice(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    return size.x;
  }

  public static int calculateHeightDevice(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    return size.y;
  }

  private static final int MIN_RAM_MEMORY = 256;
  public static boolean checkDeviceHasEnoughRamMemory() {
    try {
      final Runtime runtime = Runtime.getRuntime();
      final long usedMemInMB = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
      final long maxHeapSizeInMB = runtime.maxMemory() / 1048576L;
      final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;

      return availHeapSizeInMB > MIN_RAM_MEMORY;
    } catch (Exception e) {
      return true;
    }
  }
}
