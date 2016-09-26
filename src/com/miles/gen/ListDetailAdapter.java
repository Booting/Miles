package com.miles.gen;

import java.util.HashMap;
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

public class ListDetailAdapter extends BaseAdapter {
    private static LayoutInflater mInflater = null;
    private Context context;
    private Typeface fontUbuntuL, fontUbuntuB;
    private String name, ket, address, placeId;
    private int width = 0;
    private JSONArray feature;
    private Map<Integer, Integer> map;
    private Activity activity;
    private String status, from;
    private ListDetailAdapterListener listener;
    
    public ListDetailAdapter(Context mContext, ListDetailAdapterListener mListener, String mName, String mKet, String mAddress, JSONArray mFeature, int mWidth, String mStatus, String mPlaceId, String mFrom) {
        context     = mContext;
        activity    = (Activity) mContext;
        mInflater   = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        name        = mName;
        ket		    = mKet;
        fontUbuntuL = FontCache.get(mContext, "Ubuntu-L");
		fontUbuntuB = FontCache.get(mContext, "Ubuntu-B");
		width		= mWidth;
		address     = mAddress;
		feature     = mFeature;
		status		= mStatus;
		placeId     = mPlaceId;
		listener    = mListener;
		from        = mFrom;
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

    @SuppressLint({ "UseSparseArrays", "InflateParams" })
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
        
        if (status.equalsIgnoreCase("event")) {
        	holder.btnPromo.setText("ATTEND");
        } else {
        	holder.btnPromo.setText("CHECK-IN");
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
        holder.txtName.setText(name);
        holder.txtKet.setText("");
        holder.txtAddress.setText(address);
        
        holder.txtKetA.setText("NOW");
        holder.txtKetB.setText("10 AM - 10 PM");
        holder.txtKetA.setVisibility(View.GONE);
        holder.txtKetB.setVisibility(View.GONE);
        
        if (ket.equalsIgnoreCase("") || ket.equalsIgnoreCase("-")) {
        	holder.linTelp.setVisibility(View.GONE);
        } else {
        	holder.txtTelp.setText(ket);
        	holder.linTelp.setVisibility(View.VISIBLE);
        }
        
        if (feature==null || feature.length()==0) {
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
				if (holder.btnPromo.getText().toString().equalsIgnoreCase("ATTEND")) {
					
				} else {
					listener.onCheckInClicked(placeId);
				}
			}
		});
        
        holder.btnCall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (ket.equalsIgnoreCase("") || ket.equalsIgnoreCase("-")) {
					Toast.makeText(activity, "Sorry, No Telp Number!", Toast.LENGTH_SHORT).show();
				} else {
					String uri = "tel:" + ket.trim();
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
				activity.startActivity(new Intent(context, DetailActivity.class)
					.putExtra("Status", status)
					.putExtra("PlaceId", placeId)
					.putExtra("From", from));
			}
		});
        
        return convertView;
    }
    
    public interface ListDetailAdapterListener {
        public void onCheckInClicked(String placeId);
    }
}
