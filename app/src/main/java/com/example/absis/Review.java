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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.absis.model.RequestHandler;
import com.example.absis.model.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Review extends Activity {
	Button bt_back;
	String regno, stdid, fname, format;
	String eid, it;
	String sno;
	View promptsView,promptsView1;
	ListView listView;
	private static String uname,phone;
	private Handler handler = new Handler();
	ArrayAdapter<String> adpt;
	private ProgressDialog pd;
	Context contxt;
	JSONArray complistjarr;
	ArrayList<HashMap<String,String>> cont;
	ListAdapter adp;
	String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		setContentView(R.layout.activity_complainlist);

		if(!SharedPrefManager.getInstance(this).isLoggedIn()){
			startActivity(new Intent(Review.this,MainActivity.class));
			finish();
		}

		User user = SharedPrefManager.getInstance(this).getUser();
		name=String.valueOf(user.getUsername());

		//adpt = new ArrayAdapter<String>(this, R.layout.layout_img, R.id.name);
		cont = new ArrayList<>();

		listView = (ListView) findViewById(R.id.listView2);
		bt_back=(Button) findViewById(R.id.bt_back);

		callBusList();

		bt_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(Review.this, Home.class);
//				intent.putExtra("name",name);
//				startActivity(intent);
				onBackPressed();
				finish();
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				// String i=adapter1.getItem(position).toString();

				String str = listView.getItemAtPosition(position).toString();
				Intent iobj = new Intent(Review.this, ComplaintDetiles.class);
				iobj.putExtra("name", str);
				iobj.putExtra("role","user");
				startActivity(iobj);

			}
		});
	}


	public void callBusList()
	{
		pd = new ProgressDialog(this);
		pd.setTitle("Processing...");
		pd.setMessage("Please wait.");
		pd.setCancelable(false);
		pd.setIndeterminate(true);
		pd.show();
		try
		{

			Thread thread = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						featclist();
//						handler.post(new Runnable()
//						{
//							@Override
//							public void run()
//							{
//								if (pd != null)
//								{
//									pd.dismiss();
//								}
//							}
//						});
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

		} catch (Exception e)
		{
			//tv.setText(e.getMessage());
			e.printStackTrace();
		}
	}
	private void featclist(){

		class featchlist extends AsyncTask<Void, Void, String>{
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(String s) {
				super.onPostExecute(s);

				adp = new SimpleAdapter(
						Review.this,cont,R.layout.layout_img,new String[]{"id","complaint"},new int[] {
						R.id.id_img,R.id.name
				});
				try{

					JSONObject obj = new JSONObject(s);

					//if no error in response
					if (!obj.getBoolean("error")) {
						Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

						//getting the user from the response

						complistjarr = obj.getJSONArray("complist");

						for(int i=0;i<complistjarr.length();i++){
							HashMap<String,String> con=new HashMap<>();
							JSONObject jsonObject= complistjarr.getJSONObject(i);

							String id=jsonObject.getString("id");
							String name="'"+jsonObject.getString("complaint")+"'";

							con.put("id",id);
//						adpt.add(jsonObject.getString("name"));
//						adpt.add(jsonObject.getString("complaint"));
//						adpt.add(jsonObject.getString("address"));
//						adpt.add(jsonObject.getString("mobile"));
//						adpt.add(jsonObject.getString("lat"));
//						adpt.add(jsonObject.getString("lng"));
//						adpt.add(jsonObject.getString("image_name"));

							con.put("complaint",name);

							cont.add(con);
						}

					} else if(obj.getBoolean("error")) {
						Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

					}else {
						Toast.makeText(getApplicationContext(), "Some Think wrong", Toast.LENGTH_SHORT).show();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				listView.setAdapter(adp);
				pd.dismiss();
			}

			@Override
			protected String doInBackground(Void... voids) {
				RequestHandler requestHandler = new RequestHandler();
				HashMap<String, String> liparams = new HashMap<>();
				liparams.put("name", name);

				return requestHandler.sendPostRequest(URLs.URL_USERCLIST,liparams);
			}
		}
		featchlist flist=new featchlist();
		flist.execute();
	}
}