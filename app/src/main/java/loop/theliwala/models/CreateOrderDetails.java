package loop.theliwala.models;

/**
 * Created by Neeraj on 8/24/2017.
 */

public class CreateOrderDetails {
    private int ProductId;
    private float Quantity;
    //private double TotalPrice;

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public float getQuantity() {
        return Quantity;
    }

    public void setQuantity(float quantity) {
        Quantity = quantity;
    }

//    public double getTotalPrice() {
//        return TotalPrice;
//    }
//
//    public void setTotalPrice(double totalPrice) {
//        TotalPrice = totalPrice;
//    }
}
