package com.gigigo.orchextra.core.data.api.dto.elementcache

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity

@Entity(tableName = "cached_elements_preview")
class ApiElementCachePreview(
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    val text: String?,
    val behaviour: String?,
    @ColumnInfo(name = "image_thumb") val imageThumb: String?)

