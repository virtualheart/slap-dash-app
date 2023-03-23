package com.example.absis;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Login extends Activity {

	ImageButton close,iorganise,istudent,istaff;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		close = (ImageButton) findViewById(R.id.close);
		iorganise =(ImageButton) findViewById(R.id.iorganise);
		istudent =(ImageButton) findViewById(R.id.istudent);
		istaff =(ImageButton) findViewById(R.id.istaff);

		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				startActivity(new Intent(Login.this,UserHome.class));
				onBackPressed();
				finish();
			}
		});

		iorganise.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(getApplicationContext(),Signup.class));
			}
		});

		istudent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(),Userlist.class);
				intent.putExtra("role","admin");
				startActivity(intent);
			}
		});

		istaff.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(),Userlist.class);
				intent.putExtra("role","user");
				startActivity(intent);
			}
		});
	}
}
