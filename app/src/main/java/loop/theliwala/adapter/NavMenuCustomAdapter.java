package loop.theliwala.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;

import loop.theliwala.R;
import loop.theliwala.activity.DashboardActivity;
import loop.theliwala.activity.LoginActivity;
import loop.theliwala.database.DbHelper;
import loop.theliwala.fragment.FavouriteStoresFragment;
import loop.theliwala.fragment.HomeFragment;
import loop.theliwala.fragment.MyBasketFragment;
import loop.theliwala.fragment.OrdersFragment;
import loop.theliwala.fragment.UserAddressFragment;
import loop.theliwala.models.Data;
import loop.theliwala.utilities.FontManager;


/**
 * Created by u on 7/26/2017.
 */

public class NavMenuCustomAdapter extends BaseAdapter {
    private Context context;
    List<String> icondatalist;
    //    String nav_menu[];
    private LayoutInflater inflater;
    HashSet<Integer> selectedPosition = new HashSet<>();
    List<String> stringList;
    DbHelper dbHelper;

    public NavMenuCustomAdapter(Context context, String[] nav_menu, List<String> icondatalist) {
        this.context = context;
        this.icondatalist = icondatalist;
//        this.nav_menu = nav_menu;
        inflater = LayoutInflater.from(context);
    }

    public NavMenuCustomAdapter(DashboardActivity context, List<String> stringList, List<String> icondatalist) {
        this.context = context;
        this.icondatalist = icondatalist;
        this.stringList = stringList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return icondatalist.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            Typeface materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
            view = inflater.inflate(R.layout.item_list_nav_menu, viewGroup, false);
            Typeface regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
            holder.forTitle = (TextView) view.findViewById(R.id.nav_menutitle);
            holder.menuItemLayout = (LinearLayout) view.findViewById(R.id.menuItemLayout);
            holder.forIcon = (TextView) view.findViewById(R.id.nav_menuicon);
            holder.forIcon.setTypeface(materialDesignIcons);
            holder.forTitle.setTypeface(regular);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.forTitle.setTag(position);
        holder.forTitle.setText(stringList.get(position));
        holder.forIcon.setText(icondatalist.get(position));
//----------fill selected value------
//        if (selectedPosition.contains(position)) {
//            holder.menuItemLayout.setBackgroundColor(Color.parseColor("#BDBDBD"));
//            holder.forTitle.setTextColor(Color.parseColor("#388e3c"));
//            holder.forIcon.setTextColor(Color.parseColor("#388e3c"));
//        } else {
//            holder.menuItemLayout.setBackgroundColor(Color.WHITE);
//            holder.forIcon.setTextColor(Color.parseColor("#BDBDBD"));
//            holder.forTitle.setTextColor(Color.parseColor("#212121"));
//        }
        holder.forTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                int id = sharedPreferences.getInt("userId", 0);
                if (id != 0) {
                    if (position == 0) {
                        setUpHomeFragment();
                    }
//                    if (position == 1) {
//                        NotificationFragment fragment = NotificationFragment.newInstance("", "");
//                        moveFragment(fragment);
//                    }
                    if (position == 1) {
//                        LocationFragment fragment = LocationFragment.newInstance("", false);
//                        moveFragment(fragment);
                    }
//                    if (position == 2) {
//                        FavouriteStoresFragment fragment = FavouriteStoresFragment.newInstance("", "");
//                        moveFragment(fragment);
//                    }
                    if (position == 1) {
                        UserAddressFragment fragment = UserAddressFragment.newInstance(false, 0);
                        moveFragment(fragment);
                    }
//                    if (position == 4) {
//                        TrackOrderFragment fragment = TrackOrderFragment.newInstance("", "");
//                        moveFragment(fragment);
//                    }
                    if (position == 2) {
                        OrdersFragment fragment = OrdersFragment.newInstance("", "");
                        moveFragment(fragment);
                    }
                    if (position == 3) {
                        MyBasketFragment fragment = MyBasketFragment.newInstance("", "");
                        moveFragment(fragment);
                    }

//                    if (position == 7) {
//                        UserProfileFragment fragment = UserProfileFragment.newInstance("", "");
//                        moveFragment(fragment);
//                    }
                } else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }
                int pos = (int) view.getTag();
                if (selectedPosition.contains(pos)) {
                    //selectedPosition.remove(pos);
                    //selectedPosition.clear();
                } else {
                    selectedPosition.clear();
                    selectedPosition.add(pos);
                }
                notifyDataSetChanged();
                DashboardActivity rootActivity = (DashboardActivity) context;
                rootActivity.NavHide();

            }
        });
        return view;
    }

    //open home fragement
    private void setUpHomeFragment() {
        Fragment fragment;
        fragment = HomeFragment.newInstance(0, "");
        moveFragment(fragment);
    }

    //move to fragment
    public void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public class ViewHolder {
        public TextView forTitle;
        public TextView forIcon;
        public LinearLayout menuItemLayout;
    }
}


