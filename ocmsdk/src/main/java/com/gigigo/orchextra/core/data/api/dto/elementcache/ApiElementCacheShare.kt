package com.gigigo.orchextra.core.data.api.dto.elementcache

import android.arch.persistence.room.Entity

@Entity(tableName = "cached_elements_share")
class ApiElementCacheShare(
    val url: String?,
    val text: String?)