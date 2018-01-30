package com.gigigo.orchextra.core.data.api.dto.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "tag",
    indices = [(Index(name = "idx_tag_name", value = "name", unique = true))])
class Tag(
    @NonNull @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String)
