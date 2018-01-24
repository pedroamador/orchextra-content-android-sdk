package com.gigigo.orchextra.core.data.api.dto.elementcache

import android.arch.persistence.room.Entity

@Entity(tableName = "cached_elements_render_fa")
class FederatedAuthorizationData(
    var active: Boolean = false,
    var type: String?,
    var keys: CidKeyData?)