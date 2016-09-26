package com.miles.gen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.miles.gen.ListDetailAdapter.ListDetailAdapterListener;
import com.miles.referensi.FontCache;
import com.miles.referensi.Referensi;
import com.yalantis.phoenix.PullToRefreshNightView;
import com.yalantis.phoenix.PullToRefreshView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Toast;

public class FavoriteActivity extends Activity implements OnClickListener, ListDetailAdapterListener {
	private TextView txtName, txtFilter;
	private Button btnBack, btnDayLife, btnNightLife, btnEvent;
	@SuppressWarnings("unused")
	private Typeface fontUbuntuL, fontUbuntuB;
	private ListAdapter listAdapter;
	private ListView lsvList, lsvListNight;
	private LinearLayout linParent;
    private RequestQueue queue;
	private SharedPreferences milesPref;
    private DisplayMetrics displaymetrics;
    private ProgressBar progressBar;
    private RelativeLayout bottomBarA, bottomBarB, bottomBarC, bottomBarD, bottomBarE;
    private View viewBarA, viewBarB, viewBarC, viewBarD, viewBarE;
    private ImageView imgArrow, imgSearch;
    private PullToRefreshView mPullToRefreshView;
    private PullToRefreshNightView mPullToRefreshNightView;
    public static final int REFRESH_DELAY = 1000;
    private ProgressDialog pDialog;
    private String city = "", categori = "1", url = "get15topplace";
    
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        
        progressBar  = (ProgressBar) findViewById(R.id.progressBusy);
        milesPref 	 = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
		queue 		 = Volley.newRequestQueue(this);
        fontUbuntuL  = FontCache.get(this, "Ubuntu-L");
		fontUbuntuB  = FontCache.get(this, "Ubuntu-B");
		txtName		 = (TextView) findViewById(R.id.txtName);
		txtFilter    = (TextView) findViewById(R.id.txtFilter);
		btnBack      = (Button) findViewById(R.id.btnBack);
		btnDayLife   = (Button) findViewById(R.id.btnDayLife);
		btnNightLife = (Button) findViewById(R.id.btnNightLife);
		btnEvent     = (Button) findViewById(R.id.btnEvent);
		lsvList		 = (ListView) findViewById(R.id.lsvList);
		lsvListNight = (ListView) findViewById(R.id.lsvListNight);
		linParent	 = (LinearLayout) findViewById(R.id.linParent);
		bottomBarA   = (RelativeLayout) findViewById(R.id.bottomBarA);
		bottomBarB   = (RelativeLayout) findViewById(R.id.bottomBarB);
		bottomBarC   = (RelativeLayout) findViewById(R.id.bottomBarC);
		bottomBarD   = (RelativeLayout) findViewById(R.id.bottomBarD);
		bottomBarE   = (RelativeLayout) findViewById(R.id.bottomBarE);
		viewBarA     = (View) findViewById(R.id.viewBarA);
		viewBarB     = (View) findViewById(R.id.viewBarB);
		viewBarC     = (View) findViewById(R.id.viewBarC);
		viewBarD     = (View) findViewById(R.id.viewBarD);
		viewBarE     = (View) findViewById(R.id.viewBarE);
		imgArrow	 = (ImageView) findViewById(R.id.imgArrow);
		imgSearch    = (ImageView) findViewById(R.id.imgSearch);
		city 		 = milesPref.getString("city", "");
		
		txtName.setText("FAVORITE");
		btnDayLife.setText("Top");
		btnNightLife.setText("Trending");
		btnEvent.setText("Newest");
		
		txtName.setTypeface(fontUbuntuL);
		btnDayLife.setTypeface(fontUbuntuL);
		btnNightLife.setTypeface(fontUbuntuL);
		btnEvent.setTypeface(fontUbuntuL);
		txtFilter.setTypeface(fontUbuntuL);
		
		viewBarA.setVisibility(View.GONE);
		viewBarB.setVisibility(View.GONE);
		viewBarC.setVisibility(View.GONE);
		viewBarD.setVisibility(View.VISIBLE);
		viewBarE.setVisibility(View.GONE);
		txtFilter.setVisibility(View.VISIBLE);
		imgArrow.setVisibility(View.GONE);
		imgSearch.setVisibility(View.GONE);
		
		displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		
		btnDayLife.setActivated(true);
		btnDayLife.setOnClickListener(this);
		btnNightLife.setOnClickListener(this);
		btnEvent.setOnClickListener(this);
		bottomBarA.setOnClickListener(this);
		bottomBarB.setOnClickListener(this);
		bottomBarC.setOnClickListener(this);
		bottomBarD.setOnClickListener(this);
		bottomBarE.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		
		pDialog = new ProgressDialog(FavoriteActivity.this);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);
        
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
            	getPlaceFavorit(url);
            }
        });
        
        mPullToRefreshNightView = (PullToRefreshNightView) findViewById(R.id.pull_to_refresh_night);
        mPullToRefreshNightView.setOnRefreshListener(new PullToRefreshNightView.OnRefreshListener() {
            @Override
            public void onRefresh() {
            	getPlaceFavorit(url);
            }
        });
        
        getPlaceFavorit(url);
	}
	
	@SuppressLint("NewApi")
	@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDayLife:
            	if (categori.equalsIgnoreCase("1")) {
	            	linParent.setBackgroundColor(Color.parseColor("#48d1b6"));
	            	viewBarA.setBackgroundColor(Color.parseColor("#48d1b6"));
            	} else {
            		linParent.setBackgroundColor(Color.parseColor("#1f4d6f"));
                	viewBarA.setBackgroundColor(Color.parseColor("#1f4d6f"));
            	}
            	btnDayLife.setActivated(true);
            	btnNightLife.setActivated(false);
            	btnEvent.setActivated(false);
            	if (categori.equalsIgnoreCase("1")) {
            		if (mPullToRefreshNightView.getVisibility()==View.VISIBLE) {
                		mPullToRefreshNightView.setVisibility(View.INVISIBLE);
                	}
            	} else {
            		if (mPullToRefreshView.getVisibility()==View.VISIBLE) {
                		mPullToRefreshView.setVisibility(View.INVISIBLE);
                	}
            	}
            	url = "get15topplace";
            	getPlaceFavorit(url);
            	break;
            case R.id.btnNightLife:
            	if (categori.equalsIgnoreCase("1")) {
	            	linParent.setBackgroundColor(Color.parseColor("#48d1b6"));
	            	viewBarA.setBackgroundColor(Color.parseColor("#48d1b6"));
            	} else {
            		linParent.setBackgroundColor(Color.parseColor("#1f4d6f"));
                	viewBarA.setBackgroundColor(Color.parseColor("#1f4d6f"));
            	}
            	btnNightLife.setActivated(true);
            	btnDayLife.setActivated(false);
            	btnEvent.setActivated(false);
            	if (categori.equalsIgnoreCase("1")) {
            		if (mPullToRefreshNightView.getVisibility()==View.VISIBLE) {
                		mPullToRefreshNightView.setVisibility(View.INVISIBLE);
                	}
            	} else {
            		if (mPullToRefreshView.getVisibility()==View.VISIBLE) {
                		mPullToRefreshView.setVisibility(View.INVISIBLE);
                	}
            	}
            	url = "get15trendingplace";
            	getPlaceFavorit(url);
            	break;
            case R.id.btnEvent:
            	if (categori.equalsIgnoreCase("1")) {
	            	linParent.setBackgroundColor(Color.parseColor("#48d1b6"));
	            	viewBarA.setBackgroundColor(Color.parseColor("#48d1b6"));
            	} else {
            		linParent.setBackgroundColor(Color.parseColor("#1f4d6f"));
                	viewBarA.setBackgroundColor(Color.parseColor("#1f4d6f"));
            	}
            	btnEvent.setActivated(true);
            	btnDayLife.setActivated(false);
            	btnNightLife.setActivated(false);
            	if (mPullToRefreshNightView.getVisibility()==View.VISIBLE) {
            		mPullToRefreshNightView.setVisibility(View.INVISIBLE);
            	} else if (mPullToRefreshView.getVisibility()==View.VISIBLE) {
            		mPullToRefreshView.setVisibility(View.INVISIBLE);
            	}
            	url = "get15newplace";
            	getPlaceFavorit(url);
            	break;
            case R.id.bottomBarA:
            	Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		startActivity(intent);
        		finish();
            	break;
            case R.id.bottomBarB:
            	startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            	break;
            case R.id.bottomBarC:
            	startActivity(new Intent(getApplicationContext(), FilteringActivity.class));
            	break;
            case R.id.btnBack:
            	startActivityForResult(new Intent(getApplicationContext(), LeftMenuActivity.class).putExtra("Categori", "Star"), 1);
            	break;
            case R.id.bottomBarE:
            	startActivity(new Intent(getApplicationContext(), HomeMapsActivity.class));
    			break;
            case R.id.btnSearch:
            	startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            	break;
        }
	}
	
	public void getPlaceFavorit(String api) {
		if (categori.equalsIgnoreCase("1")) {
			if (!mPullToRefreshView.isShown()) {
				progressBar.setVisibility(View.VISIBLE);
				mPullToRefreshView.setVisibility(View.INVISIBLE);
			}
		} else {
			if (!mPullToRefreshNightView.isShown()) {
				progressBar.setVisibility(View.VISIBLE);
				mPullToRefreshNightView.setVisibility(View.INVISIBLE);
			}
		}
		
		String url = Referensi.url+"/"+api+"/"+city+"/"+categori;
		System.out.println("getPlaceFavorit (url): "+url);
		JsonArrayRequest jsArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
        		System.out.println("getPlaceFavorit (response): "+response);
            	listAdapter = new ListAdapter(FavoriteActivity.this, FavoriteActivity.this, response, displaymetrics.widthPixels, "day", "Favorite");
    			
            	if (categori.equalsIgnoreCase("1")) {
	            	lsvList.setAdapter(listAdapter);
	    			if (!mPullToRefreshView.isShown()) {
	        			progressBar.setVisibility(View.GONE);
		        		mPullToRefreshView.setVisibility(View.VISIBLE);
	            	} else {
		    			mPullToRefreshView.postDelayed(new Runnable() {
			                @Override
			                public void run() {
			                    mPullToRefreshView.setRefreshing(false);
			                }
			            }, REFRESH_DELAY);
	            	}
            	} else {
            		lsvListNight.setAdapter(listAdapter);
	    			if (!mPullToRefreshNightView.isShown()) {
	        			progressBar.setVisibility(View.GONE);
	        			mPullToRefreshNightView.setVisibility(View.VISIBLE);
	            	} else {
	            		mPullToRefreshNightView.postDelayed(new Runnable() {
			                @Override
			                public void run() {
			                	mPullToRefreshNightView.setRefreshing(false);
			                }
			            }, REFRESH_DELAY);
	            	}
            	}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	if (categori.equalsIgnoreCase("1")) {
	            	if (!mPullToRefreshView.isShown()) {
	            		progressBar.setVisibility(View.GONE);
	            	} else {
	            		mPullToRefreshView.postDelayed(new Runnable() {
			                @Override
			                public void run() {
			                    mPullToRefreshView.setRefreshing(false);
			                }
			            }, REFRESH_DELAY);
	            	}
            	} else {
            		if (!mPullToRefreshNightView.isShown()) {
	            		progressBar.setVisibility(View.GONE);
	            	} else {
	            		mPullToRefreshNightView.postDelayed(new Runnable() {
			                @Override
			                public void run() {
			                	mPullToRefreshNightView.setRefreshing(false);
			                }
			            }, REFRESH_DELAY);
	            	}
            	}
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsArrayRequest);
	}
	
	public void onCheckInClicked(String placeId) {
		pDialog.show();
		String url = Referensi.url+"/checkIn";
		JSONObject jsObject = new JSONObject();
		try {
			jsObject.put("profile_id", milesPref.getString("id", ""));
			jsObject.put("place_id", placeId);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("onCheckInClicked [params]: "+jsObject + ", URL: "+url);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	System.out.println("onCheckInClicked: "+response);
            	Toast.makeText(getBaseContext(), "CheckIn "+ response.optString("status"), Toast.LENGTH_SHORT).show();
            	pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	pDialog.dismiss();
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsObjRequest);
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
    			categori = data.getStringExtra("Categori");
            	if (categori.equalsIgnoreCase("1")) {
	    			linParent.setBackgroundColor(Color.parseColor("#48d1b6"));
	                viewBarD.setBackgroundColor(Color.parseColor("#48d1b6"));
	                if (mPullToRefreshNightView.getVisibility()==View.VISIBLE) {
	            		mPullToRefreshNightView.setVisibility(View.INVISIBLE);
	            	}
            	} else {
            		linParent.setBackgroundColor(Color.parseColor("#1f4d6f"));
                	viewBarD.setBackgroundColor(Color.parseColor("#1f4d6f"));
                	if (mPullToRefreshView.getVisibility()==View.VISIBLE) {
                		mPullToRefreshView.setVisibility(View.INVISIBLE);
                	}
            	}
                getPlaceFavorit(url);
            }
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
	
}
