package com.gigigo.orchextra.core.data.api.dto.content

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElement

@Entity(tableName = "sections")
class ApiContentItem(
    @NonNull @PrimaryKey val slug: String,
    val type: String?,
    val tags: List<String>?,
    val layout: ApiContentItemLayout?,
    val elements: List<ApiElement>?)