package com.gigigo.orchextra.core.data.api.dto.article

import android.arch.persistence.room.ColumnInfo

class ApiArticleElement(
    val type: String?,
    @ColumnInfo(name = "custom_properties") val customProperties: Map<String, Any>?,
    val render: ApiArticleElementRender?)