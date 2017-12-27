package com.gigigo.orchextra.wrapper;

import android.app.Application;
import android.util.Log;
import com.gigigo.orchextra.core.OrchextraOptions;
import com.gigigo.orchextra.geofence.OxGeofenceImp;
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver;
import com.gigigo.orchextra.scanner.OxScannerImp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import orchextra.javax.inject.Inject;

public class Ox3ManagerImp implements OxManager {

  private static final String TAG = "Ox3ManagerImp";
  private com.gigigo.orchextra.core.Orchextra orchextra;
  private OnCustomSchemeReceiver onCustomSchemeReceiver;
  private HashMap<CrmUser.Gender, String> genders;
  private OrchextraCompletionCallback orchextraCompletionCallback;
  private Application app;
  private Config config;

  @Inject public Ox3ManagerImp() {
    genders = new HashMap<>();
    genders.put(CrmUser.Gender.GenderFemale, "female");
    genders.put(CrmUser.Gender.GenderMale, "male");
    genders.put(CrmUser.Gender.GenderND, "nd");

    this.orchextra = com.gigigo.orchextra.core.Orchextra.INSTANCE;
  }

  @Override public void callOnCustomSchemeReceiver(String customScheme) {
    if (onCustomSchemeReceiver != null) {
      onCustomSchemeReceiver.onReceive(customScheme);
    }
  }

  @Override @Deprecated public void startImageRecognition() {
    throw new Error("Operation is not implemented.");
  }

  @Override public void startScanner() {
    orchextra.openScanner();
  }

  @Override public void init(final Application app, Config config) {
    this.app = app;
    this.config = config;

    orchextra.setStatusListener(isReady -> {
      if (isReady) {
        orchextra.getTriggerManager().setScanner(OxScannerImp.Factory.create(app));
        orchextra.getTriggerManager().setGeofence(OxGeofenceImp.Factory.create(app));
        //orchextra.getTriggerManager()
        //    .setIndoorPositioning(OxIndoorPositioningImp.Factory.create(app));
        orchextra.setNotificationActivityClass(config.getNotificationActivityClass());

        orchextraCompletionCallback.onSuccess();
        Ox3ManagerImp.this.getToken();
      } else {
        orchextraCompletionCallback.onError("SDK isn't ready");
      }
    });

    orchextra.setErrorListener(error -> orchextraCompletionCallback.onError(Integer.toString(error.getCode())));
    orchextra.setCustomActionListener(
        customSchema -> onCustomSchemeReceiver.onReceive(customSchema));
  }

  @Override public void getToken() {
    orchextra.getToken(oxToken -> orchextraCompletionCallback.onConfigurationReceive(oxToken));
  }

  @Override public void bindUser(CrmUser crmUser) {
    // TODO bindUser in ox3
  }

  @Override public void unBindUser() {
    throw new Error("Operation is not implemented.");
  }

  @Override public void bindDevice(String businessUnit) {
    List<String> bussinessUnits = new ArrayList();
    bussinessUnits.add(businessUnit);
    if (orchextra.isReady()) {
      orchextra.getCrmManager().setDeviceData(null, bussinessUnits);
    }
  }

  @Override public void unBindDevice() {
    throw new Error("Operation is not implemented.");
  }

  @Override public void setOnCustomSchemeReceiver(OnCustomSchemeReceiver onCustomSchemeReceiver) {
    this.onCustomSchemeReceiver = onCustomSchemeReceiver;
  }

  @Override public void start() {
    Log.wtf(TAG, "Ox3ManagerImp#start()");
  }

  @Override public void stop() {
    orchextra.finish();
  }

  @Override
  public void updateSDKCredentials(String apiKey, String apiSecret, boolean forceCallback) {
    this.config.apiKey = apiKey;
    this.config.apiSecret = apiSecret;
    initOx();
  }

  private void initOx() {
    orchextraCompletionCallback = config.orchextraCompletionCallback;

    OrchextraOptions options = new OrchextraOptions.Builder()
        //.firebaseApiKey("AIzaSyDlMIjwx2r0oc0W7O4WPb7CvRhjCVHOZBk")
        //.firebaseApplicationId("1:327008883283:android:5a0b51c3ef8892e0")
        .debuggable(true).build();

    orchextra.init(app, config.getApiKey(), config.getApiSecret(), options);
    orchextra.setScanTime(30);
  }
}
