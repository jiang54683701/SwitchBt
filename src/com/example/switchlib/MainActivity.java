package com.example.switchlib;

import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		SwitchBt bt = (SwitchBt) findViewById(R.id.switchBt);
		bt.switchOn();
	}

}
