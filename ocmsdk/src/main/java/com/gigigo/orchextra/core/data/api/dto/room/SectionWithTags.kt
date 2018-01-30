package com.gigigo.orchextra.core.data.api.dto.room

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

data class SectionWithTags(
    @Embedded
    val section: Section,
    @Relation(
        parentColumn = "id",
        entityColumn = "tag_id",
        entity = SectionTag::class,
        projection = ["name"]
    ) val tagIdList: List<Long>
)