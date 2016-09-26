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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.miles.referensi.FontCache;
import com.miles.referensi.GPSTracker;
import com.miles.referensi.ImageLoader;
import com.miles.referensi.Referensi;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeMapsActivity extends Activity implements OnClickListener, OnMarkerClickListener {
	private GoogleMap mMap;
    private MapFragment fragment;
	private TextView txtName, txtFilter, txtTitle, txtAddress, txtTelp;
	private Button btnBack, btnSearch, btnDayLife, btnNightLife, btnEvent;
	private Typeface fontUbuntuL, fontUbuntuB;
	private LinearLayout linParent;
    private RequestQueue queue;
	private SharedPreferences milesPref;
    private DisplayMetrics displaymetrics;
    private ProgressBar progressBar;
    private RelativeLayout bottomBarA, bottomBarB, bottomBarC, bottomBarD, bottomBarE, relDesc;
    private View viewBarA, viewBarB, viewBarC, viewBarD, viewBarE;
    private ImageView imgArrow;
    private ProgressDialog pDialog;
    private GPSTracker gps;
    private String city = "";
    private ImageView imgSearch, imgPlace;
    private ImageLoader imgLoader;
    
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activityslideup, R.anim.no_anim);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_maps);
        
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
		fragment     = (MapFragment) getFragmentManager().findFragmentById(R.id.maps);
		mMap      	 = fragment.getMap();
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
		imgSearch    = (ImageView) findViewById(R.id.imgSearch);
		relDesc      = (RelativeLayout) findViewById(R.id.relDesc);
		imgPlace     = (ImageView) findViewById(R.id.imgPlace);
		txtTitle	 = (TextView) findViewById(R.id.txtTitle);
		txtAddress	 = (TextView) findViewById(R.id.txtAddress);
		txtTelp		 = (TextView) findViewById(R.id.txtTelp);
		imgLoader 	 = new ImageLoader(HomeMapsActivity.this);
		
		txtName.setTypeface(fontUbuntuL);
		btnDayLife.setTypeface(fontUbuntuL);
		btnNightLife.setTypeface(fontUbuntuL);
		btnEvent.setTypeface(fontUbuntuL);
		txtFilter.setTypeface(fontUbuntuL);
		txtTitle.setTypeface(fontUbuntuB);
		txtAddress.setTypeface(fontUbuntuL);
		txtTelp.setTypeface(fontUbuntuL);
		
		viewBarA.setVisibility(View.GONE);
		viewBarB.setVisibility(View.GONE);
		viewBarC.setVisibility(View.GONE);
		viewBarD.setVisibility(View.GONE);
		viewBarE.setVisibility(View.VISIBLE);
		txtFilter.setVisibility(View.VISIBLE);
		imgArrow.setVisibility(View.GONE);
		imgSearch.setVisibility(View.GONE);
		
		displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		
		txtName.setText("MAPS");
		txtName.setTypeface(fontUbuntuL);
		
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
		mMap.setOnMarkerClickListener(this);
		
		pDialog = new ProgressDialog(HomeMapsActivity.this);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);
		
        getDayLife("day");
	}
	
	/**
     * Initialize the location manager.
     */
    @SuppressWarnings("unused")
	private void initLocationManager() {
    	// create class object
        gps = new GPSTracker(HomeMapsActivity.this);

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
            	viewBarE.setBackgroundColor(Color.parseColor("#48d1b6"));
            	btnDayLife.setActivated(true);
            	btnNightLife.setActivated(false);
            	btnEvent.setActivated(false);
            	getDayLife("day");
            	break;
            case R.id.btnNightLife:
            	linParent.setBackgroundColor(Color.parseColor("#1f4d6f"));
            	viewBarE.setBackgroundColor(Color.parseColor("#1f4d6f"));
            	btnNightLife.setActivated(true);
            	btnDayLife.setActivated(false);
            	btnEvent.setActivated(false);
            	getNightLife("night");
            	break;
            case R.id.btnEvent:
            	linParent.setBackgroundColor(Color.parseColor("#5b1f6f"));
            	viewBarE.setBackgroundColor(Color.parseColor("#5b1f6f"));
            	btnEvent.setActivated(true);
            	btnDayLife.setActivated(false);
            	btnNightLife.setActivated(false);
            	getEventLife("event");
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
            case R.id.bottomBarD:
            	startActivity(new Intent(getApplicationContext(), FavoriteActivity.class));
            	break;
            case R.id.btnBack:
            	startActivity(new Intent(getApplicationContext(), LeftMenuActivity.class).putExtra("Categori", "Home"));
            	break;
            case R.id.bottomBarE:
    			break;
            case R.id.btnSearch:
            	startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            	break;
        }
	}
	
	public void getDayLife(final String status) {
		progressBar.setVisibility(View.VISIBLE);
		
		String url = Referensi.url+"/get15newplace/"+city+"/1";
		JsonArrayRequest jsArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
            	JSONArray jsonArray, jsonName, jsonAddress, jsonTelp;
				try {
					jsonArray   = new JSONArray(response.optJSONObject(0).optString("photo"));
					jsonName    = new JSONArray(response.optJSONObject(0).optString("name"));
					jsonAddress = new JSONArray(response.optJSONObject(0).optString("address"));
					jsonTelp    = new JSONArray(response.optJSONObject(0).optString("telp"));
					
	            	for (int i=0; i<jsonArray.length(); i++) {
	            		int posFrom    = jsonArray.optString(i).indexOf("(");
	            		int posTo      = jsonArray.optString(i).indexOf(")");
	            		String newLok  = jsonArray.optString(i).substring(posFrom+1, posTo);
	            		String[] split = newLok.split(",");
	            		initializeMap(Double.parseDouble(split[0]), Double.parseDouble(split[1]), 
	            				jsonName.optString(i), jsonAddress.optString(i), jsonTelp.optString(i), 
	            				jsonArray.optString(i), jsonArray.length()/2, i);
	            	}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	progressBar.setVisibility(View.GONE);
            }
        });
        queue.add(jsArrayRequest);
	}
	
	public void getNightLife(final String status) {
		progressBar.setVisibility(View.VISIBLE);
		
		String url = Referensi.url+"/get15newplace/"+city+"/0";
		JsonArrayRequest jsArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
            	mMap.clear();
            	relDesc.setVisibility(View.GONE);
            	
            	JSONArray jsonArray, jsonName, jsonAddress, jsonTelp;
				try {
					jsonArray   = new JSONArray(response.optJSONObject(0).optString("photo"));
					jsonName    = new JSONArray(response.optJSONObject(0).optString("name"));
					jsonAddress = new JSONArray(response.optJSONObject(0).optString("address"));
					jsonTelp    = new JSONArray(response.optJSONObject(0).optString("telp"));
					
	            	for (int i=0; i<jsonArray.length(); i++) {
	            		int posFrom    = jsonArray.optString(i).indexOf("(");
	            		int posTo      = jsonArray.optString(i).indexOf(")");
	            		String newLok  = jsonArray.optString(i).substring(posFrom+1, posTo);
	            		String[] split = newLok.split(",");
	            		initializeMap(Double.parseDouble(split[0]), Double.parseDouble(split[1]), 
	            				jsonName.optString(i), jsonAddress.optString(i), jsonTelp.optString(i), 
	            				jsonArray.optString(i), jsonArray.length()/2, i);
	            	}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	progressBar.setVisibility(View.GONE);
            }
        });
        queue.add(jsArrayRequest);
	}
	
	public void getEventLife(final String status) {
		progressBar.setVisibility(View.VISIBLE);
		
		String url = Referensi.url+"/event/public";
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	mMap.clear();
            	relDesc.setVisibility(View.GONE);
            	
            	JSONArray jsonArray = new JSONArray();
				for (int i=0; i<response.optJSONArray("value").length(); i++) {
            		jsonArray.put(response.optJSONArray("value").optJSONObject(i).optString("location"));
            		int posFrom    = jsonArray.optString(i).indexOf("(");
            		int posTo      = jsonArray.optString(i).indexOf(")");
            		String newLok  = jsonArray.optString(i).substring(posFrom+1, posTo);
            		String[] split = newLok.split(",");
            		initializeMap(Double.parseDouble(split[0]), Double.parseDouble(split[1]), 
            				response.optJSONArray("value").optJSONObject(i).optString("name"), 
            				response.optJSONArray("value").optJSONObject(i).optString("address"),
            				response.optJSONArray("value").optJSONObject(i).optString("cp"),
            				response.optJSONArray("value").optJSONObject(i).optString("photo"), jsonArray.length()/2, i);
            	}
    			progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	progressBar.setVisibility(View.GONE);
            }
        });
        queue.add(jsObjRequest);
	}
	
	private void initializeMap(double lat1, double lng1, String placeName, String address, String telp, String photo, int central, int position) {
        if (mMap!=null) {
            LatLng latLng = new LatLng(lat1, lng1);
            mMap.addMarker(new MarkerOptions().position(latLng)
            		.title(placeName+"|"+address+"|"+telp+"|"+photo)
            		.icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant)));
            
            if (central==position) {
            	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        } else {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.maps)).getMap();
            Toast.makeText(getApplicationContext(), "MAP NULL", Toast.LENGTH_LONG).show();
        }
    }
	
	@Override
    public boolean onMarkerClick(final Marker marker) {
    	try {
    		final Handler handler = new Handler();
            final long startTime  = SystemClock.uptimeMillis();
            final long duration   = 2000;
            
            Projection proj = mMap.getProjection();
            final LatLng markerLatLng = marker.getPosition();
            Point startPoint = proj.toScreenLocation(markerLatLng);
            startPoint.offset(0, -100);
            final LatLng startLatLng = proj.fromScreenLocation(startPoint);
            final Interpolator interpolator = new BounceInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - startTime;
                    float t = interpolator.getInterpolation((float) elapsed / duration);
                    double lng = t * markerLatLng.longitude + (1 - t) * startLatLng.longitude;
                    double lat = t * markerLatLng.latitude + (1 - t) * startLatLng.latitude;
                    marker.setPosition(new LatLng(lat, lng));
                    if (t < 1.0) {
                        handler.postDelayed(this, 16);
                    }
                }
            });
    		
            relDesc.setVisibility(View.VISIBLE);
	    	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));

	    	final String[] splitMoreDetail = marker.getTitle().split("\\|");
	    	if (marker.getTitle()!=null) {
		    	// Title and Address
	    		txtTitle.setText(splitMoreDetail[0]);
		    	txtAddress.setText(splitMoreDetail[1]);
		    	
		    	// Phone Number
		    	if (splitMoreDetail[2].equalsIgnoreCase("") || splitMoreDetail[2].equalsIgnoreCase("-")) {
		    		txtTelp.setText("-");
		    	} else {
		    		txtTelp.setText(splitMoreDetail[2]);
		    	}
		    	
		    	// Image
		    	int loader = R.drawable.miles_loader;
		    	imgLoader.DisplayImage(Referensi.url2+splitMoreDetail[3].replace(" ", "%20"), loader, imgPlace);
	    	} else {
	            relDesc.setVisibility(View.GONE);
	    	}
	    	
    	} catch (Exception e) {
            relDesc.setVisibility(View.GONE);
		}
    	return true;
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
