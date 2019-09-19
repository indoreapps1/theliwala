package loop.theliwala.models;

/**
 * Created by user on 8/11/2017.
 */

public class AddNewAddressData {
    private String LoginID;

    private String AddressId;

    private String CityId;

    private String CompleteAddress;

    private String StateId;

    private String PhoneNumber;

    private String ZipCode;

    private String LandMark;

    public String getLoginID ()
    {
        return LoginID;
    }

    public void setLoginID (String LoginID)
    {
        this.LoginID = LoginID;
    }

    public String getAddressId ()
    {
        return AddressId;
    }

    public void setAddressId (String AddressId)
    {
        this.AddressId = AddressId;
    }

    public String getCityId ()
    {
        return CityId;
    }

    public void setCityId (String CityId)
    {
        this.CityId = CityId;
    }

    public String getCompleteAddress ()
    {
        return CompleteAddress;
    }

    public void setCompleteAddress (String CompleteAddress)
    {
        this.CompleteAddress = CompleteAddress;
    }

    public String getStateId ()
    {
        return StateId;
    }

    public void setStateId (String StateId)
    {
        this.StateId = StateId;
    }

    public String getPhoneNumber ()
    {
        return PhoneNumber;
    }

    public void setPhoneNumber (String PhoneNumber)
    {
        this.PhoneNumber = PhoneNumber;
    }

    public String getZipCode ()
    {
        return ZipCode;
    }

    public void setZipCode (String ZipCode)
    {
        this.ZipCode = ZipCode;
    }

    public String getLandMark ()
    {
        return LandMark;
    }

    public void setLandMark (String LandMark)
    {
        this.LandMark = LandMark;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [LoginID = "+LoginID+", AddressId = "+AddressId+", CityId = "+CityId+", CompleteAddress = "+CompleteAddress+", StateId = "+StateId+", PhoneNumber = "+PhoneNumber+", ZipCode = "+ZipCode+", LandMark = "+LandMark+"]";
    }
}
