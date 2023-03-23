package com.example.absis;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Review extends Activity {
	Intent in;
	TextView animalId;
	ListView list;
	ArrayAdapter<String> adapter;
	private String TAG ="Vik";
	ProgressDialog pbar;
	Handler handle=new Handler();
	String nam;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review);
		Button aBack=(Button)findViewById(R.id.buttoneventtype);
		aBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent iobj = new Intent(Review.this,Home.class);
		            startActivity(iobj);
			}
		});
		
		
		Intent iobj = getIntent();
		nam = iobj.getStringExtra("name");
		adapter = new ArrayAdapter<String>(this,R.layout.layout_img,R.id.name);
		list = (ListView) findViewById(R.id.listview1);
		AsyncCallWS task =new AsyncCallWS();
		task.execute();
		
		list.setOnItemClickListener(new OnItemClickListener()
		{
			
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				
				String i=adapter.getItem(position).toString();
				 Log.i(TAG, i);
				
				 
				
				//HashMap<String, String> hm = adpt.getItem(position);
				//String selectedFromList =(String) (listView.getItemAtPosition(position));
				Intent iobj = new Intent(Review.this,Complaint.class);
				iobj.putExtra("name",i);
		            startActivity(iobj);
			
				
				
			}
		});
	}

	private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
       
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            
            adapter.clear();
            call();
            return null;
        }

       
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            if (pbar != null)
			{
            	pbar.dismiss();
			}
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

     
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            pbar = ProgressDialog.show(Review.this,"Wait", "Connecting...");
        }

       
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");
        }

    }
	
	public void call()
	{
		try
		{
			String SOAP_ACTION = "http://tempuri.org/ListService9";
			String METHOD_NAME = "ListService9";
			String NAMESPACE = "http://tempuri.org/";
			String URL="http://athidharman.in/android/Slap/Service.asmx";
			
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			
			request.addProperty("type",nam);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			
			SoapObject res = (SoapObject) envelope.getResponse();
			for (int i = 0; i < res.getPropertyCount(); i++)
			{
				SoapObject res_in = (SoapObject) res.getProperty(i);
				
				String na="Post By :"+res_in.getProperty("cid").toString()+"\n"+"Complaint :"+res_in.getProperty("comp").toString()+"\n"+"Location From :"+res_in.getProperty("loc").toString()+"\n"+"Status   :" +res_in.getProperty("status").toString();
				
				adapter.add(na);
				adapter.notifyDataSetChanged();
			}
		} catch (Exception e)
		{
			// tv.setText(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume()
	{
		//AsyncCallWS task =new AsyncCallWS();
		//task.execute();
		
		super.onResume();
	}
	

}
