package loop.theliwala.models;

/**
 * Created by user on 8/9/2017.
 */

public class CityListResponse {
    private String serverResponseTime;

    private String isSuccess;

    public String getServerResponseTime ()
    {
        return serverResponseTime;
    }

    public void setServerResponseTime (String serverResponseTime)
    {
        this.serverResponseTime = serverResponseTime;
    }

    public String getIsSuccess ()
    {
        return isSuccess;
    }

    public void setIsSuccess (String isSuccess)
    {
        this.isSuccess = isSuccess;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [serverResponseTime = "+serverResponseTime+", isSuccess = "+isSuccess+"]";
    }

}
