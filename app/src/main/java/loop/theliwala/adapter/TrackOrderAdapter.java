package loop.theliwala.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import loop.theliwala.R;
import loop.theliwala.fragment.OrderDetailFragment;
import loop.theliwala.models.Data;
import loop.theliwala.utilities.FontManager;
import loop.theliwala.utilities.Utility;

/**
 * Created by HP on 20-08-2017.
 */

public class TrackOrderAdapter extends RecyclerView.Adapter<TrackOrderAdapter.ViewHolder> {


    private Context context;
    private Typeface materialDesignIcons, regular, medium;
    //private  String []status;
    private List<Data> trackData;

    public TrackOrderAdapter(Context context, List<Data> trackData) {
        this.context = context;
        this.trackData = trackData;
        this.materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        this.medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        this.regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track_order_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int i) {
        holder.tv_order_number.setText(trackData.get(i).getOrderNumber());
        Date startDateTime = null;
        String sdateTime = null;
        try {
            startDateTime = Utility.toCalendar(trackData.get(i).getOrderTime());
            sdateTime = Utility.convertDate(startDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //get value based on array position Wednesday,15,Jun,6,30,AM,2000,06 index(0,1,2,3,4,5,6,7)
        if (sdateTime != null) {
            String[] startArray = sdateTime.split(",");
            holder.tv_date.setText(startArray[1] + "-" + startArray[2] + "-" + startArray[6]);
        }
        holder.tv_amount.setText(String.valueOf(trackData.get(i).getNetPrice()));
        holder.tv_store_name.setText(trackData.get(i).getStoreName());
        String oStatus = trackData.get(i).getOrderStatus();
        if (oStatus.equalsIgnoreCase("Received")) {
            holder.tv_track_status.setText(oStatus);
            holder.tv_track_status.setTextColor(ContextCompat.getColor(context,R.color.Dim_red));
        }
        if (oStatus.equalsIgnoreCase("Preparing")) {
            holder.tv_track_status.setText(oStatus);
            holder.tv_track_status.setTextColor(ContextCompat.getColor(context,R.color.shop_name));
        }
        if (oStatus.equalsIgnoreCase("Dispatched")) {
            holder.tv_track_status.setText(oStatus);
            holder.tv_track_status.setTextColor(ContextCompat.getColor(context,R.color.dark_green));
        }
        if (oStatus.equalsIgnoreCase("Store Rejected")) {
            holder.tv_track_status.setText(oStatus);
            holder.tv_track_status.setTextColor(ContextCompat.getColor(context,R.color.Dark_red));
        }
        holder.liner_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDetailFragment fragment = OrderDetailFragment.newInstance(trackData.get(i).getOrderId(), trackData.get(i).getOrderNumber(), trackData.get(i).getStoreId(), trackData.get(i).getStoreName());
                moveFragment(fragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trackData.size();
    }


    private void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_amount, tv_store_name, tv_track_status, tv_date, rupee_icon, tv_order_number;
        LinearLayout liner_layout;

        public ViewHolder(View view) {
            super(view);
            tv_amount = (TextView) view.findViewById(R.id.tv_amount);
            tv_store_name = (TextView) view.findViewById(R.id.tv_store_name);
            rupee_icon = (TextView) view.findViewById(R.id.rupee_icon);
            tv_track_status = (TextView) view.findViewById(R.id.tv_track_status);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_order_number = (TextView) view.findViewById(R.id.tv_order_number);
            liner_layout = (LinearLayout) view.findViewById(R.id.liner_layout);

            //set Typeface..............
            rupee_icon.setTypeface(materialDesignIcons);
            rupee_icon.setText(Html.fromHtml("&#xf1af;"));
            tv_amount.setTypeface(medium);
            tv_store_name.setTypeface(regular);
            tv_track_status.setTypeface(medium);
            tv_date.setTypeface(regular);
            tv_order_number.setTypeface(medium);
        }
    }
}

