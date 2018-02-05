package com.gigigo.orchextra.core.data.vo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "federated_authorization",

    foreignKeys = [
      (ForeignKey(
          entity = SectionRender::class,
          parentColumns = arrayOf("id"),
          childColumns = arrayOf("section_render_id"),
          onDelete = ForeignKey.CASCADE))])
class FederatedAuthorization(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @NonNull @ColumnInfo(name = "section_render_id") val sectionRenderId: Long,
    @NonNull val active: Boolean,
    @NonNull val type: String)
