package loop.theliwala.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import loop.theliwala.R;
import loop.theliwala.database.DbHelper;
import loop.theliwala.models.Data;
import loop.theliwala.models.MyBasket;
import loop.theliwala.utilities.FontManager;


/**
 * Created by LALIT on 9/7/2017.
 */

public class BasketInnerAdapter extends RecyclerView.Adapter<BasketInnerAdapter.ViewHolder> {
    private List<MyBasket> basketItemdata;
    private Context context;
    private Typeface materialDesignIcons, medium, regular, bold;
    private List<Data> storeData;
    private int storePosition;
    private int storeId;
    private int CategoryId;

    public BasketInnerAdapter(Context context, List<MyBasket> basketItemdata) {
        this.context = context;
        this.basketItemdata = basketItemdata;
        this.storeData = storeData;
        this.storePosition = storePosition;
        this.storeId = storeId;
        this.CategoryId = CategoryId;
        this.medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        this.regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        this.bold = FontManager.getFontTypeface(context, "fonts/roboto.bold.ttf");
        this.materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_basket_inner, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.Quantity.setText(String.valueOf(basketItemdata.get(i).getQuantity()));
        viewHolder.item_name.setText(basketItemdata.get(i).getProductName());
        viewHolder.itemPrice.setText(String.valueOf(basketItemdata.get(i).getPrice()));
        viewHolder.increase_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float count = basketItemdata.get(i).getQuantity();
                basketItemdata.get(i).setQuantity((float) (count + 0.5));
                addProduct(i, true);
                notifyDataSetChanged();
            }
        });
        viewHolder.decrement_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float count = basketItemdata.get(i).getQuantity();
                if (count > 0.5) {
                    basketItemdata.get(i).setQuantity((float) (count - 0.5));
                    addProduct(i, false);
                }
                notifyDataSetChanged();
            }
        });
        viewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbHelper dbHelper = new DbHelper(context);
                dbHelper.deleteBasketOrderDataByProductId(basketItemdata.get(i).getProductId());
                basketItemdata.remove(i);
                if (basketItemdata.size() == 0) {//delete store data if all item deleted from this store
//                    dbHelper.deleteSelectedStoreById(storeId);
//                    storeData.remove(storePosition);
//                    if (storeData.size() == 0) {
                    ((FragmentActivity) context).getSupportFragmentManager().popBackStack();//back to profile screen
                } else {
                    Intent myIntent = new Intent("basketItem");
                    myIntent.putExtra("basketFlag", true);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                }
//                }
                notifyDataSetChanged();
            }
        });
    }

    //add product in database
    private void addProduct(int position, Boolean orderAddOrDelete) {
        float Quantity = 0;
        DbHelper dbHelper = new DbHelper(context);
        MyBasket myBasket = new MyBasket();
        myBasket.setProductId(basketItemdata.get(position).getProductId());
        String productName = basketItemdata.get(position).getProductName();
        myBasket.setProductName(productName);
        myBasket.setStoreId(storeId);
        myBasket.setCategoryId(CategoryId);
        Quantity = basketItemdata.get(position).getQuantity();
        myBasket.setQuantity(Quantity);
        myBasket.setPrice(basketItemdata.get(position).getPrice());
        myBasket.setDiscount(basketItemdata.get(position).getDiscount());
        String UOM = basketItemdata.get(position).getUOM();
        myBasket.setUOM(UOM);
        if (Quantity != 0) {
            dbHelper.upsertBasketOrderData(myBasket);
            Data storeData = dbHelper.getStoreData(storeId);//get store details
            if (storeData != null) {
                storeData.setCategoryId(CategoryId);
                dbHelper.upsertSelectedStoreData(storeData);
            }
        } else
            dbHelper.deleteBasketOrderDataByProductId(basketItemdata.get(position).getProductId());//delete item
        notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        return basketItemdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Quantity, item_name, itemPrice, tv_delete, icon_rupees, decrement_Product, increase_Product;

        public ViewHolder(View view) {
            super(view);
            Quantity = (TextView) view.findViewById(R.id.Quantity);
            item_name = (TextView) view.findViewById(R.id.item_name);
            itemPrice = (TextView) view.findViewById(R.id.itemPrice);
            tv_delete = (TextView) view.findViewById(R.id.tv_delete);
            icon_rupees = (TextView) view.findViewById(R.id.icon_rupees);
            increase_Product = (TextView) view.findViewById(R.id.increase_Product);
            decrement_Product = (TextView) view.findViewById(R.id.decrement_Product);
            increase_Product.setTypeface(materialDesignIcons);
            decrement_Product.setTypeface(materialDesignIcons);
            increase_Product.setText(Html.fromHtml("&#xf419;"));
            decrement_Product.setText(Html.fromHtml("&#xf377;"));
            tv_delete.setTypeface(materialDesignIcons);
            icon_rupees.setTypeface(materialDesignIcons);
            tv_delete.setText(Html.fromHtml("&#xf156;"));
            icon_rupees.setText(Html.fromHtml("&#xf1af;"));
            Quantity.setTypeface(regular);
            item_name.setTypeface(regular);
            itemPrice.setTypeface(regular);
        }
    }
}

