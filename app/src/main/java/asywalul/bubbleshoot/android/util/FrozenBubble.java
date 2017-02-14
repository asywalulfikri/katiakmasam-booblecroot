package asywalul.bubbleshoot.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import asywalul.bubbleshoot.android.Game_Activity;
import asywalul.bubbleshoot.android.R;


public class FrozenBubble extends Activity
{
  public final static int SOUND_WON = 0;
  public final static int SOUND_LOST = 1;
  public final static int SOUND_LAUNCH = 2;
  public final static int SOUND_DESTROY = 3;
  public final static int SOUND_REBOUND = 4;
  public final static int SOUND_STICK = 5;
  public final static int SOUND_HURRY = 6;
  public final static int SOUND_NEWROOT = 7;
  public final static int SOUND_NOH = 8;
  public final static int NUM_SOUNDS = 9;

  public final static int GAME_NORMAL = 0;
  public final static int GAME_COLORBLIND = 1;

  public final static int MENU_COLORBLIND_MODE_ON = 1;
  public final static int MENU_COLORBLIND_MODE_OFF = 2;
  public final static int MENU_FULLSCREEN_ON = 3;
  public final static int MENU_FULLSCREEN_OFF = 4;
  public final static int MENU_SOUND_ON = 5;
  public final static int MENU_SOUND_OFF = 6;
  public final static int MENU_DONT_RUSH_ME = 7;
  public final static int MENU_RUSH_ME = 8;
  public final static int MENU_NEW_GAME = 9;
  
  public final static int MENU_EDITOR = 11;
  public final static int MENU_TOUCHSCREEN_AIM_THEN_SHOOT = 12;
  public final static int MENU_TOUCHSCREEN_POINT_TO_SHOOT = 13;

  public final static String PREFS_NAME = "game";
  private static int gameMode = GAME_NORMAL;
  private static boolean soundOn = true;
  private static boolean dontRushMe = false;
  private static boolean aimThenShoot = false;

  private boolean fullscreen = true;

  private GameView.GameThread mGameThread;
  private GameView mGameView;
  
  private static final String EDITORACTION = "com.example.util.GAME";
  private boolean activityCustomStarted = false;

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    super.onCreateOptionsMenu(menu);
   
      
     return true;
  }
   

  @Override
  public boolean onPrepareOptionsMenu(Menu menu)
  {
    super.onPrepareOptionsMenu(menu);
    menu.findItem(MENU_SOUND_ON).setVisible(!getSoundOn());
    menu.findItem(MENU_SOUND_OFF).setVisible(getSoundOn());
    menu.findItem(MENU_FULLSCREEN_ON).setVisible(!fullscreen);
    menu.findItem(MENU_FULLSCREEN_OFF).setVisible(fullscreen);
    menu.findItem(MENU_TOUCHSCREEN_AIM_THEN_SHOOT).setVisible(
        !getAimThenShoot());
    menu.findItem(MENU_TOUCHSCREEN_POINT_TO_SHOOT).setVisible(
        getAimThenShoot());
    menu.findItem(MENU_DONT_RUSH_ME).setVisible(!getDontRushMe());
    menu.findItem(MENU_RUSH_ME).setVisible(getDontRushMe());
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch (item.getItemId()) {
    case MENU_NEW_GAME:
      mGameThread.newGame();
      return true;
    case MENU_COLORBLIND_MODE_ON:
      setMode(GAME_COLORBLIND);
      return true;
    case MENU_COLORBLIND_MODE_OFF:
      setMode(GAME_NORMAL);
      return true;
    case MENU_FULLSCREEN_ON:
      fullscreen = true;
      setFullscreen();
      return true;
    case MENU_FULLSCREEN_OFF:
      fullscreen = false;
      setFullscreen();
      return true;
    case MENU_SOUND_ON:
      setSoundOn(true);
      return true;
    case MENU_SOUND_OFF:
      setSoundOn(false);
      return true;
    case MENU_TOUCHSCREEN_AIM_THEN_SHOOT:
      setAimThenShoot(true);
      return true;
    case MENU_TOUCHSCREEN_POINT_TO_SHOOT:
      setAimThenShoot(false);
      return true;
    case MENU_DONT_RUSH_ME:
      setDontRushMe(true);
      return true;
    case MENU_RUSH_ME:
      setDontRushMe(false);
      return true;
//    case MENU_EDITOR:
//      startEditor();
//      return true;
    }
    return false;
  }

  private void setFullscreen()
  {
    if (fullscreen) {
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
      getWindow().clearFlags(
          WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    } else {
      getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
      getWindow().addFlags(
          WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }
    mGameView.requestLayout();
  }

  public synchronized static void setMode(int newMode)
  {
    gameMode = newMode;
  }

  public synchronized static int getMode()
  {
    return gameMode;
  }

  public synchronized static boolean getSoundOn()
  {
    return soundOn;
  }

  public synchronized static void setSoundOn(boolean so)
  {
    soundOn = so;
  }

  public synchronized static boolean getAimThenShoot()
  {
    return aimThenShoot;
  }

  public synchronized static void setAimThenShoot(boolean ats)
  {
    aimThenShoot = ats;
  }

  public synchronized static boolean getDontRushMe()
  {
    return dontRushMe;
  }

  public synchronized static void setDontRushMe(boolean dont)
  {
    dontRushMe = dont;
  }

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    if (savedInstanceState != null) {
      //Log.i("frozen-bubble", "FrozenBubble.onCreate(...)");
    } else {
      //Log.i("frozen-bubble", "FrozenBubble.onCreate(null)");
    }
    super.onCreate(savedInstanceState);
    setVolumeControlStream(AudioManager.STREAM_MUSIC);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    
    //SessionMAndroidConfig.getInstance().setServer(SessionMAndroidConfig.SERVER_DEV);
    //SessionM.getInstance().startSession(this, "");
    
    // Allow editor functionalities.
    Intent i = getIntent();
     
      // Default intent.
      activityCustomStarted = false;
      setContentView(R.layout.game_activity);
      mGameView = (GameView)findViewById(R.id.game);
     mGameView=new GameView(this, i.getExtras().getByteArray("customstage"), i.getIntExtra("stage", 0));
     setContentView(mGameView);
     mGameThread = mGameView.getThread();
 
    if (savedInstanceState != null) {
      mGameThread.restoreState(savedInstanceState);
    }
    mGameView.requestFocus();
    setFullscreen();
  }

  /**
   * Invoked when the Activity loses user focus.
   */
  @Override
  protected void onPause() {
    //Log.i("frozen-bubble", "FrozenBubble.onPause()");
    super.onPause();
    mGameView.getThread().pause();
    // Allow editor functionalities.
 
//      // Editor's intent is running.
      SharedPreferences sp = getSharedPreferences(PREFS_NAME,
                                                  Context.MODE_PRIVATE);
      SharedPreferences.Editor editor = sp.edit();
      int NumofComplete=sp.getInt("level", 1);
      int Current=this.mGameThread.getCurrentLevelIndex()+1;
      if(Current>NumofComplete)
      {
    	  editor.putInt("level", mGameThread.getCurrentLevelIndex()+1);
          editor.commit();
      }
      
   }

  @Override
  protected void onStop() {
    //Log.i("frozen-bubble", "FrozenBubble.onStop()");
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    //Log.i("frozen-bubble", "FrozenBubble.onDestroy()");
    super.onDestroy();
    if (mGameView != null) {
      mGameView.cleanUp();
    }
    mGameView = null;
    mGameThread = null;    
  }

  /**
   * Notification that something is about to happen, to give the Activity a
   * chance to save state.
   *
   * @param outState a Bundle into which this Activity should save its state
   */
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    //Log.i("frozen-bubble", "FrozenBubble.onSaveInstanceState()");
    // Just have the View's thread save its state into our Bundle.
    super.onSaveInstanceState(outState);
    mGameThread.saveState(outState);
  }

  

  @Override
	 public void onBackPressed()
	 {
		 super.onBackPressed();
		 Intent intback=new Intent(getApplicationContext(),Game_Activity.class);
		 intback.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 startActivity(intback);
		//	finish();
	 }
 
  
}
