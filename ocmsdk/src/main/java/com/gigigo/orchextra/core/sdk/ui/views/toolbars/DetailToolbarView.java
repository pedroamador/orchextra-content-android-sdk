package com.gigigo.orchextra.core.sdk.ui.views.toolbars;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.OcmEvent;
import com.gigigo.orchextra.ocmsdk.R;

public class DetailToolbarView extends FrameLayout {

  private final Context context;

  private View detailToolbar;
  private TextView detailTitleText;
  private View backToolbarButton;
  private View shareToolbarButton;
  private View backToolbarBgButton;
  private View shareToolbarBgButton;

  private boolean isBlocked;
  private String title;

  private boolean isFirstScrollPreview;
  private boolean isFirstScrollFull;

  public DetailToolbarView(@NonNull Context context) {
    super(context);
    this.context = context;

    init();
  }

  public DetailToolbarView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    this.context = context;

    init();
  }

  public DetailToolbarView(@NonNull Context context, @Nullable AttributeSet attrs,
      int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;

    init();
  }

  private void init() {
    initViews();
    setToolbarTitle();
  }

  private void setToolbarTitle() {
    detailTitleText.setText(title);
  }

  private void initViews() {
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.view_detail_toolbar_layout, this, true);

    detailToolbar = view.findViewById(R.id.detailToolbar);
    detailTitleText = (TextView) view.findViewById(R.id.detailTitleText);
    backToolbarButton = view.findViewById(R.id.back_toolbar_button);
    shareToolbarButton = view.findViewById(R.id.share_toolbar_button);
    backToolbarBgButton = view.findViewById(R.id.back_toolbar_bg_button);
    shareToolbarBgButton = view.findViewById(R.id.share_bg_toolbar_button);

    isFirstScrollPreview = true;
    isFirstScrollFull = true;
  }

  public void switchBetweenButtonAndToolbar(boolean areVisibleToolbar) {
    switchBetweenButtonAndToolbar(areVisibleToolbar, false);
  }

  public void switchBetweenButtonAndToolbar(boolean areVisibleToolbar, boolean forceChange) {
    boolean hasToChangeViews = (detailToolbar.getVisibility() == GONE && areVisibleToolbar)
        || (detailToolbar.getVisibility() == VISIBLE && !areVisibleToolbar);

    if (hasToChangeViews && !isBlocked || hasToChangeViews && forceChange) {

      detailToolbar.setVisibility(areVisibleToolbar ? View.VISIBLE : View.GONE);

      backToolbarBgButton.setVisibility(!areVisibleToolbar ? View.VISIBLE : View.INVISIBLE);

      shareToolbarBgButton.setVisibility(!areVisibleToolbar ? View.VISIBLE : View.INVISIBLE);

      detailTitleText.setVisibility(areVisibleToolbar ? View.VISIBLE : View.GONE);

      if (isFirstScrollFull && areVisibleToolbar) {
        OCManager.notifyEvent(OcmEvent.CONTENT_FULL);
      } else if (isFirstScrollPreview && !areVisibleToolbar) {
        OCManager.notifyEvent(OcmEvent.CONTENT_PREVIEW);
      }
    }
  }

  public void setOnClickBackButtonListener(OnClickListener onClickBackButtonListener) {
    backToolbarButton.setOnClickListener(onClickBackButtonListener);
  }

  public void setOnClickShareButtonListener(OnClickListener onClickShareButtonListener) {
    shareToolbarButton.setOnClickListener(onClickShareButtonListener);
  }

  public void setShareButtonVisible(boolean shareButtonVisible) {
    shareToolbarButton.setVisibility(shareButtonVisible ? View.VISIBLE : View.GONE);
  }

  public void blockSwipeEvents(boolean isBlocked) {
    this.isBlocked = isBlocked;
  }

  public void setToolbarTitle(String title) {
    this.title = title;
    setToolbarTitle();
  }
}
