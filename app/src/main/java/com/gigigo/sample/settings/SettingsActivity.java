package com.gigigo.sample.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.gigigo.sample.App;
import com.gigigo.sample.R;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

  private static final String TAG = "SettingsActivity";
  public static final int RESULT_CODE = 0x23;
  private EditText apiKeyEditText;
  private EditText apiSecretEditText;
  private List<ProjectData> projectDataList;
  private boolean doubleTap = false;
  private int currentProject = -1;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    initView();
    projectDataList = ProjectData.getDefaultProjectDataList();
  }

  private void initView() {
    setTitle("");
    initToolbar();
    apiKeyEditText = (EditText) findViewById(R.id.apiKeyEditText);
    apiSecretEditText = (EditText) findViewById(R.id.apiSecretEditText);

    Button startButton = (Button) findViewById(R.id.startButton);
    startButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        startOrchextra();
      }
    });

    View projectsView = findViewById(R.id.projectsView);
    projectsView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (doubleTap) {

          if (currentProject >= projectDataList.size() - 1) {
            currentProject = 0;
          } else {
            currentProject++;
          }

          loadProjectData();
          return;
        }

        doubleTap = true;
        new Handler().postDelayed(new Runnable() {
          @Override public void run() {
            doubleTap = false;
          }
        }, 500);
      }
    });
  }

  void loadProjectData() {

    apiKeyEditText.setText(projectDataList.get(currentProject).getApiKey());
    apiSecretEditText.setText(projectDataList.get(currentProject).getApiSecret());
  }

  private void startOrchextra() {
    String apiKey = apiKeyEditText.getText().toString();
    String apiSecret = apiSecretEditText.getText().toString();

    if (apiKey.isEmpty()) {
      Toast.makeText(this, "Empty api key", Toast.LENGTH_SHORT).show();
      return;
    }

    if (apiSecret.isEmpty()) {
      Toast.makeText(this, "Empty api secret", Toast.LENGTH_SHORT).show();
      return;
    }

    App app = (App) getApplication();
    app.setApiKey(apiKey);
    app.setApiSecret(apiSecret);

    Intent returnIntent = new Intent();
    setResult(Activity.RESULT_OK, returnIntent);
    finish();
  }

  private void initToolbar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        onBackPressed();
      }
    });

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
  }

  public static void openForResult(Activity context) {
    Intent intent = new Intent(context, SettingsActivity.class);
    context.startActivityForResult(intent, RESULT_CODE);
  }
}
