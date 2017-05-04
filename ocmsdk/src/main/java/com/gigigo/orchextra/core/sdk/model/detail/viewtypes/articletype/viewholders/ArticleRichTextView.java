package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;
import javax.inject.Inject;

public class ArticleRichTextView extends ArticleBaseView<ArticleRichTextElement> {

  private TextView articleRichText;

  private OcmContextProvider ocmContextProvider;

  public ArticleRichTextView(Context context, ArticleRichTextElement articleElement) {
    super(context, articleElement);
  }

  @Override protected int getViewLayout() {
    return R.layout.view_article_rich_text_item;
  }

  @Override protected void bindViews() {
    articleRichText = (TextView) itemView.findViewById(R.id.article_rich_text);
    ocmContextProvider = OCManager.getOcmContextProvider();
  }


  @Override protected void bindTo(ArticleRichTextElement articleElement) {
    if (!TextUtils.isEmpty(articleElement.getHtml())) {
      setTextViewHTML(articleRichText, articleElement.getHtml());
    }
  }

  protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
    int start = strBuilder.getSpanStart(span);
    int end = strBuilder.getSpanEnd(span);
    int flags = strBuilder.getSpanFlags(span);
    ClickableSpan clickable = new ClickableSpan() {
      public void onClick(View view) {
        openChromeTabs(span.getURL());
      }
    };
    strBuilder.setSpan(clickable, start, end, flags);
    strBuilder.removeSpan(span);
  }

  protected void setTextViewHTML(TextView text, String html) {
    CharSequence sequence = Html.fromHtml(html);
    SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
    URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
    for (URLSpan span : urls) {
      makeLinkClickable(strBuilder, span);
    }
    text.setText(strBuilder);
    text.setMovementMethod(LinkMovementMethod.getInstance());
  }

  private void openChromeTabs(String url) {
    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
    builder.setToolbarColor(getContext().getResources().getColor(R.color.oc_background_detail_toolbar));
    //builder.setCloseButtonIcon(BitmapFactory.decodeResource(
    //    getResources(), android.R.drawable.ic_menu_b));
    CustomTabsIntent customTabsIntent = builder.build();
    customTabsIntent.launchUrl(ocmContextProvider.getCurrentActivity(), Uri.parse(url));
  }
}