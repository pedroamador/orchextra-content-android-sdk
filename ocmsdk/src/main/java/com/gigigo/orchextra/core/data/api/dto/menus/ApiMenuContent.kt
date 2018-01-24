package com.gigigo.orchextra.core.data.api.dto.menus

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElement

@Entity(tableName = "menus")
data class ApiMenuContent(
    @NonNull @PrimaryKey @ColumnInfo(name = "id") val _id: String?,
    val slug: String?,
    val elements: List<ApiElement>?)