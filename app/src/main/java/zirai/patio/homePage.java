package zirai.patio;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class homePage extends AppCompatActivity {
    String res = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        httpClient client = new httpClient("https://ziraipatio.herokuapp.com/api/appData", this);
        client.execute();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/baloo.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    class httpClient extends AsyncTask<Void, Void, String> {

        private Exception exception;
        private String u = "";
        private Context c;
        httpClient(String u, Context c){
            this.u = u;
            this.c = c;
        }

        protected String doInBackground(Void... urls) {

            try {
                URL url = new URL(u);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {

            try{
                JSONArray array = new JSONArray(response);
                LinearLayout lv = (LinearLayout) findViewById(R.id.appList);
                if(array.length() == 0)
                {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.topMargin = 100;
                    params.bottomMargin = 50;
                    params.gravity =  Gravity.BOTTOM | Gravity.CENTER;
                    ImageView imgView = new ImageView(c);
                    Picasso.with(c).load(R.drawable.patio).resize(800, 500).centerCrop().into(imgView);

                    TextView textView = new TextView(c);
                    Typeface face=Typeface.createFromAsset(getAssets(), "fonts/baloo.ttf");
                    textView.setText("No app was subscribed. Please subscribe at our web store.");
                    textView.setTypeface(face);
                    textView.setTextSize(25);
                    textView.setGravity(Gravity.CENTER);
                    lv.addView(imgView, params);
                    lv.addView(textView);
                }
                else
                {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.topMargin = 30;
                    params.bottomMargin = 30;
                    params.gravity = Gravity.BOTTOM | Gravity.CENTER;
                    for(int a = 0; a < array.length(); a ++) {
                        JSONObject obj = array.getJSONObject(a);

                        ImageView imgView = new ImageView(c);
                        String url = obj.getString("image");
                        Picasso.with(c).load(url).resize(500, 500).centerCrop().into(imgView);

                        TextView textView = new TextView(c);
                        String name = obj.getString("appName");
                        Typeface face=Typeface.createFromAsset(getAssets(), "fonts/baloo.ttf");
                        textView.setText(name);
                        textView.setTypeface(face);
                        textView.setTextSize(20);

                        lv.addView(imgView, params);
                        lv.addView(textView, params);
                    }
                }

            }
            catch(Exception e){
                Log.e("2", "Unexpected JSON exception", e);
            }
        }
    }
}
