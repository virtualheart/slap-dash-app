package com.example.absis;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button bt_submit,bt_register;
	EditText et_uname,et_upass;
	Context contxt;
	Spinner spin_usr_type;

	Handler handler = new Handler();
	private ProgressDialog pd;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		contxt = this;
		
		spin_usr_type=(Spinner)findViewById(R.id.spin_usr_type);

		bt_submit= (Button) this.findViewById(R.id.btn_login);
		bt_register= (Button) this.findViewById(R.id.btn_register);
		et_uname=(EditText) this.findViewById(R.id.txt_login_user);
		et_upass=(EditText) this.findViewById(R.id.txt_login_pass);
		bt_submit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(spin_usr_type.getSelectedItem().toString().trim().equalsIgnoreCase("Admin"))
				{
				Intent intObj = new Intent(MainActivity.this,AdminHome.class);
				//intObj.putExtra("name",et_uname.getText().toString());
				startActivity(intObj);
				finish();
				}
			else if(!spin_usr_type.getSelectedItem().toString().equals("Admin") &&et_uname.getText().length()!=0 && et_upass.getText().length()!=0)
				{
					login_thread();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Do not empty Username and Password", Toast.LENGTH_LONG).show();
				}
			}
		});
		bt_register.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
				
				Intent intObj1 = new Intent(MainActivity.this,Signup.class);
				intObj1.putExtra("name",et_uname.getText().toString());
				startActivity(intObj1);
				finish();
			
			}
		});
	}
	
	public void login_thread()
	{
		pd = new ProgressDialog(contxt);
		pd.setTitle("Processing...");
		pd.setMessage("Please wait.");
		pd.setCancelable(false);
		pd.setIndeterminate(true);
		pd.show();
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					call();
					handler.post(new Runnable()
					{
						@Override
						public void run()
						{
							if (pd != null)
							{
								pd.dismiss();
							}
						}
					});
				} catch (Exception e)
				{
					e.printStackTrace();
					handler.post(new Runnable()
					{
						@Override
						public void run()
						{
							if (pd != null)
							{
								pd.dismiss();
							}
						}
					});
				}
			}
		});
		thread.start();
	}
	public void call()
	{
		try
		{
			String SOAP_ACTION = "http://tempuri.org/loginService2";
			String METHOD_NAME = "loginService2";
			String NAMESPACE = "http://tempuri.org/";
			String URL = "http://athidharman.in/android/Slap/Service.asmx";
			
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			request.addProperty("name",et_uname.getText().toString());
			request.addProperty("pass",et_upass.getText().toString());			request.addProperty("up_date",spin_usr_type.getSelectedItem().toString().trim());
			request.addProperty("utype",spin_usr_type.getSelectedItem().toString().trim());

			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = (Object) envelope.getResponse();
			if(result.toString().equals("Login Success"))
			{
				handler.post(new Runnable()
				{
					@Override
					public void run()
					{
						Toast.makeText(getApplicationContext(), "Login Success ...!", Toast.LENGTH_SHORT).show();
						if(spin_usr_type.getSelectedItem().toString().trim().equalsIgnoreCase("Counselor"))
						{
						Intent intObj = new Intent(MainActivity.this,UserHome.class);
						intObj.putExtra("name",et_uname.getText().toString());
						startActivity(intObj);
						finish();
						}else{
							Intent intObj = new Intent(MainActivity.this,Home.class);
							intObj.putExtra("name",et_uname.getText().toString());
							startActivity(intObj);
							finish();
						}
					}
				});
				
			}
			else
			{
				handler.post(new Runnable()
				{
					@Override
					public void run()
					{
						et_uname.setText("");
						Toast.makeText(getApplicationContext(),"Login Failure ...!", Toast.LENGTH_SHORT).show();
					}
				});
				
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
