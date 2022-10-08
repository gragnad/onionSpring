package requestmethod;

import db.DataBase;
import dto.UserDto;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static util.HttpRequestUtils.parseQueryString;

public class PostMethod {
    private static final Logger log = LoggerFactory.getLogger(PostMethod.class);

    public static boolean execute(String url, String postData) {
        Map<String, String> dataMap = parseQueryString(postData);
        switch (url) {
            case "/user/create":
                UserDto userDto = new UserDto(dataMap.get("userId"),
                        dataMap.get("password"),
                        dataMap.get("name"),
                        dataMap.get("email"));
                if (userDto.isInAll()) {
                    DataBase.addUser(new User(userDto));
                    return true;
                }
                return false;
        }
        return false;
    }
}
