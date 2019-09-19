package loop.theliwala.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import loop.theliwala.R;
import loop.theliwala.database.DbHelper;
import loop.theliwala.models.Data;
import loop.theliwala.models.MyBasket;
import loop.theliwala.utilities.FontManager;

/**
 * Created by LALIT on 8/14/2017.
 */
public class YourOrderAdpater extends RecyclerView.Adapter<YourOrderAdpater.ViewHolder> {
    private List<MyBasket> orderDataList;
    private Context context;
    private boolean isEditDeleteRequired;

    public YourOrderAdpater(Context context, List<MyBasket> categoriesItemsDatas) {
        this.context = context;
        this.orderDataList = categoriesItemsDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.your_order_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int i) {
        // holder.quality_item.setText(String.valueOf(orderDataList.get(i).getQuantity()) + " " + orderDataList.get(i).getUOM());
        if (orderDataList.get(i).getQuantity() != 0) {
            holder.Quantity.setText(String.valueOf(orderDataList.get(i).getQuantity()));
        } else {
            DecimalFormat df = new DecimalFormat("0");
            String value = df.format(orderDataList.get(i).getQuantity());
            holder.Quantity.setText(value);
        }

        holder.nameofdish_item.setText(orderDataList.get(i).getProductName());
        holder.price_item.setText(orderDataList.get(i).getPrice() + "");
    }

    //move to fragment
    public void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public int getItemCount() {
        return orderDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameofdish_item, price_item, delete_icon, rupees_icon, increase_Product, decrement_Product, Quantity;
        LinearLayout layout_action;

        public ViewHolder(View view) {
            super(view);
            delete_icon = (TextView) view.findViewById(R.id.delete_icon);
            rupees_icon = (TextView) view.findViewById(R.id.rupees_icon);
            nameofdish_item = (TextView) view.findViewById(R.id.nameofdish_item);
            price_item = (TextView) view.findViewById(R.id.price_item);
            Typeface regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
            Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
            increase_Product = (TextView) view.findViewById(R.id.increase_Product);
            decrement_Product = (TextView) view.findViewById(R.id.decrement_Product);
            Quantity = (TextView) view.findViewById(R.id.Quantity);
            increase_Product.setTypeface(materialdesignicons_font);
            decrement_Product.setTypeface(materialdesignicons_font);
            increase_Product.setText(Html.fromHtml("&#xf419;"));
            decrement_Product.setText(Html.fromHtml("&#xf377;"));
            delete_icon.setTypeface(materialdesignicons_font);
            delete_icon.setText(Html.fromHtml("&#xf5e8;"));
            rupees_icon.setTypeface(materialdesignicons_font);
            rupees_icon.setText(Html.fromHtml("&#xf1af;"));
            nameofdish_item.setTypeface(regular);
            Quantity.setTypeface(regular);
            price_item.setTypeface(regular);
            layout_action = (LinearLayout) view.findViewById(R.id.layout_action);
            //delete_icon=(TextView) view.findViewById(R.id.delete_icon);
        }
    }
}
