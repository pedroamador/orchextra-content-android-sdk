package com.gigigo.orchextra.core.data.api.dto.article

import android.arch.persistence.room.ColumnInfo

class ApiArticleElementRender(
    val text: String? = null,
    @ColumnInfo(name = "image_url") val imageUrl: String? = null,
    @ColumnInfo(name = "element_url") val elementUrl: String? = null,
    val html: String? = null,
    val format: String? = null,
    val source: String? = null,
    @ColumnInfo(name = "image_thumb") val imageThumb: String? = null,
    val ratios: List<Float>? = null,
    val type: String? = null,
    val size: String? = null,
    @ColumnInfo(name = "text_color") val textColor: String? = null,
    @ColumnInfo(name = "background_color") val bgColor: String? = null)