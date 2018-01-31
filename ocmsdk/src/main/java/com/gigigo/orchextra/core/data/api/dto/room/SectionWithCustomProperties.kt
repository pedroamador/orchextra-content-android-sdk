package com.gigigo.orchextra.core.data.api.dto.room

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

data class SectionWithCustomProperties(

    @Embedded
    val section: Section,

    @Relation(
        parentColumn = "id",
        entityColumn = "section_id",
        entity = CustomProperty::class
    ) val tags: List<CustomProperty>
)
