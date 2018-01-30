package com.gigigo.orchextra.core.data.api.dto.room

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

data class MenuWithSections(
    @Embedded
    val menu: Menu,
    @Relation(
        parentColumn = "id",
        entityColumn = "section_id",
        entity = MenuSection::class
    ) val sectionIdList: List<Long>
)
