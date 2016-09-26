package com.miles.gen;

import org.json.JSONArray;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.miles.gen.FilterAdapter.FilterAdapterListener;
import com.miles.gen.ListDetailAdapter.ListDetailAdapterListener;
import com.miles.referensi.FontCache;
import com.miles.referensi.Referensi;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FilteringActivity extends Activity implements OnClickListener, ListDetailAdapterListener, FilterAdapterListener {
	private TextView txtName, txtFilter;
	private Button btnBack;
	private Typeface fontUbuntuL;
	private ListView lsvList;
	private LinearLayout linParent, layoutTabbar;
    private RequestQueue queue;
    private SharedPreferences milesPref;
    private DisplayMetrics displaymetrics;
    private ProgressBar progressBar;
    private RelativeLayout bottomBarA, bottomBarB, bottomBarC, bottomBarD, bottomBarE;
    private View viewBarA, viewBarB, viewBarC, viewBarD, viewBarE;
    private ImageView imgSearch;
	private String city = "";
    private FilterAdapter filterAdapter;
    private ImageView imgArrow;
    private String type = "top", newCategori = "", newType = "top", lastCat = "0", lastFrom = "0", lastTo = "0";
    
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activityslideup, R.anim.no_anim);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_filter);
        
        progressBar    = (ProgressBar) findViewById(R.id.progressBusy);
        milesPref 	   = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
		queue 		   = Volley.newRequestQueue(this);
        fontUbuntuL    = FontCache.get(this, "Ubuntu-L");
		txtName		   = (TextView) findViewById(R.id.txtName);
		btnBack        = (Button) findViewById(R.id.btnBack);
		lsvList		   = (ListView) findViewById(R.id.lsvList);
		imgSearch      = (ImageView) findViewById(R.id.imgSearch);
		linParent	   = (LinearLayout) findViewById(R.id.linParent);
		layoutTabbar   = (LinearLayout) findViewById(R.id.layoutTabbar);
		bottomBarA     = (RelativeLayout) findViewById(R.id.bottomBarA);
		bottomBarB     = (RelativeLayout) findViewById(R.id.bottomBarB);
		bottomBarC     = (RelativeLayout) findViewById(R.id.bottomBarC);
		bottomBarD     = (RelativeLayout) findViewById(R.id.bottomBarD);
		bottomBarE     = (RelativeLayout) findViewById(R.id.bottomBarE);
		viewBarA       = (View) findViewById(R.id.viewBarA);
		viewBarB       = (View) findViewById(R.id.viewBarB);
		viewBarC       = (View) findViewById(R.id.viewBarC);
		viewBarD       = (View) findViewById(R.id.viewBarD);
		viewBarE       = (View) findViewById(R.id.viewBarE);
		txtFilter      = (TextView) findViewById(R.id.txtFilter);
		imgArrow	   = (ImageView) findViewById(R.id.imgArrow);
		city 		   = milesPref.getString("city", "");
		
		viewBarA.setVisibility(View.GONE);
		viewBarB.setVisibility(View.GONE);
		viewBarC.setVisibility(View.VISIBLE);
		viewBarD.setVisibility(View.GONE);
		viewBarE.setVisibility(View.GONE);
		imgSearch.setVisibility(View.GONE);
		layoutTabbar.setVisibility(View.GONE);
		linParent.setBackgroundColor(Color.parseColor("#30ad9c"));
		txtFilter.setVisibility(View.VISIBLE);
		imgArrow.setVisibility(View.GONE);
		
		txtName.setText("FILTERING");
		txtName.setTypeface(fontUbuntuL);
		
		bottomBarA.setOnClickListener(this);
		bottomBarB.setOnClickListener(this);
		bottomBarC.setOnClickListener(this);
		bottomBarD.setOnClickListener(this);
		bottomBarE.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		
	    displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	    
        filter(type, "0", "0", "0");
	}
	
	public void onCheckInClicked(String placeId) {}
	
	public void onFilterClicked(String type, String categori, String priceFrom, String priceTo) {
		lastCat  = categori;
		lastFrom = priceFrom;
		lastTo   = priceTo;
		
		filter(type, categori, priceFrom, priceTo);
	}
	
	public void filter(final String type, final String categori, final String priceFrom, final String priceTo) {
		progressBar.setVisibility(View.VISIBLE);
		String url = Referensi.url+"/filter/price/"+city+"/"+type+"/"+categori+"/"+priceFrom.replace(".", "")+"/"+priceTo.replace(".", "");
		
    	if (categori.equalsIgnoreCase("0")) {
    		newCategori = "day";
    	} else if (categori.equalsIgnoreCase("1")) {
    		newCategori = "night";
    	} else if (categori.equalsIgnoreCase("2")) {
    		newCategori = "event";
    	}
		
		System.out.println("filter (url): "+url);
    	JsonArrayRequest jsArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
            	System.out.println("filter (response): "+response);
            	
            	filterAdapter = new FilterAdapter(FilteringActivity.this, FilteringActivity.this, FilteringActivity.this, 
            			response, displaymetrics.widthPixels, newCategori, type, priceFrom, priceTo, "Filtering");
        	    lsvList.setAdapter(filterAdapter);
        	    progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	System.out.println("filter (error): "+error.toString());
            	filterAdapter = new FilterAdapter(FilteringActivity.this, FilteringActivity.this, FilteringActivity.this, 
            			null, displaymetrics.widthPixels, newCategori, type, priceFrom, priceTo, "Filtering");
        	    lsvList.setAdapter(filterAdapter);
        	    progressBar.setVisibility(View.GONE);
            }
        });
        queue.add(jsArrayRequest);
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
    			newType = data.getStringExtra("Type");
            	filter(newType, lastCat, lastFrom, lastTo);
            }
        }
	}
	
	@SuppressLint("NewApi")
	@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottomBarA:
            	Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		startActivity(intent);
        		finish();
            	break;
            case R.id.bottomBarB:
            	startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            	break;
            case R.id.bottomBarD:
            	startActivity(new Intent(getApplicationContext(), FavoriteActivity.class));
            	break;
            case R.id.btnBack:
            	startActivityForResult(new Intent(getApplicationContext(), LeftMenuActivity.class).putExtra("Categori", "Filter"), 1);
            	break;
            case R.id.bottomBarE:
            	startActivity(new Intent(getApplicationContext(), HomeMapsActivity.class));
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
