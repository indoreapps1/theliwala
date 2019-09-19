package loop.theliwala.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import loop.theliwala.R;
import loop.theliwala.models.Data;
import loop.theliwala.utilities.FontManager;

public class SelectCityAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Data> cityList;
    public Context mContext;
    private Typeface materialDesignIcons, regular;
    private Boolean addNewAddressFlage;

    public SelectCityAdapter(Context mContext, List<Data> cityList) {
        mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.cityList = cityList;
        this.materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(mContext, "fonts/materialdesignicons-webfont.otf");
        this.regular = FontManager.getFontTypeface(mContext, "fonts/roboto.regular.ttf");
    }
    public SelectCityAdapter(Context mContext, List<Data> list, Boolean addNewAddressFlage) {
        mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.cityList = list;
        this.addNewAddressFlage = addNewAddressFlage;
        this.materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(mContext, "fonts/materialdesignicons-webfont.otf");
        this.regular = FontManager.getFontTypeface(mContext, "fonts/roboto.regular.ttf");
    }

    @Override
    public int getCount() {

        return cityList.size();
    }


    @Override
    public Object getItem(int arg0) {

        return null;
    }

    @Override
    public long getItemId(int arg0) {

        return 0;
    }

    @Override
    public View getView(final int position, View convertview, ViewGroup parent) {
        ViewHolder holder;
        if (convertview == null) {
            convertview = mInflater.inflate(R.layout.state_item, null);
            holder = new ViewHolder();
            {
                holder.cityName = (TextView) convertview.findViewById(R.id.name);
                if (cityList != null) {
                    holder.cityName.setText(cityList.get(position).getCityName());
                    holder.cityName.setTypeface(regular);
                    convertview.setTag(holder);
                }
            }
        } else {
            holder = (ViewHolder) convertview.getTag();
        }
        return convertview;
    }

    public static class ViewHolder {
        TextView cityName;

    }
}
