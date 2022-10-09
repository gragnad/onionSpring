package legacy;

import nalstudio.db.DataBase;
import nalstudio.model.dto.UserDto;
import nalstudio.model.dao.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static nalstudio.util.HttpRequestUtils.parseQueryString;

public class PostMethod extends BasicHttpMethod{
    private static final Logger log = LoggerFactory.getLogger(PostMethod.class);

    public PostMethod() {
        isSuccess = false;
        makeCookie = "";
    }

    public Map<String, Object> execute(String url, String postData) {
        Map<String, String> dataMap = parseQueryString(postData);
        Map<String, Object> resultMap = new HashMap<>();
        switch (url) {
            case "/user/create":
                UserDto userDto = new UserDto(dataMap.get("userId"),
                        dataMap.get("password"),
                        dataMap.get("name"),
                        dataMap.get("email"));
                if (userDto.isInAll()) {
                    DataBase.addUser(new User(userDto));
                    isSuccess = true;
                } else {
                    isSuccess = false;
                }
                this.resultUrl = "/index.html";
                break;
            case "/user/login":
                User user = DataBase.findUserById(dataMap.get("userId"));
                if(user != null) {
                    resultMap.put(responseData, user);
                    this.resultUrl = "/index.html";
                    this.makeCookie = "logined=true";
                    isSuccess = true;
                } else {
                    isSuccess = false;
                    this.resultUrl = "/user/login_failed.html";
                    this.makeCookie = "logined=false";
                }
                break;

        }

        return resultMap;
    }


}
