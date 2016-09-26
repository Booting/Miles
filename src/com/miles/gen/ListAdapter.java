package com.miles.gen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import org.json.JSONArray;
import org.json.JSONException;

import com.miles.gen.ListDetailAdapter.ListDetailAdapterListener;
import com.miles.gridview.DynamicHeightImageView;
import com.miles.referensi.HorizontalListView;
import com.miles.referensi.ImageLoader;
import com.miles.referensi.LoopViewPager;
import com.miles.referensi.Referensi;
import com.viewpagerindicator.CirclePageIndicator;

public class ListAdapter extends BaseAdapter {
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
    private String status, from;
    private ListDetailAdapterListener listener;
    private JSONArray jsonAddress, jsonFeature, jsonId, jsonName, jsonPhoto, jsonTelp;
    
    private final String PRODUCT_ADDRESS = "address";
    private final String PRODUCT_FEATURE = "feature";
    private final String PRODUCT_ID      = "id";
    private final String PRODUCT_NAME    = "name";
    private final String PRODUCT_PHOTO   = "photo";
    private final String PRODUCT_TELP    = "telp";
    
    public ListAdapter(Context mContext, ListDetailAdapterListener mListener, JSONArray mJsonArray, int mWidth, String mStatus, String mFrom) {
        context   = mContext;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imgLoader = new ImageLoader(mContext);
        jsonArray = mJsonArray;
        width     = mWidth;
        activity  = (Activity) context;
        status    = mStatus;
        listener  = mListener;
        from      = mFrom;
        
        try {
            jsonAddress = new JSONArray(this.jsonArray.optJSONObject(0).optString(PRODUCT_ADDRESS));
            jsonFeature = new JSONArray(this.jsonArray.optJSONObject(0).optString(PRODUCT_FEATURE));
            jsonId      = new JSONArray(this.jsonArray.optJSONObject(0).optString(PRODUCT_ID));
            jsonName    = new JSONArray(this.jsonArray.optJSONObject(0).optString(PRODUCT_NAME));
            jsonPhoto   = new JSONArray(this.jsonArray.optJSONObject(0).optString(PRODUCT_PHOTO));
            jsonTelp    = new JSONArray(this.jsonArray.optJSONObject(0).optString(PRODUCT_TELP));
        } catch (JSONException e) { }
    }

    public void refreshData() {
    	super.notifyDataSetChanged();
    }

    public static class ViewHolder {
        public LoopViewPager imagePager;
        public CirclePageIndicator cIndicator;
        public HorizontalListView horzontList;
    }

    @Override
    public int getCount() {
        return jsonId.length();
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
            convertView = mInflater.inflate(R.layout.list_item, null);

            holder = new ViewHolder();
            holder.imagePager  = (LoopViewPager) convertView.findViewById(R.id.pagerItemImages);
            holder.cIndicator  = (CirclePageIndicator) convertView.findViewById(R.id.indicator);
            holder.horzontList = (HorizontalListView) convertView.findViewById(R.id.HorizontalListView);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        listDetailAdapter = new ListDetailAdapter(context, listener, jsonName.optString(position), 
        		jsonTelp.optString(position), jsonAddress.optString(position), 
        		jsonFeature.optJSONArray(position), width, status,
        		jsonId.optString(position), from);
        holder.horzontList.setAdapter(listDetailAdapter);
        
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonPhoto.optString(position));
        
        productImageAdapter = new ImagePagerAdapter(jsonArray, jsonPhoto.optString(position));
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

}
