package com.example.absis;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NextPage extends Activity {
	EditText txtbox_reg_user,txtbox_reg_pass,txtbox_reg_repass,txtbox_reg_mail,txtbox_reg_phone;
	Button btn_reg_register,btn_getlocation,bt_back,btn_nxt_upload;
	Gpsservice myGpsclass;
	   double mysourcelat,mySourcelng;
		private static int RESULT_LOAD_IMAGE = 1;
		String iMage;
	    private static final int CAMERA_REQUEST = 1888; 

	Context contxt;
	Handler handler = new Handler();
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_next_page);
contxt = this;
myGpsclass=new Gpsservice(getApplicationContext());
	
		txtbox_reg_user=(EditText) findViewById(R.id.txtbox_reg_user);
		txtbox_reg_pass=(EditText) findViewById(R.id.txtbox_reg_repass);
		txtbox_reg_mail=(EditText) findViewById(R.id.txtbox_reg_mail);
		txtbox_reg_phone=(EditText) findViewById(R.id.txtbox_reg_phone);
		btn_getlocation=(Button) findViewById(R.id.btn_getlocation);
		btn_reg_register=(Button) findViewById(R.id.btn_nxt_register);
		
		btn_nxt_upload=(Button) findViewById(R.id.btn_nxt_upload);

		bt_back=(Button) findViewById(R.id.bt_back);
		
		bt_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent iobj = new Intent(NextPage.this,Home.class);
		            startActivity(iobj);
			}
		});
		

		btn_nxt_upload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
	            startActivityForResult(cameraIntent, CAMERA_REQUEST);
//				startActivityForResult(i, RESULT_LOAD_IMAGE);
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
//                startActivityForResult(cameraIntent, CAMERA_REQUEST); 

			}
		});
		btn_getlocation.setOnClickListener(new View.OnClickListener() {
		         public void onClick(View view) {

			           // displayCurrentLocation();
			        	 mysourcelat=myGpsclass.Findlatitude();
			        	
							mySourcelng=myGpsclass.FindLongitude();
							 Log.v("Lat", mySourcelng+"");
							  Log.v("Lng", mySourcelng+"");
							Geocoder aGeocoder=new Geocoder(getApplicationContext());
							try {
								List<Address> aList = aGeocoder.getFromLocation(mysourcelat, mySourcelng, 2);
								String aSublocality=aList.get(0).getSubLocality()+","+aList.get(0).getLocality();
								txtbox_reg_mail.setText(aSublocality);
								Log.e("Sub locality",aList+"");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			         
		    
		         }
		      });
		btn_reg_register.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(txtbox_reg_user.getText().length()!=0 && txtbox_reg_pass.getText().length()!=0  && txtbox_reg_mail.getText().length()!=0  && txtbox_reg_phone.getText().length()!=0)
				{
				
						login_thread();
					
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Do not empty any field", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK ) {
			
			 Bitmap photo = (Bitmap) data.getExtras().get("data"); 
//	          imgView.setImageBitmap(photo);
	            
	            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	            photo.compress(Bitmap.CompressFormat.PNG, 50,
						byteArrayOutputStream);
	            byte[]	bimgdata = byteArrayOutputStream.toByteArray();
	    		iMage = Base64.encodeToString(
	    					bimgdata, Base64.DEFAULT);
	    		login_thread();	
	    		//	imgCaptureService();

//			Uri selectedImage = data.getData();
//			String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//			Cursor cursor = getContentResolver().query(selectedImage,
//					filePathColumn, null, null, null);
//			cursor.moveToFirst();
//
//			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//			String picturePath = cursor.getString(columnIndex);
//			cursor.close();
//            Uri filePath = data.getData();
//			Bitmap bitmap;
//			try {
//				bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//				 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//		            bitmap.compress(Bitmap.CompressFormat.PNG, 50,
//							byteArrayOutputStream);
//		            byte[]	bimgdata = byteArrayOutputStream.toByteArray();
//		    		iMage = Base64.encodeToString(
//		    					bimgdata, Base64.DEFAULT);
//		    		login_thread();		    	//	imgCaptureService();
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		     // Bitmap photo = (Bitmap) data.getExtras().get("data"); 
//	          imgView.setImageBitmap(photo);
	            
	           	//ImageView imageView = (ImageView) findViewById(R.id.imgView);
			//imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

		}
    
    
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
			String SOAP_ACTION = "http://tempuri.org/addNewComplaint";
			String METHOD_NAME = "addNewComplaint";
			String NAMESPACE = "http://tempuri.org/";
			String URL = "http://athidharman.in/android/Slap/Service.asmx";
			Calendar c = Calendar.getInstance();
			System.out.println("Current time => " + c.getTime());

			SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
			String formattedDate = df.format(c.getTime());

			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			request.addProperty("uname",txtbox_reg_user.getText().toString());
			request.addProperty("comp",txtbox_reg_pass.getText().toString()+""+formattedDate+""+c.getTime());
			request.addProperty("loc",txtbox_reg_mail.getText().toString());
			request.addProperty("phone",txtbox_reg_phone.getText().toString());
	
			request.addProperty("img",iMage);

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
						Toast.makeText(getApplicationContext(), "Complaint Posted ...!", Toast.LENGTH_LONG).show();

						Intent intObj1 = new Intent(NextPage.this,Home.class);
						//intObj1.putExtra("name",et_uname.getText().toString());
						startActivity(intObj1);
						finish();
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
	
	 public void imgCaptureService() {

			try {

				String URL="http://athidharman.in/android/Securesys/Service.asmx";
				final String NAMESPACE = "http://tempuri.org/";

				String SOAP_ACTION = "http://tempuri.org/newblog1";
				String METHOD_NAME = "newblog1";
			
				//String URL = "http://cyberstudents.in/android/Toyota_final/Service.asmx";

				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

				
				
		
					
//					Log.e(aAddition, "Java Bala");
		          
					request.addProperty("img",iMage);
					
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);
					envelope.dotNet = true;
					envelope.setOutputSoapObject(request);
					HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
					androidHttpTransport.call(SOAP_ACTION, envelope);
					final Object result = (Object) envelope.getResponse();
					Toast.makeText(contxt, "Success...!", Toast.LENGTH_SHORT);
					 Intent intent1 = new Intent(NextPage.this, NextPage.class);
						
	                 startActivity(intent1);
					if (result.toString().equals("Blog Posted")) {

						Toast.makeText(contxt, "Success...!", Toast.LENGTH_SHORT)
								.show();
						 Intent intent = new Intent(NextPage.this, Home.class);
							
		                 startActivity(intent);

					} else {

						Toast.makeText(contxt, result.toString(), Toast.LENGTH_SHORT)
								.show();
						 Intent intent = new Intent(NextPage.this, NextPage.class);
							
		                 startActivity(intent);
					}
				
			} catch (Exception e) {
		e.printStackTrace();
			}
		}

}
