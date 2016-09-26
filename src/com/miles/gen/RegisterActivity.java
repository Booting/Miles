package com.miles.gen;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
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
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	private EditText txtEmail, txtPassword, txtConfirmPassword, txtFirstName, txtLastName, txtPhone; 
	private TextView txtSignInOne, txtSignInTwo, txtGender;
	private Typeface fontUbuntuL, fontUbuntuB;
    private Button btnRegister, txtDateOfBirth, btnUploadPhoto;
    private RequestQueue queue;
	private SharedPreferences milesPref;
    private RadioGroup radioSex;
    private RadioButton radioMale, radioFemale, radioSexButton;
    private LinearLayout linEmailPassword, linBiodata, linAvatar;
    private int year, month, day;
    private String profileId, birthDay;
    private CircleImageView imgProfile;
    private ProgressDialog pDialog;
    private int serverResponseCode = 0;
    private String imagepath=null, imagename=null;
    private ImageLoader imgLoader;
    
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        
        if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		
        imgLoader          = new ImageLoader(this);
        milesPref 		   = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
		queue 			   = Volley.newRequestQueue(this);
		fontUbuntuL 	   = FontCache.get(this, "Ubuntu-L");
		fontUbuntuB 	   = FontCache.get(this, "Ubuntu-B");
		txtEmail    	   = (EditText) findViewById(R.id.txtEmail);
		txtPassword        = (EditText) findViewById(R.id.txtPassword);
		txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
		btnRegister		   = (Button) findViewById(R.id.btnRegister);
		txtSignInOne       = (TextView) findViewById(R.id.txtSignInOne);
		txtSignInTwo       = (TextView) findViewById(R.id.txtSignInTwo);
		txtFirstName	   = (EditText) findViewById(R.id.txtFirstName); 
		txtLastName		   = (EditText) findViewById(R.id.txtLastName); 
		txtPhone		   = (EditText) findViewById(R.id.txtPhone);
		txtGender		   = (TextView) findViewById(R.id.txtGender);
		txtDateOfBirth     = (Button) findViewById(R.id.txtDateOfBirth);
		radioMale		   = (RadioButton) findViewById(R.id.radioMale);
		radioFemale		   = (RadioButton) findViewById(R.id.radioFemale);
		linEmailPassword   = (LinearLayout) findViewById(R.id.linEmailPassword);
		linBiodata		   = (LinearLayout) findViewById(R.id.linBiodata);
		linAvatar		   = (LinearLayout) findViewById(R.id.linAvatar);
		imgProfile         = (CircleImageView) findViewById(R.id.imgProfile);
		btnUploadPhoto	   = (Button) findViewById(R.id.btnUploadPhoto);
		radioSex		   = (RadioGroup) findViewById(R.id.radioSex);
		
		txtEmail.setTypeface(fontUbuntuL);
		txtPassword.setTypeface(fontUbuntuL);
		txtConfirmPassword.setTypeface(fontUbuntuL);
		txtSignInOne.setTypeface(fontUbuntuL);
		txtSignInTwo.setTypeface(fontUbuntuB);
		btnRegister.setTypeface(fontUbuntuB);
		txtFirstName.setTypeface(fontUbuntuL); 
		txtLastName.setTypeface(fontUbuntuL);
		txtPhone.setTypeface(fontUbuntuL);
		txtGender.setTypeface(fontUbuntuL);
		txtDateOfBirth.setTypeface(fontUbuntuL);
		radioMale.setTypeface(fontUbuntuL);
		radioFemale.setTypeface(fontUbuntuL);
		btnUploadPhoto.setTypeface(fontUbuntuB);
		
		imgProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);        
        int loader = R.drawable.miles_loader;
        imgLoader.DisplayImage(null, loader, imgProfile);
		
		final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
        
        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage("Loading ....");
		
		txtDateOfBirth.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(999);
			}
		});
		
		btnUploadPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (imagepath==null) {
					Toast.makeText(getBaseContext(), "Photo is Required!", Toast.LENGTH_SHORT).show();
				} else {
					//pDialog.show();
					//uploadFile(imagepath, profileId);
					new UploadImage().execute();
				}
			}
		});
		
		btnRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (btnRegister.getText().toString().equalsIgnoreCase("Skip")) {
					Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
	                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
	                overridePendingTransition( R.anim.slide_in_right, R.anim.slide_out_right );
					finish();
				} else {
					if (txtEmail.getText().toString().equalsIgnoreCase("") && txtPassword.getText().toString().equalsIgnoreCase("") && 
							txtConfirmPassword.getText().toString().equalsIgnoreCase("")) {
						txtEmail.setError("Email is Required!");
						txtPassword.setError("Password is required!");
						txtConfirmPassword.setError("Confirm Password is Required");
					} else if (txtEmail.getText().toString().equalsIgnoreCase("")) {
						txtEmail.setError("Email is Required!");
					} else if (txtPassword.getText().toString().equalsIgnoreCase("")) {
						txtPassword.setError("Password is required!");
					} else if (txtConfirmPassword.getText().toString().equalsIgnoreCase("")) {
						txtConfirmPassword.setError("Confirm Password is Required");
					} else if (!txtPassword.getText().toString().equalsIgnoreCase(txtConfirmPassword.getText().toString())) {
						txtConfirmPassword.setError("Password not match!");
					} else {
						accExist();
					}
				}
			}
		});
		
		txtSignInTwo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(), LoginActivity.class));
				overridePendingTransition( R.anim.slide_in_right, R.anim.slide_out_right );
				finish();
			}
		});
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		if (id == 999) {
			return new DatePickerDialog(this, myDateListener, year, month, day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			String mMonth = null, mDay = null;
			arg2=arg2+1;
			if (arg2<10) {
				mMonth="0"+arg2;
		    } else {
		    	mMonth=""+arg2;
		    }
			if (arg3<10) {
				mDay="0"+arg3;
		    } else {
		    	mDay=""+arg3;
		    }
			txtDateOfBirth.setText(arg1+"-"+mMonth+"-"+mDay);
			birthDay = arg1+mMonth+mDay;
		}
	};
   
	public void register() {
		String url = Referensi.url+"/user";
		int selectedId = radioSex.getCheckedRadioButtonId();
		radioSexButton = (RadioButton) findViewById(selectedId);
		JSONObject jsObject = new JSONObject();
		try {
			jsObject.put("username", txtEmail.getText().toString());
			jsObject.put("password", Referensi.md5(txtPassword.getText().toString()));
			jsObject.put("first_name", txtFirstName.getText().toString());
			jsObject.put("last_name", txtLastName.getText().toString());
			jsObject.put("birthday", birthDay);
			if (radioSexButton.getText().toString().equalsIgnoreCase("Male")) {
				jsObject.put("sex", "0");
			} else {
				jsObject.put("sex", "1");
			}
			jsObject.put("photo", "");
			jsObject.put("phone", txtPhone.getText().toString());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("register: "+jsObject);
    	JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	System.out.println("register: "+response);
            	try {
					if (response.getString("status").equalsIgnoreCase("success")) {
						profileId = response.getString("id");
						
						linBiodata.setVisibility(View.GONE);
						linAvatar.setVisibility(View.VISIBLE);
						btnRegister.setText("Skip");
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
	
	public void accExist() {
		pDialog.show();
		String url = Referensi.url+"/accExist/"+txtEmail.getText().toString();
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	try {
					if (response.getString("status").equalsIgnoreCase("exist")) {
						txtEmail.setError("Email was exist!");
					} else {
						if (linBiodata.getVisibility()==View.VISIBLE) {
							if (txtFirstName.getText().toString().equalsIgnoreCase("") && txtLastName.getText().toString().equalsIgnoreCase("")
								&& txtPhone.getText().toString().equalsIgnoreCase("") && txtDateOfBirth.getText().toString().equalsIgnoreCase("")) {
								txtFirstName.setError("First Name is Required!");
								txtLastName.setError("Last Name is Required!");
								txtPhone.setError("Phone is Required!");
								txtDateOfBirth.setError("Date of Birth is Required!");
							} else if (txtFirstName.getText().toString().equalsIgnoreCase("")) {
								txtFirstName.setError("First Name is Required!");
							} else if (txtLastName.getText().toString().equalsIgnoreCase("")) {
								txtLastName.setError("Last Name is Required!");
							} else if (txtPhone.getText().toString().equalsIgnoreCase("")) {
								txtPhone.setError("Phone is Required!");
							} else if (txtDateOfBirth.getText().toString().equalsIgnoreCase("")) {
								txtDateOfBirth.setError("Date of Birth is Required!");
							} else {
								cekUser();
							}
						}
						linEmailPassword.setVisibility(View.GONE);
						linBiodata.setVisibility(View.VISIBLE);
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
	
	public void cekUser() {
		pDialog.show();
		String url = Referensi.url+"/user/name/"+txtFirstName.getText().toString();
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	System.out.println("cekUser: "+response);
            	try {
					if (response.getString("status").equalsIgnoreCase("exist")) {
						txtFirstName.setError("Name was Exist!");
					} else if (response.getString("status").equalsIgnoreCase("error")) { 
						//Toast.makeText(getBaseContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
						if (linAvatar.getVisibility()==View.VISIBLE) {
							linBiodata.setVisibility(View.VISIBLE);
							linAvatar.setVisibility(View.GONE);
						}
						register();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	pDialog.dismiss();
            	txtFirstName.setError("Name was Exist!");
            }
        });
        queue.add(jsObjRequest);
	}
	
	public void loadImagefromGallery(View view) {
		Intent intent = new Intent(); 
		intent.setType("image/*"); 
		intent.setAction(Intent.ACTION_GET_CONTENT); 
		startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1); 
    }
 
    // When Image is selected from Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == 1 && resultCode == RESULT_OK) {
            //Bitmap photo = (Bitmap) data.getData().getPath(); 
            //Uri imagename=data.getData();
            Uri selectedImageUri = data.getData();
            imagepath = getPath(selectedImageUri);
            Bitmap bitmap=BitmapFactory.decodeFile(imagepath);
            imgProfile.setImageBitmap(bitmap);
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
        	return uploadFile(imagepath, profileId);
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
                  
                 if (serverResponseCode == 200) {
                     runOnUiThread(new Runnable() {
                          public void run() {
                              uploadPhoto(sourceFile.getName());
                          }
                      });                
                 }    
                  
                 //close the streams //
                 fileInputStream.close();
                 dos.flush();
                 dos.close();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);  
            }       
            return serverResponseCode;
         } // End else block 
    } 
    
    public void uploadPhoto(String filePhoto) {
		String url = Referensi.url+"/user/photo/"+profileId;
		JSONObject jsObject = new JSONObject();
		try {
			jsObject.put("photo", "file_upload/user/"+profileId+"/"+filePhoto);
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
						SharedPreferences.Editor editor = milesPref.edit();
						editor.putString("id", profileId).commit();
						
						Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
		                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
		                overridePendingTransition( R.anim.slide_in_right, R.anim.slide_out_right );
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
    
	@Override
	public boolean onKeyUp( int keyCode, KeyEvent event ){
		if( keyCode == KeyEvent.KEYCODE_BACK ) {
			if (linEmailPassword.getVisibility()==View.VISIBLE) {
				startActivity(new Intent(getApplicationContext(), LoginActivity.class));
				overridePendingTransition( R.anim.slide_in_right, R.anim.slide_out_right );
				finish();
			} else if (linBiodata.getVisibility()==View.VISIBLE) {
				linEmailPassword.setVisibility(View.VISIBLE);
				linBiodata.setVisibility(View.GONE);
			} else if (linAvatar.getVisibility()==View.VISIBLE) {
				//linBiodata.setVisibility(View.VISIBLE);
				//linAvatar.setVisibility(View.GONE);
				//btnRegister.setText("Next");
				Toast.makeText(getBaseContext(), "Can't back, please finish registration with upload your picture", Toast.LENGTH_SHORT).show();
			}
			return true;
		}
		return super.onKeyUp( keyCode, event );
	}
	
}
