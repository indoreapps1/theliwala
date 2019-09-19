package loop.theliwala.models;

/**
 * Created by Neeraj on 4/10/2017.
 */

public class CacheServiceCallData {
    private String ServerRequestDateTime;
    private String url;

    public String getServerRequestDateTime() {
        return ServerRequestDateTime;
    }

    public void setServerRequestDateTime(String serverRequestDateTime) {
        ServerRequestDateTime = serverRequestDateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
