package com.gigigo.orchextra.core.data.api.dto.elementcache

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementSegmentation

@Entity(tableName = "cached_elements")
class ApiElementCache(
    @NonNull @PrimaryKey val slug: String,
    val type: String? = null,
    val tags: List<String>? = null,
    val preview: ApiElementCachePreview? = null,
    val render: ApiElementCacheRender? = null,
    val share: ApiElementCacheShare? = null,
    val segmentation: ApiElementSegmentation? = null,
    val name: String? = null,
    @ColumnInfo(name = "updated_at") var updatedAt: Long = 0)
