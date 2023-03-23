package com.example.absis;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class GetView extends Activity {

	String nam;
	ImageView i;
	
	private String TAG ="Vik";
	private Handler handler = new Handler();
	private ProgressDialog simpleWaitDialog;
	String out=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_view);
		
		Intent iobj = getIntent();
		nam = iobj.getStringExtra("name");
		
		i=(ImageView) findViewById(R.id.img_view);
		
		AsyncCallWS task =new AsyncCallWS();
		task.execute();
		
	}
	private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            call();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            if (simpleWaitDialog != null)
			{
            	simpleWaitDialog.dismiss();
			}
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            simpleWaitDialog = ProgressDialog.show(GetView.this,"Wait", "Connecting...");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");
        }

    }
	
	public void call()
	{
		try
		{
			String SOAP_ACTION = "http://tempuri.org/geImageView";
			String METHOD_NAME = "geImageView";
			String NAMESPACE = "http://tempuri.org/";
			String URL ="http://athidharman.in/android/Slap/Service.asmx";
			
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			request.addProperty("bid",nam);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);

			SoapObject result = (SoapObject) envelope.getResponse();
			
			out=result.getProperty(0).toString();
	


			handler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stu
					//Toast.makeText(getApplicationContext(), out,Toast.LENGTH_SHORT).show();
					byte[] decodedString = Base64.decode(out, Base64.DEFAULT);
					Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
					//writeFile(decodedString, "/sdcard/" + text);
					//Toast.makeText(getApplicationContext(), "File Stored : /sdcard/" + text, Toast.LENGTH_SHORT).show();
					i.setImageBitmap(decodedByte);

				}
			});
			/*
			int rcount = (result.getPropertyCount());
			if (rcount >= 2)
			{
				String res_info = result.getProperty(0).toString();
				final String res_uid = result.getProperty(1).toString();
				if(res_info.equals("Login Success"))
				{
					handler.post(new Runnable()
					{
						@Override
						public void run()
						{
							if (simpleWaitDialog != null)
							{
								simpleWaitDialog.dismiss();
							}
							Toast.makeText(getApplicationContext(), "Login Success ...!", Toast.LENGTH_SHORT).show();
						}
					});
					//finish();
				}
				else
				{
					handler.post(new Runnable()
					{
						@Override
						public void run()
						{
							if (simpleWaitDialog != null)
							{
								simpleWaitDialog.dismiss();
							}
							Toast.makeText(getApplicationContext(),"Login Failure ...!", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
			else
			{
				handler.post(new Runnable()
				{
					@Override
					public void run()
					{
						if (simpleWaitDialog != null)
						{
							simpleWaitDialog.dismiss();
						}
						Toast.makeText(getApplicationContext(),"Login Failure ...!", Toast.LENGTH_SHORT).show();
					}
				});
			}
			*/
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
}


