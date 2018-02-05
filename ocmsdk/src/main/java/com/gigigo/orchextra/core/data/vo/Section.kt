package com.gigigo.orchextra.core.data.vo

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
          onDelete = CASCADE))])
class Section(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @NonNull @ColumnInfo(name = "menu_id") val menuId: Long,
    @NonNull @ColumnInfo(name = "element_url") val elementUrl: String,
    @NonNull val slug: String,
    @NonNull val type: String,
    @NonNull @ColumnInfo(name = "updated_at") val updatedAt: Long)
