package com.miles.gen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import com.miles.referensi.FontCache;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EventDetailAdapter extends BaseAdapter {
    private static LayoutInflater mInflater = null;
    private Context context;
    private Typeface fontUbuntuL, fontUbuntuB;
    private int width = 0;
    private JSONArray feature, jsonArray;
    private Map<Integer, Integer> map;
    private Activity activity;
    private String from;
    private EventDetailAdapterListener listener;
    private int newPosition=0;
    
    private final String PRODUCT_ID       = "id";
    private final String PRODUCT_NAME     = "name";
    private final String PRODUCT_CP       = "cp";
    private final String PRODUCT_WEBSITE  = "website";
    private final String PRODUCT_HOST     = "host";
    private final String PRODUCT_STARTT   = "start_time";
    private final String PRODUCT_ENDT     = "end_time";
    private final String PRODUCT_DESC     = "description";
    private final String PRODUCT_MEMBERS  = "membership";
    private final String PRODUCT_PHOTO    = "photo";
    private final String PRODUCT_DRESSCO  = "dresscode";
    private final String PRODUCT_PRICE    = "price";
    private final String PRODUCT_TYPE     = "type";
    private final String PRODUCT_LOCATION = "location";
    private final String PRODUCT_ADDRESS  = "address";
    private final String PRODUCT_RANK     = "rank";
    private final String PRODUCT_PROMO    = "promo";
    
    private	ArrayList<String> arrayId = new ArrayList<String>(),
    		arrayName     = new ArrayList<String>(),
    	    arrayCp       = new ArrayList<String>(),
    		arrayWebsite  = new ArrayList<String>(),
    		arrayHost     = new ArrayList<String>(),
    		arrayStartt   = new ArrayList<String>(),
    		arrayEndt     = new ArrayList<String>(),
    	    arrayDesc     = new ArrayList<String>(),
    	    arrayMembers  = new ArrayList<String>(),
    	    arrayPhoto    = new ArrayList<String>(),
    	    arrayDressco  = new ArrayList<String>(),
    	    arrayPrice    = new ArrayList<String>(),
    	    arrayType     = new ArrayList<String>(),
    	    arrayLocation = new ArrayList<String>(),
    	    arrayAddress  = new ArrayList<String>(),
    	    arrayRank     = new ArrayList<String>(),
    	    arrayPromo    = new ArrayList<String>();
    
    public EventDetailAdapter(Context mContext, EventDetailAdapterListener mListener, JSONArray mJsonArray, int mWidth, int mPosition, String mFrom) {
        context     = mContext;
        activity    = (Activity) mContext;
        mInflater   = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fontUbuntuL = FontCache.get(mContext, "Ubuntu-L");
		fontUbuntuB = FontCache.get(mContext, "Ubuntu-B");
		width		= mWidth;
		listener    = mListener;
		jsonArray   = mJsonArray;
		newPosition = mPosition;
		from        = mFrom;
		
		for (int i=0; i<jsonArray.length(); i++) {
			arrayId.add(this.jsonArray.optJSONObject(i).optString(PRODUCT_ID));
			arrayName.add(this.jsonArray.optJSONObject(i).optString(PRODUCT_NAME));
			arrayCp.add(this.jsonArray.optJSONObject(i).optString(PRODUCT_CP));
			arrayWebsite.add(this.jsonArray.optJSONObject(i).optString(PRODUCT_WEBSITE));
			arrayHost.add(this.jsonArray.optJSONObject(i).optString(PRODUCT_HOST));
			arrayStartt.add(this.jsonArray.optJSONObject(i).optString(PRODUCT_STARTT));
			arrayEndt.add(this.jsonArray.optJSONObject(i).optString(PRODUCT_ENDT));
			arrayDesc.add(this.jsonArray.optJSONObject(i).optString(PRODUCT_DESC));
			arrayMembers.add(this.jsonArray.optJSONObject(i).optString(PRODUCT_MEMBERS));
			arrayPhoto.add(this.jsonArray.optJSONObject(i).optString(PRODUCT_PHOTO));
			arrayDressco.add(this.jsonArray.optJSONObject(i).optString(PRODUCT_DRESSCO));
			arrayPrice.add(this.jsonArray.optJSONObject(i).optString(PRODUCT_PRICE));
			arrayType.add(this.jsonArray.optJSONObject(i).optString(PRODUCT_TYPE));
			arrayLocation.add(this.jsonArray.optJSONObject(i).optString(PRODUCT_LOCATION));
			arrayAddress.add(this.jsonArray.optJSONObject(i).optString(PRODUCT_ADDRESS));
			arrayRank.add(this.jsonArray.optJSONObject(i).optString(PRODUCT_RANK));
			arrayPromo.add(this.jsonArray.optJSONObject(i).optString(PRODUCT_PROMO));
        }
    }
    
    public static class ViewHolder {
        public TextView txtName, txtKet, txtKetA, txtKetB, txtAddress, txtTelp;
        public LinearLayout linName, linDetail, linAction, linTelp, linIconA, linIconB, linIconC; 
        public Button btnPromo, btnCall, btnDetail;
        public ImageView imgIconA, imgIconB, imgIconC, imgIconD, imgIconE, imgIconF, imgIconG, imgIconH, imgIconI;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({ "UseSparseArrays", "InflateParams", "SimpleDateFormat" })
	@Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_detail, null);

            holder 			  = new ViewHolder();
            holder.txtName    = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtKet     = (TextView) convertView.findViewById(R.id.txtKet);
            holder.linName    = (LinearLayout) convertView.findViewById(R.id.linName);
            holder.linDetail  = (LinearLayout) convertView.findViewById(R.id.linDetail);
            holder.linAction  = (LinearLayout) convertView.findViewById(R.id.linAction);
            holder.linTelp    = (LinearLayout) convertView.findViewById(R.id.linTelp);
            holder.linIconA   = (LinearLayout) convertView.findViewById(R.id.linIconA);
            holder.linIconB   = (LinearLayout) convertView.findViewById(R.id.linIconB);
            holder.linIconC   = (LinearLayout) convertView.findViewById(R.id.linIconC);
            holder.txtKetA    = (TextView) convertView.findViewById(R.id.txtKetA);
            holder.txtKetB    = (TextView) convertView.findViewById(R.id.txtKetB);
            holder.txtAddress = (TextView) convertView.findViewById(R.id.txtAddress);
            holder.txtTelp    = (TextView) convertView.findViewById(R.id.txtTelp);
            holder.btnPromo	  = (Button) convertView.findViewById(R.id.btnPromo);
            holder.btnCall	  = (Button) convertView.findViewById(R.id.btnCall);
            holder.btnDetail  = (Button) convertView.findViewById(R.id.btnDetail);
            holder.imgIconA   = (ImageView) convertView.findViewById(R.id.imgIconA);
            holder.imgIconB   = (ImageView) convertView.findViewById(R.id.imgIconB);
            holder.imgIconC   = (ImageView) convertView.findViewById(R.id.imgIconC);
            holder.imgIconD   = (ImageView) convertView.findViewById(R.id.imgIconD);
            holder.imgIconE   = (ImageView) convertView.findViewById(R.id.imgIconE);
            holder.imgIconF   = (ImageView) convertView.findViewById(R.id.imgIconF);
            holder.imgIconG   = (ImageView) convertView.findViewById(R.id.imgIconG);
            holder.imgIconH   = (ImageView) convertView.findViewById(R.id.imgIconH);
            holder.imgIconI   = (ImageView) convertView.findViewById(R.id.imgIconI);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.txtName.setTypeface(fontUbuntuB);
        holder.txtKet.setTypeface(fontUbuntuL);
        holder.txtKetA.setTypeface(fontUbuntuL);
        holder.txtKetB.setTypeface(fontUbuntuL);
        holder.txtAddress.setTypeface(fontUbuntuL);
        holder.txtTelp.setTypeface(fontUbuntuL);
        holder.btnPromo.setTypeface(fontUbuntuL);
        holder.btnCall.setTypeface(fontUbuntuL);
        holder.btnDetail.setTypeface(fontUbuntuL);
        
        holder.btnPromo.setText("ATTEND");
        holder.txtName.setText(arrayName.get(newPosition));
        //holder.txtKetA.setText("NOW");
        //holder.txtKetB.setText("10 AM - 10 PM");
        holder.txtAddress.setText(arrayAddress.get(newPosition));
        
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMM yyyy HH:mm a", Locale.ENGLISH);
        try {
            Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(arrayStartt.get(newPosition));
            Date endDate   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(arrayEndt.get(newPosition));
	        holder.txtKetA.setText("Start : "+formatter.format(startDate)+"\nEnd : "+formatter.format(endDate));
        } catch (ParseException e) {
    		e.printStackTrace();
    	}
        
        String dresscode="-", host="-", price="-";
        if (!arrayDressco.get(newPosition).equalsIgnoreCase("")) {
        	dresscode = arrayDressco.get(newPosition);
        }
        if (!arrayHost.get(newPosition).equalsIgnoreCase("null")) {
        	host = arrayHost.get(newPosition);
        }
        if (!arrayPrice.get(newPosition).equalsIgnoreCase("")) {
        	price = arrayPrice.get(newPosition);
        }
        if (!arrayDesc.get(newPosition).equalsIgnoreCase("")) {
        	holder.txtKet.setText(arrayDesc.get(newPosition) + 
	        		"\nDresscode : " + dresscode +
	        		"\nHost : "+host+
	        		"\nPrice : "+price);
        } else {
	        holder.txtKet.setText("Dresscode : " + dresscode +
	        		"\nHost : "+host+
	        		"\nPrice : "+price);
        }
        
        if (arrayCp.get(newPosition).equalsIgnoreCase("") || arrayCp.get(newPosition).equalsIgnoreCase("-")) {
        	holder.linTelp.setVisibility(View.GONE);
        } else {
        	holder.txtTelp.setText(arrayCp.get(newPosition));
        	holder.linTelp.setVisibility(View.VISIBLE);
        }
        
        if (feature==null) {
        	holder.linIconA.setVisibility(View.GONE);
        	holder.linIconB.setVisibility(View.GONE);
        	holder.linIconC.setVisibility(View.GONE);
        } else {
        	if (feature.length()<=3) {
        		holder.linIconB.setVisibility(View.GONE);
        		holder.linIconC.setVisibility(View.GONE);
        	} else if (feature.length()<=6) {
        		holder.linIconC.setVisibility(View.GONE);
        	}
    		map = new HashMap<Integer, Integer>();
        	for (int i=0; i<feature.length(); i++) {
        		if (feature.optString(i).equalsIgnoreCase("eatery")) {
        			map.put(i, R.drawable.icon_a);
        		} else if (feature.optString(i).equalsIgnoreCase("wine")) {
        			map.put(i, R.drawable.icon_b);
        		} else if (feature.optString(i).equalsIgnoreCase("coffee")) {
        			map.put(i, R.drawable.icon_c);
        		} else if (feature.optString(i).equalsIgnoreCase("wifi")) {
        			map.put(i, R.drawable.icon_d);
        		} else if (feature.optString(i).equalsIgnoreCase("beer")) {
        			map.put(i, R.drawable.icon_e);
        		} else if (feature.optString(i).equalsIgnoreCase("liquor")) {
        			map.put(i, R.drawable.icon_f);
        		} else if (feature.optString(i).equalsIgnoreCase("lounge&bar")) {
        			map.put(i, R.drawable.icon_g);
        		} else if (feature.optString(i).equalsIgnoreCase("club")) {
        			map.put(i, R.drawable.icon_h);
        		} else if (feature.optString(i).equalsIgnoreCase("dessert")) {
        			map.put(i, R.drawable.icon_h);
        		} else if (feature.optString(i).equalsIgnoreCase("breakfast")) {
        			map.put(i, R.drawable.icon_h);
        		} else if (feature.optString(i).equalsIgnoreCase("patisserie")) {
        			map.put(i, R.drawable.icon_h);
        		} else if (feature.optString(i).equalsIgnoreCase("hightea")) {
        			map.put(i, R.drawable.icon_h);
        		}
        	}
        	try {
	        	if (map.size()==1) {
	        		holder.imgIconA.setImageResource(map.get(0));
	        	} else if (map.size()==2) {
	        		holder.imgIconA.setImageResource(map.get(0));
	        		holder.imgIconB.setImageResource(map.get(1));
	        	} else if (map.size()==3) {
	        		holder.imgIconA.setImageResource(map.get(0));
	        		holder.imgIconB.setImageResource(map.get(1));
	        		holder.imgIconC.setImageResource(map.get(2));
	        	} else if (map.size()==4) {
	        		holder.imgIconA.setImageResource(map.get(0));
	        		holder.imgIconB.setImageResource(map.get(1));
	        		holder.imgIconC.setImageResource(map.get(2));
	        		holder.imgIconD.setImageResource(map.get(3));
	        	} else if (map.size()==5) {
	        		holder.imgIconA.setImageResource(map.get(0));
	        		holder.imgIconB.setImageResource(map.get(1));
	        		holder.imgIconC.setImageResource(map.get(2));
	        		holder.imgIconD.setImageResource(map.get(3));
	        		holder.imgIconE.setImageResource(map.get(4));
	        	} else if (map.size()==6) {
	        		holder.imgIconA.setImageResource(map.get(0));
	        		holder.imgIconB.setImageResource(map.get(1));
	        		holder.imgIconC.setImageResource(map.get(2));
	        		holder.imgIconD.setImageResource(map.get(3));
	        		holder.imgIconE.setImageResource(map.get(4));
	        		holder.imgIconF.setImageResource(map.get(5));
	        	} else if (map.size()==7) {
	        		holder.imgIconA.setImageResource(map.get(0));
	        		holder.imgIconB.setImageResource(map.get(1));
	        		holder.imgIconC.setImageResource(map.get(2));
	        		holder.imgIconD.setImageResource(map.get(3));
	        		holder.imgIconE.setImageResource(map.get(4));
	        		holder.imgIconF.setImageResource(map.get(5));
	        		holder.imgIconG.setImageResource(map.get(6));
	        	} else if (map.size()==8) {
	        		holder.imgIconA.setImageResource(map.get(0));
	        		holder.imgIconB.setImageResource(map.get(1));
	        		holder.imgIconC.setImageResource(map.get(2));
	        		holder.imgIconD.setImageResource(map.get(3));
	        		holder.imgIconE.setImageResource(map.get(4));
	        		holder.imgIconF.setImageResource(map.get(5));
	        		holder.imgIconG.setImageResource(map.get(6));
	        		holder.imgIconH.setImageResource(map.get(7));
	        	} else if (map.size()==9) {
	        		holder.imgIconA.setImageResource(map.get(0));
	        		holder.imgIconB.setImageResource(map.get(1));
	        		holder.imgIconC.setImageResource(map.get(2));
	        		holder.imgIconD.setImageResource(map.get(3));
	        		holder.imgIconE.setImageResource(map.get(4));
	        		holder.imgIconF.setImageResource(map.get(5));
	        		holder.imgIconG.setImageResource(map.get(6));
	        		holder.imgIconH.setImageResource(map.get(7));
	        		holder.imgIconI.setImageResource(map.get(8));
	        	} 
        	} catch (Exception e) {}
        }
        
        ViewGroup.LayoutParams params  = holder.linName.getLayoutParams();
	    params.width = (int) (0.65*width);
	    ViewGroup.LayoutParams params2 = holder.linDetail.getLayoutParams();
	    params2.width = (int) (0.35*width);
	    
        if (position==0) {
        	holder.linName.setVisibility(View.VISIBLE);
        	holder.linDetail.setVisibility(View.GONE);
        	holder.linAction.setVisibility(View.GONE);
        } else if (position==1) {
        	holder.linDetail.setVisibility(View.VISIBLE);
        	holder.linName.setVisibility(View.GONE);
        	holder.linAction.setVisibility(View.GONE);
        } else if (position==2) {
        	holder.linAction.setVisibility(View.VISIBLE);
        	holder.linDetail.setVisibility(View.GONE);
        	holder.linName.setVisibility(View.GONE);
        }
        
        holder.btnPromo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				listener.onAttendClicked(arrayId.get(newPosition));
			}
		});
        
        holder.btnCall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (arrayCp.get(newPosition).equalsIgnoreCase("") || arrayCp.get(newPosition).equalsIgnoreCase("-")) {
					Toast.makeText(activity, "Sorry, No Telp Number!", Toast.LENGTH_SHORT).show();
				} else {
					String noTelp[] = arrayCp.get(newPosition).split("~");
					String uri = "tel:" + noTelp[1].replace("-", "").trim();
	                Intent intent = new Intent(Intent.ACTION_CALL);
	                intent.setData(Uri.parse(uri));
	                activity.startActivity(intent);
				}
			}
		});
        
        holder.btnDetail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*activity.startActivity(new Intent(context, DetailActivity.class)
					.putExtra("Name", holder.txtName.getText().toString())
					.putExtra("Status", "event")
					.putExtra("Telp", holder.txtTelp.getText().toString()));*/
				activity.startActivity(new Intent(context, DetailActivity.class)
					.putExtra("Status", "Event")
					.putExtra("PlaceId", arrayId.get(newPosition))
					.putExtra("From", from));
			}
		});
        
        return convertView;
    }
    
    public interface EventDetailAdapterListener {
        public void onAttendClicked(String placeId);
    }
}
