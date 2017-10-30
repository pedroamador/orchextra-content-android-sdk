package com.gigigo.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.gigigo.orchextra.Orchextra;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.OCManagerCallbacks;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmCallbacks;
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver;
import com.gigigo.orchextra.ocm.callbacks.OnRequiredLoginCallback;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.sample.settings.SettingsActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private static final Boolean AUTO_INIT = true;
  private TabLayout tabLayout;
  private View loadingView;
  private View emptyView;
  private View errorView;
  private View networkErrorView;
  private ViewPager viewpager;
  private View newContentMainContainer;
  private List<UiMenu> menuContent;

  private OnRequiredLoginCallback onDoRequiredLoginCallback = new OnRequiredLoginCallback() {
    @Override public void doRequiredLogin() {
      Toast.makeText(getApplicationContext(), "Item needs permissions", Toast.LENGTH_SHORT).show();
    }
  };

  @Override protected void onResume() {
    super.onResume();
    //ReadedArticles
    if (OCManager.getShowReadArticles()) {
      //pagerAdapter.reloadSections();

      //Toast.makeText(this, "Refresh grid from integratied app if readed articles are enabled transform number"
      //    + OCManager.transform, Toast.LENGTH_LONG).show();
      //OCManager.transform+=1;
    }
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();

    Ocm.setOnDoRequiredLoginCallback(onDoRequiredLoginCallback);

    if (AUTO_INIT) {
      initDefaultOcm();
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    if (!AUTO_INIT) {
      getMenuInflater().inflate(R.menu.menu_main, menu);
    }
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case R.id.action_init:
        initDefaultOcm();
        return true;
      case R.id.action_clean:
        Toast.makeText(MainActivity.this, "Delete all data webStorage", Toast.LENGTH_LONG).show();
        clearDataAndGoToChangeCountryView();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void initViews() {
    tabLayout = (TabLayout) findViewById(R.id.tabLayout);
    viewpager = (ViewPager) findViewById(R.id.viewpager);
    newContentMainContainer = findViewById(R.id.newContentMainContainer);
    loadingView = findViewById(R.id.loading_view);
    emptyView = findViewById(R.id.empty_view);
    errorView = findViewById(R.id.error_view);
    networkErrorView = findViewById(R.id.network_error_view);

    ScreenSlidePagerAdapter pagerAdapter =
        new ScreenSlidePagerAdapter(getSupportFragmentManager(), new ArrayList<UiMenu>());
    viewpager.setAdapter(pagerAdapter);
    viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    errorView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getContent();
      }
    });
    emptyView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getContent();
      }
    });
    networkErrorView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getContent();
      }
    });

    ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsButton);
    settingsButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        SettingsActivity.openForResult(MainActivity.this);
      }
    });
  }

  private void initDefaultOcm() {

    if (!Utils.isOnline(getApplicationContext())) {
      showNetworkErrorView();
      return;
    }

    showLoading();
    ContentManager contentManager = ContentManager.getInstance();
    contentManager.start(new ContentManager.ContentManagerCallback<String>() {
      @Override public void onSuccess(String result) {
        getContent();
      }

      @Override public void onError(Exception exception) {
        Toast.makeText(MainActivity.this, "Credentails error: " + exception.getMessage(),
            Toast.LENGTH_SHORT).show();
      }
    });

    Ocm.setOnCustomSchemeReceiver(new OnCustomSchemeReceiver() {
      @Override public void onReceive(String customScheme) {
        Toast.makeText(MainActivity.this, customScheme, Toast.LENGTH_SHORT).show();
        Orchextra.startScannerActivity();
      }
    });
    Ocm.start();
  }

  //region clear all data
  private void clearDataAndGoToChangeCountryView() {
    clearApplicationData();
    Orchextra.stop(); //asv V.I.Code
    Ocm.clearData(true, true, new OCManagerCallbacks.Clear() {
      @Override public void onDataClearedSuccessfull() {

      }

      @Override public void onDataClearFails(Exception e) {

      }
    });
  }

  public void clearApplicationData() {
    File cache = getCacheDir();
    File appDir = new File(cache.getParent());
    if (appDir.exists()) {
      String[] children = appDir.list();
      for (String s : children) {
        if (!s.equals("lib")) {
          deleteDir(new File(appDir, s));
        }
      }
    }
  }

  public static boolean deleteDir(File dir) {
    if (dir != null && dir.isDirectory()) {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++) {
        boolean success = deleteDir(new File(dir, children[i]));
        if (!success) {
          return false;
        }
      }
    }

    return dir.delete();
  }
  //endregion

  void getContent() {
    Ocm.getMenus(true, new OcmCallbacks.Menus() {
      @Override public void onMenusLoaded(List<UiMenu> uiMenu) {
        if (uiMenu == null) {
          Toast.makeText(MainActivity.this, "menu is null", Toast.LENGTH_SHORT).show();
          showErrorView();
        } else {
          if (uiMenu.isEmpty()) {
            showEmptyView();
          } else {
            showContentView();
            tabLayout.removeAllTabs();
            viewpager.setOffscreenPageLimit(uiMenu.size());
            onGoDetailView(uiMenu);
            menuContent = uiMenu;

            ScreenSlidePagerAdapter pagerAdapter =
                new ScreenSlidePagerAdapter(getSupportFragmentManager(), uiMenu);
            viewpager.setAdapter(pagerAdapter);

            checkIfMenuHasChanged(uiMenu);
          }
        }
      }

      @Override public void onMenusFails(Throwable e) {
        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        showErrorView();
      }
    });
  }

  private void checkIfMenuHasChanged(final List<UiMenu> oldMenus) {
    Ocm.getMenus(true, new OcmCallbacks.Menus() {
      @Override public void onMenusLoaded(List<UiMenu> newMenus) {
        if (oldMenus == null || newMenus == null) {
          return;
        }
        if (oldMenus.size() != newMenus.size()) {
          showIconNewContent(newMenus);
        } else {
          for (int i = 0; i < newMenus.size(); i++) {
            if (oldMenus.get(i).getUpdateAt() != newMenus.get(i).getUpdateAt()) {
              showIconNewContent(newMenus);
              return;
            }
          }
        }
      }

      @Override public void onMenusFails(Throwable e) {
      }
    });
  }

  private void showIconNewContent(final List<UiMenu> newMenus) {
    newContentMainContainer.setVisibility(View.VISIBLE);
    newContentMainContainer.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        newContentMainContainer.setVisibility(View.GONE);

        showContentView();
        menuContent = newMenus;
        viewpager.removeAllViews();
        ScreenSlidePagerAdapter pagerAdapter =
            new ScreenSlidePagerAdapter(getSupportFragmentManager(), newMenus);
        viewpager.setAdapter(pagerAdapter);

        tabLayout.removeAllTabs();
        onGoDetailView(newMenus);
      }
    });
  }

  private void onGoDetailView(List<UiMenu> uiMenu) {
    tabLayout.removeAllTabs();
    if (uiMenu.size() > 0) {
      UiMenu menu;
      TabLayout.Tab tab;

      for (int i = 0; i < uiMenu.size(); i++) {
        menu = uiMenu.get(i);
        tab = tabLayout.newTab().setText(menu.getText());
        tabLayout.addTab(tab);
      }
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == SettingsActivity.RESULT_CODE) {
      if (resultCode == Activity.RESULT_OK) {
        ContentManager contentManager = ContentManager.getInstance();
        contentManager.clear();
        getContent();
      }
    }
  }

  UiMenu getUiMenu(int position) {
    return menuContent.get(position);
  }

  private void showLoading() {
    loadingView.setVisibility(View.VISIBLE);
    viewpager.setVisibility(View.GONE);
    emptyView.setVisibility(View.GONE);
    errorView.setVisibility(View.GONE);
    networkErrorView.setVisibility(View.GONE);
  }

  private void showEmptyView() {
    loadingView.setVisibility(View.GONE);
    viewpager.setVisibility(View.GONE);
    errorView.setVisibility(View.GONE);
    emptyView.setVisibility(View.VISIBLE);
    networkErrorView.setVisibility(View.GONE);
  }

  private void showErrorView() {
    loadingView.setVisibility(View.GONE);
    viewpager.setVisibility(View.GONE);
    emptyView.setVisibility(View.GONE);
    errorView.setVisibility(View.VISIBLE);
    networkErrorView.setVisibility(View.GONE);
  }

  private void showContentView() {
    loadingView.setVisibility(View.GONE);
    emptyView.setVisibility(View.GONE);
    errorView.setVisibility(View.GONE);
    viewpager.setVisibility(View.VISIBLE);
    networkErrorView.setVisibility(View.GONE);
  }

  private void showNetworkErrorView() {
    loadingView.setVisibility(View.GONE);
    emptyView.setVisibility(View.GONE);
    errorView.setVisibility(View.GONE);
    viewpager.setVisibility(View.GONE);
    networkErrorView.setVisibility(View.VISIBLE);
  }
}
