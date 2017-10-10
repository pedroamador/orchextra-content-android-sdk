package com.gigigo.sample;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.gigigo.orchextra.core.Orchextra;
import com.gigigo.orchextra.ocm.OCManagerCallbacks;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmCallbacks;
import com.gigigo.orchextra.ocm.callbacks.OcmCredentialCallback;
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver;
import com.gigigo.orchextra.ocm.callbacks.OnRequiredLoginCallback;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private static final Boolean AUTO_INIT = true;
  static final String COUNTRY = "it";
  private TabLayout tabLayout;
  private View loadingView;
  private View emptyView;
  private View errorView;
  private View networkErrorView;
  private ViewPager viewpager;
  private ScreenSlidePagerAdapter pagerAdapter;
  private View newContentMainContainer;

  private TabLayout.OnTabSelectedListener onTabSelectedListener =
      new TabLayout.OnTabSelectedListener() {
        @Override public void onTabSelected(TabLayout.Tab tab) {
          viewpager.setCurrentItem(tab.getPosition());
          ScreenSlidePageFragment frag =
              ((ScreenSlidePageFragment) pagerAdapter.getItem(viewpager.getCurrentItem()));
          frag.reloadSection();
        }

        @Override public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override public void onTabReselected(TabLayout.Tab tab) {
          viewpager.setCurrentItem(tab.getPosition());
          ((ScreenSlidePageFragment) pagerAdapter.getItem(
              viewpager.getCurrentItem())).reloadSection();
        }
      };

  private OnRequiredLoginCallback onDoRequiredLoginCallback = new OnRequiredLoginCallback() {
    @Override public void doRequiredLogin() {
      Toast.makeText(getApplicationContext(), "Item needs permissions", Toast.LENGTH_SHORT).show();
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();

    Ocm.setOnDoRequiredLoginCallback(onDoRequiredLoginCallback);

    if (AUTO_INIT) {
      startCredentials();
    }
  }

  @Override protected void onResume() {
    super.onResume();
    //ReadedArticles
    //if (OCManager.getShowReadedArticlesInGrayScale() && pagerAdapter != null) {
    //  Toast.makeText(this, "Refresh grid from integratied app if readed articles are enabled"
    //      + OCManager.getShowReadedArticlesInGrayScale(), Toast.LENGTH_LONG).show();
    //}
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
        startCredentials();
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

    pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
    viewpager.setAdapter(pagerAdapter);
    viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    errorView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        startCredentials();
      }
    });
    networkErrorView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        startCredentials();
      }
    });
  }

  private void startCredentials() {

    Ocm.setOcmCredentialCallback(new OcmCredentialCallback() {

      @Override public void onCredentialReceiver(String accessToken) {
        //TODO Fix in Orchextra
        runOnUiThread(new Runnable() {
          @Override public void run() {
            getContent();
          }
        });
      }

      @Override public void onCredentailError(String code) {
        Snackbar.make(tabLayout,
            "No Internet Connection: " + code + "\n check Credentials-Enviroment",
            Snackbar.LENGTH_INDEFINITE).show();
      }
    });

    Ocm.initialize(getApplication(), App.API_KEY, App.API_SECRET);
    Ocm.setBusinessUnit(COUNTRY);

    Ocm.setOnCustomSchemeReceiver(new OnCustomSchemeReceiver() {
      @Override public void onReceive(String customScheme) {
        Toast.makeText(MainActivity.this, customScheme, Toast.LENGTH_SHORT).show();
        Orchextra.INSTANCE.openScanner();
      }
    });
  }

  //region clear all data
  private void clearDataAndGoToChangeCountryView() {
    clearApplicationData();
    Orchextra.INSTANCE.finish();

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

  private void getContent() {
    Ocm.getMenus(false, new OcmCallbacks.Menus() {
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
            pagerAdapter.setDataItems(uiMenu);
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
        pagerAdapter.setDataItems(newMenus);
        viewpager.removeAllViews();
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

    tabLayout.addOnTabSelectedListener(onTabSelectedListener);
  }

  public boolean isOnline() {
    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    return netInfo != null && netInfo.isConnectedOrConnecting();
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
