package loop.theliwala.models;

/**
 * Created by user on 8/10/2017.
 */

public class SelectLocality {
    private CityListResponse response;

    private LocalityData[] data;

    public CityListResponse getResponse ()
    {
        return response;
    }

    public void setResponse (CityListResponse response)
    {
        this.response = response;
    }

    public LocalityData[] getData ()
    {
        return data;
    }

    public void setData (LocalityData[] data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", data = "+data+"]";
    }
}
