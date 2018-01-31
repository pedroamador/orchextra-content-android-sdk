package com.gigigo.orchextra.core.data.api.dto.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "custom_property",

    foreignKeys = [
      (ForeignKey(
          entity = Section::class,
          parentColumns = arrayOf("id"),
          childColumns = arrayOf("section_id"),
          onDelete = ForeignKey.CASCADE))])
class CustomProperty(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @NonNull @ColumnInfo(name = "section_id") val sectionId: Long,
    @NonNull val name: String,
    @NonNull val value: String)
