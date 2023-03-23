package com.example.absis;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Complaint extends Activity {
	Button bt_questions,bt_back;
	String regno, stdid, fname, format;
	String eid, it;
	String sno;
	View promptsView,promptsView1;
	ListView listView;
	private ProgressDialog simpleWaitDialog;
	ProgressDialog pbar;
	private static String uname,phone;
	ArrayAdapter<String> adpt;
	public static String URL = "http://athidharman.in/android/Slap/Service.asmx";
	private static final String NAMESPACE = "http://tempuri.org/";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_complaint);
Intent intobj = getIntent();
		
//		
//		uname    = intobj.getStringExtra("name");
//		phone    = intobj.getStringExtra("phone");
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		adpt = new ArrayAdapter<String>(this, R.layout.layout_img, R.id.name);
	listView = (ListView) findViewById(R.id.listview1);
	bt_back=(Button)findViewById(R.id.bt_back);

	callBusList();
	
	   bt_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		
				Intent intObj1 = new Intent(Complaint.this,AdminHome.class);
				//intObj1.putExtra("name",et_uname.getText().toString());
				startActivity(intObj1);
				finish();				}
		});
		
	listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// // TODO Auto-generated method stub
				// String i=adapter1.getItem(position).toString();

				String str = listView.getItemAtPosition(position).toString();
				Intent iobj = new Intent(Complaint.this, Details.class);
				iobj.putExtra("name", str);
				startActivity(iobj);

			}
		});
	}

	public void display(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
		
	public void callBusList()
	{
		try
		{
			//adpt1.clear();

			
			String SOAP_ACTION = "http://tempuri.org/getProductlist";
			String METHOD_NAME = "getProductlist";

			List<String> list1 = new ArrayList<String>();
			
			/** Called when the activity is first created. */
		
			Log.v("hi","hi");
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapObject response = (SoapObject)envelope.getResponse();
			Log.v("hi",response.getPropertyCount()+"");
			Log.v("hi",response.getProperty(1).toString());
			//listview_array=new String[response.getPropertyCount()];
			for(int i=0;i<response.getPropertyCount();i++)
			{
				list1.add((String) response.getProperty(i).toString());
//				Log.v("ravi",response.getProperty(i).toString());
		

			}
			adpt.addAll(list1);
			listView.setAdapter(adpt);
		} catch (Exception e)
		{
			//tv.setText(e.getMessage());
			e.printStackTrace();
			Log.v("hi","hello");
			
		}
	}
	
	}