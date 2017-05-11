package zirai.patio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    String res = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/baloo.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        EditText e = (EditText) findViewById(R.id.login_pw);
        e.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void submitLogin(View view){
        EditText username = (EditText)findViewById(R.id.login_id);
        EditText password = (EditText)findViewById(R.id.login_pw);
        String strUsername = username.getText().toString();
        String strPassword = password.getText().toString();
        new httpClient("https://ziraipatio.herokuapp.com/login", this).execute(strUsername, strPassword);
        //new httpClient("http://192.168.0.2:1337/login", this).execute(strUsername, strPassword);
    }

    class httpClient extends AsyncTask<String, Void, String> {

        private String u = "";
        private Context c;
        httpClient(String u, Context c){
            this.u = u;
            this.c = c;
        }

        protected String doInBackground(String... params) {

            BufferedReader inBuffer = null;
            String result = "fail";
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost request = new HttpPost(u);
                List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("username", params[0]));
                postParameters.add(new BasicNameValuePair("password", params[1]));
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                        postParameters);

                request.setEntity(formEntity);
                HttpResponse response = httpClient.execute(request);
                result = EntityUtils.toString(response.getEntity());

            } catch(Exception e) {
                // Do something about exceptions
                result = e.getMessage();
            } finally {
                if (inBuffer != null) {
                    try {
                        inBuffer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return  result;
        }

        protected void onPostExecute(String response) {
            Log.e("login here HAHAHA   ", response);

            try{
                JSONObject obj = new JSONObject(response);
                String username = obj.getString("username");
                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(c.openFileOutput("config.txt", Context.MODE_PRIVATE));
                    outputStreamWriter.write(username);
                    outputStreamWriter.close();
                }
                catch (Exception e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
                changePage();
            }
            catch(Exception e){
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Invalid Login", Snackbar.LENGTH_LONG);
                snackbar.show();
                Log.e("2", "Unexpected JSON exception", e);
            }
        }

        public void changePage(){
            Intent intent = new Intent(c, homePage.class);
            startActivity(intent);
        }
    }
}
