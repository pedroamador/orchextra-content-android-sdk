package com.gigigo.orchextra.core.data.api.dto.elementcache

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import com.gigigo.orchextra.core.data.api.dto.article.ApiArticleElement

@Entity(tableName = "cached_elements_render")
class ApiElementCacheRender(
    @ColumnInfo(name = "content_url") val contentUrl: String?,
    val url: String?,
    val title: String?,
    val elements: List<ApiArticleElement>?,
    @ColumnInfo(name = "scheme_uri") val schemeUri: String?,
    val source: String?,
    val format: String?,
    @ColumnInfo(name = "federated_auth") var federatedAuth: FederatedAuthorizationData?)