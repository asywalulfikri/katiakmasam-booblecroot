package asywalul.bubbleshoot.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class AboutActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);



		;
	}

}
