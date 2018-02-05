package com.gigigo.orchextra.core.data.vo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "element",

    foreignKeys = [
      (ForeignKey(
          entity = SectionRender::class,
          parentColumns = arrayOf("id"),
          childColumns = arrayOf("section_render_parent_id"),
          onDelete = ForeignKey.CASCADE)),
      (ForeignKey(
          entity = SectionRender::class,
          parentColumns = arrayOf("id"),
          childColumns = arrayOf("section_render_child_id"),
          onDelete = ForeignKey.CASCADE))])
data class Element(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @NonNull @ColumnInfo(name = "section_render_parent_id") val sectionRenderParentId: Long,
    @NonNull @ColumnInfo(name = "section_render_child_id") val sectionRenderChildId: Long,
    @NonNull val type: String)
