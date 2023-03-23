package com.example.absis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.absis.model.RequestHandler;
import com.example.absis.model.SharedPrefManager;

import org.json.JSONObject;

import java.util.HashMap;

public class Details extends Activity {

    EditText txtbox_reg_user, txtbox_reg_pass, txtbox_reg_commands, txtbox_reg_area, txtbox_reg_phone;
    String name, id, img_url, complaint, address, mobile, up_date, commands, com_state;
    TextView txt_state;
    ImageView img_compimg;
    Spinner spin_com_state;
    Handler handler = new Handler();
    Button btn_compd_bck, btn_compd_update;
    private ProgressDialog pd;
    Context contxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //readypage();

        contxt = this;
        spin_com_state = (Spinner) findViewById(R.id.spin_com_state);
        txtbox_reg_user = (EditText) findViewById(R.id.txtbox_reg_user_up);
        txtbox_reg_pass = (EditText) findViewById(R.id.txtbox_reg_repass_up);
        txtbox_reg_area = (EditText) findViewById(R.id.txtbox_reg_area_up);
        txtbox_reg_phone = (EditText) findViewById(R.id.txtbox_reg_phone_up);
        txtbox_reg_commands = (EditText) findViewById(R.id.txtbox_reg_commands_up);
        img_compimg = (ImageView) findViewById(R.id.compimg);
        txt_state = (TextView) findViewById(R.id.compd_state);

        btn_compd_bck = (Button) findViewById(R.id.bt_back_up);
        btn_compd_update = (Button) findViewById(R.id.btn_nxt_register_up);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        Intent getval = getIntent();

        id = getval.getStringExtra("id");
        name = getval.getStringExtra("name");
        complaint = getval.getStringExtra("complaint");
        address = getval.getStringExtra("address");
        mobile = getval.getStringExtra("mobile");
        commands = getval.getStringExtra("commands");
        up_date = getval.getStringExtra("up_date");
        com_state = getval.getStringExtra("com_state");

        txtbox_reg_user.setText(name);
        txtbox_reg_user.setEnabled(false);
        txtbox_reg_pass.setText(complaint);
        txtbox_reg_pass.setEnabled(false);
        txtbox_reg_area.setText(address);
        txtbox_reg_area.setEnabled(false);
        txtbox_reg_phone.setText(mobile);
        txtbox_reg_phone.setEnabled(false);
        txtbox_reg_commands.setText(commands);
        txt_state.setText(com_state);

//		txtbox_reg_user.setText(name);
//		txtbox_reg_user.setKeyListener(null);
//		txtbox_reg_user.setEnabled(false);

        btn_compd_bck.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();
                finish();
            }
        });

        btn_compd_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_thread();
            }
        });
    }

    public void login_thread() {
        pd = new ProgressDialog(contxt);
        pd.setTitle("Processing...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    call();
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (pd != null) {
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

        final String state = spin_com_state.getSelectedItem().toString().trim();
        final String commands = txtbox_reg_commands.getText().toString().trim();

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
                        onBackPressed();
                        finish();
                    } else if (obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                }
                pd.dismiss();
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> HashMapParams = new HashMap<String, String>();

                HashMapParams.put("commands", commands);
                HashMapParams.put("com_state", state);
                HashMapParams.put("id", id);


                Log.e("request  processed", "requesr processed");
                return requestHandler.sendPostRequest(URLs.URL_UPDATECOM, HashMapParams);

            }
        }
        AsyncTaskUploadClass as = new AsyncTaskUploadClass();
        as.execute();
    }

}

