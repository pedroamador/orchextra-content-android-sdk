package com.gigigo.orchextra.core.sdk.model.grid.horizontalviewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.orchextra.core.controller.dto.CellCarouselContentData;
import com.gigigo.ui.imageloader.ImageLoader;
import java.util.List;

public class HorizontalViewPagerAdapter extends FragmentStatePagerAdapter {

  private final ImageLoader imageLoader;
  private List<Cell> cellDataList;
  private OnClickItemListener onClickItemListener;

  public HorizontalViewPagerAdapter(FragmentManager fm, ImageLoader imageLoader) {
    super(fm);
    this.imageLoader = imageLoader;
  }

  public HorizontalViewPagerAdapter(FragmentManager fm, ImageLoader imageLoader, List<Cell> cellDataList) {
    super(fm);
    this.cellDataList = cellDataList;
    this.imageLoader = imageLoader;
  }

  @Override public Fragment getItem(final int position) {
    CellCarouselContentData cell = (CellCarouselContentData) cellDataList.get(position);

    HorizontalItemPageFragment horizontalItemPageFragment =
        HorizontalItemPageFragment.newInstance();
    horizontalItemPageFragment.setImageLoader(imageLoader);
    horizontalItemPageFragment.setCell(cell);
    horizontalItemPageFragment.setOnClickItemListener(new HorizontalItemPageFragment.OnClickItemListener() {
      @Override public void onClickItem(View view) {
        if (onClickItemListener != null) {
          onClickItemListener.onClickItem(position, view);
        }
      }
    });

    return horizontalItemPageFragment;
  }

  @Override public int getCount() {
    return cellDataList != null ? cellDataList.size() : 0;
  }

  public void setItems(List<Cell> cellDataList) {
    this.cellDataList = cellDataList;
    notifyDataSetChanged();
  }

  public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
    this.onClickItemListener = onClickItemListener;
  }

  public interface OnClickItemListener {
    void onClickItem(int position, View view);
  }
}
