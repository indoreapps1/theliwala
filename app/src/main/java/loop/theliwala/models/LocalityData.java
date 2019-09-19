package loop.theliwala.models;

/**
 * Created by user on 8/10/2017.
 */

public class LocalityData {

    private int LocalityId;

    private int CityId;

    private String LocalityName;

    public int getLocalityId ()
    {
        return LocalityId;
    }

    public void setLocalityId (int LocalityId)
    {
        this.LocalityId = LocalityId;
    }

    public int getCityId ()
    {
        return CityId;
    }

    public void setCityId (int CityId)
    {
        this.CityId = CityId;
    }

    public String getLocalityName ()
    {
        return LocalityName;
    }

    public void setLocalityName (String LocalityName)
    {
        this.LocalityName = LocalityName;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [LocalityId = "+LocalityId+", CityId = "+CityId+", LocalityName = "+LocalityName+"]";
    }
    }

