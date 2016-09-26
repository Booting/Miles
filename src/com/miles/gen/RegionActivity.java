package com.miles.gen;

import com.miles.referensi.Referensi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

public class RegionActivity extends Activity implements OnClickListener {
	private ImageButton btnRegionJakarta, btnRegionBandung, btnRegionBali, btnRegionSurabaya, btnRegionYogyakarta;
	private SharedPreferences milesPref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activityslideup, R.anim.no_anim);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_region);
        
        milesPref 	 		= getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
        btnRegionJakarta	= (ImageButton) findViewById(R.id.btnRegionJakarta);
		btnRegionBandung    = (ImageButton) findViewById(R.id.btnRegionBandung);
		btnRegionBali 		= (ImageButton) findViewById(R.id.btnRegionBali);
		btnRegionSurabaya   = (ImageButton) findViewById(R.id.btnRegionSurabaya);
		btnRegionYogyakarta	= (ImageButton) findViewById(R.id.btnRegionYogyakarta);
		
		btnRegionJakarta.setOnClickListener(this);
		btnRegionBandung.setOnClickListener(this);
		btnRegionBali.setOnClickListener(this);
		btnRegionSurabaya.setOnClickListener(this);
		btnRegionYogyakarta.setOnClickListener(this);
	}
	
	@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegionJakarta:
            	SharedPreferences.Editor editor = milesPref.edit();
				editor.putString("city", "Jakarta").commit();
				startActivity(new Intent(getApplicationContext(), HomeActivity.class));
				overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left );
				finish();
            	break;
            case R.id.btnRegionBandung:
            	SharedPreferences.Editor editor1 = milesPref.edit();
				editor1.putString("city", "Bandung").commit();
				startActivity(new Intent(getApplicationContext(), HomeActivity.class));
				overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left );
				finish();
            	break;
            case R.id.btnRegionBali:
            	SharedPreferences.Editor editor2 = milesPref.edit();
				editor2.putString("city", "Bali").commit();
				startActivity(new Intent(getApplicationContext(), HomeActivity.class));
				overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left );
				finish();
            	break;
            case R.id.btnRegionSurabaya:
            	SharedPreferences.Editor editor3 = milesPref.edit();
				editor3.putString("city", "Surabaya").commit();
				startActivity(new Intent(getApplicationContext(), HomeActivity.class));
				overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left );
				finish();
            	break;
            case R.id.btnRegionYogyakarta:
            	SharedPreferences.Editor editor4 = milesPref.edit();
				editor4.putString("city", "Yogyakarta").commit();
				startActivity(new Intent(getApplicationContext(), HomeActivity.class));
				overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left );
				finish();
            	break;
        }
	}
	
	@Override
	public boolean onKeyUp( int keyCode, KeyEvent event ){
		if( keyCode == KeyEvent.KEYCODE_BACK ) {
			finish();
			return true;
		}
		return super.onKeyUp( keyCode, event );
	}
	
	@Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.no_anim, R.anim.activityslidedown);
    }
	
}
