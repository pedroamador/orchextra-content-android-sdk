package com.gigigo.orchextra.core.sdk.model.grid.horizontalviewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.gigigo.orchextra.core.controller.dto.CellCarouselContentData;
import com.gigigo.orchextra.core.domain.entities.elements.ElementSectionView;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;

public class HorizontalItemPageFragment extends Fragment {

  private ImageLoader imageLoader;
  private CellCarouselContentData cell;
  private ImageView horizontalItemImageView;
  private OnClickItemListener onClickItemListener;

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

  private void initViews(final View view) {
    horizontalItemImageView = (ImageView) view.findViewById(R.id.horizontalItemImageView);
    view.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (onClickItemListener != null) {
          onClickItemListener.onClickItem(view);
        }
      }
    });
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    setImage();
  }

  private void setImage() {
    if (cell != null && cell.getData() != null && cell.getData().getSectionView() != null) {
      ElementSectionView sectionView = cell.getData().getSectionView();

      String imageUrl = sectionView.getImageUrl();
      imageLoader.load(imageUrl).into(horizontalItemImageView);
    }
  }

  public void setImageLoader(ImageLoader imageLoader) {
    this.imageLoader = imageLoader;
  }

  public void setCell(CellCarouselContentData cell) {
    this.cell = cell;
  }

  public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
    this.onClickItemListener = onClickItemListener;
  }

  public interface OnClickItemListener {
    void onClickItem(View view);
  }
}
