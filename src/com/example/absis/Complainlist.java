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

public class Complainlist extends Activity {
	Button bt_questions,bt_back;
	String regno, stdid, fname, format;
	String eid, it;
	String sno;
	View promptsView,promptsView1;
	ListView listView;
	private ProgressDialog simpleWaitDialog;
	private String TAG = "Vik";
	ProgressDialog pbar;
	private static String uname,phone;
	private Handler handler = new Handler();
	ArrayAdapter<String> adpt;
	public static String URL = "http://athidharman.in/android/Slap/Service.asmx";
	private static final String NAMESPACE = "http://tempuri.org/";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_complainlist);
		Intent intobj = getIntent();
		
//		
//		uname    = intobj.getStringExtra("name");
//		phone    = intobj.getStringExtra("phone");
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		adpt = new ArrayAdapter<String>(this, R.layout.layout_img, R.id.name);
	listView = (ListView) findViewById(R.id.listView2);

	callBusList();
	
//	   bt_back.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//		
//				 Intent intent = new Intent(Complainlist.this, UserHome.class);
//			
//                startActivity(intent);
//			}
//		});
//		
	listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// // TODO Auto-generated method stub
				// String i=adapter1.getItem(position).toString();

				String str = listView.getItemAtPosition(position).toString();
				Intent iobj = new Intent(Complainlist.this, Details.class);
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
				Log.v("ravi",response.getProperty(i).toString());
				adpt.add(response.getProperty(i).toString());
				listView.setAdapter(adpt);

			}
			
		} catch (Exception e)
		{
			//tv.setText(e.getMessage());
			e.printStackTrace();
			Log.v("hi","hello");
			
		}
	}
	
	}