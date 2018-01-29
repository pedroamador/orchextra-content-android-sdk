package com.gigigo.orchextra.core.data.api.dto.menus

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "menus")
data class Menu(
    @NonNull @PrimaryKey @ColumnInfo(name = "id")
    val _id: String?,
    val slug: String?)