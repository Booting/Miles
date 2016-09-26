package com.miles.gen;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.miles.referensi.FontCache;
import com.miles.referensi.GPSTracker;
import com.miles.referensi.Referensi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText txtEmail, txtPassword;
	private TextView txtForgotPassword, txtRegisterOne, txtRegisterTwo, txtOr, lblSignWithFacebook;
	private Typeface fontUbuntuL, fontUbuntuB;
    private RelativeLayout loginBtn;
    private Button btnSignIn;
    private RequestQueue queue;
    private SharedPreferences milesPref;
    private LinearLayout linPassword, linRegister;
    private View vPassword;
    private ProgressDialog pDialog;
    @SuppressWarnings("unused")
	private String facebookAccessToken;
    private GPSTracker gps;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
		
		milesPref 		  	= getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
		queue 			  	= Volley.newRequestQueue(this);
		fontUbuntuL 	  	= FontCache.get(this, "Ubuntu-L");
		fontUbuntuB 	  	= FontCache.get(this, "Ubuntu-B");
		txtEmail    	  	= (EditText) findViewById(R.id.txtEmail);
		txtPassword       	= (EditText) findViewById(R.id.txtPassword);
		txtForgotPassword 	= (TextView) findViewById(R.id.txtForgotPassword);
        loginBtn          	= (RelativeLayout) findViewById(R.id.btnLogin);
        btnSignIn		  	= (Button) findViewById(R.id.btnSignIn);
        txtRegisterOne    	= (TextView) findViewById(R.id.txtRegisterOne);
        txtRegisterTwo    	= (TextView) findViewById(R.id.txtRegisterTwo);
        txtOr			    = (TextView) findViewById(R.id.txtOr);
        linPassword		    = (LinearLayout) findViewById(R.id.linPassword);
        linRegister		    = (LinearLayout) findViewById(R.id.linRegister);
        vPassword		    = (View) findViewById(R.id.vPassword);
        lblSignWithFacebook = (TextView) findViewById(R.id.lblSignWithFacebook);
        
		txtEmail.setTypeface(fontUbuntuL);
		txtPassword.setTypeface(fontUbuntuL);
		txtForgotPassword.setTypeface(fontUbuntuL);
		txtRegisterOne.setTypeface(fontUbuntuL);
		txtRegisterTwo.setTypeface(fontUbuntuB);
		lblSignWithFacebook.setTypeface(fontUbuntuB);
		btnSignIn.setTypeface(fontUbuntuB);
		txtOr.setTypeface(fontUbuntuL);
		
		pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Loading ....");
		
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				facebookAuthorize();
			}
		});
        
        txtRegisterTwo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
				overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left );
				finish();
			}
		});
        
        btnSignIn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// affia007@gmail.com && BIAO01tB
				if (txtEmail.getText().toString().equalsIgnoreCase("")&&txtPassword.getText().toString().equalsIgnoreCase("")) {
					txtEmail.setError("Email is required!");
					txtPassword.setError("Password is required!");
				} else if (txtEmail.getText().toString().equalsIgnoreCase("")) {
					txtEmail.setError("Email is required!");
				} else if (txtPassword.getText().toString().equalsIgnoreCase("") && btnSignIn.getText().toString().equalsIgnoreCase("Sign In")) {
					txtPassword.setError("Password is required!");
				} else {
					pDialog.show();
			        
					if (btnSignIn.getText().toString().equalsIgnoreCase("Sign In")) {
						String url = Referensi.url+"/accLogin/"+txtEmail.getText().toString()+"/"+Referensi.md5(txtPassword.getText().toString());
						JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
				            @Override
				            public void onResponse(JSONObject response) {
				            	System.out.println("LOGIN: "+response.toString());
				            	try {
									if (response.getString("status").equalsIgnoreCase("accept")) {
										SharedPreferences.Editor editor = milesPref.edit();
										editor.putString("id", response.getString("id")).commit();
										initLocationManager();
									}
								} catch (JSONException e) {
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
					} else {
						String url = Referensi.url+"/forgotPassword";
						JSONObject jsObject = new JSONObject();
						try {
							jsObject.put("email", txtEmail.getText().toString());
						} catch (JSONException e) {}

		            	System.out.println("FORGOTPASSWORD: "+jsObject.toString());
						JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsObject, new Response.Listener<JSONObject>() {
				            @Override
				            public void onResponse(JSONObject response) {
				            	System.out.println("FORGOTPASSWORD: "+response.toString());
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
			}
		});
        
        txtForgotPassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				linPassword.setVisibility(View.GONE);
				txtForgotPassword.setVisibility(View.GONE);
				vPassword.setVisibility(View.GONE);
				btnSignIn.setText("Forgot Password");
				txtOr.setVisibility(View.GONE);
				loginBtn.setVisibility(View.GONE);
				linRegister.setVisibility(View.GONE);
			}
		});
	}
	
    public void facebookAuthorize() {
        Session session = Session.getActiveSession();
        if (session == null) {
            session = new Session(LoginActivity.this);
            Session.setActiveSession(session);
        }

        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setCallback(callback).setPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends")));
        } else {
            Session.openActiveSession(LoginActivity.this, true, callback);
        }
    }
    
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            facebookAccessToken = session.getAccessToken().toString();
            if (session.getState() == SessionState.OPENED) {
                facebookGetUser();
            }
            if (session.getState() == SessionState.CLOSED_LOGIN_FAILED) {
                if (Session.getActiveSession() != null) {
                    Session.getActiveSession().closeAndClearTokenInformation();
                }
            }
        }

    };
    
    private void facebookGetUser() {
        Session session = Session.getActiveSession();
        //REQUEST
        com.facebook.Request requestUserInfo = com.facebook.Request.newMeRequest(session, new com.facebook.Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, com.facebook.Response response) {
                JSONObject facebookUserInfo = user.getInnerJSONObject();
                try {
                	pDialog.show();
					String url = Referensi.url+"/accFbLogin/"+facebookUserInfo.getString("email")+"/"+Referensi.md5("miles");
	            	System.out.println("FBLOGIN: "+url);
	            	JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			            @Override
			            public void onResponse(JSONObject response) {
			            	System.out.println("FBLOGIN: "+response.toString());
			            	try {
								if (response.getString("status").equalsIgnoreCase("accept")) {
									SharedPreferences.Editor editor = milesPref.edit();
									editor.putString("id", response.getString("id")).commit();
									initLocationManager();
								}
							} catch (JSONException e) {
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
				} catch (JSONException e) {
					// TODO Auto-generated cat ch block
					e.printStackTrace();
					if (Session.getActiveSession() != null)
                        Session.getActiveSession().closeAndClearTokenInformation();
				}
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email");
        requestUserInfo.setParameters(parameters);
        requestUserInfo.executeAsync();
    }
    
    /**
     * Initialize the location manager.
     */
    private void initLocationManager() {
    	// create class object
        gps = new GPSTracker(LoginActivity.this);

        // check if GPS enabled     
        if (gps.canGetLocation()) {
        	getNameCity(gps.getLatitude(), gps.getLongitude());
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
        	startActivity(new Intent(getApplicationContext(), RegionActivity.class));
			finish();
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
            		
            		if (newCity.contains("Jakarta")||newCity.contains("jakarta")) {
            			newCity = "Jakarta";
            		} else if (newCity.contains("Bandung")||newCity.contains("bandung")) {
            			newCity = "Bandung";
            		} else if (newCity.contains("Bali")||newCity.contains("bali")) {
            			newCity = "Bali";
            		} else if (newCity.contains("Surabaya")||newCity.contains("surabaya")) {
            			newCity = "Surabaya";
            		} else if (newCity.contains("Yogyakarta")||newCity.contains("yogyakarta")) {
            			newCity = "Yogyakarta";
            		} else {
            			newCity = "Jakarta";
            		}
            		
            		SharedPreferences.Editor editor = milesPref.edit();
					editor.putString("city", newCity).commit();
            		
            		startActivity(new Intent(getApplicationContext(), HomeActivity.class));
					overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left );
					finish();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					startActivity(new Intent(getApplicationContext(), RegionActivity.class));
					finish();
					e.printStackTrace();
				}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	startActivity(new Intent(getApplicationContext(), RegionActivity.class));
				finish();
            	Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsObjRequest);
	}

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(LoginActivity.this, requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
    }
    
    @Override
	public boolean onKeyUp( int keyCode, KeyEvent event ){
		if( keyCode == KeyEvent.KEYCODE_BACK ) {
			if (btnSignIn.getText().toString().equalsIgnoreCase("Forgot Password")||btnSignIn.getText().toString().equalsIgnoreCase("Change Password")) {
				linPassword.setVisibility(View.VISIBLE);
				txtForgotPassword.setVisibility(View.VISIBLE);
				vPassword.setVisibility(View.VISIBLE);
				btnSignIn.setText("Sign In");
				txtOr.setVisibility(View.VISIBLE);
				loginBtn.setVisibility(View.VISIBLE);
				linRegister.setVisibility(View.VISIBLE);
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyUp( keyCode, event );
	}
}
