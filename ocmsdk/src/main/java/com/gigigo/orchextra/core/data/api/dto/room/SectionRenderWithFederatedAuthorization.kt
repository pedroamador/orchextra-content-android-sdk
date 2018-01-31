package com.gigigo.orchextra.core.data.api.dto.room

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

data class SectionRenderWithFederatedAuthorization(

    @Embedded val id: FederatedAuthorization,

    @Relation(
        parentColumn = "id",
        entityColumn = "section_render_id",
        entity = FederatedAuthorization::class
    ) val federatedAuth: FederatedAuthorizationKeys)
