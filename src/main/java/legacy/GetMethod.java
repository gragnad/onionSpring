package legacy;

import nalstudio.db.DataBase;
import nalstudio.model.dto.UserDto;
import nalstudio.model.dao.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static nalstudio.util.HttpRequestUtils.parseQueryString;

public class GetMethod extends BasicHttpMethod{

    private static final Logger log = LoggerFactory.getLogger(GetMethod.class);

    public GetMethod() {
        isSuccess = false;
    }

    public String execute(String url) {
        String urlPath = "";
        Map<String, String> urlQuery = new HashMap<>();
        if(url.contains("?")) {
            int index = url.indexOf("?");
            urlPath = url.substring(0, index);
            String params = url.substring(index + 1);
            urlQuery = parseQueryString(params);
            if(urlPath.equals("/user/create")){
                UserDto userDto = new UserDto(urlQuery.get("userId"),
                        urlQuery.get("password"),
                        urlQuery.get("name"),
                        urlQuery.get("email"));
                if(userDto.isInAll()) {
                    DataBase.addUser(new User(userDto));
                }
            }
        } else {
            urlPath = url;
        }
        return urlPath;
    }
}
