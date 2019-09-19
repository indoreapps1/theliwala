package loop.theliwala.models;

/**
 * Created by LALIT on 8/12/2017.
 */

public class ContentData {
    private Response response;

    private Data data;
    private AddNewAddressData addNewAddressData;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public AddNewAddressData getAddNewAddressData() {
        return addNewAddressData;
    }

    public void setAddNewAddressData(AddNewAddressData addNewAddressData) {
        this.addNewAddressData = addNewAddressData;
    }


    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    private Addresses[] addresses;

    public Addresses[] getAddresses() {
        return addresses;
    }

    public void setAddresses(Addresses[] addresses) {
        this.addresses = addresses;
    }

    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    private String Message;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    @Override
    public String toString() {
        return "ClassPojo [response = " + response + ", data = " + data + "]";
    }
}