package com.gigigo.orchextra.core.data.vo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "federated_authorization_keys",

    foreignKeys = [
      (ForeignKey(
          entity = FederatedAuthorization::class,
          parentColumns = arrayOf("id"),
          childColumns = arrayOf("federated_authorization_id"),
          onDelete = ForeignKey.CASCADE))])
class FederatedAuthorizationKeys(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @NonNull @ColumnInfo(name = "federated_authorization_id") val federatedAuthorizationId: Long,
    @NonNull @ColumnInfo(name = "site_name") val siteName: String)
