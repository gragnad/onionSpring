package nalstudio.controller;

import nalstudio.controller.basic.AbstractController;
import nalstudio.db.DataBase;
import nalstudio.model.dto.UserDto;
import nalstudio.httpmethod.HttpRequest;
import nalstudio.httpmethod.HttpResponse;
import nalstudio.model.dao.User;

public class CreateUserController extends AbstractController {
    @Override
    public void service(HttpRequest request, HttpResponse response) {

    }

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {

    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        UserDto userDto = new UserDto(
                request.getParameter("userId"),
                request.getParameter("userId"),
                request.getParameter("userId"),
                request.getParameter("userId")
        );
        if(userDto.isInAll()) {
            User user = new User(userDto);
            DataBase.addUser(user);
            response.sendRedirect("/index.html");
        } else {
            response.sendRedirect("/index.html");
        }
    }
}
