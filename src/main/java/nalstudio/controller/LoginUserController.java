package nalstudio.controller;

import nalstudio.controller.basic.AbstractController;
import nalstudio.db.DataBase;
import nalstudio.httpmethod.HttpRequest;
import nalstudio.httpmethod.HttpResponse;
import nalstudio.model.dao.User;
import nalstudio.session.HttpSession;

public class LoginUserController extends AbstractController {


    @Override
    public void service(HttpRequest request, HttpResponse response) {

    }

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {

    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        String userId = request.getParameter("userId");
        User user = DataBase.findUserById(userId);
        try {
            if (user == null) {
                response.sendRedirect("/user/login_failed.html");
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                response.forward("/user/list.html");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
