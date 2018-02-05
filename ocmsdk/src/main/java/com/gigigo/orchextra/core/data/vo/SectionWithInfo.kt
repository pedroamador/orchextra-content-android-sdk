package com.gigigo.orchextra.core.data.vo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

data class SectionWithInfo(

    @Embedded
    val section: Section,

    @Relation(
        parentColumn = "id",
        entityColumn = "section_id",
        entity = SectionView::class
    ) @ColumnInfo(name = "section_view") val sectionView: SectionView,

    @Relation(
        parentColumn = "id",
        entityColumn = "section_id",
        entity = CustomProperty::class
    ) @ColumnInfo(name = "custom_properties") val customProperties: List<CustomProperty>,

    @Relation(
        parentColumn = "id",
        entityColumn = "section_id",
        entity = Tag::class
    ) val tags: List<Tag>,

    @Relation(
        parentColumn = "id",
        entityColumn = "section_id",
        entity = SectionShare::class
    ) val share: SectionShare,

    @Relation(
        parentColumn = "id",
        entityColumn = "section_id",
        entity = SectionRender::class
    ) val render: SectionRender)
