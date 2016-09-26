package com.miles.gen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.miles.referensi.CircleImageView;
import com.miles.referensi.FontCache;
import com.miles.referensi.ImageLoader;
import com.miles.referensi.Referensi;

public class SearchPeopleAdapter extends BaseAdapter {
    private static LayoutInflater mInflater = null;
    private Context context;
	private Typeface fontUbuntuL, fontUbuntuB;
	private JSONArray jsonArray;
    private ImageLoader imgLoader;
    
    private final String PEOPLE_ID 	  		 = "id";
    private final String PEOPLE_ACCOUNT_ID 	 = "account_id";
    private final String PEOPLE_FIRST_NAME   = "first_name";
    private final String PEOPLE_LAST_NAME    = "last_name";
    private final String PEOPLE_BIRTHDAY 	 = "birthday";
    private final String PEOPLE_SEX 		 = "sex";
    private final String PEOPLE_PHOTO 		 = "photo";
    private final String PEOPLE_EMAIL 		 = "email";
    private final String PEOPLE_PHONE_NUMBER = "phone_number";
    private final String PEOPLE_NUM_FOLLOWER = "num_follower";
    private final String PEOPLE_NUM_REVIEW   = "num_review";
    private final String PEOPLE_NUM_INVITED  = "num_invited";
    
    private	ArrayList<String> arrayID = new ArrayList<String>(),
    		arrayAccountId   = new ArrayList<String>(),
    		arrayFirstName   = new ArrayList<String>(),
    	    arrayLastName  	 = new ArrayList<String>(),
    	    arrayBirthday  	 = new ArrayList<String>(),
    	    arraySex         = new ArrayList<String>(),
    	    arrayPhoto 		 = new ArrayList<String>(),
    	    arrayEmail  	 = new ArrayList<String>(),
    	    arrayPhoneNumber = new ArrayList<String>(),
    	    arrayNumFollower = new ArrayList<String>(),
    	    arrayNumReview   = new ArrayList<String>(),
    	    arrayNumInvited  = new ArrayList<String>();
    
    public SearchPeopleAdapter(Context mContext, JSONArray mJsonArray) {
        context     = mContext;
        mInflater   = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fontUbuntuL = FontCache.get(context, "Ubuntu-L");
		fontUbuntuB = FontCache.get(context, "Ubuntu-B");
		jsonArray   = mJsonArray;
        imgLoader   = new ImageLoader(mContext);
        
		for (int i=0; i<jsonArray.length(); i++) {
			arrayID.add(this.jsonArray.optJSONObject(i).optString(PEOPLE_ID));
        	arrayAccountId.add(this.jsonArray.optJSONObject(i).optString(PEOPLE_ACCOUNT_ID));
        	arrayFirstName.add(this.jsonArray.optJSONObject(i).optString(PEOPLE_FIRST_NAME));
        	arrayLastName.add(this.jsonArray.optJSONObject(i).optString(PEOPLE_LAST_NAME));
        	arrayBirthday.add(this.jsonArray.optJSONObject(i).optString(PEOPLE_BIRTHDAY));
        	arraySex.add(this.jsonArray.optJSONObject(i).optString(PEOPLE_SEX));
        	arrayPhoto.add(this.jsonArray.optJSONObject(i).optString(PEOPLE_PHOTO));
        	arrayEmail.add(this.jsonArray.optJSONObject(i).optString(PEOPLE_EMAIL));
        	arrayPhoneNumber.add(this.jsonArray.optJSONObject(i).optString(PEOPLE_PHONE_NUMBER));
        	arrayNumFollower.add(this.jsonArray.optJSONObject(i).optString(PEOPLE_NUM_FOLLOWER));
        	arrayNumReview.add(this.jsonArray.optJSONObject(i).optString(PEOPLE_NUM_REVIEW));
        	arrayNumInvited.add(this.jsonArray.optJSONObject(i).optString(PEOPLE_NUM_INVITED));
        }
    }

    public void refreshData() {
    	super.notifyDataSetChanged();
    }

    public static class ViewHolder {
        public CircleImageView profileImage;
        public TextView txtSex, txtName, txtReview, txtDetail;
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

    @SuppressLint({ "InflateParams", "SimpleDateFormat" })
	@Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_search_people, null);

            holder = new ViewHolder();
            holder.profileImage = (CircleImageView) convertView.findViewById(R.id.profileImage);
            holder.txtSex       = (TextView) convertView.findViewById(R.id.txtSex);
            holder.txtName      = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtReview    = (TextView) convertView.findViewById(R.id.txtReview);
            holder.txtDetail    = (TextView) convertView.findViewById(R.id.txtDetail);
            holder.relParent    = (RelativeLayout) convertView.findViewById(R.id.relParent);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtSex.setTypeface(fontUbuntuL);
        holder.txtName.setTypeface(fontUbuntuB);
        holder.txtReview.setTypeface(fontUbuntuL);
        holder.txtDetail.setTypeface(fontUbuntuL);
        
        holder.txtName.setText(arrayFirstName.get(position) + " " + arrayLastName.get(position));
        holder.txtReview.setText(arrayNumReview.get(position) + " Review, " + 
        		arrayNumFollower.get(position) + " Follower, " + arrayNumInvited.get(position) + " Invited");   
        
        String email = "", phone = "", gender = "", birthday = "-";
        if (arrayEmail.get(position).equalsIgnoreCase("null")) {
        	email = "-";
        } else {
        	email = arrayEmail.get(position);
        }
        
        if (arrayPhoneNumber.get(position).equalsIgnoreCase("null")) {
        	phone = "-";
        } else {
        	phone = arrayPhoneNumber.get(position);
        }
        
        if (arraySex.get(position).equalsIgnoreCase("0")) {
        	gender = "FEMALE";
        } else {
        	gender = "MALE";
        }
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(arrayBirthday.get(position));
	        birthday  = formatter.format(date);
        } catch (ParseException e) {
    		e.printStackTrace();
    	}
        
        holder.txtDetail.setText("Email : " + email + "\nPhone : " + phone + "\n" +
        		"Sex : " + gender + "\nBirthday : " + birthday);
        
        int loaderr = R.drawable.miles_loader;
        imgLoader.DisplayImage(Referensi.url2+arrayPhoto.get(position).replace(" ", "%20"), loaderr, holder.profileImage);
        
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
