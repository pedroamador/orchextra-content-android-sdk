package com.gigigo.sample;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.gigigo.orchextra.Orchextra;
import com.gigigo.orchextra.ocm.OCManager;
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

  private TabLayout tabLayout;
  private ViewPager viewpager;
  private ScreenSlidePagerAdapter adapter;
  private View newContentMainContainer;

  private TabLayout.OnTabSelectedListener onTabSelectedListener =
      new TabLayout.OnTabSelectedListener() {
        @Override public void onTabSelected(TabLayout.Tab tab) {
          viewpager.setCurrentItem(tab.getPosition());
          ScreenSlidePageFragment frag =
              ((ScreenSlidePageFragment) adapter.getItem(viewpager.getCurrentItem()));
          frag.reloadSection();
        }

        @Override public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override public void onTabReselected(TabLayout.Tab tab) {
          viewpager.setCurrentItem(tab.getPosition());
          ((ScreenSlidePageFragment) adapter.getItem(viewpager.getCurrentItem())).reloadSection();
        }
      };

  private OnRequiredLoginCallback onDoRequiredLoginCallback = new OnRequiredLoginCallback() {
    @Override public void doRequiredLogin() {
      Toast.makeText(getApplicationContext(), "Item needs permissions", Toast.LENGTH_SHORT).show();
    }
  };

  @Override protected void onResume() {
    super.onResume();
    //ReadedArticles
    if (OCManager.getShowReadArticles() && adapter != null) {
      adapter.reloadSections();

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
  }

  private void initViews() {
    tabLayout = (TabLayout) findViewById(R.id.tabLayout);
    viewpager = (ViewPager) findViewById(R.id.viewpager);
    View fabReload = findViewById(R.id.fabReload);
    View fabChange = findViewById(R.id.fabChange);
    View fabClean = findViewById(R.id.fabClean);

    fabChange.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        startCredentials();
        if (OCManager.getShowReadArticles() && adapter != null) {
          //OCManager.transform+=1;
          adapter.reloadSections();
          //Toast.makeText(MainActivity.this, "Refresh grid from integratied app if readed articles are enabled transform number"
          //    + OCManager.transform, Toast.LENGTH_LONG).show();
        }
      }
    });

    fabClean.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        //oldcode
        //Toast.makeText(MainActivity.this, "Delete all data webStorage", Toast.LENGTH_LONG).show();
        //clearDataAndGoToChangeCountryView();


        Toast.makeText(MainActivity.this, "Ocm.TestVimeoVideoFeature", Toast.LENGTH_LONG).show();


        final String CLIENT_ID = "084c3b59bac4ed2e8a08698d3d28071f8bd4f3bf";
        final String CLIENTE_SECRET =
            "gLURPc2Cpcc5nj8ck3DYBt/avOhaYy0mcFTxCsmsyfVa9kJrXOFx6Cxau/CUOX4vZrYS2Y5/9rUJDtSMNgc4rjTNT55dTFlk9q51hlNOAnjg9hjV1UIYZo9cGYS54UON";
        final String SCOPE = "private public video_files";

        final String VERTICAL_VIDEO="237059608";
        final String VIDEO_ID ="234291582";// "236232109";
        final String ACCESS_TOKEN = "50163590b4402cceefb2c78a7aba7093";

        Ocm.TestVimeoVideoFeature(MainActivity.this, ACCESS_TOKEN, VIDEO_ID);
      }
    });

    newContentMainContainer = findViewById(R.id.newContentMainContainer);

    fabReload.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        adapter.reloadSections();
      }
    });

    adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
    viewpager.setAdapter(adapter);
    viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
  }

  static String country = "it";

  private void startCredentials() {
    Ocm.setBusinessUnit(country);
    Ocm.startWithCredentials(App.API_KEY, App.API_SECRET, new OcmCredentialCallback() {
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

    Ocm.setOnCustomSchemeReceiver(new OnCustomSchemeReceiver() {
      @Override public void onReceive(String customScheme) {
        Toast.makeText(MainActivity.this, customScheme, Toast.LENGTH_SHORT).show();
        Orchextra.startScannerActivity();
      }
    });
    Ocm.start();//likewoah
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

  private void getContent() {
    Ocm.getMenus(false, new OcmCallbacks.Menus() {
      @Override public void onMenusLoaded(List<UiMenu> uiMenu) {
        if (uiMenu == null) {
          Toast.makeText(MainActivity.this, "menu is null", Toast.LENGTH_SHORT).show();
        } else {
          viewpager.setOffscreenPageLimit(uiMenu.size());
          onGoDetailView(uiMenu);
          adapter.setDataItems(uiMenu);
          checkIfMenuHasChanged(uiMenu);
        }
      }

      @Override public void onMenusFails(Throwable e) {
        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

        adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        adapter.setDataItems(newMenus);
        viewpager.removeAllViews();
        viewpager.setAdapter(adapter);

        tabLayout.removeAllTabs();
        onGoDetailView(newMenus);
      }
    });
  }

  private void onGoDetailView(List<UiMenu> uiMenu) {
    tabLayout.removeAllTabs();
    if (uiMenu.size() > 0) {
      for (int i = 0; i < uiMenu.size(); i++) {
        UiMenu menu = uiMenu.get(i);
        TabLayout.Tab tab = tabLayout.newTab().setText(menu.getText());
        tabLayout.addTab(tab);
      }
    }

    tabLayout.addOnTabSelectedListener(onTabSelectedListener);
  }
}
