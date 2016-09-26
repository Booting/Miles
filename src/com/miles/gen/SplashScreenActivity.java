package com.miles.gen;

import com.miles.referensi.Referensi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
 
public class SplashScreenActivity extends Activity {
    private static int SPLASH_TIME_OUT = 3000;
    private SharedPreferences milesPref;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splashscreen);

		milesPref = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
		
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	if (!milesPref.getString("id", "").equals("")) {
        			startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        			overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left );
        			finish();
        		} else {
	                startActivity( new Intent( getApplicationContext(), LoginActivity.class ));
	        		overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left );
	                finish();
        		}
            }
        }, SPLASH_TIME_OUT);
    }
 
}
