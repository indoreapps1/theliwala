package loop.theliwala.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import loop.theliwala.R;
import loop.theliwala.activity.DashboardActivity;
import loop.theliwala.adapter.ParentMenuAdapter;
import loop.theliwala.ballsynccustomprogress.BallTriangleSyncDialog;
import loop.theliwala.database.DbHelper;
import loop.theliwala.framework.IAsyncWorkCompletedCallback;
import loop.theliwala.framework.ServiceCaller;
import loop.theliwala.models.Data;
import loop.theliwala.utilities.FontManager;
import loop.theliwala.utilities.Utility;


public class ParentMenuFragment extends Fragment {

    private int storeId;
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ParentMenuFragment() {
        // Required empty public constructor
    }


    public static ParentMenuFragment newInstance(int storeId, String param2) {
        ParentMenuFragment fragment = new ParentMenuFragment();
        Bundle args = new Bundle();
        args.putInt("StoreId", storeId);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            storeId = getArguments().getInt("StoreId");
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    RecyclerView recycler_view;
    View view;
    Context context;
    TextView tv_title;
    LinearLayout linearLayout;
    ParentMenuAdapter parentMenuAdapter;
    List<Data> menulist;
    Typeface materialDesignIcons, regular, bold;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_parent_menu, container, false);
        materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        init();
        getAllMenuList();
        return view;
    }

    private void init() {
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreenTitle("Select Menu");
        //tv_title = (TextView) view.findViewById(R.id.tv_title);
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(new GridLayoutManager(context, 2));
        recycler_view.setHasFixedSize(false);
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        bold = FontManager.getFontTypeface(context, "fonts/roboto.bold.ttf");
        // tv_title.setText("Select Menu");
        //tv_title.setTypeface(bold);

    }

    //    //get allmenu list data
    private void getAllMenuList() {
        if (Utility.isOnline(context)) {
            final BallTriangleSyncDialog dotDialog = new BallTriangleSyncDialog(context);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(context);
            serviceCaller.callGetAllMenuListService(storeId, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    if (isComplete) {
                        DbHelper dbHelper = new DbHelper(context);
                        menulist = dbHelper.getMenuListData();
                        if (menulist != null) {
                            parentMenuAdapter = new ParentMenuAdapter(context, menulist, storeId);
                            recycler_view.setAdapter(parentMenuAdapter);
                        } else {
                            noDataFound();
                        }
                    } else {
                        noDataFound();
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }

                }
            });
        } else {
            // Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, context);//off line msg....
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.no_data_found, null);
            TextView nodata = (TextView) view.findViewById(R.id.nodata);
            TextView nodataIcon = (TextView) view.findViewById(R.id.nodataIcon);
            nodataIcon.setTypeface(materialDesignIcons);
            nodataIcon.setText(Html.fromHtml("&#xf5aa;"));
            nodata.setText("No internet Connection found");
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.removeAllViews();
            linearLayout.addView(view);
        }
    }

    private void noDataFound() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.no_data_found, null);
        TextView nodataIcon = (TextView) view.findViewById(R.id.nodataIcon);
        TextView nodata = (TextView) view.findViewById(R.id.nodata);
        nodataIcon.setTypeface(materialDesignIcons);
        nodataIcon.setText(Html.fromHtml("&#xf187;"));
        nodata.setText("Any menu not found.");
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.removeAllViews();
        linearLayout.addView(view);
    }


}
