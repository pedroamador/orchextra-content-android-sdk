package com.gigigo.orchextra.core.data.api.dto.room

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

data class FederatedAuthorizationWithKeys(

    @Embedded val federatedAuthorization: FederatedAuthorization,

    @Relation(
        parentColumn = "id",
        entityColumn = "federated_authorization_id",
        entity = FederatedAuthorizationKeys::class
    ) val keys: FederatedAuthorizationKeys)
