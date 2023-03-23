package com.example.absis;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Details extends Activity {
	EditText tb_update_bname,tb_update_btype,tb_update_bsrc,tb_update_bdest,tb_update_cost,tb_update_mobile,tb_update_not,tb_update_uname,tb_update_state,tb_update_country,tb_update_regid,tb_update_status;
	String extStorageDirectory;
	Button bt_close,but_update_back;
	int s1,s2,s3;
Spinner sp_accept;
String name;
Context contxt;

	String SOAP_ACTION = "http://tempuri.org/getProduct";
	String METHOD_NAME = "getProduct";
	String NAMESPACE = "http://tempuri.org/";
    String URL ="http://athidharman.in/android/Slap/Service.asmx";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		contxt = this;

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
Intent intobj = getIntent();
		
	name    = intobj.getStringExtra("name");
			callgetBus(name);
				
				

				
				
								tb_update_bname=(EditText) findViewById(R.id.tb_update_bname);
				tb_update_btype=(EditText) findViewById(R.id.tb_update_btype);
				tb_update_bsrc=(EditText) findViewById(R.id.tb_update_bsrc);
				tb_update_bdest=(EditText) findViewById(R.id.tb_update_bdest);
				tb_update_cost=(EditText) findViewById(R.id.tb_update_bcost);
				tb_update_not=(EditText) findViewById(R.id.tb_update_not);
				tb_update_status=(EditText)findViewById(R.id.tb_update_status);
				but_update_back=(Button)findViewById(R.id.but_update_back);
			
				//bt_close=(Button) findViewById(R.id.bt_close);
				callgetBus(name);

			//	tb_update_arrtime=(TimePicker) findViewById(R.id.tb_update_arrtime);
				//tb_update_deptime=(TimePicker) findViewById(R.id.tb_update_deptime);
				//tb_update_arrtime.setIs24HourView(true);
				//tb_update_deptime.setIs24HourView(true);
				

			but_update_back.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						
						try
						{
					
							
							/** Called when the activity is first created. */
							String SOAP_ACTION1 = "http://tempuri.org/updateProduct";
							String METHOD_NAME1 = "updateProduct";
							Log.v("hi","hi");
							SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
						
							request.addProperty("cid",tb_update_bname.getText().toString());
							request.addProperty("status",tb_update_status.getText().toString());

							SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
							envelope.dotNet = true;
							envelope.setOutputSoapObject(request);
							HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
							androidHttpTransport.call(SOAP_ACTION1, envelope);
							SoapObject response = (SoapObject)envelope.bodyIn;
							Toast.makeText(getBaseContext(), "Success ...!", Toast.LENGTH_SHORT).show();
							
						} catch (Exception e)
						{
							//tv.setText(e.getMessage());
							e.printStackTrace();
							Log.v("hi","hello");
							
						}

					}
				});

			}


			public void callgetBus(String bid)
			{
				try
				{
					
					/** Called when the activity is first created. */
				
					
					SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
					request.addProperty("bid",bid);
				//	Log.v("Vehicle No",b_id );
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					envelope.dotNet = true;
					envelope.setOutputSoapObject(request);
					HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
					androidHttpTransport.call(SOAP_ACTION, envelope);
					SoapObject response = (SoapObject) envelope.getResponse();
					
					//Toast.makeText(getApplicationContext(),response.toString() , Toast.LENGTH_LONG).show();
					
					tb_update_bname.setText(response.getProperty(0).toString());
					tb_update_btype.setText(response.getProperty(1).toString());
					tb_update_bsrc.setText(response.getProperty(2).toString());
					tb_update_bdest.setText(response.getProperty(3).toString());
					tb_update_cost.setText(response.getProperty(4).toString());
					tb_update_not.setText(response.getProperty(5).toString());
					tb_update_status.setText(response.getProperty(6).toString());
					//String arr_time[]=response.getPropertyAsString(6).split(":");
					//String dep_time[]=response.getPropertyAsString(7).split(":");
					
					//tb_update_arrtime.setCurrentHour(Integer.parseInt(arr_time[0]));
					//tb_update_arrtime.setCurrentMinute(Integer.parseInt(arr_time[1]));
					
					//tb_update_deptime.setCurrentHour(Integer.parseInt(dep_time[0]));
				//	tb_update_deptime.setCurrentMinute(Integer.parseInt(dep_time[1]));
					
				} catch (Exception e)
				{
					//tv.setText(e.getMessage());
					e.printStackTrace();
				}
				 
			}

		}
