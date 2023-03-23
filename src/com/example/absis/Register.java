package com.example.absis;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {
	EditText txtbox_reg_user,txtbox_reg_pass,txtbox_reg_repass,txtbox_reg_mail,txtbox_reg_phone;
	Button btn_reg_register;
	
	Context contxt;
	Handler handler = new Handler();
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
contxt = this;
		
		txtbox_reg_user=(EditText) findViewById(R.id.txtbox_reg_user);
		txtbox_reg_pass=(EditText) findViewById(R.id.txtbox_reg_pass);
		txtbox_reg_repass=(EditText) findViewById(R.id.txtbox_reg_repass);
		txtbox_reg_mail=(EditText) findViewById(R.id.txtbox_reg_mail);
		txtbox_reg_phone=(EditText) findViewById(R.id.txtbox_reg_phone);
		
		
		
		
		btn_reg_register=(Button) findViewById(R.id.btn_reg_register);
		
		btn_reg_register.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(txtbox_reg_user.getText().length()!=0 && txtbox_reg_pass.getText().length()!=0 && txtbox_reg_repass.getText().length()!=0 && txtbox_reg_mail.getText().length()!=0  && txtbox_reg_phone.getText().length()!=0)
				{
					if(txtbox_reg_pass.getText().toString().equals(txtbox_reg_repass.getText().toString()))
					{
						login_thread();
					}
					else
					{
						Toast.makeText(getApplicationContext(), "Password not Match", Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Do not empty any field", Toast.LENGTH_LONG).show();
				}
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
			String SOAP_ACTION = "http://tempuri.org/useregService";
			String METHOD_NAME = "useregService";
			String NAMESPACE = "http://tempuri.org/";
			String URL = "http://athidharman.in/android/Slap/Service.asmx";
			
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			request.addProperty("name",txtbox_reg_user.getText().toString());
			request.addProperty("pass",txtbox_reg_pass.getText().toString());
			request.addProperty("phone",txtbox_reg_phone.getText().toString());
			request.addProperty("mail",txtbox_reg_mail.getText().toString());
			request.addProperty("ip",getIPAddress(true));
			request.addProperty("up_date",getDateTime());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			//HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,6000);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = (Object) envelope.getResponse();
			
			if(result.toString().equals("Success"))
			{
				handler.post(new Runnable()
				{
					@Override
					public void run()
					{
						Toast.makeText(getApplicationContext(), "Registration Success ...!", Toast.LENGTH_SHORT).show();
					}
				});
				
				finish();
			}
			else
			{
				handler.post(new Runnable()
				{
					@Override
					public void run()
					{
						txtbox_reg_pass.setText("");
						txtbox_reg_repass.setText("");
						Toast.makeText(getApplicationContext(),"Registration Failure ...!", Toast.LENGTH_SHORT).show();
					}
				});
				
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String getDateTime() 
	 { 
		 //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		 Date date = new Date(); 
		 return dateFormat.format(date); 
	 }
	
	public static String getIPAddress(boolean useIPv4)
	{
		try
		{
			List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces)
			{
				List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
				for (InetAddress addr : addrs)
				{
					if (!addr.isLoopbackAddress())
					{
						String sAddr = addr.getHostAddress().toUpperCase();
						boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
						if (useIPv4)
						{
							if (isIPv4)
								return sAddr;
						} else
						{
							if (!isIPv4)
							{
								int delim = sAddr.indexOf('%'); // drop ip6 port
																// suffix
								return delim < 0 ? sAddr : sAddr.substring(0, delim);
							}
						}
					}
				}
			}
		} catch (Exception ex)
		{
		} // for now eat exceptions
		return "";
	}
}
