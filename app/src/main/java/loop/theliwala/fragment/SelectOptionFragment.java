package loop.theliwala.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import loop.theliwala.R;
import loop.theliwala.activity.DashboardActivity;
import loop.theliwala.database.DbHelper;
import loop.theliwala.models.Data;
import loop.theliwala.myalert.SweetAlertDialog;
import loop.theliwala.utilities.FontManager;

public class SelectOptionFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int StoreID;
    private String mParam2;

    public static SelectOptionFragment newInstance(int StoreID, String param2) {
        SelectOptionFragment fragment = new SelectOptionFragment();
        Bundle args = new Bundle();
        args.putInt("StoreID", StoreID);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            StoreID = getArguments().getInt("StoreID");
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private View view;
    private Context context;
    private TextView purchech_order, offer, loyalties, store_name;
    private Typeface regular;
    ImageView storeImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_select_option, container, false);
        init();
        return view;
    }

    private void init() {
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setItemCart();
        rootActivity.setScreenLocation(true);
        rootActivity.setScreenFavourite(false);
        rootActivity.setScreenSave(false);
        rootActivity.setScreenTitle("Select Option");
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");

        purchech_order = (TextView) view.findViewById(R.id.purchech_order);
        loyalties = (TextView) view.findViewById(R.id.loyalies);
        offer = (TextView) view.findViewById(R.id.offers);
        store_name = (TextView) view.findViewById(R.id.store_name);
        storeImage = (ImageView) view.findViewById(R.id.store_image);
        purchech_order.setTypeface(regular);
        loyalties.setTypeface(regular);
        offer.setTypeface(regular);
        purchech_order.setOnClickListener(this);
        loyalties.setOnClickListener(this);
        offer.setOnClickListener(this);
        // store data...............
        DbHelper dbHelper = new DbHelper(context);
        Data data = dbHelper.getStoreData(StoreID);
        if (data != null) {
            String sName = data.getStoreName();
            String sProfile = data.getStorePicturesUrl();
            if (sName != null && !sName.equalsIgnoreCase("")) {
                store_name.setText(sName);
            }
            if (sProfile != null && !sProfile.equalsIgnoreCase("")) {
                //Picasso.with(context).load(sProfile).into(storeImage);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.purchech_order:
                ParentMenuFragment fragment = ParentMenuFragment.newInstance(StoreID, "");
                //list.get(position).getStoreId();
                moveFragment(fragment);
                break;

            case R.id.loyalies:
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Sorry...")
                        .setContentText("There is Nothing here.")
                        .show();
                break;
            case R.id.offers:
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Sorry...")
                        .setContentText("There is Nothing here")
                        .show();
                break;
        }
    }

    private void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
