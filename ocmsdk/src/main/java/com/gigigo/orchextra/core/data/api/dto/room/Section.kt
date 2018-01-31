package com.gigigo.orchextra.core.data.api.dto.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "section",

    foreignKeys = [
      (ForeignKey(
          entity = Menu::class,
          parentColumns = arrayOf("id"),
          childColumns = arrayOf("menu_id"),
          onDelete = CASCADE)),
      (ForeignKey(
          entity = SectionView::class,
          parentColumns = arrayOf("id"),
          childColumns = arrayOf("section_view_id")))])
class Section(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @NonNull @ColumnInfo(name = "menu_id") val menuId: Long,
    @NonNull @ColumnInfo(name = "element_url") val elementUrl: String,
    @NonNull val slug: String,
    @NonNull @ColumnInfo(name = "section_view_id") val sectionViewId: Long)
