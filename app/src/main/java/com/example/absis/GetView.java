package com.example.absis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URL;

public class GetView extends Activity {

	Context contxt;
	private ProgressDialog pd;

	Button btn_back;
	ImageView i;
	private String TAG ="Vik";
	private Handler handler = new Handler();
	private ProgressDialog simpleWaitDialog;
	String name,image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_view);

		contxt = this;

		Intent iobj = getIntent();
		image = iobj.getStringExtra("img");

		i=(ImageView) findViewById(R.id.img_view);
		btn_back=(Button)findViewById(R.id.btn_back);
		pd = new ProgressDialog(contxt);
		pd.setTitle("Processing...");
		pd.setMessage("Please wait.");
		pd.setCancelable(false);
		pd.setIndeterminate(true);
		pd.show();

		AsyncCallWS task =new AsyncCallWS();
		task.execute();

		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
				finish();
			}
		});


	}
	private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
			return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            	pd.dismiss();
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
			URL com_img = null;
			try {
				com_img = new URL(image);
				Bitmap bitmap = BitmapFactory.decodeStream(com_img.openConnection().getInputStream());
				i.setImageBitmap(bitmap);
				i.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(contxt, "Image open error", Toast.LENGTH_SHORT).show();
//				Intent iobj = new Intent(GetView.this,ComplaintDetiles.class);
//				iobj.putExtra("name",name);
//				startActivity(iobj);
				onBackPressed();
				finish();
			}
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");
        }

    }
}


