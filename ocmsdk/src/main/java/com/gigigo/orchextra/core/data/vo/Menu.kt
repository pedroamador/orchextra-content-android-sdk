package com.gigigo.orchextra.core.data.vo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "menu",
    indices = [(Index(name = "idx_menu_slug", value = "slug", unique = true))])
data class Menu(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @NonNull val slug: String)