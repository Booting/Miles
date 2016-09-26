package com.miles.gen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import org.json.JSONArray;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

public class CommentAdapter extends BaseAdapter {
    private static LayoutInflater mInflater = null;
    private Context context;
	private Typeface fontUbuntuL, fontUbuntuB;
	private JSONArray jsonArray;
    private ImageLoader imgLoader;
    private RequestQueue queue;
	private ProgressDialog pDialog;
	private SharedPreferences milesPref;
	private int positionn;
    private View parentt;
	
	private final String COMMENT_ID         = "id";
    private final String COMMENT_PLACE_ID   = "place_id";
    private final String COMMENT_PROFILE_ID = "profile_id";
    private final String COMMENT_TEXT       = "text";
    private final String COMMENT_RATING     = "rating";
    private final String COMMENT_NUMLIKE    = "num_like";
    private final String COMMENT_DATE       = "date";
    private final String COMMENT_NAME       = "name";
    private final String COMMENT_PHOTO      = "photo";
    private final String COMMENT_LIKE       = "like";
    
    private	ArrayList<String> arrayId = new ArrayList<String>(),
    		arrayPlaceId   = new ArrayList<String>(),
    	    arrayProfileId = new ArrayList<String>(),
    		arrayText  	   = new ArrayList<String>(),
    		arrayRating    = new ArrayList<String>(),
    		arrayNumLike   = new ArrayList<String>(),
    		arrayDate      = new ArrayList<String>(),
    	    arrayName      = new ArrayList<String>(),
    	    arrayPhoto     = new ArrayList<String>(),
    	    arrayLike      = new ArrayList<String>();
    
    public CommentAdapter(Context mContext, JSONArray mJsonArray) {
        context     = mContext;
    	milesPref 	= context.getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
        queue 		= Volley.newRequestQueue(context);
        mInflater   = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fontUbuntuL = FontCache.get(context, "Ubuntu-L");
		fontUbuntuB = FontCache.get(context, "Ubuntu-B");
		jsonArray   = mJsonArray;
		imgLoader   = new ImageLoader(mContext);
		
		pDialog = new ProgressDialog(context);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);
		
		for (int i=0; i<jsonArray.length(); i++) {
			arrayId.add(this.jsonArray.optJSONObject(i).optString(COMMENT_ID));
			arrayPlaceId.add(this.jsonArray.optJSONObject(i).optString(COMMENT_PLACE_ID));
			arrayProfileId.add(this.jsonArray.optJSONObject(i).optString(COMMENT_PROFILE_ID));
			arrayText.add(this.jsonArray.optJSONObject(i).optString(COMMENT_TEXT));
			arrayRating.add(this.jsonArray.optJSONObject(i).optString(COMMENT_RATING));
			arrayNumLike.add(this.jsonArray.optJSONObject(i).optString(COMMENT_NUMLIKE));
			arrayDate.add(this.jsonArray.optJSONObject(i).optString(COMMENT_DATE));
			arrayName.add(this.jsonArray.optJSONObject(i).optString(COMMENT_NAME));
			arrayLike.add(this.jsonArray.optJSONObject(i).optString(COMMENT_LIKE));
			arrayPhoto.add(this.jsonArray.optJSONObject(i).optString(COMMENT_PHOTO));
			arrayLike.add(this.jsonArray.optJSONObject(i).optString(COMMENT_LIKE));
        }
    }

    public void refreshData() {
    	super.notifyDataSetChanged();
    }

    public static class ViewHolder {
        public CircleImageView profileImage;
        public TextView txtName, txtDate, txtReview, txtDetail;
        public Button btnFollow;
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

    @SuppressLint({ "InflateParams", "SimpleDateFormat" })
	@Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_comment, null);

            holder = new ViewHolder();
            holder.profileImage = (CircleImageView) convertView.findViewById(R.id.profileImage);
            holder.txtName      = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtDate      = (TextView) convertView.findViewById(R.id.txtDate);
            holder.txtReview    = (TextView) convertView.findViewById(R.id.txtReview);
            holder.txtDetail    = (TextView) convertView.findViewById(R.id.txtDetail);
            holder.btnFollow    = (Button) convertView.findViewById(R.id.btnFollow);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.txtName.setTypeface(fontUbuntuB);
        holder.txtDate.setTypeface(fontUbuntuL);
        holder.txtReview.setTypeface(fontUbuntuL);
        holder.txtDetail.setTypeface(fontUbuntuL);
        
        holder.txtName.setText(arrayName.get(position));
        holder.txtDetail.setText(arrayText.get(position));
        
        /*SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy", Locale.ENGLISH);
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(arrayDate.get(position));
	        holder.txtDate.setText(formatter.format(date));
        } catch (ParseException e) {
    		e.printStackTrace();
    	}*/
        
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.ENGLISH);
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(arrayDate.get(position));
	        holder.txtReview.setText(formatter.format(date));
        } catch (ParseException e) {
    		e.printStackTrace();
    	}
        
        int loader = R.drawable.miles_loader;
        imgLoader.DisplayImage(Referensi.url2+arrayPhoto.get(position).replace(" ", "%20"), loader, holder.profileImage);
        
        if (position%2==0) {
        	holder.btnFollow.setBackgroundResource(R.drawable.circle_follow_blue);
        } else {
        	holder.btnFollow.setBackgroundResource(R.drawable.circle_follow);
        }
        
        /*holder.btnFollow.setTag(position);
        
        positionn    = position;
        View _parent = (View)convertView.getParent();
        parentt      = _parent;
        
        checkFollow(arrayProfileId.get(position));*/
        
        /*holder.btnFollow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pDialog.show();
				String url = Referensi.url+"/follow";
				JSONObject jsObject = new JSONObject();
				try {
					jsObject.put("profile_id", arrayProfileId.get(position));
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
			}
		});*/
        
        holder.profileImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				context.startActivity(new Intent(context.getApplicationContext(), ProfileActivity.class).putExtra("id", arrayProfileId.get(position)));
			}
		});
        
        holder.txtName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				context.startActivity(new Intent(context.getApplicationContext(), ProfileActivity.class).putExtra("id", arrayProfileId.get(position)));
			}
		});

        return convertView;
    }
    
    public void checkFollow(String userId) {
		String url = Referensi.url+"/checkfollow/"+userId+"/"+milesPref.getString("id", "");
		
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	if (parentt != null) {
	            	final ImageView imgFolloww = (ImageView) parentt.findViewWithTag(positionn);
	            	if (response.optString("status").equalsIgnoreCase("yes")) {
	            		imgFolloww.setBackgroundResource(R.drawable.circle_follow_blue);
	                } else {
	                	imgFolloww.setBackgroundResource(R.drawable.circle_follow);
	                }
            	}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { }
        });
        queue.add(jsObjRequest);
	}

}
