package com.example.absis;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.absis.model.RequestHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Userlist extends AppCompatActivity {

    Button bt_back;
    ListView listView;
    private Handler handler = new Handler();
    private ProgressDialog pd;
    JSONArray complistjarr;
    Context contxt;
    ArrayList<HashMap<String,String>> cont;
    ListAdapter adp;
    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);

        getSupportActionBar().hide();

        bt_back = (Button) findViewById(R.id.bt_backul);
        listView = (ListView) findViewById(R.id.listViewul);
        cont = new ArrayList<>();

        Intent intent = getIntent();
        role = intent.getStringExtra("role");
        callAdminList();

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                // String i=adapter1.getItem(position).toString();

                String str = listView.getItemAtPosition(position).toString();
                Intent objint = new Intent(Userlist.this, ComplaintDetiles.class);
                objint.putExtra("name",str);
//                startActivity(objint);
//				startActivity(new Intent(Complainlist.this, ComplaintDetiles.class));

            }
        });
    }

    public void callAdminList()
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

        class featchlist extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                adp = new SimpleAdapter(
                        Userlist.this,cont,R.layout.layout_img,new String[]{"id","username"},new int[] {
                        R.id.id_img,R.id.name
                });
                try{

                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response

                        complistjarr = obj.getJSONArray("user");

                        for(int i=0;i<complistjarr.length();i++){
                            HashMap<String,String> con=new HashMap<>();
                            JSONObject jsonObject= complistjarr.getJSONObject(i);

                            String id=jsonObject.getString("id");
                            String name=jsonObject.getString("username");

                            con.put("id", id );

//						adpt.add(jsonObject.getString("name"));
//						adpt.add(jsonObject.getString("complaint"));
//						adpt.add(jsonObject.getString("address"));
//						adpt.add(jsonObject.getString("mobile"));
//						adpt.add(jsonObject.getString("lat"));
//						adpt.add(jsonObject.getString("lng"));
//						adpt.add(jsonObject.getString("image_name"));

                            con.put("username",name);
                            Log.i("con value ", String.valueOf(con));

                            cont.add(con);
                            Log.i("value add", String.valueOf(cont));
                        }
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

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

                liparams.put("role", role);

                return requestHandler.sendPostRequest(URLs.URL_USERRLIST,liparams);
            }
        }
        featchlist flist=new featchlist();
        flist.execute();
    }

}