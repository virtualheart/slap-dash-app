package com.example.absis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.absis.model.SharedPrefManager;

public class Home extends Activity {
Button bt_attence,bt_mark,bt_logout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		bt_attence=(Button)findViewById(R.id.bt_attance);
		bt_mark=(Button)findViewById(R.id.bt_mark);
		bt_logout=(Button)findViewById(R.id.bt_logout);


		bt_attence.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(Home.this,NextPage.class));
//				finish();

			}
		});
		bt_mark.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(Home.this,Review.class));
//				finish();

			}
		});

		bt_logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				finish();
				startActivity(new Intent(Home.this,MainActivity.class));
				SharedPrefManager.getInstance(Home.this).logout();
			}
		});
		
	}
}