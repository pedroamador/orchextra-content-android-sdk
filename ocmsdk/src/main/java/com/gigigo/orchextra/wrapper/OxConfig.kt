package com.gigigo.orchextra.wrapper

import android.app.Activity

data class OxConfig(
    val apiKey: String = "",
    val apiSecret: String = "",
    val senderId: String = "",
    val notificationActivityClass: Class<Activity>? = null)