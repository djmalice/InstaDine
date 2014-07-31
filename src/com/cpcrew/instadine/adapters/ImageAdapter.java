/**
 * 
 */
package com.cpcrew.instadine.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.cpcrew.instadine.utils.Utils;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author raji
 *
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mFacebookIds;

    public ImageAdapter(Context c, ArrayList<String> facebookIds) {
        mContext = c;
        mFacebookIds = new ArrayList<String>();
        mFacebookIds = facebookIds;
    }

    public int getCount() {
        return mFacebookIds.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
    	CircularImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new CircularImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8,8,8,8);
            imageView.setMaxHeight(85);
            imageView.setMinimumHeight(85);
            imageView.setBorderWidth(1);
        } else {
            imageView = (CircularImageView) convertView;
        }

        //imageView.setImageResource(mThumbIds[position]);
        ImageLoader imageLoader = ImageLoader.getInstance();
		//populate view with tweet data
		imageLoader.displayImage(Utils.getFacebookImageUrl(mFacebookIds.get(position)),imageView);
        return imageView;
    }

    // references to our images
//    private Integer[] mThumbIds = {
//            R.drawable.ic_group, R.drawable.ic_group,
//            R.drawable.ic_group, R.drawable.ic_group,
//            R.drawable.ic_group, R.drawable.ic_group,
//            R.drawable.ic_group, R.drawable.ic_group,
//    };
}