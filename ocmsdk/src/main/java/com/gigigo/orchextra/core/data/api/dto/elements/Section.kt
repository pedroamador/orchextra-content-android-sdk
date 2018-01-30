package com.gigigo.orchextra.core.data.api.dto.elements

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "section",
    indices = [(Index(name = "idx_slug", value = "slug", unique = true))],
    foreignKeys = [(ForeignKey(
        entity = SectionView::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("label")))])
class Section(
    @NonNull @PrimaryKey @ColumnInfo(name = "id") val elementUrl: String,
    @NonNull val slug: String,
    @ColumnInfo(name = "label") val sectionView: String,
    @ColumnInfo(name = "custom_properties") val customProperties: Map<String, Any>?,
    val tags: List<String>?,
    val segmentation: ApiElementSegmentation?)
