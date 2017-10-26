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

    projectDataList.add(new ProjectData("9d9f74d0a9b293a2ea1a7263f47e01baed2cb0f3",
        "6a4d8072f2a519c67b0124656ce6cb857a55276a"));
    projectDataList.add(new ProjectData("ef08c4dccb7649b9956296a863db002a68240be2",
        "6bc18c500546f253699f61c11a62827679178400"));

    return projectDataList;
  }
}
