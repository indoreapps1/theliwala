package loop.theliwala.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import loop.theliwala.R;
import loop.theliwala.models.OrderDetails;
import loop.theliwala.utilities.FontManager;


/**
 * Created by user on 11/8/2017.
 */

public class OrderHistoryDetailAdapter extends RecyclerView.Adapter<OrderHistoryDetailAdapter.ViewHolder> {

    private OrderDetails[] orderDataList;
    private Context context;
    Typeface medium, regular;

    public OrderHistoryDetailAdapter(Context context, OrderDetails[] orderData) {
        this.context = context;
        this.orderDataList = orderData;
        this.medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        this.regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        holder.quality_item.setText(orderDataList[i].getQuantity() + "Kg");
        holder.nameofdish_item.setText(orderDataList[i].getProductName());
        holder.price_item.setText(orderDataList[i].getUnitPrice() + "");
    }

    @Override
    public int getItemCount() {
        return orderDataList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView quality_item, nameofdish_item, price_item, rupees_icon;


        public ViewHolder(View view) {
            super(view);
            quality_item = (TextView) view.findViewById(R.id.quality_item);
            rupees_icon = (TextView) view.findViewById(R.id.rupees_icon);
            nameofdish_item = (TextView) view.findViewById(R.id.nameofdish_item);
            price_item = (TextView) view.findViewById(R.id.price_item);

            Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
            rupees_icon.setTypeface(materialdesignicons_font);
            rupees_icon.setText(Html.fromHtml("&#xf1af;"));
            quality_item.setTypeface(medium);
            nameofdish_item.setTypeface(medium);
            price_item.setTypeface(medium);
        }
    }
}

