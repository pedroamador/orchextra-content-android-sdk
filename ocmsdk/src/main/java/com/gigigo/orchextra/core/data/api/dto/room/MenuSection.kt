package com.gigigo.orchextra.core.data.api.dto.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "menu_section")
data class MenuSection(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "menu_id") val menuId: Long,
    @ColumnInfo(name = "section_id") val sectionId: Long)
