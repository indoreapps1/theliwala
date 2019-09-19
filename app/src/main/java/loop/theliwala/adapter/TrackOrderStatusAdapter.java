package loop.theliwala.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import loop.theliwala.R;
import loop.theliwala.timelineview.TimelineView;
import loop.theliwala.utilities.FontManager;
import loop.theliwala.utilities.Utility;

/**
 * Created by user on 10/27/2017.
 */
public class TrackOrderStatusAdapter extends RecyclerView.Adapter<TrackOrderStatusAdapter.ViewHolder> {

    //private List<Data> orderDataList;
    private Context context;
    Typeface medium, regular;
    List<String> stringList;
    String orderStatus, orderTime;
    String[] startArray;

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    public TrackOrderStatusAdapter(Context context, String orderStatus, String orderTime, List<String> stringList) {
        this.context = context;
        this.orderStatus = orderStatus;
        this.orderTime = orderTime;
        this.stringList = stringList;
        this.medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        this.regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track_order_status, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        holder.tv_status.setText(stringList.get(i));
        Date startDateTime = null;
        String sdateTime = null;

        try {
            if (orderTime != null) {
                startDateTime = Utility.toCalendar(orderTime);
                sdateTime = Utility.convertDate(startDateTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (sdateTime != null) {
            startArray = sdateTime.split(",");
        }

        if (orderStatus.equalsIgnoreCase("Received")) {
            if (i == 0) {
                holder.timelineView.setStartLine(Color.TRANSPARENT, 0);
                holder.timelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.marker));
                holder.timelineView.setEndLine(ContextCompat.getColor(context, R.color.green),0);
                holder.tv_time.setText("Time:- " + startArray[3] + ":" + startArray[4] + " " + startArray[5]);
            }
        }
        // for preparing........
        if (orderStatus.equalsIgnoreCase("Preparing")) {
            if (i == 0) {
                holder.timelineView.setStartLine(Color.TRANSPARENT, 0);
                holder.timelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.marker));
                holder.timelineView.setEndLine(ContextCompat.getColor(context, R.color.green), 0);
            }
            if (i == 1) {
                holder.timelineView.setStartLine(ContextCompat.getColor(context, R.color.green), 0);
                holder.timelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.marker));
                holder.timelineView.setEndLine(ContextCompat.getColor(context, R.color.green), 0);
                holder.tv_time.setText("Time:- " + startArray[3] + ":" + startArray[4] + " " + startArray[5]);
            }
        }
        // for Dispatched............
        if (orderStatus.equalsIgnoreCase("Dispatched")) {
            if (i == 0) {
                holder.timelineView.setStartLine(Color.TRANSPARENT, 0);
                holder.timelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.marker));
                holder.timelineView.setEndLine(ContextCompat.getColor(context, R.color.green), 0);
            }
            if (i == 1) {
                holder.timelineView.setStartLine(ContextCompat.getColor(context, R.color.green), 0);
                holder.timelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.marker));
                holder.timelineView.setEndLine(ContextCompat.getColor(context, R.color.green), 0);
            }
            if (i == 2) {
                holder.timelineView.setStartLine(ContextCompat.getColor(context, R.color.green), 0);
                holder.timelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.marker));
                holder.timelineView.setEndLine(Color.TRANSPARENT, 0);
                holder.tv_time.setText("Time:- " + startArray[3] + ":" + startArray[4] + " " + startArray[5]);
            }
        }
        // for Delivered.................
//        if (orderStatus.equalsIgnoreCase("Delivered")) {
//            if (i == 0) {
//                holder.timelineView.setStartLine(Color.TRANSPARENT, 0);
//                holder.timelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.marker));
//                holder.timelineView.setEndLine(ContextCompat.getColor(context, R.color.green), 0);
//            }
//            if (i == 1) {
//                holder.timelineView.setStartLine(ContextCompat.getColor(context, R.color.green), 0);
//                holder.timelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.marker));
//                holder.timelineView.setEndLine(ContextCompat.getColor(context, R.color.green), 0);
//            }
//            if (i == 2) {
//                holder.timelineView.setStartLine(ContextCompat.getColor(context, R.color.green), 0);
//                holder.timelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.marker));
//                holder.timelineView.setEndLine(ContextCompat.getColor(context, R.color.green), 0);
//            }
//            if (i == 3) {
//                holder.timelineView.setStartLine(ContextCompat.getColor(context, R.color.green), 0);
//                holder.timelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.marker));
//                holder.timelineView.setEndLine(Color.TRANSPARENT, 0);
//            }
//        }


        if (i == stringList.size() - 1) {
            holder.timelineView.setEndLine(Color.TRANSPARENT, 0);
        }
//        if (orderStatus.equals("Order Recived")) {
//
//        }
//        context.startService(new Intent(context, TrackStatusService.class));
//
//
//        if (orderStatus.equalsIgnoreCase("Store Rejected")) {
//            holder.linearLayout.setVisibility(View.GONE);
//            holder.tv_canceled.setVisibility(View.VISIBLE);
//            holder.tv_canceled.setGravity(Gravity.CENTER);
//            holder.tv_canceled.setTextSize(30);
//            holder.tv_canceled.setTextColor(Color.RED);
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View view = inflater.inflate(R.layout.no_data_found, null);
//            TextView nodataIcon = (TextView) view.findViewById(R.id.nodataIcon);
//            TextView nodata = (TextView) view.findViewById(R.id.nodata);
//            nodata.setTypeface(regular);
//            //nodataIcon.setTypeface(materialdesignicons_font);
//            //nodataIcon.setText(Html.fromHtml("&#xf187;"));
//            nodata.setText("Order Rejected.");
//            nodata.setTextColor(Color.RED);
//            nodata.setTextSize(30);
//            holder.linearLayout.setGravity(Gravity.CENTER);
//            holder.linearLayout.removeAllViews();
//            holder.linearLayout.addView(view);
//        }
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_status, tv_time, tv_date, tv_canceled;
        TimelineView timelineView;
        LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);
            linearLayout = (LinearLayout) view.findViewById(R.id.layout);
            tv_canceled = (TextView) view.findViewById(R.id.tv_canceld);
            tv_status = (TextView) view.findViewById(R.id.tv_status);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            //  tv_date = (TextView) view.findViewById(R.id.tv_date);
            timelineView = (TimelineView) view.findViewById(R.id.time_marker);
            tv_canceled.setTypeface(medium);
            tv_status.setTypeface(medium);
            tv_time.setTypeface(regular);
        }
    }
}