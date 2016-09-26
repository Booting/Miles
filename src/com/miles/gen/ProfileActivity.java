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
import com.miles.gen.TimelineAdapter.TimelineAdapterListener;
import com.miles.referensi.FontCache;
import com.miles.referensi.ImageLoader;
import com.miles.referensi.Referensi;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends Activity implements OnClickListener, TimelineAdapterListener {
	private TextView txtName, txtSave, txtNowBack, txtStatus;
	private Button btnBack, btnFollowing, btnFollowers, btnSetting;
	private Typeface fontUbuntuL, fontUbuntuB;
	private ListView lsvList, lsvListt, lsvListTimeline;
	@SuppressWarnings("unused")
	private LinearLayout linParent, linFollow, linProfile, linImage, linFolloww;
	private FollowAdapter followAdapter, followAdapterr;
    private RequestQueue queue;
    private SharedPreferences milesPref;
    private RelativeLayout bottomBarA, bottomBarB, bottomBarC, bottomBarD, bottomBarE;
    private View viewBarA, viewBarB, viewBarC, viewBarD, viewBarE;
    private TimelineAdapter timelineAdapter;
    private ProgressBar progressBar;
    private ImageView imgProfileZ, iconFollow, imgSetting;
    private JSONObject jsObject;
    private ImageLoader imgLoader;
    private JSONArray jsonArray;
    private String userId="", status="";
	private ProgressDialog pDialog;
    
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activityslideup, R.anim.no_anim);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile);

        imgLoader         = new ImageLoader(this);
        progressBar 	  = (ProgressBar) findViewById(R.id.progressBusy);
        milesPref 	      = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
		queue 		      = Volley.newRequestQueue(this);
        fontUbuntuL       = FontCache.get(this, "Ubuntu-L");
		fontUbuntuB       = FontCache.get(this, "Ubuntu-B");
		txtName		      = (TextView) findViewById(R.id.txtName);
		btnFollowing      = (Button) findViewById(R.id.btnFollowing);
		btnFollowers      = (Button) findViewById(R.id.btnFollowers);
		btnSetting		  = (Button) findViewById(R.id.btnSetting);
		lsvList		      = (ListView) findViewById(R.id.lsvList);
		lsvListt	      = (ListView) findViewById(R.id.lsvListt);
		linParent	      = (LinearLayout) findViewById(R.id.linParent);
		txtSave		      = (TextView) findViewById(R.id.txtSave);
		linFollow		  = (LinearLayout) findViewById(R.id.linFollow);
		linProfile		  = (LinearLayout) findViewById(R.id.linProfile);
		btnBack			  = (Button) findViewById(R.id.btnBack);
		bottomBarA        = (RelativeLayout) findViewById(R.id.bottomBarA);
		bottomBarB   	  = (RelativeLayout) findViewById(R.id.bottomBarB);
		bottomBarC   	  = (RelativeLayout) findViewById(R.id.bottomBarC);
		bottomBarD   	  = (RelativeLayout) findViewById(R.id.bottomBarD);
		bottomBarE   	  = (RelativeLayout) findViewById(R.id.bottomBarE);
		viewBarA     	  = (View) findViewById(R.id.viewBarA);
		viewBarB     	  = (View) findViewById(R.id.viewBarB);
		viewBarC     	  = (View) findViewById(R.id.viewBarC);
		viewBarD     	  = (View) findViewById(R.id.viewBarD);
		viewBarE     	  = (View) findViewById(R.id.viewBarE);
		lsvListTimeline   = (ListView) findViewById(R.id.lsvListTimeline);
		linImage		  = (LinearLayout) findViewById(R.id.linImage);
		imgProfileZ		  = (ImageView) findViewById(R.id.imgProfileZ);
		txtNowBack		  = (TextView) findViewById(R.id.txtNowBack);
		linFolloww		  = (LinearLayout) findViewById(R.id.linFolloww);
		txtStatus		  = (TextView) findViewById(R.id.txtStatus);
		iconFollow		  = (ImageView) findViewById(R.id.iconFollow);
		imgSetting        = (ImageView) findViewById(R.id.imgSetting);
		
		viewBarA.setVisibility(View.GONE);
		viewBarB.setVisibility(View.VISIBLE);
		viewBarC.setVisibility(View.GONE);
		viewBarD.setVisibility(View.GONE);
		viewBarE.setVisibility(View.GONE);
		
		pDialog = new ProgressDialog(ProfileActivity.this);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);
		
		txtName.setTypeface(fontUbuntuL);
		btnFollowing.setTypeface(fontUbuntuL);
		btnFollowers.setTypeface(fontUbuntuL);
		txtSave.setTypeface(fontUbuntuB);
		txtNowBack.setTypeface(fontUbuntuB);
		txtStatus.setTypeface(fontUbuntuL);
		
		btnFollowing.setActivated(true);
		btnFollowing.setOnClickListener(this);
		btnFollowers.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		bottomBarA.setOnClickListener(this);
		bottomBarB.setOnClickListener(this);
		bottomBarC.setOnClickListener(this);
		bottomBarD.setOnClickListener(this);
		bottomBarE.setOnClickListener(this);
		btnSetting.setOnClickListener(this);
		linFolloww.setOnClickListener(this);
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		ViewGroup.LayoutParams params  = imgProfileZ.getLayoutParams();
	    params.height  = (int) (0.50*displaymetrics.heightPixels);
	    
	    checkUser();
	}
	
	public void checkUser() {
		if (getIntent().getExtras()!=null) {
	    	if (getIntent().getExtras().getString("id").equalsIgnoreCase(milesPref.getString("id", ""))) {
	    		btnSetting.setVisibility(View.VISIBLE);
		    	imgSetting.setVisibility(View.VISIBLE);
		    	linFolloww.setVisibility(View.GONE);
		    	userId = milesPref.getString("id", "");
	    	} else {
	    		btnSetting.setVisibility(View.GONE);
		    	imgSetting.setVisibility(View.GONE);
		    	linFolloww.setVisibility(View.VISIBLE);
		    	userId = getIntent().getExtras().getString("id");
	    	}
	    } else {
	    	btnSetting.setVisibility(View.VISIBLE);
	    	imgSetting.setVisibility(View.VISIBLE);
	    	linFolloww.setVisibility(View.GONE);
	    	userId = milesPref.getString("id", "");
	    }
	    
	    checkFollow();
	}
	
	public void checkFollow() {
		progressBar.setVisibility(View.VISIBLE);
		String url = Referensi.url+"/checkfollow/"+userId+"/"+milesPref.getString("id", "");
		
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	System.out.println("checkFollow (response): "+response);
            	try {
					status = response.getString("status");
					if (status.equalsIgnoreCase("yes")) {
						iconFollow.setVisibility(View.GONE);
			        	txtStatus.setText("Unfollow");
			        } else {
			        	iconFollow.setVisibility(View.VISIBLE);
			        	txtStatus.setText("Follow");
			        }
					getUserProfile();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					getUserProfile();
				}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	progressBar.setVisibility(View.GONE);
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsObjRequest);
	}
	
	public void getUserProfile() {
		progressBar.setVisibility(View.VISIBLE);
		lsvListTimeline.setVisibility(View.GONE);
		
		String url = Referensi.url+"/user/"+userId;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	getUserTimeline(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	//progressBar.setVisibility(View.GONE);
            	getUserTimeline(null);
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsObjRequest);
	}
	
	public void getUserTimeline(final JSONObject responsee) {
		String url = Referensi.url+"/timeline/"+userId;
		JsonArrayRequest jsARequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
            	System.out.println("getUserTimeline (response): ");
            	System.out.println("getUserTimeline (response): ");
            	System.out.println("getUserTimeline (response): "+response);
            	
        		jsObject  = responsee;
        		jsonArray = response;
        		
        		getListFollowing(responsee, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	getListFollowing(responsee, jsonArray);
            }
        });
        queue.add(jsARequest);
	}
	
	public void getListFollowing(final JSONObject jsonObject, final JSONArray jsonArray) {
		String url = Referensi.url+"/following/"+userId;
    	System.out.println("getListFollowing: "+url);
		JsonArrayRequest jsARequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
            	System.out.println("getListFollowing: "+response);
            	followAdapter = new FollowAdapter(ProfileActivity.this, response, "Following");
        		lsvList.setAdapter(followAdapter);
        		getListFollowers(jsonObject, jsonArray, response.length());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	System.out.println("getListFollowing: "+error.toString());
            	getListFollowers(jsonObject, jsonArray, 0);
            }
        });
        queue.add(jsARequest);
	}
	
	public void getListFollowers(final JSONObject jsonObject, final JSONArray jsonArray, final int countFollowing) {
		String url = Referensi.url+"/follower/"+userId;
    	System.out.println("getListFollowers: "+url);
		JsonArrayRequest jsARequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
            	System.out.println("getListFollowers: "+response);
            	followAdapterr = new FollowAdapter(ProfileActivity.this, response, "Followers");
        		lsvListt.setAdapter(followAdapterr);
        		
        		timelineAdapter = new TimelineAdapter(ProfileActivity.this, ProfileActivity.this, jsonObject, jsonArray, countFollowing, response.length(), status);
        		lsvListTimeline.setAdapter(timelineAdapter);
        		
        		progressBar.setVisibility(View.GONE);
        		lsvListTimeline.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	System.out.println("getListFollowers: "+error.toString());
            	timelineAdapter = new TimelineAdapter(ProfileActivity.this, ProfileActivity.this, jsonObject, jsonArray, countFollowing, 0, status);
        		lsvListTimeline.setAdapter(timelineAdapter);
        		
        		progressBar.setVisibility(View.GONE);
        		lsvListTimeline.setVisibility(View.VISIBLE);
            }
        });
        queue.add(jsARequest);
	}
	
	@SuppressLint("NewApi")
	@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFollowing:
            	btnFollowing.setActivated(true);
            	btnFollowers.setActivated(false);
            	lsvList.setVisibility(View.VISIBLE);
            	lsvListt.setVisibility(View.GONE);
            	break;
            case R.id.btnFollowers:
            	btnFollowers.setActivated(true);
            	btnFollowing.setActivated(false);
            	lsvList.setVisibility(View.GONE);
            	lsvListt.setVisibility(View.VISIBLE);
            	break;
            case R.id.btnBack:
            	if (linProfile.getVisibility()==View.VISIBLE) {
            		finish();
            	} else {
            		linProfile.setVisibility(View.VISIBLE);
                	linFollow.setVisibility(View.GONE);
            	}
            	break;
            case R.id.bottomBarA:
            	Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		startActivity(intent);
        		finish();
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
            case R.id.btnSetting:
            	if (progressBar.getVisibility()!=View.VISIBLE) {
            		startActivity(new Intent(getApplicationContext(), SettingActivity.class).putExtra("Profile", jsObject.toString()));
            	}
            	break;
            case R.id.linFolloww:
            	followOrUnfollow();
            	break;
        }
	}
	
	public void followOrUnfollow() {
		if (txtStatus.getText().toString().equalsIgnoreCase("Follow")) {
			pDialog.show();
			String url = Referensi.url+"/follow";
			JSONObject jsObject = new JSONObject();
			try {
				jsObject.put("profile_id", userId);
				jsObject.put("follower_id", milesPref.getString("id", ""));
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			
			System.out.println("onFollowClicked [params]: "+jsObject + ", URL: "+url);
			JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsObject, new Response.Listener<JSONObject>() {
	            @Override
	            public void onResponse(JSONObject response) {
					System.out.println("onFollowClicked [response]: "+response);
	            	try {
						if (response.getString("status").equalsIgnoreCase("success")) {
							Toast.makeText(ProfileActivity.this, "Follow "+ response.optString("status"), Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(ProfileActivity.this, "Follow "+ response.optString("status"), Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	pDialog.dismiss();
	            	checkUser();
	            }
	        }, new Response.ErrorListener() {
	            @Override
	            public void onErrorResponse(VolleyError error) {
	            	pDialog.dismiss();
	                Toast.makeText(ProfileActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
	            }
	        });
	        queue.add(jsObjRequest);
		} else {
			pDialog.show();
			String url = Referensi.url+"/follow/"+userId+"/"+milesPref.getString("id", "");
			
			System.out.println("onUnfollowClicked [url]: "+url);
			JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
	            @Override
	            public void onResponse(JSONObject response) {
					System.out.println("onUnfollowClicked [response]: "+response);
	            	try {
						if (response.getString("status").equalsIgnoreCase("success")) {
							Toast.makeText(ProfileActivity.this, "Unfollow "+ response.optString("status"), Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(ProfileActivity.this, "Unfollow "+ response.optString("status"), Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	pDialog.dismiss();
	            	checkUser();
	            }
	        }, new Response.ErrorListener() {
	            @Override
	            public void onErrorResponse(VolleyError error) {
	            	pDialog.dismiss();
	                Toast.makeText(ProfileActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
	            }
	        });
	        queue.add(jsObjRequest);
		}
	}
	
	@SuppressLint("NewApi")
	public void onLinCoFollowersClicked() {
		linProfile.setVisibility(View.GONE);
    	linFollow.setVisibility(View.VISIBLE);
    	btnFollowers.setActivated(true);
    	btnFollowing.setActivated(false);
    	lsvList.setVisibility(View.GONE);
    	lsvListt.setVisibility(View.VISIBLE);
	}
	
	@SuppressLint("NewApi")
	public void onLinCoFollowingClicked() {
		linProfile.setVisibility(View.GONE);
    	linFollow.setVisibility(View.VISIBLE);
    	btnFollowers.setActivated(false);
    	btnFollowing.setActivated(true);
    	lsvList.setVisibility(View.VISIBLE);
    	lsvListt.setVisibility(View.GONE);
	}
	
	public void onProfileImageClicked(String imageUrl) {
		int loaderr = R.drawable.miles_loader;
        imgLoader.DisplayImage(imageUrl, loaderr, imgProfileZ);
		linImage.setVisibility(View.VISIBLE);
		//lsvListTimeline.setVisibility(View.GONE);
	}
	
	public void closeClick(View v) {
		linImage.setVisibility(View.GONE);
		//lsvListTimeline.setVisibility(View.VISIBLE);
	}
	
	@Override
	public boolean onKeyUp( int keyCode, KeyEvent event ){
		if( keyCode == KeyEvent.KEYCODE_BACK ) {
			if (linProfile.getVisibility()==View.VISIBLE) {
        		finish();
        	} else {
        		linProfile.setVisibility(View.VISIBLE);
            	linFollow.setVisibility(View.GONE);
        	}
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
