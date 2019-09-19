package loop.theliwala.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import loop.theliwala.R;
import loop.theliwala.utilities.FontManager;


public class ConfirmPaymentActivity extends AppCompatActivity {
    TextView payment_status, tv_paymentId, tv_total, tv_paidFor, done;
    String OrderNo, TransactionId;
    int OrderId;
    float NetPayable;
    LinearLayout layout_trackOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);
        inti();
    }

    private void inti() {
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        payment_status = (TextView) findViewById(R.id.payment_status);
        done = (TextView) findViewById(R.id.done);
        tv_paymentId = (TextView) findViewById(R.id.tv_TransactionId);
        tv_total = (TextView) findViewById(R.id.tv_Total);
        tv_paidFor = (TextView) findViewById(R.id.tv_paidFor);
        layout_trackOrder = (LinearLayout) findViewById(R.id.layout_trakOrder);
        done.setTypeface(materialdesignicons_font);
        done.setText(Html.fromHtml("&#xf5e0;"));
        TransactionId = getIntent().getStringExtra("TransactionId");
        OrderNo = getIntent().getStringExtra("OrderNo");
        OrderId = getIntent().getIntExtra("orderId", 0);
        NetPayable = getIntent().getFloatExtra("NetPayable", 0);
        //set values.....
        payment_status.setText("Your Payment Successfully");
        tv_paymentId.setText(TransactionId);
        tv_paidFor.setText(String.valueOf(OrderNo));
        tv_total.setText(String.valueOf(NetPayable));
        layout_trackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent confirmIntent = new Intent(ConfirmPaymentActivity.this, DashboardActivity.class);
                confirmIntent.putExtra("NavigateFlag", 1);
                confirmIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(confirmIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent confirmIntent = new Intent(ConfirmPaymentActivity.this, DashboardActivity.class);
        confirmIntent.putExtra("NavigateFlag", 2);
        confirmIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(confirmIntent);
    }
}
