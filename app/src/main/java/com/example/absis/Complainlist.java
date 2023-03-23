package com.example.absis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.absis.model.RequestHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Complainlist extends Activity {
	Button bt_back;
	ListView listView;
	private Handler handler = new Handler();
	private ProgressDialog pd;
	JSONArray complistjarr;
	Context contxt;
	ArrayList<HashMap<String,String>> cont;
	ListAdapter adp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		contxt = this;
		setContentView(R.layout.activity_complainlist);
		cont = new ArrayList<>();

		listView = (ListView) findViewById(R.id.listView2);
		bt_back=(Button) findViewById(R.id.bt_back);

	callBusList();
	
	   bt_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				 Intent intent = new Intent(Complainlist.this, UserHome.class);
//                	startActivity(intent);
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
				Intent objint = new Intent(Complainlist.this, ComplaintDetiles.class);
				objint.putExtra("name",str);
				objint.putExtra("role","admin");
				startActivity(objint);
//				startActivity(new Intent(Complainlist.this, ComplaintDetiles.class));

			}
		});
	}
		
	public void callBusList()
	{
		pd = new ProgressDialog(contxt);
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
						Complainlist.this,cont,R.layout.layout_img,new String[]{"id","complaint"},new int[] {
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
				if (pd != null)
				{
						pd.dismiss();
				}
			}


			@Override
			protected String doInBackground(Void... voids) {
				RequestHandler requestHandler = new RequestHandler();
				HashMap<String, String> liparams = new HashMap<>();
				liparams.put("list", "list");

				return requestHandler.sendPostRequest(URLs.URL_COMPLIST,liparams);
			}
		}
		featchlist flist=new featchlist();
		flist.execute();
	}
}