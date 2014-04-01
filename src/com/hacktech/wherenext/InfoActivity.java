package com.hacktech.wherenext;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class InfoActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		ActionBar actBar = getActionBar();
		
		Bundle args = new Bundle();
		args.putString("location", getIntent().getExtras().getString("location"));
		
		actBar.removeAllTabs();
		actBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actBar.addTab(actBar.newTab()
		 		 .setText("Info")
		 		 .setTabListener(new TabListener<InfoFragment>(
		 				 this, "info", InfoFragment.class, args)));
		actBar.addTab(actBar.newTab()
		 		 .setText("Events")
		 		 .setTabListener(new TabListener<EventFragment>(
		 				 this, "events", EventFragment.class, args)));
	}
		
}
