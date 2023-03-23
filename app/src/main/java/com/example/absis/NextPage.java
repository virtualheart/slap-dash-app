package com.example.absis;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.absis.model.RequestHandler;
import com.example.absis.model.SharedPrefManager;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class NextPage extends Activity {

	private int GALLERY = 1, CAMERA = 2;
	EditText txtbox_reg_user,txtbox_reg_pass,txtbox_reg_repass,txtbox_reg_mail,txtbox_reg_phone;
	Button btn_reg_register,btn_getlocation,bt_back,btn_nxt_upload;
	ImageView img_compimg;
	double mysourcelat,mySourcelng;
	Context contxt;
	private ProgressDialog pd;
	Handler handler = new Handler();
	Bitmap FixBitmap;
	String ConvertImage ;
	byte[] byteArray ;
	String GetImageNameFromEditText,name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_next_page);
		contxt = this;

		txtbox_reg_user=(EditText) findViewById(R.id.txtbox_reg_user);
		txtbox_reg_pass=(EditText) findViewById(R.id.txtbox_reg_repass);
		txtbox_reg_mail=(EditText) findViewById(R.id.txtbox_reg_mail);
		txtbox_reg_phone=(EditText) findViewById(R.id.txtbox_reg_phone);
		btn_getlocation=(Button) findViewById(R.id.btn_getlocation);
		btn_reg_register=(Button) findViewById(R.id.btn_nxt_register);
		img_compimg=(ImageView)findViewById(R.id.compimg);
		btn_nxt_upload=(Button) findViewById(R.id.btn_nxt_upload);

		if(!SharedPrefManager.getInstance(this).isLoggedIn()){
			startActivity(new Intent(NextPage.this,MainActivity.class));
			finish();
		}

		User user = SharedPrefManager.getInstance(this).getUser();
		name=String.valueOf(user.getUsername());

		txtbox_reg_user.setText(name);
		txtbox_reg_user.setKeyListener(null);
		txtbox_reg_user.setEnabled(false);

		bt_back=(Button) findViewById(R.id.bt_back);

		bt_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
				finish();
			}
		});


		btn_nxt_upload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPictureDialog();
			}
		});

		btn_getlocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// displayCurrentLocation();
				if (ContextCompat.checkSelfPermission(contxt, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contxt, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
					ActivityCompat.requestPermissions(NextPage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

				} else {
//					Toast.makeText(contxt, "You need have granted permission", Toast.LENGTH_SHORT).show();
					Gpsservice gps = new Gpsservice(contxt, NextPage.this);

					// Check if GPS enabled
					if (gps.canGetLocation()) {

						mysourcelat = gps.getLatitude();
						mySourcelng = gps.getLongitude();

						// \n is for new line
					} else {
						// Can't get location.
						// GPS or network is not enabled.
						// Ask user to enable GPS/network in settings.
						gps.showSettingsAlert();
					}

					Log.v("Lat", mySourcelng + "");
					Log.v("Lng", mySourcelng + "");

					Geocoder aGeocoder = new Geocoder(getApplicationContext());
					try {
						List<Address> aList = aGeocoder.getFromLocation(mysourcelat, mySourcelng, 1);
						Log.e("Sub locality", aList + "");
						if (aList.size() > 0) {
//							String aSublocality = aList.get(0).getSubLocality() + "," + aList.get(0).getLocality();
							String aSublocality = aList.get(0).getLocality();

							txtbox_reg_mail.setText(aSublocality);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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

	private void showPictureDialog() {
		AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
		pictureDialog.setTitle("Select Action");
		String[] pictureDialogItems = {
				"Photo Gallery",
				"Camera" };
		pictureDialog.setItems(pictureDialogItems,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
							case 0:
								choosePhotoFromGallary();
								break;
							case 1:
								takePhotoFromCamera();
								break;
						}
					}
				});
		pictureDialog.show();
	}
	public void choosePhotoFromGallary() {
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		startActivityForResult(galleryIntent, GALLERY);

	}

	private void takePhotoFromCamera() {
		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, CAMERA);
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    FixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    // String path = saveImage(bitmap);
                    //Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    img_compimg.setImageBitmap(FixBitmap);
                    img_compimg.setVisibility(View.VISIBLE);
					btn_reg_register.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(NextPage.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            FixBitmap = (Bitmap) data.getExtras().get("data");
            img_compimg.setImageBitmap(FixBitmap);
            img_compimg.setVisibility(View.VISIBLE);
            //  saveImage(thumbnail);
            //Toast.makeText(ShadiRegistrationPart5.this, "Image Saved!", Toast.LENGTH_SHORT).show();
			btn_reg_register.setVisibility(View.VISIBLE);

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
//					handler.post(new Runnable()
//					{
//						@Override
//						public void run()
//						{
//							if (pd != null)
//							{
//								pd.dismiss();
//							}
//						}
//					});
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
	public void call() {
		final String name = txtbox_reg_user.getText().toString().trim();
		final String complint = txtbox_reg_pass.getText().toString().trim();
		final String area = txtbox_reg_mail.getText().toString().trim();
		final String mobile = txtbox_reg_phone.getText().toString().trim();
		final String ip=getIPAddress(true);
		final String update=getDateTime();

		Random r = new Random();
		GetImageNameFromEditText = "img_" + r.nextInt(999999999);

		ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();

		FixBitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream1);

		byteArray = byteArrayOutputStream1.toByteArray();

		ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
//		Log.e("image processed", "image processed " + ConvertImage);
//		Log.e("image processed name", "image processed " + GetImageNameFromEditText);


		class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(String s) {
				super.onPostExecute(s);
				try {
					//converting response to json object
					JSONObject obj = new JSONObject(s);

					//if no error in response
					if (!obj.getBoolean("error")) {
						Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
						startActivity(new Intent(getApplicationContext(), Home.class));
						finish();
					}else if (obj.getBoolean("error")) {
						Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
					}
				}catch(Exception e){
						Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
				}
				pd.dismiss();
			}

			@Override
			protected String doInBackground(Void... voids) {
				RequestHandler requestHandler = new RequestHandler();

				HashMap<String, String> HashMapParams = new HashMap<String, String>();

				HashMapParams.put("image_name", GetImageNameFromEditText);
				HashMapParams.put("image_tag", ConvertImage);
				HashMapParams.put("name", name);
				HashMapParams.put("complaint", complint);
				HashMapParams.put("address", area);
				HashMapParams.put("mobile", mobile);
				HashMapParams.put("lat", String.valueOf(mysourcelat));
				HashMapParams.put("lng", String.valueOf(mySourcelng));
				HashMapParams.put("ip", ip);
				HashMapParams.put("up_date", update);

				Log.e("request  processed", "requesr processed");
				return requestHandler.sendPostRequest(URLs.URL_COMPFILE, HashMapParams);

			}
		}
		AsyncTaskUploadClass as = new AsyncTaskUploadClass();
		as.execute();
	}

	public String getDateTime()
	{
		//DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
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


	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 5) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// Now user should be able to use camera

			} else {

				Toast.makeText(NextPage.this, "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();

			}
		}
	}
}
