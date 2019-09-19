package loop.theliwala.models;


public class Data {
    private int loginId;

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    private int otp;

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private int status;

    private int Role;
    private int addressId;

    private String phone;
    private String name;
    private String landmark;
    private String state;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private String city;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;

    private String EmailID;


    public int getLoginID() {
        return loginId;
    }

    public void setLoginID(int LoginID) {
        this.loginId = LoginID;
    }


    public int getRole() {
        return Role;
    }

    public void setRole(int Role) {
        this.Role = Role;
    }


    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String EmailID) {
        this.EmailID = EmailID;
    }

    private int CityId;

    private String CityName;

    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }

    private int LocalityId;
    private String LocalityName;


    public int getLocalityId() {
        return LocalityId;
    }

    public void setLocalityId(int localityId) {
        LocalityId = localityId;
    }

    public String getLocalityName() {
        return LocalityName;
    }

    public void setLocalityName(String localityName) {
        LocalityName = localityName;
    }

    private int StoreId;
    private String StoreName;
    private String StorePhoneNumber;
    private String StoreEmailId;
    private String StoreAddress;
    private String StoreStatus;

    private String OpeningTime;

    private String ClosingTime;
    private double DeliveryCharge;

    public double getDeliveryCharge() {
        return DeliveryCharge;
    }

    public void setDeliveryCharge(double deliveryCharge) {
        DeliveryCharge = deliveryCharge;
    }


    public int getStoreId() {
        return StoreId;
    }

    public void setStoreId(int storeId) {
        StoreId = storeId;
    }

//    public String getStoreName() {
//        return StoreName;
//    }
//
//    public void setStoreName(String storeName) {
//        StoreName = storeName;
//    }


    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String StoreName) {
        this.StoreName = StoreName;
    }


    public String getStorePhoneNumber() {
        return StorePhoneNumber;
    }

    public void setStorePhoneNumber(String storePhoneNumber) {
        StorePhoneNumber = storePhoneNumber;
    }

    public String getStoreEmailId() {
        return StoreEmailId;
    }

    public void setStoreEmailId(String storeEmailId) {
        StoreEmailId = storeEmailId;
    }

    public String getStoreAddress() {
        return StoreAddress;
    }

    public void setStoreAddress(String storeAddress) {
        StoreAddress = storeAddress;
    }

    public String getStoreStatus() {
        return StoreStatus;
    }

    public void setStoreStatus(String storeStatus) {
        StoreStatus = storeStatus;
    }

    public String getOpeningTime() {
        return OpeningTime;
    }

    public void setOpeningTime(String openingTime) {
        OpeningTime = openingTime;
    }

    public String getClosingTime() {
        return ClosingTime;
    }

    public void setClosingTime(String closingTime) {
        ClosingTime = closingTime;
    }

    private int categoryId;
    private int productId;
    private String discountedPrice;
    private String price;
    private String productName;
    private String productImage;
    private String categoryName;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    private float countValue = 1;

    public float getCountValue() {
        return countValue;
    }

    public void setCountValue(float countValue) {
        this.countValue = countValue;
    }


    private String TransactionId;

    private String PaymentMode;

    private String PaymentOrderId;

    private String CreatedOn;

    private float TotalAmount;

    private String Id;

    private String PaymentId;

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String TransactionId) {
        this.TransactionId = TransactionId;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String PaymentMode) {
        this.PaymentMode = PaymentMode;
    }

    public String getPaymentOrderId() {
        return PaymentOrderId;
    }

    public void setPaymentOrderId(String PaymentOrderId) {
        this.PaymentOrderId = PaymentOrderId;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String CreatedOn) {
        this.CreatedOn = CreatedOn;
    }

    public float getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(float TotalAmount) {
        this.TotalAmount = TotalAmount;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getPaymentId() {
        return PaymentId;
    }

    public void setPaymentId(String PaymentId) {
        this.PaymentId = PaymentId;
    }

    private Boolean isSelectGm;
    private Boolean isSelectKg;
    private Boolean isSelectManualy;

    public Boolean getSelectGm() {
        return isSelectGm;
    }

    public void setSelectGm(Boolean selectGm) {
        isSelectGm = selectGm;
    }

    public Boolean getSelectKg() {
        return isSelectKg;
    }

    public void setSelectKg(Boolean selectKg) {
        isSelectKg = selectKg;
    }

    public Boolean getSelectManualy() {
        return isSelectManualy;
    }

    public void setSelectManualy(Boolean selectManualy) {
        isSelectManualy = selectManualy;
    }

    private int PromoCodeId;
    private String PromoCode;

    public int getPromoCodeId() {
        return PromoCodeId;
    }

    public void setPromoCodeId(int promoCodeId) {
        PromoCodeId = promoCodeId;
    }

    public String getPromoCode() {
        return PromoCode;
    }

    public void setPromoCode(String promoCode) {
        PromoCode = promoCode;
    }

    //    get favourite store by user .........................
    private String StorePicturesUrl;

    public String getStorePicturesUrl() {
        return StorePicturesUrl;
    }

    public void setStorePicturesUrl(String StorePicturesUrl) {
        this.StorePicturesUrl = StorePicturesUrl;
    }
// get my order history data..............................

    private String OrderTime;

    private String OrderStatus;

    private float Quantiy;

    private String OrderNumber;

    private float TotalPrice;

    private float GrandTotal;

    private float SpecialDiscount;

    private float TotalGST;

    public float getTotalGST() {
        return TotalGST;
    }

    public void setTotalGST(float totalGST) {
        TotalGST = totalGST;
    }

    private float SubTotal;

    public float getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(float subTotal) {
        SubTotal = subTotal;
    }


    public float getSpecialDiscount() {
        return SpecialDiscount;
    }

    public void setSpecialDiscount(float specialDiscount) {
        SpecialDiscount = specialDiscount;
    }

    private int OrderId;

    public float getGrandTotal() {
        return GrandTotal;
    }

    public void setGrandTotal(float grandTotal) {
        GrandTotal = grandTotal;
    }


    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String OrderTime) {
        this.OrderTime = OrderTime;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String OrderStatus) {
        this.OrderStatus = OrderStatus;
    }

    private float Quantity;

    public float getQuantity() {
        return Quantity;
    }

    public void setQuantity(float Quantity) {
        this.Quantity = Quantity;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String OrderNumber) {
        this.OrderNumber = OrderNumber;
    }

    public float getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(float TotalPrice) {
        this.TotalPrice = TotalPrice;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int OrderId) {
        this.OrderId = OrderId;
    }

    private String CategoryPictures;
    private String CategoryDescription;
    private String ProductPicturesUrl;

    public String getCategoryPictures() {
        return CategoryPictures;
    }

    public void setCategoryPictures(String categoryPictures) {
        CategoryPictures = categoryPictures;
    }

    public String getCategoryDescription() {
        return CategoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        CategoryDescription = categoryDescription;
    }

    public String getProductPicturesUrl() {
        return ProductPicturesUrl;
    }

    public void setProductPicturesUrl(String productPicturesUrl) {
        ProductPicturesUrl = productPicturesUrl;
    }

    private String ProfilePictureUrl;

    public String getProfilePictureUrl() {
        return ProfilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        ProfilePictureUrl = profilePictureUrl;
    }

    private String PaymentURL;

    public String getPaymentURL() {
        return PaymentURL;
    }

    public void setPaymentURL(String PaymentURL) {
        this.PaymentURL = PaymentURL;
    }

    private boolean FavouriteStore;

    public boolean isFavouriteStore() {
        return FavouriteStore;
    }

    public void setFavouriteStore(boolean favouriteStore) {
        FavouriteStore = favouriteStore;
    }

    private OrderDetails[] OrderDetails;

    public OrderDetails[] getOrderDetails() {
        return OrderDetails;
    }

    public void setOrderDetails(OrderDetails[] orderDetails) {
        OrderDetails = orderDetails;
    }

    private float shippingCharge;

    public float getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(float shippingCharge) {
        this.shippingCharge = shippingCharge;
    }

    private float NetPrice;


    public float getNetPrice() {
        return NetPrice;
    }

    public void setNetPrice(float netPrice) {
        NetPrice = netPrice;
    }


    private String MenuName;

    private String ImageUrl;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public float getQuantiy() {
        return Quantiy;
    }

    public void setQuantiy(float quantiy) {
        Quantiy = quantiy;
    }

    private int MenuId;

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    private int discount;

    public String getMenuName() {
        return MenuName;
    }

    public void setMenuName(String MenuName) {
        this.MenuName = MenuName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public int getMenuId() {
        return MenuId;
    }

    public void setMenuId(int MenuId) {
        this.MenuId = MenuId;
    }


    @Override
    public String toString() {
        return "ClassPojo [Name = " + name + ", loginId = " + loginId + ", Otp = " + otp + ", Role = " + Role + ", PhoneNumber = " + phone + ", EmailID = " + EmailID + ",LocalityId = " + LocalityId + ", StorePhoneNumber = " + StorePhoneNumber + ", StoreId = " + StoreId + ", StoreName = " + StoreName + ", StorePicturesUrl = " + StorePicturesUrl + ",StoreAddress = " + StoreAddress + ", StoreEmailId = " + StoreEmailId + ",OrderTime = " + OrderTime + ", OrderStatus = " + OrderStatus + ", Quantity = " + Quantity + ", StoreId = " + StoreId + ",OrderNumber = " + OrderNumber + ", TotalPrice = " + TotalPrice + " ,OrderId = " + OrderId + "]";
    }
}