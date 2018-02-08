package com.gigigo.orchextra.wrapper

import android.app.Application
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver

interface OxManager {

  fun startImageRecognition()

  fun startScanner()

  fun init(app: Application, config: OxConfig, callback: OrchextraCompletionCallback)

  fun getToken()

  fun bindUser(crmUser: CrmUser)

  fun unBindUser()

  fun bindDevice(device: String)

  fun unBindDevice()

  fun setOnCustomSchemeReceiver(onCustomSchemeReceiver: OnCustomSchemeReceiver)

  fun callOnCustomSchemeReceiver(customScheme: String)

  fun start()

  fun stop()

  fun updateSDKCredentials(apiKey: String, apiSecret: String, forceCallback: Boolean)
}