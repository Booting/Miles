package com.miles.gen;

import com.miles.referensi.FontCache;
import com.miles.referensi.Referensi;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LeftMenuActivity extends Activity {
	private TextView txtCity, txtEatery, txtWine, txtCoffee, txtWifi, txtBeer, 
		    txtLoungeAndBar, txtClub, txtDessert, txtBreakfast, txtPatisserie, txtHightea, txtCancel,
			txtFilterByFeature, txtShowMoreLess, txtFilterByCategori, txtTop, txtTrending, txtNewest;
	private Typeface fontUbuntuL, fontUbuntuB;
	private LinearLayout linCancel, linFilterByFeatureList, linShowMoreLess, linMore, linFilterByCategoriList,
			linCategoriThree;
	private RelativeLayout relFilterByFeature, relFilterByCategori;
	private SharedPreferences milesPref;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_left_menu);
        
        milesPref 		        = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
        fontUbuntuL             = FontCache.get(this, "Ubuntu-L");
		fontUbuntuB             = FontCache.get(this, "Ubuntu-B");
		txtCity		            = (TextView) findViewById(R.id.txtCity);
		txtEatery	            = (TextView) findViewById(R.id.txtEatery);
		txtWine		            = (TextView) findViewById(R.id.txtWine); 
		txtCoffee	            = (TextView) findViewById(R.id.txtCoffee); 
		txtWifi	                = (TextView) findViewById(R.id.txtWifi); 
		txtBeer		            = (TextView) findViewById(R.id.txtBeer);
		txtCancel               = (TextView) findViewById(R.id.txtCancel);
		linCancel		        = (LinearLayout) findViewById(R.id.linCancel);
		relFilterByFeature      = (RelativeLayout) findViewById(R.id.relFilterByFeature);
		txtFilterByFeature      = (TextView) findViewById(R.id.txtFilterByFeature);
		linFilterByFeatureList  = (LinearLayout) findViewById(R.id.linFilterByFeatureList);
		linShowMoreLess		    = (LinearLayout) findViewById(R.id.linShowMoreLess);
		txtShowMoreLess         = (TextView) findViewById(R.id.txtShowMoreLess);
		txtLoungeAndBar		    = (TextView) findViewById(R.id.txtLoungeAndBar);
		linMore				    = (LinearLayout) findViewById(R.id.linMore);
		txtClub			        = (TextView) findViewById(R.id.txtClub);
		txtDessert			    = (TextView) findViewById(R.id.txtDessert);
		txtBreakfast		    = (TextView) findViewById(R.id.txtBreakfast);
		txtPatisserie		    = (TextView) findViewById(R.id.txtPatisserie);
		txtHightea			    = (TextView) findViewById(R.id.txtHightea);
		relFilterByCategori	    = (RelativeLayout) findViewById(R.id.relFilterByCategori);
		linFilterByCategoriList = (LinearLayout) findViewById(R.id.linFilterByCategoriList);
		txtFilterByCategori		= (TextView) findViewById(R.id.txtFilterByCategori);
		txtTop				    = (TextView) findViewById(R.id.txtTop);
		txtTrending			    = (TextView) findViewById(R.id.txtTrending);
		txtNewest				= (TextView) findViewById(R.id.txtNewest);
		linCategoriThree        = (LinearLayout) findViewById(R.id.linCategoriThree);
		
		if (getIntent().getExtras().getString("Categori").equalsIgnoreCase("Filter") ||
			getIntent().getExtras().getString("Categori").equalsIgnoreCase("Star")) {
			relFilterByFeature.setVisibility(View.GONE);
			linFilterByFeatureList.setVisibility(View.GONE);
			relFilterByCategori.setVisibility(View.VISIBLE);
			linFilterByCategoriList.setVisibility(View.VISIBLE);
			if (getIntent().getExtras().getString("Categori").equalsIgnoreCase("Star")) {
				txtTop.setText("Day Life");
				txtTrending.setText("Night Life");
				txtNewest.setText("Event");
				linCategoriThree.setVisibility(View.GONE);
			}
		}
		
		txtCity.setTypeface(fontUbuntuB);
		txtFilterByFeature.setTypeface(fontUbuntuL);
		txtEatery.setTypeface(fontUbuntuL);
		txtWine.setTypeface(fontUbuntuL);
		txtCoffee.setTypeface(fontUbuntuL);
		txtWifi.setTypeface(fontUbuntuL);
		txtBeer.setTypeface(fontUbuntuL);
		txtShowMoreLess.setTypeface(fontUbuntuL);
		txtLoungeAndBar.setTypeface(fontUbuntuL);
		txtClub.setTypeface(fontUbuntuL);
		txtDessert.setTypeface(fontUbuntuL);
		txtBreakfast.setTypeface(fontUbuntuL);
		txtPatisserie.setTypeface(fontUbuntuL);
		txtHightea.setTypeface(fontUbuntuL);
		txtCancel.setTypeface(fontUbuntuB);
		txtFilterByCategori.setTypeface(fontUbuntuL);
		txtTop.setTypeface(fontUbuntuL);
		txtTrending.setTypeface(fontUbuntuL);
		txtNewest.setTypeface(fontUbuntuL);
		txtCity.setText(milesPref.getString("city", ""));
		
	    linShowMoreLess.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (linMore.getVisibility()==View.VISIBLE) {
					txtShowMoreLess.setText("Show more . . .");
					linMore.setVisibility(View.GONE);
				} else {
					txtShowMoreLess.setText("Show less . . .");
					linMore.setVisibility(View.VISIBLE);
				}
			}
		});
		
		linCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		txtCity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				startActivity(new Intent(getApplicationContext(), RegionActivity.class));
			}
		});
	}
	
	public void onCategoriOneClick(View v) {
		if (getIntent().getExtras().getString("Categori").equalsIgnoreCase("Star")) {
			Intent intent = new Intent().putExtra("Categori", "1");
			setResult(Activity.RESULT_OK, intent);
			finish();
		} else {
			Intent intent = new Intent().putExtra("Type", "top");
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	}
	
	public void onCategoriTwoClick(View v) {
		if (getIntent().getExtras().getString("Categori").equalsIgnoreCase("Star")) {
			Intent intent = new Intent().putExtra("Categori", "0");
			setResult(Activity.RESULT_OK, intent);
			finish();
		} else {
			Intent intent = new Intent().putExtra("Type", "trending");
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	}
	
	public void onCategoriThreeClick(View v) {
		if (getIntent().getExtras().getString("Categori").equalsIgnoreCase("Star")) {
			
		} else {
			Intent intent = new Intent().putExtra("Type", "new");
			setResult(Activity.RESULT_OK, intent);
			finish();
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
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
	
}
