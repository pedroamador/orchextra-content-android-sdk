package com.gigigo.orchextra.core.data.api.dto.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "tags")
class Tags(
    @NonNull @PrimaryKey(autoGenerate = true)
    val id: Integer,
    val name: String)
