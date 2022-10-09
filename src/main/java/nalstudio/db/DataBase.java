package nalstudio.db;

import com.google.common.collect.Maps;
import nalstudio.model.dao.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

public class DataBase {

    private final static Logger log = LoggerFactory.getLogger(DataBase.class);
    private static Map<String, User> users = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
        log.info("add User : " + user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
