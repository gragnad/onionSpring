package webserver;

import httpmethod.HttpRequest;
import httpmethod.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;


public class RequestHandler extends Thread{
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
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
            switch (path) {
                case "/user/create":
                    String password = request.getParameter("password");
                    //log.info(password);
                    response.sendRedirect("/index.html");
                    break;
                case "/user/login":
                    String userid = request.getParameter("userId");
                    //log.info(userid);
                    response.addHeader("Set-Cookie", "logined=true");
                    response.sendRedirect("/index.html");
                    break;
                case "/":
                case "/index.html":
                    response.forward("/index.html");
                    break;
                default:
                    response.forward(path);
                    break;
            }

        } catch (IOException | NullPointerException e) {
            log.error(e.getMessage());
        }
    }


}
