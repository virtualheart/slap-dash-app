package com.example.absis;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.absis.model.RequestHandler;
import com.example.absis.model.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;

public class ComplaintDetiles extends AppCompatActivity {
    String name,id,img_url,complaint,address,mobile,up_date,commands,com_state,chrole;
    TextView txt_name,txt_complaint,txt_address,txt_mobile,txt_date,txt_commands,txt_state;
    ImageView img_compd;
    Button btn_compd_bck,btn_compd_update;
    private ProgressDialog pd;
    Context contxt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_detiles);

        User user = SharedPrefManager.getInstance(this).getUser();

        contxt = this;
        Intent ini = getIntent();
        name = ini.getStringExtra("name");
        chrole=ini.getStringExtra("role");

//        Toast.makeText(getApplicationContext(), chrole, Toast.LENGTH_SHORT).show();
        try {
//            name=name.replaceAll(" ","");
            JSONObject jsonObject = null;
            jsonObject = new JSONObject(name);
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("id", jsonObject.get("id"));
            id= String.valueOf(jsonObject1.get("id"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        StrictMode.ThreadPolicy sp = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(sp);

        getSupportActionBar().hide();

        txt_name=(TextView) findViewById(R.id.compd_name);
        txt_complaint=(TextView) findViewById(R.id.compd_complaint);
        txt_address=(TextView) findViewById(R.id.compd_address);
        txt_mobile=(TextView) findViewById(R.id.compd_mob);
        txt_date=(TextView) findViewById(R.id.compd_date);
        txt_commands=(TextView)findViewById(R.id.compd_commands);
        txt_state=(TextView)findViewById(R.id.compd_state);

        img_compd = (ImageView) findViewById(R.id.compd_img);
        btn_compd_update=(Button) findViewById(R.id.compd_update);
        btn_compd_bck=(Button) findViewById(R.id.compd_bck);

        if(!(chrole.equals("admin"))){
            btn_compd_update.setVisibility(View.GONE);
        }

        featchdata();

        btn_compd_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(ComplaintDetiles.this,Complainlist.class));
                onBackPressed();
                finish();
            }
        });

        btn_compd_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //img_url,complaint,address,mobile,up_date
                Intent intent=new Intent(getApplicationContext(),Details.class);
                intent.putExtra("id",id);
                intent.putExtra("name",name);
                intent.putExtra("complaint",complaint);
                intent.putExtra("address",address);
                intent.putExtra("mobile",mobile);
                intent.putExtra("commands",commands);
                intent.putExtra("up_date",up_date);
                intent.putExtra("com_state",com_state);
                startActivity(intent);
            }
        });

    }

    private void featchdata() {

        pd = new ProgressDialog(contxt);
        pd.setTitle("Processing...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();

        class featchdata extends AsyncTask<Void,Void, String>{

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String,String> parm = new HashMap<>();
                parm.put("id",id);
                return requestHandler.sendPostRequest(URLs.URL_USERCOMPLIST,parm);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {

                    JSONObject obj = new JSONObject(s);
                    JSONArray jsonArr = obj.getJSONArray("usercomplist");

                    for(int i=0;i<jsonArr.length();i++) {

                        HashMap<String, String> con = new HashMap<>();
                        JSONObject jsonObject = jsonArr.getJSONObject(i);

                        img_url = jsonObject.getString("img");
                        name = jsonObject.getString("name");
                        complaint = jsonObject.getString("complaint");
                        address = jsonObject.getString("address");
                        mobile = jsonObject.getString("mobile");
                        up_date = jsonObject.getString("up_date");
                        commands = jsonObject.getString("commands");
                        com_state = jsonObject.getString("com_state");

                        URL com_img = new URL(img_url);
                        Bitmap bitmap = BitmapFactory.decodeStream(com_img.openConnection().getInputStream());
                        img_compd.setImageBitmap(bitmap);

                        txt_name.setText(name);
                        txt_complaint.setText(complaint);
                        txt_address.setText(address);
                        txt_mobile.setText(mobile);
                        txt_date.setText(up_date);
                        txt_state.setText(com_state);
                        txt_commands.setText(commands);

                        txt_name.setVisibility(View.VISIBLE);
                        txt_complaint.setVisibility(View.VISIBLE);
                        txt_address.setVisibility(View.VISIBLE);
                        txt_mobile.setVisibility(View.VISIBLE);
                        txt_date.setVisibility(View.VISIBLE);
                        img_compd.setVisibility(View.VISIBLE);
                        txt_commands.setVisibility(View.VISIBLE);
                        txt_state.setVisibility(View.VISIBLE);

                        img_compd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent iobj = new Intent(ComplaintDetiles.this, GetView.class);
                                iobj.putExtra("img", img_url);

                                startActivity(iobj);                            }
                        });
                    }
                }catch (Exception e){

                    Toast.makeText(contxt, "Data fetch error", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();
                    e.printStackTrace();
                }
                pd.dismiss();
            }
        }
        featchdata fd = new featchdata();
        fd.execute();
    }

}