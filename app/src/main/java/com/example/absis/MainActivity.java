package com.example.absis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.absis.model.RequestHandler;
import com.example.absis.model.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends Activity {
    Button bt_submit,bt_register;
    EditText et_uname,et_upass;
    Context contxt;
    Handler handler = new Handler();
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contxt = this;

        //first getting the values
        bt_submit= (Button) this.findViewById(R.id.btn_login);
        bt_register= (Button) this.findViewById(R.id.btn_register);
        et_uname=(EditText) this.findViewById(R.id.txt_login_user);
        et_upass=(EditText) this.findViewById(R.id.txt_login_pass);


        bt_submit.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                login_thread();
            }
        });
        bt_register.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intObj1 = new Intent(MainActivity.this,Register.class);
                startActivity(intObj1);
//                finish();
            }
        });
    }

    public void login_thread()
    {//validating inputs
        final String username = et_uname.getText().toString();
        final String password = et_upass.getText().toString();
        if (TextUtils.isEmpty(username)) {
            et_uname.setError("Please enter your username");
            et_uname.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            et_upass.setError("Please enter your password");
            et_upass.requestFocus();
            return;
        }

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
                    userLogin(username,password);
//                    handler.post(new Runnable()
//                    {
//                        @Override
//                        public void run()
//                        {
//                            if (pd != null)
//                            {
//                                pd.dismiss();
//                            }
//                        }
//                    });
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
    private void userLogin(String username,String password) {

        //if everything is fine

        class UserLogin extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

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
                        Toast.makeText(MainActivity.this, "Welcome "+userJson.getString("username"), Toast.LENGTH_SHORT).show();
                        if(userJson.getString("role").equals("admin")){
                            Intent intObj1 = new Intent(getApplicationContext(),UserHome.class);
                            startActivity(intObj1);
                        } else if (userJson.getString("role").equals("user")) {
                            Intent intObj2 = new Intent(getApplicationContext(),Home.class);
                            startActivity(intObj2);
                        }
                        pd.dismiss();

                        finish();
                    } else if (obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.dismiss();

                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);

                //returing the response

                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}
