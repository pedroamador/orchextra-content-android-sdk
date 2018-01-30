package com.gigigo.orchextra.core.data.api.dto.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementSegmentation

@Entity(tableName = "section",
    indices = [(Index(name = "idx_section_element_url", value = "element_url", unique = true)),
    (Index(name = "idx_section_slug", value = "slug", unique = true))],
    foreignKeys = [(ForeignKey(
        entity = SectionView::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("label")))])
class Section(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @NonNull @ColumnInfo(name = "element_url") val elementUrl: String,
    @NonNull val slug: String,
    @ColumnInfo(name = "label") val sectionView: String,
    @ColumnInfo(name = "custom_properties") val customProperties: Map<String, Any>?,
    val segmentation: ApiElementSegmentation?)
