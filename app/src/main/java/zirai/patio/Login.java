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
        httpClient client = new httpClient("https://ziraipatio.herokuapp.com/api/appData");



//        Log.e("1", res);
        EditText username = (EditText)findViewById(R.id.login_id);
        EditText password = (EditText)findViewById(R.id.login_pw);
        String strUsername = username.getText().toString();
        String strPassword = password.getText().toString();
        // client.execute(strUsername, strPassword);
        Intent intent = new Intent(this, homePage.class);
        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();
        try{
            jo.put("appName", "Spin'zer for Kids");
            jo.put("image", "https://puu.sh/vLmqa/63f1137485.png");

            ja.put(jo);

            jo = new JSONObject();
            jo.put("appName", "Angry Gorilla");
            jo.put("image", "https://puu.sh/vMu8I/935de3032f.jpg");

            ja.put(jo);
        } catch (Exception e)
        {

        }

        intent.putExtra("JSON", ja.toString());
        startActivity(intent);
    }

    class httpClient extends AsyncTask<String, Void, String> {

        private String u = "";
        httpClient(String u){
            this.u = u;
        }

        protected String doInBackground(String... params) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(u);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("username", params[0]));
                nameValuePairs.add(new BasicNameValuePair("password", params[1]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

            } catch (Exception e) {
                Log.e("Post error: ", e.toString());
            }
            return "";
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
