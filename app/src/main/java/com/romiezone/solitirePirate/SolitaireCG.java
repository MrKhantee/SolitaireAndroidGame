/*
  Copyright 2008 Google Inc.
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.

  Modified by Curtis Gedak 2015
*/
package com.romiezone.solitirePirate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.OrientationListener;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.Locale;

// Base activity class.
public class SolitaireCG extends Activity implements SensorEventListener {


    public static String VERSION_NAME = "";

    private static final int MENU_SELECT_GAME = 1;
    private static final int MENU_NEW = 2;
    private static final int MENU_RESTART = 3;
    private static final int MENU_OPTIONS = 4;
    private static final int MENU_STATS = 5;
    private static final int MENU_HELP = 6;
    private static final int MENU_BAKERSGAME = 7;
    private static final int MENU_BLACKWIDOW = 8;
    private static final int MENU_FORTYTHIEVES = 9;
    private static final int MENU_FREECELL = 10;
    private static final int MENU_GOLF = 11;
    private static final int MENU_KLONDIKE_DEALONE = 12;
    private static final int MENU_KLONDIKE_DEALTHREE = 13;
    private static final int MENU_SPIDER = 14;
    private static final int MENU_TARANTULA = 15;
    private static final int MENU_TRIPEAKS = 16;
    private static final int MENU_VEGAS_DEALONE = 17;
    private static final int MENU_VEGAS_DEALTHREE = 18;

    // View extracted from main.xml.
    private View mMainView;
    private SolitaireView mSolitaireView;
    private SharedPreferences mSettings;
    ImageButton home, undo, exit;
    static TextView txt_score, txt_score_anim;
    static Context context;
    static ImageView img_empty;
    OrientationListener orientationListener;
    static int senseor = 0;

    // Shared preferences are where the various user settings are stored.
    public SharedPreferences GetSettings() {
        return mSettings;
    }

    RelativeLayout splas;
    static boolean land = false;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    public static int val = 1;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            float deltaX = Math.abs(last_x - x);
            float deltaY = Math.abs(last_y - y);

            long curTime = System.currentTimeMillis();
            if (deltaX > 2) {
                //landscape
                if (val != 1) {
                    val = 1;
                    senseor = 1;
//                        setContentView(R.layout.main_land);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    recreate();
                }
            }
            if (deltaY > 2) {
                //Portrait
                if (val != 2) {
                    val = 2;
                    senseor = 1;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    recreate();
                }
            }
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                last_x = x;
                last_y = y;
            }
        }
    }

    @Override
    public void recreate() {
        super.recreate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Force landscape and no title for extra room
//        if()
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        } else {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        orientationListener = new OrientationListener(getApplicationContext()) {
//            @Override
//            public void onOrientationChanged(int orientation) {
//                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    //landscape
//                    if (val != 1) {
//                        val = 1;
////                        setContentView(R.layout.main_land);
//                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                    }
//                } else {
//                    //Portrait
//                    if (val != 2) {
//                        val = 2;
////                        setContentView(R.layout.main);
//                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                    }
//                }
//            }
//        };
//        orientationListener.enable();
        // If the user has never accepted the EULA show it again.
        mSettings = getSharedPreferences("SolitairePreferences", 0);
        setContentView(R.layout.main);
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        mMainView = findViewById(R.id.main_view);
        mSolitaireView = (SolitaireView) findViewById(R.id.solitaire);
        context = getApplicationContext();
        mSolitaireView.SetTextView((TextView) findViewById(R.id.text));
        txt_score = (TextView) findViewById(R.id.txt_score);
        txt_score_anim = (TextView) findViewById(R.id.txt_score_animation);
        txt_score_anim.setVisibility(View.GONE);
        //StartSolitaire(savedInstanceState);
        registerForContextMenu(mSolitaireView);
        img_empty = (ImageView) findViewById(R.id.img_empty);
        img_empty.setColorFilter(Color.RED);
        img_empty.setVisibility(View.GONE);
        // Set global variable for versionName
        try {
            VERSION_NAME = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            Log.e("SolitaireCG.java", e.getMessage());
        }
        home = (ImageButton) findViewById(R.id.home);
        undo = (ImageButton) findViewById(R.id.undo);
        exit = (ImageButton) findViewById(R.id.exit);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOptionsMenu();
            }
        });
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSolitaireView.Undo();

            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Exit();

            }
        });
        splas = (RelativeLayout) findViewById(R.id.splash);
        if (senseor > 0)
            SplashScreen();
    }

    public void Exit() {
        AlertDialog.Builder ab = new AlertDialog.Builder(SolitaireCG.this);
        ab.setTitle("Do you want to exit?");
        ab.setNeutralButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        ab.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        ab.show();
    }

    public static void Empty() {
        img_empty.setVisibility(View.VISIBLE);
    }

    public void setScore(int score) {
        String val = "score";
        txt_score_anim.setVisibility(View.VISIBLE);
        SharedPreferences SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int s = SharedPreferences.getInt(val, 0);
        SharedPreferences.Editor editor = SharedPreferences.edit();
        editor.putInt(val, s + score);
        editor.apply();

        if (score > 0) {
            txt_score_anim.setTextColor(Color.GREEN);
            txt_score_anim.setText(String.format(Locale.ENGLISH, "+%s", String.valueOf(score)));
        } else {
            txt_score_anim.setTextColor(Color.RED);
            txt_score_anim.setText(String.format(Locale.ENGLISH, "%s", String.valueOf(score)));
        }
        if ((s + score) >= 0)
            txt_score.setText(String.valueOf(s + score));
        Animation set = AnimationUtils.loadAnimation(context, R.anim.score);
        txt_score_anim.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txt_score_anim.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            //landscape
//            if (val != 1) {
//                val = 1;
////                        setContentView(R.layout.main_land);
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            }
//        } else {
//            //Portrait
//            if (val != 2) {
//                val = 2;
////                        setContentView(R.layout.main);
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            }
//        }
//    }

    // Entry point for starting the game.
    //public void StartSolitaire(Bundle savedInstanceState) {
    @Override
    public void onStart() {
        super.onStart();
//        if (!land) {
//            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//
//            } else {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            }
//            land = true;
//        }
        mSolitaireView.onStart();

        if (mSettings.getBoolean("SolitaireSaveValid", false)) {
            SharedPreferences.Editor editor = GetSettings().edit();
            editor.putBoolean("SolitaireSaveValid", false);
            editor.commit();
            // If save is corrupt, just start a new game.
            if (mSolitaireView.LoadSave()) {
                if (senseor > 0)
                    SplashScreen();
                SharedPreferences SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                setScore(0);
                return;
            }
        }
        SharedPreferences SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editors = SharedPreferences.edit();
        editors.putInt("score", 0);
        editors.apply();
        SharedPreferences.Editor editor = GetSettings().edit();
        editor.putInt("SpiderSuits", 1);
        editor.commit();
        mSolitaireView.InitGame(mSettings.getInt("LastType", Rules.SPIDER));

    }


    // Force show splash screen if this is the first time played.
    private void SplashScreen() {
//        if (!mSettings.getBoolean("PlayedBefore", false)) {
//            mSolitaireView.DisplaySplash();
//        }
        splas.setVisibility(View.VISIBLE);
        Handler h = new Handler(Looper.getMainLooper());
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                splas.setVisibility(View.GONE);
            }
        }, 3000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        SubMenu subMenu = menu.addSubMenu(0, MENU_SELECT_GAME, 0, R.string.menu_selectgame);
        subMenu.add(0, MENU_BLACKWIDOW, 0, R.string.menu_blackwidow);
        subMenu.add(0, MENU_TARANTULA, 0, R.string.menu_tarantula);
        subMenu.add(0, MENU_SPIDER, 0, R.string.menu_spider);

        menu.add(0, MENU_NEW, 0, R.string.menu_new);
        menu.add(0, MENU_RESTART, 0, R.string.menu_restart);
        menu.add(0, MENU_OPTIONS, 0, R.string.menu_options);
        menu.add(0, MENU_STATS, 0, R.string.menu_stats);
        menu.add(0, MENU_HELP, 0, R.string.menu_help);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences.Editor editor = GetSettings().edit();
        SharedPreferences SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editors = SharedPreferences.edit();
        switch (item.getItemId()) {
            case MENU_BLACKWIDOW:
                editor.putInt("SpiderSuits", 1);
                editor.commit();
                mSolitaireView.InitGame(Rules.SPIDER);
                editors.putInt("score", 0);
                editors.apply();
                setScore(0);
                break;
            case MENU_SPIDER:
                editor.putInt("SpiderSuits", 4);
                editors.putInt("score", 0);
                setScore(0);
                editor.commit();
                mSolitaireView.InitGame(Rules.SPIDER);
                break;
            case MENU_TARANTULA:
                editor.putInt("SpiderSuits", 2);
                editor.commit();
                mSolitaireView.InitGame(Rules.SPIDER);
                editors.putInt("score", 0);
                editors.apply();
                setScore(0);
                break;
            case MENU_NEW:
                mSolitaireView.InitGame(mSettings.getInt("LastType", Rules.KLONDIKE));
                editors.putInt("score", 0);
                editors.apply();
                setScore(0);
                break;
            case MENU_RESTART:
                mSolitaireView.RestartGame();
                editors.putInt("score", 0);
                editors.apply();
                setScore(0);
                break;
            case MENU_OPTIONS:
                DisplayOptions();
                break;
            case MENU_STATS:
                DisplayStats();
                break;
            case MENU_HELP:
//                DisplayHelp();
                break;
        }

        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context, menu);
    }

    // Alternate Menu
    // Invoked with long press and needed on some devices where Android
    // options menu is not accessible or available.
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        SharedPreferences.Editor editor = GetSettings().edit();
        SharedPreferences SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editors = SharedPreferences.edit();
        switch (item.getItemId()) {
            case R.id.context_blackwidow:
                editor = GetSettings().edit();
                editor.putInt("SpiderSuits", 1);
                editor.commit();
                mSolitaireView.InitGame(Rules.SPIDER);
                editors.putInt("score", 0);
                editors.apply();
                setScore(0);
                break;
            case R.id.context_spider:
                editor.putInt("SpiderSuits", 4);
                editor.commit();
                mSolitaireView.InitGame(Rules.SPIDER);
                editors.putInt("score", 0);
                editors.apply();
                setScore(0);
                break;
            case R.id.context_tarantula:
                editor.putInt("SpiderSuits", 2);
                editor.commit();
                mSolitaireView.InitGame(Rules.SPIDER);
                editors.putInt("score", 0);
                editors.apply();
                setScore(0);
                break;
            case R.id.context_new:
                mSolitaireView.InitGame(mSettings.getInt("LastType", Rules.KLONDIKE));
                editors.putInt("score", 0);
                editors.apply();
                setScore(0);
                break;
            case R.id.context_restart:
                mSolitaireView.RestartGame();
                editors.putInt("score", 0);
                editors.apply();
                setScore(0);
                break;
            case R.id.context_options:
                DisplayOptions();
                break;
            case R.id.context_stats:
                DisplayStats();
                break;
            case R.id.context_help:
//                DisplayHelp();
                break;
            case R.id.context_exit:
                mSolitaireView.SaveGame();
                finish();
                break;
            default:
                return super.onContextItemSelected(item);
        }

        return false;
    }


    @Override
    protected void onPause() {
        super.onPause();
        String val = "score";
        mSolitaireView.onPause();
        senSensorManager.unregisterListener(this);
        SharedPreferences SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SharedPreferences.edit();
        editor.putInt(val, Integer.parseInt(txt_score.getText().toString()));
        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSolitaireView.SaveGame();
        String val = "score";
        SharedPreferences SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SharedPreferences.edit();
        editor.putInt(val, Integer.parseInt(txt_score.getText().toString()));
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSolitaireView.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        setScore(0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void DisplayOptions() {
        mSolitaireView.SetTimePassing(false);
        new Options(this, mSolitaireView.GetDrawMaster());
    }

    public void DisplayStats() {
        mSolitaireView.SetTimePassing(false);
        new Stats(this, mSolitaireView);
    }

    public void CancelOptions() {
        setContentView(mMainView);
        mSolitaireView.requestFocus();
        mSolitaireView.SetTimePassing(true);
    }

    public void NewOptions() {
        setContentView(mMainView);
        SharedPreferences.Editor editor = GetSettings().edit();
        editor.putInt("SpiderSuits", 1);
        editor.apply();
        mSolitaireView.InitGame(mSettings.getInt("LastType", Rules.SPIDER));
    }

    // This is called for option changes that require a refresh, but not a new game
    public void RefreshOptions() {
        setContentView(mMainView);
        mSolitaireView.RefreshOptions();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Exit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        senseor = 0;
    }
}
