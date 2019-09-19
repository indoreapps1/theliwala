package loop.theliwala.models;

/**
 * Created by LALIT on 8/10/2017.
 */

public class Response {
    private String serverResponseTime;

    private Boolean isSuccess;

    public String getServerResponseTime ()
    {
        return serverResponseTime;
    }

    public void setServerResponseTime (String serverResponseTime)
    {
        this.serverResponseTime = serverResponseTime;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [serverResponseTime = "+serverResponseTime+", isSuccess = "+isSuccess+"]";
    }
}
