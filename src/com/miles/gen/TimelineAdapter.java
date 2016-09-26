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
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miles.referensi.CircleImageView;
import com.miles.referensi.FontCache;
import com.miles.referensi.ImageLoader;
import com.miles.referensi.Referensi;

public class TimelineAdapter extends BaseAdapter {
    private static LayoutInflater mInflater = null;
    private Context context;
	private Typeface fontUbuntuL, fontUbuntuB;
    private ImageLoader imgLoader;
    private Activity activity;
    private TimelineAdapterListener listener;
    private JSONObject response;
    private JSONArray jArrayTimeline;
    private int mCountFollowing, mCountFollowers;
	private SharedPreferences milesPref;
	private String status="";
    
    private final String TIMELINE_ID 	  = "id";
    private final String TIMELINE_TYPE    = "type";
    private final String TIMELINE_TEXT    = "text";
    private final String TIMELINE_PLACEID = "place_id";
    private final String TIMELINE_TIME    = "time";
    private final String TIMELINE_NAME    = "name";
    private final String TIMELINE_PHOTO   = "photo";
    
    private	ArrayList<String> arrayID = new ArrayList<String>(),
    		arrayType    = new ArrayList<String>(),
    		arrayText    = new ArrayList<String>(),
    		arrayPlaceId = new ArrayList<String>(),
    		arrayTime    = new ArrayList<String>(),
    		arrayName    = new ArrayList<String>(),
    	    arrayPhoto   = new ArrayList<String>();
    
    public TimelineAdapter(Context mContext, TimelineAdapterListener mListener, JSONObject jsonObject, JSONArray jsonArray, int countFollowing, int countFollowers, String mStatus) {
        context         = mContext;
        milesPref 	    = context.getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
        mInflater       = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fontUbuntuL     = FontCache.get(context, "Ubuntu-L");
		fontUbuntuB     = FontCache.get(context, "Ubuntu-B");
        imgLoader       = new ImageLoader(mContext);
        activity        = (Activity) context;
        listener	    = mListener;
        response        = jsonObject;
        jArrayTimeline  = jsonArray;
        mCountFollowing = countFollowing; 
        mCountFollowers = countFollowers;
        status          = mStatus;
        
        if (mStatus.equalsIgnoreCase("yes")) {
	        if (jArrayTimeline!=null) {
		        for (int i=0; i<jArrayTimeline.length(); i++) {
		        	arrayID.add(this.jArrayTimeline.optJSONObject(i).optString(TIMELINE_ID));
		        	arrayType.add(this.jArrayTimeline.optJSONObject(i).optString(TIMELINE_TYPE));
		        	arrayText.add(this.jArrayTimeline.optJSONObject(i).optString(TIMELINE_TEXT));
		        	arrayPlaceId.add(this.jArrayTimeline.optJSONObject(i).optString(TIMELINE_PLACEID));
		        	arrayTime.add(this.jArrayTimeline.optJSONObject(i).optString(TIMELINE_TIME));
		        	arrayName.add(this.jArrayTimeline.optJSONObject(i).optString(TIMELINE_NAME));
		        	arrayPhoto.add(this.jArrayTimeline.optJSONObject(i).optString(TIMELINE_PHOTO));
		        }
	        }
        }
    }

    public void refreshData() {
    	for (int i=arrayID.size(); i<this.jArrayTimeline.length(); i++) {
        	arrayID.add(this.jArrayTimeline.optJSONObject(i).optString(TIMELINE_ID));
        	arrayType.add(this.jArrayTimeline.optJSONObject(i).optString(TIMELINE_TYPE));
        	arrayText.add(this.jArrayTimeline.optJSONObject(i).optString(TIMELINE_TEXT));
        	arrayPlaceId.add(this.jArrayTimeline.optJSONObject(i).optString(TIMELINE_PLACEID));
        	arrayTime.add(this.jArrayTimeline.optJSONObject(i).optString(TIMELINE_TIME));
        	arrayName.add(this.jArrayTimeline.optJSONObject(i).optString(TIMELINE_NAME));
        	arrayPhoto.add(this.jArrayTimeline.optJSONObject(i).optString(TIMELINE_PHOTO));
        }
    	super.notifyDataSetChanged();
    }

    public static class ViewHolder {
        public TextView txtKet, txtKett, txtDate, txtDatee, txtComment, txtCountFollowers, txtFollowers, txtCountFollowing, 
        	txtFollowing, txtWhatsUp, txtProfName;
        public ImageView imgPreview, imgBgProfile, imgProfile;
        public LinearLayout linCoFollowers, linCoFollowing, linCheckIn, linAttend, linBeforeFollow;
        public RelativeLayout relProfile, relProfilee;
        public CircleImageView profileImage, profileImagee;
    }

    @Override
    public int getCount() {
    	if (jArrayTimeline==null) {
    		return 1;
    	} else {
    		if (status.equalsIgnoreCase("yes")) {
    			return jArrayTimeline.length();
    		} else {
    			return 1;
    		}
    	}
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
            convertView = mInflater.inflate(R.layout.list_timeline, null);

            holder 		      		 = new ViewHolder();
            holder.profileImage      = (CircleImageView) convertView.findViewById(R.id.profileImage);
            holder.profileImagee     = (CircleImageView) convertView.findViewById(R.id.profileImagee);
            holder.txtKet     		 = (TextView) convertView.findViewById(R.id.txtKet);
            holder.txtKett    		 = (TextView) convertView.findViewById(R.id.txtKett);
            holder.txtDate    		 = (TextView) convertView.findViewById(R.id.txtDate);
            holder.txtDatee   		 = (TextView) convertView.findViewById(R.id.txtDatee);
            holder.txtComment		 = (TextView) convertView.findViewById(R.id.txtComment);
            holder.imgPreview 		 = (ImageView) convertView.findViewById(R.id.imgPreview);
            holder.imgBgProfile      = (ImageView) convertView.findViewById(R.id.imgBgProfile);
            holder.imgProfile		 = (ImageView) convertView.findViewById(R.id.imgProfile);
            holder.linCoFollowers 	 = (LinearLayout) convertView.findViewById(R.id.linCoFollowers);
            holder.txtCountFollowers = (TextView) convertView.findViewById(R.id.txtCountFollowers);  
            holder.txtFollowers	     = (TextView) convertView.findViewById(R.id.txtFollowers);
            holder.txtCountFollowing = (TextView) convertView.findViewById(R.id.txtCountFollowing); 
            holder.txtFollowing	     = (TextView) convertView.findViewById(R.id.txtFollowing);
            holder.txtWhatsUp	     = (TextView) convertView.findViewById(R.id.txtWhatsUp);
            holder.txtProfName	     = (TextView) convertView.findViewById(R.id.txtProfName);
            holder.linCoFollowing    = (LinearLayout) convertView.findViewById(R.id.linCoFollowing);
            holder.relProfile		 = (RelativeLayout) convertView.findViewById(R.id.relProfile);
            holder.relProfilee		 = (RelativeLayout) convertView.findViewById(R.id.relProfilee);
            holder.linCheckIn		 = (LinearLayout) convertView.findViewById(R.id.linCheckIn);
            holder.linAttend		 = (LinearLayout) convertView.findViewById(R.id.linAttend);
            holder.linBeforeFollow   = (LinearLayout) convertView.findViewById(R.id.linBeforeFollow);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        if (position==0) {
        	holder.relProfile.setVisibility(View.VISIBLE);
        	holder.relProfilee.setVisibility(View.VISIBLE);
        } else {
        	holder.relProfile.setVisibility(View.GONE);
        	holder.relProfilee.setVisibility(View.GONE);
        }
        
        int loader = R.drawable.miles_loader;
        
        if (jArrayTimeline==null) {
        	holder.linCheckIn.setVisibility(View.GONE);
        	holder.linAttend.setVisibility(View.GONE);
        	holder.linBeforeFollow.setVisibility(View.VISIBLE);
        } else {
        	if (status.equalsIgnoreCase("yes")) {
	        	if (arrayType.get(position).equalsIgnoreCase("check_in") || arrayType.get(position).equalsIgnoreCase("review")) {
	            	holder.linCheckIn.setVisibility(View.VISIBLE);
	            	holder.linAttend.setVisibility(View.GONE);
	                imgLoader.DisplayImage(Referensi.url2+arrayPhoto.get(position).replace(" ", "%20"), loader, holder.profileImage);
	            } else {
	            	holder.linCheckIn.setVisibility(View.GONE);
	            	holder.linAttend.setVisibility(View.VISIBLE);
	                imgLoader.DisplayImage(Referensi.url2+arrayPhoto.get(position).replace(" ", "%20"), loader, holder.profileImagee);
	            }
        	} else {
				holder.linCheckIn.setVisibility(View.GONE);
            	holder.linAttend.setVisibility(View.GONE);
            	holder.linBeforeFollow.setVisibility(View.VISIBLE);
			}
        }
        
        DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		ViewGroup.LayoutParams params  = holder.imgBgProfile.getLayoutParams();
	    params.width   = (int) (0.42*displaymetrics.widthPixels);
	    params.height  = (int) (0.30*displaymetrics.heightPixels);
	    
	    String newCity = milesPref.getString("city", ""); 
	    if (newCity.contains("Jakarta")||newCity.contains("jakarta")) {
	    	holder.imgBgProfile.setImageResource(R.drawable.bg_jakarta); 
		} else if (newCity.contains("Bandung")||newCity.contains("bandung")) {
			holder.imgBgProfile.setImageResource(R.drawable.bg_bandung);
		} else if (newCity.contains("Bali")||newCity.contains("bali")) {
			holder.imgBgProfile.setImageResource(R.drawable.bg_bali);
		} else if (newCity.contains("Surabaya")||newCity.contains("surabaya")) {
			holder.imgBgProfile.setImageResource(R.drawable.bg_surabaya);
		} else if (newCity.contains("Yogyakarta")||newCity.contains("yogyakarta")) {
			holder.imgBgProfile.setImageResource(R.drawable.bg_yogyakarta);
		} else {
			holder.imgBgProfile.setImageResource(R.drawable.bg_jakarta);
		}
	    
		ViewGroup.LayoutParams params1 = holder.imgProfile.getLayoutParams();
	    params1.width  = (int) (0.80*displaymetrics.widthPixels);
	    params1.height = (int) (0.30*displaymetrics.heightPixels); 
	    
	    holder.imgProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);   
        imgLoader.DisplayImage(Referensi.url2+response.optString("photo").replace(" ", "%20"), loader, holder.imgProfile);
	    
	    ViewGroup.MarginLayoutParams param   = (ViewGroup.MarginLayoutParams) holder.linCoFollowing.getLayoutParams();
        param.setMargins(0, (int) (0.23*displaymetrics.heightPixels), (int) (0.03*displaymetrics.widthPixels), 0);
        ViewGroup.MarginLayoutParams paramss = (ViewGroup.MarginLayoutParams) holder.linCoFollowers.getLayoutParams();
        paramss.setMargins(0, (int) (0.18*displaymetrics.heightPixels), (int) (0.03*displaymetrics.widthPixels), 0);
        
        holder.txtDate.setTypeface(fontUbuntuL);
        holder.txtDatee.setTypeface(fontUbuntuL);
        holder.txtKet.setTypeface(fontUbuntuL);
        holder.txtKett.setTypeface(fontUbuntuL);
        holder.txtComment.setTypeface(fontUbuntuL);
        holder.txtCountFollowers.setTypeface(fontUbuntuB);
        holder.txtFollowers.setTypeface(fontUbuntuL);
        holder.txtCountFollowing.setTypeface(fontUbuntuB);
        holder.txtFollowing.setTypeface(fontUbuntuL);
        holder.txtWhatsUp.setTypeface(fontUbuntuL);
        holder.txtProfName.setTypeface(fontUbuntuB);

        holder.txtProfName.setText(response.optString("first_name")+" "+response.optString("last_name"));
        //holder.txtCountFollowers.setText(response.optString("num_follower"));
        //holder.txtCountFollowing.setText(response.optString("num_invited"));
        holder.txtCountFollowers.setText(""+mCountFollowers);
        holder.txtCountFollowing.setText(""+mCountFollowing);
        
        try {
        	imgLoader.DisplayImage(Referensi.url2+arrayPhoto.get(position).replace(" ", "%20"), loader, holder.imgPreview);
        } catch (Exception e) {
        	imgLoader.DisplayImage("", loader, holder.imgPreview);
		}
        
        try {
        	SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMM yyyy HH:mm a", Locale.ENGLISH);
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(arrayTime.get(position));
                holder.txtDate.setText(formatter.format(date));
    	        holder.txtDatee.setText(formatter.format(date));
            } catch (ParseException e) {
        		e.printStackTrace();
        	}
	        holder.txtKet.setText(arrayName.get(position)+" "+arrayText.get(position));
	        holder.txtKett.setText(arrayPlaceId.get(position));
	        holder.txtComment.setText(arrayName.get(position)+" "+arrayText.get(position));
        } catch (Exception e) {
			// TODO: handle exception
		}
        
        holder.linCoFollowers.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				listener.onLinCoFollowersClicked();
			}
		});
        
        holder.linCoFollowing.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				listener.onLinCoFollowingClicked();
			}
		});
        
        holder.imgProfile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				listener.onProfileImageClicked(Referensi.url2+response.optString("photo").replace(" ", "%20"));
			}
		});
        
        return convertView;
    }
    
    public interface TimelineAdapterListener {
        public void onLinCoFollowersClicked();
        public void onLinCoFollowingClicked();
        public void onProfileImageClicked(String imageUrl);
    }

}
