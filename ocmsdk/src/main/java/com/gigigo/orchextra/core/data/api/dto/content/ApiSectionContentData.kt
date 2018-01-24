package com.gigigo.orchextra.core.data.api.dto.content

import android.arch.persistence.room.ColumnInfo
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCache
import com.mskn73.kache.Kacheable

class ApiSectionContentData : Kacheable {
  val content: ApiContentItem? = null
  @ColumnInfo(name = "cached_elements") val elementsCache: Map<String, ApiElementCache>? = null
  override var key: String = ""
  val version: String? = null
  @ColumnInfo(name = "expire_at") val expireAt: String? = null
  @ColumnInfo(name = "is_from_cloud")var isFromCloud: Boolean = false
}
