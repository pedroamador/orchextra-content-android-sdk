package com.gigigo.sample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

  private List<UiMenu> menuContent;

  public ScreenSlidePagerAdapter(FragmentManager fm) {
    super(fm);
    menuContent = new ArrayList<>();
  }

  @Override public Fragment getItem(int position) {
    UiMenu menu = menuContent.get(position);

    return ScreenSlidePageFragment.newInstance(menu.getElementUrl(),
        getNumberOfImagesToDownload(position));
  }

  public void setDataItems(Collection<UiMenu> collection) {
    menuContent.clear();
    menuContent.addAll(collection);
    notifyDataSetChanged();
  }

  @Override public int getItemPosition(Object object) {
    if (object instanceof ScreenSlidePageFragment) {
      ((ScreenSlidePageFragment) object).reloadSection();
    }
    return super.getItemPosition(object);
  }

  @Override public int getCount() {
    return menuContent.size();
  }

  private int getNumberOfImagesToDownload(int position) {
    int number = 12;
    if (position == 0) {
      number = 12;
    } else if (position == 1 || position == 2) number = 6;
    return number;
  }
}