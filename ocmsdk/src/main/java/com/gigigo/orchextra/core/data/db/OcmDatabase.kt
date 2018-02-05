package com.gigigo.orchextra.core.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.gigigo.orchextra.core.data.vo.*

@Database(
    entities = [(Menu::class), (Section::class), (MenuWithSections::class),
      (SectionRender::class), (SectionRenderWithInfo::class), (SectionShare::class),
      (SectionView::class), (Tag::class), (CustomProperty::class), (Element::class),
      (FederatedAuthorization::class), (FederatedAuthorizationKeys::class),
      (FederatedAuthorizationWithKeys::class), (SectionWithInfo::class)],
    version = 1,
    exportSchema = false
)
abstract class OcmDatabase : RoomDatabase() {
  companion object {
    private val DEFAULT_DATABASE_NAME = "ocm.db"

    fun create(context: Context, useInMemory: Boolean,
        dbName: String = DEFAULT_DATABASE_NAME): OcmDatabase {
      val databaseBuilder = if (useInMemory) {
        Room.inMemoryDatabaseBuilder(context, OcmDatabase::class.java)
      } else {
        Room.databaseBuilder(context, OcmDatabase::class.java, dbName)
      }
      return databaseBuilder
          .fallbackToDestructiveMigration()
          .build()
    }
  }

  abstract fun menus(): MenuDao
}