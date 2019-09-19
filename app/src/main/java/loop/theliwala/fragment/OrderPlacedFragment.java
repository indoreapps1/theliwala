package loop.theliwala.fragment;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import loop.theliwala.R;
import loop.theliwala.activity.DashboardActivity;
import loop.theliwala.utilities.CompatibilityUtility;
import loop.theliwala.utilities.FontManager;

/**
 * Created by LALIT on 8/14/2017.
 */

public class OrderPlacedFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename and change types and number of parameters
    public static OrderPlacedFragment newInstance(String OrderNumber, String param2) {
        OrderPlacedFragment fragment = new OrderPlacedFragment();
        Bundle args = new Bundle();
        args.putString("OrderNumber", OrderNumber);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }
    private String OrderNumber;
    private String mParam2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            OrderNumber = getArguments().getString("OrderNumber");
            mParam2 = getArguments().getString("param2");
        }
    }

    private Typeface medium, regular, italic;
    TextView textView_orderNumber, textView_TrackOrder, textView_orderId, textView_sucessfully, textView_Thankyou;
    View view;
    private Context context;
    LinearLayout layout_trackOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        view = inflater.inflate(R.layout.fragment_order_placed, container, false);
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setScreenSave(false);
        rootActivity.setScreenFavourite(false);
        rootActivity.setScreenLocation(false);
        rootActivity.setItemCart();
        rootActivity.setScreenTitle("Order Placed");
        initViews();//initilization...........
        setTypeFace();//set text style.......
        return view;
    }

    //set text style.......
    private void setTypeFace() {
        textView_orderNumber.setTypeface(regular);
        textView_TrackOrder.setTypeface(medium);
        textView_orderId.setTypeface(regular);
        textView_sucessfully.setTypeface(italic);
        textView_Thankyou.setTypeface(italic);
    }

    //initilization...........
    private void initViews() {
        medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        italic = FontManager.getFontTypeface(context, "fonts/roboto.italic.ttf");
        textView_orderNumber = (TextView) view.findViewById(R.id.tv_orderNumber);
        textView_TrackOrder = (TextView) view.findViewById(R.id.tv_track);
        textView_orderId = (TextView) view.findViewById(R.id.tv_order);
        textView_sucessfully = (TextView) view.findViewById(R.id.tv_success);
        textView_Thankyou = (TextView) view.findViewById(R.id.tv_thanku);
        layout_trackOrder = (LinearLayout) view.findViewById(R.id.layout_trakOrder);
        layout_trackOrder.setOnClickListener(this);
        if (OrderNumber != null) {
            textView_orderNumber.setText(OrderNumber);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_trakOrder:
                OrdersFragment fragment = OrdersFragment.newInstance("", "");
                moveFragment(fragment);
//                Intent intent = new Intent(context, DashboardActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("NavigateFlag",1);
//                startActivity(intent);
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
