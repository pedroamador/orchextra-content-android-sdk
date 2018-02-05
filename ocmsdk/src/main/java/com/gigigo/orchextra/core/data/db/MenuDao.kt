package com.gigigo.orchextra.core.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.gigigo.orchextra.core.data.vo.Menu
import com.gigigo.orchextra.core.data.vo.MenuWithSections

@Dao
interface MenuDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(menus: List<Menu>)

  @Query("SELECT * FROM menu WHERE id = :menuId")
  fun get(menuId: Long): MenuWithSections

  @Query("DELETE FROM menu WHERE id = :menuId")
  fun delete(menuId: Long)
}
