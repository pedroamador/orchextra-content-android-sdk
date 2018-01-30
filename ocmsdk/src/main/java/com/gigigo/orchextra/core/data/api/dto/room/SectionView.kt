package com.gigigo.orchextra.core.data.api.dto.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "section_view")
class SectionView(
    @NonNull @PrimaryKey @ColumnInfo(name = "id") val text: String,
    val imageUrl: String?,
    val imageThumb: String?)