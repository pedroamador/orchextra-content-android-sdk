package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Priority;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.orchextra.core.data.rxCache.imageCache.loader.OcmImageLoader;
import com.gigigo.orchextra.core.domain.entities.article.ArticleButtonElement;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocmsdk.R;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ArticleButtonView extends BaseViewHolder<ArticleButtonElement> {

  private final Context context;
  //TODO: what's FA?? more descriptive name
  private final FrameLayout flFA;
  private final ProgressBar faLoading;
  private TextView articleTextButton;
  private ImageView articleImageButton;

  public ArticleButtonView(Context context, ViewGroup parent, FrameLayout flFA) {
    super(context, parent, R.layout.view_article_button_item);

    this.context = context;
    this.flFA = flFA;
    faLoading = (ProgressBar) flFA.findViewById(R.id.progressFA);

    articleTextButton = (TextView) itemView.findViewById(R.id.articleTextButton);
    articleImageButton = (ImageView) itemView.findViewById(R.id.articleImageButton);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void bindTextButton(final ArticleButtonElement articleElement) {
    articleTextButton.setVisibility(View.VISIBLE);

    articleTextButton.setText(articleElement.getText());

    try {
      articleTextButton.setTextColor(Color.parseColor(articleElement.getTextColor()));
      articleTextButton.setBackgroundColor(Color.parseColor(articleElement.getBgColor()));
    } catch (Exception ignored) {
    }

    ViewGroup.LayoutParams lp = getLayoutParams(articleElement);
    articleTextButton.setLayoutParams(lp);

    //TODO: two click listener? they are the same, just add it to itemView
    /*
    articleTextButton.setOnClickListener(v -> {
      flFA.setVisibility(View.VISIBLE);
      faLoading.setVisibility(View.VISIBLE);
      processClickListener(articleElement.getElementUrl());
    });
    */
  }

  @NonNull private ViewGroup.LayoutParams getLayoutParams(ArticleButtonElement articleElement) {
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
    int paddingHeight =
        (int) context.getResources().getDimension(R.dimen.ocm_height_article_button);
    int padding = (int) context.getResources().getDimension(paddingRes);
    FrameLayout.LayoutParams lp =
        new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, paddingHeight);
    lp.setMargins(padding, 0, padding, 0);
    return lp;
  }

  private void bindImageButton(final ArticleButtonElement articleElement) {
    articleImageButton.setVisibility(View.VISIBLE);

    float ratioImage = ImageGenerator.getRatioImage(articleElement.getImageUrl());

    int realWidthDevice = DeviceUtils.calculateRealWidthDeviceInImmersiveMode(context);
    int calculatedHeight = (int) (realWidthDevice / ratioImage);

    OcmImageLoader.load(context, articleElement.getImageUrl())
        .priority(Priority.NORMAL)
        .override(realWidthDevice, calculatedHeight)
        .into(articleImageButton);

    //TODO: two click listener? they are the same, just add it to itemView
    /*
    articleImageButton.setOnClickListener(v -> {
      flFA.setVisibility(View.VISIBLE);
      faLoading.setVisibility(View.VISIBLE);
      processClickListener(articleElement.getElementUrl());
    });
    */
  }

  /*
  //TODO: view just manage view, not logic in click event, propagate it to ArticleContentData
  private void processClickListener(String elementUrl) {
    if (elementUrl != null) {
      Ocm.processDeepLinks(elementUrl);
    }
  }
  */

  @Override public void bindTo(ArticleButtonElement articleButtonElement, int i) {
    switch (articleButtonElement.getType()) {
      case IMAGE:
        bindImageButton(articleButtonElement);
        break;
      case DEFAULT:
        bindTextButton(articleButtonElement);
    }
  }

  @Override public void onClick(View v) {
    flFA.setVisibility(View.VISIBLE);
    faLoading.setVisibility(View.VISIBLE);
    super.onClick(v);
  }
}
