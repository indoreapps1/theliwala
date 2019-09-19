package loop.theliwala.models;

/**
 * Created by Neeraj on 9/15/2017.
 */

public class OrderDetails {
    private Float Quantity;

    private int OrderNumber;

    private int OrderDetailsId;

    private int ProductId;

    public Float getQuantity() {
        return Quantity;
    }

    public void setQuantity(Float quantity) {
        Quantity = quantity;
    }

    public int getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        OrderNumber = orderNumber;
    }

    public int getOrderDetailsId() {
        return OrderDetailsId;
    }

    public void setOrderDetailsId(int orderDetailsId) {
        OrderDetailsId = orderDetailsId;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    private String ProductName;
    private Double UnitPrice;

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public Double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        UnitPrice = unitPrice;
    }
}
