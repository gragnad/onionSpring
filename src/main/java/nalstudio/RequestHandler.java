package nalstudio;

import nalstudio.controller.CreateUserController;
import nalstudio.controller.ListUserController;
import nalstudio.controller.LoginUserController;
import nalstudio.controller.basic.AbstractController;
import nalstudio.httpmethod.HttpRequest;
import nalstudio.httpmethod.HttpResponse;
import nalstudio.session.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class RequestHandler extends Thread{
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;

    Map<String, AbstractController> controllerMap;
    HttpSession httpSession;
    private CreateUserController createUserController;
    private LoginUserController loginUserController;
    private ListUserController listSuerController;
    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.httpSession = new HttpSession();
        this.controllerMap = new HashMap<>();

        this.createUserController = new CreateUserController();
        this.loginUserController = new LoginUserController();
        this.listSuerController = new ListUserController();

        controllerMap.put("/user/create", createUserController);
        controllerMap.put("/user/login", loginUserController);
        controllerMap.put("/user/list.html", listSuerController);
    }

    @Override
    public void run() {
        //log.debug("New Client Connect! Connect IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        //Stream : external data (read, send)
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO this function is Process for client request
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);
            String path = request.getPath();

            AbstractController controller = controllerMap.get(path);
            if(controller != null) {
                switch (request.getMethod()) {
                    case HttpRequest.HttpMethod.GET:
                        controller.doGet(request, response);
                        break;
                    case HttpRequest.HttpMethod.POST:
                        controller.doPost(request, response);
                        break;
                }
            } else {
                switch (path) {
                    case "/":
                    case "/index.html":
                        response.forward("/index.html");
                        break;
                    default:
                        response.forward(path);
                        break;
                }
            }
        } catch (IOException | NullPointerException e) {
            log.error(e.getMessage());
        }
    }


}
