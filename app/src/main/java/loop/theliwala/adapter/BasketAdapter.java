package loop.theliwala.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import loop.theliwala.R;
import loop.theliwala.database.DbHelper;
import loop.theliwala.fragment.UserAddressFragment;
import loop.theliwala.models.Data;
import loop.theliwala.models.MyBasket;
import loop.theliwala.myalert.SweetAlertDialog;
import loop.theliwala.utilities.FontManager;
import loop.theliwala.utilities.Utility;


/**
 * Created by user on 8/14/2017.
 */
public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {
    private List<Data> basketdata;
    private Context context;
    private Typeface materialDesignIcons, medium, regular, bold;
    DbHelper dbHelper;
    Data data;

    public BasketAdapter(Context context, List<Data> basketdata) {
        this.context = context;
        this.basketdata = basketdata;
        this.medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        this.regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        this.bold = FontManager.getFontTypeface(context, "fonts/roboto.bold.ttf");
        this.materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_basket_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        dbHelper = new DbHelper(context);
        data = dbHelper.getStoreData(basketdata.get(i).getStoreId());
        viewHolder.storeName.setText(basketdata.get(i).getStoreName());
        viewHolder.checkOutIcon.setText(Html.fromHtml("&#xf054;"));
        List<MyBasket> orderList = dbHelper.GetAllBasketOrderDataBasedOnStoreId(basketdata.get(i).getStoreId());
        if (orderList != null && orderList.size() > 0) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            viewHolder.recycleView_inner.setLayoutManager(layoutManager);
//            BasketInnerAdapter adapter = new BasketInnerAdapter(context, orderList, basketdata, i, basketdata.get(i).getStoreId(), basketdata.get(i).getCategoryId());
//            viewHolder.recycleView_inner.setAdapter(adapter);
        }


        viewHolder.checkoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.getStoreStatus().equalsIgnoreCase("true")) {
                    Utility.setStoreIDFromSharedPreferences(context, basketdata.get(i).getStoreId());
                    UserAddressFragment fragment = UserAddressFragment.newInstance(true, basketdata.get(i).getStoreId());
                    moveFragment(fragment);
                } else {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Sorry...")
                            .setContentText("Store is Closed")
                            .show();
                    //Utility.alertForErrorMessage("Sorry Store is Closed.", context);
                }
            }
        });
    }

    private void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public int getItemCount() {
        return basketdata.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recycleView_inner;
        TextView storeName, checkOut, checkOutIcon;
        LinearLayout checkoutLayout;

        public ViewHolder(View view) {
            super(view);
            recycleView_inner = (RecyclerView) view.findViewById(R.id.recycleView_inner);
            //   edit = (TextView) view.findViewById(R.id.edit);
            storeName = (TextView) view.findViewById(R.id.storeName);
            checkOut = (TextView) view.findViewById(R.id.checkOut);
            checkOutIcon = (TextView) view.findViewById(R.id.checkOutIcon);
            checkoutLayout = (LinearLayout) view.findViewById(R.id.checkoutLayout);
            checkOut.setTypeface(medium);
            storeName.setTypeface(medium);
            //edit.setTypeface(medium);
            checkOutIcon.setTypeface(materialDesignIcons);
        }
    }

}
