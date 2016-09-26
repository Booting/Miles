package com.miles.gen;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
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

public class FollowAdapter extends BaseAdapter {
    private static LayoutInflater mInflater = null;
    private Context context;
	private Typeface fontUbuntuL, fontUbuntuB;
	private JSONArray jsonArray;
    private ImageLoader imgLoader;
    private String status;
    private RequestQueue queue;
	private ProgressDialog pDialog;
	private SharedPreferences milesPref;

    private final String FOLLOWERS_ID 	  = "follower_id";
    private final String FOLLOWING_ID 	  = "profile_id";
    private final String FOLLOWING_NAME   = "name";
    private final String FOLLOWING_PHOTO  = "photo";
    private final String FOLLOWING_FOLLOW = "follow";
    
    private	ArrayList<String> arrayID = new ArrayList<String>(),
    		arrayName   = new ArrayList<String>(),
    		arrayPhoto  = new ArrayList<String>(),
    	    arrayFollow = new ArrayList<String>();
    
    public FollowAdapter(Context mContext, JSONArray mJsonArray, String mStatus) {
        context     = mContext;
        milesPref 	= context.getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
        queue 		= Volley.newRequestQueue(context);
        mInflater   = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fontUbuntuL = FontCache.get(context, "Ubuntu-L");
		fontUbuntuB = FontCache.get(context, "Ubuntu-B");
		jsonArray   = mJsonArray;
        imgLoader   = new ImageLoader(mContext);
        status      = mStatus;
        
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);
		
		for (int i=0; i<jsonArray.length(); i++) {
			arrayName.add(this.jsonArray.optJSONObject(i).optString(FOLLOWING_NAME));
        	arrayPhoto.add(this.jsonArray.optJSONObject(i).optString(FOLLOWING_PHOTO));
        	if (status.equalsIgnoreCase("Followers")) {
        		arrayFollow.add(this.jsonArray.optJSONObject(i).optString(FOLLOWING_FOLLOW));
        		arrayID.add(this.jsonArray.optJSONObject(i).optString(FOLLOWERS_ID));
        	} else {
        		arrayID.add(this.jsonArray.optJSONObject(i).optString(FOLLOWING_ID));
        	}
        }
    }

    public void refreshData() {
    	super.notifyDataSetChanged();
    }

    public static class ViewHolder {
        public CircleImageView profileImage;
        public TextView txtName, txtDetail, txtStatus;
        public ImageView iconFollow;
        public LinearLayout linFollow;
        public RelativeLayout relParent;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
	@Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_follow, null);

            holder = new ViewHolder();
            holder.profileImage = (CircleImageView) convertView.findViewById(R.id.profileImage);
            holder.txtName      = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtDetail    = (TextView) convertView.findViewById(R.id.txtDetail);
            holder.txtStatus    = (TextView) convertView.findViewById(R.id.txtStatus);
            holder.iconFollow   = (ImageView) convertView.findViewById(R.id.iconFollow);
            holder.linFollow    = (LinearLayout) convertView.findViewById(R.id.linFollow);
            holder.relParent    = (RelativeLayout) convertView.findViewById(R.id.relParent);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.txtName.setTypeface(fontUbuntuB);
        holder.txtDetail.setTypeface(fontUbuntuL);
        holder.txtStatus.setTypeface(fontUbuntuL);
        
        holder.txtName.setText(arrayName.get(position));
        holder.txtDetail.setText(arrayName.get(position));        
        int loaderr = R.drawable.miles_loader;
        imgLoader.DisplayImage(Referensi.url2+arrayPhoto.get(position).replace(" ", "%20"), loaderr, holder.profileImage);
        
        if (status.equalsIgnoreCase("Followers")) {
	        if (arrayFollow.get(position).equalsIgnoreCase("yes")) {
	        	holder.iconFollow.setVisibility(View.GONE);
	        	holder.txtStatus.setText("Unfollow");
	        } else {
	        	holder.iconFollow.setVisibility(View.VISIBLE);
	        	holder.txtStatus.setText("Follow");
	        }
    	} else {
    		holder.iconFollow.setVisibility(View.GONE);
        	holder.txtStatus.setText("Unfollow");
    	}
        
        holder.linFollow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (holder.txtStatus.getText().toString().equalsIgnoreCase("Follow")) {
					pDialog.show();
					String url = Referensi.url+"/follow";
					JSONObject jsObject = new JSONObject();
					try {
						jsObject.put("profile_id", arrayID.get(position));
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
									Toast.makeText(context, "Follow "+ response.optString("status"), Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(context, "Follow "+ response.optString("status"), Toast.LENGTH_SHORT).show();
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
			                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
			            }
			        });
			        queue.add(jsObjRequest);
				} else {
					pDialog.show();
					String url = Referensi.url+"/follow/"+arrayID.get(position)+"/"+milesPref.getString("id", "");
					
					System.out.println("onUnfollowClicked [url]: "+url);
					JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
			            @Override
			            public void onResponse(JSONObject response) {
							System.out.println("onUnfollowClicked [response]: "+response);
			            	try {
								if (response.getString("status").equalsIgnoreCase("success")) {
									Toast.makeText(context, "Unfollow "+ response.optString("status"), Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(context, "Unfollow "+ response.optString("status"), Toast.LENGTH_SHORT).show();
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
			                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
			            }
			        });
			        queue.add(jsObjRequest);
				}
			}
		});
        
        holder.relParent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				context.startActivity(new Intent(context.getApplicationContext(), ProfileActivity.class).putExtra("id", arrayID.get(position)));
			}
		});

        return convertView;
    }

}
