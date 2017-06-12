package com.gigigo.orchextra.ocm;

public final class OcmStyleUiBuilder {

  private String titleFontPath;
  private String normalFonPath;
  private String mediumFontPath;
  private String lightFontPath;
  private boolean titleToolbarEnabled = false;
  private boolean thumbnailEnabled = true;
  private boolean statusBarEnabled = true;

  /**
   * Path to the font to apply in titles of the app
   */
  public OcmStyleUiBuilder setTitleFont(String titleFontPath) {
    this.titleFontPath = titleFontPath;
    return this;
  }

  /**
   * Path to the font to apply in general texts of the app
   */
  public OcmStyleUiBuilder setNormalFont(String normalFonPath) {
    this.normalFonPath = normalFonPath;
    return this;
  }

  /**
   * Path to the font to apply in text of buttons of the app
   */
  public OcmStyleUiBuilder setMediumFont(String mediumFontPath) {
    this.mediumFontPath = mediumFontPath;
    return this;
  }

  @Deprecated
  public OcmStyleUiBuilder setLightFont(String lightFontPath) {
    this.lightFontPath = lightFontPath;
    return this;
  }

  public OcmStyleUiBuilder setTitleToolbarEnabled(boolean enabled) {
    this.titleToolbarEnabled = enabled;
    return this;
  }

  public OcmStyleUiBuilder setThumbnailEnabled(boolean thumbnailEnabled) {
    this.thumbnailEnabled = thumbnailEnabled;
    return this;
  }

  public void setEnabledStatusBar(boolean statusBarEnabled) {
    this.statusBarEnabled = statusBarEnabled;
  }

  public String getTitleFontPath() {
    return titleFontPath;
  }

  public String getNormalFonPath() {
    return normalFonPath;
  }

  public String getMediumFontPath() {
    return mediumFontPath;
  }

  @Deprecated
  public String getLightFontPath() {
    return lightFontPath;
  }

  public boolean isTitleToolbarEnabled() {
    return titleToolbarEnabled;
  }

  public boolean isThumbnailEnabled() {
    return thumbnailEnabled;
  }

  public boolean isStatusBarEnabled() {
    return statusBarEnabled;
  }
}
