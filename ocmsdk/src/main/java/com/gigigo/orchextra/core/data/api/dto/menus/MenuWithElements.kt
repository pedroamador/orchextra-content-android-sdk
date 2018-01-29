package com.gigigo.orchextra.core.data.api.dto.menus

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElement
import com.gigigo.orchextra.core.data.api.dto.elements.Element

data class MenuWithElements(
    @Embedded
    val menu: Menu,
    @Relation(
        parentColumn = "id",
        entityColumn = "slug",
        entity = Element::class
    ) val elementList: List<ApiElement>?
)
