package com.gigigo.orchextra.core.data.api.dto.elements

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "elements")
class ApiElement(
    val tags: List<String>?,
    val segmentation: ApiElementSegmentation?,
    @ColumnInfo(name = "custom_properties") val customProperties: Map<String, Any>?,
    @ColumnInfo(name = "section_view") val sectionView: ApiElementSectionView?,
    //TODO: Should be the PK?
    @NonNull @PrimaryKey val slug: String,
    @ColumnInfo(name = "element_url") val elementUrl: String?,
    val name: String?,
    val dates: List<List<String>>?)
