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
import com.miles.gen.EventDetailAdapter.EventDetailAdapterListener;
import com.miles.gen.ListDetailAdapter.ListDetailAdapterListener;
import com.miles.referensi.FontCache;
import com.miles.referensi.GPSTracker;
import com.miles.referensi.Referensi;
import com.yalantis.phoenix.PullToRefreshEventView;
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

public class HomeActivity extends Activity implements OnClickListener, ListDetailAdapterListener, EventDetailAdapterListener {
	private TextView txtName, txtFilter;
	private Button btnBack, btnSearch, btnDayLife, btnNightLife, btnEvent;
	@SuppressWarnings("unused")
	private Typeface fontUbuntuL, fontUbuntuB;
	private ListAdapter listAdapter;
	private EventAdapter eventAdapter;
	private ListView lsvList, lsvListNight, lsvListEvent;
	private LinearLayout linParent;
    private RequestQueue queue;
	private SharedPreferences milesPref;
    private DisplayMetrics displaymetrics;
    private ProgressBar progressBar;
    private RelativeLayout bottomBarA, bottomBarB, bottomBarC, bottomBarD, bottomBarE;
    private View viewBarA, viewBarB, viewBarC, viewBarD, viewBarE;
    private ImageView imgArrow;
    private PullToRefreshView mPullToRefreshView;
    private PullToRefreshNightView mPullToRefreshNightView;
    private PullToRefreshEventView mPullToRefreshEventView;
    public static final int REFRESH_DELAY = 1000;
    private ProgressDialog pDialog;
    private GPSTracker gps;
    private String city = "";
    
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
		btnSearch	 = (Button) findViewById(R.id.btnSearch);
		btnDayLife   = (Button) findViewById(R.id.btnDayLife);
		btnNightLife = (Button) findViewById(R.id.btnNightLife);
		btnEvent     = (Button) findViewById(R.id.btnEvent);
		lsvList		 = (ListView) findViewById(R.id.lsvList);
		lsvListNight = (ListView) findViewById(R.id.lsvListNight);
		lsvListEvent = (ListView) findViewById(R.id.lsvListEvent);
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
		city 		 = milesPref.getString("city", "");
		
		txtName.setTypeface(fontUbuntuL);
		btnDayLife.setTypeface(fontUbuntuL);
		btnNightLife.setTypeface(fontUbuntuL);
		btnEvent.setTypeface(fontUbuntuL);
		txtFilter.setTypeface(fontUbuntuL);
		
		viewBarA.setVisibility(View.VISIBLE);
		viewBarB.setVisibility(View.GONE);
		viewBarC.setVisibility(View.GONE);
		viewBarD.setVisibility(View.GONE);
		viewBarE.setVisibility(View.GONE);
		txtFilter.setVisibility(View.VISIBLE);
		imgArrow.setVisibility(View.GONE);
		
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
		btnSearch.setOnClickListener(this);
		
		pDialog = new ProgressDialog(HomeActivity.this);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);
		
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
            	getDayLife("day");
            }
        });
        
        mPullToRefreshNightView = (PullToRefreshNightView) findViewById(R.id.pull_to_refresh_night);
        mPullToRefreshNightView.setOnRefreshListener(new PullToRefreshNightView.OnRefreshListener() {
            @Override
            public void onRefresh() {
            	getNightLife("night");
            }
        });
        
        mPullToRefreshEventView = (PullToRefreshEventView) findViewById(R.id.pull_to_refresh_event);
        mPullToRefreshEventView.setOnRefreshListener(new PullToRefreshEventView.OnRefreshListener() {
            @Override
            public void onRefresh() {
            	getEventLife("event");
            }
        });
        
        getDayLife("day");
        //initLocationManager();
	}
	
	/**
     * Initialize the location manager.
     */
    @SuppressWarnings("unused")
	private void initLocationManager() {
    	// create class object
        gps = new GPSTracker(HomeActivity.this);

        // check if GPS enabled     
        if (gps.canGetLocation()) {
        	getNameCity(gps.getLatitude(), gps.getLongitude());
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
    
    public void getNameCity(double lat, double lng) {
		String url = "http://maps.google.com/maps/api/geocode/json?latlng="+lat+","+lng+"&sensor=false";
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("NewApi")
			@Override
            public void onResponse(JSONObject response) {
            	try {
            		JSONArray jsonArray  = new JSONArray(response.getString("results"));
            		JSONArray jsonArray2 = new JSONArray(jsonArray.getJSONObject(0).getString("address_components"));
            		String newCity       = jsonArray2.getJSONObject(6).getString("short_name");
            		
            		SharedPreferences.Editor editor = milesPref.edit();
					editor.putString("city", newCity).commit();
            		
            		if (newCity.contains("Jakarta")||newCity.contains("jakarta")) {
            			city = "Jakarta";
            		} else if (newCity.contains("Bandung")||newCity.contains("bandung")) {
            			city = "Bandung";
            		} else if (newCity.contains("Bali")||newCity.contains("bali")) {
            			city = "Bali";
            		} else if (newCity.contains("Surabaya")||newCity.contains("surabaya")) {
            			city = "Surabaya";
            		} else if (newCity.contains("Yogyakarta")||newCity.contains("yogyakarta")) {
            			city = "Yogyakarta";
            		}
            		
            		getDayLife("day");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					getDayLife("day");
					e.printStackTrace();
				}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	getDayLife("day");
            	Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsObjRequest);
	}
	
	@SuppressLint("NewApi")
	@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDayLife:
            	linParent.setBackgroundColor(Color.parseColor("#48d1b6"));
            	viewBarA.setBackgroundColor(Color.parseColor("#48d1b6"));
            	btnDayLife.setActivated(true);
            	btnNightLife.setActivated(false);
            	btnEvent.setActivated(false);
            	if (mPullToRefreshNightView.getVisibility()==View.VISIBLE) {
            		mPullToRefreshNightView.setVisibility(View.INVISIBLE);
            	} else if (mPullToRefreshEventView.getVisibility()==View.VISIBLE) {
            		mPullToRefreshEventView.setVisibility(View.INVISIBLE);
            	}
            	getDayLife("day");
            	break;
            case R.id.btnNightLife:
            	linParent.setBackgroundColor(Color.parseColor("#1f4d6f"));
            	viewBarA.setBackgroundColor(Color.parseColor("#1f4d6f"));
            	btnNightLife.setActivated(true);
            	btnDayLife.setActivated(false);
            	btnEvent.setActivated(false);
            	if (mPullToRefreshView.getVisibility()==View.VISIBLE) {
            		mPullToRefreshView.setVisibility(View.INVISIBLE);
            	} else if (mPullToRefreshEventView.getVisibility()==View.VISIBLE) {
            		mPullToRefreshEventView.setVisibility(View.INVISIBLE);
            	}
            	getNightLife("night");
            	break;
            case R.id.btnEvent:
            	linParent.setBackgroundColor(Color.parseColor("#5b1f6f"));
            	viewBarA.setBackgroundColor(Color.parseColor("#5b1f6f"));
            	btnEvent.setActivated(true);
            	btnDayLife.setActivated(false);
            	btnNightLife.setActivated(false);
            	if (mPullToRefreshView.getVisibility()==View.VISIBLE) {
            		mPullToRefreshView.setVisibility(View.INVISIBLE);
            	} else if (mPullToRefreshNightView.getVisibility()==View.VISIBLE) {
            		mPullToRefreshNightView.setVisibility(View.INVISIBLE);
            	}
            	getEventLife("event");
            	break;
            case R.id.bottomBarA:
            	break;
            case R.id.bottomBarB:
            	startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            	break;
            case R.id.bottomBarC:
            	startActivity(new Intent(getApplicationContext(), FilteringActivity.class));
            	break;
            case R.id.bottomBarD:
            	startActivity(new Intent(getApplicationContext(), FavoriteActivity.class));
            	break;
            case R.id.btnBack:
            	startActivity(new Intent(getApplicationContext(), LeftMenuActivity.class).putExtra("Categori", "Home"));
            	break;
            case R.id.bottomBarE:
            	startActivity(new Intent(getApplicationContext(), HomeMapsActivity.class));
    			break;
            case R.id.btnSearch:
            	startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            	break;
        }
	}
	
	public void get15newplacetorecommendation(final String status) {
		if (!mPullToRefreshView.isShown()) {
			progressBar.setVisibility(View.VISIBLE);
			lsvList.setVisibility(View.GONE);
			mPullToRefreshView.setVisibility(View.INVISIBLE);
		}
		
		String url = Referensi.url+"/get15newplacetorecommendation";
		JsonArrayRequest jsArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
            	listAdapter = new ListAdapter(HomeActivity.this, HomeActivity.this, response, displaymetrics.widthPixels, status, "Home");
    			lsvList.setAdapter(listAdapter);
    			
    			if (!mPullToRefreshView.isShown()) {
	            	progressBar.setVisibility(View.GONE);
	        		lsvList.setVisibility(View.VISIBLE);
	        		mPullToRefreshView.setVisibility(View.VISIBLE);
            	} else {
	    			mPullToRefreshView.postDelayed(new Runnable() {
		                @Override
		                public void run() {
		                    mPullToRefreshView.setRefreshing(false);
		                }
		            }, REFRESH_DELAY);
            	}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsArrayRequest);
	}
	
	public void getDayLife(final String status) {
		if (!mPullToRefreshView.isShown()) {
			progressBar.setVisibility(View.VISIBLE);
			mPullToRefreshView.setVisibility(View.INVISIBLE);
		}
		
		String url = Referensi.url+"/get15newplace/"+city+"/1";
		JsonArrayRequest jsArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
            	listAdapter = new ListAdapter(HomeActivity.this, HomeActivity.this, response, displaymetrics.widthPixels, status, "Home");
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
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsArrayRequest);
	}
	
	public void getNightLife(final String status) {
		if (!mPullToRefreshNightView.isShown()) {
			progressBar.setVisibility(View.VISIBLE);
			lsvListNight.setVisibility(View.GONE);
			mPullToRefreshNightView.setVisibility(View.INVISIBLE);
		}
		
		String url = Referensi.url+"/get15newplace/"+city+"/0";
		JsonArrayRequest jsArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
            	listAdapter = new ListAdapter(HomeActivity.this, HomeActivity.this, response, displaymetrics.widthPixels, status, "Home");
    			lsvListNight.setAdapter(listAdapter);
    			
    			if (!mPullToRefreshNightView.isShown()) {
    				progressBar.setVisibility(View.GONE);
            		lsvListNight.setVisibility(View.VISIBLE);
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsArrayRequest);
	}
	
	public void getEventLife(final String status) {
		if (!mPullToRefreshEventView.isShown()) {
			progressBar.setVisibility(View.VISIBLE);
			lsvListEvent.setVisibility(View.GONE);
			mPullToRefreshEventView.setVisibility(View.INVISIBLE);
		}
		
		String url = Referensi.url+"/event/public";
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	System.out.println("getEventLife (response): ");
            	System.out.println("getEventLife (response): ");
            	System.out.println("getEventLife (response): "+response);
            	eventAdapter = new EventAdapter(HomeActivity.this, HomeActivity.this, response.optJSONArray("value"), displaymetrics.widthPixels, "Home");
    			lsvListEvent.setAdapter(eventAdapter);
    			
    			if (!mPullToRefreshEventView.isShown()) {
    				progressBar.setVisibility(View.GONE);
            		lsvListEvent.setVisibility(View.VISIBLE);
            		mPullToRefreshEventView.setVisibility(View.VISIBLE);
    			} else {
    				mPullToRefreshEventView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        	mPullToRefreshEventView.setRefreshing(false);
                        }
                    }, REFRESH_DELAY);
    			}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	if (!mPullToRefreshEventView.isShown()) {
            		progressBar.setVisibility(View.GONE);
            	} else {
            		mPullToRefreshEventView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        	mPullToRefreshEventView.setRefreshing(false);
                        }
                    }, REFRESH_DELAY);
            	}
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsObjRequest);
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
	
	public void onAttendClicked(String placeId) { 
		pDialog.show();
		String url = Referensi.url+"/goingPublicEvent";
		JSONObject jsObject = new JSONObject();
		try {
			jsObject.put("event_id", placeId);
			jsObject.put("profile_id", milesPref.getString("id", ""));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("onAttendClicked (params): "+jsObject + ", URL: "+url);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.PUT, url, jsObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	System.out.println("onAttendClicked (response): "+response);
            	Toast.makeText(getBaseContext(), "Attend "+ response.optString("status"), Toast.LENGTH_SHORT).show();
            	pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	System.out.println("onAttendClicked (error): "+error.toString());
            	pDialog.dismiss();
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsObjRequest);
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
