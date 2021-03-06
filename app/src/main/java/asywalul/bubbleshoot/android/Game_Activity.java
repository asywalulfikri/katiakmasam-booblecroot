package asywalul.bubbleshoot.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.GridView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.InputStream;
import java.util.ArrayList;

import asywalul.bubbleshoot.android.util.FrozenBubble;
import asywalul.bubbleshoot.android.util.LevelManager;

public class Game_Activity extends Activity implements StageAdapter.KeyClickListener {

	byte[] customLevels;
	int totalstage,seperate;
	ArrayList<String> number;
	LevelManager levelManager;
	GridView grid;
	StageAdapter adapter;
	String allLevels;
	public final static String PREFS_NAME = "game";
	private AdView mAdView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_activity);

		mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder()
				.build();
		mAdView.loadAd(adRequest);


		grid=(GridView)findViewById(R.id.stage_grid);

		try
		{
			InputStream is = getAssets().open("levels.txt");
			int size = is.available();
			customLevels=new byte[size];
			is.read(customLevels);       
			allLevels = new String(customLevels);
			seperate = allLevels.indexOf("\n\n");

			totalstage=(allLevels.length())/seperate;
			Log.e("number of stage", ""+totalstage);
			number=new ArrayList<String>();

			for(int i=1;i<totalstage+1;i++)
			{
				number.add(String.valueOf(i));

			}

			is.close();
		}
		catch(Exception e)
		{

		}

		SharedPreferences sp = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		int NumofComplete=sp.getInt("level", 1);
		//		 Log.e("Number of Stage Clear",""+NumofComplete);

		adapter=new StageAdapter(this,number,this,NumofComplete);
		grid.setAdapter(adapter);

 
	}
	 
	@Override
	public void keyClickedIndex(int index) {
		// TODO Auto-generated method stub
	 

		Intent intgame=new Intent(getApplicationContext(),FrozenBubble.class);
		intgame.putExtra("customstage", customLevels);
		intgame.putExtra("stage", index-1);
		startActivity(intgame);
	}




}
