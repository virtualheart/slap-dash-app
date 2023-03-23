package com.example.absis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.absis.model.SharedPrefManager;

public class UserHome extends Activity {
	Button bt_back,bt_logout,bt_manage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_home);
		bt_back=(Button) findViewById(R.id.bt_complaint);
		bt_logout= (Button)findViewById(R.id.bt_logout) ;
		bt_manage=(Button) findViewById(R.id.bt_manage);

		bt_back.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent iobj = new Intent(UserHome.this,Complainlist.class);
	            startActivity(iobj);
			}
		});

		bt_manage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(UserHome.this,Login.class));
			}
		});

		bt_logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				finish();
				startActivity(new Intent(UserHome.this,MainActivity.class));
				SharedPrefManager.getInstance(getApplicationContext()).logout();

			}
		});

	}

}
