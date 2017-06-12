package com.gigigo.orchextra.core.data.rxCache.imageCache.loader;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.gigigo.ggglogger.GGGLogImpl;
import com.gigigo.ggglogger.LogLevel;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by francisco.hernandez on 12/6/17.
 */

public class OcmImageLoader {
  private static String TAG = OcmImageLoader.class.getSimpleName();

  public static void load(Context mContext, String url, ImageView into) {
    File cacheFile = getCacheFile(mContext, md5(url));
    if (cacheFile.exists()) {
      GGGLogImpl.log("(DISK)  " + url, LogLevel.INFO, TAG);
      Glide.with(mContext).load(cacheFile).into(into);
    } else {
      GGGLogImpl.log("(CLOUD) " + url, LogLevel.INFO, TAG);
      Glide.with(mContext).load(url).into(into);
    }
  }

  public static String md5(String s) {
    if (s.contains("?")) {
      int index = s.indexOf("?");
      s = s.substring(0, index);
    }
    try {
      // Create MD5 Hash
      MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
      digest.update(s.getBytes());
      byte messageDigest[] = digest.digest();

      // Create Hex String
      StringBuffer hexString = new StringBuffer();
      for (int i = 0; i < messageDigest.length; i++)
        hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return "";
  }

  public static File getCacheDir(Context mContext) {
    // Create a path pointing to the system-recommended cache dir for the app, with sub-dir named
    // thumbnails
    File cacheDir = new File(mContext.getCacheDir(), "images");
    return cacheDir;
  }

  public static File getCacheFile(Context mContext, String filename) {
    File cacheFile = new File(getCacheDir(mContext), filename);
    return cacheFile;
  }
}
