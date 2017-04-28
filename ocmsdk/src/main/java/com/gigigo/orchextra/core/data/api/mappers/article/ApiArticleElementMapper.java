package com.gigigo.orchextra.core.data.api.mappers.article;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.core.data.api.dto.article.ApiArticleElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleButtonElement;
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleButtonSize;
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleButtonType;
import com.gigigo.orchextra.core.domain.entities.article.ArticleHeaderElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleVideoElement;
import com.gigigo.orchextra.core.data.api.dto.article.ApiArticleElementRender;
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleTypeSection;

public class ApiArticleElementMapper implements
    ExternalClassToModelMapper<ApiArticleElement, ArticleElement> {

  @Override public ArticleElement externalClassToModel(ApiArticleElement data) {

    String apiType = data.getType();
    ArticleTypeSection articleTypeSection = ArticleTypeSection.convertStringToEnum(apiType);

    ApiArticleElementRender render = data.getRender();

    if (render != null) {
      return convertArticleByType(articleTypeSection, render);
    }

    return null;
  }

  @Nullable private ArticleElement convertArticleByType(ArticleTypeSection articleTypeSection,
      ApiArticleElementRender render) {
    switch (articleTypeSection) {
      case HEADER:
        return getArticleHeaderElement(render);
      case IMAGE:
        return getArticleImageElement(render);
      case VIDEO:
        return getArticleVideoElement(render);
      case RICH_TEXT:
        return getArticleRichTextElement(render);
      case BUTTON:
        return getArticleButtonElement(render);
    }
    return null;
  }

  @NonNull private ArticleElement getArticleVideoElement(ApiArticleElementRender render) {
    ArticleVideoElement articleVideoElement = new ArticleVideoElement();
    articleVideoElement.setImageUrl(render.getImageUrl());
    articleVideoElement.setFormat(render.getFormat());
    articleVideoElement.setSource(render.getSource());
    return articleVideoElement;
  }

  @NonNull private ArticleElement getArticleRichTextElement(ApiArticleElementRender render) {
    ArticleRichTextElement articleRichTextElement = new ArticleRichTextElement();
    articleRichTextElement.setHtml(render.getText());
    return articleRichTextElement;
  }

  @NonNull private ArticleElement getArticleImageElement(ApiArticleElementRender data) {
    ArticleImageElement articleImageElement = new ArticleImageElement();
    articleImageElement.setImageUrl(data.getImageUrl());
    articleImageElement.setImageThumb(data.getImageThumb());
    articleImageElement.setElementUrl(data.getElementUrl());
    return articleImageElement;
  }

  @NonNull private ArticleElement getArticleHeaderElement(ApiArticleElementRender data) {
    ArticleHeaderElement articleHeaderElement = new ArticleHeaderElement();
    articleHeaderElement.setHtml(data.getText());
    articleHeaderElement.setImageUrl(data.getImageUrl());
    articleHeaderElement.setImageThumb(data.getImageThumb());
    return articleHeaderElement;
  }

  @NonNull private ArticleElement getArticleButtonElement(ApiArticleElementRender render) {
    ArticleButtonElement articleButtonElement = new ArticleButtonElement();
    articleButtonElement.setType(ArticleButtonType.convertStringToEnum(render.getType()));
    articleButtonElement.setSize(ArticleButtonSize.convertStringToEnum(render.getSize()));
    articleButtonElement.setElementUrl(render.getElementUrl());
    articleButtonElement.setText(render.getText());
    articleButtonElement.setTextColor(render.getTextColor());
    articleButtonElement.setBgColor(render.getBgColor());
    articleButtonElement.setImageUrl(render.getImageUrl());
    return articleButtonElement;
  }
}
