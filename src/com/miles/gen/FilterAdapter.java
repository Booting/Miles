package com.miles.gen;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;

import com.miles.gen.ListDetailAdapter.ListDetailAdapterListener;
import com.miles.gridview.DynamicHeightImageView;
import com.miles.referensi.FontCache;
import com.miles.referensi.HorizontalListView;
import com.miles.referensi.ImageLoader;
import com.miles.referensi.LoopViewPager;
import com.miles.referensi.Referensi;
import com.viewpagerindicator.CirclePageIndicator;

public class FilterAdapter extends BaseAdapter {
    private static LayoutInflater mInflater = null;
    private Context context;
    private ImageLoader imgLoader;
    private JSONArray jsonArray;
    private ImagePagerAdapter productImageAdapter;
    @SuppressWarnings("unused")
	private int imagePosition = 0, width = 0;
    private ListDetailAdapter listDetailAdapter;
    @SuppressWarnings("unused")
	private Activity activity;
    private String status, categori, from, to, activityFrom;
    private ListDetailAdapterListener listener;
    private JSONArray jsonAddress, jsonFeature, jsonId, jsonName, jsonPhoto, jsonTelp;
    private Typeface fontUbuntuL;
    private FilterAdapterListener listenerr;
    
    private final String PRODUCT_ADDRESS = "address";
    private final String PRODUCT_FEATURE = "feature";
    private final String PRODUCT_ID      = "id";
    private final String PRODUCT_NAME    = "name";
    private final String PRODUCT_PHOTO   = "photo";
    private final String PRODUCT_TELP    = "telp";
    
    public FilterAdapter(Context mContext, ListDetailAdapterListener mListener, FilterAdapterListener mListenerr, 
    	JSONArray mJsonArray, int mWidth, String mStatus, String mCategori, String mFrom, String mTo, String mActivityFrom) {
        context      = mContext;
        mInflater    = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imgLoader    = new ImageLoader(mContext);
        jsonArray    = mJsonArray;
        width        = mWidth;
        activity     = (Activity) context;
        status       = mStatus;
        categori     = mCategori;
        listener     = mListener;
        listenerr    = mListenerr;
        from         = mFrom;
        to           = mTo;
        fontUbuntuL  = FontCache.get(context, "Ubuntu-L");
        activityFrom = mActivityFrom;
        
        if (jsonArray!=null) {
	        try {
	            jsonAddress = new JSONArray(this.jsonArray.optJSONObject(0).optString(PRODUCT_ADDRESS));
	            jsonFeature = new JSONArray(this.jsonArray.optJSONObject(0).optString(PRODUCT_FEATURE));
	            jsonId      = new JSONArray(this.jsonArray.optJSONObject(0).optString(PRODUCT_ID));
	            jsonName    = new JSONArray(this.jsonArray.optJSONObject(0).optString(PRODUCT_NAME));
	            jsonPhoto   = new JSONArray(this.jsonArray.optJSONObject(0).optString(PRODUCT_PHOTO));
	            jsonTelp    = new JSONArray(this.jsonArray.optJSONObject(0).optString(PRODUCT_TELP));
	        } catch (JSONException e) { }
        }
    }

    public void refreshData() {
    	super.notifyDataSetChanged();
    }

    public static class ViewHolder {
        public LoopViewPager imagePager;
        public CirclePageIndicator cIndicator;
        public HorizontalListView horzontList;
        public TextView txtPrinceRange, txtInterest, txtFrom, txtTo;
        public Button btnDayLife, btnNightLife, btnEvent;
        public LinearLayout linFilter, layoutTabbar;
        public ImageView imgFilter;
        public Spinner spinFrom, spinTo;
        public RelativeLayout relList;
    }

    @Override
    public int getCount() {
    	if (jsonId==null) {
    		return 1;
    	} else {
    		if (jsonId.length()==0) {
	    		return 1;
	    	} else {
	    		return jsonId.length()+1;
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

    @SuppressLint({ "InflateParams", "NewApi" })
	@Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.filter_item, null);

            holder = new ViewHolder();
            holder.imagePager     = (LoopViewPager) convertView.findViewById(R.id.pagerItemImages);
            holder.cIndicator     = (CirclePageIndicator) convertView.findViewById(R.id.indicator);
            holder.horzontList    = (HorizontalListView) convertView.findViewById(R.id.HorizontalListView);
            holder.btnDayLife     = (Button) convertView.findViewById(R.id.btnDayLife);
            holder.btnNightLife   = (Button) convertView.findViewById(R.id.btnNightLife);
            holder.btnEvent       = (Button) convertView.findViewById(R.id.btnEvent);
            holder.imgFilter	  = (ImageView) convertView.findViewById(R.id.imgFilter);
            holder.linFilter	  = (LinearLayout) convertView.findViewById(R.id.linFilter);
            holder.layoutTabbar   = (LinearLayout) convertView.findViewById(R.id.layoutTabbar);
            holder.txtPrinceRange = (TextView) convertView.findViewById(R.id.txtPrinceRange);
            holder.txtInterest	  = (TextView) convertView.findViewById(R.id.txtInterest);
            holder.txtFrom		  = (TextView) convertView.findViewById(R.id.txtFrom);
            holder.txtTo		  = (TextView) convertView.findViewById(R.id.txtTo);
            holder.spinFrom	      = (Spinner) convertView.findViewById(R.id.spinFrom);
            holder.spinTo		  = (Spinner) convertView.findViewById(R.id.spinTo);
            holder.relList        = (RelativeLayout) convertView.findViewById(R.id.relList);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        List<String> categories = new ArrayList<String>();
        List<String> categoriess = new ArrayList<String>();
        
        if (position==0) {
        	holder.linFilter.setVisibility(View.VISIBLE);
        	holder.layoutTabbar.setVisibility(View.VISIBLE);
        	holder.relList.setVisibility(View.GONE);
        	
	        holder.btnDayLife.setTypeface(fontUbuntuL);
	        holder.btnNightLife.setTypeface(fontUbuntuL);
	        holder.btnEvent.setTypeface(fontUbuntuL);
	        holder.txtPrinceRange.setTypeface(fontUbuntuL);
	        holder.txtInterest.setTypeface(fontUbuntuL);
	        holder.txtFrom.setTypeface(fontUbuntuL);
	        holder.txtTo.setTypeface(fontUbuntuL);
			
	        //holder.btnDayLife.setText("TOP");
	        //holder.btnNightLife.setText("TRENDING");
	        //holder.btnEvent.setText("NEWEST");
	        holder.btnDayLife.setText("Day Life");
	        holder.btnNightLife.setText("Night Life");
	        holder.btnEvent.setText("Event");
	        
	        if (status.equalsIgnoreCase("day")) {
	        	holder.btnDayLife.setActivated(true);
	        } else if (status.equalsIgnoreCase("night")) {
	        	holder.btnNightLife.setActivated(true);
	        } else {
	        	holder.btnEvent.setActivated(true);
	        }
	        
	        // Spinner Drop down elements
	        categories.add("0");
	        categories.add("25.000");
	        categories.add("50.000");
	        categories.add("100.000");
	        categories.add("200.000");
	        categories.add("400.000");
	        categories.add("800.000");
	        categories.add("more than 1.000.000");
	        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, categories);
	        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        holder.spinFrom.setAdapter(dataAdapter);
	        
	        // Spinner Drop down elements
	        categoriess.add("0");
	        categoriess.add("25.000");
	        categoriess.add("50.000");
	        categoriess.add("100.000");
	        categoriess.add("200.000");
	        categoriess.add("400.000");
	        categoriess.add("800.000");
	        categoriess.add("more than 1.000.000");
	        ArrayAdapter<String> dataAdapterr = new ArrayAdapter<String>(context, R.layout.spinner_item, categoriess);
	        dataAdapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        holder.spinTo.setAdapter(dataAdapterr);
	        
        } else {
        	holder.linFilter.setVisibility(View.GONE);
        	holder.layoutTabbar.setVisibility(View.GONE);
        	holder.relList.setVisibility(View.VISIBLE);
        }
        
        try {
	        listDetailAdapter = new ListDetailAdapter(context, listener, jsonName.optString(position-1), 
	        		jsonTelp.optString(position-1), jsonAddress.optString(position-1), 
	        		jsonFeature.optJSONArray(position-1), width, status,
	        		jsonId.optString(position-1), activityFrom);
	        holder.horzontList.setAdapter(listDetailAdapter);
	        
	        JSONArray jsonArray = new JSONArray();
	        jsonArray.put(jsonPhoto.optString(position-1));
	        
	        productImageAdapter = new ImagePagerAdapter(jsonArray, jsonPhoto.optString(position-1));
	        holder.imagePager.setAdapter(productImageAdapter);
	        holder.cIndicator.setViewPager(holder.imagePager);
	        holder.cIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
	            @Override
	            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
	            @Override
	            public void onPageSelected(int position) {
	                imagePosition = position;
	            }
	            @Override
	            public void onPageScrollStateChanged(int state) { }
	        });
        } catch (Exception e) {
			// TODO: handle exception
		}
        
        holder.btnDayLife.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				holder.btnDayLife.setActivated(true);
				holder.btnNightLife.setActivated(false);
				holder.btnEvent.setActivated(false);
				listenerr.onFilterClicked(categori, "0", holder.spinFrom.getSelectedItem().toString(), holder.spinTo.getSelectedItem().toString());
			}
		});
        
        holder.btnNightLife.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				holder.btnNightLife.setActivated(true);
				holder.btnDayLife.setActivated(false);
				holder.btnEvent.setActivated(false);
				listenerr.onFilterClicked(categori, "1", holder.spinFrom.getSelectedItem().toString(), holder.spinTo.getSelectedItem().toString());
			}
		});
        
        holder.btnEvent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				holder.btnEvent.setActivated(true);
				holder.btnDayLife.setActivated(false);
				holder.btnNightLife.setActivated(false);
				listenerr.onFilterClicked(categori, "2", holder.spinFrom.getSelectedItem().toString(), holder.spinTo.getSelectedItem().toString());
			}
		});
        
        for (int i=0; i<categories.size(); i++) {
        	if (from.equalsIgnoreCase(categories.get(i))) {
        		holder.spinFrom.setSelection(i);
        	}
        }
        
        for (int i=0; i<categoriess.size(); i++) {
        	if (to.equalsIgnoreCase(categoriess.get(i))) {
        		holder.spinTo.setSelection(i);
        	}
        }
        
        holder.spinFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        		if (from.equalsIgnoreCase(holder.spinFrom.getSelectedItem().toString())) {
        			
        		} else {
        			String newStatus = "";
        			if (status.equalsIgnoreCase("day")) {
        				newStatus = "0";
        				holder.btnDayLife.setActivated(true);
        				holder.btnNightLife.setActivated(false);
        				holder.btnEvent.setActivated(false);
        			} else if (status.equalsIgnoreCase("night")) {
        				newStatus = "1";
        				holder.btnNightLife.setActivated(true);
        				holder.btnDayLife.setActivated(false);
        				holder.btnEvent.setActivated(false);
        			} else if (status.equalsIgnoreCase("event")) {
        				newStatus = "2";
        				holder.btnEvent.setActivated(true);
        				holder.btnDayLife.setActivated(false);
        				holder.btnNightLife.setActivated(false);
        			}
        			listenerr.onFilterClicked(categori, newStatus, holder.spinFrom.getSelectedItem().toString(), holder.spinTo.getSelectedItem().toString());
        		}
        	}
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        
        holder.spinTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        		if (to.equalsIgnoreCase(holder.spinTo.getSelectedItem().toString())) {
        			
        		} else {
        			String newStatus = "";
        			if (status.equalsIgnoreCase("day")) {
        				newStatus = "0";
        				holder.btnDayLife.setActivated(true);
        				holder.btnNightLife.setActivated(false);
        				holder.btnEvent.setActivated(false);
        			} else if (status.equalsIgnoreCase("night")) {
        				newStatus = "1";
        				holder.btnNightLife.setActivated(true);
        				holder.btnDayLife.setActivated(false);
        				holder.btnEvent.setActivated(false);
        			} else if (status.equalsIgnoreCase("event")) {
        				newStatus = "2";
        				holder.btnEvent.setActivated(true);
        				holder.btnDayLife.setActivated(false);
        				holder.btnNightLife.setActivated(false);
        			}
        			listenerr.onFilterClicked(categori, newStatus, holder.spinFrom.getSelectedItem().toString(), holder.spinTo.getSelectedItem().toString());
        		}
        	}
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        
        return convertView;
    }
    
    private class ImagePagerAdapter extends PagerAdapter {
        private JSONArray jsonProductImages;
        private String images;

        public ImagePagerAdapter(JSONArray jsonProductImages, String mImages) {
            this.jsonProductImages = jsonProductImages;
            this.images = mImages;
        }
        @Override
        public int getCount() {
            return jsonProductImages.length();
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
        	DynamicHeightImageView imageView = new DynamicHeightImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);     
            imageView.setHeightRatio(1.0);
            
            int loader = R.drawable.miles_loader;
            images = images.replace(" ", "%20");
            imgLoader.DisplayImage(Referensi.url2+images, loader, imageView);
            ((ViewPager) container).addView(imageView, 0);
            
            imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent i = new Intent(context, PhotoViewer.class);
		            i.putExtra("image_list", jsonProductImages.toString());
		            i.putExtra("position", position);
		            context.startActivity(i);
				}
			});
            
            return imageView;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }
    
    public interface FilterAdapterListener {
    	public void onFilterClicked(String type, String categori, String priceFrom, String priceTo);
    }

}
