package com.gigigo.orchextra.core.data.api.dto.room

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

data class SectionWithTags(

    @Embedded
    val section: Section,

    @Relation(
        parentColumn = "id",
        entityColumn = "section_id",
        entity = Tag::class
    ) val tags: List<Tag>
)
