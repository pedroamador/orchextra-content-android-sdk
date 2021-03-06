package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.gigigo.orchextra.core.domain.entities.article.ArticleButtonElement;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;

public class ArticleButtonView extends ArticleBaseView<ArticleButtonElement> {

  private final ImageLoader imageLoader;

  private TextView articleTextButton;
  private ImageView articleImageButton;

  public ArticleButtonView(Context context, ArticleButtonElement articleElement,
      ImageLoader imageLoader) {
    super(context, articleElement);
    this.imageLoader = imageLoader;
  }

  @Override protected int getViewLayout() {
    return R.layout.view_article_button_item;
  }

  @Override protected void bindViews() {
    articleTextButton = (TextView) itemView.findViewById(R.id.articleTextButton);
    articleImageButton = (ImageView) itemView.findViewById(R.id.articleImageButton);
  }

  @Override protected void bindTo(ArticleButtonElement articleElement) {
    switch (articleElement.getType()) {
      case IMAGE:
        bindImageButton(articleElement);
        break;
      case DEFAULT:
        bindTextButton(articleElement);
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP) private void bindTextButton(final ArticleButtonElement articleElement) {
    articleTextButton.setVisibility(VISIBLE);

    articleTextButton.setText(articleElement.getText());

    try {
      articleTextButton.setTextColor(Color.parseColor(articleElement.getTextColor()));
      articleTextButton.setBackgroundColor(Color.parseColor(articleElement.getBgColor()));
    } catch (Exception ignored) {
    }

    LayoutParams lp = getLayoutParams(articleElement);
    articleTextButton.setLayoutParams(lp);

    articleTextButton.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        processClickListener(articleElement.getElementUrl());
      }
    });
  }

  @NonNull private LayoutParams getLayoutParams(ArticleButtonElement articleElement) {
    int paddingRes = 0;
    switch (articleElement.getSize()) {
      case BIG:
        paddingRes = R.dimen.ocm_margin_article_big_button;
        break;
      case MEDIUM:
        paddingRes = R.dimen.ocm_margin_article_medium_button;
        break;
      case SMALL:
        paddingRes = R.dimen.ocm_margin_article_small_button;
        break;
    }
    int paddingHeight = (int) getResources().getDimension(R.dimen.ocm_height_article_button);
    int padding = (int) getResources().getDimension(paddingRes);
    LayoutParams lp =
        new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, paddingHeight);
    lp.setMargins(padding, 0, padding, 0);
    return lp;
  }

  private void bindImageButton(final ArticleButtonElement articleElement) {
    articleImageButton.setVisibility(VISIBLE);

    imageLoader.load(articleElement.getImageUrl()).into(articleImageButton);

    articleImageButton.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        processClickListener(articleElement.getElementUrl());
      }
    });
  }

  private void processClickListener(String elementUrl) {
    if (elementUrl != null) {
      Ocm.processDeepLinks(elementUrl);
    }
  }
}
