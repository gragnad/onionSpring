package legacy;

import java.util.Map;

public class BasicHttpMethod {
    protected static final String responseData = "reposeData";

    protected boolean isSuccess = false;
    protected String makeCookie;
    protected String resultUrl = "";
    public boolean isSuccess() {
        return isSuccess;
    }

    public String getResultUrl() {
        return resultUrl;
    }

    public String getMakeCookie() {
        return makeCookie;
    }
}
