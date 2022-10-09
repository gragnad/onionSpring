package nalstudio.session;

import java.util.HashMap;
import java.util.Map;

public class HttpSessions {
    private static Map<String, HttpSession> sessions = new HashMap<String, HttpSession>();

    public static HttpSession getSession(String uuid) {
        HttpSession session = sessions.get(uuid);

        if(session == null) {
            session = new HttpSession();
            sessions.put(uuid, session);
            return session;
        }
        return session;
    }

    public static void remove(String uuid) {
        sessions.remove(uuid);
    }

}
