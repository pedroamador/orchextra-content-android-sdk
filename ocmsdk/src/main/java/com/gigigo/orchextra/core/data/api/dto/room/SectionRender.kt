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
    @ColumnInfo(name = "content_url") val contentUrl: String = "",
    val linked: Boolean = false,
    val url: String = "",
    @ColumnInfo(name = "image_url") val imageUrl: String = "",
    @ColumnInfo(name = "image_thumb") val imageThumb: String = "",
    val type: String = "",
    val size: String = "",
    @ColumnInfo(name = "element_url") val elementUrl: String = "",
    val text: String = "",
    @ColumnInfo(name = "text_color") val textColor: String = "",
    @ColumnInfo(name = "background_color") val bgColor: String = "")
