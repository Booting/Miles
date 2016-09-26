package com.miles.gen;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.miles.referensi.CircleImageView;
import com.miles.referensi.FontCache;
import com.miles.referensi.ImageLoader;
import com.miles.referensi.Referensi;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

public class SettingActivity extends Activity implements OnClickListener {
	private TextView txtName, txtSave, txtMyAccount, txtSendPushFrom, txtSendPushTo, 
		txtNewest, txtTimeline, txtRecomended, txtEmail, txtMakeYourProfile; 
	private Button txtAboutMiles, txtPrivacyPolicy, txtPushNotifications, txtFindFriends, 
		txtEditMyProfile, txtChangePassword, txtLogOut;
	private Button btnBack;
	private Typeface fontUbuntuL, fontUbuntuB;
    private RequestQueue queue;
    private SharedPreferences milesPref;
    private ImageView imgSetting, imgArrawOne, imgArrawTwo, imgACPass;
    @SuppressWarnings("unused")
	private LinearLayout linPushNotifDetail, linSettingMenu, linEditProfileDetail, linChangePassDetail;
    private EditText txtUserName, txtFirstName, txtLastName, txtOldPassword, txtPassword, txtReTypePassword;
    private CircleImageView profileImage;
    private JSONObject jsObject;
    private ImageLoader imgLoader;
    private String imagepath=null, imagename=null;
    private ProgressDialog pDialog;
    
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);

        imgLoader            = new ImageLoader(this);
        milesPref 	      	 = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
		queue 		      	 = Volley.newRequestQueue(this);
        fontUbuntuL       	 = FontCache.get(this, "Ubuntu-L");
		fontUbuntuB       	 = FontCache.get(this, "Ubuntu-B");
		txtName		      	 = (TextView) findViewById(R.id.txtName);
		txtSave		      	 = (TextView) findViewById(R.id.txtSave);
		btnBack			  	 = (Button) findViewById(R.id.btnBack);
		imgSetting		     = (ImageView) findViewById(R.id.imgSetting);
		txtAboutMiles     	 = (Button) findViewById(R.id.txtAboutMiles); 
		txtPrivacyPolicy     = (Button) findViewById(R.id.txtPrivacyPolicy); 
		txtPushNotifications = (Button) findViewById(R.id.txtPushNotifications); 
		txtFindFriends		 = (Button) findViewById(R.id.txtFindFriends); 
		txtMyAccount		 = (TextView) findViewById(R.id.txtMyAccount); 
		txtEditMyProfile     = (Button) findViewById(R.id.txtEditMyProfile); 
		txtChangePassword    = (Button) findViewById(R.id.txtChangePassword);
		txtLogOut		     = (Button) findViewById(R.id.txtLogOut);
		linPushNotifDetail   = (LinearLayout) findViewById(R.id.linPushNotifDetail);
		linSettingMenu		 = (LinearLayout) findViewById(R.id.linSettingMenu);
		linEditProfileDetail = (LinearLayout) findViewById(R.id.linEditProfileDetail);
		linChangePassDetail  = (LinearLayout) findViewById(R.id.linChangePassDetail);
		txtSendPushFrom		 = (TextView) findViewById(R.id.txtSendPushFrom); 
		txtSendPushTo		 = (TextView) findViewById(R.id.txtSendPushTo);
		txtNewest			 = (TextView) findViewById(R.id.txtNewest); 
		txtTimeline			 = (TextView) findViewById(R.id.txtTimeline); 
		txtRecomended		 = (TextView) findViewById(R.id.txtRecomended); 
		txtEmail			 = (TextView) findViewById(R.id.txtEmail); 
		imgArrawOne			 = (ImageView) findViewById(R.id.imgArrawOne);
		imgArrawTwo			 = (ImageView) findViewById(R.id.imgArrawTwo);
		imgACPass		     = (ImageView) findViewById(R.id.imgACPass);
		txtMakeYourProfile	 = (TextView) findViewById(R.id.txtMakeYourProfile);
		txtUserName          = (EditText) findViewById(R.id.txtUserName); 
		txtFirstName		 = (EditText) findViewById(R.id.txtFirstName); 
		txtLastName			 = (EditText) findViewById(R.id.txtLastName); 
		txtOldPassword   	 = (EditText) findViewById(R.id.txtOldPassword); 
		txtPassword			 = (EditText) findViewById(R.id.txtPassword); 
		txtReTypePassword	 = (EditText) findViewById(R.id.txtReTypePassword);
		profileImage		 = (CircleImageView) findViewById(R.id.profileImage);
		
		try {
			jsObject		 = new JSONObject(getIntent().getExtras().getString("Profile"));
			txtFirstName.setText(jsObject.getString("first_name"));
			txtLastName.setText(jsObject.getString("last_name"));
			if (!jsObject.isNull("email")) {
				txtUserName.setText(jsObject.getString("email"));
			}
			profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);        
            int loader = R.drawable.miles_loader;
            imgLoader.DisplayImage(Referensi.url2+jsObject.getString("photo").replace(" ", "%20"), loader, profileImage);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pDialog = new ProgressDialog(SettingActivity.this);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);
		
		imgSetting.setVisibility(View.GONE);
		txtSave.setVisibility(View.VISIBLE);
		txtName.setText("SETTINGS");
		txtName.setTypeface(fontUbuntuL);
		txtSave.setTypeface(fontUbuntuB);
		txtAboutMiles.setTypeface(fontUbuntuL); 
		txtPrivacyPolicy.setTypeface(fontUbuntuL); 
		txtPushNotifications.setTypeface(fontUbuntuL); 
		txtFindFriends.setTypeface(fontUbuntuL); 
		txtMyAccount.setTypeface(fontUbuntuL); 
		txtEditMyProfile.setTypeface(fontUbuntuL); 
		txtChangePassword.setTypeface(fontUbuntuL);
		txtLogOut.setTypeface(fontUbuntuL);
		txtSendPushFrom.setTypeface(fontUbuntuB);
		txtSendPushTo.setTypeface(fontUbuntuB);
		txtNewest.setTypeface(fontUbuntuL);
		txtTimeline.setTypeface(fontUbuntuL);
		txtRecomended.setTypeface(fontUbuntuL);
		txtEmail.setTypeface(fontUbuntuL);
		txtMakeYourProfile.setTypeface(fontUbuntuB);
		txtUserName.setTypeface(fontUbuntuL);
		txtFirstName.setTypeface(fontUbuntuL);
		txtLastName.setTypeface(fontUbuntuL);
		txtOldPassword.setTypeface(fontUbuntuL);
		txtPassword.setTypeface(fontUbuntuL);
		txtReTypePassword.setTypeface(fontUbuntuL);
		
		btnBack.setOnClickListener(this);
		txtPushNotifications.setOnClickListener(this);
		txtEditMyProfile.setOnClickListener(this);
		txtChangePassword.setOnClickListener(this);
		txtSave.setOnClickListener(this);
		txtLogOut.setOnClickListener(this);
	}
	
	@SuppressLint("NewApi")
	@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
            	finish();
            	break;
            case R.id.txtPushNotifications:
            	if (linPushNotifDetail.getVisibility()==View.GONE) {
            		animate(imgArrawOne).rotationBy(90).setDuration(400);
        			linPushNotifDetail.setVisibility(View.VISIBLE);
            	} else {
            		animate(imgArrawOne).rotationBy(-90).setDuration(400);
        			linPushNotifDetail.setVisibility(View.GONE);
            	}
            	if (linEditProfileDetail.getVisibility()==View.VISIBLE) {
            		animate(imgArrawTwo).rotationBy(-90).setDuration(400);
        			linEditProfileDetail.setVisibility(View.GONE);
            	}
            	if (linChangePassDetail.getVisibility()==View.VISIBLE) {
            		animate(imgACPass).rotationBy(-90).setDuration(400);
        			linChangePassDetail.setVisibility(View.GONE);
            	}
            	break;
            case R.id.txtEditMyProfile:
            	if (linEditProfileDetail.getVisibility()==View.GONE) {
            		animate(imgArrawTwo).rotationBy(90).setDuration(400);
            		linEditProfileDetail.setVisibility(View.VISIBLE);
            	} else {
            		animate(imgArrawTwo).rotationBy(-90).setDuration(400);
            		linEditProfileDetail.setVisibility(View.GONE);
            	}
            	if (linPushNotifDetail.getVisibility()==View.VISIBLE) {
            		animate(imgArrawOne).rotationBy(-90).setDuration(400);
            		linPushNotifDetail.setVisibility(View.GONE);
            	}
            	if (linChangePassDetail.getVisibility()==View.VISIBLE) {
            		animate(imgACPass).rotationBy(-90).setDuration(400);
        			linChangePassDetail.setVisibility(View.GONE);
            	}
            	break;
            case R.id.txtChangePassword:
            	if (linChangePassDetail.getVisibility()==View.GONE) {
            		animate(imgACPass).rotationBy(90).setDuration(400);
            		linChangePassDetail.setVisibility(View.VISIBLE);
            	} else {
            		animate(imgACPass).rotationBy(-90).setDuration(400);
            		linChangePassDetail.setVisibility(View.GONE);
            	}
            	if (linPushNotifDetail.getVisibility()==View.VISIBLE) {
            		animate(imgArrawOne).rotationBy(-90).setDuration(400);
            		linPushNotifDetail.setVisibility(View.GONE);
            	}
            	if (linEditProfileDetail.getVisibility()==View.VISIBLE) {
            		animate(imgArrawTwo).rotationBy(-90).setDuration(400);
            		linEditProfileDetail.setVisibility(View.GONE);
            	}
            	break;
            case R.id.txtSave:
            	if (linChangePassDetail.getVisibility()==View.VISIBLE) {
            		if (txtOldPassword.getText().toString().equalsIgnoreCase("")) {
            			txtOldPassword.setError("Old Password is Required!");
            		} else if (txtPassword.getText().toString().equalsIgnoreCase("")) {
            			txtPassword.setError("New Password is Required!");
            		} else if (txtReTypePassword.getText().toString().equalsIgnoreCase("")) {
            			txtReTypePassword.setError("Retype Password is Required!");
            		} else if (!txtPassword.getText().toString().equalsIgnoreCase(txtReTypePassword.getText().toString())) {
            			txtPassword.setError("New Password not match with Retype Password!");
            			txtReTypePassword.setError("Retype Password not match with New Password!");
            		} else {
            			changePassword();
            		}
            	} else {
	            	if (imagepath==null) {
	            		Toast.makeText(getBaseContext(), "Profile image is required!", Toast.LENGTH_SHORT).show();
	            	} else {
	            		new UploadImage().execute();
	            	}
            	}
            	break;
            case R.id.txtLogOut:
            	doLogout();
            	break;
        }
	}
	
	public void changePassword() {
		String url = Referensi.url+"/changePassword";
		JSONObject jsObject = new JSONObject();
		try {
			jsObject.put("profile_id", milesPref.getString("id", ""));
			jsObject.put("oldpassword", Referensi.md5(txtOldPassword.getText().toString()));
			jsObject.put("newpassword", Referensi.md5(txtPassword.getText().toString()));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		System.out.println("changePassword: "+url);
		System.out.println("changePassword: "+jsObject);
    	JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.PUT, url, jsObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	System.out.println("changePassword: "+response);
            	try {
					if (response.getString("status").equalsIgnoreCase("success")) {
						Toast.makeText(SettingActivity.this, "Change Status Success", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getBaseContext(), response.getString("status"), Toast.LENGTH_SHORT).show();
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
	}
	
	public void doLogout() {
		pDialog.show();
		String url = Referensi.url+"/accLogout";
		JSONObject jsObject = new JSONObject();
		try {
			jsObject.put("Id", milesPref.getString("id", ""));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("doLogout: "+jsObject);
    	JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	System.out.println("doLogout: "+response);
            	try {
					if (response.getString("status").equalsIgnoreCase("logout")) {
						SharedPreferences.Editor editor = milesPref.edit();
						editor.putString("id", response.getString("id")).commit();
						
						Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
			    		intent.putExtra("EXIT", true);
			    		startActivity(intent);
			    		finish();
					} else {
						Toast.makeText(getBaseContext(), response.getString("status"), Toast.LENGTH_SHORT).show();
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
	}
	
	public void profileImageClick(View view) {
		Intent intent = new Intent(); 
		intent.setType("image/*"); 
		intent.setAction(Intent.ACTION_GET_CONTENT); 
		startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1); 
    }
	
	// When Image is selected from Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            imagepath = getPath(selectedImageUri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap=BitmapFactory.decodeFile(imagepath, options);
            profileImage.setImageBitmap(bitmap);
    	}
    }
    
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    
    private class UploadImage extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
        }
        @Override
        protected Integer doInBackground(Integer... params) {
        	return uploadFile(imagepath, milesPref.getString("id", ""));
        }
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result==200) {
            	uploadPhoto(imagename);
            } else {
            	pDialog.dismiss();
            }
        }
    }
    
    public int uploadFile(String sourceFileUri, String profileId) {
    	String fileName = sourceFileUri;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;  
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024; 
        final File sourceFile = new File(sourceFileUri); 
        int serverResponseCode = 0;
        imagename = sourceFile.getName();
         
        if (!sourceFile.isFile()) {
        	runOnUiThread(new Runnable() {
                 public void run() {
                	 Toast.makeText(getBaseContext(), "Source File not exist", Toast.LENGTH_SHORT).show();
                 }
             }); 
             return 0;
        } else {
             try {    
                 // open a URL connection to the Servlet
                 FileInputStream fileInputStream = new FileInputStream(sourceFile);
                 URL url = new URL(Referensi.url+"/mobileupload/uploadPhp.php?id"+profileId);
                  
                 // Open a HTTP  connection to  the URL
                 conn = (HttpURLConnection) url.openConnection(); 
                 conn.setDoInput(true); // Allow Inputs
                 conn.setDoOutput(true); // Allow Outputs
                 conn.setUseCaches(false); // Don't use a Cached Copy
                 conn.setRequestMethod("POST");
                 conn.setRequestProperty("Connection", "Keep-Alive");
                 conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                 conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                 conn.setRequestProperty("uploaded_file", fileName); 
                  
                 dos = new DataOutputStream(conn.getOutputStream());
                 dos.writeBytes(twoHyphens + boundary + lineEnd); 
                 dos.writeBytes("Content-Disposition: form-data; name='uploaded_file';filename='"+ fileName + "'" + lineEnd);
                 dos.writeBytes(lineEnd);
        
                 // create a buffer of  maximum size
                 bytesAvailable = fileInputStream.available(); 
                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
                 buffer = new byte[bufferSize];
        
                 // read file and write it into form...
                 bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                    
                 while (bytesRead > 0) {
                	 dos.write(buffer, 0, bufferSize);
                	 bytesAvailable = fileInputStream.available();
                	 bufferSize = Math.min(bytesAvailable, maxBufferSize);
                	 bytesRead = fileInputStream.read(buffer, 0, bufferSize); 
                 }
        
                 // send multipart form data necesssary after file data...
                 dos.writeBytes(lineEnd);
                 dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
        
                 // Responses from the server (code and message)
                 serverResponseCode = conn.getResponseCode();
                 String serverResponseMessage = conn.getResponseMessage();
                 Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
                  
                 //close the streams //
                 fileInputStream.close();
                 dos.flush();
                 dos.close();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SettingActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SettingActivity.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);  
            }       
            return serverResponseCode;
         } // End else block 
    }
    
    public void uploadPhoto(String filePhoto) {
		String url = Referensi.url+"/user/photo/"+milesPref.getString("id", "");
		JSONObject jsObject = new JSONObject();
		try {
			jsObject.put("photo", "file_upload/user/"+filePhoto);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("uploadPhoto: "+jsObject);
		System.out.println("uploadPhoto: "+url);
    	JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.PUT, url, jsObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	System.out.println("uploadPhoto: "+response);
            	try {
					if (response.getString("status").equalsIgnoreCase("success")) {
						Toast.makeText(SettingActivity.this, "Update profile Success", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getBaseContext(), response.getString("status"), Toast.LENGTH_SHORT).show();
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
