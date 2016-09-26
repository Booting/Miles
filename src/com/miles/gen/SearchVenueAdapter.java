package com.miles.gen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import com.miles.gridview.DynamicHeightImageView;
import com.miles.referensi.FontCache;
import com.miles.referensi.ImageLoader;
import com.miles.referensi.Referensi;
import java.util.ArrayList;

public class SearchVenueAdapter extends BaseAdapter {

    private final String VENUE_ID 	  	   = "id";
    private final String VENUE_NAME 	   = "name";
    private final String VENUE_LOCATION    = "location";
    private final String VENUE_ADDRESS     = "address";
    private final String VENUE_TELP    	   = "telp";
    private final String VENUE_WEBSITE 	   = "website";
    private final String VENUE_EMAIL 	   = "email";
    private final String VENUE_RATING 	   = "rating";
    private final String VENUE_DAY_LIFE    = "day_life";
    private final String VENUE_CREATE_TIME = "create_time";
    private final String VENUE_PHOTO 	   = "photo";
    private final String VENUE_VISIBILITY  = "visibility";
    private final String VENUE_CITY 	   = "city";
    private final String VENUE_MIN_PRICE   = "min_price";
    private final String VENUE_MAX_PRICE   = "max_price";

    private ArrayList<String> arrayId = new ArrayList<String>(),
            arrayName       = new ArrayList<String>(),
            arrayLocation   = new ArrayList<String>(),
            arrayAddress    = new ArrayList<String>(),
            arrayTelp       = new ArrayList<String>(),
            arrayWebsite 	= new ArrayList<String>(),
            arrayEmail      = new ArrayList<String>(),
            arrayRating     = new ArrayList<String>(),
            arrayDayLife    = new ArrayList<String>(),
            arrayCreateTime = new ArrayList<String>(),
            arrayPhoto      = new ArrayList<String>(),
            arrayVisibility = new ArrayList<String>(),
            arrayCity       = new ArrayList<String>(),
            arrayMinPrice   = new ArrayList<String>(),
            arrayMaxPrice   = new ArrayList<String>();

    private final LayoutInflater mLayoutInflater;
    private ImageLoader imgLoader;
    public JSONArray jsonItemList;
    private Context _context;
    private Typeface fontUbuntuL, fontUbuntuB;
    private Activity activity;
    
    public SearchVenueAdapter(Context context, JSONArray jsonItemList) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.jsonItemList    = jsonItemList;
        _context             = context;
		imgLoader   		 = new ImageLoader(_context);
        fontUbuntuL 		 = FontCache.get(context, "Ubuntu-L");
		fontUbuntuB 		 = FontCache.get(context, "Ubuntu-B");
		activity			 = (Activity) _context;
        
        for (int i = 0; i < this.jsonItemList.length(); i++) {
        	arrayId.add(this.jsonItemList.optJSONObject(i).optString(VENUE_ID));
        	arrayName.add(this.jsonItemList.optJSONObject(i).optString(VENUE_NAME));
        	arrayLocation.add(this.jsonItemList.optJSONObject(i).optString(VENUE_LOCATION));
        	arrayAddress.add(this.jsonItemList.optJSONObject(i).optString(VENUE_ADDRESS));
        	arrayTelp.add(this.jsonItemList.optJSONObject(i).optString(VENUE_TELP));
        	arrayWebsite.add(this.jsonItemList.optJSONObject(i).optString(VENUE_WEBSITE));
        	arrayEmail.add(this.jsonItemList.optJSONObject(i).optString(VENUE_EMAIL));
        	arrayRating.add(this.jsonItemList.optJSONObject(i).optString(VENUE_RATING));
        	arrayDayLife.add(this.jsonItemList.optJSONObject(i).optString(VENUE_DAY_LIFE));
        	arrayCreateTime.add(this.jsonItemList.optJSONObject(i).optString(VENUE_CREATE_TIME));
        	arrayPhoto.add(this.jsonItemList.optJSONObject(i).optString(VENUE_PHOTO));
        	arrayVisibility.add(this.jsonItemList.optJSONObject(i).optString(VENUE_VISIBILITY));
        	arrayCity.add(this.jsonItemList.optJSONObject(i).optString(VENUE_CITY));
        	arrayMinPrice.add(this.jsonItemList.optJSONObject(i).optString(VENUE_MIN_PRICE));
        	arrayMaxPrice.add(this.jsonItemList.optJSONObject(i).optString(VENUE_MAX_PRICE));
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return jsonItemList.length();
    }

    @Override
    public Object getItem(int position) {
        return this.jsonItemList.optString(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_search_venue, parent, false);
            vh = new ViewHolder();

            vh.relItem     = (LinearLayout) convertView.findViewById(R.id.relItem);
            vh.imgItem     = (DynamicHeightImageView) convertView.findViewById(R.id.imgItem);
            vh.txtItemName = (TextView) convertView.findViewById(R.id.txtItemName);
            vh.txtAddress  = (TextView) convertView.findViewById(R.id.txtAddress);
            vh.txtId       = (TextView) convertView.findViewById(R.id.txtId);
            
            vh.txtItemName.setTypeface(fontUbuntuB);
            vh.txtAddress.setTypeface(fontUbuntuL);
            vh.txtId.setTypeface(fontUbuntuL);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.imgItem.setHeightRatio(1.0);
        int loader = R.drawable.miles_loader;
        imgLoader.DisplayImage(Referensi.url2+arrayPhoto.get(position).replace(" ", "%20"), loader, vh.imgItem);
        
        vh.txtItemName.setText(arrayName.get(position));
        vh.txtAddress.setText(arrayAddress.get(position));
        vh.txtId.setText(arrayId.get(position));
        
        vh.relItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				activity.startActivity(new Intent(_context, DetailActivity.class)
					.putExtra("Status", "day")
					.putExtra("PlaceId", arrayId.get(position))
					.putExtra("From", "Home"));
			}
		});
        
        return convertView;
    }

    static class ViewHolder {
    	LinearLayout relItem;
        DynamicHeightImageView imgItem;
        TextView txtItemName;
        TextView txtAddress;
        TextView txtId;
    }

}