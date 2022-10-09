package nalstudio.session;

import java.util.Map;

import static nalstudio.util.HttpRequestUtils.parseCookies;

public class HttpCookie {
    private Map<String, String> cookies;

    public HttpCookie(String cookiesValue) {
        this.cookies = parseCookies(cookiesValue);
    }

    public String getCookie(String key) {
        return cookies.get(key);
    }
}
