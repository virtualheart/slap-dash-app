package com.example.absis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Home extends Activity {
Button bt_attence,bt_mark,bt_ques;
String name=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		bt_attence=(Button)findViewById(R.id.bt_attance);
		bt_mark=(Button)findViewById(R.id.bt_mark);
		Intent iobj = getIntent();
		name= iobj.getStringExtra("name");
	
		bt_attence.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intObj1 = new Intent(Home.this,NextPage.class);
				intObj1.putExtra("name",name);
				startActivity(intObj1);
				finish();
			}
		});
		bt_mark.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intObj1 = new Intent(Home.this,Review.class);
				intObj1.putExtra("name",name);
				startActivity(intObj1);
				finish();
			}
		});
		
	}
}