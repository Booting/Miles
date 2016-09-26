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
import com.miles.gridview.StaggeredGridView;
import com.miles.referensi.FontCache;
import com.miles.referensi.Referensi;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SearchActivity extends Activity implements OnClickListener {
	private EditText txtSearch;
	private Button btnPeople, btnVenue;
	private Typeface fontUbuntuL;
	private ListView lsvList; 
	private StaggeredGridView lsvListVenue;
	private RequestQueue queue;
    private SharedPreferences milesPref;
    private ProgressBar progressBar;
    private DisplayMetrics displaymetrics;
    private String city = "";
    private ImageView imgSearch;
    private SearchPeopleAdapter seAdapter;
    private SearchVenueAdapter seAdapter2;
    
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
        
        progressBar    = (ProgressBar) findViewById(R.id.progressBusy);
        milesPref 	   = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
		queue 		   = Volley.newRequestQueue(this);
        fontUbuntuL    = FontCache.get(this, "Ubuntu-L");
		txtSearch	   = (EditText) findViewById(R.id.txtSearch);
		btnPeople      = (Button) findViewById(R.id.btnPeople);
		btnVenue       = (Button) findViewById(R.id.btnVenue);
		lsvList		   = (ListView) findViewById(R.id.lsvList);
		imgSearch      = (ImageView) findViewById(R.id.imgSearch);
		lsvListVenue   = (StaggeredGridView) findViewById(R.id.lsvListVenue);
		
		displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		
		txtSearch.setTypeface(fontUbuntuL);
		btnPeople.setTypeface(fontUbuntuL);
		btnVenue.setTypeface(fontUbuntuL);
		btnPeople.setActivated(true);
		btnPeople.setOnClickListener(this);
		btnVenue.setOnClickListener(this);
		imgSearch.setOnClickListener(this);
		
		String newCity = milesPref.getString("city", ""); 
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
		} else {
			city = "Jakarta";
		}
	}
	
	@SuppressLint("NewApi")
	@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPeople:
            	btnPeople.setActivated(true);
            	btnVenue.setActivated(false);
            	lsvListVenue.setVisibility(View.GONE);
            	lsvList.setVisibility(View.VISIBLE);
            	break;
            case R.id.btnVenue:
            	btnPeople.setActivated(false);
            	btnVenue.setActivated(true);
            	lsvListVenue.setVisibility(View.VISIBLE);
            	lsvList.setVisibility(View.GONE);
            	break;
            case R.id.imgSearch:
            	if (btnPeople.isActivated()) {
					searchPeople(txtSearch.getText().toString().trim());
				} else {
					searchVenue(txtSearch.getText().toString().trim());
				}
            	break;
        }
	}
	
	public void searchPeople(String search) {
		progressBar.setVisibility(View.VISIBLE);
		lsvList.setVisibility(View.GONE);
		
		String url = Referensi.url+"/search/user/"+search;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	try {
					seAdapter = new SearchPeopleAdapter(SearchActivity.this, response.getJSONArray("value"));
					lsvList.setAdapter(seAdapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
            	
    			progressBar.setVisibility(View.GONE);
        		lsvList.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	progressBar.setVisibility(View.GONE);
            	Toast.makeText(getBaseContext(), "People Not Found", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsObjRequest);
	}
	
	public void searchVenue(String search) {
		progressBar.setVisibility(View.VISIBLE);
		lsvListVenue.setVisibility(View.GONE);
		
		String url = Referensi.url+"/searchPlace/"+search+"/"+city+"/0";
		JsonArrayRequest jsArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @SuppressLint("NewApi")
			@Override
            public void onResponse(JSONArray response) {
            	seAdapter2 = new SearchVenueAdapter(SearchActivity.this, response);
    			lsvListVenue.setAdapter(seAdapter2);
    			
    			progressBar.setVisibility(View.GONE);
    			lsvListVenue.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	progressBar.setVisibility(View.GONE);
            	Toast.makeText(getBaseContext(), "Place Not Found", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsArrayRequest);
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
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
	
}
