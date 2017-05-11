package zirai.patio;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void submitLogin(View view){
        httpClient client = new httpClient("https://ziraipatio.herokuapp.com/androidLogin");

        EditText username = (EditText)findViewById(R.id.login_id);
        EditText password = (EditText)findViewById(R.id.login_pw);
        String strUsername = username.getText().toString();
        String strPassword = password.getText().toString();
        // client.execute(strUsername, strPassword);
        Intent intent = new Intent(this, homePage.class);

        startActivity(intent);
    }

    class httpClient extends AsyncTask<String, Void, String> {

        private String u = "";
        httpClient(String u){
            this.u = u;
        }

        protected String doInBackground(String... params) {

            BufferedReader inBuffer = null;
            String result = "fail";
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost request = new HttpPost(u);
                List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("name", params[0]));

                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                        postParameters);

                request.setEntity(formEntity);
                httpClient.execute(request);
                result="got it";

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

            try{
                JSONArray array = new JSONArray(response);
            }
            catch(Exception e){
                Log.e("2", "Unexpected JSON exception", e);
            }
        }
    }
}
