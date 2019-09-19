package loop.theliwala.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import loop.theliwala.R;
import loop.theliwala.fragment.SelectOptionFragment;
import loop.theliwala.activity.LoginActivity;
import loop.theliwala.database.DbHelper;
import loop.theliwala.framework.IAsyncWorkCompletedCallback;
import loop.theliwala.framework.ServiceCaller;
import loop.theliwala.models.Data;
import loop.theliwala.myalert.SweetAlertDialog;
import loop.theliwala.utilities.FontManager;
import loop.theliwala.utilities.Utility;

/**
 * Created by user on 8/9/2017.
 */

public class SelectStoreAdapter extends RecyclerView.Adapter<SelectStoreAdapter.MyViewHolder> {
    Context context;
    private List<Data> list = new ArrayList<>();
    private Typeface materialDesignIcons, medium, regular;
    HashSet<Integer> selectedPosition = new HashSet<>();
    String sdateTime = null, edateTime = null, closeTiminig = null, openingTime = null, openingamorpm = null, closingamorpm = null, openTime = null, closeTime = null;

    public SelectStoreAdapter(Context context, List<Data> list) {
        this.context = context;
        this.list = list;
        this.medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        this.regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        this.materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_store_item_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.store_name.setText(list.get(position).getStoreName());
        Date startDateTime = null, endDateTime = null;

        String[] startArray, endArray;

        try {
            startDateTime = Utility.toCalendar(list.get(position).getOpeningTime());
            endDateTime = Utility.toCalendar(list.get(position).getClosingTime());
            sdateTime = Utility.convertDate(startDateTime);
            edateTime = Utility.convertDate(endDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (sdateTime != null) {
            startArray = sdateTime.split(",");
            openTime = startArray[3] + ":" + startArray[4];
            openingamorpm = startArray[5];
        }
        if (edateTime != null) {
            endArray = edateTime.split(",");
            closeTime = endArray[3] + ":" + endArray[4];
            closingamorpm = endArray[5];

        }
        //if (list.get(position).getStorePicturesUrl() != null) {
        // Picasso.with(context).load(list.get(position).getStorePicturesUrl()).placeholder(R.drawable.logo).into(holder.store_image);
        // }
        if (list.get(position).getStoreAddress() != null) {
            holder.store_address.setText(list.get(position).getStoreAddress());
        } else {
            holder.store_address.setVisibility(View.GONE);
        }
        if (list.get(position).getStoreEmailId() != null) {
            holder.store_email_address.setText(list.get(position).getStoreEmailId());
        } else {
            holder.email_layout.setVisibility(View.GONE);
        }
        if (list.get(position).getStorePhoneNumber() != null) {
            holder.storestore_contact_number.setText(list.get(position).getStorePhoneNumber() + "/");
        } else {
            holder.phone_layout.setVisibility(View.GONE);
        }
        if (list.get(position).getOpeningTime() != null && list.get(position).getClosingTime() != null) {
            holder.store_timing.setText(openTime + openingamorpm + " to " + closeTime + closingamorpm + "/");
        } else {
            holder.store_timing.setVisibility(View.GONE);
        }
        if (list.get(position).getStoreStatus() != null) {
            if (list.get(position).getStoreStatus().equalsIgnoreCase("true")) {
                holder.store_status.setText("Open");
            } else {
                holder.store_status.setText("Closed");
                holder.store_status.setTextColor(ContextCompat.getColor(context, R.color.secondary_text));
            }
        } else {
            holder.store_status.setVisibility(View.GONE);
        }

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).getStoreStatus().equalsIgnoreCase("true")) {
                    Utility.setStoreIDFromSharedPreferences(context, list.get(position).getStoreId());
                    SelectOptionFragment fragment = SelectOptionFragment.newInstance(list.get(position).getStoreId(), "");
                    //list.get(position).getStoreId();
                    moveFragment(fragment);
                } else {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Sorry...")
                            .setContentText("Store is Closed.")
                            .show();
                    //Utility.alertForErrorMessage("Sorry store is closed.", context);
                }

            }
        });
        holder.heart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isOnline(context)) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                    int id = sharedPreferences.getInt("userId", 0);
                    if (id != 0) {
                        if (list.get(position).isFavouriteStore()) {
                            makeUnFavorite(id, position);
                        } else {
                            makeFavorite(id, list.get(position).getStoreId(), position);
                        }
                    } else {
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                } else {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("You are Offline. Please check your Internet Connection.")
                            .show();
                    //Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, context);//offline check..........
                }
            }
        });
        FavouriteOrNOt(holder, position);
    }

    //used for heart icon red color...
    private void FavouriteOrNOt(MyViewHolder holder, int position) {
        if (list.get(position).isFavouriteStore()) {
            holder.heart_icon.setTextColor(Color.RED);
            holder.heart_icon.setText(Html.fromHtml("&#xf2d1;"));
        } else {
            holder.heart_icon.setTextColor(Color.parseColor("#a3a3a3"));
        }
    }

    private void makeFavorite(final int loginID, int storeId, final int position) {
        // final BallTriangleSyncDialog dotDialog = new BallTriangleSyncDialog(context);
        //dotDialog.show();
        ServiceCaller serviceCaller = new ServiceCaller(context);
        serviceCaller.callAddUpdateFavouriteStoreByUserService(loginID, storeId, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    Intent myIntent = new Intent("favorite");
                    myIntent.putExtra("favoriteFlg", true);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                } else {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Sorry...")
                            .setContentText("Didn't Add in Favourite Store! Try Again.")
                            .show();
                    //Utility.alertForErrorMessage("Didn't Add In Favourite Store! Please Try Again", context);// do not add address message.......
                }
               /* if (dotDialog.isShowing()) {
                    dotDialog.dismiss();

                }*/
            }
        });

    }

    //for un favority store
    private void makeUnFavorite(int loginID, final int position) {
        //final DbHelper dbHelper = new DbHelper(context);
        //final BallTriangleSyncDialog dotDialog = new BallTriangleSyncDialog(context);
        //dotDialog.show();
        ServiceCaller serviceCaller = new ServiceCaller(context);
        serviceCaller.makeUnFavoriteStoreService(loginID, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    Intent myIntent = new Intent("favorite");
                    myIntent.putExtra("favoriteFlg", true);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                } else {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Sorry...")
                            .setContentText("Didn't Unfavourite Store !Please Try Again")
                            .show();
                    //Utility.alertForErrorMessage("Didn't Unfavourite Store! Please Try Again", context);// do not add address message.......
                }
               /* if (dotDialog.isShowing()) {
                    dotDialog.dismiss();

                }*/
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
        return list.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView store_image;
        TextView store_name, heart_icon, store_address, store_email_address, storestore_contact_number, store_timing, status_icon, store_status;
        CardView card_view;
        //  ShineButton heart_icon;
        LinearLayout email_layout, phone_layout, timing_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            heart_icon = (TextView) itemView.findViewById(R.id.heart_icon);
            store_image = (ImageView) itemView.findViewById(R.id.store_image);
            store_name = (TextView) itemView.findViewById(R.id.store_name);
            store_address = (TextView) itemView.findViewById(R.id.store_address);
            store_email_address = (TextView) itemView.findViewById(R.id.store_email_address);
            storestore_contact_number = (TextView) itemView.findViewById(R.id.store_contact_number);
            //  email_icon = (TextView) itemView.findViewById(R.id.email_icon);
            //  contact_icon = (TextView) itemView.findViewById(R.id.contact_icon);
            //timing_icon = (TextView) itemView.findViewById(R.id.timing_icon);
//            status_icon = (TextView) itemView.findViewById(R.id.status_icon);
            store_timing = (TextView) itemView.findViewById(R.id.store_timing);
            store_status = (TextView) itemView.findViewById(R.id.store_status);
            email_layout = (LinearLayout) itemView.findViewById(R.id.email_layout);
            phone_layout = (LinearLayout) itemView.findViewById(R.id.phone_layout);
            timing_layout = (LinearLayout) itemView.findViewById(R.id.timing_layout);
            card_view = (CardView) itemView.findViewById(R.id.card_view);

            //  email_icon.setTypeface(materialDesignIcons);
            //  email_icon.setText(Html.fromHtml("&#xf1f0;"));
            heart_icon.setTypeface(materialDesignIcons);
            heart_icon.setText(Html.fromHtml("&#xf2d5;"));
            // contact_icon.setTypeface(materialDesignIcons);
            // contact_icon.setText(Html.fromHtml("&#xf3f2;"));
            //  timing_icon.setTypeface(materialDesignIcons);
            //  timing_icon.setText(Html.fromHtml("&#xf150;"));
            store_name.setTypeface(medium);
            store_address.setTypeface(regular);
            store_email_address.setTypeface(regular);
            storestore_contact_number.setTypeface(regular);
            store_timing.setTypeface(regular);
            store_status.setTypeface(regular);

        }
    }


}

