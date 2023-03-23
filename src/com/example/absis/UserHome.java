package com.example.absis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class UserHome extends Activity {
	Button bt_submit,bt_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_home);
		bt_submit=(Button) findViewById(R.id.bt_complaint);

		bt_submit.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent iobj = new Intent(UserHome.this,Complainlist.class);
	            startActivity(iobj);
	
			}
		});
	}

	}
