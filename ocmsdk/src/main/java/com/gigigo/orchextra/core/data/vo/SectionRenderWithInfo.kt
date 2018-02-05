package com.gigigo.orchextra.core.data.vo

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

data class SectionRenderWithInfo(

    @Embedded val sectionRender: SectionRender,

    @Relation(
        parentColumn = "id",
        entityColumn = "section_render_id",
        entity = FederatedAuthorization::class
    ) val federatedAuth: FederatedAuthorization,

    @Relation(
        parentColumn = "id",
        entityColumn = "section_render_id",
        entity = Element::class
    ) val elements: List<Element>)
