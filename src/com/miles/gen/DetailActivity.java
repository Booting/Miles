package com.miles.gen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.miles.referensi.EventInfo;
import com.miles.referensi.FontCache;
import com.miles.referensi.GPSTracker;
import com.miles.referensi.GoogleMapV2Direction;
import com.miles.referensi.ImageLoader;
import com.miles.referensi.NestedListView;
import com.miles.referensi.Referensi;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
 
public class DetailActivity extends ActionBarActivity implements OnClickListener {
    private SharedPreferences milesPref;
    private GoogleMap mMap;
    private SupportMapFragment fragment;
    private ImageView imgYellowShadow, imgView, imgSetting, imgYellowShadow2, btnNavigation; 
    private ImageSwitcher imgViewDetail, imgLike, imgDisLike;
    private RelativeLayout relMap;
    private TextView txtLocationName, txtDateTime, txtLocationDetail, txtPlaceName;
    private Typeface fontUbuntuL, fontUbuntuB;
    private ImageLoader imgLoader;
    private TextView lblFloor, txtFloor, lblStoreCategory, txtStoreCategory, lblPhone, txtPhone, lblCollections,
    	txtCollections, lblCuisines, txtCuisines, lblOpeningHours, txtOpeningHours,
    	txtLikeCount, txtDisLikeCount, txtSaySomething, txtName, txtWhatSay, txtShare;
    private Button btnAttend, btnCall, btnPost, btnSetting, btnBack;
    private EditText txtComment;
    private LinearLayout linLike, linDisLike, linShare;
    private boolean isLike=false, isDisLike=false;
    private NestedListView lsvList;
    private CommentAdapter commentAdapter;
    private RelativeLayout bottomBarA, bottomBarB, bottomBarC, bottomBarD, bottomBarE, relZoom, relUnZoom;
    private View viewBarA, viewBarB, viewBarC, viewBarD, viewBarE;
    private DisplayMetrics displaymetrics;
    private GPSTracker gps;
    private Double newLatitude, newLongitude;
    private LatLng fromPosition, toPosition;
	private GoogleMapV2Direction md;
	private ProgressDialog pDialog;
    private RequestQueue queue;
    private String placeIDD, params, from;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);

        queue 		      = Volley.newRequestQueue(this);
        imgLoader 	      = new ImageLoader(DetailActivity.this);
        fontUbuntuL       = FontCache.get(this, "Ubuntu-L");
		fontUbuntuB       = FontCache.get(this, "Ubuntu-B");
		milesPref 		  = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
		fragment  		  = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
		//fragmentZoom	  = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapsZomm);
		mMap      		  = fragment.getMap();
		imgYellowShadow   = (ImageView) findViewById(R.id.imgYellowShadow);
		relMap			  = (RelativeLayout) findViewById(R.id.relMap);
		txtLocationName   = (TextView) findViewById(R.id.txtLocationName);
		txtDateTime		  = (TextView) findViewById(R.id.txtDateTime);
		txtLocationDetail = (TextView) findViewById(R.id.txtLocationDetail);
		imgViewDetail	  = (ImageSwitcher) findViewById(R.id.imgViewDetail);
		imgLike           = (ImageSwitcher) findViewById(R.id.imgLike);
		imgDisLike        = (ImageSwitcher) findViewById(R.id.imgDisLike);
		imgView			  = (ImageView) findViewById(R.id.imgView);
		txtPlaceName	  = (TextView) findViewById(R.id.txtPlaceName);
		lblFloor		  = (TextView) findViewById(R.id.lblFloor); 
		txtFloor		  = (TextView) findViewById(R.id.txtFloor); 
		lblStoreCategory  = (TextView) findViewById(R.id.lblStoreCategory); 
		txtStoreCategory  = (TextView) findViewById(R.id.txtStoreCategory); 
		lblPhone		  = (TextView) findViewById(R.id.lblPhone); 
		txtPhone		  = (TextView) findViewById(R.id.txtPhone); 
		lblCollections	  = (TextView) findViewById(R.id.lblCollections);
    	txtCollections	  = (TextView) findViewById(R.id.txtCollections); 
    	lblCuisines		  = (TextView) findViewById(R.id.lblCuisines); 
    	txtCuisines		  = (TextView) findViewById(R.id.txtCuisines);
    	lblOpeningHours	  = (TextView) findViewById(R.id.lblOpeningHours);
    	txtOpeningHours	  = (TextView) findViewById(R.id.txtOpeningHours);
    	txtLikeCount	  = (TextView) findViewById(R.id.txtLikeCount);
    	txtDisLikeCount	  = (TextView) findViewById(R.id.txtDisLikeCount);
    	btnAttend		  = (Button) findViewById(R.id.btnAttend);
    	btnCall			  = (Button) findViewById(R.id.btnCall);
    	txtSaySomething	  = (TextView) findViewById(R.id.txtSaySomething);
    	txtComment		  = (EditText) findViewById(R.id.txtComment);
    	btnPost			  = (Button) findViewById(R.id.btnPost);
    	btnSetting		  = (Button) findViewById(R.id.btnSetting);
    	imgSetting		  = (ImageView) findViewById(R.id.imgSetting);
    	txtName			  = (TextView) findViewById(R.id.txtName);
    	btnBack		      = (Button) findViewById(R.id.btnBack);
    	txtWhatSay		  = (TextView) findViewById(R.id.txtWhatSay);
    	linLike			  = (LinearLayout) findViewById(R.id.linLike);
    	linDisLike        = (LinearLayout) findViewById(R.id.linDisLike);
    	lsvList			  = (NestedListView) findViewById(R.id.lsvList);
		bottomBarA   	  = (RelativeLayout) findViewById(R.id.bottomBarA);
		bottomBarB   	  = (RelativeLayout) findViewById(R.id.bottomBarB);
		bottomBarC   	  = (RelativeLayout) findViewById(R.id.bottomBarC);
		bottomBarD   	  = (RelativeLayout) findViewById(R.id.bottomBarD);
		bottomBarE   	  = (RelativeLayout) findViewById(R.id.bottomBarE);
		viewBarA          = (View) findViewById(R.id.viewBarA);
		viewBarB          = (View) findViewById(R.id.viewBarB);
		viewBarC          = (View) findViewById(R.id.viewBarC);
		viewBarD          = (View) findViewById(R.id.viewBarD);
		viewBarE          = (View) findViewById(R.id.viewBarE);
		txtShare		  = (TextView) findViewById(R.id.txtShare);
		linShare		  = (LinearLayout) findViewById(R.id.linShare);
		relZoom			  = (RelativeLayout) findViewById(R.id.relZoom);
		imgYellowShadow2  = (ImageView) findViewById(R.id.imgYellowShadow2);
		relUnZoom		  = (RelativeLayout) findViewById(R.id.relUnZoom);
		btnNavigation     = (ImageView) findViewById(R.id.btnNavigation);
		from			  = getIntent().getExtras().getString("From");
		
		if (from.equalsIgnoreCase("Home")) {
			viewBarA.setVisibility(View.VISIBLE);
			viewBarB.setVisibility(View.GONE);
			viewBarC.setVisibility(View.GONE);
			viewBarD.setVisibility(View.GONE);
			viewBarE.setVisibility(View.GONE);
		} else if (from.equalsIgnoreCase("Filtering")) {
			viewBarA.setVisibility(View.GONE);
			viewBarB.setVisibility(View.GONE);
			viewBarC.setVisibility(View.VISIBLE);
			viewBarD.setVisibility(View.GONE);
			viewBarE.setVisibility(View.GONE);
		} else if (from.equalsIgnoreCase("Favorite")) {
			viewBarA.setVisibility(View.GONE);
			viewBarB.setVisibility(View.GONE);
			viewBarC.setVisibility(View.GONE);
			viewBarD.setVisibility(View.VISIBLE);
			viewBarE.setVisibility(View.GONE);
		}
		
    	btnSetting.setVisibility(View.GONE);
    	imgSetting.setVisibility(View.GONE);
    	txtName.setTypeface(fontUbuntuL);
		txtLocationName.setTypeface(fontUbuntuL);
		txtDateTime.setTypeface(fontUbuntuL);
		txtLocationDetail.setTypeface(fontUbuntuL);
		txtPlaceName.setTypeface(fontUbuntuB);
		lblFloor.setTypeface(fontUbuntuL); 
		txtFloor.setTypeface(fontUbuntuL); 
		lblStoreCategory.setTypeface(fontUbuntuL); 
		txtStoreCategory.setTypeface(fontUbuntuL); 
		lblPhone.setTypeface(fontUbuntuL); 
		txtPhone.setTypeface(fontUbuntuL); 
		lblCollections.setTypeface(fontUbuntuL);
    	txtCollections.setTypeface(fontUbuntuL); 
    	lblCuisines.setTypeface(fontUbuntuL); 
    	txtCuisines.setTypeface(fontUbuntuL); 
    	lblOpeningHours.setTypeface(fontUbuntuL); 
    	txtOpeningHours.setTypeface(fontUbuntuL);
    	txtLikeCount.setTypeface(fontUbuntuL);
    	txtDisLikeCount.setTypeface(fontUbuntuL);
    	btnAttend.setTypeface(fontUbuntuL);
    	btnCall.setTypeface(fontUbuntuL);
    	txtSaySomething.setTypeface(fontUbuntuL);
    	txtComment.setTypeface(fontUbuntuL);
    	btnPost.setTypeface(fontUbuntuL);
    	txtWhatSay.setTypeface(fontUbuntuL);
    	txtShare.setTypeface(fontUbuntuL);
    	
    	pDialog = new ProgressDialog(DetailActivity.this);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);
    	
    	btnBack.setOnClickListener(this);
    	linLike.setOnClickListener(this);
    	linDisLike.setOnClickListener(this);
		bottomBarA.setOnClickListener(this);
		bottomBarB.setOnClickListener(this);
		bottomBarC.setOnClickListener(this);
		bottomBarD.setOnClickListener(this);
		bottomBarE.setOnClickListener(this);
		btnCall.setOnClickListener(this);
		btnAttend.setOnClickListener(this);
		btnPost.setOnClickListener(this);
		
		if (getIntent().getExtras().getString("Status").equalsIgnoreCase("event")) {
        	viewBarA.setBackgroundColor(Color.parseColor("#5b1f6f"));
    		btnAttend.setText("ATTEND");
    		placeIDD = getIntent().getExtras().getString("PlaceId");
    	} else {
    		if (getIntent().getExtras().getString("Status").equalsIgnoreCase("day")) {
            	viewBarA.setBackgroundColor(Color.parseColor("#48d1b6"));
    		} else {
            	viewBarA.setBackgroundColor(Color.parseColor("#1f4d6f"));
    		}
    		btnAttend.setText("CHECK-IN");
    		placeIDD = getIntent().getExtras().getString("PlaceId");
    	}
		
		displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		ViewGroup.LayoutParams params  = relMap.getLayoutParams();
	    params.height = (int) (0.40*displaymetrics.heightPixels);
	    
	    ViewGroup.LayoutParams paramss = imgView.getLayoutParams();
		paramss.height = (int) (0.40*displaymetrics.heightPixels);
	    
	    ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) imgYellowShadow.getLayoutParams();
	    param.height = (int) (0.20*displaymetrics.heightPixels);
        param.setMargins(0, (int) (0.21*displaymetrics.heightPixels), 0, 0);
        
        ViewGroup.MarginLayoutParams param1 = (ViewGroup.MarginLayoutParams) linShare.getLayoutParams();
	    param1.setMargins(0, (int) (0.31*displaymetrics.heightPixels), (int) (0.05*displaymetrics.widthPixels), 0);
	    
	    ViewGroup.MarginLayoutParams param2 = (ViewGroup.MarginLayoutParams) relZoom.getLayoutParams();
	    param2.height = (int) (0.20*displaymetrics.heightPixels);
        param2.setMargins(0, (int) (0.26*displaymetrics.heightPixels), 0, 0);
	    
        imgViewDetail.setFactory(new ViewSwitcher.ViewFactory() {
            @SuppressLint("InlinedApi")
			@SuppressWarnings("deprecation")
			@Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myView.setLayoutParams(new ImageSwitcher.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT));
                myView.setImageResource(R.drawable.icon_three_dot);
                return myView;
            }
        });
        imgViewDetail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (txtLocationDetail.getVisibility()==View.VISIBLE) {
					Animation in = AnimationUtils.loadAnimation(DetailActivity.this, R.anim.activityslideup);
                    Animation out = AnimationUtils.loadAnimation(DetailActivity.this, R.anim.activityslidedown);
                    imgViewDetail.setInAnimation(in);
                    imgViewDetail.setOutAnimation(out);
                    imgViewDetail.setImageResource(R.drawable.icon_three_dot);
					txtLocationDetail.setVisibility(View.GONE);
				} else {
					Animation in = AnimationUtils.loadAnimation(DetailActivity.this, R.anim.activityslideup);
                    Animation out = AnimationUtils.loadAnimation(DetailActivity.this, R.anim.activityslidedown);
                    imgViewDetail.setInAnimation(in);
                    imgViewDetail.setOutAnimation(out);
                    imgViewDetail.setImageResource(R.drawable.icon_x);
					txtLocationDetail.setVisibility(View.VISIBLE);
				}
			}
		});
        
        imgLike.setFactory(new ViewSwitcher.ViewFactory() {
            @SuppressLint("InlinedApi")
			@SuppressWarnings("deprecation")
			@Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myView.setLayoutParams(new ImageSwitcher.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT));
                myView.setImageResource(R.drawable.like);
                return myView;
            }
        });
        imgDisLike.setFactory(new ViewSwitcher.ViewFactory() {
            @SuppressLint("InlinedApi")
			@SuppressWarnings("deprecation")
			@Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myView.setLayoutParams(new ImageSwitcher.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT));
                myView.setImageResource(R.drawable.dislike);
                return myView;
            }
        });
        
		getPlaceDetail();
    }
    
    public void getPlaceDetail() {
		pDialog.show();
		String url="";
		if (getIntent().getExtras().getString("Status").equalsIgnoreCase("event")) {
			url = Referensi.url+"/event/"+placeIDD;
			params = "value";
		} else {
			url = Referensi.url+"/place/"+placeIDD;
			params = "place";
		}

    	System.out.println("getPlaceDetail: "+url);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("SimpleDateFormat")
			@Override
            public void onResponse(JSONObject response) {
            	System.out.println("getPlaceDetail (response): "+response);
            	try {
            		txtPlaceName.setText(response.getJSONObject(params).getString("name"));
	            	txtLocationDetail.setText(response.getJSONObject(params).getString("address"));
            		
            		if (params.equalsIgnoreCase("place")) {
            			txtName.setText(response.getJSONObject(params).getString("city"));
                		txtLocationName.setText(response.getJSONObject(params).getString("city"));
	            		if (response.getJSONObject(params).getString("telp").equalsIgnoreCase("-") || response.getJSONObject(params).getString("telp").equalsIgnoreCase(" ")) {
		            		txtPhone.setText("-");
		            	} else {
		            		txtPhone.setText(response.getJSONObject(params).getString("telp"));
		            	}
		            	txtDateTime.setText(response.getJSONObject(params).getString("email"));
		            	txtOpeningHours.setText(response.getJSONObject(params).getString("email"));
            		} else {
            			txtName.setText("EVENT");
            			txtLocationName.setText(response.getJSONObject(params).getString("address"));
            			txtPhone.setText("-");
            			
            			SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMM yyyy HH:mm a", Locale.ENGLISH);
            	        try {
            	            Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(response.getJSONObject(params).getString("start_time"));
            	            Date endDate   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(response.getJSONObject(params).getString("end_time"));
            	            txtDateTime.setText(formatter.format(startDate)+" - "+formatter.format(endDate));
            	            txtOpeningHours.setText(formatter.format(startDate)+" - "+formatter.format(endDate));
            	        } catch (ParseException e) {
            	    		e.printStackTrace();
            	    	}
            		}
	            	
	            	int loader = R.drawable.miles_loader;
	        		imgLoader.DisplayImage(Referensi.url2+response.getJSONObject(params).getString("photo").replace(" ", "%20"), loader, imgView);
	        		
	        		String latLong[] = response.getJSONObject(params).getString("location").split(",");
	        		double lat = Double.parseDouble(latLong[0].replace("(", ""));
	        		double lng = Double.parseDouble(latLong[1].replace(")", ""));
	        		initLocationManager(lat, lng);
				} catch (JSONException e) {
					e.printStackTrace();
				}
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
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btnBack:
        	finish();
        	break;
        case R.id.linLike:
        	onLikeClicked();
        	if (isLike) {
				Animation in = AnimationUtils.loadAnimation(DetailActivity.this, R.anim.activityslideup);
                Animation out = AnimationUtils.loadAnimation(DetailActivity.this, R.anim.activityslidedown);
                imgLike.setInAnimation(in);
                imgLike.setOutAnimation(out);
                imgLike.setImageResource(R.drawable.like);
				isLike=false;
			} else {
				Animation in = AnimationUtils.loadAnimation(DetailActivity.this, R.anim.activityslidedown);
                Animation out = AnimationUtils.loadAnimation(DetailActivity.this, R.anim.activityslideup);
                imgLike.setInAnimation(in);
                imgLike.setOutAnimation(out);
                imgLike.setImageResource(R.drawable.like_blue);
				isLike=true;
			}
        	break;
        case R.id.linDisLike:
        	if (isDisLike) {
				Animation in = AnimationUtils.loadAnimation(DetailActivity.this, R.anim.activityslideup);
                Animation out = AnimationUtils.loadAnimation(DetailActivity.this, R.anim.activityslidedown);
                imgDisLike.setInAnimation(in);
                imgDisLike.setOutAnimation(out);
                imgDisLike.setImageResource(R.drawable.dislike);
                isDisLike=false;
			} else {
				Animation in = AnimationUtils.loadAnimation(DetailActivity.this, R.anim.activityslidedown);
                Animation out = AnimationUtils.loadAnimation(DetailActivity.this, R.anim.activityslideup);
                imgDisLike.setInAnimation(in);
                imgDisLike.setOutAnimation(out);
                imgDisLike.setImageResource(R.drawable.dislike_red);
                isDisLike=true;
			}
        	break;
        case R.id.bottomBarA:
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
        case R.id.bottomBarE:
        	startActivity(new Intent(getApplicationContext(), HomeMapsActivity.class));
			break;
        case R.id.btnCall:
        	if (txtPhone.getText().toString().equalsIgnoreCase("") || txtPhone.getText().toString().equalsIgnoreCase("-")) {
				Toast.makeText(DetailActivity.this, "Sorry, No Telp Number!", Toast.LENGTH_SHORT).show();
			} else {
				String uri = "tel:" + txtPhone.getText().toString().replace("-", "").trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
			}
        	break;
        case R.id.btnAttend:
        	onCheckInClicked(getIntent().getExtras().getString("PlaceId"));
        	break;
        case R.id.btnPost:
        	onPostClicked();
        	break;
        }
    }
    
    public void onPostClicked() {
		pDialog.show();
		String postType = "";
		JSONObject jsObject = new JSONObject();
		try {
			if (getIntent().getExtras().getString("Status").equalsIgnoreCase("event")) {
				postType = "/evtComment";
				jsObject.put("event_id", placeIDD);
			} else {
				postType = "/review";
				jsObject.put("place_id", placeIDD);
				jsObject.put("rating", "5");
			}
			jsObject.put("profile_id", milesPref.getString("id", ""));
			jsObject.put("text", txtComment.getText().toString());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		String url = Referensi.url+postType;
		
		System.out.println("onPostClicked [params]: "+jsObject + ", URL: "+url);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	try {
					if (response.getString("status").equalsIgnoreCase("success")) {
						txtComment.setText("");
						getPlaceReviewList();
					} else {
						Toast.makeText(getBaseContext(), "Review "+ response.optString("status"), Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
    
    public void onLikeClicked() {
		pDialog.show();
		String url = Referensi.url+"/likeReview";
		JSONObject jsObject = new JSONObject();
		try {
			jsObject.put("review_id", placeIDD);
			jsObject.put("profile_id", milesPref.getString("id", ""));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("onLikeClicked (params): "+jsObject + ", URL: "+url);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
        		System.out.println("onLikeClicked (response): "+response);
            	try {
					if (response.getString("status").equalsIgnoreCase("success")) {
						getCountLikeReview();
					} else {
						Toast.makeText(getBaseContext(), "Like "+ response.optString("status"), Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
        		System.out.println("onLikeClicked (error): "+error.toString());
            	pDialog.dismiss();
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsObjRequest);
	}
    
    public void onCheckInClicked(String placeId) {
    	if (getIntent().getExtras().getString("Status").equalsIgnoreCase("event")) {
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
    	} else {
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
	}
    
    public void btnZoomMapClick(View v) {
    	ViewGroup.LayoutParams params  = relMap.getLayoutParams();
	    params.height = (int) (0.82*displaymetrics.heightPixels);
	    relMap.setLayoutParams(params);
	    
	    imgYellowShadow2.setVisibility(View.VISIBLE);
	    imgYellowShadow.setVisibility(View.GONE);
	    linShare.setVisibility(View.GONE);
	    ViewGroup.LayoutParams params1 = imgYellowShadow2.getLayoutParams();
	    params1.height = (int) (0.18*displaymetrics.heightPixels);
	    imgYellowShadow2.setLayoutParams(params1);

	    relUnZoom.setVisibility(View.VISIBLE);
	    btnNavigation.setVisibility(View.VISIBLE);
	    relZoom.setVisibility(View.GONE);
	    ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) relUnZoom.getLayoutParams();
	    params2.height = (int) (0.15*displaymetrics.heightPixels);
	    relUnZoom.setLayoutParams(params2);
    }
    
    public void btnUnZoomMapClick(View v) {
    	imgYellowShadow.setVisibility(View.VISIBLE);
	    linShare.setVisibility(View.VISIBLE);
	    imgYellowShadow2.setVisibility(View.GONE);

	    relZoom.setVisibility(View.VISIBLE);
	    relUnZoom.setVisibility(View.GONE);
	    btnNavigation.setVisibility(View.GONE);
	    
	    ViewGroup.LayoutParams params  = relMap.getLayoutParams();
	    params.height = (int) (0.40*displaymetrics.heightPixels);
	    relMap.setLayoutParams(params);
    }
    
    public void btnNavigationClick(View v) {
    	md = new GoogleMapV2Direction();
    	getDirectionMap(fromPosition, toPosition);
    }
    
    /**
     * Initialize the location manager.
     */
    private void initLocationManager(double latitude, double longitude) {
    	// create class object
        gps = new GPSTracker(DetailActivity.this);

        // check if GPS enabled     
        if (gps.canGetLocation()) {
        	newLatitude  = gps.getLatitude();
            newLongitude = gps.getLongitude();
            initializeMap(latitude, longitude);
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
    
    private void initializeMap(double lat1, double lng1) {
        if (mMap!=null) {
            fromPosition = new LatLng(newLatitude, newLongitude);
			toPosition   = new LatLng(lat1, lng1);

            Location locationA = new Location("point A");
            locationA.setLatitude(newLatitude);
            locationA.setLongitude(newLongitude);

            Location locationB = new Location("point B");
            locationB.setLatitude(lat1);
            locationB.setLongitude(lng1);

            EventInfo eventInfo = new EventInfo(new LatLng(lat1, lng1), "Location", "");
            
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toPosition, 15));
            mMap.addMarker(new MarkerOptions()
	            .position(eventInfo.getLatLong())
	            .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));
            mMap.addMarker(new MarkerOptions().position(fromPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
    		getPlaceReviewList();
        } else {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps)).getMap();
            Toast.makeText(getApplicationContext(), "MAP NULL", Toast.LENGTH_LONG).show();
        }
    }
    
    public void getPlaceReviewList() {
    	String urlReview = "";
    	if (getIntent().getExtras().getString("Status").equalsIgnoreCase("event")) {
    		urlReview = "/evtComment/";
    	} else {
    		urlReview = "/review/place/";
    	}
    	
		String url = Referensi.url+urlReview+placeIDD+"/"+milesPref.getString("id", "");
    	System.out.println("getPlaceReviewList: "+url);
    	JsonArrayRequest jsArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
            	System.out.println("getPlaceReviewList [response]: "+response);
            	commentAdapter = new CommentAdapter(DetailActivity.this, response);
        		lsvList.setAdapter(commentAdapter);
        		getCountLikeReview();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	System.out.println("getPlaceReviewList [error]: "+error.toString());
            	getCountLikeReview();
            }
        });
        queue.add(jsArrayRequest);
	}
    
    public void getCountLikeReview() {
		String url = Referensi.url+"/likeReview/"+placeIDD;

    	System.out.println("getCountLikeReview: "+url);
    	JsonArrayRequest jsArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
            	System.out.println("getCountLikeReview (response): "+response);
            	txtLikeCount.setText(""+response.length());
            	pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	System.out.println("getCountLikeReview (error): "+error.toString());
            	pDialog.dismiss();
            }
        });
        queue.add(jsArrayRequest);
	}
	
    private void getDirectionMap(LatLng from, LatLng to) {
		LatLng fromto[] = { from, to };
		new LongOperation().execute(fromto);
	}

	private class LongOperation extends AsyncTask<LatLng, Void, Document> {
		@Override
		protected Document doInBackground(LatLng... params) {
			Document doc = md.getDocument(params[0], params[1], GoogleMapV2Direction.MODE_DRIVING);
			return doc;
		}
		@Override
		protected void onPostExecute(Document result) {
			setResult(result);
		}
		@Override
		protected void onPreExecute() { }
		@Override
		protected void onProgressUpdate(Void... values) { }
	}

	public void setResult(Document doc) {
		ArrayList<LatLng> directionPoint = md.getDirection(doc);
		PolylineOptions rectLine = new PolylineOptions().width(8).color(Color.RED);

		for (int i = 0; i < directionPoint.size(); i++) {
			rectLine.add(directionPoint.get(i));
		}

		mMap.addPolyline(rectLine);
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
