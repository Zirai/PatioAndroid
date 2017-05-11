package zirai.patio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.net.URL;

public class homePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        String jsonString = getIntent().getStringExtra("JSON");
        try{
            JSONArray array = new JSONArray(jsonString);
            for(int a = 0; a < array.length(); a ++){
                LinearLayout lv = (LinearLayout) findViewById(R.id.appList);

                ImageView imgView = new ImageView(this);
//                imgView.setAdjustViewBounds(true);
//                imgView.setId(a);
//                imgView.getLayoutParams().height = 20;
//                imgView.getLayoutParams().width = 20;
//                URL url = new URL("https://puu.sh/vLmqa/63f1137485.png");
//                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                imgView.setImageBitmap(bmp);
//                imgView.setImageResource(R.drawable.patio);
                Picasso.with(this).load("https://puu.sh/vLmqa/63f1137485.png").resize(300, 300).into(imgView);
                lv.addView(imgView);
            }
        }
        catch(Exception e){
            Log.e("2", "Unexpected JSON exception", e);
        }


    }

}
