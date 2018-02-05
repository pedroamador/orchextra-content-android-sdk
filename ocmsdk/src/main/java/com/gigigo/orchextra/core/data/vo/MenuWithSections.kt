package com.gigigo.orchextra.core.data.vo

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

data class MenuWithSections(

    @Embedded val menu: Menu,

    @Relation(
        parentColumn = "id",
        entityColumn = "menu_id",
        entity = Section::class
    ) val sections: List<Section>
)
