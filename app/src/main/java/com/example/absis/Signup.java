package com.example.absis;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.absis.model.RequestHandler;
import com.example.absis.model.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Signup extends Activity {
	EditText txtbox_reg_user,txtbox_reg_pass,txtbox_reg_repass,txtbox_reg_mail,txtbox_reg_phone,txtbox_reg_address;
	Button btn_reg_register,btn_reg_bakck;
	Spinner spin_usr_type;
	Context contxt;
	Handler handler = new Handler();
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
contxt = this;

StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
StrictMode.setThreadPolicy(policy);


		spin_usr_type=(Spinner)findViewById(R.id.spin_usr_type);
		txtbox_reg_user=(EditText) findViewById(R.id.txtbox_reg_user);
		txtbox_reg_pass=(EditText) findViewById(R.id.txtbox_reg_pass);
		txtbox_reg_repass=(EditText) findViewById(R.id.txtbox_reg_repass);
		txtbox_reg_mail=(EditText) findViewById(R.id.txtbox_reg_mail);
		txtbox_reg_phone=(EditText) findViewById(R.id.txtbox_reg_phone);
		txtbox_reg_address=(EditText) findViewById(R.id.txtbox_reg_address);

		btn_reg_register=(Button) findViewById(R.id.btn_reg_register);
		btn_reg_bakck =(Button) findViewById(R.id.btn_reg_bakck);

		btn_reg_bakck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
				finish();
			}
		});

		btn_reg_register.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(txtbox_reg_user.getText().length()!=0 && txtbox_reg_pass.getText().length()!=0 && txtbox_reg_repass.getText().length()!=0 && txtbox_reg_mail.getText().length()!=0  && txtbox_reg_phone.getText().length()!=0 ) {

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
			final String role = spin_usr_type.getSelectedItem().toString().trim();
			final String username = txtbox_reg_user.getText().toString();
			final String password = txtbox_reg_pass.getText().toString();
			final String phone = txtbox_reg_phone.getText().toString();
			final String email = txtbox_reg_mail.getText().toString();
			final String ip=getIPAddress(true);
			final String update=getDateTime();

			class RegisterUser extends AsyncTask<Void, Void, String> {

				private ProgressBar progressBar;
				@Override
				protected String doInBackground(Void... voids) {
					//creating request handler object
					RequestHandler requestHandler = new RequestHandler();

					//creating request parameters
					HashMap<String, String> params = new HashMap<>();
					params.put("username", username);
					params.put("email", email);
					params.put("password", password);
					params.put("cpassword", password);
					params.put("mobile", phone);
					params.put("ip", ip);
					params.put("up_date", update);
					params.put("role", role);

					//returing the response
					return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);
				}

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					//displaying the progress bar while user registers on the server
				}

				@Override
				protected void onPostExecute(String s) {
					super.onPostExecute(s);
					//hiding the progressbar after completion

					try {
						//converting response to json object
						JSONObject obj = new JSONObject(s);

						//if no error in response
						if (!obj.getBoolean("error")) {
							Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

							//getting the user from the response
							JSONObject userJson = obj.getJSONObject("user");

							//creating a new user object
							User user = new User(
									userJson.getInt("id"),
									userJson.getString("username"),
									userJson.getString("mobile"),
									userJson.getString("role")
							);

							//storing the user in shared preferences
							SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

							//starting the profile activity
//							startActivity(new Intent(getApplicationContext(), Home.class));
							Intent objint = new Intent(Signup.this,Login.class);
							startActivity(objint);
							finish();

						} else if (obj.getBoolean("error")) {
							Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
						}
						pd.dismiss();

					} catch (JSONException e) {
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
						pd.dismiss();

					}
				}
			}
			//executing the async task
			RegisterUser ru = new RegisterUser();
			ru.execute();

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
