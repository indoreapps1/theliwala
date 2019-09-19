package loop.theliwala.models;

/**
 * Created by Neeraj on 8/15/2017.
 */

public class Addresses {
    private int LoginID;
    private int LocalityId;
    private int AddressId;

    private int CityId;

    private String CompleteAddress;

    private int StateId;

    private String PhoneNumber;

    private String ZipCode;

    private String LandMark;


    public int getAddressId() {
        return AddressId;
    }

    public void setAddressId(int addressId) {
        AddressId = addressId;
    }

    public String getCompleteAddress() {
        return CompleteAddress;
    }

    public void setCompleteAddress(String CompleteAddress) {
        this.CompleteAddress = CompleteAddress;
    }


    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }

    public String getZipCode() {
        return ZipCode;
    }

    public void setZipCode(String ZipCode) {
        this.ZipCode = ZipCode;
    }

    public int getLoginID() {
        return LoginID;
    }

    public void setLoginID(int loginID) {
        LoginID = loginID;
    }

    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }

    public int getStateId() {
        return StateId;
    }

    public void setStateId(int stateId) {
        StateId = stateId;
    }

    public String getLandMark() {
        return LandMark;
    }

    public void setLandMark(String landMark) {
        LandMark = landMark;
    }

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

    private String LocalityName;
    @Override
    public String toString() {
        return "ClassPojo [LoginID = " + LoginID + ", AddressId = " + AddressId + ", LocalityId = " + LocalityId + ", CityId = " + CityId + ",  CompleteAddress = " + CompleteAddress + ", StateId = " + StateId + ", PhoneNumber = " + PhoneNumber + ", ZipCode = " + ZipCode + ", LandMark = " + LandMark + "]";
    }
}
