package com.gigigo.orchextra.core.data.api.dto.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "section_tag")
data class SectionTag(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "section_id") val sectionId: Long,
    @ColumnInfo(name = "tag_id") val tagId: Long)
