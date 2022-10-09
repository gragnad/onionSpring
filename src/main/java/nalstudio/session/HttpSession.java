package nalstudio.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class HttpSession {

    private UUID uuid;
    private Map<String, Object> sessionData;

    public HttpSession() {
        this.uuid = UUID.randomUUID();
        this.sessionData = new HashMap<>();
    }

    public void setAttribute(String name, Object value) {
        sessionData.put(name, value);
    }

    public Object getAttribute(String name) {
        return sessionData.get(name);
    }

    public void removeAttributes(String name) {
        sessionData.remove(name);
    }

    //all delete
    public void invalidate() {
        Set<String> keys = sessionData.keySet();
        for(String key : keys) {
            sessionData.remove(key);
        }
    }

    public UUID getId() {
        return uuid;
    }

    public String getJSessionId() {
        return "JSESSIONID=" + uuid;
    }
}
