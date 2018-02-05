package com.gigigo.orchextra.core.data.vo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "section_share",

    foreignKeys = [
      (ForeignKey(
          entity = Section::class,
          parentColumns = arrayOf("id"),
          childColumns = arrayOf("section_id"),
          onDelete = ForeignKey.CASCADE))])
class SectionShare(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @NonNull @ColumnInfo(name = "section_id") val sectionId: Long,
    @NonNull val url: String,
    val text: String = "",
    @ColumnInfo(name = "share_landing_url") val shareLandingUrl: String = "")
