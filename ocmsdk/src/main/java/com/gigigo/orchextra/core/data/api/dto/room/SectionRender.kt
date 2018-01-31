package com.gigigo.orchextra.core.data.api.dto.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "section_render",

    foreignKeys = [
      (ForeignKey(
          entity = Section::class,
          parentColumns = arrayOf("id"),
          childColumns = arrayOf("section_id"),
          onDelete = ForeignKey.CASCADE))])
class SectionRender(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @NonNull @ColumnInfo(name = "section_id") val sectionId: Long,
    // TODO: This field is not mandatory
    @ColumnInfo(name = "content_url") val contentUrl: String?,
    // TODO: This field is not mandatory
    val linked: Boolean?,
    // TODO: This field is not mandatory
    val url: String?)
