package loop.theliwala.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import loop.theliwala.R;
import loop.theliwala.activity.DashboardActivity;
import loop.theliwala.database.DbHelper;
import loop.theliwala.fragment.ProductListFragment;
import loop.theliwala.models.Data;
import loop.theliwala.models.MyBasket;
import loop.theliwala.utilities.FontManager;
import loop.theliwala.utilities.Utility;


/**
 * Created by user on 8/9/2017.
 */

public class BestSellAdapter extends RecyclerView.Adapter<BestSellAdapter.ViewHolder> {

    private List<Data> productData;
    private Context context;
    private int storeId;
    private int categoryId;
    private Typeface materialDesignIcons, medium, italic, regular;
//    ProductListFragment productListFragment;

    public BestSellAdapter(Context context, List<Data> productData) {
        this.context = context;
        this.productData = productData;
        this.medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        this.regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        this.italic = FontManager.getFontTypeface(context, "fonts/roboto.italic.ttf");
        this.materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bestsell_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        Picasso.get().load(productData.get(position).getProductImage()).placeholder(R.drawable.logo).into(viewHolder.image);
        viewHolder.productTitle.setText(productData.get(position).getProductName());
        viewHolder.productprice.setText("\u20B9" + String.valueOf(productData.get(position).getDiscountedPrice()));
        viewHolder.productprice.setPaintFlags(viewHolder.productprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.textView_nos.setText("" + productData.get(position).getCountValue());
        viewHolder.productPrice.setText(String.valueOf(productData.get(position).getPrice()));
        viewHolder.discount.setText(String.valueOf(productData.get(position).getDiscount() + "% Off"));
        viewHolder.increase_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float count = productData.get(position).getCountValue();
                productData.get(position).setCountValue((float) (count + 0.5));
                addItemToCart(position);
                notifyDataSetChanged();
            }
        });
        viewHolder.decrement_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float count = productData.get(position).getCountValue();
                if (count > 0.5) {
                    productData.get(position).setCountValue((float) (count - 0.5));
                }
                addItemToCart(position);
                notifyDataSetChanged();
            }

        });
        checkDataQuntity(viewHolder, position);
        viewHolder.textView_addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToCart(position);
                viewHolder.linearLayout_addSub.setVisibility(View.VISIBLE);
                viewHolder.linearLayout_addSub.setPadding(0, 0, 10, 0);
                viewHolder.addLayout.setVisibility(View.GONE);
                DashboardActivity rootActivity = (DashboardActivity) context;
                if (rootActivity != null) {
                    rootActivity.setItemCart();
                }
            }
        });
    }

    private void checkDataQuntity(BestSellAdapter.ViewHolder viewHolder, int position) {
        DbHelper dbHelper = new DbHelper(context);
        MyBasket myBasket = dbHelper.getBasketOrderData(productData.get(position).getProductId());
        if (myBasket != null) {
            if (myBasket.getQuantity() != 0) {
                viewHolder.linearLayout_addSub.setVisibility(View.VISIBLE);
                viewHolder.linearLayout_addSub.setPadding(0, 0, 10, 0);
                viewHolder.addLayout.setVisibility(View.GONE);
                viewHolder.textView_nos.setText("" + myBasket.getQuantity());
            } else {
                viewHolder.linearLayout_addSub.setVisibility(View.GONE);
                viewHolder.addLayout.setVisibility(View.VISIBLE);
//                dbHelper.deleteBasketOrderDataByProductId(productData.get(position).getProductId());

            }
        }
    }

    // add items in cart.............
    public void addItemToCart(int position) {
        DbHelper dbHelper = new DbHelper(context);
        MyBasket myBasket = new MyBasket();
        myBasket.setProductId(productData.get(position).getProductId());
        String productName = productData.get(position).getProductName();
        myBasket.setProductName(productName);
        myBasket.setCategoryId(categoryId);
        myBasket.setPrice(Float.parseFloat(productData.get(position).getPrice()));
        myBasket.setQuantity(productData.get(position).getCountValue());
        myBasket.setDiscount(productData.get(position).getDiscount());
        if (getCurrentDateTime() != null) {
            myBasket.setOrderTime(getCurrentDateTime());
        }
        dbHelper.upsertBasketOrderData(myBasket);
//        productListFragment.checkBasketExistsDataOrNot();
        notifyDataSetChanged();
    }

    //get current date time
    private String getCurrentDateTime() {
        String dateTimeStr = null;
        Date currentTime = Calendar.getInstance().getTime();
        String dateTime = Utility.convertDate(currentTime);
        String[] startArray = dateTime.split(",");
        //get value based on array position Wednesday,15,Jun,6,30,AM,2000,06 index(0,1,2,3,4,5,6,7)
        dateTimeStr = startArray[1] + "" + startArray[2] + " " + startArray[6];
        return dateTimeStr;
    }

    @Override
    public int getItemCount() {
        return productData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productTitle, productPrice, rupees_icon, productprice, textView_addToCart, discount,
                increase_Product, textView_nos, decrement_Product;
        LinearLayout linearLayout_addSub, addLayout;
        CardView card_view;
        ImageView image;

        public ViewHolder(View view) {
            super(view);
            productTitle = view.findViewById(R.id.productTitle);
            productprice = view.findViewById(R.id.productprice);
            productPrice = view.findViewById(R.id.productPrice);
            discount = view.findViewById(R.id.discount);
            rupees_icon = view.findViewById(R.id.rupees_icon);
            textView_addToCart = view.findViewById(R.id.textView_addToCart);
            card_view = view.findViewById(R.id.card_view);
            textView_nos = view.findViewById(R.id.textView_nos);
            increase_Product = view.findViewById(R.id.increase_Product);
            decrement_Product = view.findViewById(R.id.decrement_Product);
            linearLayout_addSub = view.findViewById(R.id.linearLayout_addSub);
            addLayout = view.findViewById(R.id.addLayout);
            image = view.findViewById(R.id.image);
            productTitle.setTypeface(medium);
            rupees_icon.setTypeface(materialDesignIcons);
            increase_Product.setTypeface(materialDesignIcons);
            decrement_Product.setTypeface(materialDesignIcons);
            rupees_icon.setText(Html.fromHtml("&#xf1af;"));
            increase_Product.setText(Html.fromHtml("&#xf415;"));
            decrement_Product.setText(Html.fromHtml("&#xf5b0;"));
            productprice.setTypeface(regular);
            discount.setTypeface(regular);
            productPrice.setTypeface(medium);
            textView_addToCart.setTypeface(medium);
        }
    }
}
