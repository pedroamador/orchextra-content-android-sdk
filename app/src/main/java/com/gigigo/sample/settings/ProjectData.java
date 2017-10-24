package com.gigigo.sample.settings;

import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

public final class ProjectData {

  @NonNull private final String apiKey;
  @NonNull private final String apiSecret;

  public ProjectData(@NonNull String apiKey, @NonNull String apiSecret) {
    this.apiKey = apiKey;
    this.apiSecret = apiSecret;
  }

  @NonNull public String getApiKey() {
    return apiKey;
  }

  @NonNull public String getApiSecret() {
    return apiSecret;
  }

  @NonNull public static List<ProjectData> getDefaultProjectDataList() {

    List<ProjectData> projectDataList = new ArrayList<>();

    projectDataList.add(new ProjectData("apiKey1", "apiSecret1"));
    projectDataList.add(new ProjectData("apiKey2", "apiSecret2"));
    projectDataList.add(new ProjectData("apiKey3", "apiSecret3"));

    return projectDataList;
  }
}
