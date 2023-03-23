package com.example.absis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AdminHome extends Activity {
	Button bt_attence,bt_mark,bt_submit,bt_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_home);
		
		
		bt_submit=(Button)findViewById(R.id.bt_complaint);
		bt_view=(Button)findViewById(R.id.bt_view);


		bt_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intObj1 = new Intent(AdminHome.this,Complaint.class);
				//intObj1.putExtra("name",et_uname.getText().toString());
				startActivity(intObj1);
				finish();
			}
		});
		bt_view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intObj1 = new Intent(AdminHome.this,GetList.class);
				//intObj1.putExtra("name",et_uname.getText().toString());
				startActivity(intObj1);
				finish();
			}
		});
		
	}

		
	}

