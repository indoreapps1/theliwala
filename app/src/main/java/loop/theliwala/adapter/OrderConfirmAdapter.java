package loop.theliwala.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import loop.theliwala.R;
import loop.theliwala.models.MyBasket;
import loop.theliwala.utilities.FontManager;


/**
 * Created by user on 10/26/2017.
 */

public class OrderConfirmAdapter extends RecyclerView.Adapter<OrderConfirmAdapter.ViewHolder> {
    private List<MyBasket> orderDataList;
    private Context context;
    private boolean isEditDeleteRequired;
    private int storeId;

    public OrderConfirmAdapter(Context context, List<MyBasket> categoriesItemsDatas, int storeId) {
        this.context = context;
        this.orderDataList = categoriesItemsDatas;
        this.storeId = storeId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_confirm_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int i) {
        if (orderDataList.get(i).getUOM() != null && orderDataList.get(i).getUOM().equalsIgnoreCase("kg")) {
            holder.quality_item.setText(String.valueOf(orderDataList.get(i).getQuantity()) + " " + orderDataList.get(i).getUOM());
        } else {

            DecimalFormat df = new DecimalFormat("0");
            String value = df.format(orderDataList.get(i).getQuantity());
            holder.quality_item.setText(value + " " + orderDataList.get(i).getUOM());
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
        TextView quality_item, nameofdish_item, price_item, rupees_icon;

        public ViewHolder(View view) {
            super(view);
            quality_item = (TextView) view.findViewById(R.id.quality_item);
            rupees_icon = (TextView) view.findViewById(R.id.rupees_icon);
            nameofdish_item = (TextView) view.findViewById(R.id.nameofdish_item);
            price_item = (TextView) view.findViewById(R.id.price_item);
            Typeface regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
            Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
            rupees_icon.setTypeface(materialdesignicons_font);
            rupees_icon.setText(Html.fromHtml("&#xf1af;"));
            quality_item.setTypeface(regular);
            nameofdish_item.setTypeface(regular);
            price_item.setTypeface(regular);
            //delete_icon=(TextView) view.findViewById(R.id.delete_icon);
        }
    }
}

