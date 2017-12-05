package com.gigigo.orchextra.core.data.api.mappers.elements;

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementSegmentation;
import com.gigigo.orchextra.core.domain.entities.elements.ElementSegmentation;
import com.gigigo.orchextra.core.domain.entities.menus.RequiredAuthoritation;

public class ApiElementSegmentationMapper
    implements ExternalClassToModelMapper<ApiElementSegmentation, ElementSegmentation> {

  @Override public ElementSegmentation externalClassToModel(ApiElementSegmentation data) {
    ElementSegmentation model = new ElementSegmentation();

    model.setRequiredAuth(RequiredAuthoritation.convert(data.getRequiredAuth()));

    return model;
  }
}
