package loop.theliwala.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import loop.theliwala.R;
import loop.theliwala.utilities.FontManager;

/**
 * Created by LALIT on 8/14/2017.
 */
public class StoreDetailAdapter extends PagerAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<Integer> store_imageList;
    private Typeface roboto_light;

    public StoreDetailAdapter(Context mContext, ArrayList<Integer> tourImageList) {
        this.mContext = mContext;
        this.store_imageList = tourImageList;
        inflater = LayoutInflater.from(mContext);
        roboto_light = FontManager.getFontTypeface(mContext, "fonts/roboto.light.ttf");
    }
    @Override
    public int getCount() {
        if (store_imageList.size() != 0) {
            return store_imageList.size();
        }
        return 0;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewHolder holder;
        View itemView = null;
        if (itemView == null) {
            holder = new ViewHolder();
            itemView = inflater.inflate(R.layout.store_item, container, false);
            holder.store_image = (ImageView) itemView.findViewById(R.id.store_image);

            itemView.setTag(holder);

        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        //holder.imageView.setImageResource(tourImageList.get(position));
        Picasso.get().load(store_imageList.get(position)).resize(1024, 1204).into(holder.store_image);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public class ViewHolder {
        public ImageView store_image;
        //public TextView tourText1;
    }


}
