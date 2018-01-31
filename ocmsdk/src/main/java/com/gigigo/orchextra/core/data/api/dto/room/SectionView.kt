package com.gigigo.orchextra.core.data.api.dto.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "section_view",

    indices = arrayOf(Index(name = "idx_section_view_text", value = "text")))
class SectionView(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val text: String,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "image_thumb") val imageThumb: String?)
