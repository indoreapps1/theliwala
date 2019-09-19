package loop.theliwala.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import loop.theliwala.R;
import loop.theliwala.database.DbHelper;
import loop.theliwala.fragment.YourOrderFragment;
import loop.theliwala.framework.IAsyncWorkCompletedCallback;
import loop.theliwala.framework.ServiceCaller;
import loop.theliwala.models.Addresses;
import loop.theliwala.models.Data;
import loop.theliwala.myalert.SweetAlertDialog;
import loop.theliwala.utilities.Contants;
import loop.theliwala.utilities.FontManager;
import loop.theliwala.utilities.Utility;

/**
 * Created by user on 8/14/2017.
 */
public class UserAddressListAdapter extends RecyclerView.Adapter<UserAddressListAdapter.ViewHolder> {
    private List<Data> addresslist;
    private Context context;
    Typeface roboto_light, regular, materialdesignicons_font, medium;
    private LayoutInflater layoutInflater;
    private Boolean navigateFlag;
    private int localityId;
    private int StoreId;


    public UserAddressListAdapter(Context context, List<Data> addresslist, Boolean navigateFlag) {
        this.context = context;
        this.addresslist = addresslist;
        this.navigateFlag = navigateFlag;
        layoutInflater = (LayoutInflater.from(context));
        roboto_light = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/roboto.light.ttf");
        this.medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        this.regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_getall_addresss, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.completeaddress.setText(addresslist.get(position).getAddress() + "," + addresslist.get(position).getLandmark());
        holder.phonenumber.setText(addresslist.get(position).getName() + "," + addresslist.get(position).getPhone());
        holder.locality_name.setText(addresslist.get(position).getCity() + "," + addresslist.get(position).getState());
        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("change_tab");
                intent.putExtra("flag", true);
                intent.putExtra("addressid", addresslist.get(position).getAddressId());
                context.sendBroadcast(intent);
                // AddNewAddressFragment fragment = AddNewAddressFragment.newInstance(addresslist.get(position).getAddressId(), true);//AddressId and true for edit address
                //moveFragment(fragment);
            }
        });
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover this file!")
                        .setCancelText("No")
                        .setConfirmText("Yes,delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                // reuse previous dialog instance
                                sDialog.setTitleText("Deleted!")
                                        .setContentText("Your imaginary file has been deleted!")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                deleteAddress(position);
                            }
                        }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                        .show();
                // alertForDeletetMessage(position);
            }
        });
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbHelper dbHelper = new DbHelper(context);
                Data storeData = dbHelper.getStoreData(StoreId);
                if (navigateFlag) {
                    YourOrderFragment fragment = YourOrderFragment.newInstance(addresslist.get(position).getAddressId(), StoreId);
                    moveFragment(fragment);

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

    private void deleteAddress(int position) {

        if (Utility.isOnline(context)) {
            if (addresslist.get(position).getAddressId() != 0) {
                callDeleteAddressService(addresslist.get(position).getAddressId(), position);
            } else {

//                Intent intent = new Intent("data_action");
//                intent.putExtra("flag", "true");
//                context.sendBroadcast(intent);

            }
        } else {
            Toast.makeText(context, Contants.OFFLINE_MESSAGE, Toast.LENGTH_LONG).show();
        }
        notifyDataSetChanged();

    }


    //delete address
    private void callDeleteAddressService(int addressId, final int position) {
        final ServiceCaller serviceCaller = new ServiceCaller(context);
        serviceCaller.callDeleteAddressDataService(addressId, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    DbHelper dbHelper = new DbHelper(context);
                    dbHelper.deleteAddressData(addresslist.get(position).getAddressId());
                    addresslist.remove(position);
                    Intent intent = new Intent("data");
                    intent.putExtra("flag", "true");
                    context.sendBroadcast(intent);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Address not Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return addresslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView completeaddress, Zipcode, phonenumber, tv_delete, tv_edit, locality_name;
        LinearLayout mainLayout;

        public ViewHolder(View view) {
            super(view);
            completeaddress = (TextView) view.findViewById(R.id.completeaddress);
            locality_name = (TextView) view.findViewById(R.id.locality_name);
            // Zipcode = (TextView) view.findViewById(R.id.Zipcode);
            phonenumber = (TextView) view.findViewById(R.id.phonenumber);
            tv_edit = (TextView) view.findViewById(R.id.tv_edit);
            tv_delete = (TextView) view.findViewById(R.id.tv_delete);
            mainLayout = (LinearLayout) view.findViewById(R.id.mainLayout);
            completeaddress.setTypeface(medium);
            //   Zipcode.setTypeface(medium);
            tv_edit.setTypeface(medium);
            tv_delete.setTypeface(medium);
            phonenumber.setTypeface(regular);
            locality_name.setTypeface(regular);


        }
    }
}
