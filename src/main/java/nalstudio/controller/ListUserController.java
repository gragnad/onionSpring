package nalstudio.controller;

import nalstudio.controller.basic.AbstractController;
import nalstudio.db.DataBase;
import nalstudio.httpmethod.HttpRequest;
import nalstudio.httpmethod.HttpResponse;
import nalstudio.model.dao.User;

import java.util.Collection;

public class ListUserController extends AbstractController {
    @Override
    public void doGet(HttpRequest request, HttpResponse response)  {

        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect("/user/login.html");
        } else {
            Collection<User> users = DataBase.findAll();
            StringBuilder sb = new StringBuilder();
            sb.append("<table border='1'>");
            for (User user : users) {
                sb.append("<tr>");
                sb.append("<td>" + user.getUserId() + "</td>");
                sb.append("<td>" + user.getName() + "</td>");
                sb.append("<td>" + user.getEmail() + "</td>");
                sb.append("</tr>");
            }
            sb.append("</table>");
            response.forwardBody(request.getPath(), sb.toString());
        }
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {

    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {

    }
}
