package com.gigigo.orchextra.core.sdk.model.grid.horizontalviewpager;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.gigigo.orchextra.core.controller.dto.CellCarouselContentData;
import com.gigigo.orchextra.ocm.views.UiListedBaseContentData;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;
import com.gigigo.ui.imageloader.ImageLoaderCallback;
import com.vuforia.Frame;

public class HorizontalItemPageFragment extends Fragment {

  private ImageLoader imageLoader;
  private CellCarouselContentData cell;
  private ImageView horizontalItemImageView;
  private OnClickHorizontalItem onClickHorizontalItem;
  private View horizontalItemContainer;
  private ImageView imageToExpandInDetail;

  public static HorizontalItemPageFragment newInstance() {
    return new HorizontalItemPageFragment();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_horizontal_item_pager_view, container, false);

    initViews(view);

    return view;
  }

  private void initViews(View view) {
    horizontalItemContainer = view.findViewById(R.id.horizontalItemContainer);
    horizontalItemImageView = (ImageView) view.findViewById(R.id.horizontalItemImageView);
    imageToExpandInDetail = (ImageView) view.findViewById(R.id.image_to_expand_in_detail);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    setImage();
    setListeners();
  }

  private void setImage() {
    String imageUrl = cell.getData().getSectionView().getImageUrl();
    imageLoader.load(imageUrl).into(horizontalItemImageView);
  }

  private void setListeners() {
    horizontalItemImageView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (onClickHorizontalItem != null) {
          onClickHorizontalItem.onClickItem(horizontalItemContainer);


        }
      }
    });
  }



  public void setImageLoader(ImageLoader imageLoader) {
    this.imageLoader = imageLoader;
  }

  public void setCell(CellCarouselContentData cell) {
    this.cell = cell;
  }

  public void setOnClickHorizontalItem(OnClickHorizontalItem onClickHorizontalItem) {
    this.onClickHorizontalItem = onClickHorizontalItem;
  }

  public interface OnClickHorizontalItem {
    void onClickItem(View view);
  }
}
