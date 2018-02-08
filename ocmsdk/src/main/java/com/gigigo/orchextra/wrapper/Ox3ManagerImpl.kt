package com.gigigo.orchextra.wrapper

import android.app.Application
import android.util.Log
import com.gigigo.orchextra.core.Orchextra
import com.gigigo.orchextra.core.OrchextraOptions
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver


class Ox3ManagerImpl : OxManager {

  private val orchextra = Orchextra
  private lateinit var orchextraCompletionCallback: OrchextraCompletionCallback
  private lateinit var app: Application
  private var config = OxConfig()
  private val genders: HashMap<CrmUser.Gender, String> = HashMap()
  private var onCustomSchemeReceiver: OnCustomSchemeReceiver? = null

  init {
    genders[CrmUser.Gender.GenderFemale] = "female"
    genders[CrmUser.Gender.GenderMale] = "male"
    genders[CrmUser.Gender.GenderND] = "nd"
  }

  override fun startImageRecognition() = TODO("not implemented")

  override fun startScanner() = orchextra.openScanner()

  override fun init(app: Application, config: OxConfig, callback: OrchextraCompletionCallback) {

    this.app = app
    this.config = config
    this.orchextraCompletionCallback = callback
  }

  override fun getToken() {
    orchextra.getToken { oxToken -> orchextraCompletionCallback?.onConfigurationReceive(oxToken) }
  }

  override fun bindUser(crmUser: CrmUser) {
    TODO("not implemented")
  }

  override fun unBindUser() {
    TODO("not implemented")
  }

  override fun bindDevice(device: String) {
    TODO("not implemented")
  }

  override fun unBindDevice() {
    TODO("not implemented")
  }

  override fun setOnCustomSchemeReceiver(onCustomSchemeReceiver: OnCustomSchemeReceiver) {
    this.onCustomSchemeReceiver = onCustomSchemeReceiver
  }

  override fun callOnCustomSchemeReceiver(customScheme: String) {
    TODO("not implemented")
  }

  override fun start() {
    Log.wtf(TAG, "Ox3ManagerImp#start()")
  }

  override fun stop() = orchextra.finish()

  override fun updateSDKCredentials(apiKey: String, apiSecret: String, forceCallback: Boolean) {
    config = config.copy(apiKey = apiKey, apiSecret = apiSecret)
    initOx()
  }

  private fun initOx() {
    val options = OrchextraOptions.Builder()
        //.firebaseApiKey("AIzaSyDlMIjwx2r0oc0W7O4WPb7CvRhjCVHOZBk")
        //.firebaseApplicationId("1:327008883283:android:5a0b51c3ef8892e0")
        .debuggable(true).build()

    orchextra.init(app, config.apiKey, config.apiSecret, options)
    orchextra.setScanTime(30)
  }

  companion object {
    private val TAG = "Ox3ManagerImp"
  }
}